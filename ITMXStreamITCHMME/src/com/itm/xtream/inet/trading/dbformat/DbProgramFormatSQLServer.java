/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbformat;

import com.itm.generic.engine.database.setup.ITMDBQueryBuilder;
import com.itm.generic.engine.database.setup.ITMDBQueryBuilder.QBDBType;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.xtream.inet.trading.dbscheme.DbProgramSchemeSettings;

/**
 *
 * @author fredy
 */
public class DbProgramFormatSQLServer {
    
    public DbProgramFormatSQLServer() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public static class FormatProgramSettings{
        
        public FormatProgramSettings() {
            //.nothing todo here :)
        }
        
        public static String selectDbSetting(QBDBType dbType, 
            String zInputModuleGroupName
            ){ //.method:
            ITMDBQueryBuilder iqb = new ITMDBQueryBuilder(dbType);
            iqb.setSchemaName(DbProgramSchemeSettings.TblTS._SCHEME.cname);
            iqb.setTableName(DbProgramSchemeSettings.TblTS._TABLE.cname);
            iqb.addColumn("*", "");
            iqb.addWhere(DbProgramSchemeSettings.TblTS.MODULE.cname, zInputModuleGroupName);
            return iqb.getSelectQuery();
        }
        
        public static String selectDbSettingSeq(QBDBType dbType, 
            String zInputModuleGroupName
            ){ //.method:
            ITMDBQueryBuilder iqb = new ITMDBQueryBuilder(dbType);
            iqb.setSchemaName(DbProgramSchemeSettings.TblTS._SCHEME.cname);
            iqb.setTableName(DbProgramSchemeSettings.TblTS._TABLE.cname);
            iqb.addColumn("*", "");
            iqb.addColumn("RIGHT(" + DbProgramSchemeSettings.TblTS.CONFIG.cname + ", CHARINDEX('_',REVERSE(" + DbProgramSchemeSettings.TblTS.CONFIG.cname + ")) - 1) AS " + DbProgramSchemeSettings.TblTS.SEQ.cname, "");
            iqb.addWhere(DbProgramSchemeSettings.TblTS.MODULE.cname, zInputModuleGroupName);
            return iqb.getSelectQuery();
        }
        
    }
}
