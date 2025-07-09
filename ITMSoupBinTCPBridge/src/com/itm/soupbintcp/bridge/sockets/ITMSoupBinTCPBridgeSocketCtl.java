/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.sockets;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketConsts.SoupBinTCPBridgeSocketSetup;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgSequencedDataPacket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeSocketCtl {
    
    private String fConnectionName                                  = "";
    
    //.config socket:
    private int fSocketSendBufferSize                               = 10000; //.apm:20220325:ref hrn;
    private boolean fSocketSendAutoFlush                            = true; //.apm:20220325:ref hrn;
    private int fSocketReceiveBufferSize                            = 6400; //.apm:20220325:ref hrn;
    
    //.heartbeat:
    private boolean fHeartBeatEnabled                               = false;
    private long    fLastServerHeartBeatRcvMs                       = 0;
    
    //.msgcompare & reconnect sequence mode:
    private boolean fCmpMsgExact                                    = false;
    private int fReconSeqMode                                       = 0;
    
    //.authentication:
    private boolean fAuthAutoLogin                                  = true;
    private String fAuthUserName                                    = "";
    private String fAuthUserPassword                                = "";
    private String fAuthLastUserSession                             = "";
    private String fAuthLastRejectReasonCode                        = "";
    private boolean fAuthLastAccepted                               = false;
    
    //.sequencedno & recovery:
    private long fCurrentSequencedNo                                = 0; //.sequenceno yg sudah valid dan dipakai saat ini;
    private long fRecoveryExpectationSequencedNo                    = 0; //.target sequenceno jika akan menyambung ulang koneksi; biasanya = fcurrentsequencedno (jika fcurrentsequencedno >= 1), atau = 1 jika fcurrentsequencedno = 0, dengan tujuan pegecekan messages yg akan diterima adalah benar lanjutan dari messages yg terputus sebelumnya;
    private long fRecoveryRealitySequencedNo                        = 0; //.sequenceno yang didapat saat menyambung ulang koneksi;
    
    private ITMSoupBinTCPMsgBase fRecentSequencedMsg                = null;
    
    //.select:
    private boolean bAutoSelect                                     = false;
    
    private ITMSoupBinTCPBridgeSocketChannel fSocketChannel;
    
    private List<ITMSoupBinTCPBridgeSocketListener> fServerListenersList;
    
    public ITMSoupBinTCPBridgeSocketCtl(String zConnectionName) {
        this.fConnectionName = zConnectionName;
        //.EXXX.
    }
    
    public String getConnectionName() {
        return fConnectionName;
    }

    public void setConnectionName(String fConnectionName) {
        this.fConnectionName = fConnectionName;
        try{
            if (fSocketChannel != null){
                fSocketChannel.setConnectionName(this.fConnectionName);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    //.config socket:
    public int getSocketSendBufferSize() {
        return fSocketSendBufferSize;
    }

    public void setSocketSendBufferSize(int fSocketSendBufferSize) {
        this.fSocketSendBufferSize = fSocketSendBufferSize;
    }

    public int getSocketReceiveBufferSize() {
        return fSocketReceiveBufferSize;
    }

    public void setSocketReceiveBufferSize(int fSocketReceiveBufferSize) {
        this.fSocketReceiveBufferSize = fSocketReceiveBufferSize;
    }
    
    

    public boolean isSocketSendAutoFlush() {
        return fSocketSendAutoFlush;
    }

    public void setSocketSendAutoFlush(boolean fSocketSendAutoFlush) {
        this.fSocketSendAutoFlush = fSocketSendAutoFlush;
    }
    
    //.heartbeat:
    public boolean isHeartBeatEnabled() {
        return fHeartBeatEnabled;
    }

    public void setHeartBeatEnabled(boolean fHeartBeatEnabled) {
        this.fHeartBeatEnabled = fHeartBeatEnabled;
        try{
            if (fSocketChannel != null){
                fSocketChannel.setClientHeartBeatEnabled(this.fHeartBeatEnabled);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }

    public long getfLastServerHeartBeatRcvMs() {
        return fLastServerHeartBeatRcvMs;
    }

    public void setfLastServerHeartBeatRcvMs(long fLastServerHeartBeatRcvMs) {
        this.fLastServerHeartBeatRcvMs = fLastServerHeartBeatRcvMs;
    }
    
    
    //.msgcompare & reconnect sequence mode:
    public boolean isCmpMsgExact() {
        return fCmpMsgExact;
    }

    public void setCmpMsgExact(boolean fCmpMsgExact) {
        this.fCmpMsgExact = fCmpMsgExact;
        try{
            if (fSocketChannel != null){
                fSocketChannel.setCmpMsgExact(this.fCmpMsgExact);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    public int getReconSeqMode() {
        return fReconSeqMode;
    }

    public void setReconSeqMode(int fReconSeqMode) {
        this.fReconSeqMode = fReconSeqMode;
        try{
            if (fSocketChannel != null){
                fSocketChannel.setReconSeqMode(this.fReconSeqMode);
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    
    //.authentication:
    public boolean isAuthAutoLogin() {
        return fAuthAutoLogin;
    }

    public void setAuthAutoLogin(boolean fAuthAutoLogin) {
        this.fAuthAutoLogin = fAuthAutoLogin;
    }

    public String getAuthUserName() {
        return fAuthUserName;
    }

    public void setAuthUserName(String fAuthUserName) {
        this.fAuthUserName = fAuthUserName;
    }

    public String getAuthUserPassword() {
        return fAuthUserPassword;
    }

    public void setAuthUserPassword(String fAuthUserPassword) {
        this.fAuthUserPassword = fAuthUserPassword;
    }

    public String getAuthLastUserSession() {
        return fAuthLastUserSession;
    }

    public void setAuthLastUserSession(String fAuthLastUserSession) {
        this.fAuthLastUserSession = fAuthLastUserSession;
    }
    
    public String getAuthLastRejectReasonCode() {
        return fAuthLastRejectReasonCode;
    }

    public void setAuthLastRejectReasonCode(String fAuthLastRejectReasonCode) {
        this.fAuthLastRejectReasonCode = fAuthLastRejectReasonCode;
    }
    
    public boolean isAuthLastAccepted() {
        return fAuthLastAccepted;
    }

    public void setAuthLastAccepted(boolean fAuthLastAccepted) {
        this.fAuthLastAccepted = fAuthLastAccepted;
    }

    //.sequencedno & recovery:
    public long getCurrentSequencedNo() {
        return fCurrentSequencedNo;
    }

    public void setCurrentSequencedNo(long fCurrentSequencedNo) {
        this.fCurrentSequencedNo = fCurrentSequencedNo;
    }

    public long getRecoveryExpectationSequencedNo() {
        return fRecoveryExpectationSequencedNo;
    }

    public void setRecoveryExpectationSequencedNo(long fRecoveryExpectationSequencedNo) {
        this.fRecoveryExpectationSequencedNo = fRecoveryExpectationSequencedNo;
    }

    public long getRecoveryRealitySequencedNo() {
        return fRecoveryRealitySequencedNo;
    }

    public void setRecoveryRealitySequencedNo(long fRecoveryRealitySequencedNo) {
        this.fRecoveryRealitySequencedNo = fRecoveryRealitySequencedNo;
    }

    public ITMSoupBinTCPMsgBase getRecentSequencedMsg() {
        return fRecentSequencedMsg;
    }

    public void setRecentSequencedMsg(ITMSoupBinTCPMsgBase fRecentSequencedMsg) {
        this.fRecentSequencedMsg = fRecentSequencedMsg;
    }
    
    public long getPreparedRecoveryExpectationSequencedNo(){
        long mOut = (this.fCurrentSequencedNo + 1);
        if (this.fReconSeqMode > 0){
            //.apm:20200728:harus mengikuti setting, clear recent message:
            this.fCurrentSequencedNo = (this.fReconSeqMode - 1); //.agar current sequencedno jadi awal (0);
            this.fRecentSequencedMsg = null;
        }
        if (this.fCurrentSequencedNo <= 0){
            this.fCurrentSequencedNo = 0;
            this.fRecentSequencedMsg = null;
            this.fRecoveryRealitySequencedNo = 0;
            this.fRecoveryExpectationSequencedNo = 1;
            mOut = this.fRecoveryExpectationSequencedNo;
        }else{
            if (this.fRecentSequencedMsg != null){
                ITMSoupBinTCPMsgSequencedDataPacket smsg = (ITMSoupBinTCPMsgSequencedDataPacket)this.fRecentSequencedMsg;
                if (smsg != null){
                    if (smsg.getSelfSequencedNo() == this.fCurrentSequencedNo){
                        this.fRecoveryRealitySequencedNo = 0;
                        this.fRecoveryExpectationSequencedNo = this.fCurrentSequencedNo;
                        mOut = this.fRecoveryExpectationSequencedNo;
                    }else{
                        this.fRecentSequencedMsg = null;
                        this.fRecoveryRealitySequencedNo = 0;
                        this.fRecoveryExpectationSequencedNo = (this.fCurrentSequencedNo + 1);
                        mOut = this.fRecoveryExpectationSequencedNo;
                    }
                }else{
                    this.fRecentSequencedMsg = null;
                    this.fRecoveryRealitySequencedNo = 0;
                    this.fRecoveryExpectationSequencedNo = (this.fCurrentSequencedNo + 1);
                    mOut = this.fRecoveryExpectationSequencedNo;
                }
            }else{
                this.fRecentSequencedMsg = null;
                this.fRecoveryRealitySequencedNo = 0;
                this.fRecoveryExpectationSequencedNo = (this.fCurrentSequencedNo + 1);
                mOut = this.fRecoveryExpectationSequencedNo;
            }
        }
        return mOut;
    }
    
    public boolean isAutoSelect() {
        return bAutoSelect;
    }

    public void setAutoSelect(boolean bAutoSelect) {
        this.bAutoSelect = bAutoSelect;
    }
    //.protected synchronized
    protected synchronized boolean addSocketEventHandler(ITMSoupBinTCPBridgeSocketListener eventHandler){
        boolean bOut = false;
        try{
            if (eventHandler != null){
                if (this.fServerListenersList == null) {
                    this.fServerListenersList = new ArrayList<>();
                }
                if (!this.fServerListenersList.contains(eventHandler)){ //.@only once per object.@
                    this.fServerListenersList.add(eventHandler);
                }
            }
            bOut = true;
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    //.protected synchronized
    protected synchronized boolean removeSocketEventHandler(ITMSoupBinTCPBridgeSocketListener eventHandler){
        boolean bOut = false;
        try{
            if (eventHandler != null){
                if (this.fServerListenersList != null) {
                    this.fServerListenersList.remove(eventHandler);
                }
            }
            bOut = true;
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    //.public synchronized
    public ITMSoupBinTCPBridgeSocketChannel getChannel() {
        return fSocketChannel;
    }
    //.public synchronized
    public boolean isConnected(){
        boolean bOut = false;
        try{
            if (fSocketChannel != null){
                if (fSocketChannel.getSocket() != null){
                    if (!fSocketChannel.isClosed()){
                        if ((!fSocketChannel.isInputShutdown()) && (!fSocketChannel.isOutputShutdown())){
                            bOut = fSocketChannel.isConnected();
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    //.protected synchronized
    protected synchronized boolean connect(String zInputServerAddress, int iInputServerPort, int iConnectTimeOut){
        boolean bOut = false;
        try{
            if ((zInputServerAddress != null) && (zInputServerAddress.length() > 0) && (iInputServerPort >= SoupBinTCPBridgeSocketSetup.I_REF_PORT_MIN_NUMBER) && (iInputServerPort <= SoupBinTCPBridgeSocketSetup.I_REF_PORT_MAX_NUMBER) && (iConnectTimeOut >= 0)){
                if (fSocketChannel == null){
                    fSocketChannel = new ITMSoupBinTCPBridgeSocketChannel(this.fConnectionName);
                    //.set controller:
                    fSocketChannel.setSocketController(this);
                    //.set events:
                    fSocketChannel.setSocketListeners(this.fServerListenersList);
                    //.set properties:
                    fSocketChannel.setClientHeartBeatEnabled(this.fHeartBeatEnabled);
                    fSocketChannel.setCmpMsgExact(this.fCmpMsgExact);
                    fSocketChannel.setReconSeqMode(this.fReconSeqMode);
                    fSocketChannel.setSocketSendBufferSize(this.fSocketSendBufferSize);
                    fSocketChannel.setSocketRecvBufferSize(this.fSocketReceiveBufferSize);
                    fSocketChannel.setSocketSendAutoFlush(this.fSocketSendAutoFlush);
                    
                }
                if (fSocketChannel.isChannelAlreadyWasted()){
                    //.ganti channel:
                    ITMSoupBinTCPBridgeSocketChannel newSch = new ITMSoupBinTCPBridgeSocketChannel(this.fConnectionName);
                    //.set controller:
                    newSch.setSocketController(this);
                    //.backup events:
                    newSch.setSocketListeners(this.fServerListenersList);
                    //.backup queue:
                    newSch.setfQueue(fSocketChannel.getfQueue());//.set properties:
                    //.backup properties:
                    newSch.setClientHeartBeatIdleIntervalMS(fSocketChannel.getClientHeartBeatIdleIntervalMS());
                    newSch.setClientHeartBeatEnabled(fSocketChannel.isClientHeartBeatEnabled());
                    newSch.setCmpMsgExact(fSocketChannel.isCmpMsgExact());
                    newSch.setReconSeqMode(fSocketChannel.getReconSeqMode());
                    newSch.setSocketSendBufferSize(fSocketChannel.getSocketSendBufferSize());
                    newSch.setSocketRecvBufferSize(fSocketChannel.getSocketRecvBufferSize());
                    newSch.setSocketSendAutoFlush(fSocketChannel.isSocketSendAutoFlush());
                    
                    //.result:
                    fSocketChannel = newSch;
                }
                Socket sock = fSocketChannel.getSocket();
                if ((sock == null) || (fSocketChannel.isClosed()) || (fSocketChannel.isInputShutdown()) || (fSocketChannel.isOutputShutdown()) || (!fSocketChannel.isSocketWasWorking())){
                    try{
                        sock = new Socket();
                        sock.setReceiveBufferSize(2 * 1024 * 1024); //.2MB;
                        if (fSocketChannel.getSocketRecvBufferSize() > 0){
                            sock.setReceiveBufferSize(fSocketChannel.getSocketRecvBufferSize());
                            System.err.println("ITMSoupBinTCPBridgeSocketCtl sock.bufferRecvSize = " + sock.getReceiveBufferSize());
                        }
                        SocketAddress sockAddr = new InetSocketAddress(zInputServerAddress, iInputServerPort);
                        sock.connect(sockAddr, iConnectTimeOut);
                        //.menunggu sampai connect atau sampai tidak bisa connect ke exception:
                        sock.setKeepAlive(true);
                        sock.setTcpNoDelay(true);
                        try{ //.apm:20220325:ref hrn;
                            if (fSocketChannel.getSocketSendBufferSize() > 0){
                                sock.setSendBufferSize(fSocketChannel.getSocketSendBufferSize());
                                System.err.println("ITMSoupBinTCPBridgeSocketCtl sock.bufferSendSize = " + sock.getSendBufferSize());
                            }
                        }catch(SocketException ex0){
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                        }catch(Exception ex0){
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                        }
                        fSocketChannel.setSupplyAddress(zInputServerAddress);
                        fSocketChannel.setSupplyPort(iInputServerPort);
                        fSocketChannel.setChannelID(ITMSoupBinTCPBridgeSocketGeneral.getInstance.generateClientChannelID()); //.unique.
                        fSocketChannel.StartChannel(sock);
                        bOut = true;
                    }catch(IOException ex1){
                        fSocketChannel.raiseOnError(ex1);
                    }catch(Exception ex1){
                        fSocketChannel.raiseOnError(ex1);
                    }
                }else{
                    bOut = true;
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    //.protected synchronized
    protected synchronized boolean disconnect(){
        boolean bOut = false;
        try{
            if (fSocketChannel != null){
                fSocketChannel.StopChannel();
                bOut = fSocketChannel.isClosed();
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return bOut;
    }

}
