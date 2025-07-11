/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.books;

import com.itm.idx.message.IDXMessage;

/**
 *
 * @author fredy
 */
public class SheetOfMARTINSimEveryRequest {
    
    private long orderToken;

    private final IDXMessage idxMessage;

    public SheetOfMARTINSimEveryRequest(long orderToken, IDXMessage idxMessage){
        this.orderToken = orderToken;
        this.idxMessage = idxMessage;
    }
    
    public long getOrderToken() {
        return orderToken;
    }

    public IDXMessage getIdxMessage() {
        return idxMessage;
    }
    
}
