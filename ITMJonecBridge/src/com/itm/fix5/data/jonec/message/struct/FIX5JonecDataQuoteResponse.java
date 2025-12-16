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
public class FIX5JonecDataQuoteResponse extends FIX5JonecDataHeader {
    private String fQuoteRespID = ""; //.
    private String fQuoteID = ""; //.
    private String fQuoteRespType = ""; //.
    private String fOrderCapacity = ""; //.
    private String fNoPartyIDs = ""; //.
    private String fPartyID1 = "";
    private String fPartyIDSource1 = "";
    private String fPartyRole1 = ""; //.PARTY_ROLE_12_EXECUTING_TRADER
    private String fPartyID2 = "";
    private String fPartyIDSource2 = "";
    private String fPartyRole2 = ""; //.PARTY_ROLE_1_EXECUTING_FIRM
    private String fPartyID3 = "";
    private String fPartyIDSource3 = "";
    private String fPartyRole3 = ""; //.PARTY_ROLE_24_CUSTOMER_ACCOUNT
    private String fNoPartySubIDs = ""; //.
    private String fPartySubID = ""; //.
    private String fPartySubIDType = ""; //.
    private String fSymbol = ""; //.
    private String fSecurityID = ""; //.
    private String fSecurityIDSource = ""; //.
    private String fSettlMethod = ""; //.
    private String fOrderQty = ""; //.
    private String fSide = ""; //.
    private String fOrdType = ""; //.
    private String fText = ""; //.
    private String fPrice = ""; //.
    private String fValidUntilTime = ""; //.
    private String fTransactTime = ""; //.
    

