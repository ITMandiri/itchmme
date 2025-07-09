/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.db.record.object;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;

/**
 *
 * @author fredy
 */
public class StockDataRecord {
    private String fSecurityCode;
    private String fSecurityID;
    private String fSecurityName;
    private String fSecurityStatus;
    private String fLotSize;
    private String fPriceStep;
    private String fSecurityTradingStatus;
    private String fBoard_RG;
    private String fBoard_TN;
    private String fBoard_NG;
    private String fBoard_TS;
    private String fStockType;
    private String fPrevPrice;
    private String fFaceValue;
    private String fPreOpening;
    private String fListedSize;
    private String fTradeableSize;
    private String fStockMargin;
    private String fRemark;
    private String fRemark2;
    private String fLastPrice_RG;
    private String fLastPrice_TN;
    private String fLastPrice_NG;
    private String fStockDate;
    private String fSecurityStatus_TN;
    private String fSecurityStatus_NG;
    
    public StockDataRecord() {
    }

    public String getfSecurityCode() {
        return this.fSecurityCode;
    }

    public void setfSecurityCode(String fSecurityCode) {
        this.fSecurityCode = fSecurityCode;
    }

    public String getfSecurityID() {
        return this.fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }

    public String getfSecurityName() {
        return this.fSecurityName;
    }

    public void setfSecurityName(String fSecurityName) {
        this.fSecurityName = fSecurityName;
    }

    public String getfSecurityStatus() {
        return this.fSecurityStatus;
    }

    public void setfSecurityStatus(String fSecurityStatus) {
        this.fSecurityStatus = fSecurityStatus;
    }

    public String getfLotSize() {
        return this.fLotSize;
    }

    public void setfLotSize(String fLotSize) {
        this.fLotSize = fLotSize;
    }

    public String getfPriceStep() {
        return this.fPriceStep;
    }

    public void setfPriceStep(String fPriceStep) {
        this.fPriceStep = fPriceStep;
    }

    public String getfSecurityTradingStatus() {
        return this.fSecurityTradingStatus;
    }

    public void setfSecurityTradingStatus(String fSecurityTradingStatus) {
        this.fSecurityTradingStatus = fSecurityTradingStatus;
    }

    public String getfBoard_RG() {
        return this.fBoard_RG;
    }

    public void setfBoard_RG(String fBoard_RG) {
        this.fBoard_RG = fBoard_RG;
    }

    public String getfBoard_TN() {
        return this.fBoard_TN;
    }

    public void setfBoard_TN(String fBoard_TN) {
        this.fBoard_TN = fBoard_TN;
    }

    public String getfBoard_NG() {
        return this.fBoard_NG;
    }

    public void setfBoard_NG(String fBoard_NG) {
        this.fBoard_NG = fBoard_NG;
    }

    public String getfBoard_TS() {
        return this.fBoard_TS;
    }

    public void setfBoard_TS(String fBoard_TS) {
        this.fBoard_TS = fBoard_TS;
    }

    public String getfStockType() {
        return this.fStockType;
    }

    public void setfStockType(String fStockType) {
        this.fStockType = fStockType;
    }

    public String getfPrevPrice() {
        return this.fPrevPrice;
    }

    public void setfPrevPrice(String fPrevPrice) {
        this.fPrevPrice = fPrevPrice;
    }

    public String getfFaceValue() {
        return this.fFaceValue;
    }

    public void setfFaceValue(String fFaceValue) {
        this.fFaceValue = fFaceValue;
    }

    public String getfPreOpening() {
        return this.fPreOpening;
    }

    public void setfPreOpening(String fPreOpening) {
        this.fPreOpening = fPreOpening;
    }

    public String getfListedSize() {
        return this.fListedSize;
    }

    public void setfListedSize(String fListedSize) {
        this.fListedSize = fListedSize;
    }

    public String getfTradeableSize() {
        return this.fTradeableSize;
    }

    public void setfTradeableSize(String fTradeableSize) {
        this.fTradeableSize = fTradeableSize;
    }

    public String getfStockMargin() {
        return this.fStockMargin;
    }

    public void setfStockMargin(String fStockMargin) {
        this.fStockMargin = fStockMargin;
    }

    public String getfRemark() {
        return this.fRemark;
    }

    public void setfRemark(String fRemark) {
        this.fRemark = fRemark;
    }

    public String getfRemark2() {
        return this.fRemark2;
    }

