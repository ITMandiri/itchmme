/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketController;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketMgr;
import com.itm.xtream.inet.trading.mapbackup.ITMMapBackupMgr;
import com.itm.xtream.inet.trading.mapbackup.ITMMapBackupProcessor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.Timer;

/**
 *
 * @author fredy
 */
public class BookOfJONECSimCalcQty {
    //.single instance:
    public final static BookOfJONECSimCalcQty getInstance = new BookOfJONECSimCalcQty();
    
    public final static String BACKUP_CODE = "BOOK_OF_JONEC_SIM_CALC_QTY";
    
    public ITMMapBackupProcessor backupProcessor;
    
    private final ConcurrentHashMap<Long, SheetOfJONECSimCalcQty> chmSheets = new ConcurrentHashMap<>();
    
    public BookOfJONECSimCalcQty() {
//        backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, SheetOfJONECSimCalcQty.class);
//        backupProcessor.startProcessor();
        initializeBackupProcessors(true);
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheet(SheetOfJONECSimCalcQty mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getOrderToken() > 0)){
                Long vId = mSheet.getOrderToken();
                if (this.chmSheets.containsKey(vId)){
                    this.chmSheets.replace(vId, mSheet);
                }else{
                    this.chmSheets.put(vId, mSheet);
                }
                mOut = true;
                //. set sequence ouch terakhir, untuk keperluan skip process
                ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmOUCHs = ITMSoupBinTCPOUCHPacketMgr.getInstance.getAllConnectionLines();
                if (chmOUCHs.containsKey("OUCH_TRANSACTION")){
                    ITMSoupBinTCPOUCHPacketController mController = chmOUCHs.get("OUCH_TRANSACTION");
                    mSheet.setLastOuchSeq(mController.getCurrentSequencedNo());
                }
                //.backup:
                this.backupProcessor.backupMapObjectToFile(vId, mSheet);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentHashMap<Long, SheetOfJONECSimCalcQty> retrieveAllSheets(){
        return this.chmSheets;
    }
    
    public SheetOfJONECSimCalcQty retrieveSheet(Long vOrderToken){
        SheetOfJONECSimCalcQty mOut = null;
        try{
            mOut = this.chmSheets.get(vOrderToken);
            if (mOut != null){
                //. 20211115 hrn: saat retrive sheet juga pastikan catat freq terakhir ouch
                //. set sequence ouch terakhir, untuk keperluan skip process
                ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmOUCHs = ITMSoupBinTCPOUCHPacketMgr.getInstance.getAllConnectionLines();
                if (chmOUCHs.containsKey("OUCH_TRANSACTION")){
                    ITMSoupBinTCPOUCHPacketController mController = chmOUCHs.get("OUCH_TRANSACTION");
                    mOut.setLastOuchSeq(mController.getCurrentSequencedNo());
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.chmSheets.clear();
            mOut = this.chmSheets.isEmpty();
            //.backup:
            this.backupProcessor.clearMapFromFile();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void initializeBackupProcessors(boolean bFirstTime){
        try{
            backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, SheetOfJONECSimCalcQty.class);
            backupProcessor.startProcessor();
            if (bFirstTime){
                Timer hTmr = new Timer(1000, new ActionListener() {
                    String zPrevDate = DateTimeHelper.getDateSVRTRXFormat();
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            String zNextDate = DateTimeHelper.getDateSVRTRXFormat();
                            if (!zNextDate.equals(this.zPrevDate)){
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "need recall initialize action performed backup processors: prevdate=" + this.zPrevDate + ", nextdate=" + zNextDate);
                                this.zPrevDate = zNextDate;
                                initializeBackupProcessors(false);
                            }
                        }catch (Exception ex0) {
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "initialize action performed backup processors exception:" + ex0);
                        }
                    }
                });
                hTmr.start();
            }
            
        }catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, (bFirstTime ? "" : "recall ") + "initialize backup processors exception:" + ex0);
        }
    }
}