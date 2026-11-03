/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbsave;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.xtream.inet.trading.db.record.object.StockDataRecord;
import com.itm.xtream.inet.trading.dbaccess.DBAccessManagement;
import com.itm.xtream.inet.trading.dbformat.SwDbRiskMgtFormat;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Penulisan data stok ke database.
 *
 * 20261029: penulisan ke database dilakukan oleh thread tersendiri ("DbRiskMgtWriteStockData-Writer") sehingga
 * thread pemroses feed (ITCH / ITCH MDF) TIDAK menunggu koneksi / eksekusi query ke database.
 * - insertOrUpdateStockData() / updateStockDataLastPrice() hanya menyusun query (snapshot isi record saat dipanggil)
 *   lalu memasukkannya ke antrean, kemudian langsung kembali.
 * - Urutan query dijaga (satu thread penulis, FIFO).
 * - Jika koneksi database tidak tersedia / terputus, query tidak dibuang: penulis mencoba lagi secara berkala
 *   sambil mempertahankan urutan. Query yang gagal karena kesalahan SQL (bukan koneksi) dicatat lalu dilewati.
 *
 * @author fredy
 */
public class DbRiskMgtWriteStockData{
    //.single instance:
    public final static DbRiskMgtWriteStockData getInstance = new DbRiskMgtWriteStockData();
    private  HashMap<String, StockDataRecord> mapStock = new HashMap<>();

    private final static int MAX_QUEUE_SIZE                 = 1000000;      //.batas pengaman memori (query stok jumlahnya kecil).
    private final static int MAX_BATCH_SIZE                 = 500;          //.jumlah query yang ditulis per satu koneksi / statement.
    private final static long RETRY_DELAY_MS                = 3000;         //.jeda mencoba lagi jika koneksi database tidak tersedia.
    private final static long SHUTDOWN_WAIT_MS              = 5000;         //.lama maksimum menunggu antrean habis saat aplikasi ditutup.

    private final LinkedBlockingQueue<String> lbqQueries    = new LinkedBlockingQueue<String>(MAX_QUEUE_SIZE);
    private final AtomicInteger aiPending                   = new AtomicInteger(0);   //.antrean + yang sedang ditulis.
    private long tLastFullLog                               = 0;
    private long tLastNoConnLog                             = 0;

