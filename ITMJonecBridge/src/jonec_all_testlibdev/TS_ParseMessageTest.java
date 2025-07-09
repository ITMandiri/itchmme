/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.generic.engine.socket.setup.ITMSocketCtrlGeneral;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.idx.data.ori.consts.ORIDataConst.ORIMsgType;
import com.itm.idx.data.ori.message.processor.ORIMessageProcessor;
import com.itm.idx.data.ori.message.struct.ORIDataAdministrativeLogon;
import com.itm.idx.data.ori.message.struct.ORIDataNewOrder;
import com.itm.idx.message.IDXMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author aripam
 */

public class TS_ParseMessageTest {
    
    public enum Account {
        A("Aq"),
        I("Iq"),
        S("Sq");
        
        private String value;
        Account(String acc) {
            this.value = acc;
        }
        public String getAccount() {
            return this.value;
        }
        
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static ArrayList<String> extractJonecDataFromLog(byte[] bData) {
        ArrayList<String> arrRes = new ArrayList<>();

        int startPos = -1;
        int endPos = -1;
        String tmpLine;

        for (int i = 0; i < bData.length; i++) {

            if (bData[i] == '8') {
                if (i > 0) {
                    if (bData[i - 1] == 0) {
                        startPos = i;
                    }
                }
            } else if (bData[i] == '1') {
                if (i > 0) {
                    if (bData[i - 1] == 1) {
                        if (bData[i + 1] == '0' && bData[i + 2] == '=') {
                            if (startPos < 0) {
                                continue;
                            }
                            endPos = i;
                            try {
                                //. extrack datanya
                                
                                tmpLine = new String(bData, startPos, (endPos - startPos), "UTF-8");
                                arrRes.add(tmpLine);
                                //. preset
                                startPos = -1;
                                endPos = -1;
                            } catch (UnsupportedEncodingException ex) {
                            }
                        }
                    }
                }
            }

        }

        return arrRes;
    }

