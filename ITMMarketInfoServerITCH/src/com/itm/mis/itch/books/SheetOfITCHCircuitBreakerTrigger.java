/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgCircuitBreakerTrigger;

/**
 *
 * @author fredy
 */

public class SheetOfITCHCircuitBreakerTrigger extends SheetOfITCHBase {
    
    private final ITCHMsgCircuitBreakerTrigger mMessage;
    
    public SheetOfITCHCircuitBreakerTrigger(ITCHMsgCircuitBreakerTrigger mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgCircuitBreakerTrigger getMessage() {
        return this.mMessage;
    }

}
