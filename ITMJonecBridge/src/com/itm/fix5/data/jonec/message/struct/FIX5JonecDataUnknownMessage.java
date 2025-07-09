/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.message.struct;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5JonecDataUnknownMessage extends FIX5JonecDataHeader {
    
    private String fInputMessage                                = "";
    
    public FIX5JonecDataUnknownMessage(Map<String, ArrayList<String>> inputMsgFields, String fInputMessage) {
        super(inputMsgFields);
        this.fInputMessage = fInputMessage;
    }
    
    public FIX5JonecDataUnknownMessage(String fInputMessage) {
        super(null);
        this.fInputMessage = fInputMessage;
    }

    public String getfInputMessage() {
        return fInputMessage;
    }

    public void setfInputMessage(String fInputMessage) {
        this.fInputMessage = fInputMessage;
    }
    
}
