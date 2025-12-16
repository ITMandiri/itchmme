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
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancel;
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.ts.ouch.consts.OUCHConsts;
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
public class JONECSimWorkDataNegotiationDealCancel {
    //.single instance:
    public final static JONECSimWorkDataNegotiationDealCancel getInstance = new JONECSimWorkDataNegotiationDealCancel();
    
    public JONECSimWorkDataNegotiationDealCancel() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, ORIDataNegotiationDealCancel mInputMsgRequest){
        try{
            long vOriginOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfOrigClOrdID());
            long vEveryOrderToken = BookOfJONECSimToken.getInstance.generateTrxToken(mInputMsgRequest.getfClOrdID());
            
            if ((vOriginOrderToken > 0) && (vEveryOrderToken > 0)){
                //.save to memory:
//                BookOfJONECSimOriginRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimOriginRequest(vOriginOrderToken, mInputMsgRequest));
                BookOfJONECSimEveryRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimEveryRequest(vEveryOrderToken, mInputMsgRequest));
                
                SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOriginOrderToken);
                //.testonly:
                
                //.real:
                if (mOriginRequest != null){
                    if (mOriginRequest.getIdxMessage() instanceof ORIDataNegotiationDeal){
                        ORIDataNegotiationDeal mOriginRequestMsg = ((ORIDataNegotiationDeal)mOriginRequest.getIdxMessage());
                        
                        FIX5JonecDataTradeCaptureReport mFixMsg;
                        String zFixMsg = "";
                        FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLine();
                        if (mTrxCtl != null){
                            QRIDataNegDealListMessage mNGList = BookOfMARTINNegDealList.getInstance.retrieveSheet(StringHelper.toLong(mInputMsgRequest.getfOrderID()));
                            
                            mFixMsg = new FIX5JonecDataTradeCaptureReport(new HashMap());
                            mFixMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.TRADE_CAPTURE_REPORT);
                            mFixMsg.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                            mFixMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5LocalFormatDetail());
                            mFixMsg.setfSenderSubID(mTrxCtl.getTraderCode());
                            
                            mFixMsg.setfTradeReportID(mInputMsgRequest.getfClOrdID());
//                            mFixMsg.setfTradeReportTransType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REPORT_TRANS_TYPE_NEW);
                            mFixMsg.setfTradeReportType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REPORT_TYPE_CANCEL);
                            mFixMsg.setfSettlDate(mOriginRequestMsg.getfSettlDate());
                            mFixMsg.setfSettlMethod(mOriginRequestMsg.getfSettlDeliveryType().equals(ORIDataConst.ORIFieldValue.SETTLDELIVERYTYPE_VERSUS) ? FIX5JonecDataConst.FIX5JonecFieldValue.DELIVERY_TYPE_VERSUS_PAYMENT : FIX5JonecDataConst.FIX5JonecFieldValue.DELIVERY_TYPE_FREE_OF_PAYMENT );
                            mFixMsg.setfSymbol(mInputMsgRequest.getfSymbol()+ "_" +FIX5JonecDataConst.FIX5JonecFieldValue.SECURITY_SUB_TYPE_NG);
                            mFixMsg.setfSecuritySubType("1");
                            mFixMsg.setfLastPx(StringHelper.fromDouble(mOriginRequestMsg.getfPrice()));
                            mFixMsg.setfLastQty(StringHelper.fromLong(mInputMsgRequest.getfOrderQty()));
                            mFixMsg.setfNoSides(StringHelper.fromInt(1));
                            
                            mFixMsg.setfSide1(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL );                         
                            mFixMsg.setfNoPartyIDs1(StringHelper.fromInt(4));
                            //.customer account
                            mFixMsg.setfPartyID1a(mOriginRequestMsg.getfComplianceID());
                            mFixMsg.setfPartyIDSource1a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                            mFixMsg.setfPartyRole1a(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_24_CUSTOMER_ACCOUNT);
                            //.executing trader
                            mFixMsg.setfPartyID1b(mTrxCtl.getTraderCode());
                            mFixMsg.setfPartyIDSource1b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                            mFixMsg.setfPartyRole1b(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER);
                            //.executing firm
                            mFixMsg.setfPartyID1c(FIX5JonecDataConst.FIX5JonecFieldValue.SENDER_COMP_ID);
                            mFixMsg.setfPartyIDSource1c(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                            mFixMsg.setfPartyRole1c(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM);
                            //.noPartySub 1
                            mFixMsg.setfNoPartySubIDs1(FIX5JonecDataConst.FIX5JonecFieldValue.NO_PARTY_SUB_IDS_EXECUTING_FIRM);
                            mFixMsg.setfPartySubID1(OUCHConsts.OUCHValue.ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE.toUpperCase()+"   ");
                                                        
                            if (!StringHelper.isNullOrEmpty(mInputMsgRequest.getfText())){
                                mFixMsg.setfPartySubID1("RZAZ");//????????????????????????????
                            }
                            mFixMsg.setfPartySubIDType1(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_SUB_ID_TYPE);
                            //.contra firm
                            mFixMsg.setfPartyID1d((!StringHelper.isNullOrEmpty(mOriginRequestMsg.getSfCounterpartUserID())) ? mOriginRequestMsg.getSfCounterpartUserID().substring(0, 2).toUpperCase() : "");
                            mFixMsg.setfPartyIDSource1d(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
                            mFixMsg.setfPartyRole1d(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_17_CONTRA_FIRM);  
                            
                            //.tambahan
                            mFixMsg.setfSecurityIDSource("M");
                            mFixMsg.setfSecurityType(FIX5JonecDataConst.FIX5JonecFieldValue.SECURITY_TYPE_MULTILEGINSTRUMENT);
                            mFixMsg.setfTransactTime(FIX5DateTimeHelper.getDateTimeFIX5LocalFormatDetail());
                            mFixMsg.setfTransBkdTime(FIX5DateTimeHelper.getDateTimeFIX5LocalFormatDetail());
                            mFixMsg.setfTradeID(mInputMsgRequest.getfOrderID());
                            mFixMsg.setfSecondaryTradeID(mInputMsgRequest.getfOrderID());
                            mFixMsg.setfTradeHandlingInstr(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_HANDLING_INSTR_ONEPARTYREPORTFORMATCHING);
                            
                            zFixMsg = mFixMsg.msgToString();
                            zFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zFixMsg,true,true,mTrxCtl.getConnectionName());

                            if (!mTrxCtl.sendMessageDirect(zFixMsg)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send direct");
                            }

                        }else{
                            //.???:
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
                /***
                //.???:
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @");
                ***/
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
