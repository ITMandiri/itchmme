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
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmendReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.util.StringUtil;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.structs.OUCHMsgOrderCanceled;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackProcessor;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.helper.QRIOrderListPartialSummaryWorker;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINOrderList;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class JONECSimMsgMemWorkCanceled {
    //.single instance:
    public final static JONECSimMsgMemWorkCanceled getInstance = new JONECSimMsgMemWorkCanceled();
    
    public JONECSimMsgMemWorkCanceled() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(SheetOfOUCHBase mSheet, OUCHMsgOrderCanceled mMessage){
        try{
            long lSeqLatestSaved = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestSaved();
            long lSeqLatestReceived = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestReceived();
            if (lSeqLatestReceived <= lSeqLatestSaved){ //. skip
                return;
            }
            
            long vOrderToken = mMessage.getOrderToken();
            
            if (vOrderToken > 0){
                
                SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                
//                System.out.println("vOrderToken = " + vOrderToken);
//                System.out.println("mOriginRequest = " + mOriginRequest);
//                System.out.println("mEveryRequest = " + mEveryRequest);
//                
//                if (mOriginRequest != null){
//                    System.out.println("mOriginRequest.getIdxMessage = " + mOriginRequest.getIdxMessage());
//                }
//                if (mEveryRequest != null){
//                    System.out.println("mEveryRequest.getIdxMessage = " + mEveryRequest.getIdxMessage());
//                }
                
                SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                if (mCalcQty == null){
                    mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                    BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                }
                
                //. 2021-04-11 : pastikan yang pending parsial tidak ada
                QRIOrderListPartialSummaryWorker.getInstance.releasePendingParsialIfAvailable(StringUtil.toLong(mCalcQty.getJatsOrderNo()));
                
                //. 20210909 : handle jika sumber nya adalah ORIDataAmendOrder dan dapat autowithdraw dari BEI
                if ((mOriginRequest == null) && (mEveryRequest != null)){
                    mOriginRequest = new SheetOfJONECSimOriginRequest(vOrderToken, mEveryRequest.getIdxMessage());
                }
      
                if ((mOriginRequest != null) && (mEveryRequest != null)){
                    if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){ 
                        
                        ORIDataNewOrder mOriginRequestMsg = ((ORIDataNewOrder)mOriginRequest.getIdxMessage());
                        
                        //. note 20210702 : mungkin terjadi jika ada OUCHMsgCancelled dari BEI, tanpa kita ngirim witdhraw
                        //ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "OUCHMsgCancelled from ORIDataNewOrder, BrokerRef/getfClOrdID = " + mOriginRequestMsg.getfClOrdID() + ", mCalcQty.getJatsOrderNo = " + mCalcQty.getJatsOrderNo() + ", mCalcQty.getOriJatsOrderNo() = " + mCalcQty.getOriJatsOrderNo());
                        
                        //. set status cancel
                        mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_CANCELLED);
                        //.backup:
                        BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);
                        
                       
                        
                        ORIDataOrderCancelReply mReplyMsg = new ORIDataOrderCancelReply(new HashMap());
                        mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                        mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                        
                        //. semua response
                        mReplyMsg.setfOrderCancelReplyType(ORIDataOrderCancelReply.ORIOrderCancelReplyType.OK);
                        
                        mReplyMsg.setfOrderID(mCalcQty.getJatsOrderNo());
                        
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
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            }
                        }else{
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                        

                        
                        //.Orderlist (QRI)
                        QRIDataOrderListMessage mOriOrderList = BookOfMARTINOrderList.getInstance.retrieveSheet(StringUtil.toLong(mCalcQty.getJatsOrderNo()));
                        
 
                        
                        if (mOriOrderList != null){
                            //. update order status
                            mOriOrderList.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                            //. update exec id
                            mOriOrderList.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
                            
                            //. save orderlist ke memory martin
                            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOriOrderList);
                            //. broadcast orderlist via martin
                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOriOrderList);
                        }else{
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "JatsOrderNo @" + mCalcQty.getJatsOrderNo() + " not found in BookOfMARTINOrderList");
                        }
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend){
                        
                        ORIDataOrderAmend mOriginRequestMsg = ((ORIDataOrderAmend)mOriginRequest.getIdxMessage());
                        //.soon
                        //. check jika dari amend
//                        if (mMessage.isFromAmend()){
//                            //. set status reject untuk token baru
//                            mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_REJECTED);
//                            //.backup:
//                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);
//
//
//
//                            //. urutan jika cancel dari reject-amend
//                            //. 1. Kirim amend reply bad (broker ref baru)
//                            //. 2. Kirim cancel broker ref lama ??xx
//                            //. 3. update status OrderList Martin ??xx
//
//                            //. zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
//
//                            
//                            //. Amend Reply Bad untuk broker ref baru
//                            ORIDataOrderAmendReply mReplyAmendBadMsg = new ORIDataOrderAmendReply(new HashMap());
//                            mReplyAmendBadMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
//                            mReplyAmendBadMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
//                            mReplyAmendBadMsg.setfOrderAmendReplyType(ORIDataOrderAmendReply.ORIOrderAmendReplyType.BAD);                        
//                            mReplyAmendBadMsg.setfOrderID("000000000000");
//                            mReplyAmendBadMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
//                            mReplyAmendBadMsg.setfOrigClOrdID(" ");
//                            mReplyAmendBadMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
//                            mReplyAmendBadMsg.setfCxlRejResponseTo(2);
//                            mReplyAmendBadMsg.setfText(String.valueOf(mMessage.getCancelReason()));
//
//    //                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyAmendBadMsg.getfBundleConnectionName());
//    //                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
//    //                            if (    
//    //                                    mClientLine.getChChannel().sendMessageDirect(mReplyAmendBadMsg.msgToString())
//    //                                    
//    //                                    
//    //                                    ){
//    //                                //... .
//    //                            }else{
//    //                                //.???:
//    //                                //. TODO : handle lewat Martin
//    //                                //. masukin log
//    //                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
//    //                            }
//    //                        }else{
//    //                            //.???:
//    //                            //. TODO : handle lewat Martin
//    //                            //. masukin log
//    //                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
//    //                        }
//
//                            //. 2021-06-15 : proses ini di batalkan (di aktifkan kembali karena dari itch dapat cancelled)
//
//
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
//
//                            //. kirim order amend-reply-bad untuk broker-ref baru
//
//
//
//
//                            JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
//                            if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
//                                if (    
//                                        mClientLine.getChChannel().sendMessageDirect(mReplyAmendBadMsg.msgToString()) &&
//                                        mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())
//
//
//                                        ){
//                                    //... .
//                                }else{
//                                    //.???:
//                                    //. TODO : handle lewat Martin
//                                    //. masukin log
//                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
//                                }
//                            }else{
//                                //.???:
//                                //. TODO : handle lewat Martin
//                                //. masukin log
//                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
//                            }
//
//
//                            //.Orderlist (QRI)
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
//                        }else{ //. kemungkinan dari expired atau autowithdraw akhir hari ?????????????????????????/
//                            
//                            //. note 20210702 : mungkin terjadi jika ada OUCHMsgCancelled dari BEI, tanpa kita ngirim witdhraw
//                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "OUCHMsgCancelled from ORIDataOrderAmend, NewBrokerRef/getfClOrdID = " + mOriginRequestMsg.getfClOrdID() + ", OriBrokerRef/getfOrigClOrdID = " + mOriginRequestMsg.getfOrigClOrdID() + ", mCalcQty.getJatsOrderNo = " + mCalcQty.getJatsOrderNo() + ", mCalcQty.getOriJatsOrderNo() = " + mCalcQty.getOriJatsOrderNo());
//                        
//                            //. set status reject untuk token baru
//                            mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_CANCELLED);
//                            //.backup:
//                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);
//                            
//                            ORIDataOrderCancelReply mReplyMsg = new ORIDataOrderCancelReply(new HashMap());
//                            mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
//                            mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
//
//                            //. semua response
//                            mReplyMsg.setfOrderCancelReplyType(ORIDataOrderCancelReply.ORIOrderCancelReplyType.OK);
//
//                            mReplyMsg.setfOrderID(mCalcQty.getJatsOrderNo()); //. jats no baru
//
//                            mReplyMsg.setfExecRefID(mOriginRequestMsg.getfClOrdID());
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
//
//
//                            JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
//                            if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
//                                if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
//                                    //... .
//                                }else{
//                                    //.???:
//                                    //. TODO : handle lewat Martin
//                                    //. masukin log
//                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
//                                }
//                            }else{
//                                //.???:
//                                //. TODO : handle lewat Martin
//                                //. masukin log
//                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
//                            }
//
//                            //.Orderlist (QRI)
//                            QRIDataOrderListMessage mOriOrderList = BookOfMARTINOrderList.getInstance.retrieveSheet(StringUtil.toLong(mCalcQty.getJatsOrderNo()));
//
//                            if (mOriOrderList != null){
//                                //. update order status
//                                mOriOrderList.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
//                                //. update exec id
//                                mOriOrderList.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
//                                
//                                //. save orderlist ke memory martin
//                                BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOriOrderList);
//                                //. broadcast old orderlist via martin
//                                BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOriOrderList);
//                            }else{
//                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "JatsOrderNo @" + mCalcQty.getJatsOrderNo() + " not found in BookOfMARTINOrderList");
//                            }
//
//                        }
                        
                        
                        
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel){
                        
                        //. set status cancel
                        mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_CANCELLED);
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
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            }
                        }else{
                            //.???:
                            //. TODO : handle lewat Martin
                            //. masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                        
                        //.Orderlist (QRI)
                        QRIDataOrderListMessage mOriOrderList = BookOfMARTINOrderList.getInstance.retrieveSheet(StringUtil.toLong(mOriginRequestMsg.getfOrderID()));
                        
                        if (mOriOrderList != null){
                            //. update order status
                            mOriOrderList.setfOrdStatus(StringHelper.fromInt(mCalcQty.getOrderStatus()));
                            //. update exec id
                            mOriOrderList.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
                            
                            //. save orderlist ke memory martin
                            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOriOrderList);
                            //. broadcast old orderlist via martin
                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOriOrderList);
                        }else{
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "JatsOrderNo @" + mOriginRequestMsg.getfOrderID() + " not found in BookOfMARTINOrderList");
                        }
                        
                    }else{
                        //.???:
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }else{
                    //.???:
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @ mEveryRequest null");
                }
                
            }else{
                //.???:
                //. masukin log
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @ by vOrderToken 0");
            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }

}