/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.client.subscription;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataSecuritiesMessage;
import com.itm.idx.data.qri.message.struct.QRIDataSecurityAttributeMessage;
import com.itm.idx.data.qri.message.struct.QRIDataSubscription;
import com.itm.idx.data.qri.message.struct.QRIDataTradeListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataTradingLimitMessage;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackController;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackProcessor;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINNegDealList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINOrderList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINSecurityAttributeList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINSecurityList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINTradeList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINTradingLimitList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class MartinClientSubscriptionManagement {
    
    public enum MartinSubscriptionResult{
        Subscribe               ("S"),
        Unsubscribe             ("U");
        
        private MartinSubscriptionResult(String val){
            this.value = val;
        }
        
        private String value;        
        public String getValue() {
            return value;
        }
        
    }
    
    //.single instance:
    public final static MartinClientSubscriptionManagement getInstance = new MartinClientSubscriptionManagement();
    
    private final ConcurrentHashMap<String, List<QRIDataSubscription>> chmSubscriptions = new ConcurrentHashMap<>();
     
    public MartinClientSubscriptionManagement() {
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public MartinSubscriptionResult addSubscription(QRIDataSubscription m){
        //. 20220804 hrn : log subscription
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "addSubscription : ConnName = " + m.getfConnName() + ", ReqType = " + m.getfReqType().getValue());
        
        //. 20210906 hrn: sudah tidak ada unsubscribe
        MartinSubscriptionResult mResult = MartinSubscriptionResult.Subscribe;
        
        if (chmSubscriptions.containsKey(m.getfConnName())){
            List<QRIDataSubscription> mList = chmSubscriptions.get(m.getfConnName());
            //. sudah tidak ada unsubscribe
            boolean bFound = false;
            for (int i = 0; i < mList.size(); i++){
                QRIDataSubscription o = mList.get(i);
                if (o.getfReqType() == m.getfReqType()){ //. sudah pernah subscribe artinya harus unsub
                    mResult = MartinSubscriptionResult.Subscribe;
                    //. remove dari list
                    /////mList.remove(o);
                    bFound = true;
                    break;
                }
            }
            
            if (!bFound){
                mList.add(m);
            }
            
        }else{
            List<QRIDataSubscription> mList = new ArrayList<>();
            mList.add(m);
            chmSubscriptions.put(m.getfConnName(), mList);
        }
        
        return mResult;
    }
    
    public MartinSubscriptionResult addSubscription_old(QRIDataSubscription m){
        //. 20210906 hrn: sudah tidak ada unsubscribe
        MartinSubscriptionResult mResult = MartinSubscriptionResult.Subscribe;
        
        if (chmSubscriptions.containsKey(m.getfConnName())){
            List<QRIDataSubscription> mList = chmSubscriptions.get(m.getfConnName());
            //. sudah tidak ada unsubscribe
//            for (int i = 0; i < mList.size(); i++){
//                QRIDataSubscription o = mList.get(i);
//                if (o.getfReqType() == m.getfReqType()){ //. sudah pernah subscribe artinya harus unsub
//                    mResult = MartinSubscriptionResult.Unsubscribe;
//                    //. remove dari list
//                    mList.remove(o);
//                    break;
//                }
//            }
            
            if (mResult == MartinSubscriptionResult.Subscribe){
                //. tambahkan ke list
                mList.add(m);
            }
        }else{
            List<QRIDataSubscription> mList = new ArrayList<>();
            mList.add(m);
            chmSubscriptions.put(m.getfConnName(), mList);
            mResult = MartinSubscriptionResult.Subscribe;
        }
        
        return mResult;
    }
    public List<String> getClientBySubscription(QRIDataSubscription.ReqSubscriptionType mType, boolean bSkipCheckType){
        List<String> mResult = new ArrayList<>();
        
        for (Map.Entry<String, List<QRIDataSubscription>> entry : chmSubscriptions.entrySet()) {
            String connKey = entry.getKey();
            
            List<QRIDataSubscription> mList = entry.getValue();
            for (int i = 0; i < mList.size(); i++){
                QRIDataSubscription o = mList.get(i);
                if (!bSkipCheckType){
                    if (o.getfReqType() == mType){
                        if (!mResult.contains(connKey)){ //. biar tidak double
                            mResult.add(connKey);
                        }

                    }
                }else{
                    if (!mResult.contains(connKey)){ //. biar tidak double
                        mResult.add(connKey);
                    }
                }
                
            }
            
        }
        
        return mResult;
    }
    
    public boolean broadcastSubscriptionData(QRIDataSubscription m){
        boolean mOut = false;
        
        String zConnName = m.getfConnName();
        
        if (null != m.getfReqType())switch (m.getfReqType()) {
            case OrderSubscript:
                ConcurrentHashMap<Long, QRIDataOrderListMessage> mListOrder = BookOfMARTINOrderList.getInstance.retrieveAllSheets();
                for (Map.Entry<Long, QRIDataOrderListMessage> entry : mListOrder.entrySet()) {
                    QRIDataOrderListMessage v  = entry.getValue();
                    v.setfConnName(zConnName);
                    MARTINSimCallbackProcessor mMartinLine = MARTINSimCallbackController.getInstance.getActiveChannelProcessorByConnName(zConnName);
                    if ((mMartinLine != null) && (mMartinLine.getAlreadyLoggedIn()) && ((mMartinLine.getChChannel() != null))){
                        if (mMartinLine.getChChannel().sendMessageDirect(v.msgToString())){
                            //... .
                        }else{ //. gagal dikirim ke klien
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                    }else{ //. tidak bisa dikirim ke klien
                        //.???:
                        //. TODO : handle lewat Martin
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }
                break;
            case TradeSubscript:
                ConcurrentHashMap<String, QRIDataTradeListMessage> mListTrade = BookOfMARTINTradeList.getInstance.retrieveAllSheets();
                for (Map.Entry<String, QRIDataTradeListMessage> entry : mListTrade.entrySet()) {
                    QRIDataTradeListMessage v  = entry.getValue();
                    v.setfConnName(zConnName);
                    MARTINSimCallbackProcessor mMartinLine = MARTINSimCallbackController.getInstance.getActiveChannelProcessorByConnName(zConnName);
                    if ((mMartinLine != null) && (mMartinLine.getAlreadyLoggedIn()) && ((mMartinLine.getChChannel() != null))){
                        if (mMartinLine.getChChannel().sendMessageDirect(v.msgToString())){
                            //... .
                        }else{ //. gagal dikirim ke klien
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                    }else{ //. tidak bisa dikirim ke klien
                        //.???:
                        //. TODO : handle lewat Martin
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }
                break;
            case NegSubscript:
                ConcurrentHashMap<Long, QRIDataNegDealListMessage> mListNegdeal = BookOfMARTINNegDealList.getInstance.retrieveAllSheets();
                for (Map.Entry<Long, QRIDataNegDealListMessage> entry : mListNegdeal.entrySet()) {
                    QRIDataNegDealListMessage v  = entry.getValue();
                    v.setfConnName(zConnName);
                    MARTINSimCallbackProcessor mMartinLine = MARTINSimCallbackController.getInstance.getActiveChannelProcessorByConnName(zConnName);
                    if ((mMartinLine != null) && (mMartinLine.getAlreadyLoggedIn()) && ((mMartinLine.getChChannel() != null))){
                        if (mMartinLine.getChChannel().sendMessageDirect(v.msgToString())){
                            //... .
                        }else{ //. gagal dikirim ke klien
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                    }else{ //. tidak bisa dikirim ke klien
                        //.???:
                        //. TODO : handle lewat Martin
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }
                break;
            case AdvSubscript:
                break;
            case SecSubscript:
                //. pada kasus aplikasi ITCH dan OUCH pisah server, data ini akan kosong : 20210816
                //. syncron dulu
                BookOfMARTINSecurityList.getInstance.synchDataFromITCH();
                int iRecordNum = 0;
                int iRecordLast = 0;
                
                ConcurrentHashMap<String, QRIDataSecuritiesMessage> mListSec = BookOfMARTINSecurityList.getInstance.retrieveAllSheets(m.getfSymbolSfx());
                for (Map.Entry<String, QRIDataSecuritiesMessage> entry : mListSec.entrySet()) {
                    iRecordNum++;
                    
                    QRIDataSecuritiesMessage v  = entry.getValue();
                    v.setfRecordNumber(iRecordNum);
                    if (iRecordNum >= mListSec.size()){
                        iRecordLast = 1;
                    }
                    v.setfLastRecord(iRecordLast);
                    v.setfConnName(zConnName);
                    MARTINSimCallbackProcessor mMartinLine = MARTINSimCallbackController.getInstance.getActiveChannelProcessorByConnName(zConnName);
                    if ((mMartinLine != null) && (mMartinLine.getAlreadyLoggedIn()) && ((mMartinLine.getChChannel() != null))){
                        if (mMartinLine.getChChannel().sendMessageDirect(v.msgToString())){
                            //... .
                        }else{ //. gagal dikirim ke klien
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                    }else{ //. tidak bisa dikirim ke klien
                        //.???:
                        //. TODO : handle lewat Martin
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }
                break;
            case SecAttributeSubscript:
                ConcurrentHashMap<String, QRIDataSecurityAttributeMessage> mListSecAttr = BookOfMARTINSecurityAttributeList.getInstance.retrieveAllSheets();
                for (Map.Entry<String, QRIDataSecurityAttributeMessage> entry : mListSecAttr.entrySet()) {
                    QRIDataSecurityAttributeMessage v  = entry.getValue();
                    v.setfConnName(zConnName);
                    MARTINSimCallbackProcessor mMartinLine = MARTINSimCallbackController.getInstance.getActiveChannelProcessorByConnName(zConnName);
                    if ((mMartinLine != null) && (mMartinLine.getAlreadyLoggedIn()) && ((mMartinLine.getChChannel() != null))){
                        if (mMartinLine.getChChannel().sendMessageDirect(v.msgToString())){
                            //... .
                        }else{ //. gagal dikirim ke klien
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                    }else{ //. tidak bisa dikirim ke klien
                        //.???:
                        //. TODO : handle lewat Martin
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }
                break;
            case LimitSubscript:
                ConcurrentHashMap<Long, QRIDataTradingLimitMessage> mListLimit = BookOfMARTINTradingLimitList.getInstance.retrieveAllSheets();
                
                for (Map.Entry<Long, QRIDataTradingLimitMessage> entry : mListLimit.entrySet()) {
                    QRIDataTradingLimitMessage v  = entry.getValue();
                    v.setfConnName(zConnName);
                    MARTINSimCallbackProcessor mMartinLine = MARTINSimCallbackController.getInstance.getActiveChannelProcessorByConnName(zConnName);
                    if ((mMartinLine != null) && (mMartinLine.getAlreadyLoggedIn()) && ((mMartinLine.getChChannel() != null))){
                        if (mMartinLine.getChChannel().sendMessageDirect(v.msgToString())){
                            //... .
                        }else{ //. gagal dikirim ke klien
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                    }else{ //. tidak bisa dikirim ke klien
                        //.???:
                        //. TODO : handle lewat Martin
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }
                break;
            default:
                break;
        }
        
        return mOut;
    }
}
