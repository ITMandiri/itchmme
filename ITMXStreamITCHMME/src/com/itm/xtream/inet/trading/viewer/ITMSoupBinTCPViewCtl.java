/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.viewer;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPViewCtl {
    //.target display:
    private ITMSoupBinTCPViewFrame viewFrame;
    
    public ITMSoupBinTCPViewCtl() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
        
    }
    
    public boolean checkFrame()
    {
        boolean bOut = false;
        try{
            if (this.viewFrame == null){
                this.viewFrame = new ITMSoupBinTCPViewFrame();
            }
            if (!this.viewFrame.isDisplayable()){
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "-ViewFrame DESTROYED : not displayable. Window re-created.");
                this.viewFrame = new ITMSoupBinTCPViewFrame();
            }
            bOut = true;
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean showFrame() //.tampilkan viewframe.
    {
        boolean bOut = false;
        try{
           //.buat (jika belum ada) dan tampilkan:
            checkFrame();
            this.viewFrame.showThis();
            bOut = true;
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean hideFrame() //.sembunyikan viewframe.
    {
        boolean bOut = false;
        try{
            if (this.viewFrame != null){
                //.tampilkan:
                this.viewFrame.hideThis();
                bOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return bOut;
    }

}
