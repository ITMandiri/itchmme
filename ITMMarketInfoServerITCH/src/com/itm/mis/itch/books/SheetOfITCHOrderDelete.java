/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgOrderDelete;

/**
 *
 * @author fredy
 */
public class SheetOfITCHOrderDelete extends SheetOfITCHBase {
    
    private final ITCHMsgOrderDelete mMessage;
    
    public SheetOfITCHOrderDelete(ITCHMsgOrderDelete mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgOrderDelete getMessage() {
        return this.mMessage;
    }

}
