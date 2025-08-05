/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.consts;

/**
 *
 * @author Ari Pambudi
 */
public class OUCHConsts {
    
    public class OUCHConnectionType {
        
        //.connection types:
        public static final String CONNECTIONTYPE_TRANSACTION                   = "TRANSACTION";
        
    }
    
    public class OUCHMessageType {
        
        //.message types:
        public static final String MESSAGETYPE_INBOUND_ENTER_ORDER              = "O";
        public static final String MESSAGETYPE_INBOUND_REPLACE_ORDER            = "U";
        public static final String MESSAGETYPE_INBOUND_CANCEL_ORDER             = "X";
        public static final String MESSAGETYPE_INBOUND_CANCEL_BY_ORDER_ID       = "Y";
        public static final String MESSAGETYPE_OUTBOUND_ACCEPTED                = "A";
        public static final String MESSAGETYPE_OUTBOUND_REJECTED_ORDER          = "J";
        public static final String MESSAGETYPE_OUTBOUND_REPLACED                = "U";
        public static final String MESSAGETYPE_OUTBOUND_CANCELED                = "C";
        public static final String MESSAGETYPE_OUTBOUND_EXECUTED_ORDER          = "E";
        
    }
    
    public class OUCHValue {
        
        //.order verbs:
        public static final String ORDER_VERB_BUY                               = "B";
        public static final String ORDER_VERB_SELL                              = "S";
        public static final String ORDER_VERB_SHORT_SELL                        = "T";
        public static final String ORDER_VERB_PRICE_STABILIZATION               = "P"; //.only buy;
        public static final String ORDER_VERB_MARGIN                            = "M"; //.only buy;
        
        //.order sources:
        public static final String ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE                          = "q";
        public static final String ORDER_SOURCE_INDIVIDUAL_INVESTOR_SHARIA                          = "r";
        public static final String ORDER_SOURCE_INSTITUTIONAL_INVESTOR_DMA                          = "s";
        public static final String ORDER_SOURCE_INSTITUTIONAL_INVESTOR_SHARIA                       = "t";
        public static final String ORDER_SOURCE_SALES_ONLINE                                        = "u";
        public static final String ORDER_SOURCE_SALES_SHARIA                                        = "v";
        public static final String ORDER_SOURCE_SALES_REMOTE                                        = "w";
        public static final String ORDER_SOURCE_DEALER_SHARIA                                       = "x";
        public static final String ORDER_SOURCE_DEALER_REMOTE                                       = "y";
        public static final String ORDER_SOURCE_HOUSE_REMOTE                                        = "z";
        public static final String ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE_AUTOMATED_ORDERING       = "Q";
        public static final String ORDER_SOURCE_INDIVIDUAL_INVESTOR_SHARIA_AUTOMATED_ORDERING       = "R";
        public static final String ORDER_SOURCE_INSTITUTIONAL_INVESTOR_DMA_AUTOMATED_ORDERING       = "S";
        public static final String ORDER_SOURCE_INSTITUTIONAL_INVESTOR_SHARIA_AUTOMATED_ORDERING    = "T";
        public static final String ORDER_SOURCE_SALES_ONLINE_AUTOMATED_ORDERING                     = "U";
        public static final String ORDER_SOURCE_SALES_SHARIA_AUTOMATED_ORDERING                     = "V";
        public static final String ORDER_SOURCE_SALES_REMOTE_AUTOMATED_ORDERING                     = "W";
        public static final String ORDER_SOURCE_DEALER_SHARIA_AUTOMATED_ORDERING                    = "X";
        public static final String ORDER_SOURCE_DEALER_REMOTE_AUTOMATED_ORDERING                    = "Y";
        public static final String ORDER_SOURCE_HOUSE_REMOTE_AUTOMATED_ORDERING                     = "Z";
        
        //.order domiciles:
        public static final String ORDER_DOMICILE_INDONESIA                     = "I";
        public static final String ORDER_DOMICILE_ASING                         = "A";
        public static final String ORDER_DOMICILE_SENDIRI                       = "S";
        public static final String ORDER_DOMICILE_FOREIGN                       = "F";
        
