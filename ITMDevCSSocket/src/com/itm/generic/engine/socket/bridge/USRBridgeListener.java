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

public interface USRBridgeListener {
    
    public abstract void onConnected (ITMSocketChannel channel, USRBridgeController controller, String ConnectionName); //.saat terhubung.
    public abstract void onDisconnected (ITMSocketChannel channel, USRBridgeController controller, String ConnectionName); //.saat putus.
    public abstract void onReceive (ITMSocketChannel channel, USRBridgeController controller, String ConnectionName, String messageLine); //.saat terima message line.
    public abstract void onSent (ITMSocketChannel channel, USRBridgeController controller, String ConnectionName, String messageLine); //.saat kirim message line.
    public abstract void onError (ITMSocketChannel channel, USRBridgeController controller, String ConnectionName, Exception exception); //.saat ada error.
    
}
