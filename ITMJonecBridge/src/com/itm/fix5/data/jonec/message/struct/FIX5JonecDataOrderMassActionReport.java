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
public class FIX5JonecDataOrderMassActionReport extends FIX5JonecDataHeader {

    //.reference name: Order Mass Action Report (BZ)

    //.fields:
    private String fClOrdID = "";
    private String fSecondaryClOrdID = "";
    private String fMassActionReportID = "";
    private String fMassActionType = "";
    private String fMassActionScope = "";
    private String fMassActionResponse = "";
    private String fMassActionRejectReason = "";
    private String fTransactTime = "";

    public FIX5JonecDataOrderMassActionReport(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }
    
    public String getfSecondaryClOrdID() {
        return fSecondaryClOrdID;
    }

    public void setfSecondaryClOrdID(String fSecondaryClOrdID) {
        this.fSecondaryClOrdID = fSecondaryClOrdID;
    }
    
    public String getfMassActionReportID() {
        return fMassActionReportID;
    }

    public void setfMassActionReportID(String fMassActionReportID) {
        this.fMassActionReportID = fMassActionReportID;
    }
    
    public String getfMassActionType() {
        return fMassActionType;
    }

    public void setfMassActionType(String fMassActionType) {
        this.fMassActionType = fMassActionType;
    }
    
    public String getfMassActionScope() {
        return fMassActionScope;
    }

    public void setfMassActionScope(String fMassActionScope) {
        this.fMassActionScope = fMassActionScope;
    }
    
    public String getfMassActionResponse() {
        return fMassActionResponse;
    }

    public void setfMassActionResponse(String fMassActionResponse) {
        this.fMassActionResponse = fMassActionResponse;
    }
    
    public String getfMassActionRejectReason() {
        return fMassActionRejectReason;
    }

    public void setfMassActionRejectReason(String fMassActionRejectReason) {
        this.fMassActionRejectReason = fMassActionRejectReason;
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
                            case FIX5JonecFieldTag.CLORDID:
                                setfClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.SECONDARYCLORDID:
                                setfSecondaryClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.MASSACTIONREPORTID:
                                setfMassActionReportID(zValue);
                                break;
                            case FIX5JonecFieldTag.MASSACTIONTYPE:
                                setfMassActionType(zValue);
                                break;
                            case FIX5JonecFieldTag.MASSACTIONSCOPE:
                                setfMassActionScope(zValue);
                                break;
                            case FIX5JonecFieldTag.MASSACTIONRESPONSE:
                                setfMassActionResponse(zValue);
                                break;
                            case FIX5JonecFieldTag.MASSACTIONREJECTREASON:
                                setfMassActionRejectReason(zValue);
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
            sb.append(FIX5JonecFieldTag.CLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECONDARYCLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecondaryClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.MASSACTIONREPORTID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassActionReportID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.MASSACTIONTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassActionType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.MASSACTIONSCOPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassActionScope()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.MASSACTIONRESPONSE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassActionResponse()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.MASSACTIONREJECTREASON).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassActionRejectReason()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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