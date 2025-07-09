/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.api;

import com.itm.idx.data.jonec.consts.JonecConst;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataSubscription;
import com.itm.idx.data.qri.message.struct.logon.QRIDataLogon;
import com.itm.idx.data.qri.message.struct.logout.QRIDataLogout;
import com.itm.idx.data.qri.message.struct.password.QRIDataChangePassword;
import com.itm.idx.data.qri.util.StringUtil;

/**
 *
 * @author Hirin
 */
public class QRIAccess {
    private String strUserID;
    private String strPassword;
    private String strConnName;
    public QRIAccess(String _userid, String _passw, String _connName){
        this.strUserID = StringUtil.rightJustify(_userid, 12);
        this.strPassword = StringUtil.rightJustify(_passw, 10);
        this.strConnName = _connName;
    }
    
    public String login(){
        QRIDataLogon logon = new  QRIDataLogon();
        logon.setfConnName(strConnName);
        logon.setfUserID(strUserID);
        logon.setfCurrentPassword(strPassword);
        
        return logon.msgToString();
    }
    
    public String logout(){
        QRIDataLogout logout = new QRIDataLogout();
        logout.setfConnName(strConnName);
        logout.setfText(strUserID);
        
        return logout.msgToString();
    }
    
    public String changePassword(String newPass){
        QRIDataChangePassword password = new QRIDataChangePassword();
        password.setfConnName(strConnName);
        password.setfUserID(strUserID);
        password.setfCurrentPassword(strPassword);
        password.setfNewPassword(newPass);
        password.setfReturnValue(JonecConst.SINGLE_SPACE);
        return password.msgToString();
    }
    
    public String subscribeOrder(int seqNum){ //.auto.1
        QRIDataSubscription subscribe = new QRIDataSubscription();
        
        subscribe.setfConnName(strConnName);
        subscribe.setfUserID(strUserID);
        subscribe.setfReqType(QRIDataSubscription.ReqSubscriptionType.OrderSubscript);
        subscribe.setfSeqNum(seqNum);
        subscribe.setfLast(0);
        
        return subscribe.msgToString();
    }
    
    public String subscribeTrade(int seqNum){ //.auto.2
        QRIDataSubscription subscribe = new QRIDataSubscription();
        subscribe.setfConnName(strConnName);
        subscribe.setfUserID(strUserID);
        subscribe.setfReqType(QRIDataSubscription.ReqSubscriptionType.TradeSubscript);
        subscribe.setfSeqNum(seqNum);
        subscribe.setfLast(0);
        subscribe.setfSide(QRIDataConst.QRIFieldValue.SIDE_BUY); //.1
        return subscribe.msgToString();
    }
    
    public String subscribeNego(int seqNum){ //.auto.5
        QRIDataSubscription subscribe = new QRIDataSubscription();
        
        subscribe.setfConnName(strConnName);
        subscribe.setfUserID(strUserID);
        subscribe.setfReqType(QRIDataSubscription.ReqSubscriptionType.NegSubscript);
        subscribe.setfSeqNum(seqNum);
        subscribe.setfLast(0);
        
        return subscribe.msgToString();
     }
     
     public String subscribeTradingLimit(int seqNum, String brokerCode){ //.auto.3
          QRIDataSubscription subscribe = new QRIDataSubscription();
        
        subscribe.setfConnName(strConnName);
        subscribe.setfUserID(strUserID);
        subscribe.setfReqType(QRIDataSubscription.ReqSubscriptionType.LimitSubscript);
        subscribe.setfSeqNum(seqNum);
        subscribe.setfExecBroker(brokerCode);
        
        return subscribe.msgToString();
         
     }
     
     public String subscribeSecurity(int seqNum, QRIDataConst.SymbolSfx type){ //.auto.4
        QRIDataSubscription subscribe = new QRIDataSubscription();
        
        subscribe.setfConnName(strConnName);
        subscribe.setfUserID(strUserID);
        subscribe.setfReqType(QRIDataSubscription.ReqSubscriptionType.SecSubscript);
        subscribe.setfSeqNum(seqNum);
        subscribe.setfSymbolSfx(type.getValue());
        subscribe.setfMarketCode(QRIDataConst.MARKET_CODE_EQUITY);
        
        return subscribe.msgToString();
    }
     
     public String subscribeSecurityAttribute(int seqNum, String secID, QRIDataConst.SymbolSfx type){
        QRIDataSubscription subscribe = new QRIDataSubscription();
        
        subscribe.setfConnName(strConnName);
        subscribe.setfUserID(strUserID);
        subscribe.setfReqType(QRIDataSubscription.ReqSubscriptionType.SecAttributeSubscript);
        subscribe.setfSeqNum(seqNum);
        subscribe.setfSymbolSfx(type.getValue());
        subscribe.setfSecurityID(secID);
        subscribe.setfMarketCode(QRIDataConst.MARKET_CODE_EQUITY);
        
        return subscribe.msgToString();
     }
     
     public String subscribeAdvertising(int seqNum, String secID, QRIDataConst.SymbolSfx type){
        QRIDataSubscription subscribe = new QRIDataSubscription();
        
        subscribe.setfConnName(strConnName);
        subscribe.setfUserID(strUserID);
        subscribe.setfReqType(QRIDataSubscription.ReqSubscriptionType.AdvSubscript);
        subscribe.setfSeqNum(seqNum);
        subscribe.setfSymbolSfx(type.getValue());
        subscribe.setfSecurityID(secID);
        subscribe.setfMarketCode(QRIDataConst.MARKET_CODE_EQUITY);
        
        return subscribe.msgToString();
    }
}
