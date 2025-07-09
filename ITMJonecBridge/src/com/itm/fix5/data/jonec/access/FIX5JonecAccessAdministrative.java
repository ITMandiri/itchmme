/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.access;

import com.itm.fix5.data.helpers.FIX5CheckSumHelper;
import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldValue;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecMsgType;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogon;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogout;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5JonecAccessAdministrative {
    FIX5IDXBridgeController hController     = null;
    String zConnectorCode                   = "";
    String zTraderCode                      = "";
    String zPassword1                       = "";
    String zPassword2                       = "";
    String zConnectionName                  = "";
    
    public FIX5JonecAccessAdministrative(FIX5IDXBridgeController hController, String zConnectorCode, String zTraderCode, String zPassword1, String zPassword2, String zConnectionName) {
        this.hController = hController;
        this.zConnectorCode = zConnectorCode;
        this.zTraderCode = zTraderCode;
        this.zPassword1 = zPassword1;
        this.zPassword2 = zPassword2;
        this.zConnectionName = zConnectionName;
    }
    
    public String login() {
        String mOut = "";
        
        if (this.hController != null){
            FIX5JonecDataLogon mMsg = new FIX5JonecDataLogon(new HashMap<String, ArrayList<String>>());
            mMsg.setfMsgType(FIX5JonecMsgType.LOGON);
            mMsg.setfMsgSeqNum(this.hController.getNextTXSequencedNo());
            mMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
            mMsg.setfEncryptMethod(StringHelper.fromInt(FIX5JonecFieldValue.ADMIN_ENCRYPT_METHOD));
            mMsg.setfHeartBtInt(StringHelper.fromInt(FIX5JonecFieldValue.ADMIN_HEARTBEAT_TIME));
            mMsg.setfUsername(this.zConnectorCode);
            mMsg.setfPassword(this.zPassword1);
            if (this.zPassword2 == null ? this.zPassword1 != null : !this.zPassword2.equals(this.zPassword1)){
                mMsg.setfNewPassword(this.zPassword2);
            }
            mMsg.setfDefaultApplVerID(StringHelper.fromInt(FIX5JonecFieldValue.ADMIN_DEFAULT_APPL_VER_ID));
            mOut = mMsg.msgToString();
            if (this.hController.isCalcHeader()){
                mOut = FIX5CheckSumHelper.repackMessageWithChecksum(mOut,true,true,this.zConnectionName);
            }
        }
        return mOut;
    }
    
    public String logout() {
        String mOut = "";
        
        if (this.hController != null){
            FIX5JonecDataLogout mMsg = new FIX5JonecDataLogout(new HashMap<String, ArrayList<String>>());
            mMsg.setfMsgType(FIX5JonecMsgType.LOGOUT);
            mMsg.setfMsgSeqNum(this.hController.getNextTXSequencedNo());
            mMsg.setfSendingTime(FIX5DateTimeHelper.getDateTimeFIX5UTCFormatDetail());
            mMsg.setfText(FIX5JonecFieldValue.ADMIN_LOGOUT_TEXT);
            mOut = mMsg.msgToString();
            if (this.hController.isCalcHeader()){
                mOut = FIX5CheckSumHelper.repackMessageWithChecksum(mOut,true,true,this.zConnectionName);
            }
        }
        return mOut;
    }
    
}
