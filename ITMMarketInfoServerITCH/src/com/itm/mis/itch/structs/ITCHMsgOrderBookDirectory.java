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

public class ITCHMsgOrderBookDirectory extends ITCHMsgBase {

    private int nanos = 0;
    private int orderBookId = 0;
    private String symbol = "";
    private String longName = "";
    private String isin = "";
    private byte financialProduct = 0;
    private String tradingCurrency = "";
    private short decimalsInPrice = 0;
    private short decimalsInNominalValue = 0;
    private int roundLotSize = 0;
    private long nominalValue = 0;
    private byte numberOfLegs = 0;
    private int underlyingOrderBookId = 0;
    private long strikePrice = 0; // menggunakan long untuk Price
    private int expirationDate = 0; // menggunakan int untuk Date
    private short decimalsInStrikePrice = 0;
    private byte optionType = 0;
    private short decimalsInQuantity = 0;
    private byte testOrderbook = 0;
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

    public byte getTestOrderbook() {
        return testOrderbook;
    }

    public void setTestOrderbook(byte testOrderbook) {
        this.testOrderbook = testOrderbook;
    }

    public byte getQuantityExpressedIn() {
        return quantityExpressedIn;
    }

    public void setQuantityExpressedIn(byte quantityExpressedIn) {
        this.quantityExpressedIn = quantityExpressedIn;
    }
    
    @Override
    public byte[] buildMessage() {
        byte[] mOut = resetCumulativeBytes()
                .concatenateField((byte) 'R', SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 0, 1) // msgType
                .concatenateField(getNanos(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4)
                .concatenateField(getOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4)
                .concatenateField(getSymbol(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 32)
                .concatenateField(getLongName(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 41, 64)
                .concatenateField(getIsin(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 105, 12)
                .concatenateField(getFinancialProduct(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 117, 1)
                .concatenateField(getTradingCurrency(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 118, 3)
                .concatenateField(getDecimalsInPrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 121, 2)
                .concatenateField(getDecimalsInNominalValue(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 123, 2)
                .concatenateField(getRoundLotSize(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 125, 4)
                .concatenateField(getNominalValue(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 129, 8)
                .concatenateField(getNumberOfLegs(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 137, 1)
                .concatenateField(getUnderlyingOrderBookId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 138, 4)
                .concatenateField(getStrikePrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 142, 8)
                .concatenateField(getExpirationDate(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 150, 4)
                .concatenateField(getDecimalsInStrikePrice(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 154, 2)
                .concatenateField(getOptionType(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 156, 1)
                .concatenateField(getDecimalsInQuantity(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 157, 2)
                .concatenateField(getTestOrderbook(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 159, 1)
                .concatenateField(getQuantityExpressedIn(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 160, 1)
                .putPacketLength() // .last set before get cumulative bytes;
                .getCumulativeBytes();
        return mOut;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 161 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) {
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setSymbol(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 32));
                    setLongName(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 41, 64));
                    setIsin(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 105, 12));
                    setFinancialProduct(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 117, 1));
                    setTradingCurrency(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 118, 3));
                    setDecimalsInPrice(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 121, 2));
                    setDecimalsInNominalValue(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 123, 2));
                    setRoundLotSize(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 125, 4));
                    setNominalValue(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 129, 8));
                    setNumberOfLegs(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 137, 1));
                    setUnderlyingOrderBookId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 138, 4));
                    setStrikePrice(decodeLong(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 142, 8));
                    setExpirationDate(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 150, 4));
                    setDecimalsInStrikePrice(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 154, 2));
                    setOptionType(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 156, 1));
                    setDecimalsInQuantity(decodeShort(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 157, 2));
                    setTestOrderbook(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 159, 1));
                    setQuantityExpressedIn(decodeByte(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 160, 1));

                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for OrderBookDirectoryMessage");
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }

}
