/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.struct;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.util.MsgBuilder;
import com.itm.idx.data.qri.util.StringUtil;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIDataTradingInfoReplyMessage extends QRIDataHeader{
    
    private String      fTradingSessionID;
    private int         fTradSesStatus;
    private String      fText;
    
    public QRIDataTradingInfoReplyMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }
    
    public QRIDataTradingInfoReplyMessage(){
    }

    public String getfTradingSessionID() {
        return fTradingSessionID;
    }

    public void setfTradingSessionID(String fTradingSessionID) {
        this.fTradingSessionID = fTradingSessionID;
    }

    public int getfTradSesStatus() {
        return fTradSesStatus;
    }

    public void setfTradSesStatus(int fTradSesStatus) {
        this.fTradSesStatus = fTradSesStatus;
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        //. set message header (MsgType) = h
        setfMsgType(QRIDataConst.MsgType.TradingInfo.getValue());
        
        msgBuild.setMessage(getStringHeader());
        
        msgBuild.addData(QRIDataConst.QRITag.MsgType, getfMsgType());
        msgBuild.addData(QRIDataConst.QRITag.Checksum, StringHelper.addZeroFromInt(0, 3));
        
        return convertToJonectMessage(msgBuild.getMessage(), getfMsgType());
    }
    
    @Override
    public void assignValue(){
        Map<String, String> fieldMap = getMapMsgFields();
        
        Enumeration<String> strEnum = Collections.enumeration(fieldMap.keySet());
        String keyField;
        String val;
        //.-- asign nilai header nya dulu
        assignHeaderValue();
        while(strEnum.hasMoreElements()) {
            keyField = strEnum.nextElement();
            val = fieldMap.get(keyField);
            switch(keyField){
                case QRIDataConst.QRITag.TradingSessionID: 
                    setfTradingSessionID(val);
                    break;
                case QRIDataConst.QRITag.TradSesStatus: 
                    setfTradSesStatus(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.Text: 
                    setfText(val);
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
}
