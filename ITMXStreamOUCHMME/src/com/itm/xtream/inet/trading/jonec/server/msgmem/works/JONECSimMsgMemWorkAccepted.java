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
import com.itm.idx.data.ori.message.struct.ORIDataNewOrderReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.util.StringUtil;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.consts.OUCHConsts;
import com.itm.ts.ouch.structs.OUCHMsgOrderAccepted;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackProcessor;
import com.itm.xtream.inet.trading.racing.mgr.ITMTradingServerRacingMgr;
import com.itm.xtream.inet.trading.replytimeout.mgr.ITMTradingServerReplyTimeOutMgr;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class JONECSimMsgMemWorkAccepted {
    //.single instance:
    public final static JONECSimMsgMemWorkAccepted getInstance = new JONECSimMsgMemWorkAccepted();
    
    public JONECSimMsgMemWorkAccepted() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(SheetOfOUCHBase mSheet, OUCHMsgOrderAccepted mMessage){
        try{
            long lSeqLatestSaved = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestSaved();
            long lSeqLatestReceived = BookOfJONECSimToken.getInstance.getLastTrxSeqLatestReceived();
            if (lSeqLatestReceived <= lSeqLatestSaved){ //. skip
                return;
            }
            
            //. hrn: 2022-08-30 remove memory untuk flag order timeout
            ITMTradingServerReplyTimeOutMgr.getInstance.removeToken(mMessage.getOrderToken());
            
            //. 2022-04-13 : hrn : penanda order accepted sudah datang (sudah buka)
            ITMTradingServerRacingMgr.getInstance.setbOrderMessageAccepted(true);
            int bOrderRacingMode = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_mode;
            
            //. 2022-02-14 : hrn : cek order racing
            if (ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_enable && bOrderRacingMode == 1){ //. order racing mode harus single order (1)
                boolean bStillSendingRacing = ITMTradingServerRacingMgr.getInstance.isbStillSendingRacing();
                if (bStillSendingRacing){
                    ORIDataNewOrder mCurrentOrderRacing = ITMTradingServerRacingMgr.getInstance.getCurrentOrderRacing();
                    if (mCurrentOrderRacing != null){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Dapat message OUCHMsgAccepted cek trigger doReleaseAllPendingOrder. BrokerRef = " + mMessage.getCustomerInfo()+ ", OrderToken = " + mMessage.getOrderToken() + ", mCurrentOrderRacing.getfClOrdID() = " + mCurrentOrderRacing.getfClOrdID());
                        if (mCurrentOrderRacing.getfClOrdID().equals(mMessage.getCustomerInfo().replaceAll("#",""))){
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Dapat message OUCHMsgAccepted untuk trigger doReleaseAllPendingOrder. BrokerRef = " + mMessage.getCustomerInfo() + ", OrderToken = " + mMessage.getOrderToken());
                            //. remove order pertama dan tarik sisa pending order lalu set semua flag jadi normal
                            ITMTradingServerRacingMgr.getInstance.doReleaseAllPendingOrder(true);
                        }
                    }
                }
            }
            
            long vOrderToken = mMessage.getOrderToken();
            
            if (vOrderToken > 0){
                
                SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);
                
                if ((mOriginRequest != null) && (mEveryRequest != null)){
                    if (mOriginRequest.getIdxMessage() instanceof ORIDataNewOrder){
                        
                        ORIDataNewOrder mOriginRequestMsg = ((ORIDataNewOrder)mOriginRequest.getIdxMessage());
                        
                        //. jika Order State = “D”ead maka jadikan Reject/Reply Bad, selain itu respon normal
                        if (String.valueOf(mMessage.getOrderState()).equalsIgnoreCase(OUCHConsts.OUCHValue.ORDER_STATE_DEAD)){

                            ORIDataNewOrderReply mReplyMsg = new ORIDataNewOrderReply(new HashMap());
                            mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                            mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                            mReplyMsg.setfNewOrderReplyType(ORIDataNewOrderReply.ORINewOrderReplyType.BAD);

                            mReplyMsg.setfOrderID(ORIDataConst.ORIFieldValue.ORDERID_NO_JATS_ORDERNUMBER);
                            mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                            mReplyMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
                            mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
                            mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_REJECTED);
                            mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
                            mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                            mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                            mReplyMsg.setfLeavesQty(0);
                            mReplyMsg.setfCumQty(0);
                            mReplyMsg.setfAvgPx(0);
                            mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
                            
                            mReplyMsg.setfText("(" + "9" + ") reason: Order State Dead.");
                            
                            mReplyMsg.setfLastPx(0);
                            mReplyMsg.setfLastShares(0);

                            JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                            if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                                if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                    //... .
                                }else{
                                    //.???:
                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                                }
                            }else{
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            }
                        }else{
                            //. response OK
                            SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                            if (mCalcQty == null){
                                mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                                BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                            }
                            mCalcQty.setQtyOrder(mMessage.getQuantity());
                            mCalcQty.setOrderStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_NEW); //. order status = new
                            mCalcQty.setJatsOrderNo(StringHelper.fromLong(mMessage.getOrderId()));
                            mCalcQty.setBrokerRef(mOriginRequestMsg.getfClOrdID());
                            //.backup:
                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);



                            ORIDataNewOrderReply mReplyMsg = new ORIDataNewOrderReply(new HashMap());
                            mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                            mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                            mReplyMsg.setfNewOrderReplyType(ORIDataNewOrderReply.ORINewOrderReplyType.OK);

                            //. hrn: asumsi getOrderReferenceNumberExternal = getOrderReferenceNumber
                            mReplyMsg.setfOrderID(StringHelper.fromLong(mMessage.getOrderId()));

                            mReplyMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                            mReplyMsg.setfExecID(DateTimeHelper.getTimeIDXTRXExecReportFormatFromDate(mSheet.getMessageDate()));
                            mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_NEW);
                            mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_NEW);
                            mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_NEW);
                            mReplyMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                            mReplyMsg.setfSide(mOriginRequestMsg.getfSide());
                            mReplyMsg.setfLeavesQty(mMessage.getQuantity());
                            mReplyMsg.setfCumQty(0);
                            mReplyMsg.setfAvgPx(0);
                            mReplyMsg.setfHandlInst(mOriginRequestMsg.getfHandlInst());
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

                            QRIDataOrderListMessage mOrderListMsg = new QRIDataOrderListMessage(new HashMap());
                            mOrderListMsg.setfOrderID(mMessage.getOrderId());
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
                            mOrderListMsg.setfSymbol(mOriginRequestMsg.getfSymbol());
                            mOrderListMsg.setfSymbolSfx(mOriginRequestMsg.getfSymbolSfx());
                            mOrderListMsg.setfSecurityID(mOriginRequestMsg.getfSecurityID());
                            mOrderListMsg.setfSide(mOriginRequestMsg.getfSide());
                            mOrderListMsg.setfOrderQty(mCalcQty.getQtyOrder());
                            mOrderListMsg.setfOrderType(StringUtil.toInteger(mOriginRequestMsg.getfOrdType()));
                            mOrderListMsg.setfPrice(mOriginRequestMsg.getfPrice());
                            mOrderListMsg.setfTimeInForce(mOriginRequestMsg.getfTimeInForce());
                            mOrderListMsg.setfExpiredDate(mOriginRequestMsg.getfExpireDate());
                            mOrderListMsg.setfExecInst(mOriginRequestMsg.getfExecInst());
                            mOrderListMsg.setfLeavesQty(mCalcQty.getQtyLeave());
                            mOrderListMsg.setfCumQty(mCalcQty.getQtyMatch());
                            mOrderListMsg.setfAvgPx(0);
                            mOrderListMsg.setfTradeDate(DateTimeHelper.getDateSVRTRXFormatFromDate(mSheet.getMessageDate())); //. ??
                            mOrderListMsg.setfTransactionTime(DateTimeHelper.getDateTimeIDXTRXFormat(mSheet.getMessageDate())); //. ??
                            mOrderListMsg.setfText("");
                            mOrderListMsg.setfClearingAccount("");
                            mOrderListMsg.setfComplianceID(mOriginRequestMsg.getfComplianceID());                        

                            //. save orderlist ke memory martin
//                            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mOrderListMsg);
//                            //. broadcast orderlist via martin
//                            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mOrderListMsg);
                        }
                        
                        
                        
                                                                      
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend){
                        //.???:
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel){
                        //.???:
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }else{
                        //.???:
                        //. masukin log
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }else{
                    //.???:
                    //. masukin log
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                }
                
            }else{
                //.???:
                //. masukin log
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
            }

        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }

}
