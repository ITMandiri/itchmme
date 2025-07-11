/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.mapbackup;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.idx.data.helpers.DateTimeHelper;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class ITMMapBackupMgr {
    
    public final static ITMMapBackupMgr getInstance = new ITMMapBackupMgr();
    
    //.daftar saja, bukan urutan:
    private ConcurrentHashMap<String, ITMMapBackupProcessor> activeBackupsProcessors = new ConcurrentHashMap<>(); //.daftar channel dan processor.<Channel,JONECSimCallbackProcessor>
    
    //.constructor:
    public ITMMapBackupMgr(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public synchronized ITMMapBackupProcessor getOrCreateBackupProcessor(String zBackupCode, boolean bUniquePerDay, boolean bVerifyRunning, ConcurrentHashMap oTargetMap, Class tTargetMapKeyType, Class tTargetMapValueType){
        ITMMapBackupProcessor mOut = null;
        try{
            String zFullBackupCode = zBackupCode;
            String zCodeDelimiter = "__";
            if (bUniquePerDay){
                zFullBackupCode += (zCodeDelimiter + DateTimeHelper.getDateSVRTRXFormat());
            }
            if (this.activeBackupsProcessors.containsKey(zFullBackupCode)){
                mOut = this.activeBackupsProcessors.get(zFullBackupCode);
            }else {
                ITMMapBackupProcessor mProcessor = new ITMMapBackupProcessor(zFullBackupCode, oTargetMap, tTargetMapKeyType, tTargetMapValueType);
                mProcessor.startProcessor();
                this.activeBackupsProcessors.put(zFullBackupCode, mProcessor);
                mOut = this.activeBackupsProcessors.get(zFullBackupCode);
            }
            if (bVerifyRunning && (mOut != null)){
                mOut.wakeUpProcessor();
            }
            if (bUniquePerDay && (!this.activeBackupsProcessors.isEmpty())){
                ArrayList<String> lstBackupCodeToRemove = new ArrayList<>();
                for(String zEachBackupCode : this.activeBackupsProcessors.keySet()){
                    if ((zEachBackupCode.startsWith(zBackupCode + zCodeDelimiter)) && (!zEachBackupCode.equals(zFullBackupCode))){
                        lstBackupCodeToRemove.add(zEachBackupCode);
                    }
                }
                if (!lstBackupCodeToRemove.isEmpty()){
                    for(String zEachDelBackupCode : lstBackupCodeToRemove){
                        ITMMapBackupProcessor mEachDelBackupProcessor = this.activeBackupsProcessors.get(zEachDelBackupCode);
                        if (mEachDelBackupProcessor != null){
                            mEachDelBackupProcessor.stopProcessor();
                            this.activeBackupsProcessors.remove(zEachDelBackupCode);
                        }
                    }
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "get backup processor exception:" + ex0);
        }
        return mOut;
    }
    
}

