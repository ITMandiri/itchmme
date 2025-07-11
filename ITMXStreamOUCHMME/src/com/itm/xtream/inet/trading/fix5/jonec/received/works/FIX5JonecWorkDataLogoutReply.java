/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogoutReply;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataLogoutReply {
    //.single instance:
    public final static FIX5JonecWorkDataLogoutReply getInstance = new FIX5JonecWorkDataLogoutReply();
    
    public FIX5JonecWorkDataLogoutReply() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataLogoutReply mInputMsgRequest){
        try{
            controller.setIsAdminLoggedOn(false);
            controller.setLastMessage(mInputMsgRequest.getfText());
            controller.stopConnection();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
}
