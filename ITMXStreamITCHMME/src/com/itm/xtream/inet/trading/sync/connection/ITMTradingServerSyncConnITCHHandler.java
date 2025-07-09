/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.sync.connection;

import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketChannel;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketListener;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;

/**
 *
 * @author fredy
 */
public class ITMTradingServerSyncConnITCHHandler implements ITMSoupBinTCPBridgeSocketListener {
    
    @Override
    public void onConnected(ITMSoupBinTCPBridgeSocketChannel channel) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ITMTradingServerSyncConnectionMgr.getInstance.startCheckITCHEndTime();
    }

    @Override
    public void onDisconnected(ITMSoupBinTCPBridgeSocketChannel channel) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ITMTradingServerSyncConnectionMgr.getInstance.terminateAllClientConnections(false);
    }

    @Override
    public void onMessage(ITMSoupBinTCPBridgeSocketChannel channel, byte[] messageLine, ITMSoupBinTCPMsgBase messageObject) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSent(ITMSoupBinTCPBridgeSocketChannel channel, byte[] messageLine) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onError(ITMSoupBinTCPBridgeSocketChannel channel, Exception exception) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTimeToHeartBeat(ITMSoupBinTCPBridgeSocketChannel channel) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
