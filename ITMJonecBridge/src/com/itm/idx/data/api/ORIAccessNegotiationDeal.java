/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.api;

import com.itm.idx.data.helpers.IDXConverterHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDeal.ORINegotiationDealType;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealAmend;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealAmend.ORINegotiationDealAmendType;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancel;

/**
 *
 * @author aripam
 */
public class ORIAccessNegotiationDeal {
    private String fUserID                                      = "";
    private String fPassword                                    = "";
    private String fConnectionName                              = "";
    
    public ORIAccessNegotiationDeal(String zUserID, String zPassword, String zConnectionName) {
        this.fUserID = zUserID;
        this.fPassword = zPassword;
        this.fConnectionName = zConnectionName;
    }
    
    public String orderTwoSide(int iHandleInst, String zSide, String zStockCode, String zSecurityID, String zBoardCode, 
        double dPrice, long lQuantity, String zBrokerRef, String zAccount, String zTransactionTime,
        long lIndicatedOrder, String zSubCounterpartUserID, int iSubReminderTimeInterval,
        String zComplianceID
        , String zSettlementDate
        , String zSettlementInstruction
    ) {
        ORIDataNegotiationDeal msg = new ORIDataNegotiationDeal(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfNegotiationDealType(ORINegotiationDealType.TwoSide);
        msg.setfClientID(fUserID);
        msg.setfHandlInst(iHandleInst); //.negotiation order.//
        msg.setfSide(zSide); //.buy.//
        msg.setfSymbol(zStockCode);//
        msg.setfSecurityID(zSecurityID);//
        msg.setfSymbolSfx(zBoardCode);//
        msg.setfPrice(dPrice);//
        msg.setfOrderQty(lQuantity);//
        msg.setfClOrdID(zBrokerRef);
        msg.setfAccount(zAccount);//
        msg.setfTransactTime(zTransactionTime);
        msg.setfIOIId(lIndicatedOrder);
        msg.setfOrdType(ORIDataConst.ORIFieldValue.ORDTYPE_PREVIOUSLY_INDICATED); //.previously indicated.//
        msg.setfClearingAccount(ORIDataConst.ORIFieldValue.SINGLE_SPACE);//
        msg.setfComplianceID(IDXConverterHelper.fromSIDToComplianceTradingID(zComplianceID));//
        msg.setSfCounterpartUserID(zSubCounterpartUserID); //.different from current.
        msg.setSfReminderTimeInterval(iSubReminderTimeInterval);
        msg.setfSettlDate(zSettlementDate);
        msg.setfSettlDeliveryType(zSettlementInstruction);
        //... .
        return msg.msgToString();
    }
    
    public String orderCrossing(int iHandleInst, String zSide, String zStockCode, String zSecurityID, String zBoardCode, 
        double dPrice, long lQuantity, String zBrokerRef, String zAccount, String zTransactionTime,
        long lIndicatedOrder, String zSubCounterpartUserID, 
        String zSubCounterpartAccount, String zSubCounterpartBrokerReference, String zSubCounterpartTradingID, 
        int iSubReminderTimeInterval,
        String zComplianceID
        , String zSettlementDate
        , String zSettlementInstruction
    ) {
        ORIDataNegotiationDeal msg = new ORIDataNegotiationDeal(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfNegotiationDealType(ORINegotiationDealType.Crossing);
        msg.setfClientID(fUserID);
        msg.setfHandlInst(iHandleInst); //.negotiation order.//
        msg.setfSide(zSide); //.buy.//
        msg.setfSymbol(zStockCode);//
        msg.setfSecurityID(zSecurityID);//
        msg.setfSymbolSfx(zBoardCode);//
        msg.setfPrice(dPrice);//
        msg.setfOrderQty(lQuantity);//
        msg.setfClOrdID(zBrokerRef);
        msg.setfAccount(zAccount);//
        msg.setfTransactTime(zTransactionTime);
        msg.setfIOIId(lIndicatedOrder);
        msg.setfOrdType(ORIDataConst.ORIFieldValue.ORDTYPE_PREVIOUSLY_INDICATED); //.previously indicated.//
        msg.setfClearingAccount(ORIDataConst.ORIFieldValue.SINGLE_SPACE);//
        msg.setfComplianceID(IDXConverterHelper.fromSIDToComplianceTradingID(zComplianceID));//
        msg.setSfCounterpartUserID(zSubCounterpartUserID); //.same as current.
        msg.setSfCounterpartAccount(zSubCounterpartAccount);
        msg.setSfCounterpartBrokerReference(zSubCounterpartBrokerReference);
        msg.setSfCounterpartTradingID(zSubCounterpartTradingID);
        msg.setSfReminderTimeInterval(iSubReminderTimeInterval);
        msg.setfSettlDate(zSettlementDate);
        msg.setfSettlDeliveryType(zSettlementInstruction);
        //... .
        return msg.msgToString();
    }
    
    public String orderConfirm(int iHandleInst, String zSide, String zStockCode, String zSecurityID, String zBoardCode, 
        double dPrice, long lQuantity, String zBrokerRef, String zAccount, String zTransactionTime,
        long lIndicatedOrderNoToConfirm,
        String zComplianceID
    ) {
        ORIDataNegotiationDeal msg = new ORIDataNegotiationDeal(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfNegotiationDealType(ORINegotiationDealType.Confirmation);
        msg.setfClientID(fUserID);
        msg.setfHandlInst(iHandleInst); //.negotiation order.//
        msg.setfSide(zSide); //.buy.//
        msg.setfSymbol(zStockCode);//
        msg.setfSecurityID(zSecurityID);//
        msg.setfSymbolSfx(zBoardCode);//
        msg.setfPrice(dPrice);//
        msg.setfOrderQty(lQuantity);//
        msg.setfClOrdID(zBrokerRef);
        msg.setfAccount(zAccount);//
        msg.setfTransactTime(zTransactionTime);
        msg.setfIOIId(lIndicatedOrderNoToConfirm);
        msg.setfOrdType(ORIDataConst.ORIFieldValue.ORDTYPE_PREVIOUSLY_INDICATED); //.previously indicated.//
        msg.setfClearingAccount(ORIDataConst.ORIFieldValue.SINGLE_SPACE);//
        msg.setfComplianceID(IDXConverterHelper.fromSIDToComplianceTradingID(zComplianceID));//
        //... .
        return msg.msgToString();
    }
    
    public String amendTwoSide(String zBrokerRefToAmend, String zReplacementBrokerRef, String zOrderIDToAmend,
        String zAccount, int iHandleInst, String zSide, String zStockCode,
        double dReplacementPrice, long lReplacementQuantity, String zTransactionTime,
        String zSubCounterpartUserID, int iSubReminderTimeInterval,
        String zComplianceID
        , String zSettlementDate
        , String zSettlementInstruction
    ) {
        ORIDataNegotiationDealAmend msg = new ORIDataNegotiationDealAmend(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfNegotiationDealAmendType(ORINegotiationDealAmendType.TwoSide);
        msg.setfOrderID(zOrderIDToAmend); //
        msg.setfClientID(fUserID); //
        msg.setfOrigClOrdID(zBrokerRefToAmend); //
        msg.setfClOrdID(zReplacementBrokerRef); //
        msg.setfAccount(zAccount); //
        msg.setfHandlInst(iHandleInst); //
        msg.setfSymbol(zStockCode); //
        msg.setfSide(zSide); //
        msg.setfTransactTime(zTransactionTime); //
        msg.setfOrderQty(lReplacementQuantity); //
        msg.setfOrdType(ORIDataConst.ORIFieldValue.ORDTYPE_PREVIOUSLY_INDICATED); //
        msg.setfPrice(dReplacementPrice); //
        msg.setSfCounterpartUserID(zSubCounterpartUserID); ////
        msg.setSfReminderTimeInterval(iSubReminderTimeInterval); ////
        msg.setfClearingAccount(ORIDataConst.ORIFieldValue.SINGLE_SPACE); //
        msg.setfComplianceID(IDXConverterHelper.fromSIDToComplianceTradingID(zComplianceID)); //
        //... .
        msg.setfSettlDate(zSettlementDate);
        msg.setfSettlDeliveryType(zSettlementInstruction);
        //... .
        return msg.msgToString();
    }
    
    public String cancel(String zBrokerRefToCancel, String zBrokerRef, int iWithdrawingTypeInOrderQty, String zOrderIDToCancel,
        String zSide, String zStockCode, String zCancelTransactionTime,
        String zSubUserID, String zSubBrokerID) {
        ORIDataNegotiationDealCancel msg = new ORIDataNegotiationDealCancel(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfOrigClOrdID(zBrokerRefToCancel);
        msg.setfClOrdID(zBrokerRef);
        msg.setfOrderQty(iWithdrawingTypeInOrderQty);
        msg.setfOrderID(zOrderIDToCancel);
        msg.setfClientID(fUserID);
        msg.setfSide(zSide);
        msg.setfSymbol(zStockCode);
        msg.setfTransactTime(zCancelTransactionTime);
        msg.setSfUserID(zSubUserID);
        msg.setSfBrokerID(zSubBrokerID);
        //... .
        return msg.msgToString();
    }
    
    
}
