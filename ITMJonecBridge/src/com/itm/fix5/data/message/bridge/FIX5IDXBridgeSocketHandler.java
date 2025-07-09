/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.message.bridge;

import com.itm.fix5.data.jonec.message.processor.FIX5JonecMessageProcessor;
import com.itm.fix5.data.jonec.message.struct.FIX5IDXMessage;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController.FIX5IDXBridgeStatus;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController.FIX5IDXGroupMessageType;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.setup.ITMSocketListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5IDXBridgeSocketHandler implements ITMSocketListener {
    
    private final FIX5IDXBridgeController headCtrl;
    private final FIX5JonecMessageProcessor fix5JonecMsgMapper = new FIX5JonecMessageProcessor();
    
    //.informasi listener:
    private final List<FIX5IDXBridgeListener> fBridgeListeners = new ArrayList<>();
    
    public FIX5IDXBridgeSocketHandler(FIX5IDXBridgeController headController) {
        this.headCtrl = headController;
        //.EXXX.
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.INIT, "");
    }
    
    public FIX5JonecMessageProcessor getFix5JonecMsgMapper() {
        return fix5JonecMsgMapper;
    }

    public void addBridgeListener(FIX5IDXBridgeListener newListener){
        try{
            if (newListener == null){
                return;
            }
            if (!this.fBridgeListeners.contains(newListener)){
                this.fBridgeListeners.add(newListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    public void removeBridgeListener(FIX5IDXBridgeListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            if (this.fBridgeListeners.contains(oldListener)){
                this.fBridgeListeners.remove(oldListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnConnected(ITMSocketChannel channel){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<FIX5IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    FIX5IDXBridgeListener lstr = (FIX5IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onConnected(channel, headCtrl, headCtrl.getConnectionName());
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnDisconnected(ITMSocketChannel channel){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<FIX5IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    FIX5IDXBridgeListener lstr = (FIX5IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onDisconnected(channel, headCtrl, headCtrl.getConnectionName());
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnReceive(ITMSocketChannel channel, String messageLine, FIX5IDXMessage messageObject){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<FIX5IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    FIX5IDXBridgeListener lstr = (FIX5IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onReceive(channel, headCtrl, headCtrl.getConnectionName(), messageLine, messageObject);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnSent(ITMSocketChannel channel, String messageLine){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<FIX5IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    FIX5IDXBridgeListener lstr = (FIX5IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onSent(channel, headCtrl, headCtrl.getConnectionName(), messageLine);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnError(ITMSocketChannel channel, Exception exception){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<FIX5IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    FIX5IDXBridgeListener lstr = (FIX5IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onError(channel, headCtrl, headCtrl.getConnectionName(), exception);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    
    
    
    @Override
    public void onConnected(ITMSocketChannel channel) {
        //.set status:
        try{
            headCtrl.setStsBridgeStatus(FIX5IDXBridgeStatus.SCK_CONNECTED);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
        //.broadcast:
        raiseOnConnected(channel);
        
    }

    @Override
    public void onDisconnected(ITMSocketChannel channel) {
        //.set status:
        try{
            headCtrl.setStsBridgeStatus(FIX5IDXBridgeStatus.SCK_DISCONNECTED);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
        //.broadcast:
        raiseOnDisconnected(channel);
        
    }
    
    @Override
    public void onMessage(ITMSocketChannel channel, String messageLine) {
        
        //.broadcast:
        if (headCtrl.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE){
            raiseOnReceive(channel, messageLine, fix5JonecMsgMapper.parseMessage(messageLine));
        }
        
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
