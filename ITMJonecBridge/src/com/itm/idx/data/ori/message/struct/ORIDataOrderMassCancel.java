/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataOrderMassCancel extends ORIDataHeader {
    
    //.fields:
    private String fClOrdID                                     = "";
    private String fMassWithdrawRequestType                     = "";
    private String fSymbol                                      = "";
    private String fSymbolSfx                                   = "";
    private String fSecurityID                                  = "";
    private String fTradingSessionID                            = "";
    private String fSide                                        = "";
    //.permintaan tambahan:
    private String fClientID                                    = "";
    
    public ORIDataOrderMassCancel(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }

    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }

    public String getfMassWithdrawRequestType() {
        return fMassWithdrawRequestType;
    }

    public void setfMassWithdrawRequestType(String fMassWithdrawRequestType) {
        this.fMassWithdrawRequestType = fMassWithdrawRequestType;
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

    public String getfTradingSessionID() {
        return fTradingSessionID;
    }

    public void setfTradingSessionID(String fTradingSessionID) {
        this.fTradingSessionID = fTradingSessionID;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }

    public String getfClientID() {
        return fClientID;
    }

    public void setfClientID(String fClientID) {
        this.fClientID = fClientID;
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
                        case ORIDataConst.ORIFieldTag.MASSWITHDRAWREQUESTTYPE:
                            setfMassWithdrawRequestType(zValue);
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
                        case ORIDataConst.ORIFieldTag.TRADINGSESSIONID:
                            setfTradingSessionID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SIDE:
                            setfSide(zValue);
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
            sb.append(ORIDataConst.ORIFieldTag.CLORDID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClOrdID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.MASSWITHDRAWREQUESTTYPE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfMassWithdrawRequestType()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            if (!StringHelper.isNullOrEmpty(getfSymbol())){
                sb.append(ORIDataConst.ORIFieldTag.SYMBOL).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                sb.append(getfSymbol()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                sb.append(ORIDataConst.ORIFieldTag.SECURITYID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSymbolSfx())){
                sb.append(ORIDataConst.ORIFieldTag.SYMBOLSFX).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                sb.append(ORIDataConst.ORIFieldValue.SYMBOLSFX_PREFIX).append(getfSymbolSfx()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            }
            sb.append(ORIDataConst.ORIFieldTag.TRADINGSESSIONID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfTradingSessionID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.SIDE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfSide()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            //.permintaan tambahan:
            sb.append(ORIDataConst.ORIFieldTag.CLIENTID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClientID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            
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
                setfMsgType(ORIDataConst.ORIMsgType.ORDER_MASS_CANCEL_REQUEST);
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
