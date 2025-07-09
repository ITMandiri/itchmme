/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.struct;

import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.Map;

/**
 *
 * @author aripam
 */
public class ORIDataNegotiationDealCancelReply extends ORIDataHeader {

    //.enums:
    public enum ORINegotiationDealCancelReplyType{
        OK,
        BAD,
        INFO
    }
    
    //.fields:
    private String fOrderID                                     = "";
    private String fExecID                                      = "";
    private int fExecTransType                                  = 0;
    private String fExecRefID                                   = "";
    private int fExecType                                       = 0;
    private int fOrdStatus                                      = 0;
    private String fSymbol                                      = "";
    private String fSide                                        = "";
    private long fLeavesQty                                     = 0;
    private long fCumQty                                        = 0;
    private double fAvgPx                                       = 0;
    private int fHandlInst                                      = 0;
    private String fText                                        = "";
    private double fLastPx                                      = 0;
    private long fLastShares                                    = 0;
    //.+BAD:
    private String fClOrdID                                     = "";
    private String fOrigClOrdID                                 = "";
    private int fCxlRejResponseTo                               = 0;
    //.+INFO:
    private String fOrdType                                     = "";
    
    
    private ORINegotiationDealCancelReplyType fNegotiationDealCancelReplyType;
    
    public ORIDataNegotiationDealCancelReply(Map<String, String> inputMsgFields) {
        super(inputMsgFields);
    }
    
    public String getfOrderID() {
        return fOrderID;
    }

    public void setfOrderID(String fOrderID) {
        this.fOrderID = fOrderID;
    }

    public String getfExecID() {
        return fExecID;
    }

    public void setfExecID(String fExecID) {
        this.fExecID = fExecID;
    }

    public int getfExecTransType() {
        return fExecTransType;
    }

    public void setfExecTransType(int fExecTransType) {
        this.fExecTransType = fExecTransType;
    }

    public String getfExecRefID() {
        return fExecRefID;
    }

    public void setfExecRefID(String fExecRefID) {
        this.fExecRefID = fExecRefID;
    }

    public int getfExecType() {
        return fExecType;
    }

    public void setfExecType(int fExecType) {
        this.fExecType = fExecType;
    }

    public int getfOrdStatus() {
        return fOrdStatus;
    }

    public void setfOrdStatus(int fOrdStatus) {
        this.fOrdStatus = fOrdStatus;
    }

    public String getfSymbol() {
        return fSymbol;
    }

    public void setfSymbol(String fSymbol) {
        this.fSymbol = fSymbol;
    }

    public String getfSide() {
        return fSide;
    }

    public void setfSide(String fSide) {
        this.fSide = fSide;
    }

    public long getfLeavesQty() {
        return fLeavesQty;
    }

    public void setfLeavesQty(long fLeavesQty) {
        this.fLeavesQty = fLeavesQty;
    }

    public long getfCumQty() {
        return fCumQty;
    }

    public void setfCumQty(long fCumQty) {
        this.fCumQty = fCumQty;
    }

    public double getfAvgPx() {
        return fAvgPx;
    }

    public void setfAvgPx(double fAvgPx) {
        this.fAvgPx = fAvgPx;
    }

    public int getfHandlInst() {
        return fHandlInst;
    }

    public void setfHandlInst(int fHandlInst) {
        this.fHandlInst = fHandlInst;
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }

    public double getfLastPx() {
        return fLastPx;
    }

    public void setfLastPx(double fLastPx) {
        this.fLastPx = fLastPx;
    }

    public long getfLastShares() {
        return fLastShares;
    }

    public void setfLastShares(long fLastShares) {
        this.fLastShares = fLastShares;
    }

    public String getfClOrdID() {
        return fClOrdID;
    }

    public void setfClOrdID(String fClOrdID) {
        this.fClOrdID = fClOrdID;
    }

    public String getfOrigClOrdID() {
        return fOrigClOrdID;
    }

    public void setfOrigClOrdID(String fOrigClOrdID) {
        this.fOrigClOrdID = fOrigClOrdID;
    }

    public int getfCxlRejResponseTo() {
        return fCxlRejResponseTo;
    }

    public void setfCxlRejResponseTo(int fCxlRejResponseTo) {
        this.fCxlRejResponseTo = fCxlRejResponseTo;
    }

    public String getfOrdType() {
        return fOrdType;
    }

    public void setfOrdType(String fOrdType) {
        this.fOrdType = fOrdType;
    }

    public ORINegotiationDealCancelReplyType getfNegotiationDealCancelReplyType() {
        return fNegotiationDealCancelReplyType;
    }

    public void setfNegotiationDealCancelReplyType(ORINegotiationDealCancelReplyType fNegotiationDealCancelReplyType) {
        this.fNegotiationDealCancelReplyType = fNegotiationDealCancelReplyType;
    }

