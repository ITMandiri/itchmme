/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.msgmemory;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.mis.itch.books.BookOfITCHAddOrder;
import com.itm.mis.itch.books.BookOfITCHCircuitBreakerTrigger;
import com.itm.mis.itch.books.BookOfITCHEquilibriumPrice;
import com.itm.mis.itch.books.BookOfITCHExchangeDirectory;
import com.itm.mis.itch.books.BookOfITCHGlimpseSnapshot;
import com.itm.mis.itch.books.BookOfITCHIndexPrice;
import com.itm.mis.itch.books.BookOfITCHIndicativeQuote;
import com.itm.mis.itch.books.BookOfITCHIssuerDirectory;
import com.itm.mis.itch.books.BookOfITCHMarketByPrice;
import com.itm.mis.itch.books.BookOfITCHMarketDirectory;
import com.itm.mis.itch.books.BookOfITCHMarketSegmentDirectory;
import com.itm.mis.itch.books.BookOfITCHOrderBookClear;
import com.itm.mis.itch.books.BookOfITCHOrderBookDirectory;
import com.itm.mis.itch.books.BookOfITCHOrderBookDirectoryMDF;
import com.itm.mis.itch.books.BookOfITCHOrderBookState;
import com.itm.mis.itch.books.BookOfITCHOrderDelete;
import com.itm.mis.itch.books.BookOfITCHOrderExecuted;
import com.itm.mis.itch.books.BookOfITCHOrderExecutedWithPrice;
import com.itm.mis.itch.books.BookOfITCHParticipantDirectory;
import com.itm.mis.itch.books.BookOfITCHPriceLimits;
import com.itm.mis.itch.books.BookOfITCHReferencePrice;
import com.itm.mis.itch.books.BookOfITCHSystemEvent;
import com.itm.mis.itch.books.BookOfITCHTickSizeTable;
import com.itm.mis.itch.books.BookOfITCHTrade;
import com.itm.mis.itch.books.BookOfITCHTradeStatistics;
import com.itm.mis.itch.books.BookOfITCHTradeTicker;
import com.itm.mis.itch.books.SheetOfITCHAddOrder;
import com.itm.mis.itch.books.SheetOfITCHBase;
import com.itm.mis.itch.books.SheetOfITCHCircuitBreakerTrigger;
import com.itm.mis.itch.books.SheetOfITCHEquilibriumPrice;
import com.itm.mis.itch.books.SheetOfITCHExchangeDirectory;
import com.itm.mis.itch.books.SheetOfITCHGlimpseSnapshot;
import com.itm.mis.itch.books.SheetOfITCHIndexPrice;
import com.itm.mis.itch.books.SheetOfITCHIndicativeQuote;
import com.itm.mis.itch.books.SheetOfITCHIssuerDirectory;
import com.itm.mis.itch.books.SheetOfITCHMarketByPrice;
import com.itm.mis.itch.books.SheetOfITCHMarketDirectory;
import com.itm.mis.itch.books.SheetOfITCHMarketSegmentDirectory;
import com.itm.mis.itch.books.SheetOfITCHOrderBookClear;
import com.itm.mis.itch.books.SheetOfITCHOrderBookDirectory;
import com.itm.mis.itch.books.SheetOfITCHOrderBookDirectoryMDF;
import com.itm.mis.itch.books.SheetOfITCHOrderBookState;
import com.itm.mis.itch.books.SheetOfITCHOrderDelete;
import com.itm.mis.itch.books.SheetOfITCHOrderExecuted;
import com.itm.mis.itch.books.SheetOfITCHOrderExecutedWithPrice;
import com.itm.mis.itch.books.SheetOfITCHParticipantDirectory;
import com.itm.mis.itch.books.SheetOfITCHPriceLimits;
import com.itm.mis.itch.books.SheetOfITCHReferencePrice;
import com.itm.mis.itch.books.SheetOfITCHSystemEvent;
import com.itm.mis.itch.books.SheetOfITCHTickSizeTable;
import com.itm.mis.itch.books.SheetOfITCHTrade;
import com.itm.mis.itch.books.SheetOfITCHTradeStatistics;
import com.itm.mis.itch.books.SheetOfITCHTradeTicker;
import com.itm.mis.itch.bridge.ITMITCHMsgMemoryMgr;
import com.itm.mis.itch.consts.ITCHConsts.ITCHValue;
import com.itm.mis.itch.msgparser.ITMSoupBinTCPITCHMDFMsgParser;
import com.itm.mis.itch.msgparser.ITMSoupBinTCPITCHMsgParser;
import com.itm.mis.itch.structs.ITCHMsgAddOrder;
import com.itm.mis.itch.structs.ITCHMsgBase;
import com.itm.mis.itch.structs.ITCHMsgCircuitBreakerTrigger;
import com.itm.mis.itch.structs.ITCHMsgEquilibriumPrice;
import com.itm.mis.itch.structs.ITCHMsgExchangeDirectory;
import com.itm.mis.itch.structs.ITCHMsgGlimpseSnapshot;
import com.itm.mis.itch.structs.ITCHMsgIndexPrice;
import com.itm.mis.itch.structs.ITCHMsgIndicativeQuote;
import com.itm.mis.itch.structs.ITCHMsgIssuerDirectory;
import com.itm.mis.itch.structs.ITCHMsgMarketByPrice;
import com.itm.mis.itch.structs.ITCHMsgMarketDirectory;
import com.itm.mis.itch.structs.ITCHMsgMarketSegmentDirectory;
import com.itm.mis.itch.structs.ITCHMsgOrderBookClear;
import com.itm.mis.itch.structs.ITCHMsgOrderBookDirectory;
import com.itm.mis.itch.structs.ITCHMsgOrderBookDirectoryMDF;
import com.itm.mis.itch.structs.ITCHMsgOrderBookState;
import com.itm.mis.itch.structs.ITCHMsgOrderDelete;
import com.itm.mis.itch.structs.ITCHMsgOrderExecuted;
import com.itm.mis.itch.structs.ITCHMsgOrderExecutedWithPrice;
import com.itm.mis.itch.structs.ITCHMsgParticipantDirectory;
import com.itm.mis.itch.structs.ITCHMsgPriceLimits;
import com.itm.mis.itch.structs.ITCHMsgReferencePrice;
import com.itm.mis.itch.structs.ITCHMsgSystemEvent;
import com.itm.mis.itch.structs.ITCHMsgTickSizeTable;
import com.itm.mis.itch.structs.ITCHMsgTrade;
import com.itm.mis.itch.structs.ITCHMsgTradeStatistics;
import com.itm.mis.itch.structs.ITCHMsgTradeTicker;
import com.itm.mis.itch.structs.ITCHMsgUnknown;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.ITCHType;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;

