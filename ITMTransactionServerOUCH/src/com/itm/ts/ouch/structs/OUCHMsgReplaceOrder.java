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

public class OUCHMsgReplaceOrder extends OUCHMsgBase {

    private long existingOrderToken;
    private long replacementOrderToken;
    private long quantity;
    private long price;
    private byte openClose;
    private String clientAccount;
    private String customerInfo;
    private String exchangeInfo;
    private long displayQuantity;
    private byte timeInForce;
    private short timeInForceData;
    private int selfMatchPreventionKey;

    public long getExistingOrderToken() {
        return existingOrderToken;
    }

    public void setExistingOrderToken(long existingOrderToken) {
        this.existingOrderToken = existingOrderToken;
    }

    public long getReplacementOrderToken() {
        return replacementOrderToken;
    }

    public void setReplacementOrderToken(long replacementOrderToken) {
        this.replacementOrderToken = replacementOrderToken;
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

    public byte getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(byte timeInForce) {
        this.timeInForce = timeInForce;
    }

    public short getTimeInForceData() {
        return timeInForceData;
    }

    public void setTimeInForceData(short timeInForceData) {
        this.timeInForceData = timeInForceData;
    }

    public int getSelfMatchPreventionKey() {
        return selfMatchPreventionKey;
    }

    public void setSelfMatchPreventionKey(int selfMatchPreventionKey) {
        this.selfMatchPreventionKey = selfMatchPreventionKey;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 113 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 8 + 8 + 8 + 8 + 1 + 16 + 15 + 32 + 8 + 1 + 2 + 4 = 104
                    setExistingOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8));
                    setReplacementOrderToken(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8));
                    setQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8));
                    setPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 8));
                    setOpenClose(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 33, 1));
                    setClientAccount(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 34, 16));
                    setCustomerInfo(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 50, 15));
                    setExchangeInfo(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 65, 32));
                    setDisplayQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 97, 8));
                    setTimeInForce(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 105, 1));
                    setTimeInForceData(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 106, 2));
                    setSelfMatchPreventionKey(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 108, 4));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OUCHMsgReplaceOrder");
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
                .concatenateField(getExistingOrderToken(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 8)
                .concatenateField(getReplacementOrderToken(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 8)
                .concatenateField(getQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 17, 8)
                .concatenateField(getPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 25, 8)
                .concatenateField(getOpenClose(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 33, 1)
                .concatenateField(getClientAccount(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 34, 16)
                .concatenateField(getCustomerInfo(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 50, 15)
                .concatenateField(getExchangeInfo(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 65, 32)
                .concatenateField(getDisplayQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 97, 8)
                .concatenateField(getTimeInForce(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 105, 1)
                .concatenateField(getTimeInForceData(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 106, 2)
                .concatenateField(getSelfMatchPreventionKey(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 108, 4)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
