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
public class QRIDataNewsMessage extends QRIDataHeader{
    private int             fLinesOfText;
    private String          fNewsTitle;
    private int             fEncodedTextLen;
    private int             fEncodedText;
    private String          fOrigTime;
    private String          fHeadline;
    
    public QRIDataNewsMessage(){
    }
    
    public QRIDataNewsMessage(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public int getfLinesOfText() {
        return fLinesOfText;
    }

    public void setfLinesOfText(int fLinesOfText) {
        this.fLinesOfText = fLinesOfText;
    }

    public String getfNewsTitle() {
        return fNewsTitle;
    }

    public void setfNewsTitle(String fNewsTitle) {
        this.fNewsTitle = fNewsTitle;
    }

    public int getfEncodedText() {
        return fEncodedText;
    }

    public void setfEncodedText(int fEncodedText) {
        this.fEncodedText = fEncodedText;
    }

    public String getfOrigTime() {
        return fOrigTime;
    }

    public void setfOrigTime(String fOrigTime) {
        this.fOrigTime = fOrigTime;
    }

    public String getfHeadline() {
        return fHeadline;
    }

    public void setfHeadline(String fHeadline) {
        this.fHeadline = fHeadline;
    }

    public int getfEncodedTextLen() {
        return fEncodedTextLen;
    }

    public void setfEncodedTextLen(int fEncodedTextLen) {
        this.fEncodedTextLen = fEncodedTextLen;
    }
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
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
                case QRIDataConst.QRITag.LinesOfText:
                    setfLinesOfText(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.Text:
                    setfNewsTitle(val);
                    break;
                case QRIDataConst.QRITag.EncodedTextLen:
                    setfEncodedTextLen(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.EncodeText:
                    setfEncodedText(StringUtil.toInteger(val));
                    break;
                case QRIDataConst.QRITag.OrigTime:
                    setfOrigTime(val);
                    break;
                case QRIDataConst.QRITag.Headline:
                    setfHeadline(val);
                    break;
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }
}
