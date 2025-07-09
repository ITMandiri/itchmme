/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgAddOrder;

/**
 *
 * @author fredy
 */
public class SheetOfITCHAddOrder extends SheetOfITCHBase {
    
    private final ITCHMsgAddOrder mMessage;
    
    public SheetOfITCHAddOrder(ITCHMsgAddOrder mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgAddOrder getMessage() {
        return this.mMessage;
    }

}
