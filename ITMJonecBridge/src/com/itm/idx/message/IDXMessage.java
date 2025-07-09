/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.message;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class IDXMessage {
    
    private Map<String, String> mapMsgFields                    = new HashMap<>();
    
    public IDXMessage(Map<String, String> inputMsgFields) {
        this.mapMsgFields = inputMsgFields;
    }

    public IDXMessage() {
    }
    
    public Map<String, String> getMapMsgFields() {
        return mapMsgFields;
    }

    public void setMapMsgFields(Map<String, String> mapMsgFields) {
        this.mapMsgFields = mapMsgFields;
    }
    
    public void assignValue(){
        
    }
    
}
