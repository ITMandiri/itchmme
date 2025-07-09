/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgGlimpseSnapshot;

/**
 *
 * @author fredy
 */
public class SheetOfITCHGlimpseSnapshot extends SheetOfITCHBase {
    
    private final ITCHMsgGlimpseSnapshot mMessage;
    
    public SheetOfITCHGlimpseSnapshot(ITCHMsgGlimpseSnapshot mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), 0));
    }
    
    public ITCHMsgGlimpseSnapshot getMessage() {
        return this.mMessage;
    }

}
