/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.dynamicstruct;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPPacketType;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPDynStructParser {
    
    public final static ITMSoupBinTCPDynStructParser getInstance = new ITMSoupBinTCPDynStructParser();
    
    public ITMSoupBinTCPDynStructMsgBase parseITCHPacket(byte[] btPacketBytes, boolean isOutput, boolean isSequenced, ITMSoupBinTCPDynStructBase moduleDynStructObj){
        ITMSoupBinTCPDynStructMsgBase mOut = new ITMSoupBinTCPDynStructMsgBase(((ITMSoupBinTCPDynStructITCH)moduleDynStructObj).itch);
        try{
            if ((btPacketBytes != null) && (btPacketBytes.length > 0) && (moduleDynStructObj != null)){
                ITMSoupBinTCPDynStructCore mCore = ((ITMSoupBinTCPDynStructITCH)moduleDynStructObj).itch;
                if (mCore != null){
                    boolean bMsgFound = false;
                    int pBeginMsgPos = 3;
                    String zMsgType = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,pBeginMsgPos,1);
                    if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(zMsgType)){
                        for (ITMSoupBinTCPDynStructCore.CCS_message mMessage : mCore.messages) {
                            if ((isOutput) && (mMessage.direction.equalsIgnoreCase("OUT"))){
                                if ((zMsgType.equals(mMessage.type)) && (mMessage.sequenced == isSequenced)){
                                    bMsgFound = true;
                                }
                            }else if ((!isOutput) && (mMessage.direction.equalsIgnoreCase("IN"))){
                                if ((zMsgType.equals(mMessage.type)) && (mMessage.sequenced == isSequenced)){
                                    bMsgFound = true;
                                }
                            }
                            if (bMsgFound){
                                mOut.msg.type = mMessage.type;
                                mOut.msg.name = mMessage.name;
                                mOut.msg.description = mMessage.description;
                                mOut.msg.direction = mMessage.direction;
                                mOut.msg.sequenced = mMessage.sequenced;
                                if (mMessage.fields != null){
                                    mOut.msg.fields = mMessage.cloneFields();
                                    for (ITMSoupBinTCPDynStructCore.CCS_message.CCS_message_field mField : mOut.msg.fields) {
                                        if (mField.type.equalsIgnoreCase("Alpha")){
                                            mField.msgstrval = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,(pBeginMsgPos + mField.offset),mField.len);
                                        } else if ((mField.type.equalsIgnoreCase("Integer")) || (mField.type.equalsIgnoreCase("Token"))){
                                            mField.msglongval = ITMSoupBinTCPBridgePacketFormat.decodeLong(btPacketBytes,(pBeginMsgPos + mField.offset),mField.len);
                                        } else if (mField.type.equalsIgnoreCase("Price")) {
                                            mField.msgdoubleval = ITMSoupBinTCPBridgePacketFormat.decodeDouble(btPacketBytes,(pBeginMsgPos + mField.offset),mField.len);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if (!bMsgFound){
                            pBeginMsgPos = 2;
                            zMsgType = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,pBeginMsgPos,1);
                            if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(zMsgType)){
                                mOut = parseStdPacketHdr(btPacketBytes, isOutput, isSequenced, mOut);
                            }
                        }
                    }else{
                        pBeginMsgPos = 2;
                        zMsgType = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,pBeginMsgPos,1);
                        if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(zMsgType)){
                            mOut = parseStdPacketHdr(btPacketBytes, isOutput, isSequenced, mOut);
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ITMSoupBinTCPDynStructMsgBase parseOUCHPacket(byte[] btPacketBytes, boolean isOutput, boolean isSequenced, ITMSoupBinTCPDynStructBase moduleDynStructObj){
        ITMSoupBinTCPDynStructMsgBase mOut = new ITMSoupBinTCPDynStructMsgBase(((ITMSoupBinTCPDynStructOUCH)moduleDynStructObj).ouch);
        try{
            if ((btPacketBytes != null) && (btPacketBytes.length > 0) && (moduleDynStructObj != null)){
                ITMSoupBinTCPDynStructCore mCore = ((ITMSoupBinTCPDynStructOUCH)moduleDynStructObj).ouch;
                if (mCore != null){
                    boolean bMsgFound = false;
                    int pBeginMsgPos = 3;
                    String zMsgType = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,pBeginMsgPos,1);
                    if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(zMsgType)){
                        for (ITMSoupBinTCPDynStructCore.CCS_message mMessage : mCore.messages) {
                            if ((isOutput) && (mMessage.direction.equalsIgnoreCase("OUT"))){
                                if ((zMsgType.equals(mMessage.type)) && (mMessage.sequenced == isSequenced)){
                                    bMsgFound = true;
                                }
                            }else if ((!isOutput) && (mMessage.direction.equalsIgnoreCase("IN"))){
                                if ((zMsgType.equals(mMessage.type)) && (mMessage.sequenced == isSequenced)){
                                    bMsgFound = true;
                                }
                            }
                            if (bMsgFound){
                                mOut.msg.type = mMessage.type;
                                mOut.msg.name = mMessage.name;
                                mOut.msg.description = mMessage.description;
                                mOut.msg.direction = mMessage.direction;
                                mOut.msg.sequenced = mMessage.sequenced;
                                if (mMessage.fields != null){
                                    mOut.msg.fields = mMessage.cloneFields();
                                    for (ITMSoupBinTCPDynStructCore.CCS_message.CCS_message_field mField : mOut.msg.fields) {
                                        if (mField.type.equalsIgnoreCase("Alpha")){
                                            mField.msgstrval = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,(pBeginMsgPos + mField.offset),mField.len);
                                        } else if ((mField.type.equalsIgnoreCase("Integer")) || (mField.type.equalsIgnoreCase("Token"))){
                                            mField.msglongval = ITMSoupBinTCPBridgePacketFormat.decodeLong(btPacketBytes,(pBeginMsgPos + mField.offset),mField.len);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if (!bMsgFound){
                            pBeginMsgPos = 2;
                            zMsgType = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,pBeginMsgPos,1);
                            if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(zMsgType)){
                                mOut = parseStdPacketHdr(btPacketBytes, isOutput, isSequenced, mOut);
                            }
                        }
                    }else{
                        pBeginMsgPos = 2;
                        zMsgType = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,pBeginMsgPos,1);
                        if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(zMsgType)){
                            mOut = parseStdPacketHdr(btPacketBytes, isOutput, isSequenced, mOut);
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ITMSoupBinTCPDynStructMsgBase parseStdPacketHdr(byte[] btPacketBytes, boolean isOutput, boolean isSequenced, ITMSoupBinTCPDynStructMsgBase mDefMsg){
        ITMSoupBinTCPDynStructMsgBase mOut = mDefMsg;
        try{
            String zHdrNamePrefix = "SOUPBINTCP:";
            int pBeginMsgPos = 2;
            String zMsgType = ITMSoupBinTCPBridgePacketFormat.decodeString(btPacketBytes,pBeginMsgPos,1);
            if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(zMsgType)){
                switch (zMsgType) {
                    case SoupBinTCPPacketType.PACKETTYPE_DEBUG_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "DEBUG";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_LOGIN_ACCEPTED_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "LOGIN_ACCEPTED";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_LOGIN_REJECTED_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "LOGIN_REJECTED";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_SEQUENCED_DATA_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "UNKNOWN_SEQUENCED_DATA";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_SERVER_HEARTBEAT_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "SERVER_HEARTBEAT";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_END_OF_SESSION_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "END_OF_SESSION";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_LOGIN_REQUEST_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "LOGIN_REQUEST";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_UNSEQUENCED_DATA_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "UNKNOWN_UNSEQUENCED_DATA";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_CLIENT_HEARTBEAT_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "CLIENT_HEARTBEAT";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    case SoupBinTCPPacketType.PACKETTYPE_LOGOUT_REQUEST_PACKET:
                        mOut.msg.type = zMsgType;
                        mOut.msg.name = zHdrNamePrefix + "LOGOUT_REQUEST";
                        mOut.msg.description = mOut.msg.name;
                        mOut.msg.direction = (isOutput ? "OUT" : "IN");
                        mOut.msg.sequenced = isSequenced;
                        break;
                    default:
                        break;
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
