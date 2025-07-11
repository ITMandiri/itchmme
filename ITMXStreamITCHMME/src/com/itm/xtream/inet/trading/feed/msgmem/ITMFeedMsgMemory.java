/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.msgmem;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.mis.itch.books.BookOfITCHEquilibriumPrice;
import com.itm.mis.itch.books.BookOfITCHIssuerDirectory;
import com.itm.mis.itch.books.BookOfITCHMarketSegmentDirectory;
import com.itm.mis.itch.books.BookOfITCHOrderBookDirectoryMDF;
import com.itm.mis.itch.books.BookOfITCHOrderBookState;
import com.itm.mis.itch.books.BookOfITCHParticipantDirectory;
import com.itm.mis.itch.books.SheetOfITCHBase;
import com.itm.mis.itch.books.SheetOfITCHEquilibriumPrice;
import com.itm.mis.itch.books.SheetOfITCHIssuerDirectory;
import com.itm.mis.itch.books.SheetOfITCHMarketSegmentDirectory;
import com.itm.mis.itch.books.SheetOfITCHOrderBookDirectoryMDF;
import com.itm.mis.itch.books.SheetOfITCHOrderBookState;
import com.itm.mis.itch.books.SheetOfITCHParticipantDirectory;
import com.itm.mis.itch.bridge.ITMITCHMsgMemoryMgr;
import com.itm.mis.itch.consts.ITCHConsts;
import com.itm.mis.itch.structs.ITCHMsgAddOrder;
import com.itm.mis.itch.structs.ITCHMsgBase;
import com.itm.mis.itch.structs.ITCHMsgCircuitBreakerTrigger;
import com.itm.mis.itch.structs.ITCHMsgEquilibriumPrice;
import com.itm.mis.itch.structs.ITCHMsgExchangeDirectory;
import com.itm.mis.itch.structs.ITCHMsgGlimpseSnapshot;
import com.itm.mis.itch.structs.ITCHMsgIndexMember;
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
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;
import com.itm.xtream.inet.trading.db.record.object.StockDataRecord;
import com.itm.xtream.inet.trading.dbsave.DbRiskMgtWriteStockData;
import com.itm.xtream.inet.trading.feed.consts.FeedConsts;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgBase;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgBrokerData;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgIndices;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgOrder;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgOrderClear;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgStockData;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgStockSummary;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgTheoreticalPV;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgTrade;
import com.itm.xtream.inet.trading.feed.structs.FEEDMsgTradingStatus;
import com.itm.xtream.inet.trading.feed.structs.FEEDSuspendReleaseStock;
import com.itm.xtream.inet.trading.feed.util.FEEDMsgHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import com.itm.mis.itch.bridge.ITMITCHMsgMemoryListener;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author fredy
 */
public class ITMFeedMsgMemory implements ITMITCHMsgMemoryListener{
    public final static ITMFeedMsgMemory getInstance = new ITMFeedMsgMemory();
    
    private ArrayList<FEEDMsgBase> lstFeedMsg = new ArrayList<>();
    private ArrayList<String> lstFeedStr = new ArrayList<>();
    private int iSequenceSize = 0; 
    
    private String zLastSentDateDFEndSendingRecord = "";
    private String zLastRunningTrade = "";
    private int mPrevSize = 0;
    
    
    public boolean isAlreadyPreClosing = false;
    
    private boolean bStartListerner = false;
    public void startListener(){
        if (!bStartListerner){
            ITMITCHMsgMemoryMgr.getInstance.addMsgMemoryListener(this);
            bStartListerner = true;
            //////////////startTimer();
        }
    }
    
    public void resetData(){
        lstFeedMsg.clear();
        lstFeedStr.clear();
        iSequenceSize = 0;
        ITMFeedMsgFile.getInstance.resetFileMemory();
        //XCHSplitterServerInputBridge.getInstance.clearXCHMessages();
    }
    private ITMFeedMsgMemory() {
        //.nothing todo here :)
        
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
        
    }

    public ArrayList<FEEDMsgBase> getLstFeedMsg() {
        return lstFeedMsg;
    }
    
    public ArrayList<String> getLstFeedStr() {
        return lstFeedStr;
    }
    
