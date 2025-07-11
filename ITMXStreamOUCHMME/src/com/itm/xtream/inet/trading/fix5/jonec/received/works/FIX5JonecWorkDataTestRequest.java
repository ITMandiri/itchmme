/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecMsgType;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTestRequest;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataTestRequest {
    //.single instance:
    public final static FIX5JonecWorkDataTestRequest getInstance = new FIX5JonecWorkDataTestRequest();
    
    public FIX5JonecWorkDataTestRequest() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataTestRequest mInputMsgRequest){
        try{
            
            FIX5JonecDataTestRequest mMsg = new FIX5JonecDataTestRequest(new HashMap());
            mMsg.setfMsgType(FIX5JonecMsgType.TEST_REQUEST);
            mMsg.setfMsgSeqNum(controller.getNextTXSequencedNo());
            mMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
            mMsg.setfTestReqID(mInputMsgRequest.getfTestReqID());
            String zMsg = mMsg.msgToString();
            zMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zMsg,true,true,controller.getConnectionName());
            channel.sendMessageDirect(zMsg);
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
}
