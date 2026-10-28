/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataExecutionReport;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValue;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrderReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmendReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.consts.QRIDataConst.QRIFieldValue;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataTradeListMessage;
import com.itm.idx.data.qri.util.StringUtil;
import com.itm.ts.ouch.consts.OUCHConsts;
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
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.helper.QRIOrderListPartialSummaryWorker;
import com.itm.xtream.inet.trading.martin.server.books.BookOfMARTINSimToken;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINOrderList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINTradeList;
import com.itm.xtream.inet.trading.racing.mgr.ITMTradingServerRacingMgr;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataExecutionReport {
    //.single instance:
    public final static FIX5JonecWorkDataExecutionReport getInstance = new FIX5JonecWorkDataExecutionReport();
    
    public FIX5JonecWorkDataExecutionReport() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    //. uuuuuuuuuuu
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataExecutionReport mInputMsgRequest){
        try{
            System.out.println("FIX5JonecWorkDataExecutionReport.doWork");
//            System.out.println(mInputMsgRequest.msgDataToString());
            if (controller != null){
                
                long vOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfClOrdID());
                long vPrevOrderToken = 0;
                
                //. check if amend
                if (mInputMsgRequest.getfOrigClOrdID() != null && !mInputMsgRequest.getfOrigClOrdID().equalsIgnoreCase("")){
                    //. amend / cancel
                    vPrevOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfOrigClOrdID());
                }
//                System.out.println("vOrderToken = " + vOrderToken);
//                System.out.println("vPrevOrderToken = " + vPrevOrderToken);
                
                        
                SheetOfJONECSimOriginRequest mOriginRequest = null;
                SheetOfJONECSimEveryRequest mEveryRequest = null;
//                System.out.println("mInputMsgRequest.getfClOrdID() = " + mInputMsgRequest.getfClOrdID());
//                System.out.println("mInputMsgRequest.getfOrigClOrdID() = " + mInputMsgRequest.getfOrigClOrdID());
//                System.out.println("vOrderToken = " + vOrderToken);
                
                if (vOrderToken > 0 && vPrevOrderToken <= 0){
                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                    mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                }else if (vOrderToken > 0 && vPrevOrderToken > 0){  
                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vPrevOrderToken);
                    mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                }
                //.??????
                if ((mOriginRequest == null) && (mEveryRequest != null)){
                    mOriginRequest = new SheetOfJONECSimOriginRequest(vOrderToken, mEveryRequest.getIdxMessage());
                }
                
