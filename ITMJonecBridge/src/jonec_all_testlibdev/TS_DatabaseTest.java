/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.generic.engine.database.setup.ITMDBAccess;
import com.itm.generic.engine.database.setup.ITMDBConnection;
import com.itm.generic.engine.database.setup.ITMDBManager;
import com.itm.generic.engine.database.setup.ITMDBVarsConsts;
import com.itm.idx.data.helpers.BrokerReferenceHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aripam
 */
public class TS_DatabaseTest {
    
    public final static int IREF_DB_ID = 1;
    public ITMDBConnection dbc = new ITMDBConnection();
    
    public ITMDBAccess dba = new ITMDBAccess();
    
    private String JDBCDriver = ITMDBVarsConsts.DBDriverSetup.JDBC_DRIVER_NAME_SQLSERVER;
    private String ZDBServerAddress = "localhost";
    private int IDBServerPort = 3456; //(-1);
    private String ZUserName = "sa";
    private String ZUserPass = "sql";
    private String ZDBName = "relitrade";
    
    public TS_DatabaseTest() {
    }
    
    public void runDB(){
        
        System.out.println("---------------");
        System.out.println(BrokerReferenceHelper.generateBrokerRef(99999999, 999999, 999999));
        String yy = "";
        for (int iMax = 0; iMax < 2147483647; iMax++){
            String zz = BrokerReferenceHelper.PositiveIntToAlphaNumBase(iMax);
            if (zz.length() >= 4){
                System.out.println("MAX_3=" + (iMax - 1) + "\tALPHA_3=" + yy);
                break;
            }
            yy = zz;
        }
        
        System.out.println(Long.toString(-1234567890, 36));
        System.out.println(Long.parseLong("ARIPAMBUDI", 36));
        
        String zInputString = "ARIPAMBUDI";
        Long lEnc1 = Long.parseLong(zInputString, 36);
        System.err.println("lEnc1::" + lEnc1);
        String zEnc2 = Long.toString(lEnc1, 36);
        System.err.println("zEnc2::" + zEnc2);
        System.err.println(Long.toString(Long.parseLong(zEnc2, 36), 36));
        
        
        
        if (!ITMDBManager.getInstance.isConnected(dbc)){
            dbc.setfCheckDriverClass(true);
            dbc.setfJDBCDriver(JDBCDriver);
            dbc.setfHostAddress(ZDBServerAddress);
            dbc.setfHostPort(IDBServerPort);
            dbc.setfUserName(ZUserName);
            dbc.setfUserPassword(ZUserPass);
            dbc.setfDatabaseName(ZDBName);
        }
        
        
        
        boolean bMustLoop = true;
        
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        String zInput = "";
        
        while(bMustLoop){
            System.out.println("A=StartDB / Z=StopDB / C=CheckDB / Q=QueryDB / U=UpdateDB / X=Exit");
            try {
                zInput = bfr.readLine();
            } catch (IOException ex) {
                //.exxx.
                System.err.println(ex);
            }
            switch(zInput.toUpperCase().trim()){
                case "A":
                    System.out.println("--starting dbserver...before=" + ITMDBManager.getInstance.isConnected(dbc));
                    ITMDBManager.getInstance.openConnection(dbc);
                    System.out.println("--starting dbserver...after=" + ITMDBManager.getInstance.isConnected(dbc));
                    break;
                case "Z":
                    System.out.println("--stopping dbserver...before=" + ITMDBManager.getInstance.isConnected(dbc));
                    ITMDBManager.getInstance.closeConnection(dbc);
                    System.out.println("--stopping dbserver...after=" + ITMDBManager.getInstance.isConnected(dbc));
                    break;
                case "C":
                    System.out.println("--checking dbserver...before=" + ITMDBManager.getInstance.isConnected(dbc));
                    System.out.println("--checking dbserver...after=" + ITMDBManager.getInstance.isConnected(dbc));
                    break;
                case "X":
                    System.out.println("--exitting dbserver...before=" + ITMDBManager.getInstance.isConnected(dbc));
                    ITMDBManager.getInstance.closeConnection(dbc);
                    System.out.println("--exitting dbserver...after=" + ITMDBManager.getInstance.isConnected(dbc));
                    bMustLoop = false;
                    break;
                case "Q":
                    System.out.print("type query here: ");
                    try {
                        zInput = bfr.readLine();
                    } catch (IOException ex) {
                        //.exxx.
                        System.err.println(ex);
                    }
                    if ((zInput != null) && (zInput.length() > 0)){
                        System.out.println("send query to database..." + zInput);
                        
                        dba.setConn(dbc);
                        ResultSet rs = dba.getRecordSetQuery(zInput);
                        System.out.println("--queryResult: " + rs);
                        if (rs != null){
                            int cRows = 0;
                            try {
                                while(rs.next()){
                                    cRows++;
                                }
                            } catch (SQLException ex404) {
                                System.err.println(ex404);
                            }
                            System.out.println("---rows: " + cRows);
                            try {
                                rs.close();
                            } catch (SQLException ex405) {
                                System.err.println(ex405);
                            }
                        }
                        
                    }else{
                        System.err.println("query empty.");
                    }
                    break;
                case "U":
                    System.out.print("type update query here: ");
                    try {
                        zInput = bfr.readLine();
                    } catch (IOException ex) {
                        //.exxx.
                        System.err.println(ex);
                    }
                    if ((zInput != null) && (zInput.length() > 0)){
                        System.out.println("send update query to database..." + zInput);
                        
                    }else{
                        System.err.println("update query empty.");
                    }
                    break;
                case "V":
                    int iInput = 0;
                    try {
                        zInput = bfr.readLine();
                        iInput = Integer.valueOf(zInput);
                    } catch (IOException ex) {
                        //.exxx.
                        System.err.println(ex);
                    } catch(Exception ex00){
                        //.exxx.
                        System.err.println(ex00);
                    }
                    String zOutEnc = BrokerReferenceHelper.PositiveIntToAlphaNumBase(iInput);
                    System.err.println("ALPHA.ENCODE='" + zOutEnc + "'");
                    System.err.println("HEXAD.ENCODE='" + Integer.toHexString(iInput) + "'");
                    System.err.println("ALPHA.DECODE='" + BrokerReferenceHelper.PositiveAlphaNumBaseToInt(zOutEnc) + "'");
                    break;
                default:
                    System.err.println("--unknown command : " + zInput);
                    break;
            }
            
        }
        
        
        
        
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TS_DatabaseTest tsdb = new TS_DatabaseTest();
        tsdb.runDB();
        System.out.println("Selesai.");
    }
}
