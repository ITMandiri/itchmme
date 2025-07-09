/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.feed.consts;

/**
 *
 * @author fredy
 */
public class FeedConsts {
    
    public class TradingStatusConsts {
        public TradingStatusConsts() {
            
        }
        public final static String GENERIC_EMPTY_STRING                         = "";
        public final static String TRADINGSTATUS_STATUS_BEGIN_SENDING_RECORDS   = "1";
        public final static String TRADINGSTATUS_STATUS_IDLE_TIME               = "2";
        public final static String TRADINGSTATUS_STATUS_BEGIN_FIRST_SESSION     = "3";
        public final static String TRADINGSTATUS_STATUS_END_FIRST_SESSION       = "4";
        public final static String TRADINGSTATUS_STATUS_BEGIN_SECOND_SESSION    = "5";
        public final static String TRADINGSTATUS_STATUS_END_SECOND_SESSION      = "6";
        public final static String TRADINGSTATUS_STATUS_END_SENDING_RECORDS     = "7";
        public final static String TRADINGSTATUS_STATUS_BEGIN_PRE_OPENING       = "8";
        public final static String TRADINGSTATUS_STATUS_END_PRE_OPENING         = "9";
        public final static String TRADINGSTATUS_STATUS_BEGIN_PRE_CLOSING       = "a";
        public final static String TRADINGSTATUS_STATUS_END_PRE_CLOSING         = "b";
        public final static String TRADINGSTATUS_STATUS_BEGIN_POST_TRADING      = "c";
        public final static String TRADINGSTATUS_STATUS_END_POS_TRADING         = "d";
        public final static String TRADINGSTATUS_STATUS_TRADING_SUSPENSION      = "e";
        public final static String TRADINGSTATUS_STATUS_TRADING_ACTIVATION      = "f";
        public final static String TRADINGSTATUS_STATUS_BOARD_SUSPENSION        = "g";
        public final static String TRADINGSTATUS_STATUS_BOARD_ACTIVATION        = "h";
        public final static String TRADINGSTATUS_STATUS_INSTRUMENT_SUSPENSION   = "i";
        public final static String TRADINGSTATUS_STATUS_INSTRUMENT_ACTIVATION   = "j";
        public final static String TRADINGSTATUS_STATUS_MARKET_SUSPENSION       = "k";
        public final static String TRADINGSTATUS_STATUS_MARKET_ACTIVATION       = "l";
        public final static String TRADINGSTATUS_STATUS_BREAK                   = "m";
        public final static String TRADINGSTATUS_STATUS_BREAK_CALL              = "n";
        public final static String TRADINGSTATUS_STATUS_RANDOM_CLOSE            = "p";
        public final static String TRADINGSTATUS_STATUS_CALL_RANDOM_CLOSE       = "o";
        public final static String TRADINGSTATUS_STATUS_CLOSE                   = "p";
        public final static String TRADINGSTATUS_STATUS_MATCHING_CA             = "q";
        public final static String TRADINGSTATUS_STATUS_MATCHING_CLOSE          = "r";
        public final static String TRADINGSTATUS_STATUS_MATCHING_PRE_OPEN       = "s";
        public final static String TRADINGSTATUS_STATUS_NON_CANCELLATION        = "t";
        public final static String TRADINGSTATUS_STATUS_TRADING_HALT            = "u";
        public final static String TRADINGSTATUS_MSG_MARKET_EQ                  = "EQ";
        public final static String TRADINGSTATUS_MSG_INSTRUMENT_ORDI            = "ORDI";
        public final static String TRADINGSTATUS_MSG_INSTRUMENT_ORDI_PREOPEN    = "ORDI_PREOPEN";
        public final static String TRADINGSTATUS_MSG_INSTRUMENT_MUTI            = "MUTI";
        public final static String TRADINGSTATUS_MSG_INSTRUMENT_WARI            = "WARI";
        public final static String TRADINGSTATUS_MSG_INSTRUMENT_RGHI            = "RGHI";
        public final static String TRADINGSTATUS_MSG_INSTRUMENT_ACCEL           = "ACCEL"; 
        public final static String TRADINGSTATUS_MSG_INSTRUMENT_S_WARI          = "S_WARI";
    }
    
