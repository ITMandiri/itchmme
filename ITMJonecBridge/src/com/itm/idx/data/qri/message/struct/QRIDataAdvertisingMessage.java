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
public class QRIDataAdvertisingMessage extends QRIDataHeader {
    private long            fOrderID;
    private String          fClOrdID;
    private int             fHandlInst;
    private long            fOrigClOrdID;
    private String          fClientID;
    private String          fExecBroker;
    private String          fExecID;
    private int             fExecTransType;
    private int             fExecType;
    private String          fOrdStatus;
    private String          fAccount;
    private String          fFutSettDate;
    private String          fSymbol;
    private String          fSymbolSfx;
    private String          fSecurityID;
    private String          fSide;
    private long            fOrderQty;
    private int             fOrderType;
    private double          fPrice;
    private String          fTimeInForce;
    private String          fExpiredDate;
    private String          fExecInst;
    private long            fLeavesQty;
    private long            fCumQty;
    private long            fAvgPx;
    private String          fTradeDate;
    private String          fTransactionTime;
    private String          fText;
    private String          fClearingAccount;
    
    public QRIDataAdvertisingMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }
    
    public QRIDataAdvertisingMessage(){
        
    }

    public long getfOrderID() {
        return fOrderID;
    }

    public void setfOrderID(long fOrderID) {
        this.fOrderID = fOrderID;
    }

    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }

    public int getfHandlInst() {
        return fHandlInst;
    }

    public void setfHandlInst(int fHandlInst) {
        this.fHandlInst = fHandlInst;
    }

    public long getfOrigClOrdID() {
        return fOrigClOrdID;
    }

    public void setfOrigClOrdID(long fOrigClOrdID) {
        this.fOrigClOrdID = fOrigClOrdID;
    }

    public String getfClientID() {
        return fClientID;
    }

    public void setfClientID(String fClientID) {
        this.fClientID = fClientID;
    }

    public String getfExecBroker() {
        return fExecBroker;
    }

    public void setfExecBroker(String fExecBroker) {
        this.fExecBroker = fExecBroker;
    }

    public String getfExecID() {
        return fExecID;
    }

    public void setfExecID(String fExecID) {
        this.fExecID = fExecID;
    }

    public int getfExecTransType() {
        return fExecTransType;
    }

    public void setfExecTransType(int fExecTransType) {
        this.fExecTransType = fExecTransType;
    }

    public int getfExecType() {
        return fExecType;
    }

    public void setfExecType(int fExecType) {
        this.fExecType = fExecType;
    }

    public String getfOrdStatus() {
        return fOrdStatus;
    }

    public void setfOrdStatus(String fOrdStatus) {
        this.fOrdStatus = fOrdStatus;
    }

    public String getfAccount() {
        return fAccount;
    }

    public void setfAccount(String fAccount) {
        this.fAccount = fAccount;
    }

    public String getfFutSettDate() {
        return fFutSettDate;
    }

    public void setfFutSettDate(String fFutSettDate) {
        this.fFutSettDate = fFutSettDate;
    }

    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }

    public String getfSymbolSfx() {
        return fSymbolSfx;
    }

    public void setfSymbolSfx(String fSymbolSfx) {
        this.fSymbolSfx = fSymbolSfx;
    }

    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }

    public long getfOrderQty() {
        return fOrderQty;
    }

    public void setfOrderQty(long fOrderQty) {
        this.fOrderQty = fOrderQty;
    }

    public int getfOrderType() {
        return fOrderType;
    }

    public void setfOrderType(int fOrderType) {
        this.fOrderType = fOrderType;
    }

    public double getfPrice() {
        return fPrice;
    }

    public void setfPrice(double fPrice) {
        this.fPrice = fPrice;
    }

    public String getfTimeInForce() {
        return fTimeInForce;
    }

    public void setfTimeInForce(String fTimeInForce) {
        this.fTimeInForce = fTimeInForce;
    }

    public String getfExpiredDate() {
        return fExpiredDate;
    }

    public void setfExpiredDate(String fExpiredDate) {
        this.fExpiredDate = fExpiredDate;
    }

    public String getfExecInst() {
        return fExecInst;
    }

    public void setfExecInst(String fExecInst) {
        this.fExecInst = fExecInst;
    }

    public long getfLeavesQty() {
        return fLeavesQty;
    }

    public void setfLeavesQty(long fLeavesQty) {
        this.fLeavesQty = fLeavesQty;
    }

    public long getfCumQty() {
        return fCumQty;
    }

    public void setfCumQty(long fCumQty) {
        this.fCumQty = fCumQty;
    }

    public long getfAvgPx() {
        return fAvgPx;
    }

    public void setfAvgPx(long fAvgPx) {
        this.fAvgPx = fAvgPx;
    }

    public String getfTradeDate() {
        return fTradeDate;
    }

    public void setfTradeDate(String fTradeDate) {
        this.fTradeDate = fTradeDate;
    }

    public String getfTransactionTime() {
        return fTransactionTime;
    }

    public void setfTransactionTime(String fTransactionTime) {
        this.fTransactionTime = fTransactionTime;
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }

    public String getfClearingAccount() {
        return fClearingAccount;
    }

    public void setfClearingAccount(String fClearingAccount) {
        this.fClearingAccount = fClearingAccount;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        //. set message header (MsgType) = 8
        setfMsgType(QRIDataConst.MsgType.ExecutionReport.getValue());
        
        msgBuild.setMessage(getStringHeader());
        
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
                case QRIDataConst.QRITag.OrderID: 
                    setfOrderID(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.ClOrdID:
                    setfClOrdID(val);
                    break;
                case QRIDataConst.QRITag.HandlInst:
                    setfHandlInst(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.OrigClOrdID:
                    setfOrigClOrdID(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.ClientID: 
                    setfClientID(val);
                    break;
                case QRIDataConst.QRITag.ExecBroker: 
                    setfExecBroker(val);
                    break;
                case QRIDataConst.QRITag.ExecID: 
                    setfExecID(val);
                    break;
                case QRIDataConst.QRITag.ExecTransType: 
                    setfExecTransType(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.ExecType:
                    setfExecType(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.OrdStatus: 
                    setfOrdStatus(val);
                    break;
                case QRIDataConst.QRITag.Account: 
                    setfAccount(val);
                    break;
                case QRIDataConst.QRITag.FutSettDate: 
                    setfFutSettDate(val);
                    break;
                case QRIDataConst.QRITag.Symbol: 
                    setfSymbol(val);
                    break;
                case QRIDataConst.QRITag.SymbolSfx: 
                    setfSymbolSfx(val);
                    break;
                case QRIDataConst.QRITag.SecurityID: 
                    setfSecurityID(val);
                    break;
                case QRIDataConst.QRITag.OrdType: 
                    setfOrderType(StringUtil.toInteger(val));
                    break;    
                case QRIDataConst.QRITag.Side: 
                    setfSide(val);
                    break;
                case QRIDataConst.QRITag.OrderQty: 
                    setfOrderQty(StringUtil.toLong(val));
                    break;               
                case QRIDataConst.QRITag.Price: 
                    setfPrice(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.TimeInForce: 
                    setfTimeInForce(val);
                    break;
                case QRIDataConst.QRITag.ExpireDate: 
                    setfExpiredDate(val);
                    break;
                case QRIDataConst.QRITag.ExecInst: 
                    setfExecInst(val);
                    break;
                case QRIDataConst.QRITag.LeavesQty: 
                    setfLeavesQty(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.CumQty: 
                    setfCumQty(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.AvgPx: 
                    setfAvgPx(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.TransactTime: 
                    setfTransactionTime(val);
                    break;
                case QRIDataConst.QRITag.Text: 
                    setfText(val);
                    break;
                case QRIDataConst.QRITag.ClearingAccount: 
                    setfClearingAccount(val);
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }

}
