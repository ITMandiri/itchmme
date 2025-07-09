/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.dynamicstruct;

import com.itm.soupbintcp.bridge.dynamicstruct.ITMSoupBinTCPDynStructCore.CCS_message;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPDynStructMsgBase {
    public ITMSoupBinTCPDynStructCore.CCS_message msg;
    
    public ITMSoupBinTCPDynStructMsgBase(ITMSoupBinTCPDynStructCore parentCore){
        msg = parentCore.new CCS_message();
    }
    
}
