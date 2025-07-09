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
import com.itm.mis.itch.structs.ITCHMsgBase;
import com.itm.mis.itch.structs.ITCHMsgCircuitBreakerTrigger;
import com.itm.mis.itch.structs.ITCHMsgEquilibriumPrice;
import com.itm.mis.itch.structs.ITCHMsgExchangeDirectory;
import com.itm.mis.itch.structs.ITCHMsgIndexMember;
import com.itm.mis.itch.structs.ITCHMsgIndexPrice;
import com.itm.mis.itch.structs.ITCHMsgIndicativeQuote;
import com.itm.mis.itch.structs.ITCHMsgIssuerDirectory;
import com.itm.mis.itch.structs.ITCHMsgMarketByPrice;
import com.itm.mis.itch.structs.ITCHMsgMarketDirectory;
import com.itm.mis.itch.structs.ITCHMsgMarketSegmentDirectory;
import com.itm.mis.itch.structs.ITCHMsgOrderBookDirectoryMDF;
import com.itm.mis.itch.structs.ITCHMsgOrderBookState;
import com.itm.mis.itch.structs.ITCHMsgParticipantDirectory;
import com.itm.mis.itch.structs.ITCHMsgPriceLimits;
import com.itm.mis.itch.structs.ITCHMsgReferencePrice;
import com.itm.mis.itch.structs.ITCHMsgSecond;
import com.itm.mis.itch.structs.ITCHMsgSystemEvent;
import com.itm.mis.itch.structs.ITCHMsgTickSizeTable;
import com.itm.mis.itch.structs.ITCHMsgTradeStatistics;
import com.itm.mis.itch.structs.ITCHMsgTradeTicker;
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
public class ITMSoupBinTCPITCHMDFMsgParser {
    
    public final static ITMSoupBinTCPITCHMDFMsgParser getInstance = new ITMSoupBinTCPITCHMDFMsgParser();
    
