/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.bridge;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.structs.OUCHMsgBase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author fredy
 */
public class ITMOUCHMsgMemoryMgr {
    public final static ITMOUCHMsgMemoryMgr getInstance = new ITMOUCHMsgMemoryMgr();
    
    private List<ITMOUCHMsgMemoryListener> fMsgMemoryListeners;
    
    private ITMOUCHMsgMemoryMgr(){
        fMsgMemoryListeners = new ArrayList<>();
    }
    
    //.listener methods:
    public List<ITMOUCHMsgMemoryListener> getMsgMemoryListeners() {
        return fMsgMemoryListeners;
    }
    
    public void setMsgMemoryListeners(List<ITMOUCHMsgMemoryListener> fMsgMemoryListeners)  {
        this.fMsgMemoryListeners = fMsgMemoryListeners;
    }
    
    public void addMsgMemoryListener(ITMOUCHMsgMemoryListener newListener){
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
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
    public void removeMsgMemoryListener(ITMOUCHMsgMemoryListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            this.fMsgMemoryListeners.remove(oldListener);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
    public void raiseOnMessage(OUCHMsgBase ouchMessage, SheetOfOUCHBase mSheetBase){
        try{
            
            if (this.fMsgMemoryListeners.size() > 0){
                for (Iterator<ITMOUCHMsgMemoryListener> iterSockListener = this.fMsgMemoryListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMOUCHMsgMemoryListener lstr = (ITMOUCHMsgMemoryListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onMessage(ouchMessage, mSheetBase);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
}