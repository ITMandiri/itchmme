/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.message.struct;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.jonec.consts.JonecConst;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.util.MsgBuilder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author Hirin
 */
public class QRIDataSubscription extends QRIDataHeader{
    
    public enum SubscribeType{
        Subscribe,
        Unsubscribe
    }
    
    public enum ReqSubscriptionType{
        OrderSubscript          ("O"),
        TradeSubscript          ("T"),
        NegSubscript            ("N"),
        LimitSubscript          ("L"),
        SecSubscript            ("E"),
        SecAttributeSubscript   ("C"),
        AdvSubscript            ("B");
        
        private ReqSubscriptionType(String val){
            this.value = val;
        }
        
        private String value;        
        public String getValue() {
            return value;
        }
        
        
    }
    
    private String                  fUserID;
    private ReqSubscriptionType     fReqType;
    private int                     fSeqNum;
    private int                     fLast;
    private String                  fSide; //.(REV)apm:20190903:int-->String;
    private String                  fExecBroker;
    private String                  fSymbolSfx;
    private String                  fSecurityID;
    private String                  fMarketCode;
    private String                  fBoardId;
    private SubscribeType           fSubscribeType;
    
    public QRIDataSubscription(){
        setfMsgType(QRIDataConst.MsgType.Subscription.getValue());
    }
    
    public QRIDataSubscription(Map<String, String> fieldMap){
        super(fieldMap);
    }

    public String getfUserID() {
        return fUserID;
    }

    public void setfUserID(String fUserID) {
        this.fUserID = fUserID;
    }

    public ReqSubscriptionType getfReqType() {
        return fReqType;
    }

    public void setfReqType(ReqSubscriptionType fReqType) {
        this.fReqType = fReqType;
    }

    public int getfSeqNum() {
        return fSeqNum;
    }

    public void setfSeqNum(int fSeqNum) {
        this.fSeqNum = fSeqNum;
    }

    public int getfLast() {
        return fLast;
    }

    public void setfLast(int fLast) {
        this.fLast = fLast;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }

    public String getfExecBroker() {
        if (fExecBroker == null) fExecBroker = JonecConst.DEFAULT_EXEC_BROKER_CODE;
        return fExecBroker;
    }

    public void setfExecBroker(String fExecBroker) {
        this.fExecBroker = fExecBroker;
    }

    public String getfSymbolSfx() {
        return fSymbolSfx;
    }

    public void setfSymbolSfx(String fSymbolSfx) {
        this.fSymbolSfx = fSymbolSfx;
    }

    public String getfSecurityID() {
        return fSecurityID;
    }

    public void setfSecurityID(String fSecurityID) {
        this.fSecurityID = fSecurityID;
    }

    public String getfMarketCode() {
        return fMarketCode;
    }

    public void setfMarketCode(String fMarketCode) {
        this.fMarketCode = fMarketCode;
    }

    public String getfBoardId() {
        return fBoardId;
    }

    public void setfBoardId(String fBoardId) {
        this.fBoardId = fBoardId;
    }
    
    public SubscribeType getfSubscribeType() {
        return fSubscribeType;
    }

    public void setfSubscribeType(SubscribeType fSubscribeType) {
        this.fSubscribeType = fSubscribeType;
    }
    
    
    public String msgToString(){
        MsgBuilder msgBuild = new MsgBuilder(QRIDataConst.MESSAGE_INNER_TAG_SEPARATOR, String.valueOf(QRIDataConst.MESSAGE_INTER_TAG_SEPARATOR));
        msgBuild.setMessage(getStringHeader());
        msgBuild.addData(QRIDataConst.QRITag.UserID, fUserID);
        msgBuild.addData(QRIDataConst.QRITag.ReqType, fReqType.getValue());
        msgBuild.addData(QRIDataConst.QRITag.SeqNum, String.valueOf(fSeqNum));
        if (fReqType == ReqSubscriptionType.OrderSubscript || 
            fReqType == ReqSubscriptionType.TradeSubscript ||
            fReqType == ReqSubscriptionType.NegSubscript  )
        {
            msgBuild.addData(QRIDataConst.QRITag.Last, String.valueOf(fLast));
        }
        
        if (fReqType == ReqSubscriptionType.SecSubscript ||  fReqType == ReqSubscriptionType.SecAttributeSubscript ) {
            msgBuild.addData(QRIDataConst.QRITag.SymbolSfx, String.valueOf(fSymbolSfx));
        }
        
        if (fReqType == ReqSubscriptionType.SecAttributeSubscript ||
            fReqType == ReqSubscriptionType.AdvSubscript           )
        {
            msgBuild.addData(QRIDataConst.QRITag.SecurityID, String.valueOf(fSecurityID));
        }
        
        if (fReqType == ReqSubscriptionType.SecSubscript || fReqType == ReqSubscriptionType.SecAttributeSubscript) {
            msgBuild.addData(QRIDataConst.QRITag.MarketCode, String.valueOf(fMarketCode));
        }
        
        if (fReqType == ReqSubscriptionType.TradeSubscript){
            msgBuild.addData(QRIDataConst.QRITag.Side, String.valueOf(fSide));
        }
        
        if (fReqType == ReqSubscriptionType.LimitSubscript){
            msgBuild.addData(QRIDataConst.QRITag.ExecBroker, fExecBroker);
        }
        
        if (fReqType == ReqSubscriptionType.AdvSubscript){
            msgBuild.addData(QRIDataConst.QRITag.SymbolSfx, fSymbolSfx);
        }
        
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
                case QRIDataConst.QRITag.UserID:
                    setfUserID(val);
                    break;
                case QRIDataConst.QRITag.ReqType:
                    if (val.equals(ReqSubscriptionType.OrderSubscript.getValue())){
                        setfReqType(ReqSubscriptionType.OrderSubscript);
                    }else if (val.equals(ReqSubscriptionType.TradeSubscript.getValue())){
                        setfReqType(ReqSubscriptionType.TradeSubscript);
                    }else if (val.equals(ReqSubscriptionType.NegSubscript.getValue())){
                        setfReqType(ReqSubscriptionType.NegSubscript);
                    }else if (val.equals(ReqSubscriptionType.LimitSubscript.getValue())){
                        setfReqType(ReqSubscriptionType.LimitSubscript);
                    }else if (val.equals(ReqSubscriptionType.SecSubscript.getValue())){
                        setfReqType(ReqSubscriptionType.SecSubscript);
                    }else if (val.equals(ReqSubscriptionType.SecAttributeSubscript.getValue())){
                        setfReqType(ReqSubscriptionType.SecAttributeSubscript);
                    }else if (val.equals(ReqSubscriptionType.AdvSubscript.getValue())){
                        setfReqType(ReqSubscriptionType.AdvSubscript);
                    }
                    break;
                case QRIDataConst.QRITag.SeqNum:
                    setfSeqNum(StringHelper.toInt(val));
                    break;
                case QRIDataConst.QRITag.Last:
                    setfLast(StringHelper.toInt(val));
                    break;
                case QRIDataConst.QRITag.Side:
                    setfSide(val);
                    break;
                case QRIDataConst.QRITag.ExecBroker:
                    setfExecBroker(val);
                    break;
                case QRIDataConst.QRITag.SymbolSfx:
                    setfSymbolSfx(val);
                    break;
                case QRIDataConst.QRITag.SecurityID:
                    setfSecurityID(val);
                    break;
                case QRIDataConst.QRITag.MarketCode:
                    setfMarketCode(val);
                    break;
                    
                default:
                    //. tag yang terlewatkan
                    break;
            }
        }
        
    }


}
