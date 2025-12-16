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
public class FIX5JonecDataOrderMassStatusRequest extends FIX5JonecDataHeader {
    //.reference name: OrderMassStatusRequest (AF)

    //.fields:
    private String fLastMsgSeqNumProcessed = "";
    private String fMassStatusReqID = "";
    private String fMassStatusReqType = "";
    private String fNoPartyIDs = "";
    private String fPartyID = "";
    private String fPartyIDSource = "";
    private String fPartyRole = "";

    public FIX5JonecDataOrderMassStatusRequest(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfLastMsgSeqNumProcessed() {
        return fLastMsgSeqNumProcessed;
    }

    public void setfLastMsgSeqNumProcessed(String fLastMsgSeqNumProcessed) {
        this.fLastMsgSeqNumProcessed = fLastMsgSeqNumProcessed;
    }

    public String getfMassStatusReqID() {
        return fMassStatusReqID;
    }

    public void setfMassStatusReqID(String fMassStatusReqID) {
        this.fMassStatusReqID = fMassStatusReqID;
    }

    public String getfMassStatusReqType() {
        return fMassStatusReqType;
    }

    public void setfMassStatusReqType(String fMassStatusReqType) {
        this.fMassStatusReqType = fMassStatusReqType;
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
                            case FIX5JonecDataConst.FIX5JonecFieldTag.LASTMSGSEQNUMPROCESSED:
                                setfLastMsgSeqNumProcessed(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.MASSSTATUSREQID:
                                setfMassStatusReqID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.MASSSTATUSREQTYPE:
                                setfMassStatusReqType(zValue);
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
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.LASTMSGSEQNUMPROCESSED).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfLastMsgSeqNumProcessed()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.MASSSTATUSREQID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassStatusReqID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.MASSSTATUSREQTYPE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassStatusReqType()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNoPartyIDs()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyIDSource()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyRole()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
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
