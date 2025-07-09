/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.util;

/**
 *
 * @author Hirin
 */
public class StringUtil {

    public StringUtil(){
    }
    
    public static int toInteger(String val){
        try{
            if (val == null || val == "" || val == "0.0") return 0;
            return Integer.valueOf(val);
        }catch(Exception ex){
            UnitTest.log("Exception: StringUtil.toInteger(" + val + ")" + ex.toString());
        }
        
        return 0;
    }
    
    public static long toLong(String val){
        try{
            if (val == null || val == "" || val == "0.0") return 0;
            return Long.valueOf(val);
        }catch(Exception ex){
            //UnitTest.log("Exception: StringUtil.toLong(" + val + ")" + ex.toString());
        }
        
        return 0;
    }
    
    public static double toDouble(String val){
        try{
            if (val == null || val == "") return 0;
            return Double.valueOf(val);
        }catch(Exception ex){
            UnitTest.log("Exception: StringUtil.toDouble(" + val + ")" + ex.toString());
        }
        
        return 0;
    }
    
    private static String generateSpace(int len){
        String space = "";
        for (int i = 0; i < len; i++){
            space += " ";
        }        
        return space;
    }
    
    public static String rightJustify(Object val, int charLen){
        if (val == null) return "";
        if (val.toString().length() < charLen){
            return val.toString() + generateSpace(charLen - val.toString().length());
        }
        
        return val.toString();
    }
}
