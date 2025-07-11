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
import com.itm.idx.data.qri.message.struct.QRIDataSubscription;
import com.itm.idx.data.qri.message.struct.QRIDataTradeListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataTradingLimitMessage;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackController;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackProcessor;
import com.itm.xtream.inet.trading.martin.server.client.subscription.MartinClientSubscriptionManagement;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class BookOfMARTINTradingLimitList {
    
    public final static BookOfMARTINTradingLimitList getInstance = new BookOfMARTINTradingLimitList();
    
    private final ConcurrentHashMap<Long, QRIDataTradingLimitMessage> chmSheets = new ConcurrentHashMap<>();
    
    public BookOfMARTINTradingLimitList(){
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean brodcastToSubscriber(QRIDataTradingLimitMessage v){
        boolean mOut = false;
        try{
            List<String> lstSubsClient =  MartinClientSubscriptionManagement.getInstance.getClientBySubscription(QRIDataSubscription.ReqSubscriptionType.LimitSubscript, true);         
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
    
    public boolean addOrUpdateSheet(QRIDataTradingLimitMessage mSheet){
        boolean mOut = false;
        mSheet.setfSeqNumber(this.chmSheets.size() + 1);
        try{
            if ((mSheet != null) && (mSheet.getfSeqNumber() > 0)){
                Long vId = mSheet.getfSeqNumber();
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
    
    public ConcurrentHashMap<Long, QRIDataTradingLimitMessage> retrieveAllSheets(){
        return this.chmSheets;
    }
    
    public QRIDataTradingLimitMessage retrieveSheet(Long vId){
        QRIDataTradingLimitMessage mOut = null;
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
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
}
