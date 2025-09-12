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
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmendReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.util.StringUtil;
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
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINOrderList;
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
            //.????????????????????????????????????????????????????????????????????????????????/
            //.20250724: sementara remark untuk bebas order
            if (lSeqLatestReceived <= lSeqLatestSaved){ //. skip
                return;
            }
            
            long vOrderToken = mMessage.getOrderToken();
            
            ITMTradingServerReplyTimeOutMgr.getInstance.removeToken(mMessage.getOrderToken());
            
            if (vOrderToken > 0){
                
                SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                         
                SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                if (mCalcQty == null){
                    mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                    BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                }
                
                if ((mOriginRequest == null) && (mEveryRequest != null)){
                    mOriginRequest = new SheetOfJONECSimOriginRequest(vOrderToken, mEveryRequest.getIdxMessage());
                }
                
                if ((mOriginRequest != null) && (mEveryRequest != null)){
                    
                    if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){
                        
                        //.soon
                        if (ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_enable){
                            boolean bStillSendingRacing = ITMTradingServerRacingMgr.getInstance.isbStillSendingRacing();
                            if (bStillSendingRacing){
                                ORIDataNewOrder mCurrentOrderRacing = ITMTradingServerRacingMgr.getInstance.getCurrentOrderRacing();
                                if (mCurrentOrderRacing != null){
                                    String zBrokerRef = BookOfMARTINSimToken.getInstance.findBrokerRefByToken(vOrderToken);
                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk cek trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", mCurrentOrderRacing.getfClOrdID() = " + mCurrentOrderRacing.getfClOrdID());  
                                    if (!StringHelper.isNullOrEmpty(zBrokerRef)){
                                        //.soon
                                        if (mCurrentOrderRacing.getfClOrdID().equals(zBrokerRef.replaceAll("#",""))){
                                            //. cek, alasan reject nya, jika R (not allow) / H (not tradeable) maka ulangi
                                            switch (mMessage.getRejectCode()) {
                                                case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED:
                                                    //. catatan : menurut pengalaman ini juga bisa terjadi pada stock suspend/bei suspend
                                                    // anggap belum buka
                                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getRejectCode());
                                                    //. di ulangi lagi
                                                    ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                    
                                                    //. 2022-02-17 : jangan teruskan ke klien
                                                    return;
                                                case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME:
                                                    // anggap belum buka
                                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getRejectCode());
                                                    //. di ulangi lagi
                                                    ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                    //. 2022-02-17 : jangan teruskan ke klien
                                                    return;
                                                default:
                                                    // anggap sudah buka
                                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Dapat message OUCHMsgRejectedOrder untuk trigger doReleaseAllPendingOrder. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getRejectCode());
                                                    //. remove order pertama dan tarik sisa pending order lalu set semua flag jadi normal
                                                    ITMTradingServerRacingMgr.getInstance.doReleaseAllPendingOrder(true);
                                                    break;
                                            }
                                            
                                        }
                                    }else{
                                        //. kirim apa adanya saja
                                        //. cek, alasan reject nya, jika R (not allow) / H (not tradeable) maka ulangi
                                        switch (mMessage.getRejectCode()) {
                                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED:
                                                //. catatan : menurut pengalaman ini juga bisa terjadi pada stock suspend/bei suspend
                                                // anggap belum buka
                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getRejectCode());
                                                //. di ulangi lagi
                                                ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                //. 2022-02-17 : jangan teruskan ke klien
                                                return;
                                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME:
                                                // anggap belum buka
                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getRejectCode());
                                                //. di ulangi lagi
                                                ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                //. 2022-02-17 : jangan teruskan ke klien
                                                return;
                                            default:
                                                // anggap sudah buka
                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Dapat message OUCHMsgRejectedOrder untuk trigger doReleaseAllPendingOrder. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + mMessage.getRejectCode());
                                                //. remove order pertama dan tarik sisa pending order lalu set semua flag jadi normal
                                                ITMTradingServerRacingMgr.getInstance.doReleaseAllPendingOrder(true);
                                                break;
                                        }
                                    }
                                }
                            }else{
                                //. hrn : sedang tidak ada OrderRacing dari single Order, maka ulangi sampai ada flag accepted // by config jam
                                boolean bCurrMessageAccepted =  ITMTradingServerRacingMgr.getInstance.getbOrderMessageAccepted();
                                boolean bRejectShouldRetry = false;
                                if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED == mMessage.getRejectCode()){
                                    bRejectShouldRetry = true;
                                }else if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME == mMessage.getRejectCode()){
                                    bRejectShouldRetry = true;
                                }
                                if (bRejectShouldRetry){
                                    boolean inRetryRange = ITMTradingServerRacingMgr.getInstance.isTimeInRangeOrderRetry(false);
                                    // if (!bCurrMessageAccepted){ //. cara lama
                                    if (inRetryRange){ //. cara baru (pakai rentang waktu)
                                        //. ulangi dan jangan teruskan ke klien (TS)
                                        JONECSimWorkDataNewOrder.getInstance.doWork(null, (ORIDataNewOrder)mOriginRequest.getIdxMessage());
                                        return;
                                    }else{
                                        //. log, karena sudah datang accepted (cara baru: diluar range) tapi masih ada yang reject Non Tradeable (H)
                                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Order rejected (" + mMessage.getRejectCode() + "), after flag MessageAccepted set TRUE, order Token = " + vOrderToken + ", BrokerRef = " + ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID());
                                    }
                                }else{
                                    //. 2023-07-27 : req pak adry : lihat variable invalid_price_retry_time, berapa kali mau di retry ketika dapat invalid price                                    
//                                    if (OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_INVALID_PRICE.equalsIgnoreCase(mMessage.getReason())){
//                                        String zOriginBrokerRef = ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID();
//                                        boolean bEligible = ITMTradingServerRetryMgr.getInstance.checkIfEligibleToRetry(zOriginBrokerRef);
//                                        if (bEligible){ //. ulang send
//                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Order rejected (" + mMessage.getRejectCode() + "), eligible to rety send, order Token = " + vOrderToken + ", BrokerRef = " + ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID());
//                                            //. ulangi dan jangan teruskan ke klien (TS)
//                                            JONECSimWorkDataNewOrder.getInstance.doWork(null, (ORIDataNewOrder)mOriginRequest.getIdxMessage());
//                                            return;
//                                        }
//                                    }
                                }
                            }
                        }
                        
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
                        mReplyMsg.setfText(mMessage.getRejectDesc());
