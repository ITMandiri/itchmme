/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.idx.data.message.bridge.IDXBridgeController;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author aripam
 */
public class TS_ConnectionTest {
    //.single instance:
    public final static TS_ConnectionTest getInstance = new TS_ConnectionTest();
    
    private IDXBridgeController oriBridgeCtrl = new IDXBridgeController("");
    
    private String zConnectionName  = "ID_JONEC_001_CONNECTION_TEST";
    private String zSvrIPAddress    = "192.168.0.104"; //"192.168.0.3";
    private int iSvrPort            = 1117; //2000;
    private int iSvrTimeOut         = 60000;
    private int iCheckInterval      = 10000;
    
    public IDXBridgeController getOriRecvSocket() {
        return oriBridgeCtrl;
    }
    
    public void runAsJONECClient(){
        
        RecvRequestTestMsgHandler msgHandler = new RecvRequestTestMsgHandler();
        
        oriBridgeCtrl.setMsgGroupType(IDXBridgeController.IDXGroupMessageType.ORI_MESSAGE);
        oriBridgeCtrl.setConnectionName(zConnectionName);
        oriBridgeCtrl.setRefServerAddress(zSvrIPAddress);
        oriBridgeCtrl.setRefServerPort(iSvrPort);
        oriBridgeCtrl.setRefTryConnectTimeOut(iSvrTimeOut);
        oriBridgeCtrl.setRefCheckInterval(iCheckInterval);
        oriBridgeCtrl.addBridgeListener(msgHandler);
        
        oriBridgeCtrl.startConnection();
        
        boolean bRun = true;
        
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        String zInput = "";
        
        while(bRun){
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                
            }
        }
        
        oriBridgeCtrl.stopConnection();
        
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        TS_ConnectionTest.getInstance.runAsJONECClient();
        
    }
}
