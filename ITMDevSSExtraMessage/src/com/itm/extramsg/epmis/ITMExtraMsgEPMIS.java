/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.extramsg.epmis;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;

/**
 *
 * @author Ari Pambudi
 */
public class ITMExtraMsgEPMIS {
    //.single instance:
    public final static ITMExtraMsgEPMIS getInstance = new ITMExtraMsgEPMIS();
    
    public ITMExtraMsgEPMIS() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.SERVER, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public String getExtraMessageContent(String zMessageCode, Object mObjectThis, Object mObjectA, Object mObjectB, Object mObjectC, Object mObjectD, Object mObjectE){
        String mOut = "";
        try{
            if ((zMessageCode != null) && (!zMessageCode.equals(""))){
                
            }
        }catch(Exception ex0){
            //System.err.println(ex0);
        }
        return mOut;
    }
    
}
