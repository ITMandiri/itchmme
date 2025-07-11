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
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataTradeListMessage;
import com.itm.idx.data.qri.util.StringUtil;
import com.itm.mis.itch.books.BookOfITCHParticipantDirectory;
import com.itm.mis.itch.books.SheetOfITCHParticipantDirectory;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.structs.OUCHMsgOrderExecuted;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.helper.QRIOrderListPartialSummaryWorker;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINOrderList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINTradeList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author fredy
 */
public class JONECSimMsgMemWorkExecutedOrder {
    //.single instance:
    public final static JONECSimMsgMemWorkExecutedOrder getInstance = new JONECSimMsgMemWorkExecutedOrder();
    
    public JONECSimMsgMemWorkExecutedOrder() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(SheetOfOUCHBase mSheet, OUCHMsgOrderExecuted mMessage){
        try{
            long lSeqLatestSaved = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestSaved();
            long lSeqLatestReceived = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestReceived();
            if (lSeqLatestReceived <= lSeqLatestSaved){ //. skip
                return;
            }
            //. hrn 20211026 : untuk mengurangi generate meesage OrderList, ketika dapat Trade di hold dulu X ms
            //. kirim yang posisi yang terakhir
            
            long vOrderToken = mMessage.getOrderToken();
            
            //. flag apakah parsial
            boolean isPartial = false;
            
            if (vOrderToken > 0){
                //.????: 1. Orderlist (QRI)
                //.????: 2. Tradelist (QRI)   
                
                //. 1. siapkan memory dengan key Token
                //. 2. isi memory : QtyOrder, QtyMatch, QtyLeave (QtyOrder - QtyMatch) 
                //. JONECSimMsgMemWorkAccepted : QtyOrder = Message, QtyMatch = QtyOrder
                //. JONECSimMsgMemWorkExecutedOrder : kalkulasi QtyMatch
                //. JONECSimMsgMemWorkReplaced: QtyOrder = Message, QtyMatch = Lookup Ke Orig nya
                SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                
                //. 2022-07-29 : BUG-FIX if (mOriginRequest != null && mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){ di ganti jadi if (mEveryRequest != null && mEveryRequest.getIdxMessage() instanceof ORIDataNewOrder){
                //. fungsinya jika ada overlap antara kirim withdraw dan executed dari bursa
                //. untuk yang terakhir amend ambil dari mEveryRequest
                if ((mOriginRequest != null) || (mEveryRequest != null)){
                    if (mEveryRequest != null && mEveryRequest.getIdxMessage() instanceof ORIDataNewOrder){

                        if (mCalcQty == null){
                            mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                        }
                        
                        //. zzz jika sudah full skipp / jatsNo nya empty skkip / LastJatsOrderNo pernah di proses
                        if (mCalcQty.getOrderStatus() == QRIDataConst.QRIFieldValue.ORDSTATUS_FULLY_MATCH || "".equals(mCalcQty.getJatsOrderNo()) || mCalcQty.getLastJatsTradeNo() >= mMessage.getMatchId()){
                            System.err.println("OUCHMsgExecutedOrder mCalcQty.getLastJatsTradeNo() = " + mCalcQty.getLastJatsTradeNo() + ", mMessage.getMatchNumber() = " + mMessage.getMatchId()+ ", mCalcQty.getJatsOrderNo() = " + mCalcQty.getJatsOrderNo());
                            return;
                        }
                        
                        //. tambah match
                        mCalcQty.addQtyMatch(mMessage.getTradeQuantity());
                        //. set LastJastNo
                        mCalcQty.setLastJatsTradeNo(mMessage.getMatchId());
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
                        //. xyz
//                        System.err.println("mCalcQty.getJatsOrderNo = " + mCalcQty.getJatsOrderNo() + ", Partial = " + isPartial);
//                        System.err.println("mCalcQty.getQtyOrder = " + mCalcQty.getQtyOrder() + ", Status = " + StringHelper.fromInt(mCalcQty.getOrderStatus()));
//                        System.err.println("mMessage.getExecutedQuantity = " + mMessage.getExecutedQuantity());
//                        System.err.println("mCalcQty.getQtyLeave = " + mCalcQty.getQtyLeave());
//                        System.err.println("mCalcQty.getQtyMatch = " + mCalcQty.getQtyMatch());

                        
                        ORIDataNewOrder mOriginRequestMsg = ((ORIDataNewOrder)mEveryRequest.getIdxMessage());
                        
                        //. OrderList dengan kalkulasi
                        QRIDataOrderListMessage mOrderListMsg = new QRIDataOrderListMessage(new HashMap());
                        mOrderListMsg.setfOrderID(StringHelper.toLong(mCalcQty.getJatsOrderNo()));
                        mOrderListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                        mOrderListMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                        mOrderListMsg.setfOrigClOrdID(StringUtil.toLong(mCalcQty.getOriJatsOrderNo()));
                        mOrderListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                        mOrderListMsg.setfExecBroker("");
                        mOrderListMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
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
                        mOrderListMsg.setfTimeInForce(mOriginRequestMsg.getfTimeInForce());
                        mOrderListMsg.setfExpiredDate(mOriginRequestMsg.getfExpireDate());
                        mOrderListMsg.setfExecInst(mOriginRequestMsg.getfExecInst());
                        mOrderListMsg.setfLeavesQty(mCalcQty.getQtyLeave());
                        mOrderListMsg.setfCumQty(mCalcQty.getQtyMatch());
                        mOrderListMsg.setfAvgPx(0);
                        mOrderListMsg.setfTradeDate(DateTimeHelper.getDateSVRTRXFormatFromDate(mSheet.getMessageDate())); //. ??
                        mOrderListMsg.setfTransactionTime(DateTimeHelper.getDateTimeIDXTRXFormat(mSheet.getMessageDate())); //. ??
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
                        mTradeListMsg.setfSecondaryOrderID(mMessage.getMatchId());
                        mTradeListMsg.setfTransactionTime(DateTimeHelper.getDateTimeIDXTRXFormat(mSheet.getMessageDate()));
                        mTradeListMsg.setfEffectiveTime(DateTimeHelper.getDateTimeIDXTRXFormat(mSheet.getMessageDate()));
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
                        //.soon
//                        mTradeListMsg.setfPrice(mMessage.getExecutedPrice());
//                        mTradeListMsg.setfCumQty(mMessage.getExecutedQuantity());
                        mTradeListMsg.setfText("");
                        mTradeListMsg.setfClearingAccount(" ");
                        mTradeListMsg.setfFutSettDate("");
                        mTradeListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NORMAL_MATCH);
                        mTradeListMsg.setfLastPx(0);
                        mTradeListMsg.setfNoContraBrokers(1);                        
                        mTradeListMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
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
                        
                    }else if (mEveryRequest != null && mEveryRequest.getIdxMessage() instanceof ORIDataOrderAmend){
                        if (mCalcQty == null){
                            mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                        }
                        
                        //. zzz jika sudah full skipp / jatsNo nya empty skkip / LastJatsOrderNo pernah di proses
                        if (mCalcQty.getOrderStatus() == QRIDataConst.QRIFieldValue.ORDSTATUS_FULLY_MATCH || "".equals(mCalcQty.getJatsOrderNo()) || mCalcQty.getLastJatsTradeNo() >= mMessage.getMatchId()){
                            System.err.println("OUCHMsgExecutedOrder mCalcQty.getLastJatsTradeNo() = " + mCalcQty.getLastJatsTradeNo() + ", mMessage.getMatchNumber() = " + mMessage.getMatchId()+ ", mCalcQty.getJatsOrderNo() = " + mCalcQty.getJatsOrderNo());
                            return;
                        }
                        
                        //. tambah match
                        mCalcQty.addQtyMatch(mMessage.getTradeQuantity());
                        //. set LastJastNo
                        mCalcQty.setLastJatsTradeNo(mMessage.getMatchId());
                        
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
                        //. xyz
//                        System.err.println("mCalcQty.getJatsOrderNo = " + mCalcQty.getJatsOrderNo());
//                        System.err.println("mCalcQty.getQtyOrder = " + mCalcQty.getQtyOrder());
//                        System.err.println("mMessage.getExecutedQuantity = " + mMessage.getExecutedQuantity());
//                        System.err.println("mCalcQty.getQtyLeave = " + mCalcQty.getQtyLeave());
//                        System.err.println("mCalcQty.getQtyMatch = " + mCalcQty.getQtyMatch());
                        
                        ORIDataOrderAmend mOriginRequestMsg = ((ORIDataOrderAmend)mEveryRequest.getIdxMessage());
                        
                        QRIDataOrderListMessage mOldOrderList = BookOfMARTINOrderList.getInstance.retrieveSheet(StringUtil.toLong(mCalcQty.getOriJatsOrderNo()));
                        
                        //. OrderList dengan kalkulasi
                        QRIDataOrderListMessage mOrderListMsg = new QRIDataOrderListMessage(new HashMap());
                        mOrderListMsg.setfOrderID(StringHelper.toLong(mCalcQty.getJatsOrderNo()));
                        mOrderListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                        mOrderListMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                        mOrderListMsg.setfOrigClOrdID(StringUtil.toLong(mCalcQty.getOriJatsOrderNo()));
                        mOrderListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                        mOrderListMsg.setfExecBroker("");
                        mOrderListMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
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
                        mOrderListMsg.setfTradeDate(DateTimeHelper.getDateSVRTRXFormatFromDate(mSheet.getMessageDate())); //. ??
                        mOrderListMsg.setfTransactionTime(DateTimeHelper.getDateTimeIDXTRXFormat(mSheet.getMessageDate())); //. ??
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
                            //. broadcast orderlist via martin
                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOrderListMsg);
                        }
                        
                        
                        //. Tradelist
                        QRIDataTradeListMessage mTradeListMsg = new QRIDataTradeListMessage(new HashMap());
                        mTradeListMsg.setfOrderID(StringHelper.toLong(mCalcQty.getJatsOrderNo()));
                        mTradeListMsg.setfClOrdID(mCalcQty.getBrokerRef());
                        mTradeListMsg.setfSecondaryOrderID(mMessage.getMatchId());
                        mTradeListMsg.setfTransactionTime(DateTimeHelper.getDateTimeIDXTRXFormat(mSheet.getMessageDate()));
                        mTradeListMsg.setfEffectiveTime(DateTimeHelper.getDateTimeIDXTRXFormat(mSheet.getMessageDate()));
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
                        if (mOldOrderList != null){
                            mTradeListMsg.setfSymbolSfx(mOldOrderList.getfSymbolSfx());
                            mTradeListMsg.setfSecurityID(mOldOrderList.getfSecurityID());
                        }
                        //.soon
//                        mTradeListMsg.setfPrice(mMessage.getExecutedPrice());
//                        mTradeListMsg.setfCumQty(mMessage.getExecutedQuantity());
                        mTradeListMsg.setfText("");
                        mTradeListMsg.setfClearingAccount(" ");
                        mTradeListMsg.setfFutSettDate("");
                        mTradeListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NORMAL_MATCH);
                        mTradeListMsg.setfLastPx(0);
                        mTradeListMsg.setfNoContraBrokers(1);                        
                        mTradeListMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
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
                        
                    }
                }
                

            }else{
                //.???:
                //. masukin log
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }

}
