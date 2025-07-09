/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.ori.consts;

import com.itm.idx.data.jonec.consts.JonecConst;

/**
 *
 * @author aripam
 */
public class ORIDataConst {
    
    public class ORIFieldFmt{
        public static final String KV_SEPARATOR                                         = "="; //.key-field separator.
        public static final String HEADER_FIELD_SEPARATOR                               = "\003"; //.header field separator.
        public static final String FIELD_SEPARATOR                                      = "\001"; //.field separator.
        public static final String NULL_SEPARATOR                                       = "\000"; //.null separator.
        public static final String FIND_FIRST_TAG_WITH_NULL                             = NULL_SEPARATOR + ORIFieldTag.BEGINSTRING + KV_SEPARATOR;
        public static final String FIND_FIRST_TAG                                       = ORIFieldTag.BEGINSTRING + KV_SEPARATOR;
        public static final String FIND_LAST_TAG                                        = FIELD_SEPARATOR + ORIFieldTag.CHECKSUM + KV_SEPARATOR;
        public static final String FIND_LAST_TAG_SERVER_SIM                             = FIELD_SEPARATOR + HEADER_FIELD_SEPARATOR;
        
    }
    
    public class ORIFieldTag{
        
        //.header:
        public static final String BEGINSTRING                                          = "8";
        public static final String BODYLENGTH                                           = "9";
        public static final String MSGSEQNUM                                            = "34";
        public static final String MSGTYPE                                              = "35";
        public static final String SENDERCOMPID                                         = "49";
        public static final String SENDERSUBID                                          = "50";
        public static final String SENDINGTIME                                          = "52";
        public static final String TARGETCOMPID                                         = "56";
        public static final String SENDERLOCATIONID                                     = "142";
        //.data:
        public static final String ACCOUNT                                              = "1";
        public static final String AVGPX                                                = "6";
        public static final String CHECKSUM                                             = "10";
        public static final String CLORDID                                              = "11";
        public static final String CUMQTY                                               = "14";
        public static final String EXECID                                               = "17";
        public static final String EXECINST                                             = "18";
        public static final String EXECREFID                                            = "19";
        public static final String EXECTRANSTYPE                                        = "20";
        public static final String HANDLINST                                            = "21";
        public static final String IOIID                                                = "23";
        public static final String LASTPX                                               = "31";
        public static final String LASTSHARES                                           = "32";
        public static final String ORDERID                                              = "37";
        public static final String ORDERQTY                                             = "38";
        public static final String ORDSTATUS                                            = "39";
        public static final String ORDTYPE                                              = "40";
        public static final String ORIGCLORDID                                          = "41";
        public static final String PRICE                                                = "44";
        public static final String SECURITYID                                           = "48";
        public static final String SIDE                                                 = "54";
        public static final String SYMBOL                                               = "55";
        public static final String TEXT                                                 = "58";
        public static final String TIMEINFORCE                                          = "59";
        public static final String TRANSACTTIME                                         = "60";
        public static final String VALIDUNTILTIME                                       = "62";
        public static final String SETTLDATE                                            = "64";
        public static final String SYMBOLSFX                                            = "65";
        public static final String ENCRYPTMETHOD                                        = "98";
        public static final String STOPPX                                               = "99";
        public static final String HEARTBTINT                                           = "108";
        public static final String CLIENTID                                             = "109";
        public static final String QUOTEID                                              = "117";
        public static final String BIDPX                                                = "132";
        public static final String OFFERPX                                              = "133";
        public static final String BIDSIZE                                              = "134";
        public static final String OFFERSIZE                                            = "135";
        public static final String EXECTYPE                                             = "150";
        public static final String LEAVESQTY                                            = "151";
        public static final String SETTLDELIVERYTYPE                                    = "172";
        public static final String SECONDARYORDERID                                     = "198";
        public static final String NOQUOTEENTRIES                                       = "295";
        public static final String QUOTEACKSTATUS                                       = "297";
        public static final String QUOTECANCELTYPE                                      = "298";
        public static final String QUOTEREJECTREASON                                    = "300";
        public static final String TRADINGSESSIONID                                     = "336";
        public static final String TRADSESSTATUS                                        = "340";
        public static final String COMPLIANCEID                                         = "376";
        public static final String EXPIREDATE                                           = "432";
        public static final String CXLREJRESPONSETO                                     = "434";
        public static final String CLEARINGACCOUNT                                      = "440";
        public static final String USERID                                               = "6001";
        public static final String CURRENTPASSWORD                                      = "6002";
        public static final String NEWPASSWORD                                          = "6003";
        public static final String RETURNVALUE                                          = "6004";
        public static final String ERRORCODE                                            = "6063";
        public static final String ERRORLEVEL                                           = "6064";
        public static final String REFERRORCODE                                         = "6065";
        public static final String PREVIOUSREQUEST                                      = "6066";
        public static final String MASSWITHDRAWREQUESTTYPE                              = "6067";
        public static final String MASSWITHDRAWRESPONSE                                 = "6068";
        public static final String MASSWITHDRAWREJECTREASON                             = "6069";
        public static final String LOGONREPLY                                           = "6090";
        public static final String LOGONDESC                                            = "6091";
        
    }
    
