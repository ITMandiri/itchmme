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

public class OUCHMsgOrderReplaced extends OUCHMsgBase {

    private long timestamp;
    private long replacementOrderToken;
    private long previousOrderToken;
    private int orderBookId;
    private String side;
    private long orderId;
    private long quantity;
    private long price;
    private byte timeInForce;
    private byte openClose;
    private String clientAccount;
    private byte orderState;
    private String customerInfo;
    private String exchangeInfo;
    private long displayQuantity;
    private byte orderType;
    private short timeInForceData;
    private byte orderCapacity;
    private int selfMatchPreventionKey;
    private short attributes;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getReplacementOrderToken() {
        return replacementOrderToken;
    }

    public void setReplacementOrderToken(long replacementOrderToken) {
        this.replacementOrderToken = replacementOrderToken;
    }

    public long getPreviousOrderToken() {
        return previousOrderToken;
    }

    public void setPreviousOrderToken(long previousOrderToken) {
        this.previousOrderToken = previousOrderToken;
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

    public byte getOrderState() {
        return orderState;
    }

    public void setOrderState(byte orderState) {
        this.orderState = orderState;
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
                if (btMessageBytes.length >= 138 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 8 + 8 + 8 + 4 + 1 + 8 + 8 + 8 + 1 + 1 + 16 + 1 + 15 + 32 + 8 + 1 + 2 + 1 + 4 + 2 = 138
                    setTimestamp(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8));
                    setReplacementOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setPreviousOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 4));
                    setSide(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 29, 1));
                    setOrderId(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 30, 8));
                    setQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 38, 8));
                    setPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 46, 8));
                    setTimeInForce(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 54, 1));
                    setOpenClose(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 55, 1));
                    setClientAccount(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 56, 16));
                    setOrderState(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 72, 1));
                    setCustomerInfo(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 73, 15));
                    setExchangeInfo(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 88, 32));
                    setDisplayQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 120, 8));
                    setOrderType(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 128, 1));
                    setTimeInForceData(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 129, 2));
                    setOrderCapacity(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 131, 1));
                    setSelfMatchPreventionKey(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 132, 4));
                    setAttributes(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 136, 2));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgOrderReplaced");
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
                .concatenateField(getReplacementOrderToken(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8)
                .concatenateField(getPreviousOrderToken(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8)
                .concatenateField(getOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 4)
                .concatenateField(getSide(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 29, 1)
                .concatenateField(getOrderId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 30, 8)
                .concatenateField(getQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 38, 8)
                .concatenateField(getPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 46, 8)
                .concatenateField(getTimeInForce(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 54, 1)
                .concatenateField(getOpenClose(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 55, 1)
                .concatenateField(getClientAccount(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 56, 16)
                .concatenateField(getOrderState(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 72, 1)
                .concatenateField(getCustomerInfo(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 73, 15)
                .concatenateField(getExchangeInfo(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 88, 32)
                .concatenateField(getDisplayQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 120, 8)
                .concatenateField(getOrderType(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 128, 1)
                .concatenateField(getTimeInForceData(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 129, 2)
                .concatenateField(getOrderCapacity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 131, 1)
                .concatenateField(getSelfMatchPreventionKey(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 132, 4)
                .concatenateField(getAttributes(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 136, 2)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
