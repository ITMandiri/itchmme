/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.struct;

import com.itm.idx.data.jonec.consts.JonecConst;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.util.MsgBuilder;
import com.itm.idx.data.qri.util.StringUtil;
import com.itm.idx.message.IDXMessage;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIDataHeader extends IDXMessage{
    private String          fBeginString;
    private int             fBodyLength;
    private String          fMsgType;
    private String          fSenderCompID;
    private String          fTargetCompID;
    private String          fMsgSeqNum;
    private String          fSenderSubID;
    private String          fSenderLocationID;
    private String          fSendingTime;
    
    private String          fConnName;
    
    public QRIDataHeader(){

    }
    
    public QRIDataHeader(Map<String, String> fieldMap){
        super(fieldMap);
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

    public String getfConnName() {
        return fConnName;
    }

    public void setfConnName(String fConnName) {
        this.fConnName = fConnName;
    }
    
    protected String convertToJonectMessage(String basicMsg, String msgType){
        final int checksum = 0;
        StringBuilder buffer;
        buffer = new StringBuilder(JonecConst.JONEC_MESSAGE_VERSION).append(QRIDataConst.HEADER_FIELD_SEPARATOR);   //. 0 MessageVersion
        buffer.append(msgType).append(QRIDataConst.HEADER_FIELD_SEPARATOR);                                          //. 1 MessageType
        buffer.append(getfConnName()).append(QRIDataConst.HEADER_FIELD_SEPARATOR);               //. 2 SessionName                  
        buffer.append("0").append(QRIDataConst.HEADER_FIELD_SEPARATOR);                                              //. 3 SeqNum
        buffer.append("N").append(QRIDataConst.HEADER_FIELD_SEPARATOR);                                              //. 4 PossDup
        buffer.append(basicMsg);
        buffer.append(QRIDataConst.HEADER_FIELD_SEPARATOR).append(checksum).append(QRIDataConst.HEADER_FIELD_SEPARATOR);
        
        return buffer.toString();
    }
    
    protected String getStringHeader(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        if (fBeginString == null) fBeginString = JonecConst.FIX_BEGIN_STRING_VALUE;
        msgBuild.addData(QRIDataConst.QRITag.BeginString, fBeginString);
        if (fBodyLength > 0) msgBuild.addData(QRIDataConst.QRITag.BodyLength, String.valueOf(fBodyLength));
        msgBuild.addData(QRIDataConst.QRITag.MsgType, fMsgType);
        msgBuild.addData(QRIDataConst.QRITag.SenderCompID, fSenderCompID);
        msgBuild.addData(QRIDataConst.QRITag.TargetCompID, fTargetCompID);
        msgBuild.addData(QRIDataConst.QRITag.MsgSeqNum, fMsgSeqNum);
        msgBuild.addData(QRIDataConst.QRITag.SenderSubID, fSenderSubID);
        msgBuild.addData(QRIDataConst.QRITag.SenderLocID, fSenderLocationID);
        msgBuild.addData(QRIDataConst.QRITag.SendingTime, fSendingTime);
        return msgBuild.getMessage();
    }
    
    protected void assignHeaderValue(){
        Map<String, String> fieldMap = getMapMsgFields();

        Enumeration<String> strEnum = Collections.enumeration(fieldMap.keySet());
        String keyField;
        String val;
        
        while(strEnum.hasMoreElements()) {
            keyField = strEnum.nextElement();
            val = fieldMap.get(keyField);
            switch(keyField){
                case QRIDataConst.QRITag.BeginString:
                    setfBeginString(val);
                    break;
                case QRIDataConst.QRITag.BodyLength:
                    setfBodyLength(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.MsgType:
                    setfMsgType(val);
                    break;
                case QRIDataConst.QRITag.SenderCompID:
                    setfSenderCompID(val);
                    break;
                case QRIDataConst.QRITag.TargetCompID:
                    setfTargetCompID(val);
                    break;
                case QRIDataConst.QRITag.MsgSeqNum:
                    setfMsgSeqNum(val);
                    break;
                case QRIDataConst.QRITag.SenderSubID:
                    setfSenderSubID(val);
                    break;
                case QRIDataConst.QRITag.SenderLocID:
                    setfSenderLocationID(val);
                    break;
               case QRIDataConst.QRITag.SendingTime:
                    setfSendingTime(val);
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
    }
}
