/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.structs;

/**
 *
 * @author fredy
 */
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.consts.ITMSoupBinTCPBridgeConsts.SoupBinTCPOffset;

public class ITCHMsgMarketSegmentDirectory extends ITCHMsgBase {

    private int nanos;
    private int marketSegmentId;
    private String marketSegmentName;
    private String marketSegmentLongName;

    public int getNanos() {
        return nanos;
    }

    public void setNanos(int nanos) {
        this.nanos = nanos;
    }

    public int getMarketSegmentId() {
        return marketSegmentId;
    }

    public void setMarketSegmentId(int marketSegmentId) {
        this.marketSegmentId = marketSegmentId;
    }

    public String getMarketSegmentName() {
        return marketSegmentName;
    }

    public void setMarketSegmentName(String marketSegmentName) {
        this.marketSegmentName = marketSegmentName;
    }

    public String getMarketSegmentLongName() {
        return marketSegmentLongName;
    }

    public void setMarketSegmentLongName(String marketSegmentLongName) {
        this.marketSegmentLongName = marketSegmentLongName;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try {
            if (super.parseMessage(btMessageBytes)) {
                if (btMessageBytes.length >= 105 + SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD) { // 1 + 4 + 4 + 32 + 64 = 105
                    setNanos(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4));
                    setMarketSegmentId(decodeInteger(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4));
                    setMarketSegmentName(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 32));
                    setMarketSegmentLongName(decodeString(btMessageBytes, SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 41, 64));
                    mOut = true;
                } else {
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR,
                            "Insufficient bytes for ITCHMsgMarketSegmentDirectory");
                }
            }
        } catch (Exception ex0) {
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.ITCH, logLevel.ERROR, ex0);
        }
        return mOut;
    }

    @Override
    public byte[] buildMessage() {
        byte[] mOut = resetCumulativeBytes()
                .concatenateField(getNanos(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 1, 4)
                .concatenateField(getMarketSegmentId(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 5, 4)
                .concatenateField(getMarketSegmentName(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 9, 32)
                .concatenateField(getMarketSegmentLongName(), SoupBinTCPOffset.OFFSET_FIELD_PAYLOAD + 41, 64)
                .putPacketLength()
                .getCumulativeBytes();
        return mOut;
    }
}
