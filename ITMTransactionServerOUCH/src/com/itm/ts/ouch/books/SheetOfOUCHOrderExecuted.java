/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgOrderExecuted;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHOrderExecuted extends SheetOfOUCHBase {
    
    private final OUCHMsgOrderExecuted mMessage;
    
    public SheetOfOUCHOrderExecuted(OUCHMsgOrderExecuted mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(0, mMessage.getTimestamp()));
    }
    
    public OUCHMsgOrderExecuted getMessage() {
        return this.mMessage;
    }

}