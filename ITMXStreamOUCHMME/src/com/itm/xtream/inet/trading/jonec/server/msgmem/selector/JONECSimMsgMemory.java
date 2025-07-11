/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.msgmem.selector;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.bridge.ITMOUCHMsgMemoryListener;
import com.itm.ts.ouch.bridge.ITMOUCHMsgMemoryMgr;
import com.itm.ts.ouch.structs.OUCHMsgBase;
import com.itm.ts.ouch.structs.OUCHMsgCancelOrder;
import com.itm.ts.ouch.structs.OUCHMsgEnterOrder;
import com.itm.ts.ouch.structs.OUCHMsgOrderAccepted;
import com.itm.ts.ouch.structs.OUCHMsgOrderCanceled;
import com.itm.ts.ouch.structs.OUCHMsgOrderExecuted;
import com.itm.ts.ouch.structs.OUCHMsgOrderRejected;
import com.itm.ts.ouch.structs.OUCHMsgOrderReplaced;
import com.itm.ts.ouch.structs.OUCHMsgReplaceOrder;
import com.itm.ts.ouch.structs.OUCHMsgUnknown;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkAccepted;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkBrokenTrade;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkCanceled;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkExecutedOrder;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkRejectedOrder;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkReplaced;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkSystemEvent;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkTradingLimit;
import com.itm.xtream.inet.trading.jonec.server.msgmem.works.JONECSimMsgMemWorkTradingLimitStatus;

/**
 *
 * @author fredy
 */
public class JONECSimMsgMemory implements ITMOUCHMsgMemoryListener {
    //.single instance ya:
    public final static JONECSimMsgMemory getInstance = new JONECSimMsgMemory();
    
    private boolean bStartListerner = false;
    public void startListener(){
        if (!bStartListerner){
            ITMOUCHMsgMemoryMgr.getInstance.addMsgMemoryListener(this);
            bStartListerner = true;
        }
    }
    
    private JONECSimMsgMemory() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean processMsgOUCH(OUCHMsgBase ouchMessage, SheetOfOUCHBase mSheet){
        
        boolean mOut = false;
        try{
            if (ouchMessage != null){
                if (ouchMessage instanceof OUCHMsgUnknown){
                    //... .
                }else if (ouchMessage instanceof OUCHMsgOrderAccepted){
                    JONECSimMsgMemWorkAccepted.getInstance.doWork(mSheet, (OUCHMsgOrderAccepted) ouchMessage);
                    //... .
                    //.soon
//                }else if (ouchMessage instanceof OUCHMsgBrokenTrade){
//                    JONECSimMsgMemWorkBrokenTrade.getInstance.doWork(mSheet, (OUCHMsgBrokenTrade) ouchMessage);
//                    //... .
                }else if (ouchMessage instanceof OUCHMsgCancelOrder){
                    //... .
                }else if (ouchMessage instanceof OUCHMsgOrderCanceled){
                    JONECSimMsgMemWorkCanceled.getInstance.doWork(mSheet, (OUCHMsgOrderCanceled) ouchMessage);
                    //... .
                }else if (ouchMessage instanceof OUCHMsgEnterOrder){
                    //... .
                }else if (ouchMessage instanceof OUCHMsgOrderExecuted){
                    JONECSimMsgMemWorkExecutedOrder.getInstance.doWork(mSheet, (OUCHMsgOrderExecuted) ouchMessage);
                    //... .
                }else if (ouchMessage instanceof OUCHMsgOrderRejected){
                    JONECSimMsgMemWorkRejectedOrder.getInstance.doWork(mSheet, (OUCHMsgOrderRejected) ouchMessage);
                    //... .
                }else if (ouchMessage instanceof OUCHMsgReplaceOrder){
                    //... .
                }else if (ouchMessage instanceof OUCHMsgOrderReplaced){
                    JONECSimMsgMemWorkReplaced.getInstance.doWork(mSheet, (OUCHMsgOrderReplaced) ouchMessage);
                    //... .
//                }else if (ouchMessage instanceof OUCHMsgSystemEvent){
//                    JONECSimMsgMemWorkSystemEvent.getInstance.doWork(mSheet, (OUCHMsgSystemEvent) ouchMessage);
//                    //... .
//                }else if (ouchMessage instanceof OUCHMsgTradingLimit){
//                    JONECSimMsgMemWorkTradingLimit.getInstance.doWork(mSheet, (OUCHMsgTradingLimit) ouchMessage);
//                    //... .
//                }else if (ouchMessage instanceof OUCHMsgTradingLimitStatus){
//                    JONECSimMsgMemWorkTradingLimitStatus.getInstance.doWork(mSheet, (OUCHMsgTradingLimitStatus) ouchMessage);
                    //... .
                }
                //... .
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    @Override
    public void onMessage(OUCHMsgBase itchMessage, SheetOfOUCHBase mSheetBase) {
        try{
            processMsgOUCH(itchMessage, mSheetBase);
       }catch(Exception ex0){
           ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);

       }
    }
    
}