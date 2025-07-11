/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.replytimeout.mgr;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketMgr;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class ITMTradingServerReplyTimeOutMgr {
    public final static ITMTradingServerReplyTimeOutMgr getInstance = new ITMTradingServerReplyTimeOutMgr();
    
    private final ConcurrentHashMap<Long, Long> chmSheets = new ConcurrentHashMap<>();
    
    public ITMTradingServerReplyTimeOutMgr(){
        int bReplyTimeout = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_reply_timeout;
            
        if (bReplyTimeout > 0){ //. yang butuh timer yang mode single order, untuk firs order racing
            if (bReplyTimeout < 1000){ //. minimal 1 detik
                bReplyTimeout = 1000;
            }
            Timer timer = new Timer();
            TimerTask task = new TimeOutWorker();
            timer.schedule(task, bReplyTimeout , bReplyTimeout);
        }
    }
    
    public boolean addOrUpdateToken(long lToken){
        boolean mOut = false;
        int bReplyTimeout = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_reply_timeout;
        try{
            if (lToken > 0 && bReplyTimeout > 0){
                long mCurrentTime = System.currentTimeMillis();
                this.chmSheets.put(lToken, mCurrentTime);
                mOut = true;

            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private boolean isTimeOutAndNeedDisconnect(){
        boolean mOut = false;
        
        try{
            int vReplyTimeout = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_reply_timeout;
            long vCurrentTime = System.currentTimeMillis();
            if (vReplyTimeout > 0){ //. yang butuh timer yang mode single order, untuk firs order racing
                if (vReplyTimeout < 1000){ //. minimal 1 detik
                    vReplyTimeout = 1000;
                }

                if (chmSheets.size() > 0){
                    for(Long vToken : this.chmSheets.keySet()){
                        long vOrderTime = this.chmSheets.get(vToken);
                        if ((vCurrentTime - vOrderTime) > vReplyTimeout){
                            mOut = true;
                            break;
                        }
                    }
                }

            }
        }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    } 


    public boolean removeToken(long lToken){
        boolean mOut = false;
        try{
            if (lToken > 0 && this.chmSheets.size() > 0 && this.chmSheets.containsKey(lToken)){
                this.chmSheets.remove(lToken);
                mOut = true;

            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void clearToken(){
        try{
            this.chmSheets.clear();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        
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
    public boolean isTimeInRangeOrderTimeout(){ //. jika dalam rentang OrderTimeout, maka di cek apakah orderreply nya timeout atau tidak
        boolean mOut = false;
        try{
            //. pastikan enable order racing
            int bOrderTimeoutEnable = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_reply_timeout;
            
            if (bOrderTimeoutEnable > 0){ //. jika  0 maka enable
                ITMTradingServerSettingsMgr.OUCH_OrderReplyTimeOut[] times = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_reply_timeout_time;
                if (times.length > 0){
                    for (ITMTradingServerSettingsMgr.OUCH_OrderReplyTimeOut time : times) {
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
    
    private class TimeOutWorker extends TimerTask
    {
        @Override
        public void run()
        {
            try{
               
                boolean bInRange = isTimeInRangeOrderTimeout();
                if (bInRange){
                    boolean bShouldDisconnect = isTimeOutAndNeedDisconnect();
                    if (bShouldDisconnect){
                        clearToken(); //. clear memory token
                        int iTotalLine = ITMSoupBinTCPOUCHPacketMgr.getInstance.doDisconnectLine(true, "");
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Auto disconnect OUCH = " + iTotalLine + " Lines");
                        iTotalLine = ITMSoupBinTCPOUCHPacketMgr.getInstance.doConnectLine(true, "");
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "Auto connect OUCH = " + iTotalLine + " Lines");
                    }
                }else{
                    clearToken();
                }
                
            }catch (Exception ex0) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
        }
    }   
}
