/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.client.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataNewOrderSingle;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketController;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketMgr;
import com.itm.ts.ouch.consts.OUCHConsts;
import com.itm.ts.ouch.structs.OUCHMsgEnterOrder;
import com.itm.xtream.inet.trading.consts.ITMTradingServerConsts;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.replytimeout.mgr.ITMTradingServerReplyTimeOutMgr;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class JONECSimWorkDataNewOrder {
    //.single instance:
    public final static JONECSimWorkDataNewOrder getInstance = new JONECSimWorkDataNewOrder();
    
    public JONECSimWorkDataNewOrder() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public synchronized void doWork(ITMSocketChannel channel, ORIDataNewOrder mInputMsgRequest){
        try{
            long vOriginOrderToken = BookOfJONECSimToken.getInstance.generateTrxToken(mInputMsgRequest.getfClOrdID());
            long vEveryOrderToken = vOriginOrderToken; //BrokerReferenceHelper.getOrderID_BrokerRef(mInputMsgRequest.getfClOrdID());
            
            if ((vOriginOrderToken > 0) && (vEveryOrderToken > 0)){
                //. hrn: 2022-08-30 add memory untuk flag order timeout
                ITMTradingServerReplyTimeOutMgr.getInstance.addOrUpdateToken(vEveryOrderToken);
                
                //.save to memory:
                BookOfJONECSimOriginRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimOriginRequest(vOriginOrderToken, mInputMsgRequest));
                BookOfJONECSimEveryRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimEveryRequest(vEveryOrderToken, mInputMsgRequest));
                //.process:
                switch (mInputMsgRequest.getfHandlInst()) {
                    case ORIDataConst.ORIFieldValue.HANDLINST_NORMAL:
                        if (ITMTradingServerConsts.EngineSetup.FIX5_ONLY){
                            FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLine();
                            if (mTrxCtl != null){
                                FIX5JonecDataNewOrderSingle mNormalNewOrder = new FIX5JonecDataNewOrderSingle(new HashMap());
                                mNormalNewOrder.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.NEW_ORDER_SINGLE);
                                mNormalNewOrder.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                                mNormalNewOrder.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                                mNormalNewOrder.setfSenderSubID(mTrxCtl.getTraderCode());

                                mNormalNewOrder.setfClOrdID(mInputMsgRequest.getfClOrdID());
                                mNormalNewOrder.setfHandlInst(StringHelper.fromInt(mInputMsgRequest.getfHandlInst()));
//                                mNormalNewOrder.setfAccountType(
//                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
//                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
//                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
//                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
//                                        FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
//                                    );
//                                mNormalNewOrder.setfOrderRestrictions("X Y");
                                mNormalNewOrder.setfOrderQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
                                mNormalNewOrder.setfPrice(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                                mNormalNewOrder.setfSide(mInputMsgRequest.getfSide());
                                mNormalNewOrder.setfTransactTime(mNormalNewOrder.getfSendingTime());
                                mNormalNewOrder.setfText((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())) ? mInputMsgRequest.getfText() : "New Order " + mInputMsgRequest.getfSymbolSfx());
                                //////mNormalNewOrder.setfOrdType(FIX5JonecFieldValue.ORD_TYPE_LIMIT);
                                //////mNormalNewOrder.setfTimeInForce(
                                //////        (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION)) ? FIX5JonecFieldValue.TIME_IN_FORCE_SESSION : 
                                //////        (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY)) ? FIX5JonecFieldValue.TIME_IN_FORCE_DAY : 
                                //////        FIX5JonecFieldValue.TIME_IN_FORCE_DAY
                                //////        );
                                mNormalNewOrder.setfTimeInForce(mInputMsgRequest.getfTimeInForce()); //. TimeInForce Fix4.2 = TimeInForce 5
                                
                                if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_MARKET_NONSTOP)){  //. OrdType Fix4.2 <> OrdType Fix 5
                                    mNormalNewOrder.setfOrdType(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_MARKET);
                                }else{
                                    mNormalNewOrder.setfOrdType(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_LIMIT);
                                }

                                mNormalNewOrder.setfSymbol(mInputMsgRequest.getfSymbol()+"_RG");
//                                mNormalNewOrder.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0", ""));
                                mNormalNewOrder.setfNoPartyIDs(StringHelper.fromInt(3));
                                //.executing trader
                                mNormalNewOrder.setfPartyID1(mTrxCtl.getTraderCode());
                                mNormalNewOrder.setfPartyIDSource1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                                mNormalNewOrder.setfPartyRole1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER);
                                //.noPartySub
                                mNormalNewOrder.setfNoPartySubIDs(FIX5JonecDataConst.FIX5JonecFieldValue.NO_PARTY_SUB_IDS_EXECUTING_FIRM);
                                mNormalNewOrder.setfPartySubID(OUCHConsts.OUCHValue.ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE.toUpperCase()+"   ");                            
                                if (!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())){
                                    mNormalNewOrder.setfPartySubID(mInputMsgRequest.getfText());
                                }   
                                mNormalNewOrder.setfPartySubIDType(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_SUB_ID_TYPE);
                                //.executing firm
                                mNormalNewOrder.setfPartyID2(FIX5JonecDataConst.FIX5JonecFieldValue.SENDER_COMP_ID);
                                mNormalNewOrder.setfPartyIDSource2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                                mNormalNewOrder.setfPartyRole2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM);
                                //.customer account
                                mNormalNewOrder.setfPartyID3(mInputMsgRequest.getfComplianceID());
                                mNormalNewOrder.setfPartyIDSource3(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                                mNormalNewOrder.setfPartyRole3(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_24_CUSTOMER_ACCOUNT);

                                String zNormalNewOrderFixMsg = mNormalNewOrder.msgToString();
                                zNormalNewOrderFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zNormalNewOrderFixMsg,true,true,mTrxCtl.getConnectionName());

                                if (!mTrxCtl.sendMessageDirect(zNormalNewOrderFixMsg)){
                                    //.???:
                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send as normal");
                                }
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Found route @new order as normal");
                            }else{
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Found route @new order as normal but no controller");
                            }
                        }else{
                            OUCHMsgEnterOrder mNewOrder = new OUCHMsgEnterOrder();
                            mNewOrder.setType(OUCHConsts.OUCHMessageType.MESSAGETYPE_INBOUND_ENTER_ORDER);
                            mNewOrder.setOrderToken(vOriginOrderToken);
                            mNewOrder.setCustomerInfo("#" + mInputMsgRequest.getfClOrdID());//. 20210830 : hrn: supaya dianggap tidak valid di sisi FIX Lama (4.2)
                            mNewOrder.setClientAccount(mInputMsgRequest.getfComplianceID());
                            mNewOrder.setAttributes((short) OUCHConsts.OUCHValue.ATTRIBUTE_UNDEFINED);
                            switch (mInputMsgRequest.getfSide()) {
                                case ORIDataConst.ORIFieldValue.SIDE_BUY:
                                    mNewOrder.setSide(OUCHConsts.OUCHValue.ORDER_VERB_BUY);
                                    break;
                                case ORIDataConst.ORIFieldValue.SIDE_SELL:
                                    mNewOrder.setSide(OUCHConsts.OUCHValue.ORDER_VERB_SELL);
                                    break;
                                case ORIDataConst.ORIFieldValue.SIDE_SELL_SHORT:
                                    mNewOrder.setSide(OUCHConsts.OUCHValue.ORDER_VERB_SHORT_SELL);
                                    break;
                                case ORIDataConst.ORIFieldValue.SIDE_MARGIN_REQUEST:
                                    mNewOrder.setSide(OUCHConsts.OUCHValue.ORDER_VERB_BUY);
                                    mNewOrder.setAttributes((short) OUCHConsts.OUCHValue.ATTRIBUTE_MARGIN);
                                    break;
                                case ORIDataConst.ORIFieldValue.SIDE_PRICE_STABILIZATION:
                                    mNewOrder.setSide(OUCHConsts.OUCHValue.ORDER_VERB_BUY);
                                    mNewOrder.setAttributes((short) OUCHConsts.OUCHValue.ATTRIBUTE_PRICE_STABILIZATION);
                                    break;
                                default:
                                    break;
                            }
                            
                            mNewOrder.setExchangeInfo(OUCHConsts.OUCHValue.ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE.toUpperCase()+"   ");
                                                        
                            if (!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())){
                                mNewOrder.setExchangeInfo(mInputMsgRequest.getfText());
                            }
                            mNewOrder.setQuantity(mInputMsgRequest.getfOrderQty());
                            mNewOrder.setOrderBookId(StringHelper.toInt(mInputMsgRequest.getfSecurityID()));
                            //.20250814: untuk market order, sekarang ouch minta min long value
                            if (mInputMsgRequest.getfPrice() <= 0) {
                                mNewOrder.setPrice(Long.MIN_VALUE);
                            } else {
                                mNewOrder.setPrice((long) mInputMsgRequest.getfPrice());
                            }
                            
                            mNewOrder.setOpenClose((byte) OUCHConsts.OUCHValue.OPEN_CLOSE_DEFAULT);
                            mNewOrder.setDisplayQuantity(0);
                            //.??????????????????????????
                            mNewOrder.setOrderCapacity((byte) OUCHConsts.OUCHValue.ORDER_CAPACITY_INDIVIDUAL);
                            mNewOrder.setSelfMatchPreventionKey(0);
                            
                            if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_MARKET_NONSTOP)){
                               mNewOrder.setOrderType((byte) OUCHConsts.OUCHValue.ORDER_TYPE_MARKET);
                            } else if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP)){
                                mNewOrder.setOrderType((byte) OUCHConsts.OUCHValue.ORDER_TYPE_LIMIT);
                            } else if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_MARKET_TO_LIMIT)){
                                mNewOrder.setOrderType((byte) OUCHConsts.OUCHValue.ORDER_TYPE_MARKET_TO_LIMIT);
                            } else if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_BEST_ORDER)){
                                mNewOrder.setOrderType((byte) OUCHConsts.OUCHValue.ORDER_TYPE_BEST_ORDER);
                            } else if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_IMBALANCE)){
                                mNewOrder.setOrderType((byte) OUCHConsts.OUCHValue.ORDER_TYPE_IMBALANCE);
                            } else {
                                mNewOrder.setOrderType((byte) OUCHConsts.OUCHValue.ORDER_TYPE_MARKET);
                            }
                            
                            switch (mInputMsgRequest.getfTimeInForce()) {
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_GTS);
                                    //.???????????????????
                                    mNewOrder.setTimeInForceData((short) 1);
                                    break;
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_DAY);
                                    break;
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_IOC: //. FAK
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_FAK);
                                    break;
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_FOK:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_FOK);
                                    break;
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_GTD:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_DAYS);
                                    //.???????????????????
                                    mNewOrder.setTimeInForceData((short) 1);
                                    break;
                                default:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_DAY);
                                    break;
                            }   

                            byte[] btNewOrder = mNewOrder.buildMessage();

                            if ((btNewOrder != null) && (btNewOrder.length > 0)){
                                ITMSoupBinTCPOUCHPacketController mTrxCtl = ITMSoupBinTCPOUCHPacketMgr.getInstance.getNextActiveConnectionLine();
                                if (mTrxCtl != null){
                                    if (!mTrxCtl.getChannel().sendMessageDirect(btNewOrder)){
                                        //.???:
                                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                                    }
                                }else{ //. tidak konek ke OUCH
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
                        }
                        break;
                    case ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT:
                        FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLine();
                        if (mTrxCtl != null){
                            FIX5JonecDataNewOrderSingle mAdvNewOrder = new FIX5JonecDataNewOrderSingle(new HashMap());
                            mAdvNewOrder.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.INDICATIVE_QUOTE);
                            mAdvNewOrder.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                            mAdvNewOrder.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                            mAdvNewOrder.setfSenderSubID(mTrxCtl.getTraderCode());
                            
//                            mAdvNewOrder.setfClOrdID(mInputMsgRequest.getfClOrdID());
//                            mAdvNewOrder.setfHandlInst(StringHelper.fromInt(mInputMsgRequest.getfHandlInst()));
//                            mAdvNewOrder.setfAccountType(
//                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
//                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
//                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
//                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
//                                    FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
//                                );
//                            mAdvNewOrder.setfOrderRestrictions("X Y");
                            mAdvNewOrder.setfOrderQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
//                            mAdvNewOrder.setfOrdType(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_LIMIT);
//                            mAdvNewOrder.setfPrice(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                            mAdvNewOrder.setfSide(mInputMsgRequest.getfSide());
                            mAdvNewOrder.setfTransactTime(mAdvNewOrder.getfSendingTime());
                            mAdvNewOrder.setfText((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())) ? mInputMsgRequest.getfText() : "New Advertisement");
