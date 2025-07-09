/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.mis.itch.consts;

/**
 *
 * @author Ari Pambudi
 */
public class ITCHConsts {
    
    public class ITCHConnectionType {
        
        //.connection types:
        public static final String CONNECTIONTYPE_ITCH                          = "ITCH";
        public static final String CONNECTIONTYPE_ITCH_MDF                      = "ITCH_MDF";
        
    }
    
    public class ITCHMessageType {
        
        //.message types:
        public static final String MESSAGETYPE_SECONDS_MESSAGE                  = "T";
        public static final String MESSAGETYPE_SYSTEM_EVENT                     = "S";
        public static final String MESSAGETYPE_TICK_SIZE_TABLE                  = "L";
        public static final String MESSAGETYPE_ORDER_BOOK_DIRECTORY             = "R";
        public static final String MESSAGETYPE_EQUILIBRIUM_PRICE                = "Z";
        public static final String MESSAGETYPE_ORDER_BOOK_STATE                 = "O";
        public static final String MESSAGETYPE_ADD_ORDER                        = "A";
        public static final String MESSAGETYPE_ORDER_EXECUTED                   = "E";
        public static final String MESSAGETYPE_ORDER_EXECUTED_WITH_PRICE        = "C";
        public static final String MESSAGETYPE_TRADE                            = "P";
        public static final String MESSAGETYPE_ORDER_DELETE                     = "D";
        public static final String MESSAGETYPE_ORDER_BOOK_CLEAR                 = "d";
        public static final String MESSAGETYPE_GLIMPSE_SNAPSHOT                 = "G";
        
    }
    
    public class ITCHMDFMessageType {
        
        //.message types:
        public static final String MESSAGETYPE_SECONDS_MESSAGE                  = "T";
        public static final String MESSAGETYPE_SYSTEM_EVENT                     = "S";
        public static final String MESSAGETYPE_TICK_SIZE_TABLE                  = "L";
        public static final String MESSAGETYPE_ISSUER_DIRECTORY                 = "V";
        public static final String MESSAGETYPE_ORDER_BOOK_DIRECTORY             = "R";
        public static final String MESSAGETYPE_INDEX_PRICE                      = "J";
        public static final String MESSAGETYPE_EQUILIBRIUM_PRICE                = "Z";
        public static final String MESSAGETYPE_ORDER_BOOK_STATE                 = "O";
        public static final String MESSAGETYPE_PARTICIPANT_DIRECTORY            = "K";
        public static final String MESSAGETYPE_REFERENCE_PRICE                  = "Q";
        public static final String MESSAGETYPE_EXCHANGE_DIRECTORY               = "e";
        public static final String MESSAGETYPE_MARKET_DIRECTORY                 = "m";
        public static final String MESSAGETYPE_MARKET_SEGMENT_DIRECTORY         = "s";
        public static final String MESSAGETYPE_MARKET_BY_PRICE                  = "b";
        public static final String MESSAGETYPE_TRADE_STATISTICS                 = "I";
        public static final String MESSAGETYPE_TRADE_TICKER                     = "i";
        public static final String MESSAGETYPE_PRICE_LIMITS                     = "k";
        public static final String MESSAGETYPE_CIRCUIT_BREAKER_TRIGGER          = "c";
        public static final String MESSAGETYPE_INDICATIVE_QUOTE                 = "q";
        public static final String MESSAGETYPE_INDEX_MEMBER                     = "D";
        
    }
    
    public class ITCHValue {
        
        //.system event codes:
        public static final String SYSTEM_EVENT_CODE_FIRST_OF_MESSAGE           = "O";
        public static final String SYSTEM_EVENT_CODE_START_OF_SYSTEM_HOUR       = "S";
        public static final String SYSTEM_EVENT_CODE_START_OF_MARKET_HOUR       = "Q";
        public static final String SYSTEM_EVENT_CODE_END_OF_MARKET_HOUR         = "M";
        public static final String SYSTEM_EVENT_CODE_OPENING_AUCTION_START      = "P";
        public static final String SYSTEM_EVENT_CODE_CLOSING_AUCTION_START      = "K";
        public static final String SYSTEM_EVENT_CODE_SCHEDULED_AUCTION_START    = "V";
        public static final String SYSTEM_EVENT_CODE_SCHEDULED_AUCTION_CLOSE    = "U";
        public static final String SYSTEM_EVENT_CODE_START_POST_TRADING         = "T";
        public static final String SYSTEM_EVENT_CODE_SCHEDULED_BREAK_START      = "A";
        public static final String SYSTEM_EVENT_CODE_SCHEDULED_BREAK_END        = "B";
        public static final String SYSTEM_EVENT_CODE_END_OF_SYSTEM_HOUR         = "E";
        public static final String SYSTEM_EVENT_CODE_LAST_OF_MESSAGE            = "C";
        public static final String SYSTEM_EVENT_CODE_TRADING_SUSPENSION         = "X";
        public static final String SYSTEM_EVENT_CODE_TRADING_ACTIVATION         = "Y";
        
