/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.example.listener;

/**
 *
 * @author aripam
 */
public abstract class CBindingListener implements IBindingListener {

    @Override
    public void onConnected(Object channel) {
        System.out.println("C:onConnected=" + channel);
    }

    @Override
    public void onDisconnected(Object channel) {
        System.out.println("C:onDisconnected=" + channel);
    }

    @Override
    public void onMessage(Object channel, String messageLine) {
        System.out.println("C:onMessage=" + channel + ",Message=" + messageLine);
    }

    @Override
    public void onError(Object channel, Object exception) {
        System.out.println("C:onError=" + channel + ",Exception=" + exception);
    }
    
}
