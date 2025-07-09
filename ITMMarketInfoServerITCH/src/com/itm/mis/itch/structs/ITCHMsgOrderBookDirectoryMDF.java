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

public class ITCHMsgOrderBookDirectoryMDF extends ITCHMsgBase {

    private int nanos = 0;
    private int orderBookId = 0;
    private byte partition = 0;
    private String symbol = "";
    private String longName = "";
    private String isin = "";
    private String assetName = "";
    private byte financialProduct = 0;
    private String tradingCurrency = "";
    private short decimalsInPrice = 0;
    private short decimalsInNominalValue = 0;
    private int roundLotSize = 0;
    private long nominalValue = 0;
    private byte numberOfLegs = 0;
    private int underlyingOrderBookId = 0;
    private long strikePrice = 0;
    private int expirationDate = 0;
    private short decimalsInStrikePrice = 0;
    private byte optionType = 0;
    private short decimalsInQuantity = 0;
    private int marketId = 0;
    private int exchangeId = 0;
    private String sectorCode = "";
    private long tradableQuantity = 0;
    private long outstandingQuantity = 0;
    private int lastTradedDate = 0;
    private long contractMultiplier = 0;
    private long multiplier = 0;
    private short decimalsInMultiplier = 0;
    private long minOrderQuantity = 0;
    private long maxOrderQuantity = 0;
    private int numberOfSettlementDays = 0;
    private byte primary = 0;
    private byte testOrderbook = 0;
    private String listingBoard = "";
    private long minOrderValue = 0;
    private long maxOrderValue = 0;
    private short decimalsInOrderValue = 0;
    private String assetExtendedName = "";
    private int marketSegmentId = 0;
    private int issuerId = 0;
    private long ipoPrice = 0;
    private int delistingDate = 0;
    private String remarks = "";
    private byte quantityExpressedIn = 0;

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

    public byte getPartition() {
        return partition;
    }

