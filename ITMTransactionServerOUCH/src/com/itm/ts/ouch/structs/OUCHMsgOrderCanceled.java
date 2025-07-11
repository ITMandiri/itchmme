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

public class OUCHMsgOrderCanceled extends OUCHMsgBase {

    private long timestamp;
    private long orderToken;
    private int orderBookId;
    private String side;
    private long orderId;
    private byte cancelReason;

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

    public byte getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(byte cancelReason) {
        this.cancelReason = cancelReason;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 31 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 8 + 8 + 4 + 1 + 8 + 1 = 31
                    setTimestamp(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8));
                    setOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 4));
                    setSide(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 21, 1));
                    setOrderId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 22, 8));
                    setCancelReason(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 30, 1));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgOrderCanceled");
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
                .concatenateField(getOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 4)
                .concatenateField(getSide(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 21, 1)
                .concatenateField(getOrderId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 22, 8)
                .concatenateField(getCancelReason(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 30, 1)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
