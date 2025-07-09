/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.process;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.BrokerReferenceHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataAdvertisingMessage;
import com.itm.idx.data.qri.message.struct.QRIDataErrorMessage;
import com.itm.idx.data.qri.message.struct.QRIDataITMMessage;
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataNewsMessage;
import com.itm.idx.data.qri.message.struct.QRIDataOrderListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataSecuritiesMessage;
import com.itm.idx.data.qri.message.struct.QRIDataSecurityAttributeMessage;
import com.itm.idx.data.qri.message.struct.QRIDataSubscription;
import com.itm.idx.data.qri.message.struct.QRIDataTradeListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataTradingInfoReplyMessage;
import com.itm.idx.data.qri.message.struct.QRIDataTradingLimitMessage;
import com.itm.idx.data.qri.message.struct.QRIDataUnknownMessage;
import com.itm.idx.data.qri.message.struct.logon.QRIDataLogon;
import com.itm.idx.data.qri.message.struct.logon.QRIDataLogonReply;
import com.itm.idx.data.qri.message.struct.logout.QRIDataLogout;
import com.itm.idx.data.qri.message.struct.logout.QRIDataLogoutReply;
import com.itm.idx.data.qri.message.struct.password.QRIDataChangePassword;
import com.itm.idx.data.qri.message.struct.password.QRIDataChangePasswordReply;
import com.itm.idx.data.qri.message.struct.password.QRIDataChangePasswordReply.ChangePassReplyType;
import com.itm.idx.data.qri.util.JonecUtil;
import com.itm.idx.data.qri.util.StringUtil;
import com.itm.idx.message.IDXMessage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIMessageProcessor {

    public QRIMessageProcessor(){
    }
    
    public IDXMessage processMessage(String originalMessage, boolean forServerSimulation){
        
        Map<String, String> mapMsg = mappingMessage(originalMessage);
        
        String msgType = mapMsg.get(QRIDataConst.QRITag.MsgType);
        return processMessageByMessageType(msgType, mapMsg, originalMessage, forServerSimulation);
        
    }
    
    private IDXMessage processMessageExecReport(Map<String, String> mapMsg){
        
        String secondOrderID =  mapMsg.get(QRIDataConst.QRITag.SecondaryOrderID);
        String noContraBroker = mapMsg.get(QRIDataConst.QRITag.NoContraBrokers);
        String handleInst = mapMsg.get(QRIDataConst.QRITag.HandlInst);
        IDXMessage retObject = null;
        if (secondOrderID != null){ //. terdapat secondary order id /trade number (Trade List)
            QRIDataTradeListMessage tradelist = new QRIDataTradeListMessage();
            retObject = tradelist;
        }else{ //. tidak terdapat secondary order id (bisa Order List/order Neg Deal)
            if (noContraBroker == null){ //. tidak terdapat field no contra broker (Order List)
                if ((handleInst != null) && (StringUtil.toInteger(handleInst) == ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT)){
                    QRIDataAdvertisingMessage orderlist = new QRIDataAdvertisingMessage();
                    retObject = orderlist;
                }else{
                    QRIDataOrderListMessage orderlist = new QRIDataOrderListMessage();
                    retObject = orderlist;
                }
            }else{ //. terdapat confield no contra broker (Neg Deal)
                QRIDataNegDealListMessage negdeal = new QRIDataNegDealListMessage();
                retObject = negdeal;
            }
        }
        
        return retObject;
    }
    
    private IDXMessage processMessageByMessageType(String msgType, Map<String, String> mapMsg, String originalMsg, boolean forServerSimulation){
        IDXMessage retObject = null;
        String fConnName = "";
        if (forServerSimulation == true && msgType != null){
            String[] arrField = originalMsg.split(QRIDataConst.HEADER_FIELD_SEPARATOR,-1);
            if (arrField.length > 2){
                fConnName = arrField[2];
            }
        }
        if (msgType == null){
            retObject = new QRIDataUnknownMessage(originalMsg);
        }else if (msgType.equals(QRIDataConst.MsgType.ChangePass.getValue())){
            if (forServerSimulation){
                QRIDataChangePassword chgpwdr = new QRIDataChangePassword(mapMsg);
                chgpwdr.setfConnName(fConnName);
                retObject = chgpwdr;
            }else{
                QRIDataChangePasswordReply chgpwdr = new QRIDataChangePasswordReply();
                chgpwdr.setfChangePassReplyType(ChangePassReplyType.ChangePassBAD);
                if (mapMsg.containsKey(QRIDataConst.QRITag.ReturnValue)){
                    String zText2Parse = mapMsg.get(QRIDataConst.QRITag.ReturnValue);
                    if (!StringHelper.isNullOrEmpty(zText2Parse)){
                        zText2Parse = zText2Parse.toLowerCase();
                        if ((zText2Parse.contains("success")) && (zText2Parse.contains("changed"))){
                            chgpwdr.setfChangePassReplyType(ChangePassReplyType.ChangePassOK);
                        }
                    }
                }
                retObject = chgpwdr;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.Logon.getValue())){
            if (forServerSimulation){
                QRIDataLogon logonr = new QRIDataLogon(mapMsg);
                logonr.setfConnName(fConnName);
                retObject = logonr;
            }else{
                QRIDataLogonReply logonr = new QRIDataLogonReply();
                retObject = logonr;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.Logout.getValue())){
            if (forServerSimulation){
                QRIDataLogout logoutr = new QRIDataLogout(mapMsg);
                logoutr.setfConnName(fConnName);
                retObject = logoutr;
            }else{
                QRIDataLogoutReply logoutr = new QRIDataLogoutReply();
                retObject = logoutr;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.Subscription.getValue())){
            if (forServerSimulation){
                QRIDataSubscription subsr = new QRIDataSubscription();
                subsr.setfConnName(fConnName);
                retObject = subsr;
            }else{
                
            }
        }else if(msgType.equals(QRIDataConst.MsgType.News.getValue())){
            if (forServerSimulation){
                
            }else{
                QRIDataNewsMessage news = new QRIDataNewsMessage();
                retObject = news;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.ExecutionReport.getValue())){
            if (forServerSimulation){
                
            }else{
                //. proses kusus
                retObject = processMessageExecReport(mapMsg);
            }
        }else if(msgType.equals(QRIDataConst.MsgType.GeneralError.getValue())){
            if (forServerSimulation){
                
            }else{
                QRIDataErrorMessage err = new QRIDataErrorMessage();
                String zBrokerRef = BrokerReferenceHelper.findBrokerRefFromJatsError(mapMsg.get(QRIDataConst.QRITag.RefErrorCode));
                err.setfBrokerRef(zBrokerRef);
                retObject = err;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.SecurityChangeReport.getValue())){
            if (forServerSimulation){
                
            }else{
                QRIDataSecurityAttributeMessage secAttr = new QRIDataSecurityAttributeMessage();
                retObject = secAttr;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.SecurityReport.getValue())){
            if (forServerSimulation){
                
            }else{
                QRIDataSecuritiesMessage secr = new QRIDataSecuritiesMessage();
                retObject = secr;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.TradingInfo.getValue())){
            if (forServerSimulation){
                
            }else{
                QRIDataTradingInfoReplyMessage tradinfo = new QRIDataTradingInfoReplyMessage();
                retObject = tradinfo;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.TradingLimit.getValue())){
            if (forServerSimulation){
                
            }else{
                QRIDataTradingLimitMessage tradlimit = new QRIDataTradingLimitMessage();
                retObject = tradlimit;
            }
        }else if(msgType.equals(QRIDataConst.MsgType.ITM.getValue())){
            if (forServerSimulation){
                
            }else{
                QRIDataITMMessage news = new QRIDataITMMessage();
                retObject = news;
            }
        }else{
            retObject = new QRIDataUnknownMessage(originalMsg);
        }
        
        if (retObject != null){
            retObject.setMapMsgFields(mapMsg);
            retObject.assignValue();
        }
        return retObject;
    }
    
    public Map<String, String> mappingMessage(String strMessage){
        HashMap<String, String> fieldMsg = new HashMap<>();
        String[] arrField = strMessage.split(Character.toString(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR),-1);
        
        
        if (arrField.length > 1){
            for (int i = 0; i < arrField.length; i++){
                String[] arrData = arrField[i].split(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR,-1);
                if (arrData.length == 2){
                    //. hilangkan header jonec (jika ada)
                    if (i == 0){
                        arrData[0] = JonecUtil.clearJonecHeader(arrData[0]);
                    }
                    fieldMsg.put(arrData[0], arrData[1]);
                }else{
                    //. invalid data in the tag (has '=' char more than one) 
                }
            }
            
        }
        
        return fieldMsg;
    }
    
    
}
