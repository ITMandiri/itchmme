/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.server.main;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import com.itm.xtream.inet.trading.feed.server.bridge.FeedInputBridge;
import com.itm.xtream.inet.trading.feed.server.callback.FeedServerCallbackController;
import com.itm.xtream.inet.trading.feed.util.FEEDMsgHelper;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.Date;

/**
 *
 * @author fredy
 */
public class DataFeedAccess implements Runnable {
    public final static DataFeedAccess getInstance = new DataFeedAccess();
    
    public ITMTradingServerSettingsMgr      itmSetting                   = ITMTradingServerSettingsMgr.getInstance;
    public FeedInputBridge                  itmDFSInputBridge            = FeedInputBridge.getInstance;
    public FeedServerCallbackController     itmDFSOutputCallback         = FeedServerCallbackController.getInstance;
    
    private Thread thAccessWorker;
    
    public DataFeedAccess(){
        
    }
    
    public void runServerOnce(){
        try{
            if ((this.thAccessWorker == null) || (!this.thAccessWorker.isAlive())){
                this.thAccessWorker = new Thread(this);
                this.thAccessWorker.start();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "runServer:MainLoop1: " + ex0.getMessage());
        }
    }
    
    public void runServerAsync(){
        //. listener MsgMemory
        ///ITMFeedMsgMemory.getInstance.startListener(); //.apm:20210506:move to server main, for datafile restore process;
        
        String szTradingDaysListed = itmSetting.getSettings().server_settings.datafeed_client.tradingdays;
        int iActiveTimeBegin = FEEDMsgHelper.getInstance.str2Int(itmSetting.getSettings().server_settings.datafeed_client.begindaytime);
        int iActiveTimeEnd = FEEDMsgHelper.getInstance.str2Int(itmSetting.getSettings().server_settings.datafeed_client.enddaytime);
        
        boolean bIsCurrentTradingDay = false;
        boolean bIsCurrentActiveTime = false;
        
        boolean loopEnabled = true;
        while(loopEnabled){
            try{
                itmDFSInputBridge.setCurrentDate(FEEDMsgHelper.getInstance.str2Int(ITMSoupBinTCPBridgePacketFormat.getDateDataFeedFormatFromDate(new Date())));
                bIsCurrentTradingDay = szTradingDaysListed.contains(String.valueOf(FEEDMsgHelper.getInstance.currentTradingDayNumber()));
                //.set current check untuk active time:
                bIsCurrentActiveTime = ((FEEDMsgHelper.getInstance.intIdxFmtCurrentPCTime() >= iActiveTimeBegin) && (FEEDMsgHelper.getInstance.intIdxFmtCurrentPCTime() <= iActiveTimeEnd));
                
                
                if (itmDFSInputBridge.getCurrentDate() > 0){
                    if ((bIsCurrentTradingDay == true) && (bIsCurrentActiveTime == true)){

                        if (!itmDFSOutputCallback.bIsServerBound()){
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Try Listen for DataFeed Client Connection, ListenPort=" + itmSetting.getSettings().server_settings.datafeed_client.port);
                            itmDFSOutputCallback.serverConnect(itmSetting.getSettings().server_settings.datafeed_client.port);
                        }
                    }else{
                        if (itmDFSOutputCallback.bIsServerBound()){
                            itmDFSOutputCallback.serverDisconnect();
                        }
                    }
                    
                }
                
                try {
                    Thread.sleep(itmSetting.getSettings().server_settings.datafeed_client.pausebreak);
                } catch (InterruptedException ex0) {
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0.getMessage());
                } catch (Exception ex0) {
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0.getMessage());
                }
                if (loopEnabled){
                    
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "runServer:MainLoop11: " + ex0.getMessage());
            }
        }
    }

    @Override
    public void run() {
        runServerAsync();
    }
    
}
