/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.setup;

import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author aripam
 */
public class ITMSocketCtrlGeneral {
    //.single instance:
    public final static ITMSocketCtrlGeneral getInstance = new ITMSocketCtrlGeneral();
    
    private int iCounterForServer                               = 0;
    private int iCounterForClient                               = 0;
    
    //.id dengan tanda berasal dari listener (kita sebagai server):
    public synchronized String generateServerChannelID(){
        String zOut = "1X1"; //<1="AsServer"/2="AsClient">yyMMdd<0.000.000.000=counter[10]><00=random[2]>
        try{
            zOut = "1";
            zOut += new SimpleDateFormat("yyMMdd").format(new Date());
            int iCurCounter;
            iCounterForServer++;
            if (iCounterForServer <= 0){
                iCounterForServer = 1;
            }
            iCurCounter = iCounterForServer;
            zOut += StringHelper.addZeroFromInt(iCurCounter, 10);
            String zNextTwo = StringHelper.addZeroFromInt(new Random().nextInt(), 10);
            zNextTwo = zNextTwo.substring(zNextTwo.length() - 2);
            zOut += zNextTwo;
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return zOut;
    }
    
    //.id dengan tanda berasal dari client (kita sebagai client):
    public synchronized String generateClientChannelID(){
        String zOut = "2X2"; //<1="AsServer"/2="AsClient">yyMMdd<0.000.000.000=counter[10]><00=random[2]>
        try{
            zOut = "2";
            zOut += new SimpleDateFormat("yyMMdd").format(new Date());
            int iCurCounter;
            iCounterForClient++;
            if (iCounterForClient <= 0){
                iCounterForClient = 1;
            }
            iCurCounter = iCounterForClient;
            zOut += StringHelper.addZeroFromInt(iCurCounter, 10);
            String zNextTwo = StringHelper.addZeroFromInt(new Random().nextInt(), 10);
            zNextTwo = zNextTwo.substring(zNextTwo.length() - 2);
            zOut += zNextTwo;
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return zOut;
    }
    
}
