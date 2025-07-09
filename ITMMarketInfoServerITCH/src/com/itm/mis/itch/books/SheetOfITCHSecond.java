/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgSecond;


/**
 *
 * @author Ari Pambudi
 */
public class SheetOfITCHSecond extends SheetOfITCHBase {
    
    private final ITCHMsgSecond mMessage;
    
    public SheetOfITCHSecond(ITCHMsgSecond mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(mMessage.getSeconds(), 0));
    }
    
    public ITCHMsgSecond getMessage() {
        return this.mMessage;
    }

}
