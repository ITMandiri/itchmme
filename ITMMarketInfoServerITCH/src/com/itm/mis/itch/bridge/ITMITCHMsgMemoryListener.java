/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.bridge;

import com.itm.mis.itch.books.SheetOfITCHBase;
import com.itm.mis.itch.structs.ITCHMsgBase;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.ITCHType;

/**
 *
 * @author hirin
 */
public interface ITMITCHMsgMemoryListener {
    public abstract void onMessage (ITCHMsgBase itchMessage, SheetOfITCHBase mSheetBase, ITCHType itchType);
    public abstract void onMessageRaw (ITCHMsgBase itchMessage);
}
