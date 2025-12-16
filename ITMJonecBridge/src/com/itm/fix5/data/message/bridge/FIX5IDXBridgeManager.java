/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.message.bridge;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.admin.FIX5JonecDoReqAdministration;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderMassStatusRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTradeCaptureReportRequest;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController.FIX5IDXGroupMessageType;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController.FIX5IDXSubGroupMessageType;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5IDXBridgeManager {
    
    public final static FIX5IDXBridgeManager getInstance = new FIX5IDXBridgeManager();
    
    private final ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5ConnLines = new ConcurrentHashMap<>(); //.penampung list koneksi / line fix5.
    
    private int iNextUseActiveJatsJonecLineIndex                                = 0;
    
    public FIX5IDXBridgeManager() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.FIX5, logLevel.INIT, "");
    }
    
    public boolean buildAndInsertFIX5ConnLine(String zInputConnectionName, FIX5IDXGroupMessageType eInputConnType, 
                                                int vConnectionOrderNo,
                                                String zInputServerAddress, int iInputServerPort, 
                                                int iInputTryConnectTimeOut, int iInputCheckInterval, 
                                                FIX5IDXBridgeListener mInputBridgeListener, boolean bRejectUpdate,
                                                String zConnectorCode,
                                                String zTraderCode,
                                                String zPassword1,
                                                String zPassword2,
                                                boolean bHeartBeat,
                                                boolean bCmpMsgExact,
                                                int iReconSeqMode,
                                                boolean bCalcHeader,
                                                boolean bAutoSelect,
                                                FIX5IDXBridgeController.FIX5IDXSubGroupMessageType bFororder
                                                ){
        boolean bOut = false;
        try{
            if ((zInputConnectionName != null) && (zInputConnectionName.length() > 0)){
                FIX5IDXBridgeController mTmpConnLine = new FIX5IDXBridgeController(zInputConnectionName);
                mTmpConnLine.setConnectionName(zInputConnectionName);
                //... .
                mTmpConnLine.setMsgGroupType(eInputConnType);
                mTmpConnLine.addBridgeListener(mInputBridgeListener);
                mTmpConnLine.setRefServerAddress(zInputServerAddress);
                mTmpConnLine.setRefServerPort(iInputServerPort);
                mTmpConnLine.setRefTryConnectTimeOut(iInputTryConnectTimeOut);
                mTmpConnLine.setRefCheckInterval(iInputCheckInterval);
                //... .
                mTmpConnLine.setConnectorCode(zConnectorCode);
                mTmpConnLine.setTraderCode(zTraderCode);
                mTmpConnLine.setPassword1(zPassword1);
                mTmpConnLine.setPassword2(zPassword2);
                mTmpConnLine.setHeartBeat(bHeartBeat);
                mTmpConnLine.setCmpMsgExact(bCmpMsgExact);
                mTmpConnLine.setReconSeqMode(iReconSeqMode);
                mTmpConnLine.setCalcHeader(bCalcHeader);
                mTmpConnLine.setAutoSelect(bAutoSelect);
                mTmpConnLine.setMsgSubGroupType(bFororder); //.pembeda antara order entry dengan drop copy
                
                //... .
                bOut = insertOrUpdateJatsConnLine(mTmpConnLine, bRejectUpdate);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean insertOrUpdateJatsConnLine(FIX5IDXBridgeController mInputConnLine, boolean bRejectUpdate){
        boolean bOut = false;
        try{
            if (mInputConnLine != null){
                if ((mInputConnLine.getConnectionName()!= null) && (mInputConnLine.getConnectionName().length() > 0)){
                    if (this.chmFIX5ConnLines.containsKey(mInputConnLine.getConnectionName())){
                        if (!bRejectUpdate){
                            //.disconnect yg lama:
                            disconnectFIX5ConnLine(mInputConnLine.getConnectionName(), true);
                            //.replace yg baru:
                            bOut = (this.chmFIX5ConnLines.put(mInputConnLine.getConnectionName(), mInputConnLine) != null);
                        }
                    }else{
                        this.chmFIX5ConnLines.put(mInputConnLine.getConnectionName(), mInputConnLine);
                        bOut = this.chmFIX5ConnLines.containsKey(mInputConnLine.getConnectionName());
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean isJatsConnLineAvailable(String zInputConnectionName){
        boolean bOut = false;
        try{
            if ((zInputConnectionName != null) && (zInputConnectionName.length() > 0)){
                bOut = this.chmFIX5ConnLines.containsKey(zInputConnectionName);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public ConcurrentHashMap<String, FIX5IDXBridgeController> getChmFIX5ConnLines() {
        return this.chmFIX5ConnLines;
    }
    
    public FIX5IDXBridgeController getFIX5ConnLine(String zInputConnectionName){
        FIX5IDXBridgeController mOut = null;
        try{
            if ((zInputConnectionName != null) && (zInputConnectionName.length() > 0)){
                mOut = this.chmFIX5ConnLines.get(zInputConnectionName);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public void subscribeDropCopy() {
        String randomRequestID = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 6);

        FIX5JonecDataTradeCaptureReportRequest mFixTCRRequestMsg;
        String zFixTCRRequestMsg;
        FIX5JonecDataOrderMassStatusRequest mFixOMSRequestMsg;
        String zFixOMSRequestMsg;
        FIX5IDXBridgeController mTrxCtl = FIX5IDXBridgeManager.getInstance.getNextActiveFIX5JonecLineDropCopy();
        if (mTrxCtl != null){
            mFixTCRRequestMsg = new FIX5JonecDataTradeCaptureReportRequest(new HashMap());
            mFixTCRRequestMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.TRADE_CAPTURE_REPORT_REQUEST);
            mFixTCRRequestMsg.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
            mFixTCRRequestMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5LocalFormatDetail());
            mFixTCRRequestMsg.setfSenderSubID(mTrxCtl.getTraderCode());

            mFixTCRRequestMsg.setfLastMsgSeqNumProcessed("0");
            mFixTCRRequestMsg.setfTradeRequestID(randomRequestID);
            mFixTCRRequestMsg.setfTradeRequestType(FIX5JonecDataConst.FIX5JonecFieldValue.TRADE_REQUEST_TYPE_ALL_TRADES);
            mFixTCRRequestMsg.setfSubscriptionRequestType(FIX5JonecDataConst.FIX5JonecFieldValue.SUBSCRIPTION_REQUEST_TYPE_SUBSCRIBE);
            mFixTCRRequestMsg.setfNoPartyIDs("1");
            mFixTCRRequestMsg.setfPartyID(FIX5JonecDataConst.FIX5JonecFieldValue.SENDER_COMP_ID);
            mFixTCRRequestMsg.setfPartyIDSource(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
            mFixTCRRequestMsg.setfPartyRole(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM);

            zFixTCRRequestMsg = mFixTCRRequestMsg.msgToString();
            zFixTCRRequestMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zFixTCRRequestMsg,true,true,mTrxCtl.getConnectionName());

            if (!mTrxCtl.sendMessageDirect(zFixTCRRequestMsg)){
                //.???:
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send");
            }

            mFixOMSRequestMsg = new FIX5JonecDataOrderMassStatusRequest(new HashMap());
            mFixOMSRequestMsg.setfMsgType(FIX5JonecDataConst.FIX5JonecMsgType.ORDER_MASS_STATUS_REQUEST);
            mFixOMSRequestMsg.setfMsgSeqNum(mTrxCtl.getNextTXSequencedNo());
            mFixOMSRequestMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5LocalFormatDetail());
            mFixOMSRequestMsg.setfSenderSubID(mTrxCtl.getTraderCode());

            mFixOMSRequestMsg.setfLastMsgSeqNumProcessed("0");
            mFixOMSRequestMsg.setfMassStatusReqID(randomRequestID);
            mFixOMSRequestMsg.setfMassStatusReqType(FIX5JonecDataConst.FIX5JonecFieldValue.MASS_STATUS_REQ_TYPE_SUBSCRIBE);
            mFixOMSRequestMsg.setfNoPartyIDs("1");
            mFixOMSRequestMsg.setfPartyID(FIX5JonecDataConst.FIX5JonecFieldValue.SENDER_COMP_ID);
            mFixOMSRequestMsg.setfPartyIDSource(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ID_SOURCE_PARTICIPANT_IDENTIFIER_NEW);
            mFixOMSRequestMsg.setfPartyRole(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM);

            zFixOMSRequestMsg = mFixOMSRequestMsg.msgToString();
            zFixOMSRequestMsg = FIX5CheckSumHelper.repackMessageWithChecksum(zFixOMSRequestMsg,true,true,mTrxCtl.getConnectionName());

            if (!mTrxCtl.sendMessageDirect(zFixOMSRequestMsg)){
                //.???:
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, "No route @cannot send");
            }
        }
    }
    
    public boolean connectFIX5ConnLine(String zInputConnectionName){
        boolean bOut = false;
        try{
            if ((zInputConnectionName != null) && (zInputConnectionName.length() > 0)){
                FIX5IDXBridgeController mTmpLine = this.chmFIX5ConnLines.get(zInputConnectionName);
                if (mTmpLine != null){
                    bOut = mTmpLine.startConnection();
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean disconnectFIX5ConnLine(String zInputConnectionName, boolean bWithMessage){
        boolean bOut = false;
        try{
            if ((zInputConnectionName != null) && (zInputConnectionName.length() > 0)){
                FIX5IDXBridgeController mTmpLine = this.chmFIX5ConnLines.get(zInputConnectionName);
                if (mTmpLine != null){
                    if (bWithMessage){
                        //.ini fungsi halus disconnect setelah logout:
                        FIX5JonecDoReqAdministration.getInstance.doLogout(mTmpLine);
                        bOut = true;
                    }else{
                        bOut = mTmpLine.stopConnection();
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public int countActiveFIX5ConnLinesToJonec(){ //.x;
        int iOut = 0;
        try{
            if (!this.chmFIX5ConnLines.isEmpty()){
                ///Set<String> setJatsLineKeys = this.chmFIX5ConnLines.keySet();
                List<String> setJatsLineKeys = new ArrayList<>();
                for(FIX5IDXBridgeController mEachCtl : this.chmFIX5ConnLines.values()){
                    if (mEachCtl != null){
                        setJatsLineKeys.add(mEachCtl.getConnectionName());
                    }
                }
                ///if (setJatsLineKeys != null){
                    for (String zJatsLineKey : setJatsLineKeys) {
                        if (!StringHelper.isNullOrEmpty(zJatsLineKey)){
                            FIX5IDXBridgeController mTmpBridge = getFIX5ConnLine(zJatsLineKey);
                            if ((mTmpBridge != null) && (mTmpBridge.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE)){
                                if (mTmpBridge.isConnected()){
                                    iOut++;
                                }
                            }
                        }
                    }
                ///}
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return iOut;
    }
    
    public boolean connectAllFIX5ConnLinesToJonec(){
        boolean bOut = false;
        try{
            if (!this.chmFIX5ConnLines.isEmpty()){
                ///Set<String> setJatsLineKeys = this.chmFIX5ConnLines.keySet();
                List<String> setJatsLineKeys = new ArrayList<>();
                for(FIX5IDXBridgeController mEachCtl : this.chmFIX5ConnLines.values()){
                    if (mEachCtl != null){
                        setJatsLineKeys.add(mEachCtl.getConnectionName());
                    }
                }
                ///if (setJatsLineKeys != null){
                    for (String zJatsLineKey : setJatsLineKeys) {
                        if (!StringHelper.isNullOrEmpty(zJatsLineKey)){
                            FIX5IDXBridgeController mTmpBridge = getFIX5ConnLine(zJatsLineKey);
                            if ((mTmpBridge != null) && (mTmpBridge.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE)){
                                if (connectFIX5ConnLine(zJatsLineKey)){
                                    bOut = true; //.paling tidak ada satu yg aktif.
                                }
                            }
                        }
                    }
                ///}
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean stopAllConnections(){
        boolean bOut = false;
        try{
            if (!this.chmFIX5ConnLines.isEmpty()){
                ///Set<String> setJatsLineKeys = this.chmFIX5ConnLines.keySet();
                List<String> setJatsLineKeys = new ArrayList<>();
                for(FIX5IDXBridgeController mEachCtl : this.chmFIX5ConnLines.values()){
                    if (mEachCtl != null){
                        setJatsLineKeys.add(mEachCtl.getConnectionName());
                    }
                }
                ///if (setJatsLineKeys != null){
                    for (String zJatsLineKey : setJatsLineKeys) {
                        if (!StringHelper.isNullOrEmpty(zJatsLineKey)){
                            FIX5IDXBridgeController mTmpBridge = getFIX5ConnLine(zJatsLineKey);
                            if (mTmpBridge != null){
                                if (mTmpBridge.stopConnection()){
                                    bOut = true; //.paling tidak ada satu yg aktif.
                                }
                            }
                        }
                    }
                ///}
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean logoutAllConnections(){
        boolean bOut = false;
        try{
            if (!this.chmFIX5ConnLines.isEmpty()){
                Set<String> setJatsLineKeys = this.chmFIX5ConnLines.keySet();
                if (setJatsLineKeys != null){
                    for (String zJatsLineKey : setJatsLineKeys) {
                        if (!StringHelper.isNullOrEmpty(zJatsLineKey)){
                            FIX5IDXBridgeController mTmpBridge = getFIX5ConnLine(zJatsLineKey);
                            if (mTmpBridge != null){
                                //.ada di daftar act koneksi (controller):
                                //.ini fungsi halus disconnect setelah logout:
                                FIX5JonecDoReqAdministration.getInstance.doLogout(mTmpBridge);
                                bOut = true;
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public boolean connectAllFIX5ConnLines(){
        boolean bOut = false;
        try{
            if (!this.chmFIX5ConnLines.isEmpty()){
                if (connectAllFIX5ConnLinesToJonec()){
                    bOut = true; //.paling tidak ada satu yg aktif.
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    
    public FIX5IDXBridgeController getNextActiveFIX5JonecLine() { //.random berdasar hash.
        FIX5IDXBridgeController mOut = null;
        try{
            if (!this.chmFIX5ConnLines.isEmpty()){
                int cTmpMaxConnIndex = 0;
                int cTmpCurConnIndex = 0;
                List<String> lstConnNames = new ArrayList<>();
                //.hitung total jalur jonec:
                try{
                    for(FIX5IDXBridgeController mTmpConnLine : this.chmFIX5ConnLines.values()){
                        if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.getMsgSubGroupType() == FIX5IDXSubGroupMessageType.FIX5_ORDER_ENTRY) && (mTmpConnLine.isConnected())){
                            cTmpMaxConnIndex++;
                            lstConnNames.add(mTmpConnLine.getConnectionName());
                        }
                    }
                    /***
                    Set<String> setConnLineKeys = this.chmFIX5ConnLines.keySet();
                    for (String zTmpLineKey : setConnLineKeys) {
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.isConnected())){
                                cTmpMaxConnIndex++;
                            }
                        }
                    }
                    ***/
                }catch(Exception ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
                }
                //.cari tahu jalur berikutnya yg bisa dipakai:
                if (cTmpMaxConnIndex > 0){
                    if (this.iNextUseActiveJatsJonecLineIndex >= cTmpMaxConnIndex){
                        this.iNextUseActiveJatsJonecLineIndex = 0; //.kembali ke awal.
                    }
                    FIX5IDXBridgeController mTmpRsvConnLine = null;
                    Object[] arrConnLineKeys = lstConnNames.toArray();
                    Arrays.sort(arrConnLineKeys); //.diurutkan dulu.
                    for (Object oTmpLineKey : arrConnLineKeys){
                        String zTmpLineKey = (String)oTmpLineKey;
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.getMsgSubGroupType() == FIX5IDXSubGroupMessageType.FIX5_ORDER_ENTRY) && (mTmpConnLine.isConnected())){
                                mTmpRsvConnLine = mTmpConnLine; //.buat reservasi jika tidak dapat jalur terakhir.
                                if ((this.iNextUseActiveJatsJonecLineIndex > cTmpCurConnIndex)){
                                    cTmpCurConnIndex++;
                                }else{
                                    mOut = mTmpConnLine;
                                    this.iNextUseActiveJatsJonecLineIndex++;
                                    break;
                                }
                            }
                        }
                    }
                    /***
                    Object[] arrConnLineKeys = this.chmFIX5ConnLines.keySet().toArray();
                    Arrays.sort(arrConnLineKeys); //.diurutkan dulu.
                    for (Object oTmpLineKey : arrConnLineKeys){
                        String zTmpLineKey = (String)oTmpLineKey;
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.isConnected())){
                                mTmpRsvConnLine = mTmpConnLine; //.buat reservasi jika tidak dapat jalur terakhir.
                                if ((this.iNextUseActiveJatsJonecLineIndex > cTmpCurConnIndex)){
                                    cTmpCurConnIndex++;
                                }else{
                                    mOut = mTmpConnLine;
                                    this.iNextUseActiveJatsJonecLineIndex++;
                                    break;
                                }
                            }
                        }
                    }
                    ***/
                    if ((mOut == null) && (mTmpRsvConnLine != null)){
                        mOut = mTmpRsvConnLine;
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public FIX5IDXBridgeController getNextActiveFIX5JonecLineDropCopy() { //.random berdasar hash.
        FIX5IDXBridgeController mOut = null;
        try{
            if (!this.chmFIX5ConnLines.isEmpty()){
                int cTmpMaxConnIndex = 0;
                int cTmpCurConnIndex = 0;
                List<String> lstConnNames = new ArrayList<>();
                //.hitung total jalur jonec:
                try{
                    for(FIX5IDXBridgeController mTmpConnLine : this.chmFIX5ConnLines.values()){
                        if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.getMsgSubGroupType() == FIX5IDXSubGroupMessageType.FIX5_DROP_COPY) && (mTmpConnLine.isConnected())){
                            cTmpMaxConnIndex++;
                            lstConnNames.add(mTmpConnLine.getConnectionName());
                        }
                    }
                    /***
                    Set<String> setConnLineKeys = this.chmFIX5ConnLines.keySet();
                    for (String zTmpLineKey : setConnLineKeys) {
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.isConnected())){
                                cTmpMaxConnIndex++;
                            }
                        }
                    }
                    ***/
                }catch(Exception ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
                }
                //.cari tahu jalur berikutnya yg bisa dipakai:
                if (cTmpMaxConnIndex > 0){
                    if (this.iNextUseActiveJatsJonecLineIndex >= cTmpMaxConnIndex){
                        this.iNextUseActiveJatsJonecLineIndex = 0; //.kembali ke awal.
                    }
                    FIX5IDXBridgeController mTmpRsvConnLine = null;
                    Object[] arrConnLineKeys = lstConnNames.toArray();
                    Arrays.sort(arrConnLineKeys); //.diurutkan dulu.
                    for (Object oTmpLineKey : arrConnLineKeys){
                        String zTmpLineKey = (String)oTmpLineKey;
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.getMsgSubGroupType() == FIX5IDXSubGroupMessageType.FIX5_DROP_COPY) && (mTmpConnLine.isConnected())){
                                mTmpRsvConnLine = mTmpConnLine; //.buat reservasi jika tidak dapat jalur terakhir.
                                if ((this.iNextUseActiveJatsJonecLineIndex > cTmpCurConnIndex)){
                                    cTmpCurConnIndex++;
                                }else{
                                    mOut = mTmpConnLine;
                                    this.iNextUseActiveJatsJonecLineIndex++;
                                    break;
                                }
                            }
                        }
                    }
                    /***
                    Object[] arrConnLineKeys = this.chmFIX5ConnLines.keySet().toArray();
                    Arrays.sort(arrConnLineKeys); //.diurutkan dulu.
                    for (Object oTmpLineKey : arrConnLineKeys){
                        String zTmpLineKey = (String)oTmpLineKey;
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.isConnected())){
                                mTmpRsvConnLine = mTmpConnLine; //.buat reservasi jika tidak dapat jalur terakhir.
                                if ((this.iNextUseActiveJatsJonecLineIndex > cTmpCurConnIndex)){
                                    cTmpCurConnIndex++;
                                }else{
                                    mOut = mTmpConnLine;
                                    this.iNextUseActiveJatsJonecLineIndex++;
                                    break;
                                }
                            }
                        }
                    }
                    ***/
                    if ((mOut == null) && (mTmpRsvConnLine != null)){
                        mOut = mTmpRsvConnLine;
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public FIX5IDXBridgeController getNextActiveFIX5JonecLineAll() { //.random berdasar hash.
        FIX5IDXBridgeController mOut = null;
        try{
            if (!this.chmFIX5ConnLines.isEmpty()){
                int cTmpMaxConnIndex = 0;
                int cTmpCurConnIndex = 0;
                List<String> lstConnNames = new ArrayList<>();
                //.hitung total jalur jonec:
                try{
                    for(FIX5IDXBridgeController mTmpConnLine : this.chmFIX5ConnLines.values()){
                        if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.isConnected())){
                            cTmpMaxConnIndex++;
                            lstConnNames.add(mTmpConnLine.getConnectionName());
                        }
                    }
                    /***
                    Set<String> setConnLineKeys = this.chmFIX5ConnLines.keySet();
                    for (String zTmpLineKey : setConnLineKeys) {
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.isConnected())){
                                cTmpMaxConnIndex++;
                            }
                        }
                    }
                    ***/
                }catch(Exception ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
                }
                //.cari tahu jalur berikutnya yg bisa dipakai:
                if (cTmpMaxConnIndex > 0){
                    if (this.iNextUseActiveJatsJonecLineIndex >= cTmpMaxConnIndex){
                        this.iNextUseActiveJatsJonecLineIndex = 0; //.kembali ke awal.
                    }
                    FIX5IDXBridgeController mTmpRsvConnLine = null;
                    Object[] arrConnLineKeys = lstConnNames.toArray();
                    Arrays.sort(arrConnLineKeys); //.diurutkan dulu.
                    for (Object oTmpLineKey : arrConnLineKeys){
                        String zTmpLineKey = (String)oTmpLineKey;
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.isConnected())){
                                mTmpRsvConnLine = mTmpConnLine; //.buat reservasi jika tidak dapat jalur terakhir.
                                if ((this.iNextUseActiveJatsJonecLineIndex > cTmpCurConnIndex)){
                                    cTmpCurConnIndex++;
                                }else{
                                    mOut = mTmpConnLine;
                                    this.iNextUseActiveJatsJonecLineIndex++;
                                    break;
                                }
                            }
                        }
                    }
                    /***
                    Object[] arrConnLineKeys = this.chmFIX5ConnLines.keySet().toArray();
                    Arrays.sort(arrConnLineKeys); //.diurutkan dulu.
                    for (Object oTmpLineKey : arrConnLineKeys){
                        String zTmpLineKey = (String)oTmpLineKey;
                        if ((zTmpLineKey != null) && (zTmpLineKey.length() > 0)){
                            FIX5IDXBridgeController mTmpConnLine = this.chmFIX5ConnLines.get(zTmpLineKey);
                            if ((mTmpConnLine != null) && (mTmpConnLine.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE) && (mTmpConnLine.isConnected())){
                                mTmpRsvConnLine = mTmpConnLine; //.buat reservasi jika tidak dapat jalur terakhir.
                                if ((this.iNextUseActiveJatsJonecLineIndex > cTmpCurConnIndex)){
                                    cTmpCurConnIndex++;
                                }else{
                                    mOut = mTmpConnLine;
                                    this.iNextUseActiveJatsJonecLineIndex++;
                                    break;
                                }
                            }
                        }
                    }
                    ***/
                    if ((mOut == null) && (mTmpRsvConnLine != null)){
                        mOut = mTmpRsvConnLine;
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
