/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.callback;

import com.itm.fix5.data.jonec.admin.FIX5JonecDoReqAdministration;
import com.itm.fix5.data.jonec.message.struct.FIX5IDXMessage;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataHeader;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeListener;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.xtream.inet.trading.fix5.jonec.books.BookOfFIX5JonecRequest;
import com.itm.xtream.inet.trading.fix5.jonec.books.SheetOfFIX5JonecRequest;

/**
 *
 * @author fredy
 */
public class FIX5JonecMsgTransRecvHandler implements FIX5IDXBridgeListener {
    
    @Override
    public void onConnected(ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.SOCKET, ITMFileLoggerVarsConsts.logLevel.INFO, "FIX5-CONNECTED#" + ConnectionName + ", ChID:" + channel.getChannelID() + ", IP:" + channel.getAddress() + ", Port:" + channel.getPort());
        
        System.out.println("FIX5-CONNECTED#" + ConnectionName + "=\t" + channel.getChannelID());
        
        //.anggap mau logon:
        FIX5JonecDoReqAdministration.getInstance.doLogon(controller);
        
    }

    @Override
    public void onDisconnected(ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.SOCKET, ITMFileLoggerVarsConsts.logLevel.INFO, "FIX5-DISCONNECTED#" + ConnectionName + ", ChID:" + channel.getChannelID() + ", IP:" + channel.getAddress() + ", Port:" + channel.getPort());
        
        System.out.println("FIX5-DISCONNECTED#" + ConnectionName + "=\t" + channel.getChannelID());
        
        //.anggap sudah logout:
        FIX5JonecDoReqAdministration.getInstance.doLogoff(controller);
        
    }

    @Override
    public void onReceive(ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName, String messageLine, FIX5IDXMessage messageObject) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        System.out.println("FIX5-RECEIVED#" + ConnectionName + "=\t" + channel.getChannelID() + "\t" + messageLine);
        
        FIX5JonecDataHeader mMsgHdr = (FIX5JonecDataHeader)messageObject;
        if (mMsgHdr != null){
            SheetOfFIX5JonecRequest mSheet = BookOfFIX5JonecRequest.getInstance.retrieveSheetRecv(mMsgHdr.getfMsgSeqNum());
            if (mSheet == null){
                mSheet = new SheetOfFIX5JonecRequest(mMsgHdr.getfMsgSeqNum());
                mSheet.setFixMsg(messageLine);
                BookOfFIX5JonecRequest.getInstance.addOrUpdateSheetRecv(mSheet);
            }else{
                mSheet.setFixMsg(messageLine);
                BookOfFIX5JonecRequest.getInstance.backupProcessorRecv.backupMapObjectToFile(mMsgHdr.getfMsgSeqNum(), mSheet);
            }
        }
        
        FIX5JonecMsgTransRecvBridge.getInstance.processMessage(channel, controller, messageLine, messageObject);
        
    }

    @Override
    public void onSent(ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName, String messageLine) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        System.out.println("FIX5-SENT#" + ConnectionName + "=\t" + channel.getChannelID() + "\t" + messageLine);
        
        Long vMsgSeq = controller.getBridgeSockHandler().getFix5JonecMsgMapper().findMsgSeqNum(messageLine);
        SheetOfFIX5JonecRequest mSheet = BookOfFIX5JonecRequest.getInstance.retrieveSheetSend(vMsgSeq);
        if (mSheet == null){
            mSheet = new SheetOfFIX5JonecRequest(vMsgSeq);
            mSheet.setFixMsg(messageLine);
            BookOfFIX5JonecRequest.getInstance.addOrUpdateSheetSend(mSheet);
        }else{
            mSheet.setFixMsg(messageLine);
            BookOfFIX5JonecRequest.getInstance.backupProcessorSend.backupMapObjectToFile(vMsgSeq, mSheet);
        }
        
    }

    @Override
    public void onError(ITMSocketChannel channel, FIX5IDXBridgeController controller, String ConnectionName, Exception exception) {
        ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.SOCKET, ITMFileLoggerVarsConsts.logLevel.INFO, "JATS-ERROR#" + ConnectionName + ", ChID:" + channel.getChannelID() + ", IP:" + channel.getAddress() + ", Port:" + channel.getPort(), exception);
        
        System.out.println("FIX5-ERROR#" + ConnectionName + "; msg=\t" + exception.getMessage());
        
    }

}
