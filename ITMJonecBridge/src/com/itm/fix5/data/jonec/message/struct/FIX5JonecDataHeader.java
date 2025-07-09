/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.message.struct;

import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldFmt;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldTag;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldValueLength;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldValue;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5JonecDataHeader extends FIX5IDXMessage {
    
    //.fields:
    //.header:
    private String fBeginString                                 = FIX5JonecFieldValue.BEGIN_STRING;
    private int fBodyLength                                     = 0;
    private String fMsgType                                     = "";
    private String fSenderSubID                                 = "";
    private String fSenderCompID                                = FIX5JonecFieldValue.SENDER_COMP_ID;
    private String fTargetCompID                                = FIX5JonecFieldValue.TARGET_COMP_ID;
    private long fMsgSeqNum                                     = 0;
    private String fSendingTime                                 = "";
    //.trailer:
    private int fCheckSum                                       = 0;
    
    
    
    public FIX5JonecDataHeader(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }

    public String getfBeginString() {
        return fBeginString;
    }

    public void setfBeginString(String fBeginString) {
        this.fBeginString = fBeginString;
    }

    public int getfBodyLength() {
        return fBodyLength;
    }

    public void setfBodyLength(int fBodyLength) {
        this.fBodyLength = fBodyLength;
    }

    public String getfMsgType() {
        return fMsgType;
    }

    public void setfMsgType(String fMsgType) {
        this.fMsgType = fMsgType;
    }

    public String getfSenderSubID() {
        return fSenderSubID;
    }

    public void setfSenderSubID(String fSenderSubID) {
        this.fSenderSubID = fSenderSubID;
    }

    public String getfSenderCompID() {
        return fSenderCompID;
    }

    public void setfSenderCompID(String fSenderCompID) {
        this.fSenderCompID = fSenderCompID;
    }

    public String getfTargetCompID() {
        return fTargetCompID;
    }

    public void setfTargetCompID(String fTargetCompID) {
        this.fTargetCompID = fTargetCompID;
    }

    public long getfMsgSeqNum() {
        return fMsgSeqNum;
    }

    public void setfMsgSeqNum(long fMsgSeqNum) {
        this.fMsgSeqNum = fMsgSeqNum;
    }

    public String getfSendingTime() {
        return fSendingTime;
    }

    public void setfSendingTime(String fSendingTime) {
        this.fSendingTime = fSendingTime;
    }

    public int getfCheckSum() {
        return fCheckSum;
    }

    public void setfCheckSum(int fCheckSum) {
        this.fCheckSum = fCheckSum;
    }

    public boolean assignHeaderMessage(){
        boolean bOut = false;
        try{
            Map<String, ArrayList<String>> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    ///String zValue = mapFields.get(zKey);
                    for(String zValue : mapFields.get(zKey)){
                        switch(zKey){
                            case FIX5JonecFieldTag.BEGINSTRING:
                                setfBeginString(zValue);
                                break;
                            case FIX5JonecFieldTag.BODYLENGTH:
                                setfBodyLength(StringHelper.toInt(zValue));
                                break;
                            case FIX5JonecFieldTag.MSGTYPE:
                                setfMsgType(zValue);
                                break;
                            case FIX5JonecFieldTag.SENDERSUBID:
                                setfSenderSubID(zValue);
                                break;
                            case FIX5JonecFieldTag.SENDERCOMPID:
                                setfSenderCompID(zValue);
                                break;
                            case FIX5JonecFieldTag.TARGETCOMPID:
                                setfTargetCompID(zValue);
                                break;
                            case FIX5JonecFieldTag.MSGSEQNUM:
                                setfMsgSeqNum(StringHelper.toLong(zValue));
                                break;
                            case FIX5JonecFieldTag.SENDINGTIME:
                                setfSendingTime(zValue);
                                break;
                            case FIX5JonecFieldTag.CHECKSUM:
                                setfCheckSum(StringHelper.toInt(zValue));
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
    
    public String msgHeaderToString() {
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            
            sb.append(FIX5JonecFieldTag.BEGINSTRING).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfBeginString()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            sb.append(FIX5JonecFieldTag.BODYLENGTH).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfBodyLength()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            sb.append(FIX5JonecFieldTag.MSGTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMsgType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            if (!StringHelper.isNullOrEmpty(getfSenderSubID())){
                sb.append(FIX5JonecFieldTag.SENDERSUBID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSenderSubID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfSenderCompID())){
                sb.append(FIX5JonecFieldTag.SENDERCOMPID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSenderCompID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            sb.append(FIX5JonecFieldTag.TARGETCOMPID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTargetCompID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            sb.append(FIX5JonecFieldTag.MSGSEQNUM).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMsgSeqNum()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            sb.append(FIX5JonecFieldTag.SENDINGTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSendingTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public String msgTrailerToString(){
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            
            sb.append(FIX5JonecFieldTag.CHECKSUM).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(StringHelper.addZeroFromInt(getfCheckSum(), FIX5JonecFieldValueLength.CHECKSUM)).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
}
