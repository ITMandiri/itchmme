/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.bridge;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.mis.itch.books.SheetOfITCHBase;
import com.itm.mis.itch.structs.ITCHMsgBase;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.ITCHType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author hirin
 */
public class ITMITCHMsgMemoryMgr {
    public final static ITMITCHMsgMemoryMgr getInstance = new ITMITCHMsgMemoryMgr();
    
    private List<ITMITCHMsgMemoryListener> fMsgMemoryListeners;
    
    private ITMITCHMsgMemoryMgr(){
        fMsgMemoryListeners = new ArrayList<>();
    }
    
    //.listener methods:
    public List<ITMITCHMsgMemoryListener> getMsgMemoryListeners() {
        return fMsgMemoryListeners;
    }
    
    public void setMsgMemoryListeners(List<ITMITCHMsgMemoryListener> fMsgMemoryListeners)  {
        this.fMsgMemoryListeners = fMsgMemoryListeners;
    }
    
    public void addMsgMemoryListener(ITMITCHMsgMemoryListener newListener){
        try{
            if (newListener == null){
                return;
            }
            if (this.fMsgMemoryListeners == null){
                this.fMsgMemoryListeners = new ArrayList<>();
            }
            if (!this.fMsgMemoryListeners.contains(newListener)){ //.@only once per object.@
                this.fMsgMemoryListeners.add(newListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
    }
    
    public void removeMsgMemoryListener(ITMITCHMsgMemoryListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            this.fMsgMemoryListeners.remove(oldListener);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
    }
    
    public void raiseOnMessage(ITCHMsgBase itchMessage, SheetOfITCHBase mSheetBase, ITCHType itchType){
        try{
            
            if (this.fMsgMemoryListeners.size() > 0){
                for (Iterator<ITMITCHMsgMemoryListener> iterSockListener = this.fMsgMemoryListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMITCHMsgMemoryListener lstr = (ITMITCHMsgMemoryListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onMessage(itchMessage, mSheetBase, itchType);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
    }
    
    public void raiseOnMessageRaw(ITCHMsgBase itchMessage){
        try{
            
            if (this.fMsgMemoryListeners.size() > 0){
                for (Iterator<ITMITCHMsgMemoryListener> iterSockListener = this.fMsgMemoryListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMITCHMsgMemoryListener lstr = (ITMITCHMsgMemoryListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onMessageRaw(itchMessage);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
    }
}
