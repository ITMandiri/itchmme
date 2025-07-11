/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author fredy
 */
public class MARTINSimCallbackProcessor implements Runnable {
    
    private ITMSocketChannel chChannel;
    private final AtomicBoolean bAlreadyLoggedIn                        = new AtomicBoolean(false);
    private String zConnName                                            = "";
    private String zUserCode                                            = "";

    private Thread thOutWorker;
    private final AtomicBoolean bOutWorkerMustRun                       = new AtomicBoolean(false);
    
    public MARTINSimCallbackProcessor(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
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
    
    public String getConnName() {
        return zConnName;
    }

    public void setConnName(String zConnName) {
        this.zConnName = zConnName;
    }

    public String getUserCode() {
        return zUserCode;
    }

    public void setUserCode(String zUserCode) {
        this.zUserCode = zUserCode;
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
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "cannot start output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }else{
                    try{
                        wakeUpProcessor();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "cannot notify-resume output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "start processor exception:" + ex0);
        }
    }
    
    public synchronized void wakeUpProcessor(){
        try{
            if (this.thOutWorker != null){
                if (this.thOutWorker.isAlive()){
                    try{
                        notify();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "cannot notify-resume output callback processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "wakeup processor exception:" + ex0);
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
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "cannot notify-close output processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "stop processor exception:" + ex0);
        }
    }
    
    @Override
    public void run() {
        //////throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        while(this.bOutWorkerMustRun.get()){
            if (chChannel != null){
                //////if (chChannel.isConnected() && (!chChannel.isClosed()) && (getAlreadyLoggedIn())){
                if (chChannel.isConnected() && (!chChannel.isClosed())){
                    try{
                        if (getAlreadyLoggedIn()){
                            
                            
                            
                        }
                        //.tunggu perintah berikutnya:
                        synchronized(this){
                            wait(10); //.istirahat dulu sebentar :)
                        }
                    }catch(InterruptedException ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "interrupt to output processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "exception to output processor worker thread for:" + chChannel.getChannelID() + " > err:" + ex0);
                    }
                }
            }
        }
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INFO, "output processor worker thread finished.");
    }
    
}