    public class ORIMsgType{
        
        //.administrative:
        public static final String LOGON                                                = "A";
        public static final String LOGOUT                                               = "5";
        public static final String CHANGE_PASSWORD                                      = "U1";
        
        //.application:
        public static final String EXECUTION_REPORT                                     = "8";
        public static final String ORDER_CANCEL_REJECT                                  = "9";
        public static final String QUOTE_ACKNOWLEDGEMENT                                = "b";
        public static final String TRADING_INFO                                         = "h";
        public static final String ORDER_MASS_CANCEL_REPLY                              = "r1";
        public static final String NEGDEAL_REMINDER                                     = "U3";
        public static final String ERROR_MESSAGE                                        = "U7";
        public static final String NEW_ORDER                                            = "D";
        public static final String ORDER_CANCEL_REQUEST                                 = "F";
        public static final String ORDER_CANCEL_REPLACE                                 = "G";
        public static final String ORDER_MASS_CANCEL_REQUEST                            = "q1";
        public static final String QUOTE                                                = "S";
        public static final String QUOTE_CANCEL                                         = "Z";
        
        //.unknown-tambahan:
        public static final String UNKNOWN_HEART_BEAT                                   = "0";
        
        
    }
    
    public class ORIFieldValueLength{
        
        public static final int ADMIN_USERID                                            = 12;
        public static final int ADMIN_PASSWORD                                          = 10;
        public static final int CHECKSUM                                                = 3;
        
        public static final int ORDER_CANCEL_SUB_TEXT_OPERATION                         = 1;
        public static final int ORDER_CANCEL_SUB_TEXT_PRICE_OPERATION_CODE              = 1;
        public static final int ORDER_CANCEL_SUB_TEXT_PRICE                             = 10;
        public static final int ORDER_CANCEL_SUB_TEXT_USERID                            = 12;
        public static final int ORDER_CANCEL_SUB_TEXT_BROKERID                          = 12;
        
        public static final int ORDER_NEGDEAL_SUB_TEXT_COUNTERPART_USERID               = 12;
        public static final int ORDER_NEGDEAL_SUB_TEXT_MATCH_REFERENCE                  = 10;
        public static final int ORDER_NEGDEAL_SUB_TEXT_COUNTERPART_ACCOUNT              = 12;
        public static final int ORDER_NEGDEAL_SUB_TEXT_REMINDER_TIME_INTERVAL           = 4;
        public static final int ORDER_NEGDEAL_SUB_TEXT_COUNTERPART_BROKER_REF           = 20;
        public static final int ORDER_NEGDEAL_SUB_TEXT_COUNTERPART_TRADING_ID           = 15;
        public static final int ORDER_NEGDEAL_CANCEL_SUB_TEXT_USERID                    = 12;
        public static final int ORDER_NEGDEAL_CANCEL_SUB_TEXT_BROKERID                  = 12;
        public static final int ORDER_NEGDEAL_AMEND_SUB_TEXT_COUNTERPART_USERID         = 12;
        public static final int ORDER_NEGDEAL_AMEND_SUB_TEXT_REMINDER_TIME_INTERVAL     = 4;
        
    }
    
