/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIFieldValue;
import com.itm.idx.data.ori.message.processor.ORIMessageProcessor;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeChangePasswordReply;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogonReply;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogoutReply;
import com.itm.idx.data.ori.message.struct.ORIDataErrorMessage;
import com.itm.idx.data.ori.message.struct.ORIDataLiquidityProviderOrderCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealAmendReply;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealConfirmedInfo;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReminder;
import com.itm.idx.data.ori.message.struct.ORIDataNegotiationDealReply;
import com.itm.idx.data.ori.message.struct.ORIDataNewLiquidityProviderOrderReply;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrderReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderAmendReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataOrderMassCancelReply;
import com.itm.idx.data.ori.message.struct.ORIDataTradingInfo;
import com.itm.idx.data.ori.message.struct.ORIDataUnknownMessage;
import com.itm.idx.message.IDXMessage;

/**
 *
 * @author aripam
 */
public class ORIMessageReplyBroadcast {
    //.single instance:
    public final static ORIMessageReplyBroadcast getInstance = new ORIMessageReplyBroadcast();
    
    private ORIMessageProcessor oriMapper = new ORIMessageProcessor();
    
    public ORIMessageReplyBroadcast() {
        //.EXXX.
    }
    
    
    
    
    public void ReviewJONECReplyMessage(String zInputMessage){
        //.test:
        try{
            IDXMessage oriMsg = oriMapper.parseMessage(zInputMessage, false);
            if (oriMsg == null){
                System.out.println("REPLY=Null\t" + zInputMessage);
                
            }else if (oriMsg instanceof ORIDataAdministrativeLogonReply){
                System.out.println("REPLY=ORIDataAdministrativeLogonReply\t" + zInputMessage);
                ORIDataAdministrativeLogonReply oriDefMsg = (ORIDataAdministrativeLogonReply)oriMsg;
                System.out.println(oriDefMsg.getfAdministrativeLogonReplyType());
                if (oriDefMsg.getfAdministrativeLogonReplyType().equals(ORIAdministrativeLogonReplyType.OK2)){
                    switch (StringHelper.toInt(oriDefMsg.getfLogonReply())) {
                        case ORIFieldValue.ADMIN_LOGON_REPLY_PWD_HAS_EXPIRED:
                            //.password expire:
                            System.out.println("WARNING=PasswordExpired, disconnectNow");
                            TS_ConnectionTest.getInstance.getOriRecvSocket().stopConnection();
                            break;
                        case ORIFieldValue.ADMIN_LOGON_REPLY_PWD_NEAR_EXPIRE:
                            //.password near expire:
                            System.out.println("WARNING=PasswordNearExpire, Password-change-required (should change password before order entry)");
                            break;
                        default:   
                            
                            //.test tambahan di sini:
//////
//////                        //.+coba order:
//////                        ORIDataNewOrder onew1 = new ORIDataNewOrder(null);
//////                        onew1.setfMsgType(ORIDataConst.ORIMsgType.NEW_ORDER);//
//////                        onew1.setfClOrdID("A1;1;1-APM");
//////                        onew1.setfClientID("lstr1001");
//////                        onew1.setfAccount("I");//
//////                        onew1.setfHandlInst(2); //.advertisement order.//
//////                        onew1.setfExecInst(""); //.di Order Method Table.//
//////                        onew1.setfSymbol("ASRI");//
//////                        onew1.setfSymbolSfx("0RG");//
//////                        onew1.setfSecurityID("448");//
//////                        onew1.setfSide("1"); //.buy.//
//////                        onew1.setfTransactTime("20140224-12:30:00");
//////                        onew1.setfOrderQty(1);//
//////                        onew1.setfOrdType("7"); //.di Order Method Table.//
//////                        onew1.setfPrice(1111);//
//////                        onew1.setfStopPx(0);//
//////                        onew1.setfTimeInForce("0"); //.di Order Method Table.//
//////                        onew1.setfExpireDate("0");//
//////                        onew1.setfText("");//
//////                        onew1.setfClearingAccount("");//
//////                        onew1.setfComplianceID("0");//
//////                        System.out.println("SEND=\t" + onew1.msgToString());
//////                        TS_ConnectionTest.getInstance.getOriRecvSocket().sendToMessageQueue(onew1.msgToString());
//////
//////                        
//////                        //.+coba order:
//////                        ORIDataNewOrder onew2 = new ORIDataNewOrder(null);
//////                        onew2.setfMsgType(ORIDataConst.ORIMsgType.NEW_ORDER);
//////                        onew2.setfClOrdID("B1;1;2-APM");
//////                        onew2.setfClientID("lstr1001");
//////                        onew2.setfAccount("I");
//////                        onew2.setfHandlInst(1); //.normal order.
//////                        onew2.setfExecInst(""); //.di Order Method Table.
//////                        onew2.setfSymbol("ANTM");
//////                        onew2.setfSymbolSfx("0RG");
//////                        onew2.setfSecurityID("448");
//////                        onew2.setfSide("1"); //.buy.
//////                        onew2.setfTransactTime("20140224-12:40:00");
//////                        onew2.setfOrderQty(2);
//////                        onew2.setfOrdType("7"); //.di Order Method Table.
//////                        onew2.setfPrice(2222);
//////                        onew2.setfStopPx(0);
//////                        onew2.setfTimeInForce("S"); //.di Order Method Table.
//////                        onew2.setfExpireDate("0");
//////                        onew2.setfText("");
//////                        onew2.setfClearingAccount("");
//////                        onew2.setfComplianceID("0");
//////                        System.out.println("SEND=\t" + onew2.msgToString());
//////                        TS_ConnectionTest.getInstance.getOriRecvSocket().sendToMessageQueue(onew2.msgToString());
//////
//////                        
//////                        //.+coba order:
//////                        ORIDataNewOrder onew3 = new ORIDataNewOrder(null);
//////                        onew3.setfMsgType(ORIDataConst.ORIMsgType.NEW_ORDER);
//////                        onew3.setfClOrdID("C1;1;3-APM");
//////                        onew3.setfClientID("lstr1001");
//////                        onew3.setfAccount("I");
//////                        onew3.setfHandlInst(1); //.normal order.
//////                        onew3.setfExecInst(""); //.di Order Method Table.
//////                        onew3.setfSymbol("ANTM");
//////                        onew3.setfSymbolSfx("0RG");
//////                        onew3.setfSecurityID("448");
//////                        onew3.setfSide("1"); //.buy.
//////                        onew3.setfTransactTime("20140224-12:50:00");
//////                        onew3.setfOrderQty(3);
//////                        onew3.setfOrdType("7"); //.di Order Method Table.
//////                        onew3.setfPrice(1055);
//////                        onew3.setfStopPx(0);
//////                        onew3.setfTimeInForce("S"); //.di Order Method Table.
//////                        onew3.setfExpireDate("0");
//////                        onew3.setfText("");
//////                        onew3.setfClearingAccount("");
//////                        onew3.setfComplianceID("0");
//////                        System.out.println("SEND=\t" + onew3.msgToString());
//////                        TS_ConnectionTest.getInstance.getOriRecvSocket().sendToMessageQueue(onew3.msgToString());
//////
                            break;
                    }
                }
            }else if (oriMsg instanceof ORIDataAdministrativeChangePasswordReply){
                System.out.println("REPLY=ORIDataAdministrativeChangePasswordReply\t" + zInputMessage);
                ORIDataAdministrativeChangePasswordReply oriDefMsg = (ORIDataAdministrativeChangePasswordReply)oriMsg;
                System.out.println(oriDefMsg.getfAdministrativeChangePasswordReplyType());
            }else if (oriMsg instanceof ORIDataAdministrativeLogoutReply){
                System.out.println("REPLY=ORIDataAdministrativeLogoutReply\t" + zInputMessage);
                ORIDataAdministrativeLogoutReply oriDefMsg = (ORIDataAdministrativeLogoutReply)oriMsg;
                
            }else if (oriMsg instanceof ORIDataErrorMessage){
                System.out.println("REPLY=ORIDataErrorMessage\t" + zInputMessage);
                ORIDataErrorMessage oriDefMsg = (ORIDataErrorMessage)oriMsg;
                System.out.println(oriDefMsg.getfErrorMessageType() + "=" + oriDefMsg.getfBrokerRef());
            }else if (oriMsg instanceof ORIDataTradingInfo){
                System.out.println("REPLY=ORIDataTradingInfo\t" + zInputMessage);
                ORIDataTradingInfo oriDefMsg = (ORIDataTradingInfo)oriMsg;
                
            }else if (oriMsg instanceof ORIDataNewOrderReply){
                System.out.println("REPLY=ORIDataNewOrderReply\t" + zInputMessage);
                ORIDataNewOrderReply oriDefMsg = (ORIDataNewOrderReply)oriMsg;
                System.out.println(oriDefMsg.getfNewOrderReplyType() + "=" + oriDefMsg.getfHandlInst());
            }else if (oriMsg instanceof ORIDataOrderAmendReply){
                System.out.println("REPLY=ORIDataOrderAmendReply\t" + zInputMessage);
                ORIDataOrderAmendReply oriDefMsg = (ORIDataOrderAmendReply)oriMsg;
                System.out.println(oriDefMsg.getfOrderAmendReplyType());
            }else if (oriMsg instanceof ORIDataOrderCancelReply){
                System.out.println("REPLY=ORIDataOrderCancelReply\t" + zInputMessage);
                ORIDataOrderCancelReply oriDefMsg = (ORIDataOrderCancelReply)oriMsg;
                System.out.println(oriDefMsg.getfOrderCancelReplyType());
            }else if (oriMsg instanceof ORIDataOrderMassCancelReply){
                System.out.println("REPLY=ORIDataOrderMassCancelReply\t" + zInputMessage);
                ORIDataOrderMassCancelReply oriDefMsg = (ORIDataOrderMassCancelReply)oriMsg;
                
            }else if (oriMsg instanceof ORIDataNegotiationDealReply){
                System.out.println("REPLY=ORIDataNegotiationDealReply\t" + zInputMessage);
                ORIDataNegotiationDealReply oriDefMsg = (ORIDataNegotiationDealReply)oriMsg;
                System.out.println(oriDefMsg.getfNegotiationDealReplyType());
            }else if (oriMsg instanceof ORIDataNegotiationDealAmendReply){
                System.out.println("REPLY=ORIDataNegotiationDealAmendReply\t" + zInputMessage);
                ORIDataNegotiationDealAmendReply oriDefMsg = (ORIDataNegotiationDealAmendReply)oriMsg;
                System.out.println(oriDefMsg.getfNegotiationDealAmendReplyType());
            }else if (oriMsg instanceof ORIDataNegotiationDealCancelReply){
                System.out.println("REPLY=ORIDataNegotiationDealCancelReply\t" + zInputMessage);
                ORIDataNegotiationDealCancelReply oriDefMsg = (ORIDataNegotiationDealCancelReply)oriMsg;
                System.out.println(oriDefMsg.getfNegotiationDealCancelReplyType());
            }else if (oriMsg instanceof ORIDataNegotiationDealConfirmedInfo){
                System.out.println("REPLY=ORIDataNegotiationDealConfirmedInfo\t" + zInputMessage);
                ORIDataNegotiationDealConfirmedInfo oriDefMsg = (ORIDataNegotiationDealConfirmedInfo)oriMsg;
                
            }else if (oriMsg instanceof ORIDataNegotiationDealReminder){
                System.out.println("REPLY=ORIDataNegotiationDealReminder\t" + zInputMessage);
                ORIDataNegotiationDealReminder oriDefMsg = (ORIDataNegotiationDealReminder)oriMsg;
                
            }else if (oriMsg instanceof ORIDataNewLiquidityProviderOrderReply){
                System.out.println("REPLY=ORIDataNewLiquidityProviderOrderReply\t" + zInputMessage);
                ORIDataNewLiquidityProviderOrderReply oriDefMsg = (ORIDataNewLiquidityProviderOrderReply)oriMsg;
                
            }else if (oriMsg instanceof ORIDataLiquidityProviderOrderCancelReply){
                System.out.println("REPLY=ORIDataLiquidityProviderOrderCancelReply\t" + zInputMessage);
                ORIDataLiquidityProviderOrderCancelReply oriDefMsg = (ORIDataLiquidityProviderOrderCancelReply)oriMsg;
                System.out.println(oriDefMsg.getfLiquidityProviderOrderCancelReplyType());
            }else if (oriMsg instanceof ORIDataUnknownMessage){
                System.out.println("*REPLY=ORIDataUnknownMessage\t" + zInputMessage);
                ORIDataUnknownMessage oriDefMsg = (ORIDataUnknownMessage)oriMsg;
                
            }
        }catch(Exception ex0){
            //.EXXX.
        }
    }
    
}
