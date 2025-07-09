/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.consts;

/**
 *
 * @author Hirin
 */
public class QRIDataConst {
    
    public enum MsgType{
        Logon                   ("A"),
        News                    ("B"),
        Logout                  ("5"),
        ChangePass              ("U1"),
        Subscription            ("U2"),
        ExecutionReport         ("8"),
        SecurityReport          ("U4"),
        SecurityChangeReport    ("U5"),
        TradingLimit            ("U6"),
        TradingInfo             ("h"),
        GeneralError            ("U7"),
        Unknown_HeartBeat       ("0"),
        ITM                     ("ITM"); //. kusus itm
        
        public String value;

        private MsgType(String val) {
            this.value = val;
        }
        
        public String getValue(){
            return value;
        }
    }
    
    public static class QRITag{
        //. 
        public final static String BeginString              = "8";
        public final static String BodyLength               = "9";
        public final static String MsgType                  = "35";
        public final static String SenderCompID             = "49";
        public final static String TargetCompID             = "56";
        public final static String MsgSeqNum                = "34";
        public final static String SenderSubID              = "50";
        public final static String SenderLocID              = "142";
        public final static String SendingTime              = "52";
        //.
        public final static String Account                  = "1";
        public final static String AvgPx                    = "6";        
        public final static String ClOrdID                  = "11";
        public final static String CumQty                   = "14";
        public final static String Currency                 = "15";
        public final static String ExecID                   = "17";
        public final static String ExecInst                 = "18";
        public final static String ExecTransType            = "20";
        public final static String HandlInst                = "21";
        public final static String LastPx                   = "31";
        public final static String LinesOfText              = "33";
        public final static String OrderID                  = "37";
        public final static String OrderQty                 = "38";
        public final static String OrdStatus                = "39";
        public final static String OrdType                  = "40";
        public final static String OrigClOrdID              = "41";
        public final static String OrigTime                 = "42";
        public final static String Price                    = "44";
        public final static String SecurityID               = "48";
        public final static String Side                     = "54";
        public final static String Symbol                   = "55";
        public final static String Text                     = "58";
        public final static String TimeInForce              = "59";
        public final static String TransactTime             = "60";
        public final static String FutSettDate              = "64";
        public final static String SymbolSfx                = "65";
        public final static String TradedDate               = "75";
        public final static String ExecBroker               = "76";
        public final static String EncryptMethod            = "98";
        public final static String HeartBtInt               = "108";
        public final static String ClientID                 = "109";
        public final static String Headline                 = "148";
        public final static String ExecType                 = "150";
        public final static String LeavesQty                = "151";
        public final static String AccruedInterestRate      = "158";
        public final static String EffectiveTime            = "168";
        public final static String SettlDeliveryType        = "172";
        public final static String SecondaryOrderID         = "198";
        public final static String SecurityTradingStatus    = "326";
        public final static String TradingSessionID         = "336";
        public final static String ContraTrader             = "337";
        public final static String TradSesStatus            = "340";
        public final static String EncodedTextLen           = "354";
        public final static String EncodeText               = "355";
        public final static String ContraBroker             = "375";
        public final static String ComplianceID             = "376";
        public final static String NoContraBrokers          = "382";
        public final static String ExpireDate               = "432";
        public final static String ClearingAccount          = "440";
        public final static String UserID                   = "6001";
        public final static String CurrentPassword          = "6002";
        public final static String NewPassword              = "6003";
        public final static String ReturnValue              = "6004";
        public final static String RecordNumber             = "6005";
        public final static String Remarks                  = "6006";
        public final static String Shortname                = "6007";
        public final static String Status                   = "6008";
        public final static String MarketCode               = "6009";
        public final static String InstrCode                = "6010";
        public final static String SectorCode               = "6012";
        public final static String OrderMethods             = "6013";
        public final static String LotSize                  = "6014";
        public final static String MinimumStep              = "6015";
        public final static String FaceValue                = "6018";
        public final static String FaceUnit                 = "6019";
        public final static String Earnings                 = "6020";
        public final static String PreviousDate             = "6021";
        public final static String PreviousPrice            = "6022";
        public final static String PriceFormat              = "6023";
        public final static String Decimals                 = "6024";
        public final static String QuantityLimit            = "6025";
        public final static String VolTraded                = "6028";
        public final static String ValTraded                = "6029";
        public final static String Yield                    = "6030";
        public final static String PrimaryDist              = "6032";
        public final static String ForeignLimit             = "6033";
        public final static String ForeignLevel             = "6034";
        public final static String ForeignAvail             = "6035";
        public final static String Index                    = "6036";
        public final static String OpenOrders               = "6037";
        public final static String ListedSize               = "6038";
        public final static String TradeAbleSize            = "6039";
        public final static String AvgLast                  = "6040";
        public final static String ListingDate              = "6041";
        public final static String LastRecord               = "6042";
        public final static String SecIndex                 = "6044";
        public final static String NoSecCReport             = "6045";
        public final static String AttributeCode            = "6046";
        public final static String AttributeData            = "6047";
        public final static String CouponFreq               = "6048";
        public final static String ReqType                  = "6050";
        public final static String SeqNum                   = "6051";
        public final static String Last                     = "6052";
        public final static String Tag                      = "6054";
        public final static String Description              = "6055";
        public final static String OpenBal                  = "6056";
        public final static String CurrentPost              = "6057";
        public final static String PlannedPos               = "6058";
        public final static String Limit1                   = "6059";
        public final static String Limit1Set                = "6060";
        public final static String Limit2                   = "6061";
        public final static String Limit2Set                = "6062";
        public final static String ErrorCode                = "6063";
        public final static String ErrorLevel               = "6064";
        public final static String RefErrorCode             = "6065";
        public final static String PreviousRequest          = "6066";
        public final static String DelistingDate            = "6070";
        public final static String Remarks2                 = "6071";
        public final static String LogonReply               = "6090";
        public final static String LogonDesc                = "6091";
        
