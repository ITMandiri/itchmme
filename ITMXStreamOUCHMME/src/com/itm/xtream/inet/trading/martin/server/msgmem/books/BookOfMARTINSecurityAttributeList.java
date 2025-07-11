/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.msgmem.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.idx.data.qri.message.struct.QRIDataSecurityAttributeMessage;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class BookOfMARTINSecurityAttributeList {
    
    public final static BookOfMARTINSecurityAttributeList getInstance = new BookOfMARTINSecurityAttributeList();
    
    private final ConcurrentHashMap<String, QRIDataSecurityAttributeMessage> chmSheets = new ConcurrentHashMap<>();
    
    public BookOfMARTINSecurityAttributeList(){
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean addOrUpdateSheet(QRIDataSecurityAttributeMessage mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getfSecCReportCode() != null)){
                String vId = mSheet.getfSecCReportCode();
                if (this.chmSheets.containsKey(vId)){
                    this.chmSheets.replace(vId, mSheet);
                }else{
                    this.chmSheets.put(vId, mSheet);
                }
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentHashMap<String, QRIDataSecurityAttributeMessage> retrieveAllSheets(){
        return this.chmSheets;
    }
    
    public QRIDataSecurityAttributeMessage retrieveSheet(String vId){
        QRIDataSecurityAttributeMessage mOut = null;
        try{
            mOut = this.chmSheets.get(vId);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.chmSheets.clear();
            mOut = this.chmSheets.isEmpty();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
}
