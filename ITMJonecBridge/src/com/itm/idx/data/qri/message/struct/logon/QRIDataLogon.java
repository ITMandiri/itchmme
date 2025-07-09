/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.struct.logon;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.jonec.consts.JonecConst;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataHeader;
import com.itm.idx.data.qri.util.MsgBuilder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIDataLogon extends QRIDataHeader{
    private int fEncryptedMethod;
    private int fHeartBtInt;
    private String fUserID;
    private String fCurrentPassword;
    
    public QRIDataLogon(){
        setfMsgType(QRIDataConst.MsgType.Logon.getValue());
    }

    public QRIDataLogon(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public int getfEncryptedMethod() {
        return fEncryptedMethod;
    }

    public void setfEncryptedMethod(int fEncryptedMethod) {
        this.fEncryptedMethod = fEncryptedMethod;
    }

    public int getfHeartBtInt() {
        if (fHeartBtInt <= 0) return fHeartBtInt = JonecConst.DEFAULT_HEART_BEAT_INT;
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

    public String getfCurrentPassword() {
        return fCurrentPassword;
    }

    public void setfCurrentPassword(String fCurrentPassword) {
        this.fCurrentPassword = fCurrentPassword;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        msgBuild.setMessage(getStringHeader());
        msgBuild.addData(QRIDataConst.QRITag.EncryptMethod, String.valueOf(fEncryptedMethod));
        msgBuild.addData(QRIDataConst.QRITag.HeartBtInt, String.valueOf(fHeartBtInt));
        msgBuild.addData(QRIDataConst.QRITag.UserID, String.valueOf(fUserID));
        msgBuild.addData(QRIDataConst.QRITag.CurrentPassword, String.valueOf(fCurrentPassword));
        
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
                    setfEncryptedMethod(StringHelper.toInt(val));
                    break;
                case QRIDataConst.QRITag.HeartBtInt:
                    setfHeartBtInt(StringHelper.toInt(val));
                    break;
                case QRIDataConst.QRITag.UserID:
                    setfUserID(StringHelper.trimStr(val));
                    break;
                case QRIDataConst.QRITag.CurrentPassword:
                    setfCurrentPassword(StringHelper.trimStr(val));
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }

}
