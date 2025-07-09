/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.sync.connection;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.mis.itch.callback.ITMSoupBinTCPITCHPacketController;
import com.itm.mis.itch.callback.ITMSoupBinTCPITCHPacketMgr;
import com.itm.xtream.inet.trading.feed.msgmem.ITMFeedMsgMemory;
import com.itm.xtream.inet.trading.feed.server.callback.FeedServerCallbackController;
import com.itm.xtream.inet.trading.feed.server.callback.FeedServerCallbackProcessor;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class ITMTradingServerSyncConnectionMgr { //.apm:20210723:ref hrn:prevent or reduce data loss caused by not syncronized input output connections;
    
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
                
                boolean bIs_ITCH_Used = false;
                boolean bIs_ITCHMDF_Used = false;
                
                boolean bIs_ITCH_Ok = false;
                boolean bIs_ITCHMDF_Ok = false;
                
                ConcurrentHashMap<String, ITMSoupBinTCPITCHPacketController> chmITCHs = ITMSoupBinTCPITCHPacketMgr.getInstance.getAllConnectionLines();
//                ConcurrentHashMap<String, ITMSoupBinTCPITCHMDFPacketController> chmITCHMDFs = ITMSoupBinTCPITCHMDFPacketMgr.getInstance.getAllConnectionLines();

                for(ITMSoupBinTCPITCHPacketController mEachConn : chmITCHs.values()){
                    if (mEachConn != null && mEachConn.getConnectionName().toUpperCase().contains("TOTAL") && mEachConn.getConnectionName().toUpperCase().contains("VIEW")){
                        bIs_ITCH_Used = true;
                        if (mEachConn.getChannel() != null && mEachConn.getChannel().isConnected() && mEachConn.isAuthLastAccepted()){
                            bIs_ITCH_Ok = true;
                        }
                        break;
                    }
                }
                
//                for(ITMSoupBinTCPITCHMDFPacketController mEachConn : chmITCHMDFs.values()){
//                    if (mEachConn != null && mEachConn.getConnectionName().toUpperCase().contains("TOTAL") && mEachConn.getConnectionName().toUpperCase().contains("VIEW")){
//                        bIs_ITCHMDF_Used = true;
//                        if (mEachConn.getChannel() != null && mEachConn.getChannel().isConnected() && mEachConn.isAuthLastAccepted()){
//                            bIs_ITCHMDF_Ok = true;
//                        }
//                        break;
//                    }
//                }
                
                if (!bIs_ITCH_Used){
                    bIs_ITCH_Ok = true;
                }
                
                if (!bIs_ITCHMDF_Used){
                    bIs_ITCHMDF_Ok = true;
                }
                  
                mOut = (bIs_ITCH_Ok && bIs_ITCHMDF_Ok);
                
            }else{
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public void broadCastServerRGStatusToMartin(boolean bConnect){
        if (ITMFileLoggerVarsConsts.APP_CLIENT_CODE.equals(ITMFileLoggerVarsConsts.AppClientCode.CLIENT_RELI_LS)){
            return;
        }
    }
    
    public void broadCastServerNGStatusToMartin(boolean bConnect){
        if (ITMFileLoggerVarsConsts.APP_CLIENT_CODE.equals(ITMFileLoggerVarsConsts.AppClientCode.CLIENT_RELI_LS)){
            return;
        }
    }
    
    public int terminateAllClientConnections(boolean bOverrideEnableFlag){
        int mOut = 0;
        try{
            
            if (bOverrideEnableFlag || ENABLE_SYNC_CONNECT){
                
                ConcurrentHashMap<ITMSocketChannel, FeedServerCallbackProcessor> chmFeedChs = FeedServerCallbackController.getInstance.getAllChannelsProcessorsList();

                List<ITMSocketChannel> lstChannels = new ArrayList<>();

                try{
                    if (!chmFeedChs.isEmpty()){
                        lstChannels.clear();
                        Enumeration eKeys = chmFeedChs.keys();
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
            
            ConcurrentHashMap<String, ITMSoupBinTCPITCHPacketController> chmITCHs = ITMSoupBinTCPITCHPacketMgr.getInstance.getAllConnectionLines();           
//            ConcurrentHashMap<String, ITMSoupBinTCPITCHMDFPacketController> chmITCHMDFs = ITMSoupBinTCPITCHMDFPacketMgr.getInstance.getAllConnectionLines();           
            
            try{
                if (!chmITCHs.isEmpty()){
                    for(ITMSoupBinTCPITCHPacketController mCtl : chmITCHs.values()){
                        mCtl.addExtraEventHandler(new ITMTradingServerSyncConnITCHHandler());
                        mOut++;
                    }
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
            
//            try{
//                if (!chmITCHMDFs.isEmpty()){
//                    for(ITMSoupBinTCPITCHMDFPacketController mCtl : chmITCHMDFs.values()){
//                        mCtl.addExtraEventHandler(new ITMTradingServerSyncConnITCHHandler());
//                        mOut++;
//                    }
//                }
//            }catch(Exception ex0){
//                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
//            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public synchronized int startCheckITCHEndTime(){
        int mOut = 0;
        try{
            String zCheckEndTime = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.data_end_time;
            if (!StringHelper.isNullOrEmpty(zCheckEndTime)){
                final int iEndTime = StringHelper.toInt(zCheckEndTime.replaceAll(":", ""));
                if ((hTmrITCHCheckEndTime == null) && (iEndTime > 0)){
                    hTmrITCHCheckEndTime = new Timer();
                    hTmrITCHCheckEndTime.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try{
                                String zCurTime = DateTimeHelper.getTimeSVRTRXFormat();
                                int iCurTime = StringHelper.toInt(zCurTime.replaceAll(":", ""));
                                if (iCurTime >= iEndTime){
                                    
                                    int cActiveCount = 0;
                                    ConcurrentHashMap<String, ITMSoupBinTCPITCHPacketController> chmITCHs = ITMSoupBinTCPITCHPacketMgr.getInstance.getAllConnectionLines();
                                    
                                    for(ITMSoupBinTCPITCHPacketController mEachConn : chmITCHs.values()){
                                        if (mEachConn != null && mEachConn.getChannel() != null && mEachConn.getChannel().isConnected() && !mEachConn.getChannel().isChannelAlreadyWasted()){
                                            cActiveCount++;
                                        }
                                    }

                                    if (cActiveCount > 0){
                                        //.send end sending record:
                                        ITMFeedMsgMemory.getInstance.checkAndSendManuallyDFEndSendingRecord();
                                        
                                        //.diconnect all:
                                        for(ITMSoupBinTCPITCHPacketController mEachConn : chmITCHs.values()){
                                            if (mEachConn != null && mEachConn.getChannel() != null && mEachConn.getChannel().isConnected() && !mEachConn.getChannel().isChannelAlreadyWasted()){
                                                ITMSoupBinTCPITCHPacketMgr.getInstance.doDisconnectLine(false, mEachConn.getConnectionName());
                                            }
                                        }
                                        
                                        ///System.out.println("@iCurTime=" + iCurTime + ", iEndTime=" + iEndTime + ", cActiveCount=" + cActiveCount);
                                        
                                    }
                                    
//                                    int cActiveCountMDF = 0;
//                                    ConcurrentHashMap<String, ITMSoupBinTCPITCHMDFPacketController> chmITCHMDFs = ITMSoupBinTCPITCHMDFPacketMgr.getInstance.getAllConnectionLines();
//                                    
//                                    for(ITMSoupBinTCPITCHMDFPacketController mEachConn : chmITCHMDFs.values()){
//                                        if (mEachConn != null && mEachConn.getChannel() != null && mEachConn.getChannel().isConnected() && !mEachConn.getChannel().isChannelAlreadyWasted()){
//                                            cActiveCountMDF++;
//                                        }
//                                    }
//
//                                    if (cActiveCountMDF > 0){
//                                        //.send end sending record:
//                                        ITMFeedMsgMemory.getInstance.checkAndSendManuallyDFEndSendingRecord();
//                                        
//                                        //.diconnect all:
//                                        for(ITMSoupBinTCPITCHMDFPacketController mEachConn : chmITCHMDFs.values()){
//                                            if (mEachConn != null && mEachConn.getChannel() != null && mEachConn.getChannel().isConnected() && !mEachConn.getChannel().isChannelAlreadyWasted()){
//                                                ITMSoupBinTCPITCHMDFPacketMgr.getInstance.doDisconnectLine(false, mEachConn.getConnectionName());
//                                            }
//                                        }
//                                        
//                                        ///System.out.println("@iCurTime=" + iCurTime + ", iEndTime=" + iEndTime + ", cActiveCount=" + cActiveCount);
//                                        
//                                    }
                                    
                                }
                            }catch(Exception ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                            }
                        }
                    }, 1, 1000);
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}

