/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValueLength;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataNegotiationDealAmend extends ORIDataHeader {

    //.enums:
    public enum ORINegotiationDealAmendType{
        TwoSide
    }
    
    //.fields:
    private String fOrderID                                     = "";
    private String fClientID                                    = "";
    private String fOrigClOrdID                                 = "";
    private String fClOrdID                                     = "";
    private String fAccount                                     = "";
    private int fHandlInst                                      = 0;
    private String fSymbol                                      = "";
    private String fSide                                        = "";
    private String fTransactTime                                = "";
    private long fOrderQty                                      = 0;
    private String fOrdType                                     = "";
    private double fPrice                                       = 0;
    private String fSettlDate                                   = ""; //.(NEW)apm:20190903:v210;
    private String fSettlDeliveryType                           = ""; //.(NEW)apm:20190903:v210;
    private String fText                                        = "";
    private String fClearingAccount                             = "";
    private String fComplianceID                                = "";
    //.subfields:
    private String sfCounterpartUserID                          = "";
    private int sfReminderTimeInterval                          = 0;
    
    
    private ORINegotiationDealAmendType fNegotiationDealAmendType;
    
    public ORIDataNegotiationDealAmend(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }
    
    public String getfOrderID() {
        return fOrderID;
    }

    public void setfOrderID(String fOrderID) {
        this.fOrderID = fOrderID;
    }

    public String getfClientID() {
        return fClientID;
    }

    public void setfClientID(String fClientID) {
        this.fClientID = fClientID;
    }

    public String getfOrigClOrdID() {
        return fOrigClOrdID;
    }

    public void setfOrigClOrdID(String fOrigClOrdID) {
        this.fOrigClOrdID = fOrigClOrdID;
    }

    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }

    public String getfAccount() {
        return fAccount;
    }

    public void setfAccount(String fAccount) {
        this.fAccount = fAccount;
    }

    public int getfHandlInst() {
        return fHandlInst;
    }

    public void setfHandlInst(int fHandlInst) {
        this.fHandlInst = fHandlInst;
    }

    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }

    public String getfTransactTime() {
        return fTransactTime;
    }

    public void setfTransactTime(String fTransactTime) {
        this.fTransactTime = fTransactTime;
    }

    public long getfOrderQty() {
        return fOrderQty;
    }

    public void setfOrderQty(long fOrderQty) {
        this.fOrderQty = fOrderQty;
    }

    public String getfOrdType() {
        return fOrdType;
    }

    public void setfOrdType(String fOrdType) {
        this.fOrdType = fOrdType;
    }

    public double getfPrice() {
        return fPrice;
    }

    public void setfPrice(double fPrice) {
        this.fPrice = fPrice;
    }
    
    public String getfSettlDate() {
        return fSettlDate;
    }

    public void setfSettlDate(String fSettlDate) {
        this.fSettlDate = fSettlDate;
    }

    public String getfSettlDeliveryType() {
        return fSettlDeliveryType;
    }

    public void setfSettlDeliveryType(String fSettlDeliveryType) {
        this.fSettlDeliveryType = fSettlDeliveryType;
    }
    
    public String getSfCounterpartUserID() {
        return sfCounterpartUserID;
    }

    public void setSfCounterpartUserID(String sfCounterpartUserID) {
        this.sfCounterpartUserID = sfCounterpartUserID;
    }

    public int getSfReminderTimeInterval() {
        return sfReminderTimeInterval;
    }

    public void setSfReminderTimeInterval(int sfReminderTimeInterval) {
        this.sfReminderTimeInterval = sfReminderTimeInterval;
    }
    
    public String getfText() {
        try{
            //.generate subfieldvalue:
            StringBuilder sb = new StringBuilder();
            switch(fNegotiationDealAmendType){
                case TwoSide: //.LS-XX 'UUUUUUUUUUUUTTTT' U=CounterpartUserID, T=ReminderTime.
                    sb.append(StringHelper.addSpaces(sfCounterpartUserID, ORIFieldValueLength.ORDER_NEGDEAL_AMEND_SUB_TEXT_COUNTERPART_USERID));
                    sb.append(StringHelper.addZeroFromInt(sfReminderTimeInterval, ORIFieldValueLength.ORDER_NEGDEAL_AMEND_SUB_TEXT_REMINDER_TIME_INTERVAL));
                    break;
                default:
                    break;
            }
            fText = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
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

    public ORINegotiationDealAmendType getfNegotiationDealAmendType() {
        return fNegotiationDealAmendType;
    }

    public void setfNegotiationDealAmendType(ORINegotiationDealAmendType fNegotiationDealAmendType) {
        this.fNegotiationDealAmendType = fNegotiationDealAmendType;
    }
    
    public boolean assignMessage(){
        boolean bOut = false;
        try{
            //.assign header:
            assignHeaderMessage();
            //.assign data:
            Map<String, String> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    String zValue = mapFields.get(zKey);
                    switch(zKey){
                        case ORIDataConst.ORIFieldTag.ORDERID:
                            setfOrderID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.CLIENTID:
                            setfClientID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.ORIGCLORDID:
                            setfOrigClOrdID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.CLORDID:
                            setfClOrdID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.ACCOUNT:
                            setfAccount(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.HANDLINST:
                            setfHandlInst(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.SYMBOL:
                            setfSymbol(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SIDE:
                            setfSide(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.TRANSACTTIME:
                            setfTransactTime(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.ORDERQTY:
                            setfOrderQty(StringHelper.toLong(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.ORDTYPE:
                            setfOrdType(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.PRICE:
                            setfPrice(StringHelper.toDouble(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.SETTLDATE:
                            setfSettlDate(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SETTLDELIVERYTYPE:
                            setfSettlDeliveryType(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.TEXT:
                            setfText(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.CLEARINGACCOUNT:
                            setfClearingAccount(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.COMPLIANCEID:
                            setfComplianceID(zValue);
                            break;
                        default:
                            break;
                    }
                }
                bOut = true;
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return bOut;
    }
    
    public String msgDataToString() {
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(ORIDataConst.ORIFieldTag.ORDERID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.CLIENTID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClientID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.ORIGCLORDID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrigClOrdID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.CLORDID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClOrdID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.ACCOUNT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfAccount()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.HANDLINST).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfHandlInst()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.SYMBOL).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSymbol()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.SIDE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSide()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.TRANSACTTIME).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfTransactTime()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.ORDERQTY).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderQty()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.ORDTYPE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrdType()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.PRICE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfPrice()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.TEXT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfText()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.CLEARINGACCOUNT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClearingAccount()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.COMPLIANCEID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfComplianceID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            //.apm:20191128:ref hrn:
            if (!StringHelper.isNullOrEmpty(getfSettlDate())){
                sb.append(ORIDataConst.ORIFieldTag.SETTLDATE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                sb.append(getfSettlDate()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSettlDeliveryType())){
                sb.append(ORIDataConst.ORIFieldTag.SETTLDELIVERYTYPE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                sb.append(getfSettlDeliveryType()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            }
            
            //... .
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public String msgToString() {
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            if (StringHelper.isNullOrEmpty(getfMsgType())){
                setfMsgType(ORIDataConst.ORIMsgType.ORDER_CANCEL_REPLACE);
            }
            sb.append(msgBundlePrefixToString());
            sb.append(msgHeaderToString());
            sb.append(msgDataToString());
            sb.append(msgBundleSuffixToString());
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
}
