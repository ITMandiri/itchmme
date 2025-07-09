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
public class ITMSoupBinTCPMsgLoginAcceptedPacket extends ITMSoupBinTCPMsgBase {

    private String Session                                                      = "";
    private long SequenceNumber                                                 = 0;
    
    public ITMSoupBinTCPMsgLoginAcceptedPacket(){
        super.setPacketType(ITMSoupBinTCPBridgeConsts.SoupBinTCPPacketType.PACKETTYPE_LOGIN_ACCEPTED_PACKET);
    }

    public String getSession() {
        return Session;
    }

    public void setSession(String Session) {
        this.Session = Session;
    }

    public long getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(long SequenceNumber) {
        this.SequenceNumber = SequenceNumber;
    }
    
    @Override
    public byte[] buildPacket(){
        byte[] mOut = resetCumulativeBytes()
        .concatenateField(getPacketLength(), 0, 2)
        .concatenateField(getPacketType(), 2, 1)
        .concatenateField(encodeStringLeftPadded(getSession(),10,CHARACTER_SPACE), 3, 10)
        .concatenateField(encodeStringRightPadded(String.valueOf(getSequenceNumber()),20,CHARACTER_SPACE), 13, 20)
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
                
                setSession(decodeStringTrim(btPacketBytes, 3, 10));
                setSequenceNumber(toLong(decodeStringTrim(btPacketBytes, 13, 20)));
                
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
