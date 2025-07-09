/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataNewLiquidityProviderOrder extends ORIDataHeader {
    
    //.fields:
    private String fQuoteID                                     = "";
    private String fAccount                                     = "";
    private String fSymbol                                      = "";
    private String fSymbolSfx                                   = "";
    private String fSecurityID                                  = "";
    private String fValidUntilTime                              = "";
    private double fBidPx                                       = 0;
    private double fOfferPx                                     = 0;
    private long fBidSize                                       = 0;
    private long fOfferSize                                     = 0;
    private String fComplianceID                                = "";
    
    
    public ORIDataNewLiquidityProviderOrder(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }

    public String getfQuoteID() {
        return fQuoteID;
    }

    public void setfQuoteID(String fQuoteID) {
        this.fQuoteID = fQuoteID;
    }

    public String getfAccount() {
        return fAccount;
    }

    public void setfAccount(String fAccount) {
        this.fAccount = fAccount;
    }

    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }

    public String getfSymbolSfx() {
        return fSymbolSfx;
    }

    public void setfSymbolSfx(String fSymbolSfx) {
        this.fSymbolSfx = fSymbolSfx;
    }

    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }

    public String getfValidUntilTime() {
        return fValidUntilTime;
    }

    public void setfValidUntilTime(String fValidUntilTime) {
        this.fValidUntilTime = fValidUntilTime;
    }

    public double getfBidPx() {
        return fBidPx;
    }

    public void setfBidPx(double fBidPx) {
        this.fBidPx = fBidPx;
    }

    public double getfOfferPx() {
        return fOfferPx;
    }

    public void setfOfferPx(double fOfferPx) {
        this.fOfferPx = fOfferPx;
    }

    public long getfBidSize() {
        return fBidSize;
    }

    public void setfBidSize(long fBidSize) {
        this.fBidSize = fBidSize;
    }

    public long getfOfferSize() {
        return fOfferSize;
    }

    public void setfOfferSize(long fOfferSize) {
        this.fOfferSize = fOfferSize;
    }

    public String getfComplianceID() {
        return fComplianceID;
    }

    public void setfComplianceID(String fComplianceID) {
        this.fComplianceID = fComplianceID;
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
                        case ORIDataConst.ORIFieldTag.ACCOUNT:
                            setfAccount(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SYMBOL:
                            setfSymbol(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SYMBOLSFX:
                            setfSymbolSfx(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SECURITYID:
                            setfSecurityID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.VALIDUNTILTIME:
                            setfValidUntilTime(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.BIDPX:
                            setfBidPx(StringHelper.toDouble(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.OFFERPX:
                            setfOfferPx(StringHelper.toDouble(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.BIDSIZE:
                            setfBidSize(StringHelper.toLong(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.OFFERSIZE:
                            setfOfferSize(StringHelper.toLong(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.COMPLIANCEID:
                            setfComplianceID(zValue);
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
    
}
