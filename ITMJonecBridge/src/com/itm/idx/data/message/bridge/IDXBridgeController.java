/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.message.bridge;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketCtrlClient;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author aripam
 */
public class IDXBridgeController extends ITMSocketCtrlClient {
    
    public enum IDXGroupMessageType{
        ORI_MESSAGE,
        QRI_MESSAGE,
        ORM_MESSAGE //. hirin: 20220823 - ORM (TM) : hanya untuk transaksi regular (non ng)
    }
    
    public enum IDXBridgeStatus{ //.connecting, connected, disconnecting, disconnected.
        SCK_DISCONNECTED        (0, "Disconnected"),
        SCK_DISCONNECTING       (1, "Disconnecting"),
        SCK_CONNECTING          (2, "Connecting"),
        SCK_CONNECTED           (3, "Connected");
        
        public final int order;
        public final String cname;

        private IDXBridgeStatus(int order, String cname) {
            this.order = order;
            this.cname = cname;
        }

    }
    
    private IDXBridgeSocketHandler bridgeSockHandler;
    private InBridgeSocketGuardWorker recvGdWorker;
    
    private String zRefServerAddress                            = "";
    private int iRefServerPort                                  = 0;
    private int iRefTryConnectTimeOut                           = 0; //.milidetik | milliseconds.
    private int iRefCheckInterval                               = 0;
    
    private IDXGroupMessageType msgGroupType                    = IDXGroupMessageType.ORI_MESSAGE;
    private IDXBridgeStatus stsBridgeStatus                     = IDXBridgeStatus.SCK_DISCONNECTED;
    
    private AtomicBoolean bMustConnected                        = new AtomicBoolean(false);
    
    //.tambahan:
    private boolean bIsAdminLoggedOn                            = false;
    private boolean bIsAdminPasswordExpired                     = false;
    private boolean bIsAdminPasswordNearExpire                  = false;
    private String zLastMessage                                 = "";
    
