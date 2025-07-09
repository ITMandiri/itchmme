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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIDataNegDealListMessage extends QRIDataHeader{
    private String          fOrderID;
    private String          fClOrdID;
    private String          fClientID;
    private String          fExecBroker;
    private int             fNoContraBrokers;
    private String          fContraTrader;
    private String          fContraBroker;
    private int             fExecID;
    private int             fExecTransType;
    private int             fExecType;
    private String          fOrdStatus;
    private String          fAccount;
    private String          fFutSettDate;
    private String          fSettlDeliveryType;
    private String          fSymbol;
    private String          fSymbolSfx;
    private String          fSecurityID;
    private String          fSide; //.(REV)apm:20190903:int-->String;
    private double          fPrice;
    private String          fEffectiveTime;
    private long            fLastPx;
    private long            fLeavesQty;
    private long            fCumQty;
    private long            fAvgPx;
    private String          fText;
    private String          fClearingAccount;
    private String          fComplianceID;
    private long            fOrderQty;
    
    public QRIDataNegDealListMessage(){
    }
    public QRIDataNegDealListMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public String getfOrderID() {
        return fOrderID;
    }

    public void setfOrderID(String fOrderID) {
        this.fOrderID = fOrderID;
    }

    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
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

    public int getfNoContraBrokers() {
        return fNoContraBrokers;
    }

    public void setfNoContraBrokers(int fNoContraBrokers) {
        this.fNoContraBrokers = fNoContraBrokers;
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

    public int getfExecID() {
        return fExecID;
    }

    public void setfExecID(int fExecID) {
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

    public String getfSettlDeliveryType() {
        return fSettlDeliveryType;
    }

    public void setfSettlDeliveryType(String fSettlDeliveryType) {
        this.fSettlDeliveryType = fSettlDeliveryType;
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

    public double getfPrice() {
        return fPrice;
    }

    public void setfPrice(double fPrice) {
        this.fPrice = fPrice;
    }

    public String getfEffectiveTime() {
        return fEffectiveTime;
    }

    public void setfEffectiveTime(String fEffectiveTime) {
        this.fEffectiveTime = fEffectiveTime;
    }

    public long getfLastPx() {
        return fLastPx;
    }

    public void setfLastPx(long fLastPx) {
        this.fLastPx = fLastPx;
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

    public String getfComplianceID() {
        return fComplianceID;
    }

    public void setfComplianceID(String fComplianceID) {
        this.fComplianceID = fComplianceID;
    }

    public long getfOrderQty() {
        return fOrderQty;
    }

    public void setfOrderQty(long fOrderQty) {
        this.fOrderQty = fOrderQty;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        //. set message header (MsgType) = 8
        setfMsgType(QRIDataConst.MsgType.ExecutionReport.getValue());
        
        msgBuild.setMessage(getStringHeader());
        
        msgBuild.addData(QRIDataConst.QRITag.OrderID, String.valueOf(fOrderID));
        msgBuild.addData(QRIDataConst.QRITag.ClOrdID, String.valueOf(fClOrdID));
        msgBuild.addData(QRIDataConst.QRITag.ClientID, String.valueOf(fClientID));
        msgBuild.addData(QRIDataConst.QRITag.ExecBroker, String.valueOf(fExecBroker));
        msgBuild.addData(QRIDataConst.QRITag.NoContraBrokers, String.valueOf(fNoContraBrokers));
        msgBuild.addData(QRIDataConst.QRITag.ContraBroker, String.valueOf(fContraBroker));
        msgBuild.addData(QRIDataConst.QRITag.ContraTrader, String.valueOf(fContraTrader));
        msgBuild.addData(QRIDataConst.QRITag.ExecID, String.valueOf(fExecID));
        msgBuild.addData(QRIDataConst.QRITag.ExecTransType, String.valueOf(fExecTransType));
        msgBuild.addData(QRIDataConst.QRITag.ExecType, String.valueOf(fExecType));
        msgBuild.addData(QRIDataConst.QRITag.OrdStatus, String.valueOf(fOrdStatus));
        msgBuild.addData(QRIDataConst.QRITag.Account, String.valueOf(fAccount));
        msgBuild.addData(QRIDataConst.QRITag.FutSettDate, String.valueOf(fFutSettDate));
        msgBuild.addData(QRIDataConst.QRITag.SettlDeliveryType, String.valueOf(fSettlDeliveryType));
        msgBuild.addData(QRIDataConst.QRITag.Symbol, String.valueOf(fSymbol));
        msgBuild.addData(QRIDataConst.QRITag.SymbolSfx, String.valueOf(fSymbolSfx));
        msgBuild.addData(QRIDataConst.QRITag.SecurityID, String.valueOf(fSecurityID));
        msgBuild.addData(QRIDataConst.QRITag.Side, String.valueOf(fSide));
        msgBuild.addData(QRIDataConst.QRITag.Price, String.valueOf(fPrice));
        msgBuild.addData(QRIDataConst.QRITag.EffectiveTime, String.valueOf(fEffectiveTime));
        msgBuild.addData(QRIDataConst.QRITag.LastPx, String.valueOf(fLastPx));
        msgBuild.addData(QRIDataConst.QRITag.LeavesQty, String.valueOf(fLeavesQty));
        msgBuild.addData(QRIDataConst.QRITag.CumQty, String.valueOf(fCumQty));
        msgBuild.addData(QRIDataConst.QRITag.AvgPx, String.valueOf(fAvgPx));
        msgBuild.addData(QRIDataConst.QRITag.Text, String.valueOf(fText));
        msgBuild.addData(QRIDataConst.QRITag.ClearingAccount, String.valueOf(fClearingAccount));   
        msgBuild.addData(QRIDataConst.QRITag.ComplianceID, String.valueOf(fComplianceID));
        msgBuild.addData(QRIDataConst.QRITag.OrderQty, String.valueOf(fOrderQty));
        
        msgBuild.addData(QRIDataConst.QRITag.MsgType, getfMsgType());
        msgBuild.addData(QRIDataConst.QRITag.Checksum, StringHelper.addZeroFromInt(0, 3));
        
        return convertToJonectMessage(msgBuild.getMessage(), getfMsgType());
    }
    
    @Override
    public void assignValue(){
        Map<String, String> fieldMap = new HashMap<>();
        
        fieldMap = getMapMsgFields();
        
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
                    setfOrderID(val); 
                    break;
                case QRIDataConst.QRITag.ClOrdID: 
                    setfClOrdID(val); 
                    break;
                case QRIDataConst.QRITag.ClientID: 
                    setfClientID(val); 
                    break;
                case QRIDataConst.QRITag.ExecBroker: 
                    setfExecBroker(val); 
                    break;
                case QRIDataConst.QRITag.NoContraBrokers: 
                    setfNoContraBrokers(StringUtil.toInteger(val)); 
                    break;
                case QRIDataConst.QRITag.ContraBroker: 
                    setfContraBroker(val); 
                    break;
                case QRIDataConst.QRITag.ContraTrader: 
                    setfContraTrader(val); 
                    break;
                case QRIDataConst.QRITag.ExecID: 
                    setfExecID(StringUtil.toInteger(val)); 
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
                case QRIDataConst.QRITag.SettlDeliveryType: 
                    setfSettlDeliveryType(val); 
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
                case QRIDataConst.QRITag.Price: 
                    setfPrice(StringUtil.toDouble(val)); 
                    break;
                case QRIDataConst.QRITag.EffectiveTime: 
                    setfEffectiveTime(val); 
                    break;
                case QRIDataConst.QRITag.LastPx: 
                    setfLastPx(StringUtil.toLong(val)); 
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
                case QRIDataConst.QRITag.Text: 
                    setfText(val); 
                    break;
                case QRIDataConst.QRITag.ClearingAccount: 
                    setfClearingAccount(val); 
                    break;
                case QRIDataConst.QRITag.ComplianceID: 
                    setfComplianceID(val); 
                    break;
                case QRIDataConst.QRITag.OrderQty: 
                    setfOrderQty(StringUtil.toLong(val)); 
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
}