    public class ORIFieldValue{
        //.bundle:
        public static final String BUNDLE_MESSAGE_VERSION                               = "IDXEQ";
        public static final String BUNDLE_CONNECTION_NAME_JONES_1                       = JonecConst.DEFAULT_EXEC_BROKER_CODE + "_01_001_toJONES"; //. AB_01_001_toJONES
        public static final String BUNDLE_CONNECTION_NAME_JONES_2                       = JonecConst.DEFAULT_EXEC_BROKER_CODE + "_01_002_toJONES"; //. AB_01_002_toJONES
        public static final String BUNDLE_POS_DUP                                       = "N";
        
        public static final String NEW_LINE                                             = "\r\n";
        public static final String SINGLE_SPACE                                         = " ";
        public static final String EMPTY_STRING                                         = "";
        
        
        //.header:
        public static final String FIX_ENGINE_VERSION                                   = "FIX.4.2-JSX";
        
        //.administrative:
        public static final int ADMIN_ENCRYPT_METHOD                                    = 0;
        public static final int ADMIN_HEARTBEAT_TIME                                    = 45;
        public static final int ADMIN_LOGON_REPLY_PWD_OK                                = 1;
        public static final int ADMIN_LOGON_REPLY_PWD_NEAR_EXPIRE                       = 2; //.apm:20190903:v210:= Current password expires in n day(s) or Password-change-required (should change password before order entry)
        public static final int ADMIN_LOGON_REPLY_PWD_HAS_EXPIRED                       = 3;
        
        //.application:
        public static final String SYMBOLSFX_PREFIX                                     = "0"; //.0RG/0TN/0NG/(NEW:20190903)0OD
        
        public static final String BOARD_RG                                             = "RG"; //.Regular.
        public static final String BOARD_TN                                             = "TN"; //.Tunai.
        public static final String BOARD_NG                                             = "NG"; //.Negosiasi.
        public static final String BOARD_TS                                             = "TS"; //.???.
        public static final String BOARD_OD                                             = "OD"; //.???:(NEW:20190903)Odd-Lot Regular Market.
        
        public static final String STOKTYPE_ORDI                                        = "ORDI"; //.apm:20210927:new;
        public static final String STOKTYPE_ORDI_PREOPEN                                = "ORDI_PREOPEN"; //.apm:20210927:new;
        public static final String STOKTYPE_WARI                                        = "WARI"; //.apm:20210927:new;
        public static final String STOKTYPE_ACCEL                                       = "ACCEL"; //.apm:20210927:new;
        public static final String STOKTYPE_MUTI                                        = "MUTI"; //.hrn:20211201:new;
        public static final String STOKTYPE_RGHI                                        = "RGHI"; //.hrn:20211201:new;
        
        public static final String ACCOUNT_A                                            = "A"; //.Asing.
        public static final String ACCOUNT_I                                            = "I"; //.Indonesia.
        public static final String ACCOUNT_S                                            = "S"; //.Sendiri.
        public static final String ACCOUNT_F                                            = "F"; //.???
        
        public static final int HANDLINST_NORMAL                                        = 1;
        public static final int HANDLINST_ADVERTISEMENT                                 = 2;
        public static final int HANDLINST_NEGOTIATIONDEAL                               = 3;
        
