/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dcsplitter.bridge;

import com.itm.fix5.data.jonec.message.struct.FIX5IDXMessage;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.mis.itch.consts.ITCHConsts;
import com.itm.mis.itch.structs.ITCHMsgSystemEvent;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ari Pambudi
 */
public class DCSplitterServerInputBridge {
    
    public final static DCSplitterServerInputBridge getInstance = new DCSplitterServerInputBridge();
    
    public ITMTradingServerSettingsMgr itmSetting                               = ITMTradingServerSettingsMgr.getInstance;
    
    private final AtomicInteger aiCurrentDate                                   = new AtomicInteger(0);
    
    private final ArrayList<FIX5IDXMessage> arrDCMessages = new ArrayList<>();
    
    public DCSplitterServerInputBridge(){
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public int getCurrentDate() {
        return this.aiCurrentDate.get();
    }
    
    public int setCurrentDate(int vNewValue){
        return this.aiCurrentDate.getAndSet(vNewValue);
    }

    public ArrayList<FIX5IDXMessage> getArrDCMessages() {
        return arrDCMessages;
    }
    
    public void addDCMessage(FIX5IDXMessage dcMsg){
        try{
            if (dcMsg != null) {
                arrDCMessages.add(dcMsg);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void clearDCMessages(){
        try{
            FIX5IDXMessage mFirstMsg = null;
            arrDCMessages.clear();
            if (mFirstMsg != null){
                arrDCMessages.add(mFirstMsg);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
}
