/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.helpers;

import com.itm.generic.engine.socket.uhelpers.StringHelper;

/**
 *
 * @author aripam
 */
public class BrokerReferenceHelper {
    
    private final static int RADIX_BASE36                       = 36;
    
    private final static String BROKER_REF_DELIMITER            = ";";
    
    public BrokerReferenceHelper() {
        //.EXXX.
    }
    
    public static String generateBrokerRef(int nOrderID, int nClientAccountID, int nExecutorID){
        String zOut = "0;0;0"; //.xxxxx;yyyy;zzzz
        //.MAX_3=     46.655	ALPHA_3=ZZZ
        //.MAX_4=  1.679.615	ALPHA_4=ZZZZ
        //.MAX_5= 60.466.175	ALPHA_5=ZZZZZ
        try{
            
            //.jadikan alphanumeric positive:
            String zORDID = PositiveIntToAlphaNumBase(nOrderID);
            String zCLAID = PositiveIntToAlphaNumBase(nClientAccountID);
            String zEXCID = PositiveIntToAlphaNumBase(nExecutorID);
            
            //.digabungkan:
            zOut = zORDID + BROKER_REF_DELIMITER + zCLAID + BROKER_REF_DELIMITER + zEXCID;
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static int getOrderID_BrokerRef(String zGenBrokerRef){
        int iOut = 0; //.index=0.
        try{
            if ((zGenBrokerRef != null) && (zGenBrokerRef.length() > 0)){
                String zIn = zGenBrokerRef.split("[" + BROKER_REF_DELIMITER + "]")[0];
                if ((zIn != null) && (zIn.length() > 0)){
                    iOut = PositiveAlphaNumBaseToInt(zIn);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return iOut;
    }
    
    public static int getClientAccountID_BrokerRef(String zGenBrokerRef){
        int iOut = 0; //.index=1.
        try{
            if ((zGenBrokerRef != null) && (zGenBrokerRef.length() > 0)){
                String zIn = zGenBrokerRef.split("[" + BROKER_REF_DELIMITER + "]")[1];
                if ((zIn != null) && (zIn.length() > 0)){
                    iOut = PositiveAlphaNumBaseToInt(zIn);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return iOut;
    }
    
    public static int getExecutorID_BrokerRef(String zGenBrokerRef){
        int iOut = 0; //.index=2.
        try{
            if ((zGenBrokerRef != null) && (zGenBrokerRef.length() > 0)){
                String zIn = zGenBrokerRef.split("[" + BROKER_REF_DELIMITER + "]")[2];
                if ((zIn != null) && (zIn.length() > 0)){
                    iOut = PositiveAlphaNumBaseToInt(zIn);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return iOut;
    }
    
    public static String PositiveIntToAlphaNumBase(int iInput){
        String zOut = ""; //.alphanumeric=base36.
        try{
            //.tidak menerima bilangan negatif:
            if (iInput >= 0){
                zOut = Integer.toString(iInput, RADIX_BASE36);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static int PositiveAlphaNumBaseToInt(String zAlphaNumeric){
        int iOut = 0; //.min=0, max=2147483647. .alphanumeric=base36.
        try{
            if ((zAlphaNumeric != null) && (zAlphaNumeric.length() > 0)){
                iOut = Integer.parseInt(zAlphaNumeric, RADIX_BASE36);
                //.tidak menerima bilangan negatif:
                if (iOut < 0){
                    iOut = 0;
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return iOut;
    }
    
    public static String findBrokerRefFromJatsError(String zInputRefErrorCode){
        String zOut = "";//.cek brokerref (cari field "broker"+"ref":'value1;value2;value3'):
        try{
            if ((zInputRefErrorCode != null) && (zInputRefErrorCode.length() > 0)){
                //.cek apakah delimiter ada (2):
                int cDelimiterCount = zInputRefErrorCode.split("[" + BROKER_REF_DELIMITER + "]").length;
                if (cDelimiterCount >= 3){
                    //.cari "broker":
                    final String z_broker = "broker";
                    final String z_ref = "ref";
                    final String z_kv = ":";
                    int pLastPos = zInputRefErrorCode.toLowerCase().lastIndexOf(z_broker);
                    if (pLastPos >= 0){
                        pLastPos += z_broker.length();
                        pLastPos = zInputRefErrorCode.toLowerCase().indexOf(z_ref, pLastPos);
                        if (pLastPos >= 0){
                            pLastPos += z_ref.length();
                            pLastPos = zInputRefErrorCode.toLowerCase().indexOf(z_kv, pLastPos);
                            if (pLastPos >= 0){
                                pLastPos += z_kv.length();
                                String zRawBrokerRef = zInputRefErrorCode.substring(pLastPos);
                                zRawBrokerRef = zRawBrokerRef.trim();
                                if (zRawBrokerRef.length() > 0){
                                    //.cek format:
                                    if (StringHelper.splitAll(zRawBrokerRef, BROKER_REF_DELIMITER).length == 3){
                                        //... .
                                        //.kembali:
                                        zOut = zRawBrokerRef;
                                        return zOut;
                                    }
                                }
                            }
                        }
                    }
                    //.cari untuk format lain, cek versi 2:
                    if (StringHelper.isNullOrEmpty(zOut)){
                        String[] arrFreeSpaces = StringHelper.splitAll(zInputRefErrorCode, " "); //pisahkan menggunakan spasinya.
                        if ((arrFreeSpaces != null) && (arrFreeSpaces.length > 2)){
                            for (String zPerFieldStage : arrFreeSpaces){
                                if ((!StringHelper.isNullOrEmpty(zPerFieldStage)) && (StringHelper.splitAll(zPerFieldStage.trim(), BROKER_REF_DELIMITER).length == 3)){
                                    //... .
                                    //.kembali:
                                    zOut = zPerFieldStage.trim();
                                    return zOut;
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
}
