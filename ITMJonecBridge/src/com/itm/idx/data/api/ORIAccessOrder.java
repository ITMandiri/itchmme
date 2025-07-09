/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.api;

import com.itm.idx.data.helpers.IDXConverterHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmend;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancel;
import com.itm.idx.data.ori.message.struct.ORIDataOrderMassCancel;

/**
 *
 * @author aripam
 */
public class ORIAccessOrder {
    private String fUserID                                      = "";
    private String fPassword                                    = "";
    private String fConnectionName                              = "";
    
    public ORIAccessOrder(String zUserID, String zPassword, String zConnectionName) {
        this.fUserID = zUserID;
        this.fPassword = zPassword;
        this.fConnectionName = zConnectionName;
    }
    
    public String order(int iHandleInst, String zSide, String zStockCode, String zSecurityID, String zBoardCode, 
        double dPrice, long lQuantity, String zBrokerRef, String zAccount, String zTransactionTime,
        String zMethodExecInst, String zMethodOrderType, String zMethodTimeInForce,
        String zComplianceID){
        ORIDataNewOrder msg = new ORIDataNewOrder(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfClientID(fUserID);
        msg.setfHandlInst(iHandleInst); //.advertisement order.//
        msg.setfSide(zSide); //.buy.//
        msg.setfSymbol(zStockCode);//
        msg.setfSecurityID(zSecurityID);//
        msg.setfSymbolSfx(zBoardCode);//
        msg.setfPrice(dPrice);//
        msg.setfOrderQty(lQuantity);//
        msg.setfClOrdID(zBrokerRef);
        msg.setfAccount(zAccount);//
        msg.setfTransactTime(zTransactionTime);
        msg.setfExecInst(zMethodExecInst); //.di Order Method Table.//
        msg.setfOrdType(zMethodOrderType); //.di Order Method Table.//
        msg.setfTimeInForce(zMethodTimeInForce); //.di Order Method Table.//
        msg.setfStopPx(0);//.Required for OrdType = "Stop" or OrdType = "Stop limit".
        msg.setfExpireDate("0");//
        msg.setfText(ORIDataConst.ORIFieldValue.EMPTY_STRING);//
        msg.setfClearingAccount(ORIDataConst.ORIFieldValue.SINGLE_SPACE);//
        msg.setfComplianceID(IDXConverterHelper.fromSIDToComplianceTradingID(zComplianceID));//
        //... .
        return msg.msgToString();
    }
    
    //. 2024-06-09 : Tambahan field text
    public String order(int iHandleInst, String zSide, String zStockCode, String zSecurityID, String zBoardCode, 
        double dPrice, long lQuantity, String zBrokerRef, String zAccount, String zTransactionTime,
        String zMethodExecInst, String zMethodOrderType, String zMethodTimeInForce,
        String zComplianceID, String zText){
        ORIDataNewOrder msg = new ORIDataNewOrder(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfClientID(fUserID);
        msg.setfHandlInst(iHandleInst); //.advertisement order.//
        msg.setfSide(zSide); //.buy.//
        msg.setfSymbol(zStockCode);//
        msg.setfSecurityID(zSecurityID);//
        msg.setfSymbolSfx(zBoardCode);//
        msg.setfPrice(dPrice);//
        msg.setfOrderQty(lQuantity);//
        msg.setfClOrdID(zBrokerRef);
        msg.setfAccount(zAccount);//
        msg.setfTransactTime(zTransactionTime);
        msg.setfExecInst(zMethodExecInst); //.di Order Method Table.//
        msg.setfOrdType(zMethodOrderType); //.di Order Method Table.//
        msg.setfTimeInForce(zMethodTimeInForce); //.di Order Method Table.//
        msg.setfStopPx(0);//.Required for OrdType = "Stop" or OrdType = "Stop limit".
        msg.setfExpireDate("0");//
        msg.setfText(zText);//. digunakan untuk ordersource di ouch
        msg.setfClearingAccount(ORIDataConst.ORIFieldValue.SINGLE_SPACE);//
        msg.setfComplianceID(IDXConverterHelper.fromSIDToComplianceTradingID(zComplianceID));//
        //... .
        return msg.msgToString();
    }
    
    public String amend(String zBrokerRefToAmend, String zReplacementBrokerRef, String zOrderIDToAmend,
        String zAccount, int iHandleInst, String zSide, String zStockCode, String zSecurityID, 
        double dReplacementPrice, long lReplacementQuantity, String zTransactionTime, 
        String zMethodOrderType, String zMethodTimeInForce,
        String zComplianceID){
        ORIDataOrderAmend msg = new ORIDataOrderAmend(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfOrderID(zOrderIDToAmend);
        msg.setfClientID(fUserID);
        msg.setfOrigClOrdID(zBrokerRefToAmend);
        msg.setfClOrdID(zReplacementBrokerRef);
        msg.setfAccount(zAccount);
        msg.setfHandlInst(iHandleInst);
        msg.setfSymbol(zStockCode);
        msg.setfSide(zSide);
        msg.setfTransactTime(zTransactionTime);
        msg.setfOrderQty(lReplacementQuantity);
        msg.setfOrdType(zMethodOrderType);
        msg.setfPrice(dReplacementPrice);
        msg.setfTimeInForce(zMethodTimeInForce);
        msg.setfExpireDate("0");
        msg.setfText(ORIDataConst.ORIFieldValue.EMPTY_STRING);
        msg.setfClearingAccount(ORIDataConst.ORIFieldValue.SINGLE_SPACE);
        msg.setfComplianceID(IDXConverterHelper.fromSIDToComplianceTradingID(zComplianceID));
        //... .
        return msg.msgToString();
    }
    
    public String cancel(String zBrokerRefToCancel, String zBrokerRef, int zHandleInstInOrderQty, String zOrderIDToCancel,
        String zSide, String zStockCode, String zSecurityID, String zBoardCode, double dPrice, String zAccount, String zCancelTransactionTime){
        ORIDataOrderCancel msg = new ORIDataOrderCancel(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfOrigClOrdID(zBrokerRefToCancel);
        msg.setfClOrdID(zBrokerRef);
        msg.setfOrderQty(zHandleInstInOrderQty);
        msg.setfOrderID(zOrderIDToCancel);
        msg.setfClientID(fUserID);
        msg.setfSide(zSide);
        msg.setfSymbol(zStockCode);
        msg.setfSecurityID(zSecurityID);
        msg.setfSymbolSfx(zBoardCode);
        msg.setSfPrice(dPrice);
        msg.setfAccount(zAccount);
        msg.setfTransactTime(zCancelTransactionTime);
        //... .
        return msg.msgToString();
    }
    
    public String cancel(String zBrokerRefToCancel, String zBrokerRef, int iHandleInstInOrderQty, String zOrderIDToCancel,
        String zSide, String zStockCode, String zSecurityID, String zBoardCode, double dPrice, String zAccount, String zOrderToCancelTransactionTime,
        String zSubUserID, String zSubBrokerID){
        ORIDataOrderCancel msg = new ORIDataOrderCancel(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfOrigClOrdID(zBrokerRefToCancel);
        msg.setfClOrdID(zBrokerRef);
        msg.setfOrderQty(iHandleInstInOrderQty);
        msg.setfOrderID(zOrderIDToCancel);
        msg.setfClientID(fUserID);
        msg.setfSide(zSide);
        msg.setfSymbol(zStockCode);
        msg.setfSecurityID(zSecurityID);
        msg.setfSymbolSfx(zBoardCode);
        msg.setSfPrice(dPrice);
        msg.setfAccount(zAccount);
        msg.setfTransactTime(zOrderToCancelTransactionTime);
        //... .
        msg.setSfUserID(zSubUserID);
        msg.setSfBrokerID(zSubBrokerID);
        //... .
        return msg.msgToString();
    }
    
    public String massCancel(String zMassCancelBrokerRef, String zMassCancelScopeType, String zSecurityCode, String zBoardCode, String zSecurityID){
        ORIDataOrderMassCancel msg = new ORIDataOrderMassCancel(null);
        msg.setfBundleConnectionName(fConnectionName);
        msg.setfClOrdID(zMassCancelBrokerRef);
        msg.setfMassWithdrawRequestType(zMassCancelScopeType);
        msg.setfSymbol(zSecurityCode);
        msg.setfSymbolSfx(zBoardCode);
        msg.setfSecurityID(zSecurityID);
        msg.setfTradingSessionID("0");
        msg.setfSide("0");
        //... .
        //.permintaan tambahan:
        msg.setfClientID(fUserID);
        //... .
        return msg.msgToString();
    }
    
}