    public FIX5JonecDataQuoteResponse(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    
    public String getfQuoteRespID() {
        return fQuoteRespID;
    }

    public void setfQuoteRespID(String fQuoteRespID) {
        this.fQuoteRespID = fQuoteRespID;
    }

    public String getfQuoteID() {
        return fQuoteID;
    }

    public void setfQuoteID(String fQuoteID) {
        this.fQuoteID = fQuoteID;
    }

    public String getfQuoteRespType() {
        return fQuoteRespType;
    }

    public void setfQuoteRespType(String fQuoteRespType) {
        this.fQuoteRespType = fQuoteRespType;
    }

    public String getfOrderCapacity() {
        return fOrderCapacity;
    }

    public void setfOrderCapacity(String fOrderCapacity) {
        this.fOrderCapacity = fOrderCapacity;
    }

    public String getfNoPartyIDs() {
        return fNoPartyIDs;
    }

    public void setfNoPartyIDs(String fNoPartyIDs) {
        this.fNoPartyIDs = fNoPartyIDs;
    }
    
    public String getfPartyID1() {
        return fPartyID1;
    }

    public void setfPartyID1(String fPartyID1) {
        this.fPartyID1 = fPartyID1;
    }
    
    public String getfPartyIDSource1() {
        return fPartyIDSource1;
    }

    public void setfPartyIDSource1(String fPartyIDSource1) {
        this.fPartyIDSource1 = fPartyIDSource1;
    }
    
    public String getfPartyRole1() {
        return fPartyRole1;
    }

    public void setfPartyRole1(String fPartyRole1) {
        this.fPartyRole1 = fPartyRole1;
    }
    
    public String getfPartyID2() {
        return fPartyID2;
    }

    public void setfPartyID2(String fPartyID2) {
        this.fPartyID2 = fPartyID2;
    }
    
    public String getfPartyIDSource2() {
        return fPartyIDSource2;
    }

    public void setfPartyIDSource2(String fPartyIDSource2) {
        this.fPartyIDSource2 = fPartyIDSource2;
    }
    
    public String getfPartyRole2() {
        return fPartyRole2;
    }

    public void setfPartyRole2(String fPartyRole2) {
        this.fPartyRole2 = fPartyRole2;
    }

    public String getfPartyID3() {
        return fPartyID3;
    }

    public void setfPartyID3(String fPartyID3) {
        this.fPartyID3 = fPartyID3;
    }

    public String getfPartyIDSource3() {
        return fPartyIDSource3;
    }

    public void setfPartyIDSource3(String fPartyIDSource3) {
        this.fPartyIDSource3 = fPartyIDSource3;
    }

    public String getfPartyRole3() {
        return fPartyRole3;
    }

    public void setfPartyRole3(String fPartyRole3) {
        this.fPartyRole3 = fPartyRole3;
    }

    public String getfNoPartySubIDs() {
        return fNoPartySubIDs;
    }

    public void setfNoPartySubIDs(String fNoPartySubIDs) {
        this.fNoPartySubIDs = fNoPartySubIDs;
    }

    public String getfPartySubID() {
        return fPartySubID;
    }

    public void setfPartySubID(String fPartySubID) {
        this.fPartySubID = fPartySubID;
    }

    public String getfPartySubIDType() {
        return fPartySubIDType;
    }

    public void setfPartySubIDType(String fPartySubIDType) {
        this.fPartySubIDType = fPartySubIDType;
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

    public String getfSettlMethod() {
        return fSettlMethod;
    }

    public void setfSettlMethod(String fSettlMethod) {
        this.fSettlMethod = fSettlMethod;
    }

    public String getfOrderQty() {
        return fOrderQty;
    }

    public void setfOrderQty(String fOrderQty) {
        this.fOrderQty = fOrderQty;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }

    public String getfOrdType() {
        return fOrdType;
    }

    public void setfOrdType(String fOrdType) {
        this.fOrdType = fOrdType;
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }

    public String getfPrice() {
        return fPrice;
    }

    public void setfPrice(String fPrice) {
        this.fPrice = fPrice;
    }

    public String getfValidUntilTime() {
        return fValidUntilTime;
    }

    public void setfValidUntilTime(String fValidUntilTime) {
        this.fValidUntilTime = fValidUntilTime;
    }

    public String getfTransactTime() {
        return fTransactTime;
    }

    public void setfTransactTime(String fTransactTime) {
        this.fTransactTime = fTransactTime;
    }
    
    //.process:
    public boolean assignMessage(){
        boolean bOut = false;
        try{
            //.assign header:
            assignHeaderMessage();
            //.assign data:
            int cPartyID = 0;
            int cPartyIDSource = 0;
            int cPartyRole = 0;
            Map<String, ArrayList<String>> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    ///String zValue = mapFields.get(zKey);
                    for(String zValue : mapFields.get(zKey)){
                        switch(zKey){
                            case FIX5JonecDataConst.FIX5JonecFieldTag.QUOTERESPID:
                                setfQuoteRespID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.QUOTEID:
                                setfQuoteID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.QUOTERESPTYPE:
                                setfQuoteRespType(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.ORDERCAPACITY:
                                setfOrderCapacity(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.NOPARTYIDS:
                                setfNoPartyIDs(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PARTYID:
                                cPartyID++;
                                if (cPartyID == 1) setfPartyID1(zValue);
                                if (cPartyID == 2) setfPartyID2(zValue);
                                if (cPartyID == 3) setfPartyID3(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PARTYIDSOURCE:
                                cPartyIDSource++;
                                if (cPartyIDSource == 1) setfPartyIDSource1(zValue);
                                if (cPartyIDSource == 2) setfPartyIDSource2(zValue);
                                if (cPartyIDSource == 3) setfPartyIDSource3(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PARTYROLE:
                                cPartyRole++;
                                if (cPartyRole == 1) setfPartyRole1(zValue);
                                if (cPartyRole == 2) setfPartyRole2(zValue);
                                if (cPartyRole == 3) setfPartyRole3(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.NOPARTYSUBIDS:
                                setfNoPartySubIDs(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PARTYSUBID:
                                setfPartySubID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PARTYSUBIDTYPE:
                                setfPartySubIDType(zValue);
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
                            case FIX5JonecDataConst.FIX5JonecFieldTag.SETTLMETHOD:
                                setfSettlMethod(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.ORDERQTY:
                                setfOrderQty(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.SIDE:
                                setfSide(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.ORDTYPE:
                                setfOrdType(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.TEXT:
                                setfText(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.PRICE:
                                setfPrice(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.VALIDUNTILTIME:
                                setfValidUntilTime(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.TRANSACTTIME:
                                setfTransactTime(zValue);
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
            if (!StringHelper.isNullOrEmpty(getfQuoteRespID())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.QUOTERESPID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteRespID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfQuoteID())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.QUOTEID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfQuoteRespType())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.QUOTERESPTYPE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteRespType()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfOrderCapacity())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.ORDERCAPACITY).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrderCapacity()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfNoPartyIDs())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartyIDs()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyID1())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID1()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource1())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource1()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyRole1())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole1()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyID2())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID2()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource2())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource2()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyRole2())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole2()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            //...
            if (!StringHelper.isNullOrEmpty(getfNoPartySubIDs())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.NOPARTYSUBIDS).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartySubIDs()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartySubID())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYSUBID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartySubID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartySubIDType())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYSUBIDTYPE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartySubIDType()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            //...
            if (!StringHelper.isNullOrEmpty(getfPartyID3())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID3()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource3())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource3()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyRole3())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole3()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
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
            if (!StringHelper.isNullOrEmpty(getfSettlMethod())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.SETTLMETHOD).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSettlMethod()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfOrderQty())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.ORDERQTY).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrderQty()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSide())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.SIDE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSide()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfOrdType())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.ORDTYPE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrdType()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfText())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.TEXT).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfText()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPrice())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PRICE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPrice()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }   
            if (!StringHelper.isNullOrEmpty(getfValidUntilTime())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.VALIDUNTILTIME).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfValidUntilTime()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }     
            if (!StringHelper.isNullOrEmpty(getfTransactTime())){
                sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.TRANSACTTIME).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTransactTime()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
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
