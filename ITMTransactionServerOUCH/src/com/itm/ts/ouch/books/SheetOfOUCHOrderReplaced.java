/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgOrderReplaced;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHOrderReplaced extends SheetOfOUCHBase {
    
    private final OUCHMsgOrderReplaced mMessage;
    
    public SheetOfOUCHOrderReplaced(OUCHMsgOrderReplaced mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(0, mMessage.getTimestamp()));
    }
    
    public OUCHMsgOrderReplaced getMessage() {
        return this.mMessage;
    }

}