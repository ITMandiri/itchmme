/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbscheme;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.xtream.inet.trading.dbscheme.DbSchemeVarsConsts.DbColType;

/**
 *
 * @author fredy
 */
public class DbProgramSchemeSettings {
    
    private final static String Z_NAME_OF_SCHEME                = "serversettings";
    
    public DbProgramSchemeSettings() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public static enum TblTS {
        //.nama skema:
        _SCHEME                         (Z_NAME_OF_SCHEME, DbColType.STRING),
        //.nama tabel:
        _TABLE                          ("TS", DbColType.STRING),
        //.nama kolom:
        MODULE                          ("Module", DbColType.STRING),
	CONFIG                          ("Config", DbColType.STRING),
	SETTING                         ("Setting", DbColType.STRING),
	DESCRIPTION                     ("Description", DbColType.STRING),
        //.memory:
        SEQ                             ("Seq", DbColType.NUMERIC);
        
        public final String cname;
        public final DbColType ctype;
        
        private TblTS(String cname, DbColType ctype) {
            this.cname = cname;
            this.ctype = ctype;
        }
    }
    
}
