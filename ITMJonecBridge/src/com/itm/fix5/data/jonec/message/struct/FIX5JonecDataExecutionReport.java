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
public class FIX5JonecDataExecutionReport extends FIX5JonecDataHeader {

    //.reference name: Execution Report (8)

    //.fields:
    private String fClOrdID = "";
    private String fExecID = "";
    private String fExecInst = "";
    private String fOrderID = "";
    private String fSecondaryOrderID = "";
    private String fOrigClOrdID = "";
    private String fExecType = "";
    private String fNoPartyIDs = "";
    private String fPartyID = "";
    private String fPartyIDSource = "";
    private String fPartyRole = "";
    private String fSymbol = "";
    private String fSecuritySubType = "";
    private String fSecurityID = "";
    private String fAccountType = "";
    private String fCumQty = "";
    private String fLastPx = "";
    private String fLastQty = "";
    private String fOrderQty = "";
    private String fOrdStatus = "";
    private String fOrdType = "";
    private String fPrice = "";
    private String fSide = "";
    private String fTimeInForce = "";
    private String fTransactTime = "";
    private String fTradeDate = "";
    private String fExpireTime = "";
    private String fSettlDate = "";
    private String fOrdRejReason = "";
    private String fLeavesQty = "";
    private String fTrdMatchID = "";
    private String fAggressorIndicator = "";
    private String fText = "";
    private String fOrderRestrictions = "";
    private String fMassStatusReqID = "";
    private String fSecondaryClOrdID = "";
    private String fCopyMsgIndicator = "";

    public FIX5JonecDataExecutionReport(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }
    
    public String getfExecID() {
        return fExecID;
    }

    public void setfExecID(String fExecID) {
        this.fExecID = fExecID;
    }
    
    public String getfExecInst() {
        return fExecInst;
    }

    public void setfExecInst(String fExecInst) {
        this.fExecInst = fExecInst;
    }
    
    public String getfOrderID() {
        return fOrderID;
    }

    public void setfOrderID(String fOrderID) {
        this.fOrderID = fOrderID;
    }
    
    public String getfSecondaryOrderID() {
        return fSecondaryOrderID;
    }

    public void setfSecondaryOrderID(String fSecondaryOrderID) {
        this.fSecondaryOrderID = fSecondaryOrderID;
    }
    
    public String getfOrigClOrdID() {
        return fOrigClOrdID;
    }

    public void setfOrigClOrdID(String fOrigClOrdID) {
        this.fOrigClOrdID = fOrigClOrdID;
    }
    
    public String getfExecType() {
        return fExecType;
    }

    public void setfExecType(String fExecType) {
        this.fExecType = fExecType;
    }
    
    public String getfNoPartyIDs() {
        return fNoPartyIDs;
    }

    public void setfNoPartyIDs(String fNoPartyIDs) {
        this.fNoPartyIDs = fNoPartyIDs;
    }
    
    public String getfPartyID() {
        return fPartyID;
    }

    public void setfPartyID(String fPartyID) {
        this.fPartyID = fPartyID;
    }
    
    public String getfPartyIDSource() {
        return fPartyIDSource;
    }

    public void setfPartyIDSource(String fPartyIDSource) {
        this.fPartyIDSource = fPartyIDSource;
    }
    
    public String getfPartyRole() {
        return fPartyRole;
    }

