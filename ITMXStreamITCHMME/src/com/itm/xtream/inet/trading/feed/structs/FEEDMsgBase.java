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
public class FEEDMsgBase {
    
    public static final int SHARE_PER_LOT = 100;
    
    private String header;
    private String date;
    private String time;
    private String seq;
    private String recType;
    
    public FEEDMsgBase(){
        setHeader("IDX");
    }
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getRecType() {
        return recType;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }
    
    protected String getHeaderMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeader()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getDate()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getTime()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getSeq()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getRecType());
        
        return sb.toString();
    }
}

