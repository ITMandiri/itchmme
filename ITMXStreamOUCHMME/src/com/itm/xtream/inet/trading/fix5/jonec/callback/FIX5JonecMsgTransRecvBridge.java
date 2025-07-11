/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.callback;

import com.itm.fix5.data.jonec.message.struct.FIX5IDXMessage;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataBusinessMessageReject;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataExecutionReport;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataHeader;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataHeartbeat;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogonReply;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogoutReply;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataMassQuote;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataMassQuoteAcknowledgement;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataNewOrderSingle;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderCancelReject;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderCancelReplaceRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderCancelRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderMassActionReport;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderMassActionRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataPositionReport;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataReject;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataRequestforPositions;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataResendRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataSequenceReset;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTestRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTradeCaptureReport;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTradeCaptureReportAck;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataUnknownMessage;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataBusinessMessageReject;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataExecutionReport;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataHeartbeat;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataLogonReply;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataLogoutReply;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataMassQuote;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataMassQuoteAcknowledgement;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataNewOrderSingle;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataOrderCancelReject;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataOrderCancelReplaceRequest;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataOrderCancelRequest;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataOrderMassActionReport;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataOrderMassActionRequest;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataPositionReport;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataReject;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataRequestforPositions;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataResendRequest;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataSequenceReset;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataTestRequest;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataTradeCaptureReport;
import com.itm.xtream.inet.trading.fix5.jonec.received.works.FIX5JonecWorkDataTradeCaptureReportAck;

/**
 *
 * @author fredy
 */
public class FIX5JonecMsgTransRecvBridge {
    //.single instance ya:
    public final static FIX5JonecMsgTransRecvBridge getInstance = new FIX5JonecMsgTransRecvBridge();
    
    //.constructor:
    public FIX5JonecMsgTransRecvBridge(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public synchronized boolean processMessage(ITMSocketChannel channel, FIX5IDXBridgeController controller, String messageLine, FIX5IDXMessage messageObject){
        boolean mOut = false;
        if ((!channel.isClosed()) && (channel.isConnected())){
            boolean bNeedToDisconnect = false;
            if (messageObject != null){
                FIX5JonecDataHeader mHdr = (FIX5JonecDataHeader)messageObject;
                if (mHdr.getfMsgSeqNum() > controller.getCurrentRXSequencedNo()){
                    controller.setCurrentRXSequencedNo(mHdr.getfMsgSeqNum());
                }
            }
            if (messageObject == null || messageObject instanceof FIX5JonecDataUnknownMessage){
                bNeedToDisconnect = true;
            }else if (messageObject instanceof FIX5JonecDataLogonReply){
                FIX5JonecWorkDataLogonReply.getInstance.doWork(channel, controller, (FIX5JonecDataLogonReply) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataLogoutReply){
                FIX5JonecWorkDataLogoutReply.getInstance.doWork(channel, controller, (FIX5JonecDataLogoutReply) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataReject){
                FIX5JonecWorkDataReject.getInstance.doWork(channel, controller, (FIX5JonecDataReject) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataHeartbeat){
                FIX5JonecWorkDataHeartbeat.getInstance.doWork(channel, controller, (FIX5JonecDataHeartbeat) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataTestRequest){
                FIX5JonecWorkDataTestRequest.getInstance.doWork(channel, controller, (FIX5JonecDataTestRequest) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataSequenceReset){
                FIX5JonecWorkDataSequenceReset.getInstance.doWork(channel, controller, (FIX5JonecDataSequenceReset) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataTradeCaptureReport){
                FIX5JonecWorkDataTradeCaptureReport.getInstance.doWork(channel, controller, (FIX5JonecDataTradeCaptureReport) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataTradeCaptureReportAck){
                FIX5JonecWorkDataTradeCaptureReportAck.getInstance.doWork(channel, controller, (FIX5JonecDataTradeCaptureReportAck) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataResendRequest){
                FIX5JonecWorkDataResendRequest.getInstance.doWork(channel, controller, (FIX5JonecDataResendRequest) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataRequestforPositions){
                FIX5JonecWorkDataRequestforPositions.getInstance.doWork(channel, controller, (FIX5JonecDataRequestforPositions) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataBusinessMessageReject){
                FIX5JonecWorkDataBusinessMessageReject.getInstance.doWork(channel, controller, (FIX5JonecDataBusinessMessageReject) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataExecutionReport){
                FIX5JonecWorkDataExecutionReport.getInstance.doWork(channel, controller, (FIX5JonecDataExecutionReport) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataMassQuote){
                FIX5JonecWorkDataMassQuote.getInstance.doWork(channel, controller, (FIX5JonecDataMassQuote) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataMassQuoteAcknowledgement){
                FIX5JonecWorkDataMassQuoteAcknowledgement.getInstance.doWork(channel, controller, (FIX5JonecDataMassQuoteAcknowledgement) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataNewOrderSingle){
                FIX5JonecWorkDataNewOrderSingle.getInstance.doWork(channel, controller, (FIX5JonecDataNewOrderSingle) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataOrderCancelReject){
                FIX5JonecWorkDataOrderCancelReject.getInstance.doWork(channel, controller, (FIX5JonecDataOrderCancelReject) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataOrderCancelReplaceRequest){
                FIX5JonecWorkDataOrderCancelReplaceRequest.getInstance.doWork(channel, controller, (FIX5JonecDataOrderCancelReplaceRequest) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataOrderCancelRequest){
                FIX5JonecWorkDataOrderCancelRequest.getInstance.doWork(channel, controller, (FIX5JonecDataOrderCancelRequest) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataOrderMassActionReport){
                FIX5JonecWorkDataOrderMassActionReport.getInstance.doWork(channel, controller, (FIX5JonecDataOrderMassActionReport) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataOrderMassActionRequest){
                FIX5JonecWorkDataOrderMassActionRequest.getInstance.doWork(channel, controller, (FIX5JonecDataOrderMassActionRequest) messageObject);
                mOut = true;
            }else if (messageObject instanceof FIX5JonecDataPositionReport){
                FIX5JonecWorkDataPositionReport.getInstance.doWork(channel, controller, (FIX5JonecDataPositionReport) messageObject);
                mOut = true;
            }
            
            if (bNeedToDisconnect){
                channel.StopChannel();
            }
        }
        return mOut;
    }
    
}
