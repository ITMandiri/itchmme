/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.structs;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.packetbuilder.ITMSoupBinTCPBridgePacketBuilder;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPMsgBase extends ITMSoupBinTCPBridgePacketBuilder {
    
    private int PacketLength                                                    = 0;
    private String PacketType                                                   = "";
    
    //.internal:
    private byte[] PacketBuildBytes                                              = {};
    private byte[] PacketParseBytes                                              = {};

    public int getPacketLength() {
        return PacketLength;
    }

    public void setPacketLength(int PacketLength) {
        this.PacketLength = PacketLength;
    }

    public String getPacketType() {
        return PacketType;
    }

    public void setPacketType(String PacketType) {
        this.PacketType = PacketType;
    }
    
    public byte[] getPacketBuildBytes() {
        return PacketBuildBytes;
    }

    public void setPacketBuildBytes(byte[] PacketBuildBytes) {
        this.PacketBuildBytes = PacketBuildBytes;
    }
    
    public byte[] getPacketParseBytes() {
        return PacketParseBytes;
    }

    public void setPacketParseBytes(byte[] PacketBytes) {
        this.PacketParseBytes = PacketBytes;
    }
    
    public byte[] buildPacket(){
        byte[] mOut = resetCumulativeBytes()
        .concatenateField(getPacketLength(), 0, 2)
        .concatenateField(getPacketType(), 2, 1)
        .putPacketLength() //.last set before get cumulative bytes;
        .getCumulativeBytes();
        setPacketBuildBytes(mOut);
        return mOut;
    }
    
    public boolean parsePacket(byte[] btPacketBytes){
        boolean mOut = false;
        try{
            setPacketParseBytes(btPacketBytes);
            setPacketLength(decodeInteger(btPacketBytes, 0, 2));
            setPacketType(decodeString(btPacketBytes, 2, 1));
            mOut = true;
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.SOUPBINTCP, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
