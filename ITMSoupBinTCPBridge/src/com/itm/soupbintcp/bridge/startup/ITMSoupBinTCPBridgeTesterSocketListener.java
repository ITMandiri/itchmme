/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.startup;

import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketChannel;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketListener;
import com.itm.soupbintcp.bridge.startup.ITMSoupBinTCPBridgeTester.ITMTestSoupBinTCPSocket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgClientHeartbeatPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgSequencedDataPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgUnknownDataPacket;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeTesterSocketListener extends ITMSoupBinTCPBridgePacketFormat implements ITMSoupBinTCPBridgeSocketListener {
    
    private ITMTestSoupBinTCPSocket headCtrl;
    
    long cAllMsgReceivedCount = 0;
    
    public ITMSoupBinTCPBridgeTesterSocketListener(ITMTestSoupBinTCPSocket headController) {
        this.headCtrl = headController;
    }
    
    @Override
    public void onConnected(ITMSoupBinTCPBridgeSocketChannel channel) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (channel != null){
            //... .
        }
    }

    @Override
    public void onDisconnected(ITMSoupBinTCPBridgeSocketChannel channel) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (channel != null){
            //... .
        }
    }

    @Override
    public void onMessage(ITMSoupBinTCPBridgeSocketChannel channel, byte[] messageLine, ITMSoupBinTCPMsgBase messageObject) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (channel != null){
            cAllMsgReceivedCount++;
            if (messageObject instanceof ITMSoupBinTCPMsgUnknownDataPacket){
                System.out.println("RECV_COUNT=" + cAllMsgReceivedCount + " > " + convertBytesToHex(messageLine));                
                String zMsg = new String(messageLine);
                System.out.println("onMessage (RECV_COUNT=" + cAllMsgReceivedCount + ") # BAD_MGS_LENGTH=" + messageLine.length + " @ " + messageObject + " : <" + zMsg + ">");
            }else if (messageObject instanceof ITMSoupBinTCPMsgSequencedDataPacket){
                System.out.println("onMessage (RECV_COUNT=" + cAllMsgReceivedCount + ") # SEQ_MGS_LENGTH=" + messageLine.length + " @ SEQ_NO=" + ((ITMSoupBinTCPMsgSequencedDataPacket) messageObject).getSelfSequencedNo() + " > " + convertBytesToHex(messageLine));                
            }else{
                System.out.println("onMessage (RECV_COUNT=" + cAllMsgReceivedCount + ") # OK_MGS_LENGTH=" + messageLine.length + " @ " + messageObject);
            }
            
//////            
//////            //.test berhenti paksa:
//////            if (headCtrl.getCurrentSequencedNo() == 5){
//////                headCtrl.doDisconnect();
//////            }
//////            
            
        }
    }

    @Override
    public void onSent(ITMSoupBinTCPBridgeSocketChannel channel, byte[] messageLine) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (channel != null){
            
        }
    }

    @Override
    public void onError(ITMSoupBinTCPBridgeSocketChannel channel, Exception exception) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (channel != null){
            
            System.err.println("ERROR: Exception=" + exception.getMessage());
            
        }
    }

    @Override
    public void onTimeToHeartBeat(ITMSoupBinTCPBridgeSocketChannel channel) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (channel != null){
            
            ITMSoupBinTCPMsgClientHeartbeatPacket mObjClientHeartBeatReq = new ITMSoupBinTCPMsgClientHeartbeatPacket();
            
            byte[] arb_chb = mObjClientHeartBeatReq.buildPacket();
            
            //System.out.println("ON_IDLE_SEND_CLIENT_HEARTBEAT: " + convertBytesToHex(arb_chb));
            
            boolean b_send = channel.sendMessageDirect(arb_chb);
            
            if (b_send){
                //... .
            }
            
        }
    }

}