        //.time of forces:
//        public static final int TIME_OF_FORCE_SESSION                           = 99997;
//        public static final int TIME_OF_FORCE_DAY                               = 99998;
//        public static final int TIME_OF_FORCE_IMMEDIATE                         = 0;
        //.new time of forces:
        public static final int TIME_OF_FORCE_UNDEFINED                         = 0;
        public static final int TIME_OF_FORCE_DAY                               = 1;
        public static final int TIME_OF_FORCE_GTC                               = 2;
        public static final int TIME_OF_FORCE_FAK                               = 3;
        public static final int TIME_OF_FORCE_FOK                               = 4;
        public static final int TIME_OF_FORCE_GTS                               = 5;
        public static final int TIME_OF_FORCE_DAYS                              = 6;
        
        //.open close
        public static final int OPEN_CLOSE_DEFAULT                              = 0;
        public static final int OPEN_CLOSE_OPEN                                 = 1;
        public static final int OPEN_CLOSE_CLOSE                                = 2;
        public static final int OPEN_CLOSE_MANDATORY_CLOSE                      = 3;
        
        //.order type 
        public static final int ORDER_TYPE_LIMIT                                = 1;
        public static final int ORDER_TYPE_MARKET                               = 2;
        public static final int ORDER_TYPE_MARKET_TO_LIMIT                      = 3;
        public static final int ORDER_TYPE_BEST_ORDER                           = 4;
        public static final int ORDER_TYPE_IMBALANCE                            = 5;
        
        //.order capacity
        public static final int ORDER_CAPACITY_UNDEFINED                        = 0;
        public static final int ORDER_CAPACITY_AGENCY                           = 1;
        public static final int ORDER_CAPACITY_PROPRIETARY                      = 2;
        public static final int ORDER_CAPACITY_INDIVIDUAL                       = 3;
        public static final int ORDER_CAPACITY_PRINCIPAL                        = 4;
        public static final int ORDER_CAPACITY_RISK_LESS_PRINCIPAL              = 5;
        
        //.attributes
        public static final int ATTRIBUTE_UNDEFINED                             = 0;
        public static final int ATTRIBUTE_MARKET_BID                            = 1;
        public static final int ATTRIBUTE_PRICE_STABILIZATION                   = 2;
        public static final int ATTRIBUTE_MARGIN                                = 3;
                
        //.trading limit types:
        public static final String TRADING_LIMIT_TYPE_POOL                      = "P";
        public static final String TRADING_LIMIT_TYPE_EQUITY                    = "E";
        public static final String TRADING_LIMIT_TYPE_DERIVATIVE                = "D";
        
        //.system event codes:
        public static final String SYSTEM_EVENT_CODE_START_OF_DAY               = "S";
        public static final String SYSTEM_EVENT_CODE_END_OF_DAY                 = "E";
        
