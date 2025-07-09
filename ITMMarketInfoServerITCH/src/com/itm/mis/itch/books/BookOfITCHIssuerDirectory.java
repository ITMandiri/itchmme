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
public class BookOfITCHIssuerDirectory extends BookOfITCHBase {
    //.single instance:
    public final static BookOfITCHIssuerDirectory getInstance = new BookOfITCHIssuerDirectory();
    
    private final ConcurrentHashMap<Integer, SheetOfITCHIssuerDirectory> chmDirectory = new ConcurrentHashMap<>();
    
    public BookOfITCHIssuerDirectory() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheet(SheetOfITCHIssuerDirectory mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getMessage() != null) && (mSheet.getMessage().getIssuerId()> 0)){
                int vId = mSheet.getMessage().getIssuerId();
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
    
    public ConcurrentHashMap<Integer, SheetOfITCHIssuerDirectory> retrieveAllSheets(){
        return this.chmDirectory;
    }
    
    public SheetOfITCHIssuerDirectory retrieveSheet(int vIssuerId){
        SheetOfITCHIssuerDirectory mOut = null;
        try{
            mOut = this.chmDirectory.get(vIssuerId);
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
