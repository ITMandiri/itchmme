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
    private String fRejectText = "";
    private String fTradeID = "";
    private String fSecondaryTradeID = "";
    private String fTradeHandlingInstr = "";
    private String fExecType = "";
    private String fSymbol = "";
    private String fSecurityID = "";
    private String fSecurityIDSource = "";
    private String fSecurityType = "";
    private String fSecuritySubType = "";
    private String fMatchStatus = "";
    private String fNoSides = "";
    private String fSide = "";

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

    public String getfRejectText() {
        return fRejectText;
    }

    public void setfRejectText(String fRejectText) {
        this.fRejectText = fRejectText;
    }

    public String getfTradeID() {
        return fTradeID;
    }

    public void setfTradeID(String fTradeID) {
        this.fTradeID = fTradeID;
    }

    public String getfSecondaryTradeID() {
        return fSecondaryTradeID;
    }

    public void setfSecondaryTradeID(String fSecondaryTradeID) {
        this.fSecondaryTradeID = fSecondaryTradeID;
    }

    public String getfTradeHandlingInstr() {
        return fTradeHandlingInstr;
    }

    public void setfTradeHandlingInstr(String fTradeHandlingInstr) {
        this.fTradeHandlingInstr = fTradeHandlingInstr;
    }

    public String getfExecType() {
        return fExecType;
    }

    public void setfExecType(String fExecType) {
        this.fExecType = fExecType;
    }

    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }

    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }

    public String getfSecurityIDSource() {
        return fSecurityIDSource;
    }

    public void setfSecurityIDSource(String fSecurityIDSource) {
        this.fSecurityIDSource = fSecurityIDSource;
    }

    public String getfSecurityType() {
        return fSecurityType;
    }

    public void setfSecurityType(String fSecurityType) {
        this.fSecurityType = fSecurityType;
    }

    public String getfSecuritySubType() {
        return fSecuritySubType;
    }

    public void setfSecuritySubType(String fSecuritySubType) {
        this.fSecuritySubType = fSecuritySubType;
    }

    public String getfMatchStatus() {
        return fMatchStatus;
    }

    public void setfMatchStatus(String fMatchStatus) {
        this.fMatchStatus = fMatchStatus;
    }

    public String getfNoSides() {
        return fNoSides;
    }

    public void setfNoSides(String fNoSides) {
        this.fNoSides = fNoSides;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
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
                            case FIX5JonecFieldTag.REJECTTEXT:
                                setfRejectText(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEID:
                                setfTradeID(zValue);
                                break;
                            case FIX5JonecFieldTag.SECONDARYTRADEID:
                                setfSecondaryTradeID(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEHANDLINGINSTR:
                                setfTradeHandlingInstr(zValue);
                                break;
                            case FIX5JonecFieldTag.EXECTYPE:
                                setfExecType(zValue);
                                break;
                            case FIX5JonecFieldTag.SYMBOL:
                                setfSymbol(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYID:
                                setfSecurityID(zValue);
                                break;
                            case FIX5JonecFieldTag.IDSOURCE:
                                setfSecurityIDSource(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYTYPE:
                                setfSecurityType(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYSUBTYPE:
                                setfSecuritySubType(zValue);
                                break;
                            case FIX5JonecFieldTag.MATCHSTATUS:
                                setfMatchStatus(zValue);
                                break;
                            case FIX5JonecFieldTag.NOSIDES:
                                setfNoSides(zValue);
                                break;
                            case FIX5JonecFieldTag.SIDE:
                                setfSide(zValue);
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
            if (!StringHelper.isNullOrEmpty(getfRejectText())){
                sb.append(FIX5JonecFieldTag.REJECTTEXT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfRejectText()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeID())){
                sb.append(FIX5JonecFieldTag.TRADEID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecondaryTradeID())){
                sb.append(FIX5JonecFieldTag.SECONDARYTRADEID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecondaryTradeID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeHandlingInstr())){
                sb.append(FIX5JonecFieldTag.TRADEHANDLINGINSTR).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeHandlingInstr()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfExecType())){
                sb.append(FIX5JonecFieldTag.EXECTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfExecType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSymbol())){
                sb.append(FIX5JonecFieldTag.SYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecurityID())){
                sb.append(FIX5JonecFieldTag.SECURITYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecurityIDSource())){
                sb.append(FIX5JonecFieldTag.IDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityIDSource()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecurityType())){
                sb.append(FIX5JonecFieldTag.SECURITYTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecuritySubType())){
                sb.append(FIX5JonecFieldTag.SECURITYSUBTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecuritySubType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfMatchStatus())){
                sb.append(FIX5JonecFieldTag.MATCHSTATUS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfMatchStatus()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfNoSides())){
                sb.append(FIX5JonecFieldTag.NOSIDES).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoSides()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSide())){
                sb.append(FIX5JonecFieldTag.SIDE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSide()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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