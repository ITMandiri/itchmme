/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.generic.engine.socket.bridge.SVRBridgeController;
import com.itm.generic.engine.socket.bridge.SVRBridgeListener;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;

/**
 *
 * @author aripam
 */
public class RecvListenerClientMsgHandler implements SVRBridgeListener {

    @Override
    public void onConnected(ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName) {
        System.out.println("Client:" + channel.getChannelID() + "\t→[onConnected]\t" + "th:" + Thread.currentThread().getId() + "\t");
    }

    @Override
    public void onDisconnected(ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName) {
        System.out.println("Client:" + channel.getChannelID() + "\t→[onDisconnected]\t" + "th:" + Thread.currentThread().getId() + "\t");
    }

    @Override
    public void onReceive(ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName, String messageLine) {
        System.out.println("Client:" + channel.getChannelID() + "\t→[onReceive]\t" + "th:" + Thread.currentThread().getId() + "\t" + messageLine);
    }

    @Override
    public void onSent(ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName, String messageLine) {
        System.out.println("Client:" + channel.getChannelID() + "\t→[onSent]\t" + "th:" + Thread.currentThread().getId() + "\t" + messageLine);
    }

    @Override
    public void onError(ITMSocketChannel channel, SVRBridgeController controller, String ConnectionName, Exception exception) {
        System.out.println("Client:" + channel.getChannelID() + "\t→[onError]\t" + "th:" + Thread.currentThread().getId() + "\t" + exception.getMessage());
    }
    
}
