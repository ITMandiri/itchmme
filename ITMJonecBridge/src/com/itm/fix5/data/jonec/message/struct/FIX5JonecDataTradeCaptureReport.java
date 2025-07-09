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
public class FIX5JonecDataTradeCaptureReport extends FIX5JonecDataHeader {

    //.reference name: Trade Capture Report (AE)

    //.fields:
    private String fTradeReportID = ""; //.
    private String fExecID = "";
    private String fTradeReportTransType = ""; //.
    private String fTradeReportType = ""; //.
    private String fTradeReportRefID = "";
    private String fMatchType = "";
    private String fTrdMatchID = "";
    private String fSettlDate = ""; //.
    private String fDeliveryType = ""; //.
    private String fSymbol = ""; //.
    private String fSecuritySubType = ""; //.
    private String fSecurityID = "";
    private String fLastPx = ""; //.
    private String fLastQty = ""; //.
    private String fTransactTime = "";
    private String fNoSides = ""; //.
    private String fSide1 = ""; //.
    private String fSide2 = ""; //.
    private String fAccountType1 = ""; //.
    private String fAccountType2 = ""; //.
    private String fNoPartyIDs1 = ""; //.
    private String fNoPartyIDs2 = ""; //.
    private String fPartyID1a = ""; //.
    private String fPartyID1b = ""; //.
    private String fPartyID2a = ""; //.
    private String fPartyID2b = ""; //.
    private String fPartyID3a = ""; //.
    private String fPartyID3b = ""; //.
    private String fPartyIDSource1a = ""; //.
    private String fPartyIDSource1b = ""; //.
    private String fPartyIDSource2a = ""; //.
    private String fPartyIDSource2b = ""; //.
    private String fPartyIDSource3a = ""; //.
    private String fPartyIDSource3b = ""; //.
    private String fPartyRole1a = ""; //.
    private String fPartyRole1b = ""; //.
    private String fPartyRole2a = ""; //.
    private String fPartyRole2b = ""; //.
    private String fPartyRole3a = ""; //.
    private String fPartyRole3b = ""; //.

    public FIX5JonecDataTradeCaptureReport(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfTradeReportID() {
        return fTradeReportID;
    }

    public void setfTradeReportID(String fTradeReportID) {
        this.fTradeReportID = fTradeReportID;
    }
    
    public String getfExecID() {
        return fExecID;
    }

    public void setfExecID(String fExecID) {
        this.fExecID = fExecID;
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
    
    public String getfTradeReportRefID() {
        return fTradeReportRefID;
    }

    public void setfTradeReportRefID(String fTradeReportRefID) {
        this.fTradeReportRefID = fTradeReportRefID;
    }
    
    public String getfMatchType() {
        return fMatchType;
    }

    public void setfMatchType(String fMatchType) {
        this.fMatchType = fMatchType;
    }
    
    public String getfTrdMatchID() {
        return fTrdMatchID;
    }

    public void setfTrdMatchID(String fTrdMatchID) {
        this.fTrdMatchID = fTrdMatchID;
    }
    
    public String getfSettlDate() {
        return fSettlDate;
    }

    public void setfSettlDate(String fSettlDate) {
        this.fSettlDate = fSettlDate;
    }
    
    public String getfDeliveryType() {
        return fDeliveryType;
    }

    public void setfDeliveryType(String fDeliveryType) {
        this.fDeliveryType = fDeliveryType;
    }
    
    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }
    
    public String getfSecuritySubType() {
        return fSecuritySubType;
    }

    public void setfSecuritySubType(String fSecuritySubType) {
        this.fSecuritySubType = fSecuritySubType;
    }
    
    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }
    
    public String getfLastPx() {
        return fLastPx;
    }

    public void setfLastPx(String fLastPx) {
        this.fLastPx = fLastPx;
    }
    
    public String getfLastQty() {
        return fLastQty;
    }

    public void setfLastQty(String fLastQty) {
        this.fLastQty = fLastQty;
    }
    
    public String getfTransactTime() {
        return fTransactTime;
    }

    public void setfTransactTime(String fTransactTime) {
        this.fTransactTime = fTransactTime;
    }
    
