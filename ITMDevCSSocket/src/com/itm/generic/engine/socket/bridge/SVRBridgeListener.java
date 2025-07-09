/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.bridge;

import com.itm.generic.engine.socket.setup.ITMSocketChannel;

/**
 *
 * @author aripam
 */
public interface SVRBridgeListener {
    
    public abstract void onConnected (ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName); //.saat terhubung.
    public abstract void onDisconnected (ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName); //.saat putus.
    public abstract void onReceive (ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName, String messageLine); //.saat terima message line.
    public abstract void onSent (ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName, String messageLine); //.saat kirim message line.
    public abstract void onError (ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName, Exception exception); //.saat ada error.
    
}
