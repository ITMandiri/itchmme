/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.martin.server.bridge;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.process.QRIMessageProcessor;
import com.itm.idx.data.qri.message.struct.QRIDataHeader;
import com.itm.idx.data.qri.message.struct.QRIDataITMMessage;
import com.itm.idx.data.qri.message.struct.QRIDataSubscription;
import com.itm.idx.data.qri.message.struct.QRIDataUnknownMessage;
import com.itm.idx.data.qri.message.struct.logon.QRIDataLogon;
import com.itm.idx.data.qri.message.struct.logon.QRIDataLogonReply;
import com.itm.idx.data.qri.message.struct.logout.QRIDataLogout;
import com.itm.idx.data.qri.message.struct.logout.QRIDataLogoutReply;
import com.itm.idx.data.qri.message.struct.password.QRIDataChangePassword;
import com.itm.idx.data.qri.message.struct.password.QRIDataChangePasswordReply;
import com.itm.idx.message.IDXMessage;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackController;
import com.itm.xtream.inet.trading.martin.server.callback.MARTINSimCallbackProcessor;
import com.itm.xtream.inet.trading.martin.server.client.works.MARTINSimWorkDataSubscription;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import com.itm.xtream.inet.trading.sync.connection.ITMTradingServerSyncConnectionMgr;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author fredy
 */
public class MARTINSimInputBridge {
    //.single instance ya:
    public final static MARTINSimInputBridge getInstance = new MARTINSimInputBridge();
    
    private MARTINSimCallbackController callBackController;
    
    private final QRIMessageProcessor qriProcessor                              = new QRIMessageProcessor();
    
    private final AtomicInteger aiCurrentDate                                   = new AtomicInteger(0);
    
    //.constructor:
    public MARTINSimInputBridge(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public MARTINSimCallbackController getCallBackController() {
        return callBackController;
    }

    public void setCallBackController(MARTINSimCallbackController callBackController) {
        this.callBackController = callBackController;
    }

    public int getCurrentDate() {
        return this.aiCurrentDate.get();
    }
    
    public int setCurrentDate(int vNewValue){
        return this.aiCurrentDate.getAndSet(vNewValue);
    }
    
    
    
    private void netWriteMsgln(String szMsgToSend, ITMSocketChannel chMsg){
        if (chMsg != null){
            try{
                if (szMsgToSend != null){
                    if (!chMsg.sendMessageDirect(szMsgToSend)){
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "client cannot write msg:" + szMsgToSend + " > id:" + chMsg.getChannelID());
                    }
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "client exception cannot write msg:" + szMsgToSend + " > id:" + chMsg.getChannelID());
            }
        }
    }
    
