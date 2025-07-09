/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.idx.data.helpers.DateTimeHelper;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.api.ORIAccessAdministrative;
import com.itm.idx.data.api.ORIAccessNegotiationDeal;
import com.itm.idx.data.api.ORIAccessOrder;
import com.itm.idx.data.api.QRIAccess;
import com.itm.idx.data.jonec.consts.JonecConst;
import com.itm.idx.data.message.bridge.IDXBridgeController;
import com.itm.idx.data.message.bridge.IDXBridgeController.IDXGroupMessageType;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValue;
import com.itm.idx.data.qri.consts.QRIDataConst;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author aripam
 */
public class TS_ConsoleRequestTest {
    //.single instance:
    public final static TS_ConsoleRequestTest getInstance = new TS_ConsoleRequestTest();
    
    private IDXBridgeController oriBridgeCtrl1 = new IDXBridgeController("");
    private IDXBridgeController oriBridgeCtrl2 = new IDXBridgeController("");
    private IDXBridgeController qriBridgeCtrl1 = new IDXBridgeController("");
    
    boolean bTSMustRun = true;
    
    private final static String zDefaultPassword    = "Jak@rta130";
    private final static String zDefaultIPAddress   = "192.168.0.3";//"10.0.0.4";//"192.168.0.3";
    private final static int iDefaultPort           = 2000;
    private final static int iDefaultConnectTimeOut = 30000;
    private final static int iDefaultCheckInterval  = 60000;
    
    
    
    private String zORI1UserID          = "shtr1001";
    private String zORI1UserPassword    = zDefaultPassword;
    
    private String zORI2UserID          = "shtr1002";
    private String zORI2UserPassword    = zDefaultPassword;
    
    private String zQRI1UserID          = "shmi1001";
    private String zQRI1UserPassword    = zDefaultPassword;
    
    
    private String zORI1ConnectionName      = JonecConst.DEFAULT_EXEC_BROKER_CODE + "_01_001_toJONES";
    private String zORI1SvrIPAddress        = zDefaultIPAddress; //"192.168.0.3";
    private int iORI1SvrPort                = iDefaultPort; //2000;
    private int iORI1SvrTimeOut             = iDefaultConnectTimeOut;
    private int iORI1CheckInterval          = iDefaultCheckInterval;
    private IDXGroupMessageType ORI1Type    = IDXGroupMessageType.ORI_MESSAGE;
    private RecvRequestTestMsgHandler hORI1 = new RecvRequestTestMsgHandler();
    
    private String zORI2ConnectionName      = JonecConst.DEFAULT_EXEC_BROKER_CODE + "_01_002_toJONES";
    private String zORI2SvrIPAddress        = zDefaultIPAddress; //"192.168.0.3";
    private int iORI2SvrPort                = iDefaultPort; //2000;
    private int iORI2SvrTimeOut             = iDefaultConnectTimeOut;
    private int iORI2CheckInterval          = iDefaultCheckInterval;
    private IDXGroupMessageType ORI2Type    = IDXGroupMessageType.ORI_MESSAGE;
    private RecvRequestTestMsgHandler hORI2 = new RecvRequestTestMsgHandler();
    
    private String zQRI1ConnectionName      = JonecConst.DEFAULT_EXEC_BROKER_CODE + "_01_901_toMARTINS";
    private String zQRI1SvrIPAddress        = zDefaultIPAddress; //"192.168.0.3";
    private int iQRI1SvrPort                = iDefaultPort; //2000;
    private int iQRI1SvrTimeOut             = iDefaultConnectTimeOut;
    private int iQRI1CheckInterval          = iDefaultCheckInterval;
    private IDXGroupMessageType QRI1Type    = IDXGroupMessageType.QRI_MESSAGE;
    private RecvRequestTestMsgHandler hQRI1 = new RecvRequestTestMsgHandler();
    
    
    public IDXBridgeController getOriBridgeCtrl1() {
        return oriBridgeCtrl1;
    }
    
    public IDXBridgeController getOriBridgeCtrl2() {
        return oriBridgeCtrl2;
    }
    
    public IDXBridgeController getQriBridgeCtrl1() {
        return qriBridgeCtrl1;
    }

    public boolean isbTSMustRun() {
        return bTSMustRun;
    }

    public void setbTSMustRun(boolean bTSMustRun) {
        this.bTSMustRun = bTSMustRun;
    }
    
