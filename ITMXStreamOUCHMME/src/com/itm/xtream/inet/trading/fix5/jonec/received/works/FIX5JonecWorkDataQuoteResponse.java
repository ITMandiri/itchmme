/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.fix5.jonec.received.works;

import com.itm.fix5.data.helpers.FIX5DateTimeHelper;
import com.itm.fix5.data.jonec.consts.FIX5JonecDataConst;
import com.itm.fix5.data.jonec.message.struct.FIX5JonecDataQuoteResponse;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.data.qri.consts.QRIDataConst;
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.jonec.server.books.BookOfJONECSimToken;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimEveryRequest;
import com.itm.xtream.inet.trading.jonec.server.books.SheetOfJONECSimOriginRequest;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINNegDealList;
import java.util.HashMap;

/**
 *
 * @author fredy
 */
public class FIX5JonecWorkDataQuoteResponse {
    public final static FIX5JonecWorkDataQuoteResponse getInstance = new FIX5JonecWorkDataQuoteResponse();
    
    public FIX5JonecWorkDataQuoteResponse() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public void doWork(ITMSocketChannel channel, FIX5IDXBridgeController controller, FIX5JonecDataQuoteResponse mInputMsgRequest){
        try {
            //....
            String zTraderID = "";
            String zBrokerID = "";
            if (mInputMsgRequest.getfPartyRole1().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                zTraderID = mInputMsgRequest.getfPartyID1(); //.SHJFE1
            }
            if (mInputMsgRequest.getfPartyRole2().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                zTraderID = mInputMsgRequest.getfPartyID2();
            }
            if (mInputMsgRequest.getfPartyRole3().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_12_EXECUTING_TRADER)){
                zTraderID = mInputMsgRequest.getfPartyID3();
            }
            if (mInputMsgRequest.getfPartyRole1().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                zBrokerID = mInputMsgRequest.getfPartyID1(); //.SH
            }
            if (mInputMsgRequest.getfPartyRole2().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                zBrokerID = mInputMsgRequest.getfPartyID2();
            }
            if (mInputMsgRequest.getfPartyRole3().equalsIgnoreCase(FIX5JonecDataConst.FIX5JonecFieldValue.PARTY_ROLE_1_EXECUTING_FIRM)){
                zBrokerID = mInputMsgRequest.getfPartyID3();
            }


            if (StringHelper.isNullOrEmpty(zTraderID)){
                zTraderID = (!StringHelper.isNullOrEmpty(zBrokerID) ? (zBrokerID.toLowerCase()) : "" );
            }
            if ((StringHelper.isNullOrEmpty(zBrokerID)) && (!StringHelper.isNullOrEmpty(zTraderID))){
                zBrokerID = (zTraderID.length() > 2 ? (zTraderID.substring(0, 2).toUpperCase()) : zTraderID.toUpperCase() );
            }
                
            QRIDataNegDealListMessage mNegDealListMsg = new QRIDataNegDealListMessage(new HashMap());
            
            mNegDealListMsg.setfReportType(QRIDataConst.ReportType.QUOTE_RESPONSE.value);
            mNegDealListMsg.setfOrderID(mInputMsgRequest.getfQuoteRespID());
            mNegDealListMsg.setfClOrdID(mInputMsgRequest.getfQuoteID()); 

            mNegDealListMsg.setfClientID(zTraderID);
            mNegDealListMsg.setfExecBroker(zBrokerID);
            mNegDealListMsg.setfContraBroker(zBrokerID);
            mNegDealListMsg.setfContraTrader(zTraderID);

            mNegDealListMsg.setfNoContraBrokers(1);
            mNegDealListMsg.setfExecID(StringHelper.toInt(FIX5DateTimeHelper.getServerIDXTimeExecReportStrFromFIX5UTCFormatDetail(StringHelper.isNullOrEmpty(mInputMsgRequest.getfTransactTime()) ? mInputMsgRequest.getfSendingTime() : mInputMsgRequest.getfTransactTime())));
            mNegDealListMsg.setfExecTransType(QRIDataConst.QRIFieldValue.EXECTRANSTYPE_STATUS);
            mNegDealListMsg.setfExecType(QRIDataConst.QRIFieldValue.EXECTYPE_NEW);
            mNegDealListMsg.setfOrdStatus(QRIDataConst.NegDealStatus.UNCONFIRMED_DEAL.getValue());
            mNegDealListMsg.setfAccount(ORIDataConst.ORIFieldValue.ACCOUNT_I);
            mNegDealListMsg.setfFutSettDate(mInputMsgRequest.getfValidUntilTime());
            mNegDealListMsg.setfSettlDeliveryType(mInputMsgRequest.getfSettlMethod());
            mNegDealListMsg.setfSymbol(mInputMsgRequest.getfSymbol());
            mNegDealListMsg.setfSymbolSfx("0");
            mNegDealListMsg.setfSecurityID(mInputMsgRequest.getfSecurityID());
            mNegDealListMsg.setfSide(mInputMsgRequest.getfSide().equals(ORIDataConst.ORIFieldValue.SIDE_BUY) ? FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_BUY : FIX5JonecDataConst.FIX5JonecFieldValue.SIDE_SELL);
            mNegDealListMsg.setfPrice(StringHelper.toLong(mInputMsgRequest.getfPrice()));
            mNegDealListMsg.setfEffectiveTime(FIX5DateTimeHelper.getServerIDXDateTimeStrFromFIX5UTCFormatDetail(StringHelper.isNullOrEmpty(mInputMsgRequest.getfTransactTime()) ? mInputMsgRequest.getfSendingTime() : mInputMsgRequest.getfTransactTime()));
            mNegDealListMsg.setfLastPx(0);
            mNegDealListMsg.setfLeavesQty(0);
            mNegDealListMsg.setfCumQty(0);
            mNegDealListMsg.setfAvgPx(0);
            mNegDealListMsg.setfText("");
            mNegDealListMsg.setfClearingAccount(" ");
            mNegDealListMsg.setfComplianceID("");
            mNegDealListMsg.setfOrderQty(StringHelper.toLong(mInputMsgRequest.getfOrderQty()));

            //. save orderlist ke memory martin
            BookOfMARTINNegDealList.getInstance.addOrUpdateSheet(mNegDealListMsg);
            //. broadcast orderlist via martin
            BookOfMARTINNegDealList.getInstance.brodcastToSubscriber(mNegDealListMsg);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
}