        public final static String ITMMsgType               = "9001";
        public final static String MsgSubType1              = "9002";
        public final static String MsgSubValue1             = "9003";
        public final static String MsgSubType2              = "9004";
        public final static String MsgSubValue2             = "9005";
        //.
        public final static String Checksum                 = "10";
    }
    
    public enum SymbolSfx{
        BoardRG                   ("0RG"),
        BoardTN                   ("0TN"),
        BoardNG                   ("0NG"),
        BoardTS                   ("0TS");
        
        
        public String value;

        private SymbolSfx(String val) {
            this.value = val;
        }
        
        public String getValue(){
            return value;
        }
    }
    
    public enum NegDealStatus{
        UNCONFIRMED_DEAL                    ("0", "Unconfirmed"),
        CONFIRMED_DEAL                      ("D", "Confirmed"),
        MATCHED_DEAL                        ("2", "Matched"),
        WITHDRAWN_DEAL                      ("4", "Withdrawn");
        
        
        public String value;
        public String desc;

        private NegDealStatus(String val, String desc) {
            this.value = val;
            this.desc = desc;
        }
        
        public String getValue(){
            return value;
        }
        
        public String getDesc(){
            return desc;
        }
    }
    
    public static final String HEADER_FIELD_SEPARATOR               = "\003"; //.header field separator.
    
    public static final String MESSAGE_INNER_TAG_SEPARATOR          = "=";
    public static final char MESSAGE_INTER_TAG_SEPARATOR            = 1;
    
    public final static String BAD_ORDER_ID                         = "000000000000";
    public final static String MARKET_CODE_EQUITY                   = "EQ";
    
    //.administrative:
    public static final int ADMIN_ENCRYPT_METHOD                                    = 0;
    public static final int ADMIN_HEARTBEAT_TIME                                    = 45;
    public static final int ADMIN_LOGON_REPLY_PWD_OK                                = 1;
    public static final int ADMIN_LOGON_REPLY_PWD_NEAR_EXPIRE                       = 2; //.apm:20190903:v210:= Current password expires in n day(s) or Password-change-required (should change password before order entry)
    public static final int ADMIN_LOGON_REPLY_PWD_HAS_EXPIRED                       = 3;

    public class QRIFieldValue{
        
        public final static String SIDE_BUY                         = "1";
        public final static String SIDE_SELL                        = "2";
        public final static String SIDE_SELL_SHORT                  = "5";
        public final static String SIDE_MARGIN                      = "M";
        public final static String SIDE_PRICE_STABILIZATION         = "P";
        
        public final static String SECURITY_INSTR_PRE_OPENING       = "ORDI_PREOPEN";
        public final static String SECURITY_PRE_OPENING             = "14";
        
        public final static int PRE_OPENING_ON                      = 1;
        public final static int PRE_OPENING_OFF                     = 2;
        
        public final static String SECURITY_STATUS_SUSPEND          = "S";
        public final static String SECURITY_STATUS_ACTIVE           = "A";
        
        public final static String REMARK_INFO_MARGINABLE           = "M";
        public final static String REMARK_INFO_MARGINABLE_SHORT     = "S";
        
        public final static int MARGINABLE_ON                       = 1;
        public final static int MARGINABLE_OFF                      = 0;
        
