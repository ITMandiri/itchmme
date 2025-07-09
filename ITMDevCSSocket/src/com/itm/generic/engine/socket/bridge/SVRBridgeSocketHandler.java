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
public class SVRBridgeSocketHandler implements ITMSocketListener {
    
    private SVRBridgeController headCtrl;
    
    //.informasi listener:
    private List<SVRBridgeListener> fBridgeListeners = new ArrayList<>();

    public SVRBridgeSocketHandler(SVRBridgeController headCtrl) {
        this.headCtrl = headCtrl;
        //.EXXX.
    }
    
    public void addBridgeListener(SVRBridgeListener newListener){
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
    
    public void removeBridgeListener(SVRBridgeListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            this.fBridgeListeners.remove(oldListener);
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnConnected(ITMSocketChannel channel){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<SVRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    SVRBridgeListener lstr = (SVRBridgeListener)iterBridgeListener.next();
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
                for (Iterator<SVRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    SVRBridgeListener lstr = (SVRBridgeListener)iterBridgeListener.next();
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
                for (Iterator<SVRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    SVRBridgeListener lstr = (SVRBridgeListener)iterBridgeListener.next();
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
                for (Iterator<SVRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    SVRBridgeListener lstr = (SVRBridgeListener)iterBridgeListener.next();
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
                for (Iterator<SVRBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    SVRBridgeListener lstr = (SVRBridgeListener)iterBridgeListener.next();
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
