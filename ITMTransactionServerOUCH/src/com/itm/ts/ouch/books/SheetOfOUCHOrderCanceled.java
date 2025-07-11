/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgOrderCanceled;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHOrderCanceled extends SheetOfOUCHBase {
    
    private final OUCHMsgOrderCanceled mMessage;
    
    public SheetOfOUCHOrderCanceled(OUCHMsgOrderCanceled mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(0, mMessage.getTimestamp()));
    }
    
    public OUCHMsgOrderCanceled getMessage() {
        return this.mMessage;
    }

}
