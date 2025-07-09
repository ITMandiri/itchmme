/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.database.setup;

import com.itm.generic.engine.database.setup.ITMDBQueryBuilder.QBDBType;
import com.itm.generic.engine.database.setup.ITMDBVarsConsts.DBDriverSetup;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author aripam
 */
public class ITMDBManager {
    //.single instance:
    public final static ITMDBManager getInstance = new ITMDBManager();
    
    private ConcurrentHashMap<Integer, ITMDBConnection> mapConnections = new ConcurrentHashMap<>();
    
    public ITMDBManager() {
        //.EXXX.
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.INIT, "");
    }
    
    /*
     * internal: load db connector library.
     */
    private boolean checkDriverClass(ITMDBConnection mInputConnection){
        boolean bOut = false;
        try{
            if ((mInputConnection != null)){
                if (!mInputConnection.isfCheckDriverClass()){
                    bOut = true; //.bypass OK.
                }else if ((mInputConnection.getfJDBCDriver() != null) && (mInputConnection.getfJDBCDriver().length() > 0)){
                    try{
                        //.cari driver class name dari driver name (jdbc dbms specific):
                        String zDriverClassName = "";
                        if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(ITMDBVarsConsts.DBDriverSetup.JDBC_DRIVER_NAME_SQLSERVER)){
                            zDriverClassName = ITMDBVarsConsts.DBDriverSetup.LIBRARY_CLASS_NAME_SQLSERVER;
                        }else if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(ITMDBVarsConsts.DBDriverSetup.JDBC_DRIVER_NAME_MYSQL)){
                            zDriverClassName = ITMDBVarsConsts.DBDriverSetup.LIBRARY_CLASS_NAME_MYSQL;
                        }else if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(ITMDBVarsConsts.DBDriverSetup.JDBC_DRIVER_NAME_POSTGRESQL)){
                            zDriverClassName = ITMDBVarsConsts.DBDriverSetup.LIBRARY_CLASS_NAME_POSTGRESQL;
                        }
                        //.test load jdbc library:
                        if (zDriverClassName.length() > 0){
                            Class.forName(zDriverClassName);
                            bOut = true;
                        }
                    }catch(Exception ex1){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex1);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    /*
     * external: cek apakah per koneksi masih terhubung dengan database.
     */
    public boolean isConnected(ITMDBConnection mInputConnection){
        boolean bOut = false;
        try{
            if (mInputConnection != null){
                if (mInputConnection.getfConnection() != null){
                    try{
                        if (!mInputConnection.getfConnection().isClosed()){
                            if (mInputConnection.getfConnection().isValid(ITMDBVarsConsts.DBDriverSetup.TEST_CONNECTION_VALIDITY_TIMEOUT)){
                                bOut = true;
                            }
                        }
                    }catch(SQLException ex11){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex11);
                    }catch(Exception ex12){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex12);
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    /*
     * external: tutup per koneksi ke database.
     */
    public boolean closeConnection(ITMDBConnection mInputConnection){
        boolean bOut = false;
        try{
            if ((mInputConnection != null) && (mInputConnection.getfConnection() != null)){
                try{
                    if (!mInputConnection.getfConnection().isClosed()){
                        mInputConnection.getfConnection().close();
                    }
                }catch(SQLException ex11){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex11);
                }catch(Exception ex12){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex12);
                }
                try{
                    if (mInputConnection.getfConnection().isClosed()){
                        bOut = true;
                    }
                }catch(SQLException ex21){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex21);
                }catch(Exception ex22){
                    //.EXXX.
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex22);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    /*
     * external: buka per koneksi ke database. ITMDBConnection(input) harus sudah diisi terlebih dulu.
     * requirement: *.jar jdbc untuk mysql,sqlserver,postgres.
     */
    public boolean openConnection(ITMDBConnection mInputConnection) {
        boolean bOut = false;
        try{
            if (mInputConnection != null){
                if (mInputConnection.getfConnection() != null){
                    try{
                        if (!mInputConnection.getfConnection().isClosed()){
                            if (mInputConnection.getfConnection().isValid(ITMDBVarsConsts.DBDriverSetup.TEST_CONNECTION_VALIDITY_TIMEOUT)){
                                bOut = true;
                            }
                        }
                    }catch(SQLException ex11){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex11);
                    }catch(Exception ex12){
                        //.EXXX.
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex12);
                    }
                }
                if (!bOut){
                    //.coba buat koneksi baru:
                    if (checkDriverClass(mInputConnection)){
                        //.syarat terpenuhi:
                        if ((mInputConnection.getfJDBCDriver() != null) && (mInputConnection.getfJDBCDriver().length() > 0) &&
                            (mInputConnection.getfHostPort() >= (-1)) && (mInputConnection.getfHostPort() < 65536) ){
                            //.db connection url:
                            String zDBConnectorUrl = mInputConnection.getfJDBCDriver() + "://" + 
                                                    mInputConnection.getfHostAddress();
                            if ((mInputConnection.getfHostPort() >= (0))){
                                zDBConnectorUrl += ":" + mInputConnection.getfHostPort();
                            }
                            //.db connection properties:
                            Properties mDBConnectorProperty = new Properties();
                            if ((mInputConnection.getfUserName() != null)){
                                mDBConnectorProperty.setProperty("user", mInputConnection.getfUserName());
                            }
                            if ((mInputConnection.getfUserPassword() != null)){
                                mDBConnectorProperty.setProperty("password", mInputConnection.getfUserPassword());
                            }
                            //.db additional url & properties (jdbc dbms specific):
                            if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(ITMDBVarsConsts.DBDriverSetup.JDBC_DRIVER_NAME_SQLSERVER)){
                                mDBConnectorProperty.setProperty("applicationName", ITMDBVarsConsts.DBDriverSetup.CONNECTION_NAME);
                                //.? sqlserver: keep alive ?
                                if ((mInputConnection.getfDatabaseName() != null) && (mInputConnection.getfDatabaseName().length() > 0)){
                                    mDBConnectorProperty.setProperty("databaseName", mInputConnection.getfDatabaseName());
                                }
                            }else if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(ITMDBVarsConsts.DBDriverSetup.JDBC_DRIVER_NAME_MYSQL)){
                                //.? mysql: connection name ?
                                mDBConnectorProperty.setProperty("autoReconnect","true");
                                if ((mInputConnection.getfDatabaseName() != null) && (mInputConnection.getfDatabaseName().length() > 0)){
                                    zDBConnectorUrl += "/" + mInputConnection.getfDatabaseName();
                                }
                            }else if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(ITMDBVarsConsts.DBDriverSetup.JDBC_DRIVER_NAME_POSTGRESQL)){
                                mDBConnectorProperty.setProperty("ApplicationName", ITMDBVarsConsts.DBDriverSetup.CONNECTION_NAME);
                                mDBConnectorProperty.setProperty("tcpKeepAlive","true");
                                if ((mInputConnection.getfDatabaseName() != null) && (mInputConnection.getfDatabaseName().length() > 0)){
                                    zDBConnectorUrl += "/" + mInputConnection.getfDatabaseName();
                                }
                            }
                            //.coba buat koneksi database:
                            try{
                                Connection conn = DriverManager.getConnection(zDBConnectorUrl, mDBConnectorProperty);
                                mInputConnection.setfConnection(conn);
                            }catch(SQLException ex21){
                                //.EXXX.
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex21);
                            }catch(Exception ex22){
                                //.EXXX.
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex22);
                            }
                            try{
                                if ((mInputConnection.getfConnection() != null) && (!mInputConnection.getfConnection().isClosed())){
                                    bOut = true;
                                }
                            }catch(SQLException ex21){
                                //.EXXX.
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex21);
                            }catch(Exception ex22){
                                //.EXXX.
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex22);
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    /*
     * external: collection - ambil per koneksi berdasar id koneksi. hanya jika sudah pakai rememberConnection(...).
     */
    public ITMDBConnection getConnection (int iInputConnectionKey, boolean bAutoOpenConnection){
        ITMDBConnection mOut = null;
        try{
            if (this.mapConnections != null) {
                if (this.mapConnections.containsKey(iInputConnectionKey)){
                    mOut = this.mapConnections.get(iInputConnectionKey);
                    if (bAutoOpenConnection){
                        if (mOut != null){
                            openConnection(mOut);
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    /*
     * external: collection - simpan per koneksi berdasar id koneksi, agar dapat diambil dengan getConnection(...)
     */
    public boolean rememberConnection(int iInputConnectionKey, ITMDBConnection mInputConnection, boolean replaceExisting){
        boolean bOut = false;
        try{
            if (mInputConnection != null){
                if (this.mapConnections == null){
                    this.mapConnections = new ConcurrentHashMap<>();
                }
                if (this.mapConnections != null) {
                    if (this.mapConnections.containsKey(iInputConnectionKey)){
                        if (replaceExisting){
                            //.yg lama harus dibersihkan dulu:
                            closeConnection(this.mapConnections.get(iInputConnectionKey));
                            //.ganti dengan yg baru:
                            this.mapConnections.put(iInputConnectionKey, mInputConnection);
                            bOut = true;
                        }
                    }else{
                        this.mapConnections.put(iInputConnectionKey, mInputConnection);
                        bOut = true;
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    /*
     * external: collection - (re)start semua koneksi yg tersimpan dari rememberConnection(...).
     */
    public boolean startConnections(boolean bRestart) {
        boolean bOut = false;
        try{
            if ((this.mapConnections != null) && (!this.mapConnections.isEmpty())){
                for (Iterator<Integer> iterConnKey = this.mapConnections.keySet().iterator(); iterConnKey.hasNext(); ){
                    Integer iConnKey = (Integer)iterConnKey.next();
                    if (iConnKey != null){
                        ITMDBConnection mDBConn = this.mapConnections.get(iConnKey);
                        if (bRestart){
                            //.harus dibersihkan dulu:
                            closeConnection(mDBConn);
                        }
                        //.diperbarui:
                        openConnection(mDBConn);
                    }
                }
                bOut = true;
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    /*
     * external: collection - hapus semua koneksi yg tersimpan dari rememberConnection(...).
     */
    public boolean clearConnections() {
        boolean bOut = false;
        try{
            if ((this.mapConnections != null) && (!this.mapConnections.isEmpty())){
                for (Iterator<Integer> iterConnKey = this.mapConnections.keySet().iterator(); iterConnKey.hasNext(); ){
                    Integer iConnKey = (Integer)iterConnKey.next();
                    if (iConnKey != null){
                        ITMDBConnection mDBConn = this.mapConnections.get(iConnKey);
                        //.harus dibersihkan dulu:
                        closeConnection(mDBConn);
                        //.dihapus:
                        iterConnKey.remove();
                    }
                }
                bOut = true;
            }
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public QBDBType getQBDBType(ITMDBConnection mInputConnection){
        QBDBType mOut = QBDBType.MSSQL; //default.
        try{
            if (mInputConnection != null) {
                if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(DBDriverSetup.JDBC_DRIVER_NAME_SQLSERVER)){
                    mOut = QBDBType.MSSQL;
                }else if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(DBDriverSetup.JDBC_DRIVER_NAME_MYSQL)){
                    mOut = QBDBType.MYSQL;
                }else if (mInputConnection.getfJDBCDriver().equalsIgnoreCase(DBDriverSetup.JDBC_DRIVER_NAME_POSTGRESQL)){
                    mOut = QBDBType.POSTGRE;
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
}
