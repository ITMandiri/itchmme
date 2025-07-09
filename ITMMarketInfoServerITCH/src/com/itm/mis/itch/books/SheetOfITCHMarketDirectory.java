/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgMarketDirectory;

/**
 *
 * @author fredy
 */
public class SheetOfITCHMarketDirectory extends SheetOfITCHBase {
    
    private final ITCHMsgMarketDirectory mMessage;
    
    public SheetOfITCHMarketDirectory(ITCHMsgMarketDirectory mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgMarketDirectory getMessage() {
        return this.mMessage;
    }

}
