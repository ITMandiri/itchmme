/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.struct;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.util.MsgBuilder;
import com.itm.idx.data.qri.util.StringUtil;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIDataSecuritiesMessage extends QRIDataHeader{
    private int             fRecordNumber;
    private String          fSecurityID;
    private String          fSymbol;
    private String          fRemarks;
    private String          fRemarks2;
    private String          fShortName;
    private String          fStatus;
    private String          fSecurityTradingStatus;
    private String          fMarketCode;
    private String          fInstrCode;
    private String          fSymbolSfx;
    private String          fSectorCode;
    private String          fOrderMethods;
    private int             fLotSize;
    private int             fMinimumStep;
    private double          fFaceValue;
    private String          fFaceUnit;
    private double          fEarnings;
    private String          fPreviousDate;
    private double          fPreviousPrice;
    private String          fPriceFormat;
    private int             fDecimals;
    private long            fQtyLimit;
    private long            fVolTraded;
    private double          fValTraded;
    private int             fYield;
    private double          fAccruedInterestRate;
    private String          fPrimaryDist;
    private int             fForeignLimit;
    private long            fForeignLevel;
    private long            fForeignAvail;
    private double          fIndex;
    private int             fOpenOrders;
    private long            fListedSize;
    private long            fTradeableSize;
    private double          fAvgLast;
    private int             fListingDate;
    private int             fDeListingDate;
    private int             fCouponFreq;
    private int             fLastRecord;
    
    public QRIDataSecuritiesMessage(){
    }
    
    public QRIDataSecuritiesMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public int getfRecordNumber() {
        return fRecordNumber;
    }

    public void setfRecordNumber(int fRecordNumber) {
        this.fRecordNumber = fRecordNumber;
    }

    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }

    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }

    public String getfRemarks() {
        return fRemarks;
    }

    public void setfRemarks(String fRemarks) {
        this.fRemarks = fRemarks;
    }

    public String getfRemarks2() {
        return fRemarks2;
    }

    public void setfRemarks2(String fRemarks2) {
        this.fRemarks2 = fRemarks2;
    }

    public String getfShortName() {
        return fShortName;
    }

    public void setfShortName(String fShortName) {
        this.fShortName = fShortName;
    }

    public String getfStatus() {
        return fStatus;
    }

    public void setfStatus(String fStatus) {
        this.fStatus = fStatus;
    }

    public String getfSecurityTradingStatus() {
        return fSecurityTradingStatus;
    }

    public void setfSecurityTradingStatus(String fSecurityTradingStatus) {
        this.fSecurityTradingStatus = fSecurityTradingStatus;
    }

    public String getfMarketCode() {
        return fMarketCode;
    }

    public void setfMarketCode(String fMarketCode) {
        this.fMarketCode = fMarketCode;
    }

    public String getfInstrCode() {
        return fInstrCode;
    }

    public void setfInstrCode(String fInstrCode) {
        this.fInstrCode = fInstrCode;
    }

    public String getfSymbolSfx() {
        return fSymbolSfx;
    }

    public void setfSymbolSfx(String fSymbolSfx) {
        this.fSymbolSfx = fSymbolSfx;
    }

    public String getfSectorCode() {
        return fSectorCode;
    }

    public void setfSectorCode(String fSectorCode) {
        this.fSectorCode = fSectorCode;
    }

    public String getfOrderMethods() {
        return fOrderMethods;
    }

    public void setfOrderMethods(String fOrderMethods) {
        this.fOrderMethods = fOrderMethods;
    }

    public int getfLotSize() {
        return fLotSize;
    }

    public void setfLotSize(int fLotSize) {
        this.fLotSize = fLotSize;
    }

    public int getfMinimumStep() {
        return fMinimumStep;
    }

    public void setfMinimumStep(int fMinimumStep) {
        this.fMinimumStep = fMinimumStep;
    }

    public double getfFaceValue() {
        return fFaceValue;
    }

    public void setfFaceValue(double fFaceValue) {
        this.fFaceValue = fFaceValue;
    }

    public String getfFaceUnit() {
        return fFaceUnit;
    }

    public void setfFaceUnit(String fFaceUnit) {
        this.fFaceUnit = fFaceUnit;
    }

    public double getfEarnings() {
        return fEarnings;
    }

    public void setfEarnings(double fEarnings) {
        this.fEarnings = fEarnings;
    }

    public String getfPreviousDate() {
        return fPreviousDate;
    }

    public void setfPreviousDate(String fPreviousDate) {
        this.fPreviousDate = fPreviousDate;
    }

    public double getfPreviousPrice() {
        return fPreviousPrice;
    }

    public void setfPreviousPrice(double fPreviousPrice) {
        this.fPreviousPrice = fPreviousPrice;
    }

    public String getfPriceFormat() {
        return fPriceFormat;
    }

    public void setfPriceFormat(String fPriceFormat) {
        this.fPriceFormat = fPriceFormat;
    }

    public int getfDecimals() {
        return fDecimals;
    }

    public void setfDecimals(int fDecimals) {
        this.fDecimals = fDecimals;
    }

    public long getfQtyLimit() {
        return fQtyLimit;
    }

    public void setfQtyLimit(long fQtyLimit) {
        this.fQtyLimit = fQtyLimit;
    }

    public long getfVolTraded() {
        return fVolTraded;
    }

    public void setfVolTraded(long fVolTraded) {
        this.fVolTraded = fVolTraded;
    }

    public double getfValTraded() {
        return fValTraded;
    }

    public void setfValTraded(double fValTraded) {
        this.fValTraded = fValTraded;
    }

    public int getfYield() {
        return fYield;
    }

    public void setfYield(int fYield) {
        this.fYield = fYield;
    }

    public double getfAccruedInterestRate() {
        return fAccruedInterestRate;
    }

    public void setfAccruedInterestRate(double fAccruedInterestRate) {
        this.fAccruedInterestRate = fAccruedInterestRate;
    }

    public String getfPrimaryDist() {
        return fPrimaryDist;
    }

    public void setfPrimaryDist(String fPrimaryDist) {
        this.fPrimaryDist = fPrimaryDist;
    }

    public int getfForeignLimit() {
        return fForeignLimit;
    }

    public void setfForeignLimit(int fForeignLimit) {
        this.fForeignLimit = fForeignLimit;
    }

    public long getfForeignLevel() {
        return fForeignLevel;
    }

    public void setfForeignLevel(long fForeignLevel) {
        this.fForeignLevel = fForeignLevel;
    }

    public long getfForeignAvail() {
        return fForeignAvail;
    }

    public void setfForeignAvail(long fForeignAvail) {
        this.fForeignAvail = fForeignAvail;
    }

    public double getfIndex() {
        return fIndex;
    }

    public void setfIndex(double fIndex) {
        this.fIndex = fIndex;
    }

    public int getfOpenOrders() {
        return fOpenOrders;
    }

    public void setfOpenOrders(int fOpenOrders) {
        this.fOpenOrders = fOpenOrders;
    }

    public long getfListedSize() {
        return fListedSize;
    }

    public void setfListedSize(long fListedSize) {
        this.fListedSize = fListedSize;
    }

    public long getfTradeableSize() {
        return fTradeableSize;
    }

    public void setfTradeableSize(long fTrdeableSize) {
        this.fTradeableSize = fTrdeableSize;
    }

    public double getfAvgLast() {
        return fAvgLast;
    }

    public void setfAvgLast(double fAvgLast) {
        this.fAvgLast = fAvgLast;
    }

    public int getfListingDate() {
        return fListingDate;
    }

    public void setfListingDate(int fListingDate) {
        this.fListingDate = fListingDate;
    }

    public int getfDeListingDate() {
        return fDeListingDate;
    }

    public void setfDeListingDate(int fDeListingDate) {
        this.fDeListingDate = fDeListingDate;
    }

    public int getfCouponFreq() {
        return fCouponFreq;
    }

    public void setfCouponFreq(int fCouponFreq) {
        this.fCouponFreq = fCouponFreq;
    }

    public int getfLastRecord() {
        return fLastRecord;
    }

    public void setfLastRecord(int fLastRecord) {
        this.fLastRecord = fLastRecord;
    }
    
     public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        //. set message header (MsgType) = U4
        setfMsgType(QRIDataConst.MsgType.SecurityReport.getValue());
        msgBuild.setMessage(getStringHeader());
        msgBuild.addData(QRIDataConst.QRITag.RecordNumber, String.valueOf(fRecordNumber));   
        msgBuild.addData(QRIDataConst.QRITag.SecurityID, String.valueOf(fSecurityID));   
        msgBuild.addData(QRIDataConst.QRITag.Symbol, String.valueOf(fSymbol));
        msgBuild.addData(QRIDataConst.QRITag.Remarks, String.valueOf(fRemarks));
        msgBuild.addData(QRIDataConst.QRITag.Remarks2, String.valueOf(fRemarks2));
        msgBuild.addData(QRIDataConst.QRITag.Shortname, String.valueOf(fShortName));
        msgBuild.addData(QRIDataConst.QRITag.Status, String.valueOf(fStatus));
        msgBuild.addData(QRIDataConst.QRITag.SecurityTradingStatus, String.valueOf(fSecurityTradingStatus));
        msgBuild.addData(QRIDataConst.QRITag.MarketCode, String.valueOf(fMarketCode));
        msgBuild.addData(QRIDataConst.QRITag.InstrCode, String.valueOf(fInstrCode));
        msgBuild.addData(QRIDataConst.QRITag.SymbolSfx, String.valueOf(fSymbolSfx));
        msgBuild.addData(QRIDataConst.QRITag.SectorCode, String.valueOf(fSectorCode));
        msgBuild.addData(QRIDataConst.QRITag.OrderMethods, String.valueOf(fOrderMethods));   
        msgBuild.addData(QRIDataConst.QRITag.LotSize, String.valueOf(fLotSize));
        msgBuild.addData(QRIDataConst.QRITag.MinimumStep, String.valueOf(fMinimumStep));
        msgBuild.addData(QRIDataConst.QRITag.FaceValue, String.valueOf(fFaceValue));
        msgBuild.addData(QRIDataConst.QRITag.FaceUnit, String.valueOf(fFaceUnit));
        msgBuild.addData(QRIDataConst.QRITag.Earnings, String.valueOf(fEarnings));
        msgBuild.addData(QRIDataConst.QRITag.PreviousDate, String.valueOf(fPreviousDate));
        msgBuild.addData(QRIDataConst.QRITag.PreviousPrice, String.valueOf(fPreviousPrice));
        msgBuild.addData(QRIDataConst.QRITag.PriceFormat, String.valueOf(fPriceFormat));
        msgBuild.addData(QRIDataConst.QRITag.Decimals, String.valueOf(fDecimals));
        msgBuild.addData(QRIDataConst.QRITag.QuantityLimit, String.valueOf(fQtyLimit));
        msgBuild.addData(QRIDataConst.QRITag.VolTraded, String.valueOf(fVolTraded));
        msgBuild.addData(QRIDataConst.QRITag.ValTraded, String.valueOf(fValTraded));
        msgBuild.addData(QRIDataConst.QRITag.Yield, String.valueOf(fAccruedInterestRate));
        msgBuild.addData(QRIDataConst.QRITag.PrimaryDist, String.valueOf(fPrimaryDist));
        msgBuild.addData(QRIDataConst.QRITag.ForeignLimit, String.valueOf(fForeignLimit));
        msgBuild.addData(QRIDataConst.QRITag.ForeignLevel, String.valueOf(fForeignLevel));
        msgBuild.addData(QRIDataConst.QRITag.ForeignAvail, String.valueOf(fForeignAvail));
        msgBuild.addData(QRIDataConst.QRITag.Index, String.valueOf(fIndex));
        msgBuild.addData(QRIDataConst.QRITag.OpenOrders, String.valueOf(fOpenOrders));
        msgBuild.addData(QRIDataConst.QRITag.ListedSize, String.valueOf(fListedSize));
        msgBuild.addData(QRIDataConst.QRITag.TradeAbleSize, String.valueOf(fTradeableSize));
        msgBuild.addData(QRIDataConst.QRITag.AvgLast, String.valueOf(fAvgLast));
        msgBuild.addData(QRIDataConst.QRITag.ListingDate, String.valueOf(fListingDate));
        msgBuild.addData(QRIDataConst.QRITag.DelistingDate, String.valueOf(fDeListingDate));
        msgBuild.addData(QRIDataConst.QRITag.CouponFreq, String.valueOf(fCouponFreq));
        msgBuild.addData(QRIDataConst.QRITag.LastRecord, String.valueOf(fLastRecord));
        
        msgBuild.addData(QRIDataConst.QRITag.MsgType, getfMsgType());
        msgBuild.addData(QRIDataConst.QRITag.Checksum, StringHelper.addZeroFromInt(0, 3));
        
        return convertToJonectMessage(msgBuild.getMessage(), getfMsgType());
    }
     
    @Override
     public void assignValue(){
        Map<String, String> fieldMap = getMapMsgFields();
        
        Enumeration<String> strEnum = Collections.enumeration(fieldMap.keySet());
        String keyField;
        String val;
        //.-- asign nilai header nya dulu
        assignHeaderValue();
        while(strEnum.hasMoreElements()) {
            keyField = strEnum.nextElement();
            val = fieldMap.get(keyField);
            switch(keyField){
                case QRIDataConst.QRITag.RecordNumber:
                    setfRecordNumber(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.SecurityID:
                    setfSecurityID(val);
                    break;
                case QRIDataConst.QRITag.Symbol:
                    setfSymbol(val);
                    break;
                case QRIDataConst.QRITag.Remarks:
                    setfRemarks(val);
                    break;
                case QRIDataConst.QRITag.Remarks2:
                    setfRemarks2(val);
                    break;
                case QRIDataConst.QRITag.Shortname:
                    setfShortName(val);
                    break;
                case QRIDataConst.QRITag.Status:
                    setfStatus(val);
                    break;
                case QRIDataConst.QRITag.SecurityTradingStatus:
                    setfSecurityTradingStatus(val);
                    break;
                case QRIDataConst.QRITag.MarketCode:
                    setfMarketCode(val);
                    break;
                case QRIDataConst.QRITag.InstrCode:
                    setfInstrCode(val);
                    break;
                case QRIDataConst.QRITag.SymbolSfx:
                    setfSymbolSfx(val);
                    break;
                case QRIDataConst.QRITag.SectorCode:
                    setfSectorCode(val);
                    break;
                case QRIDataConst.QRITag.OrderMethods:
                    setfOrderMethods(val);
                    break;
                case QRIDataConst.QRITag.LotSize:
                    setfLotSize(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.MinimumStep:
                    setfMinimumStep(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.FaceValue:
                    setfFaceValue(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.FaceUnit:
                    setfFaceUnit(val);
                    break;
                case QRIDataConst.QRITag.Earnings:
                    setfEarnings(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.PreviousDate:
                    setfPreviousDate(val);
                    break;
                case QRIDataConst.QRITag.PreviousPrice:
                    setfPreviousPrice(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.PriceFormat:
                    setfPriceFormat(val);
                    break;
                case QRIDataConst.QRITag.Decimals:
                    setfDecimals(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.QuantityLimit:
                    setfQtyLimit(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.VolTraded:
                    setfVolTraded(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.ValTraded:
                    setfValTraded(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.Yield:
                    setfYield(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.AccruedInterestRate:
                    setfAccruedInterestRate(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.PrimaryDist:
                    setfPrimaryDist(val);
                    break;
                case QRIDataConst.QRITag.ForeignLimit:
                    setfForeignLimit(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.ForeignLevel:
                    setfForeignLevel(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.ForeignAvail:
                    setfForeignAvail(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.Index:
                    setfIndex(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.OpenOrders:
                    setfOpenOrders(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.ListedSize:
                    setfListedSize(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.TradeAbleSize:
                    setfTradeableSize(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.AvgLast:
                    setfAvgLast(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.ListingDate:
                    setfListingDate(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.DelistingDate:
                    setfDeListingDate(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.CouponFreq:
                    setfCouponFreq(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.LastRecord:  
                    setfLastRecord(StringUtil.toInteger(val));
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
    
}
