/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.books;

import com.itm.ts.ouch.structs.OUCHMsgCancelByOrderID;

/**
 *
 * @author fredy
 */
public class SheetOfOUCHCancelByOrderID extends SheetOfOUCHBase {
    
    private final OUCHMsgCancelByOrderID mMessage;
    
    public SheetOfOUCHCancelByOrderID(OUCHMsgCancelByOrderID mMessage){
        this.mMessage = mMessage;
    }
    
    public OUCHMsgCancelByOrderID getMessage() {
        return this.mMessage;
    }

}