/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.sync.connection;

import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.setup.ITMSocketListener;

/**
 *
 * @author fredy
 */
public class ITMTradingServerSyncConnFIX5Handler implements ITMSocketListener {

    @Override
    public void onConnected(ITMSocketChannel channel) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ITMTradingServerSyncConnectionMgr.getInstance.broadCastServerNGStatusToMartin(true);
    }

    @Override
    public void onDisconnected(ITMSocketChannel channel) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ITMTradingServerSyncConnectionMgr.getInstance.terminateAllClientConnections(false);
        ITMTradingServerSyncConnectionMgr.getInstance.broadCastServerNGStatusToMartin(false);
    }

    @Override
    public void onMessage(ITMSocketChannel channel, String messageLine) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSent(ITMSocketChannel channel, String messageLine) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onError(ITMSocketChannel channel, Exception exception) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
