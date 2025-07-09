/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.message.processor;

import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldFmt;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldTag;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValue;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValueLength;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIMsgType;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeChangePasswordReply;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogonReply;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogoutReply;
import com.itm.idx.data.ori.message.struct.ORIDataErrorMessage;
import com.itm.idx.data.ori.message.struct.ORIDataErrorMessage.ORIErrorMessageType;
import com.itm.idx.data.ori.message.struct.ORIDataLiquidityProviderOrderCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataLiquidityProviderOrderCancelReply.ORILiquidityProviderOrderCancelReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReminder;
import com.itm.idx.data.ori.message.struct.ORIDataNewLiquidityProviderOrderReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderMassCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataTradingInfo;
import com.itm.idx.message.IDXMessage;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealAmendReply;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealAmendReply.ORINegotiationDealAmendReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancelReply.ORINegotiationDealCancelReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReply;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReply.ORINegotiationDealReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrderReply;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrderReply.ORINewOrderReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmendReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmendReply.ORIOrderAmendReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply.ORIOrderCancelReplyType;
import java.util.HashMap;
import java.util.Map;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.helpers.BrokerReferenceHelper;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeChangePassword;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeChangePasswordReply.ORIAdministrativeChangePasswordReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogon;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogout;
import com.itm.idx.data.ori.message.struct.ORIDataHeader;
import com.itm.idx.data.ori.message.struct.ORIDataLiquidityProviderOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancel;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealConfirmedInfo;
import com.itm.idx.data.ori.message.struct.ORIDataNewLiquidityProviderOrder;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderMassCancel;
import com.itm.idx.data.ori.message.struct.ORIDataUnknownMessage;

/**
 *
 * @author aripam
 */
public class ORIMessageProcessor {

    public ORIMessageProcessor() {
    }
    
