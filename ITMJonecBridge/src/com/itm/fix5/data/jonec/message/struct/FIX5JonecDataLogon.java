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
public class FIX5JonecDataLogon extends FIX5JonecDataHeader {

    //.reference name: Logon (A)

    //.fields:
    private String fEncryptMethod = "";
    private String fHeartBtInt = "";
    private String fResetSeqNumFlag = "";
    private String fUsername = "";
    private String fPassword = "";
    private String fNewPassword = "";
    private String fText = "";
    private String fDefaultApplVerID = "";

    public FIX5JonecDataLogon(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfEncryptMethod() {
        return fEncryptMethod;
    }

    public void setfEncryptMethod(String fEncryptMethod) {
        this.fEncryptMethod = fEncryptMethod;
    }
    
    public String getfHeartBtInt() {
        return fHeartBtInt;
    }

    public void setfHeartBtInt(String fHeartBtInt) {
        this.fHeartBtInt = fHeartBtInt;
    }
    
    public String getfResetSeqNumFlag() {
        return fResetSeqNumFlag;
    }

    public void setfResetSeqNumFlag(String fResetSeqNumFlag) {
        this.fResetSeqNumFlag = fResetSeqNumFlag;
    }
    
    public String getfUsername() {
        return fUsername;
    }

    public void setfUsername(String fUsername) {
        this.fUsername = fUsername;
    }
    
    public String getfPassword() {
        return fPassword;
    }

    public void setfPassword(String fPassword) {
        this.fPassword = fPassword;
    }
    
    public String getfNewPassword() {
        return fNewPassword;
    }

    public void setfNewPassword(String fNewPassword) {
        this.fNewPassword = fNewPassword;
    }
    
    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }
    
    public String getfDefaultApplVerID() {
        return fDefaultApplVerID;
    }

    public void setfDefaultApplVerID(String fDefaultApplVerID) {
        this.fDefaultApplVerID = fDefaultApplVerID;
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
                            case FIX5JonecFieldTag.ENCRYPTMETHOD:
                                setfEncryptMethod(zValue);
                                break;
                            case FIX5JonecFieldTag.HEARTBTINT:
                                setfHeartBtInt(zValue);
                                break;
                            case FIX5JonecFieldTag.RESETSEQNUMFLAG:
                                setfResetSeqNumFlag(zValue);
                                break;
                            case FIX5JonecFieldTag.USERNAME:
                                setfUsername(zValue);
                                break;
                            case FIX5JonecFieldTag.PASSWORD:
                                setfPassword(zValue);
                                break;
                            case FIX5JonecFieldTag.NEWPASSWORD:
                                setfNewPassword(zValue);
                                break;
                            case FIX5JonecFieldTag.TEXT:
                                setfText(zValue);
                                break;
                            case FIX5JonecFieldTag.DEFAULTAPPLVERID:
                                setfDefaultApplVerID(zValue);
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
            sb.append(FIX5JonecFieldTag.ENCRYPTMETHOD).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfEncryptMethod()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.HEARTBTINT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfHeartBtInt()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            if (!StringHelper.isNullOrEmpty(getfResetSeqNumFlag())){
                sb.append(FIX5JonecFieldTag.RESETSEQNUMFLAG).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfResetSeqNumFlag()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            sb.append(FIX5JonecFieldTag.USERNAME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfUsername()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            if (!StringHelper.isNullOrEmpty(getfNewPassword())){
                sb.append(FIX5JonecFieldTag.PASSWORD).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPassword()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
                sb.append(FIX5JonecFieldTag.NEWPASSWORD).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNewPassword()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }else{
                sb.append(FIX5JonecFieldTag.PASSWORD).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPassword()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfText())){
                sb.append(FIX5JonecFieldTag.TEXT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfText()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            sb.append(FIX5JonecFieldTag.DEFAULTAPPLVERID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfDefaultApplVerID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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