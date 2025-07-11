/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.msgmem.works;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrderReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.consts.OUCHConsts;
import com.itm.ts.ouch.structs.OUCHMsgOrderRejected;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackProcessor;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataNewOrder;
import com.itm.xtream.inet.trading.martin.server.books.BookOfMARTINSimToken;
import com.itm.xtream.inet.trading.racing.mgr.ITMTradingServerRacingMgr;
import com.itm.xtream.inet.trading.racing.retry.mgr.ITMTradingServerRetryMgr;
import com.itm.xtream.inet.trading.replytimeout.mgr.ITMTradingServerReplyTimeOutMgr;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class JONECSimMsgMemWorkRejectedOrder {
    //.single instance:
    public final static JONECSimMsgMemWorkRejectedOrder getInstance = new JONECSimMsgMemWorkRejectedOrder();
    
    public JONECSimMsgMemWorkRejectedOrder() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(SheetOfOUCHBase mSheet, OUCHMsgOrderRejected mMessage){
        try{
            long lSeqLatestSaved = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestSaved();
            long lSeqLatestReceived = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestReceived();
            if (lSeqLatestReceived <= lSeqLatestSaved){ //. skip
                return;
            }
            
            long vOrderToken = mMessage.getOrderToken();
            
            ITMTradingServerReplyTimeOutMgr.getInstance.removeToken(mMessage.getOrderToken());
            
            if (vOrderToken > 0){
                
                SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                         
                if ((mOriginRequest != null) && (mEveryRequest != null)){
                    
                    if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){
                        
                        //.soon
//                        if (ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_enable){
//                            boolean bStillSendingRacing = ITMTradingServerRacingMgr.getInstance.isbStillSendingRacing();
//                            if (bStillSendingRacing){
//                                ORIDataNewOrder mCurrentOrderRacing = ITMTradingServerRacingMgr.getInstance.getCurrentOrderRacing();
//                                if (mCurrentOrderRacing != null){
//                                    String zBrokerRef = BookOfMARTINSimToken.getInstance.findBrokerRefByToken(vOrderToken);
//                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk cek trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", mCurrentOrderRacing.getfClOrdID() = " + mCurrentOrderRacing.getfClOrdID());  
//                                    if (!StringHelper.isNullOrEmpty(zBrokerRef)){
//                                        //.soon
//                                        if (mCurrentOrderRacing.getfClOrdID().equals(zBrokerRef.replaceAll("#",""))){
//                                            //. cek, alasan reject nya, jika R (not allow) / H (not tradeable) maka ulangi
//                                            if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_INSTRUMENT_BOARD_MARKET_NOT_TRADEABLE.equalsIgnoreCase(mMessage.getRejectCode())){
//                                                //. catatan : menurut pengalaman ini juga bisa terjadi pada stock suspend/bei suspend
//                                                // anggap belum buka
//                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getReason());                                                
//                                                //. di ulangi lagi                                                
//                                                ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
//                                                
//                                                //. 2022-02-17 : jangan teruskan ke klien
//                                                return;
//                                            }else if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDER_NOT_ALLOWED_THIS_TIME.equalsIgnoreCase(mMessage.getReason())){
//                                                // anggap belum buka
//                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getReason());                                                                                                
//                                                //. di ulangi lagi
//                                                ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
//                                                //. 2022-02-17 : jangan teruskan ke klien
//                                                return;
//                                            }else{
//                                                // anggap sudah buka
//                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Dapat message OUCHMsgRejectedOrder untuk trigger doReleaseAllPendingOrder. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getReason());
//                                                //. remove order pertama dan tarik sisa pending order lalu set semua flag jadi normal
//                                                ITMTradingServerRacingMgr.getInstance.doReleaseAllPendingOrder(true);
//                                            }
//                                            
//                                        }
//                                    }else{
//                                        //. kirim apa adanya saja
//                                        //. cek, alasan reject nya, jika R (not allow) / H (not tradeable) maka ulangi
//                                        if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_INSTRUMENT_BOARD_MARKET_NOT_TRADEABLE.equalsIgnoreCase(mMessage.getReason())){
//                                            //. catatan : menurut pengalaman ini juga bisa terjadi pada stock suspend/bei suspend
//                                            // anggap belum buka
//                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getReason());                                                
//                                            //. di ulangi lagi                                                
//                                            ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
//                                            //. 2022-02-17 : jangan teruskan ke klien
//                                            return;
//                                        }else if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDER_NOT_ALLOWED_THIS_TIME.equalsIgnoreCase(mMessage.getReason())){
//                                            // anggap belum buka
//                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getReason());                                                                                                
//                                            //. di ulangi lagi
//                                            ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
//                                            //. 2022-02-17 : jangan teruskan ke klien
//                                            return;
//                                        }else{
//                                            // anggap sudah buka
//                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Dapat message OUCHMsgRejectedOrder untuk trigger doReleaseAllPendingOrder. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getReason());
//                                            //. remove order pertama dan tarik sisa pending order lalu set semua flag jadi normal
//                                            ITMTradingServerRacingMgr.getInstance.doReleaseAllPendingOrder(true);
//                                        }
//                                    }
//                                }
//                            }else{
//                                //. hrn : sedang tidak ada OrderRacing dari single Order, maka ulangi sampai ada flag accepted // by config jam
//                                boolean bCurrMessageAccepted =  ITMTradingServerRacingMgr.getInstance.getbOrderMessageAccepted();
//                                boolean bRejectShouldRetry = false;
//                                if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_INSTRUMENT_BOARD_MARKET_NOT_TRADEABLE.equalsIgnoreCase(mMessage.getReason())){
//                                    bRejectShouldRetry = true;
//                                }else if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDER_NOT_ALLOWED_THIS_TIME.equalsIgnoreCase(mMessage.getReason())){
//                                    bRejectShouldRetry = true;
//                                }
//                                if (bRejectShouldRetry){
//                                    boolean inRetryRange = ITMTradingServerRacingMgr.getInstance.isTimeInRangeOrderRetry(false);
//                                    // if (!bCurrMessageAccepted){ //. cara lama
//                                    if (inRetryRange){ //. cara baru (pakai rentang waktu)
//                                        //. ulangi dan jangan teruskan ke klien (TS)
//                                        JONECSimWorkDataNewOrder.getInstance.doWork(null, (ORIDataNewOrder)mOriginRequest.getIdxMessage());
//                                        return;
//                                    }else{
//                                        //. log, karena sudah datang accepted (cara baru: diluar range) tapi masih ada yang reject Non Tradeable (H)
//                                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Order rejected (" + mMessage.getReason() + "), after flag MessageAccepted set TRUE, order Token = " + vOrderToken + ", BrokerRef = " + ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID());
//                                    }
//                                }else{
//                                    //. 2023-07-27 : req pak adry : lihat variable invalid_price_retry_time, berapa kali mau di retry ketika dapat invalid price                                    
//                                    if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_INVALID_PRICE.equalsIgnoreCase(mMessage.getReason())){
//                                        String zOriginBrokerRef = ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID();
//                                        boolean bEligible = ITMTradingServerRetryMgr.getInstance.checkIfEligibleToRetry(zOriginBrokerRef);
//                                        if (bEligible){ //. ulang send
//                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Order rejected (" + mMessage.getReason() + "), eligible to rety send, order Token = " + vOrderToken + ", BrokerRef = " + ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID());
//                                            //. ulangi dan jangan teruskan ke klien (TS)
//                                            JONECSimWorkDataNewOrder.getInstance.doWork(null, (ORIDataNewOrder)mOriginRequest.getIdxMessage());
//                                            return;
//                                        }
//                                    }
//                                }
//                            }
//                        }
                        
                        //.soon
//                        if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_INVALID_PRICE.equalsIgnoreCase(mMessage.getReason())){
//                            String zOriginBrokerRef = ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID();
//                            boolean bEligible = ITMTradingServerRetryMgr.getInstance.checkIfEligibleToRetry(zOriginBrokerRef);
//                            if (bEligible){ //. ulang send
//                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Order rejected (" + mMessage.getReason() + "), eligible to rety send, order Token = " + vOrderToken + ", BrokerRef = " + ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID());
//                                //. ulangi dan jangan teruskan ke klien (TS)
//                                JONECSimWorkDataNewOrder.getInstance.doWork(null, (ORIDataNewOrder)mOriginRequest.getIdxMessage());
//                                return;
//                            }
//                        }
                        ORIDataNewOrder mOriginRequestMsg = ((ORIDataNewOrder)mOriginRequest.getIdxMessage());
                        
                        ORIDataNewOrderReply mReplyMsg = new ORIDataNewOrderReply(new HashMap());
                        mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                        mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                        mReplyMsg.setfNewOrderReplyType(ORIDataNewOrderReply.ORINewOrderReplyType.BAD);
                        
                        mReplyMsg.setfOrderID(ORIDataConst.ORIFieldValue.ORDERID_NO_JATS_ORDERNUMBER);
                        mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                        mReplyMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
                        mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
                        mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_REJECTED);
                        mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
                        mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                        mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                        mReplyMsg.setfLeavesQty(0);
                        mReplyMsg.setfCumQty(0);
                        mReplyMsg.setfAvgPx(0);
                        mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                        //.soon
//                        switch (mMessage.getReason()) {
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_INSTRUMENT_BOARD_MARKET_NOT_TRADEABLE:
//                                mReplyMsg.setfText("(293)" + "(" + mMessage.getReason() + ")reason: order book instrument board market not tradeable");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_INVALID_QUANTITY_OR_EXCEEDED:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: invalid quantity or exceeded");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_INVALID_ORDERBOOK_IDENTIFIER:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: invalid order book identifier");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDER_NOT_ALLOWED_THIS_TIME:
//                                mReplyMsg.setfText("(293)" + "(" + mMessage.getReason() + ")reason: order not allowed this time");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_INVALID_PRICE:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: invalid price");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_INVALID_ORDER_TYPE:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: invalid order type");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_FLOW_CONTROL_IN_PLACE_FOR_USER:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: flow control in place for user");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDER_SOURCE_NOT_VALID:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: order source not valid");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDER_EXCEEDED_FIRM_TRADING_LIMIT:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: order exceeded firm trading limit");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_UNKNOWN:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: unknown");
//                                break;
//                            //.tambahan Ardi:20230919
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_INVALID_MINIMUM_QUANTITY:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: order invalid minimum quantity");
//                                break;
//                            default:
//                                mReplyMsg.setfText("(" + mMessage.getReason() + ")reason: default_unknown");
//                                break;
//                        }
                        mReplyMsg.setfLastPx(0);
                        mReplyMsg.setfLastShares(0);

                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                //... .
                            }else{
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            }
                        }else{
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }

                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend){
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel){
                        
                        // 20231004:Ardi - Untuk order cancel dapat reply H dari bursa (OUCH Reject Message)
                        // perlu dikirimka ke TS agar dapat memberi tahu user via msg box ke client
                        
                        SheetOfJONECSimCalcQty mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                        ORIDataOrderCancel mOriginRequestMsg = ((ORIDataOrderCancel)mOriginRequest.getIdxMessage());

                        //. note 20210702 : mungkin terjadi jika ada OUCHMsgCancelled dari BEI, tanpa kita ngirim witdhraw
                        //ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "OUCHMsgCancelled from ORIDataNewOrder, BrokerRef/getfClOrdID = " + mOriginRequestMsg.getfClOrdID() + ", mCalcQty.getJatsOrderNo = " + mCalcQty.getJatsOrderNo() + ", mCalcQty.getOriJatsOrderNo() = " + mCalcQty.getOriJatsOrderNo());

                        //. tidak perlu set status cancel
//                        mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_CANCELLED);
                        //.backup:
                        BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);

                        ORIDataOrderCancelReply mReplyMsg = new ORIDataOrderCancelReply(new HashMap());
                        mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                        mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());

                        //. semua response
                        mReplyMsg.setfOrderCancelReplyType(ORIDataOrderCancelReply.ORIOrderCancelReplyType.OK);

