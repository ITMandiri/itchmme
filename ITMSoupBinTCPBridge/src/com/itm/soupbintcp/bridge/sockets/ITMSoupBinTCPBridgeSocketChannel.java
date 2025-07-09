/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.sockets;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupMgr;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import com.itm.soupbintcp.bridge.packetparser.ITMSoupBinTCPBridgePacketParser;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgLoginAcceptedPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgLoginRejectPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgLoginRequestPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgSequencedDataPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgServerHeartbeatPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgUnsequencedDataPacket;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeSocketChannel {
    //.informasi channel:
    private String fConnectionName                              = ""; //.nama koneksi.
    private String fSupplyAddress                               = ""; //.yg disupply dari user.
    private int fSupplyPort                                     = 0; //.yg disupply dari user.
    private String fChannelID                                   = ""; //.tetap   //<1="AsServer"/2="AsClient"><"SoupBinTCP">yyMMdd<0.000.000.000=counter[10]><00=random[2]>
    private Socket fSocket;
    //.config socket:
    private int fSocketSendBufferSize                           = 10000; //.apm:20220325:ref hrn;
    private boolean fSocketSendAutoFlush                        = true; //.apm:20220325:ref hrn;
    private int fSocketRecvBufferSize                           = 6400; //.apm:20220325:ref hrn;
    //.informasi queue:
    private ConcurrentLinkedQueue<byte[]> fQueue;
    //.informasi controller:
    private ITMSoupBinTCPBridgeSocketCtl fSocketController;
    //.informasi listener:
    private List<ITMSoupBinTCPBridgeSocketListener> fSocketListeners;
    //.informasi worker:
    private ITMSoupBinTCPBridgeChannelReaderWorker fReaderWorker;
    private ITMSoupBinTCPBridgeChannelWriterWorker fWriterWorker;
    //.informasi tanda akhir:
    private boolean fSocketWasWorking                           = false; //.socket pernah terpakai.
    private boolean fSocketAlreadyDisconnected                  = false; //.socket sudah pernah disconnect.
    private boolean fChannelAlreadyWasted                       = false; //.channel sudah tidak terpakai.
    //.informasi client heartbeat:
    private long fClientHeartBeatIdleIntervalMS                 = 2000;
    private boolean fClientHeartBeatEnabled                     = false;
    
    //.msgcompare & reconnect sequence mode:
    private boolean fCmpMsgExact                                    = false;
    private int fReconSeqMode                                       = 0;
    
    public ITMSoupBinTCPBridgeSocketChannel(String zConnectionName) {
        this.fConnectionName = zConnectionName;
        //.EXXX.
    }
    
    //.channel methods:
    
    public String getConnectionName() {
        return fConnectionName;
    }

    public void setConnectionName(String fConnectionName) {
        this.fConnectionName = fConnectionName;
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean isConnected(){
        boolean bOut = false;
        try{
            if (this.fSocket != null){
                //.apm:20200526:kenapa selalu connected?
                ///bOut = this.fSocket.isConnected();
                if (!this.fSocket.isClosed()){
                    bOut = this.fSocket.isConnected();
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return bOut;
    }

    public int getSocketSendBufferSize() {
        return fSocketSendBufferSize;
    }

    public void setSocketSendBufferSize(int fSocketSendBufferSize) {
        this.fSocketSendBufferSize = fSocketSendBufferSize;
    }

    public int getSocketRecvBufferSize() {
        return fSocketRecvBufferSize;
    }

    public void setSocketRecvBufferSize(int fSocketRecvBufferSize) {
        this.fSocketRecvBufferSize = fSocketRecvBufferSize;
    }

        
    public boolean isSocketSendAutoFlush() {
        return fSocketSendAutoFlush;
    }

    public void setSocketSendAutoFlush(boolean fSocketSendAutoFlush) {
        this.fSocketSendAutoFlush = fSocketSendAutoFlush;
    }
    
    public synchronized boolean sendMessageDirect(byte[] btMessageBytes){
        boolean bOut = false;
        try{
            if ((btMessageBytes != null) && (btMessageBytes.length > 0)){
                if (fSocket != null){
                    if (!fSocket.isClosed()){
                        OutputStream ost = fSocket.getOutputStream();
                        ost.write(btMessageBytes);
                        if (fSocketSendAutoFlush == true){
                            ost.flush();
                        }
                        raiseOnSent(btMessageBytes);
                        bOut = true;
                    }
                }
            }
        }catch(IOException ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
            raiseOnError(ex0);
            System.out.println("fSocket.isClosed()A = " + fSocket.isClosed());
            System.out.println("fSocket.isConnected()A = " + fSocket.isConnected());
            System.out.println("fSocket.isInputShutdown()A = " + fSocket.isInputShutdown());
            System.out.println("fSocket.isOutputShutdown()A = " + fSocket.isOutputShutdown());
            System.out.println("fSocket.fSocketSendBufferSize.A = " + fSocketSendBufferSize);
            System.out.println("fSocket.fSocketSendAutoFlush.A = " + fSocketSendAutoFlush);
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@stop channel TRY.A");
            //. hirin-2021-05-19 : berhentikan channel socket (anggap putus)
            StopChannel();
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@stop channel TRY.B");
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
            raiseOnError(ex0);
            System.out.println("fSocket.isClosed()B = " + fSocket.isClosed());
            System.out.println("fSocket.isConnected()B = " + fSocket.isConnected());
            System.out.println("fSocket.isInputShutdown()B = " + fSocket.isInputShutdown());
            System.out.println("fSocket.isOutputShutdown()B = " + fSocket.isOutputShutdown());
            System.out.println("fSocket.fSocketSendBufferSize.B = " + fSocketSendBufferSize);
            System.out.println("fSocket.fSocketSendAutoFlush.B = " + fSocketSendAutoFlush);
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@stop channel TRY.C");
            //. hirin-2021-05-19 : berhentikan channel socket (anggap putus)
            StopChannel();
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@stop channel TRY.D");
        }
        return bOut;
    }
    
    public synchronized boolean addSendQueue(byte[] btMessageBytes){
        boolean bOut = false;
        try{
            if ((btMessageBytes != null) && (btMessageBytes.length > 0)){
                if (this.fQueue == null) {
                    this.fQueue = new ConcurrentLinkedQueue<>();
                }
                this.fQueue.add(btMessageBytes);
                //.beri tanda worker:
                if (fWriterWorker != null){
                    fWriterWorker.RunWorker();
                }
                bOut = true;
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
            raiseOnError(ex0);
        }
        return bOut;
    }

    public ConcurrentLinkedQueue<byte[]> getfQueue() {
        return fQueue;
    }

    public void setfQueue(ConcurrentLinkedQueue<byte[]> fQueue) {
        this.fQueue = fQueue;
    }
    
    //.controller methods:
    public ITMSoupBinTCPBridgeSocketCtl getSocketController() {
        return fSocketController;
    }

    public void setSocketController(ITMSoupBinTCPBridgeSocketCtl fSocketController) {
        this.fSocketController = fSocketController;
    }
    
    //.listener methods:
    public List<ITMSoupBinTCPBridgeSocketListener> getSocketListeners() {
        return fSocketListeners;
    }
    
    public void setSocketListeners(List<ITMSoupBinTCPBridgeSocketListener> fSocketListeners)  {
        this.fSocketListeners = fSocketListeners;
    }
    
    public void addSocketListener(ITMSoupBinTCPBridgeSocketListener newListener){
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    public void removeSocketListener(ITMSoupBinTCPBridgeSocketListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            this.fSocketListeners.remove(oldListener);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnConnected(){
        try{
            //.jika autologin:
            if ((this.getSocketController() != null) && (this.getSocketController().isAuthAutoLogin())){
                
                this.getSocketController().setAuthLastAccepted(false);
                this.getSocketController().setAuthLastRejectReasonCode("");
                
                ITMSoupBinTCPMsgLoginRequestPacket mReqObjLogin = new ITMSoupBinTCPMsgLoginRequestPacket();
                mReqObjLogin.setUsername(this.getSocketController().getAuthUserName());
                mReqObjLogin.setPassword(this.getSocketController().getAuthUserPassword());
                mReqObjLogin.setRequestedSession(this.getSocketController().getAuthLastUserSession());
                mReqObjLogin.setRequestedSequenceNumber(this.getSocketController().getPreparedRecoveryExpectationSequencedNo());
                byte[] arbReqLogin = mReqObjLogin.buildPacket();
                
                System.out.println("ON_CONNECTED_AUTO_SEND_LOGIN_REQUEST: " + ITMSoupBinTCPBridgePacketFormat.convertBytesToHex(arbReqLogin));
                
                boolean bReqSent = this.sendMessageDirect(arbReqLogin);
                if (!bReqSent){
                    raiseOnError(new Exception("On Connected >> Cannot send autologin !"));
                }
                
            }
            //.lanjutan:
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSoupBinTCPBridgeSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSoupBinTCPBridgeSocketListener lstr = (ITMSoupBinTCPBridgeSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onConnected(this);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnDisconnected(){
        try{
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSoupBinTCPBridgeSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSoupBinTCPBridgeSocketListener lstr = (ITMSoupBinTCPBridgeSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onDisconnected(this);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnMessage(byte[] btMessageBytes){
        try{
            boolean bContinueBroadcast = false;
            long backupRecordNo = 0; //.sequencedmsg >= 0; selain itu = 0;
            //.object message dibuat 1 saja, jadi byref jika listener lebih dari 1 (walau seharusnya hanya 1):
            ITMSoupBinTCPMsgBase messageObject = ITMSoupBinTCPBridgePacketParser.getInstance.parsePacket(btMessageBytes);        
            if (this.getSocketController() != null){
                //.catat terakhir terima message dari server : note = jika memperlambat proses, cari cara lain
                this.getSocketController().setfLastServerHeartBeatRcvMs(System.currentTimeMillis());
            
                if (messageObject instanceof ITMSoupBinTCPMsgLoginAcceptedPacket){
                    this.getSocketController().setAuthLastAccepted(true);
                    this.getSocketController().setAuthLastRejectReasonCode("");
                    this.getSocketController().setAuthLastUserSession(((ITMSoupBinTCPMsgLoginAcceptedPacket) messageObject).getSession());
                    this.getSocketController().setRecoveryRealitySequencedNo(((ITMSoupBinTCPMsgLoginAcceptedPacket) messageObject).getSequenceNumber());
                    //.cek sequenceno:
                    if (this.getSocketController().getRecoveryRealitySequencedNo() != this.getSocketController().getRecoveryExpectationSequencedNo()){
                        raiseOnError(new Exception("FATAL: " + this.fConnectionName + " > On Login message >> expectation & reality SequencedNo not equal (Expectation=" + this.getSocketController().getRecoveryExpectationSequencedNo() + ", Reality=" + this.getSocketController().getRecoveryRealitySequencedNo() + ") !"));
                    }else{
                        bContinueBroadcast = true;
                    }
                    //.reset pewaktu terakhir terima heartbeat server:
                    this.getSocketController().setfLastServerHeartBeatRcvMs(System.currentTimeMillis());
                    //.backup:
                    ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsDUMPRecord(this.fConnectionName, false, backupRecordNo, btMessageBytes);
                }else if (messageObject instanceof ITMSoupBinTCPMsgLoginRejectPacket){
                    this.getSocketController().setAuthLastAccepted(false);
                    this.getSocketController().setAuthLastRejectReasonCode(((ITMSoupBinTCPMsgLoginRejectPacket) messageObject).getRejectReasonCode());
                    this.getSocketController().setRecoveryRealitySequencedNo(0);
                    bContinueBroadcast = true;
                    //.reset pewaktu terakhir terima heartbeat server:
                    this.getSocketController().setfLastServerHeartBeatRcvMs(System.currentTimeMillis());
                    //.backup:
                    ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsDUMPRecord(this.fConnectionName, false, backupRecordNo, btMessageBytes);
                }else if (messageObject instanceof ITMSoupBinTCPMsgSequencedDataPacket){
                    backupRecordNo = this.getSocketController().getRecoveryExpectationSequencedNo();
                    if (this.getSocketController().getRecoveryExpectationSequencedNo() == this.getSocketController().getCurrentSequencedNo()){
                        if (this.getSocketController().getRecentSequencedMsg() != null){
                            ITMSoupBinTCPMsgSequencedDataPacket mPrevMsg = ((ITMSoupBinTCPMsgSequencedDataPacket)this.getSocketController().getRecentSequencedMsg());
                            if (mPrevMsg.getSelfSequencedNo() == this.getSocketController().getRecoveryExpectationSequencedNo()){
                                //.compare with recent message before connection lost or restart application:
                                byte[] arbPrev = this.getSocketController().getRecentSequencedMsg().getPacketParseBytes();
                                byte[] arbNext = btMessageBytes;
                                boolean cmpEqual = ITMSoupBinTCPBridgePacketFormat.compareMessageIsEqual(arbPrev, arbNext, this.fCmpMsgExact);
                                if (cmpEqual){
                                    //.increment expectation sequencedno:
                                    this.getSocketController().setRecoveryExpectationSequencedNo(this.getSocketController().getRecoveryExpectationSequencedNo() + 1);
                                    //.backup atau tidak?
                                    //.... .
                                }else{
                                    raiseOnError(new Exception("FATAL: " + this.fConnectionName + " > On Sequenced message >> expectation & recent message SequencedNo equal but bytes not equal (Expectation=" + this.getSocketController().getRecoveryExpectationSequencedNo() + ", Recent=" + mPrevMsg.getSelfSequencedNo() + ", Current=" + this.getSocketController().getCurrentSequencedNo() + ", ExpectationHexMsg=" + ITMSoupBinTCPBridgePacketFormat.convertBytesToHex(arbPrev) + ", CurrentHexMsg=" + ITMSoupBinTCPBridgePacketFormat.convertBytesToHex(arbNext) + ") !"));
                                }
                            }else{
                                raiseOnError(new Exception("FATAL: " + this.fConnectionName + " > On Sequenced message >> expectation & recent message SequencedNo not equal (Expectation=" + this.getSocketController().getRecoveryExpectationSequencedNo() + ", Recent=" + mPrevMsg.getSelfSequencedNo() + ", Current=" + this.getSocketController().getCurrentSequencedNo() + ") !"));
                            }
                        }else{
                            raiseOnError(new Exception("FATAL: " + this.fConnectionName + " > On Sequenced message >> expectation & current SequencedNo equal but no recent sequenced message (Expectation=" + this.getSocketController().getRecoveryExpectationSequencedNo() + ", Current=" + this.getSocketController().getCurrentSequencedNo() + ") !"));
                        }
                    }else{
                        //.harusnya yg masuk blok ini adalah:
                        //.a=expectation > current :: benar;
                        //.b=expectation < current :: salah;
                        if (this.getSocketController().getRecoveryExpectationSequencedNo() > this.getSocketController().getCurrentSequencedNo()){
                            if ((this.getSocketController().getRecoveryExpectationSequencedNo() - this.getSocketController().getCurrentSequencedNo()) == 1){
                                //.urut sequencedno:
                                this.getSocketController().setCurrentSequencedNo(this.getSocketController().getCurrentSequencedNo() + 1);
                                this.getSocketController().setRecoveryExpectationSequencedNo(this.getSocketController().getRecoveryExpectationSequencedNo() + 1);
                                ((ITMSoupBinTCPMsgSequencedDataPacket) messageObject).setSelfSequencedNo(this.getSocketController().getCurrentSequencedNo());
                                this.getSocketController().setRecentSequencedMsg(messageObject);
                                bContinueBroadcast = true;
                                //.backup:
                                ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsRECVSequencedRecord(this.fConnectionName, backupRecordNo, btMessageBytes);
                            }else{
                                //.lompat sequencedno:
                                raiseOnError(new Exception("FATAL: " + this.fConnectionName + " > On Sequenced message >> expectation message SequencedNo jump more than 1 current message SequencedNo (Expectation=" + this.getSocketController().getRecoveryExpectationSequencedNo() + ", Current=" + this.getSocketController().getCurrentSequencedNo() + ") !"));
                            }
                        }else{
                            raiseOnError(new Exception("FATAL: " + this.fConnectionName + " > On Sequenced message >> expectation message SequencedNo less than current message SequencedNo (Expectation=" + this.getSocketController().getRecoveryExpectationSequencedNo() + ", Current=" + this.getSocketController().getCurrentSequencedNo() + ") !"));
                        }
                    }
                    //.backup:
                    ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsDUMPRecord(this.fConnectionName, false, backupRecordNo, btMessageBytes);
                }else if (messageObject instanceof ITMSoupBinTCPMsgUnsequencedDataPacket){
                    bContinueBroadcast = true;
                    //.backup:
                    ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsRECVUnsequencedRecord(this.fConnectionName, backupRecordNo, btMessageBytes);
                    ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsDUMPRecord(this.fConnectionName, false, backupRecordNo, btMessageBytes);
                }else if (messageObject instanceof ITMSoupBinTCPMsgServerHeartbeatPacket){
                    bContinueBroadcast = true;
                    //.catat terakhir terima heartbeat server
                    this.getSocketController().setfLastServerHeartBeatRcvMs(System.currentTimeMillis());
                    //.backup:
                    ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsDUMPRecord(this.fConnectionName, false, backupRecordNo, btMessageBytes);
                }else{
                    //.backup:
                    ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsDUMPRecord(this.fConnectionName, false, backupRecordNo, btMessageBytes);
                }
            }
            //.lanjutan:
            if (bContinueBroadcast){
                if (this.fSocketListeners.size() > 0){
                    for (Iterator<ITMSoupBinTCPBridgeSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                        ITMSoupBinTCPBridgeSocketListener lstr = (ITMSoupBinTCPBridgeSocketListener)iterSockListener.next();
                        if (lstr != null){
                            lstr.onMessage(this, btMessageBytes, messageObject);
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    private void raiseOnSent(byte[] btMessageBytes){
        try{
            long backupRecordNo = 0; //.sequencedmsg >= 0; selain itu = 0;
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSoupBinTCPBridgeSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSoupBinTCPBridgeSocketListener lstr = (ITMSoupBinTCPBridgeSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onSent(this, btMessageBytes);
                    }
                }
            }
            //.backup:
            ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsSENDRecord(this.fConnectionName, backupRecordNo, btMessageBytes);
            ITMSoupBinTCPBridgeBackupMgr.getInstance.saveAsDUMPRecord(this.fConnectionName, true, backupRecordNo, btMessageBytes);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    public void raiseOnError(Exception exception){
        try{
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSoupBinTCPBridgeSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSoupBinTCPBridgeSocketListener lstr = (ITMSoupBinTCPBridgeSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onError(this, exception);
                    }
                }
            }
            //.check console:
            System.out.println("@raiseOnError: Msg=" + exception.getMessage());
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    public void raiseOnTimeToHeartBeat(){
        try{
            if (this.fSocketListeners.size() > 0){
                for (Iterator<ITMSoupBinTCPBridgeSocketListener> iterSockListener = this.fSocketListeners.iterator(); iterSockListener.hasNext(); ){
                    ITMSoupBinTCPBridgeSocketListener lstr = (ITMSoupBinTCPBridgeSocketListener)iterSockListener.next();
                    if (lstr != null){
                        lstr.onTimeToHeartBeat(this);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
    }
    
    //.flags method:
    public boolean isChannelAlreadyWasted() {
        return fChannelAlreadyWasted;
    }
    
    public boolean isSocketWasWorking() {
        return fSocketWasWorking;
    }
    
    //.informasi client heartbeat:
    public long getClientHeartBeatIdleIntervalMS() {
        return fClientHeartBeatIdleIntervalMS;
    }

    public void setClientHeartBeatIdleIntervalMS(long fClientHeartBeatIdleIntervalMS) {
        this.fClientHeartBeatIdleIntervalMS = fClientHeartBeatIdleIntervalMS;
    }
    
    public boolean isClientHeartBeatEnabled() {
        return fClientHeartBeatEnabled;
    }

    public void setClientHeartBeatEnabled(boolean fClientHeartBeatEnabled) {
        this.fClientHeartBeatEnabled = fClientHeartBeatEnabled;
    }
    
    //.informasi msgcompare & reconnect sequence mode:
    public boolean isCmpMsgExact() {
        return fCmpMsgExact;
    }

    public void setCmpMsgExact(boolean fCmpMsgExact) {
        this.fCmpMsgExact = fCmpMsgExact;
    }
    
    public int getReconSeqMode() {
        return fReconSeqMode;
    }

    public void setReconSeqMode(int fReconSeqMode) {
        this.fReconSeqMode = fReconSeqMode;
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
                        fReaderWorker = new ITMSoupBinTCPBridgeChannelReaderWorker();
                    }
                    if (fWriterWorker == null) {
                        fWriterWorker = new ITMSoupBinTCPBridgeChannelWriterWorker();
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
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                raiseOnError(ex0);
            }
            fChannelAlreadyWasted = true;
            if (!fSocketAlreadyDisconnected){
                raiseOnDisconnected();
                fSocketAlreadyDisconnected = true;
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
            raiseOnError(ex0);
        }
    }
    
    
    //.worker inner class:
    private class ITMSoupBinTCPBridgeChannelReaderWorker implements Runnable{
        private Thread thRdWorker;
        private AtomicBoolean bRdWorkerMustRun                  = new AtomicBoolean(false);
        private DataInputStream mTopBuffReader                  = null;
        
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
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
                                mTopBuffReader = new DataInputStream(fSocket.getInputStream());
                            }catch(IOException ex0){
                                //.EXXX.
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                                raiseOnError(ex0);
                            }catch(Exception ex0){
                                //.EXXX.
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                                raiseOnError(ex0);
                            }
                            this.thRdWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                            this.bRdWorkerMustRun.set(true);
                            this.thRdWorker.setPriority(Thread.NORM_PRIORITY);
                            this.thRdWorker.setName(fConnectionName + "-R");
                            this.thRdWorker.start();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                raiseOnError(ex0);
            }
        }
        
        @Override
        public void run() {
            DataInputStream mReader = mTopBuffReader;
            
            while (this.bRdWorkerMustRun.get()){
                if (fSocket != null){
//                    try {
//                        if (getSocketRecvBufferSize() > 0){
//                            fSocket.setReceiveBufferSize(getSocketRecvBufferSize());
//                            System.out.println("fSocket.setReceiveBufferSize = " + getSocketRecvBufferSize());
//                        }
//                        
//                    } catch (SocketException ex) {
//                        Logger.getLogger(ITMSoupBinTCPBridgeSocketChannel.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    if (!fSocket.isClosed()){
                        try{
                            //.proses [read] here:
                            if (mReader != null && mReader.available() >= 2) {
                                int vPacketBytesLength = mReader.readUnsignedShort(); //.baca 2 bytes soupbintcp sebagai panjang message;
                                if (vPacketBytesLength > 0){
                                    byte[] btBytesBuffer = new byte[vPacketBytesLength];
                                    mReader.readFully(btBytesBuffer);
                                    byte[] btBytesResult = btBytesBuffer;
                                    if (btBytesResult == null){
                                        raiseOnError(new Exception("Object 'btBytesResult' null not allowed !"));
                                    }else if (btBytesResult.length == vPacketBytesLength){
                                        btBytesResult = ITMSoupBinTCPBridgePacketFormat.bufferAppend(ITMSoupBinTCPBridgePacketFormat.encodeInteger(vPacketBytesLength, 2), btBytesResult);
                                        raiseOnMessage(btBytesResult);
                                    }else{
                                        raiseOnError(new Exception("Empty packet read length result: Expectation=" + vPacketBytesLength + ", Reality=" + btBytesResult.length + " !"));
                                    }
                                }else{
                                    raiseOnError(new Exception("Negative or zero packet length (" + vPacketBytesLength + ") not allowed, Connection will be disconnect !"));
                                    
                                    //. hirin-2021-05-19 : jika dapat byte 00, akan di lakukan disconnect
                                    break;
                                }
                            }
                        }catch(EOFException ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                            //.berhenti:
                            break;
                        }catch(IOException ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                            //.berhenti:
                            break;
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                            //.berhenti:
                            break;
                        }
                    }
                }
            }
            //.info worker selesai:
            //.EXXX.
            //.#worker selesai:
            //.EXXX.
            StopChannel();
        }
        
    }
    
    private class ITMSoupBinTCPBridgeChannelWriterWorker implements Runnable{
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                        }
                    }else{
                        try{
                            this.notify();
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                        }
                    }
                }
            }catch(Exception ex0){
                //.EXXX.
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                raiseOnError(ex0);
            }
        }
        
        @Override
        public void run() {
            ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat RUN!");
            long tHeartBeatNextCheck = System.currentTimeMillis();
            long tHeartBeatPrevCheck = tHeartBeatNextCheck;
            int cHeartBeatIncrementCheck = 0;
            while (this.bWrWorkerMustRun.get()){
                if (fSocket != null){
                    if (!fSocket.isClosed()){
                        try{
                            tHeartBeatNextCheck = System.currentTimeMillis();
                            cHeartBeatIncrementCheck = 0;
                            //.proses [write] here:
                            if (fQueue != null){
                                while (fQueue.size() > 0){
                                    if (!this.bWrWorkerMustRun.get()){
                                        break;
                                    }
                                    byte[] btGetBytes = fQueue.peek();
                                    byte[] btGetRef = {};
                                    if ((btGetBytes == null) || (btGetBytes.length <= 0)){
                                        btGetRef = fQueue.poll();
                                    }else{
                                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@queue SEND.A");
                                        if (sendMessageDirect(btGetBytes)){
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@queue SEND.B");
                                            btGetRef = fQueue.poll();
                                            cHeartBeatIncrementCheck++;
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@queue SEND.C");
                                        }else{
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@queue SEND.D");
                                        }
                                    }
                                }
                            }
                            if (cHeartBeatIncrementCheck > 0){
                                tHeartBeatNextCheck = System.currentTimeMillis();
                                tHeartBeatPrevCheck = tHeartBeatNextCheck;
                            }
                            if (Math.abs(tHeartBeatNextCheck - tHeartBeatPrevCheck) > getClientHeartBeatIdleIntervalMS()){
                                tHeartBeatPrevCheck = tHeartBeatNextCheck;
                                ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat TIME_TO_SEND.A");
                                //.check last received server heartbeat:
                                if ((ITMSoupBinTCPBridgeSocketGeneral.getInstance.getiHeartbeatTimeout() > 0)
                                    && (getSocketController().getfLastServerHeartBeatRcvMs() > 0) 
                                    && (tHeartBeatNextCheck > getSocketController().getfLastServerHeartBeatRcvMs())
                                    && ((tHeartBeatNextCheck - getSocketController().getfLastServerHeartBeatRcvMs()) > ITMSoupBinTCPBridgeSocketGeneral.getInstance.getiHeartbeatTimeout())){
                                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, "Need To Stop Socket Channel by Server HeartBeat TimeOut : Setting = " + ITMSoupBinTCPBridgeSocketGeneral.getInstance.getiHeartbeatTimeout() + ", Current = " + (tHeartBeatNextCheck - getSocketController().getfLastServerHeartBeatRcvMs()));
                                    break;
                                }
                                //.raise event client heartbeat:
                                raiseOnTimeToHeartBeat();
                                ////////ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.WARNING, "@heartbeat TIME_TO_SEND.B");
                            }
                            //.#istirahat dulu sebentar :)
                            synchronized(this){
                                wait(1);
                            }
                        }catch(InterruptedException ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
                            raiseOnError(ex0);
                            //.berhenti:
                            break;
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
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
