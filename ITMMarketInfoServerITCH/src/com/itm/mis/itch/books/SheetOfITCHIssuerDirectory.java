/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgIssuerDirectory;

/**
 *
 * @author fredy
 */
public class SheetOfITCHIssuerDirectory extends SheetOfITCHBase {
    
    private final ITCHMsgIssuerDirectory mMessage;
    
    public SheetOfITCHIssuerDirectory(ITCHMsgIssuerDirectory mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgIssuerDirectory getMessage() {
        return this.mMessage;
    }

}
