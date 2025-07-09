/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbformat;

import com.itm.generic.engine.database.setup.ITMDBConnection;
import com.itm.generic.engine.database.setup.ITMDBQueryBuilder.QBDBType;
import com.itm.xtream.inet.trading.db.record.object.StockDataRecord;

/**
 *
 * @author fredy
 */
public class SwDbRiskMgtFormat {

    public SwDbRiskMgtFormat() {
        //.nothing todo here :)
    }
    
    
    public static String insertOrUpdateStockData(StockDataRecord record){ //.method:
        QBDBType dbType = QBDBType.MSSQL;
        switch(dbType){
            case MSSQL: return DbRiskMgtFormatSQLServer.FormatRiskMgtStock.insertOrUpdateStockData(dbType, record);
        }
        return DbRiskMgtFormatSQLServer.FormatRiskMgtStock.insertOrUpdateStockData(dbType, record); //.default.
    }
    
    public static String selectLoadStockData(ITMDBConnection mInputConnection, String stockDate){ //.method:
        QBDBType dbType = QBDBType.MSSQL;
        switch(dbType){
            case MSSQL: return DbRiskMgtFormatSQLServer.FormatRiskMgtStock.selectLoadStockData(dbType,stockDate);
        }
        return DbRiskMgtFormatSQLServer.FormatRiskMgtStock.selectLoadStockData(dbType, stockDate); //.default.
    }
    
    public static String updateStockDataLastPrice(StockDataRecord record){ //.method:
        QBDBType dbType = QBDBType.MSSQL;
        switch(dbType){
            case MSSQL: return DbRiskMgtFormatSQLServer.FormatRiskMgtStock.updateStockDataLastPrice(dbType, record);
        }
        return DbRiskMgtFormatSQLServer.FormatRiskMgtStock.updateStockDataLastPrice(dbType, record); //.default.
    }
    
}