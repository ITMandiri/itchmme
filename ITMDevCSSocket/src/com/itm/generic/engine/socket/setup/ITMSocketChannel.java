/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.setup;

import com.itm.generic.engine.socket.setup.ITMSocketVarsConsts.SocketSetup;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author aripam
 */
public class ITMSocketChannel {
    //.informasi channel:
    private String fConnectionName                              = ""; //.nama koneksi.
    private String fSupplyAddress                               = ""; //.yg disupply dari user.
    private int fSupplyPort                                     = 0; //.yg disupply dari user.
    private String fChannelID                                   = ""; //.tetap   //<1="AsServer"/2="AsClient">yyMMdd<0.000.000.000=counter[10]><00=random[2]>
    private Socket fSocket;
    private ConcurrentLinkedQueue<String> fQueue;
    //.informasi listener:
    private List<ITMSocketListener> fSocketListeners;
    //.informasi worker:
    private ChannelReaderWorker fReaderWorker;
    private ChannelWriterWorker fWriterWorker;
    //.informasi tanda akhir:
    private boolean fSocketWasWorking                           = false; //.socket pernah terpakai.
    private boolean fSocketAlreadyDisconnected                  = false; //.socket sudah pernah disconnect.
    private boolean fChannelAlreadyWasted                       = false; //.channel sudah tidak terpakai.
    //.custom line factory:
    private boolean fCustomLineFactory                          = false;
    private ITMSocketCustomeLineFactoryInterface fCustomLineFactoryWorker;
    
    private final Object lockWrite = new Object();
    
    public ITMSocketChannel(String zConnectionName) {
        this.fConnectionName = zConnectionName;
        //.EXXX.
    }
    
    public ITMSocketChannel(String zConnectionName, ITMSocketCustomeLineFactoryInterface fCustomLineFactoryWorker) {
        this.fConnectionName = zConnectionName;
        this.fCustomLineFactoryWorker = fCustomLineFactoryWorker;
        this.fCustomLineFactory = (fCustomLineFactoryWorker != null);
        //.EXXX.
    }
    
    //.channel methods:
    
    public String getConnectionName() {
        return fConnectionName;
    }

    public void setConnectionName(String fConnectionName) {
        this.fConnectionName = fConnectionName;
    }
    
    public boolean isfCustomLineFactory() {
        return fCustomLineFactory;
    }

    public ITMSocketCustomeLineFactoryInterface getfCustomLineFactoryWorker() {
        return fCustomLineFactoryWorker;
    }
    
    public String getSupplyAddress() {
        return fSupplyAddress;
    }

    public void setSupplyAddress(String fSupplyAddress) {
        this.fSupplyAddress = fSupplyAddress;
    }

    public int getSupplyPort() {
        return fSupplyPort;
    }

    public void setSupplyPort(int fSupplyPort) {
        this.fSupplyPort = fSupplyPort;
    }
    
    public String getChannelID() {
        return fChannelID;
    }

    public void setChannelID(String fChannelID) {
        this.fChannelID = fChannelID;
    }

    public Socket getSocket() {
        return fSocket;
    }

