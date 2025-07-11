/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.client.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderMassActionRequest;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataOrderMassCancel;
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
public class JONECSimWorkDataOrderMassCancel {
    //.single instance:
    public final static JONECSimWorkDataOrderMassCancel getInstance = new JONECSimWorkDataOrderMassCancel();
    
    public JONECSimWorkDataOrderMassCancel() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, ORIDataOrderMassCancel mInputMsgRequest){
        try{
            long vOriginOrderToken = BookOfJONECSimToken.getInstance.generateTrxToken(mInputMsgRequest.getfClOrdID());
            long vEveryOrderToken = vOriginOrderToken; //BrokerReferenceHelper.getOrderID_BrokerRef(mInputMsgRequest.getfClOrdID());
            
            if ((vOriginOrderToken > 0) && (vEveryOrderToken > 0)){
                //.save to memory:
                BookOfJONECSimOriginRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimOriginRequest(vOriginOrderToken, mInputMsgRequest));
                BookOfJONECSimEveryRequest.getInstance.addOrUpdateSheet(new SheetOfJONECSimEveryRequest(vEveryOrderToken, mInputMsgRequest));
                //.testonly:
                
                //.real:
                FIX5JonecDataOrderMassActionRequest mFixMsg;
                String zFixMsg = "";
                FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLine();
                if (mTrxCtl != null){
                    mFixMsg = new FIX5JonecDataOrderMassActionRequest(new HashMap());
                    mFixMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.ORDER_MASS_ACTION_REQUEST);
                    mFixMsg.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
                    mFixMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                    mFixMsg.setfSenderSubID(mTrxCtl.getTraderCode());
                    mFixMsg.setfClOrdID(mInputMsgRequest.getfClOrdID());
                    mFixMsg.setfSecondaryClOrdID(mInputMsgRequest.getfClOrdID() + "mc");
                    mFixMsg.setfMassActionType(FIX5JonecDataConst.FIX5JonecFieldValue.MASS_ACTION_TYPE_CANCEL_ORDERS);
                    mFixMsg.setfMassActionScope(
                                mInputMsgRequest.getfMassWithdrawRequestType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_SECURITY) ? FIX5JonecDataConst.FIX5JonecFieldValue.MASS_ACTION_SCOPE_ALL_ORDERS_FOR_A_SECURITY :
                                mInputMsgRequest.getfMassWithdrawRequestType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_ALL_ORDERS_OR_FIRM) ? FIX5JonecDataConst.FIX5JonecFieldValue.MASS_ACTION_SCOPE_ALL_ORDERS_FOR_A_FIRM :
                                mInputMsgRequest.getfMassWithdrawRequestType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_PARTICULAR_BOARD) ? FIX5JonecDataConst.FIX5JonecFieldValue.MASS_ACTION_SCOPE_PER_BOARD_FOR_A_FIRM :
                                mInputMsgRequest.getfMassWithdrawRequestType()
                            );
                    mFixMsg.setfTransactTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                    if (mInputMsgRequest.getfMassWithdrawRequestType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_SECURITY)){
                        mFixMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
                        mFixMsg.setfSymbol(mInputMsgRequest.getfSymbol());
                    }else if (mInputMsgRequest.getfMassWithdrawRequestType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_ALL_ORDERS_OR_FIRM)){
                        //... .
                    }else if (mInputMsgRequest.getfMassWithdrawRequestType().equalsIgnoreCase(ORIDataConst.ORIFieldValue.MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_PARTICULAR_BOARD)){
                        mFixMsg.setfSecuritySubType((!StringHelper.isNullOrEmpty(mInputMsgRequest.getfSymbolSfx())) ? mInputMsgRequest.getfSymbolSfx().replaceAll("0", "") : "");
                    }
                    
                    zFixMsg = mFixMsg.msgToString();
                    zFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zFixMsg,true,true,mTrxCtl.getConnectionName());

                    if (!mTrxCtl.sendMessageDirect(zFixMsg)){
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
