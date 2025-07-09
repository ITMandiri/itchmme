/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.struct.password;

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
public class QRIDataChangePassword extends QRIDataHeader{
    private String fUserID;
    private String fCurrentPassword;
    private String fNewPassword;
    private String fReturnValue;
    
    
    public QRIDataChangePassword(){
        setfMsgType(QRIDataConst.MsgType.ChangePass.getValue());
    }
    
    public QRIDataChangePassword(Map<String, String> fieldMap){
        super(fieldMap);
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

    public String getfNewPassword() {
        return fNewPassword;
    }

    public void setfNewPassword(String fNewPassword) {
        this.fNewPassword = fNewPassword;
    }

    public String getfReturnValue() {
        return fReturnValue;
    }

    public void setfReturnValue(String fReturnValue) {
        this.fReturnValue = fReturnValue;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        msgBuild.setMessage(getStringHeader());
        msgBuild.addData(QRIDataConst.QRITag.UserID, fUserID);
        msgBuild.addData(QRIDataConst.QRITag.CurrentPassword, fCurrentPassword);
        msgBuild.addData(QRIDataConst.QRITag.NewPassword, fNewPassword);
        msgBuild.addData(QRIDataConst.QRITag.ReturnValue, fReturnValue);
        
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
                case QRIDataConst.QRITag.UserID:
                    setfUserID(val);
                    break;
                case QRIDataConst.QRITag.CurrentPassword:
                    setfCurrentPassword(val);
                    break;
                case QRIDataConst.QRITag.NewPassword:
                    setfNewPassword(val);
                    break;
                case QRIDataConst.QRITag.ReturnValue:
                    setfReturnValue(val);
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }

}
