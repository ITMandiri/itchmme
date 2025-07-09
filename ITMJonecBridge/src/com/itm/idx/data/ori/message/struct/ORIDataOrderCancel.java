/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValue;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValueLength;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataOrderCancel extends ORIDataHeader {
    
    //.fields:
    private String fOrigClOrdID                                 = "";
    private String fOrderID                                     = "";
    private String fClOrdID                                     = "";
    private String fAccount                                     = "";
    private String fClientID                                    = "";
    private String fSymbol                                      = "";
    private String fSymbolSfx                                   = "";
    private String fSecurityID                                  = "";
    private String fSide                                        = "";
    private String fTransactTime                                = "";
    private long fOrderQty                                      = 0;
    private String fText                                        = ""; //."A2PPPPPPPPPPUUUUUUUUUUUUBBBBBBBBBBBB"; P=Price, U=UserID, B=BrokerID.
    //.subfields:
    private String sfOperation                                  = ORIFieldValue.ORDER_CANCEL_SUB_TEXT_OPERATION;
    private String sfPriceOperationCode                         = ORIFieldValue.ORDER_CANCEL_SUB_TEXT_PRICE_OPERATION_CODE;
    private double sfPrice                                      = 0;
    private String sfUserID                                     = ORIFieldValue.ORDER_CANCEL_SUB_TEXT_USERID;
    private String sfBrokerID                                   = ORIFieldValue.ORDER_CANCEL_SUB_TEXT_BROKERID;
    
    
    public ORIDataOrderCancel(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }

    public String getfOrigClOrdID() {
        return fOrigClOrdID;
    }

    public void setfOrigClOrdID(String fOrigClOrdID) {
        this.fOrigClOrdID = fOrigClOrdID;
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

    public String getfAccount() {
        return fAccount;
    }

    public void setfAccount(String fAccount) {
        this.fAccount = fAccount;
    }

    public String getfClientID() {
        return fClientID;
    }

    public void setfClientID(String fClientID) {
        this.fClientID = fClientID;
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

    public String getSfOperation() {
        return sfOperation;
    }

    public void setSfOperation(String sfOperation) {
        this.sfOperation = sfOperation;
    }

    public String getSfPriceOperationCode() {
        return sfPriceOperationCode;
    }

    public void setSfPriceOperationCode(String sfPriceOperationCode) {
        this.sfPriceOperationCode = sfPriceOperationCode;
    }

    public double getSfPrice() {
        return sfPrice;
    }

    public void setSfPrice(double sfPrice) {
        this.sfPrice = sfPrice;
    }

    public String getSfUserID() {
        return sfUserID;
    }

    public void setSfUserID(String sfUserID) {
        this.sfUserID = sfUserID;
    }

    public String getSfBrokerID() {
        return sfBrokerID;
    }

    public void setSfBrokerID(String sfBrokerID) {
        this.sfBrokerID = sfBrokerID;
    }
    
    public String getfText() {
        try{
            //.generate subfieldvalue:
            StringBuilder sb = new StringBuilder();
            sb.append(sfOperation);
            sb.append(sfPriceOperationCode);
            sb.append(StringHelper.addSpaces(StringHelper.fromDouble(sfPrice), ORIFieldValueLength.ORDER_CANCEL_SUB_TEXT_PRICE));
            sb.append(StringHelper.addSpaces(sfUserID, ORIFieldValueLength.ORDER_CANCEL_SUB_TEXT_USERID));
            sb.append(StringHelper.addSpaces(sfBrokerID, ORIFieldValueLength.ORDER_CANCEL_SUB_TEXT_BROKERID));
            fText = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
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
                        case ORIDataConst.ORIFieldTag.ORIGCLORDID:
                            setfOrigClOrdID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.ORDERID:
                            setfOrderID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.CLORDID:
                            setfClOrdID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.ACCOUNT:
                            setfAccount(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.CLIENTID:
                            setfClientID(zValue);
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
                        case ORIDataConst.ORIFieldTag.TEXT:
                            setfText(zValue);
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
        //          A2PPPPPPPPPPUUUUUUUUUUUUBBBBBBBBBBBB
        //SEND : 58=A21000                             A
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(ORIDataConst.ORIFieldTag.ORIGCLORDID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrigClOrdID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.ORDERID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.CLORDID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClOrdID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.ACCOUNT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfAccount()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.CLIENTID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClientID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.SYMBOL).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSymbol()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.SYMBOLSFX).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldValue.SYMBOLSFX_PREFIX).append(getfSymbolSfx()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.SECURITYID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSecurityID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.SIDE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSide()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.TRANSACTTIME).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfTransactTime()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.ORDERQTY).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderQty()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.TEXT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR); //.inner.
            sb.append(getfText()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            
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
                setfMsgType(ORIDataConst.ORIMsgType.ORDER_CANCEL_REQUEST);
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
