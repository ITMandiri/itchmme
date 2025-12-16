/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldValue;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTradeCaptureReportAck;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancel;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReply;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
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
public class FIX5JonecWorkDataTradeCaptureReportAck {
    //.single instance:
    public final static FIX5JonecWorkDataTradeCaptureReportAck getInstance = new FIX5JonecWorkDataTradeCaptureReportAck();
    
    public FIX5JonecWorkDataTradeCaptureReportAck() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataTradeCaptureReportAck mInputMsgRequest){
        try{
            if ((controller != null) && (mInputMsgRequest != null)){
                System.err.println("TRADE CAPTURE REPORT ACK = "+mInputMsgRequest.msgToString());
                //.untuk stock dari ack ini dinormalize dulu
                mInputMsgRequest.setfSymbol(FIX5CheckSumHelper.fixNegDealNormalizeStock(mInputMsgRequest.getfSymbol()));
                long vOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfTradeReportID());
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
                            
                        }
                    }
                    
                    if ((mOriginRequest != null) && (mEveryRequest != null)){
                        
                        if (mInputMsgRequest.getfTrdRptStatus().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_STATUS_ACCEPTED)){
                            //... .
                            if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_SUBMIT)){
                                //.contoh: 58=(1128): Security is not currently trading
                                
                                ORIDataNegotiationDeal mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());

                                ORIDataNegotiationDealReply mReplyMsg = new ORIDataNegotiationDealReply(new HashMap());
                                mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                mReplyMsg.setfNegotiationDealReplyType(ORIDataNegotiationDealReply.ORINegotiationDealReplyType.ReplyOK);
                                
                                mReplyMsg.setfOrderID(mInputMsgRequest.getfTradeID());
                                mReplyMsg.setfClOrdID(mInputMsgRequest.getfTradeReportID());
                                
                                //.20251203: antisipasi jika transactTime tidak dikirim
                                if (StringHelper.isNullOrEmpty(mInputMsgRequest.getfTransactTime())) {
                                    mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfSendingTime()));
                                } else {
                                    mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                }
                                
                                mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
                                mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_NEW);
                                //.jika oneside order status = open, twoside order status = full match
                                if (!StringHelper.isNullOrEmpty(mInputMsgRequest.getfMatchStatus()) && mInputMsgRequest.getfMatchStatus().equalsIgnoreCase("1")) {
                                    mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_NEW);
                                } else {
                                    mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_FULLY_MATCH);
                                }
                                
                                mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                                mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                                mReplyMsg.setfLeavesQty(0);
                                mReplyMsg.setfCumQty(0);
                                mReplyMsg.setfAvgPx(0);
                                mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                if (mInputMsgRequest.getfText().isEmpty()) {
                                    mReplyMsg.setfText(mInputMsgRequest.getfRejectText());
                                } else {
                                    mReplyMsg.setfText(mInputMsgRequest.getfText());
                                }
                                
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
                                //. jika oneside maka kirim negdeallist
                                if (mInputMsgRequest.getfTradeHandlingInstr().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_HANDLING_INSTR_ONEPARTYREPORTFORMATCHING)) {
                                    //.buat qrinegdeallist(martin):
                                    boolean bNegDealListFirstFound = false;
                                    QRIDataNegDealListMessage mNegDealListMsg = BookOfMARTINNegDealList.getInstance.retrieveSheet(StringHelper.toLong(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(),false)));
                                    if (mNegDealListMsg == null){
                                        mNegDealListMsg = new QRIDataNegDealListMessage(new HashMap());
                                    }else{
                                        bNegDealListFirstFound = true;
                                    }

                                    if (!bNegDealListFirstFound){
                                        //.????????????????????????? antara menggunakan orderID atau TradeReportID
                                        mNegDealListMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeID(), false));
                                    }

                                    mNegDealListMsg.setfClOrdID(mInputMsgRequest.getfTradeReportID());
                                    mNegDealListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                                    mNegDealListMsg.setfExecBroker(mOriginRequestMsg.getfSenderSubID());
                                    mNegDealListMsg.setfNoContraBrokers(1);
                                    //. ambil 2 karakter awal dari trader code
                                    mNegDealListMsg.setfContraBroker(mOriginRequestMsg.getSfCounterpartUserID().substring(0, 2).toUpperCase());
                                    mNegDealListMsg.setfContraTrader(mOriginRequestMsg.getSfCounterpartUserID());
                                    mNegDealListMsg.setfExecID(StringHelper.toInt(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime())));
                                    mNegDealListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);

                                    if ((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfMatchStatus())) && (mInputMsgRequest.getfMatchStatus().equalsIgnoreCase("0"))){
                                        mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NORMAL_MATCH);
                                        mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.MATCHED_DEAL.getValue());
                                    }else{
                                        mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                                        mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.CONFIRMED_DEAL.getValue());
                                    }

                                    mNegDealListMsg.setfAccount(
                                            mOriginRequestMsg.getfAccount().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN) ? ORIDataConst.ORIFieldValue.ACCOUNT_I :
                                            mOriginRequestMsg.getfAccount().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER) ? ORIDataConst.ORIFieldValue.ACCOUNT_A :
                                            mOriginRequestMsg.getfAccount().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN) ? ORIDataConst.ORIFieldValue.ACCOUNT_S :
                                            mOriginRequestMsg.getfAccount().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER) ? ORIDataConst.ORIFieldValue.ACCOUNT_F :
                                            mOriginRequestMsg.getfAccount()
                                    );

                                    mNegDealListMsg.setfFutSettDate((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDate() : "");
                                    mNegDealListMsg.setfSettlDeliveryType(mOriginRequestMsg.getfSettlDeliveryType());
                                    mNegDealListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                                    mNegDealListMsg.setfSymbolSfx("0" + mInputMsgRequest.getfSecuritySubType());
                                    mNegDealListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                                    mNegDealListMsg.setfSide(mOriginRequestMsg.getfSide());
                                    mNegDealListMsg.setfPrice(mOriginRequestMsg.getfPrice());
//                                    mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                    mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mOriginRequestMsg.getfTransactTime()));
//                                    mNegDealListMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                                    mNegDealListMsg.setfLeavesQty(0);
//                                    if ((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfTrdMatchID())) && (!mInputMsgRequest.getfTrdMatchID().equalsIgnoreCase("0"))){
//                                        mNegDealListMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
//                                    }else{
//                                        mNegDealListMsg.setfCumQty(0);
//                                    }
                                    mNegDealListMsg.setfAvgPx(0);
                                    mNegDealListMsg.setfText("  " + mInputMsgRequest.getfTradeReportID() + "/" + mInputMsgRequest.getfExecID() + " ");
                                    mNegDealListMsg.setfClearingAccount(" ");
                                    mNegDealListMsg.setfComplianceID((mOriginRequestMsg != null) ? mOriginRequestMsg.getfComplianceID() : "");
                                    mNegDealListMsg.setfOrderQty(mOriginRequestMsg.getfOrderQty());

                                    //. save orderlist ke memory martin
                                    BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
                                    //. broadcast orderlist via martin
                                    BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
                                }
                                
                            } else {
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "Unidentified trade report type = " +mInputMsgRequest.getfTradeReportType());
                            }
                        }else if (mInputMsgRequest.getfTrdRptStatus().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_STATUS_REJECTED)){
                            if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_SUBMIT)){
                                //.contoh: 58=(1128): Security is not currently trading
                                
                                ORIDataNegotiationDeal mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());

                                ORIDataNegotiationDealReply mReplyMsg = new ORIDataNegotiationDealReply(new HashMap());
                                mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                mReplyMsg.setfNegotiationDealReplyType(ORIDataNegotiationDealReply.ORINegotiationDealReplyType.ReplyBAD);
                                
                                ///mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportRefID(),false));
                                mReplyMsg.setfOrderID("0");
                                mReplyMsg.setfClOrdID(mInputMsgRequest.getfTradeReportID());
                                
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
                                if (mInputMsgRequest.getfText().isEmpty()) {
                                    mReplyMsg.setfText(mInputMsgRequest.getfRejectText());
                                } else {
                                    mReplyMsg.setfText(mInputMsgRequest.getfText());
                                }
                                
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
                                
                                QRIDataNegDealListMessage mNGList = BookOfMARTINNegDealList.getInstance.retrieveSheet(StringHelper.toLong(mInputMsgRequest.getfTradeReportRefID()));
                                if (mNGList != null){
                                    
                                    mNGList.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_CANCELLED);
                                    mNGList.setfOrdStatus(QRIDataConst.NegDealStatus.WITHDRAWN_DEAL.getValue());

                                    //. save orderlist ke memory martin
                                    BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNGList);
                                    //. broadcast orderlist via martin
                                    BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNGList);
                                }
                                
                            }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_ACCEPT)){
                                //.contoh: 58=(412): Unable to confirm - not an unconfirmed deal
                                
                                ORIDataNegotiationDeal mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());

                                ORIDataNegotiationDealReply mReplyMsg = new ORIDataNegotiationDealReply(new HashMap());
                                mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                mReplyMsg.setfNegotiationDealReplyType(ORIDataNegotiationDealReply.ORINegotiationDealReplyType.ReplyBAD);
                                
                                ///mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportRefID(),false));
                                mReplyMsg.setfOrderID("0");
                                mReplyMsg.setfClOrdID(mInputMsgRequest.getfTradeReportID());
                                
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
                                mReplyMsg.setfText(mInputMsgRequest.getfText());
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
                                
                                QRIDataNegDealListMessage mNGList = BookOfMARTINNegDealList.getInstance.retrieveSheet(StringHelper.toLong(mInputMsgRequest.getfTradeReportRefID()));
                                if (mNGList != null){
                                    
                                    mNGList.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_CANCELLED);
                                    mNGList.setfOrdStatus(QRIDataConst.NegDealStatus.WITHDRAWN_DEAL.getValue());

                                    //. save orderlist ke memory martin
                                    BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNGList);
                                    //. broadcast orderlist via martin
                                    BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNGList);
                                }
                                
                            }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_DECLINE)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                            }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_CANCEL)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                            }else{
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                            }
                        } else if (mInputMsgRequest.getfTrdRptStatus().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_STATUS_CANCELLED)){
                            //... .
                            if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_CANCEL)){
                                //.contoh: 58=(1128): Security is not currently trading
                                
                                ORIDataNegotiationDeal mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());
                                //.buat qrinegdeallist(martin):
                                boolean bNegDealListFirstFound = false;
                                QRIDataNegDealListMessage mNegDealListMsg = BookOfMARTINNegDealList.getInstance.retrieveSheet(StringHelper.toLong(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(),false)));
                                if (mNegDealListMsg == null){
                                    mNegDealListMsg = new QRIDataNegDealListMessage(new HashMap());
                                }else{
                                    bNegDealListFirstFound = true;
                                }

                                if (!bNegDealListFirstFound){
                                    //.????????????????????????? antara menggunakan orderID atau TradeReportID
                                    mNegDealListMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeID(), false));
                                }

                                mNegDealListMsg.setfClOrdID(mInputMsgRequest.getfTradeReportID());
                                mNegDealListMsg.setfClientID(mOriginRequestMsg.getfClientID());
                                mNegDealListMsg.setfExecBroker(mOriginRequestMsg.getfSenderSubID());
                                mNegDealListMsg.setfNoContraBrokers(1);
                                //. ambil 2 karakter awal dari trader code
                                mNegDealListMsg.setfContraBroker(mOriginRequestMsg.getSfCounterpartUserID().substring(0, 2).toUpperCase());
                                mNegDealListMsg.setfContraTrader(mOriginRequestMsg.getSfCounterpartUserID());
                                mNegDealListMsg.setfExecID(StringHelper.toInt(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime())));
                                mNegDealListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);

                                mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_CANCELLED);
                                mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.WITHDRAWN_DEAL.getValue());                                

                                mNegDealListMsg.setfAccount(
                                        mOriginRequestMsg.getfAccount().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN) ? ORIDataConst.ORIFieldValue.ACCOUNT_I :
                                        mOriginRequestMsg.getfAccount().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER) ? ORIDataConst.ORIFieldValue.ACCOUNT_A :
                                        mOriginRequestMsg.getfAccount().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN) ? ORIDataConst.ORIFieldValue.ACCOUNT_S :
                                        mOriginRequestMsg.getfAccount().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER) ? ORIDataConst.ORIFieldValue.ACCOUNT_F :
                                        mOriginRequestMsg.getfAccount()
                                );

                                mNegDealListMsg.setfFutSettDate((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDate() : "");
                                mNegDealListMsg.setfSettlDeliveryType(mOriginRequestMsg.getfSettlDeliveryType());
                                mNegDealListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                                mNegDealListMsg.setfSymbolSfx("0" + mInputMsgRequest.getfSecuritySubType());
                                mNegDealListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                                mNegDealListMsg.setfSide(mOriginRequestMsg.getfSide());
                                mNegDealListMsg.setfPrice(mOriginRequestMsg.getfPrice());
//                                    mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mOriginRequestMsg.getfTransactTime()));
//                                    mNegDealListMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                                mNegDealListMsg.setfLeavesQty(0);
//                                    if ((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfTrdMatchID())) && (!mInputMsgRequest.getfTrdMatchID().equalsIgnoreCase("0"))){
//                                        mNegDealListMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
//                                    }else{
//                                        mNegDealListMsg.setfCumQty(0);
//                                    }
                                mNegDealListMsg.setfAvgPx(0);
                                mNegDealListMsg.setfText("  " + mInputMsgRequest.getfTradeReportID() + "/" + mInputMsgRequest.getfExecID() + " ");
                                mNegDealListMsg.setfClearingAccount(" ");
                                mNegDealListMsg.setfComplianceID((mOriginRequestMsg != null) ? mOriginRequestMsg.getfComplianceID() : "");
                                mNegDealListMsg.setfOrderQty(mOriginRequestMsg.getfOrderQty());

                                //. save orderlist ke memory martin
                                BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
                                //. broadcast orderlist via martin
                                BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
                                
                            } else {
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "Unidentified trade report type = " +mInputMsgRequest.getfTradeReportType());
                            }
                        }
                        
                    }else{
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                    }
                }else{
                    //.???:
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                }
            }else{
                //.???:
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
}
