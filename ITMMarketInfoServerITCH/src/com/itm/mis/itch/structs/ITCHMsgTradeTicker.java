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

public class ITCHMsgTradeTicker extends ITCHMsgBase {

    private int nanos;
    private int orderBookId;
    private long dealId;
    private byte dealSource;
    private long price;
    private long quantity;
    private long dealTime;
    private byte action;
    private String aggressor;
    private short tradeReportType;
    private byte crossedTrade;

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

    public long getDealId() {
        return dealId;
    }

    public void setDealId(long dealId) {
        this.dealId = dealId;
    }

    public byte getDealSource() {
        return dealSource;
    }

    public void setDealSource(byte dealSource) {
        this.dealSource = dealSource;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public String getAggressor() {
        return aggressor;
    }

    public void setAggressor(String aggressor) {
        this.aggressor = aggressor;
    }

    public short getTradeReportType() {
        return tradeReportType;
    }

    public void setTradeReportType(short tradeReportType) {
        this.tradeReportType = tradeReportType;
    }

    public byte getCrossedTrade() {
        return crossedTrade;
    }

    public void setCrossedTrade(byte crossedTrade) {
        this.crossedTrade = crossedTrade;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 47 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 4 + 4 + 8 + 1 + 8 + 8 + 8 + 1 + 1 + 2 + 1 = 47
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setDealId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setDealSource(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 1));
                    setPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 18, 8));
                    setQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 26, 8));
                    setDealTime(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 34, 8));
                    setAction(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 42, 1));
                    setAggressor(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 43, 1));
                    setTradeReportType(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 44, 2));
                    setCrossedTrade(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 46, 1));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgTradeTicker");
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
                .concatenateField(getDealId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8)
                .concatenateField(getDealSource(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 1)
                .concatenateField(getPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 18, 8)
                .concatenateField(getQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 26, 8)
                .concatenateField(getDealTime(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 34, 8)
                .concatenateField(getAction(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 42, 1)
                .concatenateField(getAggressor(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 43, 1)
                .concatenateField(getTradeReportType(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 44, 2)
                .concatenateField(getCrossedTrade(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 46, 1)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
