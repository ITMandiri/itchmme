/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.setup.ITMSocketListener;
import com.itm.xtream.inet.trading.jonec.server.bridge.JONECSimInputBridge;
import com.itm.xtream.inet.trading.sync.connection.ITMTradingServerSyncConnectionMgr;

/**
 *
 * @author fredy
 */
public class JONECSimCallbackHandler implements ITMSocketListener {
    
    private final JONECSimCallbackController headCtrl;
    
    public JONECSimCallbackHandler(JONECSimCallbackController headController) {
        this.headCtrl = headController;
        JONECSimInputBridge.getInstance.setCallBackController(headController);
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    @Override
    public void onConnected(ITMSocketChannel channel) {
        if (ITMTradingServerSyncConnectionMgr.getInstance.isSafeForClientToConnect()){
            if (headCtrl.isChannelsProcessorsOnLimit()){

                channel.StopChannel();

                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "client channel connected but connection limit exceeded:" + channel + " > id:" + channel.getChannelID());

            }else{

                headCtrl.createChannelProcessor(channel);

                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "client channel connected:" + channel + " > id:" + channel.getChannelID());

            }
        }else{
            
            channel.StopChannel();

            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "client channel connected but denied by server sync connection check :" + channel + " > id:" + channel.getChannelID());

        }
    }

    @Override
    public void onDisconnected(ITMSocketChannel channel) {
        
        headCtrl.destroyChannelProcessor(channel);
        
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "client channel disconnected:" + channel + " > id:" + channel.getChannelID());
        
    }

    @Override
    public void onMessage(ITMSocketChannel channel, String messageLine) {
        JONECSimInputBridge.getInstance.processMessage(channel, messageLine);
    }

    @Override
    public void onSent(ITMSocketChannel channel, String messageLine) {
        
        
        
        
    }

    @Override
    public void onError(ITMSocketChannel channel, Exception exception) {
        
        //.logx:
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "client exception caught:" + channel + " > id:" + channel.getChannelID() + " > cause:" + exception.getCause().getMessage());
        
    }
    
}