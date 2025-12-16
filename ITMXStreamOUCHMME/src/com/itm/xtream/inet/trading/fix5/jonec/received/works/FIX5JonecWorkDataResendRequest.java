/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataResendRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataSequenceReset;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataResendRequest {
    //.single instance:
    public final static FIX5JonecWorkDataResendRequest getInstance = new FIX5JonecWorkDataResendRequest();
    
    public FIX5JonecWorkDataResendRequest() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataResendRequest mInputMsgRequest){
        try{
            System.err.println("RESEND REQUEST");
            
            FIX5JonecDataSequenceReset mMsg = new FIX5JonecDataSequenceReset(new HashMap());
            mMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.SEQUENCE_RESET);
            mMsg.setfMsgSeqNum(controller.getNextTXSequencedNo());
            mMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
            mMsg.setfSenderSubID(controller.getConnectorCode());
            mMsg.setfSenderCompID(FIX5JonecDataConst.FIX5JonecFieldValue.SENDER_COMP_ID);
            mMsg.setfPossDupflag("Y");
            
            mMsg.setfGapFillFlag("Y");
            mMsg.setfNewSeqNo(StringHelper.fromLong(controller.getNextTXSequencedNo() + 1));
            
            String zSeqResetFixMsg = mMsg.msgToString();
            zSeqResetFixMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zSeqResetFixMsg,true,true,controller.getConnectionName());

            if (controller.sendMessageDirect(zSeqResetFixMsg)){
                //.???:
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Send reset sequence");
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
}
