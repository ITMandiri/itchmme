/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldValue;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTradeCaptureReport;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValue;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal.ORINegotiationDealType;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReply;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReply.ORINegotiationDealReplyType;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataTradeListMessage;
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
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINTradeList;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataTradeCaptureReport {
    //.single instance:
    public final static FIX5JonecWorkDataTradeCaptureReport getInstance = new FIX5JonecWorkDataTradeCaptureReport();
    
    public FIX5JonecWorkDataTradeCaptureReport() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataTradeCaptureReport mInputMsgRequest){
        try{
            if ((controller != null) && (mInputMsgRequest != null)){
                
                boolean bIsValid = false;
                boolean bIsCounterpart = false;
                boolean bIsInisiator = false;
                
                long vOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfTradeReportRefID());
                SheetOfJONECSimOriginRequest mOriginRequest = null;
                SheetOfJONECSimEveryRequest mEveryRequest = null;
                
                if (vOrderToken > 0){
                    mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                    mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                }
                //.*traderid = <ab>tr100*; *brokerid = <AB>;
                String zTraderID = "";
                String zBrokerID = "";
                String zCounterpartTraderID = "";
                String zCounterpartBrokerID = "";
                
                if (mInputMsgRequest.getfPartyRole1a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID1a();
                }
                if (mInputMsgRequest.getfPartyRole1b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID1b();
                }
                if (mInputMsgRequest.getfPartyRole2a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID2a();
                }
                if (mInputMsgRequest.getfPartyRole2b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID2b();
                }
                if (mInputMsgRequest.getfPartyRole3a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID3a();
                }
                if (mInputMsgRequest.getfPartyRole3b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID3b();
                }
                
                if (mInputMsgRequest.getfPartyRole1a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID1a();
                }
                if (mInputMsgRequest.getfPartyRole1b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID1b();
                }
                if (mInputMsgRequest.getfPartyRole2a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID2a();
                }
                if (mInputMsgRequest.getfPartyRole2b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID2b();
                }
                if (mInputMsgRequest.getfPartyRole3a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID3a();
                }
                if (mInputMsgRequest.getfPartyRole3b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID3b();
                }
                
                if (mInputMsgRequest.getfPartyRole1a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER)){
                    zCounterpartTraderID = mInputMsgRequest.getfPartyID1a();
                }
                if (mInputMsgRequest.getfPartyRole1b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER)){
                    zCounterpartTraderID = mInputMsgRequest.getfPartyID1b();
                }
                if (mInputMsgRequest.getfPartyRole2a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER)){
                    zCounterpartTraderID = mInputMsgRequest.getfPartyID2a();
                }
                if (mInputMsgRequest.getfPartyRole2b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER)){
                    zCounterpartTraderID = mInputMsgRequest.getfPartyID2b();
                }
                if (mInputMsgRequest.getfPartyRole3a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER)){
                    zCounterpartTraderID = mInputMsgRequest.getfPartyID3a();
                }
                if (mInputMsgRequest.getfPartyRole3b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER)){
                    zCounterpartTraderID = mInputMsgRequest.getfPartyID3b();
                }
                
                if (mInputMsgRequest.getfPartyRole1a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID1a();
                }
                if (mInputMsgRequest.getfPartyRole1b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID1b();
                }
                if (mInputMsgRequest.getfPartyRole2a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID2a();
                }
                if (mInputMsgRequest.getfPartyRole2b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID2b();
                }
                if (mInputMsgRequest.getfPartyRole3a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID3a();
                }
                if (mInputMsgRequest.getfPartyRole3b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID3b();
                }
                
                if (StringHelper.isNullOrEmpty(zTraderID)){
                    zTraderID = (!StringHelper.isNullOrEmpty(zBrokerID) ? (zBrokerID.toLowerCase()) : "" );
                }
                if (StringHelper.isNullOrEmpty(zCounterpartTraderID)){
                    zCounterpartTraderID = (!StringHelper.isNullOrEmpty(zCounterpartBrokerID) ? (zCounterpartBrokerID.toLowerCase()) : "" );
                }
                if ((StringHelper.isNullOrEmpty(zBrokerID)) && (!StringHelper.isNullOrEmpty(zTraderID))){
                    zBrokerID = (zTraderID.length() > 2 ? (zTraderID.substring(0, 2).toUpperCase()) : zTraderID.toUpperCase() );
                }
                if ((StringHelper.isNullOrEmpty(zCounterpartBrokerID)) && (!StringHelper.isNullOrEmpty(zCounterpartTraderID))){
                    zCounterpartBrokerID = (zCounterpartTraderID.length() > 2 ? (zCounterpartTraderID.substring(0, 2).toUpperCase()) : zCounterpartTraderID.toUpperCase() );
                }
                
                if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_ALLEGED)){
                    //.ALLEGED(INPUT):
                    ORIDataNegotiationDeal mOriginRequestMsg = null;
                    if ((vOrderToken > 0) && (mOriginRequest != null) && (mEveryRequest != null)){
                        if (mOriginRequest.getIdxMessage() instanceof ORIDataNegotiationDeal){
                            mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());
                            //.alleged dari broker sendiri (crossing) ataupun twoside sebagai inisiator:
                            if ((mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide())) && (mInputMsgRequest.getfTradeReportTransType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TRANS_TYPE_NEW))){
                                //.inisiator(???):
                                bIsValid = true;
                                bIsCounterpart = false;
                                bIsInisiator = true;
                                //.buat executionreport(reply):
                                ORIDataNegotiationDealReply mReplyMsg = new ORIDataNegotiationDealReply(new HashMap());
                                
                                mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                                mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                                mReplyMsg.setfNegotiationDealReplyType(ORINegotiationDealReplyType.ReplyOK);
                                
                                mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false));
                                
                                mReplyMsg.setfClOrdID(mInputMsgRequest.getfTradeReportRefID());
                                
                                mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                                mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_NEW);
                                mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_NEW);
                                mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_NEW);
                                mReplyMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                                mReplyMsg.setfSide(mInputMsgRequest.getfSide1());
                                mReplyMsg.setfSettlDate(mOriginRequestMsg.getfSettlDate());
                                mReplyMsg.setfSettlDeliveryType(mOriginRequestMsg.getfSettlDeliveryType());
                                mReplyMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                                mReplyMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                                mReplyMsg.setfLeavesQty(0);
                                mReplyMsg.setfCumQty(0);
                                mReplyMsg.setfAvgPx(0);
                                mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                                mReplyMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                                mReplyMsg.setfLastShares(0);
                                
                                JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                                if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                                    if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                        //... .
                                    }else{
                                        //.???:
                                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send direct");
                                    }
                                }else{
                                    //.???:
                                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send");
                                }
                            }else if (mInputMsgRequest.getfTradeReportTransType().equalsIgnoreCase("") || (!mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide()))){
                                //.destination(???):
                                bIsValid = true;
                                bIsCounterpart = true;
                            }else{
                                bIsValid = false;
                                bIsCounterpart = false;
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @alleged(input) invalid TradeReportTransType:" + mInputMsgRequest.getfTradeReportTransType());
                            }
                        }else{
                            bIsValid = false;
                            bIsCounterpart = false;
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @alleged(input) invalid BrokerRef from TradeReportRefID:" + mInputMsgRequest.getfTradeReportRefID());
                        }
                    }else{
                        //.alleged dari broker lain:
                        bIsValid = true;
                        bIsCounterpart = true;
                    }
                    if (bIsValid){
                        //.buat qrinegdeallist(martin):
                        QRIDataNegDealListMessage mNegDealListMsg = new QRIDataNegDealListMessage(new HashMap());
                        
                        mNegDealListMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false));
                        if (bIsInisiator){
                            mNegDealListMsg.setfClOrdID(mInputMsgRequest.getfTradeReportRefID());
                        }else{
                            mNegDealListMsg.setfClOrdID("");
                        }
                        mNegDealListMsg.setfClientID(zTraderID);
                        mNegDealListMsg.setfExecBroker(zBrokerID);
                        mNegDealListMsg.setfNoContraBrokers(1);
                        mNegDealListMsg.setfContraBroker(zCounterpartBrokerID);
                        mNegDealListMsg.setfContraTrader(zCounterpartTraderID);
                        mNegDealListMsg.setfExecID(StringHelper.toInt(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime())));
                        mNegDealListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                        mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                        if (bIsInisiator){
                            mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.CONFIRMED_DEAL.getValue());
                        }else{
                            mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.UNCONFIRMED_DEAL.getValue());
                        }
                        mNegDealListMsg.setfAccount(
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN) ? ORIFieldValue.ACCOUNT_I :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER) ? ORIFieldValue.ACCOUNT_A :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN) ? ORIFieldValue.ACCOUNT_S :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER) ? ORIFieldValue.ACCOUNT_F :
                                mInputMsgRequest.getfAccountType1()
                        );
                        mNegDealListMsg.setfFutSettDate((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSettlDate())) ? mInputMsgRequest.getfSettlDate() : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDate() : ""));
                        mNegDealListMsg.setfSettlDeliveryType((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfDeliveryType())) ? (mInputMsgRequest.getfDeliveryType().equalsIgnoreCase(FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT) ? ORIFieldValue.SETTLDELIVERYTYPE_VERSUS : ORIFieldValue.SETTLDELIVERYTYPE_FREE) : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDeliveryType() : ""));
                        mNegDealListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                        mNegDealListMsg.setfSymbolSfx("0" + mInputMsgRequest.getfSecuritySubType());
                        mNegDealListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                        mNegDealListMsg.setfSide(mInputMsgRequest.getfSide1());
                        mNegDealListMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                        mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                        mNegDealListMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                        mNegDealListMsg.setfLeavesQty(0);
                        mNegDealListMsg.setfCumQty(0);
                        mNegDealListMsg.setfAvgPx(0);
                        mNegDealListMsg.setfText("  " + mInputMsgRequest.getfTradeReportID() + "/" + mInputMsgRequest.getfExecID() + " ");
                        mNegDealListMsg.setfClearingAccount(" ");
                        mNegDealListMsg.setfComplianceID((mOriginRequestMsg != null) ? mOriginRequestMsg.getfComplianceID() : "");
                        mNegDealListMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));

                        //. save orderlist ke memory martin
                        BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
                        //. broadcast orderlist via martin
                        BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
                    }
                    //.???:
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INFO, "Found route @alleged(input)");
                }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_ACCEPT)){
                    //.CONFIRM:
                    //.???:
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @accept(confirm)");
                }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_DECLINE)){
                    //.DECLINE(REJECT):
                    ORIDataNegotiationDeal mOriginRequestMsg = null;                    
                    if ((vOrderToken > 0) && (mOriginRequest != null) && (mEveryRequest != null)){
                        if (mOriginRequest.getIdxMessage() instanceof ORIDataNegotiationDeal){
                            mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());
                            if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Crossing || mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.TwoSide)
                                && (mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
                                ){
                                //.inisiator(???):
                                bIsValid = true;
                                bIsCounterpart = false;
                                bIsInisiator = true;
                            }else if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Crossing || mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.TwoSide)
                                && (mInputMsgRequest.getfSide2().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
                                ){
                                //.confirmator(???):
                                bIsValid = true;
                                bIsCounterpart = true;
                                bIsInisiator = false;
                            }else if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Confirmation)
                                && (mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
                                ){
                                //.confirmator(???):
                                bIsValid = true;
                                bIsCounterpart = true;
                                bIsInisiator = false;
                            }else{
                                bIsValid = false;
                                bIsCounterpart = false;
                                bIsInisiator = false;
                            }
                        }else{
                            bIsValid = false;
                            bIsCounterpart = false;
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @decline(reject) invalid BrokerRef from TradeReportRefID:" + mInputMsgRequest.getfTradeReportRefID());
                        }
                    }else{
                        //.submit dari broker lain:
                        bIsValid = false;
                        bIsCounterpart = true;
                    }
                    if (bIsValid){
                        //.buat executionreport(reply):
                        ORIDataNegotiationDealCancelReply mReplyMsg = new ORIDataNegotiationDealCancelReply(new HashMap());
                        
                        mReplyMsg.setfBundleMessageVersion((mOriginRequestMsg != null) ? mOriginRequestMsg.getfBundleMessageVersion() : "");
                        mReplyMsg.setfBundleConnectionName((mOriginRequestMsg != null) ? mOriginRequestMsg.getfBundleConnectionName() : "");
                        mReplyMsg.setfNegotiationDealCancelReplyType(ORIDataNegotiationDealCancelReply.ORINegotiationDealCancelReplyType.OK);
                        
                        mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false));
                        
                        mReplyMsg.setfClOrdID(mInputMsgRequest.getfTradeReportRefID());
                        mReplyMsg.setfExecRefID(mInputMsgRequest.getfTradeReportRefID());
                        
                        mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                        mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_CANCEL);
                        mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_CANCELLED);
                        mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_CANCELLED);
                        mReplyMsg.setfSymbol(" ");
                        mReplyMsg.setfSide(" ");
                        mReplyMsg.setfLeavesQty(0);
                        mReplyMsg.setfCumQty(0);
                        mReplyMsg.setfAvgPx(0);
                        mReplyMsg.setfHandlInst((mOriginRequestMsg != null) ? mOriginRequestMsg.getfHandlInst() : ORIFieldValue.HANDLINST_NEGOTIATIONDEAL);
                        mReplyMsg.setfLastPx(0);
                        mReplyMsg.setfLastShares(0);
                        
                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                //... .
                            }else{
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send direct @submit(trade)");
                            }
                        }else{
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send @submit(trade)");
                        }
                        
                        //.buat qrinegdeallist(martin):
                        QRIDataNegDealListMessage mNegDealListMsg = BookOfMARTINNegDealList.getInstance.retrieveSheet(StringHelper.toLong(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(),false)));
                        if (mNegDealListMsg != null){
                            mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_CANCELLED);
                            mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.WITHDRAWN_DEAL.getValue());
                        }
                        
                        //. save orderlist ke memory martin
                        BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
                        //. broadcast orderlist via martin
                        BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
                        
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "Found route @decline(reject) and Valid");
                    }else{
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "Found route @decline(reject) but Invalid");
                    }
                    //.???:
                    //ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @decline(reject)");
                }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_SUBMIT)){
                    //.SUBMIT(TRADE):
                    ORIDataNegotiationDeal mOriginRequestMsg = null;
                    if ((vOrderToken > 0) && (mOriginRequest != null) && (mEveryRequest != null)){
                        if (mOriginRequest.getIdxMessage() instanceof ORIDataNegotiationDeal){
                            mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());
                            if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Crossing || mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.TwoSide)
                                && (mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
                                ){
                                //.inisiator(???):
                                bIsValid = true;
                                bIsCounterpart = false;
                                bIsInisiator = true;
                            }else if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Confirmation)
                                && (mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
                                ){
                                //.confirmator(???):
                                bIsValid = true;
                                bIsCounterpart = true;
                                bIsInisiator = false;
                            }else{
                                bIsValid = false;
                                bIsCounterpart = false;
                                bIsInisiator = false;
                            }
                        }else{
                            bIsValid = false;
                            bIsCounterpart = false;
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @submit(trade) invalid BrokerRef from TradeReportRefID:" + mInputMsgRequest.getfTradeReportRefID());
                        }
                    }else{
                        //.submit dari broker lain:
                        bIsValid = false;
                        bIsCounterpart = true;
                    }
                    if (bIsValid){
                        //.buat executionreport(reply):
                        ORIDataNegotiationDealReply mReplyMsg = new ORIDataNegotiationDealReply(new HashMap());

                        mReplyMsg.setfBundleMessageVersion((mOriginRequestMsg != null) ? mOriginRequestMsg.getfBundleMessageVersion() : "");
                        mReplyMsg.setfBundleConnectionName((mOriginRequestMsg != null) ? mOriginRequestMsg.getfBundleConnectionName() : "");
                        mReplyMsg.setfNegotiationDealReplyType(ORINegotiationDealReplyType.ConfirmationOK);
                        
                        mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false));

                        mReplyMsg.setfClOrdID(mInputMsgRequest.getfTradeReportRefID());

                        mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                        mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_NEW);
                        mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_NEW);
                        if ((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfTrdMatchID())) && (!mInputMsgRequest.getfTrdMatchID().equalsIgnoreCase("0"))){
                            mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_FULLY_MATCH);
                        }else{
                            mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_NEW);
                        }
                        mReplyMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                        mReplyMsg.setfSide(mInputMsgRequest.getfSide1());
                        mReplyMsg.setfSettlDate((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSettlDate())) ? mInputMsgRequest.getfSettlDate() : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDate() : ""));
                        mReplyMsg.setfSettlDeliveryType((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfDeliveryType())) ? (mInputMsgRequest.getfDeliveryType().equalsIgnoreCase(FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT) ? ORIFieldValue.SETTLDELIVERYTYPE_VERSUS : ORIFieldValue.SETTLDELIVERYTYPE_FREE) : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDeliveryType() : ""));
                        mReplyMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                        mReplyMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                        mReplyMsg.setfLeavesQty(0);
                        mReplyMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                        mReplyMsg.setfAvgPx(0);
                        mReplyMsg.setfHandlInst((mOriginRequestMsg != null) ? mOriginRequestMsg.getfHandlInst() : ORIFieldValue.HANDLINST_NEGOTIATIONDEAL);
                        mReplyMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                        mReplyMsg.setfLastShares(0);
                        
                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                //... .
                            }else{
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send direct @submit(trade)");
                            }
                        }else{
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send @submit(trade)");
                        }
                        
                        //.buat qrinegdeallist(martin):
                        boolean bNegDealListFirstFound = false;
                        QRIDataNegDealListMessage mNegDealListMsg = BookOfMARTINNegDealList.getInstance.retrieveSheet(StringHelper.toLong(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(),false)));
                        if (mNegDealListMsg == null){
                            mNegDealListMsg = new QRIDataNegDealListMessage(new HashMap());
                        }else{
                            bNegDealListFirstFound = true;
                        }
                        
                        if (!bNegDealListFirstFound){
                            mNegDealListMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false));
                        }
                        
                        mNegDealListMsg.setfClOrdID(mInputMsgRequest.getfTradeReportRefID());
                        mNegDealListMsg.setfClientID(zTraderID);
                        mNegDealListMsg.setfExecBroker(zBrokerID);
                        mNegDealListMsg.setfNoContraBrokers(1);
                        mNegDealListMsg.setfContraBroker(zCounterpartBrokerID);
                        mNegDealListMsg.setfContraTrader(zCounterpartTraderID);
                        mNegDealListMsg.setfExecID(StringHelper.toInt(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime())));
                        mNegDealListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                        
                        if ((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfTrdMatchID())) && (!mInputMsgRequest.getfTrdMatchID().equalsIgnoreCase("0"))){
                            mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NORMAL_MATCH);
                            mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.MATCHED_DEAL.getValue());
                        }else{
                            mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                            mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.CONFIRMED_DEAL.getValue());
                        }
                        
                        mNegDealListMsg.setfAccount(
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN) ? ORIFieldValue.ACCOUNT_I :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER) ? ORIFieldValue.ACCOUNT_A :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN) ? ORIFieldValue.ACCOUNT_S :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER) ? ORIFieldValue.ACCOUNT_F :
                                mInputMsgRequest.getfAccountType1()
                        );
                        
                        mNegDealListMsg.setfFutSettDate((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSettlDate())) ? mInputMsgRequest.getfSettlDate() : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDate() : ""));
                        mNegDealListMsg.setfSettlDeliveryType((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfDeliveryType())) ? (mInputMsgRequest.getfDeliveryType().equalsIgnoreCase(FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT) ? ORIFieldValue.SETTLDELIVERYTYPE_VERSUS : ORIFieldValue.SETTLDELIVERYTYPE_FREE) : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDeliveryType() : ""));
                        mNegDealListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                        mNegDealListMsg.setfSymbolSfx("0" + mInputMsgRequest.getfSecuritySubType());
                        mNegDealListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                        mNegDealListMsg.setfSide(mInputMsgRequest.getfSide1());
                        mNegDealListMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                        mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                        mNegDealListMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                        mNegDealListMsg.setfLeavesQty(0);
                        if ((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfTrdMatchID())) && (!mInputMsgRequest.getfTrdMatchID().equalsIgnoreCase("0"))){
                            mNegDealListMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                        }else{
                            mNegDealListMsg.setfCumQty(0);
                        }
                        mNegDealListMsg.setfAvgPx(0);
                        mNegDealListMsg.setfText("  " + mInputMsgRequest.getfTradeReportID() + "/" + mInputMsgRequest.getfExecID() + "/" + mInputMsgRequest.getfTrdMatchID() + " ");
                        mNegDealListMsg.setfClearingAccount(" ");
                        mNegDealListMsg.setfComplianceID((mOriginRequestMsg != null) ? mOriginRequestMsg.getfComplianceID() : "");
                        mNegDealListMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));

                        //. save orderlist ke memory martin
                        BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
                        //. broadcast orderlist via martin
                        BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
                        
                        //.buat qritradelist(martin):
                        QRIDataTradeListMessage mTradeListMsg = new QRIDataTradeListMessage(new HashMap());
                        mTradeListMsg.setfOrderID(StringHelper.toLong(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false)));
                        mTradeListMsg.setfClOrdID(mInputMsgRequest.getfTradeReportRefID());
                        mTradeListMsg.setfSecondaryOrderID(StringHelper.toLong(mInputMsgRequest.getfTrdMatchID()));
                        mTradeListMsg.setfTransactionTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                        mTradeListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                        mTradeListMsg.setfClientID(zTraderID);
                        mTradeListMsg.setfSide(mInputMsgRequest.getfSide1());
                        mTradeListMsg.setfExecBroker(zBrokerID);
                        mTradeListMsg.setfContraTrader(zCounterpartTraderID);
                        
                        mTradeListMsg.setfContraBroker(zCounterpartBrokerID);
                        mTradeListMsg.setfAccount(
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN) ? ORIFieldValue.ACCOUNT_I :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER) ? ORIFieldValue.ACCOUNT_A :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN) ? ORIFieldValue.ACCOUNT_S :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER) ? ORIFieldValue.ACCOUNT_F :
                                mInputMsgRequest.getfAccountType1()
                        );
                        mTradeListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                        mTradeListMsg.setfSymbolSfx("0" + mInputMsgRequest.getfSecuritySubType());
                        mTradeListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                        mTradeListMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                        mTradeListMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
                        mTradeListMsg.setfText("");
                        mTradeListMsg.setfClearingAccount(" ");
                        mTradeListMsg.setfFutSettDate((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSettlDate())) ? mInputMsgRequest.getfSettlDate() : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDate() : ""));
                        if ((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfTrdMatchID())) && (!mInputMsgRequest.getfTrdMatchID().equalsIgnoreCase("0"))){
                            mTradeListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NORMAL_MATCH);
                            mTradeListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.MATCHED_DEAL.getValue());
                        }else{
                            mTradeListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                            mTradeListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.CONFIRMED_DEAL.getValue());
                        }
                        mTradeListMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                        mTradeListMsg.setfNoContraBrokers(1);                        
                        mTradeListMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                        mTradeListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                        mTradeListMsg.setfLeavesQty(0);
                        mTradeListMsg.setfAvgPx(0);
                        mTradeListMsg.setfComplianceID((mOriginRequestMsg != null) ? mOriginRequestMsg.getfComplianceID() : "");
                        
                        //. save tradelist ke memory martin
                        BookOfMARTINTradeList.getInstance.addOrUpdateSheet(mTradeListMsg);
                        //. broadcast tradelist via martin
                        BookOfMARTINTradeList.getInstance.brodcastToSubscriber(mTradeListMsg);
                        
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INFO, "Found route @submit(trade) and Valid");
                    }else{
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "Found route @submit(trade) but Invalid");
                    }
                }else {
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
