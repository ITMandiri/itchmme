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
import com.itm.idx.data.qri.message.struct.QRIDataSubscription;
import com.itm.idx.data.qri.message.struct.QRIDataTradeListMessage;
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
public class BookOfMARTINTradeList {
    
    public final static String BACKUP_CODE = "BOOK_OF_MARTIN_TRADE_LIST";
    
    public ITMMapBackupProcessor backupProcessor;
    
    public final static BookOfMARTINTradeList getInstance = new BookOfMARTINTradeList();
    
    private final ConcurrentHashMap<String, QRIDataTradeListMessage> chmSheets = new ConcurrentHashMap<>();
    
    public BookOfMARTINTradeList(){
        ////backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, QRIDataTradeListMessage.class);
        ////backupProcessor.startProcessor();
        initializeBackupProcessors(true);
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean brodcastToSubscriber(QRIDataTradeListMessage v){
        boolean mOut = false;
        try{
            List<String> lstSubsClient =  MartinClientSubscriptionManagement.getInstance.getClientBySubscription(QRIDataSubscription.ReqSubscriptionType.TradeSubscript, true);         
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
    
    public boolean addOrUpdateSheet(QRIDataTradeListMessage mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getfSecondaryOrderID()> 0)){
                Long vId1 = mSheet.getfSecondaryOrderID();
                Long vId2 = mSheet.getfOrderID();
                String vId = vId1.toString() + "_" + vId2.toString();
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
    
    public ConcurrentHashMap<String, QRIDataTradeListMessage> retrieveAllSheets(){
        return this.chmSheets;
    }
    
    public QRIDataTradeListMessage retrieveSheet(String vId){
        QRIDataTradeListMessage mOut = null;
        try{
            mOut = this.chmSheets.get(vId);
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
            backupProcessor = ITMMapBackupMgr.getInstance.getOrCreateBackupProcessor(BACKUP_CODE, true, false, chmSheets, Long.class, QRIDataTradeListMessage.class);
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
