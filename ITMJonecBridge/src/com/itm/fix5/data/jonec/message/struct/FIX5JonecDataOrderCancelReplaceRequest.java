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
public class FIX5JonecDataOrderCancelReplaceRequest extends FIX5JonecDataHeader {

    //.reference name: Order Cancel/Replace Request (a.k.a. Order Modification Request) (G)

    //.fields:
    private String fClOrdID = "";
    private String fOrderID = "";
    private String fOrigClOrdID = "";
    private String fSide = "";
    private String fSymbol = "";
    private String fSecuritySubType = "";
    private String fSecurityID = "";
    private String fNoPartyIDs = "";
    private String fPartyID1 = "";
    private String fPartyIDSource1 = "";
    private String fPartyRole1 = "";
    private String fPartyID2 = "";
    private String fPartyIDSource2 = "";
    private String fPartyRole2 = "";
    private String fAccountType = "";
    private String fExecInst = "";
    private String fOrderQty = "";
    private String fOrdType = "";
    private String fPrice = "";
    private String fTransactTime = "";
    private String fTimeInForce = "";
    private String fText = "";

    public FIX5JonecDataOrderCancelReplaceRequest(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }
    
    public String getfOrderID() {
        return fOrderID;
    }

    public void setfOrderID(String fOrderID) {
        this.fOrderID = fOrderID;
    }
    
    public String getfOrigClOrdID() {
        return fOrigClOrdID;
    }

    public void setfOrigClOrdID(String fOrigClOrdID) {
        this.fOrigClOrdID = fOrigClOrdID;
    }
    
    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
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
    
    public String getfAccountType() {
        return fAccountType;
    }

    public void setfAccountType(String fAccountType) {
        this.fAccountType = fAccountType;
    }
    
    public String getfExecInst() {
        return fExecInst;
    }

    public void setfExecInst(String fExecInst) {
        this.fExecInst = fExecInst;
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
                            case FIX5JonecFieldTag.ORDERID:
                                setfOrderID(zValue);
                                break;
                            case FIX5JonecFieldTag.ORIGCLORDID:
                                setfOrigClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.SIDE:
                                setfSide(zValue);
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
                                break;
                            case FIX5JonecFieldTag.PARTYIDSOURCE:
                                cPartyIDSource++;
                                if (cPartyIDSource == 1) setfPartyIDSource1(zValue);
                                if (cPartyIDSource == 2) setfPartyIDSource2(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYROLE:
                                cPartyRole++;
                                if (cPartyRole == 1) setfPartyRole1(zValue);
                                if (cPartyRole == 2) setfPartyRole2(zValue);
                                break;
                            case FIX5JonecFieldTag.ACCOUNTTYPE:
                                setfAccountType(zValue);
                                break;
                            case FIX5JonecFieldTag.EXECINST:
                                setfExecInst(zValue);
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
                            case FIX5JonecFieldTag.TRANSACTTIME:
                                setfTransactTime(zValue);
                                break;
                            case FIX5JonecFieldTag.TIMEINFORCE:
                                setfTimeInForce(zValue);
                                break;
                            case FIX5JonecFieldTag.TEXT:
                                setfText(zValue);
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
            sb.append(FIX5JonecFieldTag.CLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDERID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORIGCLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrigClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SIDE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSide()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECURITYSUBTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecuritySubType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECURITYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecurityID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNoPartyIDs()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
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
            
            if (!StringHelper.isNullOrEmpty(getfAccountType())){
                sb.append(FIX5JonecFieldTag.ACCOUNTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfAccountType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            sb.append(FIX5JonecFieldTag.EXECINST).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfExecInst()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDERQTY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderQty()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrdType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PRICE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPrice()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TRANSACTTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTransactTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TIMEINFORCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTimeInForce()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TEXT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfText()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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