/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgReplaceOrder;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHReplaceOrder extends SheetOfOUCHBase {
    
    private final OUCHMsgReplaceOrder mMessage;
    
    public SheetOfOUCHReplaceOrder(OUCHMsgReplaceOrder mMessage){
        this.mMessage = mMessage;
    }
    
    public OUCHMsgReplaceOrder getMessage() {
        return this.mMessage;
    }

}