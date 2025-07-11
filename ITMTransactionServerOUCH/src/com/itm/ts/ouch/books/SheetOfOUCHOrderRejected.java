/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgOrderRejected;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHOrderRejected extends SheetOfOUCHBase {
    
    private final OUCHMsgOrderRejected mMessage;
    
    public SheetOfOUCHOrderRejected(OUCHMsgOrderRejected mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(0, mMessage.getTimestamp()));
    }
    
    public OUCHMsgOrderRejected getMessage() {
        return this.mMessage;
    }

}