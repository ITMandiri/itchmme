/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.message.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5IDXMessage {
    
    private Map<String, ArrayList<String>> mapMsgFields                    = new HashMap<>();
    
    public FIX5IDXMessage(Map<String, ArrayList<String>> inputMsgFields) {
        this.mapMsgFields = inputMsgFields;
    }

    public FIX5IDXMessage() {
    }
    
    public Map<String, ArrayList<String>> getMapMsgFields() {
        return mapMsgFields;
    }

    public void setMapMsgFields(Map<String, ArrayList<String>> mapMsgFields) {
        this.mapMsgFields = mapMsgFields;
    }
    
    public void assignValue(){
        
    }
    
}
