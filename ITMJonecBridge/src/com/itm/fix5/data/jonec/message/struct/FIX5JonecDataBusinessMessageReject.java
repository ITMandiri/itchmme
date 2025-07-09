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
public class FIX5JonecDataBusinessMessageReject extends FIX5JonecDataHeader {

    //.reference name: Business Message Reject (j)

    //.fields:
    private String fRefSeqNum = "";
    private String fRefMsgType = "";
    private String fBusinessRejectRefID = "";
    private String fBusinessRejectReason = "";
    private String fText = "";

    public FIX5JonecDataBusinessMessageReject(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfRefSeqNum() {
        return fRefSeqNum;
    }

    public void setfRefSeqNum(String fRefSeqNum) {
        this.fRefSeqNum = fRefSeqNum;
    }
    
    public String getfRefMsgType() {
        return fRefMsgType;
    }

    public void setfRefMsgType(String fRefMsgType) {
        this.fRefMsgType = fRefMsgType;
    }
    
    public String getfBusinessRejectRefID() {
        return fBusinessRejectRefID;
    }

    public void setfBusinessRejectRefID(String fBusinessRejectRefID) {
        this.fBusinessRejectRefID = fBusinessRejectRefID;
    }
    
    public String getfBusinessRejectReason() {
        return fBusinessRejectReason;
    }

    public void setfBusinessRejectReason(String fBusinessRejectReason) {
        this.fBusinessRejectReason = fBusinessRejectReason;
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
            Map<String, ArrayList<String>> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    ///String zValue = mapFields.get(zKey);
                    for(String zValue : mapFields.get(zKey)){
                        switch(zKey){
                            case FIX5JonecFieldTag.REFSEQNUM:
                                setfRefSeqNum(zValue);
                                break;
                            case FIX5JonecFieldTag.REFMSGTYPE:
                                setfRefMsgType(zValue);
                                break;
                            case FIX5JonecFieldTag.BUSINESSREJECTREFID:
                                setfBusinessRejectRefID(zValue);
                                break;
                            case FIX5JonecFieldTag.BUSINESSREJECTREASON:
                                setfBusinessRejectReason(zValue);
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
            sb.append(FIX5JonecFieldTag.REFSEQNUM).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfRefSeqNum()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.REFMSGTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfRefMsgType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.BUSINESSREJECTREFID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfBusinessRejectRefID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.BUSINESSREJECTREASON).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfBusinessRejectReason()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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