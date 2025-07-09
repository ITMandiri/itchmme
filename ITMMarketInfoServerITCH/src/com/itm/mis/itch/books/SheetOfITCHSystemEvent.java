/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgSystemEvent;

/**
 *
 * @author fredy
 */
public class SheetOfITCHSystemEvent extends SheetOfITCHBase {
    
    private final ITCHMsgSystemEvent mMessage;
    
    public SheetOfITCHSystemEvent(ITCHMsgSystemEvent mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgSystemEvent getMessage() {
        return this.mMessage;
    }

}