    public synchronized boolean processMessage(ITMSocketChannel channel, String messageLine){
        boolean mOut = false;
        if ((!channel.isClosed()) && (channel.isConnected())){
            boolean bNeedToDisconnect = false;
            MARTINSimCallbackProcessor chProcessor = callBackController.getChannelProcessor(channel);
            if (chProcessor == null){
                ///netWriteMsgln("FAILED", channel);
                bNeedToDisconnect = true;
            }else if ((messageLine != null) && (messageLine.length() > 0)){
                System.out.println("@client_message=" + messageLine);
                IDXMessage mMsg = qriProcessor.processMessage(messageLine, true);
                if (mMsg == null || mMsg instanceof QRIDataUnknownMessage){
                    ///netWriteMsgln("FAILED", channel);
                    bNeedToDisconnect = true;
                }else if (chProcessor.getAlreadyLoggedIn()){
                    QRIDataHeader mRequestHdr = (QRIDataHeader)mMsg;
                    if (mMsg instanceof QRIDataLogon){
                        QRIDataLogon mMsgRequest = (QRIDataLogon)mMsg; 
                        QRIDataLogonReply mMsgRespond = new QRIDataLogonReply(new HashMap());
                        mMsgRespond.setfConnName(mRequestHdr.getfConnName());
                        mMsgRespond.setfLogonReplyType(QRIDataLogonReply.LogonReplyType.LogonReplyBAD);
                        mMsgRespond.setfText("MARTIN Sim IO Invalid operation already logged-in !");
                        netWriteMsgln(mMsgRespond.msgToString(), channel);
                        bNeedToDisconnect = true;
                        mOut = true;
                    }else if (mMsg instanceof QRIDataChangePassword){
                        QRIDataChangePassword mMsgRequest = (QRIDataChangePassword)mMsg; 
                        QRIDataChangePasswordReply mMsgRespond = new QRIDataChangePasswordReply(new HashMap());
                        mMsgRespond.setfConnName(mRequestHdr.getfConnName());
                        mMsgRespond.setfChangePassReplyType(QRIDataChangePasswordReply.ChangePassReplyType.ChangePassBAD);
                        mMsgRespond.setfUserID(" ");
                        mMsgRespond.setfCurrentPassword(" ");
                        mMsgRespond.setfNewPassword(" ");
                        mMsgRespond.setfReturnValue("MARTIN Sim IO Invalid operation for this version !");
                        netWriteMsgln(mMsgRespond.msgToString(), channel);
                        bNeedToDisconnect = true;
                        mOut = true;
                    }else if (mMsg instanceof QRIDataLogout){
                        QRIDataLogout mMsgRequest = (QRIDataLogout)mMsg; 
                        QRIDataLogoutReply mMsgRespond = new QRIDataLogoutReply(new HashMap());
                        mMsgRespond.setfConnName(mRequestHdr.getfConnName());
                        mMsgRespond.setfText("confirming logout");
                        netWriteMsgln(mMsgRespond.msgToString(), channel);
                        bNeedToDisconnect = true;
                        mOut = true;
                    }else if (mMsg instanceof QRIDataSubscription){
                        QRIDataSubscription mMsgRequest = (QRIDataSubscription)mMsg;
                        MARTINSimWorkDataSubscription.getInstance.doWork(channel, mMsgRequest);
                    }else{
                        //... .
                    }
                }else{
                    QRIDataHeader mRequestHdr = (QRIDataHeader)mMsg;
                    if (mMsg instanceof QRIDataLogon){
                        QRIDataLogon mMsgRequest = (QRIDataLogon)mMsg; 
                        boolean bFoundInSettings = false;
                        boolean bFoundInProcessors = false;
                        boolean bCorrectSetting = false;
                        //.find in settings:
                        if (ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.martin_client_connections.length > 0){
                            for (ITMTradingServerSettingsMgr.SIM_Connection mConnection : ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.martin_client_connections){
                                if ((mConnection.name.equals(mMsgRequest.getfConnName())) && (mConnection.usercode.equals(mMsgRequest.getfUserID()))){
                                    bFoundInSettings = true;
                                    if (mConnection.password.equals(mMsgRequest.getfCurrentPassword())){
                                        bCorrectSetting = true;
                                    }
                                }
                            }
                        }
                        //.find in processors:
                        if (!bFoundInProcessors){
                            if (callBackController.getActiveChannelProcessorByConnName(mMsgRequest.getfConnName()) != null){
                                bFoundInProcessors = true;
                            }
                        }
                        if (!bFoundInProcessors){
                            if (callBackController.getActiveChannelProcessorByUserCode(mMsgRequest.getfUserID()) != null){
                                bFoundInProcessors = true;
                            }
                        }
                        //.conclusion:
                        QRIDataLogonReply mMsgRespond = new QRIDataLogonReply(new HashMap());
                        mMsgRespond.setfConnName(mRequestHdr.getfConnName());
                        if (bFoundInSettings && bCorrectSetting && !bFoundInProcessors){
                            mMsgRespond.setfLogonReplyType(QRIDataLogonReply.LogonReplyType.LogonReplyOK1);
                            mMsgRespond.setfEncryptedMethod(QRIDataConst.ADMIN_ENCRYPT_METHOD);
                            mMsgRespond.setfHeartBtInt(QRIDataConst.ADMIN_HEARTBEAT_TIME);
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            mMsgRespond.setfLogonReplyType(QRIDataLogonReply.LogonReplyType.LogonReplyOK2);
                            mMsgRespond.setfEncryptedMethod(QRIDataConst.ADMIN_ENCRYPT_METHOD);
                            mMsgRespond.setfHeartBtInt(QRIDataConst.ADMIN_HEARTBEAT_TIME);
                            mMsgRespond.setfUserID(mMsgRequest.getfUserID());
                            mMsgRespond.setfLogonReply(StringHelper.fromInt(QRIDataConst.ADMIN_LOGON_REPLY_PWD_OK));
                            mMsgRespond.setfLogonDesc("MARTIN Sim IO Logged-in");
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            chProcessor.setAlreadyLoggedIn(true);
                            chProcessor.setConnName(mRequestHdr.getfConnName());
                            chProcessor.setUserCode(mMsgRequest.getfUserID());
                            mOut = true;
                            
                            //. hrn-20210813 : mengirimkan message QRIDataITMMessage terkait status ouch/fix 5 sedang offline atau online 
                            QRIDataITMMessage mRespondITMMessageRG = new QRIDataITMMessage(new HashMap());
                            QRIDataITMMessage mRespondITMMessageNG = new QRIDataITMMessage(new HashMap());
                            
                            mRespondITMMessageNG.setfConnName(mRequestHdr.getfConnName());
                            mRespondITMMessageRG.setfConnName(mRequestHdr.getfConnName());
                            
                            mRespondITMMessageRG.setfITMMsgType("servstat");
                            mRespondITMMessageNG.setfITMMsgType("servstat");
                            
                            
                            //. dapatkan status koneksi fix5 dan ouch
                            boolean bFix5Connected = ITMTradingServerSyncConnectionMgr.getInstance.isFix5Connected();
                            boolean bOUCHConnected = ITMTradingServerSyncConnectionMgr.getInstance.isOUCHConnected();
                            mRespondITMMessageRG.setfMsgSubType1("rg");
                            if (bOUCHConnected){
                                mRespondITMMessageRG.setfMsgSubValue1(1);
                            }
                            mRespondITMMessageNG.setfMsgSubType1("ng");
                            if (bOUCHConnected){
                                mRespondITMMessageNG.setfMsgSubValue1(1);
                            }
                            
                            //. kirim status rg
                            netWriteMsgln(mRespondITMMessageRG.msgToString(), channel);
                            //. kirim status ng
                            netWriteMsgln(mRespondITMMessageNG.msgToString(), channel);
                            
                        }else if (bFoundInProcessors){
                            mMsgRespond.setfLogonReplyType(QRIDataLogonReply.LogonReplyType.LogonReplyBAD);
                            mMsgRespond.setfText("MARTIN Sim IO Invalid operation connection already exist !");
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            bNeedToDisconnect = true;
                            mOut = true;
                        }else if (bFoundInSettings && !bCorrectSetting){
                            mMsgRespond.setfLogonReplyType(QRIDataLogonReply.LogonReplyType.LogonReplyBAD);
                            mMsgRespond.setfText("MARTIN Sim IO Invalid operation invalid user settings !");
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            bNeedToDisconnect = true;
                            mOut = true;
                        }else{
                            mMsgRespond.setfLogonReplyType(QRIDataLogonReply.LogonReplyType.LogonReplyBAD);
                            mMsgRespond.setfText("MARTIN Sim IO Invalid operation invalid user !");
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            bNeedToDisconnect = true;
                            mOut = true;
                        }
                    }else if (mMsg instanceof QRIDataChangePassword){
                        QRIDataChangePassword mMsgRequest = (QRIDataChangePassword)mMsg; 
                        bNeedToDisconnect = true;
                    }else if (mMsg instanceof QRIDataLogout){
                        QRIDataLogout mMsgRequest = (QRIDataLogout)mMsg; 
                        bNeedToDisconnect = true;
                    }else{
                        bNeedToDisconnect = true;
                    }
                }
            }else{
                ///netWriteMsgln("FAILED", channel);
                bNeedToDisconnect = true;
            }
            if (bNeedToDisconnect){
                channel.StopChannel();
            }
        }
        return mOut;
    }
    
}
