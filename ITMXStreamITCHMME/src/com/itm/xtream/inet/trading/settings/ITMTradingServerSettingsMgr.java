/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author fredy
 */
public class ITMTradingServerSettingsMgr {
    
    public final static ITMTradingServerSettingsMgr getInstance = new ITMTradingServerSettingsMgr();
    
    public final static String SERVER_SETTINGS_FILE_NAME = "server_settings.json";
    
    public ITMTradingServerSettingsMgr(){
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    private CTS_Server_Settings_Bundle mSettings = new CTS_Server_Settings_Bundle();

    public CTS_Server_Settings_Bundle getSettings() {
        return mSettings;
    }
    
    public class CTS_Server_Settings_Bundle {
        public CTS_Server_Settings server_settings; 
    }
    
    public class CTS_Server_Settings {
        public String version = "";
        public String description = "";
        public String fix5xmlpath = "";
        public int heartbeattimeout = 0;
        public int invalid_price_retry_time = 0;
        public OUCH_OrderRetry[] order_retry_time_invalid_price;
        public int socket_send_buffer = 6400;
        public int socket_recv_buffer = 6400;
        public boolean socket_send_autoflush = false;
        public String data_end_time = "";
        public boolean order_racing_enable = false;
        public int order_reply_timeout = 0;
        public int order_racing_mode = 1; //. 1 = single order, 2 = all order
        public int order_racing_max_delay = 200;
        public OUCH_OrderRacing[] order_racing_time;
        public OUCH_OrderRetry[] order_retry_time;
        public OUCH_OrderRacing[] order_racing_time_ca;
        public OUCH_OrderRetry[] order_retry_time_ca;
        public OUCH_OrderReplyTimeOut[] order_reply_timeout_time;
        public DB_Connection database;
        public CTS_Connection[] itch_connections;
        public CTS_Connection[] itch_mdf_connections;
        public CTS_Connection[] ouch_connections;
        public FIX_Connection[] fix5_connections;
        public DTF_Client datafeed_client;
        public XCH_Client splitter_client;
        public SIM_Connection[] jonec_client_connections;
        public SIM_Connection[] martin_client_connections;
        
    }
    
    public class DB_Connection{
        public String address = "";
        public int port = 0;
        public String username = "";
        public String password = "";
        public String dbname = "";
    }
    
    public class CTS_Connection {
        public String name = "";
        public String description = "";
        public String ipaddress = "";
        public int port = 0;
        public int timeout = 0;
        public String usercode = "";
        public String password = "";
        public boolean heartbeat = false;
        public boolean cmpmsgexact = false;
        public int reconseqmode = 0;
        public boolean autoselect = false;
    }
    
    //. 2022-02-14 : hrn-konfig untuk order racing di ouch
    public class OUCH_OrderRacing {
       public String name = "";
       public String description = "";
       public String starttime = "";
       public String endtime = "";    
       public int[]  tradingdays;
       public int bufferSend = 0;
       public int bufferRecv = 0;
       public int racingmaxdelay = 0; //.20250318: tambahan max delay untuk setiap sesi order racing
    }
    
    //. 2022-06-14 : hrn-konfig untuk order retry di ouch
    //. 2023-07-28 : dipakai juga untuk retry invalid price
    public class OUCH_OrderRetry {
       public String name = "";
       public String description = "";
       public String starttime = "";
       public String endtime = "";    
       public int[]  tradingdays; 
    }
    
    //. 2022-08-30 : hrn-konfig untuk order reply timeouttime di ouch
    public class OUCH_OrderReplyTimeOut {
       public String name = "";
       public String description = "";
       public String starttime = "";
       public String endtime = "";    
       public int[]  tradingdays; 
    }
    
    public class FIX_Connection {
        public String name = "";
        public String description = "";
        public String ipaddress = "";
        public int port = 0;
        public int timeout = 0;
        public String connectorcode = "";
        public String tradercode = "";
        public String password1 = "";
        public String password2 = "";
        public boolean heartbeat = false;
        public boolean cmpmsgexact = false;
        public int reconseqmode = 0;
        public boolean calcheader = false;
        public boolean autoselect = false;
    }
    
    public class DTF_Client{
        public int port = 0;
        public int maxcount = 0;
        public String username = "";
        public String password = "";
        public String tradingdays = "";
        public String begindaytime = "";
        public String enddaytime = "";
        public int pausebreak = 0;
    }
    
    public class XCH_Client{
        public int port = 0;
        public int maxcount = 0;
        public String usercode = "";
        public String password = "";
        public String tradingdays = "";
        public String begindaytime = "";
        public String enddaytime = "";
        public int pausebreak = 0;
    }
    
    public class SIM_Connection {
        public String name = "";
        public String description = "";
        public int port = 0;
        public int timeout = 0;
        public String usercode = "";
        public String password = "";
        public boolean heartbeat = false;
    }
    
    private File getServerSettingsFileObject(){
        File mOut = null;
        try{
            String zLocalPath = "./";
            /**********
            if (zLocalPath.isEmpty()){
                try{
                    File mFile = new File(ITMTradingServerSettingsMgr.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                    if (mFile.isDirectory()){
                        zLocalPath = mFile.getPath();
                    }
                }catch(URISyntaxException ex0){
                    zLocalPath = "";
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }catch(Exception ex0){
                    zLocalPath = "";
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }
            }
            if ((zLocalPath == null) || (zLocalPath.isEmpty())){
                try{
                    File mFile = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
                    if (mFile.isDirectory()){
                        zLocalPath = mFile.getPath();
                    }
                }catch(Exception ex0){
                    zLocalPath = "";
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }
            }
            **********/
            boolean bCheckModuleFileFound = false;
            String zCheckModuleFileDir = zLocalPath;
            
            String zCheckModuleFilePath = "";
            String zPathDelimiter = "\\";
            /**********
            if (zCheckModuleFileDir == null){
                zCheckModuleFileDir = "";
            }
            **********/
            if (zCheckModuleFileDir.contains("/")){
                zPathDelimiter = "/";
            }
            while(!bCheckModuleFileFound){
                File mCurrentDir = new File(zCheckModuleFileDir);
                zCheckModuleFilePath = (zCheckModuleFileDir + (zCheckModuleFileDir.endsWith(zPathDelimiter) ? "" : zPathDelimiter));
                if (zCheckModuleFilePath.length() <= 1){
                    zCheckModuleFilePath = "";
                }
                
                zCheckModuleFilePath += SERVER_SETTINGS_FILE_NAME;
                
                
                File mCurrentFile = new File(zCheckModuleFilePath);
//                System.out.println("zCheckModuleFilePath");
//                System.out.println(zCheckModuleFilePath);
                if ((mCurrentFile.isFile()) && (mCurrentFile.exists())){
                    //System.err.println("mCurrentFile.exists() = " + mCurrentFile.exists());
                    mOut = mCurrentFile;
                    break;
                }
                File mParentDir = mCurrentDir.getParentFile();
                if (mParentDir != null){
                    zCheckModuleFileDir = mParentDir.getPath();
                }else{
                    break;
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean loadSettings(boolean withAutoReSave) {
        boolean mOut = false;
        try{
            String zFileJSON = "";
            File mFileObject = getServerSettingsFileObject();
            if (mFileObject != null){
                try (RandomAccessFile mReader = new RandomAccessFile(mFileObject, "r")) {
                    byte[] arrBt = new byte[(int)mReader.length()];
                    mReader.seek(0);
                    mReader.readFully(arrBt);
                    zFileJSON = new String(arrBt);
                }
                if (!zFileJSON.isEmpty()){
                    Gson mGson = new Gson();
                    this.mSettings = mGson.fromJson(zFileJSON, CTS_Server_Settings_Bundle.class);
                    mOut = (this.mSettings != null);
                    if ((mOut) && (withAutoReSave)){
                        saveSettings();
                    }
                }
            }
        }catch(JsonSyntaxException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }catch(IOException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public boolean saveSettings() {
        boolean mOut = false;
        try{
            String zFileJSON = "";
            Gson mGson = new GsonBuilder().setPrettyPrinting().create();
            if (this.mSettings.server_settings == null){
                this.mSettings.server_settings = new CTS_Server_Settings();                
            }
            if (this.mSettings.server_settings.database == null){
                this.mSettings.server_settings.database = new DB_Connection();                
            }
            if (this.mSettings.server_settings.datafeed_client == null){
                this.mSettings.server_settings.datafeed_client = new DTF_Client();                
            }
            if (this.mSettings.server_settings.splitter_client == null){ //.apm:20220315:ref hrn;
                this.mSettings.server_settings.splitter_client = new XCH_Client();                
            }
            if (this.mSettings.server_settings.fix5_connections == null){
                this.mSettings.server_settings.fix5_connections = new FIX_Connection[]{};                
            }
            if (this.mSettings.server_settings.itch_connections == null){
                this.mSettings.server_settings.itch_connections = new CTS_Connection[]{};                
            }
            if (this.mSettings.server_settings.jonec_client_connections == null){
                this.mSettings.server_settings.jonec_client_connections = new SIM_Connection[]{};                
            }
            if (this.mSettings.server_settings.martin_client_connections == null){
                this.mSettings.server_settings.martin_client_connections = new SIM_Connection[]{};                
            }
            if (this.mSettings.server_settings.ouch_connections == null){
                this.mSettings.server_settings.ouch_connections = new CTS_Connection[]{};                
            }
            if (this.mSettings.server_settings.order_racing_time == null){
                this.mSettings.server_settings.order_racing_time = new OUCH_OrderRacing[]{};                
            }
            
            if (this.mSettings.server_settings.order_retry_time_invalid_price == null){
                this.mSettings.server_settings.order_retry_time_invalid_price = new OUCH_OrderRetry[]{};                
            }
            
            if (this.mSettings.server_settings.order_retry_time == null){
                this.mSettings.server_settings.order_retry_time = new OUCH_OrderRetry[]{};                
            }
            
             if (this.mSettings.server_settings.order_racing_time_ca == null){
                this.mSettings.server_settings.order_racing_time_ca = new OUCH_OrderRacing[]{};                
            }
            
            if (this.mSettings.server_settings.order_retry_time_ca == null){
                this.mSettings.server_settings.order_retry_time_ca = new OUCH_OrderRetry[]{};                
            }
            
            if (this.mSettings.server_settings.order_reply_timeout_time == null){
                this.mSettings.server_settings.order_reply_timeout_time = new OUCH_OrderReplyTimeOut[]{};                
            }
            
            
            zFileJSON = mGson.toJson(this.mSettings, CTS_Server_Settings_Bundle.class);
            if (!zFileJSON.isEmpty()){
                File mFileObject = getServerSettingsFileObject();
                if (mFileObject != null){
                    try (RandomAccessFile mWriter = new RandomAccessFile(mFileObject, "rw")) {
                        mWriter.writeBytes(zFileJSON);
                        mOut = true;
                    }
                }
            }
        }catch(JsonSyntaxException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }catch(IOException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    
    
}
