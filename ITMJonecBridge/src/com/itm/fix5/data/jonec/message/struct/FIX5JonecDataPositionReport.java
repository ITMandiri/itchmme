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
public class FIX5JonecDataPositionReport extends FIX5JonecDataHeader {

    //.reference name: Position Report (AP)

    //.fields:
    private String fPosMaintRptID = "";
    private String fPosReqID = "";
    private String fClearingBusinessDate = "";
    private String fNoPartyIDs = "";
    private String fPartyID = "";
    private String fPartyIDSource = "";
    private String fPartyRole = "";
    private String fNoPosAmt = "";
    private String fPosAmtType = "";
    private String fPosAmt = "";

    public FIX5JonecDataPositionReport(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfPosMaintRptID() {
        return fPosMaintRptID;
    }

    public void setfPosMaintRptID(String fPosMaintRptID) {
        this.fPosMaintRptID = fPosMaintRptID;
    }
    
    public String getfPosReqID() {
        return fPosReqID;
    }

    public void setfPosReqID(String fPosReqID) {
        this.fPosReqID = fPosReqID;
    }
    
    public String getfClearingBusinessDate() {
        return fClearingBusinessDate;
    }

    public void setfClearingBusinessDate(String fClearingBusinessDate) {
        this.fClearingBusinessDate = fClearingBusinessDate;
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
    
    public String getfNoPosAmt() {
        return fNoPosAmt;
    }

    public void setfNoPosAmt(String fNoPosAmt) {
        this.fNoPosAmt = fNoPosAmt;
    }
    
    public String getfPosAmtType() {
        return fPosAmtType;
    }

    public void setfPosAmtType(String fPosAmtType) {
        this.fPosAmtType = fPosAmtType;
    }
    
    public String getfPosAmt() {
        return fPosAmt;
    }

    public void setfPosAmt(String fPosAmt) {
        this.fPosAmt = fPosAmt;
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
                            case FIX5JonecFieldTag.POSMAINTRPTID:
                                setfPosMaintRptID(zValue);
                                break;
                            case FIX5JonecFieldTag.POSREQID:
                                setfPosReqID(zValue);
                                break;
                            case FIX5JonecFieldTag.CLEARINGBUSINESSDATE:
                                setfClearingBusinessDate(zValue);
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
                            case FIX5JonecFieldTag.NOPOSAMT:
                                setfNoPosAmt(zValue);
                                break;
                            case FIX5JonecFieldTag.POSAMTTYPE:
                                setfPosAmtType(zValue);
                                break;
                            case FIX5JonecFieldTag.POSAMT:
                                setfPosAmt(zValue);
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
            sb.append(FIX5JonecFieldTag.POSMAINTRPTID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPosMaintRptID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.POSREQID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPosReqID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.CLEARINGBUSINESSDATE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfClearingBusinessDate()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNoPartyIDs()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyIDSource()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyRole()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.NOPOSAMT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNoPosAmt()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.POSAMTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPosAmtType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.POSAMT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPosAmt()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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