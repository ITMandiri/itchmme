/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.fix5.data.jonec.admin;

import com.itm.fix5.data.jonec.access.FIX5JonecAccessAdministrative;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController.FIX5IDXGroupMessageType;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Ari Pambudi
 */
public class FIX5JonecDoReqAdministration {
    //.single instance:
    public final static FIX5JonecDoReqAdministration getInstance = new FIX5JonecDoReqAdministration();
    
    private final static ScheduledExecutorService schWaitLogout = Executors.newScheduledThreadPool(5);
    
    private final int WAIT_LOGOUT_TIMEOUT_SECONDS               = 10;
    
    public FIX5JonecDoReqAdministration() {
        //.nothing todo here:)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.INIT, "");
    }
    
    public void doLogon(FIX5IDXBridgeController controller){
        try{ //.bisa dilakukan setelah connected:
            controller.setLastMessage(""); //.reset message.
            if (!controller.isAdminPasswordExpired()){
                String zConnectorCode = controller.getConnectorCode();
                String zTraderCode = controller.getConnectorCode();
                String zUserPass1 = controller.getPassword1();
                String zUserPass2 = controller.getPassword2();
                String zLineName = controller.getConnectionName();
                if (null == controller.getMsgGroupType()){
                    //.unknown:
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.TRANSACTION, logLevel.WARNING, "null as unknown fix5 jonec line to logon group type: " + controller.getMsgGroupType());
                }else switch (controller.getMsgGroupType()) {
                    case FIX5_JONEC_MESSAGE:
                        //.fix5 jonec logon:
                        FIX5JonecAccessAdministrative oriAdmin = new FIX5JonecAccessAdministrative(controller, zConnectorCode, zTraderCode, zUserPass1, zUserPass2, zLineName);
                        controller.sendMessageDirect(oriAdmin.login());
                        break;
                    default:
                        //.unknown:
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.TRANSACTION, logLevel.WARNING, "unknown fix5 jonec line to logon group type: " + controller.getMsgGroupType());
                        break;
                }
            }else{
                //.password expired, minta ganti:
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.TRANSACTION, logLevel.WARNING, "fix5 jonec line password already expired: " + controller.getConnectionName());
                //.tampilkan dialog ganti password:
                
                //.selesai:... .
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
    }
    
    public void doLogout(FIX5IDXBridgeController controller){
        try{ //.bisa dilakukan sebelum disconnect:
            if (controller.isAdminLoggedOn()){
                String zConnectorCode = controller.getConnectorCode();
                String zTraderCode = controller.getConnectorCode();
                String zUserPass1 = controller.getPassword1();
                String zUserPass2 = controller.getPassword2();
                String zLineName = controller.getConnectionName();
                if (controller.getMsgGroupType() == FIX5IDXGroupMessageType.FIX5_JONEC_MESSAGE){
                    //.ori logout:
                    FIX5JonecAccessAdministrative oriAdmin = new FIX5JonecAccessAdministrative(controller, zConnectorCode, zTraderCode, zUserPass1, zUserPass2, zLineName);
                    if (!controller.sendMessageDirect(oriAdmin.logout())){
                        //.jika tidak bisa kirim request logout, langsung disconnect saja:
                        controller.stopConnection();
                    }else{
                        //.tunggu 10 detik, jika tidak ada respon, langsung disconnect saja:
                        final FIX5IDXBridgeController fnlController = controller;
                        schWaitLogout.schedule(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    if (fnlController.isAdminLoggedOn()){
                                        fnlController.stopConnection();
                                    }
                                }catch(Exception ex0){
                                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
                                }
                            }
                        }, WAIT_LOGOUT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    }
                }else{
                    //.unknown:
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.TRANSACTION, logLevel.WARNING, "unknown fix5 jonec line to logout group type: " + controller.getMsgGroupType());
                }
            }else{
                //.tetap disconnect:
                controller.stopConnection();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
    }
    
    public void doLogoff(FIX5IDXBridgeController controller){
        try{ //.bisa dilakukan sebelum disconnected:
            controller.setIsAdminLoggedOn(false); //.reset logon state (jadi logout).
            //.selesai... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SERVER, logLevel.ERROR, ex0);
        }
    }
    
}
