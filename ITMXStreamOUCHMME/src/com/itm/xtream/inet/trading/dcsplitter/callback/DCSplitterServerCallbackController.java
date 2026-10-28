/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dcsplitter.callback;

import com.itm.fix5.data.jonec.message.processor.FIX5JonecMessageProcessor;
import com.itm.fix5.data.jonec.message.struct.FIX5IDXMessage;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogonReply;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogoutReply;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import com.itm.xtream.inet.trading.sync.connection.ITMTradingServerSyncConnectionMgr;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Ari Pambudi
 */
public class DCSplitterServerCallbackController implements Runnable {
    
    public final static DCSplitterServerCallbackController getInstance = new DCSplitterServerCallbackController();
    
    public ITMTradingServerSettingsMgr              itmSetting                  = ITMTradingServerSettingsMgr.getInstance;
    
    private int iListenPort = 0;
    
    private int cChannelIDCounter = 0;
    
    private Thread thDCWorker;
    private final AtomicBoolean bDCWorkerIsBusy            = new AtomicBoolean(false);
    private final AtomicBoolean bDCWorkerMustRun           = new AtomicBoolean(false);
    
    private final String zWorkerID                          = "XCS";
    
    private ServerSocket dcListenSocket;
    
    private final ConcurrentHashMap<String, DCSplitterServerChannel> tsChannelMap = new ConcurrentHashMap<>();
    
