/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.example.listener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aripam
 */
public class BBindingListener {
    //.single instance ya:
    public final static BBindingListener getInstance = new BBindingListener();
    
    private List<IBindingListener> bindingListeners = new ArrayList<>();
    
    public BBindingListener() {
    }
    
    public final void addBindingListener(IBindingListener newListener){
        try{
            if (newListener == null){
                return;
            }
            this.bindingListeners.add(newListener);
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
    public final void removeBindingListener(IBindingListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            this.bindingListeners.remove(oldListener);
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
    public final void raiseOnConnected(Object channel){
        try{
            if (this.bindingListeners.size() > 0){
                for (IBindingListener listener : this.bindingListeners){
                    listener.onConnected(channel);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
    public final void raiseOnDisconnected(Object channel){
        try{
            if (this.bindingListeners.size() > 0){
                for (IBindingListener listener : this.bindingListeners){
                    listener.onDisconnected(channel);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
    public final void raiseOnMessage(Object channel, String messageLine){
        try{
            if (this.bindingListeners.size() > 0){
                for (IBindingListener listener : this.bindingListeners){
                    listener.onMessage(channel, messageLine);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
    public final void raiseOnError(Object channel, Object exception){
        try{
            if (this.bindingListeners.size() > 0){
                for (IBindingListener listener : this.bindingListeners){
                    listener.onError(channel, exception);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
}
