/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.backup;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeBackupMgr {
    
    public final static ITMSoupBinTCPBridgeBackupMgr getInstance = new ITMSoupBinTCPBridgeBackupMgr();
    
    private final ConcurrentHashMap<String, ITMSoupBinTCPBridgeBackupSaver> chmBackupSavers = new ConcurrentHashMap<>();
    
    public boolean saveAsRECVSequencedRecord(String refSource, long recordNo, byte[] arbMessage){
        boolean mOut = false; //.in;
        try{
            if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(refSource)){
                CSB_Backup_Record mRecord = new CSB_Backup_Record(new Date(), false, recordNo, arbMessage);
                ITMSoupBinTCPBridgeBackupSaver mSaver = null;
                    if (!this.chmBackupSavers.containsKey(refSource)){
                        mSaver = new ITMSoupBinTCPBridgeBackupSaver(refSource);
                        this.chmBackupSavers.put(refSource, mSaver);
                    }else{
                        mSaver = this.chmBackupSavers.get(refSource);
                    }
                if (mSaver != null){
                    mSaver.saveAsRECVSequencedRecord(mRecord);
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean saveAsRECVUnsequencedRecord(String refSource, long recordNo, byte[] arbMessage){
        boolean mOut = false; //.in;
        try{
            if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(refSource)){
                CSB_Backup_Record mRecord = new CSB_Backup_Record(new Date(), false, recordNo, arbMessage);
                ITMSoupBinTCPBridgeBackupSaver mSaver = null;
                    if (!this.chmBackupSavers.containsKey(refSource)){
                        mSaver = new ITMSoupBinTCPBridgeBackupSaver(refSource);
                        this.chmBackupSavers.put(refSource, mSaver);
                    }else{
                        mSaver = this.chmBackupSavers.get(refSource);
                    }
                if (mSaver != null){
                    mSaver.saveAsRECVUnsequencedRecord(mRecord);
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean saveAsSENDRecord(String refSource, long recordNo, byte[] arbMessage){
        boolean mOut = false; //.out;
        try{
            if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(refSource)){
                CSB_Backup_Record mRecord = new CSB_Backup_Record(new Date(), true, recordNo, arbMessage);
                ITMSoupBinTCPBridgeBackupSaver mSaver = null;
                    if (!this.chmBackupSavers.containsKey(refSource)){
                        mSaver = new ITMSoupBinTCPBridgeBackupSaver(refSource);
                        this.chmBackupSavers.put(refSource, mSaver);
                    }else{
                        mSaver = this.chmBackupSavers.get(refSource);
                    }
                if (mSaver != null){
                    mSaver.saveAsSENDRecord(mRecord);
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean saveAsDUMPRecord(String refSource, boolean isOutput, long recordNo, byte[] arbMessage){
        boolean mOut = false; //.all;
        try{
            if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(refSource)){
                CSB_Backup_Record mRecord = new CSB_Backup_Record(new Date(), isOutput, recordNo, arbMessage);
                ITMSoupBinTCPBridgeBackupSaver mSaver = null;
                    if (!this.chmBackupSavers.containsKey(refSource)){
                        mSaver = new ITMSoupBinTCPBridgeBackupSaver(refSource);
                        this.chmBackupSavers.put(refSource, mSaver);
                    }else{
                        mSaver = this.chmBackupSavers.get(refSource);
                    }
                if (mSaver != null){
                    mSaver.saveAsDUMPRecord(mRecord);
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public class CSB_Backup_Record {
        public Date saveDate = null;
        public boolean isOutput = false;
        public long recordNo = 0;
        public byte[] arbMessage = null;
        public CSB_Backup_Record(Date saveDate, boolean isOutput, long recordNo, byte[] arbMessage){
            this.saveDate = saveDate;
            this.isOutput = isOutput;
            this.recordNo = recordNo;
            this.arbMessage = arbMessage;
        }
    }
    
}
