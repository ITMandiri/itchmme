/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.backup;

import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForAdmin;
import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForFastWrite;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupMgr.CSB_Backup_Record;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeBackupSaver {
    
    private String zRefSource = "";
    
    private final ConcurrentLinkedQueue<CSB_Backup_Record> clqMemorySaveAsRECVSequencedRecords = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<CSB_Backup_Record> clqMemorySaveAsRECVUnsequencedRecords = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<CSB_Backup_Record> clqMemorySaveAsSENDRecords = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<CSB_Backup_Record> clqMemorySaveAsDUMPRecords = new ConcurrentLinkedQueue<>();
    
    private final CIBackupSaverWorker thBackupSaverRECVSeqWorker = new CIBackupSaverWorker(ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_TYPE_RECV_SEQEUNCED, false, clqMemorySaveAsRECVSequencedRecords);
    private final CIBackupSaverWorker thBackupSaverRECVUnSeqWorker = new CIBackupSaverWorker(ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_TYPE_RECV_UNSEQEUNCED, false, clqMemorySaveAsRECVUnsequencedRecords);
    private final CIBackupSaverWorker thBackupSaverSENDWorker = new CIBackupSaverWorker(ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_TYPE_SEND, false, clqMemorySaveAsSENDRecords);
    private final CIBackupSaverWorker thBackupSaverDUMPWorker = new CIBackupSaverWorker(ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_TYPE_DUMP, true, clqMemorySaveAsDUMPRecords);
    
    public ITMSoupBinTCPBridgeBackupSaver(String zRefSource){
        this.zRefSource = zRefSource;
    }
    
    public String getRefSource() {
        return zRefSource;
    }
    
    public boolean saveAsRECVSequencedRecord(CSB_Backup_Record mRecord){
        boolean mOut = false; //.in;
        try{
            if (mRecord != null){
                this.clqMemorySaveAsRECVSequencedRecords.add(mRecord);
                this.thBackupSaverRECVSeqWorker.resumeWorker();
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean saveAsRECVUnsequencedRecord(CSB_Backup_Record mRecord){
        boolean mOut = false; //.in;
        try{
            if (mRecord != null){
                this.clqMemorySaveAsRECVUnsequencedRecords.add(mRecord);
                this.thBackupSaverRECVUnSeqWorker.resumeWorker();
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean saveAsSENDRecord(CSB_Backup_Record mRecord){
        boolean mOut = false; //.out;
        try{
            if (mRecord != null){
                this.clqMemorySaveAsSENDRecords.add(mRecord);
                this.thBackupSaverSENDWorker.resumeWorker();
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean saveAsDUMPRecord(CSB_Backup_Record mRecord){
        boolean mOut = false; //.all;
        try{
            if (mRecord != null){
                this.clqMemorySaveAsDUMPRecords.add(mRecord);
                this.thBackupSaverDUMPWorker.resumeWorker();
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private class CIBackupSaverWorker implements Runnable {
        private Thread thWorker;
        private String zBackupType = "OTHER";
        private boolean bTypeDump = false;
        private final ConcurrentLinkedQueue<CSB_Backup_Record> clqSource;
        
        private long lLastRecordNo = 0;
        private long lCurrentRecordNo = 0; //kalau nilai = 1 --> jika bukan tipe dump, tutup log yg lama, rename jadi "<namafile>_yyyyMMdd_HHmmss.old", buat file baru, simpan record yg baru;
        
        private long tLazyInitStartTick = 0;
        private boolean bLazyInitComplete = false;
        
        private final static long LAZY_INIT_WAIT_TIME_MS = 5000;
        
        public CIBackupSaverWorker(String zBackupType, boolean bTypeDump, ConcurrentLinkedQueue<CSB_Backup_Record> clqSource){
            this.zBackupType = zBackupType;
            this.bTypeDump = bTypeDump;
            this.clqSource = clqSource;
            this.tLazyInitStartTick = System.currentTimeMillis();
            this.bLazyInitComplete = false;
        }
        
        public synchronized void resumeWorker(){
            if (thWorker == null){
                thWorker = new Thread(this);
            }
            if (thWorker != null){
                if (!thWorker.isAlive()){
                    try{
                        thWorker = new Thread(this); //.buat baru biar bisa jalan lagi (biar aman).
                        thWorker.setPriority(Thread.NORM_PRIORITY);
                        thWorker.start();
                    }catch(Exception ex0){
                        System.out.println("exception:" + ex0);
                    }
                }else{
                    try{
                        notify();
                    }catch(Exception ex0){
                        System.out.println("exception:" + ex0);
                    }
                }
            }
        }
        
        @Override
        public void run() {
            ITMFileAccessForFastWrite fileLogW = new ITMFileAccessForFastWrite();
            Iterator<CSB_Backup_Record> itrLog;
            AtomicBoolean bIsMustRunning = new AtomicBoolean(true);
            while(bIsMustRunning.get()){
                try{
                    if (!bLazyInitComplete){
                        long tDeltaTick = (System.currentTimeMillis() - tLazyInitStartTick);
                        if ((Math.abs(tDeltaTick)) >= LAZY_INIT_WAIT_TIME_MS){
                            bLazyInitComplete = true;
                        }
                    }else{
                        while (!this.clqSource.isEmpty()){
                            itrLog = this.clqSource.iterator();
                            while(itrLog.hasNext()){
                                CSB_Backup_Record mCurrentRecord = itrLog.next();
                                if (mCurrentRecord != null){
                                    lCurrentRecordNo = mCurrentRecord.recordNo;
                                }else{
                                    lCurrentRecordNo = 0;
                                }
                                lLastRecordNo = lCurrentRecordNo;
                                //.test file log:
                                String szCurrentFileName = ITMSoupBinTCPBridgeBackupConsts.ALTER_SOUPBINTCP_BACKUP_FILE_DIRECTORY + getRefSource() + "_" + this.zBackupType + "_" + ITMSoupBinTCPBridgePacketFormat.strCurrentFormattedDateFileSafe() + ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_FILE_EXTENSION;
                                if ((ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_CLOSE_FILE_ON_RESET_RECORDNO) && (!this.bTypeDump) && (lLastRecordNo == 1)){
                                    if (fileLogW.isFileAlreadyOpened()){
                                        //.tutup file yg lama:
                                        fileLogW.closeFile();
                                        //.rename file yg lama:
                                        ITMFileAccessForAdmin.renameFile(szCurrentFileName, szCurrentFileName + "_" + ITMSoupBinTCPBridgePacketFormat.strCurrentFormattedDateTimeFileSafe() + ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_OLD_FILE_EXTENSION);
                                    }
                                }
                                if (fileLogW.isFileAlreadyOpened()){
                                    //.cek apakah nama file sama:
                                    if(!szCurrentFileName.endsWith(fileLogW.getOpenedFilePath())){
                                        //.tutup file yg lama:
                                        fileLogW.closeFile();
                                        //.buat baru:
                                        //.cek folder (pengecekan ulang direktori):
                                        ITMFileAccessForAdmin.createNewDirectoryFromCurrentEx(ITMSoupBinTCPBridgeBackupConsts.ALTER_SOUPBINTCP_BACKUP_FILE_DIRECTORY);
                                        //.buat file baru:
                                        fileLogW.openCreateFileForWrite(szCurrentFileName, true);
                                    }
                                }else{
                                    //.buat baru:
                                    //.cek folder (pengecekan ulang direktori):
                                    ITMFileAccessForAdmin.createNewDirectoryFromCurrentEx(ITMSoupBinTCPBridgeBackupConsts.ALTER_SOUPBINTCP_BACKUP_FILE_DIRECTORY);
                                    //.buat file baru:
                                    fileLogW.openCreateFileForWrite(szCurrentFileName, true);
                                }
                                //.simpan ke file:
                                if (fileLogW.isFileAlreadyOpened()){
                                    if (mCurrentRecord != null){
                                        //.format:ITM|yyyy-MM-dd|HH:mm:ss|I(in)/O(out)|RecordNo|MsgType|HexMessage|OK
                                        String zRecordToLine = ITMSoupBinTCPBridgePacketFormat.convertBytesToHex(mCurrentRecord.arbMessage);
                                        String zMsgType = ((mCurrentRecord.arbMessage.length >= 4) ? Character.toString((char)mCurrentRecord.arbMessage[3]) : "");
                                        zRecordToLine = zRecordToLine + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_SUFFIX;
                                        zRecordToLine = zMsgType + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER + zRecordToLine;
                                        zRecordToLine = mCurrentRecord.recordNo + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER + zRecordToLine;
                                        zRecordToLine = ((mCurrentRecord.isOutput) ? "O" : "I") + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER + zRecordToLine;
                                        zRecordToLine = ITMSoupBinTCPBridgePacketFormat.buildTimeSVRTRXFormat(mCurrentRecord.saveDate) + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER + zRecordToLine;
                                        zRecordToLine = ITMSoupBinTCPBridgePacketFormat.buildDateSVRTRXFormat(mCurrentRecord.saveDate) + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER + zRecordToLine;
                                        zRecordToLine = ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_PREFIX + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER + zRecordToLine;
                                        fileLogW.appendLine(zRecordToLine);
                                    }
                                }
                                //.hapus dari daftar di memory:
                                itrLog.remove();
                            }
                        }
                    }
                    //.tunggu perintah berikutnya:
                    synchronized(this){
                        wait(10); //.istirahat dulu sebentar :)
                    }
                }catch(InterruptedException ex0){
                }catch(Exception ex0){
                }
            }
            if (fileLogW.isFileAlreadyOpened()){
                fileLogW.closeFile();
            }
        }
        
    }
    
}
