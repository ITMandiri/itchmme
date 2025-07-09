/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataErrorMessage extends ORIDataHeader {

    //.enums:
    public enum ORIErrorMessageType{
        PossibleDuplicateFixMessage,        //. 1   1
        UnknownJONECMessage,                //. 2   1
        JATSInterpretationErrorReply,       //. 5   1
        JONECMessageInterpretationError,    //. 3   1
        BroadcastTableFull,                 //. 3   1
        FailedToSendHeartbeatToJATS,        //. 3   1
        OthersKnown,                        //. =3{JONECMessageInterpretationError, BroadcastTableFull, FailedToSendHeartbeatToJATS}
        OthersUnknown                       //. (-1)
    }
    
    //.fields:
    private int fErrorCode                                      = 0;
    private int fErrorLevel                                     = 0;
    private String fRefErrorCode                                = "";
    private String fText                                        = "";
    private String fPreviousRequest                             = "";
    //.additional:
    private String fBrokerRef                                   = "";
    
    private ORIErrorMessageType fErrorMessageType;
    
    public ORIDataErrorMessage(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
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

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }

    public String getfPreviousRequest() {
        return fPreviousRequest;
    }

    public void setfPreviousRequest(String fPreviousRequest) {
        this.fPreviousRequest = fPreviousRequest;
    }

    public ORIErrorMessageType getfErrorMessageType() {
        return fErrorMessageType;
    }

    public void setfErrorMessageType(ORIErrorMessageType fErrorMessageType) {
        this.fErrorMessageType = fErrorMessageType;
    }
    
    public String getfBrokerRef() {
        return fBrokerRef;
    }

    public void setfBrokerRef(String fBrokerRef) {
        this.fBrokerRef = fBrokerRef;
    }
    
    public boolean assignMessage(){
        boolean bOut = false;
        try{
            //.assign header:
            assignHeaderMessage();
            //.assign data:
            Map<String, String> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    String zValue = mapFields.get(zKey);
                    switch(zKey){
                        case ORIDataConst.ORIFieldTag.ERRORCODE:
                            setfErrorCode(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.ERRORLEVEL:
                            setfErrorLevel(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.REFERRORCODE:
                            setfRefErrorCode(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.TEXT:
                            setfText(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.PREVIOUSREQUEST:
                            setfPreviousRequest(zValue);
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
    
    public String msgDataToString() {
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            
            
            
            sb.append(ORIDataConst.ORIFieldTag.CHECKSUM).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(StringHelper.addZeroFromInt(getfCheckSum(), ORIDataConst.ORIFieldValueLength.CHECKSUM)).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            
            //... .
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
                setfMsgType(ORIDataConst.ORIMsgType.ERROR_MESSAGE);
            }
            sb.append(msgBundlePrefixToString());
            sb.append(msgHeaderToString());
            sb.append(msgDataToString());
            sb.append(msgBundleSuffixToString());
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
}
