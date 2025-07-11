/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgEnterOrder;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHEnterOrder extends SheetOfOUCHBase {
    
    private final OUCHMsgEnterOrder mMessage;
    
    public SheetOfOUCHEnterOrder(OUCHMsgEnterOrder mMessage){
        this.mMessage = mMessage;
    }
    
    public OUCHMsgEnterOrder getMessage() {
        return this.mMessage;
    }

}