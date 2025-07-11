/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.msgparser;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgBase;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgSequencedDataPacket;
import com.itm.soupbintcp.bridge.structs.ITMSoupBinTCPMsgUnsequencedDataPacket;
import com.itm.ts.ouch.consts.OUCHConsts;
import com.itm.ts.ouch.structs.OUCHMsgBase;
import com.itm.ts.ouch.structs.OUCHMsgOrderAccepted;
import com.itm.ts.ouch.structs.OUCHMsgOrderCanceled;
import com.itm.ts.ouch.structs.OUCHMsgOrderExecuted;
import com.itm.ts.ouch.structs.OUCHMsgOrderRejected;
import com.itm.ts.ouch.structs.OUCHMsgOrderReplaced;
import com.itm.ts.ouch.structs.OUCHMsgUnknown;

/**
 *
 * @author fredy
 */
public class ITMSoupBinTCPOUCHMsgParser {
    
    public final static ITMSoupBinTCPOUCHMsgParser getInstance = new ITMSoupBinTCPOUCHMsgParser();
    
    public OUCHMsgBase parseMessage(byte[] btMessageBytes, ITMSoupBinTCPMsgBase mSoupBinPacketObject){
        OUCHMsgBase mOut = null;
        try{
            if ((btMessageBytes != null) && (btMessageBytes.length > ITMSoupBinTCPBridgeConsts.SoupBinTCPLength.MINIMUM_PACKET_SIZE)){
                String vMsgType = String.valueOf((char)btMessageBytes[ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD]);
                if (mSoupBinPacketObject instanceof ITMSoupBinTCPMsgSequencedDataPacket){
                    switch(vMsgType){
                        case OUCHConsts.OUCHMessageType.MESSAGETYPE_OUTBOUND_ACCEPTED:
                            OUCHMsgOrderAccepted mObjAccepted = new OUCHMsgOrderAccepted();
                            mObjAccepted.parseMessage(btMessageBytes);
                            mOut = mObjAccepted;
                            break;
                        case OUCHConsts.OUCHMessageType.MESSAGETYPE_OUTBOUND_REPLACED:
                            OUCHMsgOrderReplaced mObjReplaced = new OUCHMsgOrderReplaced();
                            mObjReplaced.parseMessage(btMessageBytes);
                            mOut = mObjReplaced;
                            break;
                        case OUCHConsts.OUCHMessageType.MESSAGETYPE_OUTBOUND_CANCELED:
                            OUCHMsgOrderCanceled mObjCanceled = new OUCHMsgOrderCanceled();
                            mObjCanceled.parseMessage(btMessageBytes);
                            mOut = mObjCanceled;
                            break;
                        case OUCHConsts.OUCHMessageType.MESSAGETYPE_OUTBOUND_EXECUTED_ORDER:
                            OUCHMsgOrderExecuted mObjExecutedOrder = new OUCHMsgOrderExecuted();
                            mObjExecutedOrder.parseMessage(btMessageBytes);
                            mOut = mObjExecutedOrder;
                            break;
                        case OUCHConsts.OUCHMessageType.MESSAGETYPE_OUTBOUND_REJECTED_ORDER:
                            OUCHMsgOrderRejected mObjRejectedOrder = new OUCHMsgOrderRejected();
                            mObjRejectedOrder.parseMessage(btMessageBytes);
                            mOut = mObjRejectedOrder;
                            break;
                    }
                } 
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.ITCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        if (mOut == null){
            mOut = new OUCHMsgUnknown();
        }
        return mOut;
    }
    
}