        //.options directory verbs:
        public static final String OPTION_DIRECTORY_VERB_PUT                    = "P";
        public static final String OPTION_DIRECTORY_VERB_CALL                   = "C";
        
        //.orderbook trading states:
        public static final String ORDERBOOK_TRADING_STATE_SUSPENDED            = "V";
        public static final String ORDERBOOK_TRADING_STATE_UNSUSPENDED          = "T";
        
        //.order verbs:
        public static final String ORDER_VERB_BUY                               = "B";
        public static final String ORDER_VERB_SELL                              = "S";
        public static final String ORDER_VERB_SETTLEMENT_PRICE_UPDATE           = "L";
        public static final String ORDER_VERB_REFERENCE_OR_INDEX_UPDATE         = ""; //.blank;
        
        //.order domiciles:
        public static final String ORDER_DOMICILE_INDONESIA                     = "I";
        public static final String ORDER_DOMICILE_ASING                         = "A";
        public static final String ORDER_DOMICILE_SENDIRI                       = "S";
        public static final String ORDER_DOMICILE_FOREIGN                       = "F";
        
        //.trade indicator:
        public static final String TRADE_INDICATOR_REGULAR                      = "R";
        public static final String TRADE_INDICATOR_UNINTENTIONAL_SELF_CROSS     = "U";
        public static final String TRADE_INDICATOR_NEGDEAL                      = "N";
        
        //.printable execution indicator:
        public static final String PRINTABLE_EXECUTION_INDICATOR_NON_PRINTABLE  = "N";
        public static final String PRINTABLE_EXECUTION_INDICATOR_PRINTABLE      = "Y";
        
        //.broken trade reasons:
        public static final String BROKEN_TRADE_REASON_SUPERVISORY              = "S";
        
        //.cross type codes:
        public static final String CROSS_TYPE_CODE_OPENING_AUCTION              = "P";
        public static final String CROSS_TYPE_CODE_INTRADAY_AUCTION             = "I";
        public static final String CROSS_TYPE_CODE_CLOSING_AUCTION              = "K";
        
        //.participant statuses:
        public static final String PARTICIPANT_STATUS_ACTIVE                    = "A";
        public static final String PARTICIPANT_STATUS_SUSPENDED                 = "S";
        
        
    }
    
    public class ITCHStateField {
        public static final String STATE_BREAK                                  = "Break";
        public static final String STATE_BREAK_CALL                             = "Break_Call";
        public static final String STATE_CALL_RANDOM_CLOSE                      = "Call_RandomClose";
        public static final String STATE_CLOSE_CALL_AUCTION                     = "Close_CallAuction";
        public static final String STATE_CLOSE_NG                               = "Close_NG";
        public static final String STATE_CLOSE_RF                               = "Close_RF";
        public static final String STATE_CLOSE_RG                               = "Close_RG";
        public static final String STATE_CLOSE_TN                               = "Close_TN";
        public static final String STATE_END_OF_DAY                             = "EndofDay";
        public static final String STATE_MATCHING_CALL_AUCTION                  = "Matching_CallAuction";
        public static final String STATE_MATCHING_CLOSE                         = "Matching_Close";
        public static final String STATE_MATCHING_PRE_OPEN                      = "Matching_PreOpen";
        public static final String STATE_NON_CANCEL                             = "NonCancel";
        public static final String STATE_POST_TRADE                             = "PostTrade";
        public static final String STATE_PRE_CLOSE                              = "PreClose";
        public static final String STATE_PRE_OPEN                               = "PreOpen";
        public static final String STATE_RANDOM_CLOSE                           = "RandomClose";
        public static final String STATE_SESSION_1_NG                           = "Session_1_NG";
        public static final String STATE_SESSION_1_RF                           = "Session_1_RF";
        public static final String STATE_SESSION_1_RG                           = "Session_1_RG";
        public static final String STATE_SESSION_1_RG_CALL                      = "Session_1_RG_Call";
        public static final String STATE_SESSION_1_TN                           = "Session_1_TN";
        public static final String STATE_SESSION_1_TN_CALL                      = "Session_1_TN_Call";
        public static final String STATE_SESSION_2_NG                           = "Session_2_NG";
        public static final String STATE_SESSION_2_RF                           = "Session_2_RF";
        public static final String STATE_SESSION_2_RG                           = "Session_2_RG";
        public static final String STATE_SESSION_2_RG_CALL                      = "Session_2_RG_Call";
        public static final String STATE_SESSION_2_TN_CALL                      = "Session_2_TN_Call";
        public static final String STATE_SESSION_3_RG_CALL                      = "Session_3_RG_Call";
        public static final String STATE_SESSION_3_TN_CALL                      = "Session_3_TN_Call";
        public static final String STATE_SESSION_4_RG_CALL                      = "Session_4_RG_Call";
        public static final String STATE_SESSION_5_RG_CALL                      = "Session_5_RG_Call";
        public static final String STATE_SOBD                                   = "SOBD";
        public static final String STATE_SUSPEND                                = "SuspendKeepOrder";
        public static final String STATE_TRADING_HALT                           = "Trading_Halt";
        public static final String STATE_UNSUSPEND                              = "Unsuspend";
    }
    
