/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgOrderExecutedWithPrice;

/**
 *
 * @author fredy
 */
public class SheetOfITCHOrderExecutedWithPrice extends SheetOfITCHBase {
    
    private final ITCHMsgOrderExecutedWithPrice mMessage;
    
    public SheetOfITCHOrderExecutedWithPrice(ITCHMsgOrderExecutedWithPrice mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgOrderExecutedWithPrice getMessage() {
        return this.mMessage;
    }

}
