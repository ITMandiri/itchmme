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
public class FEEDMsgTheoreticalPV extends FEEDMsgBase{
    private String securityCode;
    private String board;
    private String price;
    private String volume;
    private String bestBid  = "";
    private String bestOffer = "";
    private String bestBidSize = "";
    private String bestOfferSize = "";

    public FEEDMsgTheoreticalPV(){
        setRecType("C");
    }
    
    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
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

    public String getBestBid() {
        return bestBid;
    }

    public void setBestBid(String bestBid) {
        this.bestBid = bestBid;
    }

    public String getBestOffer() {
        return bestOffer;
    }

    public void setBestOffer(String bestOffer) {
        this.bestOffer = bestOffer;
    }

    public String getBestBidSize() {
        return bestBidSize;
    }

    public void setBestBidSize(String bestBidSize) {
        this.bestBidSize = bestBidSize;
    }

    public String getBestOfferSize() {
        return bestOfferSize;
    }

    public void setBestOfferSize(String bestOfferSize) {
        this.bestOfferSize = bestOfferSize;
    }
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getSecurityCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBoard()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getVolume()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestBid()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestBidSize()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestOffer()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBestOfferSize()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        return sb.toString();
    }
}
