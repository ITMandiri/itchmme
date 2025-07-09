/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.example.listener;

/**
 *
 * @author aripam
 */

public abstract interface IBindingListener {
    public abstract void onConnected (Object channel);
    public abstract void onDisconnected (Object channel);
    public abstract void onMessage (Object channel, String messageLine);
    public abstract void onError (Object channel, Object exception);
}
