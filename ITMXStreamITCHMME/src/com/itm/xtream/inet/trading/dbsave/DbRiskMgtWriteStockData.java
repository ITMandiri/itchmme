/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbsave;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.xtream.inet.trading.db.record.object.StockDataRecord;
import com.itm.xtream.inet.trading.dbaccess.DBAccessManagement;
import com.itm.xtream.inet.trading.dbformat.SwDbRiskMgtFormat;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class DbRiskMgtWriteStockData{
    //.single instance:
    public final static DbRiskMgtWriteStockData getInstance = new DbRiskMgtWriteStockData();
    private  HashMap<String, StockDataRecord> mapStock = new HashMap<>();
    
    public DbRiskMgtWriteStockData() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    //. 2022-12-06
    public boolean insertOrUpdateStockData_temp(StockDataRecord record){
        return true;
    }
    public boolean insertOrUpdateStockData(StockDataRecord record){
        boolean bOut = false;
        try{
            String zSenderID = null;
            Connection hTrxDbConn = DBAccessManagement.getInstance.getDbMasterAlreadyActiveConn();
            if (hTrxDbConn != null){
                String zQuery;
                Statement stStatement = hTrxDbConn.createStatement();
                zQuery = SwDbRiskMgtFormat.insertOrUpdateStockData(record);
                if (DBAccessManagement.getInstance.doExecuteUpdate(stStatement, zQuery) > 0){
                    bOut = true;
                }else{
                    bOut = false;
                }
            }
            
            //. 2022-12-14 : memory
            mapStock.put(record.getfSecurityCode(), record);
        }catch (Exception ex){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex);
        }
        return bOut;
    }
    
    public boolean updateStockDataLastPrice(StockDataRecord record){
        boolean bOut = false;
        try{
            String zSenderID = null;
            Connection hTrxDbConn = DBAccessManagement.getInstance.getDbMasterAlreadyActiveConn();
            if (hTrxDbConn != null){
                String zQuery;
                Statement stStatement = hTrxDbConn.createStatement();
                zQuery = SwDbRiskMgtFormat.updateStockDataLastPrice(record);                
                if (DBAccessManagement.getInstance.doExecuteUpdate(stStatement, zQuery) > 0){
                    bOut = true;
                }else{
                    bOut = false;
                }
            }
        }catch (Exception ex){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex);
        }
        return bOut;
    }
    
    public boolean isStockCA(String stock){
        boolean b = false;
        
        StockDataRecord stk = null;
        
        if (mapStock.containsKey(stock)){
            stk = mapStock.get(stock);
        }
        
        if (stk != null){
            b = getIsStockCallAuctionByRemarks2(stk.getfRemark2(), false);
        }
        return b;
    }
    
    private String strMidBase1(String inputStr, int pStartPos, int nLength){
        if ((pStartPos <= 0) || (nLength <= 0)){
            return "";
        }
        int maxLen = inputStr.length();
        if (pStartPos > maxLen){
            return "";
        }
        maxLen = maxLen - (pStartPos - 1);
        if (nLength > maxLen){
            nLength = maxLen;
        }
        try{
            return inputStr.substring((pStartPos - 1), ((pStartPos + nLength) - 1));
        }catch(Exception ex){
            return "";
        }
    }
    
    public boolean getIsStockCallAuctionByRemarks2(String zStockRemark2, boolean iDefaultvalue){
        boolean bOut = iDefaultvalue;
        try{
            if ((!StringHelper.isNullOrEmpty(zStockRemark2)) && (zStockRemark2.length() >= QRIDataConst.QRIFieldValue.STOCK_REMARKS2_EFFECTIVE_SIZE)){
                switch(strMidBase1(zStockRemark2, 30, 1)){
                    case QRIDataConst.QRIRemarks.SZREF_IDX_STOCKDATARECORD_REMARK2_30_CODE_WATCHLIST:
                        bOut = true;
                        break;
                    default:
                        //.value tidak dikenal:
                        bOut = false;
                        break;
                }
                //.selesai... .
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.SERVER, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return bOut;
    }
}
