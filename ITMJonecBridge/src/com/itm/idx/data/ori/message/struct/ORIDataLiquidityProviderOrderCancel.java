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
public class ORIDataLiquidityProviderOrderCancel extends ORIDataHeader {
    
    //.fields:
    private String fQuoteID                                     = "";
    private int fQuoteCancelType                                = 0;
    private int fNoQuoteEntries                                 = 0;
    private String fSymbol                                      = "";
    private String fSymbolSfx                                   = "";
    private String fSecurityID                                  = "";
    
    
    public ORIDataLiquidityProviderOrderCancel(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }

    public String getfQuoteID() {
        return fQuoteID;
    }

    public void setfQuoteID(String fQuoteID) {
        this.fQuoteID = fQuoteID;
    }

    public int getfQuoteCancelType() {
        return fQuoteCancelType;
    }

    public void setfQuoteCancelType(int fQuoteCancelType) {
        this.fQuoteCancelType = fQuoteCancelType;
    }

    public int getfNoQuoteEntries() {
        return fNoQuoteEntries;
    }

    public void setfNoQuoteEntries(int fNoQuoteEntries) {
        this.fNoQuoteEntries = fNoQuoteEntries;
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
                        case ORIDataConst.ORIFieldTag.QUOTECANCELTYPE:
                            setfQuoteCancelType(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.NOQUOTEENTRIES:
                            setfNoQuoteEntries(StringHelper.toInt(zValue));
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
