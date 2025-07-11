/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.idx.data.helpers.DateTimeHelper;
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
public class BookOfMARTINSimOriginRequest {
    //.single instance:
    public final static BookOfMARTINSimOriginRequest getInstance = new BookOfMARTINSimOriginRequest();
    
    public final static String BACKUP_CODE = "BOOK_OF_MARTIN_SIM_ORIGIN_REQUEST";
    
    public ITMMapBackupProcessor backupProcessor;
    
    private final ConcurrentHashMap<Long, SheetOfMARTINSimOriginRequest> chmSheets = new ConcurrentHashMap<>();
    
    public BookOfMARTINSimOriginRequest() {
        ////backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, SheetOfMARTINSimOriginRequest.class);
        ////backupProcessor.startProcessor();
        initializeBackupProcessors(true);
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheet(SheetOfMARTINSimOriginRequest mSheet){
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
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentHashMap<Long, SheetOfMARTINSimOriginRequest> retrieveAllSheets(){
        return this.chmSheets;
    }
    
    public SheetOfMARTINSimOriginRequest retrieveSheet(Long vOrderId){
        SheetOfMARTINSimOriginRequest mOut = null;
        try{
            mOut = this.chmSheets.get(vOrderId);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.chmSheets.clear();
            mOut = this.chmSheets.isEmpty();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void initializeBackupProcessors(boolean bFirstTime){
        try{
            backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, SheetOfMARTINSimOriginRequest.class);
            backupProcessor.startProcessor();
            if (bFirstTime){
                Timer hTmr = new Timer(1000, new ActionListener() {
                    String zPrevDate = DateTimeHelper.getDateSVRTRXFormat();
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            String zNextDate = DateTimeHelper.getDateSVRTRXFormat();
                            if (!zNextDate.equals(this.zPrevDate)){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "need recall initialize action performed backup processors: prevdate=" + this.zPrevDate + ", nextdate=" + zNextDate);
                                this.zPrevDate = zNextDate;
                                initializeBackupProcessors(false);
                            }
                        }catch (Exception ex0) {
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "initialize action performed backup processors exception:" + ex0);
                        }
                    }
                });
                hTmr.start();
            }
            
        }catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, (bFirstTime ? "" : "recall ") + "initialize backup processors exception:" + ex0);
        }
    }
    
}
