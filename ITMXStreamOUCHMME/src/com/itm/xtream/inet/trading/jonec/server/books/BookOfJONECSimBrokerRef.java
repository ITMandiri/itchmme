/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
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
public class BookOfJONECSimBrokerRef {
    //.single instance:
    public final static BookOfJONECSimBrokerRef getInstance = new BookOfJONECSimBrokerRef();
    
    public final static String BACKUP_CODE = "BOOK_OF_JONEC_SIM_BROKERREFS_SEQUENCE";
    
    public ITMMapBackupProcessor backupProcessorBrokerRefsSequence;
    
    private final ConcurrentHashMap<String, Integer> chmBrokerRefsSequence = new ConcurrentHashMap<>();
    
    public BookOfJONECSimBrokerRef() {
        initializeBackupProcessors(true);
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean addToMemory(String vBrokerRef, int vSequence){
        boolean mOut = false;
        try{
            if (!StringHelper.isNullOrEmpty(vBrokerRef)){
                this.chmBrokerRefsSequence.put(vBrokerRef, vSequence);
                mOut = true;
                //.backup:
                this.backupProcessorBrokerRefsSequence.backupMapObjectToFile(vBrokerRef, vSequence);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public int findSeqBrokerRef(String vBrokerRef){
        int mOut = 0;
        try{
            if (!this.chmBrokerRefsSequence.isEmpty()){
                Object val = this.chmBrokerRefsSequence.get(vBrokerRef);
                if (val instanceof Integer) {
                    mOut = (Integer) val;
                } else if (val instanceof String) {
                    mOut = Integer.parseInt((String) val);
                }
//                mOut = this.chmBrokerRefsSequence.get(vBrokerRef);
            }
        }catch(NumberFormatException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean hasBrokerRef(String vBrokerRef){
        boolean mOut = false;
        try{
            if (!this.chmBrokerRefsSequence.isEmpty()){
                mOut = this.chmBrokerRefsSequence.get(vBrokerRef) != null;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void initializeBackupProcessors(boolean bFirstTime){
        try{
            backupProcessorBrokerRefsSequence = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmBrokerRefsSequence, String.class, String.class);
            backupProcessorBrokerRefsSequence.startProcessor();
            
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
