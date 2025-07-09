/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.bridge;

import com.itm.generic.engine.socket.setup.ITMSocketCtrlClient;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author aripam
 */
public class USRBridgeController extends ITMSocketCtrlClient {
    
    public enum USRMessageGroupType{
        USR_MESSAGE_A,
        USR_MESSAGE_B,
        USR_MESSAGE_C,
        USR_MESSAGE_D,
        USR_MESSAGE_E
    }
    
    private USRMessageGroupType msgGroupType;
    
    private USRBridgeSocketHandler bridgeSockHandler;
    private InUSRBridgeSocketGuardWorker recvGdWorker;
    
    private String zRefServerAddress                            = "";
    private int iRefServerPort                                  = 0;
    private int iRefTryConnectTimeOut                           = 0;
    private int iRefCheckInterval                               = 0;
    
    private AtomicBoolean bMustConnected                        = new AtomicBoolean(false);
    
    public USRBridgeController(String zConnectionName) {
        super(zConnectionName);
        this.bridgeSockHandler = new USRBridgeSocketHandler(this);
        addSocketEventHandler(this.bridgeSockHandler);
        //.EXXX.
    }
    
    //.setup methods:
    public String getRefServerAddress() {
        return zRefServerAddress;
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
        return iRefTryConnectTimeOut;
    }

    public void setRefTryConnectTimeOut(int iRefTryConnectTimeOut) {
        this.iRefTryConnectTimeOut = iRefTryConnectTimeOut;
    }

    public int getRefCheckInterval() {
        return iRefCheckInterval;
    }

    public void setRefCheckInterval(int iRefCheckInterval) {
        this.iRefCheckInterval = iRefCheckInterval;
    }
    
    public USRMessageGroupType getMsgGroupType() {
        if (msgGroupType == null){
            msgGroupType = USRMessageGroupType.USR_MESSAGE_A;
        }
        return msgGroupType;
    }

    public void setMsgGroupType(USRMessageGroupType msgGroupType) {
        this.msgGroupType = msgGroupType;
    }
    
    
    //.listener methods:
    public void addBridgeListener(USRBridgeListener newListener){
        try{
            if (newListener == null){
                return;
            }
            bridgeSockHandler.addBridgeListener(newListener);
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    public void removeBridgeListener(USRBridgeListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            bridgeSockHandler.removeBridgeListener(oldListener);
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    
    //.connection methods:
    public synchronized boolean verifyConnection(){
        boolean bOut = false;
        try{
            if (this.bMustConnected.get()){
                //.start:proses-inworker:
                if (recvGdWorker == null){
                    recvGdWorker = new InUSRBridgeSocketGuardWorker();
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
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
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
                connect(this.zRefServerAddress, this.iRefServerPort, this.iRefTryConnectTimeOut);
            }
            //.proses-inworker:
            verifyConnection();
            //.tunggu koneksi:
            //.cek apakah sudah koneksi:
            bOut = isConnected();
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public synchronized boolean stopConnection(){
        boolean bOut = false;
        try{
            //.beri tanda terlebih dulu:
            this.bMustConnected.set(false);
            //.matikan koneksi:
            disconnect();
            //.proses-inworker:
            verifyConnection();
            //.cek apakah sudah berhenti koneksi:
            bOut = (!isConnected());
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
        
    
    //.send-message methods:
    public synchronized boolean sendMessageDirect(String zMessage){
        boolean bOut = false;
        try{
            bOut = getChannel().sendMessageDirect(zMessage);
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public synchronized boolean sendToMessageQueue(String zMessage){
        boolean bOut = false;
        try{
            bOut = getChannel().addSendQueue(zMessage);
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    
    //.worker:
    private class InUSRBridgeSocketGuardWorker implements Runnable{
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
                            System.err.println(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
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
                            System.err.println(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
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
                            System.err.println(ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            System.err.println(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
            }
        }
        
        @Override
        public void run() {
            while (this.bWrWorkerMustRun.get()){
                try{
                    //.tugas: menjaga koneksi bridge socket:
                    if (bMustConnected.get()){
                        if (!isConnected()){
                            connect(zRefServerAddress, iRefServerPort, iRefTryConnectTimeOut);
                        }
                    }else{
                        if (isConnected()){
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
                }catch(InterruptedException intex){
                    //.EXXX.
                    System.err.println(intex);
                    break;
                }catch(Exception ex0){
                    //.EXXX.
                    System.err.println(ex0);
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
