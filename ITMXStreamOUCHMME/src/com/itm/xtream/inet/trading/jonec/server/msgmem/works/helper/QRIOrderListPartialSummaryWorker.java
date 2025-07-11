/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.msgmem.works.helper;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author fredy
 */
public class QRIOrderListPartialSummaryWorker {
    //.single instance:
    public final static QRIOrderListPartialSummaryWorker getInstance = new QRIOrderListPartialSummaryWorker();
    
    private TimerTask timerTask;
    private Timer timer;
    private ConcurrentHashMap<Long, QRIDataOrderListMessage> mOrderListSummary;
    
    private QRIOrderListPartialSummaryWorker(){
        timerTask = new QRIOrderListPartialSummaryTimer();
        mOrderListSummary = new ConcurrentHashMap<>();
    }
    
    public void addData(QRIDataOrderListMessage o){
        try{
            //. xyz
            //System.err.println("addData = " + o.getfOrderID() );
            if (o.getfOrderID() > 0){
                mOrderListSummary.put(o.getfOrderID(), o);
            }
        }catch(Exception ex){
            
        }
    }
    
    public void releasePendingParsialIfAvailable(long lJatsOrderId){
        QRIDataOrderListMessage mPending = getData(lJatsOrderId);
        if (mPending != null){
            removeData(lJatsOrderId);
            //. save orderlist ke memory martin
//            BookOfMARTINOrderList.getInstance.addOrUpdateSheet(mPending);
//            //. broadcast orderlist via martin
//            BookOfMARTINOrderList.getInstance.brodcastToSubscriber(mPending);
        }
        
    }
    
    public QRIDataOrderListMessage getData(Long l){
        return mOrderListSummary.get(l);
    }
    
    public void removeData(Long l){
        try{
            mOrderListSummary.remove(l);
        }catch(Exception ex){
            
        }
    }
    
    public ConcurrentHashMap<Long, QRIDataOrderListMessage> getAllData(){
        return mOrderListSummary;
    }
    
    public void clearAllData(){
        mOrderListSummary.clear();
    }
    
    public void startWorker(int iMsRate){
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, iMsRate);
    }
    
    public void stopWorker(){
        if (timer != null){
            timer.cancel();
        }
        
    }
    
    public class QRIOrderListPartialSummaryTimer extends TimerTask{

        @Override
        public void run() {
            try{
                //if (getAllData().size() > 0) System.out.println("QRIOrderListPartialSummaryTimer.StartRun = " + getAllData().size());
                ConcurrentLinkedQueue<QRIDataOrderListMessage> mQueue = new ConcurrentLinkedQueue<>();
                Iterator iterator = getAllData().keySet().iterator();
                while (iterator.hasNext()) {
                    Long key =  (long)iterator.next();
                    QRIDataOrderListMessage value = getAllData().get(key);                    
                    mQueue.add(value);
                }
                
                clearAllData();
                
                while (!mQueue.isEmpty()){
                    QRIDataOrderListMessage value =  mQueue.poll();
                    //. save orderlist ke memory martin
//                    BookOfMARTINOrderList.getInstance.addOrUpdateSheet(value);
//                    //. broadcast orderlist via martin
//                    BookOfMARTINOrderList.getInstance.brodcastToSubscriber(value);
                }
                
               //System.out.println("QRIOrderListPartialSummaryTimer.EndRun = " + getAllData().size());

            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
        }
        
    }
    
}

