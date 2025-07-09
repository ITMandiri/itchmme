/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.dynamicstruct;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.util.ArrayList;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeDynStructOUCH {
    
    public final static ITMSoupBinTCPBridgeDynStructOUCH getInstance = new ITMSoupBinTCPBridgeDynStructOUCH();
    
    public ITMSoupBinTCPDynStructOUCH loadToDynStruct(String zJSONInput, ArrayList<String> arrErrorMsgs){
        ITMSoupBinTCPDynStructOUCH mOut = null;
        try{
            
            Gson mGson = new Gson();
            mOut = mGson.fromJson(zJSONInput, ITMSoupBinTCPDynStructOUCH.class);
            ITMSoupBinTCPBridgeDynStructValidator.getInstance.validateDynStruct(mOut, arrErrorMsgs);
            
        }catch(JsonSyntaxException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
