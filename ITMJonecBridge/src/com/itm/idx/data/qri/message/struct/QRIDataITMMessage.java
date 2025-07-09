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
public class QRIDataITMMessage extends QRIDataHeader{
    private String          fITMMsgType;
    private String          fMsgSubType1;
    private int             fMsgSubValue1;
    private String          fMsgSubType2;
    private int             fMsgSubValue2;
    
    public QRIDataITMMessage(){
    }
    
    public QRIDataITMMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public String getfITMMsgType() {
        return fITMMsgType;
    }

    public void setfITMMsgType(String fMsgType) {
        this.fITMMsgType = fMsgType;
    }

    public String getfMsgSubType1() {
        return fMsgSubType1;
    }

    public void setfMsgSubType1(String fMsgSubType1) {
        this.fMsgSubType1 = fMsgSubType1;
    }

    public int getfMsgSubValue1() {
        return fMsgSubValue1;
    }

    public void setfMsgSubValue1(int fMsgSubValue1) {
        this.fMsgSubValue1 = fMsgSubValue1;
    }

    public String getfMsgSubType2() {
        return fMsgSubType2;
    }

    public void setfMsgSubType2(String fMsgSubType2) {
        this.fMsgSubType2 = fMsgSubType2;
    }

    public int getfMsgSubValue2() {
        return fMsgSubValue2;
    }

    public void setfMsgSubValue2(int fMsgSubValue2) {
        this.fMsgSubValue2 = fMsgSubValue2;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        
        setfMsgType(QRIDataConst.MsgType.ITM.getValue());
        
        msgBuild.setMessage(getStringHeader());
        
        msgBuild.addData(QRIDataConst.QRITag.ITMMsgType, getfITMMsgType());
        msgBuild.addData(QRIDataConst.QRITag.MsgSubType1, getfMsgSubType1());
        msgBuild.addData(QRIDataConst.QRITag.MsgSubValue1, String.valueOf(getfMsgSubValue1()));
        msgBuild.addData(QRIDataConst.QRITag.MsgSubType2, getfMsgSubType2());
        msgBuild.addData(QRIDataConst.QRITag.MsgSubValue2, String.valueOf(getfMsgSubValue2()));
        
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
                case QRIDataConst.QRITag.ITMMsgType:
                    setfITMMsgType(val);
                    break;
                case QRIDataConst.QRITag.MsgSubType1:
                    setfMsgSubType1(val);
                    break;
                case QRIDataConst.QRITag.MsgSubValue1:
                    setfMsgSubValue1(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.MsgSubType2:
                    setfMsgSubType2(val);
                    break;
                case QRIDataConst.QRITag.MsgSubValue2:
                    setfMsgSubValue2(StringUtil.toInteger(val));
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
}
