/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.filelogger.setup;

/**
 *
 * @author aripam
 */
public class ITMFileLoggerVarsConsts {
    
    public static class AppClientCode {
        public final static String CLIENT_RELI_LS                   = "LS";
        public final static String CLIENT_ASET_SH                   = "SH";
        public final static String CLIENT_YULI_RS                   = "RS";
    }
    
    public final static String APP_CLIENT_CODE                      = AppClientCode.CLIENT_ASET_SH;
    public final static String APP_CLIENT_NAME                      =   ( APP_CLIENT_CODE.equals(AppClientCode.CLIENT_RELI_LS) ? "PT. RELIANCE SEKURITAS" : 
                                                                        ( APP_CLIENT_CODE.equals(AppClientCode.CLIENT_ASET_SH) ? "PT. ARTHA SEKURITAS" : 
                                                                        ( APP_CLIENT_CODE.equals(AppClientCode.CLIENT_YULI_RS) ? "PT. YULIE SEKURITAS INDONESIA" : 
                                                                        "--" //.unknown;
                                                                        )));
    
    public final static String DEFAULT_LOGGER_FILE_DIRECTORY        = "./logs/";
    public final static String DEFAULT_LOGGER_FILE_EXTENSION        = ".log";
    
    public static String ALTER_LOGGER_FILE_DIRECTORY                = DEFAULT_LOGGER_FILE_DIRECTORY;
    
    public static enum logSource{SERVER     (1, "svr"), //.terkait server.
                                TRANSACTION (2, "trx"), //.terkait transaksi.
                                REPORT      (3, "rpt"), //.terkait report.
                                BADORDER    (4, "bad"), //.terkait bad order.
                                ODD         (5, "odd"), //.terkait yg ganjil.
                                DATABASE    (6, "dbs"), //.terkait database.
                                DELRECORD   (7, "del"), //.terkait record db yg dihapus.
                                SOCKET      (8, "sck"), //.terkait soket.
                                JONECRECV   (9, "jonecrecv"), //.terkait yg diterima dari jonec.
                                JONECSEND   (10, "jonecsend"), //.terkait yg dikirim ke jonec.
                                MARTINRECV  (11, "martinrecv"), //.terkait yg diterima dari martin.
                                MARTINSEND  (12, "martinsend"), //.terkait yg dikirim ke martin.
                                RTBO        (13, "rtbo"), //.terkait realtime backoffice.
                                CREDENTIAL  (14, "cred"), //.terkait perubahan credential login.
                                EXTENSION   (15, "ext"), //.terkait library eksternal.
                                SHADOW      (16, "shadow"), //.terkait koneksi bayangan / recovery.
                                SOUPBINTCP  (17, "soupbintcp"), //.terkait library soupbintcp.
                                ITCH        (18, "itch"), //.terkait itch.
                                OUCH        (19, "ouch"), //.terkait itch.
                                DFSPLITTER  (20, "dfsplitter"), //.terkait datafeed splitter.
                                XTTS        (21, "xtts"), //.terkait datafeed splitter.
                                FIX5        (22, "fix5"), //.terkait fix5.
                                FORWARDER   (23, "forwarder"), //.terkait fix5.
                                APIBCA      (24, "apibca"); //.terkait apibca.
                                
        private final int order;
        private final String prefix;
        
        private logSource(int order, String prefix) {
            this.order = order;
            this.prefix = prefix;
        }
        
        public int getOrder() {
            return order;
        }
        
        public String getPrefix() {
            return prefix;
        }
        
    };
    
    public static enum logLevel{DEBUG, //.test debug.print.penting nggak ya? :)
                                ERROR, //.di try-catch,error, saat ada kesalahan perhitungan.
                                INFO, //.saat ada info yg cukup membantu pelacakan.
                                INIT, //.saat inisialisasi objects.
                                WARNING //.saat ada kemungkinan bisa terjadi error, saat mencoba diluar batas yg ditentukan.
    };
    
    public ITMFileLoggerVarsConsts() {
        //.nothing todo here :)
    }
    
}
