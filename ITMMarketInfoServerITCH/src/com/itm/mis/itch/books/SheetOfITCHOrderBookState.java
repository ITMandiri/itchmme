/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgOrderBookState;

/**
 *
 * @author fredy
 */
public class SheetOfITCHOrderBookState extends SheetOfITCHBase {
    
    private final ITCHMsgOrderBookState mMessage;
    
    public SheetOfITCHOrderBookState(ITCHMsgOrderBookState mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgOrderBookState getMessage() {
        return this.mMessage;
    }

}