        public final static int BOARD_SET                           = 1;
        
        public final static int STOCK_REMARKS_SIZE                  = 8;
        public final static int STOCK_REMARKS2_SIZE                 = 40;
        public final static int STOCK_REMARKS2_EFFECTIVE_SIZE       = 20; //.apm:20200606:ref hrn: yg diterima tidak sampai 40 karakter;
        
        //.pos: 4th
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_NO_INFORMATION                    = "-";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_AGRICULTURE           = "1";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_MINING                = "2";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_BASIC_INDUSTRY_MANUF  = "3";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_MISCELLANEOUS_MANUF   = "4";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_CONSUMER_GOOD_MANUF   = "5";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_PROPERTY              = "6";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_INFRASTRUCTURE        = "7";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_FINANCE               = "8";
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_CONST_INDEX_TRADE                 = "9";
    
        //.index codes:
        public final static String SZREF_IDX_INDEXCODE_ALLREGULAR               = "ALLREGULAR";
        public final static String SZREF_IDX_INDEXCODE_COMPOSITE                = "COMPOSITE";
        public final static String SZREF_IDX_INDEXCODE_AGRI                     = "AGRI";
        public final static String SZREF_IDX_INDEXCODE_MINING                   = "MINING";
        public final static String SZREF_IDX_INDEXCODE_BASIC_IND                = "BASIC-IND";
        public final static String SZREF_IDX_INDEXCODE_MISC_IND                 = "MISC-IND";
        public final static String SZREF_IDX_INDEXCODE_CONSUMER                 = "CONSUMER";
        public final static String SZREF_IDX_INDEXCODE_PROPERTY                 = "PROPERTY";
        public final static String SZREF_IDX_INDEXCODE_INFRASTRUC               = "INFRASTRUC";
        public final static String SZREF_IDX_INDEXCODE_FINANCE                  = "FINANCE";
        public final static String SZREF_IDX_INDEXCODE_TRADE                    = "TRADE";
        public final static String SZREF_IDX_INDEXCODE_MANUFACTUR               = "MANUFACTUR"; //.manufactur = BASIC-IND + MISC-IND + CONSUMER
        public final static String SZREF_IDX_INDEXCODE_LQ45                     = "LQ45";
        public final static String SZREF_IDX_INDEXCODE_JII                      = "JII";
        public final static String SZREF_IDX_INDEXCODE_MBX                      = "MBX";
        public final static String SZREF_IDX_INDEXCODE_DBX                      = "DBX";
        public final static String SZREF_IDX_INDEXCODE_KOMPAS100                = "KOMPAS100";
        public final static String SZREF_IDX_INDEXCODE_BISNIS_27                = "BISNIS-27";
        public final static String SZREF_IDX_INDEXCODE_PEFINDO25                = "PEFINDO25";
        public final static String SZREF_IDX_INDEXCODE_SRI_KEHATI               = "SRI-KEHATI";
        public final static String SZREF_IDX_INDEXCODE_ISSI                     = "ISSI";
        
        
        public static final int EXECTRANSTYPE_STATUS                                    = 3;
        
        public static final int EXECTYPE_NEW                                            = 0;
        public static final int EXECTYPE_NORMAL_MATCH                                   = 2;
        public static final int EXECTYPE_CANCELLED                                      = 4;
        public static final int EXECTYPE_REPLACEMENT                                    = 5;
        public static final int EXECTYPE_REJECTED                                       = 8;
        
        public static final int ORDSTATUS_NEW                                           = 0;
        public static final int ORDSTATUS_PARTIALLY_MATCH                               = 1;
        public static final int ORDSTATUS_FULLY_MATCH                                   = 2;
        public static final int ORDSTATUS_CANCELLED                                     = 4;
        public static final int ORDSTATUS_REPLACED                                      = 5;
        public static final int ORDSTATUS_REJECTED                                      = 8;

    }
    
