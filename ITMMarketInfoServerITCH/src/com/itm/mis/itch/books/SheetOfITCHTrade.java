/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgTrade;

/**
 *
 * @author fredy
 */
public class SheetOfITCHTrade extends SheetOfITCHBase {
    
    private final ITCHMsgTrade mMessage;
    
    public SheetOfITCHTrade(ITCHMsgTrade mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgTrade getMessage() {
        return this.mMessage;
    }

}
