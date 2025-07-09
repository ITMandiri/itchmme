/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgOrderExecuted;

/**
 *
 * @author fredy
 */
public class SheetOfITCHOrderExecuted extends SheetOfITCHBase {
    
    private final ITCHMsgOrderExecuted mMessage;
    
    public SheetOfITCHOrderExecuted(ITCHMsgOrderExecuted mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgOrderExecuted getMessage() {
        return this.mMessage;
    }

}
