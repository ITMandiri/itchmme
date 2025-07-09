/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgExchangeDirectory;

/**
 *
 * @author fredy
 */
public class SheetOfITCHExchangeDirectory extends SheetOfITCHBase {
    
    private final ITCHMsgExchangeDirectory mMessage;
    
    public SheetOfITCHExchangeDirectory(ITCHMsgExchangeDirectory mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgExchangeDirectory getMessage() {
        return this.mMessage;
    }

}
