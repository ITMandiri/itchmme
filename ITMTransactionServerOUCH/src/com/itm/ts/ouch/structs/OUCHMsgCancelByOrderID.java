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

public class OUCHMsgCancelByOrderID extends OUCHMsgBase {

    private int orderBookId;
    private String side;
    private long orderId;

    public int getOrderBookId() {
        return orderBookId;
    }

    public void setOrderBookId(int orderBookId) {
        this.orderBookId = orderBookId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 14 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 4 + 1 + 8 = 14
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setSide(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 1));
                    setOrderId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 6, 8));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgCancelByOrderID");
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
                .concatenateField(getOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4)
                .concatenateField(getSide(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 1)
                .concatenateField(getOrderId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 6, 8)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
