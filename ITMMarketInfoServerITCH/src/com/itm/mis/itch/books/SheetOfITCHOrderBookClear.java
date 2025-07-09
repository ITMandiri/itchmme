/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgOrderBookClear;

/**
 *
 * @author fredy
 */
public class SheetOfITCHOrderBookClear extends SheetOfITCHBase {
    
    private final ITCHMsgOrderBookClear mMessage;
    
    public SheetOfITCHOrderBookClear(ITCHMsgOrderBookClear mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgOrderBookClear getMessage() {
        return this.mMessage;
    }

}
