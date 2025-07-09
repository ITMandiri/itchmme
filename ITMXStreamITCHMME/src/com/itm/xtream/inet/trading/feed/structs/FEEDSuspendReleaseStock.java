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
public class FEEDSuspendReleaseStock extends FEEDMsgBase{
    private String securityCode;
    private String flag;
    private String boardCode;
    
    public FEEDSuspendReleaseStock(){
        setRecType("8");
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }
    
    public String toDataFeedMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append(getHeaderMessage()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        
        sb.append(getSecurityCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getFlag()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        sb.append(getBoardCode()).append(FEEDMsgConst.FEED_MESSAGE_SEPARATOR);
        return sb.toString();
    }
    
}
