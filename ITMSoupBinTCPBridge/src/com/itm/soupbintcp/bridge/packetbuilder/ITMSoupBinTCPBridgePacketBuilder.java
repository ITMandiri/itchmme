/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.packetbuilder;

import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPLength;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset;
import com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgePacketBuilder extends ITMSoupBinTCPBridgePacketFormat {
    
    private byte[] cumulativeBytes                                              = new byte[]{};
    
    public byte[] getCumulativeBytes(){
        return this.cumulativeBytes;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder putPacketLength(){
        int vPacketLength = this.cumulativeBytes.length;
        if (vPacketLength >= (SoupBinTCPOffset.OFFSET_FIELD_PACKET_LENGTH + SoupBinTCPLength.SIZEOF_FIELD_PACKET_LENGTH)){
            vPacketLength -= (SoupBinTCPOffset.OFFSET_FIELD_PACKET_LENGTH + SoupBinTCPLength.SIZEOF_FIELD_PACKET_LENGTH);
            this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, encodeInteger(vPacketLength, SoupBinTCPLength.SIZEOF_FIELD_PACKET_LENGTH), SoupBinTCPOffset.OFFSET_FIELD_PACKET_LENGTH, SoupBinTCPLength.SIZEOF_FIELD_PACKET_LENGTH);
        }
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder putPacketType(String vPacketType){
        this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, encodeString(vPacketType, SoupBinTCPLength.SIZEOF_FIELD_PACKET_TYPE), SoupBinTCPOffset.OFFSET_FIELD_PACKET_TYPE, SoupBinTCPLength.SIZEOF_FIELD_PACKET_TYPE);
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder resetCumulativeBytes(){
        this.cumulativeBytes = new byte[]{};
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder resizeCumulativeBytes(int vOffset, int vLength){
        if (this.cumulativeBytes.length <= 0){
            this.cumulativeBytes = bufferAlloc((vOffset + vLength),(byte)0);
        }else if (this.cumulativeBytes.length < (vOffset + vLength)){
            this.cumulativeBytes = bufferAppend(this.cumulativeBytes,bufferAlloc(((vOffset + vLength) - this.cumulativeBytes.length),(byte)0));
        }
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder concatenateField(short vShort, int vOffset, int vLength){
        resizeCumulativeBytes(vOffset, vLength);
        this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, encodeShort(vShort, vLength), vOffset, vLength);
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder concatenateField(int vInteger, int vOffset, int vLength){
        resizeCumulativeBytes(vOffset, vLength);
        this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, encodeInteger(vInteger, vLength), vOffset, vLength);
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder concatenateField(long vLong, int vOffset, int vLength){
        resizeCumulativeBytes(vOffset, vLength);
        this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, encodeLong(vLong, vLength), vOffset, vLength);
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder concatenateField(String vString, int vOffset, int vLength){
        resizeCumulativeBytes(vOffset, vLength);
        this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, encodeString(vString, vLength), vOffset, vLength);
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder concatenateFieldRightSpacePadded(String vString, int vOffset, int vLength){
        resizeCumulativeBytes(vOffset, vLength);
        this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, encodeStringRightPadded(vString, vLength, ' '), vOffset, vLength);
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder concatenateFieldLeftSpacePadded(String vString, int vOffset, int vLength){
        resizeCumulativeBytes(vOffset, vLength);
        this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, encodeStringLeftPadded(vString, vLength, ' '), vOffset, vLength);
        return this;
    }
    
    public ITMSoupBinTCPBridgePacketBuilder concatenateField(byte[] vBytes, int vOffset, int vLength){
        resizeCumulativeBytes(vOffset, vLength);
        this.cumulativeBytes = bufferSubSet(this.cumulativeBytes, vBytes, vOffset, vLength);
        return this;
    }
    
    
}
