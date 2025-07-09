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
public class FIX5JonecDataRequestforPositions extends FIX5JonecDataHeader {

    //.reference name: Request for Positions (AN)

    //.fields:
    private String fPosReqID = "";
    private String fPosReqType = "";
    private String fSubscriptionRequestType = "";
    private String fNoPartyIDs = "";
    private String fPartyID = "";
    private String fPartyIDSource = "";
    private String fPartyRole = "";
    private String fClearingBusinessDate = "";
    private String fTransactTime = "";

    public FIX5JonecDataRequestforPositions(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfPosReqID() {
        return fPosReqID;
    }

    public void setfPosReqID(String fPosReqID) {
        this.fPosReqID = fPosReqID;
    }
    
    public String getfPosReqType() {
        return fPosReqType;
    }

    public void setfPosReqType(String fPosReqType) {
        this.fPosReqType = fPosReqType;
    }
    
    public String getfSubscriptionRequestType() {
        return fSubscriptionRequestType;
    }

    public void setfSubscriptionRequestType(String fSubscriptionRequestType) {
        this.fSubscriptionRequestType = fSubscriptionRequestType;
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
    
    public String getfClearingBusinessDate() {
        return fClearingBusinessDate;
    }

    public void setfClearingBusinessDate(String fClearingBusinessDate) {
        this.fClearingBusinessDate = fClearingBusinessDate;
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
            Map<String, ArrayList<String>> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    ///String zValue = mapFields.get(zKey);
                    for(String zValue : mapFields.get(zKey)){
                        switch(zKey){
                            case FIX5JonecFieldTag.POSREQID:
                                setfPosReqID(zValue);
                                break;
                            case FIX5JonecFieldTag.POSREQTYPE:
                                setfPosReqType(zValue);
                                break;
                            case FIX5JonecFieldTag.SUBSCRIPTIONREQUESTTYPE:
                                setfSubscriptionRequestType(zValue);
                                break;
                            case FIX5JonecFieldTag.NOPARTYIDS:
                                setfNoPartyIDs(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYID:
                                setfPartyID(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYIDSOURCE:
                                setfPartyIDSource(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYROLE:
                                setfPartyRole(zValue);
                                break;
                            case FIX5JonecFieldTag.CLEARINGBUSINESSDATE:
                                setfClearingBusinessDate(zValue);
                                break;
                            case FIX5JonecFieldTag.TRANSACTTIME:
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
            sb.append(FIX5JonecFieldTag.POSREQID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPosReqID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.POSREQTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPosReqType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SUBSCRIPTIONREQUESTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSubscriptionRequestType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNoPartyIDs()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyIDSource()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyRole()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.CLEARINGBUSINESSDATE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfClearingBusinessDate()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TRANSACTTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTransactTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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