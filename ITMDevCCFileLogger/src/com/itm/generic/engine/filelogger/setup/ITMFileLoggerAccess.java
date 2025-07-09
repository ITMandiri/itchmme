/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.filelogger.setup;

import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForAdmin;
import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForFastWrite;
import static com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.ALTER_LOGGER_FILE_DIRECTORY;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author aripam
 */
public class ITMFileLoggerAccess {
    
    private String zLogFileNamePrefix   = "";
    
    private FileLoggerTaskWorker thLogWorker = new FileLoggerTaskWorker();
    
    //.penampung sementara log di memory:
    private ConcurrentLinkedQueue<String> clqInMemoryLogInfos = new ConcurrentLinkedQueue<>();
    
    private long tLazyInitStartTick = 0;
    private boolean bLazyInitComplete = false;
    
    public ITMFileLoggerAccess(String zLogFileNamePrefix) {
        this.zLogFileNamePrefix = zLogFileNamePrefix;
        this.tLazyInitStartTick = System.currentTimeMillis();
        this.bLazyInitComplete = false;
        //.nothing todo here :)
    }
    
    
    //.file logger methods:
    public void insertLog(Object cClassInfo, logLevel level, String zInputLog, Exception qException){
        try{
            //.format input log:
            String zCallerMethodName = "<?>";
            String zCallerObjectName = cClassInfo.toString();
            String zExceptionDetails = "";
            if (zCallerObjectName.length() > 0){
                String[] arrNameSpace = zCallerObjectName.split("[.]");
                if (arrNameSpace != null){
                    if (arrNameSpace.length > 0){
                        zCallerObjectName = arrNameSpace[arrNameSpace.length - 1];
                    }
                }
            }
            try{
                StackTraceElement[] stkE = Thread.currentThread().getStackTrace();
                if (stkE != null){
                    StackTraceElement stkI = stkE[4]; //.index[4] == dapat method pemanggil. index[3] == dapat method internal logger. index[2] == dapatkan method pemanggil internal logger. index[1] == dapatkan method ini (yg dipanggil).
                    if (stkI != null){
                        zCallerMethodName = stkI.getMethodName();
                    }
                }
            }catch(Exception ex0){
                System.out.println("logger.error.Exception:" + ex0);
            }
            try{
                if (qException != null){
                    zExceptionDetails = "\texception:";
                    StringWriter errWriter = new StringWriter();
                    qException.printStackTrace(new PrintWriter(errWriter));
                    zExceptionDetails += errWriter.toString();
                }
            }catch(Exception ex0){
                System.out.println("logger.error.Exception:" + ex0);
            }
            //.simpan ke antrian:
            String zFullLogMessage = ITMFileLoggerHelper.strLogDateTime() + "\t" + level + "\tth:" + Thread.currentThread().getId() + "\tcls:" + zCallerObjectName + "\tmtd:" + zCallerMethodName + "\tlog:" + zInputLog + zExceptionDetails;
            clqInMemoryLogInfos.add(zFullLogMessage);
            //.simpan ke file:
            if (thLogWorker != null){
                thLogWorker.ResumeWorker();
            }
            //.broadcast filelogger:
            ITMFileLoggerManager.getInstance.raiseOnFileLogAdded(level, zFullLogMessage);
            //.selesai... .
        }catch(Exception ex0){
            System.out.println("logger.error.Exception:" + ex0);
        }
    }
    
    private class FileLoggerTaskWorker implements Runnable{
        private Thread thWorker;
        
        public FileLoggerTaskWorker(){
            //.nothing todo here :)
        }
        
        public synchronized void ResumeWorker(){
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
                        System.out.println("logger.error.Exception:" + ex0);
                    }
                }else{
                    try{
                        notify();
                    }catch(Exception ex0){
                        System.out.println("logger.error.Exception:" + ex0);
                    }
                }
            }
        }
        
        @Override
        public void run(){ //.worker:
            ITMFileAccessForFastWrite fileLogW = new ITMFileAccessForFastWrite();
            Iterator<String> itrLog;
            AtomicBoolean bIsMustRunning = new AtomicBoolean(true);
            while(bIsMustRunning.get()){
                try{
                    if (!bLazyInitComplete){
                        long tDeltaTick = (System.currentTimeMillis() - tLazyInitStartTick);
                        if ((Math.abs(tDeltaTick)) >= 2000){
                            bLazyInitComplete = true;
                        }
                    }else{
                        while (!clqInMemoryLogInfos.isEmpty()){
                            itrLog = clqInMemoryLogInfos.iterator();
                            while(itrLog.hasNext()){
                                String szCurrentLogData = itrLog.next();
                                //.test file log:
                                String szCurrentFileName = ALTER_LOGGER_FILE_DIRECTORY + zLogFileNamePrefix + ITMFileLoggerHelper.strLogDateFileSafe() + ITMFileLoggerVarsConsts.DEFAULT_LOGGER_FILE_EXTENSION;
                                if (fileLogW.isFileAlreadyOpened()){
                                    //.cek apakah nama file sama:
                                    if(!szCurrentFileName.endsWith(fileLogW.getOpenedFilePath())){
                                        //.tulis / beritahu ada perubahan tanggal:
                                        fileLogW.appendLine("current date changed:" + ITMFileLoggerHelper.strLogDateTime() + "\t" + fileLogW.getOpenedFilePath() + "\t" + szCurrentFileName);
                                        //.tutup file yg lama:
                                        fileLogW.closeFile();
                                        //.buat baru:
                                        //.cek folder (pengecekan ulang direktori):
                                        ITMFileAccessForAdmin.createNewDirectoryFromCurrentEx(ALTER_LOGGER_FILE_DIRECTORY);
                                        //.buat file baru:
                                        fileLogW.openCreateFileForWrite(szCurrentFileName, true);
                                    }
                                }else{
                                    //.buat baru:
                                    //.cek folder (pengecekan ulang direktori):
                                    ITMFileAccessForAdmin.createNewDirectoryFromCurrentEx(ALTER_LOGGER_FILE_DIRECTORY);
                                    //.buat file baru:
                                    fileLogW.openCreateFileForWrite(szCurrentFileName, true);
                                }
                                //.simpan ke file:
                                if (fileLogW.isFileAlreadyOpened()){
                                    fileLogW.appendLine(szCurrentLogData);
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
                }catch(InterruptedException intex0){
                    //.System.out.println("error.InterruptedException:" + intex);
                }catch(Exception ex0){
                    //.System.out.println("error.Exception:" + ex);
                }
            }
            if (fileLogW.isFileAlreadyOpened()){
                fileLogW.closeFile();
            }
        }
    }
    
    
}
