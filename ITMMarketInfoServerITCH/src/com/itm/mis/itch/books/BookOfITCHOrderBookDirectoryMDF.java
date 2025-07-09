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
public class BookOfITCHOrderBookDirectoryMDF extends BookOfITCHBase {
    //.single instance:
    public final static BookOfITCHOrderBookDirectoryMDF getInstance = new BookOfITCHOrderBookDirectoryMDF();
    
    private final ConcurrentHashMap<Integer, SheetOfITCHOrderBookDirectoryMDF> chmDirectory = new ConcurrentHashMap<>();
    
    public BookOfITCHOrderBookDirectoryMDF() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheet(SheetOfITCHOrderBookDirectoryMDF mSheet){
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
    
    public ConcurrentHashMap<Integer, SheetOfITCHOrderBookDirectoryMDF> retrieveAllSheets(){
        return this.chmDirectory;
    }
    
    public SheetOfITCHOrderBookDirectoryMDF retrieveSheet(int vOrderBookId){
        SheetOfITCHOrderBookDirectoryMDF mOut = null;
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
