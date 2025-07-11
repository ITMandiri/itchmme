/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.bridge;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.processor.ORIMessageProcessor;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeChangePassword;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeChangePasswordReply;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogon;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogonReply;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogout;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogoutReply;
import com.itm.idx.data.ori.message.struct.ORIDataHeader;
import com.itm.idx.data.ori.message.struct.ORIDataLiquidityProviderOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealAmend;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancel;
import com.itm.idx.data.ori.message.struct.ORIDataNewLiquidityProviderOrder;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderMassCancel;
import com.itm.idx.data.ori.message.struct.ORIDataUnknownMessage;
import com.itm.idx.message.IDXMessage;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackController;
import com.itm.xtream.inet.trading.jonec.server.callback.JONECSimCallbackProcessor;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataLiquidityProviderOrderCancel;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataNegotiationDeal;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataNegotiationDealAmend;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataNegotiationDealCancel;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataNewLiquidityProviderOrder;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataOrderAmend;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataOrderCancel;
import com.itm.xtream.inet.trading.jonec.server.client.works.JONECSimWorkDataOrderMassCancel;
import com.itm.xtream.inet.trading.racing.mgr.ITMTradingServerRacingMgr;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author fredy
 */
public class JONECSimInputBridge {
    //.single instance ya:
    public final static JONECSimInputBridge getInstance = new JONECSimInputBridge();
    
    private JONECSimCallbackController callBackController;
    
    private final ORIMessageProcessor oriProcessor                              = new ORIMessageProcessor();
    
    private final AtomicInteger aiCurrentDate                                   = new AtomicInteger(0);
    