    public DCSplitterServerCallbackController(){
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "");
    }
    
    public int getListenPort() {
        return iListenPort;
    }

    public void setListenPort(int iListenPort) {
        this.iListenPort = iListenPort;
    }
    
    public synchronized boolean isBusy(){ return this.bDCWorkerIsBusy.get(); }
    
    public boolean shutdownListener(){
        boolean mOut = false;
        try{
            if (dcListenSocket != null){
                if (!dcListenSocket.isClosed()){
                    dcListenSocket.close();
                }
            }
        } catch (IOException ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }

    public synchronized boolean isListen(){
        boolean bOut = false;
        try{
            if (dcListenSocket != null){
                if (!dcListenSocket.isClosed()){
                    if (dcListenSocket.getInetAddress() != null){
                        if (dcListenSocket.isBound()){
                            bOut = true;
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return bOut;
    }

    private DCSplitterServerChannel createChannel(Socket hInSocket){
        DCSplitterServerChannel mOut = null;
        try{
            cChannelIDCounter++;
            String zChannelID = "DCSCH_" + cChannelIDCounter;
            mOut = new DCSplitterServerChannel(zChannelID, hInSocket);
            tsChannelMap.put(zChannelID, mOut);
            mOut.startChannel();
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentHashMap<String, DCSplitterServerChannel> getAllChannels () {
        return tsChannelMap;
    }
    
    private int stopAndRemoveAllChannels(){
        int mOut = 0;
        try{
            if (!tsChannelMap.isEmpty()){
                Object[] arrChannelIDs = tsChannelMap.keySet().toArray();
                for(Object oEachChannelID : arrChannelIDs){
                    String zEachChannelID = (String)oEachChannelID;
                    DCSplitterServerChannel mEachChannel = tsChannelMap.get(zEachChannelID);
                    if (mEachChannel != null){
                        mEachChannel.stopChannel();
                    }
                    tsChannelMap.remove(zEachChannelID);
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }

    public synchronized void startWorker(){
        try{
            if (this.thDCWorker == null){
                this.thDCWorker = new Thread(this);
            }
            if (this.thDCWorker != null){
                if (!this.thDCWorker.isAlive()){
                    try{
                        this.thDCWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                        this.bDCWorkerMustRun.set(true);
                        this.thDCWorker.setPriority(Thread.NORM_PRIORITY);
                        this.thDCWorker.setName(this.zWorkerID);
                        this.thDCWorker.start();
                    }catch(Exception ex0){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }
                }else{
                    try{
                        this.notify();
                    }catch(Exception ex0){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }

    public synchronized void stopWorker(){
        try{
            if (this.thDCWorker != null){
                if (this.thDCWorker.isAlive()){
                    try{
                        this.bDCWorkerMustRun.set(false);
                        notify();
                        shutdownListener();
                    }catch(Exception ex0){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    @Override
    public void run() {
        //.flags:
        this.bDCWorkerIsBusy.set(true);
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "DC_SPLITTER_SVR_BEGIN:@WorkerID=" + zWorkerID);
        try{
            //.open listener:
            if (dcListenSocket == null){
                dcListenSocket = new ServerSocket(iListenPort);
            }else if (dcListenSocket != null){
                if (dcListenSocket.getLocalPort() != iListenPort){
                    //.reset:
                    shutdownListener();
                    stopAndRemoveAllChannels();
                    dcListenSocket = new ServerSocket(iListenPort);
                }else if (dcListenSocket.isClosed()){
                    dcListenSocket = new ServerSocket(iListenPort);
                }
            }
            //.maintenance:
            while (this.bDCWorkerMustRun.get()){
                try{
                    if (isListen()){
                        Socket inSock = dcListenSocket.accept();
                        if (inSock != null){
                            createChannel(inSock);
                        }
                    }
                    //.#istirahat dulu sebentar :)
                    synchronized(this){
                        wait(1);
                    }
                }catch(InterruptedException intex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, intex0);
                    //.berhenti:
                    break;
                }catch(IOException ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    //.berhenti:
                    break;
                }catch(Exception ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    //.berhenti:
                    break;
                }
            }
            //.close listener:
            shutdownListener();
            stopAndRemoveAllChannels();
        }catch(IOException ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        //.flags:
        this.bDCWorkerIsBusy.set(false);
        //.#worker selesai:
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "DC_SPLITTER_SVR_END:@WorkerID=" + zWorkerID);
    }
    
    //.channels:
    public class DCSplitterServerChannel {
        
        private final String zChannelID;
        private final Socket hInSocket;
        
        private DCSplitterServerChannelReaderWorker mRRWorker;
        private DCSplitterServerChannelWriterWorker mWWWorker;
        
        private final AtomicBoolean bUserAuthIsLoggedIn     = new AtomicBoolean(false);
        
        private final AtomicLong aiLoggedInSequenceNo       = new AtomicLong(0);
        private final AtomicLong aiCurrentSequenceNo        = new AtomicLong(0);
    
        private String zSession;
        
        public DCSplitterServerChannel(String zChannelID, Socket hInSocket){
            this.zChannelID = zChannelID;
            this.hInSocket = hInSocket;
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "@DC_CC_ChannelID=" + this.zChannelID);
        }
        
        public boolean userAuthIsLoggedIn() {
            return this.bUserAuthIsLoggedIn.get();
        }

        public boolean setUserAuthIsLoggedIn(boolean vNewValue){
            return this.bUserAuthIsLoggedIn.getAndSet(vNewValue);
        }

        public long getLoggedInSequenceNo() {
            return this.aiLoggedInSequenceNo.get();
        }

        public long setLoggedInSequenceNo(long vNewValue){
            return this.aiLoggedInSequenceNo.getAndSet(vNewValue);
        }

        public long getCurrentSequenceNo() {
            return this.aiCurrentSequenceNo.get();
        }

        public long setCurrentSequenceNo(long vNewValue){
            return this.aiCurrentSequenceNo.getAndSet(vNewValue);
        }

        public String getSession() {
            return zSession;
        }
        
        public void setSession(String zSession) {
            this.zSession = zSession;
        }

        public synchronized void startChannel(){
            try{
                stopChannel();
                if (mRRWorker == null){
                    mRRWorker = new DCSplitterServerChannelReaderWorker(zChannelID, this, hInSocket);
                    mRRWorker.startChWorker();
                }
                if (mWWWorker == null){
                    mWWWorker = new DCSplitterServerChannelWriterWorker(zChannelID, this, hInSocket);
                    mWWWorker.startChWorker();
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
        }
        
        public synchronized void stopChannel(){
            try{
                if (mRRWorker != null){
                    mRRWorker.stopChWorker();
                }
                if (mWWWorker != null){
                    mWWWorker.stopChWorker();
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
        }
        
        public int sendBytesDirect(byte[] btMessageBytes){
            int mOut = 0;
            try{
                if ((btMessageBytes != null) && (btMessageBytes.length > 0)){
                    if (hInSocket != null){
                        if (!hInSocket.isClosed()){
                            OutputStream ost = hInSocket.getOutputStream();
                            ost.write(btMessageBytes);
                            ost.flush();
                            mOut = btMessageBytes.length;
                        }
                    }
                }
            }catch(IOException ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            return mOut;
        }
        
        public synchronized boolean sendMessageDirect(String zMessageLine){
            boolean bOut = false;
            try{
                if (!StringHelper.isNullOrEmpty(zMessageLine)){
                    if (hInSocket != null){
                        if (!hInSocket.isClosed()){
                            OutputStream ost = hInSocket.getOutputStream();
//                            if (fCustomLineFactory){
//                                ost.write(zMessageLine.concat(SocketSetup.NEW_LINE).getBytes());
//                            }else{
//                                //.20251127: untuk fix5 direct
//                                if (fLineReaderVersion == 2) {
                                    ost.write(zMessageLine.getBytes());
                                    ost.flush();
//                                } else { //. untuk fix5 via api
//                                    ost.write(zMessageLine.concat(SocketSetup.NEW_LINE).getBytes());
//                                }
//
//                            }
                            //////////ost.flush();
//                            raiseOnSent(zMessageLine);
                            bOut = true;
                        }
                    }
                }
            }catch(IOException ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            return bOut;
        }
        
    }
    
    
    
    private class DCSplitterServerChannelReaderWorker implements Runnable{
        private Thread thDCChWorker;
        private final AtomicBoolean bDCChWorkerIsBusy              = new AtomicBoolean(false);
        private final AtomicBoolean bDCChWorkerMustRun             = new AtomicBoolean(false);
        
        private final String zChannelID;
        private final Socket hInSocket;
        private final DCSplitterServerChannel mChannelObj;
        
        private DataInputStream hDataInputStream;
        
        private InputStream mCustomInputStream                  = null;
        
        public DCSplitterServerChannelReaderWorker(String zChannelID, DCSplitterServerChannel mChannelObj, Socket hInSocket){
            this.zChannelID = zChannelID;
            this.hInSocket = hInSocket;
            this.mChannelObj = mChannelObj;
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "@DC_RR_ChannelID=" + this.zChannelID);
        }
        
        public String getChannelID(){ return this.zChannelID; }
        public boolean isBusy(){ return this.bDCChWorkerIsBusy.get(); }
        
        public synchronized void startChWorker(){
            try{
                if (this.thDCChWorker == null){
                    this.thDCChWorker = new Thread(this);
                }
                if (this.thDCChWorker != null){
                    if (!this.thDCChWorker.isAlive()){
                        //.persiapkan bufferedreader di awal:
                        try{
                            this.hDataInputStream = new DataInputStream(this.hInSocket.getInputStream());
                            this.hInSocket.setReceiveBufferSize(1000000);
                            this.mCustomInputStream = this.hInSocket.getInputStream();
                        }catch(IOException ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                        try{
                            this.thDCChWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                            this.bDCChWorkerMustRun.set(true);
                            this.thDCChWorker.setPriority(Thread.NORM_PRIORITY);
                            this.thDCChWorker.setName(zChannelID + "_RR");
                            this.thDCChWorker.start();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
        }
        
        public synchronized void stopChWorker(){
            try{
                if (this.thDCChWorker != null){
                    if (this.thDCChWorker.isAlive()){
                        try{
                            this.bDCChWorkerMustRun.set(false);
                            notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
        }
        
        @Override
        public void run() {
            //.flags:
            this.bDCChWorkerIsBusy.set(true);
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "DC_RR_BEGIN:@ChannelID=" + zChannelID);
            try{
                InputStream in;
                DataInputStream mReader = this.hDataInputStream;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            
                in = mCustomInputStream;
                //.check connection sync:
                if (!ITMTradingServerSyncConnectionMgr.getInstance.isSafeForClientToConnect()){
                    ///this.bDCChWorkerMustRun.set(false);
                }
                //.maintenance:
                while (this.bDCChWorkerMustRun.get()){
                    try{
                        if (hInSocket.isClosed()){
                            this.bDCChWorkerMustRun.set(false);
                            break;
                        }else if (tsChannelMap.size() > itmSetting.getSettings().server_settings.splitter_dropcopy.maxcount){
                            this.bDCChWorkerMustRun.set(false);
                            break;
                        }else{
                            int b;
                            if (in != null) {
                                while ((b = in.read()) != -1) {

                                    buffer.write(b);

                                    byte[] arr = buffer.toByteArray();
                                    int len = arr.length;

                                    // FIX message minimal punya "<SOH>10=XYZ<SOH>"
                                    if (len < 8) continue;

                                    // Cari pola "<SOH>10=nnn<SOH>"
                                    for (int i = 0; i < len - 7; i++) {
                                        // cek "10="
                                        //.20260513: terjadi bug jika ada tag 110= atau ?10= dan diikuti 3 digit akan mengakibatkan message corrupt
                                        //.20260513: jadi, cara ceknya diganti <SOH>10=
                                        if (arr[i] == 0x01 && arr[i+1] == '1' && arr[i+2] == '0' && arr[i+3] == '=') {

                                            // cek checksum fixed 3 digit
                                            if (isDigit(arr[i+4]) && isDigit(arr[i+5]) && isDigit(arr[i+6])) {

                                                // cek SOH
                                                if (arr[i+7] == 0x01) {

                                                    // -> message FIX sempurna ditemukan
                                                    int msgLength = i + 8;

                                                    byte[] msgBytes = Arrays.copyOfRange(arr, 0, msgLength);
                                                    String msg = new String(msgBytes, StandardCharsets.US_ASCII);

                                                    FIX5JonecMessageProcessor fix5JonecMsgMapper = new FIX5JonecMessageProcessor();
                                                    FIX5IDXMessage messageObject = fix5JonecMsgMapper.parseMessage(msg);
                                                    if (messageObject instanceof FIX5JonecDataLogonReply){
                                                        //.kirim ke client splitter supaya dapat langsung memproses login
                                                        mChannelObj.sendMessageDirect("8=FIXT.1.19=00008435=A49=MME56=RS34=157=XXJDA152=20260708-01:36:33.91898=0108=451409=01137=910=115");
                                                    } else if (messageObject instanceof FIX5JonecDataLogoutReply){
                                                        //.kirim ke client splitter supaya dapat langsung memproses logout
                                                        mChannelObj.sendMessageDirect("8=FIXT.1.19=00007735=549=MME56=SH34=22157=XXJDA152=20260709-08:10:51.5821409=458=Logout10=125");
                                                        //.putus socket ketika ada request logout
                                                        if (this.hInSocket != null){
                                                            if (!this.hInSocket.isClosed()){
                                                                this.hInSocket.close();
                                                            }
                                                        }
                                                    }
                                                    

                                                    // sisanya masih bisa berisi FIX message berikutnya
                                                    byte[] remaining = Arrays.copyOfRange(arr, msgLength, len);

                                                    buffer.reset();
                                                    buffer.write(remaining);

                                                    break; // keluar dari for, kembali ke read loop
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                              
                        }
                        //.#istirahat dulu sebentar :)
                        synchronized(this){
                            wait(itmSetting.getSettings().server_settings.splitter_dropcopy.pausebreak > 0 ? itmSetting.getSettings().server_settings.splitter_dropcopy.pausebreak : 1);
                        }
                    }catch(InterruptedException | IOException ex0){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        //.berhenti:
                        break;
                    }catch(Exception ex0){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        //.berhenti:
                        break;
                    }
                    
                }
                try{
                    if (this.hInSocket != null){
                        if (!this.hInSocket.isClosed()){
                            this.hInSocket.close();
                        }
                    }
                }catch(IOException ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }catch(Exception ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            //.stop channel:
            try{
                mChannelObj.stopChannel();
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            //.remove channel from map:
            try{
                tsChannelMap.remove(zChannelID);
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            //.flags:
            this.bDCChWorkerIsBusy.set(false);
            //.#worker selesai:
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "DC_RR_END:@ChannelID=" + zChannelID);
        }
        
        private boolean isDigit(byte b) {
            return b >= '0' && b <= '9';
        }
        
    }
    
    private class DCSplitterServerChannelWriterWorker implements Runnable{
        private Thread thDCChWorkerWW;
        private final AtomicBoolean bDCChWorkerIsBusy              = new AtomicBoolean(false);
        private final AtomicBoolean bDCChWorkerMustRun             = new AtomicBoolean(false);
        
        private final String zChannelID;
        private final Socket hInSocket;
        private final DCSplitterServerChannel mChannelObj;
        
        private DataOutputStream hDataOutputStream;
        
        public DCSplitterServerChannelWriterWorker(String zChannelID, DCSplitterServerChannel mChannelObj, Socket hInSocket){
            this.zChannelID = zChannelID;
            this.hInSocket = hInSocket;
            this.mChannelObj = mChannelObj;
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.INIT, "@DC_WW_ChannelID=" + this.zChannelID);
        }
        
        public String getChannelID(){ return this.zChannelID; }
        public boolean isBusy(){ return this.bDCChWorkerIsBusy.get(); }
        
        public synchronized void startChWorker(){
            try{
                if (this.thDCChWorkerWW == null){
                    this.thDCChWorkerWW = new Thread(this);
                }
                if (this.thDCChWorkerWW != null){
                    if (!this.thDCChWorkerWW.isAlive()){
                        //.persiapkan bufferedwriter di awal:
                        try{
                            this.hDataOutputStream = new DataOutputStream(this.hInSocket.getOutputStream());
                            this.hInSocket.setSendBufferSize(1000000);
                        }catch(IOException ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                        try{
                            this.thDCChWorkerWW = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                            this.bDCChWorkerMustRun.set(true);
                            this.thDCChWorkerWW.setPriority(Thread.NORM_PRIORITY);
                            this.thDCChWorkerWW.setName(zChannelID + "_WW");
                            this.thDCChWorkerWW.start();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
        }
        
        public synchronized void stopChWorker(){
            try{
                if (this.thDCChWorkerWW != null){
                    if (this.thDCChWorkerWW.isAlive()){
                        try{
                            this.bDCChWorkerMustRun.set(false);
                            notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
        }
        
        @Override
        public void run() {
            //.flags:
            this.bDCChWorkerIsBusy.set(true);
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "DC_WW_BEGIN:@ChannelID=" + zChannelID);
            try{
                DataOutputStream mWriter = this.hDataOutputStream;
                //.check connection sync:
                if (!ITMTradingServerSyncConnectionMgr.getInstance.isSafeForClientToConnect()){
                    ///this.bDCChWorkerMustRun.set(false);
                }
                //.maintenance:
                boolean bFirstLoopAfterAuth = true;
                long tLastHeartBeatMS = System.currentTimeMillis();
                while (this.bDCChWorkerMustRun.get()){
                    try{
                        if (hInSocket.isClosed()){
                            this.bDCChWorkerMustRun.set(false);
                            break;
                        }else if (tsChannelMap.size() > itmSetting.getSettings().server_settings.splitter_dropcopy.maxcount){
                            this.bDCChWorkerMustRun.set(false);
                            break;
                        }else{
                            if (mChannelObj.userAuthIsLoggedIn()){
                                
                            }
                        }
                        //.#istirahat dulu sebentar :)
                        synchronized(this){
                            wait(itmSetting.getSettings().server_settings.splitter_dropcopy.pausebreak > 0 ? itmSetting.getSettings().server_settings.splitter_dropcopy.pausebreak : 1);
                        }
                    }catch(InterruptedException ex0){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        //.berhenti:
                        break;
                    }catch(Exception ex0){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        //.berhenti:
                        break;
                    }
                    
                }
                try{
                    if (this.hInSocket != null){
                        if (!this.hInSocket.isClosed()){
                            this.hInSocket.close();
                        }
                    }
                }catch(IOException ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }catch(Exception ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            //.stop channel:
            try{
                mChannelObj.stopChannel();
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            //.remove channel from map:
            try{
                tsChannelMap.remove(zChannelID);
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            //.flags:
            this.bDCChWorkerIsBusy.set(false);
            //.#worker selesai:
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "DC_WW_END:@ChannelID=" + zChannelID);
        }
        
    }
    
}
