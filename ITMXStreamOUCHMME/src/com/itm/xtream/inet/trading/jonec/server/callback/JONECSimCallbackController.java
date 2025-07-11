/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.jonec.server.callback;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.socket.setup.ITMSocketChannel;
import com.itm.generic.engine.socket.setup.ITMSocketCtrlServer;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author fredy
 */
public class JONECSimCallbackController extends ITMSocketCtrlServer {
    //.single instance ya:
    public static final String SOCKET_CONNECTION_NAME_AS_SIMULATION_OF_JONEC    = "JONEC_SIM";
    public final static JONECSimCallbackController getInstance = new JONECSimCallbackController(SOCKET_CONNECTION_NAME_AS_SIMULATION_OF_JONEC);
    
    private int HostPort;
    
    private JONECSimCallbackHandler eventHandler;
    
    //.daftar saja, bukan urutan:
    private ConcurrentHashMap<ITMSocketChannel, JONECSimCallbackProcessor> activeChannelsProcessors = new ConcurrentHashMap<>(); //.daftar channel dan processor.<Channel,JONECSimCallbackProcessor>
    
    public JONECSimCallbackController(String zConnectionName) {
        super(zConnectionName);
        this.eventHandler = new JONECSimCallbackHandler(this);
        addSocketEventHandler(this.eventHandler);
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public boolean bIsServerAlive(){
        try{
            return isListen();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "test for bIsServerAlive exception:" + ex0);
        }
        return false;
    }
    
    public boolean bIsServerConnected(){ //.mehod ini tidak berlaku. hanya untuk client socket ya.
        try{
            return isListen();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "test for bIsServerConnected exception:" + ex0);
        }
        return false;
    }
    
    public boolean bIsServerBound(){
        try{
            return isListen();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "test for bIsServerBound exception:" + ex0);
        }
        return false;
    }
    
    public boolean bIsServerReadable(){
        try{
            return isListen();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "test for bIsServerReadable exception:" + ex0);
        }
        return false;
    }
    
    public boolean bIsServerWritable(){
        try{
            return isListen();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "test for bIsServerWritable exception:" + ex0);
        }
        return false;
    }
    
    public boolean serverConnect(int inHostPort){
        this.HostPort = inHostPort;
        try {
            listen(this.HostPort);
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "gateway server channel cannot Bind:" + ex0);
            return false;
        }
        return isListen();
    }
    
    public boolean serverDisconnect(){
        shutdown();
        return (!isListen());
    }
    
    public ConcurrentHashMap<ITMSocketChannel, JONECSimCallbackProcessor> getAllChannelsProcessorsList(){
        return this.activeChannelsProcessors;
    }
    
    public boolean isChannelsProcessorsListEmpty(){
        return this.activeChannelsProcessors.isEmpty();
    }
    
    public boolean isChannelsProcessorsOnLimit(){
        boolean mOut = false;
        if (this.activeChannelsProcessors.size() >= ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.jonec_client_connections.length){
            mOut = true;
        }
        return mOut;
    }
    
    public void createChannelProcessor(ITMSocketChannel mChannel){
        try{
            if (!this.activeChannelsProcessors.containsKey(mChannel)){
                JONECSimCallbackProcessor mProcessor = new JONECSimCallbackProcessor();
                mProcessor.setChChannel(mChannel);
                mProcessor.startProcessor();
                this.activeChannelsProcessors.put(mChannel, mProcessor);
            }else{
                JONECSimCallbackProcessor mProcessor = this.activeChannelsProcessors.get(mChannel);
                mProcessor.setChChannel(mChannel);
                mProcessor.startProcessor();
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "create channel processor exception:" + ex0);
        }
    }
    
    public void destroyChannelProcessor(ITMSocketChannel mChannel){
        try{
            if (this.activeChannelsProcessors.containsKey(mChannel)){
                JONECSimCallbackProcessor mProcessor = this.activeChannelsProcessors.get(mChannel);
                if (mProcessor != null){
                    mProcessor.stopProcessor();
                }
                this.activeChannelsProcessors.remove(mChannel);
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "destroy channel processor exception:" + ex0);
        }
    }
    
    public JONECSimCallbackProcessor getChannelProcessor(ITMSocketChannel mChannel){
        JONECSimCallbackProcessor mOut = null;
        try{
            if (this.activeChannelsProcessors.containsKey(mChannel)){
                mOut = this.activeChannelsProcessors.get(mChannel);
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "get channel processor exception:" + ex0);
        }
        return mOut;
    }
    
    public JONECSimCallbackProcessor getActiveChannelProcessorByConnName(String zConnName){
        JONECSimCallbackProcessor mOut = null;
        try{
            if (this.activeChannelsProcessors.size() > 0){
                for(JONECSimCallbackProcessor mProcessor : this.activeChannelsProcessors.values()){
                    if ((mProcessor.getConnName().equals(zConnName)) && (mProcessor.getChChannel() != null) && (!mProcessor.getChChannel().isChannelAlreadyWasted())){
                        mOut = mProcessor;
                        break;
                    }
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "get channel processor exception:" + ex0);
        }
        return mOut;
    }
    
    public JONECSimCallbackProcessor getActiveChannelProcessorByUserCode(String zUserCode){
        JONECSimCallbackProcessor mOut = null;
        try{
            if (this.activeChannelsProcessors.size() > 0){
                for(JONECSimCallbackProcessor mProcessor : this.activeChannelsProcessors.values()){
                    if ((mProcessor.getUserCode().equals(zUserCode)) && (mProcessor.getChChannel() != null) && (!mProcessor.getChChannel().isChannelAlreadyWasted())){
                        mOut = mProcessor;
                        break;
                    }
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "get channel processor exception:" + ex0);
        }
        return mOut;
    }
    
}