    public static void main(String[] args) 
    {
        
        String zFilePath = "D:\\reliance\\TRANSACTIONSERVER\\samples\\test_jonec_20140225a.txt";
        File fl = new File(zFilePath);
        if (fl.isFile()){
            if (fl.canRead()){
                FileReader flFirstReader;
                try {
                    try {
                        //String zMsgData = new Scanner(fl).useDelimiter("\\Z").next();
                        //System.err.println(zMsgData);
                        //ArrayList<String> lst = extractJonecDataFromLog(zMsgData.getBytes());
                        
                        ArrayList<String> lst = new ArrayList<>();
                        BufferedReader bfr = new BufferedReader(new FileReader(fl));
                        String zMsgPerLine;
                        while ((zMsgPerLine = bfr.readLine()) != null){
                            if (!StringHelper.isNullOrEmpty(zMsgPerLine)){
                                lst.add(zMsgPerLine);
                            }
                        }
                        
                        //System.err.println(lst.size());
                        
                        for(String zMsgLine : lst){
                            //System.out.println(zMsgLine);
                            ORIMessageReplyBroadcast.getInstance.ReviewJONECReplyMessage(zMsgLine + "10=000\001");
                        }
                    } catch (Exception ex1) {
                        System.out.println(ex1);
                    }
                } catch (Exception ex0) {
                    System.out.println(ex0);
                }
            }
        }
        
        System.exit(0);
        
        
        ORIDataAdministrativeLogon olgn = new ORIDataAdministrativeLogon(null);
        olgn.setfMsgType(ORIMsgType.LOGON);
        olgn.setfMsgSeqNum("0");
        olgn.setfEncryptMethod(0);
        olgn.setfHeartBtInt(45);
        olgn.setfUserID("lstr1001");
        olgn.setfCurrentPassword("Jak@rta128");
        System.out.println(olgn.msgToString());
        
        
        ORIDataNewOrder onew = new ORIDataNewOrder(null);
        onew.setfMsgType(ORIMsgType.NEW_ORDER);
        onew.setfClOrdID("1;1;0");
        onew.setfClientID("lstr1002");
        onew.setfAccount("I");
        onew.setfHandlInst(1);
        onew.setfExecInst("");
        onew.setfSymbol("ANTM");
        onew.setfSymbolSfx("0RG");
        onew.setfSecurityID("448");
        onew.setfSide("1");
        onew.setfTransactTime("20140220-01:00:27");
        onew.setfOrderQty(2);
        onew.setfOrdType("7");
        onew.setfPrice(1728);
        onew.setfStopPx(0);
        onew.setfTimeInForce("0");
        onew.setfExpireDate("0");
        onew.setfText("");
        onew.setfClearingAccount("");
        onew.setfComplianceID("0");
        System.out.println(onew.msgToString());
        
        
        System.exit(0);
        
        System.out.println(ITMSocketCtrlGeneral.getInstance.generateClientChannelID());
        //System.out.println(StringHelper.addZeroFromInt(123, 10));
        ORIMessageProcessor orimap = new ORIMessageProcessor();
        IDXMessage orimsg = orimap.parseMessage("IDXEQDLS_01_001_toJONES0N8=FIX.4.2-JSX35=D11=1;1;0109=lstr10011=I21=118= 55=ANTM65=0RG48=44854=160=20140220-01:01:1838=240=744=173099=059=023=058=440=376=00", false);
        System.out.println(orimsg);
        
        
        System.exit(0);
//////        
//////        ORIDataAdministrativeLogon olgn = new ORIDataAdministrativeLogon(null);
//////        olgn.setfMsgSeqNum("0");
//////        olgn.setfEncryptMethod(0);
//////        olgn.setfHeartBtInt(45);
//////        olgn.setfUserID("lstr1001");
//////        olgn.setfCurrentPassword("Jak@rta128");
//////        System.out.println(olgn.msgToString());
//////        
//////        System.out.println("[" + StringHelper.addSpaces("Password", 12) + "]");
////////////        System.out.println(StringHelper.addZeroFromInt(-12345,10));
//////        
        
        System.exit(0);
        
//////        
//        
//        DBConnection con1 = new DBConnection();
//        con1.setfCheckDriverClass(true);
//        con1.setfJDBCDriver(DBDriverSetup.JDBC_DRIVER_NAME_SQLSERVER);
//        con1.setfHostAddress("192.168.0.101");
//        con1.setfHostPort(1433);
//        con1.setfUserName("sa");
//        con1.setfUserPassword("itm");
//        con1.setfDatabaseName("relitrade");
//        DBManager.getInstance.rememberConnection(1, con1, true);
//        DBConnection db1 = DBManager.getInstance.getConnection(1);
//        System.out.println(db1);
//        
//        DBConnection con2 = new DBConnection();
//        con2.setfCheckDriverClass(true);
//        con2.setfJDBCDriver(DBDriverSetup.JDBC_DRIVER_NAME_MYSQL);
//        con2.setfHostAddress("localhost");
//        con2.setfHostPort(3306);
//        con2.setfUserName("mis");
//        con2.setfUserPassword("mis");
//        con2.setfDatabaseName("mis");
//        DBManager.getInstance.rememberConnection(2, con2, true);
//        DBConnection db2 = DBManager.getInstance.getConnection(2);
//        
//        DBConnection con3 = new DBConnection();
//        con3.setfCheckDriverClass(true);
//        con3.setfJDBCDriver(DBDriverSetup.JDBC_DRIVER_NAME_POSTGRESQL);
//        con3.setfHostAddress("localhost");
//        con3.setfHostPort(5432);
//        con3.setfUserName("postgres");
//        con3.setfUserPassword("mis");
//        con3.setfDatabaseName("mis");
//        DBManager.getInstance.rememberConnection(3, con3, true);
//        DBConnection db3 = DBManager.getInstance.getConnection(3);
//        
//        
//        System.out.println(con1.equals(db2));
//        
//        System.out.println(con1.getfConnection().equals(db2.getfConnection()));
//        
//        
//        try {
//            Statement stm1 = db1.getfConnection().createStatement();
//            ResultSet rst1 = stm1.executeQuery("SELECT * FROM SYsUsEr");
//            while (rst1.next()){
//                System.out.println(rst1.getString(2));
//            }
//            System.out.println(rst1);
//        } catch (SQLException ex) {
//            Logger.getLogger(TS_ParseMessageTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        try {
//            Statement stm2 = db2.getfConnection().createStatement();
//            ResultSet rst2 = stm2.executeQuery("SELECT * FROM user_login");
//            while (rst2.next()){
//                System.out.println(rst2.getString(2));
//            }
//            System.out.println(rst2);
//        } catch (SQLException ex) {
//            Logger.getLogger(TS_ParseMessageTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        try {
//            Statement stm3 = db3.getfConnection().createStatement();
//            ResultSet rst3 = stm3.executeQuery("SELECT * FROM master.board_info");
//            while (rst3.next()){
//                System.out.println(rst3.getString(2));
//            }
//            System.out.println(rst3);
//        } catch (SQLException ex) {
//            Logger.getLogger(TS_ParseMessageTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
        
        
        
        
//        DBManager.getInstance.clearConnections();
//        
//        System.exit(0);
        
////        OBJ o1 = new OBJ();
////        OBJ o2 = o1;
//////        o1.setO(123);
////        o2 = new OBJ();
//////        String o1 = "Abc";
//////        String o2 = "Abc";
//////        o1.setZ("A");
//////        o2.setZ("A");
////        System.out.println(o1.equals(o2));
////        System.out.println(o1 == o2);
////        System.exit(0);
        // TODO code application logic here
//        String s = "\0008=FIX.4.29=10235=A49=LS_0156=JONES34=150=00152=20140121-01:59:4598=0108=456001=lstr1001    6002=Jak@rta12610=188";
//        String s = "8=FIX.4.29=10235=A49=LS_0156=JONES34=150=00152=20140121-01:59:4598=0108=456001=lstr1001    6002=Jak@rta12610=188";
//        ORIMessageProcessor omm = new ORIMessageProcessor();
//        IDXMessage omsg = omm.parseMessage(s);
//        System.out.println();
//////        Account a = Account.S;
//////        System.out.println(a.value);
//////        String z = "1||1||Abdul-Jabbar||Karim||1996||1974";
//////        String[] zar = StringHelper.splitAll(z, "Karim");
//////        String y = "123";
//////        String[] yar = y.split("[|]");
//////        String zSpacesss = StringHelper.addSpaces("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678", 255);
//////        System.out.println(Pattern.compile("[|][|]"));
//////        
//////        
//////        ORIDataHeader odh = new ORIDataHeader(null);
//////        String zOutODH = odh.msgHeaderToString();
//////        System.out.println(Pattern.compile("[|][|]"));
        
//////        IDXMessage oMsg = new ORIDataAdministrativeLogonReply(null);
//////        System.out.println(oMsg instanceof ORIDataAdministrativeLogonReply);
        
        
//////        
//////        TBindingListener tbl1 = new TBindingListener();
//////        TBindingListener tbl2 = new TBindingListener();
//////        
//////        BBindingListener bbl = BBindingListener.getInstance;
//////        
//////        bbl.addBindingListener(tbl1);
//////        bbl.addBindingListener(tbl1);
//////        bbl.addBindingListener(tbl1);
//////        
//////        bbl.addBindingListener(tbl2);
//////        bbl.addBindingListener(tbl2);
//////        bbl.addBindingListener(tbl2);
//////        
//////        bbl.raiseOnConnected(255);
//////        bbl.raiseOnMessage(255,"msg");
//////        bbl.raiseOnError(255,404);
//////        bbl.raiseOnDisconnected(255);
//////        
//////        bbl.removeBindingListener(tbl1);
//////        bbl.removeBindingListener(tbl1);
//////        bbl.removeBindingListener(tbl1);
//////        
//////        bbl.removeBindingListener(tbl2);
//////        bbl.removeBindingListener(tbl2);
//////        bbl.removeBindingListener(tbl2);
//////        
    }
    
    
    
    
}
