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
public class BookOfITCHMarketSegmentDirectory extends BookOfITCHBase {
    //.single instance:
    public final static BookOfITCHMarketSegmentDirectory getInstance = new BookOfITCHMarketSegmentDirectory();
    
    private final ConcurrentHashMap<Integer, SheetOfITCHMarketSegmentDirectory> chmDirectory = new ConcurrentHashMap<>();
    
    public BookOfITCHMarketSegmentDirectory() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheet(SheetOfITCHMarketSegmentDirectory mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getMessage() != null) && (mSheet.getMessage().getMarketSegmentId()> 0)){
                int vId = mSheet.getMessage().getMarketSegmentId();
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
    
    public ConcurrentHashMap<Integer, SheetOfITCHMarketSegmentDirectory> retrieveAllSheets(){
        return this.chmDirectory;
    }
    
    public SheetOfITCHMarketSegmentDirectory retrieveSheet(int vMarketSegmentId){
        SheetOfITCHMarketSegmentDirectory mOut = null;
        try{
            mOut = this.chmDirectory.get(vMarketSegmentId);
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
