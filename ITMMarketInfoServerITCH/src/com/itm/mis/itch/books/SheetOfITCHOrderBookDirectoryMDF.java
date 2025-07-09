/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgOrderBookDirectoryMDF;

/**
 *
 * @author fredy
 */
public class SheetOfITCHOrderBookDirectoryMDF extends SheetOfITCHBase {
    
    private final ITCHMsgOrderBookDirectoryMDF mMessage;
    
    public SheetOfITCHOrderBookDirectoryMDF(ITCHMsgOrderBookDirectoryMDF mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgOrderBookDirectoryMDF getMessage() {
        return this.mMessage;
    }

}
