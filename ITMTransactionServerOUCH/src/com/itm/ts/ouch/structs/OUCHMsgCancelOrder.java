/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.structs;

/**
 *
 * @author fredy
 */
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset;

public class OUCHMsgCancelOrder extends OUCHMsgBase {

    private long orderToken;

    public long getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(long orderToken) {
        this.orderToken = orderToken;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 9 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 8 = 9
                    setOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgCancelOrder");
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }

    @Override
    public byte[] buildMessage() {
        byte[] mOut = resetCumulativeBytes()
                .concatenateField(getOrderToken(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
