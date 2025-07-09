/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.server.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.setup.ITMSocketListener;
import com.itm.xtream.inet.trading.feed.server.bridge.FeedInputBridge;
import com.itm.xtream.inet.trading.feed.util.FEEDMsgHelper;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import com.itm.xtream.inet.trading.sync.connection.ITMTradingServerSyncConnectionMgr;

/**
 *
 * @author fredy
 */
public class FeedServerCallbackHandler implements ITMSocketListener {
    
    private final FeedServerCallbackController headCtrl;
    
    private final FeedInputBridge inputBridge                   = FeedInputBridge.getInstance;
    
    private final String DataFeedClientLoginUserName            = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.datafeed_client.username;
    private final String DataFeedClientLoginUserPassword        = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.datafeed_client.password;
    
    public FeedServerCallbackHandler(FeedServerCallbackController headController) {
        this.headCtrl = headController;
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
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
    
    @Override
    public void onConnected(ITMSocketChannel channel) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (ITMTradingServerSyncConnectionMgr.getInstance.isSafeForClientToConnect()){
            if (headCtrl.isChannelsProcessorsOnLimit()){

                channel.StopChannel();

                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "client channel connected but connection limit exceeded:" + channel + " > id:" + channel.getChannelID());

            }else{

                headCtrl.createChannelProcessor(channel);

                netWriteMsgln(inputBridge.getLastDataFeedServerWelcomeMessage(), channel);

                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "client channel connected:" + channel + " > id:" + channel.getChannelID());

            }
        }else{
            
            channel.StopChannel();

            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "client channel connected but denied by server sync connection check :" + channel + " > id:" + channel.getChannelID());

        }
    }

    @Override
    public void onDisconnected(ITMSocketChannel channel) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        headCtrl.destroyChannelProcessor(channel);
        
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.WARNING, "client channel disconnected:" + channel + " > id:" + channel.getChannelID());
        
    }

    @Override
    public void onMessage(ITMSocketChannel channel, String messageLine) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if ((!channel.isClosed()) && (channel.isConnected())){
            boolean bNeedToDisconnect = false;
            if ((messageLine != null) && (messageLine.length() > 0)){
                System.out.println("@client_message=" + messageLine);
                String[] arrRecordSplit = messageLine.split("[|]", -1);
                if (arrRecordSplit.length == 3){
                    if (
                        (DataFeedClientLoginUserName.equals(arrRecordSplit[0]))
                        && (DataFeedClientLoginUserPassword.equals(arrRecordSplit[1]))
                    ){
                        if (arrRecordSplit[2].equalsIgnoreCase("A")){
                            //.set sequence as beginning (1):
                            FeedServerCallbackProcessor mProcessor = headCtrl.getChannelProcessor(channel);
                            if (mProcessor != null){
                                netWriteMsgln("OK", channel);
                                mProcessor.adjustSequenceNo(1);
                            }else{
                                netWriteMsgln("FAILED", channel);
                                bNeedToDisconnect = true;
                            }
                        }else if (arrRecordSplit[2].equalsIgnoreCase("C")){
                            //.set sequence as current (>=1):
                            FeedServerCallbackProcessor mProcessor = headCtrl.getChannelProcessor(channel);
                            if (mProcessor != null){
                                mProcessor.adjustSequenceNo(inputBridge.getCurrentSequenceNo());
                                netWriteMsgln("OK", channel);
                            }else{
                                bNeedToDisconnect = true;
                                netWriteMsgln("FAILED", channel);                                
                            }
                        }else{
                            int vRequestedSequenceNo = FEEDMsgHelper.getInstance.str2Int(arrRecordSplit[2]);
                            if (vRequestedSequenceNo > 0){
                                //.set sequence as of:
                                FeedServerCallbackProcessor mProcessor = headCtrl.getChannelProcessor(channel);
                                if (mProcessor != null){
                                    mProcessor.adjustSequenceNo(vRequestedSequenceNo);
                                    netWriteMsgln("OK", channel);                                    
                                }else{
                                    bNeedToDisconnect = true;
                                    netWriteMsgln("FAILED", channel);                                    
                                }
                            }else{
                                bNeedToDisconnect = true;
                                netWriteMsgln("FAILED", channel);
                            }
                        }
                    }else{
                        bNeedToDisconnect = true;
                        netWriteMsgln("FAILED", channel);
                    }
                }else{
                    bNeedToDisconnect = true;
                    netWriteMsgln("FAILED", channel);                    
                }
            }else{
                bNeedToDisconnect = true;
                netWriteMsgln("FAILED", channel);                
            }
            if (bNeedToDisconnect){
                channel.StopChannel();
            }
        }
    }

    @Override
    public void onSent(ITMSocketChannel channel, String messageLine) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        
        
    }

    @Override
    public void onError(ITMSocketChannel channel, Exception exception) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        //.logx:
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "client exception caught:" + channel + " > id:" + channel.getChannelID() + " > cause:" + exception.getCause().getMessage());
        
    }
    
}
