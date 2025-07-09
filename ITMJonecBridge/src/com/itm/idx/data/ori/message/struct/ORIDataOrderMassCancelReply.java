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
public class ORIDataOrderMassCancelReply extends ORIDataHeader {
    
    //.fields:
    private String fClOrdID                                     = "";
    private String fMassWithdrawResponse                        = "";
    private String fMassWithdrawRejectReason                    = "";
    private String fText                                        = "";
    
    
    public ORIDataOrderMassCancelReply(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }

    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }

    public String getfMassWithdrawResponse() {
        return fMassWithdrawResponse;
    }

    public void setfMassWithdrawResponse(String fMassWithdrawResponse) {
        this.fMassWithdrawResponse = fMassWithdrawResponse;
    }

    public String getfMassWithdrawRejectReason() {
        return fMassWithdrawRejectReason;
    }

    public void setfMassWithdrawRejectReason(String fMassWithdrawRejectReason) {
        this.fMassWithdrawRejectReason = fMassWithdrawRejectReason;
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
                        case ORIDataConst.ORIFieldTag.CLORDID:
                            setfClOrdID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.MASSWITHDRAWRESPONSE:
                            setfMassWithdrawResponse(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.MASSWITHDRAWREJECTREASON:
                            setfMassWithdrawRejectReason(zValue);
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
            
            sb.append(ORIDataConst.ORIFieldTag.CLORDID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfClOrdID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.MASSWITHDRAWRESPONSE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfMassWithdrawResponse()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.MASSWITHDRAWREJECTREASON).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfMassWithdrawRejectReason()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
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
                setfMsgType(ORIDataConst.ORIMsgType.ORDER_MASS_CANCEL_REPLY);
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