    public String getAddress() {
        String zOut = "";
        try{
            if (this.fSocket != null){
                zOut = this.fSocket.getInetAddress().getHostAddress();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return zOut;
    }
    
    public String getLocalAddress() {
        String zOut = "";
        try{
            if (this.fSocket != null){
                zOut = this.fSocket.getLocalAddress().getHostAddress();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return zOut;
    }
    
    public int getPort() {
        int iOut = 0;
        try{
            if (this.fSocket != null){
                iOut = this.fSocket.getPort();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return iOut;
    }
    
    public int getLocalPort() {
        int iOut = 0;
        try{
            if (this.fSocket != null){
                iOut = this.fSocket.getLocalPort();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return iOut;
    }
    
    public boolean isBound(){
        boolean bOut = false;
        try{
            if (this.fSocket != null){
                bOut = this.fSocket.isBound();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public boolean isClosed(){
        boolean bOut = false;
        try{
            if (this.fSocket != null){
                bOut = this.fSocket.isClosed();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public boolean isConnected(){
        boolean bOut = false;
        try{
            if (this.fSocket != null){
                bOut = this.fSocket.isConnected();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public boolean isInputShutdown(){
        boolean bOut = false;
        try{
            if (this.fSocket != null){
                bOut = this.fSocket.isInputShutdown();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public boolean isOutputShutdown(){
        boolean bOut = false;
        try{
            if (this.fSocket != null){
                bOut = this.fSocket.isOutputShutdown();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    public synchronized boolean sendMessageDirect(String zMessageLine){
        boolean bOut = false;
        try{
            if (!StringHelper.isNullOrEmpty(zMessageLine)){
                if (fSocket != null){
                    if (!fSocket.isClosed()){
                        OutputStream ost = fSocket.getOutputStream();
                        if (fCustomLineFactory){
                            ost.write(zMessageLine.concat(SocketSetup.NEW_LINE).getBytes());
                        }else{
                            ost.write(zMessageLine.concat(SocketSetup.NEW_LINE).getBytes());
                        }
                        //////////ost.flush();
                        raiseOnSent(zMessageLine);
                        bOut = true;
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
            raiseOnError(ex0);
        }
        return bOut;
    }
    
    public synchronized boolean sendMessageDirect_ori(String zMessageLine){
        boolean bOut = false;
        try{
            if (!StringHelper.isNullOrEmpty(zMessageLine)){
                if (fSocket != null){
                    if (!fSocket.isClosed()){
                        OutputStream ost = fSocket.getOutputStream();
                        if (fCustomLineFactory){
                            ost.write(zMessageLine.concat(SocketSetup.NEW_LINE).getBytes());
                        }else{
                            ost.write(zMessageLine.concat(SocketSetup.NEW_LINE).getBytes());
                        }
                        ost.flush();
                        raiseOnSent(zMessageLine);
                        bOut = true;
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
            raiseOnError(ex0);
        }
        return bOut;
    }
    
    public boolean addSendQueue(String zMessageLine){
        boolean bOut = false;
        try{
            if (!StringHelper.isNullOrEmpty(zMessageLine)){
                if (this.fQueue == null) {
                    this.fQueue = new ConcurrentLinkedQueue<>();
                }
                synchronized(lockWrite){
                    this.fQueue.add(zMessageLine);
                }
                //.beri tanda worker:
                if (fWriterWorker != null){
                    fWriterWorker.RunWorker();
                }
                bOut = true;
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
            raiseOnError(ex0);
        }
        return bOut;
    }

    public ConcurrentLinkedQueue<String> getfQueue() {
        return fQueue;
    }

    public void setfQueue(ConcurrentLinkedQueue<String> fQueue) {
        this.fQueue = fQueue;
    }

    //.listener methods:
    public List<ITMSocketListener> getSocketListeners() {
        return fSocketListeners;
    }
    
    public void setSocketListeners(List<ITMSocketListener> fSocketListeners)  {
        this.fSocketListeners = fSocketListeners;
    }
    
    public void addSocketListener(ITMSocketListener newListener){
        try{
            if (newListener == null){
                return;
            }
            if (this.fSocketListeners == null){
                this.fSocketListeners = new ArrayList<>();
            }
            if (!this.fSocketListeners.contains(newListener)){ //.@only once per object.@
                this.fSocketListeners.add(newListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    public void removeSocketListener(ITMSocketListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            this.fSocketListeners.remove(oldListener);
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnConnected(){
        try{
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSocketListener lstr = (ITMSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onConnected(this);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnDisconnected(){
        try{
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSocketListener lstr = (ITMSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onDisconnected(this);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnMessage(String messageLine){
        try{
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSocketListener lstr = (ITMSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onMessage(this, messageLine);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    private void raiseOnSent(String messageLine){
        try{
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSocketListener lstr = (ITMSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onSent(this, messageLine);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    public void raiseOnError(Exception exception){
        try{
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSocketListener lstr = (ITMSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onError(this, exception);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    //.flags method:
    public boolean isChannelAlreadyWasted() {
        return fChannelAlreadyWasted;
    }
    
    public boolean isSocketWasWorking() {
        return fSocketWasWorking;
    }
    
    
    //.worker methods:
    public synchronized boolean StartChannel(Socket inputSocket){
        boolean bOut = false;
        try{
            if (!fChannelAlreadyWasted){
                if (inputSocket != null) {
                    fSocketWasWorking = true; //.seb=after set socket.
                    fSocket = inputSocket;
                    raiseOnConnected(); //.rev:20140506. set fSocket sebelumnya di bawah raiseOnConnected();
                    if (fReaderWorker == null) {
                        fReaderWorker = new ChannelReaderWorker();
                    }
                    if (fWriterWorker == null) {
                        fWriterWorker = new ChannelWriterWorker();
                    }
                    if (fReaderWorker != null) {
                        fReaderWorker.RunWorker();
                    }
                    if (fWriterWorker != null) {
                        fWriterWorker.RunWorker();
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
            raiseOnError(ex0);
        }
        return bOut;
    }
    
    public synchronized void StopChannel(){
        try{
            try{
                if ((fSocket != null) && (!fSocket.isClosed())){
                    fSocket.close();
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
                raiseOnError(ex0);
            }
            try{
                if (fReaderWorker != null) {
                    fReaderWorker.stopWorker();
                }
                if (fWriterWorker != null) {
                    fWriterWorker.stopWorker();
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
                raiseOnError(ex0);
            }
            fChannelAlreadyWasted = true;
            if (!fSocketAlreadyDisconnected){
                raiseOnDisconnected();
                fSocketAlreadyDisconnected = true;
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
            raiseOnError(ex0);
        }
    }
    
    
    
    //.worker inner class:
    private class ChannelReaderWorker implements Runnable{
        private Thread thRdWorker;
        private AtomicBoolean bRdWorkerMustRun                  = new AtomicBoolean(false);
        private BufferedReader mNewLineBuffReader               = null;
        private DataInputStream mCustomBuffReader               = null;
        
        public synchronized void resumeWorker(){
            try{
                if (this.thRdWorker != null){
                    if (this.thRdWorker.isAlive()){
                        try{
                            notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                System.err.println(ex0);
                //.EXXX.
            }
        }
        
        public synchronized void stopWorker(){
            try{
                if (this.thRdWorker != null){
                    if (this.thRdWorker.isAlive()){
                        try{
                            this.bRdWorkerMustRun.set(false);
                            notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            System.err.println(ex0);
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
                raiseOnError(ex0);
            }
        }
        
        public synchronized void RunWorker(){
            try{
                if (this.thRdWorker == null){
                    this.thRdWorker = new Thread(this);
                }
                if (this.thRdWorker != null){
                    if (!this.thRdWorker.isAlive()){
                        try{
                            //.persiapkan bufferedreader di awal:
                            try{
                                if (isfCustomLineFactory()){
                                    mCustomBuffReader = new DataInputStream(fSocket.getInputStream());
                                }else{
                                    mNewLineBuffReader = new BufferedReader(new InputStreamReader(fSocket.getInputStream()));
                                }
                            }catch(IOException ex0){
                                //.EXXX.
                                System.err.println(ex0);
                                raiseOnError(ex0);
                            }catch(Exception ex0){
                                //.EXXX.
                                System.err.println(ex0);
                                raiseOnError(ex0);
                            }
                            this.thRdWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                            this.bRdWorkerMustRun.set(true);
                            this.thRdWorker.setPriority(Thread.NORM_PRIORITY);
                            this.thRdWorker.setName(fConnectionName + "-R");
                            this.thRdWorker.start();
                        }catch(Exception ex0){
                            //.EXXX.
                            System.err.println(ex0);
                            raiseOnError(ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            System.err.println(ex0);
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
                raiseOnError(ex0);
            }
        }
        
        @Override
        public void run() {
            BufferedReader mNewLineReader = null;
            DataInputStream mCustomReader = null;
            if (isfCustomLineFactory()){
                mCustomReader = mCustomBuffReader;
            }else{
                mNewLineReader = mNewLineBuffReader;
            }
            while (this.bRdWorkerMustRun.get()){
                if (fSocket != null){
                    if (!fSocket.isClosed()){
                        try{
                            //.proses [read] here:
                            if (isfCustomLineFactory()){
                                if (getfCustomLineFactoryWorker() != null){
                                    if (mCustomReader != null){
                                        byte[] btBytesBuffer = new byte[1024 * 1024];
                                        int cRead = mCustomReader.read(btBytesBuffer);
                                        if (cRead > 0){
                                            byte[] btBytesResult = Arrays.copyOf(btBytesBuffer, cRead);
                                            if (btBytesResult.length > 0){
                                                String[] arrMessages = getfCustomLineFactoryWorker().onCustomDataReceived(new String(btBytesResult));
                                                if ((arrMessages != null) && (arrMessages.length > 0)){
                                                    for(String zEachMessage : arrMessages){
                                                        raiseOnMessage(zEachMessage);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }else{
                                if (mNewLineReader != null) {
                                    String zReadLine = mNewLineReader.readLine();
                                    if (zReadLine == null){
                                        //.ada baca kosong tidak benar:
                                        break;
                                    }
                                    if (zReadLine.length() > 0){
                                        raiseOnMessage(zReadLine);
                                    }
                                }
                            }
                        }catch(IOException ex0){
                            //.EXXX.
                            System.err.println(ex0);
                            raiseOnError(ex0);
                            //.berhenti:
                            break;
                        }catch(Exception ex0){
                            //.EXXX.
                            System.err.println(ex0);
                            raiseOnError(ex0);
                            //.berhenti:
                            break;
                        }
                    }
                }
            }
            //.info worker selesai:
            if (isfCustomLineFactory()){
                if (getfCustomLineFactoryWorker() != null){
                    getfCustomLineFactoryWorker().onCustomDataReset();
                }
            }
            //.EXXX.
            //.#worker selesai:
            //.EXXX.
            StopChannel();
        }
        
    }
    
    private class ChannelWriterWorker implements Runnable{
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
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                
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
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
                raiseOnError(ex0);
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
                            this.thWrWorker.setName(fConnectionName + "-W");
                            this.thWrWorker.start();
                        }catch(Exception ex0){
                            //.EXXX.
                            System.err.println(ex0);
                            raiseOnError(ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            System.err.println(ex0);
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                System.err.println(ex0);
                raiseOnError(ex0);
            }
        }
        
        @Override
        public void run() {
            String strLine;
            while (this.bWrWorkerMustRun.get()){
                if (fSocket != null){
                    if (!fSocket.isClosed()){
                        try{
                            //.proses [write] here:
                            if (fQueue != null){
                                while (fQueue.size() > 0){
//                                    if (!this.bWrWorkerMustRun.get()){
//                                        break;
//                                    }
//                                    String zGetLine = fQueue.peek();
//                                    String zGetRef = "";
//                                    if (StringHelper.isNullOrEmpty(zGetLine)){
//                                        zGetRef = fQueue.poll();
//                                    }else{
//                                        if (sendMessageDirect(zGetLine)){
//                                            zGetRef = fQueue.poll();
//                                        }
//                                    }
                                    Object[] arrData = null;
                                    synchronized(lockWrite){
                                        arrData = fQueue.toArray();
                                        fQueue.clear();

                                    }

                                    if (arrData == null){
//                                        if (bLog) System.out.println("synchronized(queueData) failed --> arrData == null");
                                        //. keluar
                                        break;
                                    }

                                    for(Object oData : arrData){
                                        strLine = (String)oData;
                                        try{
                                            sendMessageDirect(strLine);

                                        }catch(Exception ex){
//                                            ItmLogger.Instance.writeLogAsQueue("[" + sockID +  "] SockClientWriterWorker run.writeToStream exception = " + StringUtil.extractStackTrace(ex.getStackTrace())  + ", tid [" + Thread.currentThread().getId() + "]", ItmLogger.IMLogType.WARNING, SockClientWriterWorker.class.getName());
//                                            this.sockClientHandler.stopHandler();
                                            break;
                                        }


                                    }
                                }
                            }
                            //.#istirahat dulu sebentar :)
                            synchronized(this){
                                wait(1);
                            }
                        }catch(InterruptedException intex){
                            //.EXXX.
                            System.err.println(intex);
                            raiseOnError(intex);
                            //.berhenti:
                            break;
                        }catch(Exception ex0){
                            //.EXXX.
                            raiseOnError(ex0);
                            //.berhenti:
                            break;
                        }
                    }else{
                        //.socket sudah berhenti:
                        break;
                    }
                }else{
                    //.socket kosong:
                    break;
                }
            }
            //.info worker selesai:
            //.EXXX.
            //.#worker selesai:
            //.EXXX.
            StopChannel();
        }
        
    }
    
    
}