        //.accepted order sources:
        public static final String ACCEPTED_ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE                             = "a";
        public static final String ACCEPTED_ORDER_SOURCE_INDIVIDUAL_INVESTOR_SHARIA                             = "b";
        public static final String ACCEPTED_ORDER_SOURCE_INSTITUTIONAL_INVESTOR_DMA                             = "c";
        public static final String ACCEPTED_ORDER_SOURCE_INSTITUTIONAL_INVESTOR_SHARIA                          = "d";
        public static final String ACCEPTED_ORDER_SOURCE_SALES_ONLINE                                           = "e";
        public static final String ACCEPTED_ORDER_SOURCE_SALES_SHARIA                                           = "f";
        public static final String ACCEPTED_ORDER_SOURCE_SALES_REMOTE                                           = "g";
        public static final String ACCEPTED_ORDER_SOURCE_DEALER_SHARIA                                          = "h";
        public static final String ACCEPTED_ORDER_SOURCE_DEALER_REMOTE                                          = "i";
        public static final String ACCEPTED_ORDER_SOURCE_HOUSE_REMOTE                                           = "j";
        public static final String ACCEPTED_ORDER_SOURCE_INDIVIDUAL_INVESTOR_ONLINE_AUTOMATED_ORDERING          = "A";
        public static final String ACCEPTED_ORDER_SOURCE_INDIVIDUAL_INVESTOR_SHARIA_AUTOMATED_ORDERING          = "B";
        public static final String ACCEPTED_ORDER_SOURCE_INSTITUTIONAL_INVESTOR_DMA_AUTOMATED_ORDERING          = "C";
        public static final String ACCEPTED_ORDER_SOURCE_INSTITUTIONAL_INVESTOR_SHARIA_AUTOMATED_ORDERING       = "D";
        public static final String ACCEPTED_ORDER_SOURCE_SALES_ONLINE_AUTOMATED_ORDERING                        = "E";
        public static final String ACCEPTED_ORDER_SOURCE_SALES_SHARIA_AUTOMATED_ORDERING                        = "F";
        public static final String ACCEPTED_ORDER_SOURCE_SALES_REMOTE_AUTOMATED_ORDERING                        = "G";
        public static final String ACCEPTED_ORDER_SOURCE_DEALER_SHARIA_AUTOMATED_ORDERING                       = "H";
        public static final String ACCEPTED_ORDER_SOURCE_DEALER_REMOTE_AUTOMATED_ORDERING                       = "I";
        public static final String ACCEPTED_ORDER_SOURCE_HOUSE_REMOTE_AUTOMATED_ORDERING                        = "J";
        
        //.order states:
        public static final String ORDER_STATE_LIVE                             = "L";
        public static final String ORDER_STATE_DEAD                             = "D";
        
        //.order states new:
        public static final String ORDER_STATE_ON_BOOK                             = "1";
        public static final String ORDER_STATE_NOT_ON_BOOK                         = "2";
        
        //.price flags:
        public static final int PRICE_FLAG_FOR_MARKET_ORDER                     = 0x7FFFFFFF; //.0x7FFFFFFF (hex) or 2147483647 (dec) for a ‘market’ order.
        
        //.cancelled reasons:
        public static final String CANCELLED_ORDER_REASON_USER_REQUESTED                    = "U";
        public static final String CANCELLED_ORDER_REASON_TIMEOUT_ORDER_EXPIRED             = "T";
        public static final String CANCELLED_ORDER_REASON_SUPERVISORY                       = "S";
        public static final String CANCELLED_ORDER_REASON_USER_LOGGED_OFF                   = "L";
        public static final String CANCELLED_ORDER_REASON_INVALID_QUANTITY_OR_EXCEEDED      = "Z";
        public static final String CANCELLED_ORDER_REASON_ORDER_NOT_ALLOWED_THIS_TIME       = "R";
        public static final String CANCELLED_ORDER_REASON_INVALID_PRICE                     = "X";
        public static final String CANCELLED_ORDER_REASON_INVALID_ORDER_TYPE                = "Y";
        public static final String CANCELLED_ORDER_REASON_UNKNOWN                           = "W";
        //.tambahan Ardi:20230919
        public static final String CANCELLED_ORDER_REASON_IMMEDIATE                         = "I";
        
        
        //.liquidity flags:
        public static final String LIQUIDITY_FLAG_ADDED_FOR_PASSIVE_FIRM                    = "A";
        public static final String LIQUIDITY_FLAG_REMOVED_FOR_AGGRESSOR                     = "R";
        public static final String LIQUIDITY_FLAG_UNCROSS_FOR_AUCTION_EXECUTION             = "U";
        
        //.broken trade reasons:
        public static final String BROKEN_TRADE_REASON_CONSENT                  = "C";
        public static final String BROKEN_TRADE_REASON_SUPERVISORY              = "S";
        
