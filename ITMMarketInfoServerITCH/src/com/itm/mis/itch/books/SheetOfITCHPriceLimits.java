/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgPriceLimits;

/**
 *
 * @author fredy
 */
public class SheetOfITCHPriceLimits extends SheetOfITCHBase {
    
    private final ITCHMsgPriceLimits mMessage;
    
    public SheetOfITCHPriceLimits(ITCHMsgPriceLimits mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgPriceLimits getMessage() {
        return this.mMessage;
    }

}
