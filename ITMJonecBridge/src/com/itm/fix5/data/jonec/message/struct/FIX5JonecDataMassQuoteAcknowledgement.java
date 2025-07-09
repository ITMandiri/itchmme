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
public class FIX5JonecDataMassQuoteAcknowledgement extends FIX5JonecDataHeader {

    //.reference name: Mass Quote Acknowledgement (b)

    //.fields:
    private String fQuoteID = "";
    private String fQuoteRejectReason = "";
    private String fText = "";
    private String fNoQuoteSets = "";
    private String fQuoteSetID = "";
    private String fTotNoQuoteEntries = "";
    private String fNoQuoteEntries = "";
    private String fQuoteEntryID = "";
    private String fSymbol = "";
    private String fSecuritySubType = "";
    private String fSecurityID = "";
    private String fBidPx = "";
    private String fOfferPx = "";
    private String fBidSize = "";
    private String fOfferSize = "";

    public FIX5JonecDataMassQuoteAcknowledgement(Map<String, ArrayList<String>> inputMsgFields) {
        super(inputMsgFields);
    }
    
    //.getsets:
    public String getfQuoteID() {
        return fQuoteID;
    }

    public void setfQuoteID(String fQuoteID) {
        this.fQuoteID = fQuoteID;
    }
    
    public String getfQuoteRejectReason() {
        return fQuoteRejectReason;
    }

    public void setfQuoteRejectReason(String fQuoteRejectReason) {
        this.fQuoteRejectReason = fQuoteRejectReason;
    }
    
    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }
    
    public String getfNoQuoteSets() {
        return fNoQuoteSets;
    }

    public void setfNoQuoteSets(String fNoQuoteSets) {
        this.fNoQuoteSets = fNoQuoteSets;
    }
    
    public String getfQuoteSetID() {
        return fQuoteSetID;
    }

    public void setfQuoteSetID(String fQuoteSetID) {
        this.fQuoteSetID = fQuoteSetID;
    }
    
    public String getfTotNoQuoteEntries() {
        return fTotNoQuoteEntries;
    }

    public void setfTotNoQuoteEntries(String fTotNoQuoteEntries) {
        this.fTotNoQuoteEntries = fTotNoQuoteEntries;
    }
    
    public String getfNoQuoteEntries() {
        return fNoQuoteEntries;
    }

    public void setfNoQuoteEntries(String fNoQuoteEntries) {
        this.fNoQuoteEntries = fNoQuoteEntries;
    }
    
    public String getfQuoteEntryID() {
        return fQuoteEntryID;
    }

    public void setfQuoteEntryID(String fQuoteEntryID) {
        this.fQuoteEntryID = fQuoteEntryID;
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
    
    public String getfBidPx() {
        return fBidPx;
    }

    public void setfBidPx(String fBidPx) {
        this.fBidPx = fBidPx;
    }
    
    public String getfOfferPx() {
        return fOfferPx;
    }

    public void setfOfferPx(String fOfferPx) {
        this.fOfferPx = fOfferPx;
    }
    
    public String getfBidSize() {
        return fBidSize;
    }

    public void setfBidSize(String fBidSize) {
        this.fBidSize = fBidSize;
    }
    
    public String getfOfferSize() {
        return fOfferSize;
    }

    public void setfOfferSize(String fOfferSize) {
        this.fOfferSize = fOfferSize;
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
                            case FIX5JonecFieldTag.QUOTEID:
                                setfQuoteID(zValue);
                                break;
                            case FIX5JonecFieldTag.QUOTEREJECTREASON:
                                setfQuoteRejectReason(zValue);
                                break;
                            case FIX5JonecFieldTag.TEXT:
                                setfText(zValue);
                                break;
                            case FIX5JonecFieldTag.NOQUOTESETS:
                                setfNoQuoteSets(zValue);
                                break;
                            case FIX5JonecFieldTag.QUOTESETID:
                                setfQuoteSetID(zValue);
                                break;
                            case FIX5JonecFieldTag.TOTNOQUOTEENTRIES:
                                setfTotNoQuoteEntries(zValue);
                                break;
                            case FIX5JonecFieldTag.NOQUOTEENTRIES:
                                setfNoQuoteEntries(zValue);
                                break;
                            case FIX5JonecFieldTag.QUOTEENTRYID:
                                setfQuoteEntryID(zValue);
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
                            case FIX5JonecFieldTag.BIDPX:
                                setfBidPx(zValue);
                                break;
                            case FIX5JonecFieldTag.OFFERPX:
                                setfOfferPx(zValue);
                                break;
                            case FIX5JonecFieldTag.BIDSIZE:
                                setfBidSize(zValue);
                                break;
                            case FIX5JonecFieldTag.OFFERSIZE:
                                setfOfferSize(zValue);
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
            sb.append(FIX5JonecFieldTag.QUOTEID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfQuoteID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.QUOTEREJECTREASON).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfQuoteRejectReason()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TEXT).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfText()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.NOQUOTESETS).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNoQuoteSets()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.QUOTESETID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfQuoteSetID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.TOTNOQUOTEENTRIES).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfTotNoQuoteEntries()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.NOQUOTEENTRIES).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfNoQuoteEntries()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.QUOTEENTRYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfQuoteEntryID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SYMBOL).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSymbol()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECURITYSUBTYPE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecuritySubType()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.SECURITYID).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfSecurityID()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.BIDPX).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfBidPx()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.OFFERPX).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOfferPx()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.BIDSIZE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfBidSize()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
            sb.append(FIX5JonecFieldTag.OFFERSIZE).append(FIX5JonecFieldFmt.KV_SEPARATOR);
            sb.append(getfOfferSize()).append(FIX5JonecFieldFmt.FIELD_SEPARATOR);
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