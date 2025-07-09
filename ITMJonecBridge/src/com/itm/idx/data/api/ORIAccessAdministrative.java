/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.api;

import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValue;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeChangePassword;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogon;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogout;

/**
 *
 * @author aripam
 */
public class ORIAccessAdministrative {
    private String fUserID                                      = "";
    private String fPassword                                    = "";
    private String fConnectionName                              = "";
    
    public ORIAccessAdministrative(String zUserID, String zPassword, String zConnectionName) {
        this.fUserID = zUserID;
        this.fPassword = zPassword;
        this.fConnectionName = zConnectionName;
    }
    
    public String login() {
        ORIDataAdministrativeLogon msg = new ORIDataAdministrativeLogon(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfUserID(fUserID);
        msg.setfCurrentPassword(fPassword);
        msg.setfEncryptMethod(ORIFieldValue.ADMIN_ENCRYPT_METHOD);
        msg.setfHeartBtInt(ORIFieldValue.ADMIN_HEARTBEAT_TIME);
        return msg.msgToString();
    }
    
    public String logout() {
        ORIDataAdministrativeLogout msg = new ORIDataAdministrativeLogout(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfText(fUserID);
        return msg.msgToString();
    }
    
    public String changePassword(String zNewPassword) {
        ORIDataAdministrativeChangePassword msg = new ORIDataAdministrativeChangePassword(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfUserID(fUserID);
        msg.setfCurrentPassword(fPassword);
        msg.setfNewPassword(zNewPassword);
        msg.setfReturnValue(ORIFieldValue.SINGLE_SPACE);
        return msg.msgToString();
    }
    
}
