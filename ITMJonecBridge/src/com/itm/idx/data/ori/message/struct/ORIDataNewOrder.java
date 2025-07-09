/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldFmt;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldTag;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataNewOrder extends ORIDataHeader {
    
    //.fields:
    private String fClOrdID                                     = "";//
    private String fClientID                                    = "";//
    private String fAccount                                     = "";//
    private int fHandlInst                                      = 0;//
    private String fExecInst                                    = "";//
    private String fSymbol                                      = "";//
    private String fSymbolSfx                                   = "";//
    private String fSecurityID                                  = "";//
    private String fSide                                        = "";//
    private String fTransactTime                                = "";//
    private long fOrderQty                                      = 0;//
    private String fOrdType                                     = "";//
    private double fPrice                                       = 0;//
    private double fStopPx                                      = 0;//
    private String fTimeInForce                                 = "";//
    private String fExpireDate                                  = "";//?
    private String fText                                        = "";//
    private String fClearingAccount                             = "";//
    private String fComplianceID                                = "";//
    
    public ORIDataNewOrder(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
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

    public String getfExecInst() {
        return fExecInst;
    }

    public void setfExecInst(String fExecInst) {
        this.fExecInst = fExecInst;
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

    public double getfStopPx() {
        return fStopPx;
    }

    public void setfStopPx(double fStopPx) {
        this.fStopPx = fStopPx;
    }

    public String getfTimeInForce() {
        return fTimeInForce;
    }

    public void setfTimeInForce(String fTimeInForce) {
        this.fTimeInForce = fTimeInForce;
    }

    public String getfExpireDate() {
        return fExpireDate;
    }

    public void setfExpireDate(String fExpireDate) {
        this.fExpireDate = fExpireDate;
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
                        case ORIDataConst.ORIFieldTag.CLORDID:
                            setfClOrdID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.CLIENTID:
                            setfClientID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.ACCOUNT:
                            setfAccount(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.HANDLINST:
                            setfHandlInst(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.EXECINST:
                            setfExecInst(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SYMBOL:
                            setfSymbol(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SYMBOLSFX:
                            setfSymbolSfx(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SECURITYID:
                            setfSecurityID(zValue);
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
                        case ORIDataConst.ORIFieldTag.STOPPX:
                            setfStopPx(StringHelper.toDouble(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.TIMEINFORCE:
                            setfTimeInForce(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.EXPIREDATE:
                            setfExpireDate(zValue);
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
            sb.append(ORIFieldTag.CLORDID).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClOrdID()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.CLIENTID).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClientID()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.ACCOUNT).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfAccount()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.HANDLINST).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfHandlInst()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.EXECINST).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfExecInst()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.SYMBOL).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSymbol()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.SYMBOLSFX).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldValue.SYMBOLSFX_PREFIX).append(getfSymbolSfx()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.SECURITYID).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSecurityID()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.SIDE).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSide()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.TRANSACTTIME).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfTransactTime()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.ORDERQTY).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderQty()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.ORDTYPE).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrdType()).append(ORIFieldFmt.FIELD_SEPARATOR);
            //. hrn: 20211203 - price tidak diikutkan jika <= 0
            if (getfPrice() > 0){
                sb.append(ORIFieldTag.PRICE).append(ORIFieldFmt.KV_SEPARATOR);
                sb.append(getfPrice()).append(ORIFieldFmt.FIELD_SEPARATOR);
            }
            
            sb.append(ORIFieldTag.STOPPX).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfStopPx()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.TIMEINFORCE).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfTimeInForce()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.EXPIREDATE).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfExpireDate()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.TEXT).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfText()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.CLEARINGACCOUNT).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClearingAccount()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.COMPLIANCEID).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfComplianceID()).append(ORIFieldFmt.FIELD_SEPARATOR);
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
                setfMsgType(ORIDataConst.ORIMsgType.NEW_ORDER);
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
