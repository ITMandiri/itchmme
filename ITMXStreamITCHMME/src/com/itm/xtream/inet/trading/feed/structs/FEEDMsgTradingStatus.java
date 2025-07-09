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
public class FEEDMsgTradingStatus extends FEEDMsgBase{
    private String status;
    private String message;
    private String type;
    private String type2;
    private int session;

    public FEEDMsgTradingStatus(){
        setRecType("0");
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }
    
    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getStatus()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getType()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getType2()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSession()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        return sb.toString();
    }
}