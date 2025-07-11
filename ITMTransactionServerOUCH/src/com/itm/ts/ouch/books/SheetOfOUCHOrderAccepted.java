/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgOrderAccepted;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHOrderAccepted extends SheetOfOUCHBase {
    
    private final OUCHMsgOrderAccepted mMessage;
    
    public SheetOfOUCHOrderAccepted(OUCHMsgOrderAccepted mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(0, mMessage.getTimestamp()));
    }
    
    public OUCHMsgOrderAccepted getMessage() {
        return this.mMessage;
    }

}
