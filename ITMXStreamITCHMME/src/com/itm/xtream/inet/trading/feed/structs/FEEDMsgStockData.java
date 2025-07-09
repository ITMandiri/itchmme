/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.structs;

import com.itm.xtream.inet.trading.feed.util.FEEDMsgConst;

/**
 *
 * @author fredy
 */
public class FEEDMsgStockData extends FEEDMsgBase{
    private String securityCode;
    private String securityName;
    private String securityStatus;
    private String securityType;
    private String subSector;
    private String ipoPrice;
    private String basePrice;
    private String listedShare;
    private String tradeableListedShare;
    private String sharePerLot;
    private String remarks;
    private String remarks2;
    private String weight;
    
    
    public FEEDMsgStockData(){
        setRecType("3");
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public String getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(String securityStatus) {
        this.securityStatus = securityStatus;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getSubSector() {
        return subSector;
    }

    public void setSubSector(String subSector) {
        this.subSector = subSector;
    }

    public String getIpoPrice() {
        return ipoPrice;
    }

    public void setIpoPrice(String ipoPrice) {
        this.ipoPrice = ipoPrice;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getListedShare() {
        return listedShare;
    }

    public void setListedShare(String listedShare) {
        this.listedShare = listedShare;
    }

    public String getTradeableListedShare() {
        return tradeableListedShare;
    }

    public void setTradeableListedShare(String tradeableListedShare) {
        this.tradeableListedShare = tradeableListedShare;
    }

    public String getSharePerLot() {
        return sharePerLot;
    }

    public void setSharePerLot(String sharePerLot) {
        this.sharePerLot = sharePerLot;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getSecurityCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSecurityName()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSecurityStatus()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSecurityType()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSubSector()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getIpoPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBasePrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getListedShare()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getTradeableListedShare()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSharePerLot()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getRemarks()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getRemarks2()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getWeight()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        return sb.toString();
    }
}
