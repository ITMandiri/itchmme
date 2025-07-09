/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.database.setup;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.sql.Connection;

/**
 *
 * @author aripam
 */
public class ITMDBConnection {
    
    private String fJDBCDriver                                  = ""; //."jdbc:......."
    private String fHostAddress                                 = "";
    private int fHostPort                                       = 0; //.(-1 jika tanpa port).
    private String fUserName                                    = "";
    private String fUserPassword                                = "";
    private String fDatabaseName                                = "";
    
    private Connection fConnection                              = null;
    
    private boolean fCheckDriverClass                           = false; //.default.
    
    public ITMDBConnection() {
        //.EXXX.
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.INIT, "");
    }
    
    public String getfJDBCDriver() {
        return fJDBCDriver;
    }

    public void setfJDBCDriver(String fJDBCDriver) {
        this.fJDBCDriver = fJDBCDriver;
    }
    
    public String getfHostAddress() {
        return fHostAddress;
    }

    public void setfHostAddress(String fHostAddress) {
        this.fHostAddress = fHostAddress;
    }

    public int getfHostPort() {
        return fHostPort;
    }

    public void setfHostPort(int fHostPort) {
        this.fHostPort = fHostPort;
    }

    public String getfUserName() {
        return fUserName;
    }

    public void setfUserName(String fUserName) {
        this.fUserName = fUserName;
    }

    public String getfUserPassword() {
        return fUserPassword;
    }

    public void setfUserPassword(String fUserPassword) {
        this.fUserPassword = fUserPassword;
    }

    public String getfDatabaseName() {
        return fDatabaseName;
    }

    public void setfDatabaseName(String fDatabaseName) {
        this.fDatabaseName = fDatabaseName;
    }

    public Connection getfConnection() {
        return fConnection;
    }

    public void setfConnection(Connection fConnection) {
        this.fConnection = fConnection;
    }

    public boolean isfCheckDriverClass() {
        return fCheckDriverClass;
    }

    public void setfCheckDriverClass(boolean fCheckDriverClass) {
        this.fCheckDriverClass = fCheckDriverClass;
    }

}