//                System.out.println("mOriginRequest = " + mOriginRequest);
//                System.out.println("mEveryRequest = " + mEveryRequest);
//                System.out.println("mOriginRequest = " + ((mOriginRequest != null) ? mOriginRequest.getIdxMessage() : null));
                
                if ((vOrderToken > 0) && (mOriginRequest != null) && (mEveryRequest != null)){
                    if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){
                        System.out.println("mOriginRequest = mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder");
                        ORIDataNewOrder mOriginRequestMsg = ((ORIDataNewOrder)mOriginRequest.getIdxMessage());
                        switch (mOriginRequestMsg.getfHandlInst()) {
                            case ORIDataConst.ORIFieldValue.HANDLINST_NORMAL:
                                {
                                    if (mInputMsgRequest.getfExecType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.EXECTYPE_TRADE)) {
                                        //.order match
                                        //. flag apakah parsial
                                        boolean isPartial = false;
                                        
                                        SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                                        if (mCalcQty == null){
                                            mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                                        }

                                        //. zzz jika sudah full skipp / jatsNo nya empty skkip / LastJatsOrderNo pernah di proses
                                        if (mCalcQty.getOrderStatus() == QRIDataConst.QRIFieldValue.ORDSTATUS_FULLY_MATCH 
                                                || "".equals(mCalcQty.getJatsOrderNo()) 
                                                || mCalcQty.getLastJatsTradeNo() >= StringHelper.toLong(mInputMsgRequest.getfTrdMatchID())){
//                                            System.err.println("FIX5MsgExecutionReport mCalcQty.getLastJatsTradeNo() = " + mCalcQty.getLastJatsTradeNo() + ", mMessage.getMatchNumber() = " + StringHelper.toLong(mInputMsgRequest.getfTrdMatchID())+ ", mCalcQty.getJatsOrderNo() = " + mCalcQty.getJatsOrderNo());
                                            return;
                                        }

                                        //. tambah match
                                        mCalcQty.addQtyMatch(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                                        //. set LastJastNo
                                        mCalcQty.setLastJatsTradeNo(StringHelper.toLong(mInputMsgRequest.getfTrdMatchID()));
                                        //. kalkulasi status

                //                        if (mCalcQty.getQtyLeave() >= mCalcQty.getQtyMatch()){
                //                            mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_NEW);
                //                        }else 

                                        if (mCalcQty.getQtyLeave() > 0){
                                            isPartial = true;
                                            mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_PARTIALLY_MATCH);
                                        }else{
                                            mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_FULLY_MATCH);
                                        }

                                        //. OrderList dengan kalkulasi
                                        QRIDataOrderListMessage mOrderListMsg = new QRIDataOrderListMessage(new HashMap());
                                        mOrderListMsg.setfOrderID(StringHelper.toLong(mCalcQty.getJatsOrderNo()));
                                        mOrderListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                                        mOrderListMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                        mOrderListMsg.setfOrigClOrdID(StringUtil.toLong(mCalcQty.getOriJatsOrderNo()));
                                        mOrderListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                                        mOrderListMsg.setfExecBroker("");
                                        mOrderListMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                        mOrderListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                                        mOrderListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                                        mOrderListMsg.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                                        mOrderListMsg.setfAccount(mOriginRequestMsg.getfAccount());
                                        mOrderListMsg.setfFutSettDate(""); //. sementara di kasih empty, karena tidak ketemu dipakai dimana
                                        mOrderListMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                        mOrderListMsg.setfSymbolSfx(mOriginRequestMsg.getfSymbolSfx());
                                        mOrderListMsg.setfSecurityID(mOriginRequestMsg.getfSecurityID());
                                        mOrderListMsg.setfSide(mOriginRequestMsg.getfSide());
                                        mOrderListMsg.setfOrderQty(mCalcQty.getQtyOrder());
                                        mOrderListMsg.setfOrderType(StringUtil.toInteger(mOriginRequestMsg.getfOrdType()));
                                        mOrderListMsg.setfPrice(mOriginRequestMsg.getfPrice());
                                        //.20260402: jika ada harga dari new order, maka update harganya (untuk khasus market to limit)
                                        if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_MARKET_TO_LIMIT)
                                                || mOriginRequestMsg.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_MARKET_TO_LIMIT)) {
                                            if (StringUtil.toLong(mInputMsgRequest.getfLastPx()) > 0) {
                                                mOrderListMsg.setfPrice(StringUtil.toLong(mInputMsgRequest.getfLastPx()));
                                            } else {
                                                mOrderListMsg.setfPrice(mOriginRequestMsg.getfPrice());
                                            }
                                        } 
                                        mOrderListMsg.setfTimeInForce(mOriginRequestMsg.getfTimeInForce());
                                        mOrderListMsg.setfExpiredDate(mOriginRequestMsg.getfExpireDate());
                                        mOrderListMsg.setfExecInst(mOriginRequestMsg.getfExecInst());
                                        mOrderListMsg.setfLeavesQty(mCalcQty.getQtyLeave());
                                        mOrderListMsg.setfCumQty(mCalcQty.getQtyMatch());
                                        mOrderListMsg.setfAvgPx(0);
                                        mOrderListMsg.setfTradeDate(DateTimeHelper.getDateSVRTRXFormatFromDate(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                                        mOrderListMsg.setfTransactionTime(DateTimeHelper.getDateSVRTRXFormatFromDate(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                                        mOrderListMsg.setfText("");
                                        mOrderListMsg.setfClearingAccount("");
                                        mOrderListMsg.setfComplianceID(mOriginRequestMsg.getfComplianceID());                        

                                        if (isPartial){//. jika parsial ditahan dulu, lempar ke class
                                            QRIOrderListPartialSummaryWorker.getInstance.addData(mOrderListMsg);
                                        }else{
                                            //.pastikan di remove dari pendingan message di QRIOrderListPartialSummaryWorker
                                            QRIOrderListPartialSummaryWorker.getInstance.removeData(mOrderListMsg.getfOrderID());

                                            //. save orderlist ke memory martin
                                            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOrderListMsg);

                                            //. hrn-20211221 : untuk antisipasi order yang langsung match (overide dengan OrderAccepted), 
                                            //. untuk full match di tahan 50ms
                                            final QRIDataOrderListMessage mFnlOrderListMsg = mOrderListMsg;
                                            new Timer().schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    //. broadcast orderlist via martin
                                                    BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mFnlOrderListMsg);
                                                }
                                            }, 50); 

                                        }
                                        //. Tradelist
                                        QRIDataTradeListMessage mTradeListMsg = new QRIDataTradeListMessage(new HashMap());
                                        mTradeListMsg.setfOrderID(StringHelper.toLong(mCalcQty.getJatsOrderNo()));
                                        mTradeListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                                        mTradeListMsg.setfSecondaryOrderID(StringHelper.toLong(mInputMsgRequest.getfTrdMatchID()));
                                        mTradeListMsg.setfTransactionTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                        mTradeListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                        mTradeListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                                        mTradeListMsg.setfSide(mOriginRequestMsg.getfSide());
                                        mTradeListMsg.setfExecBroker("");
                                        mTradeListMsg.setfContraTrader("");
                                        //.soon
                //                        long lCounterPartID =  mMessage.getCounterPartyID(); //. ?? to broker code
                                        String lCounterPartCode = "";
                                        //.soon
                //                        SheetOfITCHParticipantDirectory mSheetParticipant = BookOfITCHParticipantDirectory.getInstance.retrieveSheet(lCounterPartID);
                //                        if (mSheetParticipant != null){
                //                            lCounterPartCode = mSheetParticipant.getMessage().getParticipantCode();
                //                        }
                                        mTradeListMsg.setfContraBroker(lCounterPartCode);
                                        mTradeListMsg.setfAccount(mOriginRequestMsg.getfAccount());
                                        mTradeListMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                        mTradeListMsg.setfSymbolSfx(mOriginRequestMsg.getfSymbolSfx());
                                        mTradeListMsg.setfSecurityID(mOriginRequestMsg.getfSecurityID());
                                        mTradeListMsg.setfPrice(StringHelper.toDouble(mInputMsgRequest.getfLastPx()));
                                        mTradeListMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                                        mTradeListMsg.setfText("");
                                        mTradeListMsg.setfClearingAccount(" ");
                                        mTradeListMsg.setfFutSettDate("");
                                        mTradeListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NORMAL_MATCH);
                                        mTradeListMsg.setfLastPx(0);
                                        mTradeListMsg.setfNoContraBrokers(1);                        
                                        mTradeListMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                        mTradeListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                                        mTradeListMsg.setfLeavesQty(0);
                                        mTradeListMsg.setfAvgPx(0);
                                        mTradeListMsg.setfComplianceID(mOriginRequestMsg.getfComplianceID());
                                        //. order status
                                        mTradeListMsg.setfOrdStatus("2");
                                        //. save tradelist ke memory martin
                                        BookOfMARTINTradeList.getInstance.addOrUpdateSheet(mTradeListMsg);
                                        //. broadcast tradelist via martin
                                        BookOfMARTINTradeList.getInstance.brodcastToSubscriber(mTradeListMsg);
                                        //.backup:
                                        BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);

                                    } else if (mInputMsgRequest.getfExecType().equalsIgnoreCase(StringHelper.fromInt(ORIDataConst.ORIFieldValue.EXECTYPE_CANCELLED))) {
                                        //.order canceled
                                        SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                                        if (mCalcQty == null){
                                            mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                                        } else {
                                            //.20260416: pastikan dulu order statusnya belum cancel agar tidak mengirim status cancel lebih dari 1 kali
                                            if (mCalcQty.getOrderStatus() == QRIFieldValue.ORDSTATUS_CANCELLED) {
                                                return;
                                            }
                                        }
                                        //. set status cancel
                                        mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_CANCELLED);
                                        //.backup:
                                        BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);

                                        ORIDataOrderCancelReply mReplyMsg = new ORIDataOrderCancelReply(new HashMap());
                                        mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                        mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                        //. semua response
                                        mReplyMsg.setfOrderCancelReplyType(ORIDataOrderCancelReply.ORIOrderCancelReplyType.OK);
                                        mReplyMsg.setfOrderID(mInputMsgRequest.getfOrderID());              
                                        mReplyMsg.setfExecRefID(mInputMsgRequest.getfClOrdID());
                                        mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                        mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_CANCEL);
                                        mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_CANCELLED);
                                        mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_CANCELLED);
                                        mReplyMsg.setfSymbol(" ");
                                        mReplyMsg.setfSide(" ");
                                        mReplyMsg.setfLeavesQty(0);
                                        mReplyMsg.setfCumQty(0);
                                        mReplyMsg.setfAvgPx(0);
                                        //////mReplyMsg.setfHandlInst(ORIFieldValue.HANDLINST_NORMAL);
                                        mReplyMsg.setfHandlInst(mOriginRequestMsg.getfSymbolSfx().equals(ORIFieldValue.BOARD_NG) ? ORIFieldValue.HANDLINST_ADVERTISEMENT : ORIFieldValue.HANDLINST_NORMAL);
                                        mReplyMsg.setfText("");
                                        mReplyMsg.setfLastPx(0);
                                        mReplyMsg.setfLastShares(0);

                                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                                //... .
                                            }else{
                                                //.???:
                                                //. TODO : handle lewat Martin
                                                //. masukin log
                                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                                            }
                                        }else{
                                            //.???:
                                            //. TODO : handle lewat Martin
                                            //. masukin log
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                                        }

                                        //.Orderlist (QRI)
                                        QRIDataOrderListMessage mOriOrderList = BookOfMARTINOrderList.getInstance.retrieveSheet(StringUtil.toLong(mInputMsgRequest.getfOrderID()));

                                        if (mOriOrderList != null){
                                            //. update order status
                                            mOriOrderList.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                                            //. update exec id
                                            mOriOrderList.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));

                                            //. save orderlist ke memory martin
                                            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOriOrderList);
                                            //. broadcast old orderlist via martin
                                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOriOrderList);
                                        }else{
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "JatsOrderNo @" + mInputMsgRequest.getfOrderID() + " not found in BookOfMARTINOrderList");
                                        }
                                    } else if (mInputMsgRequest.getfExecType().equalsIgnoreCase(StringHelper.fromInt(ORIDataConst.ORIFieldValue.EXECTYPE_REJECTED))) {
                                        //.order rejected
                                        SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                                        if (mCalcQty == null){
                                            mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                                        }
                                        //.20260407: untuk handle retry order yang reject
                                        int iRejectCode = 0;
                                        //.menggunakan cara ini untuk mengambil reject code dengan catatan formatnya selalu ([-420131] reject desc)
                                        int start = mInputMsgRequest.getfRejectText().indexOf('[') + 1;
                                        int end = mInputMsgRequest.getfRejectText().indexOf(']');
                                        
                                        if (start != -1 && end != -1 && end > start) { 
                                            iRejectCode = Integer.parseInt(mInputMsgRequest.getfRejectText().substring(start, end));
                                        } 
                                        
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
                                                            switch (iRejectCode) {
                                                                case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED:
                                                                    //. catatan : menurut pengalaman ini juga bisa terjadi pada stock suspend/bei suspend
                                                                    // anggap belum buka
                                                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + iRejectCode);
                                                                    //. di ulangi lagi
                                                                    ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();

                                                                    //. 2022-02-17 : jangan teruskan ke klien
                                                                    return;
                                                                case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME:
                                                                    // anggap belum buka
                                                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + iRejectCode);
                                                                    //. di ulangi lagi
                                                                    ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                                    //. 2022-02-17 : jangan teruskan ke klien
                                                                    return;
                                                                case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_THROTTLING: //.20260504: ketika dapat throttling, kirim ulang juga
                                                                    // anggap belum buka
                                                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + iRejectCode);
                                                                    //. di ulangi lagi
                                                                    ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                                    //. 2022-02-17 : jangan teruskan ke klien
                                                                    return;
                                                                default:
                                                                    // anggap sudah buka
                                                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Dapat message OUCHMsgRejectedOrder untuk trigger doReleaseAllPendingOrder. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + iRejectCode);
                                                                    //. remove order pertama dan tarik sisa pending order lalu set semua flag jadi normal
                                                                    ITMTradingServerRacingMgr.getInstance.doReleaseAllPendingOrder(true);
                                                                    break;
                                                            }

                                                        }
                                                    }else{
                                                        //. kirim apa adanya saja
                                                        //. cek, alasan reject nya, jika R (not allow) / H (not tradeable) maka ulangi
                                                        switch (iRejectCode) {
                                                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED:
                                                                //. catatan : menurut pengalaman ini juga bisa terjadi pada stock suspend/bei suspend
                                                                // anggap belum buka
                                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + iRejectCode);
                                                                //. di ulangi lagi
                                                                ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                                //. 2022-02-17 : jangan teruskan ke klien
                                                                return;
                                                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME:
                                                                // anggap belum buka
                                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + iRejectCode);
                                                                //. di ulangi lagi
                                                                ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                                //. 2022-02-17 : jangan teruskan ke klien
                                                                return;
                                                            case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_THROTTLING: //.20260504: ketika dapat throttling, kirim ulang juga
                                                                // anggap belum buka
                                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Dapat message OUCHMsgRejectedOrder untuk mengulangi trigger doSendingFirstOrderRacing. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + iRejectCode);
                                                                //. di ulangi lagi
                                                                ITMTradingServerRacingMgr.getInstance.doSendingFirstOrderRacing();
                                                                //. 2022-02-17 : jangan teruskan ke klien
                                                                return;
                                                            default:
                                                                // anggap sudah buka
                                                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Dapat message OUCHMsgRejectedOrder untuk trigger doReleaseAllPendingOrder. BrokerRef = " + zBrokerRef + ", OrderToken = " + vOrderToken + ", RejectReason = " + iRejectCode);
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
                                                switch (iRejectCode) {
                                                    case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED:
                                                        bRejectShouldRetry = true;
                                                        break;
                                                    case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME:
                                                        bRejectShouldRetry = true;
                                                        break;
                                                    case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_ME_MATCH_SST_MARKET_ORD:
                                                        bRejectShouldRetry = true;
                                                        break;
                                                    case OUCHConsts.OUCHValue.REJECTED_ORDER_REASON_OUCH_THROTTLING: //.20260504: ketika dapat throttling, kirim ulang juga
                                                        bRejectShouldRetry = true;
                                                        break;
                                                    default:
                                                        break;
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
                                                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Order rejected (" + iRejectCode + "), after flag MessageAccepted set TRUE, order Token = " + vOrderToken + ", BrokerRef = " + ((ORIDataNewOrder)mOriginRequest.getIdxMessage()).getfClOrdID());
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

                                        ORIDataNewOrderReply mReplyMsg = new ORIDataNewOrderReply(new HashMap());
                                        mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                        mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                        mReplyMsg.setfNewOrderReplyType(ORIDataNewOrderReply.ORINewOrderReplyType.BAD);
                                        mReplyMsg.setfOrderID(ORIDataConst.ORIFieldValue.ORDERID_NO_JATS_ORDERNUMBER);
                                        mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                                        mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                        mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
                                        mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_REJECTED);
                                        mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
                                        mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                        mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                                        mReplyMsg.setfLeavesQty(0);
                                        mReplyMsg.setfCumQty(0);
                                        mReplyMsg.setfAvgPx(0);
                                        mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                        mReplyMsg.setfText(mInputMsgRequest.getfRejectText());
                                        mReplyMsg.setfLastPx(0);
                                        mReplyMsg.setfLastShares(0);

                                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
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
                                    } else {
                                        //. response OK
                                        SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                                        if (mCalcQty == null){
                                            mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                                        } else {
                                            //.20260416: dengan asumsi order open itu adalah status pertama kali, jadi jika sudah ada memorinya (order open sebelumnya) tidak perlu kirim ulang
                                            //.ini dikarenakan adanya OE1, OE2, JDA yang mengirimkan message masing2
                                            return; 
                                        }
                                        ORIDataNewOrderReply mReplyMsg = new ORIDataNewOrderReply(new HashMap());
                                        //.20260407: order reject dihandle terpisah menggunakan exectype=8 rejected
//                                        if (mInputMsgRequest.getfOrderID().equalsIgnoreCase("NONE")) { //.reject
//                                            mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
//                                            mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
//                                            mReplyMsg.setfNewOrderReplyType(ORIDataNewOrderReply.ORINewOrderReplyType.BAD);
//                                            mReplyMsg.setfOrderID(ORIDataConst.ORIFieldValue.ORDERID_NO_JATS_ORDERNUMBER);
//                                            mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
//                                            mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
//                                            mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
//                                            mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_REJECTED);
//                                            mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
//                                            mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
//                                            mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
//                                            mReplyMsg.setfLeavesQty(0);
//                                            mReplyMsg.setfCumQty(0);
//                                            mReplyMsg.setfAvgPx(0);
//                                            mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
//                                            mReplyMsg.setfText(mInputMsgRequest.getfRejectText());
//                                            mReplyMsg.setfLastPx(0);
//                                            mReplyMsg.setfLastShares(0);
//                                        } else {       
//                                            mCalcQty.setQtyOrder(mMessage.getQuantity());
                                            //.20250801: untuk sekarang dari ouch yang dikirim outstandingnya, sehingga sekarang yang dipakai untuk qtyOrder dari msg asalnya
                                            mCalcQty.setQtyOrder(mOriginRequestMsg.getfOrderQty());
                                            mCalcQty.setOrderStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_NEW); //. order status = new
                                            mCalcQty.setJatsOrderNo(mInputMsgRequest.getfOrderID());
                                            mCalcQty.setBrokerRef(mOriginRequestMsg.getfClOrdID());
                                            //.backup:
                                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);
                                            
                                            mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                            mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                            mReplyMsg.setfNewOrderReplyType(ORIDataNewOrderReply.ORINewOrderReplyType.OK);
                                            //. hrn: asumsi getOrderReferenceNumberExternal = getOrderReferenceNumber
                                            mReplyMsg.setfOrderID(mInputMsgRequest.getfOrderID());
                                            mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                                            mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                            mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
                                            mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_NEW);
                                            mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_NEW);
                                            mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                            mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                                            mReplyMsg.setfLeavesQty(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));
                                            mReplyMsg.setfCumQty(0);
                                            mReplyMsg.setfAvgPx(0);
                                            mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                            mReplyMsg.setfLastPx(0);
                                            mReplyMsg.setfLastShares(0);
