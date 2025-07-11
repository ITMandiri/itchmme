/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.main;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import com.itm.xtream.inet.trading.feed.util.FEEDMsgHelper;
import com.itm.xtream.inet.trading.fix5.jonec.books.BookOfFIX5JonecRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.bridge.JONECSimInputBridge;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.msgmem.selector.JONECSimMsgMemory;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINNegDealList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINOrderList;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINTradeList;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.Date;

/**
 *
 * @author fredy
 */
public class JONECSimAccess implements Runnable {
    //.single instance ya:
    public final static JONECSimAccess getInstance = new JONECSimAccess();
    
    public ITMTradingServerSettingsMgr      itmSetting                      = ITMTradingServerSettingsMgr.getInstance;
    public JONECSimInputBridge              itmTRSInputBridge               = JONECSimInputBridge.getInstance;
    public JONECSimCallbackController       itmTRSOutputCallback            = JONECSimCallbackController.getInstance;
    
    private Thread thAccessWorker;
    
    public JONECSimAccess(){
        
    }
    
    public void runServerOnce(){
        try{
            if ((this.thAccessWorker == null) || (!this.thAccessWorker.isAlive())){
                this.thAccessWorker = new Thread(this);
                this.thAccessWorker.start();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "runServer:MainLoop2: " + ex0.getMessage());
        }
    }
    
    public void runServerAsync(){
        //. listener MsgMemory
        JONECSimMsgMemory.getInstance.startListener();
        
        String szTradingDaysListed = itmSetting.getSettings().server_settings.datafeed_client.tradingdays;
        int iActiveTimeBegin = FEEDMsgHelper.getInstance.str2Int(itmSetting.getSettings().server_settings.datafeed_client.begindaytime);
        int iActiveTimeEnd = FEEDMsgHelper.getInstance.str2Int(itmSetting.getSettings().server_settings.datafeed_client.enddaytime);
        
        boolean bIsCurrentTradingDay = false;
        boolean bIsCurrentActiveTime = false;
        //.orderlist dan tradelist
        
        //.restore at first:
        System.out.println("restoring data backupProcessorTokensToBrokerRefs..");
        BookOfJONECSimToken.getInstance.backupProcessorTokensToBrokerRefs.restoreMapFromFile();
        System.out.println("restoring data backupProcessorBrokerRefsToTokens..");
        BookOfJONECSimToken.getInstance.backupProcessorBrokerRefsToTokens.restoreMapFromFile();
        System.out.println("restoring data BookOfJONECSimOriginRequest..");
        BookOfJONECSimOriginRequest.getInstance.backupProcessor.restoreMapFromFile();
        System.out.println("restoring data BookOfJONECSimEveryRequest..");
        BookOfJONECSimEveryRequest.getInstance.backupProcessor.restoreMapFromFile();
        System.out.println("restoring data BookOfJONECSimCalcQty..");
        BookOfJONECSimCalcQty.getInstance.backupProcessor.restoreMapFromFile();
        //.orderlist dan tradelist
        System.out.println("restoring data BookOfMARTINOrderList..");
        BookOfMARTINOrderList.getInstance.backupProcessor.restoreMapFromFile();
        System.out.println("restoring data BookOfMARTINTradeList..");
        BookOfMARTINTradeList.getInstance.backupProcessor.restoreMapFromFile();
        System.out.println("restoring data BookOfMARTINNegDealList..");
        BookOfMARTINNegDealList.getInstance.backupProcessor.restoreMapFromFile();
        //.fix5:
        System.out.println("restoring data BookOfFIX5JonecRequest..");
        BookOfFIX5JonecRequest.getInstance.backupProcessorSend.restoreMapFromFile();
        System.out.println("restoring data BookOfFIX5JonecRequest..");
        BookOfFIX5JonecRequest.getInstance.backupProcessorRecv.restoreMapFromFile();
        System.out.println("restoring data BookOfFIX5JonecRequest..");
        BookOfFIX5JonecRequest.getInstance.restoreLastSeqFromMap();
        System.out.println("restoring data complete.");
        //.continue:
        boolean loopEnabled = true;
        while(loopEnabled){
            try{
                itmTRSInputBridge.setCurrentDate(FEEDMsgHelper.getInstance.str2Int(ITMSoupBinTCPBridgePacketFormat.getDateDataFeedFormatFromDate(new Date())));
                bIsCurrentTradingDay = szTradingDaysListed.contains(String.valueOf(FEEDMsgHelper.getInstance.currentTradingDayNumber()));
                //.set current check untuk active time:
                bIsCurrentActiveTime = ((FEEDMsgHelper.getInstance.intIdxFmtCurrentPCTime() >= iActiveTimeBegin) && (FEEDMsgHelper.getInstance.intIdxFmtCurrentPCTime() <= iActiveTimeEnd));
                
                
                if (itmTRSInputBridge.getCurrentDate() > 0){
                    if ((bIsCurrentTradingDay == true) && (bIsCurrentActiveTime == true)){

                        if ((!itmTRSOutputCallback.bIsServerBound()) && (itmSetting.getSettings().server_settings.jonec_client_connections.length > 0)){
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Try Listen for JONEC Sim Client Connection, ListenPort=" + itmSetting.getSettings().server_settings.jonec_client_connections[0].port);
                            itmTRSOutputCallback.serverConnect(itmSetting.getSettings().server_settings.jonec_client_connections[0].port);
                        }
                    }else{
                        if (itmTRSOutputCallback.bIsServerBound()){
                            itmTRSOutputCallback.serverDisconnect();
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
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "runServer:MainLoop22: " + ex0.getMessage());
            }
        }
    }
    
    @Override
    public void run() {
        runServerAsync();
    }
    
}