//                        switch (mMessage.getRejectCode()) {
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED:
//                                mReplyMsg.setfText("(" + mMessage.getRejectCode() + ")reason: Illegal transaction at this time");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_THROTTLING:
//                                mReplyMsg.setfText("(" + mMessage.getRejectCode() + ")reason: Throttling limit exceeded");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME:
//                                mReplyMsg.setfText("(" + mMessage.getRejectCode() + ")reason: Limit orders are not allowed in this session state");
//                                break;
//                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_PREMIUM:
//                                mReplyMsg.setfText("(" + mMessage.getRejectCode() + ")reason: The premium must be aligned at the price ticks for the given instrument");
//                                break;
//                            default:
//                                mReplyMsg.setfText("(" + mMessage.getRejectCode() + ")reason: default_unknown");
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
                        ORIDataOrderAmend mOriginRequestMsg = ((ORIDataOrderAmend)mOriginRequest.getIdxMessage());
                        
                        //. check jika dari amend
                            //. set status reject untuk token baru
                            mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_REJECTED);
                            //.backup:
                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);



                            //. urutan jika cancel dari reject-amend
                            //. 1. Kirim amend reply bad (broker ref baru)
                            //. 2. Kirim cancel broker ref lama ??xx
                            //. 3. update status OrderList Martin ??xx

                            //. zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

                            //.20250806: diouch sebelumnya reject amend datang dari message canceledOrder, diouch mme reply bad amend/amend reject didapat dari message rejectedOrder
                            //. Amend Reply Bad untuk broker ref baru
                            ORIDataOrderAmendReply mReplyAmendBadMsg = new ORIDataOrderAmendReply(new HashMap());
                            mReplyAmendBadMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                            mReplyAmendBadMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                            mReplyAmendBadMsg.setfOrderAmendReplyType(ORIDataOrderAmendReply.ORIOrderAmendReplyType.BAD);                        
                            mReplyAmendBadMsg.setfOrderID("000000000000");
                            mReplyAmendBadMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                            mReplyAmendBadMsg.setfOrigClOrdID(" ");
                            mReplyAmendBadMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
                            mReplyAmendBadMsg.setfCxlRejResponseTo(2);
                            mReplyAmendBadMsg.setfText(mMessage.getRejectDesc());
                            
                            //. 2021-06-15 : proses ini di batalkan (di aktifkan kembali karena dari itch dapat cancelled)
