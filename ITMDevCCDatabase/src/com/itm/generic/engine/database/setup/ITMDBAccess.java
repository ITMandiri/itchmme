/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.database.setup;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author aripam
 */
public class ITMDBAccess {
    
    private ITMDBConnection dbConn;
    
    public ITMDBAccess() {
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.INIT, "");
    }
    
    public ITMDBAccess(ITMDBConnection dbConn) {
        this.dbConn = dbConn;
    }
    
    public ITMDBConnection getConn() {
        return dbConn;
    }
    
    public void setConn(ITMDBConnection dbConn) {
        this.dbConn = dbConn;
    }
    
    public boolean isTableExist(String szTableName){
        boolean bOut = false;
        try{
            String zTableQuery = "SELECT COUNT(*) FROM " + szTableName;
            ResultSet rsTable = getRecordSetQuery(zTableQuery);
            if ((rsTable != null) && (rsTable.next())){
                bOut = true;
            }
            closeRecordSet(rsTable);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, ex0);
        }
        return bOut;
    }
    
    public int getUpdateQuery(String szRequestQuery, boolean bClose){
        int iOut = (-1); //.(-1) = ada error.
        long tmB1 = System.currentTimeMillis();
        long tmB2 = tmB1;
        try{
            if (szRequestQuery != null){
                if (szRequestQuery.length() > 0){
                    if ((this.dbConn != null)){
                        ITMDBManager.getInstance.openConnection(this.dbConn);
                        Connection hLocConn = this.dbConn.getfConnection();
                        if (hLocConn != null){
                            try {
                                Statement stStatement = hLocConn.createStatement();
                                if (stStatement != null){
                                    iOut = stStatement.executeUpdate(szRequestQuery);
                                    //.ingat, statement masih terbuka.
                                    if (bClose){
                                        stStatement.close();
                                    }
                                }
                            } catch (SQLException sqlex2) {
                                //.EXXX.
                                tmB2 = (System.currentTimeMillis() - tmB1);
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, szRequestQuery+ " -->@" + tmB2, sqlex2);
                            } catch (Exception ex2) {
                                //.EXXX.
                                tmB2 = (System.currentTimeMillis() - tmB1);
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, szRequestQuery+ " -->@" + tmB2, ex2);
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            tmB2 = (System.currentTimeMillis() - tmB1);
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, szRequestQuery+ " -->@" + tmB2, ex0);
        }
        if (iOut >= 0){
            tmB2 = (System.currentTimeMillis() - tmB1);
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.INFO, szRequestQuery + " --" + iOut + ">@" + tmB2);
        }
        return iOut;
    }
     
    public ResultSet getRecordSetQuery(String szRequestQuery){
        ResultSet rOut = null;
        long tmB1 = System.currentTimeMillis();
        long tmB2 = tmB1;
        try{
            if (szRequestQuery != null){
                if (szRequestQuery.length() > 0){
                    if ((this.dbConn != null)){
                        ITMDBManager.getInstance.openConnection(this.dbConn);
                        Connection hLocConn = this.dbConn.getfConnection();
                        if (hLocConn != null){
                            try {
                                Statement stStatement = hLocConn.createStatement();
                                if (stStatement != null){
                                    rOut = stStatement.executeQuery(szRequestQuery);
                                    //.ingat, resultset dan statement masih terbuka.
                                }
                            } catch (SQLException sqlex2) {
                                //.EXXX.
                                tmB2 = (System.currentTimeMillis() - tmB1);
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, szRequestQuery+ " -->@" + tmB2, sqlex2);
                            } catch (Exception ex2) {
                                //.EXXX.
                                tmB2 = (System.currentTimeMillis() - tmB1);
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, szRequestQuery+ " -->@" + tmB2, ex2);
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
            tmB2 = (System.currentTimeMillis() - tmB1);
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.ERROR, szRequestQuery + " -->@" + tmB2, ex0);
        }
        if (rOut != null){
            tmB2 = (System.currentTimeMillis() - tmB1);
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.DATABASE, logLevel.INFO, szRequestQuery + " --" + rOut + ">@" + tmB2);
        }
        return rOut;
    }
    
    public boolean closeRecordSet(ResultSet inRecordSet){
        boolean bOut = false;
        try{
            if (inRecordSet != null){
                Statement stStatement = inRecordSet.getStatement();
                if (!inRecordSet.isClosed()){
                    inRecordSet.close();
                }
                if (stStatement != null){
                    if (stStatement.getResultSet() != null){
                        if (stStatement.getResultSet().isClosed()){
                            if (!stStatement.isClosed()){
                                stStatement.close(); //.closeOnCompletion() tidak terimplementasikan di sqlserver.
                            }
                        }
                    }else{
                        if (!stStatement.isClosed()){
                            stStatement.close(); //.closeOnCompletion() tidak terimplementasikan di sqlserver.
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
    
    
}
