/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.books;

import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.xtream.inet.trading.mapbackup.ITMMapBackupMgr;
import com.itm.xtream.inet.trading.mapbackup.ITMMapBackupProcessor;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.Timer;

/**
 *
 * @author fredy
 */
public class BookOfFIX5JonecRequest {
    //.single instance:
    public final static BookOfFIX5JonecRequest getInstance = new BookOfFIX5JonecRequest();
    
    public final static String BACKUP_CODE_SEND = "BOOK_OF_FIX5_JONEC_SEND";
    public final static String BACKUP_CODE_RECV = "BOOK_OF_FIX5_JONEC_RECV";
    
    public ITMMapBackupProcessor backupProcessorSend;
    public ITMMapBackupProcessor backupProcessorRecv;
    
    private final ConcurrentHashMap<Long, SheetOfFIX5JonecRequest> chmSheetsFix5JonecSend = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, SheetOfFIX5JonecRequest> chmSheetsFix5JonecRecv = new ConcurrentHashMap<>();
    
    public BookOfFIX5JonecRequest() {
        initializeBackupProcessors(true);
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheetSend(SheetOfFIX5JonecRequest mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getSeq() > 0)){
                Long vId = mSheet.getSeq();
                if (this.chmSheetsFix5JonecSend.containsKey(vId)){
                    this.chmSheetsFix5JonecSend.replace(vId, mSheet);
                }else{
                    this.chmSheetsFix5JonecSend.put(vId, mSheet);
                }
                mOut = true;
                //.backup:
                this.backupProcessorSend.backupMapObjectToFile(vId, mSheet);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean addOrUpdateSheetRecv(SheetOfFIX5JonecRequest mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getSeq() > 0)){
                Long vId = mSheet.getSeq();
                if (this.chmSheetsFix5JonecRecv.containsKey(vId)){
                    this.chmSheetsFix5JonecRecv.replace(vId, mSheet);
                }else{
                    this.chmSheetsFix5JonecRecv.put(vId, mSheet);
                }
                mOut = true;
                //.backup:
                this.backupProcessorRecv.backupMapObjectToFile(vId, mSheet);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentHashMap<Long, SheetOfFIX5JonecRequest> retrieveAllSheetsSend(){
        return this.chmSheetsFix5JonecSend;
    }
    
    public ConcurrentHashMap<Long, SheetOfFIX5JonecRequest> retrieveAllSheetsRecv(){
        return this.chmSheetsFix5JonecRecv;
    }
    
    public SheetOfFIX5JonecRequest retrieveSheetSend(Long vSeq){
        SheetOfFIX5JonecRequest mOut = null;
        try{
            mOut = this.chmSheetsFix5JonecSend.get(vSeq);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public SheetOfFIX5JonecRequest retrieveSheetRecv(Long vSeq){
        SheetOfFIX5JonecRequest mOut = null;
        try{
            mOut = this.chmSheetsFix5JonecRecv.get(vSeq);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.chmSheetsFix5JonecSend.clear();
            this.chmSheetsFix5JonecRecv.clear();
            mOut = this.chmSheetsFix5JonecSend.isEmpty();
            mOut = this.chmSheetsFix5JonecRecv.isEmpty();
            //.backup:
            this.backupProcessorSend.clearMapFromFile();
            this.backupProcessorRecv.clearMapFromFile();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public long restoreLastSeqFromMap(){
        long mOut = 0;
        try{
            FIX5IDXBridgeController mController = FIX5IDXBridgeManager.getInstance.getFIX5ConnLine(ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.fix5_connections[0].name);
            if (mController != null){
                long vMaxSeq = 0;
                if (this.chmSheetsFix5JonecSend.size() > 0){
                    for(Long vEachSeq : this.chmSheetsFix5JonecSend.keySet()){
                        if (vEachSeq > vMaxSeq){
                            vMaxSeq = vEachSeq;
                        }
                    }
                }
                mController.setCurrentTXSequencedNo(vMaxSeq);
                if (vMaxSeq > mOut){
                    mOut = vMaxSeq;
                }
                vMaxSeq = 0;
                if (this.chmSheetsFix5JonecRecv.size() > 0){
                    for(Long vEachSeq : this.chmSheetsFix5JonecRecv.keySet()){
                        if (vEachSeq > vMaxSeq){
                            vMaxSeq = vEachSeq;
                        }
                    }
                }
                mController.setCurrentRXSequencedNo(vMaxSeq);
                if (vMaxSeq > mOut){
                    mOut = vMaxSeq;
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void initializeBackupProcessors(boolean bFirstTime){
        try{
            backupProcessorSend = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE_SEND, true, false, chmSheetsFix5JonecSend, Long.class, SheetOfFIX5JonecRequest.class);
            backupProcessorRecv = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE_RECV, true, false, chmSheetsFix5JonecRecv, Long.class, SheetOfFIX5JonecRequest.class);
            backupProcessorSend.startProcessor();
            backupProcessorRecv.startProcessor();
            if (!bFirstTime){
                restoreLastSeqFromMap();
            }
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
