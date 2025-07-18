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
public class QRIDataTradeListMessage extends QRIDataHeader{
    private String          fExecID;
    private long            fOrderID;
    private long            fSecondaryOrderID;
    private String          fTransactionTime;
    private String          fEffectiveTime;
    private String          fSide; //.(REV)apm:20190903:int-->String;
    private String          fClOrdID;
    private String          fClientID;
    private String          fExecBroker;
    private String          fContraTrader;
    private String          fContraBroker;
    private String          fAccount;
    private String          fSymbol;
    private String          fSymbolSfx;
    private String          fSecurityID;
    private double          fPrice;
    private long            fCumQty;
    private String          fText;
    private String          fClearingAccount;
    private String          fFutSettDate;
    private int             fExecType;
    private long            fLastPx;
    private int             fNoContraBrokers;
    private int             fExecTransType;
    private String          fOrdStatus;
    private long            fLeavesQty;
    private long            fAvgPx;
    private String          fComplianceID;
    
    
    public QRIDataTradeListMessage(){
        
    }
    public QRIDataTradeListMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public String getfExecID() {
        return fExecID;
    }

    public long getfOrderID() {
        return fOrderID;
    }

    public void setfOrderID(long fOrderID) {
        this.fOrderID = fOrderID;
    }

    public void setfExecID(String fExecID) {
        this.fExecID = fExecID;
    }

    public long getfSecondaryOrderID() {
        return fSecondaryOrderID;
    }

    public void setfSecondaryOrderID(long fSecondaryOrderID) {
        this.fSecondaryOrderID = fSecondaryOrderID;
    }

    public String getfTransactionTime() {
        return fTransactionTime;
    }

    public void setfTransactionTime(String fTransactionTime) {
        this.fTransactionTime = fTransactionTime;
    }

    public String getfEffectiveTime() {
        return fEffectiveTime;
    }