//                                        }

                                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
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
                                        if (!mInputMsgRequest.getfOrderID().equalsIgnoreCase("NONE")) {
                                            QRIDataOrderListMessage mOrderListMsg = new QRIDataOrderListMessage(new HashMap());
                                            mOrderListMsg.setfOrderID(StringHelper.toLong(mInputMsgRequest.getfOrderID()));
                                            mOrderListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                                            mOrderListMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                            mOrderListMsg.setfOrigClOrdID(StringUtil.toLong(mCalcQty.getOriJatsOrderNo()));
                                            mOrderListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                                            mOrderListMsg.setfExecBroker("");
                                            mOrderListMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                            mOrderListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                                            mOrderListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                                            mOrderListMsg.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                                            mOrderListMsg.setfAccount(mOriginRequestMsg.getfAccount());
                                            mOrderListMsg.setfFutSettDate(""); //. sementara di kasih empty, karena tidak ketemu dipakai dimana
                                            mOrderListMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                            mOrderListMsg.setfSymbolSfx(mOriginRequestMsg.getfSymbolSfx());
                                            mOrderListMsg.setfSecurityID(mOriginRequestMsg.getfSecurityID());
                                            mOrderListMsg.setfSide(mOriginRequestMsg.getfSide());
                                            mOrderListMsg.setfOrderQty(mCalcQty.getQtyOrder());
                                            mOrderListMsg.setfOrderType(StringUtil.toInteger(mOriginRequestMsg.getfOrdType()));
                                            mOrderListMsg.setfPrice(mOriginRequestMsg.getfPrice());
                                            //.20260402: jika ada harga dari new order, maka update harganya (untuk khasus market to limit)
                                            if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_MARKET_TO_LIMIT)) {
                                                if (StringUtil.toLong(mInputMsgRequest.getfPrice()) > 0 && StringUtil.toLong(mInputMsgRequest.getfPrice()) != Long.MAX_VALUE) {
                                                    //.nilai price terkadang yang dikirim 9223372036854775807 
                                                    mOrderListMsg.setfPrice(StringUtil.toLong(mInputMsgRequest.getfPrice()));
                                                } else {
                                                    mOrderListMsg.setfPrice(mOriginRequestMsg.getfPrice());
                                                }
                                            }                                             
                                            mOrderListMsg.setfTimeInForce(mOriginRequestMsg.getfTimeInForce());
                                            mOrderListMsg.setfExpiredDate(mOriginRequestMsg.getfExpireDate());
                                            mOrderListMsg.setfExecInst(mOriginRequestMsg.getfExecInst());
                                            mOrderListMsg.setfLeavesQty(mCalcQty.getQtyLeave());
                                            mOrderListMsg.setfCumQty(mCalcQty.getQtyMatch());
                                            mOrderListMsg.setfAvgPx(0);
                                            mOrderListMsg.setfTradeDate(DateTimeHelper.getDateSVRTRXFormatFromDate(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                                            mOrderListMsg.setfTransactionTime(DateTimeHelper.getDateSVRTRXFormatFromDate(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                                            mOrderListMsg.setfText("");
                                            mOrderListMsg.setfClearingAccount("");
                                            mOrderListMsg.setfComplianceID(mOriginRequestMsg.getfComplianceID());
                                            //. save orderlist ke memory martin
                                            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOrderListMsg);
                                            //. broadcast orderlist via martin
                                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOrderListMsg);
                                        }
                                    }
                                    break;
                                }
                            case ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT:
                                {
                                    SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                                    if (mCalcQty == null){
                                        mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                        BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                                    }       mCalcQty.setQtyOrder(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));
                                    mCalcQty.setOrderStatus(ORIFieldValue.ORDSTATUS_NEW);
                                    mCalcQty.setJatsOrderNo(mInputMsgRequest.getfOrderID());
                                    mCalcQty.setBrokerRef(mOriginRequestMsg.getfClOrdID());
                                    mCalcQty.setQtyMatch(StringHelper.toLong(mInputMsgRequest.getfCumQty()));
                                    if (mCalcQty.getQtyLeave() >= mCalcQty.getQtyMatch()){
                                        mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_NEW);
                                    }else if (mCalcQty.getQtyLeave() > 0){
                                        mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_PARTIALLY_MATCH);
                                    }else{
                                        mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_FULLY_MATCH);
                                    }       //.backup:
                                    BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);
                                    ORIDataNewOrderReply mReplyMsg = new ORIDataNewOrderReply(new HashMap());
                                    mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                    mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                    mReplyMsg.setfNewOrderReplyType(ORIDataNewOrderReply.ORINewOrderReplyType.OK);
                                    mReplyMsg.setfOrderID(mInputMsgRequest.getfOrderID());
                                    mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                                    mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                    mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_NEW);
                                    mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_NEW);
                                    mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_NEW);
                                    mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                    mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                                    mReplyMsg.setfLeavesQty(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));
                                    mReplyMsg.setfCumQty(0);
                                    mReplyMsg.setfAvgPx(0);
                                    mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                    mReplyMsg.setfLastPx(0);
                                    mReplyMsg.setfLastShares(0);
                                    JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                                    if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                                        if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
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
                                    QRIDataOrderListMessage mOrderListMsg = new QRIDataOrderListMessage(new HashMap());
                                    mOrderListMsg.setfOrderID(StringHelper.toLong(mInputMsgRequest.getfOrderID()));
                                    mOrderListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                                    mOrderListMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                    mOrderListMsg.setfOrigClOrdID(StringHelper.toLong(mCalcQty.getOriJatsOrderNo()));
                                    mOrderListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                                    mOrderListMsg.setfExecBroker("");
                                    mOrderListMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                    mOrderListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                                    mOrderListMsg.setfExecType(QRIFieldValue.EXECTYPE_NEW);
                                    mOrderListMsg.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                                    mOrderListMsg.setfAccount(mOriginRequestMsg.getfAccount());
                                    mOrderListMsg.setfFutSettDate(""); //. sementara di kasih empty, karena tidak ketemu dipakai dimana
                                    mOrderListMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                    mOrderListMsg.setfSymbolSfx(mOriginRequestMsg.getfSymbolSfx());
                                    mOrderListMsg.setfSecurityID(mOriginRequestMsg.getfSecurityID());
                                    mOrderListMsg.setfSide(mOriginRequestMsg.getfSide());
                                    mOrderListMsg.setfOrderQty(mCalcQty.getQtyOrder());
                                    mOrderListMsg.setfOrderType(StringHelper.toInt(mOriginRequestMsg.getfOrdType()));
                                    mOrderListMsg.setfPrice(mOriginRequestMsg.getfPrice());
                                    mOrderListMsg.setfTimeInForce(mOriginRequestMsg.getfTimeInForce());
                                    mOrderListMsg.setfExpiredDate(mOriginRequestMsg.getfExpireDate());
                                    mOrderListMsg.setfExecInst(mOriginRequestMsg.getfExecInst());
                                    mOrderListMsg.setfLeavesQty(mCalcQty.getQtyLeave());
                                    mOrderListMsg.setfCumQty(mCalcQty.getQtyMatch());
                                    mOrderListMsg.setfAvgPx(0);
                                    mOrderListMsg.setfTradeDate(DateTimeHelper.getDateSVRTRXFormatFromDate(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                                    mOrderListMsg.setfTransactionTime(DateTimeHelper.getDateTimeIDXTRXFormat(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                                    mOrderListMsg.setfText("");
                                    mOrderListMsg.setfClearingAccount("");
                                    mOrderListMsg.setfComplianceID(mOriginRequestMsg.getfComplianceID());
                                    //. save orderlist ke memory martin
                                    BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOrderListMsg);
                                    //. broadcast orderlist via martin
                                    BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOrderListMsg);
                                    break;
                                }
                            default:
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @fix5 execution report(new) invalid origin HandlInst from ClOrdID:" + mInputMsgRequest.getfClOrdID() + "[" + mOriginRequestMsg.getfHandlInst() + "]");
                                break;
                        }
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend){
                        
                        System.out.println("mOriginRequest = mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend");
                        
                        ORIDataOrderAmend mOriginRequestMsg = ((ORIDataOrderAmend)mOriginRequest.getIdxMessage());
                            
                        if (mInputMsgRequest.getfExecType().equalsIgnoreCase("F")) {
                            //. flag apakah parsial
                            boolean isPartial = false;

                            SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                            if (mCalcQty == null){
                                mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                            }

                            //. zzz jika sudah full skipp / jatsNo nya empty skkip / LastJatsOrderNo pernah di proses
                            if (mCalcQty.getOrderStatus() == QRIDataConst.QRIFieldValue.ORDSTATUS_FULLY_MATCH || "".equals(mCalcQty.getJatsOrderNo()) || mCalcQty.getLastJatsTradeNo() >= StringHelper.toLong(mInputMsgRequest.getfTrdMatchID())){
                                System.err.println("OUCHMsgExecutedOrder mCalcQty.getLastJatsTradeNo() = " + mCalcQty.getLastJatsTradeNo() + ", mMessage.getMatchNumber() = " + StringHelper.toLong(mInputMsgRequest.getfTrdMatchID())+ ", mCalcQty.getJatsOrderNo() = " + mCalcQty.getJatsOrderNo());
                                return;
                            }

                            //. tambah match
                            mCalcQty.addQtyMatch(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                            //. set LastJastNo
                            mCalcQty.setLastJatsTradeNo(StringHelper.toLong(mInputMsgRequest.getfTrdMatchID()));
                            //. kalkulasi status

    //                        if (mCalcQty.getQtyLeave() >= mCalcQty.getQtyMatch()){
    //                            mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_NEW);
    //                        }else 

                            if (mCalcQty.getQtyLeave() > 0){
                                isPartial = true;
                                mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_PARTIALLY_MATCH);
                            }else{
                                mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_FULLY_MATCH);
                            }

                            //. OrderList dengan kalkulasi
                            QRIDataOrderListMessage mOrderListMsg = new QRIDataOrderListMessage(new HashMap());
                            mOrderListMsg.setfOrderID(StringHelper.toLong(mCalcQty.getJatsOrderNo()));
                            mOrderListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                            mOrderListMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                            mOrderListMsg.setfOrigClOrdID(StringUtil.toLong(mCalcQty.getOriJatsOrderNo()));
                            mOrderListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                            mOrderListMsg.setfExecBroker("");
                            mOrderListMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                            mOrderListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                            mOrderListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                            mOrderListMsg.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                            mOrderListMsg.setfAccount(mOriginRequestMsg.getfAccount());
                            mOrderListMsg.setfFutSettDate(""); //. sementara di kasih empty, karena tidak ketemu dipakai dimana
                            mOrderListMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                            mOrderListMsg.setfSymbolSfx(mInputMsgRequest.getfSymbol());//????
                            mOrderListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());//??????
                            mOrderListMsg.setfSide(mOriginRequestMsg.getfSide());
                            mOrderListMsg.setfOrderQty(mCalcQty.getQtyOrder());
                            mOrderListMsg.setfOrderType(StringUtil.toInteger(mOriginRequestMsg.getfOrdType()));
                            mOrderListMsg.setfPrice(mOriginRequestMsg.getfPrice());
                            mOrderListMsg.setfTimeInForce(mOriginRequestMsg.getfTimeInForce());
                            mOrderListMsg.setfExpiredDate(mOriginRequestMsg.getfExpireDate());
                            mOrderListMsg.setfExecInst(mInputMsgRequest.getfExecInst());//?????
                            mOrderListMsg.setfLeavesQty(mCalcQty.getQtyLeave());
                            mOrderListMsg.setfCumQty(mCalcQty.getQtyMatch());
                            mOrderListMsg.setfAvgPx(0);
                            mOrderListMsg.setfTradeDate(DateTimeHelper.getDateSVRTRXFormatFromDate(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                            mOrderListMsg.setfTransactionTime(DateTimeHelper.getDateSVRTRXFormatFromDate(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                            mOrderListMsg.setfText("");
                            mOrderListMsg.setfClearingAccount("");
                            mOrderListMsg.setfComplianceID(mOriginRequestMsg.getfComplianceID());                        

                            if (isPartial){//. jika parsial ditahan dulu, lempar ke class
                                QRIOrderListPartialSummaryWorker.getInstance.addData(mOrderListMsg);
                            }else{
                                //.pastikan di remove dari pendingan message di QRIOrderListPartialSummaryWorker
                                QRIOrderListPartialSummaryWorker.getInstance.removeData(mOrderListMsg.getfOrderID());

                                //. save orderlist ke memory martin
                                BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOrderListMsg);

                                //. hrn-20211221 : untuk antisipasi order yang langsung match (overide dengan OrderAccepted), 
                                //. untuk full match di tahan 50ms
                                final QRIDataOrderListMessage mFnlOrderListMsg = mOrderListMsg;
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        //. broadcast orderlist via martin
                                        BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mFnlOrderListMsg);
                                    }
                                }, 50); 

                            }

                            //. Tradelist
                            QRIDataTradeListMessage mTradeListMsg = new QRIDataTradeListMessage(new HashMap());
                            mTradeListMsg.setfOrderID(StringHelper.toLong(mCalcQty.getJatsOrderNo()));
                            mTradeListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                            mTradeListMsg.setfSecondaryOrderID(StringHelper.toLong(mInputMsgRequest.getfTrdMatchID()));
                            mTradeListMsg.setfTransactionTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                            mTradeListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                            mTradeListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                            mTradeListMsg.setfSide(mOriginRequestMsg.getfSide());
                            mTradeListMsg.setfExecBroker("");
                            mTradeListMsg.setfContraTrader("");
                            //.soon
    //                        long lCounterPartID =  mMessage.getCounterPartyID(); //. ?? to broker code
                            String lCounterPartCode = "";
                            //.soon
    //                        SheetOfITCHParticipantDirectory mSheetParticipant = BookOfITCHParticipantDirectory.getInstance.retrieveSheet(lCounterPartID);
    //                        if (mSheetParticipant != null){
    //                            lCounterPartCode = mSheetParticipant.getMessage().getParticipantCode();
    //                        }
                            mTradeListMsg.setfContraBroker(lCounterPartCode);
                            mTradeListMsg.setfAccount(mOriginRequestMsg.getfAccount());
                            mTradeListMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                            mTradeListMsg.setfSymbolSfx(mOriginRequestMsg.getfSymbol());//????
                            mTradeListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());//????
                            //.20260416
