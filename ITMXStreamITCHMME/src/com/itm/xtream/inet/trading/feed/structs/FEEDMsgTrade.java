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
public class FEEDMsgTrade extends FEEDMsgBase{
    private String tradTime;
    private String tradeCommand;
    private String securityCode;
    private String boardCode;
    private String tradeNo;
    private String price;
    private String vol;
    private String buyerCode;
    private String buyerType;
    private String sellerCode;
    private String sellerType;
    private String bestBidPrice;
    private String bestBidVol;
    private String bestOfferPrice;
    private String bestOfferVol;
    private String buyerOrderNo = "";
    private String sellerOrderNo = "";
    
    public FEEDMsgTrade(){
        setRecType("2");
    }

    public String getTradTime() {
        return tradTime;
    }

    public void setTradTime(String tradTime) {
        this.tradTime = tradTime;
    }

    public String getTradeCommand() {
        return tradeCommand;
    }

    public void setTradeCommand(String tradeCommand) {
        this.tradeCommand = tradeCommand;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public String getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(String buyerType) {
        this.buyerType = buyerType;
    }

    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    public String getSellerType() {
        return sellerType;
    }

    public void setSellerType(String sellerType) {
        this.sellerType = sellerType;
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

    public String getBuyerOrderNo() {
        return buyerOrderNo;
    }

    public void setBuyerOrderNo(String buyerOrderNo) {
        this.buyerOrderNo = buyerOrderNo;
    }

    public String getSellerOrderNo() {
        return sellerOrderNo;
    }

    public void setSellerOrderNo(String sellerOrderNo) {
        this.sellerOrderNo = sellerOrderNo;
    }
    
    
     public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getTradTime()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getTradeCommand()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSecurityCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBoardCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getTradeNo()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getVol()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBuyerCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBuyerType()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSellerCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSellerType()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestBidPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestBidVol()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestOfferPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestOfferVol()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBuyerOrderNo()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSellerOrderNo()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        return sb.toString();
    }
}
