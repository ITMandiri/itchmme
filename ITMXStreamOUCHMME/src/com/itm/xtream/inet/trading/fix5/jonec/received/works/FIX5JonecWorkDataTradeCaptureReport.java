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
                //.untuk stock dari ack ini dinormalize dulu
                mInputMsgRequest.setfSymbol(FIX5CheckSumHelper.fixNegDealNormalizeStock(mInputMsgRequest.getfSymbol()));
                System.err.println("TRADE CAPTURE REPORT = "+mInputMsgRequest.msgToString());
                
                boolean bIsValid = false;
                boolean bIsCounterpart = false;
                boolean bIsInisiator = false;
                
//                long vOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfTradeReportRefID());
                long vOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfClOrderID());
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
                //.todo: apakah trade id perlu ditambahkan side atau tidak ????????????????????????????????????????????????????????????
//                String zTrdMatchIDNew = mInputMsgRequest.getfSide1() + mInputMsgRequest.getfTrdMatchID();
                
                String zTrdMatchIDNew = mInputMsgRequest.getfTrdMatchID();
                
                if (mInputMsgRequest.getfPartyRole1a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID1a(); //.SHJFE1
                }
                if (mInputMsgRequest.getfPartyRole1b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID1b();
                }
                if (mInputMsgRequest.getfPartyRole1c().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID1c();
                }
                if (mInputMsgRequest.getfPartyRole1d().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID1d();
                }
                if (mInputMsgRequest.getfPartyRole2a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID2a();
                }
                if (mInputMsgRequest.getfPartyRole2b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID2b();
                }
                if (mInputMsgRequest.getfPartyRole2c().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID2c();
                }
                if (mInputMsgRequest.getfPartyRole2d().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                    zTraderID = mInputMsgRequest.getfPartyID2d();
                }
                
                if (mInputMsgRequest.getfPartyRole1a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID1a(); //.SH
                }
                if (mInputMsgRequest.getfPartyRole1b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID1b();
                }
                if (mInputMsgRequest.getfPartyRole1c().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID1c();
                }
                if (mInputMsgRequest.getfPartyRole1d().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID1d();
                }
                if (mInputMsgRequest.getfPartyRole2a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID2a();
                }
                if (mInputMsgRequest.getfPartyRole2b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID2b();
                }
                if (mInputMsgRequest.getfPartyRole2c().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID2c();
                }
                if (mInputMsgRequest.getfPartyRole2d().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                    zBrokerID = mInputMsgRequest.getfPartyID2d();
                }
                
                if (mInputMsgRequest.getfPartyRole1a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID1a();
                }
                if (mInputMsgRequest.getfPartyRole1b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID1b();
                }
                if (mInputMsgRequest.getfPartyRole1c().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID1c();
                }
                if (mInputMsgRequest.getfPartyRole1d().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID1d();
                }
                if (mInputMsgRequest.getfPartyRole2a().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID2a();
                }
                if (mInputMsgRequest.getfPartyRole2b().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID2b();
                }
                if (mInputMsgRequest.getfPartyRole2c().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID2c();
                }
                if (mInputMsgRequest.getfPartyRole2d().equalsIgnoreCase(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM)){
                    zCounterpartBrokerID = mInputMsgRequest.getfPartyID2d();
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
                
                //.20251204: jika twoside bisa dipastikan bahwa zCounterpartTraderID = zTraderID, zCounterpartBrokerID = zBrokerID
                if (zBrokerID.equalsIgnoreCase(FIX5JonecFieldValue.SENDER_COMP_ID)) {
                    zCounterpartBrokerID = zBrokerID;
                    zCounterpartTraderID = zTraderID;
                }
                
                //. di pspp alleged new sudah pasti dari kiriman broker lain, sehingga broker ref bukan punya kita
                if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_ALLEGED)){
                    //.ALLEGED(INPUT):
                    //.buat qrinegdeallist(martin):
                    QRIDataNegDealListMessage mNegDealListMsg = new QRIDataNegDealListMessage(new HashMap());

                    mNegDealListMsg.setfOrderID(mInputMsgRequest.getfTradeID());
                    mNegDealListMsg.setfClOrdID(""); //.tidak ada broker refnya karena kiriman dari orang lain
                    
                    //. untuk oneside, pencatatan record broker dan trader dibalik
//                    mNegDealListMsg.setfClientID(zTraderID);
//                    mNegDealListMsg.setfExecBroker(zBrokerID);
//                    mNegDealListMsg.setfContraBroker(zCounterpartBrokerID);
//                    mNegDealListMsg.setfContraTrader(zCounterpartTraderID);

                    mNegDealListMsg.setfClientID(zCounterpartTraderID);
                    mNegDealListMsg.setfExecBroker(zCounterpartBrokerID);
                    mNegDealListMsg.setfContraBroker(zBrokerID);
                    mNegDealListMsg.setfContraTrader(zTraderID);
                    
                    mNegDealListMsg.setfNoContraBrokers(1);
                    mNegDealListMsg.setfExecID(StringHelper.toInt(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(StringHelper.isNullOrEmpty(mInputMsgRequest.getfTransactTime()) ? mInputMsgRequest.getfSendingTime() : mInputMsgRequest.getfTransactTime())));
                    mNegDealListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                    mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
                    mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.UNCONFIRMED_DEAL.getValue());
                    mNegDealListMsg.setfAccount(ORIFieldValue.ACCOUNT_I);
                    mNegDealListMsg.setfFutSettDate(mInputMsgRequest.getfSettlDate());
                    mNegDealListMsg.setfSettlDeliveryType(mInputMsgRequest.getfSettlMethod());
                    mNegDealListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                    mNegDealListMsg.setfSymbolSfx("0" + mInputMsgRequest.getfSecuritySubType());
                    mNegDealListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                    //.jika ada nego dari broker lain, side yang disimpan dibalik
                    mNegDealListMsg.setfSide(mInputMsgRequest.getfSide1().equals(ORIFieldValue.SIDE_BUY) ? FIX5JonecFieldValue.SIDE_SELL : FIX5JonecFieldValue.SIDE_BUY);
                    mNegDealListMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                    mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(StringHelper.isNullOrEmpty(mInputMsgRequest.getfTransactTime()) ? mInputMsgRequest.getfSendingTime() : mInputMsgRequest.getfTransactTime()));
                    mNegDealListMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                    mNegDealListMsg.setfLeavesQty(0);
                    mNegDealListMsg.setfCumQty(0);
                    mNegDealListMsg.setfAvgPx(0);
                    mNegDealListMsg.setfText("  " + mInputMsgRequest.getfTradeReportID() + "/" + mInputMsgRequest.getfExecID() + " ");
                    mNegDealListMsg.setfClearingAccount(" ");
                    mNegDealListMsg.setfComplianceID("");
                    mNegDealListMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));

                    //. save orderlist ke memory martin
                    BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
                    //. broadcast orderlist via martin
                    BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
                    
                //. untuk menghandle cancel negdeal dari broker lain    
                }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_ALLEGED_CANCEL)){
                    //.ALLEGED(CANCEL):
                    //.buat qrinegdeallist(martin):
                    QRIDataNegDealListMessage mNegDealListMsg = new QRIDataNegDealListMessage(new HashMap());

                    mNegDealListMsg.setfOrderID(mInputMsgRequest.getfTradeID());
                    mNegDealListMsg.setfClOrdID(""); //.tidak ada broker refnya karena kiriman dari orang lain
                    
                    //. untuk oneside, pencatatan record broker dan trader dibalik
//                    mNegDealListMsg.setfClientID(zTraderID);
//                    mNegDealListMsg.setfExecBroker(zBrokerID);
//                    mNegDealListMsg.setfContraBroker(zCounterpartBrokerID);
//                    mNegDealListMsg.setfContraTrader(zCounterpartTraderID);

                    mNegDealListMsg.setfClientID(zCounterpartTraderID);
                    mNegDealListMsg.setfExecBroker(zCounterpartBrokerID);
                    mNegDealListMsg.setfContraBroker(zBrokerID);
                    mNegDealListMsg.setfContraTrader(zTraderID);
                    
                    mNegDealListMsg.setfNoContraBrokers(1);
                    mNegDealListMsg.setfExecID(StringHelper.toInt(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(StringHelper.isNullOrEmpty(mInputMsgRequest.getfTransactTime()) ? mInputMsgRequest.getfSendingTime() : mInputMsgRequest.getfTransactTime())));
                    mNegDealListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
                    mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_CANCELLED);
                    mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.WITHDRAWN_DEAL.getValue());
                    mNegDealListMsg.setfAccount(ORIFieldValue.ACCOUNT_I);
                    mNegDealListMsg.setfFutSettDate(mInputMsgRequest.getfSettlDate());
                    mNegDealListMsg.setfSettlDeliveryType(mInputMsgRequest.getfSettlMethod());
                    mNegDealListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                    mNegDealListMsg.setfSymbolSfx("0" + mInputMsgRequest.getfSecuritySubType());
                    mNegDealListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                    //.jika ada nego dari broker lain, side yang disimpan dibalik
                    mNegDealListMsg.setfSide(mInputMsgRequest.getfSide1().equals(ORIFieldValue.SIDE_BUY) ? FIX5JonecFieldValue.SIDE_SELL : FIX5JonecFieldValue.SIDE_BUY);
                    mNegDealListMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                    mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                    mNegDealListMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
                    mNegDealListMsg.setfLeavesQty(0);
                    mNegDealListMsg.setfCumQty(0);
                    mNegDealListMsg.setfAvgPx(0);
                    mNegDealListMsg.setfText("  " + mInputMsgRequest.getfTradeReportID() + "/" + mInputMsgRequest.getfExecID() + " ");
                    mNegDealListMsg.setfClearingAccount(" ");
                    mNegDealListMsg.setfComplianceID("");
                    mNegDealListMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));

                    //. save orderlist ke memory martin
                    BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
                    //. broadcast orderlist via martin
                    BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);    
                }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_ACCEPT)){
                    //.CONFIRM:
                    //.???:
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @accept(confirm)");
                }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_DECLINE)){
                    //.DECLINE(REJECT):
//                    ORIDataNegotiationDeal mOriginRequestMsg = null;                    
//                    if ((vOrderToken > 0) && (mOriginRequest != null) && (mEveryRequest != null)){
//                        if (mOriginRequest.getIdxMessage() instanceof ORIDataNegotiationDeal){
//                            mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());
//                            if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Crossing || mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.TwoSide)
//                                && (mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
//                                ){
//                                //.inisiator(???):
//                                bIsValid = true;
//                                bIsCounterpart = false;
//                                bIsInisiator = true;
//                            }else if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Crossing || mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.TwoSide)
//                                && (mInputMsgRequest.getfSide2().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
//                                ){
//                                //.confirmator(???):
//                                bIsValid = true;
//                                bIsCounterpart = true;
//                                bIsInisiator = false;
//                            }else if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Confirmation)
//                                && (mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
//                                ){
//                                //.confirmator(???):
//                                bIsValid = true;
//                                bIsCounterpart = true;
//                                bIsInisiator = false;
//                            }else{
//                                bIsValid = false;
//                                bIsCounterpart = false;
//                                bIsInisiator = false;
//                            }
//                        }else{
//                            bIsValid = false;
//                            bIsCounterpart = false;
//                            //.???:
//                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @decline(reject) invalid BrokerRef from TradeReportRefID:" + mInputMsgRequest.getfTradeReportRefID());
//                        }
//                    }else{
//                        //.submit dari broker lain:
//                        bIsValid = false;
//                        bIsCounterpart = true;
//                    }
//                    if (bIsValid){
//                        //.buat executionreport(reply):
//                        ORIDataNegotiationDealCancelReply mReplyMsg = new ORIDataNegotiationDealCancelReply(new HashMap());
//                        
//                        mReplyMsg.setfBundleMessageVersion((mOriginRequestMsg != null) ? mOriginRequestMsg.getfBundleMessageVersion() : "");
//                        mReplyMsg.setfBundleConnectionName((mOriginRequestMsg != null) ? mOriginRequestMsg.getfBundleConnectionName() : "");
//                        mReplyMsg.setfNegotiationDealCancelReplyType(ORIDataNegotiationDealCancelReply.ORINegotiationDealCancelReplyType.OK);
//
//                        mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false));
//                        
//                        mReplyMsg.setfClOrdID(mInputMsgRequest.getfTradeReportRefID());
//                        mReplyMsg.setfExecRefID(mInputMsgRequest.getfTradeReportRefID());
//                        
//                        mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
//                        mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_CANCEL);
//                        mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_CANCELLED);
//                        mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_CANCELLED);
//                        mReplyMsg.setfSymbol(" ");
//                        mReplyMsg.setfSide(" ");
//                        mReplyMsg.setfLeavesQty(0);
//                        mReplyMsg.setfCumQty(0);
//                        mReplyMsg.setfAvgPx(0);
//                        mReplyMsg.setfHandlInst((mOriginRequestMsg != null) ? mOriginRequestMsg.getfHandlInst() : ORIFieldValue.HANDLINST_NEGOTIATIONDEAL);
//                        mReplyMsg.setfLastPx(0);
//                        mReplyMsg.setfLastShares(0);
//                        
//                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
//                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
//                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
//                                //... .
//                            }else{
//                                //.???:
//                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send direct @submit(trade)");
//                            }
//                        }else{
//                            //.???:
//                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send @submit(trade)");
//                        }
//                        
//                        //.buat qrinegdeallist(martin):
//                        QRIDataNegDealListMessage mNegDealListMsg = BookOfMARTINNegDealList.getInstance.retrieveSheet(StringHelper.toLong(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(),false)));
//                        if (mNegDealListMsg != null){
//                            mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_CANCELLED);
//                            mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.WITHDRAWN_DEAL.getValue());
//                        }
//                        
//                        //. save orderlist ke memory martin
//                        BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
//                        //. broadcast orderlist via martin
//                        BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
//                        
//                        //.???:
//                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "Found route @decline(reject) and Valid");
//                    }else{
//                        //.???:
//                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "Found route @decline(reject) but Invalid");
//                    }
                    //.???:
                    //ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @decline(reject)");
                }else if (mInputMsgRequest.getfTradeReportType().equalsIgnoreCase(FIX5JonecFieldValue.TRADE_REPORT_TYPE_SUBMIT)){
                    //.SUBMIT(TRADE):
                    ORIDataNegotiationDeal mOriginRequestMsg = null;
                    if ((vOrderToken > 0) && (mOriginRequest != null) && (mEveryRequest != null)){
                        if (mOriginRequest.getIdxMessage() instanceof ORIDataNegotiationDeal){
                            mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());
                            if ((mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.Crossing || mOriginRequestMsg.getfNegotiationDealType() == ORINegotiationDealType.TwoSide)
//                                && (mInputMsgRequest.getfSide1().equalsIgnoreCase(mOriginRequestMsg.getfSide()))
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @submit(trade) invalid BrokerRef from ClOrderID:" + mInputMsgRequest.getfClOrderID());
                        }
                    }else{
                        //.submit dari broker lain:
                        bIsValid = false;
                        bIsCounterpart = true;
                        System.out.println("Invalid TradeReportType Submit = "+mInputMsgRequest.msgToString());
                    }
                    if (bIsValid){
                        //.buat executionreport(reply):
                        //.20251203: sementara diremark, karena sudah dihandle dari fixOE (FIX5JonecWorkDataTradeCaptureReportAck)
//                        ORIDataNegotiationDealReply mReplyMsg = new ORIDataNegotiationDealReply(new HashMap());
//
//                        mReplyMsg.setfBundleMessageVersion((mOriginRequestMsg != null) ? mOriginRequestMsg.getfBundleMessageVersion() : "");
//                        mReplyMsg.setfBundleConnectionName((mOriginRequestMsg != null) ? mOriginRequestMsg.getfBundleConnectionName() : "");
//                        mReplyMsg.setfNegotiationDealReplyType(ORINegotiationDealReplyType.ConfirmationOK);
//                        
//                        //.20251203: ada 2 alternatif antara ambil tag 37 (orderID) / tradeReportID, untuk sementara yg dipakai orderID
////                        mReplyMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false));
//                        mReplyMsg.setfOrderID(mInputMsgRequest.getfOrderID());
//
//                        mReplyMsg.setfClOrdID(mInputMsgRequest.getfClOrderID());
//
//                        mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
//                        mReplyMsg.setfExecTransType(ORIFieldValue.EXECTRANSTYPE_NEW);
//                        mReplyMsg.setfExecType(ORIFieldValue.EXECTYPE_NEW);
//                        if ((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfTrdMatchID())) && (!mInputMsgRequest.getfTrdMatchID().equalsIgnoreCase("0"))){
//                            mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_FULLY_MATCH);
//                        }else{
//                            mReplyMsg.setfOrdStatus(ORIFieldValue.ORDSTATUS_NEW);
//                        }
//                        mReplyMsg.setfSymbol(mInputMsgRequest.getfSymbol());
//                        mReplyMsg.setfSide(mInputMsgRequest.getfSide1());
//                        mReplyMsg.setfSettlDate((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSettlDate())) ? mInputMsgRequest.getfSettlDate() : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDate() : ""));
//                        mReplyMsg.setfSettlDeliveryType((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSettlMethod())) ? (mInputMsgRequest.getfSettlMethod().equalsIgnoreCase(FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT) ? ORIFieldValue.SETTLDELIVERYTYPE_VERSUS : ORIFieldValue.SETTLDELIVERYTYPE_FREE) : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDeliveryType() : ""));
//                        mReplyMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
//                        mReplyMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
//                        mReplyMsg.setfLeavesQty(0);
//                        mReplyMsg.setfCumQty(StringHelper.toLong(mInputMsgRequest.getfLastQty()));
//                        mReplyMsg.setfAvgPx(0);
//                        mReplyMsg.setfHandlInst((mOriginRequestMsg != null) ? mOriginRequestMsg.getfHandlInst() : ORIFieldValue.HANDLINST_NEGOTIATIONDEAL);
//                        mReplyMsg.setfLastPx(StringHelper.toLong(mInputMsgRequest.getfLastPx()));
//                        mReplyMsg.setfLastShares(0);
//                        
//                        JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
//                        if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
//                            if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
//                                //... .
//                            }else{
//                                //.???:
//                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send direct @submit(trade)");
//                            }
//                        }else{
//                            //.???:
//                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send @submit(trade)");
//                        }
                        
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
                            mNegDealListMsg.setfOrderID(FIX5CheckSumHelper.fixNegDealTradeReportID(mInputMsgRequest.getfTradeReportID(), false));
                        }
                        
                        mNegDealListMsg.setfClOrdID(mInputMsgRequest.getfClOrderID());
                        mNegDealListMsg.setfClientID(zTraderID);
                        mNegDealListMsg.setfExecBroker(zBrokerID);
                        mNegDealListMsg.setfNoContraBrokers(1);
                        mNegDealListMsg.setfContraBroker(zCounterpartBrokerID);
                        mNegDealListMsg.setfContraTrader(zCounterpartTraderID);
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
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN) ? ORIFieldValue.ACCOUNT_I :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER) ? ORIFieldValue.ACCOUNT_A :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN) ? ORIFieldValue.ACCOUNT_S :
                                mInputMsgRequest.getfAccountType1().equals(FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER) ? ORIFieldValue.ACCOUNT_F :
                                mInputMsgRequest.getfAccountType1()
                        );
                        
                        mNegDealListMsg.setfFutSettDate((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSettlDate())) ? mInputMsgRequest.getfSettlDate() : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDate() : ""));
                        mNegDealListMsg.setfSettlDeliveryType((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSettlMethod())) ? (mInputMsgRequest.getfSettlMethod().equalsIgnoreCase(FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT) ? ORIFieldValue.SETTLDELIVERYTYPE_VERSUS : ORIFieldValue.SETTLDELIVERYTYPE_FREE) : ((mOriginRequestMsg != null) ? mOriginRequestMsg.getfSettlDeliveryType() : ""));
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
                        mNegDealListMsg.setfText("  " + mInputMsgRequest.getfTradeReportID() + "/" + mInputMsgRequest.getfExecID() + "/" + zTrdMatchIDNew + " ");
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
                        mTradeListMsg.setfClOrdID(mInputMsgRequest.getfClOrderID());
                        mTradeListMsg.setfSecondaryOrderID(StringHelper.toLong(zTrdMatchIDNew));
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
