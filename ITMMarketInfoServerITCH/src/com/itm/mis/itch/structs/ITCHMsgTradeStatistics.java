/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.structs;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset;

public class ITCHMsgTradeStatistics extends ITCHMsgBase {

    private int nanos;
    private int orderBookId;
    private long openPrice;
    private long highPrice;
    private long lowPrice;
    private long lastPrice;
    private long lastAuctionPrice;
    private long lastQuantity;
    private long turnOverQuantity;
    private long reportedTurnOverQuantity;
    private long turnOverValue;
    private long vwap;
    private long dailyNumberOfTrades;

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

    public long getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(long openPrice) {
        this.openPrice = openPrice;
    }

    public long getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(long highPrice) {
        this.highPrice = highPrice;
    }

    public long getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(long lowPrice) {
        this.lowPrice = lowPrice;
    }

    public long getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(long lastPrice) {
        this.lastPrice = lastPrice;
    }

    public long getLastAuctionPrice() {
        return lastAuctionPrice;
    }

    public void setLastAuctionPrice(long lastAuctionPrice) {
        this.lastAuctionPrice = lastAuctionPrice;
    }

    public long getLastQuantity() {
        return lastQuantity;
    }

    public void setLastQuantity(long lastQuantity) {
        this.lastQuantity = lastQuantity;
    }

    public long getTurnOverQuantity() {
        return turnOverQuantity;
    }

    public void setTurnOverQuantity(long turnOverQuantity) {
        this.turnOverQuantity = turnOverQuantity;
    }

    public long getReportedTurnOverQuantity() {
        return reportedTurnOverQuantity;
    }

    public void setReportedTurnOverQuantity(long reportedTurnOverQuantity) {
        this.reportedTurnOverQuantity = reportedTurnOverQuantity;
    }

    public long getTurnOverValue() {
        return turnOverValue;
    }

    public void setTurnOverValue(long turnOverValue) {
        this.turnOverValue = turnOverValue;
    }

    public long getVwap() {
        return vwap;
    }

    public void setVwap(long vwap) {
        this.vwap = vwap;
    }

    public long getDailyNumberOfTrades() {
        return dailyNumberOfTrades;
    }

    public void setDailyNumberOfTrades(long dailyNumberOfTrades) {
        this.dailyNumberOfTrades = dailyNumberOfTrades;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 97 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 4 + 4 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 = 97
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setOpenPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setHighPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8));
                    setLowPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 8));
                    setLastPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 33, 8));
                    setLastAuctionPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 41, 8));
                    setLastQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 49, 8));
                    setTurnOverQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 57, 8));
                    setReportedTurnOverQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 65, 8));
                    setTurnOverValue(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 73, 8));
                    setVwap(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 81, 8));
                    setDailyNumberOfTrades(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 89, 8));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgTradeStatistics");
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
                .concatenateField(getOpenPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8)
                .concatenateField(getHighPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8)
                .concatenateField(getLowPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 8)
                .concatenateField(getLastPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 33, 8)
                .concatenateField(getLastAuctionPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 41, 8)
                .concatenateField(getLastQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 49, 8)
                .concatenateField(getTurnOverQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 57, 8)
                .concatenateField(getReportedTurnOverQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 65, 8)
                .concatenateField(getTurnOverValue(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 73, 8)
                .concatenateField(getVwap(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 81, 8)
                .concatenateField(getDailyNumberOfTrades(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 89, 8)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
