/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataExecutionReport;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
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
import com.itm.idx.data.qri.util.StringUtil;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackProcessor;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINOrderList;
import java.util.HashMap;

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
            System.out.println(mInputMsgRequest.msgDataToString());
            if (controller != null){
                
                long vOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfClOrdID());
                long vPrevOrderToken = 0;
                
                //. check if amend
                if (mInputMsgRequest.getfOrigClOrdID() != null && !mInputMsgRequest.getfOrigClOrdID().equalsIgnoreCase("")){
                    //. amend / cancel
                    vPrevOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfOrigClOrdID());
                }
                System.out.println("vOrderToken = " + vOrderToken);
                System.out.println("vPrevOrderToken = " + vPrevOrderToken);
                
                        
                SheetOfJONECSimOriginRequest mOriginRequest = null;
                SheetOfJONECSimEveryRequest mEveryRequest = null;
                System.out.println("mInputMsgRequest.getfClOrdID() = " + mInputMsgRequest.getfClOrdID());
                System.out.println("mInputMsgRequest.getfOrigClOrdID() = " + mInputMsgRequest.getfOrigClOrdID());
                System.out.println("vOrderToken = " + vOrderToken);
                
                if (vOrderToken > 0 && vPrevOrderToken <= 0){
                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                    mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                }else if (vOrderToken > 0 && vPrevOrderToken > 0){  
                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vPrevOrderToken);
                    mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                }
                
                System.out.println("mOriginRequest = " + mOriginRequest);
                System.out.println("mEveryRequest = " + mEveryRequest);
                System.out.println("mOriginRequest = " + ((mOriginRequest != null) ? mOriginRequest.getIdxMessage() : null));
                
                if ((vOrderToken > 0) && (mOriginRequest != null) && (mEveryRequest != null)){
                    if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){
                        System.out.println("mOriginRequest = mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder");
                        ORIDataNewOrder mOriginRequestMsg = ((ORIDataNewOrder)mOriginRequest.getIdxMessage());
                        if (mOriginRequestMsg.getfHandlInst() == ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT){
                            
                            SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                            if (mCalcQty == null){
                                mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                            }

                            mCalcQty.setQtyOrder(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));
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
                            }
                            
                            //.backup:
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



                        }else{
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @fix5 execution report(new) invalid origin HandlInst from ClOrdID:" + mInputMsgRequest.getfClOrdID() + "[" + mOriginRequestMsg.getfHandlInst() + "]");
                        }
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend){
                        
                        System.out.println("mOriginRequest = mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend");
                        
                        SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                        if (mCalcQty == null){
                            mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                        }
                        
                        SheetOfJONECSimCalcQty mPrevCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vPrevOrderToken);
                        
                        ORIDataOrderAmend mOriginRequestMsg = ((ORIDataOrderAmend)mOriginRequest.getIdxMessage());
                        
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
                        
                        mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfExecID()));
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
                            System.out.println("mReplyMsg.msgToString() Data = " + mReplyMsg.msgToString());
                        
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
                        
                        
                        System.out.println("mOriginRequestMsg Data = " + mOriginRequestMsg.msgDataToString());
                        System.out.println("mOriginRequestMsg.getfHandlInst() = " + mOriginRequestMsg.getfHandlInst());
                        if ((mOriginRequestMsg.getfHandlInst() == ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT) 
                            || (mOriginRequestMsg.getfHandlInst() == ORIDataConst.ORIFieldValue.HANDLINST_NORMAL)){
                            System.out.println("Process 1");
                        
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
                            System.out.println("Process 2");
                            //. save orderlist ke memory martin
                            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOrderListMsg);
                            //. broadcast orderlist via martin
                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOrderListMsg);
                            System.out.println("mOrderListMsg.msgToString() " + mOrderListMsg.msgToString());
                            
                            System.out.println("Process 3");
                            
                            //.backup:
                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);
                            
                        }else{
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @fix5 execution report(new) invalid origin HandlInst from ClOrdID:" + mInputMsgRequest.getfClOrdID() + "[" + mOriginRequestMsg.getfHandlInst() + "]");
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
