/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.database.setup;

/**
 *
 * @author aripam
 */
public class ITMDBVarsConsts {
    
    public class DBDriverSetup{
        
        public static final String CONNECTION_NAME                              = "TransactionServerNextG";
        
        public static final int TEST_CONNECTION_VALIDITY_TIMEOUT                = 10; //.detik.
        
        public static final String LIBRARY_CLASS_NAME_SQLSERVER                 = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        public static final String JDBC_DRIVER_NAME_SQLSERVER                   = "jdbc:sqlserver";
        
        public static final String LIBRARY_CLASS_NAME_MYSQL                     = "com.mysql.jdbc.Driver";
        public static final String JDBC_DRIVER_NAME_MYSQL                       = "jdbc:mysql";
        
        public static final String LIBRARY_CLASS_NAME_POSTGRESQL                = "org.postgresql.Driver";
        public static final String JDBC_DRIVER_NAME_POSTGRESQL                  = "jdbc:postgresql";
        
    }
    
}