//                        mReplyMsg.setfOrderID(mCalcQty.getJatsOrderNo());
                        mReplyMsg.setfOrderID(mOriginRequestMsg.getfOrderID());
                        
                        mReplyMsg.setfExecRefID(mOriginRequestMsg.getfClOrdID());
                        mReplyMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
                        mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_CANCEL);
                        mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_CANCELLED);
                        mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_CANCELLED);
                        mReplyMsg.setfSymbol(" ");
                        mReplyMsg.setfSide(" ");
                        mReplyMsg.setfLeavesQty(0);
                        mReplyMsg.setfCumQty(0);
                        mReplyMsg.setfAvgPx(0);
                        mReplyMsg.setfHandlInst(ORIDataConst.ORIFieldValue.HANDLINST_NORMAL);
                        mReplyMsg.setfText("Securities cannot be canceled at this time.");
                        mReplyMsg.setfLastPx(0);
                        mReplyMsg.setfLastShares(0);
                        
//////                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ODD, logLevel.ERROR, "zzzz order token: " + vOrderToken);
//////                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ODD, logLevel.ERROR, "zzzz order id (from calc qty): " + mCalcQty.getJatsOrderNo());
//////                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ODD, logLevel.ERROR, "zzzz order id (from mOriginRequestMsg): " + mOriginRequestMsg.getfOrderID());
//////                      
//////                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ODD, logLevel.ERROR, "zzzz asumsi dapat reply H setelah melakukan order cancel.");

                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
//////                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ODD, logLevel.ERROR, "zzzz cek channel apakah kosong.");
                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                //... .
//////                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.ODD, logLevel.ERROR, "zzzz kirim ke channel (TG) berhasil.");
                            }else{
                                //.???:
                                //. TODO : handle lewat Martin
                                //. masukin log
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            }
                        }else{
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                        // --------------------
                        //.???:
//                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                    }else{
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }else{
                    //.???:
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                }
                
            }else{
                //.???:
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }

}
