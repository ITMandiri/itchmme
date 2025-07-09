/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.msgmem;

import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForAdmin;
import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForFastRead;
import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForFastWrite;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupConsts;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author fredy
 */
public class ITMFeedMsgFile {
    
    public final static ITMFeedMsgFile getInstance = new ITMFeedMsgFile();

    private final String mFeedFilePath = ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_FILE_DIRECTORY + "datafeed.mem";
    
    private final ConcurrentLinkedQueue<String> clqMemorySaveAsFeedMemory = new ConcurrentLinkedQueue<>();
    private final FeedBackupSaverWorker thBackupSaverFeedMemory = new FeedBackupSaverWorker(clqMemorySaveAsFeedMemory);
    
    private ITMFeedMsgFile(){
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    public boolean saveAsFeedMemoryRecord(String mRecord){
        boolean mOut = false; //.in;
        try{
            if (mRecord != null){
                this.clqMemorySaveAsFeedMemory.add(mRecord);
                this.thBackupSaverFeedMemory.resumeWorker();
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.SOUPBINTCP, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    public boolean resetFileMemory(){
        ITMFileAccessForFastRead mFileRdr = new ITMFileAccessForFastRead();
        if (mFileRdr.openFileForReadLinesFromFirst(mFeedFilePath)){
            long lHandle =  mFileRdr.deleteFileContents();
            if (lHandle >= 0){
                return true;
            }
        }
        
        return false;
    }
    private class FeedBackupSaverWorker implements Runnable {
        private Thread thWorker;
        
        private boolean bLazyInitComplete = false;
        private long tLazyInitStartTick = 0;
        private final static long LAZY_INIT_WAIT_TIME_MS = 3000;
        private final ConcurrentLinkedQueue<String> clqSource;
         
        public FeedBackupSaverWorker(ConcurrentLinkedQueue<String> clqSource){
            this.tLazyInitStartTick = System.currentTimeMillis();
            this.bLazyInitComplete = false;
            this.clqSource = clqSource;
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
            AtomicBoolean bIsMustRunning = new AtomicBoolean(true);
            Iterator<String> itrLog;
            String szCurrentFileName = mFeedFilePath;
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
                                
                                String mCurrentRecord = itrLog.next();
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
                                    if (mCurrentRecord != null && !mCurrentRecord.equalsIgnoreCase("")){
                                        String zRecordToLine = mCurrentRecord;
                                        fileLogW.appendLine(zRecordToLine);
                                    }
                                }
                                //.hapus dari daftar di memory:
                                itrLog.remove();
                            }
                        }
                    }
                }catch(Exception ex0){
                }
            }
            if (fileLogW.isFileAlreadyOpened()){
                fileLogW.closeFile();
            }
        }
        
    }
    
}