    public void setfPartyRole(String fPartyRole) {
        this.fPartyRole = fPartyRole;
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
    
    public String getfAccountType() {
        return fAccountType;
    }

    public void setfAccountType(String fAccountType) {
        this.fAccountType = fAccountType;
    }
    
    public String getfCumQty() {
        return fCumQty;
    }

    public void setfCumQty(String fCumQty) {
        this.fCumQty = fCumQty;
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
    
    public String getfOrderQty() {
        return fOrderQty;
    }

    public void setfOrderQty(String fOrderQty) {
        this.fOrderQty = fOrderQty;
    }
    
    public String getfOrdStatus() {
        return fOrdStatus;
    }

    public void setfOrdStatus(String fOrdStatus) {
        this.fOrdStatus = fOrdStatus;
    }
    
    public String getfOrdType() {
        return fOrdType;
    }

    public void setfOrdType(String fOrdType) {
        this.fOrdType = fOrdType;
    }
    
    public String getfPrice() {
        return fPrice;
    }

    public void setfPrice(String fPrice) {
        this.fPrice = fPrice;
    }
    
    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }
    
    public String getfTimeInForce() {
        return fTimeInForce;
    }

    public void setfTimeInForce(String fTimeInForce) {
        this.fTimeInForce = fTimeInForce;
    }
    
    public String getfTransactTime() {
        return fTransactTime;
    }

    public void setfTransactTime(String fTransactTime) {
        this.fTransactTime = fTransactTime;
    }
    
    public String getfTradeDate() {
        return fTradeDate;
    }

    public void setfTradeDate(String fTradeDate) {
        this.fTradeDate = fTradeDate;
    }
    
    public String getfExpireTime() {
        return fExpireTime;
    }

    public void setfExpireTime(String fExpireTime) {
        this.fExpireTime = fExpireTime;
    }
    
    public String getfSettlDate() {
        return fSettlDate;
    }

    public void setfSettlDate(String fSettlDate) {
        this.fSettlDate = fSettlDate;
    }
    
    public String getfOrdRejReason() {
        return fOrdRejReason;
    }

    public void setfOrdRejReason(String fOrdRejReason) {
        this.fOrdRejReason = fOrdRejReason;
    }
    
    public String getfLeavesQty() {
        return fLeavesQty;
    }

    public void setfLeavesQty(String fLeavesQty) {
        this.fLeavesQty = fLeavesQty;
    }
    
    public String getfTrdMatchID() {
        return fTrdMatchID;
    }

    public void setfTrdMatchID(String fTrdMatchID) {
        this.fTrdMatchID = fTrdMatchID;
    }
    
    public String getfAggressorIndicator() {
        return fAggressorIndicator;
    }

    public void setfAggressorIndicator(String fAggressorIndicator) {
        this.fAggressorIndicator = fAggressorIndicator;
    }
    
    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }
    
    public String getfOrderRestrictions() {
        return fOrderRestrictions;
    }

    public void setfOrderRestrictions(String fOrderRestrictions) {
        this.fOrderRestrictions = fOrderRestrictions;
    }
    
    public String getfMassStatusReqID() {
        return fMassStatusReqID;
    }

    public void setfMassStatusReqID(String fMassStatusReqID) {
        this.fMassStatusReqID = fMassStatusReqID;
    }
    
    public String getfSecondaryClOrdID() {
        return fSecondaryClOrdID;
    }

    public void setfSecondaryClOrdID(String fSecondaryClOrdID) {
        this.fSecondaryClOrdID = fSecondaryClOrdID;
    }
    
    public String getfCopyMsgIndicator() {
        return fCopyMsgIndicator;
    }

