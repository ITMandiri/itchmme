/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.bridge;

import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.setup.ITMSocketListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author aripam
 */
public class USRBridgeSocketHandler implements ITMSocketListener {
    
    private USRBridgeController headCtrl;
    
    //.informasi listener:
    private List<USRBridgeListener> fBridgeListeners = new ArrayList<>();
    
    public USRBridgeSocketHandler(USRBridgeController headController) {
        this.headCtrl = headController;
        //.EXXX.
    }
    
    public void addBridgeListener(USRBridgeListener newListener){
        try{
            if (newListener == null){
                return;
            }
            if (!this.fBridgeListeners.contains(newListener)){
                this.fBridgeListeners.add(newListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    public void removeBridgeListener(USRBridgeListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            if (this.fBridgeListeners.contains(oldListener)){
                this.fBridgeListeners.remove(oldListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnConnected(ITMSocketChannel channel){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<USRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    USRBridgeListener lstr = (USRBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onConnected(channel, headCtrl, headCtrl.getConnectionName());
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnDisconnected(ITMSocketChannel channel){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<USRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    USRBridgeListener lstr = (USRBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onDisconnected(channel, headCtrl, headCtrl.getConnectionName());
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnReceive(ITMSocketChannel channel, String messageLine){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<USRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    USRBridgeListener lstr = (USRBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onReceive(channel, headCtrl, headCtrl.getConnectionName(), messageLine);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnSent(ITMSocketChannel channel, String messageLine){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<USRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    USRBridgeListener lstr = (USRBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onSent(channel, headCtrl, headCtrl.getConnectionName(), messageLine);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnError(ITMSocketChannel channel, Exception exception){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<USRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    USRBridgeListener lstr = (USRBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onError(channel, headCtrl, headCtrl.getConnectionName(), exception);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    
    
    
    @Override
    public void onConnected(ITMSocketChannel channel) {
        //.broadcast:
        raiseOnConnected(channel);
        
    }
    
    @Override
    public void onDisconnected(ITMSocketChannel channel) {
        //.broadcast:
        raiseOnDisconnected(channel);
        
    }
    
    @Override
    public void onMessage(ITMSocketChannel channel, String messageLine) {
        //.broadcast:
        raiseOnReceive(channel, messageLine);
        
    }
    
    @Override
    public void onSent(ITMSocketChannel channel, String messageLine) {
        //.broadcast:
        raiseOnSent(channel, messageLine);
        
    }
    
    @Override
    public void onError(ITMSocketChannel channel, Exception exception) {
        //.broadcast:
        raiseOnError(channel, exception);
        
    }
    
}
