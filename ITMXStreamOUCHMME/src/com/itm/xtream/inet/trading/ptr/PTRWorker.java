/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.ptr;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.qri.message.struct.QRIDataTradingLimitMessage;
import com.itm.xtream.inet.trading.consts.ITMTradingServerConsts;
import com.itm.xtream.inet.trading.martin.server.msgmem.books.BookOfMARTINTradingLimitList;
import com.nptr.idx.mme.NptrClient;
import com.nptr.idx.mme.config.NptrConfig;
import com.nptr.idx.mme.models.LimitGroup;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author fredy
 */
public class PTRWorker {
    
    public final static PTRWorker getInstance = new PTRWorker();
    private static NptrClient client;
    private final TimerTask timerTask;
    private Timer timer;
    private String token = "";
    
    //.constructor:
    public PTRWorker(){
        setUpConfig();
        timerTask = new PTRTimer();
    }
    
    public void startWorker(int iMsRate){
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, iMsRate);
    }
    
    public void stopWorker(){
        if (timer != null){
            timer.cancel();
        }
        
    }

    private void setUpConfig() {
        try {
            Properties props = loadProperties("ptr.config");

            NptrConfig config = NptrConfig.builder()
                    .baseUrl(props.getProperty("nasdaq.nptr.baseurl"))
                    .host(props.getProperty("nasdaq.nptr.host"))
                    .username(props.getProperty("nasdaq.nptr.username"))
                    .password(props.getProperty("nasdaq.nptr.password"))
                    .sslEnabled(Boolean.parseBoolean(props.getProperty("nasdaq.nptr.ssl_enabled", "true")))
                    .certFile(props.getProperty("nasdaq.nptr.certfile"))
                    .grantType(props.getProperty("nasdaq.mme.grant_type"))
                    .clientId(props.getProperty("nasdaq.mme.clientid"))
                    .clientSecret(props.getProperty("nasdaq.mme.clientsecret"))
                    .tokenEndpoint(props.getProperty("nasdaq.mme.tokenendpoint"))
                    .connectTimeoutMs(Integer.parseInt(props.getProperty("nasdaq.mme.connect_timeout_ms", "10000")))
                    .socketTimeoutMs(Integer.parseInt(props.getProperty("nasdaq.mme.socket_timeout_ms", "30000")))
                    .build();

            client = new NptrClient(config);
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
    }
    
    private Properties loadProperties(String filename) throws Exception {
        Properties props = new Properties();

        File file = new File(filename);

        if (!file.exists()) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "File not found: " + file.getAbsolutePath());
            throw new IllegalStateException("File not found: " + file.getAbsolutePath());
        }

        try (InputStream in = new FileInputStream(file)) {
            props.load(in);
        }

        return props;
    }
    
    public class PTRTimer extends TimerTask{

        @Override
        public void run() {
            try {
                if (StringHelper.isNullOrEmpty(token)) {
                    token = client.getAccessToken();
                }
                List<LimitGroup> groups = client.limitGroups().getAll();
                Double limitEQ = 0.0;
                Double limitDER = 0.0;
                Double activeLimitEQ = 0.0;
                Double consumptionEQ = 0.0;
                Double consumptionDER = 0.0;
                
                for (LimitGroup g : groups) {
                    boolean isEQ = g.getName().endsWith("EQ");
                    boolean isDER = g.getName().endsWith("DER");
                    System.out.println(g);
                    System.out.println(g.getName());
                    for (LimitGroup.RiskCheck rc : g.getRiskChecks()) {
                        System.out.println("Id" + rc.getId());
                        System.out.println("Name" + rc.getName());
                        System.out.println("Description" + rc.getDescription());
                        System.out.println("Status" + rc.getStatus());
                        System.out.println("Override Status" + rc.getOverrideStatus());
                        System.out.println("Enabled" + rc.isEnabled());
                        System.out.println("Time Unit" + rc.getTimeUnit());
                        System.out.println("Active Limit" + rc.getActiveLimit());
                        System.out.println("Consumption" + rc.getConsumption());
                        System.out.println("Limit" + rc.getLimit());
                        System.out.println("Metric" + rc.getMetric());
                        System.out.println("Period" + rc.getPeriod());
                        System.out.println("=============================================================");
                        if (isEQ) {
                            limitEQ = rc.getLimit();
                            consumptionEQ = rc.getConsumption();
                            activeLimitEQ = rc.getActiveLimit();
                        } else if (isDER) {
                            limitDER = rc.getLimit();
                            consumptionDER = rc.getConsumption();
                        }
                    }
                }
//                System.out.println("Limit EQ = "+limitEQ.longValue());
//                System.out.println("Consumption EQ = "+consumptionEQ.longValue());
//                System.out.println("Limit DER = "+limitDER.longValue());
//                System.out.println("Consumption DER = "+consumptionDER.longValue());
                long currentLimit = limitEQ.longValue() - consumptionEQ.longValue();
                QRIDataTradingLimitMessage mLimitListMsg = new QRIDataTradingLimitMessage(new HashMap());
//            mLimitListMsg.setfBundleMessageVersion(mOriginRequestMsg.getfBundleMessageVersion());
//            mLimitListMsg.setfBundleConnectionName(mOriginRequestMsg.getfBundleConnectionName());
                mLimitListMsg.setfSeqNumber(0);
                mLimitListMsg.setfCurrency("IDR"); //. ???
                mLimitListMsg.setfExecBroker(ITMTradingServerConsts.BrokerSetup.BROKER_CODE);
                mLimitListMsg.setfTag("KPEI");
                mLimitListMsg.setfOpenBal(limitEQ.longValue()); //EQ limit
                mLimitListMsg.setfCurrentPos(activeLimitEQ.longValue()); //Active limit
                mLimitListMsg.setfPlannedPos(consumptionEQ.longValue()); //EQ consumption
                mLimitListMsg.setfLimit1(currentLimit); 
                mLimitListMsg.setfLimit1Set(StringHelper.fromDouble(consumptionEQ)); //EQ consumption
                mLimitListMsg.setfLimit2(limitDER.longValue()); //DER limit
                mLimitListMsg.setfLimit2Set(StringHelper.fromDouble(consumptionDER)); //DER consumption

                BookOfMARTINTradingLimitList.getInstance.addOrUpdateSheet(mLimitListMsg);

                BookOfMARTINTradingLimitList.getInstance.brodcastToSubscriber(mLimitListMsg);
            } catch (IOException ex0) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
            }
        }
        
    }
}