    public void setfCopyMsgIndicator(String fCopyMsgIndicator) {
        this.fCopyMsgIndicator = fCopyMsgIndicator;
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
                            case FIX5JonecFieldTag.CLORDID:
                                setfClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.EXECID:
                                setfExecID(zValue);
                                break;
                            case FIX5JonecFieldTag.EXECINST:
                                setfExecInst(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDERID:
                                setfOrderID(zValue);
                                break;
                            case FIX5JonecFieldTag.SECONDARYORDERID:
                                setfSecondaryOrderID(zValue);
                                break;
                            case FIX5JonecFieldTag.ORIGCLORDID:
                                setfOrigClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.EXECTYPE:
                                setfExecType(zValue);
                                break;
                            case FIX5JonecFieldTag.NOPARTYIDS:
                                setfNoPartyIDs(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYID:
                                setfPartyID(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYIDSOURCE:
                                setfPartyIDSource(zValue);
                                break;
                            case FIX5JonecFieldTag.PARTYROLE:
                                setfPartyRole(zValue);
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
                            case FIX5JonecFieldTag.ACCOUNTTYPE:
                                setfAccountType(zValue);
                                break;
                            case FIX5JonecFieldTag.CUMQTY:
                                setfCumQty(zValue);
                                break;
                            case FIX5JonecFieldTag.LASTPX:
                                setfLastPx(zValue);
                                break;
                            case FIX5JonecFieldTag.LASTQTY:
                                setfLastQty(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDERQTY:
                                setfOrderQty(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDSTATUS:
                                setfOrdStatus(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDTYPE:
                                setfOrdType(zValue);
                                break;
                            case FIX5JonecFieldTag.PRICE:
                                setfPrice(zValue);
                                break;
                            case FIX5JonecFieldTag.SIDE:
                                setfSide(zValue);
                                break;
                            case FIX5JonecFieldTag.TIMEINFORCE:
                                setfTimeInForce(zValue);
                                break;
                            case FIX5JonecFieldTag.TRANSACTTIME:
                                setfTransactTime(zValue);
                                break;
                            case FIX5JonecFieldTag.TRADEDATE:
                                setfTradeDate(zValue);
                                break;
                            case FIX5JonecFieldTag.EXPIRETIME:
                                setfExpireTime(zValue);
                                break;
                            case FIX5JonecFieldTag.SETTLDATE:
                                setfSettlDate(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDREJREASON:
                                setfOrdRejReason(zValue);
                                break;
                            case FIX5JonecFieldTag.LEAVESQTY:
                                setfLeavesQty(zValue);
                                break;
                            case FIX5JonecFieldTag.TRDMATCHID:
                                setfTrdMatchID(zValue);
                                break;
                            case FIX5JonecFieldTag.AGGRESSORINDICATOR:
                                setfAggressorIndicator(zValue);
                                break;
                            case FIX5JonecFieldTag.TEXT:
                                setfText(zValue);
                                break;
                            case FIX5JonecFieldTag.ORDERRESTRICTIONS:
                                setfOrderRestrictions(zValue);
                                break;
                            case FIX5JonecFieldTag.MASSSTATUSREQID:
                                setfMassStatusReqID(zValue);
                                break;
                            case FIX5JonecFieldTag.SECONDARYCLORDID:
                                setfSecondaryClOrdID(zValue);
                                break;
                            case FIX5JonecFieldTag.COPYMSGINDICATOR:
                                setfCopyMsgIndicator(zValue);
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
            sb.append(FIX5JonecFieldTag.CLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.EXECID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfExecID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.EXECINST).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfExecInst()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDERID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECONDARYORDERID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecondaryOrderID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORIGCLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrigClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.EXECTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfExecType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.NOPARTYIDS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNoPartyIDs()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYIDSOURCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyIDSource()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PARTYROLE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPartyRole()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECURITYSUBTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecuritySubType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECURITYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecurityID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ACCOUNTTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfAccountType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.CUMQTY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfCumQty()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.LASTPX).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfLastPx()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.LASTQTY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfLastQty()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDERQTY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderQty()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDSTATUS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrdStatus()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrdType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.PRICE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfPrice()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SIDE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSide()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TIMEINFORCE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTimeInForce()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TRANSACTTIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTransactTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TRADEDATE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTradeDate()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.EXPIRETIME).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfExpireTime()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SETTLDATE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSettlDate()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDREJREASON).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrdRejReason()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.LEAVESQTY).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfLeavesQty()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TRDMATCHID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTrdMatchID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.AGGRESSORINDICATOR).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfAggressorIndicator()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TEXT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfText()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.ORDERRESTRICTIONS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOrderRestrictions()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.MASSSTATUSREQID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfMassStatusReqID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECONDARYCLORDID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecondaryClOrdID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.COPYMSGINDICATOR).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfCopyMsgIndicator()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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