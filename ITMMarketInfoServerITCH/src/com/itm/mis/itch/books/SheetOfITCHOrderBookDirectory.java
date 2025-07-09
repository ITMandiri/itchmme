/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgOrderBookDirectory;

/**
 *
 * @author fredy
 */
public class SheetOfITCHOrderBookDirectory extends SheetOfITCHBase {
    
    private final ITCHMsgOrderBookDirectory mMessage;
    
    public SheetOfITCHOrderBookDirectory(ITCHMsgOrderBookDirectory mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgOrderBookDirectory getMessage() {
        return this.mMessage;
    }

}