    public IDXMessage parseMessage(String zInputMessage, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((zInputMessage != null) && (zInputMessage.length() > 0)){
                //.cari awalan dan akhiran:
                //.cari awalan:
                int pStart = zInputMessage.indexOf(ORIFieldFmt.FIND_FIRST_TAG_WITH_NULL);
                if (pStart < 0){
                    pStart = zInputMessage.indexOf(ORIFieldFmt.FIND_FIRST_TAG);
                    if (pStart > 0){
                        //.cari tahu sebelum tag apakah nomor tag atau pembatas:
                        if (Character.isDigit(zInputMessage.charAt(pStart - 1))){
                            pStart = (-1); //.posisi tidak valid.
                        }
                    }
                }else{
                    //.tambah dari awal header tag:
                    pStart = (pStart + 1);
                }
                //.cari akhiran:
                int pStop = zInputMessage.indexOf(ORIFieldFmt.FIND_LAST_TAG);
                boolean useLastTagServerSimulation = false;
                if ((pStop < 0) && (forServerSimulation)){
                    pStop = zInputMessage.indexOf(ORIFieldFmt.FIND_LAST_TAG_SERVER_SIM);
                    if (pStop >= 0){
                        useLastTagServerSimulation = true;
                    }
                }
                //.cek awalan:
                if (pStart >= 0){
                    //.cek akhiran:
                    if (pStop > pStart){
                        //.tambah sampai akhir trailer field value:
                        if (useLastTagServerSimulation){
                            pStop = (pStop + 0);
                        }else{
                            pStop = pStop + ORIFieldFmt.FIND_LAST_TAG.length() + ORIFieldValueLength.CHECKSUM;
                        }
                        //.filter ulang message:
                        String zMessageHeader = "";
                        if (forServerSimulation && (pStart > 0)){
                            zMessageHeader = zInputMessage.substring(0, pStart);
                        }
                        zInputMessage = zInputMessage.substring(pStart, pStop);
                        //.pisah fields:
                        String[] arrFields = StringHelper.splitAll(zInputMessage, ORIFieldFmt.FIELD_SEPARATOR);
                        if ((arrFields != null) && (arrFields.length > 0)){
                            Map<String, String> mapSrcFields = new HashMap<>();
                            for (String zKeyVal : arrFields){
                                String[] arrKeyVal = StringHelper.splitTwo(zKeyVal, ORIFieldFmt.KV_SEPARATOR);
                                if ((arrKeyVal != null) && (arrKeyVal.length >= 2)){
                                    String zKey = arrKeyVal[0];
                                    String zVal = arrKeyVal[1];
                                    //.siap key-value tiap field:
                                    mapSrcFields.put(zKey, zVal);
                                }
                            }
                            //.mapping message:
                            mOut = mappingMessage(mapSrcFields, forServerSimulation);
                            if (mOut == null){
                                mOut = new ORIDataUnknownMessage(mapSrcFields, zInputMessage);
                            }
                        }
                        //.mapping header:
                        if (forServerSimulation && (!StringHelper.isNullOrEmpty(zMessageHeader)) && (mOut != null)){
                            String[] arrHdrFields = StringHelper.splitAll(zMessageHeader, ORIFieldFmt.HEADER_FIELD_SEPARATOR);
                            if ((arrHdrFields != null) && (arrHdrFields.length > 0)){
                                ORIDataHeader mHdr = (ORIDataHeader)mOut;
                                if (arrHdrFields.length >= 1){
                                    mHdr.setfBundleMessageVersion(arrHdrFields[0]);
                                }
                                if (arrHdrFields.length >= 3){
                                    mHdr.setfBundleConnectionName(arrHdrFields[2]);
                                }
                                if (arrHdrFields.length >= 5){
                                    mHdr.setfBundlePosDup(arrHdrFields[4]);
                                }
                            }
                        }
                    }else{
                        //.akhiran tidak ditemukan:
                        //.EXXX.
                    }
                }else{
                    //.awalan tidak ditemukan:
                    //.EXXX.
                }
            }else{
                //.input message kosong:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        if (mOut == null){
            mOut = new ORIDataUnknownMessage(zInputMessage);
        }
        return mOut;
    }
    
