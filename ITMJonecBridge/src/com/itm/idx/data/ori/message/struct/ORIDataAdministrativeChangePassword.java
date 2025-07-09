/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIMsgType;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataAdministrativeChangePassword extends ORIDataHeader {
    
    //.fields:
    private String fUserID                                      = "";
    private String fCurrentPassword                             = "";
    private String fNewPassword                                 = "";
    private String fReturnValue                                 = "";
    
    public ORIDataAdministrativeChangePassword(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
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
                        case ORIDataConst.ORIFieldTag.USERID:
                            setfUserID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.CURRENTPASSWORD:
                            setfCurrentPassword(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.NEWPASSWORD:
                            setfNewPassword(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.RETURNVALUE:
                            setfReturnValue(zValue);
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
            sb.append(ORIDataConst.ORIFieldTag.USERID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(StringHelper.addSpaces(getfUserID(), ORIDataConst.ORIFieldValueLength.ADMIN_USERID)).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.CURRENTPASSWORD).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(StringHelper.addSpaces(getfCurrentPassword(), ORIDataConst.ORIFieldValueLength.ADMIN_PASSWORD)).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.NEWPASSWORD).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(StringHelper.addSpaces(getfNewPassword(), ORIDataConst.ORIFieldValueLength.ADMIN_PASSWORD)).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.RETURNVALUE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfReturnValue()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            
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
                setfMsgType(ORIMsgType.CHANGE_PASSWORD);
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
