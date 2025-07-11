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

public class OUCHMsgOrderExecuted extends OUCHMsgBase {

    private long timestamp;
    private long orderToken;
    private int orderBookId;
    private long tradeQuantity;
    private long tradePrice;
    private long matchId;
    private int comboGroupId;
    private byte dealSource;

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

    public long getTradeQuantity() {
        return tradeQuantity;
    }

    public void setTradeQuantity(long tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
    }

    public long getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(long tradePrice) {
        this.tradePrice = tradePrice;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public int getComboGroupId() {
        return comboGroupId;
    }

    public void setComboGroupId(int comboGroupId) {
        this.comboGroupId = comboGroupId;
    }

    public byte getDealSource() {
        return dealSource;
    }

    public void setDealSource(byte dealSource) {
        this.dealSource = dealSource;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 50 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 8 + 8 + 4 + 8 + 8 + 8 + 4 + 1 = 50
                    setTimestamp(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8));
                    setOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 4));
                    setTradeQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 21, 8));
                    setTradePrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 29, 8));
                    setMatchId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 37, 8));
                    setComboGroupId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 45, 4));
                    setDealSource(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 49, 1));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgOrderExecuted");
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
                .concatenateField(getTradeQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 21, 8)
                .concatenateField(getTradePrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 29, 8)
                .concatenateField(getMatchId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 37, 8)
                .concatenateField(getComboGroupId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 45, 4)
                .concatenateField(getDealSource(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 49, 1)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
