/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.struct.logon;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataHeader;
import com.itm.idx.data.qri.util.MsgBuilder;
import com.itm.idx.data.qri.util.StringUtil;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIDataLogonReply extends QRIDataHeader{
    
    public enum LogonReplyType{
        LogonReplyOK1,
        LogonReplyOK2,
        LogonReplyBAD
    }
    
    private int fEncryptedMethod;
    private int fHeartBtInt;
    private String fUserID;
    private String fLogonReply;
    private String fLogonDesc;
    private String fText;
    private LogonReplyType fLogonReplyType;
    
    public QRIDataLogonReply(){
        
    }
    
    public QRIDataLogonReply(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public int getfEncryptedMethod() {
        return fEncryptedMethod;
    }

    public void setfEncryptedMethod(int fEncryptedMethod) {
        this.fEncryptedMethod = fEncryptedMethod;
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
    
    public LogonReplyType getfLogonReplyType() {
        if (fLogonReply == null){
            if (fText == null){
                fLogonReplyType = LogonReplyType.LogonReplyOK1;
            }else{
                fLogonReplyType = LogonReplyType.LogonReplyBAD;
            }
        }else{
            fLogonReplyType = LogonReplyType.LogonReplyOK2;
        }
        return fLogonReplyType;
    }

    public void setfLogonReplyType(LogonReplyType fLogonReplyType) {
        this.fLogonReplyType = fLogonReplyType;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        msgBuild.setMessage(getStringHeader());
        
        if (null != getfLogonReplyType())switch (getfLogonReplyType()) {
            case LogonReplyOK1:
                msgBuild.addData(QRIDataConst.QRITag.EncryptMethod, String.valueOf(getfEncryptedMethod()));
                msgBuild.addData(QRIDataConst.QRITag.HeartBtInt, String.valueOf(getfHeartBtInt()));
                setfMsgType(QRIDataConst.MsgType.Logon.getValue());
                break;
            case LogonReplyOK2:
                msgBuild.addData(QRIDataConst.QRITag.EncryptMethod, String.valueOf(getfEncryptedMethod()));
                msgBuild.addData(QRIDataConst.QRITag.HeartBtInt, String.valueOf(getfHeartBtInt()));
                msgBuild.addData(QRIDataConst.QRITag.UserID, String.valueOf(getfUserID()));
                msgBuild.addData(QRIDataConst.QRITag.LogonReply, String.valueOf(getfLogonReply()));
                msgBuild.addData(QRIDataConst.QRITag.LogonDesc, String.valueOf(getfLogonDesc()));
                setfMsgType(QRIDataConst.MsgType.Logon.getValue());
                break;
            case LogonReplyBAD:
                msgBuild.addData(QRIDataConst.QRITag.Text, String.valueOf(getfText()));
                setfMsgType(QRIDataConst.MsgType.Logout.getValue());
                break; 
            default:
                break;
        }

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
                case QRIDataConst.QRITag.EncryptMethod:
                    setfEncryptedMethod(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.HeartBtInt:
                    setfHeartBtInt(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.UserID:
                    setfUserID(val);
                    break;
                case QRIDataConst.QRITag.LogonReply:
                    setfLogonReply(val);
                    break;
                case QRIDataConst.QRITag.LogonDesc:
                    setfLogonDesc(val);
                    break;
                case QRIDataConst.QRITag.Text:
                    setfText(val);
                    break;
                default:
                    //. tag yang terlewatkan
                    
                    //. --
                    break;
            }
        }
        
    }

}
