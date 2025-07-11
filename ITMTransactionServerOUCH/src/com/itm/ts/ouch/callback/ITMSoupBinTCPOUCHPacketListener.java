/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import static com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketChannel;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketListener;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgClientHeartbeatPacket;
import com.itm.ts.ouch.consts.OUCHConsts;
import com.itm.ts.ouch.msgmemory.ITMOUCHMsgMemory;

/**
 *
 * @author fredy
 */
public class ITMSoupBinTCPOUCHPacketListener extends ITMSoupBinTCPBridgePacketFormat implements ITMSoupBinTCPBridgeSocketListener {
    private int mIntervalCount = 0;
    
    @Override
    public void onConnected(ITMSoupBinTCPBridgeSocketChannel channel) {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        
        
    }

    @Override
    public void onDisconnected(ITMSoupBinTCPBridgeSocketChannel channel) {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        
        
    }

    @Override
    public void onMessage(ITMSoupBinTCPBridgeSocketChannel channel, byte[] messageLine, ITMSoupBinTCPMsgBase messageObject) {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try {
            if ((channel != null) && (!isNullOrEmpty(channel.getConnectionName())) && (channel.getConnectionName().contains(OUCHConsts.OUCHConnectionType.CONNECTIONTYPE_TRANSACTION))){
                ITMOUCHMsgMemory.getInstance.mapMessage(messageLine, messageObject);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }

    @Override
    public void onSent(ITMSoupBinTCPBridgeSocketChannel channel, byte[] messageLine) {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        
        
    }

    @Override
    public void onError(ITMSoupBinTCPBridgeSocketChannel channel, Exception exception) {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        
        
    }

    @Override
    public void onTimeToHeartBeat(ITMSoupBinTCPBridgeSocketChannel channel) {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        try {
            if ((channel != null) && (channel.isClientHeartBeatEnabled()) && (channel.getSocketController().isAuthLastAccepted())){
                ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat SEND.M");
                ITMSoupBinTCPMsgClientHeartbeatPacket mObjClientHeartBeatReq = new ITMSoupBinTCPMsgClientHeartbeatPacket();
                
                
                byte[] arb_chb = mObjClientHeartBeatReq.buildPacket();
                boolean b_send = channel.sendMessageDirect(arb_chb);
                
                if (b_send){
                    //... .
                    ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat SEND.M1");
                }else{
                    ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat SEND.M2");
                }
                
                mIntervalCount++;
                
            }else{
                ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat SEND.N");
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        
    }
    
}