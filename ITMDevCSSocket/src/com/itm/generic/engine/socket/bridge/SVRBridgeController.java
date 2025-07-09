/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.bridge;

import com.itm.generic.engine.socket.setup.ITMSocketCtrlServer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author aripam
 */
public class SVRBridgeController extends ITMSocketCtrlServer {
    
    public enum SVRMessageGroupType{
        SVR_MESSAGE_A,
        SVR_MESSAGE_B,
        SVR_MESSAGE_C,
        SVR_MESSAGE_D,
        SVR_MESSAGE_E
    }
    
    private SVRMessageGroupType msgGroupType;
    
    private SVRBridgeSocketHandler bridgeSockHandler;
    private InSVRBridgeSocketGuardWorker listenerGdWorker;
    
    private int iRefListenerPort                                = 0;
    private int iRefCheckInterval                               = 0;
    
    private AtomicBoolean bMustListen                           = new AtomicBoolean(false);
    
    public SVRBridgeController(String zConnectionName) {
        super(zConnectionName);
        this.bridgeSockHandler = new SVRBridgeSocketHandler(this);
        addSocketEventHandler(this.bridgeSockHandler);
        //.EXXX.
    }
    
    //.setup methods:
    public int getRefListenerPort() {
        return iRefListenerPort;
    }

    public void setRefListenerPort(int iRefListenerPort) {
        this.iRefListenerPort = iRefListenerPort;
    }

    public int getRefCheckInterval() {
        return iRefCheckInterval;
    }

    public void setRefCheckInterval(int iRefCheckInterval) {
        this.iRefCheckInterval = iRefCheckInterval;
    }
    
    public SVRMessageGroupType getMsgGroupType() {
        if (msgGroupType == null){
            msgGroupType = SVRMessageGroupType.SVR_MESSAGE_A;
        }
        return msgGroupType;
    }
    
    public void setMsgGroupType(SVRMessageGroupType msgGroupType) {
        this.msgGroupType = msgGroupType;
    }
    
    
    //.listener methods:
    public void addBridgeListener(SVRBridgeListener newListener){
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
    
    public void removeBridgeListener(SVRBridgeListener oldListener){
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
    public synchronized boolean verifyListener(){
        boolean bOut = false;
        try{
            if (this.bMustListen.get()){
                //.start:proses-inworker:
                if (listenerGdWorker == null){
                    listenerGdWorker = new InSVRBridgeSocketGuardWorker();
                }
                if (listenerGdWorker != null){
                    listenerGdWorker.RunWorker();
                }
            }else{
                //.stop:proses-inworker:
                if (listenerGdWorker != null){
                    listenerGdWorker.stopWorker();
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public synchronized boolean startListener(){
        boolean bOut = false;
        try{
            //.jalankan listener untuk pertama kali:
            listen(this.iRefListenerPort);
            //.beri tanda jaga terlebih dulu:
            this.bMustListen.set(true);
            //.proses-inworker:
            verifyListener();
            //.tunggu koneksi:
            //.cek apakah sudah listen:
            bOut = isListen();
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public synchronized boolean stopListener(){
        boolean bOut = false;
        try{
            //.beri tanda terlebih dulu:
            this.bMustListen.set(false);
            //.matikan listener:
            shutdown();
            //.proses-inworker:
            verifyListener();
            //.cek apakah sudah berhenti listen:
            bOut = (!isListen());
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    
    //.worker:
    private class InSVRBridgeSocketGuardWorker implements Runnable{
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
            //System.err.println("SVR worker START thid:" + Thread.currentThread().getId());
            while (this.bWrWorkerMustRun.get()){
                try{
                    //.tugas: menjaga koneksi bridge socket:
                    if (bMustListen.get()){
                        if (!isListen()){
                            listen(iRefListenerPort);
                        }
                    }else{
                        if (isListen()){
                            shutdown();
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
            //System.err.println("SVR worker END thid:" + Thread.currentThread().getId());
        }
        
    }
    
}