    //.constructor:
    public JONECSimInputBridge(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public JONECSimCallbackController getCallBackController() {
        return callBackController;
    }

    public void setCallBackController(JONECSimCallbackController callBackController) {
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
            JONECSimCallbackProcessor chProcessor = callBackController.getChannelProcessor(channel);
            if (chProcessor == null){
                ///netWriteMsgln("FAILED", channel);
                bNeedToDisconnect = true;
            }else if ((messageLine != null) && (messageLine.length() > 0)){
                System.out.println("@client_message=" + messageLine);
                IDXMessage mMsg = oriProcessor.parseMessage(messageLine, true);
                if (mMsg == null || mMsg instanceof ORIDataUnknownMessage){
                    ///netWriteMsgln("FAILED", channel);
                    bNeedToDisconnect = true;
                }else if (chProcessor.getAlreadyLoggedIn()){
                    ORIDataHeader mRequestHdr = (ORIDataHeader)mMsg;
                    if (mMsg instanceof ORIDataAdministrativeLogon){
                        ORIDataAdministrativeLogon mMsgRequest = (ORIDataAdministrativeLogon)mMsg; 
                        ORIDataAdministrativeLogonReply mMsgRespond = new ORIDataAdministrativeLogonReply(new HashMap());
                        mMsgRespond.setfBundleMessageVersion(mRequestHdr.getfBundleMessageVersion());
                        mMsgRespond.setfBundleConnectionName(mRequestHdr.getfBundleConnectionName());
                        mMsgRespond.setfBundlePosDup(mRequestHdr.getfBundlePosDup());
                        mMsgRespond.setfAdministrativeLogonReplyType(ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType.BAD);
                        mMsgRespond.setfText("JONEC Sim IO Invalid operation already logged-in !");
                        netWriteMsgln(mMsgRespond.msgToString(), channel);
                        bNeedToDisconnect = true;
                        mOut = true;
                    }else if (mMsg instanceof ORIDataAdministrativeChangePassword){
                        ORIDataAdministrativeChangePassword mMsgRequest = (ORIDataAdministrativeChangePassword)mMsg; 
                        ORIDataAdministrativeChangePasswordReply mMsgRespond = new ORIDataAdministrativeChangePasswordReply(new HashMap());
                        mMsgRespond.setfBundleMessageVersion(mRequestHdr.getfBundleMessageVersion());
                        mMsgRespond.setfBundleConnectionName(mRequestHdr.getfBundleConnectionName());
                        mMsgRespond.setfBundlePosDup(mRequestHdr.getfBundlePosDup());
                        mMsgRespond.setfAdministrativeChangePasswordReplyType(ORIDataAdministrativeChangePasswordReply.ORIAdministrativeChangePasswordReplyType.BAD);
                        mMsgRespond.setfUserID(" ");
                        mMsgRespond.setfCurrentPassword(" ");
                        mMsgRespond.setfNewPassword(" ");
                        mMsgRespond.setfReturnValue("JONEC Sim IO Invalid operation for this version !");
                        netWriteMsgln(mMsgRespond.msgToString(), channel);
                        bNeedToDisconnect = true;
                        mOut = true;
                    }else if (mMsg instanceof ORIDataAdministrativeLogout){
                        ORIDataAdministrativeLogout mMsgRequest = (ORIDataAdministrativeLogout)mMsg; 
                        ORIDataAdministrativeLogoutReply mMsgRespond = new ORIDataAdministrativeLogoutReply(new HashMap());
                        mMsgRespond.setfBundleMessageVersion(mRequestHdr.getfBundleMessageVersion());
                        mMsgRespond.setfBundleConnectionName(mRequestHdr.getfBundleConnectionName());
                        mMsgRespond.setfBundlePosDup(mRequestHdr.getfBundlePosDup());
                        mMsgRespond.setfText("confirming logout");
                        netWriteMsgln(mMsgRespond.msgToString(), channel);
                        bNeedToDisconnect = true;
                        mOut = true;
                    }else if (mMsg instanceof ORIDataLiquidityProviderOrderCancel){
                        ORIDataLiquidityProviderOrderCancel mMsgRequest = (ORIDataLiquidityProviderOrderCancel)mMsg; 
                        JONECSimWorkDataLiquidityProviderOrderCancel.getInstance.doWork(channel, mMsgRequest);
                    }else if (mMsg instanceof ORIDataNegotiationDeal){
                        ORIDataNegotiationDeal mMsgRequest = (ORIDataNegotiationDeal)mMsg; 
                        JONECSimWorkDataNegotiationDeal.getInstance.doWork(channel, mMsgRequest);
                    }else if (mMsg instanceof ORIDataNegotiationDealAmend){
                        ORIDataNegotiationDealAmend mMsgRequest = (ORIDataNegotiationDealAmend)mMsg; 
                        JONECSimWorkDataNegotiationDealAmend.getInstance.doWork(channel, mMsgRequest);
                    }else if (mMsg instanceof ORIDataNegotiationDealCancel){
                        ORIDataNegotiationDealCancel mMsgRequest = (ORIDataNegotiationDealCancel)mMsg; 
                        JONECSimWorkDataNegotiationDealCancel.getInstance.doWork(channel, mMsgRequest);
                    }else if (mMsg instanceof ORIDataNewLiquidityProviderOrder){
                        ORIDataNewLiquidityProviderOrder mMsgRequest = (ORIDataNewLiquidityProviderOrder)mMsg; 
                        JONECSimWorkDataNewLiquidityProviderOrder.getInstance.doWork(channel, mMsgRequest);
                    }else if (mMsg instanceof ORIDataNewOrder){
                        ORIDataNewOrder mMsgRequest = (ORIDataNewOrder)mMsg; 
                        //. 2022-02-16 : semua terkait new order lewat pengecekan OrderRacing
                        ITMTradingServerRacingMgr.getInstance.doWork(channel, mMsgRequest);                        
                    }else if (mMsg instanceof ORIDataOrderAmend){
                        ORIDataOrderAmend mMsgRequest = (ORIDataOrderAmend)mMsg; 
                        JONECSimWorkDataOrderAmend.getInstance.doWork(channel, mMsgRequest);
                    }else if (mMsg instanceof ORIDataOrderCancel){
                        ORIDataOrderCancel mMsgRequest = (ORIDataOrderCancel)mMsg; 
                        JONECSimWorkDataOrderCancel.getInstance.doWork(channel, mMsgRequest);
                    }else if (mMsg instanceof ORIDataOrderMassCancel){
                        ORIDataOrderMassCancel mMsgRequest = (ORIDataOrderMassCancel)mMsg; 
                        JONECSimWorkDataOrderMassCancel.getInstance.doWork(channel, mMsgRequest);
                    }else{
                        //... .
                    }
                }else{
                    ORIDataHeader mRequestHdr = (ORIDataHeader)mMsg;
                    if (mMsg instanceof ORIDataAdministrativeLogon){
                        ORIDataAdministrativeLogon mMsgRequest = (ORIDataAdministrativeLogon)mMsg; 
                        boolean bFoundInSettings = false;
                        boolean bFoundInProcessors = false;
                        boolean bCorrectSetting = false;
                        //.find in settings:
                        if (ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.jonec_client_connections.length > 0){
                            for (ITMTradingServerSettingsMgr.SIM_Connection mConnection : ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.jonec_client_connections){
                                if ((mConnection.name.equals(mMsgRequest.getfBundleConnectionName())) && (mConnection.usercode.equals(mMsgRequest.getfUserID()))){
                                    bFoundInSettings = true;
                                    if (mConnection.password.equals(mMsgRequest.getfCurrentPassword())){
                                        bCorrectSetting = true;
                                    }
                                }
                            }
                        }
                        
                        //.find in processors:
                        if (!bFoundInProcessors){
                            if (callBackController.getActiveChannelProcessorByConnName(mMsgRequest.getfBundleConnectionName()) != null){
                                bFoundInProcessors = true;
                            }
                        }
                        if (!bFoundInProcessors){
                            if (callBackController.getActiveChannelProcessorByUserCode(mMsgRequest.getfUserID()) != null){
                                bFoundInProcessors = true;
                            }
                        }
                        //. hrn: 20220321 : untuk antisipasi koneksi yang menggantung
                        if (bFoundInProcessors){
                            JONECSimCallbackProcessor mPrevProc = callBackController.getActiveChannelProcessorByConnName(mMsgRequest.getfBundleConnectionName());
                            if (mPrevProc == null){
                                mPrevProc = callBackController.getActiveChannelProcessorByUserCode(mMsgRequest.getfUserID());
                            }
                            String zPrevChannelID = "";
                            //. pastikan object yang berbeda
                            if (mPrevProc != null && mPrevProc.getChChannel() != null && !mPrevProc.getChChannel().getChannelID().equals(channel.getChannelID())){
                                mPrevProc.getChChannel().StopChannel();
                                //. preset 
                                bFoundInProcessors = false;
                                zPrevChannelID = mPrevProc.getChChannel().getChannelID();
                            }
                            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "FoundInProcessors, UserID " + mMsgRequest.getfUserID() + ", BundleConnectionName = " + mMsgRequest.getfBundleConnectionName() + " > id:" + channel.getChannelID() + ", previd = " + zPrevChannelID);
                        }
                        //.conclusion:
                        ORIDataAdministrativeLogonReply mMsgRespond = new ORIDataAdministrativeLogonReply(new HashMap());
                        mMsgRespond.setfBundleMessageVersion(mRequestHdr.getfBundleMessageVersion());
                        mMsgRespond.setfBundleConnectionName(mRequestHdr.getfBundleConnectionName());
                        mMsgRespond.setfBundlePosDup(mRequestHdr.getfBundlePosDup());
                        if (bFoundInSettings && bCorrectSetting && !bFoundInProcessors){
                            mMsgRespond.setfAdministrativeLogonReplyType(ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType.OK1);
                            mMsgRespond.setfEncryptMethod(ORIDataConst.ORIFieldValue.ADMIN_ENCRYPT_METHOD);
                            mMsgRespond.setfHeartBtInt(ORIDataConst.ORIFieldValue.ADMIN_HEARTBEAT_TIME);
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            mMsgRespond.setfAdministrativeLogonReplyType(ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType.OK2);
                            mMsgRespond.setfEncryptMethod(ORIDataConst.ORIFieldValue.ADMIN_ENCRYPT_METHOD);
                            mMsgRespond.setfHeartBtInt(ORIDataConst.ORIFieldValue.ADMIN_HEARTBEAT_TIME);
                            mMsgRespond.setfUserID(mMsgRequest.getfUserID());
                            mMsgRespond.setfLogonReply(StringHelper.fromInt(ORIDataConst.ORIFieldValue.ADMIN_LOGON_REPLY_PWD_OK));
                            mMsgRespond.setfLogonDesc("JONEC Sim IO Logged-in");
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            chProcessor.setAlreadyLoggedIn(true);
                            chProcessor.setConnName(mRequestHdr.getfBundleConnectionName());
                            chProcessor.setUserCode(mMsgRequest.getfUserID());
                            mOut = true;
                        }else if (bFoundInProcessors){
                            mMsgRespond.setfAdministrativeLogonReplyType(ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType.BAD);
                            mMsgRespond.setfText("JONEC Sim IO Invalid operation connection already exist !");
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            bNeedToDisconnect = true;
                            mOut = true;
                        }else if (bFoundInSettings && !bCorrectSetting){
                            mMsgRespond.setfAdministrativeLogonReplyType(ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType.BAD);
                            mMsgRespond.setfText("JONEC Sim IO Invalid operation invalid user settings !");
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            bNeedToDisconnect = true;
                            mOut = true;
                        }else{
                            mMsgRespond.setfAdministrativeLogonReplyType(ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType.BAD);
                            mMsgRespond.setfText("JONEC Sim IO Invalid operation invalid user !");
                            netWriteMsgln(mMsgRespond.msgToString(), channel);
                            bNeedToDisconnect = true;
                            mOut = true;
                        }
                    }else if (mMsg instanceof ORIDataAdministrativeChangePassword){
                        ORIDataAdministrativeChangePassword mMsgRequest = (ORIDataAdministrativeChangePassword)mMsg; 
                        bNeedToDisconnect = true;
                    }else if (mMsg instanceof ORIDataAdministrativeLogout){
                        ORIDataAdministrativeLogout mMsgRequest = (ORIDataAdministrativeLogout)mMsg; 
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