    public class ITCHStateDesc {
        public static final String STATE_BREAK                                  = "Break Session";
        public static final String STATE_BREAK_CALL                             = "Break Session Call Auction";
        public static final String STATE_CALL_RANDOM_CLOSE                      = "Random Closing at Call Auction";
        public static final String STATE_CLOSE_CALL_AUCTION                     = "Closing at Call Auction";
        public static final String STATE_CLOSE_NG                               = "Close Negotiation";
        public static final String STATE_CLOSE_RF                               = "Close Regular Futures";
        public static final String STATE_CLOSE_RG                               = "Close Regular";
        public static final String STATE_CLOSE_TN                               = "Close Cash";
        public static final String STATE_END_OF_DAY                             = "End of Day";
        public static final String STATE_MATCHING_CALL_AUCTION                  = "Matching at Call Auction";
        public static final String STATE_MATCHING_CLOSE                         = "Matching at Closing Regular";
        public static final String STATE_MATCHING_PRE_OPEN                      = "Matching at Pre-Opening";
        public static final String STATE_NON_CANCEL                             = "Non cancellation";
        public static final String STATE_POST_TRADE                             = "Post Trading";
        public static final String STATE_PRE_CLOSE                              = "Pre-Closing";
        public static final String STATE_PRE_OPEN                               = "Pre-Opening";
        public static final String STATE_RANDOM_CLOSE                           = "Random Closing";
        public static final String STATE_SESSION_1_NG                           = "Session 1 NG";
        public static final String STATE_SESSION_1_RF                           = "Session 1 Regular Futures";
        public static final String STATE_SESSION_1_RG                           = "Session 1 Regular";
        public static final String STATE_SESSION_1_RG_CALL                      = "Call Auction Session 1";
        public static final String STATE_SESSION_1_TN                           = "Session 1 Cash";
        public static final String STATE_SESSION_1_TN_CALL                      = "Call Auction Session 1 - TN";
        public static final String STATE_SESSION_2_NG                           = "Session 2 NG";
        public static final String STATE_SESSION_2_RF                           = "Session 2 Regular Futures";
        public static final String STATE_SESSION_2_RG                           = "Session 2 Regular";
        public static final String STATE_SESSION_2_RG_CALL                      = "Call Auction Session 2";
        public static final String STATE_SESSION_2_TN_CALL                      = "Call Auction Session 2 - TN";
        public static final String STATE_SESSION_3_RG_CALL                      = "Call Auction Session 3";
        public static final String STATE_SESSION_3_TN_CALL                      = "Call Auction Session 3 - TN";
        public static final String STATE_SESSION_4_RG_CALL                      = "Call Auction Session 4";
        public static final String STATE_SESSION_5_RG_CALL                      = "Call Auction Session 5";
        public static final String STATE_SOBD                                   = "Start of Business Date";
        public static final String STATE_SUSPEND                                = "Suspend with Cancel Orders";
        public static final String STATE_TRADING_HALT                           = "Trading Halt by Keeping Orders";
        public static final String STATE_UNSUSPEND                              = "Unsuspend";
    }
    
}
