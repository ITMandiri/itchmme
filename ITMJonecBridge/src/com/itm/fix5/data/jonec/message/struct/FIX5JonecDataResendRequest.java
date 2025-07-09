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
public class FIX5JonecDataResendRequest extends FIX5JonecDataHeader {

    //.reference name: Resend Request (2)

    //.fields:
    private String fBeginSeqNo = "";
    private String fEndSeqNo = "";

    public FIX5JonecDataResendRequest(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfBeginSeqNo() {
        return fBeginSeqNo;
    }

    public void setfBeginSeqNo(String fBeginSeqNo) {
        this.fBeginSeqNo = fBeginSeqNo;
    }
    
    public String getfEndSeqNo() {
        return fEndSeqNo;
    }

    public void setfEndSeqNo(String fEndSeqNo) {
        this.fEndSeqNo = fEndSeqNo;
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
                            case FIX5JonecFieldTag.BEGINSEQNO:
                                setfBeginSeqNo(zValue);
                                break;
                            case FIX5JonecFieldTag.ENDSEQNO:
                                setfEndSeqNo(zValue);
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
            sb.append(FIX5JonecFieldTag.BEGINSEQNO).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfBeginSeqNo()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ENDSEQNO).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfEndSeqNo()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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