/**
 *
 * @author Ari Pambudi
 */
public class ITMITCHMsgMemory {
    //.single instance:
    public final static ITMITCHMsgMemory getInstance = new ITMITCHMsgMemory();
    
    public ITMITCHMsgMemory() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public boolean mapMessage(byte[] btMessageBytes, ITMSoupBinTCPMsgBase mSoupBinPacketObject){
        boolean mOut = false;
        try{
            //. hrn : 20210921 kala ada ITCHMsgOrderRelink, tapi tidak didahului OrderReplace, tidak perlu di proses
            //. karena dari preop, kita tidak mendapatkan OrderReplace, melainkan AddOrder hasil summary tapi relink tetep dapet
            
            SheetOfITCHBase mSheetBase = null;
            ITCHType itchType = ITCHType.ITCH;
            ITCHMsgBase itchMessage = ITMSoupBinTCPITCHMsgParser.getInstance.parseMessage(btMessageBytes, mSoupBinPacketObject);
            
            if (itchMessage == null){
                System.err.println("itchMessage is null");
            }
            if (itchMessage != null){
                //. 20220318 : event broadcast raw (tanpa skip), untuk splitter
                ITMITCHMsgMemoryMgr.getInstance.raiseOnMessageRaw(itchMessage);
            
                if (itchMessage instanceof ITCHMsgUnknown){
                    //... .
                } else if (itchMessage instanceof ITCHMsgSystemEvent){
                    ITCHMsgSystemEvent mMsg = (ITCHMsgSystemEvent)itchMessage;
                    if (ITCHValue.SYSTEM_EVENT_CODE_FIRST_OF_MESSAGE.equals(mMsg.getEvent())){
                        clearAllBooks();
                    }else if (ITCHValue.SYSTEM_EVENT_CODE_LAST_OF_MESSAGE.equals(mMsg.getEvent())){
                        //... .
                    }
                    SheetOfITCHSystemEvent mSheet = new SheetOfITCHSystemEvent(mMsg);
                    BookOfITCHSystemEvent.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgTickSizeTable){
                    ITCHMsgTickSizeTable mMsg = (ITCHMsgTickSizeTable)itchMessage;
                    SheetOfITCHTickSizeTable mSheet = new SheetOfITCHTickSizeTable(mMsg);
                    BookOfITCHTickSizeTable.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgOrderBookDirectory){
                    ITCHMsgOrderBookDirectory mMsg = (ITCHMsgOrderBookDirectory)itchMessage;
                    SheetOfITCHOrderBookDirectory mSheet = new SheetOfITCHOrderBookDirectory(mMsg);
                    BookOfITCHOrderBookDirectory.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                }  else if (itchMessage instanceof ITCHMsgEquilibriumPrice) {
                    ITCHMsgEquilibriumPrice mMsg = (ITCHMsgEquilibriumPrice) itchMessage;
                    SheetOfITCHEquilibriumPrice mSheet = new SheetOfITCHEquilibriumPrice(mMsg);
                    BookOfITCHEquilibriumPrice.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgOrderBookState) {
                    ITCHMsgOrderBookState mMsg = (ITCHMsgOrderBookState) itchMessage;
                    SheetOfITCHOrderBookState mSheet = new SheetOfITCHOrderBookState(mMsg);
                    BookOfITCHOrderBookState.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgAddOrder) {
                    ITCHMsgAddOrder mMsg = (ITCHMsgAddOrder) itchMessage;
                    SheetOfITCHAddOrder mSheet = new SheetOfITCHAddOrder(mMsg);
                    BookOfITCHAddOrder.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgOrderExecuted) {
                    ITCHMsgOrderExecuted mMsg = (ITCHMsgOrderExecuted) itchMessage;
                    SheetOfITCHOrderExecuted mSheet = new SheetOfITCHOrderExecuted(mMsg);
                    BookOfITCHOrderExecuted.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgOrderExecutedWithPrice) {
                    ITCHMsgOrderExecutedWithPrice mMsg = (ITCHMsgOrderExecutedWithPrice) itchMessage;
                    SheetOfITCHOrderExecutedWithPrice mSheet = new SheetOfITCHOrderExecutedWithPrice(mMsg);
                    BookOfITCHOrderExecutedWithPrice.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgTrade) {
                    ITCHMsgTrade mMsg = (ITCHMsgTrade) itchMessage;
                    SheetOfITCHTrade mSheet = new SheetOfITCHTrade(mMsg);
                    BookOfITCHTrade.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgOrderDelete) {
                    ITCHMsgOrderDelete mMsg = (ITCHMsgOrderDelete) itchMessage;
                    SheetOfITCHOrderDelete mSheet = new SheetOfITCHOrderDelete(mMsg);
                    BookOfITCHOrderDelete.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgOrderBookClear) {
                    ITCHMsgOrderBookClear mMsg = (ITCHMsgOrderBookClear) itchMessage;
                    SheetOfITCHOrderBookClear mSheet = new SheetOfITCHOrderBookClear(mMsg);
                    BookOfITCHOrderBookClear.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgGlimpseSnapshot) {
                    ITCHMsgGlimpseSnapshot mMsg = (ITCHMsgGlimpseSnapshot) itchMessage;
                    SheetOfITCHGlimpseSnapshot mSheet = new SheetOfITCHGlimpseSnapshot(mMsg);
                    BookOfITCHGlimpseSnapshot.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else {
                    // ...
                }
                //... .
            }
            //. broadcast event
            ITMITCHMsgMemoryMgr.getInstance.raiseOnMessage(itchMessage, mSheetBase, itchType);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean itchMDFMapMessage(byte[] btMessageBytes, ITMSoupBinTCPMsgBase mSoupBinPacketObject){
        boolean mOut = false;
        try{
            //. hrn : 20210921 kala ada ITCHMsgOrderRelink, tapi tidak didahului OrderReplace, tidak perlu di proses
            //. karena dari preop, kita tidak mendapatkan OrderReplace, melainkan AddOrder hasil summary tapi relink tetep dapet
            
            SheetOfITCHBase mSheetBase = null;
            ITCHType itchType = ITCHType.ITCH_MDF;
            ITCHMsgBase itchMessage = ITMSoupBinTCPITCHMDFMsgParser.getInstance.parseMessage(btMessageBytes, mSoupBinPacketObject);
            
            if (itchMessage == null){
                System.err.println("itchMessage is null");
            }
            if (itchMessage != null){
                //. 20220318 : event broadcast raw (tanpa skip), untuk splitter
                ITMITCHMsgMemoryMgr.getInstance.raiseOnMessageRaw(itchMessage);
            
                if (itchMessage instanceof ITCHMsgUnknown){
                    //... .
                } else if (itchMessage instanceof ITCHMsgSystemEvent){
                    ITCHMsgSystemEvent mMsg = (ITCHMsgSystemEvent)itchMessage;
                    if (ITCHValue.SYSTEM_EVENT_CODE_FIRST_OF_MESSAGE.equals(mMsg.getEvent())){
                        clearAllBooks();
                    }else if (ITCHValue.SYSTEM_EVENT_CODE_LAST_OF_MESSAGE.equals(mMsg.getEvent())){
                        //... .
                    }
                    SheetOfITCHSystemEvent mSheet = new SheetOfITCHSystemEvent(mMsg);
                    BookOfITCHSystemEvent.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgTickSizeTable){
                    ITCHMsgTickSizeTable mMsg = (ITCHMsgTickSizeTable)itchMessage;
                    SheetOfITCHTickSizeTable mSheet = new SheetOfITCHTickSizeTable(mMsg);
                    BookOfITCHTickSizeTable.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgIssuerDirectory){
                    ITCHMsgIssuerDirectory mMsg = (ITCHMsgIssuerDirectory)itchMessage;
                    SheetOfITCHIssuerDirectory mSheet = new SheetOfITCHIssuerDirectory(mMsg);
                    BookOfITCHIssuerDirectory.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgOrderBookDirectoryMDF){
                    ITCHMsgOrderBookDirectoryMDF mMsg = (ITCHMsgOrderBookDirectoryMDF)itchMessage;
                    SheetOfITCHOrderBookDirectoryMDF mSheet = new SheetOfITCHOrderBookDirectoryMDF(mMsg);
                    BookOfITCHOrderBookDirectoryMDF.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgIndexPrice){
                    ITCHMsgIndexPrice mMsg = (ITCHMsgIndexPrice)itchMessage;
                    SheetOfITCHIndexPrice mSheet = new SheetOfITCHIndexPrice(mMsg);
                    BookOfITCHIndexPrice.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                }  else if (itchMessage instanceof ITCHMsgEquilibriumPrice) {
                    ITCHMsgEquilibriumPrice mMsg = (ITCHMsgEquilibriumPrice) itchMessage;
                    SheetOfITCHEquilibriumPrice mSheet = new SheetOfITCHEquilibriumPrice(mMsg);
                    BookOfITCHEquilibriumPrice.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgOrderBookState) {
                    ITCHMsgOrderBookState mMsg = (ITCHMsgOrderBookState) itchMessage;
                    SheetOfITCHOrderBookState mSheet = new SheetOfITCHOrderBookState(mMsg);
                    BookOfITCHOrderBookState.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgParticipantDirectory) {
                    ITCHMsgParticipantDirectory mMsg = (ITCHMsgParticipantDirectory) itchMessage;
                    SheetOfITCHParticipantDirectory mSheet = new SheetOfITCHParticipantDirectory(mMsg);
                    BookOfITCHParticipantDirectory.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgReferencePrice) {
                    ITCHMsgReferencePrice mMsg = (ITCHMsgReferencePrice) itchMessage;
                    SheetOfITCHReferencePrice mSheet = new SheetOfITCHReferencePrice(mMsg);
                    BookOfITCHReferencePrice.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgExchangeDirectory) {
                    ITCHMsgExchangeDirectory mMsg = (ITCHMsgExchangeDirectory) itchMessage;
                    SheetOfITCHExchangeDirectory mSheet = new SheetOfITCHExchangeDirectory(mMsg);
                    BookOfITCHExchangeDirectory.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgMarketDirectory) {
                    ITCHMsgMarketDirectory mMsg = (ITCHMsgMarketDirectory) itchMessage;
                    SheetOfITCHMarketDirectory mSheet = new SheetOfITCHMarketDirectory(mMsg);
                    BookOfITCHMarketDirectory.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgMarketSegmentDirectory) {
                    ITCHMsgMarketSegmentDirectory mMsg = (ITCHMsgMarketSegmentDirectory) itchMessage;
                    SheetOfITCHMarketSegmentDirectory mSheet = new SheetOfITCHMarketSegmentDirectory(mMsg);
                    BookOfITCHMarketSegmentDirectory.getInstance.addOrUpdateSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgMarketByPrice) {
                    ITCHMsgMarketByPrice mMsg = (ITCHMsgMarketByPrice) itchMessage;
                    SheetOfITCHMarketByPrice mSheet = new SheetOfITCHMarketByPrice(mMsg);
                    BookOfITCHMarketByPrice.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgTradeStatistics) {
                    ITCHMsgTradeStatistics mMsg = (ITCHMsgTradeStatistics) itchMessage;
                    SheetOfITCHTradeStatistics mSheet = new SheetOfITCHTradeStatistics(mMsg);
                    BookOfITCHTradeStatistics.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgTradeTicker) {
                    ITCHMsgTradeTicker mMsg = (ITCHMsgTradeTicker) itchMessage;
                    SheetOfITCHTradeTicker mSheet = new SheetOfITCHTradeTicker(mMsg);
                    BookOfITCHTradeTicker.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgPriceLimits) {
                    ITCHMsgPriceLimits mMsg = (ITCHMsgPriceLimits) itchMessage;
                    SheetOfITCHPriceLimits mSheet = new SheetOfITCHPriceLimits(mMsg);
                    BookOfITCHPriceLimits.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgCircuitBreakerTrigger) {
                    ITCHMsgCircuitBreakerTrigger mMsg = (ITCHMsgCircuitBreakerTrigger) itchMessage;
                    SheetOfITCHCircuitBreakerTrigger mSheet = new SheetOfITCHCircuitBreakerTrigger(mMsg);
                    BookOfITCHCircuitBreakerTrigger.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else if (itchMessage instanceof ITCHMsgIndicativeQuote) {
                    ITCHMsgIndicativeQuote mMsg = (ITCHMsgIndicativeQuote) itchMessage;
                    SheetOfITCHIndicativeQuote mSheet = new SheetOfITCHIndicativeQuote(mMsg);
                    BookOfITCHIndicativeQuote.getInstance.addSheet(mSheet);
                    mSheetBase = mSheet;
                } else {
                    // ...
                }
                //... .
            }
            //. broadcast event
            ITMITCHMsgMemoryMgr.getInstance.raiseOnMessage(itchMessage, mSheetBase, itchType);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearAllBooks(){
        boolean mOut = false;
        try{
            
            
            //. ??????????????????????
            ///FEEDMsgHelper.getInstance.clearMemory();
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
