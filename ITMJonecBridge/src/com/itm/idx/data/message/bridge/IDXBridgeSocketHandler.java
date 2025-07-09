/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.message.bridge;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.setup.ITMSocketListener;
import com.itm.idx.data.message.bridge.IDXBridgeController.IDXBridgeStatus;
import com.itm.idx.data.message.bridge.IDXBridgeController.IDXGroupMessageType;
import com.itm.idx.data.ori.message.processor.ORIMessageProcessor;
import com.itm.idx.data.qri.message.process.QRIMessageProcessor;
import com.itm.idx.message.IDXMessage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author aripam
 */
public class IDXBridgeSocketHandler implements ITMSocketListener {
    
    private IDXBridgeController headCtrl;
    private ORIMessageProcessor oriMsgMapper = new ORIMessageProcessor();
    private QRIMessageProcessor qriMsgMapper = new QRIMessageProcessor();
    
    //.informasi listener:
    private List<IDXBridgeListener> fBridgeListeners = new ArrayList<>();
    
    public IDXBridgeSocketHandler(IDXBridgeController headController) {
        this.headCtrl = headController;
        //.EXXX.
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.INIT, "");
    }
    
    public void addBridgeListener(IDXBridgeListener newListener){
        try{
            if (newListener == null){
                return;
            }
            if (!this.fBridgeListeners.contains(newListener)){
                this.fBridgeListeners.add(newListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
    }
    
    public void removeBridgeListener(IDXBridgeListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            if (this.fBridgeListeners.contains(oldListener)){
                this.fBridgeListeners.remove(oldListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnConnected(ITMSocketChannel channel){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    IDXBridgeListener lstr = (IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onConnected(channel, headCtrl, headCtrl.getConnectionName());
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnDisconnected(ITMSocketChannel channel){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    IDXBridgeListener lstr = (IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onDisconnected(channel, headCtrl, headCtrl.getConnectionName());
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnReceive(ITMSocketChannel channel, String messageLine, IDXMessage messageObject){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    IDXBridgeListener lstr = (IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onReceive(channel, headCtrl, headCtrl.getConnectionName(), messageLine, messageObject);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnSent(ITMSocketChannel channel, String messageLine){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    IDXBridgeListener lstr = (IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onSent(channel, headCtrl, headCtrl.getConnectionName(), messageLine);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnError(ITMSocketChannel channel, Exception exception){
        try{
            if (this.fBridgeListeners.size() > 0){
                for (Iterator<IDXBridgeListener> iterBridgeListener = this.fBridgeListeners.iterator(); iterBridgeListener.hasNext(); ){
                    IDXBridgeListener lstr = (IDXBridgeListener)iterBridgeListener.next();
                    if (lstr != null){
                        lstr.onError(channel, headCtrl, headCtrl.getConnectionName(), exception);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
    }
    
    
    
    
    @Override
    public void onConnected(ITMSocketChannel channel) {
        //.set status:
        try{
            headCtrl.setStsBridgeStatus(IDXBridgeStatus.SCK_CONNECTED);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
        //.broadcast:
        raiseOnConnected(channel);
        
    }

    @Override
    public void onDisconnected(ITMSocketChannel channel) {
        //.set status:
        try{
            headCtrl.setStsBridgeStatus(IDXBridgeStatus.SCK_DISCONNECTED);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
        //.broadcast:
        raiseOnDisconnected(channel);
        
    }
    
    @Override
    public void onMessage(ITMSocketChannel channel, String messageLine) {
        
        //.broadcast:
        if (headCtrl.getMsgGroupType() == IDXGroupMessageType.ORI_MESSAGE){
            raiseOnReceive(channel, messageLine, oriMsgMapper.parseMessage(messageLine, false));
        }else if (headCtrl.getMsgGroupType() == IDXGroupMessageType.QRI_MESSAGE){
            raiseOnReceive(channel, messageLine, qriMsgMapper.processMessage(messageLine, false));
        }else if (headCtrl.getMsgGroupType() == IDXGroupMessageType.ORM_MESSAGE){
            raiseOnReceive(channel, messageLine, oriMsgMapper.parseMessage(messageLine, false));
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