    public IDXMessage mappingMessage(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                switch(mapInputFields.get(ORIFieldTag.MSGTYPE)){
                    case ORIMsgType.LOGON:
                        mOut = msgTypeLogon(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.LOGOUT:
                        mOut = msgTypeLogout(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.CHANGE_PASSWORD:
                        mOut = msgTypeChangePassword(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.EXECUTION_REPORT:
                        mOut = msgTypeExecutionReport(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.ORDER_CANCEL_REJECT:
                        mOut = msgTypeOrderCancelReject(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.QUOTE_ACKNOWLEDGEMENT:
                        mOut = msgTypeQuoteAcknowledgement(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.TRADING_INFO:
                        mOut = msgTypeTradingInfo(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.ORDER_MASS_CANCEL_REPLY:
                        mOut = msgTypeOrderMassCancelReply(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.NEGDEAL_REMINDER:
                        mOut = msgTypeNegDealReminder(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.ERROR_MESSAGE:
                        mOut = msgTypeErrorMessage(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.NEW_ORDER: //.unused: request only.
                        //.EXXX.
                        mOut = msgTypeNewOrder(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.ORDER_CANCEL_REQUEST: //.unused: request only.
                        //.EXXX.
                        mOut = msgTypeOrderCancelRequest(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.ORDER_CANCEL_REPLACE: //.unused: request only.
                        //.EXXX.
                        mOut = msgTypeOrderCancelReplace(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.ORDER_MASS_CANCEL_REQUEST: //.unused: request only.
                        //.EXXX.
                        mOut = msgTypeOrderMassCancelRequest(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.QUOTE: //.unused: request only.
                        //.EXXX.
                        mOut = msgTypeQuote(mapInputFields, forServerSimulation);
                        break;
                    case ORIMsgType.QUOTE_CANCEL: //.unused: request only.
                        //.EXXX.
                        mOut = msgTypeQuoteCancel(mapInputFields, forServerSimulation);
                        break;
                    default:
                        //.EXXX.
                        break;
                }
            }else{
                //.input map kosong / kurang:
                //.EXXX.
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    private IDXMessage msgTypeLogon(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    ORIDataAdministrativeLogon msg = new ORIDataAdministrativeLogon(mapInputFields);
                    msg.assignMessage();
                    
                    //.confirm:
                    mOut = msg;
                }else{
                    ORIDataAdministrativeLogonReply msg = new ORIDataAdministrativeLogonReply(mapInputFields);
                    msg.assignMessage();
                    if (mapInputFields.containsKey(ORIFieldTag.LOGONREPLY)){
                        msg.setfAdministrativeLogonReplyType(ORIAdministrativeLogonReplyType.OK2);
                    }else{
                        msg.setfAdministrativeLogonReplyType(ORIAdministrativeLogonReplyType.OK1);
                    }
                    //.confirm:
                    mOut = msg;
                }
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
    
    private IDXMessage msgTypeLogout(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    ORIDataAdministrativeLogout msg = new ORIDataAdministrativeLogout(mapInputFields);
                    msg.assignMessage();
                    
                    //.confirm:
                    mOut = msg;
                }else{
                    ORIDataAdministrativeLogoutReply msg = new ORIDataAdministrativeLogoutReply(mapInputFields);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }
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
    
    private IDXMessage msgTypeChangePassword(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    ORIDataAdministrativeChangePassword msg = new ORIDataAdministrativeChangePassword(mapInputFields);
                    msg.assignMessage();
                    
                    //.confirm:
                    mOut = msg;
                }else{
                    ORIDataAdministrativeChangePasswordReply msg = new ORIDataAdministrativeChangePasswordReply(mapInputFields);
                    msg.assignMessage();
                    msg.setfAdministrativeChangePasswordReplyType(ORIAdministrativeChangePasswordReplyType.BAD); //.default.
                    if (mapInputFields.containsKey(ORIFieldTag.RETURNVALUE)){
                        String zText2Parse = mapInputFields.get(ORIFieldTag.RETURNVALUE);
                        if (!StringHelper.isNullOrEmpty(zText2Parse)){
                            zText2Parse = zText2Parse.toLowerCase();
                            if ((zText2Parse.contains("success")) && (zText2Parse.contains("changed"))){
                                msg.setfAdministrativeChangePasswordReplyType(ORIAdministrativeChangePasswordReplyType.OK);
                            }
                        }
                    }
                    //.confirm:
                    mOut = msg;
                }
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
    
    private IDXMessage msgTypeExecutionReport(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    //... .
                }else{
                    switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.EXECTYPE))){
                        case ORIFieldValue.EXECTYPE_NEW: //[0]=New Order Reply OK//Matched Order Reply//Negotiation Deal Reply OK\Neg. Deal Confirmation OK//Negotiation Deal Confirmed Info//
                            switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.HANDLINST))){
                                case ORIFieldValue.HANDLINST_NORMAL:
                                case ORIFieldValue.HANDLINST_ADVERTISEMENT:
                                    switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.ORDSTATUS))){
                                        case ORIFieldValue.ORDSTATUS_NEW:
                                            ORIDataNewOrderReply msg02a = new ORIDataNewOrderReply(mapInputFields);
                                            msg02a.setfNewOrderReplyType(ORINewOrderReplyType.OK);
                                            msg02a.assignMessage();
                                            //.confirm:
                                            mOut = msg02a;
                                            break;
                                        case ORIFieldValue.ORDSTATUS_PARTIALLY_MATCH:
                                        case ORIFieldValue.ORDSTATUS_FULLY_MATCH:
                                            ORIDataNewOrderReply msg02b = new ORIDataNewOrderReply(mapInputFields);
                                            msg02b.setfNewOrderReplyType(ORINewOrderReplyType.Matched);
                                            msg02b.assignMessage();
                                            //.confirm:
                                            mOut = msg02b;
                                            break;
                                        default:
                                            //.order status type tidak dikenal:
                                            //.EXXX.
                                            break;
                                    }
                                    break;
                                case ORIFieldValue.HANDLINST_NEGOTIATIONDEAL:
                                    switch(mapInputFields.get(ORIFieldTag.HANDLINST)){
                                        case ORIFieldValue.ORDTYPE_PREVIOUSLY_INDICATED:
                                            ORIDataNegotiationDealConfirmedInfo msg03a = new ORIDataNegotiationDealConfirmedInfo(mapInputFields);
                                            msg03a.assignMessage();
                                            //.confirm:
                                            mOut = msg03a;
                                            break;
                                        default:
                                            ORIDataNegotiationDealReply msg03b = new ORIDataNegotiationDealReply(mapInputFields);
                                            if (StringHelper.toInt(mapInputFields.get(ORIFieldTag.ORDSTATUS)) == ORIFieldValue.ORDSTATUS_NEW){
                                                msg03b.setfNegotiationDealReplyType(ORINegotiationDealReplyType.ReplyOK);
                                            }else{
                                                msg03b.setfNegotiationDealReplyType(ORINegotiationDealReplyType.ConfirmationOK);
                                            }
                                            msg03b.assignMessage();
                                            //.confirm:
                                            mOut = msg03b;
                                            break;
                                    }
                                    break;
                                default:
                                    //.handlinst type tidak dikenal:
                                    //.EXXX.
                                    break;
                            }
                            break;
                        case ORIFieldValue.EXECTYPE_CANCELLED: //[4]=Order Cancel Reply OK//Negotiation Deal Cancel Reply OK//Negotiation Deal Canceled Info//
                            switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.HANDLINST))){
                                case ORIFieldValue.HANDLINST_NORMAL:
                                case ORIFieldValue.HANDLINST_ADVERTISEMENT:
                                    ORIDataOrderCancelReply msg42 = new ORIDataOrderCancelReply(mapInputFields);
                                    msg42.setfOrderCancelReplyType(ORIOrderCancelReplyType.OK);
                                    msg42.assignMessage();
                                    //.confirm:
                                    mOut = msg42;
                                    break;
                                case ORIFieldValue.HANDLINST_NEGOTIATIONDEAL:
                                    ORIDataNegotiationDealCancelReply msg43 = new ORIDataNegotiationDealCancelReply(mapInputFields);
                                    switch(mapInputFields.get(ORIFieldTag.HANDLINST)){
                                        case ORIFieldValue.ORDTYPE_PREVIOUSLY_INDICATED:
                                            msg43.setfNegotiationDealCancelReplyType(ORINegotiationDealCancelReplyType.INFO);
                                            break;
                                        default:
                                            msg43.setfNegotiationDealCancelReplyType(ORINegotiationDealCancelReplyType.OK);
                                            break;
                                    }
                                    msg43.assignMessage();
                                    //.confirm:
                                    mOut = msg43;
                                    break;
                                default:
                                    //.handlinst type tidak dikenal:
                                    //.EXXX.
                                    break;
                            }
                            break;
                        case ORIFieldValue.EXECTYPE_REPLACEMENT: //[5]=Order Amend Reply OK//Negotiation Deal Amend Reply OK//Negotiation Deal Amended Info//
                            switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.HANDLINST))){
                                case ORIFieldValue.HANDLINST_NORMAL:
                                case ORIFieldValue.HANDLINST_ADVERTISEMENT:
                                    ORIDataOrderAmendReply msg52 = new ORIDataOrderAmendReply(mapInputFields);
                                    msg52.setfOrderAmendReplyType(ORIOrderAmendReplyType.OK);
                                    msg52.assignMessage();
                                    //.confirm:
                                    mOut = msg52;
                                    break;
                                case ORIFieldValue.HANDLINST_NEGOTIATIONDEAL:
                                    ORIDataNegotiationDealAmendReply msg53 = new ORIDataNegotiationDealAmendReply(mapInputFields);
                                    switch(mapInputFields.get(ORIFieldTag.HANDLINST)){
                                        case ORIFieldValue.ORDTYPE_PREVIOUSLY_INDICATED:
                                            msg53.setfNegotiationDealAmendReplyType(ORINegotiationDealAmendReplyType.INFO);
                                            break;
                                        default:
                                            msg53.setfNegotiationDealAmendReplyType(ORINegotiationDealAmendReplyType.OK);
                                            break;
                                    }
                                    msg53.assignMessage();
                                    //.confirm:
                                    mOut = msg53;
                                    break;
                                default:
                                    //.handlinst type tidak dikenal:
                                    //.EXXX.
                                    break;
                            }
                            break;
                        case ORIFieldValue.EXECTYPE_REJECTED: //[8]=New Order Reply BAD//Negotiation Deal Reply BAD\Neg. Deal Confirmation BAD//
                            switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.HANDLINST))){
                                case ORIFieldValue.HANDLINST_NORMAL:
                                case ORIFieldValue.HANDLINST_ADVERTISEMENT:
                                    ORIDataNewOrderReply msg82 = new ORIDataNewOrderReply(mapInputFields);
                                    msg82.setfNewOrderReplyType(ORINewOrderReplyType.BAD);
                                    msg82.assignMessage();
                                    //.confirm:
                                    mOut = msg82;
                                    break;
                                case ORIFieldValue.HANDLINST_NEGOTIATIONDEAL:
                                    ORIDataNegotiationDealReply msg83 = new ORIDataNegotiationDealReply(mapInputFields);
                                    msg83.setfNegotiationDealReplyType(ORINegotiationDealReplyType.ReplyBAD);
                                    msg83.assignMessage();
                                    //.confirm:
                                    mOut = msg83;
                                    break;
                                default:
                                    //.handlinst type tidak dikenal:
                                    //.EXXX.
                                    break;
                            }
                            break;
                        default:
                            //.execution report type tidak dikenal:
                            //.EXXX.
                            break;
                    }
                }
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
    
    private IDXMessage msgTypeOrderCancelReject(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    //... .
                }else{
                    switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.ORDSTATUS))){
                        case ORIFieldValue.ORDSTATUS_REJECTED:
                            switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.CXLREJRESPONSETO))){
                                case ORIFieldValue.CXLREJECTRESPONSETO_ORDER_REPLACE_REQUEST:
                                    if (mapInputFields.containsKey(ORIFieldTag.SECONDARYORDERID)){
                                        ORIDataNegotiationDealAmendReply msg22 = new ORIDataNegotiationDealAmendReply(mapInputFields);
                                        msg22.setfNegotiationDealAmendReplyType(ORINegotiationDealAmendReplyType.BAD);
                                        msg22.assignMessage();
                                        //.confirm:
                                        mOut = msg22;
                                    }else{
                                        ORIDataOrderAmendReply msg23 = new ORIDataOrderAmendReply(mapInputFields);
                                        msg23.setfOrderAmendReplyType(ORIOrderAmendReplyType.BAD);
                                        msg23.assignMessage();
                                        //.confirm:
                                        mOut = msg23;
                                    }
                                    break;
                                case ORIFieldValue.CXLREJECTRESPONSETO_ORDER_CANCEL_REQUEST:
                                    if (mapInputFields.get(ORIFieldTag.ORDERID).toUpperCase().equals(ORIFieldValue.ORDERID_JATS_ORDERNUMBER_SPECIFY_NONE)){
                                        ORIDataNegotiationDealCancelReply msg12 = new ORIDataNegotiationDealCancelReply(mapInputFields);
                                        msg12.setfNegotiationDealCancelReplyType(ORINegotiationDealCancelReplyType.BAD);
                                        msg12.assignMessage();
                                        //.confirm:
                                        mOut = msg12;
                                    }else{
                                        ORIDataOrderCancelReply msg13 = new ORIDataOrderCancelReply(mapInputFields);
                                        msg13.setfOrderCancelReplyType(ORIOrderCancelReplyType.BAD);
                                        msg13.assignMessage();
                                        //.confirm:
                                        mOut = msg13;
                                    }
                                    break;
                                default:
                                    //.reject response type tidak dikenal:
                                    //.EXXX.
                                    break;
                            }
                            break;
                        default:
                            //.order status tidak dikenal:
                            //.EXXX.
                            break;
                    }
                }
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
    
    private IDXMessage msgTypeQuoteAcknowledgement(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    //... .
                }else{
                    switch(mapInputFields.get(ORIFieldTag.QUOTEACKSTATUS)){
                        case ORIFieldValue.QUOTEACKSTATUS_NEW_ACCEPTED:
                        case ORIFieldValue.QUOTEACKSTATUS_NEW_REJECTED:
                            ORIDataNewLiquidityProviderOrderReply msg1 = new ORIDataNewLiquidityProviderOrderReply(mapInputFields);
                            msg1.assignMessage();
                            //.confirm:
                            mOut = msg1;
                            break;
                        case ORIFieldValue.QUOTEACKSTATUS_CANCEL_FOR_SECURITY:
                        case ORIFieldValue.QUOTEACKSTATUS_CANCEL_FOR_UNDERLYING_SECURITY:
                        case ORIFieldValue.QUOTEACKSTATUS_CANCEL_ALL_QUOTES:
                            ORIDataLiquidityProviderOrderCancelReply msg2 = new ORIDataLiquidityProviderOrderCancelReply(mapInputFields);
                            if (mapInputFields.containsKey(ORIFieldTag.QUOTEREJECTREASON)){
                                msg2.setfLiquidityProviderOrderCancelReplyType(ORILiquidityProviderOrderCancelReplyType.BAD);
                            }else{
                                msg2.setfLiquidityProviderOrderCancelReplyType(ORILiquidityProviderOrderCancelReplyType.OK);
                            }
                            msg2.assignMessage();
                            //.confirm:
                            mOut = msg2;
                            break;
                        default:
                            //.EXXX.
                            break;
                    }
                }
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
    
    private IDXMessage msgTypeTradingInfo(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    //... .
                }else{
                    ORIDataTradingInfo msg = new ORIDataTradingInfo(mapInputFields);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }
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
    
    private IDXMessage msgTypeOrderMassCancelReply(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    //... .
                }else{
                    ORIDataOrderMassCancelReply msg = new ORIDataOrderMassCancelReply(mapInputFields);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }
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
    
    private IDXMessage msgTypeNegDealReminder(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    //... .
                }else{
                    ORIDataNegotiationDealReminder msg = new ORIDataNegotiationDealReminder(mapInputFields);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }
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
    
    private IDXMessage msgTypeErrorMessage(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    //... .
                }else{
                    ORIDataErrorMessage msg = new ORIDataErrorMessage(mapInputFields);
                    switch(StringHelper.toInt(mapInputFields.get(ORIFieldTag.ERRORCODE))){
                        case ORIFieldValue.ERRORCODE_POSSIBLE_DUPLICATE_FIX_MESSAGE:
                            msg.setfErrorMessageType(ORIErrorMessageType.PossibleDuplicateFixMessage);
                            break;
                        case ORIFieldValue.ERRORCODE_UNKNOWN_JONEC_MESSAGE:
                            msg.setfErrorMessageType(ORIErrorMessageType.UnknownJONECMessage);
                            break;
                        case ORIFieldValue.ERRORCODE_JATS_INTERPRETATION_ERROR_REPLY:
                            msg.setfErrorMessageType(ORIErrorMessageType.JATSInterpretationErrorReply);
                            break;
                        case ORIFieldValue.ERRORCODE_OTHERS_KNOWN:
                            msg.setfErrorMessageType(ORIErrorMessageType.OthersKnown);
                            break;
                        default:
                            msg.setfErrorMessageType(ORIErrorMessageType.OthersUnknown);
                            //.EXXX.
                            break;
                    }
                    //.parsing referrcode:
                    String zFindBrokerRef = BrokerReferenceHelper.findBrokerRefFromJatsError(mapInputFields.get(ORIFieldTag.REFERRORCODE));
                    if (StringHelper.isNullOrEmpty(zFindBrokerRef)){
                        zFindBrokerRef = "";
                    }
                    msg.setfBrokerRef(zFindBrokerRef);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }
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
    
    private IDXMessage msgTypeNewOrder(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    if (mapInputFields.get(ORIFieldTag.HANDLINST).equals(StringHelper.fromInt(ORIFieldValue.HANDLINST_NORMAL))){
                        ORIDataNewOrder msg = new ORIDataNewOrder(mapInputFields);
                        msg.assignMessage();
                        //.confirm:
                        mOut = msg;
                    }else if (mapInputFields.get(ORIFieldTag.HANDLINST).equals(StringHelper.fromInt(ORIFieldValue.HANDLINST_NEGOTIATIONDEAL))){
                        ORIDataNegotiationDeal msg = new ORIDataNegotiationDeal(mapInputFields);
                        msg.assignMessage();
                        //.confirm:
                        mOut = msg;
                    }else if (mapInputFields.get(ORIFieldTag.HANDLINST).equals(StringHelper.fromInt(ORIFieldValue.HANDLINST_ADVERTISEMENT))){
                        ORIDataNewOrder msg = new ORIDataNewOrder(mapInputFields);
                        msg.assignMessage();
                        //.confirm:
                        mOut = msg;
                    }else{
                        ORIDataNewOrder msg = new ORIDataNewOrder(mapInputFields);
                        msg.assignMessage();
                        //.confirm:
                        mOut = msg;
                    }
                        
                }else{
                    //... .
                }
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
    
    private IDXMessage msgTypeOrderCancelRequest(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    if (mapInputFields.get(ORIFieldTag.ORDERQTY).equals(StringHelper.fromInt((int)ORIFieldValue.ORDERQTY_WITHDRAW_NEGDEAL))){
                        ORIDataNegotiationDealCancel msg = new ORIDataNegotiationDealCancel(mapInputFields);
                        msg.assignMessage();
                        //.confirm:
                        mOut = msg;
                    }else{
                        ORIDataOrderCancel msg = new ORIDataOrderCancel(mapInputFields);
                        msg.assignMessage();
                        //.confirm:
                        mOut = msg;
                    }
                }else{
                    //... .
                }
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
    
    private IDXMessage msgTypeOrderCancelReplace(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    ORIDataOrderAmend msg = new ORIDataOrderAmend(mapInputFields);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }else{
                    //... .
                }
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
    
    private IDXMessage msgTypeOrderMassCancelRequest(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    ORIDataOrderMassCancel msg = new ORIDataOrderMassCancel(mapInputFields);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }else{
                    //... .
                }
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
    
    private IDXMessage msgTypeQuote(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    ORIDataNewLiquidityProviderOrder msg = new ORIDataNewLiquidityProviderOrder(mapInputFields);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }else{
                    //... .
                }
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
    
    private IDXMessage msgTypeQuoteCancel(Map<String, String> mapInputFields, boolean forServerSimulation){
        IDXMessage mOut = null;
        try{
            if ((mapInputFields != null) && (mapInputFields.size() > 2)){ //.harus ada header + data + trailer.
                //.mapping message:
                if (forServerSimulation){
                    ORIDataLiquidityProviderOrderCancel msg = new ORIDataLiquidityProviderOrderCancel(mapInputFields);
                    msg.assignMessage();
                    //.confirm:
                    mOut = msg;
                }else{
                    //... .
                }
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
