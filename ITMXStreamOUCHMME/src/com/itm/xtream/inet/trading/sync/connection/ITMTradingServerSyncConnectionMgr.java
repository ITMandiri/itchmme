/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.sync.connection;

import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.idx.data.qri.message.struct.QRIDataITMMessage;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketController;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketMgr;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackProcessor;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackController;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackProcessor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class ITMTradingServerSyncConnectionMgr { 
    
    public final static ITMTradingServerSyncConnectionMgr getInstance = new ITMTradingServerSyncConnectionMgr();
    
    public final static boolean ENABLE_SYNC_CONNECT                             = true;
    public final static boolean ENABLE_SYNC_CONNECT_OUCH                        = true;
    public final static boolean ENABLE_SYNC_CONNECT_FIX5                        = false;
    
    private Timer hTmrITCHCheckEndTime;
    
    public ITMTradingServerSyncConnectionMgr(){
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean isSafeForClientToConnect(){
        boolean mOut = false;
        try{
            if (ENABLE_SYNC_CONNECT){
                
                boolean bIs_OUCH_Used = false;
                boolean bIs_FIX5_Used = false;
                
                boolean bIs_OUCH_Ok = false;
                boolean bIs_FIX5_Ok = false;
                
                ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmOUCHs = ITMSoupBinTCPOUCHPacketMgr.getInstance.getAllConnectionLines();
                ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5s = FIX5IDXBridgeManager.getInstance.getChmFIX5ConnLines();
                
                for(ITMSoupBinTCPOUCHPacketController mEachConn : chmOUCHs.values()){
                    bIs_OUCH_Used = true;
                    if (mEachConn != null && mEachConn.getChannel() != null && mEachConn.getChannel().isConnected() && mEachConn.isAuthLastAccepted()){
                        bIs_OUCH_Ok = true;
                    }
                    break;
                }
                
                for(FIX5IDXBridgeController mEachConn : chmFIX5s.values()){
                    bIs_FIX5_Used = true;
                    if (mEachConn != null && mEachConn.getChannel() != null && mEachConn.getChannel().isConnected() && (!mEachConn.getChannel().isChannelAlreadyWasted()) && mEachConn.isAdminLoggedOn()){
                        bIs_FIX5_Ok = true;
                    }
                    break;
                }
               
                if (!bIs_OUCH_Used){
                    bIs_OUCH_Ok = true;
                }
                
                if (!bIs_FIX5_Used){
                    bIs_FIX5_Ok = true;
                }
                
                if (ENABLE_SYNC_CONNECT_OUCH == true && ENABLE_SYNC_CONNECT_FIX5 == true){      
                    mOut = (bIs_OUCH_Ok && bIs_FIX5_Ok);
                }else if (ENABLE_SYNC_CONNECT_OUCH == true){   
                    mOut = (bIs_OUCH_Ok);
                }else if (ENABLE_SYNC_CONNECT_FIX5 == true){
                    mOut = (bIs_FIX5_Ok);
                }
                
            }else{
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean isFix5Connected(){
        boolean bConnected = false;
        
        try{
            boolean bIs_Used = false;
            boolean bIs_Ok = false;
            ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5s = FIX5IDXBridgeManager.getInstance.getChmFIX5ConnLines();
            
            for(FIX5IDXBridgeController mEachConn : chmFIX5s.values()){
                bIs_Used = true;
                if (mEachConn.getChannel().isConnected() && (!mEachConn.getChannel().isChannelAlreadyWasted()) && mEachConn.isAdminLoggedOn()){
                    bIs_Ok = true;
                }
                break;
            }
            bConnected = (bIs_Used && bIs_Ok);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        } 
        return bConnected;
    }
    
    public boolean isOUCHConnected(){
        boolean bConnected = false;
        
        try{
            boolean bIs_Used = false;
            boolean bIs_Ok = false;
            ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmOUCHs = ITMSoupBinTCPOUCHPacketMgr.getInstance.getAllConnectionLines();
                
            for(ITMSoupBinTCPOUCHPacketController mEachConn : chmOUCHs.values()){
                bIs_Used = true;
                if (mEachConn.getChannel().isConnected() && mEachConn.isAuthLastAccepted()){
                    bIs_Ok = true;
                }
                break;
            }
            bConnected = (bIs_Used && bIs_Ok);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        } 
        return bConnected;
    }
    
    public void broadCastServerRGStatusToMartin(boolean bConnect){
        if (ITMFileLoggerVarsConsts.APP_CLIENT_CODE.equals(ITMFileLoggerVarsConsts.AppClientCode.CLIENT_RELI_LS)){
            return;
        }
        try{
            
            ConcurrentHashMap<ITMSocketChannel, MARTINSimCallbackProcessor> chmMartinChs = MARTINSimCallbackController.getInstance.getAllChannelsProcessorsList();
            List<ITMSocketChannel> lstChannels = new ArrayList<>();
            MARTINSimCallbackProcessor mProcessor;
            try{
                if (!chmMartinChs.isEmpty()){
                    lstChannels.clear();
                    Enumeration eKeys = chmMartinChs.keys();
                    while (eKeys.hasMoreElements()) {
                        lstChannels.add((ITMSocketChannel)eKeys.nextElement());
                    }
                    for(ITMSocketChannel mChannel : lstChannels){
                        mProcessor = chmMartinChs.get(mChannel);
                        QRIDataITMMessage mRespondITMMessageRG = new QRIDataITMMessage(new HashMap());

                        mRespondITMMessageRG.setfConnName(mProcessor.getConnName());

                        mRespondITMMessageRG.setfITMMsgType("servstat");

                        mRespondITMMessageRG.setfMsgSubType1("rg");
                        if (bConnect){
                            mRespondITMMessageRG.setfMsgSubValue1(1);
                        }
                        
                        mChannel.sendMessageDirect(mRespondITMMessageRG.msgToString());
                    }
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }

            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        } 
    }
    
    public void broadCastServerNGStatusToMartin(boolean bConnect){
        if (ITMFileLoggerVarsConsts.APP_CLIENT_CODE.equals(ITMFileLoggerVarsConsts.AppClientCode.CLIENT_RELI_LS)){
            return;
        }
        try{
            
            ConcurrentHashMap<ITMSocketChannel, MARTINSimCallbackProcessor> chmMartinChs = MARTINSimCallbackController.getInstance.getAllChannelsProcessorsList();
            List<ITMSocketChannel> lstChannels = new ArrayList<>();
            MARTINSimCallbackProcessor mProcessor;
            try{
                if (!chmMartinChs.isEmpty()){
                    lstChannels.clear();
                    Enumeration eKeys = chmMartinChs.keys();
                    while (eKeys.hasMoreElements()) {
                        lstChannels.add((ITMSocketChannel)eKeys.nextElement());
                    }
                    for(ITMSocketChannel mChannel : lstChannels){
                        mProcessor = chmMartinChs.get(mChannel);
                        QRIDataITMMessage mRespondITMMessageNG = new QRIDataITMMessage(new HashMap());

                        mRespondITMMessageNG.setfConnName(mProcessor.getConnName());

                        mRespondITMMessageNG.setfITMMsgType("servstat");

                        mRespondITMMessageNG.setfMsgSubType1("ng");
                        if (bConnect){
                            mRespondITMMessageNG.setfMsgSubValue1(1);
                        }
                        
                        mChannel.sendMessageDirect(mRespondITMMessageNG.msgToString());
                    }
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }

            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        } 
    }
    
    public int terminateAllClientConnections(boolean bOverrideEnableFlag){
        int mOut = 0;
        try{
            
            if (bOverrideEnableFlag || ENABLE_SYNC_CONNECT){
                
                ConcurrentHashMap<ITMSocketChannel, JONECSimCallbackProcessor> chmJonecChs = JONECSimCallbackController.getInstance.getAllChannelsProcessorsList();
                ConcurrentHashMap<ITMSocketChannel, MARTINSimCallbackProcessor> chmMartinChs = MARTINSimCallbackController.getInstance.getAllChannelsProcessorsList();
                
                List<ITMSocketChannel> lstChannels = new ArrayList<>();

                try{
                    if (!chmJonecChs.isEmpty()){
                        lstChannels.clear();
                        Enumeration eKeys = chmJonecChs.keys();
                        while (eKeys.hasMoreElements()) {
                            lstChannels.add((ITMSocketChannel)eKeys.nextElement());
                        }
                        for(ITMSocketChannel mChannel : lstChannels){
                            mChannel.StopChannel();
                            mOut++;
                        }
                    }
                }catch(Exception ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                }
                
                try{
                    if (!chmMartinChs.isEmpty()){
                        lstChannels.clear();
                        Enumeration eKeys = chmMartinChs.keys();
                        while (eKeys.hasMoreElements()) {
                            lstChannels.add((ITMSocketChannel)eKeys.nextElement());
                        }
                        for(ITMSocketChannel mChannel : lstChannels){
                            mChannel.StopChannel();
                            mOut++;
                        }
                    }
                }catch(Exception ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                }

            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public int startListenServerSyncBridges(){
        int mOut = 0;
        try{
            
            ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmOUCHs = ITMSoupBinTCPOUCHPacketMgr.getInstance.getAllConnectionLines();
            ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5s = FIX5IDXBridgeManager.getInstance.getChmFIX5ConnLines();
            
            try{
                if (!chmOUCHs.isEmpty()){
                    for(ITMSoupBinTCPOUCHPacketController mCtl : chmOUCHs.values()){
                        mCtl.addExtraEventHandler(new ITMTradingServerSyncConnOUCHHandler());
                        mOut++;
                    }
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
            
            try{
                if (!chmFIX5s.isEmpty()){
                    for(FIX5IDXBridgeController mCtl : chmFIX5s.values()){
                        mCtl.addExtraEventHandler(new ITMTradingServerSyncConnFIX5Handler());
                        mOut++;
                    }
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
                                   
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
            
}