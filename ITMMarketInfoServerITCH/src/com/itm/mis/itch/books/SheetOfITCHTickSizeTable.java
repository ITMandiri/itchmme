/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgTickSizeTable;

/**
 *
 * @author fredy
 */
public class SheetOfITCHTickSizeTable extends SheetOfITCHBase {
    
    private final ITCHMsgTickSizeTable mMessage;
    
    public SheetOfITCHTickSizeTable(ITCHMsgTickSizeTable mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgTickSizeTable getMessage() {
        return this.mMessage;
    }

}
