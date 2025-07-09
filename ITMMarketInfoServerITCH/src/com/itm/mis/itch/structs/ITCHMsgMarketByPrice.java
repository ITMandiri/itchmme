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
import java.nio.ByteBuffer;

public class ITCHMsgMarketByPrice extends ITCHMsgBase {

    private int nanos;
    private int orderBookId;
    private byte maximumLevel;
    private PriceLevelItem[] priceLevelItems; // Array untuk PriceLevelItem

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

    public byte getMaximumLevel() {
        return maximumLevel;
    }

    public void setMaximumLevel(byte maximumLevel) {
        this.maximumLevel = maximumLevel;
    }

    public PriceLevelItem[] getPriceLevelItems() {
        return priceLevelItems;
    }

    public void setPriceLevelItems(PriceLevelItem[] priceLevelItems) {
        this.priceLevelItems = priceLevelItems;
    }

    // Inner class untuk PriceLevelItem
    public static class PriceLevelItem {
        private long price;
        private long quantity;
        private int numberOfOrders;
        private byte side;

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

        public int getNumberOfOrders() {
            return numberOfOrders;
        }

        public void setNumberOfOrders(int numberOfOrders) {
            this.numberOfOrders = numberOfOrders;
        }

        public byte getSide() {
            return side;
        }

        public void setSide(byte side) {
            this.side = side;
        }
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 10 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) {
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setMaximumLevel(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 1));

                    int numberOfLevels = getMaximumLevel();
                    PriceLevelItem[] priceLevelItems = new PriceLevelItem[numberOfLevels];
                    int offset = SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 10;

                    for (int i = 0; i < numberOfLevels; i++) {
                        PriceLevelItem item = new PriceLevelItem();
                        item.setPrice(decodeLong(btMessageBytes, offset, 8));
                        item.setQuantity(decodeLong(btMessageBytes, offset + 8, 8));
                        item.setNumberOfOrders(decodeInteger(btMessageBytes, offset + 16, 4));
                        item.setSide(decodeByte(btMessageBytes, offset + 20, 1)); // Corrected offset for side
                        priceLevelItems[i] = item;
                        offset += 21; // Increment offset for the next PriceLevelItem
                    }
                    setPriceLevelItems(priceLevelItems);
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgMarketByPrice");
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }

    @Override
    public byte[] buildMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(calculateMessageLength()); // Use calculated length
        buffer.putInt(getNanos());
        buffer.putInt(getOrderBookId());
        buffer.put(getMaximumLevel());

        if (getPriceLevelItems() != null) {
            for (PriceLevelItem item : getPriceLevelItems()) {
                buffer.putLong(item.getPrice());
                buffer.putLong(item.getQuantity());
                buffer.putInt(item.getNumberOfOrders());
                buffer.put(item.getSide());
            }
        }
        byte[] message = buffer.array();
        return concatenateLength(message);
    }

    private int calculateMessageLength() {
        int length = 10; // Base length: msgType(1) + nanos(4) + orderBookId(4) + maximumLevel(1)
        if (getPriceLevelItems() != null) {
            length += getPriceLevelItems().length * 21; // Each PriceLevelItem is 8 + 8 + 4 + 1 = 21 bytes
        }
        return length;
    }
    
    protected byte[] concatenateLength(byte[] message) {
        int length = message.length;
        byte[] lengthBytes = ByteBuffer.allocate(4).putInt(length).array();
        byte[] result = new byte[4 + length];
        System.arraycopy(lengthBytes, 0, result, 0, 4);
        System.arraycopy(message, 0, result, 4, length);
        return result;
    }
}