//                            mTradeListMsg.setfPrice(StringHelper.toDouble(mInputMsgRequest.getfPrice()));
//                            mTradeListMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));
                            mTradeListMsg.setfPrice(StringHelper.toDouble(mInputMsgRequest.getfLastPx()));
                            mTradeListMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                            mTradeListMsg.setfText("");
                            mTradeListMsg.setfClearingAccount(" ");
                            mTradeListMsg.setfFutSettDate("");
                            mTradeListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NORMAL_MATCH);
                            mTradeListMsg.setfLastPx(0);
                            mTradeListMsg.setfNoContraBrokers(1);                        
                            mTradeListMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                            mTradeListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                            mTradeListMsg.setfLeavesQty(0);
                            mTradeListMsg.setfAvgPx(0);
                            mTradeListMsg.setfComplianceID(mOriginRequestMsg.getfComplianceID());
                            //. order status
                            mTradeListMsg.setfOrdStatus("2");
                            //. save tradelist ke memory martin
                            BookOfMARTINTradeList.getInstance.addOrUpdateSheet(mTradeListMsg);
                            //. broadcast tradelist via martin
                            BookOfMARTINTradeList.getInstance.brodcastToSubscriber(mTradeListMsg);
                            //.backup:
                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);

                        } else {
                            SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                            if (mCalcQty == null){
                                mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                            }
                            //.20260416: todo jika vPrevOrderToken = 0 atau tidak ada, maka diambil dari prevBrokerRef message originnya 
                                                
                            SheetOfJONECSimCalcQty mPrevCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vPrevOrderToken);

                            mCalcQty.setQtyOrder(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));
                            if (mPrevCalcQty != null){
                                mCalcQty.setQtyMatch(mPrevCalcQty.getQtyMatch());
                                //. set status baru = status lama
                                mCalcQty.setOrderStatus(mPrevCalcQty.getOrderStatus());
                                //. set status lama = amend
                                mPrevCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_REPLACED);
                            }
                            
                            mCalcQty.setJatsOrderNo(mInputMsgRequest.getfOrderID());
                            mCalcQty.setOriJatsOrderNo(mOriginRequestMsg.getfOrderID());
                            mCalcQty.setBrokerRef(mOriginRequestMsg.getfClOrdID());
                            mCalcQty.setOriBrokerRef(mOriginRequestMsg.getfOrigClOrdID());

                            //.backup:
                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);
                            if (mPrevCalcQty != null){
                                BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vPrevOrderToken, mPrevCalcQty);
                            }

                            ORIDataOrderAmendReply mReplyMsg = new ORIDataOrderAmendReply(new HashMap());
                            mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                            mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                            mReplyMsg.setfOrderAmendReplyType(ORIDataOrderAmendReply.ORIOrderAmendReplyType.OK);
                            mReplyMsg.setfOrderID(mInputMsgRequest.getfOrderID());
                            mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                            mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                            mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_CORRECT);
                            mReplyMsg.setfExecRefID(mOriginRequestMsg.getfOrigClOrdID());
                            mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_REPLACEMENT);
                            mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_REPLACED);
                            mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                            mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                            mReplyMsg.setfOrderQty(StringUtil.toLong(mInputMsgRequest.getfOrderQty()));
                            mReplyMsg.setfLeavesQty(mCalcQty.getQtyLeave()); 
                            mReplyMsg.setfCumQty(mCalcQty.getQtyMatch());
                            mReplyMsg.setfAvgPx(0);
                            mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                            mReplyMsg.setfLastPx(mOriginRequestMsg.getfPrice());
                            mReplyMsg.setfLastShares(0);

                            JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                            if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
