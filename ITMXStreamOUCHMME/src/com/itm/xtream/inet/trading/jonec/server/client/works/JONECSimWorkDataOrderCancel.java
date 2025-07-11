/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.client.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderCancelRequest;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketController;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketMgr;
import com.itm.ts.ouch.consts.OUCHConsts;
import com.itm.ts.ouch.structs.OUCHMsgCancelOrder;
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
public class JONECSimWorkDataOrderCancel {
    //.single instance:
    public final static JONECSimWorkDataOrderCancel getInstance = new JONECSimWorkDataOrderCancel();
    
    public JONECSimWorkDataOrderCancel() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, ORIDataOrderCancel mInputMsgRequest){
        try{
            String boardNGSfx = ORIDataConst.ORIFieldValue.SYMBOLSFX_PREFIX + ORIDataConst.ORIFieldValue.BOARD_NG;
                    
            if (mInputMsgRequest.getfSymbolSfx().equalsIgnoreCase(boardNGSfx)){
                long vOriginOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfOrigClOrdID());
                long vEveryOrderToken = BookOfJONECSimToken.getInstance.generateTrxToken(mInputMsgRequest.getfClOrdID());

                if ((vOriginOrderToken > 0) && (vEveryOrderToken > 0)){
                    //.save to memory:
                    BookOfJONECSimOriginRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimOriginRequest(vOriginOrderToken, mInputMsgRequest));

                    BookOfJONECSimEveryRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimEveryRequest(vEveryOrderToken, mInputMsgRequest));
                    
                    FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLine();
                    
                    if (mTrxCtl != null){
                        FIX5JonecDataOrderCancelRequest mAdvCancelOrder = new FIX5JonecDataOrderCancelRequest(new HashMap());
                        mAdvCancelOrder.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.ORDER_CANCEL_REQUEST);
                        mAdvCancelOrder.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                        mAdvCancelOrder.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                        mAdvCancelOrder.setfSenderSubID(mTrxCtl.getTraderCode());

                        mAdvCancelOrder.setfClOrdID(mInputMsgRequest.getfClOrdID());
                        mAdvCancelOrder.setfOrderID(mInputMsgRequest.getfOrderID());
                        mAdvCancelOrder.setfOrigClOrdID(mInputMsgRequest.getfOrigClOrdID());

                        mAdvCancelOrder.setfSymbol(mInputMsgRequest.getfSymbol());
                        mAdvCancelOrder.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0",""));

                        mAdvCancelOrder.setfSide(mInputMsgRequest.getfSide());
                        mAdvCancelOrder.setfTransactTime(mAdvCancelOrder.getfSendingTime());

                        String zAdvCancelOrderFixMsg = mAdvCancelOrder.msgToString();
                        zAdvCancelOrderFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zAdvCancelOrderFixMsg,true,true,mTrxCtl.getConnectionName());

                        if (!mTrxCtl.sendMessageDirect(zAdvCancelOrderFixMsg)){
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send");
                        }
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Found route @cancel order as advertisement");

                    }else{
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Found route @cancel order as advertisement but no controller");
                    }
                }
                
                
            }else{
                long vOriginOrderToken = BookOfJONECSimToken.getInstance.findTokenByBrokerRef(mInputMsgRequest.getfOrigClOrdID());
                long vEveryOrderToken = BookOfJONECSimToken.getInstance.generateTrxToken(mInputMsgRequest.getfClOrdID());
                if (ITMTradingServerConsts.EngineSetup.FIX5_ONLY){
                    
                    if ((vOriginOrderToken > 0) && (vEveryOrderToken > 0)){
                        //.save to memory:
                        BookOfJONECSimOriginRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimOriginRequest(vOriginOrderToken, mInputMsgRequest));

                        BookOfJONECSimEveryRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimEveryRequest(vEveryOrderToken, mInputMsgRequest));

                        FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLine();

                        if (mTrxCtl != null){
                            FIX5JonecDataOrderCancelRequest mNormalCancelOrder = new FIX5JonecDataOrderCancelRequest(new HashMap());
                            mNormalCancelOrder.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.ORDER_CANCEL_REQUEST);
                            mNormalCancelOrder.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                            mNormalCancelOrder.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                            mNormalCancelOrder.setfSenderSubID(mTrxCtl.getTraderCode());

                            mNormalCancelOrder.setfClOrdID(mInputMsgRequest.getfClOrdID());
                            mNormalCancelOrder.setfOrderID(mInputMsgRequest.getfOrderID());
                            mNormalCancelOrder.setfOrigClOrdID(mInputMsgRequest.getfOrigClOrdID());

                            mNormalCancelOrder.setfSymbol(mInputMsgRequest.getfSymbol());
                            mNormalCancelOrder.setfSecuritySubType(mInputMsgRequest.getfSymbolSfx().replace("0",""));

                            mNormalCancelOrder.setfSide(mInputMsgRequest.getfSide());
                            mNormalCancelOrder.setfTransactTime(mNormalCancelOrder.getfSendingTime());

                            String zNormalCancelOrderFixMsg = mNormalCancelOrder.msgToString();
                            zNormalCancelOrderFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zNormalCancelOrderFixMsg,true,true,mTrxCtl.getConnectionName());

                            if (!mTrxCtl.sendMessageDirect(zNormalCancelOrderFixMsg)){
                                //.???:
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @cannot send as normal");
                            }
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Found route @cancel order as normal");

                        }else{
                            //.???:
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Found route @cancel order as normal but no controller");
                        }
                    }

                }else{
                    if ((vOriginOrderToken > 0) && (vEveryOrderToken > 0)){
                        //.save to memory:
                        BookOfJONECSimOriginRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimOriginRequest(vOriginOrderToken, mInputMsgRequest));

                        BookOfJONECSimEveryRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimEveryRequest(vEveryOrderToken, mInputMsgRequest));
                        //.process:
                        OUCHMsgCancelOrder mCancelOrder = new OUCHMsgCancelOrder();
                        mCancelOrder.setType(OUCHConsts.OUCHMessageType.MESSAGETYPE_INBOUND_CANCEL_ORDER);
                        mCancelOrder.setOrderToken(vOriginOrderToken);

                        byte[] btCancelOrder = mCancelOrder.buildMessage();

                        if ((btCancelOrder != null) && (btCancelOrder.length > 0)){
                            ITMSoupBinTCPOUCHPacketController mTrxCtl = ITMSoupBinTCPOUCHPacketMgr.getInstance.getNextActiveConnectionLine();
                            if (mTrxCtl != null){
                                if (!mTrxCtl.getChannel().sendMessageDirect(btCancelOrder)){
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
                    }else{
                        //.invalid input reference:
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "No route @");
                    }
                }
            }
            
            
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
}
