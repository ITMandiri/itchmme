/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.generic.engine.socket.bridge.SVRBridgeController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author aripam
 */
public class TS_ListenerTest {
    
    String z = "";
    
    public TS_ListenerTest() {
    }
    
    public synchronized void Do1(String zNew) {
        System.out.println("--enter:Do1=" + zNew);
        this.z = zNew;
        System.out.println("--write:Do1=" + this.z);
        Do2(zNew);
        System.out.println("--from:Do1/afterDo2=" + this.z);
        System.out.println("--leave:Do1");
    }
    
    public synchronized void Do2(String zNew) {
        System.out.println("--enter:Do2=" + zNew);
        this.z += zNew;
        System.out.println("--append:Do2=" + this.z);
        Do3(zNew);
        System.out.println("--from:Do2/afterDo3=" + this.z);
        System.out.println("--leave:Do2");
    }
    
    public synchronized void Do3(String zNew) {
        System.out.println("--enter:Do3=" + zNew);
        this.z += zNew;
        System.out.println("--append:Do3=" + this.z);
        System.out.println("--leave:Do3");
    }
    
    public void runListenerNow(){
        
        int nListenerPort = 6789;
        int tCheckInterval = 5000;
        String zConnName = "ConnNameA";
        
        SVRBridgeController svr = new SVRBridgeController(zConnName);
        
        RecvListenerClientMsgHandler handler = new RecvListenerClientMsgHandler();
        
        svr.setRefListenerPort(nListenerPort);
        svr.setRefCheckInterval(tCheckInterval);
        svr.addBridgeListener(handler);
        
        
        
        boolean bMustLoop = true;
        
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        String zInput = "";
        
        while(bMustLoop){
            System.out.println("A=Start / Z=Stop / X=Exit" + "\tthid:" + Thread.currentThread().getId());
            try {
                zInput = bfr.readLine();
            } catch (IOException ex) {
                //.exxx.
                System.err.println(ex);
            }
            switch(zInput.toUpperCase().trim()){
                case "A":
                    System.out.println("--starting server...before=" + svr.isListen());
                    svr.startListener();
                    System.out.println("--starting server...after=" + svr.isListen());
                    break;
                case "Z":
                    System.out.println("--stopping server...before=" + svr.isListen());
                    svr.stopListener();
                    System.out.println("--stopping server...after=" + svr.isListen());
                    break;
                case "X":
                    System.out.println("--exitting server...before=" + svr.isListen());
                    svr.stopListener();
                    System.out.println("--exitting server...after=" + svr.isListen());
                    bMustLoop = false;
                    break;
                default:
                    System.err.println("--unknown command : " + zInput);
                    break;
            }
            
        }
        
        
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TS_ListenerTest ts = new TS_ListenerTest();
        ts.Do1("HelloWorld!");
        
        ts.runListenerNow();
        
        /*
         * issue: 2014/03/14 listener return false karena tidak menunggu sampai listen.
         * issue: 2014/03/14 listener sekali dishutdown, tidak bisa di listen lagi.
         */
    }
}
