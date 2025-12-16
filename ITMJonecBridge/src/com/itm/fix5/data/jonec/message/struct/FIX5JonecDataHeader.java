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
import java.nio.charset.StandardCharsets;
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
//    private String fSenderCompID                                = "SH";
    private String fTargetCompID                                = FIX5JonecFieldValue.TARGET_COMP_ID_MME;
    private long fMsgSeqNum                                     = 0;
    private String fSendingTime                                 = "";
    //.20251202
    private String fPossDupflag                                 = "";
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

    public String getfPossDupflag() {
        return fPossDupflag;
    }

    public void setfPossDupflag(String fPossDupflag) {
        this.fPossDupflag = fPossDupflag;
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
                            case FIX5JonecFieldTag.POSSDUPFLAG:
                                setfPossDupflag(zValue);
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
                sb.append(getfSenderCompID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);//?????????????????????????????????????????????
            }
            
            sb.append(FIX5JonecFieldTag.TARGETCOMPID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTargetCompID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            sb.append(FIX5JonecFieldTag.MSGSEQNUM).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMsgSeqNum()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
            //.?????
            if (!StringHelper.isNullOrEmpty(getfPossDupflag())) {
                sb.append(FIX5JonecFieldTag.POSSDUPFLAG).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPossDupflag()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            } else {
                sb.append(FIX5JonecFieldTag.POSSDUPFLAG).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append("N").append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            
            sb.append(FIX5JonecFieldTag.SENDINGTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSendingTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
                        
            zOut = sb.toString();

//            String SOH = "\u0001";
//
//            StringBuilder sb1 = new StringBuilder();
//            sb1.append("8=FIXT.1.1").append(SOH);
//            sb1.append("9=000").append(SOH);      // placeholder
//            sb1.append("35=A").append(SOH);
//            sb1.append("49=SH").append(SOH);
//            sb1.append("56=MME").append(SOH);
//            sb1.append("34=122").append(SOH);
//            sb1.append("50=SHJFE1").append(SOH);
//            sb1.append("43=N").append(SOH);
//            sb1.append("52=20251127-01:50:20").append(SOH);
//            sb1.append("98=0").append(SOH);
//            sb1.append("108=11").append(SOH);
//            sb1.append("553=SHJFE1").append(SOH);
//            sb1.append("554=P@ssw0rd!1").append(SOH);
//            sb1.append("1137=9").append(SOH);
////            sb1.append("1128=9").append(SOH);
//            // … termasuk tag 165xx custom
//            String msgWithoutChecksum = sb1.toString();
//
//            // Hitung BodyLength
//            int bodyLength = calculateBodyLength(msgWithoutChecksum + "10=000" + SOH);
//
//            // Replace tag 9=xxx
//            String finalMsg = msgWithoutChecksum.replace("9=000", "9=" + bodyLength);
//
//            // Ambil checksum
//            String checksum = calculateChecksum(finalMsg);
//
//            // Tambahkan checksum
//            finalMsg = finalMsg + "10=" + checksum + SOH;
//
//            System.out.println(finalMsg);
//            
//            
//            zOut = finalMsg;
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String calculateChecksum(String fixMessage) {
        byte[] bytes = fixMessage.getBytes(StandardCharsets.US_ASCII);
        int sum = 0;
        for (byte b : bytes) {
            sum += b;
        }
        int cs = sum % 256;
        return String.format("%03d", cs);
    }
    
    public static int calculateBodyLength(String fixMessage) {
        int index9End = fixMessage.indexOf("\u0001", fixMessage.indexOf("9=")) + 1;
        int index10Start = fixMessage.indexOf("10=");
        return fixMessage.substring(index9End, index10Start).getBytes(StandardCharsets.US_ASCII).length;
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
