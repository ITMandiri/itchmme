/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jonec_all_testlibdev;

import com.itm.generic.engine.database.setup.ITMDBQueryBuilder;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import java.util.Locale;

/**
 *
 * @author aripam
 */
public class TestFormats {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ITMDBQueryBuilder iqb = new ITMDBQueryBuilder();
        iqb.setSchemaName("XSKEMA");
        iqb.setTableName("YTABEL");
        iqb.addColumn("ZKOLOM1", "k1");
        iqb.addColumn("ZKOLOM2", "k2");
        iqb.addColumn("ZKOLOM3", "k3");
        iqb.addWhere("WDIMANA1", "w1");
        iqb.addWhere("WDIMANA2", "w2");
        String zQuery = iqb.getUpdateQuery();
        System.out.println(zQuery);
        
        String val = "999999999999999.435";
        double dbl =  Double.parseDouble(val);
        
        //String val2 = new DecimalFormat("###.###").format(dbl);
        String val2 = StringHelper.fromDouble(dbl);
        String val3 = String.format(Locale.US, "%.4f", dbl);
        System.out.println(val2);
        System.out.println(val3);
        System.out.println("abc-def".split("[-]")[1]);
        
        long lng = Long.valueOf("999999888777666");
        System.out.println(StringHelper.fromLong(lng));
    }
}
