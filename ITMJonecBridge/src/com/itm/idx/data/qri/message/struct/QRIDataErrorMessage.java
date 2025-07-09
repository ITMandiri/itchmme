/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.struct;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.util.MsgBuilder;
import com.itm.idx.data.qri.util.StringUtil;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIDataErrorMessage  extends QRIDataHeader{
    
    public enum ErrorMessageType{
        PossibleDuplicateFix,
        UnknownJonec,
        JonecInterpretationError,
        BrodcastTableFull,
        JATSInterpretationError,
        EmptySecurityList,
        FailSendHeartbetToJats,
        MarketInfoReplyOK,
        MarketInfoReplyBAD
    }
    
    private int                 fErrorCode;
    private int                 fErrorLevel;
    private String              fRefErrorCode;
    private String              fErrorMessage;
    private String              fPreviousRequest;
    private ErrorMessageType    fErrorMessageType;
    
    //.perluasan:
    private String              fBrokerRef;
    
    
    public QRIDataErrorMessage(){
    }

    public int getfErrorCode() {
        return fErrorCode;
    }

    public void setfErrorCode(int fErrorCode) {
        this.fErrorCode = fErrorCode;
    }

    public int getfErrorLevel() {
        return fErrorLevel;
    }

    public void setfErrorLevel(int fErrorLevel) {
        this.fErrorLevel = fErrorLevel;
    }

    public String getfRefErrorCode() {
        return fRefErrorCode;
    }

    public void setfRefErrorCode(String fRefErrorCode) {
        this.fRefErrorCode = fRefErrorCode;
    }

    public String getfErrorMessage() {
        return fErrorMessage;
    }

    public void setfErrorMessage(String fErrorMessage) {
        this.fErrorMessage = fErrorMessage;
    }

    public String getfPreviousRequest() {
        return fPreviousRequest;
    }

    public void setfPreviousRequest(String fPreviousRequest) {
        this.fPreviousRequest = fPreviousRequest;
    }

    public ErrorMessageType getfErrorMessageType() {
        return fErrorMessageType;
    }

    public void setfErrorMessageType(ErrorMessageType fErrorMessageType) {
        this.fErrorMessageType = fErrorMessageType;
    }

    public String getfBrokerRef() {
        return fBrokerRef;
    }

    public void setfBrokerRef(String fBrokerRef) {
        this.fBrokerRef = fBrokerRef;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        msgBuild.setMessage(getStringHeader());
        
        msgBuild.addData(QRIDataConst.QRITag.MsgType, getfMsgType());
        msgBuild.addData(QRIDataConst.QRITag.Checksum, StringHelper.addZeroFromInt(0, 3));
        
        return convertToJonectMessage(msgBuild.getMessage(), getfMsgType());
    }
    
    @Override
     public void assignValue(){
        Map<String, String> fieldMap = getMapMsgFields();
        
        Enumeration<String> strEnum = Collections.enumeration(fieldMap.keySet());
        String keyField;
        String val;
        //.-- asign nilai header nya dulu
        assignHeaderValue();
        while(strEnum.hasMoreElements()) {
            keyField = strEnum.nextElement();
            val = fieldMap.get(keyField);
            switch(keyField){
                case QRIDataConst.QRITag.ErrorCode: 
                    setfErrorCode(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.ErrorLevel: 
                    setfErrorLevel(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.RefErrorCode: 
                    setfRefErrorCode(val);
                    break;
                case QRIDataConst.QRITag.Text:
                    setfErrorMessage(val);
                    break;
                case QRIDataConst.QRITag.PreviousRequest:
                    setfPreviousRequest(val);
                    break;                     
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
}
