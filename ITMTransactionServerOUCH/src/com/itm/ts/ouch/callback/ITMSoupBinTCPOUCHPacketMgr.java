/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class ITMSoupBinTCPOUCHPacketMgr {
    
    public final static ITMSoupBinTCPOUCHPacketMgr getInstance = new ITMSoupBinTCPOUCHPacketMgr();
    
    private final ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmConnections = new ConcurrentHashMap<>();
    
    public ITMSoupBinTCPOUCHPacketMgr(){
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> getAllConnectionLines(){
        return this.chmConnections;
    }
    
    public ITMSoupBinTCPOUCHPacketController getNextActiveConnectionLine(){
        ITMSoupBinTCPOUCHPacketController mOut = null;
        try {
            if ((this.chmConnections != null) && (!this.chmConnections.isEmpty())){
                for(ITMSoupBinTCPOUCHPacketController mTgt : this.chmConnections.values()){
                    if ((mTgt.getChannel() != null) && (mTgt.isConnected()) && (!mTgt.getChannel().isChannelAlreadyWasted())){
                        mOut = mTgt;
                        break;
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public synchronized int addConnectionLine(
            String zConnectionName
            , String zDescription
            , int iOrderNo
            , String zIPAddress
            , int iPort
            , int iTimeOut
            , String zUserCode
            , String zPassword
            , boolean bHeartBeat
            , boolean bCmpMsgExact
            , int iReconSeqMode
            , boolean bAutoSelect
            , int iSocketSendBufferSize
            , int iSocketRecvBufferSize            
            , boolean bSocketSendAutoFlush
        ) {
        int mOut = 0;
        try{
            ITMSoupBinTCPOUCHPacketController mController = null;
            if (!this.chmConnections.containsKey(zConnectionName)){
                mController = new ITMSoupBinTCPOUCHPacketController(zConnectionName, zDescription, iOrderNo, zIPAddress, iPort, iTimeOut, bHeartBeat, bCmpMsgExact, iReconSeqMode, bAutoSelect, iSocketSendBufferSize, iSocketRecvBufferSize, bSocketSendAutoFlush);
                mController.setAuthAutoLogin(true);
                mController.setAuthUserName(zUserCode);
                mController.setAuthUserPassword(zPassword);
                this.chmConnections.put(zConnectionName, mController);
                
            }else{
                mController = this.chmConnections.get(zConnectionName);
                mController.setDescription(zDescription);
                mController.setOrderNo(iOrderNo);
                mController.setAuthAutoLogin(true);
                mController.setAuthUserName(zUserCode);
                mController.setAuthUserPassword(zPassword);
                mController.setIPAddress(zIPAddress);
                mController.setPort(iPort);
                mController.setTimeOut(iTimeOut);
                mController.setHeartBeatEnabled(bHeartBeat);
                mController.setCmpMsgExact(bCmpMsgExact);
                mController.setReconSeqMode(iReconSeqMode);
                mController.setAutoSelect(bAutoSelect);
            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public int doConnectLine(boolean bAllLines, String zConnectionName){
        int mOut = 0;
        try {
            if (!chmConnections.isEmpty()){
                for(ITMSoupBinTCPOUCHPacketController mEachController : this.chmConnections.values()){
                    if ((mEachController != null) && ((bAllLines) || (mEachController.getConnectionName().equalsIgnoreCase(zConnectionName)))) {
                        if (!mEachController.isConnected()){
                            mEachController.doStartConnection();
                            if (mEachController.isConnected()){
                                mOut++;
                            }
                        } else if (mEachController.isConnected()){
                            mOut++;
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.ITCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public int doDisconnectLine(boolean bAllLines, String zConnectionName){
        int mOut = 0;
        try {
            if (!chmConnections.isEmpty()){
                for(ITMSoupBinTCPOUCHPacketController mEachController : this.chmConnections.values()){
                    if ((mEachController != null) && ((bAllLines) || (mEachController.getConnectionName().equalsIgnoreCase(zConnectionName)))) {
                        if (mEachController.isConnected()){
                            mEachController.doStopConnection();
                            if (!mEachController.isConnected()){
                                mOut++;
                            }
                        } else if (!mEachController.isConnected()){
                            if (mEachController.getStsBridgeStatus() == ITMSoupBinTCPOUCHPacketController.OUCHBridgeStatus.SCK_CONNECTING){
                                mEachController.doStopConnection();
                            }
                            mOut++;
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.ITCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public int doSetSendBufferLine(boolean bAllLines, String zConnectionName, int iBufferSize){
        int mOut = 0;
        try {
            if (!chmConnections.isEmpty() && iBufferSize > 0){
                for(ITMSoupBinTCPOUCHPacketController mEachController : this.chmConnections.values()){
                    if ((mEachController != null) && ((bAllLines) || (mEachController.getConnectionName().equalsIgnoreCase(zConnectionName)))) {
                        if (mEachController.isConnected()){
                            if (iBufferSize != mEachController.getSocketSendBufferSize()){ // pastikan belm sama
                                mEachController.setSocketSendBufferSize(iBufferSize);
                                if (mEachController.getChannel() != null){
                                    mEachController.getChannel().getSocket().setSendBufferSize(iBufferSize);
                                    mOut++;
                                    System.err.println("iBufferSize = "  + iBufferSize);
                                    System.err.println("ITMSoupBinTCPBridgeSocketCtl NEW sock.getSendBufferSize = " + mEachController.getChannel().getSocket().getSendBufferSize());
                                }
                                
                            }
                            
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.ITCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public int doSetRecvBufferLine(boolean bAllLines, String zConnectionName, int iBufferSize){
        int mOut = 0;
        try {
            if (!chmConnections.isEmpty() && iBufferSize > 0){
                for(ITMSoupBinTCPOUCHPacketController mEachController : this.chmConnections.values()){
                    if ((mEachController != null) && ((bAllLines) || (mEachController.getConnectionName().equalsIgnoreCase(zConnectionName)))) {
                        if (mEachController.isConnected()){
                            if (iBufferSize != mEachController.getSocketReceiveBufferSize()){ // pastikan belm sama
                                mEachController.setSocketReceiveBufferSize(iBufferSize);
                                if (mEachController.getChannel() != null){
                                    mEachController.getChannel().getSocket().setReceiveBufferSize(iBufferSize);
                                    mOut++;
                                    System.err.println("iBufferSize = "  + iBufferSize);
                                    System.err.println("ITMSoupBinTCPBridgeSocketCtl NEW sock.getReceiveBufferSize = " + mEachController.getChannel().getSocket().getReceiveBufferSize());
                                }
                                
                            }
                            
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.ITCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
