/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataQuoteStatusReport;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancel;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReply;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackProcessor;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINNegDealList;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataQuoteStatusReport {
    public final static FIX5JonecWorkDataQuoteStatusReport getInstance = new FIX5JonecWorkDataQuoteStatusReport();
    
    public FIX5JonecWorkDataQuoteStatusReport() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataQuoteStatusReport mInputMsgRequest){
        try {
            if ((controller != null) && (mInputMsgRequest != null)){
                String zBrokerRef = mInputMsgRequest.getfQuoteRespID().trim();
                if (StringHelper.isNullOrEmpty(mInputMsgRequest.getfQuoteRespID())) {
                    zBrokerRef = mInputMsgRequest.getfQuoteID();
                }
                long vOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(zBrokerRef);
                if (mInputMsgRequest.getfQuoteStatus().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.QUOTE_STATUS_CANCELLED)) {
                    //.20260813: broker ref response order quote dari orderan kita ada di QuoteRespID (broker ref milik kita)
                    //.sedangkan broker ref response order quote dari broker lain ada di QuoteID (broker ref milik broker lain)
                    //.ketika dapat quote status report dari quote response orang lain, maka kirim ke QRIDataNegDealList dengan quote report type QuoteResponse supaya diproses withdraw pada tabel [portfolio].[QuoteResponseList]
                    //.ketika dapat quote status report dari quote response kita, maka kirim ke ORIDataOrderCancelReply dengan mengirimkan status canceled
                    //.catat ke log 
                    
                    //.20260818: ketika dapat quote status report dari quote response kita, cukup catat di log saja tidak perlu kirim apa-apa ke TS
                    if (vOrderToken > 0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Stock-> "+mInputMsgRequest.getfSymbol() +", brokerRef-> "+ zBrokerRef + ", status-> Cancelled/Expired.");
//                        SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
//                        SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
//
//                        //.untuk handle ack dari cancel
//                        if (mEveryRequest != null) {
//                            //.ambil dari order sumbernya
//                            if (mEveryRequest.getIdxMessage() instanceof ORIDataNegotiationDealCancel){
//                                long vOrderTokenOriginalNewOrder = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(((ORIDataNegotiationDealCancel)mEveryRequest.getIdxMessage()).getfOrigClOrdID());
//                                if (vOrderTokenOriginalNewOrder > 0) {
//                                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderTokenOriginalNewOrder);
//                                }
//
//                            } else if (mEveryRequest.getIdxMessage() instanceof ORIDataNewOrder){
//                                long vOrderTokenOriginalNewOrder = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(((ORIDataNewOrder)mEveryRequest.getIdxMessage()).getfClOrdID());
//                                if (vOrderTokenOriginalNewOrder > 0) {
//                                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderTokenOriginalNewOrder);
//                                }
//
//                            }
//                        }
//
//                        if ((mOriginRequest != null) && (mEveryRequest != null)){
//                            if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){
//                                ORIDataNewOrder mOriginRequestMsg = ((ORIDataNewOrder)mOriginRequest.getIdxMessage());
//                                ORIDataOrderCancelReply mReplyMsg = new ORIDataOrderCancelReply(new HashMap());
//                                mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
//                                mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
//                                //. semua response
//                                mReplyMsg.setfOrderCancelReplyType(ORIDataOrderCancelReply.ORIOrderCancelReplyType.OK);
//                                mReplyMsg.setfOrderID("0");              
//                                mReplyMsg.setfExecRefID(mOriginRequestMsg.getfClOrdID());
//                                mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfSendingTime()));
//                                mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_CANCEL);
//                                mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_CANCELLED);
//                                mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_CANCELLED);
//                                mReplyMsg.setfSymbol(" ");
//                                mReplyMsg.setfSide(" ");
//                                mReplyMsg.setfLeavesQty(0);
//                                mReplyMsg.setfCumQty(0);
//                                mReplyMsg.setfAvgPx(0);
//                                //////mReplyMsg.setfHandlInst(ORIFieldValue.HANDLINST_NORMAL);
//                                mReplyMsg.setfHandlInst(mOriginRequestMsg.getfSymbolSfx().equals(ORIDataConst.ORIFieldValue.BOARD_NG) ? ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT : ORIDataConst.ORIFieldValue.HANDLINST_NORMAL);
//                                mReplyMsg.setfText("");
//                                mReplyMsg.setfLastPx(0);
//                                mReplyMsg.setfLastShares(0);
//
//                                JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
//                                if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
//                                    if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
//                                        //... .
//                                    }else{
//                                        //.???:
//                                        //. TODO : handle lewat Martin
//                                        //. masukin log
//                                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
//                                    }
//                                }else{
//                                    //.???:
//                                    //. TODO : handle lewat Martin
//                                    //. masukin log
//                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
//                                }
//                            }
//                        }
                    } else {
                        QRIDataNegDealListMessage mNegDealListMsg = new QRIDataNegDealListMessage(new HashMap());
            
                        mNegDealListMsg.setfReportType(QRIDataConst.ReportType.QUOTE_RESPONSE.value);
                        mNegDealListMsg.setfOrderID(mInputMsgRequest.getfQuoteRespNo());
                        mNegDealListMsg.setfClOrdID(mInputMsgRequest.getfQuoteID()); 

                        mNegDealListMsg.setfClientID("");
                        mNegDealListMsg.setfExecBroker("");
                        mNegDealListMsg.setfContraBroker("");
                        mNegDealListMsg.setfContraTrader("");

                        mNegDealListMsg.setfNoContraBrokers(1);
                        mNegDealListMsg.setfExecID(StringHelper.toInt(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfSendingTime())));
                        mNegDealListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                        mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_CANCELLED);
                        mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.WITHDRAWN_DEAL.getValue());
                        mNegDealListMsg.setfAccount(ORIDataConst.ORIFieldValue.ACCOUNT_I);
                        mNegDealListMsg.setfFutSettDate("");
                        mNegDealListMsg.setfSettlDeliveryType("");
                        mNegDealListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                        mNegDealListMsg.setfSymbolSfx("0");
                        mNegDealListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                        mNegDealListMsg.setfSide(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL);
                        mNegDealListMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfPrice()));
                        mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfSendingTime()));
                        mNegDealListMsg.setfLastPx(0);
                        mNegDealListMsg.setfLeavesQty(0);
                        mNegDealListMsg.setfCumQty(0);
                        mNegDealListMsg.setfAvgPx(0);
                        mNegDealListMsg.setfText("");
                        mNegDealListMsg.setfClearingAccount(" ");
                        mNegDealListMsg.setfComplianceID("");
                        mNegDealListMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));

                        //. save orderlist ke memory martin
                        BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
                        //. broadcast orderlist via martin
                        BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
                    }
                    
                    
                } else if (mInputMsgRequest.getfQuoteStatus().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.QUOTE_STATUS_REJECTED)
                        || !StringHelper.isNullOrEmpty(mInputMsgRequest.getfQuoteRejectReason())) {
                    
                    
                    if (vOrderToken > 0){
                        SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                        SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);

                        //.untuk handle ack dari cancel
                        if (mEveryRequest != null) {
                            //.ambil dari order sumbernya
                            if (mEveryRequest.getIdxMessage() instanceof ORIDataNegotiationDealCancel){
                                long vOrderTokenOriginalNewOrder = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(((ORIDataNegotiationDealCancel)mEveryRequest.getIdxMessage()).getfOrigClOrdID());
                                if (vOrderTokenOriginalNewOrder > 0) {
                                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderTokenOriginalNewOrder);
                                }

                            } else if (mEveryRequest.getIdxMessage() instanceof ORIDataNewOrder){
                                long vOrderTokenOriginalNewOrder = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(((ORIDataNewOrder)mEveryRequest.getIdxMessage()).getfClOrdID());
                                if (vOrderTokenOriginalNewOrder > 0) {
                                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderTokenOriginalNewOrder);
                                }

                            }
                        }

                        if ((mOriginRequest != null) && (mEveryRequest != null)){
                            if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){
                                ORIDataNewOrder mOriginRequestMsg = ((ORIDataNewOrder)mOriginRequest.getIdxMessage());

                                ORIDataNegotiationDealReply mReplyMsg = new ORIDataNegotiationDealReply(new HashMap());
                                mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                mReplyMsg.setfNegotiationDealReplyType(ORIDataNegotiationDealReply.ORINegotiationDealReplyType.ReplyBAD);

                                ///mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportRefID(),false));
                                mReplyMsg.setfOrderID("0");
                                mReplyMsg.setfClOrdID(mInputMsgRequest.getfQuoteRespID());

                                mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfSendingTime()));
                                mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
                                mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_REJECTED);
                                mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
                                mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                                mReplyMsg.setfLeavesQty(0);
                                mReplyMsg.setfCumQty(0);
                                mReplyMsg.setfAvgPx(0);
                                mReplyMsg.setfHandlInst(ORIDataConst.ORIFieldValue.HANDLINST_NEGOTIATIONDEAL);
                                mReplyMsg.setfText(mInputMsgRequest.getfRejectText());
        //                        if (mInputMsgRequest.getfText().isEmpty()) {
        //                            mReplyMsg.setfText(mInputMsgRequest.getfRejectText());
        //                        } else {
        //                            mReplyMsg.setfText(mInputMsgRequest.getfText());
        //                        }

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
                            } else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel){
                                ORIDataOrderCancel mOriginRequestMsg = ((ORIDataOrderCancel)mOriginRequest.getIdxMessage());

                                ORIDataNegotiationDealReply mReplyMsg = new ORIDataNegotiationDealReply(new HashMap());
                                mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                mReplyMsg.setfNegotiationDealReplyType(ORIDataNegotiationDealReply.ORINegotiationDealReplyType.ReplyBAD);

                                ///mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportRefID(),false));
                                mReplyMsg.setfOrderID("0");
                                mReplyMsg.setfClOrdID(mInputMsgRequest.getfQuoteRespID());

                                mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfSendingTime()));
                                mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
                                mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_REJECTED);
                                mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
                                mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                                mReplyMsg.setfLeavesQty(0);
                                mReplyMsg.setfCumQty(0);
                                mReplyMsg.setfAvgPx(0);
                                mReplyMsg.setfHandlInst(ORIDataConst.ORIFieldValue.HANDLINST_NEGOTIATIONDEAL);
                                mReplyMsg.setfText(mInputMsgRequest.getfRejectText());
        //                        if (mInputMsgRequest.getfText().isEmpty()) {
        //                            mReplyMsg.setfText(mInputMsgRequest.getfRejectText());
        //                        } else {
        //                            mReplyMsg.setfText(mInputMsgRequest.getfText());
        //                        }

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
                            }



                        }
                    }
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                }
                
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
}