    public void runAsJONECClient(){
        
        oriBridgeCtrl1.setConnectionName(zORI1ConnectionName);
        oriBridgeCtrl1.setRefServerAddress(zORI1SvrIPAddress);
        oriBridgeCtrl1.setRefServerPort(iORI1SvrPort);
        oriBridgeCtrl1.setRefTryConnectTimeOut(iORI1SvrTimeOut);
        oriBridgeCtrl1.setRefCheckInterval(iORI1CheckInterval);
        oriBridgeCtrl1.setMsgGroupType(ORI1Type);
        oriBridgeCtrl1.addBridgeListener(hORI1);
        
        oriBridgeCtrl2.setConnectionName(zORI2ConnectionName);
        oriBridgeCtrl2.setRefServerAddress(zORI2SvrIPAddress);
        oriBridgeCtrl2.setRefServerPort(iORI2SvrPort);
        oriBridgeCtrl2.setRefTryConnectTimeOut(iORI2SvrTimeOut);
        oriBridgeCtrl2.setRefCheckInterval(iORI2CheckInterval);
        oriBridgeCtrl2.setMsgGroupType(ORI2Type);
        oriBridgeCtrl2.addBridgeListener(hORI2);
        
        qriBridgeCtrl1.setConnectionName(zQRI1ConnectionName);
        qriBridgeCtrl1.setRefServerAddress(zQRI1SvrIPAddress);
        qriBridgeCtrl1.setRefServerPort(iQRI1SvrPort);
        qriBridgeCtrl1.setRefTryConnectTimeOut(iQRI1SvrTimeOut);
        qriBridgeCtrl1.setRefCheckInterval(iQRI1CheckInterval);
        qriBridgeCtrl1.setMsgGroupType(QRI1Type);
        qriBridgeCtrl1.addBridgeListener(hQRI1);
        
        
        
        oriBridgeCtrl1.startConnection();
        oriBridgeCtrl2.startConnection();
        qriBridgeCtrl1.startConnection();
        
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        String zInput = "";
        String zOutMsg = "";
        int iRefNewOrderCounter = 0;
        int iRefNewAmendCounter = 0;
        int iRefCancelOrderCounter = 0;
        
        int iRefDealNewOrderCounter = 0;
        int iRefDealAmendCounter = 0;
        int iRefDealCancelOrderCounter = 0;
        
        while(bTSMustRun){
            
            System.out.println();
            System.out.println();
            System.out.println("================================================");
            System.out.println();
            
            //.administrative:
            boolean bIsORIAdmin         = false;
            int iORIConnNumber          = 1;
            String zUserID              = "";
            String zUserPassword        = "";
            String zUserNewPassword     = "";
            
            //.new order:
            String zBoardCode           = "";
            int iHandleInst             = 0;
            String zSide                = "";
            String zStockCode           = "";
            String zSecurityID          = "";
            String zExecInst            = "";
            String zOrdType             = "";
            String zTimeInForce         = "";
            double dPrice               = 0.0;
            long lQuantity              = 0;
            String zAccount             = "";
            String zTransactTime        = "";
            String zNewClOrdID          = "";
            String zComplianceID        = "";
            //.amend order:
            String zToAmendOrderID      = "";
            String zToAmendClOrdID      = ""; //.auto.
            String zNewAmendClOrdID     = "";
            String zAmendTrxTime        = ""; //.auto.
            double dAmendPrice          = 0.0;
            long lAmendQuantity         = 0;
            
            //.cancel:
            String zToCancelClOrdID     = "";
            String zToCancelOrderID     = "";
            String zNewCancelClOrdID    = "";
            String zCancelTrxTime       = "";
            
            //.negdeal:
            int iTmReminderInterval     = 1;
            long lIndicatedOrder        = 0;
            String zCounterpartUserID   = "";
            String zCounterpartAccount  = "";
            String zCounterpartBrokerRef= "";
            String zCounterpartTradingID= "";
            
            int iWithdrawTypeInOrderQty = 0;
            String zSubUserID           = "";
            String zSubBrokerID         = "";
            
            String zSettlementDate          = "";
            String zSettlemetInstruction    = "";
            
            //.mulai:
            System.out.println("REQUEST[ ORI1 -- A1=Login / B1=Logout / C1=ChangePassword ]");
            System.out.println("REQUEST[ ORI2 -- A2=Login / B2=Logout / C2=ChangePassword ]");
            System.out.println("REQUEST[ QRI1 -- A3=Login / B3=Logout / C3=ChangePassword ]");
            System.out.println("REQUEST[ SUBSCRIBE_5 = SS / SUBSCRIBE_TRADE = SS1 / SUBSCRIBE_ORDER = SS2 / SUBSCRIBE_LIMT = SS3 / SUBSCRIBE_SECURITY = SS4  / SUBSCRIBE_NEGO = SS5 / SUBSCRIBE_ADVERTISEMENT = SS6 ]");
            System.out.println("REQUEST[ 1=RegularOrder / 2=AmendRegularOrder / 3=CancelRegularOrder ]");
            System.out.println("REQUEST[ 4=Tunai  Order / 5=AmendTunai  Order / 6=CancelTunai  Order ]");
            System.out.println("REQUEST[ 7=Nego   Order / 8=AmendNego   Order / 9=CancelNego   Order ]:");
            System.out.print  ("REQUEST[ O=NegDealOrder / M=AmendNegDealOrder / C=CancelNegDealOrder ]:");
            
            try {
                zInput = bfr.readLine();
                zInput = zInput.toUpperCase().trim();
            } catch (IOException ex) {}
            if (!StringHelper.isNullOrEmpty(zInput)){
                
                final int REQ_TYPE_LOGIN = 7;
                final int REQ_TYPE_LOGOUT = 8;
                final int REQ_TYPE_CHGPWD = 9;
                final int REQ_TYPE_SUBSCRIBE = 10;
                final int REQ_TYPE_SUBSCRIBE_TRADE = 11;
                final int REQ_TYPE_SUBSCRIBE_ORDER = 12;
                final int REQ_TYPE_SUBSCRIBE_LIMIT = 13;
                final int REQ_TYPE_SUBSCRIBE_SECURITY = 14;
                final int REQ_TYPE_SUBSCRIBE_NEGO = 15;
                final int REQ_TYPE_SUBSCRIBE_ADVERTISEMENT = 16;
                
                final int REQ_TYPE_ORDER = 1;
                final int REQ_TYPE_AMEND = 2;
                final int REQ_TYPE_CANCEL = 3;
                
                final int REQ_TYPE_NEGDEAL_ORDER_TYPE_TWOSIDE = 911;
                final int REQ_TYPE_NEGDEAL_ORDER_TYPE_CROSSING = 912;
                final int REQ_TYPE_NEGDEAL_ORDER_TYPE_CONFIRM = 913;
                
                final int REQ_TYPE_NEGDEAL_ORDER = 901;
                final int REQ_TYPE_NEGDEAL_AMEND = 902;
                final int REQ_TYPE_NEGDEAL_CANCEL = 903;
                
                final int SENDLINE_USER_1 = 1;
                final int SENDLINE_USER_2 = 2;
                
                int pvRequestType = 0;
                int pvSubRequestType = 0;
                
                int pvSendLineUserNumber = SENDLINE_USER_1;
                String zpvSendLine_UserID = "";
                String zpvSendLine_UserPassword = "";
                String zpvSendLine_UserConnection = "";
                
                switch(StringHelper.toInt(zInput)){
                    case 1: //.NewOrder--Regular
                        System.out.println("YOUR REQUEST=New RG Order,...");
                        pvRequestType = REQ_TYPE_ORDER;
                        zBoardCode = "RG";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NORMAL;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("METHOD[ Session=1 / Day=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals("1")) && (!zInput.equals("2"))){
                            System.out.println("ERR=unknown order method [ " + zInput + "]");
                            continue;
                        }
                        if (zInput.equals("1")) { //.session.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION;
                        }
                        if (zInput.equals("2")) { //.day.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY;
                        }
                        System.out.print("PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dPrice = StringHelper.toDouble(zInput);
                        System.out.print("QUANTITY(LOT):");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toLong(zInput) <= 0){
                            System.out.println("ERR=empty quantity [ " + zInput + "]");
                            continue;
                        }
                        lQuantity = StringHelper.toLong(zInput);
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    case 2: //.AmendOrder--Regular
                        System.out.println("YOUR REQUEST=Amend RG Order,...");
                        pvRequestType = REQ_TYPE_AMEND;
                        zBoardCode = "RG";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NORMAL;
                        System.out.print("BrokerRef to Amend:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty brokerRef to amend.");
                            continue;
                        }
                        zToAmendClOrdID = zInput;
                        System.out.print("JATS OrderID to Amend:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty orderid to amend.");
                            continue;
                        }
                        zToAmendOrderID = zInput;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("REPLACEMENT PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dAmendPrice = StringHelper.toDouble(zInput);
                        System.out.print("REPLACEMENT QUANTITY(LOT):");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toLong(zInput) <= 0){
                            System.out.println("ERR=empty quantity [ " + zInput + "]");
                            continue;
                        }
                        lAmendQuantity = StringHelper.toLong(zInput);
                        System.out.print("METHOD[ Session=1 / Day=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals("1")) && (!zInput.equals("2"))){
                            System.out.println("ERR=unknown order method [ " + zInput + "]");
                            continue;
                        }
                        if (zInput.equals("1")) { //.session.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION;
                        }
                        if (zInput.equals("2")) { //.day.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY;
                        }
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    case 3: //.CancelOrder--Regular
                        System.out.println("YOUR REQUEST=Cancel RG Order,...");
                        pvRequestType = REQ_TYPE_CANCEL;
                        zBoardCode = "RG";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NORMAL;
                        System.out.print("BrokerRef to Cancel:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty brokerRef to cancel.");
                            continue;
                        }
                        zToCancelClOrdID = zInput;
                        System.out.print("JATS OrderID to Cancel:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty orderid to cancel.");
                            continue;
                        }
                        zToCancelOrderID = zInput;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dPrice = StringHelper.toDouble(zInput);
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    case 4: //.NewOrder--Tunai
                        System.out.println("YOUR REQUEST=New TN Order,...");
                        pvRequestType = REQ_TYPE_ORDER;
                        zBoardCode = "TN";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NORMAL;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("METHOD[ Session=1 / Day=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals("1")) && (!zInput.equals("2"))){
                            System.out.println("ERR=unknown order method [ " + zInput + "]");
                            continue;
                        }
                        if (zInput.equals("1")) { //.session.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION;
                        }
                        if (zInput.equals("2")) { //.day.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY;
                        }
                        System.out.print("PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dPrice = StringHelper.toDouble(zInput);
                        System.out.print("QUANTITY(LOT):");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toLong(zInput) <= 0){
                            System.out.println("ERR=empty quantity [ " + zInput + "]");
                            continue;
                        }
                        lQuantity = StringHelper.toLong(zInput);
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    case 5: //.AmendOrder--Tunai
                        System.out.println("YOUR REQUEST=Amend TN Order,...");
                        pvRequestType = REQ_TYPE_AMEND;
                        zBoardCode = "TN";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NORMAL;
                        System.out.print("BrokerRef to Amend:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty brokerRef to amend.");
                            continue;
                        }
                        zToAmendClOrdID = zInput;
                        System.out.print("JATS OrderID to Amend:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty orderid to amend.");
                            continue;
                        }
                        zToAmendOrderID = zInput;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("REPLACEMENT PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dAmendPrice = StringHelper.toDouble(zInput);
                        System.out.print("REPLACEMENT QUANTITY(LOT):");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toLong(zInput) <= 0){
                            System.out.println("ERR=empty quantity [ " + zInput + "]");
                            continue;
                        }
                        lAmendQuantity = StringHelper.toLong(zInput);
                        System.out.print("METHOD[ Session=1 / Day=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals("1")) && (!zInput.equals("2"))){
                            System.out.println("ERR=unknown order method [ " + zInput + "]");
                            continue;
                        }
                        if (zInput.equals("1")) { //.session.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION;
                        }
                        if (zInput.equals("2")) { //.day.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY;
                        }
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    case 6: //.CancelOrder--Tunai
                        System.out.println("YOUR REQUEST=Cancel TN Order,...");
                        pvRequestType = REQ_TYPE_CANCEL;
                        zBoardCode = "TN";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NORMAL;
                        System.out.print("BrokerRef to Cancel:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty brokerRef to cancel.");
                            continue;
                        }
                        zToCancelClOrdID = zInput;
                        System.out.print("JATS OrderID to Cancel:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty orderid to cancel.");
                            continue;
                        }
                        zToCancelOrderID = zInput;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dPrice = StringHelper.toDouble(zInput);
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    case 7: //.NewOrder--Negosiasi
                        System.out.println("YOUR REQUEST=New NG Order,...");
                        pvRequestType = REQ_TYPE_ORDER;
                        zBoardCode = "NG";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("METHOD[ Session=1 / Day=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals("1")) && (!zInput.equals("2"))){
                            System.out.println("ERR=unknown order method [ " + zInput + "]");
                            continue;
                        }
                        if (zInput.equals("1")) { //.session.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION;
                        }
                        if (zInput.equals("2")) { //.day.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY;
                        }
                        System.out.print("PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dPrice = StringHelper.toDouble(zInput);
                        System.out.print("QUANTITY(SHARES):");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toLong(zInput) <= 0){
                            System.out.println("ERR=empty quantity [ " + zInput + "]");
                            continue;
                        }
                        lQuantity = StringHelper.toLong(zInput);
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    case 8: //.AmendOrder--Negosiasi
                        System.out.println("YOUR REQUEST=Amend NG Order,...");
                        pvRequestType = REQ_TYPE_AMEND;
                        zBoardCode = "NG";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT;
                        System.out.print("BrokerRef to Amend:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty brokerRef to amend.");
                            continue;
                        }
                        zToAmendClOrdID = zInput;
                        System.out.print("JATS OrderID to Amend:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty orderid to amend.");
                            continue;
                        }
                        zToAmendOrderID = zInput;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("REPLACEMENT PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dAmendPrice = StringHelper.toDouble(zInput);
                        System.out.print("REPLACEMENT QUANTITY(SHARES):");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toLong(zInput) <= 0){
                            System.out.println("ERR=empty quantity [ " + zInput + "]");
                            continue;
                        }
                        lAmendQuantity = StringHelper.toLong(zInput);
                        System.out.print("METHOD[ Session=1 / Day=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals("1")) && (!zInput.equals("2"))){
                            System.out.println("ERR=unknown order method [ " + zInput + "]");
                            continue;
                        }
                        if (zInput.equals("1")) { //.session.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION;
                        }
                        if (zInput.equals("2")) { //.day.
                            zExecInst = ORIDataConst.ORIFieldValue.EXECINST_SPLIT;
                            zOrdType = ORIDataConst.ORIFieldValue.ORDTYPE_LIMIT_NONSTOP;
                            zTimeInForce = ORIDataConst.ORIFieldValue.TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY;
                        }
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    case 9: //.CancelOrder--Negosiasi
                        System.out.println("YOUR REQUEST=Cancel NG Order,...");
                        pvRequestType = REQ_TYPE_CANCEL;
                        zBoardCode = "NG";
                        iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT;
                        System.out.print("BrokerRef to Cancel:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty brokerRef to cancel.");
                            continue;
                        }
                        zToCancelClOrdID = zInput;
                        System.out.print("JATS OrderID to Cancel:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty orderid to cancel.");
                            continue;
                        }
                        zToCancelOrderID = zInput;
                        System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                            System.out.println("ERR=unknown side [ " + zInput + "]");
                            continue;
                        }
                        zSide = zInput;
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                            System.out.println("---BUY---");
                        }
                        if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                            System.out.println("---SELL---");
                        }
                        System.out.print("STOCK:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty stock code.");
                            continue;
                        }
                        zStockCode = zInput;
                        System.out.print("SECURITYID:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.isNullOrEmpty(zInput)){
                            System.out.println("ERR=empty security id.");
                            continue;
                        }else if (StringHelper.toInt(zInput) <= 0){
                            System.out.println("ERR=zero security id.");
                            continue;
                        }
                        zSecurityID = zInput;
                        System.out.print("PRICE:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        if (StringHelper.toDouble(zInput) <= 0){
                            System.out.println("ERR=empty price [ " + zInput + "]");
                            continue;
                        }
                        dPrice = StringHelper.toDouble(zInput);
                        System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.toUpperCase().trim();
                        } catch (IOException ex) {}
                        if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                            System.out.println("ERR=unknown account [ " + zInput + "]");
                            continue;
                        }
                        zAccount = zInput;
                        
                        
                        
                        break;
                    default:
                        if (zInput.equals("A1")){
                            //.request login:
                            pvRequestType = REQ_TYPE_LOGIN;
                            zUserID = zORI1UserID;
                            zUserPassword = zORI1UserPassword;
                            bIsORIAdmin = true;
                            iORIConnNumber = 1;
                            
                        }else if (zInput.equals("B1")){
                            //.request logout:
                            pvRequestType = REQ_TYPE_LOGOUT;
                            zUserID = zORI1UserID;
                            zUserPassword = zORI1UserPassword;
                            bIsORIAdmin = true;
                            iORIConnNumber = 1;
                            
                        }else if (zInput.equals("C1")){
                            //.request change password:
                            pvRequestType = REQ_TYPE_CHGPWD;
                            System.out.print("enter new password:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (zInput.length() <= 0){
                                System.out.println("ERR=empty new password !");
                                continue;
                            }
                            zUserID = zORI1UserID;
                            zUserPassword = zORI1UserPassword;
                            zUserNewPassword = zInput;
                            bIsORIAdmin = true;
                            iORIConnNumber = 1;
                            
                        }else if (zInput.equals("A2")){
                            //.request login:
                            pvRequestType = REQ_TYPE_LOGIN;
                            zUserID = zORI2UserID;
                            zUserPassword = zORI2UserPassword;
                            bIsORIAdmin = true;
                            iORIConnNumber = 2;
                            
                        }else if (zInput.equals("B2")){
                            //.request logout:
                            pvRequestType = REQ_TYPE_LOGOUT;
                            zUserID = zORI2UserID;
                            zUserPassword = zORI2UserPassword;
                            bIsORIAdmin = true;
                            iORIConnNumber = 2;
                            
                        }else if (zInput.equals("C2")){
                            //.request change password:
                            pvRequestType = REQ_TYPE_CHGPWD;
                            System.out.print("enter new password:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (zInput.length() <= 0){
                                System.out.println("ERR=empty new password !");
                                continue;
                            }
                            zUserID = zORI2UserID;
                            zUserPassword = zORI2UserPassword;
                            zUserNewPassword = zInput;
                            bIsORIAdmin = true;
                            iORIConnNumber = 2;
                            
                        }else if (zInput.equals("A3")){
                            //.request login:
                            pvRequestType = REQ_TYPE_LOGIN;
                            zUserID = zQRI1UserID;
                            zUserPassword = zQRI1UserPassword;
                            bIsORIAdmin = false;
                            iORIConnNumber = 0;
                            
                        }else if (zInput.equals("B3")){
                            //.request logout:
                            pvRequestType = REQ_TYPE_LOGOUT;
                            zUserID = zQRI1UserID;
                            zUserPassword = zQRI1UserPassword;
                            bIsORIAdmin = false;
                            iORIConnNumber = 0;
                            
                        }else if (zInput.equals("C3")){
                            //.request change password:
                            pvRequestType = REQ_TYPE_CHGPWD;
                            System.out.print("enter new password:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (zInput.length() <= 0){
                                System.out.println("ERR=empty new password !");
                                continue;
                            }
                            zUserID = zQRI1UserID;
                            zUserPassword = zQRI1UserPassword;
                            zUserNewPassword = zInput;
                            bIsORIAdmin = false;
                            iORIConnNumber = 0;
                        }else if (zInput.equals("O")){
                            //.negdeal-order:
                            pvRequestType = REQ_TYPE_NEGDEAL_ORDER;
                            System.out.print("DEAL-TYPE[ TwoSide=1 / Crossing=2 / Confirm=3 ]:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.toUpperCase().trim();
                            } catch (IOException ex) {}
                            if (zInput.equals("1")){
                                pvSubRequestType = REQ_TYPE_NEGDEAL_ORDER_TYPE_TWOSIDE;
                                System.out.println("---NEG-DEAL-TWO-SIDE---");
                            }else if (zInput.equals("2")){
                                pvSubRequestType = REQ_TYPE_NEGDEAL_ORDER_TYPE_CROSSING;
                                System.out.println("---NEG-DEAL-CROSSING---");
                            }else if (zInput.equals("3")){
                                pvSubRequestType = REQ_TYPE_NEGDEAL_ORDER_TYPE_CONFIRM;
                                System.out.println("---NEG-DEAL-CONFIRM---");
                            }else{
                                System.out.println("ERR=unknown negdeal type [ " + zInput + "]");
                                continue;
                            }
                            //.field statis:
                            zBoardCode = "NG";
                            iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NEGOTIATIONDEAL;
                            iTmReminderInterval = 1;
                            lIndicatedOrder = 0; //.dipakai untuk confirm.
                            //.field tetap:
                            System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                                System.out.println("ERR=unknown side [ " + zInput + "]");
                                continue;
                            }
                            zSide = zInput;
                            if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                                System.out.println("---BUY---");
                            }
                            if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                                System.out.println("---SELL---");
                            }
                            System.out.print("STOCK:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.toUpperCase().trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty stock code.");
                                continue;
                            }
                            zStockCode = zInput;
                            System.out.print("SECURITYID:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty security id.");
                                continue;
                            }else if (StringHelper.toInt(zInput) <= 0){
                                System.out.println("ERR=zero security id.");
                                continue;
                            }
                            zSecurityID = zInput;
                            System.out.print("PRICE:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.toDouble(zInput) <= 0){
                                System.out.println("ERR=empty price [ " + zInput + "]");
                                continue;
                            }
                            dPrice = StringHelper.toDouble(zInput);
                            System.out.print("QUANTITY(UNIT):");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.toLong(zInput) <= 0){
                                System.out.println("ERR=empty quantity [ " + zInput + "]");
                                continue;
                            }
                            lQuantity = StringHelper.toLong(zInput);
                            System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.toUpperCase().trim();
                            } catch (IOException ex) {}
                            if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                                System.out.println("ERR=unknown account [ " + zInput + "]");
                                continue;
                            }
                            zAccount = zInput;
                            System.out.print("ComplianceID(SID):");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            zComplianceID = zInput;
                            System.out.print("Counterpart USERID:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty counterpart userid.");
                                continue;
                            }
                            zCounterpartUserID = zInput;
                            
                            //.pengisian untuk field TEXT:
                            switch(pvSubRequestType){
                                case REQ_TYPE_NEGDEAL_ORDER_TYPE_TWOSIDE:
                                    //.hanya counterpart userid.
                                    break;
                                case REQ_TYPE_NEGDEAL_ORDER_TYPE_CROSSING:
                                    System.out.print("Counterpart BROKERREF:");
                                    try {
                                        zInput = bfr.readLine();
                                        zInput = zInput.toUpperCase().trim();
                                    } catch (IOException ex) {}
                                    if (StringHelper.isNullOrEmpty(zInput)){
                                        System.out.println("ERR=empty counterpart brokerref.");
                                        continue;
                                    }
                                    zCounterpartBrokerRef = zInput;
                                    System.out.print("Counterpart ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                                    try {
                                        zInput = bfr.readLine();
                                        zInput = zInput.toUpperCase().trim();
                                    } catch (IOException ex) {}
                                    if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                                        System.out.println("ERR=unknown counterpart account [ " + zInput + "]");
                                        continue;
                                    }
                                    zCounterpartAccount = zInput;
                                    System.out.print("Counterpart TradingID / ComplianceID(SID):");
                                    try {
                                        zInput = bfr.readLine();
                                        zInput = zInput.trim();
                                    } catch (IOException ex) {}
                                    zCounterpartTradingID = zInput;
                                    break;
                                case REQ_TYPE_NEGDEAL_ORDER_TYPE_CONFIRM:
                                    System.out.print("ORDER NO To CONFIRM:");
                                    try {
                                        zInput = bfr.readLine();
                                        zInput = zInput.trim();
                                    } catch (IOException ex) {}
                                    if (StringHelper.toLong(zInput) <= 0){
                                        System.out.println("ERR=empty order no [ " + zInput + "]");
                                        continue;
                                    }
                                    lIndicatedOrder = StringHelper.toLong(zInput);
                                    break;
                                default:
                                    continue;
                            }
                        }else if (zInput.equals("M")){
                            //.negdeal-amend:
                            pvRequestType = REQ_TYPE_NEGDEAL_AMEND;
                            //.zOutMsg = oriDealAmend.amendTwoSide(zToAmendClOrdID*, zNewAmendClOrdID*, zToAmendOrderID*, zAccount*, iHandleInst*, zSide*, zStockCode*, dAmendPrice*, lAmendQuantity*, zAmendTrxTime*, zCounterpartUserID*, iTmReminderInterval*, zComplianceID*);
                            //.field statis:
                            zBoardCode = "NG";
                            iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NEGOTIATIONDEAL;
                            iTmReminderInterval = 1;
                            //.field tetap:
                            System.out.print("BrokerRef to Amend:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty brokerRef to amend.");
                                continue;
                            }
                            zToAmendClOrdID = zInput;
                            System.out.print("JATS OrderID to Amend:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty orderid to amend.");
                                continue;
                            }
                            zToAmendOrderID = zInput;
                            System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                                System.out.println("ERR=unknown side [ " + zInput + "]");
                                continue;
                            }
                            zSide = zInput;
                            System.out.print("STOCK:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.toUpperCase().trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty stock code.");
                                continue;
                            }
                            zStockCode = zInput;
                            System.out.print("REPLACEMENT PRICE:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.toDouble(zInput) <= 0){
                                System.out.println("ERR=empty price [ " + zInput + "]");
                                continue;
                            }
                            dAmendPrice = StringHelper.toDouble(zInput);
                            System.out.print("REPLACEMENT QUANTITY(UNIT):");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.toLong(zInput) <= 0){
                                System.out.println("ERR=empty quantity [ " + zInput + "]");
                                continue;
                            }
                            lAmendQuantity = StringHelper.toLong(zInput);
                            System.out.print("ACCOUNT[ Indonesia=I / Asing=A / Sendiri=S / ???=F ]:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.toUpperCase().trim();
                            } catch (IOException ex) {}
                            if ((!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_I)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_A)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_S)) && (!zInput.equals(ORIDataConst.ORIFieldValue.ACCOUNT_F))){
                                System.out.println("ERR=unknown account [ " + zInput + "]");
                                continue;
                            }
                            zAccount = zInput;
                            System.out.print("Counterpart USERID:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty counterpart userid.");
                                continue;
                            }
                            zCounterpartUserID = zInput;
                            System.out.print("ComplianceID(SID):");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            zComplianceID = zInput;
                            
                        }else if (zInput.equals("C")){
                            //.negdeal-cancel:
                            pvRequestType = REQ_TYPE_NEGDEAL_CANCEL;
                            //.zOutMsg = oriDealCancel.cancel(zToCancelClOrdID*, zNewCancelClOrdID*, iWithdrawTypeInOrderQty*, zToCancelOrderID*, zSide*, zStockCode*, zCancelTrxTime*, zSubUserID*, zSubBrokerID*);
                            //.field statis:
                            zBoardCode = "NG";
                            iHandleInst = ORIDataConst.ORIFieldValue.HANDLINST_NEGOTIATIONDEAL;
                            iTmReminderInterval = 1;
                            //.field tetap:
                            System.out.print("BrokerRef to Cancel:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty brokerRef to cancel.");
                                continue;
                            }
                            zToCancelClOrdID = zInput;
                            System.out.print("JATS OrderID to Cancel:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty orderid to cancel.");
                                continue;
                            }
                            zToCancelOrderID = zInput;
                            System.out.print("SIDE[ Buy=1 / Sell=2 ]:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if ((!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)) && (!zInput.equals(ORIDataConst.ORIFieldValue.SIDE_SELL))){
                                System.out.println("ERR=unknown side [ " + zInput + "]");
                                continue;
                            }
                            zSide = zInput;
                            if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_BUY)){
                                System.out.println("---BUY---");
                            }
                            if (zSide.equals(ORIDataConst.ORIFieldValue.SIDE_SELL)){
                                System.out.println("---SELL---");
                            }
                            System.out.print("STOCK:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.toUpperCase().trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty stock code.");
                                continue;
                            }
                            zStockCode = zInput;
                            iWithdrawTypeInOrderQty = ORIDataConst.ORIFieldValue.HANDLINST_ADVERTISEMENT;
                            System.out.print("WITHDRAWTYPE[ Order=1 / NegDeal=2 / 3 ] :");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if ((StringHelper.toInt(zInput) < 1) || (StringHelper.toInt(zInput) > 3)){
                                System.out.println("ERR=unknown withdraw type [ " + zInput + "]");
                                continue;
                            }
                            iWithdrawTypeInOrderQty = StringHelper.toInt(zInput);
                            System.out.print("Sub UserID to Cancel:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty sub userid to cancel.");
                                continue;
                            }
                            zSubUserID = zInput;
                            System.out.print("Sub BrokerID to Cancel:");
                            try {
                                zInput = bfr.readLine();
                                zInput = zInput.trim();
                            } catch (IOException ex) {}
                            if (StringHelper.isNullOrEmpty(zInput)){
                                System.out.println("ERR=empty sub brokerid to cancel.");
                                continue;
                            }
                            zSubBrokerID = zInput;
                            
                            
                            
                            
                        }else if (zInput.equals("SS")){
                            //.subscribe martin:
                            pvRequestType = REQ_TYPE_SUBSCRIBE;
                            
                        }else if (zInput.equals("SS1")){
                            //.subscribe martin:
                            pvRequestType = REQ_TYPE_SUBSCRIBE_TRADE;
                        }else if (zInput.equals("SS2")){
                            //.subscribe martin:
                            pvRequestType = REQ_TYPE_SUBSCRIBE_ORDER;
                        }else if (zInput.equals("SS3")){
                            //.subscribe martin:
                            pvRequestType = REQ_TYPE_SUBSCRIBE_LIMIT;
                        }else if (zInput.equals("SS4")){
                            //.subscribe martin:
                            pvRequestType = REQ_TYPE_SUBSCRIBE_SECURITY;
                        }else if (zInput.equals("SS5")){
                            //.subscribe martin:
                            pvRequestType = REQ_TYPE_SUBSCRIBE_NEGO;   
                        }else if (zInput.equals("SS6")){
                            //.subscribe martin:
                            pvRequestType = REQ_TYPE_SUBSCRIBE_ADVERTISEMENT;    

                        }else{
                            System.out.println("ERR=request not valid [ " + zInput + " ]");
                            continue;
                        }
                }
                switch(pvRequestType){
                    case REQ_TYPE_ORDER:
                        System.out.print("[Enter] to continue send new order:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        zTransactTime = DateTimeHelper.getDateTimeIDXTRXFormat();
                        zNewClOrdID = ++iRefNewOrderCounter + ";1;APM";
                        //.buat message order:
                        ORIAccessOrder oriOrder = new ORIAccessOrder(zORI1UserID, zORI1UserPassword, zORI1ConnectionName);
                        zOutMsg = oriOrder.order(iHandleInst, zSide, zStockCode, zSecurityID, zBoardCode, dPrice, lQuantity, zNewClOrdID, zAccount, zTransactTime, zExecInst, zOrdType, zTimeInForce, ORIFieldValue.COMPLIANCEID_NONE);
                        
                        //.kirim mesage order:
                        System.err.println(zOutMsg);
                        oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        break;
                    case REQ_TYPE_AMEND:
                        System.out.print("[Enter] to continue send amend order:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        zAmendTrxTime = DateTimeHelper.getDateTimeIDXTRXFormat();
                        zNewAmendClOrdID = ++iRefNewAmendCounter + ";2;APM";
                        //.buat message amend:
                        ORIAccessOrder oriAmend = new ORIAccessOrder(zORI1UserID, zORI1UserPassword, zORI1ConnectionName);
                        zOutMsg = oriAmend.amend(zToAmendClOrdID, zNewAmendClOrdID, zToAmendOrderID, zAccount, iHandleInst, zSide, zStockCode, zSecurityID, dAmendPrice, lAmendQuantity, zAmendTrxTime, zOrdType, zTimeInForce, ORIFieldValue.COMPLIANCEID_NONE);
                        
                        //.kirim mesage amend:
                        System.err.println(zOutMsg);
                        oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        break;
                    case REQ_TYPE_CANCEL:
                        System.out.print("[Enter] to continue send cancel order:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        zCancelTrxTime = DateTimeHelper.getDateTimeIDXTRXFormat();
                        zNewCancelClOrdID = ++iRefCancelOrderCounter + ";3;APM";
                        
                        //.buat message cancel:
                        ORIAccessOrder oriCancel = new ORIAccessOrder(zORI1UserID, zORI1UserPassword, zORI1ConnectionName);
                        zOutMsg = oriCancel.cancel(zToCancelClOrdID, zNewCancelClOrdID, iHandleInst, zToCancelOrderID, zSide, zStockCode, zSecurityID, zBoardCode, dPrice, zAccount, zCancelTrxTime);
                        
                        //.kirim mesage cancel:
                        System.err.println(zOutMsg);
                        oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        break;
                    case REQ_TYPE_NEGDEAL_ORDER:
                        System.out.print("LINE[ " + zORI1UserID + "=1 / " + zORI2UserID + "=2 ] :");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                            pvSendLineUserNumber = StringHelper.toInt(zInput);
                        } catch (IOException ex) {}
                        if (pvSendLineUserNumber == SENDLINE_USER_2){
                            zpvSendLine_UserID = zORI2UserID;
                            zpvSendLine_UserPassword = zORI2UserPassword;
                            zpvSendLine_UserConnection = zORI2ConnectionName;
                        }else{
                            zpvSendLine_UserID = zORI1UserID;
                            zpvSendLine_UserPassword = zORI1UserPassword;
                            zpvSendLine_UserConnection = zORI1ConnectionName;
                        }
                        System.out.print("[Enter] to continue send new deal order:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        zTransactTime = DateTimeHelper.getDateTimeIDXTRXFormat();
                        zNewClOrdID = ++iRefDealNewOrderCounter + ";D1;APM";
                        //.buat message order:
                        ORIAccessNegotiationDeal oriDealOrder = new ORIAccessNegotiationDeal(zpvSendLine_UserID, zpvSendLine_UserPassword, zpvSendLine_UserConnection);
                        switch(pvSubRequestType){
                            case REQ_TYPE_NEGDEAL_ORDER_TYPE_TWOSIDE:
                                zOutMsg = oriDealOrder.orderTwoSide(iHandleInst, zSide, zStockCode, zSecurityID, zBoardCode, dPrice, lQuantity, zNewClOrdID, zAccount, zTransactTime, lIndicatedOrder, zCounterpartUserID, iTmReminderInterval, zComplianceID, zSettlementDate, zSettlemetInstruction);
                                break;
                            case REQ_TYPE_NEGDEAL_ORDER_TYPE_CROSSING:
                                zOutMsg = oriDealOrder.orderCrossing(iHandleInst, zSide, zStockCode, zSecurityID, zBoardCode, dPrice, lQuantity, zNewClOrdID, zAccount, zTransactTime, lIndicatedOrder, zCounterpartUserID, zCounterpartAccount, zCounterpartBrokerRef, zCounterpartTradingID, iTmReminderInterval, zComplianceID, zSettlementDate, zSettlemetInstruction);
                                break;
                            case REQ_TYPE_NEGDEAL_ORDER_TYPE_CONFIRM:
                                zOutMsg = oriDealOrder.orderConfirm(iHandleInst, zSide, zStockCode, zSecurityID, zBoardCode, dPrice, lQuantity, zNewClOrdID, zAccount, zTransactTime, lIndicatedOrder, zComplianceID);
                                break;
                            default:
                                continue;
                        }
                        //.kirim mesage order:
                        System.err.println(zOutMsg);
                        if (pvSendLineUserNumber == SENDLINE_USER_2){
                            oriBridgeCtrl2.sendMessageDirect(zOutMsg);
                        }else{
                            oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        }
                        break;
                    case REQ_TYPE_NEGDEAL_AMEND:
                        System.out.print("LINE[ " + zORI1UserID + "=1 / " + zORI2UserID + "=2 ] :");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                            pvSendLineUserNumber = StringHelper.toInt(zInput);
                        } catch (IOException ex) {}
                        if (pvSendLineUserNumber == SENDLINE_USER_2){
                            zpvSendLine_UserID = zORI2UserID;
                            zpvSendLine_UserPassword = zORI2UserPassword;
                            zpvSendLine_UserConnection = zORI2ConnectionName;
                        }else{
                            zpvSendLine_UserID = zORI1UserID;
                            zpvSendLine_UserPassword = zORI1UserPassword;
                            zpvSendLine_UserConnection = zORI1ConnectionName;
                        }
                        System.out.print("[Enter] to continue send amend deal order:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        zAmendTrxTime = DateTimeHelper.getDateTimeIDXTRXFormat();
                        zNewAmendClOrdID = ++iRefDealAmendCounter + ";D2;APM";
                        pvSubRequestType = REQ_TYPE_NEGDEAL_ORDER_TYPE_TWOSIDE;
                        //.buat message order:
                        ORIAccessNegotiationDeal oriDealAmend = new ORIAccessNegotiationDeal(zpvSendLine_UserID, zpvSendLine_UserPassword, zpvSendLine_UserConnection);
                        switch(pvSubRequestType){
                            case REQ_TYPE_NEGDEAL_ORDER_TYPE_TWOSIDE:
                                zOutMsg = oriDealAmend.amendTwoSide(zToAmendClOrdID, zNewAmendClOrdID, zToAmendOrderID, zAccount, iHandleInst, zSide, zStockCode, dAmendPrice, lAmendQuantity, zAmendTrxTime, zCounterpartUserID, iTmReminderInterval, zComplianceID, zSettlementDate, zSettlemetInstruction);
                                break;
                            default:
                                continue;
                        }
                        //.kirim mesage order:
                        System.err.println(zOutMsg);
                        if (pvSendLineUserNumber == SENDLINE_USER_2){
                            oriBridgeCtrl2.sendMessageDirect(zOutMsg);
                        }else{
                            oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        }
                        break;
                    case REQ_TYPE_NEGDEAL_CANCEL:
                        System.out.print("LINE[ " + zORI1UserID + "=1 / " + zORI2UserID + "=2 ] :");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                            pvSendLineUserNumber = StringHelper.toInt(zInput);
                        } catch (IOException ex) {}
                        if (pvSendLineUserNumber == SENDLINE_USER_2){
                            zpvSendLine_UserID = zORI2UserID;
                            zpvSendLine_UserPassword = zORI2UserPassword;
                            zpvSendLine_UserConnection = zORI2ConnectionName;
                        }else{
                            zpvSendLine_UserID = zORI1UserID;
                            zpvSendLine_UserPassword = zORI1UserPassword;
                            zpvSendLine_UserConnection = zORI1ConnectionName;
                        }
                        System.out.print("[Enter] to continue send cancel deal order:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        zCancelTrxTime = DateTimeHelper.getDateTimeIDXTRXFormat();
                        zNewCancelClOrdID = ++iRefDealCancelOrderCounter + ";D3;APM";
                        //.buat message order:
                        ORIAccessNegotiationDeal oriDealCancel = new ORIAccessNegotiationDeal(zpvSendLine_UserID, zpvSendLine_UserPassword, zpvSendLine_UserConnection);
                        zOutMsg = oriDealCancel.cancel(zToCancelClOrdID, zNewCancelClOrdID, iWithdrawTypeInOrderQty, zToCancelOrderID, zSide, zStockCode, zCancelTrxTime, zSubUserID, zSubBrokerID);
                        //.kirim mesage order:
                        System.err.println(zOutMsg);
                        if (pvSendLineUserNumber == SENDLINE_USER_2){
                            oriBridgeCtrl2.sendMessageDirect(zOutMsg);
                        }else{
                            oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        }
                        break;
                    case REQ_TYPE_LOGIN:
                        System.out.print("[Enter] to continue send login:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message login:
                        if (bIsORIAdmin) {
                            if (iORIConnNumber == 1){
                                ORIAccessAdministrative oriAdmin = new ORIAccessAdministrative(zUserID, zUserPassword, zORI1ConnectionName);
                                zOutMsg = oriAdmin.login();
                            }else{
                                ORIAccessAdministrative oriAdmin = new ORIAccessAdministrative(zUserID, zUserPassword, zORI2ConnectionName);
                                zOutMsg = oriAdmin.login();
                            }
                        }else{
                            QRIAccess qriAdmin = new QRIAccess(zUserID, zUserPassword, zQRI1ConnectionName);
                            zOutMsg = qriAdmin.login();
                        }
                        
                        //.kirim mesage login:
                        System.err.println(zOutMsg);
                        if (bIsORIAdmin) {
                            if (iORIConnNumber == 1){
                                oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                            }else{
                                oriBridgeCtrl2.sendMessageDirect(zOutMsg);
                            }
                        }else{
                            qriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        }
                        break;
                    case REQ_TYPE_LOGOUT:
                        System.out.print("[Enter] to continue send logout:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message logout:
                        if (bIsORIAdmin) {
                            if (iORIConnNumber == 1){
                                ORIAccessAdministrative oriAdmin = new ORIAccessAdministrative(zUserID, zUserPassword, zORI1ConnectionName);
                                zOutMsg = oriAdmin.logout();
                            }else{
                                ORIAccessAdministrative oriAdmin = new ORIAccessAdministrative(zUserID, zUserPassword, zORI2ConnectionName);
                                zOutMsg = oriAdmin.logout();
                            }
                        }else{
                            QRIAccess qriAdmin = new QRIAccess(zUserID, zUserPassword, zQRI1ConnectionName);
                            zOutMsg = qriAdmin.logout();
                        }
                        
                        //.kirim mesage logout:
                        System.err.println(zOutMsg);
                        if (bIsORIAdmin) {
                            if (iORIConnNumber == 1){
                                oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                            }else{
                                oriBridgeCtrl2.sendMessageDirect(zOutMsg);
                            }
                        }else{
                            qriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        }
                        break;
                    case REQ_TYPE_CHGPWD:
                        System.out.print("[Enter] to continue send change password:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message changepassword:
                        if (bIsORIAdmin) {
                            if (iORIConnNumber == 1){
                                ORIAccessAdministrative oriAdmin = new ORIAccessAdministrative(zUserID, zUserPassword, zORI1ConnectionName);
                                zOutMsg = oriAdmin.changePassword(zUserNewPassword);
                            }else{
                                ORIAccessAdministrative oriAdmin = new ORIAccessAdministrative(zUserID, zUserPassword, zORI2ConnectionName);
                                zOutMsg = oriAdmin.changePassword(zUserNewPassword);
                            }
                        }else{
                            QRIAccess qriAdmin = new QRIAccess(zUserID, zUserPassword, zQRI1ConnectionName);
                            zOutMsg = qriAdmin.changePassword(zUserNewPassword);
                        }
                        
                        //.kirim mesage changepassword:
                        System.err.println(zOutMsg);
                        if (bIsORIAdmin) {
                            if (iORIConnNumber == 1){
                                oriBridgeCtrl1.sendMessageDirect(zOutMsg);
                            }else{
                                oriBridgeCtrl2.sendMessageDirect(zOutMsg);
                            }
                        }else{
                            qriBridgeCtrl1.sendMessageDirect(zOutMsg);
                        }
                        break;
                    case REQ_TYPE_SUBSCRIBE:
                        System.out.print("[Enter] to continue send subscribes:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message:
                        QRIAccess qriAccess = new QRIAccess(zQRI1UserID, zQRI1UserPassword, zQRI1ConnectionName);
                        //.kirim mesage:
                        System.err.println(qriAccess.subscribeOrder(0));
                        System.err.println(qriAccess.subscribeTrade(0));
                        System.err.println(qriAccess.subscribeSecurity(0, QRIDataConst.SymbolSfx.BoardRG));
                        System.err.println(qriAccess.subscribeNego(0));
                        System.err.println(qriAccess.subscribeTradingLimit(0, JonecConst.DEFAULT_EXEC_BROKER_CODE));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeOrder(0));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeTrade(0));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeSecurity(0, QRIDataConst.SymbolSfx.BoardRG));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeNego(0));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeTradingLimit(0, JonecConst.DEFAULT_EXEC_BROKER_CODE));
                        break;
                    case REQ_TYPE_SUBSCRIBE_TRADE:
                        System.out.print("[Enter] to continue send subscribe trade list:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message:
                        qriAccess = new QRIAccess(zQRI1UserID, zQRI1UserPassword, zQRI1ConnectionName);
                        //.kirim mesage:
                        System.err.println(qriAccess.subscribeTrade(0));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeTrade(0));
                        break;
                    case REQ_TYPE_SUBSCRIBE_ORDER:
                        System.out.print("[Enter] to continue send subscribe order list:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message:
                        qriAccess = new QRIAccess(zQRI1UserID, zQRI1UserPassword, zQRI1ConnectionName);
                        //.kirim mesage:
                        System.err.println(qriAccess.subscribeOrder(0));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeOrder(0));
                        break;
                    case REQ_TYPE_SUBSCRIBE_LIMIT:
                        System.out.print("[Enter] to continue send subscribe trading limit:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message:
                        qriAccess = new QRIAccess(zQRI1UserID, zQRI1UserPassword, zQRI1ConnectionName);
                        //.kirim mesage:
                        System.err.println(qriAccess.subscribeTradingLimit(0, JonecConst.DEFAULT_EXEC_BROKER_CODE));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeTradingLimit(0, JonecConst.DEFAULT_EXEC_BROKER_CODE));
                        break;
                    case REQ_TYPE_SUBSCRIBE_SECURITY:
                        System.out.print("[Enter] to continue send subscribe security:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message:
                        qriAccess = new QRIAccess(zQRI1UserID, zQRI1UserPassword, zQRI1ConnectionName);
                        //.kirim mesage:
                        
                        //.RG:
//                        System.err.println(qriAccess.subscribeSecurity(0, QRIDataConst.SymbolSfx.BoardRG));
//                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeSecurity(0, QRIDataConst.SymbolSfx.BoardRG));
                        
                        //.NG:
                        System.err.println(qriAccess.subscribeSecurity(0, QRIDataConst.SymbolSfx.BoardNG));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeSecurity(0, QRIDataConst.SymbolSfx.BoardNG));
                        
                        break;
                    case REQ_TYPE_SUBSCRIBE_NEGO:
                        System.out.print("[Enter] to continue send subscribe nego:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message:
                        qriAccess = new QRIAccess(zQRI1UserID, zQRI1UserPassword, zQRI1ConnectionName);
                        //.kirim mesage:
                        System.err.println(qriAccess.subscribeNego(0));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeNego(0));
                        break;
                    case REQ_TYPE_SUBSCRIBE_ADVERTISEMENT:
                        System.out.print("[Enter] to continue send subscribe advertisement:");
                        try {
                            zInput = bfr.readLine();
                            zInput = zInput.trim();
                        } catch (IOException ex) {}
                        //.buat message:
                        qriAccess = new QRIAccess(zQRI1UserID, zQRI1UserPassword, zQRI1ConnectionName);
                        //.kirim mesage:
                        System.err.println(qriAccess.subscribeAdvertising(0, "448", QRIDataConst.SymbolSfx.BoardNG));
                        qriBridgeCtrl1.sendMessageDirect(qriAccess.subscribeAdvertising(0, "448", QRIDataConst.SymbolSfx.BoardNG));
                        break;
                    default:
                        System.out.println("ERR=request type not valid [" + pvRequestType + "]");
                        break;
                }
                
            }else{
                System.out.println("ERR=request empty.");
            }
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                
            }
        }
        
        oriBridgeCtrl1.stopConnection();
        oriBridgeCtrl2.stopConnection();
        qriBridgeCtrl1.stopConnection();
        
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
//        ORIAccessNegotiationDeal msg = new ORIAccessNegotiationDeal("MyUserID", "MyUserPassword", "MyUserConnectionName");
//        String zOut = msg.orderTwoSide(123, "side", "stockCode", "securityID", "boardCode", 456, 789, "brokerRef", "account", "transactionTime", 111, "counterpartUserID", 222, "complianceID");
//        System.err.println(zOut);
//        
        TS_ConsoleRequestTest.getInstance.runAsJONECClient();
        
    }
}
