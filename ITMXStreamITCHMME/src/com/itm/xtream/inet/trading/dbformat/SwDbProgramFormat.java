/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbformat;

import com.itm.generic.engine.database.setup.ITMDBConnection;
import com.itm.generic.engine.database.setup.ITMDBQueryBuilder.QBDBType;

/**
 *
 * @author fredy
 */
public class SwDbProgramFormat {
    
    public SwDbProgramFormat() {
        //.nothing todo here :)
    }
    public static class SwFmtProgramSettings{
        
        public SwFmtProgramSettings() {
            //.nothing todo here :)
        }
        
        public static String selectDbSettingSeq(ITMDBConnection mInputConnection, 
            String zInputModuleGroupName
            ){ //.method:
            QBDBType dbType = QBDBType.MSSQL;
            switch(dbType){
                case MSSQL: return DbProgramFormatSQLServer.FormatProgramSettings.selectDbSettingSeq(dbType, zInputModuleGroupName);
            }
            return DbProgramFormatSQLServer.FormatProgramSettings.selectDbSettingSeq(dbType, zInputModuleGroupName); //.default.
        }
        
        public static String selectDbSetting(ITMDBConnection mInputConnection, 
            String zInputModuleGroupName
            ){ //.method:
            QBDBType dbType = QBDBType.MSSQL;
            switch(dbType){
                case MSSQL: return DbProgramFormatSQLServer.FormatProgramSettings.selectDbSetting(dbType, zInputModuleGroupName);
            }
            return DbProgramFormatSQLServer.FormatProgramSettings.selectDbSetting(dbType, zInputModuleGroupName); //.default.
        }
        
    }
}
