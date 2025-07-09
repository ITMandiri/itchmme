/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.setup;

/**
 *
 * @author aripam
 */
public interface ITMSocketListener {
    
    public abstract void onConnected (ITMSocketChannel channel); //.saat terhubung.
    public abstract void onDisconnected (ITMSocketChannel channel); //.saat putus.
    public abstract void onMessage (ITMSocketChannel channel, String messageLine); //.saat terima baris teks.
    public abstract void onSent (ITMSocketChannel channel, String messageLine); //.saat kirim baris teks.
    public abstract void onError (ITMSocketChannel channel, Exception exception); //.saat ada error.
    
}
