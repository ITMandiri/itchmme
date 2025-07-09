/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.backup;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgeBackupConsts {
    
    public final static String DEFAULT_SOUPBINTCP_BACKUP_FILE_DIRECTORY         = "./soupbintcp_backups/";
    public final static String DEFAULT_SOUPBINTCP_BACKUP_FILE_EXTENSION         = ".bak";
    public final static String DEFAULT_SOUPBINTCP_BACKUP_OLD_FILE_EXTENSION     = ".old";
    
    public static String ALTER_SOUPBINTCP_BACKUP_FILE_DIRECTORY                 = DEFAULT_SOUPBINTCP_BACKUP_FILE_DIRECTORY;
    
    public final static String SOUPBINTCP_BACKUP_TYPE_RECV_SEQEUNCED            = "RECV_SEQ";
    public final static String SOUPBINTCP_BACKUP_TYPE_RECV_UNSEQEUNCED          = "RECV_UNSEQ";
    public final static String SOUPBINTCP_BACKUP_TYPE_SEND                      = "SEND";
    public final static String SOUPBINTCP_BACKUP_TYPE_DUMP                      = "DUMP";
    
    public final static String SOUPBINTCP_BACKUP_LINE_FIELD_DELIMITER           = "|";
    public final static String SOUPBINTCP_BACKUP_LINE_PREFIX                    = "@";
    public final static String SOUPBINTCP_BACKUP_LINE_SUFFIX                    = "OK";
    public final static int SOUPBINTCP_BACKUP_MINIMUM_LINE_FIELDS_COUNT         = 8;
    
    public final static boolean SOUPBINTCP_BACKUP_CLOSE_FILE_ON_RESET_RECORDNO  = true; //.jika: true = akan tutup file backup yg sedang terbuka, lalu rename file, lalu buat baru file backup;
    
}
