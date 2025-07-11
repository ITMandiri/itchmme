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

public class OUCHMsgOrderRejected extends OUCHMsgBase {

    private long timestamp;
    private long orderToken;
    private long orderId;
    private int rejectCode;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(long orderToken) {
        this.orderToken = orderToken;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(int rejectCode) {
        this.rejectCode = rejectCode;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 29 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 8 + 8 + 8 + 4 = 29
                    setTimestamp(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8));
                    setOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setOrderId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8));
                    setRejectCode(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 4));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgOrderRejected");
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
                .concatenateField(getTimestamp(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8)
                .concatenateField(getOrderToken(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8)
                .concatenateField(getOrderId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8)
                .concatenateField(getRejectCode(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 4)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
