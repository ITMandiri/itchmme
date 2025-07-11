/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import java.util.Date;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHBase extends ITMSoupBinTCPBridgePacketFormat {
    
    private Date messageDate = null;

    public Date getMessageDate() {
        return this.messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
    
}
