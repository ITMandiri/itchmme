/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.server.bridge;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.xtream.inet.trading.feed.util.FEEDMsgHelper;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author fredy
 */
public class FeedInputBridge {
    //.single instance ya:
    public final static FeedInputBridge getInstance = new FeedInputBridge();
    
    private final AtomicInteger aiCurrentDate                                   = new AtomicInteger(0);
    private final AtomicInteger aiLastBackupFileLineDate                        = new AtomicInteger(0);
    private final AtomicInteger aiLastReceivedLineDate                          = new AtomicInteger(0);
    
    private final AtomicInteger aiCurrentSequenceNo                             = new AtomicInteger(0);
    private final AtomicInteger aiLastBackupFileLineSequenceNo                  = new AtomicInteger(0);
    private final AtomicInteger aiLastReceivedLineSequenceNo                    = new AtomicInteger(0);
    
    private final ConcurrentHashMap<Integer, String> chmCurrentDataFeedLines    = new ConcurrentHashMap<>();
    
    private String zLastDataFeedServerWelcomeMessage                            = "WELCOME TO DATAFEED SERVER";
    
    private long LastReceivedTimeMilliSeconds                                   = 0;
    
    //.constructor:
    public FeedInputBridge(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public int getCurrentDate() {
        return this.aiCurrentDate.get();
    }
    
    public int setCurrentDate(int vNewValue){
        return this.aiCurrentDate.getAndSet(vNewValue);
    }
    
    public int getLastBackupFileLineDate() {
        return this.aiLastBackupFileLineDate.get();
    }
    
    public int setLastBackupFileLineDate(int vNewValue){
        return this.aiLastBackupFileLineDate.getAndSet(vNewValue);
    }
    
    public int getLastReceivedLineDate() {
        return this.aiLastReceivedLineDate.get();
    }
    
    public int setLastReceivedLineDate(int vNewValue){
        return this.aiLastReceivedLineDate.getAndSet(vNewValue);
    }
    
    public int getCurrentSequenceNo() {
        return this.aiCurrentSequenceNo.get();
    }
    
    public int setCurrentSequenceNo(int vNewValue){
        return this.aiCurrentSequenceNo.getAndSet(vNewValue);
    }
    
    public int getLastBackupFileLineSequenceNo() {
        return this.aiLastBackupFileLineSequenceNo.get();
    }
    
    public int setLastBackupFileLineSequenceNo(int vNewValue){
        return this.aiLastBackupFileLineSequenceNo.getAndSet(vNewValue);
    }
    
    public int getLastReceivedLineSequenceNo() {
        return this.aiLastReceivedLineSequenceNo.get();
    }
    
    public int setLastReceivedLineSequenceNo(int vNewValue){
        return this.aiLastReceivedLineSequenceNo.getAndSet(vNewValue);
    }
    
    public void addOrUpdateCurrentDataFeedLine(int SequenceNo, String DataFeedLine){
        if (this.chmCurrentDataFeedLines.containsKey(SequenceNo)){
            this.chmCurrentDataFeedLines.replace(SequenceNo, DataFeedLine);
        }else{
            this.chmCurrentDataFeedLines.put(SequenceNo, DataFeedLine);
        }
    }
    
    public void clearCurrentDataFeedLines(){
        if (!this.chmCurrentDataFeedLines.isEmpty()){
            this.chmCurrentDataFeedLines.clear();
        }
    }
    
    public String getCurrentDataFeedLine(int vSequenceNo){
        return (this.chmCurrentDataFeedLines.containsKey(vSequenceNo) ? this.chmCurrentDataFeedLines.get(vSequenceNo) : "");
    }
    
    public String getLastDataFeedServerWelcomeMessage() {
        return this.zLastDataFeedServerWelcomeMessage;
    }

    public void setLastDataFeedServerWelcomeMessage(String zLastDataFeedServerWelcomeMessage) {
        this.zLastDataFeedServerWelcomeMessage = zLastDataFeedServerWelcomeMessage;
    }
    
    public long getLastReceivedTimeMilliSeconds() {
        return LastReceivedTimeMilliSeconds;
    }

    public void setLastReceivedTimeMilliSeconds(long LastReceivedTimeMilliSeconds) {
        this.LastReceivedTimeMilliSeconds = LastReceivedTimeMilliSeconds;
    }
    
    public synchronized boolean parseRecord(boolean bFromBackup, int vDateCode, String szRecordLine){
        boolean mOut = false;
        if (szRecordLine != null){
            if (szRecordLine.length() > 0){
                String zCheckCRC = "";
                String[] arrRecordSplit = szRecordLine.split("[|]", -1);
                if (arrRecordSplit.length > 5){
                    boolean bCanContinue = true;
                    //.cari tahu date yg diberikan:
                    int vTmpExpectationDataFeedDate = vDateCode;
                    int vTmpRealityDataFeedDate = FEEDMsgHelper.getInstance.str2Int(arrRecordSplit[1].trim());
                    if (bCanContinue){
                        if (vTmpRealityDataFeedDate != vTmpExpectationDataFeedDate){
                            bCanContinue = false;
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "backup line date not equal: expectation: " + vTmpExpectationDataFeedDate + ", reality: " + vTmpRealityDataFeedDate);
                        }
                    }
                    if (bFromBackup){
                        setLastBackupFileLineDate(vTmpRealityDataFeedDate);
                    }else{
                        setLastReceivedLineDate(vTmpRealityDataFeedDate);
                    }
                    //.cari tahu sequence no yg diberikan:
                    int vTmpExpectationSequenceNo = ((bFromBackup ? getLastBackupFileLineSequenceNo() : getLastReceivedLineSequenceNo()) + 1);
                    int vTmpRealitySequenceNo = FEEDMsgHelper.getInstance.str2Int(arrRecordSplit[3].trim());
                    if (bCanContinue){
                        if (vTmpRealitySequenceNo != vTmpExpectationSequenceNo){
                            bCanContinue = false;
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "backup line sequence no not equal: expectation: " + vTmpExpectationSequenceNo + ", reality: " + vTmpRealitySequenceNo);
                        }
                    }
                    if (bFromBackup){
                        setLastBackupFileLineSequenceNo(vTmpRealitySequenceNo);
                    }else{
                        setLastReceivedLineSequenceNo(vTmpRealitySequenceNo);
                    }
                    //.cari tahu trading status:
                    if (bCanContinue){
                        //.proses per record type:
                        switch(arrRecordSplit[4].trim()){
                            case "0":
                                String zTradingStatusCode = arrRecordSplit[5].trim();
                                switch(zTradingStatusCode){
                                    case "1":
                                        //.pengiriman data mulai:
                                        break;
                                    case "7":
                                        //.pengiriman data selesai:
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }

                    }
                    
                    if (bCanContinue){
                        addOrUpdateCurrentDataFeedLine(vTmpRealitySequenceNo, szRecordLine);
                        setCurrentSequenceNo(vTmpRealitySequenceNo);
                    }
                    mOut = bCanContinue;
                }
            }
        }
        return mOut;
    }
    
}
