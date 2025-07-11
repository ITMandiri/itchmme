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
        public static final int TIME_OF_FORCE_SESSION                           = 99997;
        public static final int TIME_OF_FORCE_DAY                               = 99998;
        public static final int TIME_OF_FORCE_IMMEDIATE                         = 0;
        
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
        
        //.Ardi:20230919 - restatement reason: 
        public static final String RESTATEMENT_REASON_PRICE_UPDATE_FROM_MARKET_TO_LIMIT                     = "P";
        
        //.trading limit not set:
        public static final long TRADING_LIMIT_NOT_SET                          = 0x7FFFFFFFFFFFFFFFL;
        
        
    }
    
    public class OUCHEngineSetup {
        public final static boolean VERSION_MARKET_ORDER                        = true;
    }
    
}
