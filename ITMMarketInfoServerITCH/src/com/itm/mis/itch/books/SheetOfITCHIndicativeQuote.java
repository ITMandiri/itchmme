/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgIndicativeQuote;

/**
 *
 * @author fredy
 */
public class SheetOfITCHIndicativeQuote extends SheetOfITCHBase {
    
    private final ITCHMsgIndicativeQuote mMessage;
    
    public SheetOfITCHIndicativeQuote(ITCHMsgIndicativeQuote mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgIndicativeQuote getMessage() {
        return this.mMessage;
    }

}
