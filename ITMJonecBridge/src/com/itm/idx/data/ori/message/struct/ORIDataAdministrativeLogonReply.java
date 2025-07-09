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
public class ORIDataAdministrativeLogonReply extends ORIDataHeader {

    //.enums:
    public enum ORIAdministrativeLogonReplyType{
        OK1,
        OK2,
        BAD
    }
    
    //.fields:
    private int fEncryptMethod                                  = 0;
    private int fHeartBtInt                                     = 0;
    //.+OK2:
    private String fUserID                                      = "";
    private String fLogonReply                                  = "";
    private String fLogonDesc                                   = "";
    //.+BAD:
    private String fText                                        = "";
    
    
    private ORIAdministrativeLogonReplyType fAdministrativeLogonReplyType;
    
    public ORIDataAdministrativeLogonReply(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }
    
    public int getfEncryptMethod() {
        return fEncryptMethod;
    }

    public void setfEncryptMethod(int fEncryptMethod) {
        this.fEncryptMethod = fEncryptMethod;
    }

    public int getfHeartBtInt() {
        return fHeartBtInt;
    }

    public void setfHeartBtInt(int fHeartBtInt) {
        this.fHeartBtInt = fHeartBtInt;
    }

    public String getfUserID() {
        return fUserID;
    }

    public void setfUserID(String fUserID) {
        this.fUserID = fUserID;
    }

    public String getfLogonReply() {
        return fLogonReply;
    }

    public void setfLogonReply(String fLogonReply) {
        this.fLogonReply = fLogonReply;
    }

    public String getfLogonDesc() {
        return fLogonDesc;
    }

    public void setfLogonDesc(String fLogonDesc) {
        this.fLogonDesc = fLogonDesc;
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }

    public ORIAdministrativeLogonReplyType getfAdministrativeLogonReplyType() {
        return fAdministrativeLogonReplyType;
    }

    public void setfAdministrativeLogonReplyType(ORIAdministrativeLogonReplyType fAdministrativeLogonReplyType) {
        this.fAdministrativeLogonReplyType = fAdministrativeLogonReplyType;
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
                        case ORIDataConst.ORIFieldTag.ENCRYPTMETHOD:
                            setfEncryptMethod(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.HEARTBTINT:
                            setfHeartBtInt(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.USERID:
                            setfUserID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.LOGONREPLY:
                            setfLogonReply(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.LOGONDESC:
                            setfLogonDesc(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.TEXT:
                            setfText(zValue);
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
            
            if (null != getfAdministrativeLogonReplyType())switch (getfAdministrativeLogonReplyType()) {
                case OK1:
                    sb.append(ORIDataConst.ORIFieldTag.ENCRYPTMETHOD).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfEncryptMethod()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.HEARTBTINT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfHeartBtInt()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);

                    sb.append(ORIDataConst.ORIFieldTag.CHECKSUM).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(StringHelper.addZeroFromInt(getfCheckSum(), ORIDataConst.ORIFieldValueLength.CHECKSUM)).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);

                    break;
                case OK2:
                    sb.append(ORIDataConst.ORIFieldTag.ENCRYPTMETHOD).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfEncryptMethod()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.HEARTBTINT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfHeartBtInt()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.USERID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfUserID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.LOGONREPLY).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfLogonReply()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.LOGONDESC).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfLogonDesc()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);

                    sb.append(ORIDataConst.ORIFieldTag.CHECKSUM).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(StringHelper.addZeroFromInt(getfCheckSum(), ORIDataConst.ORIFieldValueLength.CHECKSUM)).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);

                    break;
                case BAD:
                    sb.append(ORIDataConst.ORIFieldTag.TEXT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfText()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);

                    sb.append(ORIDataConst.ORIFieldTag.CHECKSUM).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(StringHelper.addZeroFromInt(getfCheckSum(), ORIDataConst.ORIFieldValueLength.CHECKSUM)).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);

                    break; 
                default:
                    break;
            }
            
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
                if (getfAdministrativeLogonReplyType() == ORIAdministrativeLogonReplyType.BAD){
                    setfMsgType(ORIDataConst.ORIMsgType.LOGOUT);
                }else{
                    setfMsgType(ORIDataConst.ORIMsgType.LOGON);
                }
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
