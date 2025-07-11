/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.client.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderCancelReplaceRequest;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketController;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketMgr;
import com.itm.ts.ouch.consts.OUCHConsts;
import com.itm.ts.ouch.structs.OUCHMsgReplaceOrder;
import com.itm.xtream.inet.trading.consts.ITMTradingServerConsts;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class JONECSimWorkDataOrderAmend {
    //.single instance:
    public final static JONECSimWorkDataOrderAmend getInstance = new JONECSimWorkDataOrderAmend();
    
    public JONECSimWorkDataOrderAmend() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, ORIDataOrderAmend mInputMsgRequest){
        try{
            long vOriginOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfOrigClOrdID());
            long vEveryOrderToken = BookOfJONECSimToken.getInstance.generateTrxToken(mInputMsgRequest.getfClOrdID());
            
            if ((vOriginOrderToken > 0) && (vEveryOrderToken > 0)){
                //.prev sheet:
                SheetOfJONECSimOriginRequest mOriginReqSheet = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOriginOrderToken);
                
                //.save to memory:
                BookOfJONECSimOriginRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimOriginRequest(vOriginOrderToken, mInputMsgRequest));
                
                BookOfJONECSimEveryRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimEveryRequest(vEveryOrderToken, mInputMsgRequest));
                //.process:
                switch (mInputMsgRequest.getfHandlInst()) {
                    case ORIDataConst.ORIFieldValue.HANDLINST_NORMAL:
                        if (ITMTradingServerConsts.EngineSetup.FIX5_ONLY){
                            
                            FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLine();
                            if (mTrxCtl != null){
                                FIX5JonecDataOrderCancelReplaceRequest mNormalAmdOrder = new FIX5JonecDataOrderCancelReplaceRequest(new HashMap());
                                mNormalAmdOrder.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.ORDER_CANCEL_REPLACE_REQUEST);
                                mNormalAmdOrder.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                                mNormalAmdOrder.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                                mNormalAmdOrder.setfSenderSubID(mTrxCtl.getTraderCode());

                                mNormalAmdOrder.setfClOrdID(mInputMsgRequest.getfClOrdID());
                                mNormalAmdOrder.setfOrigClOrdID(mInputMsgRequest.getfOrigClOrdID());
                                mNormalAmdOrder.setfOrderID(mInputMsgRequest.getfOrderID());

                                mNormalAmdOrder.setfSymbol(mInputMsgRequest.getfSymbol());
                                
                                mNormalAmdOrder.setfSecuritySubType(FIX5JonecDataConst.FIX5JonecFieldValue.SECURITY_SUB_TYPE_RG); //.###???
                                if (mOriginReqSheet != null && mOriginReqSheet.getIdxMessage() != null){
                                    if (mOriginReqSheet.getIdxMessage() instanceof ORIDataNewOrder){
                                        ORIDataNewOrder mOriginORIOrder = (ORIDataNewOrder)mOriginReqSheet.getIdxMessage();
                                        mNormalAmdOrder.setfSecuritySubType(mOriginORIOrder.getfSymbolSfx().replace("0", ""));
                                    }
                                }
                                
                                mNormalAmdOrder.setfNoPartyIDs(StringHelper.fromInt(2));

                                mNormalAmdOrder.setfAccountType(
                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                        mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                        FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                    );
                                mNormalAmdOrder.setfOrderQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
                                mNormalAmdOrder.setfPrice(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                                mNormalAmdOrder.setfSide(mInputMsgRequest.getfSide());
                                mNormalAmdOrder.setfTransactTime(mNormalAmdOrder.getfSendingTime());
                                mNormalAmdOrder.setfText((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())) ? mInputMsgRequest.getfText() : "Amend Advertisement");
                                //////mNormalAmdOrder.setfOrdType(FIX5JonecFieldValue.ORD_TYPE_LIMIT);
                                //////mNormalAmdOrder.setfTimeInForce(
                                //////        (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION)) ? FIX5JonecFieldValue.TIME_IN_FORCE_SESSION : 
                                //////        (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY)) ? FIX5JonecFieldValue.TIME_IN_FORCE_DAY : 
                                //////        FIX5JonecFieldValue.TIME_IN_FORCE_DAY
                                //////        );
                                mNormalAmdOrder.setfTimeInForce(mInputMsgRequest.getfTimeInForce()); //. TimeInForce Fix4.2 = TimeInForce 5
                                
                                if (mInputMsgRequest.getfOrdType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.ORDTYPE_MARKET_NONSTOP)){  //. OrdType Fix4.2 <> OrdType Fix 5
                                    mNormalAmdOrder.setfOrdType(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_MARKET);
                                }else{
                                    mNormalAmdOrder.setfOrdType(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_LIMIT);
                                }
                                
                                mNormalAmdOrder.setfPartyID1(mInputMsgRequest.getfComplianceID());
                                mNormalAmdOrder.setfPartyIDSource1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                                mNormalAmdOrder.setfPartyRole1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);

                                mNormalAmdOrder.setfPartyID2(mInputMsgRequest.getfClientID());
                                mNormalAmdOrder.setfPartyIDSource2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                                mNormalAmdOrder.setfPartyRole2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_3_CLIENT_ID);

                                String zNormalAmdOrderFixMsg = mNormalAmdOrder.msgToString();
                                zNormalAmdOrderFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zNormalAmdOrderFixMsg,true,true,mTrxCtl.getConnectionName());

                                if (!mTrxCtl.sendMessageDirect(zNormalAmdOrderFixMsg)){
                                    //.???:
                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send as normal");
                                }
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Found route @amend order as normal");
                            }else{
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Found route @amend order as normal but no controller");
                            }
                        }else{
                            OUCHMsgReplaceOrder mAmendOrder = new OUCHMsgReplaceOrder();
                            mAmendOrder.setType(OUCHConsts.OUCHMessageType.MESSAGETYPE_INBOUND_REPLACE_ORDER);
                            mAmendOrder.setExistingOrderToken(vOriginOrderToken);
                            mAmendOrder.setReplacementOrderToken(vEveryOrderToken);
                            mAmendOrder.setQuantity(mInputMsgRequest.getfOrderQty());
                            mAmendOrder.setPrice((long) mInputMsgRequest.getfPrice());

                            byte[] btAmendOrder = mAmendOrder.buildMessage();

                            if ((btAmendOrder != null) && (btAmendOrder.length > 0)){
                                ITMSoupBinTCPOUCHPacketController mTrxCtl = ITMSoupBinTCPOUCHPacketMgr.getInstance.getNextActiveConnectionLine();
                                if (mTrxCtl != null){
                                    if (!mTrxCtl.getChannel().sendMessageDirect(btAmendOrder)){
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
                            FIX5JonecDataOrderCancelReplaceRequest mAdvAmdOrder = new FIX5JonecDataOrderCancelReplaceRequest(new HashMap());
                            mAdvAmdOrder.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.ORDER_CANCEL_REPLACE_REQUEST);
                            mAdvAmdOrder.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                            mAdvAmdOrder.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                            mAdvAmdOrder.setfSenderSubID(mTrxCtl.getTraderCode());
                            
                            mAdvAmdOrder.setfClOrdID(mInputMsgRequest.getfClOrdID());
                            mAdvAmdOrder.setfOrigClOrdID(mInputMsgRequest.getfOrigClOrdID());
                            mAdvAmdOrder.setfOrderID(mInputMsgRequest.getfOrderID());
                            
                            mAdvAmdOrder.setfSymbol(mInputMsgRequest.getfSymbol());
                            mAdvAmdOrder.setfSecuritySubType(FIX5JonecDataConst.FIX5JonecFieldValue.SECURITY_SUB_TYPE_NG);
                            mAdvAmdOrder.setfNoPartyIDs(StringHelper.fromInt(2));
                            
                            mAdvAmdOrder.setfAccountType(
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_I) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_A) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_FOREIGNER : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_S) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_INDONESIAN : 
                                    mInputMsgRequest.getfAccount().equals(ORIDataConst.ORIFieldValue.ACCOUNT_F) ? FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_HOUSE_FOREIGNER : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.ACCOUNT_TYPE_CUSTOMER_INDONESIAN
                                );
                            mAdvAmdOrder.setfOrderQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
                            mAdvAmdOrder.setfOrdType(FIX5JonecDataConst.FIX5JonecFieldValue.ORD_TYPE_LIMIT);
                            mAdvAmdOrder.setfPrice(StringHelper.fromDouble(mInputMsgRequest.getfPrice()));
                            mAdvAmdOrder.setfSide(mInputMsgRequest.getfSide());
                            mAdvAmdOrder.setfTransactTime(mAdvAmdOrder.getfSendingTime());
                            mAdvAmdOrder.setfText((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())) ? mInputMsgRequest.getfText() : "Amend Advertisement");
                            mAdvAmdOrder.setfTimeInForce(
                                    (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION)) ? FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_SESSION : 
                                    (mInputMsgRequest.getfTimeInForce().equalsIgnoreCase(ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY)) ? FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_DAY : 
                                    FIX5JonecDataConst.FIX5JonecFieldValue.TIME_IN_FORCE_DAY
                                    );
                            
                            mAdvAmdOrder.setfPartyID1(mInputMsgRequest.getfComplianceID());
                            mAdvAmdOrder.setfPartyIDSource1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mAdvAmdOrder.setfPartyRole1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_5_INVESTOR_ID_SID);
                            
                            mAdvAmdOrder.setfPartyID2(mInputMsgRequest.getfClientID());
                            mAdvAmdOrder.setfPartyIDSource2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER);
                            mAdvAmdOrder.setfPartyRole2(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_3_CLIENT_ID);
                            
                            String zAdvAmdOrderFixMsg = mAdvAmdOrder.msgToString();
                            zAdvAmdOrderFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zAdvAmdOrderFixMsg,true,true,mTrxCtl.getConnectionName());

                            if (!mTrxCtl.sendMessageDirect(zAdvAmdOrderFixMsg)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send");
                            }
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Found route @amend order as advertisement");
                        }else{
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Found route @amend order as advertisement but no controller");
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
