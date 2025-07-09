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
public class FEEDMsgOrder extends FEEDMsgBase{
    private String orderTime;
    private String orderCommand;
    private String securityCode;
    private String boardCode;
    private String brokerCode;
    private String price;
    private String volume;
    private String balance;
    private String invType;
    private String orderNo;
    private String bestBidPrice;
    private String bestBidVol;
    private String bestOfferPrice;
    private String bestOfferVol;
    private String orderRef;
    
    
    public FEEDMsgOrder(){
        setRecType("1");
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderCommand() {
        return orderCommand;
    }

    public void setOrderCommand(String orderCommand) {
        this.orderCommand = orderCommand;
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

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getOrderTime()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getOrderCommand()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSecurityCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBoardCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBrokerCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getVolume()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBalance()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getInvType()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getOrderNo()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestBidPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestBidVol()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestOfferPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestOfferVol()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getOrderRef()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        return sb.toString();
    }
    
}