    public DbRiskMgtWriteStockData() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
        startWriter();
    }

    //. 2022-12-06
    public boolean insertOrUpdateStockData_temp(StockDataRecord record){
        return true;
    }

    /**
     * Simpan / update data stok ke database (asinkron).
     * @return true jika query diterima antrean (BUKAN hasil eksekusi di database).
     */
    public boolean insertOrUpdateStockData(StockDataRecord record){
        boolean bOut = false;
        try{
            String zQuery = SwDbRiskMgtFormat.insertOrUpdateStockData(record);
            bOut = enqueueQuery(zQuery);

            //. 2022-12-14 : memory
            mapStock.put(record.getfSecurityCode(), record);
        }catch (Exception ex){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex);
        }
        return bOut;
    }

    /**
     * Update harga terakhir stok di database (asinkron).
     * @return true jika query diterima antrean (BUKAN hasil eksekusi di database).
     */
    public boolean updateStockDataLastPrice(StockDataRecord record){
        boolean bOut = false;
        try{
            String zQuery = SwDbRiskMgtFormat.updateStockDataLastPrice(record);
            bOut = enqueueQuery(zQuery);
        }catch (Exception ex){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex);
        }
        return bOut;
    }

    /**
     * Jumlah query yang masih menunggu / sedang ditulis ke database.
     */
    public int getPendingCount(){
        return this.aiPending.get();
    }

    private boolean enqueueQuery(String zQuery){
        if (StringHelper.isNullOrEmpty(zQuery)){
            return false;
        }
        //.hitung dulu agar penulis tidak sempat menurunkan hitungan sebelum dinaikkan:
        this.aiPending.incrementAndGet();
        if (this.lbqQueries.offer(zQuery)){
            return true;
        }
        this.aiPending.decrementAndGet();
        long tNow = System.currentTimeMillis();
        if ((tNow - this.tLastFullLog) >= 5000){
            this.tLastFullLog = tNow;
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "stock data db write queue full (" + MAX_QUEUE_SIZE + "), query dropped: " + zQuery);
        }
        return false;
    }

    private void startWriter(){
        Thread th = new Thread(new Runnable(){
            @Override
            public void run() {
                ArrayList<String> lstBatch = new ArrayList<String>(MAX_BATCH_SIZE);
                while(true){
                    try{
                        lstBatch.clear();
                        lstBatch.add(lbqQueries.take()); //.menunggu sampai ada query.
                        lbqQueries.drainTo(lstBatch, MAX_BATCH_SIZE - 1);
                        writeBatch(lstBatch);
                    }catch(InterruptedException ex0){
                        break;
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                    }
                }
            }
        }, "DbRiskMgtWriteStockData-Writer");
        th.setDaemon(true);
        th.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
            @Override
            public void run() {
                long tEnd = System.currentTimeMillis() + SHUTDOWN_WAIT_MS;
                while ((aiPending.get() > 0) && (System.currentTimeMillis() < tEnd)){
                    try{
                        Thread.sleep(20);
                    }catch(InterruptedException ex0){
                        break;
                    }
                }
            }
        }, "DbRiskMgtWriteStockData-ShutdownWait"));
    }

    /**
     * Tulis satu kelompok query ke database secara berurutan, memakai satu koneksi / statement per kelompok.
     * Query hanya dianggap selesai jika berhasil dieksekusi atau gagal karena kesalahan SQL; jika penyebabnya
     * koneksi (tidak tersedia / terputus), query yang tersisa dicoba lagi setelah jeda, dengan urutan yang sama.
     */
    private void writeBatch(ArrayList<String> lstBatch) throws InterruptedException {
        int iIdx = 0;
        while (iIdx < lstBatch.size()){
            Connection hConn = DBAccessManagement.getInstance.getDbMasterAlreadyActiveConn();
            if (hConn == null){
                long tNow = System.currentTimeMillis();
                if ((tNow - this.tLastNoConnLog) >= 10000){
                    this.tLastNoConnLog = tNow;
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "stock data db write waiting for database connection > pending: " + this.aiPending.get());
                }
                Thread.sleep(RETRY_DELAY_MS);
                continue;
            }
            Statement stStatement = null;
            boolean bConnBroken = false;
            try{
                stStatement = hConn.createStatement();
                while (iIdx < lstBatch.size()){
                    String zQuery = lstBatch.get(iIdx);
                    try{
                        stStatement.executeUpdate(zQuery);
                        iIdx++;
                        this.aiPending.decrementAndGet();
                    }catch(SQLException sqlex){
                        if (isConnectionBroken(hConn)){
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "stock data db write connection lost, will retry: " + sqlex.getMessage());
                            bConnBroken = true;
                            break;
                        }
                        //.kesalahan SQL biasa (bukan koneksi): catat lalu lewati agar antrean tidak macet.
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage() + "\t" + zQuery);
                        iIdx++;
                        this.aiPending.decrementAndGet();
                    }
                }
            }catch(SQLException sqlex){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "stock data db write cannot create statement, will retry: " + sqlex.getMessage());
                bConnBroken = true;
            }finally{
                if (stStatement != null){
                    try{
                        stStatement.close();
                    }catch(Exception ex0){
                        //.diabaikan.
                    }
                }
            }
            if (bConnBroken){
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
    }

    private boolean isConnectionBroken(Connection hConn){
        try{
            return (hConn.isClosed() || (!hConn.isValid(2)));
        }catch(Exception ex0){
            return true;
        }
    }

    public boolean isStockCA(String stock){
        boolean b = false;

        StockDataRecord stk = null;

        if (mapStock.containsKey(stock)){
            stk = mapStock.get(stock);
        }

        if (stk != null){
            b = getIsStockCallAuctionByRemarks2(stk.getfRemark2(), false);
        }
        return b;
    }

    private String strMidBase1(String inputStr, int pStartPos, int nLength){
        if ((pStartPos <= 0) || (nLength <= 0)){
            return "";
        }
        int maxLen = inputStr.length();
        if (pStartPos > maxLen){
            return "";
        }
        maxLen = maxLen - (pStartPos - 1);
        if (nLength > maxLen){
            nLength = maxLen;
        }
        try{
            return inputStr.substring((pStartPos - 1), ((pStartPos + nLength) - 1));
        }catch(Exception ex){
            return "";
        }
    }

    public boolean getIsStockCallAuctionByRemarks2(String zStockRemark2, boolean iDefaultvalue){
        boolean bOut = iDefaultvalue;
        try{
            if ((!StringHelper.isNullOrEmpty(zStockRemark2)) && (zStockRemark2.length() >= QRIDataConst.QRIFieldValue.STOCK_REMARKS2_EFFECTIVE_SIZE)){
                switch(strMidBase1(zStockRemark2, 30, 1)){
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK2_30_CODE_WATCHLIST:
                        bOut = true;
                        break;
                    default:
                        //.value tidak dikenal:
                        bOut = false;
                        break;
                }
                //.selesai... .
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.SERVER, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return bOut;
    }
}
