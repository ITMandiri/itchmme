/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.msgmem.works;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.idx.data.qri.message.struct.QRIDataTradingLimitMessage;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.xtream.inet.trading.consts.ITMTradingServerConsts;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINTradingLimitList;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class JONECSimMsgMemWorkTradingLimitStatus {
    //.single instance:
    public final static JONECSimMsgMemWorkTradingLimitStatus getInstance = new JONECSimMsgMemWorkTradingLimitStatus();
    
    public JONECSimMsgMemWorkTradingLimitStatus() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    //.soon
//    public void doWork(SheetOfOUCHBase mSheet, OUCHMsgTradingLimitStatus mMessage){
//        try{
//            //System.err.println("OUCHMsgTradingLimitStatus : Limit = " + mMessage.getLimit() + ", Traded = " + mMessage.getTraded() + ", Used = " + mMessage.getUsed());
//            
////            //. log-hirin-xxx
////            if (mMessage != null){
////                ITMFileLoggerManager.getInstance.insertLog(this, logSource.OUCH, logLevel.INFO, "OUCHMsgTradingLimitStatus : Limit = " + mMessage.getLimit() + ", Traded = " + mMessage.getTraded() + ", Used = " + mMessage.getUsed());
////            }
//            QRIDataTradingLimitMessage mLimitListMsg = new QRIDataTradingLimitMessage(new HashMap());
////            mLimitListMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
////            mLimitListMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
//            mLimitListMsg.setfSeqNumber(0);
//            mLimitListMsg.setfCurrency("IDR"); //. ???
//            mLimitListMsg.setfExecBroker(ITMTradingServerConsts.BrokerSetup.BROKER_CODE);
//            mLimitListMsg.setfTag("KPEI");
//            mLimitListMsg.setfOpenBal(mMessage.getLimit() - mMessage.getUsed());
//            mLimitListMsg.setfCurrentPos(mMessage.getTraded());
//            mLimitListMsg.setfPlannedPos(mMessage.getUsed());
//            mLimitListMsg.setfLimit1(0);
//            mLimitListMsg.setfLimit1Set("N");
//            mLimitListMsg.setfLimit2(mMessage.getLimit());
//            mLimitListMsg.setfLimit2Set("Y");
//            
//            BookOfMARTINTradingLimitList.getInstance.addOrUpdateSheet(mLimitListMsg);
//            
//            BookOfMARTINTradingLimitList.getInstance.brodcastToSubscriber(mLimitListMsg);
//        }catch(Exception ex0){
//            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
//        }
//    }

}