//                            mAdvNewOrder.setfTimeInForce(
//                                    (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION)) ? FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_SESSION : 
//                                    (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY)) ? FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_DAY : 
//                                    FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_DAY
//                                    );
                            mAdvNewOrder.setfSymbol(mInputMsgRequest.getfSymbol()+"_NG");
//                            mAdvNewOrder.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0", ""));
                            mAdvNewOrder.setfNoPartyIDs(StringHelper.fromInt(3));
                            //.executing trader
                            mAdvNewOrder.setfPartyID1(mTrxCtl.getTraderCode());
                            mAdvNewOrder.setfPartyIDSource1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                            mAdvNewOrder.setfPartyRole1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER);
                            //.noPartySub
                            mAdvNewOrder.setfNoPartySubIDs(FIX5JonecDataConst.FIX5JonecFieldValue.NO_PARTY_SUB_IDS_EXECUTING_FIRM);
                            mAdvNewOrder.setfPartySubID(OUCHConsts.OUCHValue.ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE.toUpperCase()+"   ");                            
                            if (!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())){
                                mAdvNewOrder.setfPartySubID(mInputMsgRequest.getfText());
                            }   
                            mAdvNewOrder.setfPartySubIDType(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_SUB_ID_TYPE);
                            //.executing firm
                            mAdvNewOrder.setfPartyID2(FIX5JonecDataConst.FIX5JonecFieldValue.SENDER_COMP_ID);
                            mAdvNewOrder.setfPartyIDSource2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                            mAdvNewOrder.setfPartyRole2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM);
                            //.customer account
                            mAdvNewOrder.setfPartyID3(mInputMsgRequest.getfComplianceID());
                            mAdvNewOrder.setfPartyIDSource3(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                            mAdvNewOrder.setfPartyRole3(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_24_CUSTOMER_ACCOUNT);
                            
                            mAdvNewOrder.setfQuoteId(mInputMsgRequest.getfClOrdID());
                            mAdvNewOrder.setfQuoteType("0");
                            mAdvNewOrder.setfPrivateQuote("N");
                            mAdvNewOrder.setfSingleQuoteIndicator("N");
                            mAdvNewOrder.setfSecurityID(mInputMsgRequest.getfSecurityID());
                            mAdvNewOrder.setfSecurityIDSource("M");
                            mAdvNewOrder.setfSettlMethod("2");
                            if (mInputMsgRequest.getfSide().equalsIgnoreCase(ORIDataConst.ORIFieldValue.SIDE_BUY)) {
                                mAdvNewOrder.setfBidPx(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                            } else {
                                mAdvNewOrder.setfOfferPx(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                            }
                            mAdvNewOrder.setfOrderCapacity("A");
                            
                            String zAdvNewOrderFixMsg = mAdvNewOrder.msgToString();
                            zAdvNewOrderFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zAdvNewOrderFixMsg,true,true,mTrxCtl.getConnectionName());

                            if (!mTrxCtl.sendMessageDirect(zAdvNewOrderFixMsg)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send");
                            }
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Found route @new order as advertisement");
                        }else{
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Found route @new order as advertisement but no controller");
                        }
                        break;
                    case ORIDataConst.ORIFieldValue.HANDLINST_NEGOTIATIONDEAL:
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        break;
                    default:
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                        break;
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
