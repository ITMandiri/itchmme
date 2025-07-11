/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.racing.mgr;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketMgr;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataNewOrder;
import com.itm.xtream.inet.trading.racing.books.SheetOfORINewOrder;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author fredy
 */
public class ITMTradingServerRacingMgr {
    public final static ITMTradingServerRacingMgr getInstance = new ITMTradingServerRacingMgr();
    
    private boolean bNextForOrderRacing = false;
    private boolean bStillSendingRacing = false;
    private ORIDataNewOrder mCurrentOrderRacing = null;
    private int iMaxDelayOrderRacing = 200;
    private AtomicBoolean bOrderMessageAccepted = new AtomicBoolean(false); //. penanda, apakah BEI sudah buka dalam periode itu 
            
    public ITMTradingServerRacingMgr(){
        //. buat time per detik, untuk cek perusbahn status time in range order racing
        boolean bOrderRacingEnable = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_enable;
        int bOrderRacingMode = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_mode;
            
        if (bOrderRacingEnable && (bOrderRacingMode == 1 ||  bOrderRacingMode == 3)){ //. yang butuh timer yang mode single order, untuk firs order racing
            Timer timer = new Timer();
            TimerTask task = new RacingWorker();
            timer.schedule(task, 400 , 400);
        }
        
        //. timer untuk reset accepted, antisipasi perubahan jam trading
        Timer tmrAcceptedWorker = new Timer();
        TimerTask taskAccepted = new FlagAcceptedResetWorker();
        tmrAcceptedWorker.schedule(taskAccepted, 3000 , 3000); //. setiap 3 detik akan reset flag AcceptedMessage
            
        //. timer untuk buffer
        if (bOrderRacingEnable){
            Timer timer2 = new Timer();
            TimerTask task2 = new BufferWorker();
            timer2.schedule(task2, 1000 , 1000);
        }
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "bOrderRacingEnable = " + bOrderRacingEnable);

        
    }

    public boolean isbNextForOrderRacing() {
        return bNextForOrderRacing;
    }

    public void setbNextForOrderRacing(boolean bNextForOrderRacing) {
        this.bNextForOrderRacing = bNextForOrderRacing;
    }

    public boolean isbStillSendingRacing() {
        return bStillSendingRacing;
    }

    public void setbStillSendingRacing(boolean bStillSendingRacing) {
        this.bStillSendingRacing = bStillSendingRacing;
    }

    public boolean getbOrderMessageAccepted() {
        return bOrderMessageAccepted.get();
    }

    public void setbOrderMessageAccepted(boolean bOrderMessageAccepted) {
        this.bOrderMessageAccepted.set(bOrderMessageAccepted);
    }
    
    
    
    public void doWork(ITMSocketChannel channel, ORIDataNewOrder mInputMsgRequest){
        try{
            mInputMsgRequest.getfSymbol();
            boolean bInRange = isTimeInRangeOrderRacing(false);
            //System.err.println("HRN-ORIDataNewOrder masuk order baru, isTimeInRangeOrderRacing = " + bInRange + ", Total Antrian = " + SheetOfORINewOrder.getInstance.retrieveAllSheets().size());
            if (bInRange){ //. masuk rentang order racing, hold di memory                
                SheetOfORINewOrder.getInstance.addOrUpdateSheet(mInputMsgRequest);
            }else{
                if (!bNextForOrderRacing){
                    //. kirim apa adanya ke ouch
                    //System.err.println("HRN-ORIDataNewOrder Posisi langsung direct");
                    JONECSimWorkDataNewOrder.getInstance.doWork(channel, mInputMsgRequest);
                }else{
                    //System.err.println("HRN-ORIDataNewOrder Posisi masih OrderRacing");
            
                    //. masih ngetuk, ikut antri di memory
                    SheetOfORINewOrder.getInstance.addOrUpdateSheet(mInputMsgRequest);
                    //System.err.println("HRN-Total order yang antri : " + SheetOfORINewOrder.getInstance.retrieveAllSheets().size());
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
    //. 2024-05-07 : racing mode = 3, kirim all langsung tanpa ketuk
    public boolean doReleaseAllPendingOrder(){
        boolean mOut = false;
        //. set semua flag ke asal
        setbNextForOrderRacing(false);
        setbStillSendingRacing(false);
        setCurrentOrderRacing(null);

        try{
        //. hrn : 20240507 - Lemparan OUCH diusakan mendekati titik detik selanjutnya
        //. asumsi X ms (iRacingMaxDelay ms) sebelum pergantian detik berikut nya (X bisa dirubah-rubah)

        long lCurrMsTime = System.currentTimeMillis();
        long lModTimeMS =  lCurrMsTime % 1000;

        // rumus sleep nya (1000 - (Mod CurrentTimeMillis + X (iRacingMaxDelay))
//        long iToSleep = 1000 - (lModTimeMS + ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_max_delay);
        //.20250318: set max delay untuk setiap sesi order racing
        long iToSleep = 1000 - (lModTimeMS + iMaxDelayOrderRacing);

        if (iToSleep > 10){
            try{
                    Thread.sleep(iToSleep);
            }catch(InterruptedException ex){

            }

        }
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Release pending order, max delay: " + iMaxDelayOrderRacing);
        doReleaseAllPendingOrder(false);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    //. untuk mengirim sisa order yang masih pending
    public boolean doReleaseAllPendingOrder(boolean bWithRemoveFirstOrder){
        boolean mOut = false;
        try{
            //. set semua flag ke asal
            setbNextForOrderRacing(false);
            setbStillSendingRacing(false);
            setCurrentOrderRacing(null);
            
            String zRemovedBrokerRef = "";
            ORIDataNewOrder mRemovedOrder = null;
            if (bWithRemoveFirstOrder){
                mRemovedOrder = SheetOfORINewOrder.getInstance.removeFirstSheet();
                if (mRemovedOrder != null){
                    zRemovedBrokerRef = mRemovedOrder.getfClOrdID();
                }
            }
            ConcurrentLinkedQueue<ORIDataNewOrder> allSheet = SheetOfORINewOrder.getInstance.retrieveAllSheets();
            
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Mulai doReleaseAllPendingOrder. bWithRemoveFirstOrder = " + bWithRemoveFirstOrder + ", TotalOrder = " + allSheet.size() + ", BrokerRef = " + zRemovedBrokerRef + ", bWithRemoveFirstOrder = " + bWithRemoveFirstOrder);                           
            while(allSheet.size() > 0){
               ORIDataNewOrder mRequest = SheetOfORINewOrder.getInstance.retrieveFirstSheet();
               JONECSimWorkDataNewOrder.getInstance.doWork(null, mRequest);
               SheetOfORINewOrder.getInstance.removeFirstSheet();
            }
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Selesai doReleaseAllPendingOrder. bWithRemoveFirstOrder = " + bWithRemoveFirstOrder + ", TotalOrder = " + allSheet.size() + ", BrokerRef = " + zRemovedBrokerRef + ", bWithRemoveFirstOrder = " + bWithRemoveFirstOrder);
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean doSendingFirstOrderRacing(){
        boolean mOut = false;
        try{
            String zFirstBrokerRef = "NULL";
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Sebelum Mulai doSendingFirstOrderRacing, BrokerRef = " + zFirstBrokerRef + ", StillSendingRacing = " + isbStillSendingRacing() + ", Size = " + SheetOfORINewOrder.getInstance.retrieveAllSheets().size());
            ORIDataNewOrder firstSheet = SheetOfORINewOrder.getInstance.retrieveFirstSheet();
            if (firstSheet != null){
                setbStillSendingRacing(true);
                zFirstBrokerRef = firstSheet.getfClOrdID();
                setCurrentOrderRacing(firstSheet);
                JONECSimWorkDataNewOrder.getInstance.doWork(null, firstSheet);
            }else{
                setbStillSendingRacing(false);
            }         
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Setelah Mulai doSendingFirstOrderRacing, BrokerRef = " + zFirstBrokerRef + ", StillSendingRacing = " + isbStillSendingRacing() + ", Size = " + SheetOfORINewOrder.getInstance.retrieveAllSheets().size());            
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
    public boolean isTimeInRangeOrderRacing(boolean isCA){ //. jika dalam rentang OrderRacing, maka orderan akan di hold dulu
        boolean mOut = false;
        try{
            //. pastikan enable order racing
            boolean bOrderRacingEnable = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_enable;
            int bOrderRacingMode = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_mode;
            
            if (bOrderRacingEnable && (bOrderRacingMode == 1 || bOrderRacingMode == 3)){ //. hanya mode 1 (ketuk 1 order), dan mode 3 (all order) yang butuh masukan ke pending order
                ITMTradingServerSettingsMgr.OUCH_OrderRacing[] times = (isCA ? ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_time_ca : ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_time);
                if (times.length > 0){
                    for (ITMTradingServerSettingsMgr.OUCH_OrderRacing time : times) {
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
                                    iMaxDelayOrderRacing = time.racingmaxdelay; //.20250318: set max delay untuk setiap sesi order racing
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
    
    public int isTimeInRangeOrderRacingGetBufferSend(boolean isCA){ 
        int mOut = 0;
        try{
            //. pastikan enable order racing
            boolean bOrderRacingEnable = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_enable;
            
            if (bOrderRacingEnable){
                ITMTradingServerSettingsMgr.OUCH_OrderRacing[] times = (isCA ? ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_time_ca : ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_time);
                if (times.length > 0){
                    for (ITMTradingServerSettingsMgr.OUCH_OrderRacing time : times) {
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
                                if (isCurrentTimeInRange(time.starttime, time.endtime)){
                                    mOut = time.bufferSend;
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
    
    public int isTimeInRangeOrderRacingGetBufferReceive(boolean isCA){ 
        int mOut = 0;
        try{
            //. pastikan enable order racing
            boolean bOrderRacingEnable = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_enable;
            
            if (bOrderRacingEnable){
                ITMTradingServerSettingsMgr.OUCH_OrderRacing[] times = (isCA ? ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_time_ca : ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_time);
                if (times.length > 0){
                    for (ITMTradingServerSettingsMgr.OUCH_OrderRacing time : times) {
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
                                if (isCurrentTimeInRange(time.starttime, time.endtime)){
                                    mOut = time.bufferRecv;
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
    
    public boolean isTimeInRangeOrderRetry(boolean isCa){ //. jika dalam rentang OrderRetry, maka orderan akan di retry ketika reject dengan reason tertentu
        boolean mOut = false;
        try{
            //. pastikan enable order racing
            boolean bOrderRacingEnable = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_enable;
            int bOrderRacingMode = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_mode;
            
            if (bOrderRacingEnable && bOrderRacingMode != 1){ //. hanya mode <> 1 (retry all) yang pakai rentang jam
                ITMTradingServerSettingsMgr.OUCH_OrderRetry[] times = (isCa ? ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_retry_time_ca : ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_retry_time);
                
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

    public ORIDataNewOrder getCurrentOrderRacing() {
        return mCurrentOrderRacing;
    }

    public void setCurrentOrderRacing(ORIDataNewOrder mCurrentOrderRacing) {
        this.mCurrentOrderRacing = mCurrentOrderRacing;
    }
    
    //. 2022-07-09 : buat set ulang buffer (jika di perlukan)
    private class BufferWorker extends TimerTask
    {
        @Override
        public void run()
        {
            try{
               
                int iBufferSend = isTimeInRangeOrderRacingGetBufferSend(false);
                if (iBufferSend > 0){
                    int iTotalSetLine1 = ITMSoupBinTCPOUCHPacketMgr.getInstance.doSetSendBufferLine(true, "", iBufferSend);
                        
                }
                
                int iBuferRecv = isTimeInRangeOrderRacingGetBufferReceive(false);
                if (iBuferRecv > 0){
                    int iTotalSetLine2 = ITMSoupBinTCPOUCHPacketMgr.getInstance.doSetRecvBufferLine(true, "", iBuferRecv);
                        
                }
                
            }catch (Exception ex0) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
        }
    }   
    
    private class RacingWorker extends TimerTask
    {
        @Override
        public void run()
        {
            try{
               
                boolean bInRange = isTimeInRangeOrderRacing(false);
                //System.err.println("bInRange = " + bInRange + ", bNextForOrderRacing = " + bNextForOrderRacing + ", bStillSendingRacing = " + bStillSendingRacing);
                if (bInRange && !bNextForOrderRacing){
                    bNextForOrderRacing = true;
                }else if (bInRange && bNextForOrderRacing){
                    //....
                }else if (!bInRange && !bNextForOrderRacing){
                    //. kirim apa adanya ke ouch
                }else if (!bInRange && bNextForOrderRacing){
                    
                    if (!bStillSendingRacing){
                        //. masukan info
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "Mulai trigger doSendingFirstOrderRacing dari event timer..");  
                        //. kirim 1 order (ketuk ke ouch)
                        //. 2024-05-07 cek mode = 1 (ketuk) atau mode = 3 all order
                        int bOrderRacingMode = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.order_racing_mode;
                        if (bOrderRacingMode == 1){
                            doSendingFirstOrderRacing();
                        }else if (bOrderRacingMode == 3){
                            doReleaseAllPendingOrder(); 
                        }
                        
                    }else{
                        
                    }                    
                }
            }catch (Exception ex0) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
        }
    }   

    private class FlagAcceptedResetWorker extends TimerTask
    {
        @Override
        public void run()
        {
            try{
                setbOrderMessageAccepted(false);
            }catch (Exception ex0) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
        }
    }
        
}
