/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.server.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.xtream.inet.trading.feed.msgmem.ITMFeedMsgMemory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author fredy
 */
public class FeedServerCallbackProcessor implements Runnable {
    
    private ITMSocketChannel chChannel;
    private final AtomicBoolean bAlreadyLoggedIn                        = new AtomicBoolean(false);
    
    private Thread thOutWorker;
    private final AtomicBoolean bOutWorkerMustRun                       = new AtomicBoolean(false);
    private final AtomicBoolean bNeedAdjustSequence                     = new AtomicBoolean(false);
    
    private final AtomicInteger aiSequenceNo                            = new AtomicInteger(0);
    
    public FeedServerCallbackProcessor(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    //.basic info:
    public ITMSocketChannel getChChannel() {
        return this.chChannel;
    }
    
    public void setChChannel(ITMSocketChannel chChannel) {
        this.chChannel = chChannel;
    }
    
    public boolean getAlreadyLoggedIn() {
        return this.bAlreadyLoggedIn.get();
    }
    
    public void setAlreadyLoggedIn(boolean bLoggedIn) {
        this.bAlreadyLoggedIn.set(bLoggedIn);
    }
    
    public int getSequenceNo() {
        return this.aiSequenceNo.get();
    }
    
    public synchronized void adjustSequenceNo(int newSequenceNo){
        try{
            this.aiSequenceNo.set(newSequenceNo);
            this.bNeedAdjustSequence.set(true);
            setAlreadyLoggedIn(true);
            wakeUpProcessor();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "terminate processor exception:" + ex0);
        }
    }
    
    public synchronized void startProcessor(){
        try{
            if (this.thOutWorker == null){
                this.thOutWorker = new Thread(this);
            }
            if (this.thOutWorker != null){
                if (!this.thOutWorker.isAlive()){
                    try{
                        this.thOutWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                        this.bOutWorkerMustRun.set(true);
                        this.thOutWorker.setPriority(Thread.NORM_PRIORITY);
                        this.thOutWorker.start();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot start output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }else{
                    try{
                        wakeUpProcessor();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-resume output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "start processor exception:" + ex0);
        }
    }
    
    public synchronized void wakeUpProcessor(){
        try{
            if (this.thOutWorker != null){
                if (this.thOutWorker.isAlive()){
                    try{
                        notify();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-resume output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "wakeup processor exception:" + ex0);
        }
    }
    
    public synchronized void stopProcessor(){
        try{
            if (this.thOutWorker != null){
                if (this.thOutWorker.isAlive()){
                    try{
                        this.bOutWorkerMustRun.set(false);
                        wakeUpProcessor();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-close output processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "stop processor exception:" + ex0);
        }
    }
    
    @Override
    public void run() {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //. hrn-20210705 : variable untuk jeda ping (idle)
        int iIdleIntervalCount = 0;
        boolean bNeedToLog = false;
        int iToBeSendSequenceNo = 0;
        String zToBeSendDataFeedLine = "";
        
        while(this.bOutWorkerMustRun.get()){
            if (chChannel != null){
                if (chChannel.isConnected() && (!chChannel.isClosed())){
                    try{
                        //.kirim jika ada:
                        if (getAlreadyLoggedIn()){
                            if (bNeedAdjustSequence.get() == true){
                                bNeedToLog = true;
                                iToBeSendSequenceNo = getSequenceNo();
                                bNeedAdjustSequence.set(false);
                            }else{
                                bNeedToLog = false;
                                iToBeSendSequenceNo = (getSequenceNo() + 1);
                            }
                            while(bNeedAdjustSequence.get() == false){
                                if (iToBeSendSequenceNo <= ITMFeedMsgMemory.getInstance.getLstFeedStr().size()){

                                    if (ITMFeedMsgMemory.getInstance.getLstFeedStr().size() > 0){
                                        
                                        zToBeSendDataFeedLine = ITMFeedMsgMemory.getInstance.getLstFeedStr().get(iToBeSendSequenceNo - 1);
                                        
                                        if (bNeedToLog){
                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "MIS.iToBeSendSequenceNo value:" + iToBeSendSequenceNo + " > message:" + zToBeSendDataFeedLine);
                                        }
                                        if ((zToBeSendDataFeedLine != null) && (zToBeSendDataFeedLine.length() > 0)){
                                            chChannel.sendMessageDirect(zToBeSendDataFeedLine);
                                            //////chChannel.addSendQueue(zToBeSendDataFeedLine);
                                        }
                                        aiSequenceNo.set(iToBeSendSequenceNo);
                                        iToBeSendSequenceNo++;
                                    }

                                }else{
                                    break;
                                }
                            }
                        }
                        //.tunggu perintah berikutnya:
                        synchronized(this){
                            wait(1); //.istirahat dulu sebentar :)
                        }
                        iIdleIntervalCount++;
                        if (iIdleIntervalCount > (10 * 100)){ //. * 10ms
                            iIdleIntervalCount = 0;
                            //. kirim ping
                            chChannel.sendMessageDirect("IDXPING");
                        }
                        
                    }catch(InterruptedException ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "interrupt to output processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to output processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }
        
        bNeedAdjustSequence.set(true);
        
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "output processor worker thread finished.");
    }
    
}