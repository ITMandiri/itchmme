/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.filelogger.setup;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author aripam
 */
public class ITMFileLoggerManager {
    //.single instance ya:
    public final static ITMFileLoggerManager getInstance = new ITMFileLoggerManager();
    
    private ConcurrentHashMap<logSource, ITMFileLoggerAccess> hmLogSources = new ConcurrentHashMap<>();
    
    //.informasi listener:
    private List<ITMFileLoggerListener> fFileLoggerListeners;
    
    //.aktifkan semua untuk awalan. disable nanti saat ada permintaan:
    private boolean bEnableFileLogger           = true;
    private boolean bEnableLevelDebug           = true;
    private boolean bEnableLevelError           = true;
    private boolean bEnableLevelInfo            = true;
    private boolean bEnableLevelInit            = true;
    private boolean bEnableLevelWarning         = true;
    
    public ITMFileLoggerManager() {
        //.nothing todo here :)
    }

    public boolean isbEnableLevelDebug() {
        return bEnableLevelDebug;
    }

    public void setbEnableLevelDebug(boolean bEnableLevelDebug) {
        this.bEnableLevelDebug = bEnableLevelDebug;
    }

    public boolean isbEnableLevelError() {
        return bEnableLevelError;
    }

    public void setbEnableLevelError(boolean bEnableLevelError) {
        this.bEnableLevelError = bEnableLevelError;
    }

    public boolean isbEnableLevelInfo() {
        return bEnableLevelInfo;
    }

    public void setbEnableLevelInfo(boolean bEnableLevelInfo) {
        this.bEnableLevelInfo = bEnableLevelInfo;
    }

    public boolean isbEnableLevelInit() {
        return bEnableLevelInit;
    }

    public void setbEnableLevelInit(boolean bEnableLevelInit) {
        this.bEnableLevelInit = bEnableLevelInit;
    }

    public boolean isbEnableLevelWarning() {
        return bEnableLevelWarning;
    }

    public void setbEnableLevelWarning(boolean bEnableLevelWarning) {
        this.bEnableLevelWarning = bEnableLevelWarning;
    }
    
    public void setzLoggerDirectory(String zLoggerDirectory) {
        ITMFileLoggerVarsConsts.ALTER_LOGGER_FILE_DIRECTORY = zLoggerDirectory;
    }
    
    public void insertLog(Object cClassInfo, logSource source, logLevel level, String zInputLog){
        doInsertLog(cClassInfo, source, level, zInputLog, null);
    }
    
    public void insertLog(Object cClassInfo, logSource source, logLevel level, Exception qException){
        doInsertLog(cClassInfo, source, level, null, qException);
    }
    
    public void insertLog(Object cClassInfo, logSource source, logLevel level, String zInputLog, Exception qException){
        doInsertLog(cClassInfo, source, level, zInputLog, qException);
    }
    
    private void doInsertLog(Object cClassInfo, logSource source, logLevel level, String zInputLog, Exception qException){
        try{
            //.cek apakah file-logger diperbolehkan:
            if (bEnableFileLogger){
                //.cek apakah level diperbolehkan:
                switch(level){
                    case DEBUG:
                        if (!bEnableLevelDebug){
                            return;
                        }
                        break;
                    case ERROR:
                        if (!bEnableLevelError){
                            return;
                        }
                        break;
                    case INFO:
                        if (!bEnableLevelInfo){
                            return;
                        }
                        break;
                    case INIT:
                        if (!bEnableLevelInit){
                            return;
                        }
                        break;
                    case WARNING:
                        if (!bEnableLevelWarning){
                            return;
                        }
                        break;
                    default:
                        return;
                }
                //.cek apakah akses ke log-source-writer ada:
                if (hmLogSources == null){
                    hmLogSources = new ConcurrentHashMap<>();
                }
                if (hmLogSources != null){
                    if (!hmLogSources.containsKey(source)){
                        synchronized(this){
                            //.buat object pemroses log baru:
                            ITMFileLoggerAccess hNewLogSource = new ITMFileLoggerAccess(source.getPrefix());
                            hmLogSources.put(source, hNewLogSource);
                        }
                    }
                    ITMFileLoggerAccess hNewLogSource = hmLogSources.get(source);
                    if (hNewLogSource != null){
                        hNewLogSource.insertLog(cClassInfo, level, zInputLog, qException);
                    }
                }
            }
        }catch(Exception ex0){
            //.debug error sementara:
            //System.out.println(ex0.getMessage());
        }
        if (level != logLevel.INIT){
            //.debug.print sementara:
            //System.out.println(zInputLog);
        }
        if (level == logLevel.WARNING){
            //.debug.print sementara:
            System.out.println("warning: " + zInputLog);
        }
        if (level == logLevel.ERROR){
            //.debug.print sementara:
            System.out.println("error: " + zInputLog);
        }
    }
    
    //.listener methods:
    public List<ITMFileLoggerListener> getFileLoggerListeners() {
        return fFileLoggerListeners;
    }
    
    public void setFileLoggerListeners(List<ITMFileLoggerListener> fFileLoggerListeners)  {
        this.fFileLoggerListeners = fFileLoggerListeners;
    }
    
    
    public void addFileLoggerListener(ITMFileLoggerListener newListener){
        try{
            if (newListener == null){
                return;
            }
            if (this.fFileLoggerListeners == null){
                this.fFileLoggerListeners = new ArrayList<>();
            }
            if (!this.fFileLoggerListeners.contains(newListener)){ //.@only once per object.@
                this.fFileLoggerListeners.add(newListener);
            }
            //.selesai.
        }catch(Exception ex0){
            //.EXXX.
            System.out.println("logger.error.Exception:" + ex0);
        }
    }
    
    public void removeFileLoggerListener(ITMFileLoggerListener oldListener){
        try{
            if (oldListener == null){
                return;
            }
            if (this.fFileLoggerListeners != null){
                this.fFileLoggerListeners.remove(oldListener);
            }
        }catch(Exception ex0){
            //.EXXX.
            System.out.println("logger.error.Exception:" + ex0);
        }
    }
    
    public void raiseOnFileLogAdded(logLevel level, String zFullLogMessage){
        try{
            if ((this.fFileLoggerListeners != null) && (this.fFileLoggerListeners.size() > 0)){
                for (Iterator<ITMFileLoggerListener> iterFileLogListener = this.fFileLoggerListeners.iterator(); iterFileLogListener.hasNext(); ){
                    ITMFileLoggerListener lstr = (ITMFileLoggerListener)iterFileLogListener.next();
                    if (lstr != null){
                        lstr.onFileLogAdded(level, zFullLogMessage);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.out.println("logger.error.Exception:" + ex0);
        }
    }
    
}