        public static final String SIDE_BUY                                             = "1";
        public static final String SIDE_SELL                                            = "2";
        public static final String SIDE_BUY_MINUS                                       = "3";
        public static final String SIDE_SELL_PLUS                                       = "4";
        public static final String SIDE_SELL_SHORT                                      = "5";
        public static final String SIDE_SELL_SHORT_EXEMPT                               = "6";
        public static final String SIDE_UNDISCLOSED                                     = "7";
        public static final String SIDE_CROSS                                           = "8";
        public static final String SIDE_CROSS_SHORT                                     = "9";
        public static final String SIDE_CROSS_SHORT_EXEMPT                              = "A";
        public static final String SIDE_AS_DEFINED                                      = "B";
        public static final String SIDE_OPPOSITE                                        = "C";
        public static final String SIDE_SUBSCRIBE                                       = "D";
        public static final String SIDE_REDEEM                                          = "E";
        public static final String SIDE_LEND                                            = "F";
        public static final String SIDE_BORROW                                          = "G";
        public static final String SIDE_MARGIN_RESPONSE                                 = "H";
        public static final String SIDE_MARGIN_REQUEST                                  = "M";
        public static final String SIDE_PRICE_STABILIZATION                             = "P";
        
        
        public static final int EXECTRANSTYPE_NEW                                       = 0;
        public static final int EXECTRANSTYPE_CANCEL                                    = 1;
        public static final int EXECTRANSTYPE_CORRECT                                   = 2;
        public static final int EXECTRANSTYPE_STATUS                                    = 3;
        
        public static final int EXECTYPE_NEW                                            = 0;
        public static final int EXECTYPE_CANCELLED                                      = 4;
        public static final int EXECTYPE_REPLACEMENT                                    = 5;
        public static final int EXECTYPE_REJECTED                                       = 8;
        
        public static final int ORDSTATUS_NEW                                           = 0;
        public static final int ORDSTATUS_PARTIALLY_MATCH                               = 1;
        public static final int ORDSTATUS_FULLY_MATCH                                   = 2;
        public static final int ORDSTATUS_CANCELLED                                     = 4;
        public static final int ORDSTATUS_REPLACED                                      = 5;
        public static final int ORDSTATUS_REJECTED                                      = 8;
        
        public static final String ORDERID_NO_JATS_ORDERNUMBER                          = "000000000000";                  
        public static final String ORDERID_JATS_ORDERNUMBER_SPECIFY_NONE                = "NONE";                  
        
        public static final int CXLREJECTRESPONSETO_ORDER_CANCEL_REQUEST                = 1;
        public static final int CXLREJECTRESPONSETO_ORDER_REPLACE_REQUEST               = 2;
        
        public static final long ORDERQTY_WITHDRAW_NORMAL_ORDER                         = 1;
        public static final long ORDERQTY_WITHDRAW_ADVERTISEMENT                        = 2;
        public static final long ORDERQTY_WITHDRAW_NEGDEAL                              = 2;
        
        public static final String MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_SECURITY                = "1";
        public static final String MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_UNDERLYING_SECURITY     = "2"; //.unused.
        public static final String MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_SECURITY_TYPE           = "5"; //.unused.
        public static final String MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_TRADING_SESSION         = "6"; //.unused.
        public static final String MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_ALL_ORDERS_OR_FIRM      = "7";
        public static final String MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_PARTICULAR_BOARD        = "8";
        
        public static final String MASSWITHDRAWRESPONSE_CANCEL_ORDER_FOR_SECURITY                   = MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_SECURITY;
        public static final String MASSWITHDRAWRESPONSE_CANCEL_ORDER_FOR_UNDERLYING_SECURITY        = MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_UNDERLYING_SECURITY;
        public static final String MASSWITHDRAWRESPONSE_CANCEL_ORDER_FOR_SECURITY_TYPE              = MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_SECURITY_TYPE;
        public static final String MASSWITHDRAWRESPONSE_CANCEL_ORDER_FOR_TRADING_SESSION            = MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_TRADING_SESSION;
        public static final String MASSWITHDRAWRESPONSE_CANCEL_ORDER_FOR_ALL_ORDERS_OR_FIRM         = MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_ALL_ORDERS_OR_FIRM;
        public static final String MASSWITHDRAWRESPONSE_CANCEL_ORDER_FOR_PARTICULAR_BOARD           = MASSWITHDRAWREQUESTTYPE_CANCEL_ORDER_FOR_PARTICULAR_BOARD;
        
