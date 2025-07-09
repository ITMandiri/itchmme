/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.helpers;

/**
 *
 * @author aripam
 */
public class IDXConverterHelper {
    
    public IDXConverterHelper() {
    }
    
    public static String fromIDXBoardToTSBoard(String zIDXBoard){
        String zOut = zIDXBoard;
        try{
            if (zIDXBoard.startsWith("0")){
                zOut = zIDXBoard.substring(1);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String fromSIDToComplianceTradingID(String zSID){
        String zOut = "0";
        try{
            if (zSID != null){
                if (zSID.length() >= 13){
                    zOut = zSID.substring(7, 13);
                }else if (zSID.length() > 0){
                    zOut = zSID;
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String fromIDXDateTimeToStdDateFormat(String yyyyMMDD_HHmmssSSS){
        String zOut = yyyyMMDD_HHmmssSSS;
        try{
            zOut = yyyyMMDD_HHmmssSSS.substring(0, 4) + "-" + yyyyMMDD_HHmmssSSS.substring(4,6) + "-" + yyyyMMDD_HHmmssSSS.substring(6,8);
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return zOut;
    }
    
    public static String fromIDXDateTimeToStdTimeFormat(String yyyyMMDD_HHmmssSSS){
        String zOut = yyyyMMDD_HHmmssSSS;
        try{
            zOut = yyyyMMDD_HHmmssSSS.split("[-]")[1];
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return zOut;
    }
    
    public static String toStdTimeFormat(String HHmmssSSS){
        String zOut = HHmmssSSS;
        try{
            zOut = HHmmssSSS.substring(0, 2) + ":" + HHmmssSSS.substring(2,4) + ":" + HHmmssSSS.substring(4,6) + "." + HHmmssSSS.substring(6,9);
        }catch(Exception ex0){
            //.EXXX.
            System.err.println(ex0);
        }
        return zOut;
    }
    
}
