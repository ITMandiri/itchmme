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

public class ITCHMsgEquilibriumPrice extends ITCHMsgBase {

    private int nanos;
    private int orderBookId;
    private long bidQuantity;
    private long askQuantity;
    private long price;
    private long bestBidPrice;
    private long bestAskPrice;
    private long bestBidQuantity;
    private long bestAskQuantity;

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

    public long getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(long bidQuantity) {
        this.bidQuantity = bidQuantity;
    }

    public long getAskQuantity() {
        return askQuantity;
    }

    public void setAskQuantity(long askQuantity) {
        this.askQuantity = askQuantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getBestBidPrice() {
        return bestBidPrice;
    }

    public void setBestBidPrice(long bestBidPrice) {
        this.bestBidPrice = bestBidPrice;
    }

    public long getBestAskPrice() {
        return bestAskPrice;
    }

    public void setBestAskPrice(long bestAskPrice) {
        this.bestAskPrice = bestAskPrice;
    }

    public long getBestBidQuantity() {
        return bestBidQuantity;
    }

    public void setBestBidQuantity(long bestBidQuantity) {
        this.bestBidQuantity = bestBidQuantity;
    }

    public long getBestAskQuantity() {
        return bestAskQuantity;
    }

    public void setBestAskQuantity(long bestAskQuantity) {
        this.bestAskQuantity = bestAskQuantity;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 65 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 4 + 4 + 8 + 8 + 8 + 8 + 8 + 8 + 8 = 65
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setBidQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setAskQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8));
                    setPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 8));
                    setBestBidPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 33, 8));
                    setBestAskPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 41, 8));
                    setBestBidQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 49, 8));
                    setBestAskQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 57, 8));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgEquilibriumPrice");
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
                .concatenateField(getBidQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8)
                .concatenateField(getAskQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8)
                .concatenateField(getPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 8)
                .concatenateField(getBestBidPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 33, 8)
                .concatenateField(getBestAskPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 41, 8)
                .concatenateField(getBestBidQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 49, 8)
                .concatenateField(getBestAskQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 57, 8)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
