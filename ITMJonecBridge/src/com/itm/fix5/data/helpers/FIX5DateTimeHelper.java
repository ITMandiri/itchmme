/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.helpers;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5DateTimeHelper extends DateTimeHelper {
    
    public final static String DEF_FORMAT_FIX5_UTC_DATETIME_NORMAL      = "yyyyMMdd-HH:mm:ss";
    public final static String DEF_FORMAT_FIX5_UTC_DATETIME_DETAIL      = "yyyyMMdd-HH:mm:ss.SSS";
    
    public final static String TIME_ZONE_ID_UTC                         = "UTC";
    
    public static String getDateTimeFIX5UTCFormatNormal(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_FIX5_UTC_DATETIME_NORMAL);
            cTimeFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID_UTC));
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateTimeFIX5UTCFormatDetail(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_FIX5_UTC_DATETIME_DETAIL);
            cTimeFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID_UTC));
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static Date getServerDateTimeFromFIX5UTCFormatDetail(String zUTCDateTime){
        Date mOut = null;
        //. hrn : 20211129 : untuk mengakomodir perbedaan format zUTCDateTime
        if (zUTCDateTime.length() > DEF_FORMAT_FIX5_UTC_DATETIME_DETAIL.length()){
            zUTCDateTime = zUTCDateTime.substring(0, DEF_FORMAT_FIX5_UTC_DATETIME_DETAIL.length());
        }else if (zUTCDateTime.length() < DEF_FORMAT_FIX5_UTC_DATETIME_DETAIL.length()){
            zUTCDateTime = zUTCDateTime + ".000"; //. tambah ms
        }
        
        try{
            if ((!StringHelper.isNullOrEmpty(zUTCDateTime)) && (zUTCDateTime.length() >= 8)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_FIX5_UTC_DATETIME_DETAIL);
                cDateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID_UTC));
                mOut = cDateFormat.parse(zUTCDateTime);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static String getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(String zUTCDateTime){
        String zOut = zUTCDateTime;
        try{
            if ((!StringHelper.isNullOrEmpty(zUTCDateTime)) && (zUTCDateTime.length() >= 8)){
                Date mOut = getServerDateTimeFromFIX5UTCFormatDetail(zUTCDateTime);
                zOut = getTimeIDXTRXExecReportFormatFromDate(mOut);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getServerIDXDateTimeStrFromFIX5UTCFormatDetail(String zUTCDateTime){
        String zOut = zUTCDateTime;
        try{
            if ((!StringHelper.isNullOrEmpty(zUTCDateTime)) && (zUTCDateTime.length() >= 8)){
                Date mOut = getServerDateTimeFromFIX5UTCFormatDetail(zUTCDateTime);
                zOut = getDateTimeIDXTRXFormat(mOut);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getServerIDXDateStrFromFIX5UTCFormatDetail(String zUTCDateTime){
        String zOut = zUTCDateTime;
        try{
            if ((!StringHelper.isNullOrEmpty(zUTCDateTime)) && (zUTCDateTime.length() >= 8)){
                Date mOut = getServerDateTimeFromFIX5UTCFormatDetail(zUTCDateTime);
                zOut = getDateIDXTRXFormat(mOut);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
}