    public boolean assignMessage(){
        boolean bOut = false;
        try{
            //.assign header:
            assignHeaderMessage();
            //.assign data:
            Map<String, String> mapFields = getMapMsgFields();
            if ((mapFields != null) && (!mapFields.isEmpty())){
                for (String zKey : mapFields.keySet()){
                    String zValue = mapFields.get(zKey);
                    switch(zKey){
                        case ORIDataConst.ORIFieldTag.ORDERID:
                            setfOrderID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.EXECID:
                            setfExecID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.EXECTRANSTYPE:
                            setfExecTransType(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.EXECREFID:
                            setfExecRefID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.EXECTYPE:
                            setfExecType(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.ORDSTATUS:
                            setfOrdStatus(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.SYMBOL:
                            setfSymbol(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.SIDE:
                            setfSide(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.LEAVESQTY:
                            setfLeavesQty(StringHelper.toLong(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.CUMQTY:
                            setfCumQty(StringHelper.toLong(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.AVGPX:
                            setfAvgPx(StringHelper.toDouble(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.HANDLINST:
                            setfHandlInst(StringHelper.toInt(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.TEXT:
                            setfText(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.LASTPX:
                            setfLastPx(StringHelper.toDouble(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.LASTSHARES:
                            setfLastShares(StringHelper.toLong(zValue));
                            break;
                        case ORIDataConst.ORIFieldTag.CLORDID:
                            setfClOrdID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.ORIGCLORDID:
                            setfOrigClOrdID(zValue);
                            break;
                        case ORIDataConst.ORIFieldTag.CXLREJRESPONSETO:
                            setfCxlRejResponseTo(StringHelper.toInt(zValue));
                            break;
                        default:
                            break;
                    }
                }
                bOut = true;
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return bOut;
    }
    
    public String msgDataToString() {
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            
            if (null != getfNegotiationDealCancelReplyType())switch (getfNegotiationDealCancelReplyType()) {
                case OK:
                    sb.append(ORIDataConst.ORIFieldTag.ORDERID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfOrderID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.EXECID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfExecID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.EXECTRANSTYPE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfExecTransType()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.EXECREFID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfExecRefID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.EXECTYPE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfExecType()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.ORDSTATUS).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfOrdStatus()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.SYMBOL).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfSymbol()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.SIDE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfSide()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.LEAVESQTY).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfLeavesQty()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.CUMQTY).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfCumQty()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.AVGPX).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfAvgPx()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.HANDLINST).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfHandlInst()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.TEXT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfText()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.LASTPX).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfLastPx()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.LASTSHARES).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfLastShares()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    
                    break;
                case BAD:
                    sb.append(ORIDataConst.ORIFieldTag.ORDERID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfOrderID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.CLORDID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfClOrdID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.ORIGCLORDID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfOrigClOrdID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.ORDSTATUS).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfOrdStatus()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.CXLREJRESPONSETO).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfCxlRejResponseTo()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.TEXT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfText()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    
                    break;
                case INFO:
                    sb.append(ORIDataConst.ORIFieldTag.ORDERID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfOrderID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.EXECID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfExecID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.EXECTRANSTYPE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfExecTransType()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.EXECREFID).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfExecRefID()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.EXECTYPE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfExecType()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.ORDSTATUS).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfOrdStatus()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.SYMBOL).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfSymbol()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.SIDE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfSide()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.ORDTYPE).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfOrdType()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.LASTPX).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfLastPx()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.LASTSHARES).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfLastShares()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.LEAVESQTY).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfLeavesQty()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.CUMQTY).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfCumQty()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.AVGPX).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfAvgPx()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.HANDLINST).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfHandlInst()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    sb.append(ORIDataConst.ORIFieldTag.TEXT).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
                    sb.append(getfText()).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
                    
                    break;
                default:
                    break;
            } 
            
            sb.append(ORIDataConst.ORIFieldTag.CHECKSUM).append(ORIDataConst.ORIFieldFmt.KV_SEPARATOR);
            sb.append(StringHelper.addZeroFromInt(getfCheckSum(), ORIDataConst.ORIFieldValueLength.CHECKSUM)).append(ORIDataConst.ORIFieldFmt.FIELD_SEPARATOR);
            
            //... .
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public String msgToString() {
        String zOut = "";
        try{
            StringBuilder sb = new StringBuilder();
            if (StringHelper.isNullOrEmpty(getfMsgType())){
                if (null != getfNegotiationDealCancelReplyType())switch (getfNegotiationDealCancelReplyType()) {
                    case OK:
                        setfMsgType(ORIDataConst.ORIMsgType.EXECUTION_REPORT);
                        break;
                    case BAD:
                        setfMsgType(ORIDataConst.ORIMsgType.ORDER_CANCEL_REJECT);
                        break;
                    case INFO:
                        setfMsgType(ORIDataConst.ORIMsgType.EXECUTION_REPORT);
                        break;
                    default:
                        break;
                } 
            }
            sb.append(msgBundlePrefixToString());
            sb.append(msgHeaderToString());
            sb.append(msgDataToString());
            sb.append(msgBundleSuffixToString());
            
            zOut = sb.toString();
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
}
