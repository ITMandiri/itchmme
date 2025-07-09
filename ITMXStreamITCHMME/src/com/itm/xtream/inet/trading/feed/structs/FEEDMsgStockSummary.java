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
public class FEEDMsgStockSummary extends FEEDMsgBase{
    private String securityCode;
    private String boardCode;
    private String prevPrice;
    private String highPrice;
    private String lowPrice;
    private String closePrice;
    private String change;
    private String tradedVol;
    private String tradedVal;
    private String tradedFreq;
    private String individualIndex;
    private String availForeigner;
    private String openingPrice;
    private String bestBidPrice;
    private String bestBidVol;
    private String bestOfferPrice;
    private String bestOfferVol;
    private String avgPrice;
    private String secBoardState;
    
    
    public FEEDMsgStockSummary(){
        setRecType("5");
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public String getPrevPrice() {
        return prevPrice;
    }

    public void setPrevPrice(String prevPrice) {
        this.prevPrice = prevPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getTradedVol() {
        return tradedVol;
    }

    public void setTradedVol(String tradedVol) {
        this.tradedVol = tradedVol;
    }

    public String getTradedVal() {
        return tradedVal;
    }

    public void setTradedVal(String tradedVal) {
        this.tradedVal = tradedVal;
    }

    public String getTradedFreq() {
        return tradedFreq;
    }

    public void setTradedFreq(String tradedFreq) {
        this.tradedFreq = tradedFreq;
    }

    public String getIndividualIndex() {
        return individualIndex;
    }

    public void setIndividualIndex(String individualIndex) {
        this.individualIndex = individualIndex;
    }

    public String getAvailForeigner() {
        return availForeigner;
    }

    public void setAvailForeigner(String availForeigner) {
        this.availForeigner = availForeigner;
    }

    public String getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(String openingPrice) {
        this.openingPrice = openingPrice;
    }

    public String getBestBidPrice() {
        return bestBidPrice;
    }

    public void setBestBidPrice(String bestBidPrice) {
        this.bestBidPrice = bestBidPrice;
    }

    public String getBestBidVol() {
        return bestBidVol;
    }

    public void setBestBidVol(String bestBidVol) {
        this.bestBidVol = bestBidVol;
    }

    public String getBestOfferPrice() {
        return bestOfferPrice;
    }

    public void setBestOfferPrice(String bestOfferPrice) {
        this.bestOfferPrice = bestOfferPrice;
    }

    public String getBestOfferVol() {
        return bestOfferVol;
    }

    public void setBestOfferVol(String bestOfferVol) {
        this.bestOfferVol = bestOfferVol;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getSecBoardState() {
        return secBoardState;
    }

    public void setSecBoardState(String secBoardState) {
        this.secBoardState = secBoardState;
    }
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getSecurityCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBoardCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getPrevPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getHighPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getLowPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getClosePrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getChange()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getTradedVol()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getTradedVal()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getTradedFreq()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getIndividualIndex()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getAvailForeigner()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getOpeningPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestBidPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestBidVol()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestOfferPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestOfferVol()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getAvgPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSecBoardState()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        return sb.toString();
    }
}