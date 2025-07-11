/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.client.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTradeCaptureReport;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal;
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINNegDealList;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class JONECSimWorkDataNegotiationDeal {
    //.single instance:
    public final static JONECSimWorkDataNegotiationDeal getInstance = new JONECSimWorkDataNegotiationDeal();
    
    public JONECSimWorkDataNegotiationDeal() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, ORIDataNegotiationDeal mInputMsgRequest){
        try{
            long vOriginOrderToken = BookOfJONECSimToken.getInstance.generateTrxToken(mInputMsgRequest.getfClOrdID());
            long vEveryOrderToken = vOriginOrderToken; //BrokerReferenceHelper.getOrderID_BrokerRef(mInputMsgRequest.getfClOrdID());
            
            if ((vOriginOrderToken > 0) && (vEveryOrderToken > 0)){
                //.save to memory:
                BookOfJONECSimOriginRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimOriginRequest(vOriginOrderToken, mInputMsgRequest));
                BookOfJONECSimEveryRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimEveryRequest(vEveryOrderToken, mInputMsgRequest));
                
                //.testonly:
                
                //.real:
                FIX5JonecDataTradeCaptureReport mFixMsg;
                String zFixMsg = "";
                FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLine();
                if (mTrxCtl != null){
                    if (null == mInputMsgRequest.getfNegotiationDealType()){
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }else switch (mInputMsgRequest.getfNegotiationDealType()) {
                        case Crossing:
                            mFixMsg = new FIX5JonecDataTradeCaptureReport(new HashMap());
                            mFixMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.TRADE_CAPTURE_REPORT);
                            mFixMsg.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                            mFixMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                            mFixMsg.setfSenderSubID(mTrxCtl.getTraderCode());
                            
                            mFixMsg.setfTradeReportID(mInputMsgRequest.getfClOrdID());
                            mFixMsg.setfTradeReportTransType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REPORT_TRANS_TYPE_NEW);
                            mFixMsg.setfTradeReportType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REPORT_TYPE_SUBMIT);
                            mFixMsg.setfSettlDate(mInputMsgRequest.getfSettlDate());
                            mFixMsg.setfDeliveryType(mInputMsgRequest.getfSettlDeliveryType().equals(ORIDataConst.ORIFieldValue.SETTLDELIVERYTYPE_VERSUS) ? FIX5JonecDataConst.FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT : FIX5JonecDataConst.FIX5JonecFieldValue.DELIVERY_TYPE_FREE_OF_PAYMENT );
                            mFixMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                            mFixMsg.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0", ""));
                            mFixMsg.setfLastPx(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                            mFixMsg.setfLastQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
                            mFixMsg.setfNoSides(StringHelper.fromInt(2));
                            
                            mFixMsg.setfSide1(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL );
                            mFixMsg.setfAccountType1(
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                );
                            
                            mFixMsg.setfNoPartyIDs1(StringHelper.fromInt(2));
                            /**
                             * 
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfClientID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER);
                            **/
                            //. hrn-20211215 : ref P. Adry, TradingID (SID) tidak terisi di IDX
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfComplianceID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            
                            //. ??? cara-1
                            //mFixMsg.setfPartyID1b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfClientID())) ? mInputMsgRequest.getfClientID().substring(0, 2).toUpperCase() : "");
                            //mFixMsg.setfPartyIDSource1b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            //mFixMsg.setfPartyRole1b(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM);
                            //. ??? cara-2
                            mFixMsg.setfPartyID1b(mInputMsgRequest.getfClientID());
                            mFixMsg.setfPartyIDSource1b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER);
                            
                            /******
                            mFixMsg.setfNoPartyIDs1(StringHelper.fromInt(2));
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfComplianceID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            mFixMsg.setfPartyID1b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfClientID())) ? mInputMsgRequest.getfClientID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource1b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1b(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM);
                            ******/
                            
                            mFixMsg.setfSide2(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY );
                            mFixMsg.setfAccountType2(
                                    mInputMsgRequest.getSfCounterpartAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                    mInputMsgRequest.getSfCounterpartAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                    mInputMsgRequest.getSfCounterpartAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                    mInputMsgRequest.getSfCounterpartAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                );
                            
                            mFixMsg.setfNoPartyIDs2(StringHelper.fromInt(2));
                            /**
                            mFixMsg.setfPartyID2a(mInputMsgRequest.getSfCounterpartUserID());
                            mFixMsg.setfPartyIDSource2a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2a(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER);
                            **/
                            //. hrn-20211215 : ref P. Adry, TradingID (SID) tidak terisi di IDX
                            mFixMsg.setfPartyID2a(mInputMsgRequest.getSfCounterpartUserID());
                            mFixMsg.setfPartyIDSource2a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER);
                            
                            mFixMsg.setfPartyID2b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getSfCounterpartUserID())) ? mInputMsgRequest.getSfCounterpartUserID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource2b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM);
                            
                            /******
                            mFixMsg.setfNoPartyIDs2(StringHelper.fromInt(2));
                            mFixMsg.setfPartyID2a(mInputMsgRequest.getfComplianceID());
                            mFixMsg.setfPartyIDSource2a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2a(FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            mFixMsg.setfPartyID2b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getSfCounterpartUserID())) ? mInputMsgRequest.getSfCounterpartUserID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource2b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2b(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM);
                            ******/
                            
                            zFixMsg = mFixMsg.msgToString();
                            zFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zFixMsg,true,true,mTrxCtl.getConnectionName());
                            
                            if (!mTrxCtl.sendMessageDirect(zFixMsg)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send");
                            }
                            
                            break;
                        case TwoSide:
                            mFixMsg = new FIX5JonecDataTradeCaptureReport(new HashMap());
                            mFixMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.TRADE_CAPTURE_REPORT);
                            mFixMsg.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                            mFixMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                            mFixMsg.setfSenderSubID(mTrxCtl.getTraderCode());
                            
                            mFixMsg.setfTradeReportID(mInputMsgRequest.getfClOrdID());
                            mFixMsg.setfTradeReportTransType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REPORT_TRANS_TYPE_NEW);
                            mFixMsg.setfTradeReportType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REPORT_TYPE_SUBMIT);
                            mFixMsg.setfSettlDate(mInputMsgRequest.getfSettlDate());
                            mFixMsg.setfDeliveryType(mInputMsgRequest.getfSettlDeliveryType().equals(ORIDataConst.ORIFieldValue.SETTLDELIVERYTYPE_VERSUS) ? FIX5JonecDataConst.FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT : FIX5JonecDataConst.FIX5JonecFieldValue.DELIVERY_TYPE_FREE_OF_PAYMENT );
                            mFixMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                            mFixMsg.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0", ""));
                            mFixMsg.setfLastPx(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                            mFixMsg.setfLastQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
                            mFixMsg.setfNoSides(StringHelper.fromInt(2));
                            
                            mFixMsg.setfSide1(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL );
                            mFixMsg.setfAccountType1(
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                );
                            
                            mFixMsg.setfNoPartyIDs1(StringHelper.fromInt(2));
                            /**
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfClientID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER);
                            mFixMsg.setfPartyID1b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfClientID())) ? mInputMsgRequest.getfClientID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource1b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1b(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM);
                            **/
                            //. hrn-20211215 : ref P. Adry, TradingID (SID) tidak terisi di IDX
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfComplianceID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            
                            //.?? cara-1
                            /*
                            mFixMsg.setfPartyID1b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfClientID())) ? mInputMsgRequest.getfClientID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource1b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1b(FIX5JonecFieldValue.PARTY_ROLE_3_CLIENT_ID);
                            */
                            //.?? cara-2
                            mFixMsg.setfPartyID1b(mInputMsgRequest.getfClientID());
                            mFixMsg.setfPartyIDSource1b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER);
                            
                            
                            /******
                            mFixMsg.setfNoPartyIDs1(StringHelper.fromInt(2));
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfComplianceID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            mFixMsg.setfPartyID1b(mInputMsgRequest.getfClientID());
                            mFixMsg.setfPartyIDSource1b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1b(FIX5JonecFieldValue.PARTY_ROLE_3_CLIENT_ID);
                            ******/
                            
                            mFixMsg.setfSide2(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY );
                            mFixMsg.setfAccountType2(null); //.must be null;
                            
                            mFixMsg.setfNoPartyIDs2(StringHelper.fromInt(2));
                            mFixMsg.setfPartyID2a(mInputMsgRequest.getSfCounterpartUserID());
                            mFixMsg.setfPartyIDSource2a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER);
                            mFixMsg.setfPartyID2b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getSfCounterpartUserID())) ? mInputMsgRequest.getSfCounterpartUserID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource2b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM);
                            
                            /******
                            mFixMsg.setfNoPartyIDs2(StringHelper.fromInt(2));
                            mFixMsg.setfPartyID2a(mInputMsgRequest.getSfCounterpartUserID());
                            mFixMsg.setfPartyIDSource2a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2a(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER);
                            mFixMsg.setfPartyID2b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getSfCounterpartUserID())) ? mInputMsgRequest.getSfCounterpartUserID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource2b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2b(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM);
                            ******/
                            
                            zFixMsg = mFixMsg.msgToString();
                            zFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zFixMsg,true,true,mTrxCtl.getConnectionName());
                            
                            if (!mTrxCtl.sendMessageDirect(zFixMsg)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send");
                            }
                            
                            break;
                        case Confirmation:
                            QRIDataNegDealListMessage mNGList = BookOfMARTINNegDealList.getInstance.retrieveSheet(mInputMsgRequest.getfIOIId());
                            
                            mFixMsg = new FIX5JonecDataTradeCaptureReport(new HashMap());
                            mFixMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.TRADE_CAPTURE_REPORT);
                            mFixMsg.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                            mFixMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                            mFixMsg.setfSenderSubID(mTrxCtl.getTraderCode());
                            
                            mFixMsg.setfTradeReportID(mInputMsgRequest.getfClOrdID());
                            mFixMsg.setfTradeReportTransType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REPORT_TRANS_TYPE_NEW);
                            mFixMsg.setfTradeReportType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REPORT_TYPE_ACCEPT);
                            
                            mFixMsg.setfTradeReportRefID(FIX5CheckSumHelper.fixNegDealTradeReportID(StringHelper.fromLong(mInputMsgRequest.getfIOIId()),true));
                            mFixMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                            mFixMsg.setfDeliveryType(mInputMsgRequest.getfSettlDeliveryType().equals(ORIDataConst.ORIFieldValue.SETTLDELIVERYTYPE_VERSUS) ? FIX5JonecDataConst.FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT : FIX5JonecDataConst.FIX5JonecFieldValue.DELIVERY_TYPE_FREE_OF_PAYMENT );
                            mFixMsg.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0", ""));
                            mFixMsg.setfLastPx(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                            mFixMsg.setfLastQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
                            mFixMsg.setfNoSides(StringHelper.fromInt(2));
                            
                            mFixMsg.setfSide1(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL );
                            mFixMsg.setfAccountType1(
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                );
                            
                            mFixMsg.setfNoPartyIDs1(StringHelper.fromInt(2));
                            /**
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfClientID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER);
                            **/
                            
                            //. hrn-20211215 : ref P. Adry, TradingID (SID) tidak terisi di IDX
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfComplianceID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            //.?? cara-1
//                            mFixMsg.setfPartyID1b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfClientID())) ? mInputMsgRequest.getfClientID().substring(0, 2).toUpperCase() : "");
//                            mFixMsg.setfPartyIDSource1b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
//                            mFixMsg.setfPartyRole1b(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM);
                            //.?? cara-2
                            mFixMsg.setfPartyID1b(mInputMsgRequest.getfClientID());
                            mFixMsg.setfPartyIDSource1b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_36_ENTERING_TRADER);
                            
                            /***
                            mFixMsg.setfNoPartyIDs1(StringHelper.fromInt(2));
                            mFixMsg.setfPartyID1a(mInputMsgRequest.getfComplianceID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1a(FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            mFixMsg.setfPartyID1b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfClientID())) ? mInputMsgRequest.getfClientID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource1b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole1b(FIX5JonecFieldValue.PARTY_ROLE_7_ENTERING_FIRM);
                            ***/
                            
                            mFixMsg.setfSide2(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY );
                            mFixMsg.setfAccountType2(
                                    mInputMsgRequest.getSfCounterpartAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                    mInputMsgRequest.getSfCounterpartAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                    mInputMsgRequest.getSfCounterpartAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                    mInputMsgRequest.getSfCounterpartAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                );
                            
                            mFixMsg.setfNoPartyIDs2(StringHelper.fromInt(2));
                            /*
                            mFixMsg.setfPartyID2a((mNGList != null) ? mNGList.getfContraTrader() : "");
                            mFixMsg.setfPartyIDSource2a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2a(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER);
                            mFixMsg.setfPartyID2b((mNGList != null) ? mNGList.getfContraBroker() : "");
                            mFixMsg.setfPartyIDSource2b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2b(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM);
                            */
                            
                            //. hrn-20211215 : ref P. Adry, TradingID (SID) tidak terisi di IDX
                            mFixMsg.setfPartyID2a((mNGList != null) ? mNGList.getfContraTrader() : "");
                            mFixMsg.setfPartyIDSource2a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            mFixMsg.setfPartyID2b((mNGList != null) ? mNGList.getfContraBroker() : "");
                            mFixMsg.setfPartyIDSource2b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER);
                            
                            /***
                            mFixMsg.setfNoPartyIDs2(StringHelper.fromInt(3));
                            mFixMsg.setfPartyID2a(mInputMsgRequest.getfComplianceID());
                            mFixMsg.setfPartyIDSource2a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2a(FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            mFixMsg.setfPartyID2b((!StringHelper.isNullOrEmpty(mInputMsgRequest.getSfCounterpartUserID())) ? mInputMsgRequest.getSfCounterpartUserID() : ((mNGList != null) ? mNGList.getfContraBroker(): ""));
                            mFixMsg.setfPartyIDSource2b(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole2b(FIX5JonecFieldValue.PARTY_ROLE_37_CONTRA_TRADER);
                            
                            mFixMsg.setfPartyID3a((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfClientID())) ? mInputMsgRequest.getfClientID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource3a(FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mFixMsg.setfPartyRole3a(FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM);
                            ***/
                            
                            zFixMsg = mFixMsg.msgToString();
                            zFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zFixMsg,true,true,mTrxCtl.getConnectionName());
                            
                            if (!mTrxCtl.sendMessageDirect(zFixMsg)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send");
                            }
                            
                            break;
                        default:
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            break;
                    }
                }else{
                    //.???:
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                }
            }else{
                //.invalid input reference:
                //.???:
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
}
