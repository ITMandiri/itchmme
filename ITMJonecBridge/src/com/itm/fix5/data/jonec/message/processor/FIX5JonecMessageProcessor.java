/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.message.processor;

import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldFmt;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecFieldTag;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst.FIX5JonecMsgType;
import com.itm.fix5.data.jonec.message.struct.FIX5IDXMessage;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataBusinessMessageReject;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataExecutionReport;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataHeartbeat;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogonReply;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataLogoutReply;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataOrderCancelReject;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataReject;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataResendRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataSequenceReset;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTestRequest;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTradeCaptureReport;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataTradeCaptureReportAck;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataUnknownMessage;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5JonecMessageProcessor {
    
    public FIX5JonecMessageProcessor() {
    }
    
    public Map<String, ArrayList<String>> parseMsgToFields(String zInputMessage){
        Map<String, ArrayList<String>> mOut = new HashMap<>();
        try{
            if ((zInputMessage != null) && (zInputMessage.length() > 0)){
                //.parsing:
                String zFieldMessage = zInputMessage;
                if (zInputMessage.contains(FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR)){
                    String[] arrHeaders = StringHelper.splitAll(zInputMessage, FIX5JonecFieldFmt.HEADER_FIELD_SEPARATOR);
                    if (arrHeaders.length >= 6){
                        for (String zFieldData : arrHeaders) {
                            if ((zFieldData.contains(FIX5JonecFieldFmt.FIELD_SEPARATOR)) && (zFieldData.contains(FIX5JonecFieldFmt.KV_SEPARATOR))){
                                zFieldMessage = zFieldData;
                                break;
                            }
                        }
                    }
                }
                String[] arrFields = StringHelper.splitAll(zFieldMessage, FIX5JonecFieldFmt.FIELD_SEPARATOR);
                if ((arrFields != null) && (arrFields.length > 0)){
                    for (String zKeyVal : arrFields){
                        String[] arrKeyVal = StringHelper.splitTwo(zKeyVal, FIX5JonecFieldFmt.KV_SEPARATOR);
                        if ((arrKeyVal != null) && (arrKeyVal.length >= 2)){
                            String zKey = arrKeyVal[0];
                            String zVal = arrKeyVal[1];
                            //.siap key-value tiap field:
                            if (mOut.containsKey(zKey)){
                                ArrayList<String> arrValues = mOut.get(zKey);
                                arrValues.add(zVal);
                            }else{
                                ArrayList<String> arrValues = new ArrayList<>();
                                arrValues.add(zVal);
                                mOut.put(zKey, arrValues);
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public long findMsgSeqNum(String zInputMessage){
        long mOut = 0;
        try{
            Map<String, ArrayList<String>> mapSrcFields = parseMsgToFields(zInputMessage);
            if (!mapSrcFields.isEmpty()){
                String zFieldMsgSeqNum = "";
                if (mapSrcFields.containsKey(FIX5JonecFieldTag.MSGSEQNUM)){
                    zFieldMsgSeqNum = mapSrcFields.get(FIX5JonecFieldTag.MSGSEQNUM).get(0);
                }
                if (!StringHelper.isNullOrEmpty(zFieldMsgSeqNum)){
                    mOut = StringHelper.toLong(zFieldMsgSeqNum);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public FIX5IDXMessage parseMessage(String zInputMessage){
        FIX5IDXMessage mOut = null;
        try{
            Map<String, ArrayList<String>> mapSrcFields = parseMsgToFields(zInputMessage);
            if (!mapSrcFields.isEmpty()){
                //.mapping message:
                mOut = mappingMessage(mapSrcFields);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        if (mOut == null){
            mOut = new FIX5JonecDataUnknownMessage(zInputMessage);
        }
        return mOut;
    }
    
    public FIX5IDXMessage mappingMessage(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                String zFieldMsgType = "";
                if (mapInputFields.containsKey(FIX5JonecFieldTag.MSGTYPE)){
                    zFieldMsgType = mapInputFields.get(FIX5JonecFieldTag.MSGTYPE).get(0);
                }
                switch(zFieldMsgType){
                    case FIX5JonecMsgType.LOGON:
                        mOut = msgTypeLogon(mapInputFields);
                        break;
                    case FIX5JonecMsgType.LOGOUT:
                        mOut = msgTypeLogout(mapInputFields);
                        break;
                    case FIX5JonecMsgType.CHANGE_PASSWORD:
                        mOut = msgTypeChangePassword(mapInputFields);
                        break;
                    case FIX5JonecMsgType.RESEND:
                        mOut = msgTypeResend(mapInputFields);
                        break;
                    case FIX5JonecMsgType.HEART_BEAT:
                        mOut = msgTypeHeartbeat(mapInputFields);
                        break;
                    case FIX5JonecMsgType.TEST_REQUEST:
                        mOut = msgTypeTestRequest(mapInputFields);
                        break;
                    case FIX5JonecMsgType.REJECT:
                        mOut = msgTypeReject(mapInputFields);
                        break;
                    case FIX5JonecMsgType.SEQUENCE_RESET:
                        mOut = msgTypeSequenceReset(mapInputFields);
                        break;
                    case FIX5JonecMsgType.BUSINESS_MESSAGE_REJECT:
                        mOut = msgTypeBusinessMessageReject(mapInputFields);
                        break;
                    case FIX5JonecMsgType.EXECUTION_REPORT:
                        mOut = msgTypeExecutionReport(mapInputFields);
                        break;
                    case FIX5JonecMsgType.MASS_QUOTE:
                        mOut = msgTypeMassQuote(mapInputFields);
                        break;
                    case FIX5JonecMsgType.MASS_QUOTE_ACKNOWLEDGEMENT:
                        mOut = msgTypeMassQuoteAcknowledgement(mapInputFields);
                        break;
                    case FIX5JonecMsgType.ORDER_MASS_ACTION_REQUEST:
                        mOut = msgTypeOrderMassActionRequest(mapInputFields);
                        break;
                    case FIX5JonecMsgType.ORDER_MASS_ACTION_REPORT:
                        mOut = msgTypeOrderMassActionReport(mapInputFields);
                        break;
                    case FIX5JonecMsgType.REQUEST_FOR_POSITION:
                        mOut = msgTypeRequestForPosition(mapInputFields);
                        break;
                    case FIX5JonecMsgType.POSITION_REPORT:
                        mOut = msgTypePositionReport(mapInputFields);
                        break;
                    case FIX5JonecMsgType.TRADE_CAPTURE_REPORT:
                        mOut = msgTypeTradeCaptureReport(mapInputFields);
                        break;
                    case FIX5JonecMsgType.TRADE_CAPTURE_REPORT_ACK:
                        mOut = msgTypeTradeCaptureReportAck(mapInputFields);
                        break;
                    case FIX5JonecMsgType.NEW_ORDER_SINGLE:
                        mOut = msgTypeNewOrderSingle(mapInputFields);
                        break;
                    case FIX5JonecMsgType.ORDER_CANCEL_REJECT:
                        mOut = msgTypeOrderCancelReject(mapInputFields);
                        break;
                    case FIX5JonecMsgType.ORDER_CANCEL_REPLACE_REQUEST:
                        mOut = msgTypeOrderCancelReplaceRequest(mapInputFields);
                        break;
                    case FIX5JonecMsgType.ORDER_CANCEL_REQUEST:
                        mOut = msgTypeOrderCancelRequest(mapInputFields);
                        break;
                    default:
                        //.EXXX.
                        break;
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeLogon(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataLogonReply msg = new FIX5JonecDataLogonReply(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeLogout(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataLogoutReply msg = new FIX5JonecDataLogoutReply(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeChangePassword(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeResend(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataResendRequest msg = new FIX5JonecDataResendRequest(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeHeartbeat(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataHeartbeat msg = new FIX5JonecDataHeartbeat(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeTestRequest(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataTestRequest msg = new FIX5JonecDataTestRequest(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeReject(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataReject msg = new FIX5JonecDataReject(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeSequenceReset(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataSequenceReset msg = new FIX5JonecDataSequenceReset(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeBusinessMessageReject(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataBusinessMessageReject msg = new FIX5JonecDataBusinessMessageReject(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeExecutionReport(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataExecutionReport msg = new FIX5JonecDataExecutionReport(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeMassQuote(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeMassQuoteAcknowledgement(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeOrderMassActionRequest(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeOrderMassActionReport(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeRequestForPosition(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypePositionReport(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeTradeCaptureReport(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataTradeCaptureReport msg = new FIX5JonecDataTradeCaptureReport(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeTradeCaptureReportAck(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataTradeCaptureReportAck msg = new FIX5JonecDataTradeCaptureReportAck(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeNewOrderSingle(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeOrderCancelReject(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                FIX5JonecDataOrderCancelReject msg = new FIX5JonecDataOrderCancelReject(mapInputFields);
                msg.assignMessage();
                //.confirm:
                mOut = msg;
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeOrderCancelReplaceRequest(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private FIX5IDXMessage msgTypeOrderCancelRequest(Map<String, ArrayList<String>> mapInputFields){
        FIX5IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                //.???.
                //.confirm:
                //.???.
                //.selesai.                
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
}
