/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.structs;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPMsgLoginRequestPacket extends ITMSoupBinTCPMsgBase {
    
    private String Username                                                     = "";
    private String Password                                                     = "";
    private String RequestedSession                                             = "";
    private long RequestedSequenceNumber                                        = 0;

    public ITMSoupBinTCPMsgLoginRequestPacket(){
        super.setPacketType(ITMSoupBinTCPBridgeConsts.SoupBinTCPPacketType.PACKETTYPE_LOGIN_REQUEST_PACKET);
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getRequestedSession() {
        return RequestedSession;
    }

    public void setRequestedSession(String RequestedSession) {
        this.RequestedSession = RequestedSession;
    }

    public long getRequestedSequenceNumber() {
        return RequestedSequenceNumber;
    }

    public void setRequestedSequenceNumber(long RequestedSequenceNumber) {
        this.RequestedSequenceNumber = RequestedSequenceNumber;
    }

    @Override
    public byte[] buildPacket(){
        byte[] mOut = resetCumulativeBytes()
        .concatenateField(getPacketLength(), 0, 2)
        .concatenateField(getPacketType(), 2, 1)
        .concatenateField(encodeStringLeftPadded(getUsername(),6,CHARACTER_SPACE), 3, 6)
        .concatenateField(encodeStringLeftPadded(getPassword(),10,CHARACTER_SPACE), 9, 10)
        .concatenateField(encodeStringLeftPadded(getRequestedSession(),10,CHARACTER_SPACE), 19, 10)
        .concatenateField(encodeStringRightPadded(String.valueOf(getRequestedSequenceNumber()),20,CHARACTER_SPACE), 29, 20)
        .putPacketLength() //.last set before get cumulative bytes;
        .getCumulativeBytes();
        setPacketBuildBytes(mOut);
        return mOut;
    }
    
    @Override
    public boolean parsePacket(byte[] btPacketBytes) {
        boolean mOut = false;
        try{
            if (super.parsePacket(btPacketBytes)){
                
                setUsername(decodeStringTrim(btPacketBytes, 3, 6));
                setPassword(decodeStringTrim(btPacketBytes, 9, 10));
                setRequestedSession(decodeStringTrim(btPacketBytes, 19, 10));
                setRequestedSequenceNumber(toLong(decodeStringTrim(btPacketBytes, 29, 20)));
                
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