    public String getfNoSides() {
        return fNoSides;
    }

    public void setfNoSides(String fNoSides) {
        this.fNoSides = fNoSides;
    }
    
    public String getfSide1() {
        return fSide1;
    }

    public void setfSide1(String fSide1) {
        this.fSide1 = fSide1;
    }
    
    public String getfSide2() {
        return fSide2;
    }

    public void setfSide2(String fSide2) {
        this.fSide2 = fSide2;
    }
    
    public String getfAccountType1() {
        return fAccountType1;
    }

    public void setfAccountType1(String fAccountType1) {
        this.fAccountType1 = fAccountType1;
    }
    
    public String getfAccountType2() {
        return fAccountType2;
    }

    public void setfAccountType2(String fAccountType2) {
        this.fAccountType2 = fAccountType2;
    }
    
    public String getfNoPartyIDs1() {
        return fNoPartyIDs1;
    }

    public void setfNoPartyIDs1(String fNoPartyIDs1) {
        this.fNoPartyIDs1 = fNoPartyIDs1;
    }
    
    public String getfNoPartyIDs2() {
        return fNoPartyIDs2;
    }

    public void setfNoPartyIDs2(String fNoPartyIDs2) {
        this.fNoPartyIDs2 = fNoPartyIDs2;
    }
    
    public String getfPartyID1a() {
        return fPartyID1a;
    }

    public void setfPartyID1a(String fPartyID1a) {
        this.fPartyID1a = fPartyID1a;
    }
    
    public String getfPartyID1b() {
        return fPartyID1b;
    }

    public void setfPartyID1b(String fPartyID1b) {
        this.fPartyID1b = fPartyID1b;
    }
    
    public String getfPartyID2a() {
        return fPartyID2a;
    }

    public void setfPartyID2a(String fPartyID2a) {
        this.fPartyID2a = fPartyID2a;
    }
    
    public String getfPartyID2b() {
        return fPartyID2b;
    }

    public void setfPartyID2b(String fPartyID2b) {
        this.fPartyID2b = fPartyID2b;
    }
    
    public String getfPartyID3a() {
        return fPartyID3a;
    }

    public void setfPartyID3a(String fPartyID3a) {
        this.fPartyID3a = fPartyID3a;
    }
    
    public String getfPartyID3b() {
        return fPartyID3b;
    }

    public void setfPartyID3b(String fPartyID3b) {
        this.fPartyID3b = fPartyID3b;
    }
    
    public String getfPartyIDSource1a() {
        return fPartyIDSource1a;
    }

    public void setfPartyIDSource1a(String fPartyIDSource1a) {
        this.fPartyIDSource1a = fPartyIDSource1a;
    }
    
    public String getfPartyIDSource1b() {
        return fPartyIDSource1b;
    }

    public void setfPartyIDSource1b(String fPartyIDSource1b) {
        this.fPartyIDSource1b = fPartyIDSource1b;
    }
    
    public String getfPartyIDSource2a() {
        return fPartyIDSource2a;
    }

    public void setfPartyIDSource2a(String fPartyIDSource2a) {
        this.fPartyIDSource2a = fPartyIDSource2a;
    }
    
    public String getfPartyIDSource2b() {
        return fPartyIDSource2b;
    }

    public void setfPartyIDSource2b(String fPartyIDSource2b) {
        this.fPartyIDSource2b = fPartyIDSource2b;
    }
    
    public String getfPartyIDSource3a() {
        return fPartyIDSource3a;
    }

    public void setfPartyIDSource3a(String fPartyIDSource3a) {
        this.fPartyIDSource3a = fPartyIDSource3a;
    }
    
    public String getfPartyIDSource3b() {
        return fPartyIDSource3b;
    }

    public void setfPartyIDSource3b(String fPartyIDSource3b) {
        this.fPartyIDSource3b = fPartyIDSource3b;
    }
    
    public String getfPartyRole1a() {
        return fPartyRole1a;
    }

    public void setfPartyRole1a(String fPartyRole1a) {
        this.fPartyRole1a = fPartyRole1a;
    }
    
