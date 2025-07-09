/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.backup;

import com.itm.soupbintcp.bridge.dynamicstruct.ITMSoupBinTCPDynStructMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeBackupMsgBase {
    public String saveDateStr = null;
    public String saveTimeStr = null;
    //////public Date saveDate = null; //.do not use this! make system slow;
    public boolean isOutput = false;
    public long recordNo = 0;
    public String msgType = "";
    public byte[] arbMessage = null;
    public boolean isHeaderParsed = false;
    public boolean isContentParsed = false;
    public ITMSoupBinTCPMsgBase sbMessage = null;
    public ITMSoupBinTCPDynStructMsgBase dynMessage = null;
    
}
