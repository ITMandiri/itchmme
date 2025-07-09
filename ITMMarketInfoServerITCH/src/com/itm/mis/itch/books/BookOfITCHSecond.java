/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.util.ArrayList;

/**
 *
 * @author Ari Pambudi
 */
public class BookOfITCHSecond extends BookOfITCHBase {
    //.single instance:
    public final static BookOfITCHSecond getInstance = new BookOfITCHSecond();
    
    private final ArrayList<SheetOfITCHSecond> lstSheets = new ArrayList<>();
    
    public BookOfITCHSecond() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public boolean addSheet(SheetOfITCHSecond mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getMessage() != null)){
                this.lstSheets.add(mSheet);
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ArrayList<SheetOfITCHSecond> retrieveAllSheets(){
        return this.lstSheets;
    }
    
    public long retrieveNearestTimeStampSeconds(){
        long mOut = 0;
        try{
            if (!this.lstSheets.isEmpty()){
                mOut = this.lstSheets.get(this.lstSheets.size() - 1).getMessage().getSeconds();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearBook(boolean bPreserveLastSheet){
        boolean mOut = false;
        try{
            if (bPreserveLastSheet && (!this.lstSheets.isEmpty())){
                SheetOfITCHSecond mLastSheet = this.lstSheets.get(this.lstSheets.size() - 1);
                this.lstSheets.clear();
                mOut = this.lstSheets.isEmpty();
                if (mLastSheet != null){
                    addSheet(mLastSheet);
                }
            }else{
                this.lstSheets.clear();
                mOut = this.lstSheets.isEmpty();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