        public static final String MASSWITHDRAWREJECTREASON_CANCEL_NOT_SUPPORTED                    = "0";
        public static final String MASSWITHDRAWREJECTREASON_CANCEL_INVALID_UNKNOWN_SECURITY         = "1";
        public static final String MASSWITHDRAWREJECTREASON_CANCEL_INVALID_UNKNOWN_BOARD            = "98";
        public static final String MASSWITHDRAWREJECTREASON_CANCEL_OTHER                            = "99";
        
        public static final int QUOTECANCELTYPE_CANCEL_FOR_SECURITY                     = 1;
        public static final int QUOTECANCELTYPE_CANCEL_FOR_UNDERLYING_SECURITY          = 3;
        public static final int QUOTECANCELTYPE_CANCEL_ALL_QUOTES                       = 4;
        
        public static final String QUOTEACKSTATUS_NEW_ACCEPTED                          = "0";
        public static final String QUOTEACKSTATUS_NEW_REJECTED                          = "5";
        public static final String QUOTEACKSTATUS_CANCEL_FOR_SECURITY                   = "1";
        public static final String QUOTEACKSTATUS_CANCEL_FOR_UNDERLYING_SECURITY        = "3";
        public static final String QUOTEACKSTATUS_CANCEL_ALL_QUOTES                     = "4";
        
        public static final String QUOTEREJECTREASON_NO_ORDER_MATCHED                   = "250";
        public static final String QUOTEREJECTREASON_INVALID_BOARD                      = "2";
        public static final String QUOTEREJECTREASON_INVALID_SECURITY                   = "313";
        
        public static final String EXECINST_SPLIT                                       = "";
        
        public static final String TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_DAY                = "0";
        public static final String TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_SESSION            = "S";
        public static final String TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_IOC                = "3"; //.apm:20210707:new:
        public static final String TIMEINFORCE_LIMIT_OR_MARKET_SPLIT_FOK                = "4"; //.apm:20210707:new:
        
        public static final String ORDTYPE_MARKET_NONSTOP                               = "1"; //.apm:20210707:new:
        public static final String ORDTYPE_LIMIT_NONSTOP                                = "7";
        public static final String ORDTYPE_PREVIOUSLY_INDICATED                         = "E";
        
        public static final String COMPLIANCEID_AMEND                                   = "1";
        public static final String COMPLIANCEID_NONE                                    = "0";
        
        public static final String TRADINGSESSIONID                                     = "JSX";
        
        public static final int TRADINGSESSIONSTATUS_HALT                               = 1;
        public static final int TRADINGSESSIONSTATUS_OPEN                               = 2;
        public static final int TRADINGSESSIONSTATUS_CLOSE                              = 3;
        
        public static final int ERRORCODE_POSSIBLE_DUPLICATE_FIX_MESSAGE                = 1;
        public static final int ERRORCODE_UNKNOWN_JONEC_MESSAGE                         = 2;
        public static final int ERRORCODE_JATS_INTERPRETATION_ERROR_REPLY               = 5;
        public static final int ERRORCODE_JONEC_MESSAGE_INTERPRETATION_ERROR            = 3;
        public static final int ERRORCODE_BROADCAST_TABLE_FULL                          = 3;
        public static final int ERRORCODE_FAILED_SEND_HEARTBEAT_TO_JATS                 = 3;
        public static final int ERRORCODE_OTHERS_KNOWN                                  = 3;
        public static final int ERRORCODE_OTHERS_UNKNOWN                                = (-1);
        
        
        public static final String ORDER_CANCEL_SUB_TEXT_OPERATION                      = "A";
        public static final String ORDER_CANCEL_SUB_TEXT_PRICE_OPERATION_CODE           = "2";
        public static final String ORDER_CANCEL_SUB_TEXT_USERID                         = "";
        public static final String ORDER_CANCEL_SUB_TEXT_BROKERID                       = "A";
        
        public static final String SETTLDELIVERYTYPE_VERSUS                             = "0"; //.(NEW)apm:20190903:’Versus. Payment’: Deliver (if Sell) or Receive (if Buy) vs. (Againts) Payment;
        public static final String SETTLDELIVERYTYPE_FREE                               = "1"; //.(NEW)apm:20190903:’Free’: Deliver (if Sell) or Receive (if Buy) Free;
        
    }
    
}
