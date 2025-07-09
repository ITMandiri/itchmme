/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.consts;

import com.itm.idx.data.jonec.consts.JonecConst;

/**
 *
 * @author fredy
 */
public class ITMTradingServerConsts {
    
    public final static String SVR_APP_VERSION_BUILD                = "1.25.06.18 build_ab_2025.06.18ax-MME"; //."1.22.03.25 build_ab_2022.03.25dx"; //."1.21.12.28 build_ab_2022.03.18bx"; //."1.21.10.04 build_ab_2021.10.04a"; //."1.21.07.22 build_sh_2021.07.22a"; //."1.21.07.06 build_sh_2021.07.06b"; //. 1.20.05.27 build_sh_2020.05.27a
    public final static String SVR_APP_CUSTOM_CAPTION               = "ITCH - Datafeed 5.0.0.1 Build 22.02.25"; //. special request for custom  caption    
    public final static String SVR_PROTOCOL_VERSION                 = "1aa";
    public final static String SVR_SETTINGS_MINIMUM_VERSION         = "1.20.05.27";
    
    public ITMTradingServerConsts() {
    }
    
    public class BrokerSetup {
        public final static String BROKER_CODE                      = JonecConst.DEFAULT_EXEC_BROKER_CODE;
    }
    public class DispTextSetup {
        
        public final static String Z_TITLE_MSGBOX_CONFIRM                       = "Confirm";
        public final static String Z_TITLE_MSGBOX_WARNING                       = "Warning";
        public final static String Z_TITLE_MSGBOX_ERROR                         = "Error";
        
    }
    
    public class EngineSetup {
        public final static boolean FIX5_ONLY                                   = false;
    }
    
}
