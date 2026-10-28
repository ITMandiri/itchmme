/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dcsplitter.main;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import com.itm.xtream.inet.trading.dcsplitter.bridge.DCSplitterServerInputBridge;
import com.itm.xtream.inet.trading.dcsplitter.callback.DCSplitterServerCallbackController;
import com.itm.xtream.inet.trading.feed.util.FEEDMsgHelper;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.Date;

/**
 *
 * @author Ari Pambudi
 */
public class DCSplitterServerAccess implements Runnable {
    public final static DCSplitterServerAccess getInstance = new DCSplitterServerAccess();
    
    public ITMTradingServerSettingsMgr              itmSetting                  = ITMTradingServerSettingsMgr.getInstance;
    public DCSplitterServerInputBridge             itmDCInputBridge             = DCSplitterServerInputBridge.getInstance;
    public DCSplitterServerCallbackController      itmDCOutputCallback          = DCSplitterServerCallbackController.getInstance;
    
    private Thread thAccessWorker;
    
    public DCSplitterServerAccess(){
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public void runServerOnce(){
        try{
            if ((this.thAccessWorker == null) || (!this.thAccessWorker.isAlive())){
                this.thAccessWorker = new Thread(this);
                this.thAccessWorker.start();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "runServer:MainLoop4: " + ex0.getMessage());
        }
    }
    
    public void runServerAsync(){
        
        String szTradingDaysListed = itmSetting.getSettings().server_settings.splitter_dropcopy.tradingdays;
        int iActiveTimeBegin = FEEDMsgHelper.getInstance.str2Int(itmSetting.getSettings().server_settings.splitter_dropcopy.begindaytime);
        int iActiveTimeEnd = FEEDMsgHelper.getInstance.str2Int(itmSetting.getSettings().server_settings.splitter_dropcopy.enddaytime);
        
        boolean bIsCurrentTradingDay = false;
        boolean bIsCurrentActiveTime = false;
        
        boolean loopEnabled = true;
        while(loopEnabled){
            try{
                itmDCInputBridge.setCurrentDate(FEEDMsgHelper.getInstance.str2Int(ITMSoupBinTCPBridgePacketFormat.getDateDataFeedFormatFromDate(new Date())));
                bIsCurrentTradingDay = szTradingDaysListed.contains(String.valueOf(FEEDMsgHelper.getInstance.currentTradingDayNumber()));
                //.set current check untuk active time:
                bIsCurrentActiveTime = ((FEEDMsgHelper.getInstance.intIdxFmtCurrentPCTime() >= iActiveTimeBegin) && (FEEDMsgHelper.getInstance.intIdxFmtCurrentPCTime() <= iActiveTimeEnd));
                
                if ((itmSetting.getSettings().server_settings.splitter_dropcopy.port <= 0) || itmSetting.getSettings().server_settings.splitter_dropcopy.maxcount <= 0){
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "DCSplitter Client Connection DISABLED by settings, ListenPort=" + itmSetting.getSettings().server_settings.splitter_dropcopy.port + ", MaxCount=" + itmSetting.getSettings().server_settings.splitter_dropcopy.maxcount);
                    loopEnabled = false;
                    break;
                }
                
                if (itmDCInputBridge.getCurrentDate() > 0){
                    if ((bIsCurrentTradingDay == true) && (bIsCurrentActiveTime == true)){
                        if (!itmDCOutputCallback.isBusy()){
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Try Listen for DCSplitter Client Connection, ListenPort=" + itmSetting.getSettings().server_settings.splitter_dropcopy.port);
                            itmDCOutputCallback.setListenPort(itmSetting.getSettings().server_settings.splitter_dropcopy.port);
                            itmDCOutputCallback.startWorker();
                        }
                    }else{
                        if (itmDCOutputCallback.isBusy()){
                            itmDCOutputCallback.stopWorker();
                        }
                    }
                }
                
                try {
                    Thread.sleep(itmSetting.getSettings().server_settings.splitter_dropcopy.pausebreak > 0 ? itmSetting.getSettings().server_settings.splitter_dropcopy.pausebreak : 1);
                } catch (InterruptedException ex0) {
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0.getMessage());
                } catch (Exception ex0) {
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0.getMessage());
                }
                if (loopEnabled){
                    //... .
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "runServer:MainLoop44: " + ex0.getMessage());
            }
        }
    }

    @Override
    public void run() {
        runServerAsync();
    }
    
}
