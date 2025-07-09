/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbformat;

import com.itm.generic.engine.database.setup.ITMDBQueryBuilder;
import com.itm.xtream.inet.trading.db.record.object.StockDataRecord;
import com.itm.xtream.inet.trading.dbscheme.DbRiskMgtSchemeStock;

/**
 *
 * @author fredy
 */
public class DbRiskMgtFormatSQLServer extends SwDbProgramFormat{
    
    public static class FormatRiskMgtStock {
        
        public FormatRiskMgtStock() {
            //.nothing todo here :)
        }
        
        public static String insertOrUpdateStockData(ITMDBQueryBuilder.QBDBType dbType, StockDataRecord record){ //.method:
            ITMDBQueryBuilder iqb = new ITMDBQueryBuilder(dbType);
            iqb.setSchemaName(DbRiskMgtSchemeStock.TblStockData._SCHEME.cname);
            iqb.setTableName(DbRiskMgtSchemeStock.TblStockData._TABLE.cname);
            //.buat select query:
            iqb.addColumn("NULL", "", ITMDBQueryBuilder.QBDataType.Numeric); //.biar tanpa petik.
            iqb.addWhere(DbRiskMgtSchemeStock.TblStockData.SECURITY_CODE.cname, record.getfSecurityCode());
            String zSelectQuery = iqb.getSelectQuery();
            //.buat insert query:
            iqb.removeAllColumn();
            iqb.removeAllWhere();
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.SECURITY_CODE.cname, record.getfSecurityCode());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.SECURITY_ID.cname, record.getfSecurityID());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.SECURITY_NAME.cname, record.getfSecurityName());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.SECURITY_STATUS.cname, record.getfSecurityStatus());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.LOT_SIZE.cname, record.getfLotSize());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.PRICE_STEP.cname, record.getfPriceStep());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.SECURITY_TRADING_STATUS.cname, record.getfSecurityTradingStatus());
            if (record.getfBoard_RG() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.BOARD_RG.cname, record.getfBoard_RG());}
            if (record.getfBoard_TN() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.BOARD_TN.cname, record.getfBoard_TN());}
            if (record.getfBoard_NG() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.BOARD_NG.cname, record.getfBoard_NG());}
            if (record.getfBoard_TS() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.BOARD_TS.cname, record.getfBoard_TS());}
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.STOCK_TYPE.cname, record.getfStockType());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.PREV_PRICE.cname, record.getfPrevPrice());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.FACE_VALUE.cname, record.getfFaceValue());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.PRE_OPENING.cname, record.getfPreOpening());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.LISTED_SIZE.cname, record.getfListedSize());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.TRADEABLE_SIZE.cname, record.getfTradeableSize());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.STOCK_MARGIN.cname, record.getfStockMargin());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.REMARK.cname, record.getfRemark());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.REMARK2.cname, record.getfRemark2());
            if (record.getfLastPrice_RG() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.LASTPRICE_RG.cname, record.getfLastPrice_RG());}
            if (record.getfLastPrice_TN() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.LASTPRICE_TN.cname, record.getfLastPrice_TN());}
            if (record.getfLastPrice_NG() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.LASTPRICE_NG.cname, record.getfLastPrice_NG());}
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.STOCK_DATE.cname, record.getfStockDate());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.SECURITY_STATUS_TN.cname, record.getfSecurityStatus_TN());
            iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.SECURITY_STATUS_NG.cname, record.getfSecurityStatus_NG());

            String zInsertQuery = iqb.getInsertQuery();
            //.buat update query:
            iqb.removeColumn(DbRiskMgtSchemeStock.TblStockData.SECURITY_CODE.cname); //.key.
            iqb.removeAllWhere();
            iqb.addWhere(DbRiskMgtSchemeStock.TblStockData.SECURITY_CODE.cname, record.getfSecurityCode());
            String zUpdateQuery = iqb.getUpdateQuery();
            //.buat insert or update query:
            String zFinalQuery = "IF EXISTS (" + zSelectQuery + ") " + zUpdateQuery + " ELSE " + zInsertQuery;
            return zFinalQuery;
        }
        
        public static String selectLoadStockData(ITMDBQueryBuilder.QBDBType dbType, String stockDate){ //.method:
            ITMDBQueryBuilder iqb = new ITMDBQueryBuilder(dbType);
            iqb.setSchemaName(DbRiskMgtSchemeStock.TblStockData._SCHEME.cname);
            iqb.setTableName(DbRiskMgtSchemeStock.TblStockData._TABLE.cname);
            //.buat select query:
            iqb.addColumn("*", "");
            ITMDBQueryBuilder iqbMaxDate = new ITMDBQueryBuilder(dbType);
            iqbMaxDate.setSchemaName(DbRiskMgtSchemeStock.TblStockData._SCHEME.cname);
            iqbMaxDate.setTableName(DbRiskMgtSchemeStock.TblStockData._TABLE.cname);
            iqbMaxDate.addColumn("MAX("+ DbRiskMgtSchemeStock.TblStockData.STOCK_DATE.cname + ")", "");
            String zMaxDateQuery = "(" + iqbMaxDate.getSelectQuery() + ")";
            iqb.addWhere(DbRiskMgtSchemeStock.TblStockData.STOCK_DATE.cname, zMaxDateQuery, ITMDBQueryBuilder.QBDataType.Numeric, ITMDBQueryBuilder.QBOperatorType.AND);
            return iqb.getSelectQuery();
        }
        
        public static String updateStockDataLastPrice(ITMDBQueryBuilder.QBDBType dbType, StockDataRecord record){ //.method:
            ITMDBQueryBuilder iqb = new ITMDBQueryBuilder(dbType);
            iqb.setSchemaName(DbRiskMgtSchemeStock.TblStockData._SCHEME.cname);
            iqb.setTableName(DbRiskMgtSchemeStock.TblStockData._TABLE.cname);
            if (record.getfLastPrice_RG() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.LASTPRICE_RG.cname, record.getfLastPrice_RG());}
            if (record.getfLastPrice_TN() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.LASTPRICE_TN.cname, record.getfLastPrice_TN());}
            if (record.getfLastPrice_NG() != null) {iqb.addColumn(DbRiskMgtSchemeStock.TblStockData.LASTPRICE_NG.cname, record.getfLastPrice_NG());}
            //.buat update query:
            iqb.addWhere(DbRiskMgtSchemeStock.TblStockData.SECURITY_CODE.cname, record.getfSecurityCode());
            return iqb.getUpdateQuery();
        }
        
    }
    
}