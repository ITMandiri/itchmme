/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.books;

/**
 *
 * @author fredy
 */
public class SheetOfFIX5JonecRequest {
    
    private final long seq;
    
    private String fixMsg;
    
    public SheetOfFIX5JonecRequest(long seq){
        this.seq = seq;
    }

    public long getSeq() {
        return seq;
    }

    public String getFixMsg() {
        return fixMsg;
    }

    public void setFixMsg(String fixMsg) {
        this.fixMsg = fixMsg;
    }
    
}
