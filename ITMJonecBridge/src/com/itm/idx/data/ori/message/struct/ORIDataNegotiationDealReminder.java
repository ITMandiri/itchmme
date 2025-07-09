/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataNegotiationDealReminder extends ORIDataHeader {
    
    //.fields:
    private String fText                                        = "";
    
    public ORIDataNegotiationDealReminder(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
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
            
            sb.append(ORIDataConst.ORIFieldTag.TEXT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfText()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            
            
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
                setfMsgType(ORIDataConst.ORIMsgType.NEGDEAL_REMINDER);
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
