/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.message.struct;

import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author fredy
 */
public class FIX5JonecDataTradeCaptureReportRequestAck extends FIX5JonecDataHeader {

    //.reference name: TradeCaptureReportRequestAck(AQ)

    //.fields:
    private String fTradeRequestID = "";
    private String fTradeRequestType = "";
    private String fSubscriptionRequestType = "";
    private String fTotNumTradeReports = "";
    private String fTradeRequestResult = "";
    private String fTradeRequestStatus = "";
    private String fText = "";

    public FIX5JonecDataTradeCaptureReportRequestAck(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfTradeRequestID() {
        return fTradeRequestID;
    }

    public void setfTradeRequestID(String fTradeRequestID) {
        this.fTradeRequestID = fTradeRequestID;
    }

    public String getfTradeRequestType() {
        return fTradeRequestType;
    }

    public void setfTradeRequestType(String fTradeRequestType) {
        this.fTradeRequestType = fTradeRequestType;
    }

    public String getfSubscriptionRequestType() {
        return fSubscriptionRequestType;
    }

    public void setfSubscriptionRequestType(String fSubscriptionRequestType) {
        this.fSubscriptionRequestType = fSubscriptionRequestType;
    }

    public String getfTotNumTradeReports() {
        return fTotNumTradeReports;
    }

    public void setfTotNumTradeReports(String fTotNumTradeReports) {
        this.fTotNumTradeReports = fTotNumTradeReports;
    }

    public String getfTradeRequestResult() {
        return fTradeRequestResult;
    }

    public void setfTradeRequestResult(String fTradeRequestResult) {
        this.fTradeRequestResult = fTradeRequestResult;
    }

    public String getfTradeRequestStatus() {
        return fTradeRequestStatus;
    }

    public void setfTradeRequestStatus(String fTradeRequestStatus) {
        this.fTradeRequestStatus = fTradeRequestStatus;
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }
    
    //.process:
    public boolean assignMessage(){
        boolean bOut = false;
        try{
            //.assign header:
            assignHeaderMessage();
            //.assign data:
            Map<String, ArrayList<String>> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    ///String zValue = mapFields.get(zKey);
                    for(String zValue : mapFields.get(zKey)){
                        switch(zKey){
                            case FIX5JonecDataConst.FIX5JonecFieldTag.TRADEREQUESTID:
                                setfTradeRequestID(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.TRADEREQUESTTYPE:
                                setfTradeRequestType(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.SUBSCRIPTIONREQUESTTYPE:
                                setfSubscriptionRequestType(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.TOTNUMTRADEREPORTS:
                                setfTotNumTradeReports(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.TRADEREQUESTRESULT:
                                setfTradeRequestResult(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.TRADEREQUESTSTATUS:
                                setfTradeRequestStatus(zValue);
                                break;
                            case FIX5JonecDataConst.FIX5JonecFieldTag.TEXT:
                                setfText(zValue);
                                break;
                            
                            default:
                                break;
                        }
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
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.TRADEREQUESTID).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTradeRequestID()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.TRADEREQUESTTYPE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTradeRequestType()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.SUBSCRIPTIONREQUESTTYPE).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSubscriptionRequestType()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.TOTNUMTRADEREPORTS).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTotNumTradeReports()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.TRADEREQUESTRESULT).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTradeRequestResult()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.TRADEREQUESTSTATUS).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTradeRequestStatus()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecDataConst.FIX5JonecFieldTag.TEXT).append(FIX5JonecDataConst.FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfText()).append(FIX5JonecDataConst.FIX5JonecFieldFmt.FIELD_SEPARATOR);
            
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
                
            }
            
            sb.append(msgHeaderToString());
            sb.append(msgDataToString());
            sb.append(msgTrailerToString());
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
}
