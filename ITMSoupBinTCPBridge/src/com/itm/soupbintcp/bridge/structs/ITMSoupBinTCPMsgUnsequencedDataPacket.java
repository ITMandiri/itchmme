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
public class ITMSoupBinTCPMsgUnsequencedDataPacket extends ITMSoupBinTCPMsgBase {
    
    private byte[] Message                                                      = new byte[]{};
    
    public ITMSoupBinTCPMsgUnsequencedDataPacket(){
        super.setPacketType(ITMSoupBinTCPBridgeConsts.SoupBinTCPPacketType.PACKETTYPE_UNSEQUENCED_DATA_PACKET);
    }

    public byte[] getMessage() {
        return Message;
    }

    public void setMessage(byte[] Message) {
        this.Message = Message;
    }
    
    @Override
    public byte[] buildPacket(){
        byte[] mOut = resetCumulativeBytes()
        .concatenateField(getPacketLength(), 0, 2)
        .concatenateField(getPacketType(), 2, 1)
        .concatenateField(getMessage(), 3, getMessage().length)
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
                
                setMessage(bufferSubGet(btPacketBytes, 3, (btPacketBytes.length - 3)));
                
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
