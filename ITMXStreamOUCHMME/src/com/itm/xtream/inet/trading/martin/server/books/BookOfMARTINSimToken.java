/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
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
public class BookOfMARTINSimToken {
    //.single instance:
    public final static BookOfMARTINSimToken getInstance = new BookOfMARTINSimToken();
    
    public final static String BACKUP_CODE_TOKENS_TO_BROKERREFS = "BOOK_OF_MARTIN_SIM_TOKEN_TOKENS_TO_BROKERREFS";
    public final static String BACKUP_CODE_BROKERREFS_TO_TOKENS = "BOOK_OF_MARTIN_SIM_TOKEN_BROKERREFS_TO_TOKENS";
    
    public ITMMapBackupProcessor backupProcessorTokensToBrokerRefs;
    public ITMMapBackupProcessor backupProcessorBrokerRefsToTokens;
    
    private long vTokenLastNo = 0;
    
    private final ConcurrentHashMap<Long, String> chmTokensToBrokerRefs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> chmBrokerRefsToTokens = new ConcurrentHashMap<>();
    
    public BookOfMARTINSimToken() {
        ////backupProcessorTokensToBrokerRefs = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE_TOKENS_TO_BROKERREFS, true, false, chmTokensToBrokerRefs, Long.class, String.class);
        ////backupProcessorBrokerRefsToTokens = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE_BROKERREFS_TO_TOKENS, true, false, chmBrokerRefsToTokens, String.class, Long.class);
        ////backupProcessorTokensToBrokerRefs.startProcessor();
        ////backupProcessorBrokerRefsToTokens.startProcessor();
        initializeBackupProcessors(true);
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public synchronized long generateTrxToken(String vBrokerRef){
        long mOut = 0;
        try{
            if (!StringHelper.isNullOrEmpty(vBrokerRef)){
                this.vTokenLastNo++;
                this.chmTokensToBrokerRefs.put(this.vTokenLastNo, vBrokerRef);
                this.chmBrokerRefsToTokens.put(vBrokerRef, this.vTokenLastNo);
                mOut = this.vTokenLastNo;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public synchronized long resetTrxTokens(){
        long mOut = this.vTokenLastNo;
        try{
            this.vTokenLastNo = 0;
            this.chmTokensToBrokerRefs.clear();
            this.chmBrokerRefsToTokens.clear();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public long findTokenByBrokerRef(String vBrokerRef){
        long mOut = 0;
        try{
            if (!this.chmBrokerRefsToTokens.isEmpty()){
                mOut = this.chmBrokerRefsToTokens.get(vBrokerRef);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public String findBrokerRefByToken(long vToken){
        String mOut = "";
        try{
            if (!this.chmTokensToBrokerRefs.isEmpty()){
                mOut = this.chmTokensToBrokerRefs.get(vToken);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void initializeBackupProcessors(boolean bFirstTime){
        try{
            backupProcessorTokensToBrokerRefs = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE_TOKENS_TO_BROKERREFS, true, false, chmTokensToBrokerRefs, Long.class, String.class);
            backupProcessorBrokerRefsToTokens = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE_BROKERREFS_TO_TOKENS, true, false, chmBrokerRefsToTokens, String.class, Long.class);
            backupProcessorTokensToBrokerRefs.startProcessor();
            backupProcessorBrokerRefsToTokens.startProcessor();
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
