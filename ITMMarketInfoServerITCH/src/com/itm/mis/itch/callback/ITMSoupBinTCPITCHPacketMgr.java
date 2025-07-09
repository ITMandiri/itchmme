/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.mis.itch.callback.ITMSoupBinTCPITCHPacketController.ITCHBridgeStatus;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPITCHPacketMgr {
    
    public final static ITMSoupBinTCPITCHPacketMgr getInstance = new ITMSoupBinTCPITCHPacketMgr();
    
    private final ConcurrentHashMap<String, ITMSoupBinTCPITCHPacketController> chmConnections = new ConcurrentHashMap<>();
    
    public ITMSoupBinTCPITCHPacketMgr(){
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public ConcurrentHashMap<String, ITMSoupBinTCPITCHPacketController> getAllConnectionLines(){
        return this.chmConnections;
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
            ITMSoupBinTCPITCHPacketController mController = null;
            if (!this.chmConnections.containsKey(zConnectionName)){
                mController = new ITMSoupBinTCPITCHPacketController(zConnectionName, zDescription, iOrderNo, zIPAddress, iPort, iTimeOut, bHeartBeat, bCmpMsgExact, iReconSeqMode, bAutoSelect, iSocketSendBufferSize, iSocketRecvBufferSize, bSocketSendAutoFlush);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public int doConnectLine(boolean bAllLines, String zConnectionName){
        int mOut = 0;
        try {
            if (!chmConnections.isEmpty()){
                for(ITMSoupBinTCPITCHPacketController mEachController : this.chmConnections.values()){
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public int doDisconnectLine(boolean bAllLines, String zConnectionName){
        int mOut = 0;
        try {
            if (!chmConnections.isEmpty()){
                for(ITMSoupBinTCPITCHPacketController mEachController : this.chmConnections.values()){
                    if ((mEachController != null) && ((bAllLines) || (mEachController.getConnectionName().equalsIgnoreCase(zConnectionName)))) {
                        if (mEachController.isConnected()){
                            mEachController.doStopConnection();
                            if (!mEachController.isConnected()){
                                mOut++;
                            }
                        } else if (!mEachController.isConnected()){
                            if (mEachController.getStsBridgeStatus() == ITCHBridgeStatus.SCK_CONNECTING){
                                mEachController.doStopConnection();
                            }
                            mOut++;
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