    public void setfEffectiveTime(String fEffectiveTime) {
        this.fEffectiveTime = fEffectiveTime;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }

    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }

    public String getfContraTrader() {
        return fContraTrader;
    }

    public void setfContraTrader(String fContraTrader) {
        this.fContraTrader = fContraTrader;
    }

    public String getfContraBroker() {
        return fContraBroker;
    }

    public void setfContraBroker(String fContraBroker) {
        this.fContraBroker = fContraBroker;
    }

    public String getfAccount() {
        return fAccount;
    }

    public void setfAccount(String fAccount) {
        this.fAccount = fAccount;
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

    public double getfPrice() {
        return fPrice;
    }

    public void setfPrice(double fPrice) {
        this.fPrice = fPrice;
    }

    public long getfCumQty() {
        return fCumQty;
    }

    public void setfCumQty(long fCumQty) {
        this.fCumQty = fCumQty;
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

    public String getfFutSettDate() {
        return fFutSettDate;
    }

    public void setfFutSettDate(String fFutSettDate) {
        this.fFutSettDate = fFutSettDate;
    }

    public int getfExecType() {
        return fExecType;
    }

    public void setfExecType(int fExecType) {
        this.fExecType = fExecType;
    }

    public long getfLastPx() {
        return fLastPx;
    }

    public void setfLastPx(long fLastPx) {
        this.fLastPx = fLastPx;
    }

    public int getfNoContraBrokers() {
        return fNoContraBrokers;
    }

    public void setfNoContraBrokers(int fNoContraBrokers) {
        this.fNoContraBrokers = fNoContraBrokers;
    }

    public int getfExecTransType() {
        return fExecTransType;
    }

    public void setfExecTransType(int fExecTransType) {
        this.fExecTransType = fExecTransType;
    }

    public String getfOrdStatus() {
        return fOrdStatus;
    }

    public void setfOrdStatus(String fOrdStatus) {
        this.fOrdStatus = fOrdStatus;
    }

    public long getfLeavesQty() {
        return fLeavesQty;
    }

    public void setfLeavesQty(long fLeavesQty) {
        this.fLeavesQty = fLeavesQty;
    }

    public long getfAvgPx() {
        return fAvgPx;
    }

    public void setfAvgPx(long fAvgPx) {
        this.fAvgPx = fAvgPx;
    }

    public String getfComplianceID() {
        return fComplianceID;
    }

    public void setfComplianceID(String fComplianceID) {
        this.fComplianceID = fComplianceID;
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
    
    
    
     public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        //. set message header (MsgType) = 8
        setfMsgType(QRIDataConst.MsgType.ExecutionReport.getValue());
        
        msgBuild.setMessage(getStringHeader());
        msgBuild.addData(QRIDataConst.QRITag.ExecID, String.valueOf(fExecID));      
        msgBuild.addData(QRIDataConst.QRITag.SecondaryOrderID, String.valueOf(fSecondaryOrderID));
        msgBuild.addData(QRIDataConst.QRITag.OrderID, String.valueOf(fOrderID));
        msgBuild.addData(QRIDataConst.QRITag.TransactTime, String.valueOf(fTransactionTime));
        msgBuild.addData(QRIDataConst.QRITag.EffectiveTime, String.valueOf(fEffectiveTime));
        msgBuild.addData(QRIDataConst.QRITag.Side, String.valueOf(fSide));
        msgBuild.addData(QRIDataConst.QRITag.ClOrdID, String.valueOf(fClOrdID));
        msgBuild.addData(QRIDataConst.QRITag.ClientID, String.valueOf(fClientID));
        msgBuild.addData(QRIDataConst.QRITag.ExecBroker, String.valueOf(fExecBroker));
        msgBuild.addData(QRIDataConst.QRITag.ContraTrader, String.valueOf(fContraTrader));
        msgBuild.addData(QRIDataConst.QRITag.ContraBroker, String.valueOf(fContraBroker));
        msgBuild.addData(QRIDataConst.QRITag.Account, String.valueOf(fAccount));
        msgBuild.addData(QRIDataConst.QRITag.Symbol, String.valueOf(fSymbol));
        msgBuild.addData(QRIDataConst.QRITag.SymbolSfx, String.valueOf(fSymbolSfx));
        msgBuild.addData(QRIDataConst.QRITag.SecurityID, String.valueOf(fSecurityID));
        msgBuild.addData(QRIDataConst.QRITag.Price, String.valueOf(fPrice));
        msgBuild.addData(QRIDataConst.QRITag.CumQty, String.valueOf(fCumQty));
        msgBuild.addData(QRIDataConst.QRITag.Text, String.valueOf(fText));
        msgBuild.addData(QRIDataConst.QRITag.ClearingAccount, String.valueOf(fClearingAccount));
        msgBuild.addData(QRIDataConst.QRITag.FutSettDate, String.valueOf(fFutSettDate));
        msgBuild.addData(QRIDataConst.QRITag.ExecType, String.valueOf(fExecType));
        msgBuild.addData(QRIDataConst.QRITag.LastPx, String.valueOf(fLastPx));
        msgBuild.addData(QRIDataConst.QRITag.NoContraBrokers, String.valueOf(fNoContraBrokers));
        msgBuild.addData(QRIDataConst.QRITag.ExecTransType, String.valueOf(fExecTransType));
        msgBuild.addData(QRIDataConst.QRITag.OrdStatus, String.valueOf(fOrdStatus));
        msgBuild.addData(QRIDataConst.QRITag.LeavesQty, String.valueOf(fLeavesQty));
        msgBuild.addData(QRIDataConst.QRITag.AvgPx, String.valueOf(fAvgPx));
        msgBuild.addData(QRIDataConst.QRITag.ComplianceID, String.valueOf(fComplianceID));
        
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
                case QRIDataConst.QRITag.SecondaryOrderID: 
                    setfSecondaryOrderID(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.NoContraBrokers: 
                    setfNoContraBrokers(StringUtil.toInteger(val));
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
                case QRIDataConst.QRITag.Side: 
                    //////setfSide(StringUtil.toInteger(val));
                    setfSide(val);
                    break;
                case QRIDataConst.QRITag.ContraTrader: 
                    setfContraTrader(val);
                    break;
                case QRIDataConst.QRITag.ContraBroker: 
                    setfContraBroker(val);
                    break;
                case QRIDataConst.QRITag.Price: 
                    setfPrice(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.EffectiveTime: 
                    setfEffectiveTime(val);
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
                case QRIDataConst.QRITag.LastPx:
                    setfLastPx(StringUtil.toLong(val));
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
                case QRIDataConst.QRITag.ComplianceID:
                    setfComplianceID(val);
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
    
}
