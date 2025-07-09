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
public class FEEDMsgBrokerData extends FEEDMsgBase {
    private String brokerCode;
    private String brokerName;
    private String brokerStatus;
    
    public FEEDMsgBrokerData(){
        setRecType("4");
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerStatus() {
        return brokerStatus;
    }

    public void setBrokerStatus(String brokerStatus) {
        this.brokerStatus = brokerStatus;
    }
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getBrokerCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBrokerName()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBrokerStatus()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        return sb.toString();
    }
}
