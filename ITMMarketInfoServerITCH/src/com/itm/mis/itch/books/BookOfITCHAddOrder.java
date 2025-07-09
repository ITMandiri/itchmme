/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.books;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ari Pambudi
 */
public class BookOfITCHAddOrder extends BookOfITCHBase {
    //.single instance:
    public final static BookOfITCHAddOrder getInstance = new BookOfITCHAddOrder();
    
    private final ArrayList<SheetOfITCHAddOrder> lstSheets = new ArrayList<>();
    private final ConcurrentHashMap<Long, SheetOfITCHAddOrder> chmSheets = new ConcurrentHashMap<>();
    //. bantuan untuk menghindari data *ITCHAddOrder* yang double pada jam 15000 ke MIS/DataFeed
    private final ConcurrentHashMap<String, Integer> chmSheetsFlagCount = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> chmSheetsFlagPrevWithdraw = new ConcurrentHashMap<>();
    //. 20211227 : untuk menentukan suatu OrderNumber terakhir dapat message OrderReplace/AddOrder agar tahu Quantity yang dipakai    
    private final ConcurrentHashMap<Long, Integer> chmSheetsFlagAddOrderVsOrderReplace = new ConcurrentHashMap<>();
    
    public BookOfITCHAddOrder() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.INIT, "");
    }
    
    public boolean addSheet(SheetOfITCHAddOrder mSheet){
        boolean mOut = false;
        try{
            if ((mSheet != null) && (mSheet.getMessage() != null)){
                //this.lstSheets.add(mSheet);
                mOut = true;
                
                //. jika orderNumber > 0
                if (mSheet.getMessage().getOrderId() > 0){
                    if (chmSheets.containsKey(mSheet.getMessage().getOrderId())){
                        chmSheets.replace(mSheet.getMessage().getOrderId(), mSheet);
                        chmSheetsFlagAddOrderVsOrderReplace.replace(mSheet.getMessage().getOrderId(), 1);
                    }else{
                        chmSheets.put(mSheet.getMessage().getOrderId(), mSheet);
                        chmSheetsFlagAddOrderVsOrderReplace.put(mSheet.getMessage().getOrderId(), 1);
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    
    //. 20211227 : menentukan Message AddOrder/OrderReplace yang datang terakhir sebagai Quantity
    public int retrieveSheetFlagAddOrderVsOrderReplace(Long vOrderNumberId){
        Integer mOut = 0;
        try{
            mOut = this.chmSheetsFlagAddOrderVsOrderReplace.get(vOrderNumberId);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    //. 20211227 : ketika ada OrderReplace (ganti qty)
    public int reduceSheetFlagAddOrderVsOrderReplace(Long vOrderNumberId){
        int mOut = 0;
        try{
            if (chmSheetsFlagAddOrderVsOrderReplace.containsKey(vOrderNumberId)){
                chmSheetsFlagAddOrderVsOrderReplace.replace(vOrderNumberId, 0);
            }else{
                chmSheetsFlagAddOrderVsOrderReplace.put(vOrderNumberId, 0);
            }
            mOut = chmSheetsFlagAddOrderVsOrderReplace.get(vOrderNumberId);
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public SheetOfITCHAddOrder retrieveSheet(Long vOrderNumberId){
        SheetOfITCHAddOrder mOut = null;
        try{
            mOut = this.chmSheets.get(vOrderNumberId);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    //. untuk mendapatkan posisi Flag 1 = AddOrder, 0 = SudahCancel di Datafeed
    public int retrieveSheetFlagCount(String vOrderNumberId){
        Integer mOut = 0;
        try{
            mOut = this.chmSheetsFlagCount.get(vOrderNumberId);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    //. untuk memastikan apakah vOrderNumberId sudah pernah witdraw atau belum (case 14:50)
    public boolean retrieveSheetFlagPrevWithdraw(String vOrderNumberId){
        boolean mOut = false;
        try{
            if (this.chmSheetsFlagPrevWithdraw.containsKey(vOrderNumberId)){
                mOut = this.chmSheetsFlagPrevWithdraw.get(vOrderNumberId);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean addSheetFlagPrevWithdraw(String vOrderNumberId){
        boolean mOut = false;
        try{
            if (chmSheetsFlagPrevWithdraw.containsKey(vOrderNumberId)){
                chmSheetsFlagPrevWithdraw.replace(vOrderNumberId, Boolean.TRUE);
            }else{
                chmSheetsFlagPrevWithdraw.put(vOrderNumberId, Boolean.TRUE);
            }
            mOut = chmSheetsFlagPrevWithdraw.get(vOrderNumberId);
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    
    public int addSheetFlagCount(String vOrderNumberId){
        int mOut = 0;
        try{
            if (chmSheetsFlagCount.containsKey(vOrderNumberId)){
                chmSheetsFlagCount.replace(vOrderNumberId, chmSheetsFlagCount.get(vOrderNumberId) + 1);
            }else{
                chmSheetsFlagCount.put(vOrderNumberId, 1);
            }
            mOut = chmSheetsFlagCount.get(vOrderNumberId);
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    //. ketika ada withdraw
    public int reduceSheetFlagCount(String vOrderNumberId){
        int mOut = 0;
        try{
            if (chmSheetsFlagCount.containsKey(vOrderNumberId)){
                chmSheetsFlagCount.replace(vOrderNumberId, chmSheetsFlagCount.get(vOrderNumberId) - 1);
            }else{
                chmSheetsFlagCount.put(vOrderNumberId, -1);
            }
            mOut = chmSheetsFlagCount.get(vOrderNumberId);
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    
    public ArrayList<SheetOfITCHAddOrder> retrieveAllSheets(){
        return this.lstSheets;
    }
    
    public boolean clearBook(){
        boolean mOut = false;
        try{
            this.lstSheets.clear();
            this.chmSheets.clear();
            this.chmSheetsFlagCount.clear();
            this.chmSheetsFlagPrevWithdraw.clear();
            this.chmSheetsFlagAddOrderVsOrderReplace.clear();
            mOut = this.lstSheets.isEmpty();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    
    
    //. helper-helper
//    public SheetOfITCHAddOrder retrieveSheetByLookUp(Long vOrderNumberId){
//        //. menemukan SheetOfITCHAddOrder dari Tree (OrderReplace yang berubah price nya)
//        SheetOfITCHAddOrder mOut = null;
//        try{
//            mOut = this.chmSheets.get(vOrderNumberId);
//            
//            if (mOut == null){
//                Long mOriginalOrderNumber = vOrderNumberId;
//                SheetOfITCHOrderReplace shOrdRep = BookOfITCHOrderReplace.getInstance.retrieveDiffSheet(vOrderNumberId);
//                
//                //. coba dari OrderReplace (mundur sampai habis) : max 100x
//                for (int i = 0; i < 5000; i++){
//                    if (shOrdRep != null){
//                        mOriginalOrderNumber = shOrdRep.getMessage().getOriginalOrderNumber();
//                        shOrdRep = BookOfITCHOrderReplace.getInstance.retrieveDiffSheet(shOrdRep.getMessage().getOriginalOrderNumber());
//                        if (shOrdRep == null){
//                            mOut = this.chmSheets.get(mOriginalOrderNumber);
//                            break;
//                        }
//                    }else{ //. looping pertama
//                        mOut = this.chmSheets.get(mOriginalOrderNumber);
//                        break;
//                    }
//                }
//                
//                if (shOrdRep != null){
//                    System.err.println("ERROR: SheetOfITCHAddOrder Mentok AMEND Check nya");
//                }                
//            }
//            
//        }catch(Exception ex0){
//            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
//        }
//        return mOut;
//    }
//    
//    
//    //. jumlah Qty yang sudah match pada group order ini
//    public long retrieveTradedQtyByLookUp(Long vOrderNumberId){
//        long lOut = 0;
//        
//        SheetOfITCHAddOrder mOut = null;
//        try{
//            mOut = this.chmSheets.get(vOrderNumberId);
//            lOut += BookOfITCHOrderExecuted.getInstance.retrieveSheetQty(vOrderNumberId);
//            lOut += BookOfITCHOrderExecutedWithPrice.getInstance.retrieveSheetQty(vOrderNumberId);
//            
//            if (mOut == null){
//                //. coba dari OrderReplace (mundur sampai habis)
//                Long mOriginalOrderNumber = vOrderNumberId;
//                SheetOfITCHOrderReplace shOrdRep = BookOfITCHOrderReplace.getInstance.retrieveDiffSheet(vOrderNumberId);
//                
//                //. coba dari OrderReplace (mundur sampai habis) : max 5000
//                for (int i = 0; i < 5000; i++){
//                    if (shOrdRep != null){
//                        mOriginalOrderNumber = shOrdRep.getMessage().getOriginalOrderNumber();
//                        shOrdRep = BookOfITCHOrderReplace.getInstance.retrieveDiffSheet(shOrdRep.getMessage().getOriginalOrderNumber());
//
//                        lOut += BookOfITCHOrderExecuted.getInstance.retrieveSheetQty(mOriginalOrderNumber);
//                        lOut += BookOfITCHOrderExecutedWithPrice.getInstance.retrieveSheetQty(mOriginalOrderNumber);
//                        if (shOrdRep == null){
//                            
//                            break;
//                        }
//                    }else{ //. looping pertama
//                        
//                        break;
//                    }
//                }
//                
//                if (shOrdRep != null){
//                    System.err.println("ERROR: retrieveTradedQtyByLookUp Mentok AMEND Check nya");
//                }
//            }
//            
//        }catch(Exception ex0){
//            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
//        }
//        return lOut;
//    }
}
