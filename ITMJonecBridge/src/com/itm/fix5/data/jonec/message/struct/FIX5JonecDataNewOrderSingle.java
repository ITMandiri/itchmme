/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.message.struct;

import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldFmt;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldTag;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.Map;


/**
 *
 * @author aripam
 */
public class FIX5JonecDataNewOrderSingle extends FIX5JonecDataHeader {

    //.reference name: New Order - Single (D)

    //.fields:
    private String fClOrdID = "";
    private String fSymbol = "";
    private String fSecuritySubType = "";
    private String fSecurityID = "";
    private String fNoPartyIDs = "";
    private String fPartyID1 = "";
    private String fPartyIDSource1 = "";
    private String fPartyRole1 = ""; //.PARTY_ROLE_12_EXECUTING_TRADER
    private String fPartyID2 = "";
    private String fPartyIDSource2 = "";
    private String fPartyRole2 = ""; //.PARTY_ROLE_1_EXECUTING_FIRM
    private String fPartyID3 = "";
    private String fPartyIDSource3 = "";
    private String fPartyRole3 = ""; //.PARTY_ROLE_24_CUSTOMER_ACCOUNT
    private String fAccountType = "";
    private String fOrderRestrictions = "";
    private String fExecInst = "";
    private String fHandlInst = "";
    private String fOrderQty = "";
    private String fOrdType = "";
    private String fPrice = "";
    private String fSide = "";
    private String fTransactTime = "";
    private String fTimeInForce = "";
    private String fText = "";
    private String fNoPartySubIDs = "";
    private String fPartySubID = "";
    private String fPartySubIDType = "";
    //...
    private String fQuoteId = "";
    private String fQuoteType = "";
    private String fPrivateQuote = "";
    private String fSingleQuoteIndicator = "";
    private String fSecurityIDSource = "";
    private String fSettlMethod = "";
    private String fBidPx = "";
    private String fOfferPx = "";
    private String fOrderCapacity = "";

