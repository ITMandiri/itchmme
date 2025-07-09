/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketCtl;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPITCHPacketController extends ITMSoupBinTCPBridgeSocketCtl {
    
    public enum ITCHBridgeStatus{ //.connecting, connected, disconnecting, disconnected.
        SCK_DISCONNECTED        (0, "Disconnected"),
        SCK_DISCONNECTING       (1, "Disconnecting"),
        SCK_CONNECTING          (2, "Connecting"),
        SCK_CONNECTED           (3, "Connected");
        
        public final int order;
        public final String cname;

        private ITCHBridgeStatus(int order, String cname) {
            this.order = order;
            this.cname = cname;
        }

    }
    
    private ITCHBridgeStatus stsBridgeStatus                     = ITCHBridgeStatus.SCK_DISCONNECTED;
    
    public ITCHBridgeStatus getStsBridgeStatus() {
        if (this.stsBridgeStatus == null){
            this.stsBridgeStatus = ITCHBridgeStatus.SCK_DISCONNECTED;
        }
        return this.stsBridgeStatus;
    }

    public void setStsBridgeStatus(ITCHBridgeStatus stsBridgeStatus) {
        this.stsBridgeStatus = stsBridgeStatus;
    }
    
    private final AtomicBoolean bMustConnected                      = new AtomicBoolean(false);
    
    private final ITMSoupBinTCPITCHPacketListener evHandler;
    private ITCHInBridgeSocketGuardWorker recvGdWorker;
    private int iRefCheckInterval                                   = 0;
    
    private String zDescription;
    private int iOrderNo;
    
    private String zIPAddress;
    private int iPort;
    private int iTimeOut;
    
    public ITMSoupBinTCPITCHPacketController(
            String zConnectionName
            , String zDescription
            , int iOrderNo
            , String zIPAddress
            , int iPort
            , int iTimeOut
            , boolean bHeartBeat
            , boolean bCmpMsgExact
            , int iReconSeqMode
            , boolean bAutoSelect
            , int iSocketSendBufferSize
            , int iSocketRecvBufferSize
            , boolean bSocketSendAutoFlush
    ) {
        super(zConnectionName);
        this.evHandler = new ITMSoupBinTCPITCHPacketListener();
        this.zDescription = zDescription;
        this.iOrderNo = iOrderNo;
        this.zIPAddress = zIPAddress;
        this.iPort = iPort;
        this.iTimeOut = iTimeOut;
        this.iRefCheckInterval = iTimeOut;
        setHeartBeatEnabled(bHeartBeat);
        setCmpMsgExact(bCmpMsgExact);
        setReconSeqMode(iReconSeqMode);
        setAutoSelect(bAutoSelect);
        setSocketSendBufferSize(iSocketSendBufferSize);
        setSocketReceiveBufferSize(iSocketRecvBufferSize);
        setSocketSendAutoFlush(bSocketSendAutoFlush);
        addSocketEventHandler(this.evHandler);
    }
    
    public void addExtraEventHandler(ITMSoupBinTCPBridgeSocketListener eventHandler){
        addSocketEventHandler(eventHandler);
    }
    
    public String getDescription() {
        return zDescription;
    }

    public void setDescription(String zDescription) {
        this.zDescription = zDescription;
    }
    
    public int getOrderNo() {
        return iOrderNo;
    }

    public void setOrderNo(int iOrderNo) {
        this.iOrderNo = iOrderNo;
    }
    
    public String getIPAddress() {
        return zIPAddress;
    }

    public void setIPAddress(String zIPAddress) {
        this.zIPAddress = zIPAddress;
    }

    public int getPort() {
        return iPort;
    }

    public void setPort(int iPort) {
        this.iPort = iPort;
    }

    public int getTimeOut() {
        return iTimeOut;
    }

    public void setTimeOut(int iTimeOut) {
        this.iTimeOut = iTimeOut;
    }
    
    public int getRefCheckInterval() {
        return this.iRefCheckInterval;
    }

    public void setRefCheckInterval(int iRefCheckInterval) {
        this.iRefCheckInterval = iRefCheckInterval;
    }

    //.connection methods:
    public synchronized boolean verifyConnection(){
        boolean bOut = false;
        try{
            if (this.bMustConnected.get()){
                //.start:proses-inworker:
                if (recvGdWorker == null){
                    recvGdWorker = new ITCHInBridgeSocketGuardWorker();
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public void doStartConnection(){
        try{
            //.beri tanda terlebih dulu:
            this.bMustConnected.set(true);
            //.jalankan koneksi untuk pertama kali:
            if (!isConnected()){
                //.set (awal) status:
                stsBridgeStatus = ITCHBridgeStatus.SCK_CONNECTING;
                //.coba koneksi:
                connect(getIPAddress(), getPort(), getTimeOut());
            }
            //.proses-inworker:
            verifyConnection();
            //.tunggu koneksi:
            //.cek apakah sudah koneksi:
            boolean bOut = isConnected();
            //.set (ulang) status:
            if (bOut){
                stsBridgeStatus = ITCHBridgeStatus.SCK_CONNECTED;
            }else{
                stsBridgeStatus = ITCHBridgeStatus.SCK_DISCONNECTED;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
    }
    
    public void doStopConnection(){
        try{
            if (isConnected()){
                //.set (awal) status:
                stsBridgeStatus = ITCHBridgeStatus.SCK_DISCONNECTING;
            }
            //.beri tanda terlebih dulu:
            this.bMustConnected.set(false);
            //.matikan koneksi:
            disconnect();
            //.proses-inworker:
            verifyConnection();
            //.cek apakah sudah berhenti koneksi:
            boolean bOut = (!isConnected());
            if (bOut){
                stsBridgeStatus = ITCHBridgeStatus.SCK_DISCONNECTED;
            }else{
                stsBridgeStatus = ITCHBridgeStatus.SCK_CONNECTED;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
    }
    
    
    //.worker:
    private class ITCHInBridgeSocketGuardWorker implements Runnable{
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
            }
        }
        
        @Override
        public void run() {
            while (this.bWrWorkerMustRun.get()){
                try{
                    //.tugas: menjaga koneksi bridge socket:
                    if (bMustConnected.get()){
                        if (!isConnected()){
                            //.set (awal) status:
                            stsBridgeStatus = ITCHBridgeStatus.SCK_CONNECTING;
                            //.coba koneksi:
                            connect(getIPAddress(), getPort(), getTimeOut());
                            //.cek status:
                            stsBridgeStatus = (isConnected() ? ITCHBridgeStatus.SCK_CONNECTED : ITCHBridgeStatus.SCK_DISCONNECTED );
                        }
                    }else{
                        if (isConnected()){
                            //.set (awal) status:
                            stsBridgeStatus = ITCHBridgeStatus.SCK_DISCONNECTING;
                            //.matikan koneksi:
                            disconnect();
                            //.cek status:
                            stsBridgeStatus = (isConnected() ? ITCHBridgeStatus.SCK_CONNECTED : ITCHBridgeStatus.SCK_DISCONNECTED );
                        }
                    }
                    //.#istirahat dulu sebentar :)
                    synchronized(this){
                        if (iRefCheckInterval > 0){
                            wait(iRefCheckInterval);
                        }else{
                            wait(1000);
                        }
                    }
                }catch(InterruptedException intex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, intex0);
                    break;
                }catch(Exception ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
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
