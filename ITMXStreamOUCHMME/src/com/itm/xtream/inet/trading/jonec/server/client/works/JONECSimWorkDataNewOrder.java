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
                                mNormalNewOrder.setfAccountType(
                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                        FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                    );
                                mNormalNewOrder.setfOrderRestrictions("X Y");
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

                                mNormalNewOrder.setfSymbol(mInputMsgRequest.getfSymbol());
                                mNormalNewOrder.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0", ""));
                                mNormalNewOrder.setfNoPartyIDs(StringHelper.fromInt(2));

                                mNormalNewOrder.setfPartyID1(mInputMsgRequest.getfComplianceID());
                                mNormalNewOrder.setfPartyIDSource1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                                mNormalNewOrder.setfPartyRole1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);

                                mNormalNewOrder.setfPartyID2(mInputMsgRequest.getfClientID());
                                mNormalNewOrder.setfPartyIDSource2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                                mNormalNewOrder.setfPartyRole2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_3_CLIENT_ID);

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
                            // mNewOrder.setBrokerReference(mInputMsgRequest.getfClOrdID());
                            mNewOrder.setCustomerInfo("#" + mInputMsgRequest.getfClOrdID());//. 20210830 : hrn: supaya dianggap tidak valid di sisi FIX Lama (4.2)
                            mNewOrder.setClientAccount(mInputMsgRequest.getfComplianceID());
                            switch (mInputMsgRequest.getfSide()) {
                                case ORIDataConst.ORIFieldValue.SIDE_BUY:
                                    mNewOrder.setSide(Byte.parseByte(OUCHConsts.OUCHValue.ORDER_VERB_BUY));
                                    break;
                                case ORIDataConst.ORIFieldValue.SIDE_SELL:
                                    mNewOrder.setSide(Byte.parseByte(OUCHConsts.OUCHValue.ORDER_VERB_SELL));
                                    break;
                                case ORIDataConst.ORIFieldValue.SIDE_SELL_SHORT:
                                    mNewOrder.setSide(Byte.parseByte(OUCHConsts.OUCHValue.ORDER_VERB_SHORT_SELL));
                                    break;
                                case ORIDataConst.ORIFieldValue.SIDE_MARGIN_REQUEST:
                                    mNewOrder.setSide(Byte.parseByte(OUCHConsts.OUCHValue.ORDER_VERB_MARGIN));
                                    break;
                                case ORIDataConst.ORIFieldValue.SIDE_PRICE_STABILIZATION:
                                    mNewOrder.setSide(Byte.parseByte(OUCHConsts.OUCHValue.ORDER_VERB_PRICE_STABILIZATION));
                                    break;
                                default:
                                    break;
                            }
                            
                            mNewOrder.setExchangeInfo(OUCHConsts.OUCHValue.ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE);
                            
                            
                            if (!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())){
                                mNewOrder.setExchangeInfo(mInputMsgRequest.getfText());
                            }
                            mNewOrder.setClientAccount(mInputMsgRequest.getfAccount());
                            mNewOrder.setQuantity(mInputMsgRequest.getfOrderQty());

                            mNewOrder.setOrderBookId(StringHelper.toInt(mInputMsgRequest.getfSecurityID()));
                            
                            
                            
                            mNewOrder.setPrice((long) mInputMsgRequest.getfPrice());
                            
                            if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_MARKET_NONSTOP)){
                                mNewOrder.setPrice(0x7FFFFFFF);
                            }
                            
                            switch (mInputMsgRequest.getfTimeInForce()) {
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_SESSION);
                                    break;
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_DAY);
                                    break;
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_IOC: //. FAK
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_IMMEDIATE);
                                    //.soon
//                                    mNewOrder.setMinimumQuantity(0); //. - For FAK order, set Minimum Quantity >= 0
                                    break;
                                case ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_FOK:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_IMMEDIATE);
                                    //.soon
//                                    mNewOrder.setMinimumQuantity(mNewOrder.getQuantity()); //. - For FOK order, set Minimum Quantity = Quantity
                                    break;
                                default:
                                    mNewOrder.setTimeInForce((byte) OUCHConsts.OUCHValue.TIME_OF_FORCE_DAY);
                                    break;
                            }   
                            //.soon
//                            mNewOrder.setClientId(0);

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
                            mAdvNewOrder.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.NEW_ORDER_SINGLE);
                            mAdvNewOrder.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                            mAdvNewOrder.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                            mAdvNewOrder.setfSenderSubID(mTrxCtl.getTraderCode());
                            
                            mAdvNewOrder.setfClOrdID(mInputMsgRequest.getfClOrdID());
                            mAdvNewOrder.setfHandlInst(StringHelper.fromInt(mInputMsgRequest.getfHandlInst()));
                            mAdvNewOrder.setfAccountType(
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                );
                            mAdvNewOrder.setfOrderRestrictions("X Y");
                            mAdvNewOrder.setfOrderQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
                            mAdvNewOrder.setfOrdType(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_LIMIT);
                            mAdvNewOrder.setfPrice(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                            mAdvNewOrder.setfSide(mInputMsgRequest.getfSide());
                            mAdvNewOrder.setfTransactTime(mAdvNewOrder.getfSendingTime());
                            mAdvNewOrder.setfText((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())) ? mInputMsgRequest.getfText() : "New Advertisement");
                            mAdvNewOrder.setfTimeInForce(
                                    (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION)) ? FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_SESSION : 
                                    (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY)) ? FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_DAY : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_DAY
                                    );
                            mAdvNewOrder.setfSymbol(mInputMsgRequest.getfSymbol());
                            mAdvNewOrder.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0", ""));
                            mAdvNewOrder.setfNoPartyIDs(StringHelper.fromInt(2));
                            
                            mAdvNewOrder.setfPartyID1(mInputMsgRequest.getfComplianceID());
                            mAdvNewOrder.setfPartyIDSource1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mAdvNewOrder.setfPartyRole1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            
                            mAdvNewOrder.setfPartyID2(mInputMsgRequest.getfClientID());
                            mAdvNewOrder.setfPartyIDSource2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mAdvNewOrder.setfPartyRole2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_3_CLIENT_ID);
                            
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
