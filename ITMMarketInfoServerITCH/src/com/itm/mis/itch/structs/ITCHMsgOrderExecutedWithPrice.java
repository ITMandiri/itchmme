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

public class ITCHMsgOrderExecutedWithPrice extends ITCHMsgBase {

    private int nanos;
    private long orderId;
    private int orderBookId;
    private byte side;
    private long quantity;
    private long matchId;
    private int comboGroupId;
    private String owner;
    private String counterparty;
    private long price;
    private byte cross;
    private byte printable;

    public int getNanos() {
        return nanos;
    }

    public void setNanos(int nanos) {
        this.nanos = nanos;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getOrderBookId() {
        return orderBookId;
    }

    public void setOrderBookId(int orderBookId) {
        this.orderBookId = orderBookId;
    }

    public byte getSide() {
        return side;
    }

    public void setSide(byte side) {
        this.side = side;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public byte getCross() {
        return cross;
    }

    public void setCross(byte cross) {
        this.cross = cross;
    }

    public byte getPrintable() {
        return printable;
    }

    public void setPrintable(byte printable) {
        this.printable = printable;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 62 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 4 + 8 + 4 + 1 + 8 + 8 + 4 + 7 + 7 + 8 + 1 + 1 = 62
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 8));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 13, 4));
                    setSide(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 1));
                    setQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 18, 8));
                    setMatchId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 26, 8));
                    setComboGroupId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 34, 4));
                    setOwner(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 38, 7));
                    setCounterparty(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 45, 7));
                    setPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 52, 8));
                    setCross(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 60, 1));
                    setPrintable(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 61, 1));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgOrderExecutedWithPrice");
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
                .concatenateField(getOrderId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 8)
                .concatenateField(getOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 13, 4)
                .concatenateField(getSide(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 1)
                .concatenateField(getQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 18, 8)
                .concatenateField(getMatchId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 26, 8)
                .concatenateField(getComboGroupId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 34, 4)
                .concatenateField(getOwner(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 38, 7)
                .concatenateField(getCounterparty(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 45, 7)
                .concatenateField(getPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 52, 8)
                .concatenateField(getCross(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 60, 1)
                .concatenateField(getPrintable(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 61, 1)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
