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
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.util.StringUtil;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.structs.OUCHMsgOrderReplaced;
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
public class JONECSimMsgMemWorkReplaced {
    //.single instance:
    public final static JONECSimMsgMemWorkReplaced getInstance = new JONECSimMsgMemWorkReplaced();
    
    public JONECSimMsgMemWorkReplaced() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(SheetOfOUCHBase mSheet, OUCHMsgOrderReplaced mMessage){
        try{
            long lSeqLatestSaved = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestSaved();
            long lSeqLatestReceived = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestReceived();
            if (lSeqLatestReceived <= lSeqLatestSaved){ //. skip
                return;
            }
            
            long vPrevOrderToken = mMessage.getPreviousOrderToken();
            long vNextOrderToken = mMessage.getReplacementOrderToken();
           
            if ((vPrevOrderToken > 0) && (vNextOrderToken > 0)){
                
                SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vPrevOrderToken);
                SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vNextOrderToken);
                
                if ((mOriginRequest != null) && (mEveryRequest != null)){
                    if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){
                        //.???:
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend){
                        
                        SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vNextOrderToken);
                        if (mCalcQty == null){
                            mCalcQty = new SheetOfJONECSimCalcQty(vNextOrderToken);
                            BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                        }
                        
                        SheetOfJONECSimCalcQty mPrevCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vPrevOrderToken);
                        
                        ORIDataOrderAmend mOriginRequestMsg = ((ORIDataOrderAmend)mOriginRequest.getIdxMessage());
                        
                        mCalcQty.setQtyOrder(mMessage.getQuantity());
                        if (mPrevCalcQty != null){
                            mCalcQty.setQtyMatch(mPrevCalcQty.getQtyMatch());
                            //. set status baru = status lama
                            mCalcQty.setOrderStatus(mPrevCalcQty.getOrderStatus());
                            //. set status lama = amend
                            mPrevCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_REPLACED);
                        }
                        //.soon
//                        mCalcQty.setJatsOrderNo(StringHelper.fromLong(mMessage.getOrderReferenceNumber()));
                        mCalcQty.setOriJatsOrderNo(mOriginRequestMsg.getfOrderID());
                        mCalcQty.setBrokerRef(mOriginRequestMsg.getfClOrdID());
                        mCalcQty.setOriBrokerRef(mOriginRequestMsg.getfOrigClOrdID());
                        
                        //.backup:
                        BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vNextOrderToken, mCalcQty);
                        if (mPrevCalcQty != null){
                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vPrevOrderToken, mPrevCalcQty);
                        }
                        

                        
                        ORIDataOrderAmendReply mReplyMsg = new ORIDataOrderAmendReply(new HashMap());
                        mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                        mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                        mReplyMsg.setfOrderAmendReplyType(ORIDataOrderAmendReply.ORIOrderAmendReplyType.OK);
                        //.soon
//                        mReplyMsg.setfOrderID(StringHelper.fromLong(mMessage.getOrderReferenceNumber()));
                        mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                        mReplyMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
                        mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_CORRECT);
                        mReplyMsg.setfExecRefID(mOriginRequestMsg.getfOrigClOrdID());
                        mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_REPLACEMENT);
                        mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REPLACED);
                        mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                        mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                        mReplyMsg.setfOrderQty(mMessage.getQuantity());
                        mReplyMsg.setfLeavesQty(mCalcQty.getQtyLeave()); 
                        mReplyMsg.setfCumQty(mCalcQty.getQtyMatch());
                        mReplyMsg.setfAvgPx(0);
                        mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                        mReplyMsg.setfLastPx(mOriginRequestMsg.getfPrice());
                        mReplyMsg.setfLastShares(0);
                        
                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                //... .
                            }else{
                                //.???:
                                //. disisi klien akan menggantung, solusi : send ulang
                                //. todo : masukin log
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            }
                        }else{
                            //.???:
                            //. disisi klien akan menggantung, solusi : send ulang
                            //. todo : masukin log
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        }
                        
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
                        mOrderListMsg.setfSymbol(" ");
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
                        
                        //. old orderlist
                        if (mOldOrderList != null){
                            //. update status
                            mOldOrderList.setfOrdStatus(StringHelper.fromInt(mPrevCalcQty.getOrderStatus()));
                            //. update exec id
                            mOldOrderList.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
                            
                            //. broadcast old orderlist via martin
                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOldOrderList);
                        }else{
                            //. masukin log
                        }
                        
                        //. save orderlist ke memory martin
                        BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOrderListMsg);
                        //. broadcast orderlist via martin
                        BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOrderListMsg);
                        
                        

                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel){
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }else{
                        //.???:
                        //. todo : masukin log
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
