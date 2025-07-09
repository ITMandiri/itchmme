/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.message.bridge;

import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.idx.message.IDXMessage;

/**
 *
 * @author aripam
 */
public interface IDXBridgeListener {
    
    public abstract void onConnected (ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName); //.saat terhubung.
    public abstract void onDisconnected (ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName); //.saat putus.
    public abstract void onReceive (ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName, String messageLine, IDXMessage messageObject); //.saat terima orimessage object.
    public abstract void onSent (ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName, String messageLine); //.saat kirim orimessage line.
    public abstract void onError (ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName, Exception exception); //.saat ada error.
    
}
