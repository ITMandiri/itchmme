/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgEquilibriumPrice;

/**
 *
 * @author fredy
 */
public class SheetOfITCHEquilibriumPrice extends SheetOfITCHBase {
    
    private final ITCHMsgEquilibriumPrice mMessage;
    
    public SheetOfITCHEquilibriumPrice(ITCHMsgEquilibriumPrice mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgEquilibriumPrice getMessage() {
        return this.mMessage;
    }

}
