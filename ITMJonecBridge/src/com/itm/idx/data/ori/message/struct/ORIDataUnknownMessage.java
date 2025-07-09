/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataUnknownMessage extends ORIDataHeader {
    
    private String fInputMessage                                = "";
    
    public ORIDataUnknownMessage(Map<String, String> inputMsgFields, String fInputMessage) {
        super(inputMsgFields);
        this.fInputMessage = fInputMessage;
    }
    
    public ORIDataUnknownMessage(String fInputMessage) {
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
