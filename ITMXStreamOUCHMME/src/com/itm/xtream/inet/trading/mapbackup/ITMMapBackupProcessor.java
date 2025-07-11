/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.mapbackup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForAdmin;
import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForFastRead;
import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForFastWrite;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataLiquidityProviderOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancel;
import com.itm.idx.data.ori.message.struct.ORIDataNewLiquidityProviderOrder;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderMassCancel;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimCalcQty;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author fredy
 */
public class ITMMapBackupProcessor implements Runnable {
    
    private final String zBackupCode;

    private Thread thOutWorker;
    private final AtomicBoolean bOutWorkerMustRun                       = new AtomicBoolean(false);
    
    //.penampung sementara log di memory:
    private final ConcurrentLinkedQueue<String> clqInMemoryMapLines = new ConcurrentLinkedQueue<>();
    
    private final ConcurrentHashMap oTargetMap;
    private final Class tTargetMapKeyType;
    private final Class tTargetMapValueType;
    
    public final static String DEFAULT_MAP_BACKUP_FILE_DIRECTORY        = "./mapbackups/";
    public final static String DEFAULT_MAP_BACKUP_FILE_PREFIX           = "map_";
    public final static String DEFAULT_MAP_BACKUP_FILE_EXTENSION        = ".txt";
    
