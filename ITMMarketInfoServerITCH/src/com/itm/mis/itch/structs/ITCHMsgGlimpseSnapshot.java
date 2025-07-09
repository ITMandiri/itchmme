/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.structs;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts;
import static com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat.decodeString;

/**
 *
 * @author fredy
 */
public class ITCHMsgGlimpseSnapshot extends ITCHMsgBase {

    private String itchSequenceNumber;


    public String getItchSequenceNumber() {
        return itchSequenceNumber;
    }

    public void setItchSequenceNumber(String itchSequenceNumber) {
        this.itchSequenceNumber = itchSequenceNumber;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 52 + ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { 
                    setItchSequenceNumber(decodeString(btMessageBytes, ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 20));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.ITCH, ITMFileLoggerVarsConsts.logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgGlimpseSnapshot");
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.ITCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }

    @Override
    public byte[] buildMessage() {
        byte[] mOut = resetCumulativeBytes()
                .concatenateField(getItchSequenceNumber(), ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 20)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}

