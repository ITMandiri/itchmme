/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.filelogger.setup;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;

/**
 *
 * @author aripam
 */
public interface ITMFileLoggerListener {
    
    public abstract void onFileLogAdded(logLevel level, String zFullLogMessage);
    
}
