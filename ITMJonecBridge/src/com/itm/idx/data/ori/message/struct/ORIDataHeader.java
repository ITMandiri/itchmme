/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.idx.message.IDXMessage;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldFmt;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldTag;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValue;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValueLength;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataHeader extends IDXMessage {
    
    //.fields:
    private String fBeginString                                 = ORIFieldValue.FIX_ENGINE_VERSION;
    private int fBodyLength                                     = 0;
    private String fMsgType                                     = "";
    private String fSenderCompID                                = "";
    private String fTargetCompID                                = "";
    private String fMsgSeqNum                                   = "";
    private String fSenderSubID                                 = "";
    private String fSenderLocationID                            = "";
    private String fSendingTime                                 = "";
    
    private int fCheckSum                                       = 0;
    
    //.bundle fields:
    private String fBundleMessageVersion                        = ORIFieldValue.BUNDLE_MESSAGE_VERSION;
    private String fBundleConnectionName                        = ORIFieldValue.BUNDLE_CONNECTION_NAME_JONES_1; //.default.
    private String fBundlePosDup                                = ORIFieldValue.BUNDLE_POS_DUP;
    
    public ORIDataHeader(Map<String, String> inputMsgFields) {
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

    public String getfMsgSeqNum() {
        return fMsgSeqNum;
    }

    public void setfMsgSeqNum(String fMsgSeqNum) {
        this.fMsgSeqNum = fMsgSeqNum;
    }

    public String getfSenderSubID() {
        return fSenderSubID;
    }

    public void setfSenderSubID(String fSenderSubID) {
        this.fSenderSubID = fSenderSubID;
    }

    public String getfSenderLocationID() {
        return fSenderLocationID;
    }

    public void setfSenderLocationID(String fSenderLocationID) {
        this.fSenderLocationID = fSenderLocationID;
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
    
    //.bundle fields:
    public String getfBundleMessageVersion() {
        return fBundleMessageVersion;
    }

    public void setfBundleMessageVersion(String fBundleMessageVersion) {
        this.fBundleMessageVersion = fBundleMessageVersion;
    }

    public String getfBundleConnectionName() {
        return fBundleConnectionName;
    }

    public void setfBundleConnectionName(String fBundleConnectionName) {
        this.fBundleConnectionName = fBundleConnectionName;
    }

    public String getfBundlePosDup() {
        return fBundlePosDup;
    }

    public void setfBundlePosDup(String fBundlePosDup) {
        this.fBundlePosDup = fBundlePosDup;
    }

    public boolean assignHeaderMessage(){
        boolean bOut = false;
        try{
            Map<String, String> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    String zValue = mapFields.get(zKey);
                    switch(zKey){
                        case ORIFieldTag.BEGINSTRING:
                            setfBeginString(zValue);
                            break;
                        case ORIFieldTag.BODYLENGTH:
                            setfBodyLength(StringHelper.toInt(zValue));
                            break;
                        case ORIFieldTag.MSGTYPE:
                            setfMsgType(zValue);
                            break;
                        case ORIFieldTag.SENDERCOMPID:
                            setfSenderCompID(zValue);
                            break;
                        case ORIFieldTag.TARGETCOMPID:
                            setfTargetCompID(zValue);
                            break;
                        case ORIFieldTag.MSGSEQNUM:
                            setfMsgSeqNum(zValue);
                            break;
                        case ORIFieldTag.SENDERSUBID:
                            setfSenderSubID(zValue);
                            break;
                        case ORIFieldTag.SENDERLOCATIONID:
                            setfSenderLocationID(zValue);
                            break;
                        case ORIFieldTag.SENDINGTIME:
                            setfSendingTime(zValue);
                            break;
                        default:
                            break;
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
            sb.append(ORIFieldTag.BEGINSTRING).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfBeginString()).append(ORIFieldFmt.FIELD_SEPARATOR); //.SOH
            sb.append(ORIFieldTag.MSGTYPE).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfMsgType()).append(ORIFieldFmt.FIELD_SEPARATOR);
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public String msgBundlePrefixToString(){
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(fBundleMessageVersion).append(ORIFieldFmt.HEADER_FIELD_SEPARATOR); //.ETX.messageversion.
            sb.append(getfMsgType()).append(ORIFieldFmt.HEADER_FIELD_SEPARATOR); //.ETX.msgtype.
            sb.append(getfBundleConnectionName()).append(ORIFieldFmt.HEADER_FIELD_SEPARATOR); //.ETX.connectionname.
            sb.append("0").append(ORIFieldFmt.HEADER_FIELD_SEPARATOR); //.ETX.seqnum
            sb.append(getfBundlePosDup()).append(ORIFieldFmt.HEADER_FIELD_SEPARATOR); //.ETX.posdup.
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public String msgBundleSuffixToString(){
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(ORIFieldFmt.HEADER_FIELD_SEPARATOR); //.ETX
            sb.append(StringHelper.addZeroFromInt(getfCheckSum(), ORIFieldValueLength.CHECKSUM)).append(ORIFieldFmt.HEADER_FIELD_SEPARATOR); //.ETX.checksum.
            sb.append(ORIFieldValue.NEW_LINE);
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    
    
    
    
}
    