    public String getfPartyRole1b() {
        return fPartyRole1b;
    }

    public void setfPartyRole1b(String fPartyRole1b) {
        this.fPartyRole1b = fPartyRole1b;
    }
    
    public String getfPartyRole2a() {
        return fPartyRole2a;
    }

    public void setfPartyRole2a(String fPartyRole2a) {
        this.fPartyRole2a = fPartyRole2a;
    }
    
    public String getfPartyRole2b() {
        return fPartyRole2b;
    }

    public void setfPartyRole2b(String fPartyRole2b) {
        this.fPartyRole2b = fPartyRole2b;
    }
    
    public String getfPartyRole3a() {
        return fPartyRole3a;
    }

    public void setfPartyRole3a(String fPartyRole3a) {
        this.fPartyRole3a = fPartyRole3a;
    }
    
    public String getfPartyRole3b() {
        return fPartyRole3b;
    }

    public void setfPartyRole3b(String fPartyRole3b) {
        this.fPartyRole3b = fPartyRole3b;
    }
    
    
    //.process:
    public boolean assignMessage(){
        boolean bOut = false;
        try{
            //.assign header:
            assignHeaderMessage();
            //.assign data:
            int cSide = 0;
            int cAccountType = 0;
            int cNoPartyIDs = 0;
            int cPartyID = 0;
            int cPartyIDSource = 0;
            int cPartyRole = 0;
            Map<String, ArrayList<String>> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    ///String zValue = mapFields.get(zKey);
                    for(String zValue : mapFields.get(zKey)){
                        switch(zKey){
                            case FIX5JonecFieldTag.TRADEREPORTID:
                                setfTradeReportID(zValue);
                                break;
                            case FIX5JonecFieldTag.EXECID:
                                setfExecID(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEREPORTTRANSTYPE:
                                setfTradeReportTransType(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEREPORTTYPE:
                                setfTradeReportType(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEREPORTREFID:
                                setfTradeReportRefID(zValue);
                                break;
                            case FIX5JonecFieldTag.MATCHTYPE:
                                setfMatchType(zValue);
                                break;
                            case FIX5JonecFieldTag.TRDMATCHID:
                                setfTrdMatchID(zValue);
                                break;
                            case FIX5JonecFieldTag.SETTLDATE:
                                setfSettlDate(zValue);
                                break;
                            case FIX5JonecFieldTag.DELIVERYTYPE:
                                setfDeliveryType(zValue);
                                break;
                            case FIX5JonecFieldTag.SYMBOL:
                                setfSymbol(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYSUBTYPE:
                                setfSecuritySubType(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYID:
                                setfSecurityID(zValue);
                                break;
                            case FIX5JonecFieldTag.LASTPX:
                                setfLastPx(zValue);
                                break;
                            case FIX5JonecFieldTag.LASTQTY:
                                setfLastQty(zValue);
                                break;
                            case FIX5JonecFieldTag.TRANSACTTIME:
                                setfTransactTime(zValue);
                                break;
                            case FIX5JonecFieldTag.NOSIDES:
                                setfNoSides(zValue);
                                break;
                            case FIX5JonecFieldTag.SIDE:
                                cSide++;
                                if (cSide == 1) setfSide1(zValue);
                                if (cSide == 2) setfSide2(zValue);
                                break;
                            case FIX5JonecFieldTag.ACCOUNTTYPE:
                                cAccountType++;
                                if (cAccountType == 1) setfAccountType1(zValue);
                                if (cAccountType == 2) setfAccountType2(zValue);
                                break;
                            case FIX5JonecFieldTag.NOPARTYIDS:
                                cNoPartyIDs++;
                                if (cNoPartyIDs == 1) setfNoPartyIDs1(zValue);
                                if (cNoPartyIDs == 2) setfNoPartyIDs2(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYID:
                                cPartyID++;
                                if (cPartyID == 1) setfPartyID1a(zValue);
                                if (cPartyID == 2) setfPartyID1b(zValue);
                                if (cPartyID == 3) setfPartyID2a(zValue);
                                if (cPartyID == 4) setfPartyID2b(zValue);
                                if (cPartyID == 5) setfPartyID3a(zValue);
                                if (cPartyID == 6) setfPartyID3b(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYIDSOURCE:
                                cPartyIDSource++;
                                if (cPartyIDSource == 1) setfPartyIDSource1a(zValue);
                                if (cPartyIDSource == 2) setfPartyIDSource1b(zValue);
                                if (cPartyIDSource == 3) setfPartyIDSource2a(zValue);
                                if (cPartyIDSource == 4) setfPartyIDSource2b(zValue);
                                if (cPartyIDSource == 5) setfPartyIDSource3a(zValue);
                                if (cPartyIDSource == 6) setfPartyIDSource3b(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYROLE:
                                cPartyRole++;
                                if (cPartyRole == 1) setfPartyRole1a(zValue);
                                if (cPartyRole == 2) setfPartyRole1b(zValue);
                                if (cPartyRole == 3) setfPartyRole2a(zValue);
                                if (cPartyRole == 4) setfPartyRole2b(zValue);
                                if (cPartyRole == 5) setfPartyRole3a(zValue);
                                if (cPartyRole == 6) setfPartyRole3b(zValue);
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
            if (!StringHelper.isNullOrEmpty(getfExecID())){
                sb.append(FIX5JonecFieldTag.EXECID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfExecID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeReportTransType())){
                sb.append(FIX5JonecFieldTag.TRADEREPORTTRANSTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeReportTransType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeReportType())){
                sb.append(FIX5JonecFieldTag.TRADEREPORTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeReportType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTradeReportRefID())){
                sb.append(FIX5JonecFieldTag.TRADEREPORTREFID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeReportRefID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfMatchType())){
                sb.append(FIX5JonecFieldTag.MATCHTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfMatchType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTrdMatchID())){
                sb.append(FIX5JonecFieldTag.TRDMATCHID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTrdMatchID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSettlDate())){
                sb.append(FIX5JonecFieldTag.SETTLDATE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSettlDate()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfDeliveryType())){
                sb.append(FIX5JonecFieldTag.DELIVERYTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfDeliveryType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSymbol())){
                sb.append(FIX5JonecFieldTag.SYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecuritySubType())){
                sb.append(FIX5JonecFieldTag.SECURITYSUBTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecuritySubType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSecurityID())){
                sb.append(FIX5JonecFieldTag.SECURITYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfLastPx())){
                sb.append(FIX5JonecFieldTag.LASTPX).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfLastPx()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfLastQty())){
                sb.append(FIX5JonecFieldTag.LASTQTY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfLastQty()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfTransactTime())){
                sb.append(FIX5JonecFieldTag.TRANSACTTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTransactTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfNoSides())){
                sb.append(FIX5JonecFieldTag.NOSIDES).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoSides()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            
            
            if (!StringHelper.isNullOrEmpty(getfSide1())){
                sb.append(FIX5JonecFieldTag.SIDE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSide1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfAccountType1())){
                sb.append(FIX5JonecFieldTag.ACCOUNTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfAccountType1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfNoPartyIDs1())){
                sb.append(FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartyIDs1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyID1a())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID1a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource1a())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource1a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole1a())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole1a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyID1b())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID1b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource1b())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource1b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole1b())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole1b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            
            
            if (!StringHelper.isNullOrEmpty(getfSide2())){
                sb.append(FIX5JonecFieldTag.SIDE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSide2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfAccountType2())){
                sb.append(FIX5JonecFieldTag.ACCOUNTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfAccountType2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfNoPartyIDs2())){
                sb.append(FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartyIDs2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyID2a())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID2a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource2a())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource2a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole2a())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole2a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyID2b())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID2b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource2b())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource2b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole2b())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole2b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            
            
            if (!StringHelper.isNullOrEmpty(getfPartyID3a())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID3a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource3a())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource3a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole3a())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole3a()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            
            
            if (!StringHelper.isNullOrEmpty(getfPartyID3b())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID3b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource3b())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource3b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole3b())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole3b()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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