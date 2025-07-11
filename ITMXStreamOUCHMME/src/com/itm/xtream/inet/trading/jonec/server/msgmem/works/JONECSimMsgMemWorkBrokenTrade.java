/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.msgmem.works;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.ts.ouch.books.SheetOfOUCHBase;

/**
 *
 * @author fredy
 */
public class JONECSimMsgMemWorkBrokenTrade {
    //.single instance:
    public final static JONECSimMsgMemWorkBrokenTrade getInstance = new JONECSimMsgMemWorkBrokenTrade();
    
    public JONECSimMsgMemWorkBrokenTrade() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    //.soon
//    public void doWork(SheetOfOUCHBase mSheet, OUCHMsgBrokenTrade mMessage){
//        try{
//            
//        }catch(Exception ex0){
//            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
//        }
//    }

}