        //.rejected order reasons:
        public static final String REJECTED_ORDER_REASON_ORDERBOOK_INSTRUMENT_BOARD_MARKET_NOT_TRADEABLE    = "H";
        public static final String REJECTED_ORDER_REASON_INVALID_QUANTITY_OR_EXCEEDED                       = "Z";
        public static final String REJECTED_ORDER_REASON_INVALID_ORDERBOOK_IDENTIFIER                       = "S";
        public static final String REJECTED_ORDER_REASON_ORDER_NOT_ALLOWED_THIS_TIME                        = "R";
        public static final String REJECTED_ORDER_REASON_INVALID_PRICE                                      = "X";
        public static final String REJECTED_ORDER_REASON_INVALID_ORDER_TYPE                                 = "Y";
        public static final String REJECTED_ORDER_REASON_FLOW_CONTROL_IN_PLACE_FOR_USER                     = "F";
        public static final String REJECTED_ORDER_REASON_ORDER_SOURCE_NOT_VALID                             = "B";
        public static final String REJECTED_ORDER_REASON_ORDER_EXCEEDED_FIRM_TRADING_LIMIT                  = "j";
        public static final String REJECTED_ORDER_REASON_UNKNOWN                                            = "W";
        //.tambahan Ardi:20230919
        public static final String REJECTED_ORDER_REASON_INVALID_MINIMUM_QUANTITY                           = "N";
        
