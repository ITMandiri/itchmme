/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.msgmem.selector;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.mis.itch.books.SheetOfITCHBase;
import com.itm.mis.itch.bridge.ITMITCHMsgMemoryListener;
import com.itm.mis.itch.bridge.ITMITCHMsgMemoryMgr;
import com.itm.mis.itch.structs.ITCHMsgBase;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.ITCHType;

/**
 *
 * @author fredy
 */
public class MARTINSimMsgMemory implements ITMITCHMsgMemoryListener {
    //.single instance ya:
    public final static MARTINSimMsgMemory getInstance = new MARTINSimMsgMemory();
    
    private boolean bStartListerner = false;
    public void startListener(){
        if (!bStartListerner){
            ITMITCHMsgMemoryMgr.getInstance.addMsgMemoryListener(this);
            bStartListerner = true;
        }
    }
    
    private MARTINSimMsgMemory() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    @Override
    public void onMessage(ITCHMsgBase itchMessage, SheetOfITCHBase mSheetBase, ITCHType itchType) {
       try{
            //.???:
       }catch(Exception ex0){
           ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);

       }
    }

    @Override
    public void onMessageRaw(ITCHMsgBase itchMessage) {
        try{
            //.???:
       }catch(Exception ex0){
           ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);

       }
    }
    
}

