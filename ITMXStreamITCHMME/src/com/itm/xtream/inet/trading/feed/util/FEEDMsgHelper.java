/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class FEEDMsgHelper {
    
    private final static int INT_ZERO               = 0;
    private final static long LONG_ZERO             = 0;
    private final static double DOUBLE_ZERO         = 0;
    
    public final static FEEDMsgHelper getInstance = new FEEDMsgHelper();
    
    public ConcurrentHashMap<String, ConcurrentHashMap<String, Double>> mapIndices;
    public ConcurrentHashMap<String, ConcurrentHashMap<String, Double>> mapStock;
    
    public ConcurrentHashMap<String, Integer> mapSendStockData;
    
    public int SessionCount = 0;
    
    public long  mSecond = 0;
    
    private FEEDMsgHelper(){
        mapIndices = new ConcurrentHashMap<>();
        mapStock = new ConcurrentHashMap<>();
        mapSendStockData = new ConcurrentHashMap<>();
    }
    
    public void clearMemory(){
        mapIndices.clear();
        mapStock.clear();
        mapSendStockData.clear();
        SessionCount = 0;
    }

    
    public String getFormattedTimeStamp(){
        long totalSecs = mSecond;
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;

        String timeString = String.format("%02d%02d%02d", hours, minutes, seconds);             
        return timeString;
    }
    
    public String fmtSeq(long seq){
        String zSeq = "00000000";
        
        return (zSeq + String.valueOf(seq)).substring(String.valueOf(seq).length());

        
    }
    
    public int str2Int(String inputStr){
        if (inputStr == null){
            return INT_ZERO;
        }
        if (inputStr.length() <= INT_ZERO){
            return INT_ZERO;
        }
        try{
            int outNumber = Integer.parseInt(inputStr);
            return outNumber;
        }catch(Exception ex){
            return INT_ZERO;
        }
    }
    
    public long str2Long(String inputStr){
        if (inputStr == null){
            return LONG_ZERO;
        }
        if (inputStr.length() <= INT_ZERO){
            return LONG_ZERO;
        }
        try{
            long outNumber = Long.parseLong(inputStr);
            return outNumber;
        }catch(Exception ex){
            return LONG_ZERO;
        }
    }
    
    public double str2Double(String inputStr){
        if (inputStr == null){
            return DOUBLE_ZERO;
        }
        if (inputStr.length() <= INT_ZERO){
            return DOUBLE_ZERO;
        }
        try{
            double outNumber = Double.parseDouble(inputStr);
            return outNumber;
        }catch(Exception ex){
            return DOUBLE_ZERO;
        }
    }
    
    public double getNumberFraction(double v, long fraction){
        double dResult = v;
        if (fraction > 0){
            dResult = v / (Math.pow(10, fraction));
        }
        return dResult;
    }
    
    public int currentTradingDayNumber()
    {
        try{
            int dyCurDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            //.7=Sabtu=6; 1=Minggu=7; 2=Senin=1; dst.
            switch (dyCurDay){
                case Calendar.MONDAY: //.senin.
                    dyCurDay = 1;
                    break;
                case Calendar.TUESDAY: //.selasa.
                    dyCurDay = 2;
                    break;
                case Calendar.WEDNESDAY: //.rabu.
                    dyCurDay = 3;
                    break;
                case Calendar.THURSDAY: //.kamis.
                    dyCurDay = 4;
                    break;
                case Calendar.FRIDAY: //.jumat.
                    dyCurDay = 5;
                    break;
                case Calendar.SATURDAY: //.sabtu.
                    dyCurDay = 6;
                    break;
                case Calendar.SUNDAY: //.minggu.
                    dyCurDay = 7;
                    break;
                default:
                    dyCurDay = 0;
                    break;
            }
            return dyCurDay;
        }catch(Exception ex){
        }
        return 0; //.nilai 0 akan dicocokkan dengan nomer di setting bahwa nilai ini tidak ada.
    }
    
    public int intIdxFmtCurrentPCTime(){
        DateFormat cTimeFormat = new SimpleDateFormat("HHmmss");
        Date cDate = new Date();
        try{
            return Integer.parseInt(cTimeFormat.format(cDate));
        }catch(NumberFormatException nfex){
        }catch(Exception ex){
        }
        return 0;
    }
}

