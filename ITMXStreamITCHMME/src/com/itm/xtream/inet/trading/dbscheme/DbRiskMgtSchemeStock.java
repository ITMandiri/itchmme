/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbscheme;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;

/**
 *
 * @author fredy
 */
public class DbRiskMgtSchemeStock {
    
    private final static String Z_NAME_OF_SCHEME                = "stock";
    
    public DbRiskMgtSchemeStock() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public static enum TblStockData {
        //.nama skema:
        _SCHEME                         (Z_NAME_OF_SCHEME, DbSchemeVarsConsts.DbColType.STRING),
        //.nama tabel:
        _TABLE                          ("StockData", DbSchemeVarsConsts.DbColType.STRING),
        //.nama kolom:
        SECURITY_CODE                   ("SecurityCode", DbSchemeVarsConsts.DbColType.STRING),
	SECURITY_ID                     ("SecurityID", DbSchemeVarsConsts.DbColType.STRING),
	SECURITY_NAME                   ("SecurityName", DbSchemeVarsConsts.DbColType.STRING),
	SECURITY_STATUS                 ("SecurityStatus", DbSchemeVarsConsts.DbColType.STRING),
	LOT_SIZE                        ("LotSize", DbSchemeVarsConsts.DbColType.NUMERIC),
	PRICE_STEP                      ("PriceStep", DbSchemeVarsConsts.DbColType.NUMERIC),
	SECURITY_TRADING_STATUS         ("SecurityTradingStatus", DbSchemeVarsConsts.DbColType.STRING),
	BOARD_RG                        ("Board_RG", DbSchemeVarsConsts.DbColType.NUMERIC),
	BOARD_TN                        ("Board_TN", DbSchemeVarsConsts.DbColType.NUMERIC),
	BOARD_NG                        ("Board_NG", DbSchemeVarsConsts.DbColType.NUMERIC),
	BOARD_TS                        ("Board_TS", DbSchemeVarsConsts.DbColType.NUMERIC),
	STOCK_TYPE                      ("StockType", DbSchemeVarsConsts.DbColType.STRING),
	PREV_PRICE                      ("PrevPrice", DbSchemeVarsConsts.DbColType.NUMERIC),
	FACE_VALUE                      ("FaceValue", DbSchemeVarsConsts.DbColType.NUMERIC),
	PRE_OPENING                     ("PreOpening", DbSchemeVarsConsts.DbColType.NUMERIC),
	LISTED_SIZE                     ("ListedSize", DbSchemeVarsConsts.DbColType.NUMERIC),
	TRADEABLE_SIZE                  ("TradeableSize", DbSchemeVarsConsts.DbColType.NUMERIC),
	STOCK_MARGIN                    ("StockMargin", DbSchemeVarsConsts.DbColType.NUMERIC),
	REMARK                          ("Remark", DbSchemeVarsConsts.DbColType.STRING),
        REMARK2                         ("Remark2", DbSchemeVarsConsts.DbColType.STRING),
        LASTPRICE_RG                    ("LastPrice_RG", DbSchemeVarsConsts.DbColType.NUMERIC),
	LASTPRICE_TN                    ("LastPrice_TN", DbSchemeVarsConsts.DbColType.NUMERIC),
	LASTPRICE_NG                    ("LastPrice_NG", DbSchemeVarsConsts.DbColType.NUMERIC),
	STOCK_DATE                      ("StockDate", DbSchemeVarsConsts.DbColType.STRING),
        SECURITY_STATUS_TN              ("SecurityStatus_TN", DbSchemeVarsConsts.DbColType.STRING),
        SECURITY_STATUS_NG              ("SecurityStatus_NG", DbSchemeVarsConsts.DbColType.STRING);
        
        public final String cname;
        public final DbSchemeVarsConsts.DbColType ctype;
        
        private TblStockData(String cname, DbSchemeVarsConsts.DbColType ctype) {
            this.cname = cname;
            this.ctype = ctype;
        }
    }
    
}
