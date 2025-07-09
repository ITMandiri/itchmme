/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.msgparser;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.mis.itch.consts.ITCHConsts;
import com.itm.mis.itch.structs.ITCHMsgAddOrder;
import com.itm.mis.itch.structs.ITCHMsgBase;
import com.itm.mis.itch.structs.ITCHMsgEquilibriumPrice;
import com.itm.mis.itch.structs.ITCHMsgGlimpseSnapshot;
import com.itm.mis.itch.structs.ITCHMsgOrderBookClear;
import com.itm.mis.itch.structs.ITCHMsgOrderBookDirectory;
import com.itm.mis.itch.structs.ITCHMsgOrderBookState;
import com.itm.mis.itch.structs.ITCHMsgOrderDelete;
import com.itm.mis.itch.structs.ITCHMsgOrderExecuted;
import com.itm.mis.itch.structs.ITCHMsgOrderExecutedWithPrice;
import com.itm.mis.itch.structs.ITCHMsgSecond;
import com.itm.mis.itch.structs.ITCHMsgSystemEvent;
import com.itm.mis.itch.structs.ITCHMsgTickSizeTable;
import com.itm.mis.itch.structs.ITCHMsgTrade;
import com.itm.mis.itch.structs.ITCHMsgUnknown;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPLength;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgSequencedDataPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgUnsequencedDataPacket;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPITCHMsgParser {
    
    public final static ITMSoupBinTCPITCHMsgParser getInstance = new ITMSoupBinTCPITCHMsgParser();
    
    public ITCHMsgBase parseMessage(byte[] btMessageBytes, ITMSoupBinTCPMsgBase mSoupBinPacketObject){
        ITCHMsgBase mOut = null;
        try{
            //. hrn:2021-07-22 server heartbeat tidak diparsing disini
            if ((btMessageBytes != null) && (btMessageBytes.length > SoupBinTCPLength.MINIMUM_PACKET_SIZE)){
                String vMsgType = String.valueOf((char)btMessageBytes[SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD]);
                if (mSoupBinPacketObject instanceof ITMSoupBinTCPMsgSequencedDataPacket){
                    switch(vMsgType){
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_SECONDS_MESSAGE:
                            ITCHMsgSecond mObjTimeStamp = new ITCHMsgSecond();
                            mObjTimeStamp.parseMessage(btMessageBytes);
                            mOut = mObjTimeStamp;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_SYSTEM_EVENT:
                            ITCHMsgSystemEvent mObjSystemEvent = new ITCHMsgSystemEvent();
                            mObjSystemEvent.parseMessage(btMessageBytes);
                            mOut = mObjSystemEvent;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_TICK_SIZE_TABLE:
                            ITCHMsgTickSizeTable mObjPriceTickSizeTable = new ITCHMsgTickSizeTable();
                            mObjPriceTickSizeTable.parseMessage(btMessageBytes);
                            mOut = mObjPriceTickSizeTable;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_ORDER_BOOK_DIRECTORY:
                            ITCHMsgOrderBookDirectory mObjOrderBookDirectory = new ITCHMsgOrderBookDirectory();
                            mObjOrderBookDirectory.parseMessage(btMessageBytes);
                            mOut = mObjOrderBookDirectory;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_EQUILIBRIUM_PRICE:
                            ITCHMsgEquilibriumPrice mObjEquilibriumPrice = new ITCHMsgEquilibriumPrice();
                            mObjEquilibriumPrice.parseMessage(btMessageBytes);
                            mOut = mObjEquilibriumPrice;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_ORDER_BOOK_STATE:
                            ITCHMsgOrderBookState mObjOrderBookState = new ITCHMsgOrderBookState();
                            mObjOrderBookState.parseMessage(btMessageBytes);
                            mOut = mObjOrderBookState;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_ADD_ORDER:
                            ITCHMsgAddOrder mObjAddOrder = new ITCHMsgAddOrder();
                            mObjAddOrder.parseMessage(btMessageBytes);
                            mOut = mObjAddOrder;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_ORDER_EXECUTED:
                            ITCHMsgOrderExecuted mObjOrderExecuted = new ITCHMsgOrderExecuted();
                            mObjOrderExecuted.parseMessage(btMessageBytes);
                            mOut = mObjOrderExecuted;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_ORDER_EXECUTED_WITH_PRICE:
                            ITCHMsgOrderExecutedWithPrice mObjOrderExecutedWithPrice = new ITCHMsgOrderExecutedWithPrice();
                            mObjOrderExecutedWithPrice.parseMessage(btMessageBytes);
                            mOut = mObjOrderExecutedWithPrice;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_TRADE:
                            ITCHMsgTrade mObjTrade = new ITCHMsgTrade();
                            mObjTrade.parseMessage(btMessageBytes);
                            mOut = mObjTrade;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_ORDER_DELETE:
                            ITCHMsgOrderDelete mObjOrderDelete = new ITCHMsgOrderDelete();
                            mObjOrderDelete.parseMessage(btMessageBytes);
                            mOut = mObjOrderDelete;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_ORDER_BOOK_CLEAR:
                            ITCHMsgOrderBookClear mObjOrderBookClear = new ITCHMsgOrderBookClear();
                            mObjOrderBookClear.parseMessage(btMessageBytes);
                            mOut = mObjOrderBookClear;
                            break;
                        case ITCHConsts.ITCHMessageType.MESSAGETYPE_GLIMPSE_SNAPSHOT:
                            ITCHMsgGlimpseSnapshot mObjGlimpseSnapshot = new ITCHMsgGlimpseSnapshot();
                            mObjGlimpseSnapshot.parseMessage(btMessageBytes);
                            mOut = mObjGlimpseSnapshot;
                            break;
                        default:
                            System.out.println(vMsgType);
                    }
                } else if (mSoupBinPacketObject instanceof ITMSoupBinTCPMsgUnsequencedDataPacket){
                    switch(vMsgType){
                        
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        if (mOut == null){
            mOut = new ITCHMsgUnknown();
        }
        return mOut;
    }
    
}
