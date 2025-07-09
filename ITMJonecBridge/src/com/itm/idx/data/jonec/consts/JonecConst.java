/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.jonec.consts;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;

/**
 *
 * @author Hirin
 */
public class JonecConst {
    
    public static final String DEFAULT_EXEC_BROKER_CODE                 = ITMFileLoggerVarsConsts.APP_CLIENT_CODE; //.AB
    public static final String JONEC_MESSAGE_VERSION                    = "IDXEQ";
    public static final String FIX_BEGIN_STRING_VALUE                   = "FIX.4.2-JSX";
    public static final String JONEC_MARTINS_CONN_NAME                  = DEFAULT_EXEC_BROKER_CODE + "_01_901_toMARTINS"; //. AB_01_901_toMARTINS
    public static final int    DEFAULT_HEART_BEAT_INT                   = 45;
    public static final char JONEC_HEADER_MSG_SEPARATOR                 = 3;
    public static final String SINGLE_SPACE                             = " ";
    
    public JonecConst(){
    }
}
