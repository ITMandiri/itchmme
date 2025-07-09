/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.sockets;

import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;

/**
 *
 * @author Ari Pambudi
 */
public interface ITMSoupBinTCPBridgeSocketListener {
    
    public abstract void onConnected (ITMSoupBinTCPBridgeSocketChannel channel); //.saat terhubung.
    public abstract void onDisconnected (ITMSoupBinTCPBridgeSocketChannel channel); //.saat putus.
    public abstract void onMessage (ITMSoupBinTCPBridgeSocketChannel channel, byte[] messageLine, ITMSoupBinTCPMsgBase messageObject); //.saat terima bytes array.
    public abstract void onSent (ITMSoupBinTCPBridgeSocketChannel channel, byte[] messageLine); //.saat kirim bytes array.
    public abstract void onError (ITMSoupBinTCPBridgeSocketChannel channel, Exception exception); //.saat ada error.
    public abstract void onTimeToHeartBeat(ITMSoupBinTCPBridgeSocketChannel channel); //.saat yang tepat untuk kirim heartbeat;
    
}
