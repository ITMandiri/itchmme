/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.racing.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.xtream.inet.trading.mapbackup.ITMMapBackupMgr;
import com.itm.xtream.inet.trading.mapbackup.ITMMapBackupProcessor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.Timer;

/**
 *
 * @author fredy
 */
public class SheetOfORINewOrder {
        //.single instance:
    public final static SheetOfORINewOrder getInstance = new SheetOfORINewOrder();
    
    public final static String BACKUP_CODE = "BOOK_OF_JONEC_RACING_NEW_ORDER";
    
    public ITMMapBackupProcessor backupProcessor;
    
    private final ConcurrentLinkedQueue<ORIDataNewOrder> clqSheets = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String ,ORIDataNewOrder> chmSheets = new ConcurrentHashMap<>();
    
    public SheetOfORINewOrder() {
        initializeBackupProcessors(true);
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheet(ORIDataNewOrder mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && !StringHelper.isNullOrEmpty(mSheet.getfClOrdID())){
                String vId = mSheet.getfClOrdID();
                this.clqSheets.add(mSheet);
                this.chmSheets.put(vId, mSheet);
                mOut = true;
                //.backup:
                this.backupProcessor.backupMapObjectToFile(vId, mSheet);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentLinkedQueue<ORIDataNewOrder> retrieveAllSheets(){
        return this.clqSheets;
    }
    
    public ORIDataNewOrder retrieveSheet(String vId){
        ORIDataNewOrder mOut = null;
        try{
            mOut = this.chmSheets.get(vId);            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ORIDataNewOrder retrieveFirstSheet(){
        ORIDataNewOrder mOut = null;
        try{
            if (this.clqSheets.size() > 0){
                mOut = this.clqSheets.peek();
            }
                        
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ORIDataNewOrder removeFirstSheet(){
        ORIDataNewOrder mOut = null;
        try{
            if (this.clqSheets.size() > 0){
                mOut = this.clqSheets.poll();
                if (mOut != null){
                    this.chmSheets.remove(mOut.getfClOrdID());
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
            this.clqSheets.clear();
            mOut = this.clqSheets.isEmpty();
            this.chmSheets.clear();
            //.backup:
            this.backupProcessor.clearMapFromFile();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void initializeBackupProcessors(boolean bFirstTime){
        try{
            backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, ORIDataNewOrder.class);
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
