/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.setup;

import com.itm.generic.engine.socket.setup.ITMSocketVarsConsts.SocketSetup;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author aripam
 */
public class ITMSocketCtrlServer {
    
    private String fConnectionName                              = "";
    
    private ServerSocket fServerSocket;
    
    private List<ITMSocketChannel> fClientChannelList;
    private List<ITMSocketListener> fClientListenersList;
    
    private InSvrAcceptorSocketBackgroundWorker acceptorBgWorker;
    
    
    public ITMSocketCtrlServer(String zConnectionName) {
        this.fConnectionName = zConnectionName;
        //.EXXX.
    }
    
    public String getConnectionName() {
        return fConnectionName;
    }
    
    public void setConnectionName(String fConnectionName) {
        try{
            this.fConnectionName = fConnectionName;
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    protected synchronized boolean addSocketEventHandler(ITMSocketListener eventHandler){
        boolean bOut = false;
        try{
            if (eventHandler != null){
                if (this.fClientListenersList == null) {
                    this.fClientListenersList = new ArrayList<>();
                }
                if (!this.fClientListenersList.contains(eventHandler)){ //.@only once per object.@
                    this.fClientListenersList.add(eventHandler);
                }
            }
            bOut = true;
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    protected synchronized boolean removeSocketEventHandler(ITMSocketListener eventHandler){
        boolean bOut = false;
        try{
            if (eventHandler != null){
                if (this.fClientListenersList != null) {
                    this.fClientListenersList.remove(eventHandler);
                }
            }
            bOut = true;
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public synchronized ServerSocket getServerSocket() {
        return this.fServerSocket;
    }
    
    public synchronized boolean isListen(){
        boolean bOut = false;
        try{
            if (this.fServerSocket != null){
                if (!this.fServerSocket.isClosed()){
                    if (this.fServerSocket.getInetAddress() != null){
                        if (this.fServerSocket.isBound()){
                            bOut = true;
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    protected synchronized boolean listen(int iInputListenerPort){
        boolean bOut = false;
        try{
            if ((iInputListenerPort >= SocketSetup.I_REF_PORT_MIN_NUMBER) && (iInputListenerPort <= SocketSetup.I_REF_PORT_MAX_NUMBER)){
                if (this.fServerSocket == null){
                    this.fServerSocket = new ServerSocket();
                }
                if (this.fServerSocket != null){
                    boolean bNeedNewListen = false; //.anggap belum butuh dulu.
                    if (isListen()){
                        //.kondisi: sudah listen, apakah port sama:
                        if (fServerSocket.getLocalPort() != iInputListenerPort){
                            //.port yg diminta beda, restart listener:
                            //.hentikan listener:
                            shutdown();
                            //.buat listener baru:
                            bNeedNewListen = true;
                        }else{
                            bOut = true;
                        }
                    }else{
                        //.kondisi: sudah pernah close atau masih baru:
                        //.hentikan listener:
                        shutdown();
                        //.buat listener baru:
                        bNeedNewListen = true;
                    }
                    if (bNeedNewListen) {
                        //.coba buat listener baru:
                        this.fServerSocket = new ServerSocket(iInputListenerPort);
                        if (this.fServerSocket != null){
                            bOut = true;
                            if (bOut == true){
                                //.menunggu koneksi baru di worker.
                                if (this.acceptorBgWorker == null){
                                    this.acceptorBgWorker = new InSvrAcceptorSocketBackgroundWorker();
                                }
                                this.acceptorBgWorker.RunWorker();
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0) {
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    protected synchronized boolean shutdown(){
        boolean bOut = false;
        try{
            //.beri sinyal worker berhenti:
            if (this.acceptorBgWorker != null){
                this.acceptorBgWorker.stopWorker();
            }
            //.hentikan listen:
            try{
                if (this.fServerSocket != null){
                    this.fServerSocket.close();
                }
            }catch(Exception ex1){
                //.EXXX.
                System.err.println(ex1);
            }
            //.tutup semua koneksi (akhir):
            removeAllChannels();
            //.selesai:
            bOut = true; //.selalu.
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    
    public synchronized void cleanupChannels() {
        try{
            if ((fClientChannelList != null) && (!fClientChannelList.isEmpty())){
                for (Iterator<ITMSocketChannel> iterClientCh = fClientChannelList.iterator(); iterClientCh.hasNext(); ){
                    ITMSocketChannel sch = (ITMSocketChannel)iterClientCh.next();
                    if (sch != null){
                        if (sch.isChannelAlreadyWasted()){
                            iterClientCh.remove();
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
    protected synchronized void removeAllChannels() {
        try{
            if ((fClientChannelList != null) && (!fClientChannelList.isEmpty())){
                for (Iterator<ITMSocketChannel> iterClientCh = fClientChannelList.iterator(); iterClientCh.hasNext(); ){
                    ITMSocketChannel sch = (ITMSocketChannel)iterClientCh.next();
                    if (sch != null){
                        try{
                            sch.StopChannel();
                        }catch(Exception ex1){
                            //.EXXX.
                            System.err.println(ex1);
                        }
                        try{
                            iterClientCh.remove();
                        }catch(Exception ex2){
                            //.EXXX.
                            System.err.println(ex2);
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
    
    
    
    //.worker:
    private class InSvrAcceptorSocketBackgroundWorker implements Runnable{
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
            //System.err.println("SVR_CTR worker START thid:" + Thread.currentThread().getId());
            while (this.bWrWorkerMustRun.get()){
                try{
                    //.tugas: menerima koneksi socket dari client:
                    if (isListen()){
                        //.tunggu sampai ada koneksi masuk:
                        //.listen koneksi baru:
                        Socket inSock = fServerSocket.accept();
                        try{
                            if (inSock != null){
                                //.buat channel baru:
                                ITMSocketChannel fClientChannel = new ITMSocketChannel(fConnectionName);
                                fClientChannel.setChannelID(ITMSocketCtrlGeneral.getInstance.generateServerChannelID()); //.unique.
                                //.set events:
                                fClientChannel.setSocketListeners(fClientListenersList);
                                //.daftarkan channel:
                                if (fClientChannelList == null) {
                                    fClientChannelList = new ArrayList<>();
                                }
                                fClientChannelList.add(fClientChannel);
                                //.set property:
                                fClientChannel.setSupplyAddress("");
                                fClientChannel.setSupplyPort(0);
                                //.jalankan channel:
                                fClientChannel.StartChannel(inSock);
                                //.selesai, tunggu koneksi berikutnya... .
                            }
                        }catch(Exception ex1){
                            //.EXXX.
                            System.err.println(ex1);
                        }
                        //.bersihkan channel:
                        cleanupChannels();
                    }
                    //.#istirahat dulu sebentar :)
                    synchronized(this){
                        wait(1);
                    }
                }catch(InterruptedException intex){
                    //.EXXX.
                    System.err.println(intex);
                    //.berhenti:
                    break;
                }catch(Exception ex0){
                    //.EXXX.
                    System.err.println(ex0);
                    //.berhenti:
                    break;
                }
            }
            //.penyelesaian:
            shutdown();
            //.info worker selesai:
            //.EXXX.
            //.#worker selesai:
            //.EXXX.
            //System.err.println("SVR_CTR worker END thid:" + Thread.currentThread().getId());
        }
        
    }
    
    
    
}
