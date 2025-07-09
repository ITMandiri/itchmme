/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbaccess;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author fredy
 */
public class DBAccessManagement {
    //.single instance ya:
    public final static DBAccessManagement getInstance = new DBAccessManagement();
    
    private String szDBHostAddress                                                  = "";
    private int intDBHostPort                                                       = 0;
    private String szDBSelfName                                                     = ""; //.database name.
    private String szDBUserName                                                     = "";
    private String szDBUserPassword                                                 = "";
    
    private Connection dbMasterConnection;
    
    //.constructor:
    public DBAccessManagement(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    private boolean stopDBaseConnection(){
        //.cek apakah object koneksi sudah ada:
        if (this.dbMasterConnection == null){
            return true;
        }
        //.cek apakah koneksi sudah ditutup:
        try {
            if (this.dbMasterConnection.isClosed()){
                return true;
            }
        } catch (SQLException sqlex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex);
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex);
        }
        //.cek apakah koneksi masih valid:
        //.coba tutup koneksi db:
        try {
            this.dbMasterConnection.close();
        } catch (SQLException sqlex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex);
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex);
        }
        //.beri tahu hasil:
        try {
            if (this.dbMasterConnection.isClosed()){
                return true;
            }
        } catch (SQLException sqlex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex);
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex);
        }
        return false;
    }
    
    private boolean startDBaseConnection(){
        //.cek apakah koneksi sudah ada:
        if (this.dbMasterConnection != null){
            try {
                if (!this.dbMasterConnection.isClosed()){
                    if (this.dbMasterConnection.isValid(10)){
                        return true;
                    }
                }
            } catch (SQLException sqlex) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex);
            } catch (Exception ex) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex);
            }
        }
        //.sepertinya koneksi ke database belum ada, coba koneksi baru:
        //.cek parameter koneksi:
        if (this.szDBHostAddress == null){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "db connection: host address cannot be null.");
            return false;
        }
        if (this.szDBHostAddress.length() <= 0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "db connection: host address cannot be empty.");
            return false;
        }
        if (this.szDBUserName == null){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "db connection: user name cannot be null.");
            return false;
        }
        if (this.szDBUserName.length() <= 0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "db connection: user name cannot be empty.");
            return false;
        }
        if (this.szDBSelfName == null){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "db connection: database name cannot be null.");
            return false;
        }
        if (this.szDBSelfName.length() <= 0){
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, "db connection: database name cannot be empty.");
            return false;
        }
        //.susun string koneksi:
        String szDbConnString = "";
        
        if (this.intDBHostPort > 0){
            szDbConnString = "jdbc:sqlserver" + "://" + this.szDBHostAddress + ":" + this.intDBHostPort; //.$$$SQLSERVER--20140425.
        }else{
            szDbConnString = "jdbc:sqlserver" + "://" + this.szDBHostAddress;
        }
        
        //.cek apakah driver db sudah ada:
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException cnfex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, cnfex.getMessage());
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
        }
        //.coba koneksi ke db:
        try {
            Properties propGetConnInfo = new Properties();
            propGetConnInfo.setProperty("user",this.szDBUserName);
            propGetConnInfo.setProperty("password",this.szDBUserPassword);
            propGetConnInfo.setProperty("databaseName",this.szDBSelfName); //.$$$SQLSERVER--20140425.
            //.yuk lanjut:
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "db connection: trying to connect database server in address: " + this.szDBHostAddress + " > port: " + this.intDBHostPort + " > username: " + this.szDBUserName + " > password: " + this.szDBUserPassword + " > dbname: " + this.szDBSelfName);
            this.dbMasterConnection = DriverManager.getConnection(szDbConnString, propGetConnInfo);
        } catch (SQLException sqlex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage());
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
        }
        //.beri tahu hasil:
        if (this.dbMasterConnection != null){
            try {
                if (!this.dbMasterConnection.isClosed()){
                    if (this.dbMasterConnection.isValid(10)){
                        //.berhasil koneksi ke database:
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.INFO, "db connection: connected to database server.");
                        //.selesai:
                        return true;
                    }
                }
            } catch (SQLException sqlex) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage());
            } catch (Exception ex) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
            }
        }
        return false;
    }
    
    public Connection getDbMasterAlreadyActiveConn() {
        if (this.dbMasterConnection != null){
            try {
                if (!this.dbMasterConnection.isClosed()){
                    if (this.dbMasterConnection.isValid(10)){
                        return this.dbMasterConnection;
                    }
                } else {
                    //.harus dikoneksi lagi databasenya ya:
                    if (startDBaseConnection()){
                        return this.dbMasterConnection;
                    }
                }
            } catch (SQLException sqlex) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage());
            } catch (Exception ex) {
                ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
            }
        } else {
            //.harus dikoneksi lagi databasenya ya:
            if (startDBaseConnection()){
                return this.dbMasterConnection;
            }
        }
        return null;
    }
    
    public ResultSet getRecordSetQuery(String szRequestQuery){
        if (szRequestQuery != null){
            if (szRequestQuery.length() > 0){
                Connection hLocConn = getDbMasterAlreadyActiveConn();
                if (hLocConn != null ){
                    try {
                        Statement stStatement = hLocConn.createStatement();
                        if (stStatement != null){
                            ResultSet rsResult = stStatement.executeQuery(szRequestQuery);
                            if (rsResult != null){ //.never null ya :)
                                return rsResult;
                            }
                        }
                    } catch (SQLException sqlex) {
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage());
                    } catch (Exception ex) {
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
                    }
                }
            }
        }
        return null;
    }
    
    public void doBeginTransaction(Connection inputConn){
        try {
            if (inputConn != null){
                inputConn.setAutoCommit(false);
            }
        } catch (SQLException sqlex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage());
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
        }
    }
    
    public void doEndTransaction(Connection inputConn){
        try {
            if (inputConn != null){
                boolean bRollback = false;
                try {
                    inputConn.commit();
                } catch (SQLException sqlex) {
                    bRollback = true;
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage());
                } catch (Exception ex) {
                    bRollback = true;
                    ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
                }
                if (bRollback){
                    try {
                        inputConn.rollback();
                    } catch (SQLException sqlex) {
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage());
                    } catch (Exception ex) {
                        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
                    }
                }
                inputConn.setAutoCommit(true);
            }
        } catch (SQLException sqlex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage());
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage());
        }
    }
    
    public int doExecuteUpdate(Statement statement, String zUpdateQuery){
        int iOut = 0;
        try {
            iOut = statement.executeUpdate(zUpdateQuery);
        } catch (SQLException sqlex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage() + "\t" + zUpdateQuery);
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage() + "\t" + zUpdateQuery);
        }
        return iOut;
    }
    
    public boolean isSchemaExist(String szSchemaName){ //.$$$SQLSERVER--20140425.
        boolean bOut = false;
        try{
            if ((szSchemaName != null) && (szSchemaName.length() > 0)){
                Connection hLocConn = getDbMasterAlreadyActiveConn();
                if (hLocConn != null){
                    DatabaseMetaData dbMetaData = hLocConn.getMetaData();
                    if (dbMetaData != null){
                        ResultSet rsResultSch = dbMetaData.getSchemas(null, szSchemaName);
                        if (rsResultSch != null){
                            if (rsResultSch.next()){
                                bOut = true;
                                rsResultSch.close();
                            }
                        }
                    }
                }
            }
        } catch (SQLException sqlex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, sqlex.getMessage() + "\tschema: " + szSchemaName);
        } catch (Exception ex) {
            ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.XTTS, ITMFileLoggerVarsConsts.logLevel.ERROR, ex.getMessage() + "\tschema: " + szSchemaName);
        }
        return bOut;
    }
    
    public String getSzDBHostAddress() {
        return this.szDBHostAddress;
    }

    public void setSzDBHostAddress(String szDBHostAddress) {
        this.szDBHostAddress = szDBHostAddress;
    }

    public int getIntDBHostPort() {
        return this.intDBHostPort;
    }

    public void setIntDBHostPort(int intDBHostPort) {
        this.intDBHostPort = intDBHostPort;
    }

    public String getSzDBSelfName() {
        return this.szDBSelfName;
    }

    public void setSzDBSelfName(String szDBSelfName) {
        this.szDBSelfName = szDBSelfName;
    }
    
    public String getSzDBUserName() {
        return this.szDBUserName;
    }

    public void setSzDBUserName(String szDBUserName) {
        this.szDBUserName = szDBUserName;
    }

    public String getSzDBUserPassword() {
        return this.szDBUserPassword;
    }

    public void setSzDBUserPassword(String szDBUserPassword) {
        this.szDBUserPassword = szDBUserPassword;
    }

    
}