        //.reject order reason new
        public static final int REJECTED_ORDER_REASON_AUTH_ILL_TRT_FOR_USER                                 = -80003;
        public static final int REJECTED_ORDER_REASON_ORDERBOOK_IS_CLOSED                                   = -80005;
        public static final int REJECTED_ORDER_REASON_AUTH_LIU_NOTFOU                                       = -80007;
        public static final int REJECTED_ORDER_REASON_AUTH_CLI_NOTFOU                                       = -80009;
        public static final int REJECTED_ORDER_REASON_AUTH_USER_NOT_ACTIVE                                  = -80013;
        public static final int REJECTED_ORDER_REASON_AUTH_PART_NOT_ACTIVE                                  = -80015;
        public static final int REJECTED_ORDER_REASON_AUTH_NOT_TRADED                                       = -80017;
        public static final int REJECTED_ORDER_REASON_AUTH_OBL_NOTFOU                                       = -80021;
        public static final int REJECTED_ORDER_REASON_AUTH_TRC_INS_NOTFOUND                                 = -80023;
        public static final int REJECTED_ORDER_REASON_AUTH_TRC_SST_NOTFOUND                                 = -80025;
        public static final int REJECTED_ORDER_REASON_AUTH_TRC_UST_NOTFOUND                                 = -80027;
        public static final int REJECTED_ORDER_REASON_AUTH_LIU_NOTFOU_BID                                   = -80031;
        public static final int REJECTED_ORDER_REASON_AUTH_LIU_NOTFOU_ASK                                   = -80033;
        public static final int REJECTED_ORDER_REASON_OUCH_DUPLICATE_TOKEN                                  = -800002;
        public static final int REJECTED_ORDER_REASON_OUCH_UNKNOWN_TOKEN                                    = -800004;
        public static final int REJECTED_ORDER_REASON_OUCH_INVALID_ORDERBOOK                                = -800006;
        public static final int REJECTED_ORDER_REASON_OUCH_INVALID_SIDE                                     = -800008;
        public static final int REJECTED_ORDER_REASON_OUCH_INVALID_TIF                                      = -800010;
        public static final int REJECTED_ORDER_REASON_OUCH_CAN_NOT_CANCEL                                   = -800014;
        public static final int REJECTED_ORDER_REASON_OUCH_THROTTLING                                       = -800020;
        public static final int REJECTED_ORDER_REASON_ME_MARKET_MAKER_PROTECTION                            = -405044;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_BID_ASK                                      = -420023;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_VALIDITY                                     = -420025;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_MARBOUNCE                                    = -420027;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_PREMIUM                                      = -420029;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_QUANTITY                                     = -420045;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_ILL_ORDER_TYPE                               = -420076;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_MKT_ORDER_PRICE                              = -420078;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_BLOCK_MAX_LEGS                               = -420093;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_BLOCK_SERIES                                 = -420095;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_MEM_CRS_NOT_ALLOWED                          = -420129;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_PRICE_LIMIT                                  = -420131;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INV_STP_COND                                 = -420133;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INV_HIDDEN                                   = -420135;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INV_SHOWN                                    = -420137;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_NOT_AUTH                                     = -420139;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SHOWN_TOO_SMALL                              = -420141;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_NO_WILD_CARD                                 = -420153;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INV_OP_CLS_REQ                               = -420155;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_ORD_NOT_FOU                                  = -420177;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_FILL_OR_KILL                             = -420193;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_FILL_OR_KILL                             = -420195;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_FILL_OR_KILL                             = -420197;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_IMMEDIATE_OR_CANCEL                      = -420199;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_IMMEDIATE_OR_CANCEL                      = -420201;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_IMMEDIATE_OR_CANCEL                      = -420203;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_FILL_AND_STORE                           = -420205;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_FILL_AND_STORE                           = -420207;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_FILL_AND_STORE                           = -420209;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_LIMIT_ORD                                = -420211;
        public static final int REJECTED_ORDER_REASON_LIMIT_ORDER_NOT_ALLOWED_THIS_TIME                     = -420213;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_LIMIT_ORD                                = -420215;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_MARKET_ORD                               = -420217;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_MARKET_ORD                               = -420219;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_MARKET_ORD                               = -420221;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_ALL_OR_NONE                              = -420223;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_ALL_OR_NONE                              = -420225;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_ALL_OR_NONE                              = -420227;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_ILL_ORDER_TYPE_INT                           = -420237;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_IMBALANCE                                = -420245;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_IMBALANCE                                = -420247;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_IMBALANCE                                = -420249;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_MTL_ROUND_LOT                            = -420251;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_MTL_ROUND_LOT                            = -420253;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_MTL_ROUND_LOT                            = -420255;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_HIDDEN_AGGRESSIVE                        = -420287;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_HIDDEN_AGGRESSIVE                        = -420289;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_HIDDEN_AGGRESSIVE                        = -420291;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_MIN_BLK_SIZE                                 = -420311;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_MAX_BLK_SIZE                                 = -420313;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_DECR_SHOWN_NOHIDD                        = -420315;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_DECR_SHOWN                               = -420317;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_DECR_HIDD                                = -420319;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_INCR_SHOWN                               = -420321;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_INCR_HIDD                                = -420323;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_CLIENT                                   = -420325;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_PRICE_IMPR                               = -420327;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_PRICE_DISIMPR                            = -420329;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_TIME_EXT                                 = -420333;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_TIME_DECR                                = -420335;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_EXCH_ORDER_TYPE                          = -420339;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_DECR_SHOWN_NOHIDD                        = -420343;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_DECR_SHOWN                               = -420345;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_DECR_HIDD                                = -420347;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_INCR_SHOWN                               = -420349;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_INCR_HIDD                                = -420351;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_CLIENT                                   = -420353;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_PRICE_IMPR                               = -420355;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_PRICE_DISIMPR                            = -420357;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_TIME_EXT                                 = -420361;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_TIME_DECR                                = -420363;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_PST_ORDER                                = -420371;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_PST_ORDER                                = -420373;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_PST_ORDER                                = -420375;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_SHORT_ORDER                              = -420377;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_SHORT_ORDER                              = -420379;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_SHORT_ORDER                              = -420381;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_MB_ORDER                                 = -420383;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_MB_ORDER                                 = -420385;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_MB_ORDER                                 = -420387;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_SST_BL_ORDER                                 = -420389;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_INS_BL_ORDER                                 = -420391;
        public static final int REJECTED_ORDER_REASON_ME_MATCH_USR_BL_ORDER                                 = -420393;
//        public static final int REJECTED_ORDER_REASON_                                                      = -80000;
        
        
        //.Ardi:20230919 - restatement reason: 
        public static final String RESTATEMENT_REASON_PRICE_UPDATE_FROM_MARKET_TO_LIMIT                     = "P";
        
        //.trading limit not set:
        public static final long TRADING_LIMIT_NOT_SET                          = 0x7FFFFFFFFFFFFFFFL;
        
        
    }
    
    public class OUCHEngineSetup {
        public final static boolean VERSION_MARKET_ORDER                        = true;
    }
    
}