    public class TradingStatusDesc {
        public final static String TRADINGSTATUS_STATUS_BEGIN_SENDING_RECORDS   = "Begin sending records";
        public final static String TRADINGSTATUS_STATUS_END_SENDING_RECORDS     = "End sending records";
        public static final String TRADINGSTATUS_BREAK                          = "Break Session";
        public static final String TRADINGSTATUS_BREAK_CALL                     = "Break Session Call Auction";
        public static final String TRADINGSTATUS_CALL_RANDOM_CLOSE              = "Random Closing at Call Auction";
        public static final String TRADINGSTATUS_CLOSE_CALL_AUCTION             = "Closing at Call Auction";
        public static final String TRADINGSTATUS_CLOSE_NG                       = "Close Negotiation";
        public static final String TRADINGSTATUS_CLOSE_RF                       = "Close Regular Futures";
        public static final String TRADINGSTATUS_CLOSE_RG                       = "Close Regular";
        public static final String TRADINGSTATUS_CLOSE_TN                       = "Close Cash";
        public static final String TRADINGSTATUS_END_OF_DAY                     = "End of Day";
        public static final String TRADINGSTATUS_MATCHING_CALL_AUCTION          = "Matching at Call Auction";
        public static final String TRADINGSTATUS_MATCHING_CLOSE                 = "Matching at Closing Regular";
        public static final String TRADINGSTATUS_MATCHING_PRE_OPEN              = "Matching at Pre-Opening";
        public static final String TRADINGSTATUS_NON_CANCEL                     = "Non cancellation";
        public static final String TRADINGSTATUS_POST_TRADE                     = "Post Trading";
        public static final String TRADINGSTATUS_PRE_CLOSE                      = "Pre-Closing";
        public static final String TRADINGSTATUS_PRE_OPEN                       = "Pre-Opening";
        public static final String TRADINGSTATUS_RANDOM_CLOSE                   = "Random Closing";
        public static final String TRADINGSTATUS_SESSION_1_NG                   = "Session 1 NG";
        public static final String TRADINGSTATUS_SESSION_1_RF                   = "Session 1 Regular Futures";
        public static final String TRADINGSTATUS_SESSION_1_RG                   = "Session 1 Regular";
        public static final String TRADINGSTATUS_SESSION_1_RG_CALL              = "Call Auction Session 1";
        public static final String TRADINGSTATUS_SESSION_1_TN                   = "Session 1 Cash";
        public static final String TRADINGSTATUS_SESSION_1_TN_CALL              = "Call Auction Session 1 - TN";
        public static final String TRADINGSTATUS_SESSION_2_NG                   = "Session 2 NG";
        public static final String TRADINGSTATUS_SESSION_2_RF                   = "Session 2 Regular Futures";
        public static final String TRADINGSTATUS_SESSION_2_RG                   = "Session 2 Regular";
        public static final String TRADINGSTATUS_SESSION_2_RG_CALL              = "Call Auction Session 2";
        public static final String TRADINGSTATUS_SESSION_2_TN_CALL              = "Call Auction Session 2 - TN";
        public static final String TRADINGSTATUS_SESSION_3_RG_CALL              = "Call Auction Session 3";
        public static final String TRADINGSTATUS_SESSION_3_TN_CALL              = "Call Auction Session 3 - TN";
        public static final String TRADINGSTATUS_SESSION_4_RG_CALL              = "Call Auction Session 4";
        public static final String TRADINGSTATUS_SESSION_5_RG_CALL              = "Call Auction Session 5";
        public static final String TRADINGSTATUS_SOBD                           = "Start of Business Date";
        public static final String TRADINGSTATUS_SUSPEND                        = "Suspend with Cancel Orders";
        public static final String TRADINGSTATUS_TRADING_HALT                   = "Trading Halt by Keeping Orders";
        public static final String TRADINGSTATUS_UNSUSPEND                      = "Unsuspend";
    }
    
    public class TradingStatusType {
        public final static String TRADINGSTATUS_TYPE_MSG_ALL                   = "al";
        public final static String TRADINGSTATUS_TYPE_MSG_CALL_AUCTION          = "ca";
        public final static String TRADINGSTATUS_TYPE_MSG_NON_CALL_AUCTION      = "nc";
    }
    
    public class TradingStatusType2 {
        public final static String TRADINGSTATUS_TYPE_MSG_NG                    = "ng";
        public final static String TRADINGSTATUS_TYPE_MSG_RF                    = "rf";
        public final static String TRADINGSTATUS_TYPE_MSG_RG                    = "rg";
        public final static String TRADINGSTATUS_TYPE_MSG_TN                    = "tn";
        
    }
}
