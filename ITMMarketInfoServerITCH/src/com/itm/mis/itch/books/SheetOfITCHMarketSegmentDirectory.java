/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgMarketSegmentDirectory;

/**
 *
 * @author fredy
 */
public class SheetOfITCHMarketSegmentDirectory extends SheetOfITCHBase {
    
    private final ITCHMsgMarketSegmentDirectory mMessage;
    
    public SheetOfITCHMarketSegmentDirectory(ITCHMsgMarketSegmentDirectory mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgMarketSegmentDirectory getMessage() {
        return this.mMessage;
    }

}
