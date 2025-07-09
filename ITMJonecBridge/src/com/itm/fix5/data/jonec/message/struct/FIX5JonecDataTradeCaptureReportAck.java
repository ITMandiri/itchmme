/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.message.struct;

import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldFmt;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldTag;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.Map;


/**
 *
 * @author aripam
 */
public class FIX5JonecDataTradeCaptureReportAck extends FIX5JonecDataHeader {

    //.reference name: Trade Capture Report Ack (AR)

    //.fields:
    private String fTradeReportID = "";
    private String fTradeReportTransType = "";
    private String fTradeReportType = "";
    private String fTrdRptStatus = "";
    private String fExecID = "";
    private String fTransactTime = "";
    private String fTradeReportRejectReason = "";
    private String fTradeReportRefID = "";
    private String fText = "";

    public FIX5JonecDataTradeCaptureReportAck(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfTradeReportID() {
        return fTradeReportID;
    }

    public void setfTradeReportID(String fTradeReportID) {
        this.fTradeReportID = fTradeReportID;
    }
    
    public String getfTradeReportTransType() {
        return fTradeReportTransType;
    }

    public void setfTradeReportTransType(String fTradeReportTransType) {
        this.fTradeReportTransType = fTradeReportTransType;
    }
    
    public String getfTradeReportType() {
        return fTradeReportType;
    }

    public void setfTradeReportType(String fTradeReportType) {
        this.fTradeReportType = fTradeReportType;
    }
    
    public String getfTrdRptStatus() {
        return fTrdRptStatus;
    }

    public void setfTrdRptStatus(String fTrdRptStatus) {
        this.fTrdRptStatus = fTrdRptStatus;
    }
    
    public String getfExecID() {
        return fExecID;
    }

    public void setfExecID(String fExecID) {
        this.fExecID = fExecID;
    }
    
    public String getfTransactTime() {
        return fTransactTime;
    }

    public void setfTransactTime(String fTransactTime) {
        this.fTransactTime = fTransactTime;
    }
    
    public String getfTradeReportRejectReason() {
        return fTradeReportRejectReason;
    }

    public void setfTradeReportRejectReason(String fTradeReportRejectReason) {
        this.fTradeReportRejectReason = fTradeReportRejectReason;
    }
    
    public String getfTradeReportRefID() {
        return fTradeReportRefID;
    }

    public void setfTradeReportRefID(String fTradeReportRefID) {
        this.fTradeReportRefID = fTradeReportRefID;
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
                            case FIX5JonecFieldTag.TRADEREPORTID:
                                setfTradeReportID(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEREPORTTRANSTYPE:
                                setfTradeReportTransType(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEREPORTTYPE:
                                setfTradeReportType(zValue);
                                break;
                            case FIX5JonecFieldTag.TRDRPTSTATUS:
                                setfTrdRptStatus(zValue);
                                break;
                            case FIX5JonecFieldTag.EXECID:
                                setfExecID(zValue);
                                break;
                            case FIX5JonecFieldTag.TRANSACTTIME:
                                setfTransactTime(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEREPORTREJECTREASON:
                                setfTradeReportRejectReason(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEREPORTREFID:
                                setfTradeReportRefID(zValue);
                                break;
                            case FIX5JonecFieldTag.TEXT:
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
            if (!StringHelper.isNullOrEmpty(getfTradeReportID())){
                sb.append(FIX5JonecFieldTag.TRADEREPORTID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeReportID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeReportTransType())){
                sb.append(FIX5JonecFieldTag.TRADEREPORTTRANSTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeReportTransType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeReportType())){
                sb.append(FIX5JonecFieldTag.TRADEREPORTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeReportType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTrdRptStatus())){
                sb.append(FIX5JonecFieldTag.TRDRPTSTATUS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTrdRptStatus()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfExecID())){
                sb.append(FIX5JonecFieldTag.EXECID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfExecID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTransactTime())){
                sb.append(FIX5JonecFieldTag.TRANSACTTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTransactTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeReportRejectReason())){
                sb.append(FIX5JonecFieldTag.TRADEREPORTREJECTREASON).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeReportRejectReason()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeReportRefID())){
                sb.append(FIX5JonecFieldTag.TRADEREPORTREFID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeReportRefID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfText())){
                sb.append(FIX5JonecFieldTag.TEXT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfText()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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