/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldFmt;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldTag;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValueLength;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataAdministrativeLogon extends ORIDataHeader {
    
    //.fields:
    private int fEncryptMethod                                  = 0;
    private int fHeartBtInt                                     = 0;
    private String fUserID                                      = "";
    private String fCurrentPassword                             = "";
    
    public ORIDataAdministrativeLogon(Map<String, String> inputMsgFields) {
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

    public String getfCurrentPassword() {
        return fCurrentPassword;
    }

    public void setfCurrentPassword(String fCurrentPassword) {
        this.fCurrentPassword = fCurrentPassword;
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
                            setfUserID(StringHelper.trimStr(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.CURRENTPASSWORD:
                            setfCurrentPassword(StringHelper.trimStr(zValue));
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
            sb.append(ORIFieldTag.ENCRYPTMETHOD).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfEncryptMethod()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.HEARTBTINT).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfHeartBtInt()).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.USERID).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(StringHelper.addSpaces(getfUserID(), ORIFieldValueLength.ADMIN_USERID)).append(ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIFieldTag.CURRENTPASSWORD).append(ORIFieldFmt.KV_SEPARATOR);
            sb.append(StringHelper.addSpaces(getfCurrentPassword(), ORIFieldValueLength.ADMIN_PASSWORD)).append(ORIFieldFmt.FIELD_SEPARATOR);
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
                setfMsgType(ORIDataConst.ORIMsgType.LOGON);
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
