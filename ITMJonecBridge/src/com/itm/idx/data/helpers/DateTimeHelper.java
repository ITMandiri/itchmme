/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.helpers;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author aripam
 */
public class DateTimeHelper {
    
    public final static String DEF_FORMAT_IDX_TRX_DATETIME_NORMAL   = "yyyyMMdd-HH:mm:ss";
    public final static String DEF_FORMAT_SVR_TRX_DATETIME_NORMAL   = "yyyy-MM-dd HH:mm:ss";
    public final static String DEF_FORMAT_IDX_TRX_DATE_NORMAL       = "yyyyMMdd";
    public final static String DEF_FORMAT_SVR_TRX_DATE_NORMAL       = "yyyy-MM-dd";
    public final static String DEF_FORMAT_SVR_TRX_TIME_NORMAL       = "HH:mm:ss";
    
    public final static String DEF_FORMAT_IDN_TRX_DATE_NORMAL       = "dd-MMM-yyyy";
    public final static String DEF_FORMAT_IDX_TRX_TIME_EXEC_REPORT  = "HHmmssSSS";
    
    
    public final static int I_SERVERDAY_UNKNOWN                     = 0;
    public final static int I_SERVERDAY_MONDAY                      = 1;
    public final static int I_SERVERDAY_TUESDAY                     = 2;
    public final static int I_SERVERDAY_WEDNESDAY                   = 3;
    public final static int I_SERVERDAY_THURSDAY                    = 4;
    public final static int I_SERVERDAY_FRIDAY                      = 5;
    public final static int I_SERVERDAY_SATURDAY                    = 6;
    public final static int I_SERVERDAY_SUNDAY                      = 7;
    
    public final static String Z_SERVERDAY_UNKNOWN                  = "???";
    public final static String Z_SERVERDAY_SENIN                    = "Senin";
    public final static String Z_SERVERDAY_SELASA                   = "Selasa";
    public final static String Z_SERVERDAY_RABU                     = "Rabu";
    public final static String Z_SERVERDAY_KAMIS                    = "Kamis";
    public final static String Z_SERVERDAY_JUMAT                    = "Jum'at";
    public final static String Z_SERVERDAY_SABTU                    = "Sabtu";
    public final static String Z_SERVERDAY_MINGGU                   = "Minggu";
    
    
    public DateTimeHelper() {
    }
    
