/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.setup;

import com.itm.generic.engine.socket.setup.ITMSocketVarsConsts.SocketSetup;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aripam
 */
public class ITMSocketCtrlClient {
    
    private String fConnectionName                              = "";
    
    private boolean fCustomLineFactory                          = false;
    private ITMSocketCustomeLineFactoryInterface fCustomLineFactoryWorker;
    
    private ITMSocketChannel fServerChannel;
    
    private List<ITMSocketListener> fServerListenersList;
    
    public ITMSocketCtrlClient(String zConnectionName) {
        this.fConnectionName = zConnectionName;
        //.EXXX.
    }

    public ITMSocketCtrlClient(String zConnectionName, ITMSocketCustomeLineFactoryInterface fCustomLineFactoryWorker) {
        this.fConnectionName = zConnectionName;
        this.fCustomLineFactoryWorker = fCustomLineFactoryWorker;
        this.fCustomLineFactory = (fCustomLineFactoryWorker != null);
        //.EXXX.
    }

    public String getConnectionName() {
        return fConnectionName;
    }

    public void setConnectionName(String fConnectionName) {
        this.fConnectionName = fConnectionName;
        try{
            if (fServerChannel != null){
                fServerChannel.setConnectionName(this.fConnectionName);
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
    }
    
    public boolean isfCustomLineFactory() {
        return fCustomLineFactory;
    }

    public ITMSocketCustomeLineFactoryInterface getfCustomLineFactoryWorker() {
        return fCustomLineFactoryWorker;
    }
    
    protected synchronized boolean addSocketEventHandler(ITMSocketListener eventHandler){
        boolean bOut = false;
        try{
            if (eventHandler != null){
                if (this.fServerListenersList == null) {
                    this.fServerListenersList = new ArrayList<>();
                }
                if (!this.fServerListenersList.contains(eventHandler)){ //.@only once per object.@
                    this.fServerListenersList.add(eventHandler);
                }
            }
            bOut = true;
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    protected synchronized boolean removeSocketEventHandler(ITMSocketListener eventHandler){
        boolean bOut = false;
        try{
            if (eventHandler != null){
                if (this.fServerListenersList != null) {
                    this.fServerListenersList.remove(eventHandler);
                }
            }
            bOut = true;
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    //.public synchronized
    public ITMSocketChannel getChannel() {
        return fServerChannel;
    }
    //.public synchronized
    public boolean isConnected(){
        boolean bOut = false;
        try{
            if (fServerChannel != null){
                if (fServerChannel.getSocket() != null){
                    if (!fServerChannel.isClosed()){
                        if ((!fServerChannel.isInputShutdown()) && (!fServerChannel.isOutputShutdown())){
                            bOut = fServerChannel.isConnected();
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    protected synchronized boolean connect(String zInputServerAddress, int iInputServerPort, int iConnectTimeOut){
        boolean bOut = false;
        try{
            if ((zInputServerAddress != null) && (zInputServerAddress.length() > 0) && (iInputServerPort >= SocketSetup.I_REF_PORT_MIN_NUMBER) && (iInputServerPort <= SocketSetup.I_REF_PORT_MAX_NUMBER) && (iConnectTimeOut >= 0)){
                if (fServerChannel == null){
                    fServerChannel = new ITMSocketChannel(this.fConnectionName, this.fCustomLineFactoryWorker);
                    //.set events:
                    fServerChannel.setSocketListeners(this.fServerListenersList);
                }
                if (fServerChannel.isChannelAlreadyWasted()){
                    //.ganti channel:
                    ITMSocketChannel newSch = new ITMSocketChannel(this.fConnectionName, this.fCustomLineFactoryWorker);
                    //.backup events:
                    newSch.setSocketListeners(this.fServerListenersList);
                    //.backup queue:
                    newSch.setfQueue(fServerChannel.getfQueue());
                    fServerChannel = newSch;
                }
                Socket sock = fServerChannel.getSocket();
                if ((sock == null) || (fServerChannel.isClosed()) || (fServerChannel.isInputShutdown()) || (fServerChannel.isOutputShutdown()) || (!fServerChannel.isSocketWasWorking())){
                    try{
                        sock = new Socket();
//////////                        sock.setReceiveBufferSize(2 * 1024 * 1024);
                        SocketAddress sockAddr = new InetSocketAddress(zInputServerAddress, iInputServerPort);
                        sock.connect(sockAddr, iConnectTimeOut);
                        //.menunggu sampai connect atau sampai tidak bisa connect ke exception:
                        sock.setKeepAlive(true);
                        fServerChannel.setSupplyAddress(zInputServerAddress);
                        fServerChannel.setSupplyPort(iInputServerPort);
                        fServerChannel.setChannelID(ITMSocketCtrlGeneral.getInstance.generateClientChannelID()); //.unique.
                        fServerChannel.StartChannel(sock);
                        bOut = true;
                    }catch(Exception ex1){
                        fServerChannel.raiseOnError(ex1);
                    }
                }else{
                    bOut = true;
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }
    
    protected synchronized boolean disconnect(){
        boolean bOut = false;
        try{
            if (fServerChannel != null){
                fServerChannel.StopChannel();
                bOut = fServerChannel.isClosed();
            }
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return bOut;
    }

}
