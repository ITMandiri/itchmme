/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.main;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.mis.itch.callback.ITMSoupBinTCPITCHPacketMgr;
import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketGeneral;
import com.itm.xtream.inet.trading.consts.ITMTradingServerConsts;
import com.itm.xtream.inet.trading.datafile.restore.DataFileRestoreAccess;
import com.itm.xtream.inet.trading.dbaccess.DBAccessManagement;
import com.itm.xtream.inet.trading.feed.msgmem.ITMFeedMsgMemory;
import com.itm.xtream.inet.trading.feed.server.main.DataFeedAccess;
import com.itm.xtream.inet.trading.form.ITMTradingServerHomeForm;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import com.itm.xtream.inet.trading.sync.connection.ITMTradingServerSyncConnectionMgr;
import com.itm.xtream.inet.trading.viewer.ITMComponentLayoutHelper;

/**
 *
 * @author fredy
 */
public class ITMTradingServerMain {
    
    private final static ITMTradingServerMain getInstance = new ITMTradingServerMain();
    
    public ITMTradingServerHomeForm mHomeForm;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        System.out.println("*********************************************************");
        System.out.println("/* --------------------------------------------------- */");
        System.out.println("/* WELCOME TO XTREAM INET TRADING SERVER */");
        System.out.println("/* --------------------------------------------------- */");
        System.out.println("/* DEV: PT. INFO TEKNOLOGI MANDIRI */");
        System.out.println("/* CLI: " + ITMFileLoggerVarsConsts.APP_CLIENT_NAME + " */");
        System.out.println("/* */");
        System.out.println("/* VERSION APPL : " + ITMTradingServerConsts.SVR_APP_VERSION_BUILD );
        System.out.println("/* VER PROTOCOL : " + ITMTradingServerConsts.SVR_PROTOCOL_VERSION );
        System.out.println("/* --------------------------------------------------- */");
        System.out.println("*********************************************************");
        System.out.println("");
        System.out.println("");
        
        ITMTradingServerMain.getInstance.instanceCode();
        
    }
    
    public void instanceCode() {
        
        try{
            //.muat settings:
            ITMTradingServerSettingsMgr.getInstance.loadSettings(true);
            //.muat heartbeattimeout:
            ITMSoupBinTCPBridgeSocketGeneral.getInstance.setiHeartbeatTimeout(ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.heartbeattimeout);
            //.muat koneksi:
            int iConnectionOrderNo = 0;
            
            DBAccessManagement itmDBaseAccess = DBAccessManagement.getInstance;
            //.buka jalur ke database (load sekali di memory):
            ITMTradingServerSettingsMgr.DB_Connection mCfgDbConn = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.database;
            itmDBaseAccess.setSzDBHostAddress(mCfgDbConn.address);
            itmDBaseAccess.setIntDBHostPort(mCfgDbConn.port);
            itmDBaseAccess.setSzDBUserName(mCfgDbConn.username);
            itmDBaseAccess.setSzDBUserPassword(mCfgDbConn.password);
            itmDBaseAccess.setSzDBSelfName(mCfgDbConn.dbname);
                       
            //.sambung dan pertahankan koneksi ke database jika ada address nya:
            if (itmDBaseAccess.getSzDBHostAddress() != null && itmDBaseAccess.getSzDBHostAddress().length() > 0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "try to connect #1 to database server on address: " + mCfgDbConn.address + " on port: " + mCfgDbConn.port + ".");
                if (itmDBaseAccess.getDbMasterAlreadyActiveConn() == null){
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "cannot connect #1 to database server.");
                }else{
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "#1 connected to database server.");
                }
            }
            
            try {
                //itch:
                ITMTradingServerSettingsMgr.CTS_Connection[] arrConnections = ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.itch_connections;
                if ((arrConnections != null) && (arrConnections.length > 0)){
                    for (ITMTradingServerSettingsMgr.CTS_Connection arrConnection : arrConnections) {
                        if (arrConnection != null){
                            iConnectionOrderNo++;
                            ITMSoupBinTCPITCHPacketMgr.getInstance.addConnectionLine(
                                    arrConnection.name
                                    , arrConnection.description
                                    , iConnectionOrderNo
                                    , arrConnection.ipaddress
                                    , arrConnection.port
                                    , arrConnection.timeout
                                    , arrConnection.usercode
                                    , arrConnection.password
                                    , arrConnection.heartbeat
                                    , arrConnection.cmpmsgexact
                                    , arrConnection.reconseqmode
                                    , arrConnection.autoselect
                                    , ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.socket_send_buffer
                                    , ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.socket_recv_buffer
                                    , ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.socket_send_autoflush
                            );
                        }
                    }
                }
            } catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
            
//            iConnectionOrderNo = 0;
                        
            ITMTradingServerSyncConnectionMgr.getInstance.startListenServerSyncBridges();
            
            ITMComponentLayoutHelper.enableFirstGlobalProgramTheme();
            
            //.tampilkan dialog:
            mHomeForm = new ITMTradingServerHomeForm();
            mHomeForm.showThis();
            
            mHomeForm.setMainControlsEnableState(false); //.disable controls;
            mHomeForm.setBottomInfoMessage("Restoring Data Files, Please Wait ... .");
            ITMFeedMsgMemory mFeedMsgMemory = ITMFeedMsgMemory.getInstance; //.init feed memory;
            if (mFeedMsgMemory != null){
                mFeedMsgMemory.startListener();
                DataFileRestoreAccess.getInstance.processOnce();
            }
            mHomeForm.setBottomInfoMessage("");
            mHomeForm.setMainControlsEnableState(true); //.enable controls;
            
            DataFeedAccess.getInstance.runServerOnce();
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        
    }
    
}