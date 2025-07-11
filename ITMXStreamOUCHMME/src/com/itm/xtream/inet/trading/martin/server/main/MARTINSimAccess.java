/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.main;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import com.itm.xtream.inet.trading.feed.util.FEEDMsgHelper;
import com.itm.xtream.inet.trading.martin.server.bridge.MARTINSimInputBridge;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackController;
import com.itm.xtream.inet.trading.martin.server.msgmem.selector.MARTINSimMsgMemory;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.Date;

/**
 *
 * @author fredy
 */
public class MARTINSimAccess implements Runnable {
    //.single instance ya:
    public final static MARTINSimAccess getInstance = new MARTINSimAccess();
    
    public ITMTradingServerSettingsMgr      itmSetting                      = ITMTradingServerSettingsMgr.getInstance;
    public MARTINSimInputBridge             itmMISInputBridge               = MARTINSimInputBridge.getInstance;
    public MARTINSimCallbackController      itmMISOutputCallback            = MARTINSimCallbackController.getInstance;
    
    private Thread thAccessWorker;
    
    public MARTINSimAccess(){
        
    }
    
    public void runServerOnce(){
        try{
            if ((this.thAccessWorker == null) || (!this.thAccessWorker.isAlive())){
                this.thAccessWorker = new Thread(this);
                this.thAccessWorker.start();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "runServer:MainLoop3: " + ex0.getMessage());
        }
    }
    
    public void runServerAsync(){
        //. listener MsgMemory
        MARTINSimMsgMemory.getInstance.startListener();
        
        String szTradingDaysListed = itmSetting.getSettings().server_settings.datafeed_client.tradingdays;
        int iActiveTimeBegin = FEEDMsgHelper.getInstance.str2Int(itmSetting.getSettings().server_settings.datafeed_client.begindaytime);
        int iActiveTimeEnd = FEEDMsgHelper.getInstance.str2Int(itmSetting.getSettings().server_settings.datafeed_client.enddaytime);
        
        boolean bIsCurrentTradingDay = false;
        boolean bIsCurrentActiveTime = false;
        
        boolean loopEnabled = true;
        while(loopEnabled){
            try{
                itmMISInputBridge.setCurrentDate(FEEDMsgHelper.getInstance.str2Int(ITMSoupBinTCPBridgePacketFormat.getDateDataFeedFormatFromDate(new Date())));
                bIsCurrentTradingDay = szTradingDaysListed.contains(String.valueOf(FEEDMsgHelper.getInstance.currentTradingDayNumber()));
                //.set current check untuk active time:
                bIsCurrentActiveTime = ((FEEDMsgHelper.getInstance.intIdxFmtCurrentPCTime() >= iActiveTimeBegin) && (FEEDMsgHelper.getInstance.intIdxFmtCurrentPCTime() <= iActiveTimeEnd));
                
                
                if (itmMISInputBridge.getCurrentDate() > 0){
                    if ((bIsCurrentTradingDay == true) && (bIsCurrentActiveTime == true)){

                        if ((!itmMISOutputCallback.bIsServerBound()) && (itmSetting.getSettings().server_settings.martin_client_connections.length > 0)){
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Try Listen for MARTIN Sim Client Connection, ListenPort=" + itmSetting.getSettings().server_settings.martin_client_connections[0].port);
                            itmMISOutputCallback.serverConnect(itmSetting.getSettings().server_settings.martin_client_connections[0].port);
                        }
                    }else{
                        if (itmMISOutputCallback.bIsServerBound()){
                            itmMISOutputCallback.serverDisconnect();
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
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "runServer:MainLoop33: " + ex0.getMessage());
            }
        }
    }
    
    @Override
    public void run() {
        runServerAsync();
    }
    
}
