/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.mis.itch.structs.ITCHMsgParticipantDirectory;

/**
 *
 * @author fredy
 */
public class SheetOfITCHParticipantDirectory extends SheetOfITCHBase {
    
    private final ITCHMsgParticipantDirectory mMessage;
    
    public SheetOfITCHParticipantDirectory(ITCHMsgParticipantDirectory mMessage){
        this.mMessage = mMessage;
        super.setMessageDate(retrieveMessageDate(BookOfITCHSecond.getInstance.retrieveNearestTimeStampSeconds(), mMessage.getNanos()));
    }
    
    public ITCHMsgParticipantDirectory getMessage() {
        return this.mMessage;
    }

}
