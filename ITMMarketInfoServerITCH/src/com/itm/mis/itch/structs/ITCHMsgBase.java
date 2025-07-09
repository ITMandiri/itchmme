/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.structs;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset;
import com.itm.soupbintcp.bridge.packetbuilder.ITMSoupBinTCPBridgePacketBuilder;

/**
 *
 * @author Ari Pambudi
 */
public class ITCHMsgBase extends ITMSoupBinTCPBridgePacketBuilder {
    
    private String Type                                                         = "";
    
    //.internal:
    private byte[] MsgBuildBytes                                              = {};
    private byte[] MsgParseBytes                                              = {};
    
    public byte[] getMsgBuildBytes() {
        return MsgBuildBytes;
    }

    public void setMsgBuildBytes(byte[] MsgBuildBytes) {
        this.MsgBuildBytes = MsgBuildBytes;
    }
    
    public byte[] getMsgParseBytes() {
        return MsgParseBytes;
    }

    public void setMsgParseBytes(byte[] MsgBytes) {
        this.MsgParseBytes = MsgBytes;
    }
    
    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    
    public byte[] buildMessage(){
        byte[] mOut = resetCumulativeBytes()
        
        .putPacketLength() //.last set before get cumulative bytes;
        .getCumulativeBytes();
        setMsgBuildBytes(mOut);
        return mOut;
    }
    
    public boolean parseMessage(byte[] btMessageBytes){
        boolean mOut = false;
        try{
            setMsgParseBytes(btMessageBytes);
            setType(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 0, 1));
            mOut = true;
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    protected double getNumberFraction(double v, long fraction){
        double dResult = v;
        if (fraction > 0){
            dResult = v / (Math.pow(10, fraction));
        }
        return dResult;
    }
}