    public static String getDateTimeIDXTRXFormat(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATETIME_NORMAL);
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateTimeSVRTRXFormat(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATETIME_NORMAL);
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateSVRTRXFormat(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getTimeSVRTRXFormat(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static Date getDateSVR(){
        Date mOut = null;
        try{
            //.otomatis hari ini:
            mOut = new Date();
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static String getDateIDXTRXFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATE_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateSVRTRXFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateTimeIDXTRXFormat(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATETIME_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateIDXTRXFormat(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATE_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getTimeSVRTRXFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getTimeIDXTRXExecReportFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_TIME_EXEC_REPORT);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateTimeIDXTRXExecReportFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATETIME_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static Date parseToDateTimeIDXFormat(String zInputDateString){
        Date mOut = null;
        try{
            if (!StringHelper.isNullOrEmpty(zInputDateString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATETIME_NORMAL);
                mOut = cDateFormat.parse(zInputDateString);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static Date parseToDateTimeSVRFormat(String zInputDateString){
        Date mOut = null;
        try{
            if (!StringHelper.isNullOrEmpty(zInputDateString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATETIME_NORMAL);
                mOut = cDateFormat.parse(zInputDateString);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static Date parseToDateIDXFormat(String zInputDateString){
        Date mOut = null;
        try{
            if (!StringHelper.isNullOrEmpty(zInputDateString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATE_NORMAL);
                mOut = cDateFormat.parse(zInputDateString);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static Date parseToDateSVRFormat(String zInputDateString){
        Date mOut = null;
        try{
            if (!StringHelper.isNullOrEmpty(zInputDateString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
                mOut = cDateFormat.parse(zInputDateString);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static Date parseToTimeSVRFormat(String zInputTimeString){
        Date mOut = null;
        try{
            if (!StringHelper.isNullOrEmpty(zInputTimeString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
                mOut = cDateFormat.parse(zInputTimeString);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static boolean hasCommaDelimitedTimeSVRTRXFormatError(String zInputTimeString, boolean bAllowEmptyTime){
        boolean bOut = false; //.09:00:00,13:30:00,... .
        try{
            if (!StringHelper.isNullOrEmpty(zInputTimeString)){
                String[] arrInputTimes = StringHelper.splitAll(zInputTimeString, ",");
                if (arrInputTimes.length > 0){
                    for (String zTime : arrInputTimes){
                        if (bAllowEmptyTime == true && (StringHelper.isNullOrEmpty(zTime))){ //.apm:20221115:ref hrn:allow empty time;
                            continue;
                        }
                        if (hasTimeSVRTRXFormatError(zTime)){
                            bOut = true;
                            break;
                        }
                    }
                }else{
                    bOut = true;
                }
            }else{
                bOut = true;
            }
        }catch(Exception ex0){
            bOut = true;
            //.EXXX.
        }
        return bOut;
    }
    
    public static boolean hasTimeSVRTRXFormatError(String zInputTimeString){
        boolean bOut = false;
        try{
            if (!StringHelper.isNullOrEmpty(zInputTimeString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
                Date mOut = cDateFormat.parse(zInputTimeString);
                if (mOut == null){
                    bOut = true;
                }
            }else{
                bOut = true;
            }
        }catch(Exception ex0){
            bOut = true;
            //.EXXX.
        }
        return bOut;
    }
    
    public static boolean hasDateSVRTRXFormatError(String zInputTimeString){
        boolean bOut = false;
        try{
            if (!StringHelper.isNullOrEmpty(zInputTimeString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
                Date mOut = cDateFormat.parse(zInputTimeString);
                if (mOut == null){
                    bOut = true;
                }
            }else{
                bOut = true;
            }
        }catch(Exception ex0){
            bOut = true;
            //.EXXX.
        }
        return bOut;
    }
    
    public static Date addDays(Date dateStart, int iDaysAddCount){
        Date dOut = null; //iDaysAddCount relatif terhadap hari ini. (negatif = dikurangi, positif = ditambah).
        try{
            if (dateStart != null){
                dOut = dateStart;
                if (iDaysAddCount != 0){
                    Calendar gCalendar = new GregorianCalendar();
                    gCalendar.setTime(dateStart);
                    gCalendar.add(GregorianCalendar.DAY_OF_MONTH, iDaysAddCount);
                    dOut = gCalendar.getTime();
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return dOut;
    }
    
    public static Date addMinutes(Date dateStart, int iMinutesAddCount){
        Date dOut = null; //iDaysAddCount relatif terhadap hari ini. (negatif = dikurangi, positif = ditambah).
        try{
            if (dateStart != null){
                dOut = dateStart;
                if (iMinutesAddCount != 0){
                    Calendar gCalendar = new GregorianCalendar();
                    gCalendar.setTime(dateStart);
                    gCalendar.add(GregorianCalendar.MINUTE, iMinutesAddCount);
                    dOut = gCalendar.getTime();
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return dOut;
    }
    
    public static int compareTwoDate(Date dateA, Date dateB){
        int mOut = (0); //.dateA terhadap dateB. (-1=dateA < dateB; 0=dateA==dateB; 1=dateA > dateB).
        try{
            if ((dateA == null) && (dateB == null)){
                mOut = 0;
            }else if ((dateA == null) && (dateB != null)){
                mOut = (-1);
            }else if ((dateA != null) && (dateB == null)){
                mOut = (1);
            }else{
                mOut = dateA.compareTo(dateB);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static int compareTwoDate(Date dateA, Date dateB, boolean bWithDate, boolean bWithTime){
        int mOut = (0); //.dateA terhadap dateB. (-1=dateA < dateB; 0=dateA==dateB; 1=dateA > dateB).
        try{
            if ((dateA == null) && (dateB == null)){
                mOut = 0;
            }else if ((dateA == null) && (dateB != null)){
                mOut = (-1);
            }else if ((dateA != null) && (dateB == null)){
                mOut = (1);
            }else if ((!bWithDate) && (!bWithTime)){
                //.tidak ada yg dicocokkan:
                mOut = 0;
            }else if ((bWithDate) && (!bWithTime)){
                //.cocokkan date saja (hapus time):
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateA);
                Date dtCmpA = new GregorianCalendar(gCalendar.get(GregorianCalendar.YEAR), gCalendar.get(GregorianCalendar.MONTH), gCalendar.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
                gCalendar.setTime(dateB);
                Date dtCmpB = new GregorianCalendar(gCalendar.get(GregorianCalendar.YEAR), gCalendar.get(GregorianCalendar.MONTH), gCalendar.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
                mOut = dtCmpA.compareTo(dtCmpB);
            }else if ((!bWithDate) && (bWithTime)){
                //.cocokkan time saja (hapus date):
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateA);
                long lTimeCmpA = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1); //.ambil time saja.
                gCalendar.setTime(dateB);
                long lTimeCmpB = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1); //.ambil time saja.
                if (lTimeCmpA < lTimeCmpB){
                    mOut = (-1);
                }else if (lTimeCmpA > lTimeCmpB){
                    mOut = (1);
                }else if (lTimeCmpA == lTimeCmpB){
                    mOut = (0);
                }
            }else{
                //.semua(date & time):
                mOut = dateA.compareTo(dateB);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static long diffDateAbsolute(Date dateA, Date dateB){
        long mOut = (-1); //.dateA terhadap dateB. (-1=dateA < dateB; 0=dateA==dateB; 1=dateA > dateB).
        try{
            if ((dateA == null) && (dateB == null)){
                mOut = (-1);
            }else if ((dateA == null) && (dateB != null)){
                mOut = (-1);
            }else if ((dateA != null) && (dateB == null)){
                mOut = (-1);
            }else{
                //.cocokkan date saja (hapus time):
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateA);
                Date dtCmpA = new GregorianCalendar(gCalendar.get(GregorianCalendar.YEAR), gCalendar.get(GregorianCalendar.MONTH), gCalendar.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
                gCalendar.setTime(dateB);
                Date dtCmpB = new GregorianCalendar(gCalendar.get(GregorianCalendar.YEAR), gCalendar.get(GregorianCalendar.MONTH), gCalendar.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
                long lCmpDiff = Math.abs(dtCmpA.getTime() - dtCmpB.getTime());
                mOut = lCmpDiff / (1000 * 60 * 60 * 24);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static boolean timeOnlyBetween(Date dateFind, Date dateFrom, Date dateTo, boolean bInclusiveFrom, boolean bInclusiveTo){
        boolean bOut = false; //.mengabaikan date(). hanya time yg dilihat. kalau ada parameter yg null dianggap tidak ditemukan. @inclusive: dianggap date tsb ikut dihitung (bukan batas luar).
        try{
            if ((dateFind != null) && (dateFrom != null) && (dateTo != null)){
                //.netralkan date:
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateFind);
                long lTimeFind = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                gCalendar.setTime(dateFrom);
                long lTimeFrom = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                gCalendar.setTime(dateTo);
                long lTimeTo = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                if ((lTimeFind > lTimeFrom) && (lTimeFind < lTimeTo)){
                    bOut = true;
                }else if (bInclusiveFrom){
                    if (lTimeFind == lTimeFrom){
                        bOut = true;
                    }
                }else if (bInclusiveTo){
                    if (lTimeFind == lTimeTo){
                        bOut = true;
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return bOut;
    }
    
    public static boolean timeOnlyBetweenCA(Date dateFind, Date dateFrom, Date dateTo, boolean bInclusiveFrom, boolean bInclusiveTo, boolean bIgnoreDateTo){
        boolean bOut = false; //.mengabaikan date(). hanya time yg dilihat. kalau ada parameter yg null dianggap tidak ditemukan. @inclusive: dianggap date tsb ikut dihitung (bukan batas luar).
        try{
            //. untuk fungsi ini dateTo tidak wajib
            if ((dateFind != null) && (dateFrom != null)){
                //.netralkan date:
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateFind);
                long lTimeFind = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                gCalendar.setTime(dateFrom);
                long lTimeFrom = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                
                long lTimeTo = Long.MAX_VALUE;
                if (dateTo != null){
                    gCalendar.setTime(dateTo);
                    lTimeTo = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                }
                
                if ((lTimeFind > lTimeFrom) && (lTimeFind < lTimeTo) && bIgnoreDateTo == false){
                    bOut = true;
                }else if ((lTimeFind > lTimeFrom) && bIgnoreDateTo == true){
                    bOut = true;
                }else if (bInclusiveFrom){
                    if (lTimeFind == lTimeFrom){
                        bOut = true;
                    }
                }else if (bInclusiveTo){
                    if (lTimeFind == lTimeTo){
                        bOut = true;
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return bOut;
    }
    
    public static int determineTradingDayNumberByDate(Date dtTarget){
        int iOut = I_SERVERDAY_UNKNOWN;
        try{
            if (dtTarget != null){
                GregorianCalendar ggCal = new GregorianCalendar();
                ggCal.setTime(dtTarget);
                int dyGetDay = ggCal.get(Calendar.DAY_OF_WEEK);
                //.7=Sabtu=6; 1=Minggu=7; 2=Senin=1; dst.
                switch (dyGetDay){
                    case Calendar.MONDAY: //.senin.
                        dyGetDay = I_SERVERDAY_MONDAY;
                        break;
                    case Calendar.TUESDAY: //.selasa.
                        dyGetDay = I_SERVERDAY_TUESDAY;
                        break;
                    case Calendar.WEDNESDAY: //.rabu.
                        dyGetDay = I_SERVERDAY_WEDNESDAY;
                        break;
                    case Calendar.THURSDAY: //.kamis.
                        dyGetDay = I_SERVERDAY_THURSDAY;
                        break;
                    case Calendar.FRIDAY: //.jumat.
                        dyGetDay = I_SERVERDAY_FRIDAY;
                        break;
                    case Calendar.SATURDAY: //.sabtu.
                        dyGetDay = I_SERVERDAY_SATURDAY;
                        break;
                    case Calendar.SUNDAY: //.minggu.
                        dyGetDay = I_SERVERDAY_SUNDAY;
                        break;
                    default:
                        dyGetDay = I_SERVERDAY_UNKNOWN;
                        break;
                }
                iOut = dyGetDay;
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return iOut;
    }
    
    public static int currentTradingDayNumber()
    {
        try{
            int dyCurDay = determineTradingDayNumberByDate(getDateSVR());
            return dyCurDay;
        }catch(Exception ex){
        }
        return I_SERVERDAY_UNKNOWN; //.nilai 0 akan dicocokkan dengan nomer di setting bahwa nilai ini tidak ada.
    }
    
    public static String getTradingDayNameIDN(Date dtTarget){
        String zOut = Z_SERVERDAY_UNKNOWN;
        try{
            switch(determineTradingDayNumberByDate(dtTarget)){
                case I_SERVERDAY_MONDAY:
                    zOut = Z_SERVERDAY_SENIN;
                    break;
                case I_SERVERDAY_TUESDAY:
                    zOut = Z_SERVERDAY_SELASA;
                    break;
                case I_SERVERDAY_WEDNESDAY:
                    zOut = Z_SERVERDAY_RABU;
                    break;
                case I_SERVERDAY_THURSDAY:
                    zOut = Z_SERVERDAY_KAMIS;
                    break;
                case I_SERVERDAY_FRIDAY:
                    zOut = Z_SERVERDAY_JUMAT;
                    break;
                case I_SERVERDAY_SATURDAY:
                    zOut = Z_SERVERDAY_SABTU;
                    break;
                case I_SERVERDAY_SUNDAY:
                    zOut = Z_SERVERDAY_MINGGU;
                    break;
                default:
                    break;
            }
        }catch(Exception ex){
        }
        return zOut;
    }
    
    public static String getTradingDateNameIDN(Date dtTarget){
        String zOut = Z_SERVERDAY_UNKNOWN;
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDN_TRX_DATE_NORMAL);
            zOut = cTimeFormat.format(dtTarget);
        }catch(Exception ex){
        }
        return zOut;
    }
    
    public static boolean isValidDateFormatString(String zInputDateString, String zDateFormat){
        boolean mOut = false;
        try{
            if (
                (!StringHelper.isNullOrEmpty(zInputDateString)) 
                && (!StringHelper.isNullOrEmpty(zDateFormat))
                && (zInputDateString.length() == zDateFormat.length())
            ){
                SimpleDateFormat sdf = new SimpleDateFormat(zDateFormat);
                if (zInputDateString.equals(sdf.format(sdf.parse(zInputDateString)))){
                    mOut = true;
                }
            }
        }catch(ParseException ex){
        }catch(Exception ex){
        }
        return mOut;
    }
    
    public static Date applyPartsYearMonthDayToDate(Date dtSourceDate, Date dtDestDate){
        Date mOut = dtDestDate;
        if (mOut != null){
            Calendar gSourceCalendar = new GregorianCalendar();
            gSourceCalendar.setTime(dtSourceDate);
            Calendar gDestCalendar = new GregorianCalendar();
            gDestCalendar.setTime(dtDestDate);
            gDestCalendar.set(GregorianCalendar.YEAR, gSourceCalendar.get(GregorianCalendar.YEAR));
            gDestCalendar.set(GregorianCalendar.MONTH, gSourceCalendar.get(GregorianCalendar.MONTH));
            gDestCalendar.set(GregorianCalendar.DAY_OF_MONTH, gSourceCalendar.get(GregorianCalendar.DAY_OF_MONTH));
            mOut = gDestCalendar.getTime();
        }
        return mOut;
    }
    
}
