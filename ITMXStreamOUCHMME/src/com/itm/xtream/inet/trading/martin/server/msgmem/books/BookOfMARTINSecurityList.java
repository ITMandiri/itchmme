/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.msgmem.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataSecuritiesMessage;
import com.itm.mis.itch.books.BookOfITCHIssuerDirectory;
import com.itm.mis.itch.books.BookOfITCHOrderBookDirectory;
import com.itm.mis.itch.books.SheetOfITCHIssuerDirectory;
import com.itm.mis.itch.books.SheetOfITCHOrderBookDirectory;
import com.itm.mis.itch.structs.ITCHMsgOrderBookDirectory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class BookOfMARTINSecurityList {
    
    public final static BookOfMARTINSecurityList getInstance = new BookOfMARTINSecurityList();
    
    private final ConcurrentHashMap<String, QRIDataSecuritiesMessage> chmSheetsRG = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, QRIDataSecuritiesMessage> chmSheetsTN = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, QRIDataSecuritiesMessage> chmSheetsNG = new ConcurrentHashMap<>();
    
    public BookOfMARTINSecurityList(){
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    //. sumber data dari memory ini adalah dari ITCH
    //. fungsi ini untuk ambil sumber data nya
    //. pada kasus aplikasi ITCH dan OUCH pisah server, data ini akan kosong : 20210816
    public boolean synchDataFromITCH(){
        boolean mOut = false;
        try{
            ConcurrentHashMap<Integer, SheetOfITCHOrderBookDirectory> mListOrderBookDirectory = BookOfITCHOrderBookDirectory.getInstance.retrieveAllSheets();
            
            for (Map.Entry<Integer, SheetOfITCHOrderBookDirectory> entry : mListOrderBookDirectory.entrySet()) {
                SheetOfITCHOrderBookDirectory mSheetOrderBookDirectory = entry.getValue();
                //.soon
//                ITCHMsgOrderBookDirectory mOrderBookDirectory = mSheetOrderBookDirectory.getMessage();
//                
//                if (mOrderBookDirectory == null) continue;
//                
//                SheetOfITCHIssuerDirectory mSheetIssuerDirectory = BookOfITCHIssuerDirectory.getInstance.retrieveSheet(mOrderBookDirectory.getIssuerId());
//                SheetOfITCHSectorDirectory mSheetSectorDirectory = BookOfITCHSectorDirectory.getInstance.retrieveSheet(mOrderBookDirectory.getSectorId());
//                
//                
//                QRIDataSecuritiesMessage mSheet = new QRIDataSecuritiesMessage(new HashMap());
//                mSheet.setfRecordNumber(1);
//                if (mSheetIssuerDirectory != null){
//                    mSheet.setfSecurityID(StringHelper.fromLong(mSheetIssuerDirectory.getMessage().getIssuerId()));
//                    mSheet.setfShortName(mSheetIssuerDirectory.getMessage().getLongName());
//                }
//                
//                mSheet.setfSymbol(mOrderBookDirectory.getSecCode());
//                mSheet.setfRemarks(mOrderBookDirectory.getRemarks0());
//                mSheet.setfRemarks2(mOrderBookDirectory.getRemarks1());
//                
//                //. default active
//                mSheet.setfStatus("A");
//                SheetOfITCHOrderBookTradingAction mBTA = BookOfITCHOrderBookTradingAction.getInstance.retrieveSheet(mOrderBookDirectory.getOrderbook());
//                if (mBTA != null && mBTA.getMessage().getTradingState().equalsIgnoreCase("V")){
//                    mSheet.setfStatus("S"); //. suspend
//                }
//                
//                
//                mSheet.setfSecurityTradingStatus("T"); //. ???
//                mSheet.setfMarketCode(mOrderBookDirectory.getSecCode()); //.???
//                mSheet.setfInstrCode(mOrderBookDirectory.getInstrument());
//                mSheet.setfSymbolSfx("0" + mOrderBookDirectory.getGroup());
//                if (mSheetSectorDirectory != null){
//                    mSheet.setfSectorCode(mSheetSectorDirectory.getMessage().getSectorName());
//                }
//                mSheet.setfOrderMethods("");
//                mSheet.setfLotSize((int)mOrderBookDirectory.getSharesPerLot());
//                mSheet.setfMinimumStep(0);
//                mSheet.setfFaceValue(0);
//                mSheet.setfFaceUnit("");
//                mSheet.setfEarnings(0);
//                mSheet.setfPreviousDate("");
//                mSheet.setfPreviousPrice(0); //. ??????
//                mSheet.setfPriceFormat("");
//                mSheet.setfDecimals((int)mOrderBookDirectory.getPriceDecimals());
//                mSheet.setfQtyLimit(0);
//                mSheet.setfVolTraded(0);
//                mSheet.setfValTraded(0);
//                mSheet.setfYield(0);
//                mSheet.setfAccruedInterestRate(0);
//                mSheet.setfPrimaryDist("");
//                mSheet.setfForeignLimit((int)mOrderBookDirectory.getForeignLimit());
//                mSheet.setfForeignLevel(0);
//                mSheet.setfForeignAvail(0);
//                mSheet.setfIndex(0);
//                mSheet.setfOpenOrders(0);
//                mSheet.setfListedSize(mOrderBookDirectory.getListedShares());
//                mSheet.setfTradeableSize(mOrderBookDirectory.getTradeableShares());
//                mSheet.setfAvgLast(0);
//                mSheet.setfListingDate(0);
//                mSheet.setfDeListingDate((int)mOrderBookDirectory.getDelistingOrMaturityDate());
//                mSheet.setfCouponFreq(0);
//                mSheet.setfLastRecord(0);
//                
//                addOrUpdateSheet(mSheet);

            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean addOrUpdateSheet(QRIDataSecuritiesMessage mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getfSymbol() != null) && (mSheet.getfSymbolSfx() != null)){
                String vSymbol = mSheet.getfSymbol();
                String vBoard = mSheet.getfSymbolSfx();
                
                if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardRG.getValue())){
                    if (this.chmSheetsRG.containsKey(vSymbol)){
                        this.chmSheetsRG.replace(vSymbol, mSheet);
                    }else{
                        this.chmSheetsRG.put(vSymbol, mSheet);
                    }
                }else if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardTN.getValue())){
                    if (this.chmSheetsTN.containsKey(vSymbol)){
                        this.chmSheetsTN.replace(vSymbol, mSheet);
                    }else{
                        this.chmSheetsTN.put(vSymbol, mSheet);
                    }
                }else if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardNG.getValue())){
                    if (this.chmSheetsNG.containsKey(vSymbol)){
                        this.chmSheetsNG.replace(vSymbol, mSheet);
                    }else{
                        this.chmSheetsNG.put(vSymbol, mSheet);
                    }
                }                
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public ConcurrentHashMap<String, QRIDataSecuritiesMessage> retrieveAllSheets(String vBoard){
        if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardRG.getValue())){
            return this.chmSheetsRG;
        }else if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardTN.getValue())){
            return this.chmSheetsTN;
        }else if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardNG.getValue())){
            return this.chmSheetsNG;
        }else{
            return null;
        } 
    }
    
    public QRIDataSecuritiesMessage retrieveSheet(QRIDataSecuritiesMessage vMsg){
        QRIDataSecuritiesMessage mOut = null;
        try{
            String vSymbol = vMsg.getfSymbol();
            String vBoard = vMsg.getfSymbolSfx();

            if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardRG.getValue())){
                mOut = this.chmSheetsRG.get(vSymbol);
            }else if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardTN.getValue())){
                mOut = this.chmSheetsTN.get(vSymbol);
            }else if (vBoard.equalsIgnoreCase(QRIDataConst.SymbolSfx.BoardNG.getValue())){
                mOut = this.chmSheetsNG.get(vSymbol);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.chmSheetsRG.clear();
            this.chmSheetsTN.clear();
            this.chmSheetsNG.clear();
            mOut = this.chmSheetsRG.isEmpty() && this.chmSheetsTN.isEmpty() && this.chmSheetsNG.isEmpty();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
}