//                                System.out.println("mReplyMsg.msgToString() Data = " + mReplyMsg.msgToString());

                                if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                    //... .
                                }else{
                                    //.???:
                                    //. disisi klien akan menggantung, solusi : send ulang
                                    //. todo : masukin log
                                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                                }
                            }else{
                                //.???:
                                //. disisi klien akan menggantung, solusi : send ulang
                                //. todo : masukin log
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                            }


//                            System.out.println("mOriginRequestMsg Data = " + mOriginRequestMsg.msgDataToString());
//                            System.out.println("mOriginRequestMsg.getfHandlInst() = " + mOriginRequestMsg.getfHandlInst());
                            if ((mOriginRequestMsg.getfHandlInst() == ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT) 
                                || (mOriginRequestMsg.getfHandlInst() == ORIDataConst.ORIFieldValue.HANDLINST_NORMAL)){
//                                System.out.println("Process 1");

    //                            SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
    //                            if (mCalcQty == null){
    //                                mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
    //                                BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
    //                            }
    //                            
    //                            mCalcQty.setQtyOrder(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));
    //                            mCalcQty.setOrderStatus(ORIFieldValue.ORDSTATUS_NEW);
    //                            mCalcQty.setJatsOrderNo(mInputMsgRequest.getfOrderID());
    //                            mCalcQty.setBrokerRef(mOriginRequestMsg.getfClOrdID());
    //                            
    //                            mCalcQty.setQtyMatch(StringHelper.toLong(mInputMsgRequest.getfCumQty()));
    //                            if (mCalcQty.getQtyLeave() >= mCalcQty.getQtyMatch()){
    //                                mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_NEW);
    //                            }else if (mCalcQty.getQtyLeave() > 0){
    //                                mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_PARTIALLY_MATCH);
    //                            }else{
    //                                mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_FULLY_MATCH);
    //                            }

                                QRIDataOrderListMessage mOldOrderList = BookOfMARTINOrderList.getInstance.retrieveSheet(StringUtil.toLong(mOriginRequestMsg.getfOrderID()));

                                //. OrderList dengan kalkulasi
                                QRIDataOrderListMessage mOrderListMsg = new QRIDataOrderListMessage(new HashMap());
                                mOrderListMsg.setfOrderID(StringHelper.toLong(mCalcQty.getJatsOrderNo()));
                                mOrderListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                                mOrderListMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                mOrderListMsg.setfOrigClOrdID(StringUtil.toLong(mCalcQty.getOriJatsOrderNo()));
                                mOrderListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                                mOrderListMsg.setfExecBroker("");
                                mOrderListMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                mOrderListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                                mOrderListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                                mOrderListMsg.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                                mOrderListMsg.setfAccount(mOriginRequestMsg.getfAccount());
                                mOrderListMsg.setfFutSettDate(""); //. sementara di kasih empty, karena tidak ketemu dipakai dimana
                                mOrderListMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                if (mOldOrderList != null){
                                    mOrderListMsg.setfSymbolSfx(mOldOrderList.getfSymbolSfx());
                                    mOrderListMsg.setfSecurityID(mOldOrderList.getfSecurityID());
                                    mOrderListMsg.setfExecInst(mOldOrderList.getfExecInst());
                                }
                                mOrderListMsg.setfSide(mOriginRequestMsg.getfSide());
                                mOrderListMsg.setfOrderQty(mCalcQty.getQtyOrder());
                                mOrderListMsg.setfOrderType(StringUtil.toInteger(mOriginRequestMsg.getfOrdType()));
                                mOrderListMsg.setfPrice(mOriginRequestMsg.getfPrice());
                                mOrderListMsg.setfTimeInForce(mOriginRequestMsg.getfTimeInForce());
                                mOrderListMsg.setfExpiredDate(mOriginRequestMsg.getfExpireDate());
                                mOrderListMsg.setfLeavesQty(mCalcQty.getQtyLeave());
                                mOrderListMsg.setfCumQty(mCalcQty.getQtyMatch());
                                mOrderListMsg.setfAvgPx(0);
                                mOrderListMsg.setfTradeDate(DateTimeHelper.getDateSVRTRXFormatFromDate(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                                mOrderListMsg.setfTransactionTime(DateTimeHelper.getDateTimeIDXTRXFormat(FIX5DateTimeHelper.getServerDateTimeFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()))); //. ??
                                mOrderListMsg.setfText("");
                                mOrderListMsg.setfClearingAccount("");
                                mOrderListMsg.setfComplianceID(mOriginRequestMsg.getfComplianceID());                        
//                                System.out.println("Process 2");
                                //. save orderlist ke memory martin
                                BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOrderListMsg);
                                //. broadcast orderlist via martin
                                BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOrderListMsg);
