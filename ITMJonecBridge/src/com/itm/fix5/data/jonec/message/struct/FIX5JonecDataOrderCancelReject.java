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
public class FIX5JonecDataOrderCancelReject extends FIX5JonecDataHeader {

    //.reference name: Order Cancel Reject (9)

    //.fields:
    private String fClOrdID = "";
    private String fOrderID = "";
    private String fOrdStatus = "";
    private String fOrigClOrdID = "";
    private String fTransactTime = "";
    private String fCxlRejReason = "";
    private String fCxlRejResponseTo = "";
    private String fText = "";

    public FIX5JonecDataOrderCancelReject(Map<String, ArrayList<String>> inputMsgFields) {
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
    
    public String getfOrdStatus() {
        return fOrdStatus;
    }

    public void setfOrdStatus(String fOrdStatus) {
        this.fOrdStatus = fOrdStatus;
    }
    
    public String getfOrigClOrdID() {
        return fOrigClOrdID;
    }

    public void setfOrigClOrdID(String fOrigClOrdID) {
        this.fOrigClOrdID = fOrigClOrdID;
    }
    
    public String getfTransactTime() {
        return fTransactTime;
    }

    public void setfTransactTime(String fTransactTime) {
        this.fTransactTime = fTransactTime;
    }
    
    public String getfCxlRejReason() {
        return fCxlRejReason;
    }

    public void setfCxlRejReason(String fCxlRejReason) {
        this.fCxlRejReason = fCxlRejReason;
    }
    
    public String getfCxlRejResponseTo() {
        return fCxlRejResponseTo;
    }

    public void setfCxlRejResponseTo(String fCxlRejResponseTo) {
        this.fCxlRejResponseTo = fCxlRejResponseTo;
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
                            case FIX5JonecFieldTag.CLORDID:
                                setfClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDERID:
                                setfOrderID(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDSTATUS:
                                setfOrdStatus(zValue);
                                break;
                            case FIX5JonecFieldTag.ORIGCLORDID:
                                setfOrigClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.TRANSACTTIME:
                                setfTransactTime(zValue);
                                break;
                            case FIX5JonecFieldTag.CXLREJREASON:
                                setfCxlRejReason(zValue);
                                break;
                            case FIX5JonecFieldTag.CXLREJRESPONSETO:
                                setfCxlRejResponseTo(zValue);
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
            sb.append(FIX5JonecFieldTag.ORDSTATUS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrdStatus()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORIGCLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrigClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TRANSACTTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTransactTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.CXLREJREASON).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfCxlRejReason()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.CXLREJRESPONSETO).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfCxlRejResponseTo()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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