/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderCancelReject;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmendReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackProcessor;
import com.itm.xtream.inet.trading.replytimeout.mgr.ITMTradingServerReplyTimeOutMgr;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataOrderCancelReject {
    //.single instance:
    public final static FIX5JonecWorkDataOrderCancelReject getInstance = new FIX5JonecWorkDataOrderCancelReject();
    
    public FIX5JonecWorkDataOrderCancelReject() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataOrderCancelReject mInputMsgRequest){
        try{
            //. ?????????????????? todo (amend reply bad)
            //. yyyyyyyyy
            //.???:
            if (controller != null){
                long vOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfClOrdID());
            
                ITMTradingServerReplyTimeOutMgr.getInstance.removeToken(vOrderToken);
            
                if (vOrderToken > 0){

                    SheetOfJONECSimOriginRequest mOriginRequest = BookOfJONECSimOriginRequest.getInstance.retrieveSheet(vOrderToken);
                    SheetOfJONECSimEveryRequest mEveryRequest = BookOfJONECSimEveryRequest.getInstance.retrieveSheet(vOrderToken);

                    SheetOfJONECSimCalcQty mCalcQty = BookOfJONECSimCalcQty.getInstance.retrieveSheet(vOrderToken);
                    if (mCalcQty == null){
                        mCalcQty = new SheetOfJONECSimCalcQty(vOrderToken);
                        BookOfJONECSimCalcQty.getInstance.addOrUpdateSheet(mCalcQty);
                    }

                    if ((mOriginRequest == null) && (mEveryRequest != null)){
                        mOriginRequest = new SheetOfJONECSimOriginRequest(vOrderToken, mEveryRequest.getIdxMessage());
                    }

                    if ((mOriginRequest != null) && (mEveryRequest != null)){
                        if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderAmend){

                            ORIDataOrderAmend mOriginRequestMsg = ((ORIDataOrderAmend)mOriginRequest.getIdxMessage());

                            //. check jika dari amend
                            //. set status reject untuk token baru
                            mCalcQty.setOrderStatus(QRIDataConst.QRIFieldValue.ORDSTATUS_REJECTED);
                            //.backup:
                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);

                            //. urutan jika cancel dari reject-amend
                            //. 1. Kirim amend reply bad (broker ref baru)
                            //. 2. Kirim cancel broker ref lama ??xx
                            //. 3. update status OrderList Martin ??xx

                            //. zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

                            //.20250806: diouch sebelumnya reject amend datang dari message canceledOrder, diouch mme reply bad amend/amend reject didapat dari message rejectedOrder
                            //. Amend Reply Bad untuk broker ref baru
                            ORIDataOrderAmendReply mReplyAmendBadMsg = new ORIDataOrderAmendReply(new HashMap());
                            mReplyAmendBadMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                            mReplyAmendBadMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                            mReplyAmendBadMsg.setfOrderAmendReplyType(ORIDataOrderAmendReply.ORIOrderAmendReplyType.BAD);                        
                            mReplyAmendBadMsg.setfOrderID("000000000000");
                            mReplyAmendBadMsg.setfClOrdID(mOriginRequestMsg.getfClOrdID());
                            mReplyAmendBadMsg.setfOrigClOrdID(" ");
                            mReplyAmendBadMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_REJECTED);
                            mReplyAmendBadMsg.setfCxlRejResponseTo(2);
                            mReplyAmendBadMsg.setfText(mInputMsgRequest.getfRejectText());

                            JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyAmendBadMsg.getfBundleConnectionName());
                            if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
                                if (    
                                        mClientLine.getChChannel().sendMessageDirect(mReplyAmendBadMsg.msgToString()) 
//                                        &&
//                                        mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())


                                        ){
                                    //... .
                                }else{
                                    //.???:
                                    //. TODO : handle lewat Martin
                                    //. masukin log
                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                                }
                            }else{
                                //.???:
                                //. TODO : handle lewat Martin
                                //. masukin log
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            }
                        } else if (mOriginRequest.getIdxMessage() instanceof ORIDataOrderCancel){
                            ORIDataOrderCancel mOriginRequestMsg = ((ORIDataOrderCancel)mOriginRequest.getIdxMessage());

                            BookOfJONECSimCalcQty.getInstance.backupProcessor.backupMapObjectToFile(vOrderToken, mCalcQty);

                            ORIDataOrderCancelReply mReplyMsg = new ORIDataOrderCancelReply(new HashMap());
                            mReplyMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
                            mReplyMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                            //. semua response
                            mReplyMsg.setfOrderCancelReplyType(ORIDataOrderCancelReply.ORIOrderCancelReplyType.OK);
                            mReplyMsg.setfOrderID(mOriginRequestMsg.getfOrderID());
                            mReplyMsg.setfExecRefID(mOriginRequestMsg.getfClOrdID());
                            mReplyMsg.setfExecID(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(mInputMsgRequest.getfTransactTime()));
                            mReplyMsg.setfExecTransType(ORIDataConst.ORIFieldValue.EXECTRANSTYPE_CANCEL);
                            mReplyMsg.setfExecType(ORIDataConst.ORIFieldValue.EXECTYPE_CANCELLED);
                            mReplyMsg.setfOrdStatus(ORIDataConst.ORIFieldValue.ORDSTATUS_CANCELLED);
                            mReplyMsg.setfSymbol(" ");
                            mReplyMsg.setfSide(" ");
                            mReplyMsg.setfLeavesQty(0);
                            mReplyMsg.setfCumQty(0);
                            mReplyMsg.setfAvgPx(0);
                            mReplyMsg.setfHandlInst(ORIDataConst.ORIFieldValue.HANDLINST_NORMAL);
                            mReplyMsg.setfText("Securities cannot be canceled at this time.");
                            mReplyMsg.setfLastPx(0);
                            mReplyMsg.setfLastShares(0);

                            JONECSimCallbackProcessor mClientLine = JONECSimCallbackController.getInstance.getActiveChannelProcessorByConnName(mReplyMsg.getfBundleConnectionName());
                            if ((mClientLine != null) && (mClientLine.getAlreadyLoggedIn()) && ((mClientLine.getChChannel() != null))){
    //////                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ODD, logLevel.ERROR, "zzzz cek channel apakah kosong.");
                                if (mClientLine.getChChannel().sendMessageDirect(mReplyMsg.msgToString())){
                                    //... .
    //////                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.ODD, logLevel.ERROR, "zzzz kirim ke channel (TG) berhasil.");
                                }else{
                                    //.???:
                                    //. TODO : handle lewat Martin
                                    //. masukin log
                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                                }
                            }else{
                                //.???:
                                //. TODO : handle lewat Martin
                                //. masukin log
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                            }
                        }

                        }
                    }
                }
        
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
}
