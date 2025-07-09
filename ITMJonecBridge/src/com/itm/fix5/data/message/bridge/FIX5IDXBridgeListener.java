/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.message.bridge;

import com.itm.fix5.data.jonec.message.struct.FIX5IDXMessage;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;

/**
 *
 * @author Ari Pambudi
 */
public interface FIX5IDXBridgeListener {
    
    public abstract void onConnected (ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName); //.saat terhubung.
    public abstract void onDisconnected (ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName); //.saat putus.
    public abstract void onReceive (ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName, String messageLine, FIX5IDXMessage messageObject); //.saat terima orimessage object.
    public abstract void onSent (ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName, String messageLine); //.saat kirim orimessage line.
    public abstract void onError (ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName, Exception exception); //.saat ada error.
    
}
