/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.util.ArrayList;

/**
 *
 * @author fredy
 */
public class BookOfOUCHOrderExecuted extends BookOfOUCHBase {
    //.single instance:
    public final static BookOfOUCHOrderExecuted getInstance = new BookOfOUCHOrderExecuted();
    
    private final ArrayList<SheetOfOUCHOrderExecuted> lstSheets = new ArrayList<>();
    
    public BookOfOUCHOrderExecuted() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.OUCH, logLevel.INIT, "");
    }
    
    public boolean addSheet(SheetOfOUCHOrderExecuted mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getMessage() != null)){
                this.lstSheets.add(mSheet);
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.OUCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ArrayList<SheetOfOUCHOrderExecuted> retrieveAllSheets(){
        return this.lstSheets;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.lstSheets.clear();
            mOut = this.lstSheets.isEmpty();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.OUCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}