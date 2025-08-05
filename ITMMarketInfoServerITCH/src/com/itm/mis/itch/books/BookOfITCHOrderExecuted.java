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
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ari Pambudi
 */
public class BookOfITCHOrderExecuted extends BookOfITCHBase {
    //.single instance:
    public final static BookOfITCHOrderExecuted getInstance = new BookOfITCHOrderExecuted();
    
    private final ArrayList<SheetOfITCHOrderExecuted> lstSheets = new ArrayList<>();
    private final ConcurrentHashMap<Long, Long> chmSheets = new ConcurrentHashMap<>();
    
    public BookOfITCHOrderExecuted() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public boolean addSheet(SheetOfITCHOrderExecuted mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getMessage() != null)){
                //. memory tidak dipakai di remark
                //this.lstSheets.add(mSheet);
                
                if (chmSheets.containsKey(mSheet.getMessage().getMatchId())){
                    Long lNewQty = chmSheets.get(mSheet.getMessage().getMatchId()) + mSheet.getMessage().getQuantity();
                    chmSheets.replace(mSheet.getMessage().getMatchId(), lNewQty);
                }else{
                    chmSheets.put(mSheet.getMessage().getMatchId(), mSheet.getMessage().getQuantity());
                }
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public Long retrieveSheetQty(Long orderNumber){
        Long mOut = 0L;
        if (chmSheets.containsKey(orderNumber)){
            mOut = chmSheets.get(orderNumber);
        }
        
        return mOut;        
    }
    
    public ArrayList<SheetOfITCHOrderExecuted> retrieveAllSheets(){
        return this.lstSheets;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.lstSheets.clear();
            this.chmSheets.clear();
            mOut = this.lstSheets.isEmpty();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
