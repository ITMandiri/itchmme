/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgCancelOrder;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHCancelOrder extends SheetOfOUCHBase {
    
    private final OUCHMsgCancelOrder mMessage;
    
    public SheetOfOUCHCancelOrder(OUCHMsgCancelOrder mMessage){
        this.mMessage = mMessage;
    }
    
    public OUCHMsgCancelOrder getMessage() {
        return this.mMessage;
    }

}
