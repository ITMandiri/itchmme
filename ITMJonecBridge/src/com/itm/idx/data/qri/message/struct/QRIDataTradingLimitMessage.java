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
public class QRIDataTradingLimitMessage extends QRIDataHeader{
    private long            fSeqNumber;
    private String          fExecBroker;
    private String          fCurrency;
    private String          fTag;
    private String          fDescription;
    private long            fOpenBal;
    private long            fCurrentPos;
    private long            fPlannedPos;
    private long            fLimit1;
    private String          fLimit1Set;
    private long            fLimit2;
    private String          fLimit2Set;
    
    public QRIDataTradingLimitMessage(){
    }
    
    public QRIDataTradingLimitMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public long getfSeqNumber() {
        return fSeqNumber;
    }

    public void setfSeqNumber(long fSeqNumber) {
        this.fSeqNumber = fSeqNumber;
    }

    public String getfExecBroker() {
        return fExecBroker;
    }

    public void setfExecBroker(String fExecBroker) {
        this.fExecBroker = fExecBroker;
    }

    public String getfCurrency() {
        return fCurrency;
    }

    public void setfCurrency(String fCurrency) {
        this.fCurrency = fCurrency;
    }

    public String getfTag() {
        return fTag;
    }

    public void setfTag(String fTag) {
        this.fTag = fTag;
    }

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription;
    }

    public long getfOpenBal() {
        return fOpenBal;
    }

    public void setfOpenBal(long fOpenBal) {
        this.fOpenBal = fOpenBal;
    }

    public long getfCurrentPos() {
        return fCurrentPos;
    }

    public void setfCurrentPos(long fCurrentPos) {
        this.fCurrentPos = fCurrentPos;
    }

    public long getfPlannedPos() {
        return fPlannedPos;
    }

    public void setfPlannedPos(long fPlannedPos) {
        this.fPlannedPos = fPlannedPos;
    }

    public long getfLimit1() {
        return fLimit1;
    }

    public void setfLimit1(long fLimit1) {
        this.fLimit1 = fLimit1;
    }

    public String getfLimit1Set() {
        return fLimit1Set;
    }

    public void setfLimit1Set(String fLimit1Set) {
        this.fLimit1Set = fLimit1Set;
    }

    public long getfLimit2() {
        return fLimit2;
    }

    public void setfLimit2(long fLimit2) {
        this.fLimit2 = fLimit2;
    }

    public String getfLimit2Set() {
        return fLimit2Set;
    }

    public void setfLimit2Set(String fLimit2Set) {
        this.fLimit2Set = fLimit2Set;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        //. set message header (MsgType) = U6
        setfMsgType(QRIDataConst.MsgType.TradingLimit.getValue());
        
        msgBuild.setMessage(getStringHeader());
        msgBuild.addData(QRIDataConst.QRITag.SeqNum, String.valueOf(fSeqNumber));
        msgBuild.addData(QRIDataConst.QRITag.ExecBroker, String.valueOf(fExecBroker));
        msgBuild.addData(QRIDataConst.QRITag.Currency, String.valueOf(fCurrency));
        msgBuild.addData(QRIDataConst.QRITag.Tag, String.valueOf(fTag));
        msgBuild.addData(QRIDataConst.QRITag.Description, String.valueOf(fDescription));
        msgBuild.addData(QRIDataConst.QRITag.OpenBal, String.valueOf(fOpenBal));
        msgBuild.addData(QRIDataConst.QRITag.CurrentPost, String.valueOf(fCurrentPos));
        msgBuild.addData(QRIDataConst.QRITag.PlannedPos, String.valueOf(fPlannedPos));
        msgBuild.addData(QRIDataConst.QRITag.Limit1, String.valueOf(fLimit1));
        msgBuild.addData(QRIDataConst.QRITag.Limit1Set, String.valueOf(fLimit1Set));
        msgBuild.addData(QRIDataConst.QRITag.Limit2, String.valueOf(fLimit2));
        msgBuild.addData(QRIDataConst.QRITag.Limit2Set, String.valueOf(fLimit2Set));
        
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
                case QRIDataConst.QRITag.SeqNum:
                    setfSeqNumber(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.ExecBroker:
                    setfExecBroker(val);
                    break;
                case QRIDataConst.QRITag.Currency:
                    setfCurrency(val);
                    break;
                case QRIDataConst.QRITag.Tag:
                    setfTag(val);
                    break;
                case QRIDataConst.QRITag.Description:
                    setfDescription(val);
                    break;
                case QRIDataConst.QRITag.OpenBal:
                    setfOpenBal(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.CurrentPost:
                    setfCurrentPos(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.PlannedPos:
                    setfPlannedPos(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.Limit1:
                    setfLimit1(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.Limit1Set:
                    setfLimit1Set(val);
                    break;
                case QRIDataConst.QRITag.Limit2:
                    setfLimit2(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.Limit2Set:
                    setfLimit2Set(val);
                    break;                    
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
}
