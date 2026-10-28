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
import java.nio.charset.StandardCharsets;

public class ITCHMsgMarketByPrice extends ITCHMsgBase {

    private int nanos;
    private int orderBookId;
    private byte maximumLevel;
    private byte numberOfItems; //.20260625
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

    public byte getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(byte numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public PriceLevelItem[] getPriceLevelItems() {
        return priceLevelItems;
    }

    public void setPriceLevelItems(PriceLevelItem[] priceLevelItems) {
        this.priceLevelItems = priceLevelItems;
    }

    // Inner class untuk PriceLevelItem
    public static class PriceLevelItem {
        private String levelUpdateAction;
        private String side;
        private byte level;
        private long price;
        private long quantity;
        private byte numberOfDeletes;

        public String getLevelUpdateAction() {
            return levelUpdateAction;
        }

        public void setLevelUpdateAction(String levelUpdateAction) {
            this.levelUpdateAction = levelUpdateAction;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public byte getLevel() {
            return level;
        }

        public void setLevel(byte level) {
            this.level = level;
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

        public byte getNumberOfDeletes() {
            return numberOfDeletes;
        }

        public void setNumberOfDeletes(byte numberOfDeletes) {
            this.numberOfDeletes = numberOfDeletes;
        }
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 11 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) {
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setMaximumLevel(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 1));
                    setNumberOfItems(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 10, 1));

                    int numberOfLevels = getNumberOfItems();
                    PriceLevelItem[] priceLevelItems = new PriceLevelItem[numberOfLevels];
                    int offset = SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 11;

                    for (int i = 0; i < numberOfLevels; i++) {
                        PriceLevelItem item = new PriceLevelItem();
                        item.setLevelUpdateAction(decodeString(btMessageBytes, offset, 1));
                        item.setSide(decodeString(btMessageBytes, offset + 1, 1));
                        item.setLevel(decodeByte(btMessageBytes, offset + 2, 1));
                        item.setPrice(decodeLong(btMessageBytes, offset + 3, 8)); // Corrected offset for side
                        item.setQuantity(decodeLong(btMessageBytes, offset + 11, 8));
                        item.setNumberOfDeletes(decodeByte(btMessageBytes, offset + 19, 1));
                        priceLevelItems[i] = item;
                        offset += 20; // Increment offset for the next PriceLevelItem
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
        buffer.put(getNumberOfItems());

        if (getPriceLevelItems() != null) {
            for (PriceLevelItem item : getPriceLevelItems()) {
                buffer.put(item.getLevelUpdateAction().getBytes(StandardCharsets.US_ASCII));
                buffer.put(item.getSide().getBytes(StandardCharsets.US_ASCII));
                buffer.put(item.getLevel());
                buffer.putLong(item.getPrice());
                buffer.putLong(item.getQuantity());
                buffer.put(item.getNumberOfDeletes());
            }
        }
        byte[] message = buffer.array();
        return concatenateLength(message);
    }

    private int calculateMessageLength() {
        int length = 11; // Base length: msgType(1) + nanos(4) + orderBookId(4) + maximumLevel(1) + numberOfItems(1)
        if (getPriceLevelItems() != null) {
            length += getPriceLevelItems().length * 20; // Each PriceLevelItem is 1 + 1 + 1 + 8 + 8 + 1 = 20 bytes
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