    public boolean processMsgITCH(ITCHMsgBase itchMessage, SheetOfITCHBase mSheet){
        boolean mOut = false;
        try{
            if (itchMessage != null){
                if (itchMessage instanceof ITCHMsgUnknown){
                    //... .
                } else if (itchMessage instanceof ITCHMsgSystemEvent){
                    ITCHMsgSystemEvent mMsg = (ITCHMsgSystemEvent)itchMessage;
                    if (ITCHConsts.ITCHValue.SYSTEM_EVENT_CODE_FIRST_OF_MESSAGE.equals(mMsg.getEvent())){
                    
                    }else if (ITCHConsts.ITCHValue.SYSTEM_EVENT_CODE_LAST_OF_MESSAGE.equals(mMsg.getEvent())){
                        //... .
                    }          
                    // . FEEDMsgTradingStatus
                    FEEDMsgTradingStatus fMsg = new FEEDMsgTradingStatus();
                    switch (String.valueOf(mMsg.getEvent())) {
                        case ITCHConsts.ITCHValue.SYSTEM_EVENT_CODE_FIRST_OF_MESSAGE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SENDING_RECORDS);
                            fMsg.setMessage(FeedConsts.TradingStatusDesc.TRADINGSTATUS_STATUS_BEGIN_SENDING_RECORDS);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.resetData();
                            //. reset dari awal
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            zLastSentDateDFEndSendingRecord = "";
                            break;
                        case ITCHConsts.ITCHValue.SYSTEM_EVENT_CODE_LAST_OF_MESSAGE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_END_SENDING_RECORDS);
                            fMsg.setMessage(FeedConsts.TradingStatusDesc.TRADINGSTATUS_STATUS_END_SENDING_RECORDS);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            zLastSentDateDFEndSendingRecord = DateTimeHelper.getDateSVRTRXFormat();
                            break;
                        default:
                            break;
                    }
                   
                } else if (itchMessage instanceof ITCHMsgAddOrder){
                    //. fraksi wajib untuk mengkonversi price
                    long lPriceDecimals = 0;
                    
                    ITCHMsgAddOrder mMsg = (ITCHMsgAddOrder)itchMessage;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());

                    SheetOfITCHEquilibriumPrice sheetID = BookOfITCHEquilibriumPrice.getInstance.retrieveSheet(mMsg.getOrderBookId());
                        
                    if (sheetOD != null && mMsg.getOrderId()> 0){ //. new order
                        
                        long lQtyTraded = 0;
                        
                        lPriceDecimals = sheetOD.getMessage().getDecimalsInPrice();
//                        mMsg.setPriceDecimals(lPriceDecimals);
                        //. FEEDMsgOrder
                        byte mOrderVerb = mMsg.getSide();
//                        if (mOrderVerb != 0) mOrderVerb = 0;
//                        
//                        if (mOrderVerb.equalsIgnoreCase("b")){
//                            mOrderVerb = "0";
//                        }else if (mOrderVerb.equalsIgnoreCase("s")){
//                            mOrderVerb = "1";
//                        }
                        
                        FEEDMsgOrder fMsg = new FEEDMsgOrder();
                        fMsg.setOrderTime(ITMSoupBinTCPBridgePacketFormat.getTimeDataFeedFormatFromDate(mSheet.getMessageDate())); //. ???
                        fMsg.setOrderCommand(String.valueOf(mOrderVerb));
                        fMsg.setSecurityCode(sheetOD.getMessage().getSymbol());
                        String zBoardCode = "";
                        SheetOfITCHMarketSegmentDirectory sheetMarketSegment = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getMarketSegmentId());
                        if (sheetMarketSegment != null) {
                            zBoardCode = sheetMarketSegment.getMessage().getMarketSegmentName();
                        }
                        fMsg.setBoardCode(zBoardCode);
                        fMsg.setBrokerCode(""); 
                        fMsg.setPrice(String.valueOf(mMsg.getPrice()));
                        fMsg.setVolume(String.valueOf((mMsg.getQuantity() + lQtyTraded) * sheetOD.getMessage().getRoundLotSize())); //. ???
                        fMsg.setBalance(String.valueOf(mMsg.getQuantity() * sheetOD.getMessage().getRoundLotSize())); //. ???
//                        String mDomicile = mMsg.getDomicile();
//                        if (mDomicile == null) mDomicile = "";
//                        
//                        if (mDomicile.equalsIgnoreCase("i")){
//                            mDomicile = "D";
//                        }else if (mDomicile.equalsIgnoreCase("a")){
//                            mDomicile = "F";
//                        }
                        
                        fMsg.setInvType("");
                        fMsg.setOrderNo(String.valueOf(mMsg.getOrderId()));
                        
                        fMsg.setBestBidPrice("0"); //. ???
                        fMsg.setBestBidVol("0"); //. ???
                        fMsg.setBestOfferPrice("0"); //. ???
                        fMsg.setBestOfferVol("0"); //. ???
                        fMsg.setOrderRef("000000000000");
                        
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                    
                    //. indices
                    if (sheetID != null){ //. update index
                        lPriceDecimals = sheetID.getMessage().getPrice();
                        mMsg.setPrice(lPriceDecimals);
                        //. FEEDMsgIndices
                        FEEDMsgIndices fMsg = new FEEDMsgIndices();
                        String zIndexCode = "";
                        if (sheetOD != null) {
                            zIndexCode = sheetOD.getMessage().getSymbol();
                        }
                        
                        fMsg.setIndexCode(zIndexCode);
                        double xhcBaseVal = 0;
                        double xhcMktVal = 0;
                        double lastPrice = 0;
                        double openPrice = 0;
                        double highPrice = 0;
                        double lowPrice = 0;
                        double closePrice = 0;
                        
                        if (FEEDMsgHelper.getInstance.mapIndices.containsKey(zIndexCode)){
                            ConcurrentHashMap<String, Double> mMap = FEEDMsgHelper.getInstance.mapIndices.get(zIndexCode);
                            if (mMap.containsKey("bv")){
                                xhcBaseVal = mMap.get("bv"); 
                            }
                            if (mMap.containsKey("mv")){
                                xhcMktVal = mMap.get("mv"); 
                            }
                            
                            if (!mMap.containsKey("o")){ //. set sekali
                                mMap.put("o", (double)mMsg.getPrice());
                                mMap.put("lo", (double)mMsg.getPrice());
                                mMap.put("hi", (double)mMsg.getPrice());
                                openPrice = (double)mMsg.getPrice();
                            }else{
                                openPrice = mMap.get("o");
                            }
                            
                            mMap.put("la", (double)mMsg.getPrice());
                            lastPrice = (double)mMsg.getPrice();
                            lowPrice = mMap.get("lo");
                            highPrice = mMap.get("hi");
                            if (lowPrice > mMsg.getPrice()){
                                lowPrice = (double)mMsg.getPrice();
                                mMap.put("lo", lowPrice);
                            }
                            if (highPrice < mMsg.getPrice()){
                                highPrice = (double)mMsg.getPrice();
                                mMap.put("hi", highPrice);
                            }
                            
                            closePrice = mMap.get("p"); 
                        }else{
                            ConcurrentHashMap<String, Double> mMap = new ConcurrentHashMap<>();
                            
                            mMap.put("p", (double)mMsg.getPrice()); //. previous
                            closePrice = mMsg.getPrice();
                            mMap.put("hi", 0.0);
                            mMap.put("lo", 0.0);
                            mMap.put("la", 0.0);
                            
                            
                            FEEDMsgHelper.getInstance.mapIndices.put(zIndexCode, mMap);
                        }
                        
                        fMsg.setExchgBaseValue(String.valueOf(xhcBaseVal)); //. ???
                        fMsg.setExchgMarketValue(String.valueOf(xhcMktVal)); //. ???
                        fMsg.setIndex(String.valueOf(lastPrice));
                        fMsg.setOpen(String.valueOf(openPrice));
                        fMsg.setHigh(String.valueOf(highPrice));
                        fMsg.setLow(String.valueOf(lowPrice));
                        fMsg.setPrevIndex(String.valueOf(closePrice));
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                    
                    //. stock summary
                    if (sheetOD != null && mMsg.getOrderId() <= 0){
                        //. FEEDMsgStockSummary
                        FEEDMsgStockSummary fMsg = new FEEDMsgStockSummary();
                        String zSecCode = sheetOD.getMessage().getSymbol();
                        String zBoardCode = "";
                        
                        SheetOfITCHMarketSegmentDirectory sheetMarketSegment = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getMarketSegmentId());
                        if (sheetMarketSegment != null) {
                            zBoardCode = sheetMarketSegment.getMessage().getMarketSegmentName();
                        }
                        
                        fMsg.setSecurityCode(zSecCode);
                        fMsg.setBoardCode(zBoardCode);

                        double lastPrice = 0;
                        double openPrice = 0;
                        double highPrice = 0;
                        double lowPrice = 0;
                        double closePrice = 0;
                        double chgPrice = 0;
                        String sb = zSecCode + "_" + zBoardCode;
                        if (FEEDMsgHelper.getInstance.mapStock.containsKey(sb)){
                            ConcurrentHashMap<String, Double> mMap = FEEDMsgHelper.getInstance.mapStock.get(sb);
                            
                            if (!mMap.containsKey("o")){ //. set sekali
                                mMap.put("o", (double)mMsg.getPrice());
                                mMap.put("lo", (double)mMsg.getPrice());
                                mMap.put("hi", (double)mMsg.getPrice());
                                openPrice = (double)mMsg.getPrice();
                            }else{
                                openPrice = mMap.get("o");
                            }
                            lowPrice = mMap.get("lo");
                            highPrice = mMap.get("hi");
                            lastPrice = mMap.get("la");
                            chgPrice = (double)mMsg.getPrice() - lastPrice;
                            mMap.put("la", (double)mMsg.getPrice());
                            
                            if (lowPrice > mMsg.getPrice()){
                                lowPrice = (double)mMsg.getPrice();
                                mMap.put("lo", lowPrice);
                            }
                            if (highPrice < mMsg.getPrice()){
                                highPrice = (double)mMsg.getPrice();
                                mMap.put("hi", highPrice);
                            }
                        }else{
                            if (mMsg.getPrice() > 0){
                                StockDataRecord mCmpRec = new StockDataRecord();
                                mCmpRec.setfSecurityCode(sheetOD.getMessage().getSymbol());
                                mCmpRec.setfPrevPrice(StringHelper.fromDouble(mMsg.getPrice()));

                                //. simpan ke table database
                                DbRiskMgtWriteStockData.getInstance.insertOrUpdateStockData(mCmpRec);
                            }
                            
                            ConcurrentHashMap<String, Double> mMap = new ConcurrentHashMap<String, Double>();
                            
                            mMap.put("p", (double)mMsg.getPrice()); //. previous
                            closePrice = mMsg.getPrice();
                            mMap.put("hi", 0.0);
                            mMap.put("lo", 0.0);
                            mMap.put("la", 0.0);
                            
                            FEEDMsgHelper.getInstance.mapIndices.put(sb, mMap);
                        }
                        
                        fMsg.setPrevPrice(String.valueOf(closePrice));
                        fMsg.setHighPrice(String.valueOf(highPrice));
                        fMsg.setLowPrice(String.valueOf(lowPrice));
                        fMsg.setClosePrice(String.valueOf(lastPrice));
                        fMsg.setOpeningPrice(String.valueOf(openPrice));
                        fMsg.setChange(String.valueOf(chgPrice));
                        fMsg.setTradedVol("0"); //. ???
                        fMsg.setTradedVal("0"); //. ???
                        fMsg.setTradedFreq("0"); //. ???
                        fMsg.setIndividualIndex("0"); //. ???
                        fMsg.setAvailForeigner("0"); //. ???
                        fMsg.setBestBidPrice("0"); //. ???
                        fMsg.setBestBidVol("0"); //. ???
                        fMsg.setBestOfferPrice("0"); //. ???
                        fMsg.setBestOfferVol("0"); //. ???
                        fMsg.setAvgPrice("0"); //. ???
                        String zStatus = "0";
                        SheetOfITCHOrderBookState mBTA = BookOfITCHOrderBookState.getInstance.retrieveSheet(mMsg.getOrderBookId());
                        if (mBTA != null && mBTA.getMessage().getStateName().equalsIgnoreCase("Suspend")){
                            zStatus = "1";
                        }
                        fMsg.setSecBoardState(zStatus);
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                
                } else if (itchMessage instanceof ITCHMsgTrade){
                    ITCHMsgTrade mMsg = (ITCHMsgTrade)itchMessage;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                                        
                    if (sheetOBD != null){
                        FEEDMsgTrade fMsg = new FEEDMsgTrade();
                        fMsg.setTradTime(FEEDMsgHelper.getInstance.getFormattedTimeStamp()); //. ???
                        fMsg.setTradeCommand("0");
                        fMsg.setSecurityCode(sheetOBD.getMessage().getSymbol());
                        
                        SheetOfITCHMarketSegmentDirectory sheetMarketSegment = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                        
                        if (sheetMarketSegment != null) {
                            fMsg.setBoardCode(sheetMarketSegment.getMessage().getMarketSegmentName());
                        }
                        
                        fMsg.setTradeNo(String.valueOf(mMsg.getMatchId()));
                        fMsg.setPrice(String.valueOf(mMsg.getPrice()));
                        fMsg.setVol(String.valueOf(mMsg.getQuantity()* sheetOBD.getMessage().getRoundLotSize())); 

                        SheetOfITCHParticipantDirectory sheetPO_Buy = BookOfITCHParticipantDirectory.getInstance.retrieveSheet(mMsg.getOwner());
                        SheetOfITCHParticipantDirectory sheetPO_Sell = BookOfITCHParticipantDirectory.getInstance.retrieveSheet(mMsg.getCounterparty());

                        if (sheetPO_Buy != null){
                            fMsg.setBuyerCode(sheetPO_Buy.getMessage().getParticipantDescription()); 
                        }else{
                            fMsg.setBuyerCode("--");
                        }
                        if (sheetPO_Sell != null){
                            fMsg.setSellerCode(sheetPO_Sell.getMessage().getParticipantDescription()); 
                        }else{
                            fMsg.setSellerCode("--");
                        }

                        fMsg.setBestBidPrice("0"); 
                        fMsg.setBestBidVol("0"); 
                        fMsg.setBestOfferPrice("0");
                        fMsg.setBestOfferVol("0"); 
                        fMsg.setBuyerOrderNo(String.valueOf("0"));
                        fMsg.setSellerOrderNo(String.valueOf("0"));
                        fMsg.setBuyerType("");
                        fMsg.setSellerType("");
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    } 
                } else if (itchMessage instanceof ITCHMsgEquilibriumPrice){
                    ITCHMsgEquilibriumPrice mMsg = (ITCHMsgEquilibriumPrice)itchMessage;
                    
                    FEEDMsgTheoreticalPV fMsg = new FEEDMsgTheoreticalPV();
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                    
                    if (sheetOBD != null){
                        fMsg.setSecurityCode(sheetOBD.getMessage().getSymbol());
                        SheetOfITCHMarketSegmentDirectory sheetMarketSegment = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                        if (sheetMarketSegment != null) {
                            fMsg.setBoard(sheetMarketSegment.getMessage().getMarketSegmentName());
                        }
                        fMsg.setPrice(StringHelper.fromLong(mMsg.getPrice()));
                        fMsg.setVolume(StringHelper.fromLong(mMsg.getBidQuantity()));
                        fMsg.setBestBid(StringHelper.fromLong(mMsg.getBestBidPrice()));
                        fMsg.setBestBidSize(StringHelper.fromLong(mMsg.getBestBidQuantity()));
                        fMsg.setBestOffer(StringHelper.fromLong(mMsg.getBestAskPrice()));
                        fMsg.setBestOfferSize(StringHelper.fromLong(mMsg.getBestAskQuantity()));
                        //.-------------
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                } else if (itchMessage instanceof ITCHMsgOrderBookState){
                    ITCHMsgOrderBookState mMsg = (ITCHMsgOrderBookState)itchMessage;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                    if (sheetOBD != null){
                        SheetOfITCHMarketSegmentDirectory sheetMarketSegment = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                        String _board = "";
                        
                        if (sheetMarketSegment != null) {
                            _board = sheetMarketSegment.getMessage().getMarketSegmentName();
                        }
                          
                        FEEDSuspendReleaseStock fMsg = new FEEDSuspendReleaseStock();
                        fMsg.setSecurityCode(sheetOBD.getMessage().getSymbol());

                        String zStatus = "0";
                        if (mMsg.getStateName().equalsIgnoreCase("Suspend")){
                            
                        }
                        fMsg.setFlag(zStatus);
                        fMsg.setBoardCode(_board);
                        
                        ITCHMsgOrderBookDirectoryMDF mOrderBookDirectory = sheetOBD.getMessage();

                        StockDataRecord mCmpRec = new StockDataRecord();
                        mCmpRec.setfSecurityCode(mOrderBookDirectory.getSymbol());
                     
                        String szStockStatus = "A";
                        if (mMsg.getStateName().equalsIgnoreCase("Suspend")){
                            szStockStatus = "S";
                        }
                        
                        if (_board.equalsIgnoreCase("RG")){
                            mCmpRec.setfSecurityStatus(szStockStatus);
                        } else if (_board.equalsIgnoreCase("TN")){
                            mCmpRec.setfSecurityStatus_TN(szStockStatus);
                        } else if (_board.equalsIgnoreCase("NG")){
                             mCmpRec.setfSecurityStatus_NG(szStockStatus);
                        }
                        
                        mCmpRec.setfSecurityTradingStatus(mMsg.getStateName());
                        
                        //. simpan ke table database
                        DbRiskMgtWriteStockData.getInstance.insertOrUpdateStockData(mCmpRec);
                    }

                    // . FEEDMsgTradingStatus
                    FEEDMsgTradingStatus fMsg = new FEEDMsgTradingStatus();
                    switch (mMsg.getStateName().trim()) {
                        case ITCHConsts.ITCHStateField.STATE_BREAK:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BREAK);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_BREAK);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_BREAK_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BREAK_CALL);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_BREAK_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CALL_RANDOM_CLOSE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CALL_RANDOM_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CALL_RANDOM_CLOSE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_CALL_AUCTION:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_CALL_AUCTION);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_NG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_NG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_NG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_RF:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_RF);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RF);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_RG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_RG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_TN:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_TN);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_END_OF_DAY:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_END_SENDING_RECORDS);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_END_OF_DAY);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_MATCHING_CALL_AUCTION:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_MATCHING_CA);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_MATCHING_CALL_AUCTION);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_MATCHING_CLOSE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_MATCHING_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_MATCHING_CLOSE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_MATCHING_PRE_OPEN:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_MATCHING_PRE_OPEN);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_MATCHING_PRE_OPEN);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_NON_CANCEL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_NON_CANCELLATION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_NON_CANCEL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_POST_TRADE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_POST_TRADING);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_POST_TRADE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_PRE_CLOSE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_PRE_CLOSING);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_PRE_CLOSE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_PRE_OPEN:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_PRE_OPENING);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_PRE_OPEN);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_RANDOM_CLOSE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_RANDOM_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_RANDOM_CLOSE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_NG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_NG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_NG);
                            fMsg.setSession(1);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_RF:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_RF);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RF);
                            fMsg.setSession(1);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_RG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_RG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(1);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(1);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_TN:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_TN);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(1);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_TN_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_TN_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(1);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_NG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_NG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_NG);
                            fMsg.setSession(2);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_RF:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_RF);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RF);
                            fMsg.setSession(2);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_RG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_RG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(2);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(2);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_TN_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_TN_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(2);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_3_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_3_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(3);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_3_TN_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_3_TN_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(3);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_4_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_4_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(4);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_5_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_5_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(5);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SOBD:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SENDING_RECORDS);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SOBD);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SUSPEND:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_TRADING_SUSPENSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SUSPEND);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_TRADING_HALT:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_TRADING_HALT);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_TRADING_HALT);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_UNSUSPEND:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_TRADING_ACTIVATION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_UNSUSPEND);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        default:
                            break;
                    }                    
                } else if (itchMessage instanceof ITCHMsgOrderBookClear){ 
                    ITCHMsgOrderBookClear mMsg = (ITCHMsgOrderBookClear)itchMessage;
                    SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                    if (sheetOBD != null){
                        FEEDMsgOrderClear fMsg = new FEEDMsgOrderClear();
                        fMsg.setTimestamp(StringHelper.fromLong(mMsg.getNanos()));
                        fMsg.setStatus("1");
                        fMsg.setStockName(sheetOBD.getMessage().getSymbol());
                        String zBoardCode = "";
                        
                        SheetOfITCHMarketSegmentDirectory sheetMarketSegment = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                        if (sheetMarketSegment != null) {
                            zBoardCode = sheetMarketSegment.getMessage().getMarketSegmentName();
                        }
                        fMsg.setBoardCode(zBoardCode);
                        
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                    
                } else if (itchMessage instanceof ITCHMsgGlimpseSnapshot){
                    //...
                } else if (itchMessage instanceof ITCHMsgOrderBookDirectory){
                    //...
                } else if (itchMessage instanceof ITCHMsgOrderDelete){
                    //...
                } else if (itchMessage instanceof ITCHMsgOrderExecuted){
                    //...
                } else if (itchMessage instanceof ITCHMsgOrderExecutedWithPrice){
                    //...
                } else if (itchMessage instanceof ITCHMsgTickSizeTable){
                    //...
                } else{
                    //... .
                }
                //... .
            }
        }catch(Exception ex0){
            Object aa = itchMessage;
            Object bb = mSheet;
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean processMsgITCHMDF(ITCHMsgBase itchMessage, SheetOfITCHBase mSheet){
        boolean mOut = false;
        try{
            if (itchMessage != null){
                if (itchMessage instanceof ITCHMsgUnknown){
                    //... .
                } else if (itchMessage instanceof ITCHMsgSystemEvent){
                    //...
                } else if (itchMessage instanceof ITCHMsgParticipantDirectory){
                    ITCHMsgParticipantDirectory mMsg = (ITCHMsgParticipantDirectory)itchMessage;
                    
                    //. FeedMsgBrokerData
                    FEEDMsgBrokerData fMsg = new FEEDMsgBrokerData();
                    fMsg.setBrokerCode(mMsg.getParticipantId().trim());
                    fMsg.setBrokerName(mMsg.getParticipantDescription().trim());
                    fMsg.setBrokerStatus("0");
                    
                    ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                } else if (itchMessage instanceof ITCHMsgOrderBookDirectoryMDF){
                    ITCHMsgOrderBookDirectoryMDF mMsg = (ITCHMsgOrderBookDirectoryMDF)itchMessage;
                    
                    String _stockCode = mMsg.getSymbol();
                    String _stockName = mMsg.getLongName();
                    String _board = "";
                    String _marketSegment = "";
                    
                    SheetOfITCHMarketSegmentDirectory sheetMarketSegment = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(mMsg.getMarketSegmentId());
                    if (sheetMarketSegment != null) {
                        _board = sheetMarketSegment.getMessage().getMarketSegmentName();
                        _marketSegment = sheetMarketSegment.getMessage().getMarketSegmentName();
                    }
                    
                    if (!FEEDMsgHelper.getInstance.mapSendStockData.containsKey(_stockCode)){
                        FEEDMsgHelper.getInstance.mapSendStockData.put(_stockCode, 1);
                        FEEDMsgStockData fMsg = new FEEDMsgStockData();
                        fMsg.setSecurityCode(_stockCode.trim());
                        SheetOfITCHIssuerDirectory sheetID = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(mMsg.getIssuerId());
                        
                        if (sheetID != null){
                            _stockName = sheetID.getMessage().getLongName().trim();
                        }
                        String zStatus = "0"; 
                        
                        fMsg.setSecurityName(_stockName.trim());
                        fMsg.setSecurityStatus(zStatus);
                        fMsg.setSecurityType(_marketSegment);
                        if (mMsg.getRemarks() != null && mMsg.getRemarks().length() >= 20){
                            fMsg.setSubSector(mMsg.getRemarks().substring(14, 18));
                        }else{
                            fMsg.setSubSector(String.valueOf(mMsg.getSectorCode().trim()));
                        }
                        
                        
                        fMsg.setIpoPrice(String.valueOf(mMsg.getIpoPrice()));
                        fMsg.setBasePrice("0"); 
                        fMsg.setListedShare(String.valueOf(mMsg.getOutstandingQuantity()));
                        fMsg.setTradeableListedShare(String.valueOf(mMsg.getTradableQuantity()));
                        if (mMsg.getRoundLotSize()> 1){
                            fMsg.setSharePerLot(String.valueOf(mMsg.getRoundLotSize()));
                        }else{
                            fMsg.setSharePerLot(String.valueOf(FEEDMsgBase.SHARE_PER_LOT));
                        }
                        

                        fMsg.setRemarks(String.valueOf(mMsg.getRemarks().trim()));
                        fMsg.setRemarks2(String.valueOf(mMsg.getRemarks().trim()));
                        fMsg.setWeight("0"); //. ???

                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                        
                    }
                    
                    //. simpan stock data
                    ITCHMsgOrderBookDirectoryMDF mOrderBookDirectory = mMsg;
                    SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(mOrderBookDirectory.getIssuerId());
                    SheetOfITCHMarketSegmentDirectory mSheetMarketSegment = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(mMsg.getMarketSegmentId());
                                        
                    StockDataRecord mCmpRec = new StockDataRecord();
                    mCmpRec.setfSecurityCode(mOrderBookDirectory.getSymbol());
                    mCmpRec.setfSecurityID(StringHelper.fromLong(mSheetIssuerDirectory.getMessage().getIssuerId()));
                    mCmpRec.setfSecurityName(mSheetIssuerDirectory.getMessage().getLongName());

                    String szStockStatus = "S"; //. default disini Suspend stock nya
                    
                    mCmpRec.setfSecurityStatus(szStockStatus);
                    mCmpRec.setfSecurityStatus_TN(szStockStatus);
                    mCmpRec.setfSecurityStatus_NG(szStockStatus);
                    
                    mCmpRec.setfLotSize(StringHelper.fromLong(mOrderBookDirectory.getRoundLotSize()));
                    mCmpRec.setfPriceStep(StringHelper.fromInt(0));
                    mCmpRec.setfSecurityTradingStatus("V"); //. default nya V, bukan T
                    mCmpRec.setfBoard_RG(null);
                    mCmpRec.setfBoard_TN(null);
                    mCmpRec.setfBoard_NG(null);
                    mCmpRec.setfBoard_TS(null);
                    mCmpRec.setfStockType(mSheetMarketSegment.getMessage().getMarketSegmentName());
                    mCmpRec.setfPrevPrice(StringHelper.fromDouble(0));
                    mCmpRec.setfFaceValue(StringHelper.fromDouble(0));
                    mCmpRec.setfListedSize(StringHelper.fromLong(mOrderBookDirectory.getOutstandingQuantity()));
                    mCmpRec.setfTradeableSize(StringHelper.fromLong(mOrderBookDirectory.getTradableQuantity()));
                    mCmpRec.setfRemark(mOrderBookDirectory.getRemarks());
                    mCmpRec.setfRemark2(mOrderBookDirectory.getRemarks());
                    mCmpRec.setfStockDate(DateTimeHelper.getDateSVRTRXFormat());
                    if (mSheetMarketSegment.getMessage().getMarketSegmentName().equals(QRIDataConst.QRIFieldValue.SECURITY_INSTR_PRE_OPENING)  && szStockStatus.equals(QRIDataConst.QRIFieldValue.SECURITY_STATUS_ACTIVE)) {
                        mCmpRec.setfPreOpening(StringHelper.fromInt(QRIDataConst.QRIFieldValue.PRE_OPENING_ON));
                    }else {
                        mCmpRec.setfPreOpening(StringHelper.fromInt(QRIDataConst.QRIFieldValue.PRE_OPENING_OFF));
                    }
                    mCmpRec.setfPreOpening(StringHelper.fromInt(mCmpRec.getPreOpeningByRemarks2(mOrderBookDirectory.getRemarks(), StringHelper.toInt(mCmpRec.getfPreOpening()))));
                    mCmpRec.setfStockMargin(StringHelper.fromInt(QRIDataConst.QRIFieldValue.MARGINABLE_OFF)); //.default.
                    if ((!StringHelper.isNullOrEmpty(mOrderBookDirectory.getRemarks())) && (mOrderBookDirectory.getRemarks().length() > 3)){
                        String zMarginableStatus = mOrderBookDirectory.getRemarks().substring(2, 3);
                        if ((zMarginableStatus.equalsIgnoreCase(QRIDataConst.QRIFieldValue.REMARK_INFO_MARGINABLE)) ||
                            (zMarginableStatus.equalsIgnoreCase(QRIDataConst.QRIFieldValue.REMARK_INFO_MARGINABLE_SHORT))){
                            mCmpRec.setfStockMargin(StringHelper.fromInt(QRIDataConst.QRIFieldValue.MARGINABLE_ON)); //.set.
                        }
                    }
                    mCmpRec.setfStockMargin(StringHelper.fromInt(mCmpRec.getStockMarginByRemarks2(mOrderBookDirectory.getRemarks(), StringHelper.toInt(mCmpRec.getfStockMargin()))));

                    String szSymboxSfx = "0" + mSheetMarketSegment.getMessage().getMarketSegmentName();
                    if (szSymboxSfx.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardRG.getValue())){
                        mCmpRec.setfBoard_RG(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                        mCmpRec.setfBoard_RG(StringHelper.fromLong(mMsg.getOrderBookId()));
                    }else if (szSymboxSfx.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardTN.getValue())){
                        mCmpRec.setfBoard_TN(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                        mCmpRec.setfBoard_TN(StringHelper.fromLong(mMsg.getOrderBookId()));
                    }else if (szSymboxSfx.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardNG.getValue())){
                        mCmpRec.setfBoard_NG(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                        mCmpRec.setfLotSize(null);
                        mCmpRec.setfBoard_NG(StringHelper.fromLong(mMsg.getOrderBookId()));
                    }
                    if (szSymboxSfx.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardRG.getValue())){
                        mCmpRec.setfLastPrice_RG(StringHelper.fromDouble(0));
                    }else if (szSymboxSfx.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardTN.getValue())){
                        mCmpRec.setfLastPrice_TN(StringHelper.fromDouble(0));
                    }else if (szSymboxSfx.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardNG.getValue())){
                        mCmpRec.setfLastPrice_NG(StringHelper.fromDouble(0));
                    }

                    //. simpan ke table database
                    DbRiskMgtWriteStockData.getInstance.insertOrUpdateStockData(mCmpRec);
                    
                } else if (itchMessage instanceof ITCHMsgEquilibriumPrice){
                    //...
                } else if (itchMessage instanceof ITCHMsgOrderBookState){
                    //...   
                } else if (itchMessage instanceof ITCHMsgCircuitBreakerTrigger){
                    //...
                } else if (itchMessage instanceof ITCHMsgExchangeDirectory){
                    //...
                } else if (itchMessage instanceof ITCHMsgIndexMember){
                    //...
                } else if (itchMessage instanceof ITCHMsgIndexPrice){
                    //...
                } else if (itchMessage instanceof ITCHMsgIndicativeQuote){
                    //...
                } else if (itchMessage instanceof ITCHMsgIssuerDirectory){
                    //...
                } else if (itchMessage instanceof ITCHMsgMarketByPrice){
                    //...
                } else if (itchMessage instanceof ITCHMsgMarketDirectory){
                    //...
                } else if (itchMessage instanceof ITCHMsgMarketSegmentDirectory){
                    //...
                } else if (itchMessage instanceof ITCHMsgPriceLimits){
                    //...
                } else if (itchMessage instanceof ITCHMsgReferencePrice){
                    //...
                } else if (itchMessage instanceof ITCHMsgTickSizeTable){
                    //...
                } else if (itchMessage instanceof ITCHMsgTradeStatistics){
                    //...
                } else if (itchMessage instanceof ITCHMsgTradeTicker){
                    //...
                } else{
                    //... .
                }
                //... .
            }
        }catch(Exception ex0){
            Object aa = itchMessage;
            Object bb = mSheet;
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public synchronized void addToMemory(FEEDMsgBase o, SheetOfITCHBase m) {
        this.iSequenceSize++;
         
        o.setSeq(FEEDMsgHelper.getInstance.fmtSeq(lstFeedStr.size() + 1));
        o.setDate(ITMSoupBinTCPBridgePacketFormat.getDateDataFeedFormatFromDate(m.getMessageDate()));
        o.setTime(ITMSoupBinTCPBridgePacketFormat.getTimeDataFeedFormatFromDate(m.getMessageDate()));
        
        String _msg = "";
        if (o instanceof FEEDMsgBrokerData){
            _msg = ((FEEDMsgBrokerData)o).toDataFeedMsg();
        }else if (o instanceof FEEDMsgIndices){
            _msg = ((FEEDMsgIndices)o).toDataFeedMsg();
//        }else if (o instanceof FEEDMsgNews){
//            _msg = ((FEEDMsgNews)o).toDataFeedMsg();
//        }else if (o instanceof FEEDMsgOrder){
//            _msg = ((FEEDMsgOrder)o).toDataFeedMsg();
        }else if (o instanceof FEEDMsgStockData){
            _msg = ((FEEDMsgStockData)o).toDataFeedMsg();
        }else if (o instanceof FEEDMsgStockSummary){
            _msg = ((FEEDMsgStockSummary)o).toDataFeedMsg();
        }else if (o instanceof FEEDMsgTrade){
            _msg = ((FEEDMsgTrade)o).toDataFeedMsg();
            zLastRunningTrade = _msg;
        }else if (o instanceof FEEDMsgTradingStatus){
            _msg = ((FEEDMsgTradingStatus)o).toDataFeedMsg();
//        }else if (o instanceof FEEDSuspendReleaseBroker){
//            _msg = ((FEEDSuspendReleaseBroker)o).toDataFeedMsg();
        }else if (o instanceof FEEDSuspendReleaseStock){
            _msg = ((FEEDSuspendReleaseStock)o).toDataFeedMsg();
        }else if (o instanceof FEEDMsgTheoreticalPV){
            _msg = ((FEEDMsgTheoreticalPV)o).toDataFeedMsg();
        }else if (o instanceof FEEDMsgOrderClear){ 
            _msg = ((FEEDMsgOrderClear)o).toDataFeedMsg();
        }else{
            System.err.println("this.iSequenceSize = " + this.iSequenceSize);
            if (o != null){
                System.err.println("addToMemory dapat object = " + o.toString());
            }else{
                System.err.println("addToMemory dapat object = NULL");
            }
        }
        //. ke memory string
        lstFeedStr.add(_msg);
        
//        try
//        {
//            //System.out.println(_msg);
//            String filename= o.getDate() + ".raw";
//            FileWriter fw = new FileWriter(filename, true); 
//            fw.write(_msg + "ZZ\r\n");//appends the string to the file
//            fw.close();
//        }
//        catch(IOException ioe)
//        {
//            System.err.println("IOException: " + ioe.getMessage());
//        }
        
    }
    private int getCurTimeHHmm(){
        int iRet = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String fmt = sdf.format(cal.getTime());
        try{
            iRet = Integer.valueOf(fmt);
        }catch(Exception e){
            
        }
        return iRet;
    }
    private void startTimer(){
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                int cTme = getCurTimeHHmm();
                
                if ((cTme >= 858 && cTme <= 912) || (cTme >= 1457 && cTme <= 1515)){
                    int iCount = lstFeedStr.size();
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "lstFeedStr.size = " + iCount + ", Speed = " + (iCount - mPrevSize));
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Last RNTD = " + zLastRunningTrade);

                    mPrevSize = iCount;
                }

            }
        },0,1000);
    }
    public synchronized boolean checkAndSendManuallyDFEndSendingRecord(){
        boolean mOut = false;
        try{
            
        }catch(Exception ex0){
           ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    @Override
    public void onMessage(ITCHMsgBase itchMessage, SheetOfITCHBase mSheetBase, ITCHType itchType) {
        try{
            if (itchType == ITCHType.ITCH) {
                processMsgITCH(itchMessage, mSheetBase);
            } else if (itchType == ITCHType.ITCH_MDF) {
                processMsgITCHMDF(itchMessage, mSheetBase);
            }
            
        }catch(Exception ex0){
           ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
    @Override
    public void onMessageRaw(ITCHMsgBase itchMessage) {
        try{
            //.soon
//            XCHSplitterServerInputBridge.getInstance.addXCHMessage(itchMessage);
        }catch(Exception ex0){
           ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
}

