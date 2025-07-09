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
public class QRIDataSecurityAttributeMessage extends QRIDataHeader {
    private long            fSeqNum;
    private double          fSecIndex;
    private String          fSecurityID;
    private String          fSymbolSfx;
    private int             fNoSecCReport;
    private String          fSecCReportCode;
    private String          fSecCReportData;
    
    public QRIDataSecurityAttributeMessage(){
    }
    
    public QRIDataSecurityAttributeMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }
    
    public long getfSeqNum() {
        return fSeqNum;
    }

    public void setfSeqNum(long fSeqNum) {
        this.fSeqNum = fSeqNum;
    }

    public double getfSecIndex() {
        return fSecIndex;
    }

    public void setfSecIndex(double fSecIndex) {
        this.fSecIndex = fSecIndex;
    }

    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }

    public String getfSymbolSfx() {
        return fSymbolSfx;
    }

    public void setfSymbolSfx(String fSymbolSfx) {
        this.fSymbolSfx = fSymbolSfx;
    }

    public int getfNoSecCReport() {
        return fNoSecCReport;
    }

    public void setfNoSecCReport(int fNoSecCReport) {
        this.fNoSecCReport = fNoSecCReport;
    }

    public String getfSecCReportCode() {
        return fSecCReportCode;
    }

    public void setfSecCReportCode(String fSecCReportCode) {
        this.fSecCReportCode = fSecCReportCode;
    }

    public String getfSecCReportData() {
        return fSecCReportData;
    }

    public void setfSecCReportData(String fSecCReportData) {
        this.fSecCReportData = fSecCReportData;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        //. set message header (MsgType) = U5
        setfMsgType(QRIDataConst.MsgType.SecurityChangeReport.getValue());
        
        msgBuild.setMessage(getStringHeader());
        msgBuild.addData(QRIDataConst.QRITag.SeqNum, String.valueOf(fSeqNum));      
        msgBuild.addData(QRIDataConst.QRITag.SecIndex, String.valueOf(fSecIndex));
        msgBuild.addData(QRIDataConst.QRITag.SecurityID, String.valueOf(fSecurityID));
        msgBuild.addData(QRIDataConst.QRITag.SymbolSfx, String.valueOf(fSymbolSfx));
        msgBuild.addData(QRIDataConst.QRITag.NoSecCReport, String.valueOf(fNoSecCReport));
        msgBuild.addData(QRIDataConst.QRITag.AttributeCode, String.valueOf(fSecCReportCode));        
        msgBuild.addData(QRIDataConst.QRITag.AttributeData, String.valueOf(fSecCReportData));
        
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
                    setfSeqNum(StringUtil.toLong(val));
                    break;
                case QRIDataConst.QRITag.SecIndex:
                    setfSecIndex(StringUtil.toDouble(val));
                    break;
                case QRIDataConst.QRITag.SecurityID:
                    setfSecurityID(val);
                    break;
                case QRIDataConst.QRITag.SymbolSfx:
                    setfSymbolSfx(val);
                    break;
                case QRIDataConst.QRITag.NoSecCReport:
                    setfNoSecCReport(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.AttributeCode:
                    setfSecCReportCode(val);
                    break;
                case QRIDataConst.QRITag.AttributeData:
                    setfSecCReportData(val);
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
    
}
