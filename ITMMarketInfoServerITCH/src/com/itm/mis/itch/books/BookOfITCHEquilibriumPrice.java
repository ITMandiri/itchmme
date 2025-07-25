/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ari Pambudi
 */
public class BookOfITCHEquilibriumPrice extends BookOfITCHBase {
    //.single instance:
    public final static BookOfITCHEquilibriumPrice getInstance = new BookOfITCHEquilibriumPrice();
    
    private final ConcurrentHashMap<Integer, SheetOfITCHEquilibriumPrice> chmDirectory = new ConcurrentHashMap<>();
    
    public BookOfITCHEquilibriumPrice() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheet(SheetOfITCHEquilibriumPrice mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getMessage() != null) && (mSheet.getMessage().getOrderBookId()> 0)){
                int vId = mSheet.getMessage().getOrderBookId();
                if (vId == 4099){
                    System.err.println("");
                }
                if (this.chmDirectory.containsKey(vId)){
                    this.chmDirectory.replace(vId, mSheet);
                }else{
                    this.chmDirectory.put(vId, mSheet);
                }
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentHashMap<Integer, SheetOfITCHEquilibriumPrice> retrieveAllSheets(){
        return this.chmDirectory;
    }
    
    public SheetOfITCHEquilibriumPrice retrieveSheet(int vOrderBookId){
        SheetOfITCHEquilibriumPrice mOut = null;
        try{
            mOut = this.chmDirectory.get(vOrderBookId);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.chmDirectory.clear();
            mOut = this.chmDirectory.isEmpty();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