    public void setPartition(byte partition) {
        this.partition = partition;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public byte getFinancialProduct() {
        return financialProduct;
    }

    public void setFinancialProduct(byte financialProduct) {
        this.financialProduct = financialProduct;
    }

    public String getTradingCurrency() {
        return tradingCurrency;
    }

    public void setTradingCurrency(String tradingCurrency) {
        this.tradingCurrency = tradingCurrency;
    }

    public short getDecimalsInPrice() {
        return decimalsInPrice;
    }

    public void setDecimalsInPrice(short decimalsInPrice) {
        this.decimalsInPrice = decimalsInPrice;
    }

    public short getDecimalsInNominalValue() {
        return decimalsInNominalValue;
    }

    public void setDecimalsInNominalValue(short decimalsInNominalValue) {
        this.decimalsInNominalValue = decimalsInNominalValue;
    }

    public int getRoundLotSize() {
        return roundLotSize;
    }

    public void setRoundLotSize(int roundLotSize) {
        this.roundLotSize = roundLotSize;
    }

    public long getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(long nominalValue) {
        this.nominalValue = nominalValue;
    }

    public byte getNumberOfLegs() {
        return numberOfLegs;
    }

    public void setNumberOfLegs(byte numberOfLegs) {
        this.numberOfLegs = numberOfLegs;
    }

    public int getUnderlyingOrderBookId() {
        return underlyingOrderBookId;
    }

    public void setUnderlyingOrderBookId(int underlyingOrderBookId) {
        this.underlyingOrderBookId = underlyingOrderBookId;
    }

    public long getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(long strikePrice) {
        this.strikePrice = strikePrice;
    }

    public int getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(int expirationDate) {
        this.expirationDate = expirationDate;
    }

    public short getDecimalsInStrikePrice() {
        return decimalsInStrikePrice;
    }

    public void setDecimalsInStrikePrice(short decimalsInStrikePrice) {
        this.decimalsInStrikePrice = decimalsInStrikePrice;
    }

    public byte getOptionType() {
        return optionType;
    }

    public void setOptionType(byte optionType) {
        this.optionType = optionType;
    }

    public short getDecimalsInQuantity() {
        return decimalsInQuantity;
    }

    public void setDecimalsInQuantity(short decimalsInQuantity) {
        this.decimalsInQuantity = decimalsInQuantity;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public long getTradableQuantity() {
        return tradableQuantity;
    }

    public void setTradableQuantity(long tradableQuantity) {
        this.tradableQuantity = tradableQuantity;
    }

    public long getOutstandingQuantity() {
        return outstandingQuantity;
    }

    public void setOutstandingQuantity(long outstandingQuantity) {
        this.outstandingQuantity = outstandingQuantity;
    }

    public int getLastTradedDate() {
        return lastTradedDate;
    }

    public void setLastTradedDate(int lastTradedDate) {
        this.lastTradedDate = lastTradedDate;
    }

    public long getContractMultiplier() {
        return contractMultiplier;
    }

    public void setContractMultiplier(long contractMultiplier) {
        this.contractMultiplier = contractMultiplier;
    }

    public long getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(long multiplier) {
        this.multiplier = multiplier;
    }

    public short getDecimalsInMultiplier() {
        return decimalsInMultiplier;
    }

    public void setDecimalsInMultiplier(short decimalsInMultiplier) {
        this.decimalsInMultiplier = decimalsInMultiplier;
    }

    public long getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(long minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public long getMaxOrderQuantity() {
        return maxOrderQuantity;
    }

    public void setMaxOrderQuantity(long maxOrderQuantity) {
        this.maxOrderQuantity = maxOrderQuantity;
    }

    public int getNumberOfSettlementDays() {
        return numberOfSettlementDays;
    }

    public void setNumberOfSettlementDays(int numberOfSettlementDays) {
        this.numberOfSettlementDays = numberOfSettlementDays;
    }

    public byte getPrimary() {
        return primary;
    }

    public void setPrimary(byte primary) {
        this.primary = primary;
    }

    public byte getTestOrderbook() {
        return testOrderbook;
    }

    public void setTestOrderbook(byte testOrderbook) {
        this.testOrderbook = testOrderbook;
    }

    public String getListingBoard() {
        return listingBoard;
    }

    public void setListingBoard(String listingBoard) {
        this.listingBoard = listingBoard;
    }

    public long getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(long minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public long getMaxOrderValue() {
        return maxOrderValue;
    }

    public void setMaxOrderValue(long maxOrderValue) {
        this.maxOrderValue = maxOrderValue;
    }

    public short getDecimalsInOrderValue() {
        return decimalsInOrderValue;
    }

    public void setDecimalsInOrderValue(short decimalsInOrderValue) {
        this.decimalsInOrderValue = decimalsInOrderValue;
    }

    public String getAssetExtendedName() {
        return assetExtendedName;
    }

    public void setAssetExtendedName(String assetExtendedName) {
        this.assetExtendedName = assetExtendedName;
    }

    public int getMarketSegmentId() {
        return marketSegmentId;
    }

    public void setMarketSegmentId(int marketSegmentId) {
        this.marketSegmentId = marketSegmentId;
    }

    public int getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(int issuerId) {
        this.issuerId = issuerId;
    }

    public long getIpoPrice() {
        return ipoPrice;
    }

    public void setIpoPrice(long ipoPrice) {
        this.ipoPrice = ipoPrice;
    }

    public int getDelistingDate() {
        return delistingDate;
    }

    public void setDelistingDate(int delistingDate) {
        this.delistingDate = delistingDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public byte getQuantityExpressedIn() {
        return quantityExpressedIn;
    }

    public void setQuantityExpressedIn(byte quantityExpressedIn) {
        this.quantityExpressedIn = quantityExpressedIn;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 475 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) {
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setPartition(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 1));
                    setSymbol(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 10, 32));
                    setLongName(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 42, 64));
                    setIsin(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 106, 12));
                    setAssetName(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 118, 32));
                    setFinancialProduct(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 150, 1));
                    setTradingCurrency(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 151, 3));
                    setDecimalsInPrice(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 154, 2));
                    setDecimalsInNominalValue(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 156, 2));
                    setRoundLotSize(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 158, 4));
                    setNominalValue(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 162, 8));
                    setNumberOfLegs(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 170, 1));
                    setUnderlyingOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 171, 4));
                    setStrikePrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 175, 8));
                    setExpirationDate(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 183, 4));
                    setDecimalsInStrikePrice(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 187, 2));
                    setOptionType(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 189, 1));
                    setMarketId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 190, 4));
                    setExchangeId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 194, 4));
                    setDecimalsInQuantity(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 198, 2));
                    setSectorCode(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 200, 4));
                    setTradableQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 204, 8));
                    setOutstandingQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 212, 8));
                    setLastTradedDate(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 220, 4));
                    setContractMultiplier(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 224, 8));
                    setMultiplier(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 232, 8));
                    setDecimalsInMultiplier(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 240, 2));
                    setMinOrderQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 242, 8));
                    setMaxOrderQuantity(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 250, 8));
                    setNumberOfSettlementDays(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 258, 4));
                    setPrimary(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 262, 1));
                    setTestOrderbook(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 263, 1));
                    setListingBoard(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 264, 32));
                    setMinOrderValue(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 296, 8));
                    setMaxOrderValue(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 304, 8));
                    setDecimalsInOrderValue(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 312, 2));
                    setAssetExtendedName(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 314, 100));
                    setMarketSegmentId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 414, 4));
                    setIssuerId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 418, 4));
                    setIpoPrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 422, 8));
                    setDelistingDate(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 430, 4));
                    setRemarks(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 434, 40));
                    setQuantityExpressedIn(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 474, 1));


                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgOrderBookDirectoryMDF");
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
                .concatenateField((byte) 'R', SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 0, 1)
                .concatenateField(getNanos(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4)
                .concatenateField(getOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4)
                .concatenateField(getPartition(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 1)
                .concatenateField(getSymbol(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 10, 32)
                .concatenateField(getLongName(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 42, 64)
                .concatenateField(getIsin(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 106, 12)
                .concatenateField(getAssetName(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 118, 32)
                .concatenateField(getFinancialProduct(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 150, 1)
                .concatenateField(getTradingCurrency(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 151, 3)
                .concatenateField(getDecimalsInPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 154, 2)
                .concatenateField(getDecimalsInNominalValue(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 156, 2)
                .concatenateField(getRoundLotSize(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 158, 4)
                .concatenateField(getNominalValue(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 162, 8)
                .concatenateField(getNumberOfLegs(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 170, 1)
                .concatenateField(getUnderlyingOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 171, 4)
                .concatenateField(getStrikePrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 175, 8)
                .concatenateField(getExpirationDate(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 183, 4)
                .concatenateField(getDecimalsInStrikePrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 187, 2)
                .concatenateField(getOptionType(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 189, 1)
                .concatenateField(getDecimalsInQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 190, 2)
                .concatenateField(getMarketId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 194, 4)
                .concatenateField(getExchangeId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 198, 4)
                .concatenateField(getSectorCode(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 202, 4)
                .concatenateField(getTradableQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 206, 8)
                .concatenateField(getOutstandingQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 214, 8)
                .concatenateField(getLastTradedDate(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 222, 4)
                .concatenateField(getContractMultiplier(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 226, 8)
                .concatenateField(getMultiplier(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 234, 8)
                .concatenateField(getDecimalsInMultiplier(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 242, 2)
                .concatenateField(getMinOrderQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 244, 8)
                .concatenateField(getMaxOrderQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 252, 8)
                .concatenateField(getNumberOfSettlementDays(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 260, 4)
                .concatenateField(getPrimary(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 264, 1)
                .concatenateField(getTestOrderbook(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 265, 1)
                .concatenateField(getListingBoard(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 266, 32)
                .concatenateField(getMinOrderValue(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 298, 8)
                .concatenateField(getMaxOrderValue(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 306, 8)
                .concatenateField(getDecimalsInOrderValue(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 314, 2)
                .concatenateField(getAssetExtendedName(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 316, 100)
                .concatenateField(getMarketSegmentId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 416, 4)
                .concatenateField(getIssuerId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 420, 4)
                .concatenateField(getIpoPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 424, 8)
                .concatenateField(getDelistingDate(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 432, 4)
                .concatenateField(getRemarks(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 436, 40)
                .concatenateField(getQuantityExpressedIn(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 476, 1)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    } 
}
