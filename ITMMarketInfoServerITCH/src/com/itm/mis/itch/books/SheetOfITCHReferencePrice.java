/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgReferencePrice;

/**
 *
 * @author fredy
 */
public class SheetOfITCHReferencePrice extends SheetOfITCHBase {
    
    private final ITCHMsgReferencePrice mMessage;
    
    public SheetOfITCHReferencePrice(ITCHMsgReferencePrice mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgReferencePrice getMessage() {
        return this.mMessage;
    }

}
