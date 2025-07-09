/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.packetparser;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPLength;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPPacketType;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgClientHeartbeatPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgDebugPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgEndOfSessionPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgLoginAcceptedPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgLoginRejectPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgLoginRequestPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgLogoutRequestPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgSequencedDataPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgServerHeartbeatPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgUnknownDataPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgUnsequencedDataPacket;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgePacketParser {
    
    public final static ITMSoupBinTCPBridgePacketParser getInstance = new ITMSoupBinTCPBridgePacketParser();
    
    public ITMSoupBinTCPMsgBase parsePacket(byte[] btPacketBytes){
        ITMSoupBinTCPMsgBase mOut = null;
        try{
            if ((btPacketBytes != null) && (btPacketBytes.length >= SoupBinTCPLength.MINIMUM_PACKET_SIZE)){
                String vPacketType = String.valueOf((char)btPacketBytes[2]);
                switch(vPacketType){
                    case SoupBinTCPPacketType.PACKETTYPE_DEBUG_PACKET:
                        ITMSoupBinTCPMsgDebugPacket mObjDebug = new ITMSoupBinTCPMsgDebugPacket();
                        mObjDebug.parsePacket(btPacketBytes);
                        mOut = mObjDebug;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_LOGIN_ACCEPTED_PACKET:
                        ITMSoupBinTCPMsgLoginAcceptedPacket mObjLoginAccepted = new ITMSoupBinTCPMsgLoginAcceptedPacket();
                        mObjLoginAccepted.parsePacket(btPacketBytes);
                        mOut = mObjLoginAccepted;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_LOGIN_REJECTED_PACKET:
                        ITMSoupBinTCPMsgLoginRejectPacket mObjLoginRejected = new ITMSoupBinTCPMsgLoginRejectPacket();
                        mObjLoginRejected.parsePacket(btPacketBytes);
                        mOut = mObjLoginRejected;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_SEQUENCED_DATA_PACKET:
                        ITMSoupBinTCPMsgSequencedDataPacket mObjSequencedData = new ITMSoupBinTCPMsgSequencedDataPacket();
                        mObjSequencedData.parsePacket(btPacketBytes);
                        mOut = mObjSequencedData;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_SERVER_HEARTBEAT_PACKET:
                        ITMSoupBinTCPMsgServerHeartbeatPacket mObjServerHeartbeat = new ITMSoupBinTCPMsgServerHeartbeatPacket();
                        mObjServerHeartbeat.parsePacket(btPacketBytes);
                        mOut = mObjServerHeartbeat;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_END_OF_SESSION_PACKET:
                        ITMSoupBinTCPMsgEndOfSessionPacket mObjEndOfSession = new ITMSoupBinTCPMsgEndOfSessionPacket();
                        mObjEndOfSession.parsePacket(btPacketBytes);
                        mOut = mObjEndOfSession;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_LOGIN_REQUEST_PACKET:
                        ITMSoupBinTCPMsgLoginRequestPacket mObjLoginRequest = new ITMSoupBinTCPMsgLoginRequestPacket();
                        mObjLoginRequest.parsePacket(btPacketBytes);
                        mOut = mObjLoginRequest;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_UNSEQUENCED_DATA_PACKET:
                        ITMSoupBinTCPMsgUnsequencedDataPacket mObjUnsequencedData = new ITMSoupBinTCPMsgUnsequencedDataPacket();
                        mObjUnsequencedData.parsePacket(btPacketBytes);
                        mOut = mObjUnsequencedData;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_CLIENT_HEARTBEAT_PACKET:
                        ITMSoupBinTCPMsgClientHeartbeatPacket mObjClientHeartbeat = new ITMSoupBinTCPMsgClientHeartbeatPacket();
                        mObjClientHeartbeat.parsePacket(btPacketBytes);
                        mOut = mObjClientHeartbeat;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_LOGOUT_REQUEST_PACKET:
                        ITMSoupBinTCPMsgLogoutRequestPacket mObjLogoutRequest = new ITMSoupBinTCPMsgLogoutRequestPacket();
                        mObjLogoutRequest.parsePacket(btPacketBytes);
                        mOut = mObjLogoutRequest;
                        break;
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        if (mOut == null){
            mOut = new ITMSoupBinTCPMsgUnknownDataPacket();
        }
        return mOut;
    }
}
