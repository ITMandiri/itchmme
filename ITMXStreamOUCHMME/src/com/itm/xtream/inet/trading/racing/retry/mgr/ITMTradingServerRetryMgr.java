/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.racing.retry.mgr;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class ITMTradingServerRetryMgr {
    public final static ITMTradingServerRetryMgr getInstance = new ITMTradingServerRetryMgr();
    private int iPriceRetryTime = 0; 
    private ConcurrentHashMap<String, Integer> mapBrokerRefRetryCount = new ConcurrentHashMap<String, Integer>();
    
    public ITMTradingServerRetryMgr(){

        this.iPriceRetryTime = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.invalid_price_retry_time;
            
        
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "iPriceRetryTime = " + iPriceRetryTime);

        
    }
    
    
    public boolean isTimeInRangeOrderRetryInvalidPrice(){ //. jika dalam rentang OrderRetry, maka orderan akan di retry ketika reject dengan reason tertentu
        boolean mOut = false;
        try{
            //. pastikan enable order retry invalid time
            int iOrderRetryInvalidTime = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.invalid_price_retry_time;
            
            if (iOrderRetryInvalidTime > 0){ //. hanya mode <> 1 (retry all) yang pakai rentang jam
                ITMTradingServerSettingsMgr.OUCH_OrderRetry[] times = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_retry_time_invalid_price;
                
                if (times.length > 0){
                    for (ITMTradingServerSettingsMgr.OUCH_OrderRetry time : times) {
                        //. check weekend
                        if (time.tradingdays.length > 0){
                            boolean inDayWeek = false;
                            int iWeekDay = getWeekDay();
                            for (int i = 0; i < time.tradingdays.length; i++){
                                if (iWeekDay == time.tradingdays[i]){
                                    inDayWeek = true;
                                    break;
                                }
                            }
                            if (inDayWeek){
                                mOut =  isCurrentTimeInRange(time.starttime, time.endtime);
                                if (mOut){
                                    break;
                                }
                            }
                            
                        }
                        
                    }
                }
            }
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private int getWeekDay(){
        //. 1 = senin, 2 = selasa, dst
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK );
        dayOfWeek = dayOfWeek -1;
        if (dayOfWeek <= 0){
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }
    
    private boolean isCurrentTimeInRange(String zStartTime, String zEndTime){
        boolean mOut = false;
        
        try{
            if (!DateTimeHelper.hasTimeSVRTRXFormatError(zStartTime) && !DateTimeHelper.hasTimeSVRTRXFormatError(zEndTime)){
                String zCurTime = DateTimeHelper.getTimeSVRTRXFormat();
                int iCurTime = StringHelper.toInt(zCurTime.replaceAll(":", ""));
                int iStartTime = StringHelper.toInt(zStartTime.replaceAll(":", ""));
                int iEndTime = StringHelper.toInt(zEndTime.replaceAll(":", ""));
                if (iCurTime >= iStartTime && iCurTime <= iEndTime){
                    mOut = true;
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        
        return mOut;
    }
    
    private void addBrokerRefRetyCount(String zBrokerRef){
        if (mapBrokerRefRetryCount.containsKey(zBrokerRef)){
            int iRetry = mapBrokerRefRetryCount.get(zBrokerRef) + 1;
            mapBrokerRefRetryCount.put(zBrokerRef, iRetry)  ;
        }else{
            mapBrokerRefRetryCount.put(zBrokerRef, 1)  ;
        }
    }
    
    private int getBrokerRefRetryCount(String zBrokerRef){
        int iRetryCount = 0;
        if (mapBrokerRefRetryCount.containsKey(zBrokerRef)){
            iRetryCount = mapBrokerRefRetryCount.get(zBrokerRef);
        }
        
        return iRetryCount;
    }
    
    public boolean checkIfEligibleToRetry(String zBrokerRef){
        boolean bEligible = false;
        if (this.iPriceRetryTime > 0 && isTimeInRangeOrderRetryInvalidPrice()){
            if (this.getBrokerRefRetryCount(zBrokerRef) < this.iPriceRetryTime){
                bEligible = true;
            }
            this.addBrokerRefRetyCount(zBrokerRef);
        }
        
        return bEligible;
    }

    
        
}
