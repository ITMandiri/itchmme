/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.dynamicstruct;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeDynStructValidator {
    
    public final static ITMSoupBinTCPBridgeDynStructValidator getInstance = new ITMSoupBinTCPBridgeDynStructValidator();
    
    public final static List<String> LIST_MESSAGE_TYPES = Arrays.asList(new String[] {
                                 "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
                                ,"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
                                ,"0","1","2","3","4","5","6","7","8","9"
                                });
    public final static List<String> LIST_MESSAGE_DIRECTIONS = Arrays.asList(new String[] {"IN", "OUT"});
    public final static List<String> LIST_MESSAGE_FIELD_TYPES = Arrays.asList(new String[] {"Token", "Alpha", "Integer", "Price", "Null-Terminated Alpha"});
    public final static List<String> LIST_SOURCE_TYPES = Arrays.asList(new String[] {"pair"});
    
    public boolean validateDynStruct(ITMSoupBinTCPDynStructBase mDynStruct, ArrayList<String> arrErrorMsgs){
        boolean mOut = false;
        try{
            arrErrorMsgs.clear();
            if (mDynStruct == null){
                arrErrorMsgs.add("Dynamic Struct is NULL");
            } else if ((!(mDynStruct instanceof ITMSoupBinTCPDynStructITCH)) && (!(mDynStruct instanceof ITMSoupBinTCPDynStructOUCH))){
                arrErrorMsgs.add("Unknown instance.");
            } else {
                ITMSoupBinTCPDynStructCore mCore = ((mDynStruct instanceof ITMSoupBinTCPDynStructITCH) ? ((ITMSoupBinTCPDynStructITCH)mDynStruct).itch : ((ITMSoupBinTCPDynStructOUCH)mDynStruct).ouch);
                if (mCore == null){
                    arrErrorMsgs.add("Core is NULL.");
                }else{
                    if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(mCore.version)){
                        arrErrorMsgs.add("Core.version is empty.");
                    }
                    if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(mCore.specification)){
                        arrErrorMsgs.add("Core.specification is empty.");
                    }
                    if (mCore.messages == null){
                        arrErrorMsgs.add("Core.messages is NULL.");
                    } else if (mCore.messages.length <= 0){
                        arrErrorMsgs.add("Core.messages is empty.");
                    } else {
                        int cmhIndex = 0;
                        for (ITMSoupBinTCPDynStructCore.CCS_message eachMessage : mCore.messages) {
                            if (eachMessage == null){
                                arrErrorMsgs.add("Message at index " + cmhIndex + " NULL.");
                            } else {
                                if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachMessage.type)){
                                    arrErrorMsgs.add("Message.type at index " + cmhIndex + " is empty.");
                                } else if (eachMessage.type.length() > 1){
                                    arrErrorMsgs.add("Message.type at index " + cmhIndex + " is more than 1 character: '" + eachMessage.type + "'.");
                                } else if (!LIST_MESSAGE_TYPES.contains(eachMessage.type)){
                                    arrErrorMsgs.add("Message.type at index " + cmhIndex + " is unknown: '" + eachMessage.type + "'.");
                                }
                                if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachMessage.name)){
                                    arrErrorMsgs.add("Message.name at index " + cmhIndex + " is empty.");
                                }
                                if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachMessage.direction)){
                                    arrErrorMsgs.add("Message.direction at index " + cmhIndex + " is empty.");
                                } else if (!LIST_MESSAGE_DIRECTIONS.contains(eachMessage.direction)){
                                    arrErrorMsgs.add("Message.direction at index " + cmhIndex + " is unknown: '" + eachMessage.direction + "'.");
                                }
                                if (eachMessage.fields == null){
                                    arrErrorMsgs.add("Message.fields is NULL.");
                                } else if (eachMessage.fields.length <= 0){
                                    arrErrorMsgs.add("Message.fields is empty.");
                                } else {
                                    int cmdIndex = 0;
                                    int cmdCumulOffset = 0;
                                    for (ITMSoupBinTCPDynStructCore.CCS_message.CCS_message_field eachField : eachMessage.fields) {
                                        if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachField.name)){
                                            arrErrorMsgs.add("Message.Field.name at index " + cmhIndex + " field index " + cmdIndex + " is empty.");
                                        }
                                        if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachField.type)){
                                            arrErrorMsgs.add("Message.Field.type at index " + cmhIndex + " field index " + cmdIndex + " is empty.");
                                        } else if (!LIST_MESSAGE_FIELD_TYPES.contains(eachField.type)){
                                            arrErrorMsgs.add("Message.Field.type at index " + cmhIndex + " field index " + cmdIndex + " is unknown: '" + eachField.type + "'.");
                                        }
                                        if (eachField.offset < 0){
                                            arrErrorMsgs.add("Message.Field.offset at index " + cmhIndex + " field index " + cmdIndex + " is less than 0: '" + eachField.offset + "'.");
                                        } else if (eachField.offset != cmdCumulOffset){
                                            arrErrorMsgs.add("Message.Field.offset at index " + cmhIndex + " field index " + cmdIndex + " is not equal cumulative offset " + cmdCumulOffset + ": '" + eachField.offset + "'.");
                                        } else {
                                            cmdCumulOffset += eachField.len;
                                        }
                                        if (eachField.len <= 0){
                                            arrErrorMsgs.add("Message.Field.len at index " + cmhIndex + " field index " + cmdIndex + " is less or equal 0: '" + eachField.len + "'.");
                                        }
                                        if (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachField.source)){
                                            if ((mCore.sources == null) || (mCore.sources.length <= 0)){
                                                arrErrorMsgs.add("Message.Field.source at index " + cmhIndex + " field index " + cmdIndex + " is not found: '" + eachField.source + "'.");
                                            } else {
                                                boolean bFieldSourceFound = false;
                                                for (ITMSoupBinTCPDynStructCore.CCS_source eachSource : mCore.sources) {
                                                    if ((eachSource != null) && (!ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachSource.code))){
                                                        if (eachSource.code.equals(eachField.source)){
                                                            bFieldSourceFound = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (!bFieldSourceFound){
                                                    arrErrorMsgs.add("Message.Field.source at index " + cmhIndex + " field index " + cmdIndex + " is not found: '" + eachField.source + "'.");
                                                }
                                            }
                                        }
                                        cmdIndex++;
                                    }
                                }
                            }
                            cmhIndex++;
                        }
                    }
                    if (mCore.sources == null){
                        arrErrorMsgs.add("Core.sources is NULL.");
                    } else if (mCore.sources.length <= 0){
                        arrErrorMsgs.add("Core.sources is empty.");
                    } else {
                        int cshIndex = 0;
                        for (ITMSoupBinTCPDynStructCore.CCS_source eachSource : mCore.sources) {
                            if (eachSource == null){
                                arrErrorMsgs.add("Source at index " + cshIndex + " NULL.");
                            } else {
                                if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachSource.code)){
                                    arrErrorMsgs.add("Source.code at index " + cshIndex + " is empty.");
                                }
                                if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachSource.type)){
                                    arrErrorMsgs.add("Source.type at index " + cshIndex + " is empty.");
                                } else if (!LIST_SOURCE_TYPES.contains(eachSource.type)){
                                    arrErrorMsgs.add("Source.type at index " + cshIndex + " is unknown: '" + eachSource.type + "'.");
                                }
                                if (eachSource.items == null){
                                    arrErrorMsgs.add("Source.items is NULL.");
                                } else if (eachSource.items.length <= 0){
                                    arrErrorMsgs.add("Source.items is empty.");
                                } else {
                                    int csdIndex = 0;
                                    for (ITMSoupBinTCPDynStructCore.CCS_source.CCS_source_item eachItem : eachSource.items) {
                                        if (ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty(eachItem.value)){
                                            arrErrorMsgs.add("Source.item.value at index " + cshIndex + " item index " + csdIndex + " is empty.");
                                        }
                                        csdIndex++;
                                    }
                                }
                            }
                            cshIndex++;
                        }
                    }
                }
            }
            mOut = arrErrorMsgs.isEmpty();
        }catch(Exception ex0){
            arrErrorMsgs.add((ex0.getMessage() != null ? ex0.getMessage() : "NULL Exception Message.") + Arrays.toString(ex0.getStackTrace()));
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
