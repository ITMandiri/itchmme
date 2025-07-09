/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.message.bridge;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.setup.ITMSocketCtrlClient;
import com.itm.generic.engine.socket.setup.ITMSocketListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5IDXBridgeController extends ITMSocketCtrlClient {

    public enum FIX5IDXGroupMessageType{
        FIX5_JONEC_MESSAGE
    }
    
    public enum FIX5IDXBridgeStatus{ //.connecting, connected, disconnecting, disconnected.
        SCK_DISCONNECTED        (0, "Disconnected"),
        SCK_DISCONNECTING       (1, "Disconnecting"),
        SCK_CONNECTING          (2, "Connecting"),
        SCK_CONNECTED           (3, "Connected");
        
        public final int order;
        public final String cname;

        private FIX5IDXBridgeStatus(int order, String cname) {
            this.order = order;
            this.cname = cname;
        }

    }
    
    private final FIX5IDXBridgeSocketHandler bridgeSockHandler;
    private FIX5InBridgeSocketGuardWorker recvGdWorker;
    
    private String zRefServerAddress                            = "";
    private int iRefServerPort                                  = 0;
    private int iRefTryConnectTimeOut                           = 0; //.milidetik | milliseconds.
    private int iRefCheckInterval                               = 0;
    
    private FIX5IDXGroupMessageType msgGroupType                    = FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE;
    private FIX5IDXBridgeStatus stsBridgeStatus                     = FIX5IDXBridgeStatus.SCK_DISCONNECTED;
    
    private final AtomicBoolean bMustConnected                      = new AtomicBoolean(false);
    
    //.tambahan:
    private boolean bIsAdminLoggedOn                            = false;
    private boolean bIsAdminPasswordExpired                     = false;
    private boolean bIsAdminPasswordNearExpire                  = false;
    private String zLastMessage                                 = "";
    
    //.fix5auth&configurations:
    private int fConnectionOrderNo                              = 0;
    private String zConnectorCode                               = "";
    private String zTraderCode                                  = "";
    private String zPassword1                                   = "";
    private String zPassword2                                   = "";
    private boolean bHeartBeat                                  = false;
    private boolean bCmpMsgExact                                = false;
    private int iReconSeqMode                                   = 0;
    private boolean bCalcHeader                                 = false;
    
    //.sequencedno & recovery:
    private long fCurrentTXSequencedNo                          = 0; //.sequenceno yg sudah valid dan dipakai saat ini (transmitted);
    private long fCurrentRXSequencedNo                          = 0; //.sequenceno yg sudah valid dan dipakai saat ini (received);
    
    //.select:
    private boolean bAutoSelect                                     = false;
    
    public FIX5IDXBridgeController(String zConnectionName) {
        /******
        //.if connect to "jones fix5" (unused):
        super(zConnectionName, new FIX5IDXBridgeCustomLineFactoryWorker());
        ******/
        //.if connect to "eltrader fix5" (must use this):
        super(zConnectionName);
        this.bridgeSockHandler = new FIX5IDXBridgeSocketHandler(this);
        addSocketEventHandler(this.bridgeSockHandler);
        //.EXXX.
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.INIT, "");
    }
    
    public void addExtraEventHandler(ITMSocketListener eventHandler){
        addSocketEventHandler(eventHandler);
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

    public FIX5IDXGroupMessageType getMsgGroupType() {
        if (this.msgGroupType == null){
            this.msgGroupType = FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE;
        }
        return this.msgGroupType;
    }

    public void setMsgGroupType(FIX5IDXGroupMessageType msgGroupType) {
        this.msgGroupType = msgGroupType;
    }
    
    public FIX5IDXBridgeStatus getStsBridgeStatus() {
        if (this.stsBridgeStatus == null){
            this.stsBridgeStatus = FIX5IDXBridgeStatus.SCK_DISCONNECTED;
        }
        return this.stsBridgeStatus;
    }

    public void setStsBridgeStatus(FIX5IDXBridgeStatus stsBridgeStatus) {
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
    
    //.fix5auth&configurations:
    
    public int getfConnectionOrderNo() {
        return fConnectionOrderNo;
    }

    public void setfConnectionOrderNo(int fConnectionOrderNo) {
        this.fConnectionOrderNo = fConnectionOrderNo;
    }

    public String getConnectorCode() {
        return zConnectorCode;
    }

    public void setConnectorCode(String zConnectorCode) {
        this.zConnectorCode = zConnectorCode;
    }

    public String getTraderCode() {
        return zTraderCode;
    }

    public void setTraderCode(String zTraderCode) {
        this.zTraderCode = zTraderCode;
    }

    public String getPassword1() {
        return zPassword1;
    }

    public void setPassword1(String zPassword1) {
        this.zPassword1 = zPassword1;
    }

    public String getPassword2() {
        return zPassword2;
    }

    public void setPassword2(String zPassword2) {
        this.zPassword2 = zPassword2;
    }

    public boolean isHeartBeat() {
        return bHeartBeat;
    }

    public void setHeartBeat(boolean bHeartBeat) {
        this.bHeartBeat = bHeartBeat;
    }

    public boolean isCmpMsgExact() {
        return bCmpMsgExact;
    }

    public void setCmpMsgExact(boolean bCmpMsgExact) {
        this.bCmpMsgExact = bCmpMsgExact;
    }

    public boolean isCalcHeader() {
        return bCalcHeader;
    }

    public void setCalcHeader(boolean bCalcHeader) {
        this.bCalcHeader = bCalcHeader;
    }

    public int getReconSeqMode() {
        return iReconSeqMode;
    }

    public void setReconSeqMode(int iReconSeqMode) {
        this.iReconSeqMode = iReconSeqMode;
    }
    
    //.sequencedno & recovery:
    public long getCurrentTXSequencedNo() {
        return fCurrentTXSequencedNo;
    }

    public void setCurrentTXSequencedNo(long fCurrentTXSequencedNo) {
        this.fCurrentTXSequencedNo = fCurrentTXSequencedNo;
    }

    public long getCurrentRXSequencedNo() {
        return fCurrentRXSequencedNo;
    }

    public void setCurrentRXSequencedNo(long fCurrentRXSequencedNo) {
        this.fCurrentRXSequencedNo = fCurrentRXSequencedNo;
    }
    
    public synchronized long getNextTXSequencedNo(){
        return ++this.fCurrentTXSequencedNo;
    }
    
    public synchronized long getNextRXSequencedNo(){
        return ++this.fCurrentRXSequencedNo;
    }
    
    public boolean isAutoSelect() {
        return bAutoSelect;
    }

    public void setAutoSelect(boolean bAutoSelect) {
        this.bAutoSelect = bAutoSelect;
    }

    public FIX5IDXBridgeSocketHandler getBridgeSockHandler() {
        return bridgeSockHandler;
    }

    //.listener methods:
    public void addBridgeListener(FIX5IDXBridgeListener newListener){
        try{
            if (newListener == null){
                return;
            }
            bridgeSockHandler.addBridgeListener(newListener);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    public void removeBridgeListener(FIX5IDXBridgeListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            bridgeSockHandler.removeBridgeListener(oldListener);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
    }
    
    
    //.connection methods:
    public synchronized boolean verifyConnection(){
        boolean bOut = false;
        try{
            if (this.bMustConnected.get()){
                //.start:proses-inworker:
                if (recvGdWorker == null){
                    recvGdWorker = new FIX5InBridgeSocketGuardWorker();
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
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
                stsBridgeStatus = FIX5IDXBridgeStatus.SCK_CONNECTING;
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
                stsBridgeStatus = FIX5IDXBridgeStatus.SCK_CONNECTED;
            }else{
                stsBridgeStatus = FIX5IDXBridgeStatus.SCK_DISCONNECTED;
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public synchronized boolean stopConnection(){
        boolean bOut = false;
        try{
            if (isConnected()){
                //.set (awal) status:
                stsBridgeStatus = FIX5IDXBridgeStatus.SCK_DISCONNECTING;
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
                stsBridgeStatus = FIX5IDXBridgeStatus.SCK_DISCONNECTED;
            }else{
                stsBridgeStatus = FIX5IDXBridgeStatus.SCK_CONNECTED;
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    
    
    public synchronized boolean sendMessageDirect(String zMessage){
        boolean bOut = false;
        try{
            bOut = getChannel().sendMessageDirect(zMessage);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public synchronized boolean sendToMessageQueue(String zMessage){
        boolean bOut = false;
        try{
            bOut = getChannel().addSendQueue(zMessage);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    
    //.worker:
    private class FIX5InBridgeSocketGuardWorker implements Runnable{
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
            }
        }
        
        @Override
        public void run() {
            while (this.bWrWorkerMustRun.get()){
                try{
                    //.tugas: menjaga koneksi bridge socket:
                    if (bMustConnected.get()){
                        if (!isConnected()){
                            //.cek password (hanya connect ulang jika password belum expired):
                            if (!bIsAdminPasswordExpired){
                                //.set (awal) status:
                                stsBridgeStatus = FIX5IDXBridgeStatus.SCK_CONNECTING;
                                //.coba koneksi:
                                connect(zRefServerAddress, iRefServerPort, iRefTryConnectTimeOut);
                                //.cek status:
                                stsBridgeStatus = (isConnected() ? FIX5IDXBridgeStatus.SCK_CONNECTED : FIX5IDXBridgeStatus.SCK_DISCONNECTED );
                            }
                        }
                    }else{
                        if (isConnected()){
                            //.set (awal) status:
                            stsBridgeStatus = FIX5IDXBridgeStatus.SCK_DISCONNECTING;
                            //.matikan koneksi:
                            disconnect();
                            //.cek status:
                            stsBridgeStatus = (isConnected() ? FIX5IDXBridgeStatus.SCK_CONNECTED : FIX5IDXBridgeStatus.SCK_DISCONNECTED );
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
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, intex0);
                    break;
                }catch(Exception ex0){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.ERROR, ex0);
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