    public IDXBridgeController(String zConnectionName) {
        super(zConnectionName);
        this.bridgeSockHandler = new IDXBridgeSocketHandler(this);
        addSocketEventHandler(this.bridgeSockHandler);
        //.EXXX.
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.INIT, "");
    }
    
    public String getRefServerAddress() {
        return this.zRefServerAddress;
    }

    public void setRefServerAddress(String zRefServerAddress) {
        this.zRefServerAddress = zRefServerAddress;
    }

    public int getRefServerPort() {
        return iRefServerPort;
    }

    public void setRefServerPort(int iRefServerPort) {
        this.iRefServerPort = iRefServerPort;
    }

    public int getRefTryConnectTimeOut() {
        return this.iRefTryConnectTimeOut;
    }

    public void setRefTryConnectTimeOut(int iRefTryConnectTimeOut) {
        this.iRefTryConnectTimeOut = iRefTryConnectTimeOut;
    }

    public int getRefCheckInterval() {
        return this.iRefCheckInterval;
    }

    public void setRefCheckInterval(int iRefCheckInterval) {
        this.iRefCheckInterval = iRefCheckInterval;
    }

    public IDXGroupMessageType getMsgGroupType() {
        if (this.msgGroupType == null){
            this.msgGroupType = IDXGroupMessageType.ORI_MESSAGE;
        }
        return this.msgGroupType;
    }

    public void setMsgGroupType(IDXGroupMessageType msgGroupType) {
        this.msgGroupType = msgGroupType;
    }
    
    public IDXBridgeStatus getStsBridgeStatus() {
        if (this.stsBridgeStatus == null){
            this.stsBridgeStatus = IDXBridgeStatus.SCK_DISCONNECTED;
        }
        return this.stsBridgeStatus;
    }

    public void setStsBridgeStatus(IDXBridgeStatus stsBridgeStatus) {
        this.stsBridgeStatus = stsBridgeStatus;
    }
    
    //.tambahan:
    
    public boolean isAdminLoggedOn() {
        return this.bIsAdminLoggedOn;
    }
    
    public void setIsAdminLoggedOn(boolean bIsAdminLoggedOn) {
        this.bIsAdminLoggedOn = bIsAdminLoggedOn;
    }
    
    public boolean isAdminPasswordExpired() {
        return this.bIsAdminPasswordExpired;
    }

    public void setIsAdminPasswordExpired(boolean bIsAdminPasswordExpired) {
        this.bIsAdminPasswordExpired = bIsAdminPasswordExpired;
    }

    public boolean isAdminPasswordNearExpire() {
        return this.bIsAdminPasswordNearExpire;
    }

    public void setIsAdminPasswordNearExpire(boolean bIsAdminPasswordNearExpire) {
        this.bIsAdminPasswordNearExpire = bIsAdminPasswordNearExpire;
    }

    public String getLastMessage() {
        return this.zLastMessage;
    }

    public void setLastMessage(String zLastMessage) {
        this.zLastMessage = zLastMessage;
    }
    
    
    //.listener methods:
    public void addBridgeListener(IDXBridgeListener newListener){
        try{
            if (newListener == null){
                return;
            }
            bridgeSockHandler.addBridgeListener(newListener);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
    }
    
    public void removeBridgeListener(IDXBridgeListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            bridgeSockHandler.removeBridgeListener(oldListener);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
    }
    
    
    //.connection methods:
    public synchronized boolean verifyConnection(){
        boolean bOut = false;
        try{
            if (this.bMustConnected.get()){
                //.start:proses-inworker:
                if (recvGdWorker == null){
                    recvGdWorker = new InBridgeSocketGuardWorker();
                }
                if (recvGdWorker != null){
                    recvGdWorker.RunWorker();
                }
            }else{
                //.stop:proses-inworker:
                if (recvGdWorker != null){
                    recvGdWorker.stopWorker();
                }
            }
            bOut = true;
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public synchronized boolean startConnection(){
        boolean bOut = false;
        try{
            //.beri tanda terlebih dulu:
            this.bMustConnected.set(true);
            //.jalankan koneksi untuk pertama kali:
            if (!isConnected()){
                //.set (awal) status:
                stsBridgeStatus = IDXBridgeStatus.SCK_CONNECTING;
                //.coba koneksi:
                connect(this.zRefServerAddress, this.iRefServerPort, this.iRefTryConnectTimeOut);
            }
            //.proses-inworker:
            verifyConnection();
            //.tunggu koneksi:
            //.cek apakah sudah koneksi:
            bOut = isConnected();
            //.set (ulang) status:
            if (bOut){
                stsBridgeStatus = IDXBridgeStatus.SCK_CONNECTED;
            }else{
                stsBridgeStatus = IDXBridgeStatus.SCK_DISCONNECTED;
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public synchronized boolean stopConnection(){
        boolean bOut = false;
        try{
            if (isConnected()){
                //.set (awal) status:
                stsBridgeStatus = IDXBridgeStatus.SCK_DISCONNECTING;
            }
            //.beri tanda terlebih dulu:
            this.bMustConnected.set(false);
            //.matikan koneksi:
            disconnect();
            //.proses-inworker:
            verifyConnection();
            //.cek apakah sudah berhenti koneksi:
            bOut = (!isConnected());
            if (bOut){
                stsBridgeStatus = IDXBridgeStatus.SCK_DISCONNECTED;
            }else{
                stsBridgeStatus = IDXBridgeStatus.SCK_CONNECTED;
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    
    
    public synchronized boolean sendMessageDirect(String zMessage){
        boolean bOut = false;
        try{
            bOut = getChannel().sendMessageDirect(zMessage);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public synchronized boolean sendToMessageQueue(String zMessage){
        boolean bOut = false;
        try{
            bOut = getChannel().addSendQueue(zMessage);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    
    //.worker:
    private class InBridgeSocketGuardWorker implements Runnable{
        private Thread thWrWorker;
        private AtomicBoolean bWrWorkerMustRun                  = new AtomicBoolean(false);
        
        public synchronized void resumeWorker(){
            try{
                if (this.thWrWorker != null){
                    if (this.thWrWorker.isAlive()){
                        try{
                            notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
            }
        }
        
        public synchronized void stopWorker(){
            try{
                if (this.thWrWorker != null){
                    if (this.thWrWorker.isAlive()){
                        try{
                            this.bWrWorkerMustRun.set(false);
                            notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
            }
        }
        
        public synchronized void RunWorker(){
            try{
                if (this.thWrWorker == null){
                    this.thWrWorker = new Thread(this);
                }
                if (this.thWrWorker != null){
                    if (!this.thWrWorker.isAlive()){
                        try{
                            this.thWrWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                            this.bWrWorkerMustRun.set(true);
                            this.thWrWorker.setPriority(Thread.NORM_PRIORITY);
                            this.thWrWorker.start();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
            }
        }
        
        @Override
        public void run() {
            while (this.bWrWorkerMustRun.get()){
                try{
                    //.tugas: menjaga koneksi bridge socket:
                    if (bMustConnected.get()){
                        if (!isConnected()){
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.WARNING, "Try reconnect server, bIsAdminPasswordExpired = " + bIsAdminPasswordExpired + ", zRefServerAddress = " + zRefServerAddress + ":" + iRefServerPort + ",iRefTryConnectTimeOut = " + iRefTryConnectTimeOut + ", stsBridgeStatus = " + stsBridgeStatus.cname);
                            //.cek password (hanya connect ulang jika password belum expired):
                            if (!bIsAdminPasswordExpired){
                                //.set (awal) status:
                                stsBridgeStatus = IDXBridgeStatus.SCK_CONNECTING;
                                //.coba koneksi:
                                connect(zRefServerAddress, iRefServerPort, iRefTryConnectTimeOut);
                            }
                        }
                    }else{
                        if (isConnected()){
                            //.set (awal) status:
                            stsBridgeStatus = IDXBridgeStatus.SCK_DISCONNECTING;
                            //.matikan koneksi:
                            disconnect();
                        }
                    }
                    //.#istirahat dulu sebentar :)
                    synchronized(this){
                        if (iRefCheckInterval > 0){
                            wait(iRefCheckInterval);
                        }else{
                            wait(1);
                        }
                    }
                }catch(InterruptedException intex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, intex0);
                    break;
                }catch(Exception ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.EXTENSION, logLevel.ERROR, ex0);
                    //.berhenti:
                    break;
                }
            }
            //.info worker selesai:
            //.EXXX.
            //.#worker selesai:
            //.EXXX.
        }
        
    }
    
}
