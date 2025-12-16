/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataQuoteStatusReport;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataQuoteStatusReport {
    public final static FIX5JonecWorkDataQuoteStatusReport getInstance = new FIX5JonecWorkDataQuoteStatusReport();
    
    public FIX5JonecWorkDataQuoteStatusReport() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataQuoteStatusReport mInputMsgRequest){
        try {
            //....
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
}