    public ITMMapBackupProcessor(String zBackupCode, ConcurrentHashMap oTargetMap, Class tTargetMapKeyType, Class tTargetMapValueType){
        this.zBackupCode = zBackupCode;
        this.oTargetMap = oTargetMap;
        this.tTargetMapKeyType = tTargetMapKeyType;
        this.tTargetMapValueType = tTargetMapValueType;
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public String getBackupCode() {
        return zBackupCode;
    }
    
    public synchronized boolean clearMapFromFile(){
        boolean mOut = false;
        try{
            this.clqInMemoryMapLines.clear();
            //.hapus isi file:
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to restore map for:" + zBackupCode + " > err:" + ex0);
        }
        return mOut;
    }
    
    public synchronized boolean restoreMapFromFile(){
        boolean mOut = false;
        try{
            if (this.oTargetMap != null){
                //.load map from file:
                String szMapBackupFilePath = DEFAULT_MAP_BACKUP_FILE_DIRECTORY + DEFAULT_MAP_BACKUP_FILE_PREFIX + zBackupCode + DEFAULT_MAP_BACKUP_FILE_EXTENSION;
                File flMapBackupReloader = new File(szMapBackupFilePath);
                if (flMapBackupReloader.exists()){
                    if (flMapBackupReloader.isFile()){
                        long cLineCount = 0;
                        ITMFileAccessForFastRead mFileRdr = new ITMFileAccessForFastRead();
                        if (mFileRdr.openFileForReadLinesFromFirst(szMapBackupFilePath)){
                            try{
                                String zMapBackupLine;
                                while ((zMapBackupLine = mFileRdr.readLine()) != null){
                                    ///System.out.println(zMapBackupLine);
                                    if ((!StringHelper.isNullOrEmpty(zMapBackupLine)) && (zMapBackupLine.startsWith("M|")) && (zMapBackupLine.endsWith("|OK"))){
                                        zMapBackupLine = zMapBackupLine.substring(0, zMapBackupLine.length() - 3);
                                        String[] arrMapFields = StringHelper.splitLimit(zMapBackupLine, "|", 4);
                                        if ((arrMapFields != null) && (arrMapFields.length >= 4)){
                                            String zKeyStr = arrMapFields[1];
                                            String zValStr = arrMapFields[3];
                                            boolean bUseJSON = (arrMapFields[2].equals("1"));
                                            Object oKeyObj = null;
                                            Object oValObj = null;
                                            if (this.tTargetMapKeyType == Double.class){
                                                oKeyObj = StringHelper.toDouble(zKeyStr);
                                            }else if (this.tTargetMapKeyType == Long.class){
                                                oKeyObj = StringHelper.toLong(zKeyStr);
                                            }else if (this.tTargetMapKeyType == Integer.class){
                                                oKeyObj = StringHelper.toInt(zKeyStr);
                                            }else if (this.tTargetMapKeyType == String.class){
                                                oKeyObj = zKeyStr;
                                            }else{
                                                oKeyObj = zKeyStr;
                                            }
                                            if (bUseJSON){
                                                Gson mGson = new Gson();
                                                oValObj = mGson.fromJson(zValStr, this.tTargetMapValueType);
                                                if (oValObj instanceof  SheetOfJONECSimOriginRequest){
                                                    SheetOfJONECSimOriginRequest oo = (SheetOfJONECSimOriginRequest)oValObj;
                                                    Map<String, String> mField = oo.getIdxMessage().getMapMsgFields();
                                                    //. buat object sesuai spesifikasi class nya
                                                    if (mField != null && mField.containsKey(ORIDataConst.ORIFieldTag.MSGTYPE)){
                                                        String msgType = mField.get(ORIDataConst.ORIFieldTag.MSGTYPE);
                                                        if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.NEW_ORDER)){
                                                            if (mField.containsKey(ORIDataConst.ORIFieldTag.HANDLINST)){
                                                                String zHandlInst = mField.get(ORIDataConst.ORIFieldTag.HANDLINST);
                                                                if (zHandlInst.equalsIgnoreCase((ORIDataConst.ORIFieldValue.HANDLINST_NEGOTIATIONDEAL + ""))){
                                                                    ORIDataNegotiationDeal mNewObj = new ORIDataNegotiationDeal(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                                }else if (zHandlInst.equalsIgnoreCase((ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT + ""))){
                                                                    ORIDataNewOrder mNewObj = new ORIDataNewOrder(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                                }else{
                                                                    ORIDataNewOrder mNewObj = new ORIDataNewOrder(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                                }
                                                            }else{
                                                                ORIDataNewOrder mNewObj = new ORIDataNewOrder(mField);
                                                                mNewObj.assignMessage();
                                                                oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                            }
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.ORDER_CANCEL_REPLACE)){
                                                            ORIDataOrderAmend mNewObj = new ORIDataOrderAmend(mField);
                                                            mNewObj.assignMessage();
                                                            oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.ORDER_CANCEL_REQUEST)){
                                                            if (mField.containsKey(ORIDataConst.ORIFieldTag.ORDERQTY)){
                                                                String zOrderQty = mField.get(ORIDataConst.ORIFieldTag.ORDERQTY);
                                                                if (zOrderQty.equalsIgnoreCase(((int)ORIDataConst.ORIFieldValue.ORDERQTY_WITHDRAW_NEGDEAL + ""))){
                                                                    ORIDataNegotiationDealCancel mNewObj = new ORIDataNegotiationDealCancel(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                                }else{
                                                                    ORIDataOrderCancel mNewObj = new ORIDataOrderCancel(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                                }
                                                            }else{
                                                                ORIDataOrderCancel mNewObj = new ORIDataOrderCancel(mField);
                                                                mNewObj.assignMessage();
                                                                oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                            }
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.ORDER_MASS_CANCEL_REQUEST)){
                                                            ORIDataOrderMassCancel mNewObj = new ORIDataOrderMassCancel(mField);
                                                            mNewObj.assignMessage();
                                                            oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.QUOTE)){
                                                            ORIDataNewLiquidityProviderOrder mNewObj = new ORIDataNewLiquidityProviderOrder(mField);
                                                            mNewObj.assignMessage();
                                                            oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.QUOTE_CANCEL)){
                                                            ORIDataLiquidityProviderOrderCancel mNewObj = new ORIDataLiquidityProviderOrderCancel(mField);
                                                            mNewObj.assignMessage();
                                                            oValObj = new SheetOfJONECSimOriginRequest(oo.getOrderToken(), mNewObj);
                                                        }
                                                    }else{
                                                        oValObj = oValObj;
                                                    }
                                                    //. restore token
                                                    if (oo.getOrderToken() > BookOfJONECSimToken.getInstance.getLastTrxTokens()){
                                                        BookOfJONECSimToken.getInstance.setLastTrxTokens(oo.getOrderToken());
                                                    }
                                                }else if (oValObj instanceof  SheetOfJONECSimEveryRequest){
                                                    SheetOfJONECSimEveryRequest oo = (SheetOfJONECSimEveryRequest)oValObj;
                                                    Map<String, String> mField = oo.getIdxMessage().getMapMsgFields();
                                                    
                                                    //. buat object sesuai spesifikasi class nya
                                                    if (mField != null && mField.containsKey(ORIDataConst.ORIFieldTag.MSGTYPE)){
                                                        String msgType = mField.get(ORIDataConst.ORIFieldTag.MSGTYPE);
                                                        if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.NEW_ORDER)){
                                                            if (mField.containsKey(ORIDataConst.ORIFieldTag.HANDLINST)){
                                                                String zHandlInst = mField.get(ORIDataConst.ORIFieldTag.HANDLINST);
                                                                if (zHandlInst.equalsIgnoreCase((ORIDataConst.ORIFieldValue.HANDLINST_NEGOTIATIONDEAL + ""))){
                                                                    ORIDataNegotiationDeal mNewObj = new ORIDataNegotiationDeal(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                                }else if (zHandlInst.equalsIgnoreCase((ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT + ""))){
                                                                    ORIDataNewOrder mNewObj = new ORIDataNewOrder(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                                }else{
                                                                    ORIDataNewOrder mNewObj = new ORIDataNewOrder(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                                }
                                                            }else{
                                                                ORIDataNewOrder mNewObj = new ORIDataNewOrder(mField);
                                                                mNewObj.assignMessage();
                                                                oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                            }
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.ORDER_CANCEL_REPLACE)){
                                                            ORIDataOrderAmend mNewObj = new ORIDataOrderAmend(mField);
                                                            mNewObj.assignMessage();
                                                            oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.ORDER_CANCEL_REQUEST)){
                                                            if (mField.containsKey(ORIDataConst.ORIFieldTag.ORDERQTY)){
                                                                String zOrderQty = mField.get(ORIDataConst.ORIFieldTag.ORDERQTY);
                                                                if (zOrderQty.equalsIgnoreCase(((int)ORIDataConst.ORIFieldValue.ORDERQTY_WITHDRAW_NEGDEAL + ""))){
                                                                    ORIDataNegotiationDealCancel mNewObj = new ORIDataNegotiationDealCancel(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                                }else{
                                                                    ORIDataOrderCancel mNewObj = new ORIDataOrderCancel(mField);
                                                                    mNewObj.assignMessage();
                                                                    oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                                }
                                                            }else{
                                                                ORIDataOrderCancel mNewObj = new ORIDataOrderCancel(mField);
                                                                mNewObj.assignMessage();
                                                                oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                            }
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.ORDER_MASS_CANCEL_REQUEST)){
                                                            ORIDataOrderMassCancel mNewObj = new ORIDataOrderMassCancel(mField);
                                                            mNewObj.assignMessage();
                                                            oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.QUOTE)){
                                                            ORIDataNewLiquidityProviderOrder mNewObj = new ORIDataNewLiquidityProviderOrder(mField);
                                                            mNewObj.assignMessage();
                                                            oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                        }else if (msgType.equalsIgnoreCase(ORIDataConst.ORIMsgType.QUOTE_CANCEL)){
                                                            ORIDataLiquidityProviderOrderCancel mNewObj = new ORIDataLiquidityProviderOrderCancel(mField);
                                                            mNewObj.assignMessage();
                                                            oValObj = new SheetOfJONECSimEveryRequest(oo.getOrderToken(), mNewObj);
                                                        }
                                                    }
                                                    //. restore token
                                                    if (oo.getOrderToken() > BookOfJONECSimToken.getInstance.getLastTrxTokens()){
                                                        BookOfJONECSimToken.getInstance.setLastTrxTokens(oo.getOrderToken());
                                                    }
                                                }else if (oValObj instanceof  SheetOfJONECSimCalcQty){
                                                    SheetOfJONECSimCalcQty oo = (SheetOfJONECSimCalcQty)oValObj;
                                                    //. restore sequence
                                                    if (oo.getLastOuchSeq() > BookOfJONECSimToken.getInstance.getLastTrxSeqLatestSaved()){
                                                        BookOfJONECSimToken.getInstance.setLastTrxSeq(oo.getLastOuchSeq());
                                                    }
                                                }
                                            }else if ((!bUseJSON) && (this.tTargetMapValueType == Double.class)){
                                                oValObj = StringHelper.toDouble(zValStr);
                                            }else if ((!bUseJSON) && (this.tTargetMapValueType == Long.class)){
                                                oValObj = StringHelper.toLong(zValStr);
                                            }else if ((!bUseJSON) && (this.tTargetMapValueType == Integer.class)){
                                                oValObj = StringHelper.toInt(zValStr);
                                            }else if ((!bUseJSON) && (this.tTargetMapValueType == String.class)){
                                                oValObj = zValStr;
                                            }else{
                                                oValObj = zValStr;
                                            }
                                            if ((oKeyObj != null) && (oValObj != null)){
                                                this.oTargetMap.put(oKeyObj, oValObj);
                                                cLineCount++;
                                            }
                                        }
                                    }
                                }
                                if (cLineCount > 0){
                                    mOut = true;
                                }
                            }catch(JsonSyntaxException ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to json read restore map for:" + zBackupCode + " > err:" + ex0);
                            }catch(Exception ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to read restore map for:" + zBackupCode + " > err:" + ex0);
                            }
                            mFileRdr.closeFile();
                        }
                    }
                }
                //.reduce map file:
                //... .
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to restore map for:" + zBackupCode + " > err:" + ex0);
        }
        return mOut;
    }
    
    public synchronized boolean backupMapObjectToFile(Object mKey, Object mValue){
        boolean mOut = false;
        try{
            if ((mKey != null) && (mValue != null) && (this.oTargetMap != null)){
                boolean bUseJSON = false;
                String zKeyField = "";
                String zValueField = "";
                String zFinalLine = "";
                if (mKey instanceof Double){
                    zKeyField = StringHelper.fromDouble((double)mKey);
                }else if (mKey instanceof Long){
                    zKeyField = StringHelper.fromLong((long)mKey);
                }else if (mKey instanceof Integer){
                    zKeyField = StringHelper.fromInt((int)mKey);
                }else if (mKey instanceof String){
                    zKeyField = (String)mKey;
                }else{
                    zKeyField = mKey.toString();
                }
                if (mValue instanceof Double){
                    bUseJSON = false;
                    zValueField = StringHelper.fromDouble((double)mValue);
                }else if (mValue instanceof Long){
                    bUseJSON = false;
                    zValueField = StringHelper.fromLong((long)mValue);
                }else if (mValue instanceof Integer){
                    bUseJSON = false;
                    zValueField = StringHelper.fromInt((int)mValue);
                }else if (mValue instanceof String){
                    bUseJSON = false;
                    zValueField = (String)mValue;
                }else{
                    bUseJSON = true;
                    Gson mGson = new GsonBuilder().create();
                    zValueField = mGson.toJson(mValue, this.tTargetMapValueType);
                }
                zFinalLine = "M|" + zKeyField + "|" + ((bUseJSON ? "1" : "0") + "|" + zValueField + "|OK");
                if (!StringHelper.isNullOrEmpty(zFinalLine)){
                    this.clqInMemoryMapLines.add(zFinalLine);
                    mOut = true;
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to backup map for:" + zBackupCode + " > err:" + ex0);
        }
        return mOut;
    }
    
    public synchronized void startProcessor(){
        try{
            if (this.thOutWorker == null){
                this.thOutWorker = new Thread(this);
            }
            if (this.thOutWorker != null){
                if (!this.thOutWorker.isAlive()){
                    try{
                        this.thOutWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                        this.bOutWorkerMustRun.set(true);
                        this.thOutWorker.setPriority(Thread.NORM_PRIORITY);
                        this.thOutWorker.start();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot start output map backup processor worker thread for:" + zBackupCode + " > err:" + ex0);
                    }
                }else{
                    try{
                        wakeUpProcessor();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-resume output map backup processor worker thread for:" + zBackupCode + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "start processor exception:" + ex0);
        }
    }
    
    public synchronized void wakeUpProcessor(){
        try{
            if (this.thOutWorker != null){
                if (this.thOutWorker.isAlive()){
                    try{
                        notify();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-resume output map backup processor worker thread for:" + zBackupCode + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "wakeup processor exception:" + ex0);
        }
    }
    
    public synchronized void stopProcessor(){
        try{
            if (this.thOutWorker != null){
                if (this.thOutWorker.isAlive()){
                    try{
                        this.bOutWorkerMustRun.set(false);
                        wakeUpProcessor();
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot notify-close map backup processor worker thread for:" + zBackupCode + " > err:" + ex0);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "stop processor exception:" + ex0);
        }
    }
    
    @Override
    public void run() {
        try{
            ITMFileAccessForFastWrite fileMapW = new ITMFileAccessForFastWrite();
            Iterator<String> itrMap;
            while(this.bOutWorkerMustRun.get()){
                try{
                    while (!clqInMemoryMapLines.isEmpty()){
                        itrMap = clqInMemoryMapLines.iterator();
                        while(itrMap.hasNext()){
                            String szCurrentMapLine = itrMap.next();
                            //.test file map:
                            String szCurrentFileName = DEFAULT_MAP_BACKUP_FILE_DIRECTORY + DEFAULT_MAP_BACKUP_FILE_PREFIX + zBackupCode + DEFAULT_MAP_BACKUP_FILE_EXTENSION;
                            if (fileMapW.isFileAlreadyOpened()){
                                //... .
                            }else{
                                //.buat baru:
                                //.cek folder (pengecekan ulang direktori):
                                ITMFileAccessForAdmin.createNewDirectoryFromCurrentEx(DEFAULT_MAP_BACKUP_FILE_DIRECTORY);
                                //.buat file baru:
                                fileMapW.openCreateFileForWrite(szCurrentFileName, true);
                            }
                            //.simpan ke file:
                            if (fileMapW.isFileAlreadyOpened()){
                                fileMapW.appendLine(szCurrentMapLine);
                            }
                            //.hapus dari daftar di memory:
                            itrMap.remove();
                        }
                    }
                    //.tunggu perintah berikutnya:
                    synchronized(this){
                        wait(10); //.istirahat dulu sebentar :)
                    }
                }catch(InterruptedException ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "interrupt to map backup processor worker thread for:" + zBackupCode + " > err:" + ex0);
                }catch(Exception ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to map backup processor worker thread for:" + zBackupCode + " > err:" + ex0);
                }
            }
            //.close file if opened:
            if (fileMapW.isFileAlreadyOpened()){
                fileMapW.closeFile();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "exception to run map backup processor worker thread for:" + zBackupCode + " > err:" + ex0);
        }
        //.finish:
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "map backup processor worker thread finished.");
    }
    
}