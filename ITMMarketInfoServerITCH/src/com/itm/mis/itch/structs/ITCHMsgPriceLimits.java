/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.structs;

/**
 *
 * @author fredy
 */
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset;

public class ITCHMsgPriceLimits extends ITCHMsgBase {

    private int nanos;
    private int orderBookId;
    private long upperLimit;
    private long lowerLimit;
    private byte typePriceLimit;
    private byte category;
    private long referencePrice;

    public int getNanos() {
        return nanos;
    }

    public void setNanos(int nanos) {
        this.nanos = nanos;
    }

    public int getOrderBookId() {
        return orderBookId;
    }

    public void setOrderBookId(int orderBookId) {
        this.orderBookId = orderBookId;
    }

    public long getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(long upperLimit) {
        this.upperLimit = upperLimit;
    }

    public long getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(long lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public byte getTypePriceLimit() {
        return typePriceLimit;
    }

    public void setTypePriceLimit(byte typePriceLimit) {
        this.typePriceLimit = typePriceLimit;
    }

    public byte getCategory() {
        return category;
    }

    public void setCategory(byte category) {
        this.category = category;
    }

    public long getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(long referencePrice) {
        this.referencePrice = referencePrice;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 35 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 4 + 4 + 8 + 8 + 1 + 1 + 8 = 35
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setUpperLimit(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setLowerLimit(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8));
                    setTypePriceLimit(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 1));
                    setCategory(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 26, 1));
                    setReferencePrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 27, 8));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgPriceLimits");
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
                .concatenateField(getNanos(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4)
                .concatenateField(getOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4)
                .concatenateField(getUpperLimit(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8)
                .concatenateField(getLowerLimit(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8)
                .concatenateField(getTypePriceLimit(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 1)
                .concatenateField(getCategory(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 26, 1)
                .concatenateField(getReferencePrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 27, 8)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
