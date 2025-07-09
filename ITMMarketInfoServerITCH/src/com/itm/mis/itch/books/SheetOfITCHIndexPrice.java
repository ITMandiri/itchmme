/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgIndexPrice;

/**
 *
 * @author fredy
 */
public class SheetOfITCHIndexPrice extends SheetOfITCHBase {
    
    private final ITCHMsgIndexPrice mMessage;
    
    public SheetOfITCHIndexPrice(ITCHMsgIndexPrice mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgIndexPrice getMessage() {
        return this.mMessage;
    }

}
