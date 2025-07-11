/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.msgmemory;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.ts.ouch.books.BookOfOUCHCancelByOrderID;
import com.itm.ts.ouch.books.BookOfOUCHCancelOrder;
import com.itm.ts.ouch.books.BookOfOUCHEnterOrder;
import com.itm.ts.ouch.books.BookOfOUCHOrderAccepted;
import com.itm.ts.ouch.books.BookOfOUCHOrderCanceled;
import com.itm.ts.ouch.books.BookOfOUCHOrderExecuted;
import com.itm.ts.ouch.books.BookOfOUCHOrderRejected;
import com.itm.ts.ouch.books.BookOfOUCHOrderReplaced;
import com.itm.ts.ouch.books.BookOfOUCHReplaceOrder;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.books.SheetOfOUCHCancelByOrderID;
import com.itm.ts.ouch.books.SheetOfOUCHCancelOrder;
import com.itm.ts.ouch.books.SheetOfOUCHEnterOrder;
import com.itm.ts.ouch.books.SheetOfOUCHOrderAccepted;
import com.itm.ts.ouch.books.SheetOfOUCHOrderCanceled;
import com.itm.ts.ouch.books.SheetOfOUCHOrderExecuted;
import com.itm.ts.ouch.books.SheetOfOUCHOrderRejected;
import com.itm.ts.ouch.books.SheetOfOUCHOrderReplaced;
import com.itm.ts.ouch.books.SheetOfOUCHReplaceOrder;
import com.itm.ts.ouch.bridge.ITMOUCHMsgMemoryMgr;
import com.itm.ts.ouch.msgparser.ITMSoupBinTCPOUCHMsgParser;
import com.itm.ts.ouch.structs.OUCHMsgBase;
import com.itm.ts.ouch.structs.OUCHMsgCancelByOrderID;
import com.itm.ts.ouch.structs.OUCHMsgCancelOrder;
import com.itm.ts.ouch.structs.OUCHMsgEnterOrder;
import com.itm.ts.ouch.structs.OUCHMsgOrderAccepted;
import com.itm.ts.ouch.structs.OUCHMsgOrderCanceled;
import com.itm.ts.ouch.structs.OUCHMsgOrderExecuted;
import com.itm.ts.ouch.structs.OUCHMsgOrderRejected;
import com.itm.ts.ouch.structs.OUCHMsgOrderReplaced;
import com.itm.ts.ouch.structs.OUCHMsgReplaceOrder;
import com.itm.ts.ouch.structs.OUCHMsgUnknown;

/**
 *
 * @author fredy
 */
public class ITMOUCHMsgMemory {
    //.single instance:
    public final static ITMOUCHMsgMemory getInstance = new ITMOUCHMsgMemory();
    
    public ITMOUCHMsgMemory() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean mapMessage(byte[] btMessageBytes, ITMSoupBinTCPMsgBase mSoupBinPacketObject){
        boolean mOut = false;
        try{
            SheetOfOUCHBase mSheetBase = null;
            OUCHMsgBase ouchMessage = ITMSoupBinTCPOUCHMsgParser.getInstance.parseMessage(btMessageBytes, mSoupBinPacketObject);
            if (ouchMessage != null){
                if (ouchMessage instanceof OUCHMsgUnknown){
                    //... .
                }else if (ouchMessage instanceof OUCHMsgOrderAccepted){
                    OUCHMsgOrderAccepted mMsg = (OUCHMsgOrderAccepted)ouchMessage;
                    SheetOfOUCHOrderAccepted mSheet = new SheetOfOUCHOrderAccepted(mMsg);
                    BookOfOUCHOrderAccepted.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else if (ouchMessage instanceof OUCHMsgCancelByOrderID){
                    OUCHMsgCancelByOrderID mMsg = (OUCHMsgCancelByOrderID)ouchMessage;
                    SheetOfOUCHCancelByOrderID mSheet = new SheetOfOUCHCancelByOrderID(mMsg);
                    BookOfOUCHCancelByOrderID.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else if (ouchMessage instanceof OUCHMsgCancelOrder){
                    OUCHMsgCancelOrder mMsg = (OUCHMsgCancelOrder)ouchMessage;
                    SheetOfOUCHCancelOrder mSheet = new SheetOfOUCHCancelOrder(mMsg);
                    BookOfOUCHCancelOrder.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else if (ouchMessage instanceof OUCHMsgOrderCanceled){
                    OUCHMsgOrderCanceled mMsg = (OUCHMsgOrderCanceled)ouchMessage;
                    SheetOfOUCHOrderCanceled mSheet = new SheetOfOUCHOrderCanceled(mMsg);
                    BookOfOUCHOrderCanceled.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else if (ouchMessage instanceof OUCHMsgEnterOrder){
                    OUCHMsgEnterOrder mMsg = (OUCHMsgEnterOrder)ouchMessage;
                    SheetOfOUCHEnterOrder mSheet = new SheetOfOUCHEnterOrder(mMsg);
                    BookOfOUCHEnterOrder.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else if (ouchMessage instanceof OUCHMsgOrderExecuted){
                    OUCHMsgOrderExecuted mMsg = (OUCHMsgOrderExecuted)ouchMessage;
                    SheetOfOUCHOrderExecuted mSheet = new SheetOfOUCHOrderExecuted(mMsg);
                    BookOfOUCHOrderExecuted.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else if (ouchMessage instanceof OUCHMsgOrderRejected){
                    OUCHMsgOrderRejected mMsg = (OUCHMsgOrderRejected)ouchMessage;
                    SheetOfOUCHOrderRejected mSheet = new SheetOfOUCHOrderRejected(mMsg);
                    BookOfOUCHOrderRejected.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else if (ouchMessage instanceof OUCHMsgReplaceOrder){
                    OUCHMsgReplaceOrder mMsg = (OUCHMsgReplaceOrder)ouchMessage;
                    SheetOfOUCHReplaceOrder mSheet = new SheetOfOUCHReplaceOrder(mMsg);
                    BookOfOUCHReplaceOrder.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else if (ouchMessage instanceof OUCHMsgOrderReplaced){
                    OUCHMsgOrderReplaced mMsg = (OUCHMsgOrderReplaced)ouchMessage;
                    SheetOfOUCHOrderReplaced mSheet = new SheetOfOUCHOrderReplaced(mMsg);
                    BookOfOUCHOrderReplaced.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }else{
                    //... .
                }
                //... .
            }
            //. broadcast event
            ITMOUCHMsgMemoryMgr.getInstance.raiseOnMessage(ouchMessage, mSheetBase);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearAllBooks(){
        boolean mOut = false;
        try{
            
            BookOfOUCHOrderAccepted.getInstance.clearBook();
            BookOfOUCHCancelByOrderID.getInstance.clearBook();
            BookOfOUCHCancelOrder.getInstance.clearBook();
            BookOfOUCHOrderCanceled.getInstance.clearBook();
            BookOfOUCHEnterOrder.getInstance.clearBook();
            BookOfOUCHOrderExecuted.getInstance.clearBook();
            BookOfOUCHOrderRejected.getInstance.clearBook();
            BookOfOUCHReplaceOrder.getInstance.clearBook();
            BookOfOUCHOrderReplaced.getInstance.clearBook();
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}