/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogonReply;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataResendRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataSequenceReset;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataLogonReply {
    //.single instance:
    public final static FIX5JonecWorkDataLogonReply getInstance = new FIX5JonecWorkDataLogonReply();
    
    public FIX5JonecWorkDataLogonReply() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataLogonReply mInputMsgRequest){
        try{
            if (!controller.isAdminLoggedOn()){
                controller.setIsAdminLoggedOn(true);
            }
            
            if (controller.getMsgSubGroupType() == FIX5IDXBridgeController.FIX5IDXSubGroupMessageType.FIX5_DROP_COPY) {
                FIX5IDXBridgeManager.getInstance.subscribeDropCopy();
                controller.setIsAdminSubscribed(true);
            }
            //.20260922: untuk handle message yang belum dikirim bursa, harus mengirim resend request dengan sequence dari berapa yang belum dikirim
//            if (controller.getMsgSubGroupType() == FIX5IDXBridgeController.FIX5IDXSubGroupMessageType.FIX5_ORDER_ENTRY) {
                if (mInputMsgRequest.getfMsgSeqNum() > controller.getfPrevRXSequencedNo()+ 1) {
                    FIX5JonecDataResendRequest mMsg = new FIX5JonecDataResendRequest(new HashMap());
                    mMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.RESEND);
                    mMsg.setfMsgSeqNum(controller.getNextTXSequencedNo()); 
                    mMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
                    mMsg.setfSenderSubID(controller.getConnectorCode());
                    mMsg.setfBeginSeqNo(StringHelper.fromLong(controller.getfPrevRXSequencedNo()+ 1));
                    mMsg.setfEndSeqNo("0");
                    
                    String zSeqResendFixMsg = mMsg.msgToString();
                    zSeqResendFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zSeqResendFixMsg,true,true,controller.getConnectionName());
                    System.err.println("Melakukan Resend "+controller.getConnectionName() + ", BeginSeqNo="+ mMsg.getfBeginSeqNo() +", SeqNo=" +mInputMsgRequest.getfMsgSeqNum());
                    if (controller.sendMessageDirect(zSeqResendFixMsg)){
                        //.???:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Send resend request");
                    }
                }
//            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
}
