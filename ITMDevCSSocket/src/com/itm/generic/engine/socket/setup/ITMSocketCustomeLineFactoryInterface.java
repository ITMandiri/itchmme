/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.setup;

/**
 *
 * @author Ari Pambudi
 */
public interface ITMSocketCustomeLineFactoryInterface {
    
    public abstract String[] onCustomDataReceived(String zLineData);
    public abstract void onCustomDataReset();
    
}
