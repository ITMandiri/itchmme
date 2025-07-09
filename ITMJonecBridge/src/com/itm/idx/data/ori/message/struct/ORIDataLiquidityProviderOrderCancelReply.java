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
public class ORIDataLiquidityProviderOrderCancelReply extends ORIDataHeader {

    //.enums:
    public enum ORILiquidityProviderOrderCancelReplyType{
        OK,
        BAD
    }
    
    //.fields:
    private String fQuoteID                                     = "";
    private String fQuoteAckStatus                              = "";
    //.+BAD:
    private String fQuoteRejectReason                           = "";
    
    
    private ORILiquidityProviderOrderCancelReplyType fLiquidityProviderOrderCancelReplyType;
    
    public ORIDataLiquidityProviderOrderCancelReply(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }

    public String getfQuoteID() {
        return fQuoteID;
    }

    public void setfQuoteID(String fQuoteID) {
        this.fQuoteID = fQuoteID;
    }

    public String getfQuoteAckStatus() {
        return fQuoteAckStatus;
    }

    public void setfQuoteAckStatus(String fQuoteAckStatus) {
        this.fQuoteAckStatus = fQuoteAckStatus;
    }

    public String getfQuoteRejectReason() {
        return fQuoteRejectReason;
    }

    public void setfQuoteRejectReason(String fQuoteRejectReason) {
        this.fQuoteRejectReason = fQuoteRejectReason;
    }

    public ORILiquidityProviderOrderCancelReplyType getfLiquidityProviderOrderCancelReplyType() {
        return fLiquidityProviderOrderCancelReplyType;
    }

    public void setfLiquidityProviderOrderCancelReplyType(ORILiquidityProviderOrderCancelReplyType fLiquidityProviderOrderCancelReplyType) {
        this.fLiquidityProviderOrderCancelReplyType = fLiquidityProviderOrderCancelReplyType;
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
                        case ORIDataConst.ORIFieldTag.QUOTEID:
                            setfQuoteID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.QUOTEACKSTATUS:
                            setfQuoteAckStatus(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.QUOTEREJECTREASON:
                            setfQuoteRejectReason(zValue);
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
            
            sb.append(ORIDataConst.ORIFieldTag.QUOTEID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfQuoteID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            sb.append(ORIDataConst.ORIFieldTag.QUOTEACKSTATUS).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(getfQuoteAckStatus()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            
            if (getfLiquidityProviderOrderCancelReplyType() == ORILiquidityProviderOrderCancelReplyType.BAD){
                sb.append(ORIDataConst.ORIFieldTag.QUOTEREJECTREASON).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                sb.append(getfQuoteRejectReason()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            }
            
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
                setfMsgType(ORIDataConst.ORIMsgType.QUOTE_ACKNOWLEDGEMENT);
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
