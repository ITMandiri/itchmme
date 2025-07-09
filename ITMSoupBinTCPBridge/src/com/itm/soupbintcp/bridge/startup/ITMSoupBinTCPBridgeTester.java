/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.startup;
//////
//////import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPLength;

import com.itm.soupbintcp.bridge.sockets.ITMSoupBinTCPBridgeSocketCtl;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeTester {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        ITMSoupBinTCPBridgeTester mThis = new ITMSoupBinTCPBridgeTester();
        mThis.codehere();
        
    }
    
    public void codehere() {
        
        ITMTestSoupBinTCPSocket mSocketCtl = new ITMTestSoupBinTCPSocket("ITM_SCK_SBTCP_TEST_0001");
        mSocketCtl.setAuthAutoLogin(true);
        mSocketCtl.setAuthUserName("ABIT01");
        mSocketCtl.setAuthUserPassword("jakarta123");
        mSocketCtl.doConnect();
        
//////        byte[] bt1 = encodeInteger(0x12345678, 4);
//////        int st1 = decodeInteger(bt1,0,4);
//////        System.out.println(st1);
        
        
    }

    public class ITMTestSoupBinTCPSocket extends ITMSoupBinTCPBridgeSocketCtl {
        
        private ITMSoupBinTCPBridgeTesterSocketListener evHandler;
        
        public ITMTestSoupBinTCPSocket(String zConnectionName) {
            super(zConnectionName);
            this.evHandler = new ITMSoupBinTCPBridgeTesterSocketListener(this);
            addSocketEventHandler(this.evHandler);
            
        }
        
        public boolean doConnect(){
            int vMode = 1;
            switch (vMode) {
                //.ITCH:Port ITCH Total View
                case 1:
                    connect("172.18.2.214", 31094, 5000);
                    break;
                //.ITCH:Port ITCH News
                case 2:
                    connect("172.18.2.214", 39094, 5000);
                    break;
                //.ITCH:Port GLIMPSE
                case 3:
                    connect("172.18.2.214", 33094, 5000);
                    break;
                //.OUCH:Port Trx
                case 4:
                    connect("172.18.2.214", 32094, 5000);
                    break;
                default:
                    break;
            }
            return isConnected();
        }
        
        public boolean doDisconnect(){
            disconnect();
            return isConnected();
        }
        
    }
}
