/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.sockets;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import static com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat.addZeroFromInt;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeSocketGeneral {
    //.single instance:
    public final static ITMSoupBinTCPBridgeSocketGeneral getInstance = new ITMSoupBinTCPBridgeSocketGeneral();
    
    private int iCounterForClient                               = 0;
    private int iHeartbeatTimeout                               = 0;
    
    //.id dengan tanda berasal dari client (kita sebagai client):
    public synchronized String generateClientChannelID(){
        String zOut = "2X2"; //<1="AsServer"/2="AsClient"><"SoupBinTCP">yyMMdd<0.000.000.000=counter[10]><00=random[2]>
        try{
            zOut = "2";
            zOut += "SoupBinTCP";
            zOut += new SimpleDateFormat("yyMMdd").format(new Date());
            int iCurCounter;
            iCounterForClient++;
            if (iCounterForClient <= 0){
                iCounterForClient = 1;
            }
            iCurCounter = iCounterForClient;
            zOut += addZeroFromInt(iCurCounter, 10);
            String zNextTwo = addZeroFromInt(new Random().nextInt(), 10);
            zNextTwo = zNextTwo.substring(zNextTwo.length() - 2);
            zOut += zNextTwo;
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return zOut;
    }

    public int getiHeartbeatTimeout() {
        return iHeartbeatTimeout;
    }

    public void setiHeartbeatTimeout(int iHeartbeatTimeout) {
        this.iHeartbeatTimeout = iHeartbeatTimeout;
    }
    
    
}
