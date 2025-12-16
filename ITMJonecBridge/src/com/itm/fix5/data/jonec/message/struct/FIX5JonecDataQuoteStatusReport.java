/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.message.struct;

import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author fredy
 */
public class FIX5JonecDataQuoteStatusReport extends FIX5JonecDataHeader {
    private String fQuoteID = ""; //.
    private String fQuoteRespID = ""; //.
    private String fQuoteType = ""; //.
    private String fNoPartyIDs = ""; //.
    private String fPartyID = ""; //.
    private String fPartyIDSource = ""; //.
    private String fPartyRole = ""; //.
    private String fSymbol = ""; //.
    private String fSecurityID = ""; //.
    private String fSecurityIDSource = ""; //.
    private String fSide = ""; //.
    private String fOrderQty = ""; //.
    private String fPrice = ""; //.
    private String fQuoteStatus = ""; //.
    private String fQuoteRejectReason = ""; //.
    private String fRejectText = ""; //.    

    public FIX5JonecDataQuoteStatusReport(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfQuoteID() {
        return fQuoteID;
    }

    public void setfQuoteID(String fQuoteID) {
        this.fQuoteID = fQuoteID;
    }

    public String getfQuoteRespID() {
        return fQuoteRespID;
    }

    public void setfQuoteRespID(String fQuoteRespID) {
        this.fQuoteRespID = fQuoteRespID;
    }

    public String getfQuoteType() {
        return fQuoteType;
    }

    public void setfQuoteType(String fQuoteType) {
        this.fQuoteType = fQuoteType;
    }

    public String getfNoPartyIDs() {
        return fNoPartyIDs;
    }

    public void setfNoPartyIDs(String fNoPartyIDs) {
        this.fNoPartyIDs = fNoPartyIDs;
    }

    public String getfPartyID() {
        return fPartyID;
    }

    public void setfPartyID(String fPartyID) {
        this.fPartyID = fPartyID;
    }

    public String getfPartyIDSource() {
        return fPartyIDSource;
    }

    public void setfPartyIDSource(String fPartyIDSource) {
        this.fPartyIDSource = fPartyIDSource;
    }

    public String getfPartyRole() {
        return fPartyRole;
    }

    public void setfPartyRole(String fPartyRole) {
        this.fPartyRole = fPartyRole;
    }

    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }

    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }

    public String getfSecurityIDSource() {
        return fSecurityIDSource;
    }

    public void setfSecurityIDSource(String fSecurityIDSource) {
        this.fSecurityIDSource = fSecurityIDSource;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }

    public String getfOrderQty() {
        return fOrderQty;
    }

    public void setfOrderQty(String fOrderQty) {
        this.fOrderQty = fOrderQty;
    }

    public String getfPrice() {
        return fPrice;
    }

    public void setfPrice(String fPrice) {
        this.fPrice = fPrice;
    }

    public String getfQuoteStatus() {
        return fQuoteStatus;
    }

    public void setfQuoteStatus(String fQuoteStatus) {
        this.fQuoteStatus = fQuoteStatus;
    }

    public String getfQuoteRejectReason() {
        return fQuoteRejectReason;
    }

    public void setfQuoteRejectReason(String fQuoteRejectReason) {
        this.fQuoteRejectReason = fQuoteRejectReason;
    }

    public String getfRejectText() {
        return fRejectText;
    }

    public void setfRejectText(String fRejectText) {
        this.fRejectText = fRejectText;
    }
    
    //.process:
    public boolean assignMessage(){
        boolean bOut = false;
        try{
            //.assign header:
            assignHeaderMessage();
            //.assign data:
            
            Map<String, ArrayList<String>> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    ///String zValue = mapFields.get(zKey);
                    for(String zValue : mapFields.get(zKey)){
                        switch(zKey){
                            case FIX5JonecDataConst.FIX5JonecFieldTag.QUOTEID:
                                setfQuoteID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.QUOTERESPID:
                                setfQuoteRespID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.QUOTETYPE:
                                setfQuoteType(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.NOPARTYIDS:
                                setfNoPartyIDs(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PARTYID:
                                setfPartyID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PARTYIDSOURCE:
                                setfPartyIDSource(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PARTYROLE:
                                setfPartyRole(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.SYMBOL:
                                setfSymbol(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.SECURITYID:
                                setfSecurityID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.IDSOURCE:
                                setfSecurityIDSource(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.SIDE:
                                setfSide(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.ORDERQTY:
                                setfOrderQty(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PRICE:
                                setfPrice(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.QUOTESTATUS:
                                setfQuoteStatus(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.QUOTEREJECTREASON:
                                setfQuoteRejectReason(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.REJECTTEXT:
                                setfRejectText(zValue);
                                break;
                            default:
                                break;
                        }
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
            if (!StringHelper.isNullOrEmpty(getfQuoteID())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.QUOTEID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfQuoteRespID())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.QUOTERESPID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteRespID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfQuoteType())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.QUOTETYPE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteType()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfNoPartyIDs())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartyIDs()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyID())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyRole())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSymbol())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.SYMBOL).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSymbol()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecurityID())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.SECURITYID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecurityIDSource())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.IDSOURCE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityIDSource()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSide())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.SIDE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSide()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfOrderQty())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.ORDERQTY).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrderQty()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPrice())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PRICE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPrice()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            } 
            if (!StringHelper.isNullOrEmpty(getfQuoteStatus())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.QUOTESTATUS).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteStatus()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfQuoteRejectReason())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.QUOTEREJECTREASON).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteRejectReason()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfRejectText())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.REJECTTEXT).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfRejectText()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }   
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
                
            }
            
            sb.append(msgHeaderToString());
            sb.append(msgDataToString());
            sb.append(msgTrailerToString());
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
}