    public ITCHMsgBase parseMessage(byte[] btMessageBytes, ITMSoupBinTCPMsgBase mSoupBinPacketObject){
        ITCHMsgBase mOut = null;
        try{
            //. hrn:2021-07-22 server heartbeat tidak diparsing disini
            if ((btMessageBytes != null) && (btMessageBytes.length > SoupBinTCPLength.MINIMUM_PACKET_SIZE)){
                String vMsgType = String.valueOf((char)btMessageBytes[SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD]);
                if (mSoupBinPacketObject instanceof ITMSoupBinTCPMsgSequencedDataPacket){
                    switch(vMsgType){
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_SECONDS_MESSAGE:
                            ITCHMsgSecond mObjTimeStamp = new ITCHMsgSecond();
                            mObjTimeStamp.parseMessage(btMessageBytes);
                            mOut = mObjTimeStamp;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_SYSTEM_EVENT:
                            ITCHMsgSystemEvent mObjSystemEvent = new ITCHMsgSystemEvent();
                            mObjSystemEvent.parseMessage(btMessageBytes);
                            mOut = mObjSystemEvent;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_TICK_SIZE_TABLE:
                            ITCHMsgTickSizeTable mObjPriceTickSizeTable = new ITCHMsgTickSizeTable();
                            mObjPriceTickSizeTable.parseMessage(btMessageBytes);
                            mOut = mObjPriceTickSizeTable;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_ISSUER_DIRECTORY:
                            ITCHMsgIssuerDirectory mObjIssuerDirectory = new ITCHMsgIssuerDirectory();
                            mObjIssuerDirectory.parseMessage(btMessageBytes);
                            mOut = mObjIssuerDirectory;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_ORDER_BOOK_DIRECTORY:
                            ITCHMsgOrderBookDirectoryMDF mObjOrderBookDirectory = new ITCHMsgOrderBookDirectoryMDF();
                            mObjOrderBookDirectory.parseMessage(btMessageBytes);
                            mOut = mObjOrderBookDirectory;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_INDEX_PRICE:
                            ITCHMsgIndexPrice mObjIndexPrice = new ITCHMsgIndexPrice();
                            mObjIndexPrice.parseMessage(btMessageBytes);
                            mOut = mObjIndexPrice;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_EQUILIBRIUM_PRICE:
                            ITCHMsgEquilibriumPrice mObjEquilibriumPrice = new ITCHMsgEquilibriumPrice();
                            mObjEquilibriumPrice.parseMessage(btMessageBytes);
                            mOut = mObjEquilibriumPrice;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_ORDER_BOOK_STATE:
                            ITCHMsgOrderBookState mObjOrderBookState = new ITCHMsgOrderBookState();
                            mObjOrderBookState.parseMessage(btMessageBytes);
                            mOut = mObjOrderBookState;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_PARTICIPANT_DIRECTORY:
                            ITCHMsgParticipantDirectory mObjParticipantDirectory = new ITCHMsgParticipantDirectory();
                            mObjParticipantDirectory.parseMessage(btMessageBytes);
                            mOut = mObjParticipantDirectory;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_REFERENCE_PRICE:
                            ITCHMsgReferencePrice mObjReferencePrice = new ITCHMsgReferencePrice();
                            mObjReferencePrice.parseMessage(btMessageBytes);
                            mOut = mObjReferencePrice;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_EXCHANGE_DIRECTORY:
                            ITCHMsgExchangeDirectory mObjExchangeDirectory = new ITCHMsgExchangeDirectory();
                            mObjExchangeDirectory.parseMessage(btMessageBytes);
                            mOut = mObjExchangeDirectory;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_MARKET_DIRECTORY:
                            ITCHMsgMarketDirectory mObjMarketDirectory = new ITCHMsgMarketDirectory();
                            mObjMarketDirectory.parseMessage(btMessageBytes);
                            mOut = mObjMarketDirectory;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_MARKET_SEGMENT_DIRECTORY:
                            ITCHMsgMarketSegmentDirectory mObjMarketSegmentDirectory = new ITCHMsgMarketSegmentDirectory();
                            mObjMarketSegmentDirectory.parseMessage(btMessageBytes);
                            mOut = mObjMarketSegmentDirectory;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_MARKET_BY_PRICE:
                            ITCHMsgMarketByPrice mObjMarketByPrice = new ITCHMsgMarketByPrice();
                            mObjMarketByPrice.parseMessage(btMessageBytes);
                            mOut = mObjMarketByPrice;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_TRADE_STATISTICS:
                            ITCHMsgTradeStatistics mObjTradeStatistics = new ITCHMsgTradeStatistics();
                            mObjTradeStatistics.parseMessage(btMessageBytes);
                            mOut = mObjTradeStatistics;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_TRADE_TICKER:
                            ITCHMsgTradeTicker mObjTradeTicker = new ITCHMsgTradeTicker();
                            mObjTradeTicker.parseMessage(btMessageBytes);
                            mOut = mObjTradeTicker;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_PRICE_LIMITS:
                            ITCHMsgPriceLimits mObjPriceLimits = new ITCHMsgPriceLimits();
                            mObjPriceLimits.parseMessage(btMessageBytes);
                            mOut = mObjPriceLimits;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_CIRCUIT_BREAKER_TRIGGER:
                            ITCHMsgCircuitBreakerTrigger mObjCircuitBreakerTrigger = new ITCHMsgCircuitBreakerTrigger();
                            mObjCircuitBreakerTrigger.parseMessage(btMessageBytes);
                            mOut = mObjCircuitBreakerTrigger;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_INDICATIVE_QUOTE:
                            ITCHMsgIndicativeQuote mObjIndicativeQuote = new ITCHMsgIndicativeQuote();
                            mObjIndicativeQuote.parseMessage(btMessageBytes);
                            mOut = mObjIndicativeQuote;
                            break;
                        case ITCHConsts.ITCHMDFMessageType.MESSAGETYPE_INDEX_MEMBER:
                            ITCHMsgIndexMember mObjIndexMember = new ITCHMsgIndexMember();
                            mObjIndexMember.parseMessage(btMessageBytes);
                            mOut = mObjIndexMember;
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
