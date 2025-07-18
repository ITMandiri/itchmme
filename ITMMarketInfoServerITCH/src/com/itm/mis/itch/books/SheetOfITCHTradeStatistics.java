/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgTradeStatistics;

/**
 *
 * @author fredy
 */
public class SheetOfITCHTradeStatistics extends SheetOfITCHBase {
    
    private final ITCHMsgTradeStatistics mMessage;
    
    public SheetOfITCHTradeStatistics(ITCHMsgTradeStatistics mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgTradeStatistics getMessage() {
        return this.mMessage;
    }

}
