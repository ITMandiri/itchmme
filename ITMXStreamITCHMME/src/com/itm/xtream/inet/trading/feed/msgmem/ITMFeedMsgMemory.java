/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.msgmem;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.mis.itch.books.BookOfITCHAddOrder;
import com.itm.mis.itch.books.BookOfITCHEquilibriumPrice;
import com.itm.mis.itch.books.BookOfITCHIssuerDirectory;
import com.itm.mis.itch.books.BookOfITCHMarketSegmentDirectory;
import com.itm.mis.itch.books.BookOfITCHOrderBookDirectoryMDF;
import com.itm.mis.itch.books.BookOfITCHOrderBookState;
import com.itm.mis.itch.books.BookOfITCHParticipantDirectory;
import com.itm.mis.itch.books.SheetOfITCHAddOrder;
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
import com.itm.mis.itch.structs.ITCHMsgSecond;
import com.itm.xtream.inet.trading.feed.server.callback.FeedServerCallbackController;
import com.itm.xtream.inet.trading.feed.server.callback.FeedServerCallbackProcessor;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author fredy
 */
public class ITMFeedMsgMemory implements ITMITCHMsgMemoryListener{
    public final static ITMFeedMsgMemory getInstance = new ITMFeedMsgMemory();
    
    private ConcurrentHashMap<String, Boolean> chmSessionState = new ConcurrentHashMap<>();
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
                    //...
                } else if (itchMessage instanceof ITCHMsgAddOrder){
                    //. fraksi wajib untuk mengkonversi price
                    long lPriceDecimals = 0;
                    
                    ITCHMsgAddOrder mMsg = (ITCHMsgAddOrder)itchMessage;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                        
                    if (sheetOD != null && mMsg.getOrderId()> 0){ //. new order
//                        System.out.println(mMsg);
                        long lQtyTraded = 0;
                        
//                        lPriceDecimals = sheetOD.getMessage().getDecimalsInPrice();
//                        mMsg.setPriceDecimals(lPriceDecimals);
                        //. FEEDMsgOrder
                        String mOrderVerb = mMsg.getSide();
                        if (mOrderVerb == null) mOrderVerb = "";
                        
                        if (mOrderVerb.equalsIgnoreCase("B")){
                            mOrderVerb = "0";
                        }else if (mOrderVerb.equalsIgnoreCase("S")){
                            mOrderVerb = "1";
                        }
                        
                        FEEDMsgOrder fMsg = new FEEDMsgOrder();
                        String zSymbol = sheetOD.getMessage().getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = arrSymbol[0];
                            zBoardCode = arrSymbol[1];
                        }
                        fMsg.setOrderTime(ITMSoupBinTCPBridgePacketFormat.getTimeDataFeedFormatFromDate(mSheet.getMessageDate())); //. ???
                        fMsg.setOrderCommand(String.valueOf(mOrderVerb));
                        
                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                        }
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                            }
                        }
                        
                        fMsg.setSecurityCode(zStockCode);
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
                    
                    //. indices ?????????????????????????????????????????????????????
                    if (sheetOD != null && sheetOD.getMessage().getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_INDEX){ //. update index
                        System.out.println("Indices"+mMsg);
                        
                    }
                
                } else if (itchMessage instanceof ITCHMsgTrade){
                    ITCHMsgTrade mMsg = (ITCHMsgTrade)itchMessage;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                                        
                    if (sheetOBD != null){
                        FEEDMsgTrade fMsg = new FEEDMsgTrade();
                        
                        String zSymbol = sheetOBD.getMessage().getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = zSymbol.split("_")[0];
                            zBoardCode = zSymbol.split("_")[1];
                        }
                        fMsg.setTradTime(ITMSoupBinTCPBridgePacketFormat.getTimeDataFeedFormatFromDate(mSheet.getMessageDate())); //. ???
                        fMsg.setTradeCommand("0");
                        
                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                        }
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                            }
                        }
                        
                        fMsg.setSecurityCode(zStockCode);
                        fMsg.setBoardCode(zBoardCode);
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
                    //....
                } else if (itchMessage instanceof ITCHMsgOrderBookClear){ 
                    ITCHMsgOrderBookClear mMsg = (ITCHMsgOrderBookClear)itchMessage;
                    SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                    if (sheetOBD != null){
                        FEEDMsgOrderClear fMsg = new FEEDMsgOrderClear();
                        String zSymbol = sheetOBD.getMessage().getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = zSymbol.split("_")[0];
                            zBoardCode = zSymbol.split("_")[1];
                        }
                        
                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                        }
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                            }
                        }
                        
                        fMsg.setTimestamp(StringHelper.fromLong(mMsg.getNanos()));
                        fMsg.setStatus("1");
                        fMsg.setStockName(zStockCode);
                        fMsg.setBoardCode(zBoardCode);
                        
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                    
                } else if (itchMessage instanceof ITCHMsgGlimpseSnapshot){
                    //...
                } else if (itchMessage instanceof ITCHMsgOrderBookDirectory){
                    //...
                } else if (itchMessage instanceof ITCHMsgOrderDelete){
                    ITCHMsgOrderDelete mMsg = (ITCHMsgOrderDelete)itchMessage;
                                    
                    Long mLastOrderNumber = mMsg.getOrderId();

                    SheetOfITCHAddOrder sheetAO = BookOfITCHAddOrder.getInstance.retrieveSheet(mMsg.getOrderId());
                    if (sheetAO != null){
                        SheetOfITCHOrderBookDirectoryMDF sheetOD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(sheetAO.getMessage().getOrderBookId());
                        
                        //. 20211011 : harus pastikan apakah benar-benar type stock
                        if (sheetOD == null){
//                            System.err.println("ITCHMsgOrderDelete non stock : " + mMsg.getOrderNumber());
                            return false;
                        }
                        
                        //. 20211224 : penanda bahwa OrderNumber sudah pernah di kirim message withdraw (untuk keperluan 1450 -> 1500)
                        //.???????
                        BookOfITCHAddOrder.getInstance.addSheetFlagPrevWithdraw(StringHelper.fromLong(mMsg.getOrderId()));
                        
                        //. FEEDMsgOrder
                        String mOrderVerb = sheetAO.getMessage().getSide();
                        if (mOrderVerb == null) mOrderVerb = "";

                        if (mOrderVerb.equalsIgnoreCase("B")){
                            mOrderVerb = "2"; //. bid
                        }else if (mOrderVerb.equalsIgnoreCase("S")){
                            mOrderVerb = "3"; //. offer
                        }
                        FEEDMsgOrder fMsg = new FEEDMsgOrder();
                        fMsg.setOrderTime(ITMSoupBinTCPBridgePacketFormat.getTimeDataFeedFormatFromDate(mSheet.getMessageDate())); //. ???
                        fMsg.setOrderCommand(mOrderVerb);
                        String zSymbol = sheetOD.getMessage().getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = zSymbol.split("_")[0];
                            zBoardCode = zSymbol.split("_")[1];
                        }
                        
                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                        }
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                            }
                        }
                        fMsg.setSecurityCode(zStockCode);
                        fMsg.setBoardCode(zBoardCode);
                        fMsg.setBrokerCode(""); //. Blank if board code is Regular (RG) and Cash (TN).
                        fMsg.setPrice(String.valueOf(sheetAO.getMessage().getPrice()));

                        fMsg.setVolume(String.valueOf(sheetAO.getMessage().getQuantity() * sheetOD.getMessage().getRoundLotSize())); //. ??? - mungkin harus dihitung lagi
                        fMsg.setBalance(String.valueOf(sheetAO.getMessage().getQuantity() * sheetOD.getMessage().getRoundLotSize())); //. ??? -
                        String mDomicile = null;
                        if (mDomicile == null) mDomicile = "";

                        if (mDomicile.equalsIgnoreCase("i")){
                            mDomicile = "D";
                        }else if (mDomicile.equalsIgnoreCase("a")){
                            mDomicile = "F";
                        }

                        fMsg.setInvType(mDomicile);
                        fMsg.setOrderNo(String.valueOf(mLastOrderNumber));

                        fMsg.setBestBidPrice("0"); //. ???
                        fMsg.setBestBidVol("0"); //. ???
                        fMsg.setBestOfferPrice("0"); //. ???
                        fMsg.setBestOfferVol("0"); //. ???
                        fMsg.setOrderRef("000000000000");

                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    } else {
                        System.out.println("Tidak dapat lookup SheetOfITCHAddOrder no="+mLastOrderNumber);
                    }
                } else if (itchMessage instanceof ITCHMsgOrderExecuted){
                    ITCHMsgOrderExecuted mMsg = (ITCHMsgOrderExecuted)itchMessage;
                    
                    if (mMsg.getMatchId()> 0){
                        
                        long mLastOriginalOrderNumber = mMsg.getOrderId();
                        
                        SheetOfITCHAddOrder sheetAO = BookOfITCHAddOrder.getInstance.retrieveSheet(mMsg.getOrderId());
                        
                        if (sheetAO != null){
                            SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(sheetAO.getMessage().getOrderBookId());
                            if (sheetOBD != null){
                                FEEDMsgTrade fMsg = new FEEDMsgTrade();
                                
                                String zSymbol = sheetOBD.getMessage().getSymbol().trim();
                                String arrSymbol[] = zSymbol.split("_");
                                String zStockCode = zSymbol;
                                String zBoardCode = "";
                                if (arrSymbol.length > 1) {
                                    zStockCode = zSymbol.split("_")[0];
                                    zBoardCode = zSymbol.split("_")[1];
                                }
                                fMsg.setTradTime(ITMSoupBinTCPBridgePacketFormat.getTimeDataFeedFormatFromDate(mSheet.getMessageDate())); //. ???
                                fMsg.setTradeCommand("0");
                                
                                SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getIssuerId());
                                if (mSheetIssuerDirectory != null) {
                                    //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                                    zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                                }
                                
                                SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                                if (mSheetMarketSegmentDirectory != null) {
                                    String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                                    String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                                    if (arrMarketSegmentName.length > 1) {
                                        zBoardCode = arrMarketSegmentName[1];
                                    }
                                }
                                
                                fMsg.setSecurityCode(zStockCode);
                                fMsg.setBoardCode(zBoardCode);
                                fMsg.setTradeNo(String.valueOf(mMsg.getMatchId())); //. Blank if board code is Regular (RG) and Cash (TN).
                                fMsg.setPrice(String.valueOf(sheetAO.getMessage().getPrice()));
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
                                
                                fMsg.setBestBidPrice("0"); //. ???
                                fMsg.setBestBidVol("0"); //. ???
                                fMsg.setBestOfferPrice("0"); //. ???
                                fMsg.setBestOfferVol("0"); //. ???
                                
                                fMsg.setBuyerType("");
                                fMsg.setSellerType("");
                                //. 20211130 - override investor type dari SheetOfITCHAddOrder
                                if (sheetAO.getMessage().getSide()!= null && sheetAO.getMessage().getSide().equalsIgnoreCase("B")){
                                    fMsg.setBuyerOrderNo(String.valueOf(mLastOriginalOrderNumber));
                                    fMsg.setSellerOrderNo(String.valueOf(mLastOriginalOrderNumber) + "1"); //("0");
                                }else if (sheetAO.getMessage().getSide()!= null && sheetAO.getMessage().getSide().equalsIgnoreCase("S")){
                                    fMsg.setSellerOrderNo(String.valueOf(mLastOriginalOrderNumber));
                                    fMsg.setBuyerOrderNo(String.valueOf(mLastOriginalOrderNumber) + "1");
                                }
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            }   
                        }   
                    } else {
                        System.out.println("ITCHMsgOrderDelete. Order Executed empty id, orderid="+ mMsg.getOrderId());
                    }
                    
                } else if (itchMessage instanceof ITCHMsgOrderExecutedWithPrice){
                    ITCHMsgOrderExecutedWithPrice mMsg = (ITCHMsgOrderExecutedWithPrice)itchMessage;
                    
                    //. FEEDMsgTrade
                    if (mMsg.getMatchId()> 0){
                        SheetOfITCHAddOrder sheetAO = BookOfITCHAddOrder.getInstance.retrieveSheet(mMsg.getMatchId());
                        
                        if (sheetAO != null){
                            SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(sheetAO.getMessage().getOrderBookId());
                            if (sheetOBD != null){
                                FEEDMsgTrade fMsg = new FEEDMsgTrade();
                                
                                String zSymbol = sheetOBD.getMessage().getSymbol().trim();
                                String arrSymbol[] = zSymbol.split("_");
                                String zStockCode = zSymbol;
                                String zBoardCode = "";
                                if (arrSymbol.length > 1) {
                                    zStockCode = zSymbol.split("_")[0];
                                    zBoardCode = zSymbol.split("_")[1];
                                }
                                fMsg.setTradTime(ITMSoupBinTCPBridgePacketFormat.getTimeDataFeedFormatFromDate(mSheet.getMessageDate())); //. ???
                                fMsg.setTradeCommand("0");
                                
                                SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getIssuerId());
                                if (mSheetIssuerDirectory != null) {
                                    //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                                    zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                                }
                                
                                SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                                if (mSheetMarketSegmentDirectory != null) {
                                    String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                                    String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                                    if (arrMarketSegmentName.length > 1) {
                                        zBoardCode = arrMarketSegmentName[1];
                                    }
                                }
                                
                                fMsg.setSecurityCode(zStockCode);
                                fMsg.setBoardCode(zBoardCode);
                                fMsg.setTradeNo(String.valueOf(mMsg.getMatchId())); //. Blank if board code is Regular (RG) and Cash (TN).
                                fMsg.setPrice(String.valueOf(mMsg.getPrice()));
                                fMsg.setVol(String.valueOf(mMsg.getQuantity()* sheetOBD.getMessage().getRoundLotSize())); //. ??? - mungkin harus dihitung lagi
                                
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
                                
                                fMsg.setBestBidPrice("0"); //. ???
                                fMsg.setBestBidVol("0"); //. ???
                                fMsg.setBestOfferPrice("0"); //. ???
                                fMsg.setBestOfferVol("0"); //. ???
                                
                                fMsg.setBuyerType("");
                                fMsg.setSellerType("");
                                //. 20211130 - override investor type dari SheetOfITCHAddOrder
                                if (sheetAO.getMessage().getSide()!= null && sheetAO.getMessage().getSide().equalsIgnoreCase("B")){
                                    fMsg.setBuyerOrderNo(String.valueOf(mMsg.getMatchId()));
                                    fMsg.setSellerOrderNo(String.valueOf(mMsg.getMatchId()) + "1"); //("0");
//                                    if (!StringHelper.isNullOrEmpty(mMsg.getBuyDomicile()) && !StringHelper.isNullOrEmpty(mMsg.getSellDomicile()) ){
//                                        if (mMsg.getBuyDomicile().equalsIgnoreCase(ITCHConsts.ITCHValue.ORDER_DOMICILE_INDONESIA)){
//                                            fMsg.setBuyerType("D");
//                                        }else{
//                                            fMsg.setBuyerType("F");
//                                        }
//                                        
//                                        if (mMsg.getSellDomicile().equalsIgnoreCase(ITCHConsts.ITCHValue.ORDER_DOMICILE_INDONESIA)){
//                                            fMsg.setSellerType("D");
//                                        }else{
//                                            fMsg.setSellerType("F");
//                                        }
//                                    }
                                }else if (sheetAO.getMessage().getSide()!= null && sheetAO.getMessage().getSide().equalsIgnoreCase("S")){
                                    fMsg.setSellerOrderNo(String.valueOf(mMsg.getMatchId()));
                                    fMsg.setBuyerOrderNo(String.valueOf(mMsg.getMatchId()) + "1");
//                                    if (!StringHelper.isNullOrEmpty(mMsg.getBuyDomicile()) && !StringHelper.isNullOrEmpty(mMsg.getSellDomicile()) ){
//                                        if (mMsg.getBuyDomicile().equalsIgnoreCase(ITCHConsts.ITCHValue.ORDER_DOMICILE_INDONESIA)){
//                                            fMsg.setBuyerType("D");
//                                        }else{
//                                            fMsg.setBuyerType("F");
//                                        }
//                                        
//                                        if (mMsg.getSellDomicile().equalsIgnoreCase(ITCHConsts.ITCHValue.ORDER_DOMICILE_INDONESIA)){
//                                            fMsg.setSellerType("D");
//                                        }else{
//                                            fMsg.setSellerType("F");
//                                        }
//                                    }
                                }

                                
                                

                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            }   
                        }   
                    }
                } else if (itchMessage instanceof ITCHMsgTickSizeTable){
                    //...
                } else if (itchMessage instanceof ITCHMsgOrderBookState){
                    //...
                } else if (itchMessage instanceof ITCHMsgSecond){
                    //.??????????????????????????????????????????
                    ITCHMsgSecond mMsg = (ITCHMsgSecond)itchMessage;
                    FEEDMsgHelper.getInstance.mSecond = mMsg.getSeconds();
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
                } else if (itchMessage instanceof ITCHMsgParticipantDirectory){
                    ITCHMsgParticipantDirectory mMsg = (ITCHMsgParticipantDirectory)itchMessage;
                    
                    //. FeedMsgBrokerData
                    FEEDMsgBrokerData fMsg = new FEEDMsgBrokerData();
                    fMsg.setBrokerCode(mMsg.getParticipantId().trim());
                    fMsg.setBrokerName(mMsg.getParticipantDescription().trim());
                    fMsg.setBrokerStatus("0");
                    
                    //.20250807: pastikan data tidak ada yang null
                    if (!StringHelper.isNullOrEmpty(mMsg.getParticipantDescription().trim()) && !StringHelper.isNullOrEmpty(mMsg.getParticipantId().trim())) {
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                } else if (itchMessage instanceof ITCHMsgOrderBookDirectoryMDF){
                    ITCHMsgOrderBookDirectoryMDF mMsg = (ITCHMsgOrderBookDirectoryMDF)itchMessage;
//                    System.out.println(mMsg.getSymbol()+","+mMsg.getFinancialProduct());
//                    
//                    try
//                    {
//                        //System.out.println(_msg);
//                        String filename= "abc.txt";
//                        FileWriter fw = new FileWriter(filename, true); 
//                        fw.write(mMsg.getSymbol()+","+mMsg.getFinancialProduct() + "\r\n");//appends the string to the file
//                        fw.close();
//                    }
//                    catch(IOException ioe)
//                    {
//                        System.err.println("IOException: " + ioe.getMessage());
//                    }
                    
                    //. hanya proses stock yang equity
                    if (mMsg.getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
                        String zSymbol = mMsg.getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = zSymbol.split("_")[0];
                            zBoardCode = zSymbol.split("_")[1];
                        }
                        //.20251113: long name ada yang kosong, pakai asset extended name
//                        String zStockName = mMsg.getLongName().trim();
                        String zStockName = mMsg.getAssetExtendedName().trim();
                        //.jika getAssetExtendedName kosong, coba ambil dari longname
                        if (StringHelper.isNullOrEmpty(zStockName)) {
                            zStockName = mMsg.getLongName().trim();
                        }
                        String zMarketSegment = "";
                        String zStockID = StringHelper.fromLong(mMsg.getIssuerId());
                        String zStockType = "";
                        String zPreOpening = "";
                        String zSymboxSfx = "0";
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(mMsg.getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                                zMarketSegment = arrMarketSegmentName[0];
                            }
                        }

                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(mMsg.getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockID = StringHelper.fromLong(mSheetIssuerDirectory.getMessage().getIssuerId());
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
//                            zStockName = mSheetIssuerDirectory.getMessage().getLongName().trim();
                        }

                        if (!FEEDMsgHelper.getInstance.mapSendStockData.containsKey(zStockCode)){
                            FEEDMsgHelper.getInstance.mapSendStockData.put(zStockCode, 1);

                            FEEDMsgStockData fMsg = new FEEDMsgStockData();

                            fMsg.setSecurityCode(zStockCode);
                            String zStatus = "0"; 
                            fMsg.setSecurityName(zStockName);
                            fMsg.setSecurityStatus(zStatus);
                            fMsg.setSecurityType(zMarketSegment);
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

                            //. ??????????????????
                            FEEDMsgIndices fIndicesMsg = new FEEDMsgIndices();
                            fIndicesMsg.setIndexCode(zStockCode);
                            fIndicesMsg.setExchgBaseValue(String.valueOf(0)); //. ???
                            fIndicesMsg.setExchgMarketValue(String.valueOf(0)); //. ???
                            fIndicesMsg.setIndex(String.valueOf(0));
                            fIndicesMsg.setOpen(String.valueOf(0));
                            fIndicesMsg.setHigh(String.valueOf(0));
                            fIndicesMsg.setLow(String.valueOf(0));
                            fIndicesMsg.setPrevIndex(String.valueOf(0));

                            //. masukkan ke feed stockData hanya financialProductnya 5 = Equity
                            if (mMsg.getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            } else if (mMsg.getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_INDEX) {
    //                            ITMFeedMsgMemory.getInstance.addToMemory(fIndicesMsg, mSheet);
                            } else {
                                System.out.println("Financial product stock="+zStockCode+" => "+mMsg.getFinancialProduct());
                            }
                        }
                        //. simpan stock data                                        
                        StockDataRecord mCmpRec = new StockDataRecord();

    //                    String stockID = StringHelper.fromLong(mOrderBookDirectory.getIssuerId());
                        String szStockStatus = "S"; //. default disini Suspend stock nya

                        mCmpRec.setfSecurityStatus(szStockStatus);
                        mCmpRec.setfSecurityStatus_TN(szStockStatus);
                        mCmpRec.setfSecurityStatus_NG(szStockStatus);

                        //.reset value
                        mCmpRec.setfBoard_RG(null);
                        mCmpRec.setfBoard_TN(null);
                        mCmpRec.setfBoard_NG(null);
                        mCmpRec.setfBoard_TS(null);


                        if (mSheetMarketSegmentDirectory != null) {
                            zStockType = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName();
                            if (mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().equals(QRIDataConst.QRIFieldValue.SECURITY_INSTR_PRE_OPENING)  && szStockStatus.equals(QRIDataConst.QRIFieldValue.SECURITY_STATUS_ACTIVE)) {
                                zPreOpening = StringHelper.fromInt(QRIDataConst.QRIFieldValue.PRE_OPENING_ON);
                            }else {
                                zPreOpening = StringHelper.fromInt(QRIDataConst.QRIFieldValue.PRE_OPENING_OFF);
                            }

                            zSymboxSfx += mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName();
                            
                            //.20260110: Nilai zSymboxSfx dari marketSegment "0WATCH-CALL_RG", maka sekarang pengkondisiannya menggunakan constains _Board saja
                            if (zSymboxSfx.contains("_RG")){
                                mCmpRec.setfBoard_RG(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                                mCmpRec.setfBoard_RG(StringHelper.fromLong(mMsg.getOrderBookId()));
                                mCmpRec.setfLastPrice_RG(StringHelper.fromDouble(0));
                                mCmpRec.setfLotSize(StringHelper.fromLong(mMsg.getRoundLotSize()));
                            }else if (zSymboxSfx.contains("_TN")){
                                mCmpRec.setfBoard_TN(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                                mCmpRec.setfBoard_TN(StringHelper.fromLong(mMsg.getOrderBookId()));
                                mCmpRec.setfLastPrice_TN(StringHelper.fromDouble(0));
                            }else if (zSymboxSfx.contains("_NG")){
                                mCmpRec.setfBoard_NG(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                                mCmpRec.setfLotSize(null);
                                mCmpRec.setfBoard_NG(StringHelper.fromLong(mMsg.getOrderBookId()));
                                mCmpRec.setfLastPrice_NG(StringHelper.fromDouble(0));
                            }

                        } else { //. jika market segment tidak bisa lookup maka pakai ini
                            if (mMsg.getSymbol().trim().contains("_RG")){
                                mCmpRec.setfBoard_RG(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                                mCmpRec.setfBoard_RG(StringHelper.fromLong(mMsg.getOrderBookId()));
                                mCmpRec.setfLastPrice_RG(StringHelper.fromDouble(0));
                                mCmpRec.setfLotSize(StringHelper.fromLong(mMsg.getRoundLotSize()));
                            }else if (mMsg.getSymbol().trim().contains("_TN")){
                                mCmpRec.setfBoard_TN(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                                mCmpRec.setfBoard_TN(StringHelper.fromLong(mMsg.getOrderBookId()));
                                mCmpRec.setfLastPrice_TN(StringHelper.fromDouble(0));
                            }else if (mMsg.getSymbol().trim().contains("_NG")){
                                mCmpRec.setfBoard_NG(StringHelper.fromInt(QRIDataConst.QRIFieldValue.BOARD_SET));
                                mCmpRec.setfLotSize(null);
                                mCmpRec.setfBoard_NG(StringHelper.fromLong(mMsg.getOrderBookId()));
                                mCmpRec.setfLastPrice_NG(StringHelper.fromDouble(0));
                            }
    //                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "mSheetMarketSegment stock="+symbol+" is null.");
                        }

                        mCmpRec.setfSecurityCode(zStockCode);
                        mCmpRec.setfStockType(zMarketSegment);
                        mCmpRec.setfPreOpening(zPreOpening);

                        mCmpRec.setfSecurityID(zStockID);
                        mCmpRec.setfSecurityName(zStockName);

                        mCmpRec.setfPriceStep(StringHelper.fromInt(0));
                        mCmpRec.setfSecurityTradingStatus("V"); //. default nya V, bukan T
                        mCmpRec.setfPrevPrice(StringHelper.fromDouble(0));
                        mCmpRec.setfFaceValue(StringHelper.fromDouble(0));
                        mCmpRec.setfListedSize(StringHelper.fromLong(mMsg.getOutstandingQuantity()));
                        mCmpRec.setfTradeableSize(StringHelper.fromLong(mMsg.getTradableQuantity()));
                        mCmpRec.setfRemark(mMsg.getRemarks());
                        mCmpRec.setfRemark2(mMsg.getRemarks());
                        mCmpRec.setfStockDate(DateTimeHelper.getDateSVRTRXFormat());

                        mCmpRec.setfPreOpening(StringHelper.fromInt(mCmpRec.getPreOpeningByRemarks2(mMsg.getRemarks(), StringHelper.toInt(mCmpRec.getfPreOpening()))));
                        mCmpRec.setfStockMargin(StringHelper.fromInt(QRIDataConst.QRIFieldValue.MARGINABLE_OFF)); //.default.
                        if ((!StringHelper.isNullOrEmpty(mMsg.getRemarks())) && (mMsg.getRemarks().length() > 3)){
                            String zMarginableStatus = mMsg.getRemarks().substring(2, 3);
                            if ((zMarginableStatus.equalsIgnoreCase(QRIDataConst.QRIFieldValue.REMARK_INFO_MARGINABLE)) ||
                                (zMarginableStatus.equalsIgnoreCase(QRIDataConst.QRIFieldValue.REMARK_INFO_MARGINABLE_SHORT))){
                                mCmpRec.setfStockMargin(StringHelper.fromInt(QRIDataConst.QRIFieldValue.MARGINABLE_ON)); //.set.
                            }
                        }
                        mCmpRec.setfStockMargin(StringHelper.fromInt(mCmpRec.getStockMarginByRemarks2(mMsg.getRemarks(), StringHelper.toInt(mCmpRec.getfStockMargin()))));

                        //. simpan ke table database
                        //. simpan stock yang equity saja
                        if (mMsg.getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
//                            System.out.println(mCmpRec.getfSecurityCode() + " = " + mCmpRec.getfSecurityName());
                            DbRiskMgtWriteStockData.getInstance.insertOrUpdateStockData(mCmpRec);
                        }
                    }
                    
//                    
                    
                } else if (itchMessage instanceof ITCHMsgOrderBookState){
                    ITCHMsgOrderBookState mMsg = (ITCHMsgOrderBookState)itchMessage;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                    if (sheetOBD != null){
                        
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
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_BREAK)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_BREAK, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_BREAK_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BREAK_CALL);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_BREAK_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_BREAK_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_BREAK_CALL, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CALL_RANDOM_CLOSE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CALL_RANDOM_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CALL_RANDOM_CLOSE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_CALL_RANDOM_CLOSE)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_CALL_RANDOM_CLOSE, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_CALL_AUCTION:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_CALL_AUCTION);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_CLOSE_CALL_AUCTION)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_CLOSE_CALL_AUCTION, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_NG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_NG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_NG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_CLOSE_NG)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_CLOSE_NG, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_RF:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_RF);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RF);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_CLOSE_RF)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_CLOSE_RF, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_RG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_RG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_CLOSE_RG)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_CLOSE_RG, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_CLOSE_TN:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_CLOSE_TN);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_CLOSE_TN)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_CLOSE_TN, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_END_OF_DAY:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_END_SENDING_RECORDS);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_END_OF_DAY);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (mMsg.getOrderBookId() <= 0) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_MATCHING_CALL_AUCTION:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_MATCHING_CA);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_MATCHING_CALL_AUCTION);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_MATCHING_CALL_AUCTION)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_MATCHING_CALL_AUCTION, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_MATCHING_CLOSE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_MATCHING_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_MATCHING_CLOSE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_MATCHING_CLOSE)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_MATCHING_CLOSE, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_MATCHING_PRE_OPEN:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_MATCHING_PRE_OPEN);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_MATCHING_PRE_OPEN);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_MATCHING_PRE_OPEN)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_MATCHING_PRE_OPEN, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_NON_CANCEL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_NON_CANCELLATION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_NON_CANCEL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_NON_CANCEL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_NON_CANCEL, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_POST_TRADE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_POST_TRADING);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_POST_TRADE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_POST_TRADE)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_POST_TRADE, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_PRE_CLOSE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_PRE_CLOSING);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_PRE_CLOSE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_PRE_CLOSE)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_PRE_CLOSE, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_PRE_OPEN:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_PRE_OPENING);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_PRE_OPEN);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_PRE_OPEN)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_PRE_OPEN, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_RANDOM_CLOSE:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_RANDOM_CLOSE);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_RANDOM_CLOSE);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_RANDOM_CLOSE)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_RANDOM_CLOSE, true);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_NG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_NG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_NG);
                            fMsg.setSession(1);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_1_NG)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_1_NG, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_RF:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_RF);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RF);
                            fMsg.setSession(1);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_1_RF)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_1_RF, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_RG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_RG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(1);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_1_RG)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_1_RG, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(1);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_1_RG_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_1_RG_CALL, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_TN:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_TN);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(1);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_1_TN)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_1_TN, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_1_TN_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_1_TN_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(1);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_1_TN_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_1_TN_CALL, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_NG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_NG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_NG);
                            fMsg.setSession(2);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_2_NG)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_2_NG, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_RF:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_RF);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RF);
                            fMsg.setSession(2);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_2_RF)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_2_RF, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_RG:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_RG);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(2);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_2_RG)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_2_RG, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(2);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_2_RG_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_2_RG_CALL, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_2_TN_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_2_TN_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(2);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_2_TN_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_2_TN_CALL, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_3_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_3_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(3);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_3_RG_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_3_RG_CALL, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_3_TN_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_3_TN_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_TN);
                            fMsg.setSession(3);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_3_TN_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_3_TN_CALL, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_4_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_4_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(4);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_4_RG_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_4_RG_CALL, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SESSION_5_RG_CALL:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SESSION_5_RG_CALL);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_CALL_AUCTION);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(5);
                            if (!chmSessionState.containsKey(ITCHConsts.ITCHStateField.STATE_SESSION_5_RG_CALL)) {
                                ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                                chmSessionState.put(ITCHConsts.ITCHStateField.STATE_SESSION_5_RG_CALL, true);
                            }
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, false);
                            }
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SOBD:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_BEGIN_SENDING_RECORDS);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SOBD);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            //.20250807: tidak dipakai
//                            ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                            break;
                        case ITCHConsts.ITCHStateField.STATE_SUSPEND:
                            fMsg.setStatus(FeedConsts.TradingStatusConsts.TRADINGSTATUS_STATUS_TRADING_SUSPENSION);
                            fMsg.setMessage(ITCHConsts.ITCHStateDesc.STATE_SUSPEND);
                            fMsg.setType(FeedConsts.TradingStatusType.TRADINGSTATUS_TYPE_MSG_ALL);
                            fMsg.setType2(FeedConsts.TradingStatusType2.TRADINGSTATUS_TYPE_MSG_RG);
                            fMsg.setSession(0);
                            if (sheetOBD != null){
                                //. berlaku untuk masing" stock
                                processSuspendReleaseStock(sheetOBD, mSheet, true);
                            }
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
                } else if (itchMessage instanceof ITCHMsgEquilibriumPrice){
                    ITCHMsgEquilibriumPrice mMsg = (ITCHMsgEquilibriumPrice)itchMessage;
                    
                    FEEDMsgTheoreticalPV fMsg = new FEEDMsgTheoreticalPV();
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOBD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                    
                    if (sheetOBD != null){
                        String zSymbol = sheetOBD.getMessage().getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = zSymbol.split("_")[0];
                            zBoardCode = zSymbol.split("_")[1];
                        }
                        
                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                        }
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                            }
                        }
                        
                        fMsg.setSecurityCode(zStockCode);
                        fMsg.setBoard(zBoardCode);
                        fMsg.setPrice(StringHelper.fromDouble(mMsg.getPrice()));
                        fMsg.setVolume(StringHelper.fromLong(mMsg.getBidQuantity()));
                        fMsg.setBestBid(StringHelper.fromDouble(mMsg.getBestBidPrice()));
                        fMsg.setBestBidSize(StringHelper.fromLong(mMsg.getBestBidQuantity()));
                        fMsg.setBestOffer(StringHelper.fromDouble(mMsg.getBestAskPrice()));
                        fMsg.setBestOfferSize(StringHelper.fromLong(mMsg.getBestAskQuantity()));
                        //.-------------
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
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
                    ITCHMsgIndicativeQuote mMsg = (ITCHMsgIndicativeQuote)itchMessage;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                        
                    if (sheetOD != null && mMsg.getIndicativeQuoteId() > 0){ //. new order
//                        System.out.println(mMsg);
                        long lQtyTraded = 0;
                        
//                        lPriceDecimals = sheetOD.getMessage().getDecimalsInPrice();
//                        mMsg.setPriceDecimals(lPriceDecimals);
                        //. FEEDMsgOrder
                        String mOrderVerb = String.valueOf((char)mMsg.getSide());
                        if (mOrderVerb == null) mOrderVerb = "";
                        
                        if (mOrderVerb.equalsIgnoreCase("B")){
                            mOrderVerb = "0";
                        }else if (mOrderVerb.equalsIgnoreCase("A")){
                            mOrderVerb = "1";
                        }
                        
                        FEEDMsgOrder fMsg = new FEEDMsgOrder();
                        String zSymbol = sheetOD.getMessage().getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = arrSymbol[0];
                            zBoardCode = arrSymbol[1];
                        }
                        fMsg.setOrderTime(ITMSoupBinTCPBridgePacketFormat.getTimeDataFeedFormatFromDate(mSheet.getMessageDate())); //. ???
                        fMsg.setOrderCommand(String.valueOf(mOrderVerb));
                        
                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                        }
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                            }
                        }
                        
                        fMsg.setSecurityCode(zStockCode);
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
                        fMsg.setOrderNo(String.valueOf(mMsg.getIndicativeQuoteId()));
                        
                        fMsg.setBestBidPrice("0"); //. ???
                        fMsg.setBestBidVol("0"); //. ???
                        fMsg.setBestOfferPrice("0"); //. ???
                        fMsg.setBestOfferVol("0"); //. ???
                        fMsg.setOrderRef("000000000000");
                        
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                } else if (itchMessage instanceof ITCHMsgIssuerDirectory){
                    //...
                } else if (itchMessage instanceof ITCHMsgMarketByPrice){
                    //...
                } else if (itchMessage instanceof ITCHMsgMarketDirectory){
                    //...
                } else if (itchMessage instanceof ITCHMsgMarketSegmentDirectory){
                    ITCHMsgMarketSegmentDirectory mMsg = (ITCHMsgMarketSegmentDirectory)itchMessage;
                    
                    ConcurrentHashMap<Integer, SheetOfITCHOrderBookDirectoryMDF> mOrderBookDir = BookOfITCHOrderBookDirectoryMDF.getInstance.getOrderBookDirectoryMDFByMarketSegmentID(mMsg.getMarketSegmentId());
                    if (mOrderBookDir != null) {
                        for (SheetOfITCHOrderBookDirectoryMDF value : mOrderBookDir.values()) {
                            StockDataRecord mCmpRec = new StockDataRecord();
                            String zSymbol = value.getMessage().getSymbol().trim();
                            String arrSymbol[] = zSymbol.split("_");
                            String zStockCode = zSymbol;
                            String zMarketSegment = "";
                            if (arrSymbol.length > 1) {
                                zStockCode = zSymbol.split("_")[0];
                            }
                            
                            String zMarketSegmentName = mMsg.getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zMarketSegment = arrMarketSegmentName[0];
                            }
                            
                            mCmpRec.setfSecurityCode(zStockCode);
                            mCmpRec.setfStockType(zMarketSegment);
                            if (value.getMessage().getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
//                                System.out.println(zStockCode);
                                DbRiskMgtWriteStockData.getInstance.insertOrUpdateStockData(mCmpRec);
                            }
                        }
                        
                    }
                } else if (itchMessage instanceof ITCHMsgPriceLimits){
                    //...
                } else if (itchMessage instanceof ITCHMsgReferencePrice){
                    //...
                    ITCHMsgReferencePrice mMsg = (ITCHMsgReferencePrice)itchMessage;
                    boolean isSendRecord = false;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                    
                    if (sheetOD != null 
                            && (mMsg.getPriceType() == ITCHConsts.ITCHPriceTypeField.PRICE_TYPE_EVER_LAST 
                            || mMsg.getPriceType() == ITCHConsts.ITCHPriceTypeField.PRICE_TYPE_CLOSING_PRICE) 
                            && mMsg.getPrice() > 0) {
                        
                        //. hanya proses stock yang equity
                        if (sheetOD.getMessage().getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
                            //. dikirim sebagai prev price
                            isSendRecord = true;
                        }
                    }
                    if (isSendRecord) {
                        //. FEEDMsgStockSummary
                        FEEDMsgStockSummary fMsg = new FEEDMsgStockSummary();
                        String zSymbol = sheetOD.getMessage().getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = zSymbol.split("_")[0];
                            zBoardCode = zSymbol.split("_")[1];
                        }
                        
                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                        }
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                            }
                        }
                        if (mMsg.getPrice() == 0) {
                            if (mMsg.getPriceType() == ITCHConsts.ITCHPriceTypeField.PRICE_TYPE_EVER_LAST) {
                                System.out.println("Stock Code="+zStockCode+" Type Ever Last = 0");
                            }
                            else if (mMsg.getPriceType() == ITCHConsts.ITCHPriceTypeField.PRICE_TYPE_CLOSING_PRICE) {
                                System.out.println("Stock Code="+zStockCode+" Type Closing Price = 0");
                            }
                        }
                        
                        fMsg.setSecurityCode(zStockCode);
                        fMsg.setBoardCode(zBoardCode);

                        double lastPrice = 0;
                        double openPrice = 0;
                        double highPrice = 0;
                        double lowPrice = 0;
                        double prevPrice = mMsg.getPrice();
                        double chgPrice = 0;
                        String sb = zStockCode + "_" + zBoardCode;
                        
                        StockDataRecord mCmpRec = new StockDataRecord();
                        mCmpRec.setfSecurityCode(zStockCode);
                        mCmpRec.setfPrevPrice(StringHelper.fromDouble(prevPrice));

                        //. simpan ke table database
                        if (sheetOD.getMessage().getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
                            DbRiskMgtWriteStockData.getInstance.insertOrUpdateStockData(mCmpRec);
                        }
                        
                        fMsg.setPrevPrice(String.valueOf(prevPrice));
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
                        if (mBTA != null && mBTA.getMessage().getStateName().toLowerCase().contains("suspend")){
                            zStatus = "1";
                        }
                        fMsg.setSecBoardState(zStatus);
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                        //. set ke memori karena akan dipakai lagi di message lain
                        FEEDMsgHelper.getInstance.mapStockPrevPrice.put(sb, prevPrice);
                    }
                } else if (itchMessage instanceof ITCHMsgTickSizeTable){
                    //...
                } else if (itchMessage instanceof ITCHMsgTradeStatistics){
                    //...
                    ITCHMsgTradeStatistics mMsg = (ITCHMsgTradeStatistics)itchMessage;
                    boolean isSendRecord = false;
                    
                    SheetOfITCHOrderBookDirectoryMDF sheetOD = BookOfITCHOrderBookDirectoryMDF.getInstance.retrieveSheet(mMsg.getOrderBookId());
                    
                    if (sheetOD != null) {
                        //. hanya proses stock yang equity
                        if (sheetOD.getMessage().getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
                            //. dikirim sebagai ohlc
                            isSendRecord = true;
                        }
                    }
                    if (isSendRecord) {
                        //. FEEDMsgStockSummary
                        FEEDMsgStockSummary fMsg = new FEEDMsgStockSummary();
                        String zSymbol = sheetOD.getMessage().getSymbol().trim();
                        String arrSymbol[] = zSymbol.split("_");
                        String zStockCode = zSymbol;
                        String zBoardCode = "";
                        if (arrSymbol.length > 1) {
                            zStockCode = zSymbol.split("_")[0];
                            zBoardCode = zSymbol.split("_")[1];
                        }
                        
                        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getIssuerId());
                        if (mSheetIssuerDirectory != null) {
                            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//                            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
                        }
                        
                        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOD.getMessage().getMarketSegmentId());
                        if (mSheetMarketSegmentDirectory != null) {
                            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
                            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
                            if (arrMarketSegmentName.length > 1) {
                                zBoardCode = arrMarketSegmentName[1];
                            }
                        }
                        
                        fMsg.setSecurityCode(zStockCode);
                        fMsg.setBoardCode(zBoardCode);

                        String sb = zStockCode + "_" + zBoardCode;
                        
                        double lastPrice = mMsg.getLastPrice();
                        double openPrice = mMsg.getOpenPrice();
                        double highPrice = mMsg.getHighPrice();
                        double lowPrice = mMsg.getLowPrice();
                        double chgPrice = 0;
                        double prevPrice = 0;
                        
                        if (FEEDMsgHelper.getInstance.mapStockPrevPrice.containsKey(sb)){
                            prevPrice  = FEEDMsgHelper.getInstance.mapStockPrevPrice.get(sb);
                        } 
                        chgPrice = lastPrice - prevPrice;
                        
                        StockDataRecord mCmpRec = new StockDataRecord();
                        mCmpRec.setfSecurityCode(zStockCode);
                        mCmpRec.setfPrevPrice(StringHelper.fromDouble(prevPrice));

                        //. simpan ke table database
                        if (sheetOD.getMessage().getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
                            DbRiskMgtWriteStockData.getInstance.insertOrUpdateStockData(mCmpRec);
                        }
                        
                        fMsg.setPrevPrice(String.valueOf(prevPrice));
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
                        if (mBTA != null && mBTA.getMessage().getStateName().toLowerCase().contains("suspend")){
                            zStatus = "1";
                        }
                        fMsg.setSecBoardState(zStatus);
                        ITMFeedMsgMemory.getInstance.addToMemory(fMsg, mSheet);
                    }
                } else if (itchMessage instanceof ITCHMsgTradeTicker){
                    //...
                } else if (itchMessage instanceof ITCHMsgSecond){
                    //.??????????????????????????????????????????
                    ITCHMsgSecond mMsg = (ITCHMsgSecond)itchMessage;
                    FEEDMsgHelper.getInstance.mSecond = mMsg.getSeconds();
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
        }else if (o instanceof FEEDMsgOrder){
            _msg = ((FEEDMsgOrder)o).toDataFeedMsg();
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
            String zCurDate = DateTimeHelper.getDateSVRTRXFormat();
            if (!zCurDate.equalsIgnoreCase(zLastSentDateDFEndSendingRecord)){
                zLastSentDateDFEndSendingRecord = zCurDate;
                FEEDMsgTradingStatus fMsg = new FEEDMsgTradingStatus();
                fMsg.setStatus("7");
                fMsg.setMessage("End sending records");
                
                this.iSequenceSize++;
                
                fMsg.setSeq(FEEDMsgHelper.getInstance.fmtSeq(lstFeedStr.size() + 1));
                
                Date dtMsg = new Date();
                fMsg.setDate(DateTimeHelper.getDateIDXTRXFormat(dtMsg));
                fMsg.setTime(DateTimeHelper.getTimeSVRTRXFormatFromDate(dtMsg).replaceAll(":", ""));
                
                String zSendMsg = fMsg.toDataFeedMsg();
                if (!StringHelper.isNullOrEmpty(zSendMsg)){
                    if (zSendMsg.endsWith("|")){
                        zSendMsg += "FF";
                    }
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "@ManualSend:EndSendingRecord=" + zSendMsg);
                    
                    lstFeedStr.add(zSendMsg);
                    
                    ConcurrentHashMap<ITMSocketChannel, FeedServerCallbackProcessor> chmClientProcs = FeedServerCallbackController.getInstance.getAllChannelsProcessorsList();
                    
                    if (!chmClientProcs.isEmpty()){
                        for(FeedServerCallbackProcessor mClientProc : chmClientProcs.values()){
                            if (mClientProc != null){
                                ITMSocketChannel mClientCh = mClientProc.getChChannel();
                                if ((mClientCh != null) && (!mClientCh.isChannelAlreadyWasted()) && mClientProc.getAlreadyLoggedIn()){
                                    if (mClientCh.sendMessageDirect(zSendMsg)){
                                        mOut = true;
                                    }
                                }
                            }
                        }
                    }
                    
                }
            }
        }catch(Exception ex0){
           ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void processSuspendReleaseStock(SheetOfITCHOrderBookDirectoryMDF sheetOBD, SheetOfITCHBase mSheet, boolean isSuspend) {
        FEEDSuspendReleaseStock fSuspendReleaseStock = new FEEDSuspendReleaseStock();
        String zSymbol = sheetOBD.getMessage().getSymbol().trim();
        String arrSymbol[] = zSymbol.split("_");
        String zStockCode = zSymbol;
        String zBoardCode = "";
        if (arrSymbol.length > 1) {
            zStockCode = zSymbol.split("_")[0];
            zBoardCode = zSymbol.split("_")[1];
        }

        SheetOfITCHMarketSegmentDirectory mSheetMarketSegmentDirectory = BookOfITCHMarketSegmentDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getMarketSegmentId());
        if (mSheetMarketSegmentDirectory != null) {
            String zMarketSegmentName = mSheetMarketSegmentDirectory.getMessage().getMarketSegmentName().trim();
            String arrMarketSegmentName[] = zMarketSegmentName.split("_");
            if (arrMarketSegmentName.length > 1) {
                zBoardCode = arrMarketSegmentName[1];
            }
        }

        SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(sheetOBD.getMessage().getIssuerId());
        if (mSheetIssuerDirectory != null) {
            //.20251224: sementara diremark karena antara stockcode yang ada di issuerDirectory dengan orderBookDirectory ada yang berbeda
//            zStockCode = mSheetIssuerDirectory.getMessage().getName().trim();
        }
        fSuspendReleaseStock.setSecurityCode(zStockCode);

        String zStatus = isSuspend ? "1" : "0";
        fSuspendReleaseStock.setFlag(zStatus);
        fSuspendReleaseStock.setBoardCode(zBoardCode);

        if (sheetOBD.getMessage().getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
            ITMFeedMsgMemory.getInstance.addToMemory(fSuspendReleaseStock, mSheet);
        }

        StockDataRecord mCmpRec = new StockDataRecord();
        mCmpRec.setfSecurityCode(zStockCode);

        String szStockStatus = "S";
        if (!isSuspend) {
            szStockStatus = "A";
        }

        if (zBoardCode.equalsIgnoreCase("RG")){
            mCmpRec.setfSecurityStatus(szStockStatus);
        } else if (zBoardCode.equalsIgnoreCase("TN")){
            mCmpRec.setfSecurityStatus_TN(szStockStatus);
        } else if (zBoardCode.equalsIgnoreCase("NG")){
             mCmpRec.setfSecurityStatus_NG(szStockStatus);
        }

        mCmpRec.setfSecurityTradingStatus(szStockStatus);
        //. simpan ke table database
        if (sheetOBD.getMessage().getFinancialProduct() == ITCHConsts.ITCHFinancialProductField.FINANCIAL_PRODUCT_EQUITY) {
            DbRiskMgtWriteStockData.getInstance.insertOrUpdateStockData(mCmpRec);
        }
        
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

