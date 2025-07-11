/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.msgmem.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataSubscription;
import com.itm.xtream.inet.trading.mapbackup.ITMMapBackupMgr;
import com.itm.xtream.inet.trading.mapbackup.ITMMapBackupProcessor;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackController;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackProcessor;
import com.itm.xtream.inet.trading.martin.server.client.subscription.MartinClientSubscriptionManagement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.Timer;

/**
 *
 * @author fredy
 */
public class BookOfMARTINOrderList {
    
    public final static String BACKUP_CODE = "BOOK_OF_MARTIN_ORDER_LIST";
    
    public ITMMapBackupProcessor backupProcessor;
    
    public final static BookOfMARTINOrderList getInstance = new BookOfMARTINOrderList();
    
    private final ConcurrentHashMap<Long, QRIDataOrderListMessage> chmSheets = new ConcurrentHashMap<>();
    
    public BookOfMARTINOrderList(){
        ////backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, QRIDataOrderListMessage.class);
        ////backupProcessor.startProcessor();
        initializeBackupProcessors(true);
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean brodcastToSubscriber(QRIDataOrderListMessage v){
        boolean mOut = false;
        try{
            List<String> lstSubsClient =  MartinClientSubscriptionManagement.getInstance.getClientBySubscription(QRIDataSubscription.ReqSubscriptionType.OrderSubscript, true);         
            for (int c = 0; c < lstSubsClient.size(); c++){
                String zConnName = lstSubsClient.get(c);
                v.setfConnName(zConnName);
                MARTINSimCallbackProcessor mMartinLine = MARTINSimCallbackController.getInstance.getActiveChannelProcessorByConnName(zConnName);
                if ((mMartinLine != null) && (mMartinLine.getAlreadyLoggedIn()) && ((mMartinLine.getChChannel() != null))){
                    if (mMartinLine.getChChannel().sendMessageDirect(v.msgToString())){
                        //... .
                    }else{ //. gagal dikirim ke klien
                        //.???:
                        //. TODO : handle lewat Martin
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                    }
                }else{ //. tidak bisa dikirim ke klien
                    //.???:
                    //. TODO : handle lewat Martin
                    //. masukin log
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                }
            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean addOrUpdateSheet(QRIDataOrderListMessage mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getfOrderID()> 0)){
                Long vId = mSheet.getfOrderID();
                if (this.chmSheets.containsKey(vId)){
                    this.chmSheets.replace(vId, mSheet);
                }else{
                    this.chmSheets.put(vId, mSheet);
                }
                mOut = true;
                //.backup:
                this.backupProcessor.backupMapObjectToFile(vId, mSheet);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentHashMap<Long, QRIDataOrderListMessage> retrieveAllSheets(){
        return this.chmSheets;
    }
    
    public QRIDataOrderListMessage retrieveSheet(Long vOrderId){
        QRIDataOrderListMessage mOut = null;
        try{
            mOut = this.chmSheets.get(vOrderId);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR.ERROR, ex0);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void initializeBackupProcessors(boolean bFirstTime){
        try{
            backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, QRIDataOrderListMessage.class);
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