    public void setfRemark2(String fRemark2) {
        this.fRemark2 = fRemark2;
    }

    public String getfLastPrice_RG() {
        return fLastPrice_RG;
    }

    public void setfLastPrice_RG(String fLastPrice_RG) {
        this.fLastPrice_RG = fLastPrice_RG;
    }

    public String getfLastPrice_TN() {
        return fLastPrice_TN;
    }

    public void setfLastPrice_TN(String fLastPrice_TN) {
        this.fLastPrice_TN = fLastPrice_TN;
    }

    public String getfLastPrice_NG() {
        return fLastPrice_NG;
    }

    public void setfLastPrice_NG(String fLastPrice_NG) {
        this.fLastPrice_NG = fLastPrice_NG;
    }
    
    public String getfStockDate() {
        return fStockDate;
    }

    public void setfStockDate(String fStockDate) {
        this.fStockDate = fStockDate;
    }

    public String getfSecurityStatus_TN() {
        return fSecurityStatus_TN;
    }

    public void setfSecurityStatus_TN(String fSecurityStatus_TN) {
        this.fSecurityStatus_TN = fSecurityStatus_TN;
    }

    public String getfSecurityStatus_NG() {
        return fSecurityStatus_NG;
    }

    public void setfSecurityStatus_NG(String fSecurityStatus_NG) {
        this.fSecurityStatus_NG = fSecurityStatus_NG;
    }
    
    
    
    
    //. helper
    public int getPreOpeningByRemarks2(String zStockRemark2, int iDefaultvalue){
        int iOut = iDefaultvalue;
        try{
            if ((!StringHelper.isNullOrEmpty(zStockRemark2)) && (zStockRemark2.length() >= QRIDataConst.QRIFieldValue.STOCK_REMARKS2_EFFECTIVE_SIZE)){
                switch(strMidBase1(zStockRemark2, 4, 1)){
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK_7_REMARK2_4_CANNOT_PREOPEN:
                        iOut = QRIDataConst.QRIFieldValue.PRE_OPENING_OFF;
                        break;
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK_7_REMARK2_4_CAN_PREOPEN:
                        iOut = QRIDataConst.QRIFieldValue.PRE_OPENING_ON;
                        break;
                    default:
                        //.value tidak dikenal:
                        iOut = QRIDataConst.QRIFieldValue.PRE_OPENING_OFF;
                        break;
                }
                //.selesai... .
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return iOut;
    }
    
    public int getStockMarginByRemarks2(String zStockRemark2, int iDefaultvalue){
        int iOut = iDefaultvalue;
        try{
            if ((!StringHelper.isNullOrEmpty(zStockRemark2)) && (zStockRemark2.length() >= QRIDataConst.QRIFieldValue.STOCK_REMARKS2_EFFECTIVE_SIZE)){
                switch(strMidBase1(zStockRemark2, 3, 1)){
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK_3_NO_INFORMATION:
                        iOut = QRIDataConst.QRIFieldValue.MARGINABLE_OFF;
                        break;
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK_3_MARGINABLE_SECURITIES:
                        iOut = QRIDataConst.QRIFieldValue.MARGINABLE_ON;
                        break;
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK_3_MARGINABLE_SECURITIES_SHORT_SELL:
                        iOut = QRIDataConst.QRIFieldValue.MARGINABLE_ON;
                        break;
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK_3_UNMARGINABLE_SECURITIES:
                        iOut = QRIDataConst.QRIFieldValue.MARGINABLE_OFF;
                        break;
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK2_3_DESIGNATED_STOCK:
                        iOut = QRIDataConst.QRIFieldValue.MARGINABLE_OFF;
                        break;
                    default:
                        //.value tidak dikenal:
                        iOut = QRIDataConst.QRIFieldValue.MARGINABLE_OFF;
                        break;
                }
                //.selesai... .
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return iOut;
    }
    
    private String strMidBase1(String inputStr, int pStartPos, int nLength){
        if ((pStartPos <= 0) || (nLength <= 0)){
            return "";
        }
        int maxLen = inputStr.length();
        if (pStartPos > maxLen){
            return "";
        }
        maxLen = maxLen - (pStartPos - 1);
        if (nLength > maxLen){
            nLength = maxLen;
        }
        try{
            return inputStr.substring((pStartPos - 1), ((pStartPos + nLength) - 1));
        }catch(Exception ex){
            return "";
        }
    }
}