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
    private String fSettlMethod = ""; //.
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
    private String fPartyID1a = ""; //.PARTY_ROLE_24_CUSTOMER_ACCOUNT
    private String fPartyID1b = ""; //.PARTY_ROLE_12_EXECUTING_TRADER
    private String fPartyID1c = ""; //.PARTY_ROLE_1_EXECUTING_FIRM
    private String fPartyID1d = ""; //.PARTY_ROLE_17_CONTRA_FIRM
    private String fPartyID2a = ""; //.PARTY_ROLE_24_CUSTOMER_ACCOUNT
    private String fPartyID2b = ""; //.PARTY_ROLE_12_EXECUTING_TRADER
    private String fPartyID2c = ""; //.PARTY_ROLE_1_EXECUTING_FIRM
    private String fPartyID2d = ""; //.PARTY_ROLE_17_CONTRA_FIRM
    private String fPartyIDSource1a = ""; //.
    private String fPartyIDSource1b = ""; //.
    private String fPartyIDSource1c = ""; //.
    private String fPartyIDSource1d = ""; //.
    private String fPartyIDSource2a = ""; //.
    private String fPartyIDSource2b = ""; //.
    private String fPartyIDSource2c = ""; //.
    private String fPartyIDSource2d = ""; //.
    private String fPartyRole1a = ""; //.
    private String fPartyRole1b = ""; //.
    private String fPartyRole1c = ""; //.
    private String fPartyRole1d = ""; //.
    private String fPartyRole2a = ""; //.
    private String fPartyRole2b = ""; //.
    private String fPartyRole2c = ""; //.
    private String fPartyRole2d = ""; //.
    //.20250822
    private String fNoPartySubIDs1 = ""; //.
    private String fPartySubID1 = ""; //.
    private String fPartySubIDType1 = ""; //.
    private String fNoPartySubIDs2 = ""; //.
    private String fPartySubID2 = ""; //.
    private String fPartySubIDType2 = ""; //.
    private String fSecurityType = ""; //.
    private String fTradeHandlingInstr = ""; //.
    private String fNoLegs = ""; //.
    private String fLegSymbol = ""; //.
    private String fLegSecurityIDSource = ""; //.
    private String fLegSide = ""; //.
    private String fLegLastPx = ""; //.
    private String fLegLastQty = ""; //.
    private String fOrderCapacity1 = ""; //.
    private String fOrderCapacity2 = ""; //.
    private String fTransBkdTime = ""; //.
    private String fSecurityIDSource = ""; //.

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
    
    public String getfSettlMethod() {
        return fSettlMethod;
    }

    public void setfSettlMethod(String fSettlMethod) {
        this.fSettlMethod = fSettlMethod;
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

    public String getfPartyID1c() {
        return fPartyID1c;
    }

    public void setfPartyID1c(String fPartyID1c) {
        this.fPartyID1c = fPartyID1c;
    }

    public String getfPartyID1d() {
        return fPartyID1d;
    }

    public void setfPartyID1d(String fPartyID1d) {
        this.fPartyID1d = fPartyID1d;
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

    public String getfPartyID2c() {
        return fPartyID2c;
    }

    public void setfPartyID2c(String fPartyID2c) {
        this.fPartyID2c = fPartyID2c;
    }

    public String getfPartyID2d() {
        return fPartyID2d;
    }

    public void setfPartyID2d(String fPartyID2d) {
        this.fPartyID2d = fPartyID2d;
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

    public String getfPartyIDSource1c() {
        return fPartyIDSource1c;
    }

    public void setfPartyIDSource1c(String fPartyIDSource1c) {
        this.fPartyIDSource1c = fPartyIDSource1c;
    }

    public String getfPartyIDSource1d() {
        return fPartyIDSource1d;
    }

    public void setfPartyIDSource1d(String fPartyIDSource1d) {
        this.fPartyIDSource1d = fPartyIDSource1d;
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

    public String getfPartyIDSource2c() {
        return fPartyIDSource2c;
    }

    public void setfPartyIDSource2c(String fPartyIDSource2c) {
        this.fPartyIDSource2c = fPartyIDSource2c;
    }

    public String getfPartyIDSource2d() {
        return fPartyIDSource2d;
    }

    public void setfPartyIDSource2d(String fPartyIDSource2d) {
        this.fPartyIDSource2d = fPartyIDSource2d;
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

    public String getfPartyRole1c() {
        return fPartyRole1c;
    }

    public void setfPartyRole1c(String fPartyRole1c) {
        this.fPartyRole1c = fPartyRole1c;
    }

    public String getfPartyRole1d() {
        return fPartyRole1d;
    }

    public void setfPartyRole1d(String fPartyRole1d) {
        this.fPartyRole1d = fPartyRole1d;
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

    public String getfPartyRole2c() {
        return fPartyRole2c;
    }

    public void setfPartyRole2c(String fPartyRole2c) {
        this.fPartyRole2c = fPartyRole2c;
    }

    public String getfPartyRole2d() {
        return fPartyRole2d;
    }

    public void setfPartyRole2d(String fPartyRole2d) {
        this.fPartyRole2d = fPartyRole2d;
    }

    public String getfNoPartySubIDs1() {
        return fNoPartySubIDs1;
    }

    public void setfNoPartySubIDs1(String fNoPartySubIDs1) {
        this.fNoPartySubIDs1 = fNoPartySubIDs1;
    }

    public String getfPartySubID1() {
        return fPartySubID1;
    }

    public void setfPartySubID1(String fPartySubID1) {
        this.fPartySubID1 = fPartySubID1;
    }

    public String getfPartySubIDType1() {
        return fPartySubIDType1;
    }

    public void setfPartySubIDType1(String fPartySubIDType1) {
        this.fPartySubIDType1 = fPartySubIDType1;
    }

    public String getfNoPartySubIDs2() {
        return fNoPartySubIDs2;
    }

    public void setfNoPartySubIDs2(String fNoPartySubIDs2) {
        this.fNoPartySubIDs2 = fNoPartySubIDs2;
    }

    public String getfPartySubID2() {
        return fPartySubID2;
    }

    public void setfPartySubID2(String fPartySubID2) {
        this.fPartySubID2 = fPartySubID2;
    }

    public String getfPartySubIDType2() {
        return fPartySubIDType2;
    }

    public void setfPartySubIDType2(String fPartySubIDType2) {
        this.fPartySubIDType2 = fPartySubIDType2;
    }

    public String getfSecurityType() {
        return fSecurityType;
    }

    public void setfSecurityType(String fSecurityType) {
        this.fSecurityType = fSecurityType;
    }

    public String getfTradeHandlingInstr() {
        return fTradeHandlingInstr;
    }

    public void setfTradeHandlingInstr(String fTradeHandlingInstr) {
        this.fTradeHandlingInstr = fTradeHandlingInstr;
    }

    public String getfNoLegs() {
        return fNoLegs;
    }

    public void setfNoLegs(String fNoLegs) {
        this.fNoLegs = fNoLegs;
    }

    public String getfLegSymbol() {
        return fLegSymbol;
    }

    public void setfLegSymbol(String fLegSymbol) {
        this.fLegSymbol = fLegSymbol;
    }

    public String getfLegSecurityIDSource() {
        return fLegSecurityIDSource;
    }

    public void setfLegSecurityIDSource(String fLegSecurityIDSource) {
        this.fLegSecurityIDSource = fLegSecurityIDSource;
    }

    public String getfLegSide() {
        return fLegSide;
    }

    public void setfLegSide(String fLegSide) {
        this.fLegSide = fLegSide;
    }

    public String getfLegLastPx() {
        return fLegLastPx;
    }

    public void setfLegLastPx(String fLegLastPx) {
        this.fLegLastPx = fLegLastPx;
    }

    public String getfLegLastQty() {
        return fLegLastQty;
    }

    public void setfLegLastQty(String fLegLastQty) {
        this.fLegLastQty = fLegLastQty;
    }

    public String getfOrderCapacity1() {
        return fOrderCapacity1;
    }

    public void setfOrderCapacity1(String fOrderCapacity1) {
        this.fOrderCapacity1 = fOrderCapacity1;
    }

    public String getfOrderCapacity2() {
        return fOrderCapacity2;
    }

    public void setfOrderCapacity2(String fOrderCapacity2) {
        this.fOrderCapacity2 = fOrderCapacity2;
    }

    public String getfTransBkdTime() {
        return fTransBkdTime;
    }

    public void setfTransBkdTime(String fTransBkdTime) {
        this.fTransBkdTime = fTransBkdTime;
    }

    public String getfSecurityIDSource() {
        return fSecurityIDSource;
    }

    public void setfSecurityIDSource(String fSecurityIDSource) {
        this.fSecurityIDSource = fSecurityIDSource;
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
            int cNoPartySubIDs = 0;
            int cPartySubID = 0;
            int cPartySubIDType = 0;
            int cOrderCapacity = 0;
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
                            case FIX5JonecFieldTag.SETTLMETHOD:
                                setfSettlMethod(zValue);
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
                                if (cPartyID == 3) setfPartyID1c(zValue);
                                if (cPartyID == 4) setfPartyID1d(zValue);
                                if (cPartyID == 5) setfPartyID2a(zValue);
                                if (cPartyID == 6) setfPartyID2b(zValue);
                                if (cPartyID == 7) setfPartyID2c(zValue);
                                if (cPartyID == 8) setfPartyID2d(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYIDSOURCE:
                                cPartyIDSource++;
                                if (cPartyIDSource == 1) setfPartyIDSource1a(zValue);
                                if (cPartyIDSource == 2) setfPartyIDSource1b(zValue);
                                if (cPartyIDSource == 3) setfPartyIDSource1c(zValue);
                                if (cPartyIDSource == 4) setfPartyIDSource1d(zValue);
                                if (cPartyIDSource == 5) setfPartyIDSource2a(zValue);
                                if (cPartyIDSource == 6) setfPartyIDSource2b(zValue);
                                if (cPartyIDSource == 7) setfPartyIDSource2c(zValue);
                                if (cPartyIDSource == 8) setfPartyIDSource2d(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYROLE:
                                cPartyRole++;
                                if (cPartyRole == 1) setfPartyRole1a(zValue);
                                if (cPartyRole == 2) setfPartyRole1b(zValue);
                                if (cPartyRole == 3) setfPartyRole1c(zValue);
                                if (cPartyRole == 4) setfPartyRole1d(zValue);
                                if (cPartyRole == 5) setfPartyRole2a(zValue);
                                if (cPartyRole == 6) setfPartyRole2b(zValue);
                                if (cPartyRole == 7) setfPartyRole2c(zValue);
                                if (cPartyRole == 8) setfPartyRole2d(zValue);
                                break;
                            case FIX5JonecFieldTag.NOPARTYSUBIDS:
                                cNoPartySubIDs++;
                                if (cNoPartySubIDs == 1) setfNoPartySubIDs1(zValue);
                                if (cNoPartySubIDs == 2) setfNoPartySubIDs2(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYSUBID:
                                cPartySubID++;
                                if (cPartySubID == 1) setfPartySubID1(zValue);
                                if (cPartySubID == 2) setfPartySubID2(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYSUBIDTYPE:
                                cPartySubIDType++;
                                if (cPartySubIDType == 1) setfPartySubIDType1(zValue);
                                if (cPartySubIDType == 2) setfPartySubIDType2(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDERCAPACITY:
                                cOrderCapacity++;
                                if (cOrderCapacity == 1) setfOrderCapacity1(zValue);
                                if (cOrderCapacity == 2) setfOrderCapacity2(zValue);
                                break;
                            case FIX5JonecFieldTag.SECURITYTYPE:
                                setfSecurityType(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEHANDLINGINSTR:
                                setfTradeHandlingInstr(zValue);
                                break;
                            case FIX5JonecFieldTag.NOLEGS:
                                setfNoLegs(zValue);
                                break;
                            case FIX5JonecFieldTag.LEGSYMBOL:
                                setfLegSymbol(zValue);
                                break;
                            case FIX5JonecFieldTag.LEGSECURITYIDSOURCE:
                                setfLegSecurityIDSource(zValue);
                                break;
                            case FIX5JonecFieldTag.LEGSIDE:
                                setfLegSide(zValue);
                                break;
                            case FIX5JonecFieldTag.LEGLASTPX:
                                setfLegLastPx(zValue);
                                break;
                            case FIX5JonecFieldTag.LEGLASTQTY:
                                setfLegLastQty(zValue);
                                break;
                            case FIX5JonecFieldTag.TRANSBKDTIME:
                                setfTransBkdTime(zValue);
                                break;
                            case FIX5JonecFieldTag.IDSOURCE:
                                setfSecurityIDSource(zValue);
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
            if (!StringHelper.isNullOrEmpty(getfSecurityIDSource())){
                sb.append(FIX5JonecFieldTag.IDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityIDSource()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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
            if (!StringHelper.isNullOrEmpty(getfSettlMethod())){
                sb.append(FIX5JonecFieldTag.SETTLMETHOD).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSettlMethod()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfSymbol())){
                sb.append(FIX5JonecFieldTag.SYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfSecurityType())){
                sb.append(FIX5JonecFieldTag.SECURITYTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfSecurityType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfTradeHandlingInstr())){
                sb.append(FIX5JonecFieldTag.TRADEHANDLINGINSTR).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTradeHandlingInstr()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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
            
            //.side 1
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
            
            //.--------------------------------------------------------------------------------
            
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

            
            
            if (!StringHelper.isNullOrEmpty(getfPartyID1c())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID1c()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource1c())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource1c()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole1c())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole1c()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfNoPartySubIDs1())){
                sb.append(FIX5JonecFieldTag.NOPARTYSUBIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartySubIDs1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartySubID1())){
                sb.append(FIX5JonecFieldTag.PARTYSUBID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartySubID1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartySubIDType1())){
                sb.append(FIX5JonecFieldTag.PARTYSUBIDTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartySubIDType1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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
            
            //..................................
            
            
            if (!StringHelper.isNullOrEmpty(getfPartyID1d())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID1d()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource1d())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource1d()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole1d())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole1d()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            
            if (!StringHelper.isNullOrEmpty(getfOrderCapacity1())){
                sb.append(FIX5JonecFieldTag.ORDERCAPACITY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrderCapacity1()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            //.--------------------------------------------------------------------------------
            
            //.side 2
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
            
            //.--------------------------------------------------------------------------------

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

            
            
            if (!StringHelper.isNullOrEmpty(getfPartyID2c())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID2c()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource2c())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource2c()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole2c())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole2c()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfNoPartySubIDs2())){
                sb.append(FIX5JonecFieldTag.NOPARTYSUBIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoPartySubIDs2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartySubID2())){
                sb.append(FIX5JonecFieldTag.PARTYSUBID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartySubID2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartySubIDType2())){
                sb.append(FIX5JonecFieldTag.PARTYSUBIDTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartySubIDType2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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
            
            
            //.........................
            
            
            if (!StringHelper.isNullOrEmpty(getfPartyID2d())){
                sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyID2d()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyIDSource2d())){
                sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyIDSource2d()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfPartyRole2d())){
                sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfPartyRole2d()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            if (!StringHelper.isNullOrEmpty(getfOrderCapacity2())){
                sb.append(FIX5JonecFieldTag.ORDERCAPACITY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfOrderCapacity2()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            
            //.--------------------------------------------------------------------------------
            //.noLegs
            if (!StringHelper.isNullOrEmpty(getfNoLegs())){
                sb.append(FIX5JonecFieldTag.NOLEGS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfNoLegs()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfLegSymbol())){
                sb.append(FIX5JonecFieldTag.LEGSYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfLegSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfLegSecurityIDSource())){
                sb.append(FIX5JonecFieldTag.LEGSECURITYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfLegSecurityIDSource()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfLegSide())){
                sb.append(FIX5JonecFieldTag.LEGSIDE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfLegSide()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfLegLastPx())){
                sb.append(FIX5JonecFieldTag.LEGLASTPX).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfLegLastPx()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            if (!StringHelper.isNullOrEmpty(getfLegLastQty())){
                sb.append(FIX5JonecFieldTag.LEGLASTQTY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfLegLastQty()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            }
            //......................
            
            if (!StringHelper.isNullOrEmpty(getfTransBkdTime())){
                sb.append(FIX5JonecFieldTag.TRANSBKDTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
                sb.append(getfTransBkdTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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