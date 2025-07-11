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

public class OUCHMsgEnterOrder extends OUCHMsgBase {

    private long orderToken;
    private int orderBookId;
    private byte side;
    private long quantity;
    private long price;
    private byte timeInForce;
    private byte openClose;
    private String clientAccount;
    private String customerInfo;
    private String exchangeInfo;
    private long displayQuantity;
    private byte orderType;
    private short timeInForceData;
    private byte orderCapacity;
    private int selfMatchPreventionKey;
    private short attributes;

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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public byte getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(byte timeInForce) {
        this.timeInForce = timeInForce;
    }

    public byte getOpenClose() {
        return openClose;
    }

    public void setOpenClose(byte openClose) {
        this.openClose = openClose;
    }

    public String getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(String clientAccount) {
        this.clientAccount = clientAccount;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getExchangeInfo() {
        return exchangeInfo;
    }

    public void setExchangeInfo(String exchangeInfo) {
        this.exchangeInfo = exchangeInfo;
    }

    public long getDisplayQuantity() {
        return displayQuantity;
    }

    public void setDisplayQuantity(long displayQuantity) {
        this.displayQuantity = displayQuantity;
    }

    public byte getOrderType() {
        return orderType;
    }

    public void setOrderType(byte orderType) {
        this.orderType = orderType;
    }

    public short getTimeInForceData() {
        return timeInForceData;
    }

    public void setTimeInForceData(short timeInForceData) {
        this.timeInForceData = timeInForceData;
    }

    public byte getOrderCapacity() {
        return orderCapacity;
    }

    public void setOrderCapacity(byte orderCapacity) {
        this.orderCapacity = orderCapacity;
    }

    public int getSelfMatchPreventionKey() {
        return selfMatchPreventionKey;
    }

    public void setSelfMatchPreventionKey(int selfMatchPreventionKey) {
        this.selfMatchPreventionKey = selfMatchPreventionKey;
    }

    public short getAttributes() {
        return attributes;
    }

    public void setAttributes(short attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 113 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) {
                    setOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 4));
                    setSide(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 13, 1));
                    setQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 14, 8));
                    setPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 22, 8));
                    setTimeInForce(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 30, 1));
                    setOpenClose(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 31, 1));
                    setClientAccount(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 32, 16));
                    setCustomerInfo(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 48, 15));
                    setExchangeInfo(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 63, 32));
                    setDisplayQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 95, 8));
                    setOrderType(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 103, 1));
                    setTimeInForceData(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 104, 2));
                    setOrderCapacity(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 106, 1));
                    setSelfMatchPreventionKey(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 107, 4));
                    setAttributes(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 111, 2));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgEnterOrder");
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
                .concatenateField(getOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 4)
                .concatenateField(getSide(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 13, 1)
                .concatenateField(getQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 14, 8)
                .concatenateField(getPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 22, 8)
                .concatenateField(getTimeInForce(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 30, 1)
                .concatenateField(getOpenClose(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 31, 1)
                .concatenateField(getClientAccount(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 32, 16)
                .concatenateField(getCustomerInfo(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 48, 15)
                .concatenateField(getExchangeInfo(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 63, 32)
                .concatenateField(getDisplayQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 95, 8)
                .concatenateField(getOrderType(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 103, 1)
                .concatenateField(getTimeInForceData(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 104, 2)
                .concatenateField(getOrderCapacity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 106, 1)
                .concatenateField(getSelfMatchPreventionKey(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 107, 4)
                .concatenateField(getAttributes(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 111, 2)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
