/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.server.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.xtream.inet.trading.feed.msgmem.ITMFeedMsgMemory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author fredy
 */
public class FeedServerCallbackProcessor implements Runnable {
    
    private final static int FEED_READ_BATCH_SIZE                       = 500; //.jumlah baris datafeed yang dibaca sekaligus dari file.
    private final static long PING_INTERVAL_MS                          = 1000; //.jeda antar IDXPING ke client.
    private final static long DISCONNECTED_POLL_MS                      = 20;   //.jeda pemeriksaan ulang saat channel client terputus.
    private final static long STALLED_POLL_MS                           = 1;    //.jeda coba lagi saat data belum bisa dibaca dari file.

    private ITMSocketChannel chChannel;
    private final AtomicBoolean bAlreadyLoggedIn                       = new AtomicBoolean(false);
    
    private Thread thOutWorker;
    private final AtomicBoolean bOutWorkerMustRun                       = new AtomicBoolean(false);
    private final AtomicBoolean bNeedAdjustSequence                     = new AtomicBoolean(false);
    
    private final AtomicInteger aiSequenceNo                            = new AtomicInteger(0);
    
    public FeedServerCallbackProcessor(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    //.basic info:
    public ITMSocketChannel getChChannel() {
        return this.chChannel;
    }
    
    public void setChChannel(ITMSocketChannel chChannel) {
        this.chChannel = chChannel;
    }
    
    public boolean getAlreadyLoggedIn() {
        return this.bAlreadyLoggedIn.get();
    }
    
    public void setAlreadyLoggedIn(boolean bLoggedIn) {
        this.bAlreadyLoggedIn.set(bLoggedIn);
    }
    
    public int getSequenceNo() {
        return this.aiSequenceNo.get();
    }
    
    public synchronized void adjustSequenceNo(int newSequenceNo){
        try{
            this.aiSequenceNo.set(newSequenceNo);
            this.bNeedAdjustSequence.set(true);
            setAlreadyLoggedIn(true);
            wakeUpProcessor();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "terminate processor exception:" + ex0);
        }
    }
    
