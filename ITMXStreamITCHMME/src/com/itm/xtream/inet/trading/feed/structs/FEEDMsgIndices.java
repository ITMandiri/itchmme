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
public class FEEDMsgIndices extends FEEDMsgBase{
    private String indexCode;
    private String exchgBaseValue;
    private String exchgMarketValue;
    private String index;
    private String open;
    private String high;
    private String low;
    private String prevIndex;
    
    public FEEDMsgIndices(){
        setRecType("6");
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getExchgBaseValue() {
        return exchgBaseValue;
    }

    public void setExchgBaseValue(String exchgBaseValue) {
        this.exchgBaseValue = exchgBaseValue;
    }

    public String getExchgMarketValue() {
        return exchgMarketValue;
    }

    public void setExchgMarketValue(String exchgMarketValue) {
        this.exchgMarketValue = exchgMarketValue;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getPrevIndex() {
        return prevIndex;
    }

    public void setPrevIndex(String prevIndex) {
        this.prevIndex = prevIndex;
    }
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getIndexCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getExchgBaseValue()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getExchgMarketValue()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getIndex()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getOpen()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getHigh()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getLow()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getPrevIndex()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        return sb.toString();
    }
}