//                            ORIDataOrderCancelReply mReplyMsg = new ORIDataOrderCancelReply(new HashMap());
//                            mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
//                            mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
//
//                            //. semua response
//                            mReplyMsg.setfOrderCancelReplyType(ORIDataOrderCancelReply.ORIOrderCancelReplyType.OK);                        
//                            mReplyMsg.setfOrderID(mOriginRequestMsg.getfOrderID());                        
//                            //. untuk cancelled ouch dari message amend, yang di cancel adalah broker ref yang ori nya
//                            mReplyMsg.setfExecRefID(mOriginRequestMsg.getfOrigClOrdID());
//
//                            mReplyMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
//                            mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_CANCEL);
//                            mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_CANCELLED);
//                            mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_CANCELLED);
//                            mReplyMsg.setfSymbol(" ");
//                            mReplyMsg.setfSide(" ");
//                            mReplyMsg.setfLeavesQty(0);
//                            mReplyMsg.setfCumQty(0);
//                            mReplyMsg.setfAvgPx(0);
//                            mReplyMsg.setfHandlInst(ORIDataConst.ORIFieldValue.HANDLINST_NORMAL);
//                            mReplyMsg.setfText("");
//                            mReplyMsg.setfLastPx(0);
//                            mReplyMsg.setfLastShares(0);

                            //. kirim order amend-reply-bad untuk broker-ref baru




                            JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyAmendBadMsg.getfBundleConnectionName());
                            if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                                if (    
                                        mClientLine.getChChannel().sendMessageDirect(mReplyAmendBadMsg.msgToString()) 
//                                        &&
//                                        mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())


                                        ){
                                    //... .
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


                            //.Orderlist (QRI)
//                            QRIDataOrderListMessage mOriOrderList = BookOfMARTINOrderList.getInstance.retrieveSheet(StringUtil.toLong(mOriginRequestMsg.getfOrderID()));
//
//                            if (mOriOrderList != null){
//                                //. update order status order origin jadi cancel (karena di ouch order reject dari amend statusnya sudah berhenti)
//                                mOriOrderList.setfOrdStatus(StringHelper.fromInt(QRIDataConst.QRIFieldValue.ORDSTATUS_CANCELLED));
//                                //. update exec id
//                                mOriOrderList.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
//                                
//                                //. save orderlist ke memory martin
//                                BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOriOrderList);
//                                //. broadcast old orderlist via martin
//                                BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOriOrderList);
//                            }else{
//                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "JatsOrderNo @" + mOriginRequestMsg.getfOrderID() + " not found in BookOfMARTINOrderList");
//                            }
//                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel){
                        
                        // 20231004:Ardi - Untuk order cancel dapat reply H dari bursa (OUCH Reject Message)
                        // perlu dikirimka ke TS agar dapat memberi tahu user via msg box ke client
                        
                        //.20250806: mCalcQty dapat dari atas
//                        SheetOfJONECSimCalcQty mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
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