    public class QRIRemarks {
        //.values of remarks on stock data record:
        //.pos: 1st+2nd(remark)/1st+2nd(remark2)
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_NO_CORPORATE_ACTION              = "--"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_CUM_DIVIDEND                     = "CD"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_CUM_BONUS                        = "CB"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_CUM_RIGHT                        = "CR"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_ANY_COMBINATION_OF_CUM_REMARKS   = "CA"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_EX_DIVIDEND                      = "XD"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_EX_BONUS                         = "XB"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_EX_RIGHT                         = "XR"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_ANY_COMBINATION_OF_EX_REMARKS    = "XA"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_REVERSE_STOCK                    = "RS"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_12_STOCK_SPLIT                      = "SS"; //.apm:20190830:OLD+NEW(SAME);
        //.pos: 3rd(remark)/3th(remark2)
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_3_NO_INFORMATION                    = "-"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_3_MARGINABLE_SECURITIES             = "M"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_3_MARGINABLE_SECURITIES_SHORT_SELL  = "S"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_3_UNMARGINABLE_SECURITIES           = "U"; //.apm:20190830:OLD+NEW(SAME);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_3_DESIGNATED_STOCK                 = "D"; //.apm:20190830:NEW(ONLY);
        //.pos: 4th(remark)
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_NO_INFORMATION                     = "-"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_AGRICULTURE            = "1"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_MINING                 = "2"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_BASIC_INDUSTRY_MANUF   = "3"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_MISCELLANEOUS_MANUF    = "4"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_CONSUMER_GOOD_MANUF    = "5"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_PROPERTY               = "6"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_INFRASTRUCTURE         = "7"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_FINANCE                = "8"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_4_REMARK2_15_CONST_INDEX_TRADE                  = "9"; //.apm:20190830:OLD(ONLY);
        //.pos: 5th+6th(remark)
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_NOT_INCLUDED                     = "--"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_CONST_INDEX_JII                  = "I-"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_CONST_INDEX_LQ45                 = "L-"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_CONST_INDEX_KOMPAS100            = "K-"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_CONST_INDEX_JII_KOMPASS100       = "IK"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_CONST_INDEX_JII_LQ45             = "IL"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_CONST_INDEX_LQ45_KOMPASS100      = "LK"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_JII_LQ45_KOMPAS100        = "XX"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_JII_BISNIS27              = "IB"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_LQ45_JII_BISNIS27         = "B1"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_JII_KOMPAS100_BISNIS27    = "B2"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_JII_LQ45_KOMPAS_BISNIS    = "BX"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_LQ45_BISNIS27             = "LB"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_LQ45_KOMPAS100_BISNIS27   = "B3"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_KOMPAS100_BISNIS27        = "KB"; //.apm:20190830:OLD(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_56_INCLUD_BISNIS27                  = "B-"; //.apm:20190830:OLD(ONLY);
        //.pos: 7th(remark)/4th(remark2)
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_7_REMARK2_4_CANNOT_PREOPEN          = "-"; //.apm:20190830:OLD+NEW(CHANGED);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_7_REMARK2_4_CAN_PREOPEN             = "O"; //.apm:20190830:OLD+NEW(CHANGED);
        //.pos: 8th(remark)/5th(remark2)
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_8_REMARK2_5_NO_INFORMATION          = "-"; //.apm:20190830:OLD+NEW(CHANGED);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_8_REMARK2_5_MBX_MAIN_BOARD          = "1"; //.apm:20190830:OLD+NEW(CHANGED);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK_8_REMARK2_5_DBX_DEVELOPMENT_BOARD   = "2"; //.apm:20190830:OLD+NEW(CHANGED);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_5_ABX_ACCELERATION_BOARD           = "3"; //.apm:20190830:NEW(ONLY);
        //.pos: 19th~23th(remark2)
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_1923_NO_INFORMATION                = "-"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_19_CODE_BANKRUPTCY                 = "B"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_19_CODE_MORATORIUM_DEBT_PAYMENT    = "M"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_20_CODE_NEGATIVE_EQUITY            = "E"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_21_CODE_ADVERSE_OPINION            = "A"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_21_CODE_DISCLAIMER_OPINION         = "D"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_22_CODE_LATE_SUBMISSION            = "L"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_23_CODE_NO_SALES                   = "S"; //.apm:20190902:NEW(ONLY);
        
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_30_CODE_WATCHLIST                  = "X"; //.apm:20221107:NEW(ONLY);
        
        
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_19_DESC_BANKRUPTCY                 = "Bankruptcy filing against the company"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_19_DESC_MORATORIUM_DEBT_PAYMENT    = "Moratorioum of debt payment"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_20_DESC_NEGATIVE_EQUITY            = "Negative equity"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_21_DESC_ADVERSE_OPINION            = "Adverse opinion of the audited financial report"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_21_DESC_DISCLAIMER_OPINION         = "Disclaimer opinion of the audited financial report"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_22_DESC_LATE_SUBMISSION            = "Late submission of financial report"; //.apm:20190902:NEW(ONLY);
        public final static String SZREF_IDX_STOCKDATARECORD_REMARK2_23_DESC_NO_SALES                   = "No sales based on latest financial report"; //.apm:20190902:NEW(ONLY);
        //.eoc.
    }
    
    public QRIDataConst(){
    }
    
}
