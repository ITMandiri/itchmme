/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgMarketByPrice;

/**
 *
 * @author fredy
 */
public class SheetOfITCHMarketByPrice extends SheetOfITCHBase {
    
    private final ITCHMsgMarketByPrice mMessage;
    
    public SheetOfITCHMarketByPrice(ITCHMsgMarketByPrice mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgMarketByPrice getMessage() {
        return this.mMessage;
    }

}
