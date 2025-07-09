/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.datafile.restore;

import com.itm.generic.engine.fileaccess.setup.ITMFileAccessForFastRead;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.mis.itch.callback.ITMSoupBinTCPITCHPacketController;
import com.itm.mis.itch.callback.ITMSoupBinTCPITCHPacketMgr;
import com.itm.mis.itch.msgmemory.ITMITCHMsgMemory;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupConsts;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupMsgBase;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupParser;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;

/**
 *
 * @author fredy
 */
public class DataFileRestoreAccess {
    //.single instance ya:
    public final static DataFileRestoreAccess getInstance = new DataFileRestoreAccess();
    
    public enum E_DATAFILE_RESTORE_MODE {
        ONLY_LAST_SEQ_AND_MESSAGE
        ,FULL_RESTORE
    };
    
    public E_DATAFILE_RESTORE_MODE eDataFileRestoreMode = E_DATAFILE_RESTORE_MODE.FULL_RESTORE; //ONLY_LAST_SEQ_AND_MESSAGE;
    
    boolean bAlreadyProcessed = false;
    
    public synchronized long processOnce(){
        long mOut = 0;
        try{
            if (!this.bAlreadyProcessed){
                
                String zTodayStr = ITMSoupBinTCPBridgePacketFormat.getDateSVRTRXFormat();
                
                String zConnName_ITCH = "";
                String zConnName_ITCH_MDF = "";
                
                ITMSoupBinTCPITCHPacketController mController_ITCH = null;
                ITMSoupBinTCPITCHPacketController mController_ITCH_MDF = null;
                
                for(ITMSoupBinTCPITCHPacketController mEachConn : ITMSoupBinTCPITCHPacketMgr.getInstance.getAllConnectionLines().values()){
                    if (mEachConn.getConnectionName().toUpperCase().contains("MDF")){
                        zConnName_ITCH_MDF = mEachConn.getConnectionName();
                        mController_ITCH_MDF = mEachConn;
                    }else if (mEachConn.getConnectionName().toUpperCase().contains("ITCH")){
                        zConnName_ITCH = mEachConn.getConnectionName();
                        mController_ITCH = mEachConn;
                    }
                }
                
                String zFileName_ITCH = ITMSoupBinTCPBridgeBackupConsts.ALTER_SOUPBINTCP_BACKUP_FILE_DIRECTORY + zConnName_ITCH + "_" + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_TYPE_RECV_SEQEUNCED + "_" + ITMSoupBinTCPBridgePacketFormat.strCurrentFormattedDateFileSafe() + ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_FILE_EXTENSION;
                String zFileName_ITCH_MDF = ITMSoupBinTCPBridgeBackupConsts.ALTER_SOUPBINTCP_BACKUP_FILE_DIRECTORY + zConnName_ITCH_MDF + "_" + ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_TYPE_RECV_SEQEUNCED + "_" + ITMSoupBinTCPBridgePacketFormat.strCurrentFormattedDateFileSafe() + ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_FILE_EXTENSION;
                
                if (!StringHelper.isNullOrEmpty(zFileName_ITCH) && (mController_ITCH != null)){
                    String zDataFileName = zFileName_ITCH;
                    ITMFileAccessForFastRead mFileRdr = new ITMFileAccessForFastRead();
                    if (mFileRdr.openFileForReadLinesFromFirst(zDataFileName)){
                        try{
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Read Data File to restore BEGIN: " + zDataFileName);
                            String zDataLine;
                            ITMSoupBinTCPBridgeBackupMsgBase mMsg;
                            boolean bNeedToStop = false;
                            while ((zDataLine = mFileRdr.readLine()) != null){
                                mMsg = ITMSoupBinTCPBridgeBackupParser.getInstance.parsePacket(zDataLine);
                                if (mMsg != null && mMsg.isHeaderParsed && mMsg.isContentParsed && !mMsg.isOutput && (zTodayStr.equals(mMsg.saveDateStr))){
                                    switch (eDataFileRestoreMode) {
                                        case ONLY_LAST_SEQ_AND_MESSAGE:
                                            mController_ITCH.setCurrentSequencedNo(mMsg.recordNo);
                                            mController_ITCH.setRecentSequencedMsg(mMsg.sbMessage);
                                            break;
                                        case FULL_RESTORE:
                                            mController_ITCH.setCurrentSequencedNo(mMsg.recordNo);
                                            mController_ITCH.setRecentSequencedMsg(mMsg.sbMessage);
                                            //... .
                                            ITMITCHMsgMemory.getInstance.mapMessage(mMsg.arbMessage, mMsg.sbMessage);
                                            
                                            
                                            break;
                                        default:
                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Invalid Restore Mode Error: Mode=" + eDataFileRestoreMode + ", zDataFileName=" + zDataFileName + ", isHeaderParsed=" + mMsg.isHeaderParsed + ", isContentParsed=" + mMsg.isContentParsed + ", isOutput=" + mMsg.isOutput);
                                            bNeedToStop = true;
                                            break;
                                    }
                                }else{
                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Data File Parse Error: " + zDataFileName + ", mMsg_IsNotNull=" + (mMsg != null) + ", isHeaderParsed=" + mMsg.isHeaderParsed + ", isContentParsed=" + mMsg.isContentParsed + ", isOutput=" + mMsg.isOutput + ", saveDateStr=" + mMsg.saveDateStr);
                                    bNeedToStop = true;
                                }
                                if (bNeedToStop) { break; }
                            }
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Read Data File to restore END: " + zDataFileName);                            
                        }catch(Exception ex0){
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                        }
                        mFileRdr.closeFile();
                    }else{
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Data File to restore not found: " + zDataFileName);
                    }
                }
                if (!StringHelper.isNullOrEmpty(zFileName_ITCH_MDF) && (mController_ITCH_MDF != null)){
                    String zDataFileName = zFileName_ITCH_MDF;
                    ITMFileAccessForFastRead mFileRdr = new ITMFileAccessForFastRead();
                    if (mFileRdr.openFileForReadLinesFromFirst(zDataFileName)){
                        try{
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Read Data File to restore BEGIN: " + zDataFileName);
                            String zDataLine;
                            ITMSoupBinTCPBridgeBackupMsgBase mMsg;
                            boolean bNeedToStop = false;
                            while ((zDataLine = mFileRdr.readLine()) != null){
                                mMsg = ITMSoupBinTCPBridgeBackupParser.getInstance.parsePacket(zDataLine);
                                if (mMsg != null && mMsg.isHeaderParsed && mMsg.isContentParsed && !mMsg.isOutput && (zTodayStr.equals(mMsg.saveDateStr))){
                                    switch (eDataFileRestoreMode) {
                                        case ONLY_LAST_SEQ_AND_MESSAGE:
                                            mController_ITCH_MDF.setCurrentSequencedNo(mMsg.recordNo);
                                            mController_ITCH_MDF.setRecentSequencedMsg(mMsg.sbMessage);
                                            break;
                                        case FULL_RESTORE:
                                            mController_ITCH_MDF.setCurrentSequencedNo(mMsg.recordNo);
                                            mController_ITCH_MDF.setRecentSequencedMsg(mMsg.sbMessage);
                                            //... .
                                            
                                            ITMITCHMsgMemory.getInstance.itchMDFMapMessage(mMsg.arbMessage, mMsg.sbMessage);
                                            
                                            break;
                                        default:
                                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Invalid Restore Mode Error: Mode=" + eDataFileRestoreMode + ", zDataFileName=" + zDataFileName + ", isHeaderParsed=" + mMsg.isHeaderParsed + ", isContentParsed=" + mMsg.isContentParsed + ", isOutput=" + mMsg.isOutput);
                                            bNeedToStop = true;
                                            break;
                                    }
                                }else{
                                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "Data File Parse Error: " + zDataFileName + ", mMsg_IsNotNull=" + (mMsg != null) + ", isHeaderParsed=" + mMsg.isHeaderParsed + ", isContentParsed=" + mMsg.isContentParsed + ", isOutput=" + mMsg.isOutput + ", saveDateStr=" + mMsg.saveDateStr);
                                    bNeedToStop = true;
                                }
                                if (bNeedToStop) { break; }
                            }
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Read Data File to restore END: " + zDataFileName);                            
                        }catch(Exception ex0){
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
                        }
                        mFileRdr.closeFile();
                    }else{
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Data File to restore not found: " + zDataFileName);
                    }
                }
                
                //.flags:
                this.bAlreadyProcessed = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
}


