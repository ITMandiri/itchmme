/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.filelogger.setup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author aripam
 */

public class ITMFileLoggerHelper {
    
    public final static String DEF_FORMAT_DATETIME_NORMAL       = "dd/MM/yyyy|HH:mm:ss.SSSS";
    public final static String DEF_FORMAT_DATE_FILESAFE         = "yyyyMMdd";
    
    public ITMFileLoggerHelper() {
        //.nothing todo here :)
    }
    
    public static String strLogDateTime(){
        DateFormat cDateTimeFormat = new SimpleDateFormat(DEF_FORMAT_DATETIME_NORMAL);
        Date cDate = new Date();
        return cDateTimeFormat.format(cDate);
    }
    
    public static String strLogDateFileSafe(){
        DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_DATE_FILESAFE);
        Date cDate = new Date();
        return cDateFormat.format(cDate);
    }
    
}
