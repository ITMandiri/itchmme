/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.message.bridge.IDXBridgeController;
import com.itm.idx.data.message.bridge.IDXBridgeListener;
import com.itm.idx.data.ori.consts.ORIDataConst;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeChangePasswordReply;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogonReply;
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
import com.itm.idx.data.qri.message.struct.QRIDataNegDealListMessage;
import com.itm.idx.data.qri.message.struct.QRIDataTradingLimitMessage;
import com.itm.idx.data.qri.message.struct.QRIDataUnknownMessage;
import com.itm.idx.message.IDXMessage;

/**
 *
 * @author aripam
 */
public class RecvRequestTestMsgHandler implements IDXBridgeListener{

    @Override
    public void onConnected(ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName) {
        
        System.out.println("CONNECTED#" + ConnectionName + "=\t" + channel.getChannelID());
        
    }

    @Override
    public void onDisconnected(ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName) {
        
        System.out.println("DISCONNECTED#" + ConnectionName + "=\t" + channel.getChannelID());
        
        
        
    }

    @Override
    public void onReceive(ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName, String messageLine, IDXMessage messageObject) {
        
        //System.out.println("RECEIVE#" + ConnectionName + "=obj=" + messageObject + "; msg=\t" + messageLine);
        
        if (controller.getMsgGroupType() == IDXBridgeController.IDXGroupMessageType.ORI_MESSAGE){
            //.test:
            try{
                if (messageObject == null){
                    System.out.println("REPLY=Null\t" + messageLine);

                }else if (messageObject instanceof ORIDataAdministrativeLogonReply){
                    System.out.println("REPLY=ORIDataAdministrativeLogonReply\t" + messageLine);
                    ORIDataAdministrativeLogonReply oriDefMsg = (ORIDataAdministrativeLogonReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfAdministrativeLogonReplyType());
                    if (oriDefMsg.getfAdministrativeLogonReplyType().equals(ORIDataAdministrativeLogonReply.ORIAdministrativeLogonReplyType.OK2)){
                        switch (StringHelper.toInt(oriDefMsg.getfLogonReply())) {
                            case ORIDataConst.ORIFieldValue.ADMIN_LOGON_REPLY_PWD_HAS_EXPIRED:
                                //.password expire:
                                System.out.println("→" + "WARNING=PasswordExpired, will disconnectNow!");
                                controller.stopConnection();
                                break;
                            case ORIDataConst.ORIFieldValue.ADMIN_LOGON_REPLY_PWD_NEAR_EXPIRE:
                                //.password near expire:
                                System.out.println("→" + "WARNING=PasswordNearExpire, Password-change-required (should change password before order entry)!");
                                break;
                            default:
                                //.test tambahan di sini:
                                
                                break;
                        }
                    }
                }else if (messageObject instanceof ORIDataAdministrativeChangePasswordReply){
                    System.out.println("REPLY=ORIDataAdministrativeChangePasswordReply\t" + messageLine);
                    ORIDataAdministrativeChangePasswordReply oriDefMsg = (ORIDataAdministrativeChangePasswordReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfAdministrativeChangePasswordReplyType());
                }else if (messageObject instanceof ORIDataAdministrativeLogoutReply){
                    System.out.println("REPLY=ORIDataAdministrativeLogoutReply\t" + messageLine);
                    ORIDataAdministrativeLogoutReply oriDefMsg = (ORIDataAdministrativeLogoutReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfText());
                }else if (messageObject instanceof ORIDataErrorMessage){
                    System.out.println("REPLY=ORIDataErrorMessage\t" + messageLine);
                    ORIDataErrorMessage oriDefMsg = (ORIDataErrorMessage)messageObject;
                    System.out.println("→" + oriDefMsg.getfErrorMessageType() + "=" + oriDefMsg.getfBrokerRef());
                }else if (messageObject instanceof ORIDataTradingInfo){
                    System.out.println("REPLY=ORIDataTradingInfo\t" + messageLine);
                    ORIDataTradingInfo oriDefMsg = (ORIDataTradingInfo)messageObject;
                    System.out.println("→" + oriDefMsg.getfTradingSessionID() + "(" + oriDefMsg.getfTradSesStatus() + ")");
                }else if (messageObject instanceof ORIDataNewOrderReply){
                    System.out.println("REPLY=ORIDataNewOrderReply\t" + messageLine);
                    ORIDataNewOrderReply oriDefMsg = (ORIDataNewOrderReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfNewOrderReplyType() + "=" + oriDefMsg.getfHandlInst());
                }else if (messageObject instanceof ORIDataOrderAmendReply){
                    System.out.println("REPLY=ORIDataOrderAmendReply\t" + messageLine);
                    ORIDataOrderAmendReply oriDefMsg = (ORIDataOrderAmendReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfOrderAmendReplyType());
                }else if (messageObject instanceof ORIDataOrderCancelReply){
                    System.out.println("REPLY=ORIDataOrderCancelReply\t" + messageLine);
                    ORIDataOrderCancelReply oriDefMsg = (ORIDataOrderCancelReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfOrderCancelReplyType());
                }else if (messageObject instanceof ORIDataOrderMassCancelReply){
                    System.out.println("REPLY=ORIDataOrderMassCancelReply\t" + messageLine);
                    ORIDataOrderMassCancelReply oriDefMsg = (ORIDataOrderMassCancelReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfText());
                }else if (messageObject instanceof ORIDataNegotiationDealReply){
                    System.out.println("REPLY=ORIDataNegotiationDealReply\t" + messageLine);
                    ORIDataNegotiationDealReply oriDefMsg = (ORIDataNegotiationDealReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfNegotiationDealReplyType());
                }else if (messageObject instanceof ORIDataNegotiationDealAmendReply){
                    System.out.println("REPLY=ORIDataNegotiationDealAmendReply\t" + messageLine);
                    ORIDataNegotiationDealAmendReply oriDefMsg = (ORIDataNegotiationDealAmendReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfNegotiationDealAmendReplyType());
                }else if (messageObject instanceof ORIDataNegotiationDealCancelReply){
                    System.out.println("REPLY=ORIDataNegotiationDealCancelReply\t" + messageLine);
                    ORIDataNegotiationDealCancelReply oriDefMsg = (ORIDataNegotiationDealCancelReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfNegotiationDealCancelReplyType());
                }else if (messageObject instanceof ORIDataNegotiationDealConfirmedInfo){
                    System.out.println("REPLY=ORIDataNegotiationDealConfirmedInfo\t" + messageLine);
                    ORIDataNegotiationDealConfirmedInfo oriDefMsg = (ORIDataNegotiationDealConfirmedInfo)messageObject;
                    System.out.println("→" + oriDefMsg.getfText());
                }else if (messageObject instanceof ORIDataNegotiationDealReminder){
                    System.out.println("REPLY=ORIDataNegotiationDealReminder\t" + messageLine);
                    ORIDataNegotiationDealReminder oriDefMsg = (ORIDataNegotiationDealReminder)messageObject;
                    System.out.println("→" + oriDefMsg.getfText());
                }else if (messageObject instanceof ORIDataNewLiquidityProviderOrderReply){
                    System.out.println("REPLY=ORIDataNewLiquidityProviderOrderReply\t" + messageLine);
                    ORIDataNewLiquidityProviderOrderReply oriDefMsg = (ORIDataNewLiquidityProviderOrderReply)messageObject;
                    
                }else if (messageObject instanceof ORIDataLiquidityProviderOrderCancelReply){
                    System.out.println("REPLY=ORIDataLiquidityProviderOrderCancelReply\t" + messageLine);
                    ORIDataLiquidityProviderOrderCancelReply oriDefMsg = (ORIDataLiquidityProviderOrderCancelReply)messageObject;
                    System.out.println("→" + oriDefMsg.getfLiquidityProviderOrderCancelReplyType());
                }else if (messageObject instanceof ORIDataUnknownMessage){
                    //System.out.println("*REPLY=ORIDataUnknownMessage\t" + messageLine);
                    ORIDataUnknownMessage oriDefMsg = (ORIDataUnknownMessage)messageObject;
                    
                }
            }catch(Exception ex0){
                //.EXXX.
            }
        }else{
            if ((!(messageObject instanceof QRIDataUnknownMessage)) && (!(messageObject instanceof QRIDataTradingLimitMessage))){ //.sementara filter msg yg tidak dikenal.
                System.out.println("MARTIN.REPLY#" + ConnectionName + "=obj=" + messageObject + "; msg=\t" + messageLine);
                if (messageObject instanceof QRIDataNegDealListMessage){
                    QRIDataNegDealListMessage qriNegDealList = (QRIDataNegDealListMessage)messageObject;
                    System.out.println("→ BrokerRef = " + qriNegDealList.getfClOrdID());
                    System.out.println("→ OrderID = " + qriNegDealList.getfOrderID());
                    System.out.println("→ ContraUserID = " + qriNegDealList.getfContraTrader());
                    System.out.println("→ ContraBrokerID = " + qriNegDealList.getfContraBroker());
                    switch(qriNegDealList.getfOrdStatus()){
                        case "0":
                            System.out.println("→ Status = UNCONFIRMED");
                            break;
                        case "D":
                            System.out.println("→ Status = CONFIRMED");
                            break;
                        case "2":
                            System.out.println("→ Status = MATCHED");
                            break;
                        case "4":
                            System.out.println("→ Status = WITHDRAWN");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        
    }

    @Override
    public void onSent(ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName, String messageLine) {
        
        System.out.println("SENT#" + ConnectionName + "; msg=\t" + messageLine);
        
        
    }

    @Override
    public void onError(ITMSocketChannel channel, IDXBridgeController controller, String ConnectionName, Exception exception) {
        
        System.out.println("ERROR#" + ConnectionName + "; msg=\t" + exception.getMessage());
        
        
    }
    
}
