/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.client.works;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.idx.data.qri.message.struct.QRIDataSubscription;
import com.itm.xtream.inet.trading.martin.server.client.subscription.MartinClientSubscriptionManagement;

/**
 *
 * @author fredy
 */
public class MARTINSimWorkDataSubscription {
    //.single instance:
    public final static MARTINSimWorkDataSubscription getInstance = new MARTINSimWorkDataSubscription();
    
    public MARTINSimWorkDataSubscription() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, QRIDataSubscription mInputMsgRequest){
        try{
            MartinClientSubscriptionManagement.MartinSubscriptionResult mSubsResult =  MartinClientSubscriptionManagement.getInstance.addSubscription(mInputMsgRequest);
            if (mSubsResult == MartinClientSubscriptionManagement.MartinSubscriptionResult.Subscribe){
                //. broadcast data
                MartinClientSubscriptionManagement.getInstance.broadcastSubscriptionData(mInputMsgRequest);
                
            }else{
                
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
}
