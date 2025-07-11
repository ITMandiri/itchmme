/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.structs;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;

/**
 *
 * @author fredy
 */
public class OUCHMsgUnknown extends OUCHMsgBase {
    
    @Override
    public byte[] buildMessage() {
        byte[] mOut = resetCumulativeBytes()
        
        .putPacketLength() //.last set before get cumulative bytes;
        .getCumulativeBytes();
        
        return mOut;
    }

    @Override
    public boolean parseMessage(byte[] btMessageBytes) {
        boolean mOut = false;
        try{
            if (super.parseMessage(btMessageBytes)){
                
                //... .
                
                mOut = true;
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.OUCH, ITMFileLoggerVarsConsts.logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}