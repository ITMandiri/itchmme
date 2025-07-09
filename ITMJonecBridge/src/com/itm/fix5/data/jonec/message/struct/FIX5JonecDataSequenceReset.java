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
public class FIX5JonecDataSequenceReset extends FIX5JonecDataHeader {

    //.reference name: Sequence Reset (4)

    //.fields:
    private String fGapFillFlag = "";
    private String fNewSeqNo = "";

    public FIX5JonecDataSequenceReset(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfGapFillFlag() {
        return fGapFillFlag;
    }

    public void setfGapFillFlag(String fGapFillFlag) {
        this.fGapFillFlag = fGapFillFlag;
    }
    
    public String getfNewSeqNo() {
        return fNewSeqNo;
    }

    public void setfNewSeqNo(String fNewSeqNo) {
        this.fNewSeqNo = fNewSeqNo;
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
                            case FIX5JonecFieldTag.GAPFILLFLAG:
                                setfGapFillFlag(zValue);
                                break;
                            case FIX5JonecFieldTag.NEWSEQNO:
                                setfNewSeqNo(zValue);
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
            sb.append(FIX5JonecFieldTag.GAPFILLFLAG).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfGapFillFlag()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.NEWSEQNO).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNewSeqNo()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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