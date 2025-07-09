/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.example.listener;

/**
 *
 * @author aripam
 */
public class TBindingListener implements IBindingListener{

    public TBindingListener() {
    }

    @Override
    public void onConnected(Object channel) {
        System.out.println("T:" + this.hashCode() + "onConnected=" + channel);
    }

    @Override
    public void onDisconnected(Object channel) {
        System.out.println("T:" + this.hashCode() + "onDisconnected=" + channel);
    }

    @Override
    public void onMessage(Object channel, String messageLine) {
        System.out.println("T:" + this.hashCode() + "onMessage=" + channel + ",Message=" + messageLine);
    }

    @Override
    public void onError(Object channel, Object exception) {
        System.out.println("T:" + this.hashCode() + "onError=" + channel + ",Exception=" + exception);
    }
    
}
