/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.message.bridge;

import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldFmt;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldTag;
import com.itm.generic.engine.socket.setup.ITMSocketCustomeLineFactoryInterface;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5IDXBridgeCustomLineFactoryWorker implements ITMSocketCustomeLineFactoryInterface {
    
    private String zLineData = "";
    
    public FIX5IDXBridgeCustomLineFactoryWorker() {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }

    @Override
    public String[] onCustomDataReceived(String zLineData) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String[] mOut = new String[]{};
        if (zLineData != null && zLineData.length() > 0){
            //////System.out.println("@FIX5_DATA_RECEIVED=" + zLineData);
            List<String> lstOut = new ArrayList<>();
            this.zLineData += zLineData;
            int cLimitLineCount = 1000;
            int pEnd1 = this.zLineData.indexOf(FIX5JonecFieldFmt.FIELD_SEPARATOR + FIX5JonecFieldTag.CHECKSUM + FIX5JonecFieldFmt.KV_SEPARATOR);
            int pEnd2 = 0;
            while(pEnd1 >= 0){
                pEnd1 += (FIX5JonecFieldFmt.FIELD_SEPARATOR + FIX5JonecFieldTag.CHECKSUM + FIX5JonecFieldFmt.KV_SEPARATOR).length();
                pEnd2 = this.zLineData.indexOf(FIX5JonecFieldFmt.FIELD_SEPARATOR, pEnd1);
                if (pEnd2 > 0){
                    lstOut.add(this.zLineData.substring(0, pEnd2 + 1));
                    this.zLineData = this.zLineData.substring(pEnd2 + 1);
                    if (!StringHelper.isNullOrEmpty(this.zLineData)){
                        pEnd1 = this.zLineData.indexOf(FIX5JonecFieldFmt.FIELD_SEPARATOR + FIX5JonecFieldTag.CHECKSUM + FIX5JonecFieldFmt.KV_SEPARATOR);
                    }else{
                        pEnd1 = -1;
                    }
                }else{
                    pEnd1 = -1;
                }
                cLimitLineCount--;
                if ((cLimitLineCount <= 0) || (pEnd1 < 0) || (pEnd2 < 0)){
                    break;
                }
            }
            mOut = lstOut.toArray(mOut);
        }
        return mOut;
        
    }

    @Override
    public void onCustomDataReset() {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.zLineData = "";
    }
    
}
