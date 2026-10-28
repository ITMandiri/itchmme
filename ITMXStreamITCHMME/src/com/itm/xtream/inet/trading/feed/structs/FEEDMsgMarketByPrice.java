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
public class FEEDMsgMarketByPrice extends FEEDMsgBase{
    private String mbpTime;
    private String securityCode;
    private String boardCode;
    private String side;
    private String action;
    private String price;
    private String volume;
    private String freq;
    private String level;
    
    public FEEDMsgMarketByPrice(){
        setRecType("Z");
    }

    public String getMbpTime() {
        return mbpTime;
    }

    public void setMbpTime(String mbpTime) {
        this.mbpTime = mbpTime;
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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getMbpTime()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSecurityCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBoardCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSide()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getAction()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getPrice()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getVolume()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getFreq()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getLevel()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        return sb.toString();
    }
    
}
