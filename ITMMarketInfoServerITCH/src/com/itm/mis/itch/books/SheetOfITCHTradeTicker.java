/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgTradeTicker;

/**
 *
 * @author fredy
 */
public class SheetOfITCHTradeTicker extends SheetOfITCHBase {
    
    private final ITCHMsgTradeTicker mMessage;
    
    public SheetOfITCHTradeTicker(ITCHMsgTradeTicker mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgTradeTicker getMessage() {
        return this.mMessage;
    }

}
