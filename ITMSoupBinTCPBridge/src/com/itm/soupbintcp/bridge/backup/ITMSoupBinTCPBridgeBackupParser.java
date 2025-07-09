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
import com.itm.soupbintcp.bridge.packetparser.ITMSoupBinTCPBridgePacketParser;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgUnknownDataPacket;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeBackupParser {
    
    public final static ITMSoupBinTCPBridgeBackupParser getInstance = new ITMSoupBinTCPBridgeBackupParser();
    
    public ITMSoupBinTCPBridgeBackupMsgBase parsePacket(String zBackupLine){
        ITMSoupBinTCPBridgeBackupMsgBase mOut = new ITMSoupBinTCPBridgeBackupMsgBase();
        try{
            if ((!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(zBackupLine)) 
                    && (zBackupLine.startsWith(ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_PREFIX))
                    && (zBackupLine.endsWith(ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_SUFFIX))
                ){
                String[] arrFields = ITMSoupBinTCPBridgePacketFormat.splitAll(zBackupLine, ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER);
                if (arrFields.length >= ITMSoupBinTCPBridgeBackupConsts.SOUPBINTCP_BACKUP_MINIMUM_LINE_FIELDS_COUNT){
                    //////mOut.saveDate = ITMSoupBinTCPBridgePacketFormat.parseToDateTimeSVRFormat(arrFields[1] + " " + arrFields[2]); //.do not use this! make system slow;
                    mOut.saveDateStr = arrFields[1];
                    mOut.saveTimeStr = arrFields[2];
                    mOut.isOutput = ("O".equals(arrFields[3]));
                    mOut.recordNo = ITMSoupBinTCPBridgePacketFormat.toLong(arrFields[4]);
                    mOut.msgType = arrFields[5];
                    mOut.arbMessage = ITMSoupBinTCPBridgePacketFormat.convertHexStringToBytes(arrFields[6]);
                    if ((mOut.saveDateStr != null) && (mOut.saveTimeStr != null) && (mOut.arbMessage != null) && (mOut.arbMessage.length > 0)){
                        mOut.sbMessage = ITMSoupBinTCPBridgePacketParser.getInstance.parsePacket(mOut.arbMessage);
                        if (mOut.sbMessage != null){
                            mOut.isHeaderParsed = true;
                            if (!(mOut.sbMessage instanceof ITMSoupBinTCPMsgUnknownDataPacket)){
                                mOut.isContentParsed = true;
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
}
