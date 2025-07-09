/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.mis.itch.consts.ITCHConsts.ITCHConnectionType;
import com.itm.mis.itch.msgmemory.ITMITCHMsgMemory;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketChannel;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketListener;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgClientHeartbeatPacket;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPITCHPacketListener extends ITMSoupBinTCPBridgePacketFormat implements ITMSoupBinTCPBridgeSocketListener {

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
            if ((channel != null) && (!isNullOrEmpty(channel.getConnectionName()))){
                if (channel.getConnectionName().contains(ITCHConnectionType.CONNECTIONTYPE_ITCH)) {
                    ITMITCHMsgMemory.getInstance.mapMessage(messageLine, messageObject);
                }
                else if (channel.getConnectionName().contains(ITCHConnectionType.CONNECTIONTYPE_ITCH_MDF)) {
                    ITMITCHMsgMemory.getInstance.itchMDFMapMessage(messageLine, messageObject);
                }
                
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
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
            if ((channel != null) && (channel.isClientHeartBeatEnabled())){
                ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat SEND.K");
                ITMSoupBinTCPMsgClientHeartbeatPacket mObjClientHeartBeatReq = new ITMSoupBinTCPMsgClientHeartbeatPacket();
                
                byte[] arb_chb = mObjClientHeartBeatReq.buildPacket();
                boolean b_send = channel.sendMessageDirect(arb_chb);

                if (b_send){
                    //... .
                    ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat SEND.K1");
                }else{
                    ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat SEND.K2");
                }
                
            }else{
                ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat SEND.L");
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        
    }
    
}