//                                System.out.println("mOrderListMsg.msgToString() " + mOrderListMsg.msgToString());
//                                System.out.println("Process 3");
                                //.backup:
                                BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);

                            }else{
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @fix5 execution report(new) invalid origin HandlInst from ClOrdID:" + mInputMsgRequest.getfClOrdID() + "[" + mOriginRequestMsg.getfHandlInst() + "]");
                            }
                        }
                        
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel){
                        System.out.println("mOriginRequest = mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel");
                        
                        SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                        if (mCalcQty == null){
                            mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                        }
                        //. set status cancel
                        mCalcQty.setOrderStatus(QRIFieldValue.ORDSTATUS_CANCELLED);
                        //.backup:
                        BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);
                        
                        ORIDataOrderCancel mOriginRequestMsg = ((ORIDataOrderCancel)mOriginRequest.getIdxMessage());
                        
                        ORIDataOrderCancelReply mReplyMsg = new ORIDataOrderCancelReply(new HashMap());
                        mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                        mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                        //. semua response
                        mReplyMsg.setfOrderCancelReplyType(ORIDataOrderCancelReply.ORIOrderCancelReplyType.OK);
                        mReplyMsg.setfOrderID(mOriginRequestMsg.getfOrderID());              
                        mReplyMsg.setfExecRefID(mOriginRequestMsg.getfClOrdID());
                        mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                        mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_CANCEL);
                        mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_CANCELLED);
                        mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_CANCELLED);
                        mReplyMsg.setfSymbol(" ");
                        mReplyMsg.setfSide(" ");
                        mReplyMsg.setfLeavesQty(0);
                        mReplyMsg.setfCumQty(0);
                        mReplyMsg.setfAvgPx(0);
                        //////mReplyMsg.setfHandlInst(ORIFieldValue.HANDLINST_NORMAL);
                        mReplyMsg.setfHandlInst(mOriginRequestMsg.getfSymbolSfx().equals(ORIFieldValue.BOARD_NG) ? ORIFieldValue.HANDLINST_ADVERTISEMENT : ORIFieldValue.HANDLINST_NORMAL);
                        mReplyMsg.setfText("");
                        mReplyMsg.setfLastPx(0);
                        mReplyMsg.setfLastShares(0);
                        
                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                //... .
                            }else{
                                //.???:
                                //. TODO : handle lewat Martin
                                //. masukin log
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                            }
                        }else{
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                        }
                        
                        //.Orderlist (QRI)
                        QRIDataOrderListMessage mOriOrderList = BookOfMARTINOrderList.getInstance.retrieveSheet(StringUtil.toLong(mOriginRequestMsg.getfOrderID()));
                        
                        if (mOriOrderList != null){
                            //. update order status
                            mOriOrderList.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                            //. update exec id
                            mOriOrderList.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                            
                            //. save orderlist ke memory martin
                            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOriOrderList);
                            //. broadcast old orderlist via martin
                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOriOrderList);
                        }else{
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "JatsOrderNo @" + mOriginRequestMsg.getfOrderID() + " not found in BookOfMARTINOrderList");
                        }
                    }else{
                        System.out.println("mOriginRequest = mOriginRequest.getIdxMessage() instanceof Uknown");
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
}