    public FIX5JonecDataNewOrderSingle(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }
    
    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }
    
    public String getfSecuritySubType() {
        return fSecuritySubType;
    }

    public void setfSecuritySubType(String fSecuritySubType) {
        this.fSecuritySubType = fSecuritySubType;
    }
    
    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
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
    
    public String getfAccountType() {
        return fAccountType;
    }

    public void setfAccountType(String fAccountType) {
        this.fAccountType = fAccountType;
    }
    
    public String getfOrderRestrictions() {
        return fOrderRestrictions;
    }

    public void setfOrderRestrictions(String fOrderRestrictions) {
        this.fOrderRestrictions = fOrderRestrictions;
    }
    
    public String getfExecInst() {
        return fExecInst;
    }

    public void setfExecInst(String fExecInst) {
        this.fExecInst = fExecInst;
    }
    
    public String getfHandlInst() {
        return fHandlInst;
    }

    public void setfHandlInst(String fHandlInst) {
        this.fHandlInst = fHandlInst;
    }
    
    public String getfOrderQty() {
        return fOrderQty;
    }

    public void setfOrderQty(String fOrderQty) {
        this.fOrderQty = fOrderQty;
    }
    
    public String getfOrdType() {
        return fOrdType;
    }

    public void setfOrdType(String fOrdType) {
        this.fOrdType = fOrdType;
    }
    
    public String getfPrice() {
        return fPrice;
    }

    public void setfPrice(String fPrice) {
        this.fPrice = fPrice;
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
    
    public String getfTimeInForce() {
        return fTimeInForce;
    }

    public void setfTimeInForce(String fTimeInForce) {
        this.fTimeInForce = fTimeInForce;
    }
    
    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
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

    public String getfQuoteId() {
        return fQuoteId;
    }

    public void setfQuoteId(String fQuoteId) {
        this.fQuoteId = fQuoteId;
    }

    public String getfQuoteType() {
        return fQuoteType;
    }

    public void setfQuoteType(String fQuoteType) {
        this.fQuoteType = fQuoteType;
    }

    public String getfPrivateQuote() {
        return fPrivateQuote;
    }

    public void setfPrivateQuote(String fPrivateQuote) {
        this.fPrivateQuote = fPrivateQuote;
    }

    public String getfSingleQuoteIndicator() {
        return fSingleQuoteIndicator;
    }

    public void setfSingleQuoteIndicator(String fSingleQuoteIndicator) {
        this.fSingleQuoteIndicator = fSingleQuoteIndicator;
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

    public String getfBidPx() {
        return fBidPx;
    }

    public void setfBidPx(String fBidPx) {
        this.fBidPx = fBidPx;
    }

    public String getfOfferPx() {
        return fOfferPx;
    }

    public void setfOfferPx(String fOfferPx) {
        this.fOfferPx = fOfferPx;
    }

    public String getfOrderCapacity() {
        return fOrderCapacity;
    }

    public void setfOrderCapacity(String fOrderCapacity) {
        this.fOrderCapacity = fOrderCapacity;
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
                            case FIX5JonecFieldTag.CLORDID:
                                setfClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.SYMBOL:
                                setfSymbol(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYSUBTYPE:
                                setfSecuritySubType(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYID:
                                setfSecurityID(zValue);
                                break;
                            case FIX5JonecFieldTag.NOPARTYIDS:
                                setfNoPartyIDs(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYID:
                                cPartyID++;
                                if (cPartyID == 1) setfPartyID1(zValue);
                                if (cPartyID == 2) setfPartyID2(zValue);
                                if (cPartyID == 3) setfPartyID3(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYIDSOURCE:
                                cPartyIDSource++;
                                if (cPartyIDSource == 1) setfPartyIDSource1(zValue);
                                if (cPartyIDSource == 2) setfPartyIDSource2(zValue);
                                if (cPartyIDSource == 3) setfPartyIDSource3(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYROLE:
                                cPartyRole++;
                                if (cPartyRole == 1) setfPartyRole1(zValue);
                                if (cPartyRole == 2) setfPartyRole2(zValue);
                                if (cPartyRole == 3) setfPartyRole3(zValue);
                                break;
                            case FIX5JonecFieldTag.ACCOUNTTYPE:
                                setfAccountType(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDERRESTRICTIONS:
                                setfOrderRestrictions(zValue);
                                break;
                            case FIX5JonecFieldTag.EXECINST:
                                setfExecInst(zValue);
                                break;
                            case FIX5JonecFieldTag.HANDLINST:
                                setfHandlInst(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDERQTY:
                                setfOrderQty(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDTYPE:
                                setfOrdType(zValue);
                                break;
                            case FIX5JonecFieldTag.PRICE:
                                setfPrice(zValue);
                                break;
                            case FIX5JonecFieldTag.SIDE:
                                setfSide(zValue);
                                break;
                            case FIX5JonecFieldTag.TRANSACTTIME:
                                setfTransactTime(zValue);
                                break;
                            case FIX5JonecFieldTag.TIMEINFORCE:
                                setfTimeInForce(zValue);
                                break;
                            case FIX5JonecFieldTag.TEXT:
                                setfText(zValue);
                                break;
                            case FIX5JonecFieldTag.NOPARTYSUBIDS:
                                setfNoPartySubIDs(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYSUBID:
                                setfPartySubID(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYSUBIDTYPE:
                                setfPartySubIDType(zValue);
                                break;
                            //...
                            case FIX5JonecFieldTag.QUOTEID:
                                setfQuoteId(zValue);
                                break;
                            case FIX5JonecFieldTag.QUOTETYPE:
                                setfQuoteType(zValue);
                                break;
                            case FIX5JonecFieldTag.PRIVATEQUOTE:
                                setfPrivateQuote(zValue);
                                break;
                            case FIX5JonecFieldTag.SINGLEQUOTEINDICATOR:
                                setfSingleQuoteIndicator(zValue);
                                break;
                            case FIX5JonecFieldTag.IDSOURCE:
                                setfSecurityIDSource(zValue);
                                break;
                            case FIX5JonecFieldTag.SETTLMETHOD:
                                setfSettlMethod(zValue);
                                break;
                            case FIX5JonecFieldTag.BIDPX:
                                setfBidPx(zValue);
                                break;
                            case FIX5JonecFieldTag.OFFERPX:
                                setfOfferPx(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDERCAPACITY:
                                setfOrderCapacity(zValue);
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
            if (!StringHelper.isNullOrEmpty(getfQuoteId())){
                sb.append(FIX5JonecFieldTag.QUOTEID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteId()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfQuoteType())){
                sb.append(FIX5JonecFieldTag.QUOTETYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPrivateQuote())){
                sb.append(FIX5JonecFieldTag.PRIVATEQUOTE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPrivateQuote()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSingleQuoteIndicator())){
                sb.append(FIX5JonecFieldTag.SINGLEQUOTEINDICATOR).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSingleQuoteIndicator()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfClOrdID())){
                sb.append(FIX5JonecFieldTag.CLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            sb.append(FIX5JonecFieldTag.SYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
//            sb.append(FIX5JonecFieldTag.SECURITYSUBTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
//            sb.append(getfSecuritySubType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            if (!StringHelper.isNullOrEmpty(getfSecurityID())){
                sb.append(FIX5JonecFieldTag.SECURITYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfOrderQty())){
                sb.append(FIX5JonecFieldTag.ORDERQTY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrderQty()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfOrdType())){
                sb.append(FIX5JonecFieldTag.ORDTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrdType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPrice())){
                sb.append(FIX5JonecFieldTag.PRICE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPrice()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            sb.append(FIX5JonecFieldTag.SIDE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSide()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TRANSACTTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTransactTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            if (!StringHelper.isNullOrEmpty(getfTimeInForce())){
                sb.append(FIX5JonecFieldTag.TIMEINFORCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTimeInForce()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfNoPartyIDs())){
                sb.append(FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartyIDs()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyID1())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource1())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyRole1())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyID2())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource2())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyRole2())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            //...
            if (!StringHelper.isNullOrEmpty(getfNoPartySubIDs())){
                sb.append(FIX5JonecFieldTag.NOPARTYSUBIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartySubIDs()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartySubID())){
                sb.append(FIX5JonecFieldTag.PARTYSUBID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartySubID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartySubIDType())){
                sb.append(FIX5JonecFieldTag.PARTYSUBIDTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartySubIDType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            //...
            if (!StringHelper.isNullOrEmpty(getfPartyID3())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID3()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource3())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource3()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfPartyRole3())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole3()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfAccountType())){
                sb.append(FIX5JonecFieldTag.ACCOUNTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfAccountType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
//            sb.append(FIX5JonecFieldTag.ORDERRESTRICTIONS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
//            sb.append(getfOrderRestrictions()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
//            sb.append(FIX5JonecFieldTag.EXECINST).append(FIX5JonecFieldFmt.KV_SEPARATOR);
//            sb.append(getfExecInst()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
//            sb.append(FIX5JonecFieldTag.HANDLINST).append(FIX5JonecFieldFmt.KV_SEPARATOR);
//            sb.append(getfHandlInst()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            if (!StringHelper.isNullOrEmpty(getfSecurityIDSource())){
                sb.append(FIX5JonecFieldTag.IDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityIDSource()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSettlMethod())){
                sb.append(FIX5JonecFieldTag.SETTLMETHOD).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSettlMethod()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfBidPx())){
                sb.append(FIX5JonecFieldTag.BIDPX).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfBidPx()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfOfferPx())){
                sb.append(FIX5JonecFieldTag.OFFERPX).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOfferPx()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfOrderCapacity())){
                sb.append(FIX5JonecFieldTag.ORDERCAPACITY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrderCapacity()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfText())){
                sb.append(FIX5JonecFieldTag.TEXT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfText()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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