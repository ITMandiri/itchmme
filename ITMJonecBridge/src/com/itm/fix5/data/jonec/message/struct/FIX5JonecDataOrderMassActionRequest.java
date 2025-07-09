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
public class FIX5JonecDataOrderMassActionRequest extends FIX5JonecDataHeader {

    //.reference name: Order Mass Action Request (CA)

    //.fields:
    private String fClOrdID = "";
    private String fMassActionType = "";
    private String fMassActionScope = "";
    private String fSymbol = "";
    private String fSecuritySubType = "";
    private String fSecondaryClOrdID = "";
    private String fSecurityID = "";
    private String fTransactTime = "";

    public FIX5JonecDataOrderMassActionRequest(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
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
    
    public String getfSecondaryClOrdID() {
        return fSecondaryClOrdID;
    }

    public void setfSecondaryClOrdID(String fSecondaryClOrdID) {
        this.fSecondaryClOrdID = fSecondaryClOrdID;
    }
    
    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
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
                            case FIX5JonecFieldTag.MASSACTIONTYPE:
                                setfMassActionType(zValue);
                                break;
                            case FIX5JonecFieldTag.MASSACTIONSCOPE:
                                setfMassActionScope(zValue);
                                break;
                            case FIX5JonecFieldTag.SYMBOL:
                                setfSymbol(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYSUBTYPE:
                                setfSecuritySubType(zValue);
                                break;
                            case FIX5JonecFieldTag.SECONDARYCLORDID:
                                setfSecondaryClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYID:
                                setfSecurityID(zValue);
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
            sb.append(FIX5JonecFieldTag.MASSACTIONTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassActionType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.MASSACTIONSCOPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassActionScope()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            if (!StringHelper.isNullOrEmpty(getfSymbol())){
                sb.append(FIX5JonecFieldTag.SYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecuritySubType())){
                sb.append(FIX5JonecFieldTag.SECURITYSUBTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecuritySubType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            sb.append(FIX5JonecFieldTag.SECONDARYCLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecondaryClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            if (!StringHelper.isNullOrEmpty(getfSecurityID())){
                sb.append(FIX5JonecFieldTag.SECURITYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
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