    public synchronized void startProcessor(){
        try{
            if (this.thOutWorker == null){
                this.thOutWorker = new Thread(this);
            }
            if (this.thOutWorker != null){
                if (!this.thOutWorker.isAlive()){
                    try{
                        this.thOutWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                        this.bOutWorkerMustRun.set(true);
                        this.thOutWorker.setPriority(Thread.NORM_PRIORITY);
                        this.thOutWorker.start();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot start output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }else{
                    try{
                        wakeUpProcessor();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-resume output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "start processor exception:" + ex0);
        }
    }
    
    public synchronized void wakeUpProcessor(){
        try{
            if (this.thOutWorker != null){
                if (this.thOutWorker.isAlive()){
                    try{
                        notify();
                        ITMFeedMsgMemory.getInstance.wakeFeedWaiters(); //.20261029: thread kini menunggu di penyimpan datafeed.
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-resume output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "wakeup processor exception:" + ex0);
        }
    }
    
    public synchronized void stopProcessor(){
        try{
            if (this.thOutWorker != null){
                if (this.thOutWorker.isAlive()){
                    try{
                        this.bOutWorkerMustRun.set(false);
                        wakeUpProcessor();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-close output processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "stop processor exception:" + ex0);
        }
    }
    
    @Override
    public void run() {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //.20261029: thread tidak lagi polling wait(1): menunggu sinyal data baru dari penyimpan datafeed (tanpa wakeup yang terlewat),
        //.dan ping dikirim berdasarkan waktu (PING_INTERVAL_MS), bukan berdasarkan jumlah putaran loop.
        long tLastPing = System.currentTimeMillis();
        boolean bNeedToLog = false;
        int iToBeSendSequenceNo = 0;
        String zToBeSendDataFeedLine = "";
        
        while(this.bOutWorkerMustRun.get()){
            //.ambil versi sinyal SEBELUM memeriksa kondisi apa pun (supaya perubahan setelah ini membuat penungguan langsung kembali):
            long lSignalVersion = ITMFeedMsgMemory.getInstance.getFeedSignalVersion();
            boolean bStalled = false;                   //.ada data tapi belum bisa dibaca (masih di antrean tulis): coba lagi sebentar lagi.
            int iWaitForSequenceNo = Integer.MAX_VALUE; //.sequence yang ditunggu; MAX_VALUE = tidak menunggu data (belum login).
            long lWaitMs = PING_INTERVAL_MS;
            
            if (chChannel != null){
                if (chChannel.isConnected() && (!chChannel.isClosed())){
                    try{
                        //.kirim jika ada:
                        if (getAlreadyLoggedIn()){
                            if (bNeedAdjustSequence.get() == true){
                                bNeedToLog = true;
                                iToBeSendSequenceNo = getSequenceNo();
                                bNeedAdjustSequence.set(false);
                            }else{
                                bNeedToLog = false;
                                iToBeSendSequenceNo = (getSequenceNo() + 1);
                            }
                            while(bNeedAdjustSequence.get() == false){
                                if (iToBeSendSequenceNo <= ITMFeedMsgMemory.getInstance.getFeedCount()){

                                    //.20261029: datafeed dibaca dari file (per batch agar efisien), bukan dari list di memori:
                                    java.util.List<String> lstBatch = ITMFeedMsgMemory.getInstance.getFeedLines(iToBeSendSequenceNo, FEED_READ_BATCH_SIZE);
                                    if (lstBatch.isEmpty()){
                                        bStalled = true;
                                        break; //.tidak ada yang bisa dibaca saat ini, coba lagi pada putaran berikutnya.
                                    }
                                    for (int iBatchIdx = 0; iBatchIdx < lstBatch.size(); iBatchIdx++){
                                        if (bNeedAdjustSequence.get()){
                                            break; //.client minta pindah sequence, hentikan batch ini.
                                        }
                                        zToBeSendDataFeedLine = lstBatch.get(iBatchIdx);

                                        if (bNeedToLog){
                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "MIS.iToBeSendSequenceNo value:" + iToBeSendSequenceNo + " > message:" + zToBeSendDataFeedLine);
                                            //.20260525: untuk menghandle log yang over
                                            bNeedToLog = false;
                                        }
                                        if ((zToBeSendDataFeedLine != null) && (zToBeSendDataFeedLine.length() > 0)){
                                            chChannel.sendMessageDirect(zToBeSendDataFeedLine);
                                            //////chChannel.addSendQueue(zToBeSendDataFeedLine);
                                        }
                                        aiSequenceNo.set(iToBeSendSequenceNo);
                                        iToBeSendSequenceNo++;
                                    }

                                }else{
                                    break;
                                }
                            }
                            iWaitForSequenceNo = iToBeSendSequenceNo;
                        }
                        //. kirim ping (berdasarkan waktu):
                        long tNow = System.currentTimeMillis();
                        if ((tNow - tLastPing) >= PING_INTERVAL_MS){
                            tLastPing = tNow;
                            chChannel.sendMessageDirect("IDXPING");
                        }
                        lWaitMs = Math.max(1, PING_INTERVAL_MS - (System.currentTimeMillis() - tLastPing));
                        
                    }catch(Exception ex0){
                        lWaitMs = 1; //.jangan berputar tanpa jeda jika terus terjadi error.
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to output processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }else{
                    //.20261029: channel terputus / tertutup: jangan berputar tanpa jeda (sebelumnya memakai 100% satu core CPU).
                    lWaitMs = DISCONNECTED_POLL_MS;
                }
            }else{
                lWaitMs = DISCONNECTED_POLL_MS;
            }
            
            //.tunggu data baru / perintah berikutnya / waktu ping:
            try{
                if (bStalled){
                    ITMFeedMsgMemory.getInstance.awaitNewFeed(lSignalVersion, Integer.MAX_VALUE, STALLED_POLL_MS);
                }else{
                    ITMFeedMsgMemory.getInstance.awaitNewFeed(lSignalVersion, iWaitForSequenceNo, lWaitMs);
                }
            }catch(InterruptedException ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "interrupt to output processor worker thread for:" + (chChannel != null ? chChannel.getChannelID() : "") + " > err:" + ex0);
            }
        }
        
        bNeedAdjustSequence.set(true);
        
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "output processor worker thread finished.");
    }
    
}
