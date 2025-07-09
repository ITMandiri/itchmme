/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.socket.uhelpers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author aripam
 */
public class StringHelper {

    public StringHelper() {
    }
    
    public static String trimStr(String zInputString){
        String zOut = zInputString;
        try{
            if (!isNullOrEmpty(zInputString)){
                zOut = zInputString.trim();
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println("Exception:trimStr(" + zInputString + "): " + ex0);
//            if (zInputString != null){
//                System.err.println("Exception:trimStr*(" + zInputString + "): " + ex0);
//            }
        }
        return zOut;
    }
    
    public static int toInt(String zInputString){
        int iOut = 0;
        try{
            iOut = Integer.parseInt(zInputString);
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println("Exception:toInt(" + zInputString + "): " + ex0);
//            if (zInputString != null){
//                System.err.println("Exception:toInt*(" + zInputString + "): " + ex0);
//            }
        }
        return iOut;
    }
    
    public static long toLong(String zInputString){
        long lOut = 0;
        try{
            lOut = Long.parseLong(zInputString);
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println("Exception:toLong(" + zInputString + "): " + ex0);
        }
        return lOut;
    }
    
    public static double toDouble(String zInputString){
        double dOut = 0;
        try{
            dOut = Double.parseDouble(zInputString);
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println("Exception:toDouble(" + zInputString + "): " + ex0);
        }
        return dOut;
    }
    
    public static String fromInt(int iInputInteger){
        String zOut = "";
        try{
            zOut = Integer.toString(iInputInteger);
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println("Exception:fromInt(" + iInputInteger + "): " + ex0);
        }
        return zOut;
    }
    
    public static String fromLong(long lInputLong){
        String zOut = "";
        try{
            zOut = Long.toString(lInputLong);
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println("Exception:fromLong(" + lInputLong + "): " + ex0);
        }
        return zOut;
    }
    
    public static String fromDouble(double dInputDouble){
        String zOut = "";
        try{
            zOut = Double.toString(dInputDouble); //.tidak kuat.
            if (zOut.contains("E")){
                zOut = String.format(Locale.US, "%.4f",dInputDouble);
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println("Exception:fromDouble(" + dInputDouble + "): " + ex0);
        }
        return zOut;
    }
    
    public static String fromDoubleWithDigitGroup(double dInputDouble){
        String zOut = ""; //.format indonesia.
        try{
            //zOut = Double.toString(dInputDouble); //.tidak kuat.
            //zOut = String.format(Locale.US, "%.4f",dInputDouble);
            zOut = String.format(Locale.forLanguageTag("id"), "%1$,.2f",dInputDouble);
            
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println("Exception:fromDouble(" + dInputDouble + "): " + ex0);
        }
        return zOut;
    }
    
    
    public static boolean isNullOrEmpty(String zInputString){
        boolean bOut = false;
        try{
            if ((zInputString == null) || (zInputString.length() <= 0)){
                bOut = true;
            }
        }catch(Exception ex0){
            bOut = true;
            //.EXXX.
//            System.err.println(ex0);
        }
        return bOut;
    }
    
    public static String addSpaces(String zInputString, int iTotalLength){ //.tambah spasi [tanpa limit panjang string].
        String zOut = zInputString;
        try{
            if (iTotalLength < 0){
                iTotalLength = 0;
            }
            if (zInputString == null){
                zInputString = "";
            }
            int nStrLength = zInputString.length();
            if (nStrLength < iTotalLength){
                int nSpcLength = iTotalLength - nStrLength;
                if (nSpcLength > 0){
                    String zSpace = String.valueOf((char)32);
                    String zSpcFill = "";
                    for (int cPos = 1; cPos <= nSpcLength; cPos++){
                        zSpcFill = zSpcFill.concat(zSpace);
                    }
                    zOut = zInputString.concat(zSpcFill);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return zOut;
    }
    
    public static String addZeroFromInt(int iInputInteger, int iTotalLength){ //.tambah nol [tanpa limit panjang string].
        String zOut = fromInt(iInputInteger);
        try{
            if (iTotalLength < 0){
                iTotalLength = 0;
            }
            int nStrLength = zOut.length();
            if (nStrLength < iTotalLength){
                int nZeroLength = iTotalLength - nStrLength;
                if (nZeroLength > 0) {
                    String zZero = "0";
                    String zZeroFill = "";
                    for (int cPos = 1; cPos <= nZeroLength; cPos++){
                        zZeroFill = zZeroFill.concat(zZero);
                    }
                    if (zOut.startsWith("-")){
                        zOut = "-" + zZeroFill + zOut.substring(1);
                    }else{
                        zOut = zZeroFill + zOut;
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return zOut;
    }
    
    public static String[] splitAll(String zInputString, String zInputDelimiter){
        String [] rOut = new String[]{}; //.tidak pakai null. tetap dibuatkan.
        try{
            if ((zInputString != null) && (zInputDelimiter != null)){
                if ((zInputString.length() > 0)){
                    if (zInputDelimiter.length() > 0){
                        String tmpDelimiterOut = "";
                        for (int iPosChar = 0; iPosChar < zInputDelimiter.length(); iPosChar++){
                            tmpDelimiterOut = tmpDelimiterOut + "[" + zInputDelimiter.charAt(iPosChar) + "]";
                        }
                        zInputDelimiter = tmpDelimiterOut;
                    }
                    rOut = zInputString.split(zInputDelimiter, (-1));
                }else{
                    //.apa adanya:
                    rOut = new String[]{""};
                }
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return rOut;
    }
    
    public static String[] splitTwo(String zInputString, String zInputDelimiter){
        String [] rOut = new String[]{}; //.tidak pakai null. tetap dibuatkan.
        try{
            if ((zInputString != null) && (zInputDelimiter != null)){
                if ((zInputString.length() > 0)){
                    if (zInputDelimiter.length() > 0){
                        String tmpDelimiterOut = "";
                        for (int iPosChar = 0; iPosChar < zInputDelimiter.length(); iPosChar++){
                            
                            tmpDelimiterOut = tmpDelimiterOut + "[" + zInputDelimiter.charAt(iPosChar) + "]";
                        }
                        zInputDelimiter = tmpDelimiterOut;
                    }
                    rOut = zInputString.split(zInputDelimiter, (2)); //.biasa buat KeyValue.
                }else{
                    //.apa adanya:
                    rOut = new String[]{""};
                }
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return rOut;
    }
    
    public static String[] splitLimit(String zInputString, String zInputDelimiter, int nMaxLimit){
        String [] rOut = new String[]{}; //.tidak pakai null. tetap dibuatkan.
        try{
            if ((zInputString != null) && (zInputDelimiter != null)){
                if (nMaxLimit <= 0){
                    nMaxLimit = 1;
                }
                if ((zInputString.length() > 0)){
                    if (zInputDelimiter.length() > 0){
                        String tmpDelimiterOut = "";
                        for (int iPosChar = 0; iPosChar < zInputDelimiter.length(); iPosChar++){
                            
                            tmpDelimiterOut = tmpDelimiterOut + "[" + zInputDelimiter.charAt(iPosChar) + "]";
                        }
                        zInputDelimiter = tmpDelimiterOut;
                    }
                    rOut = zInputString.split(zInputDelimiter, (nMaxLimit)); //.biasa buat limit.
                }else{
                    //.apa adanya:
                    rOut = new String[]{""};
                }
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return rOut;
    }
    
    public static String getMD5(String zInputMessage){
        String zOut = "";
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] btHash = md5.digest(zInputMessage.getBytes("UTF-8"));
            //converting byte array to Hexadecimal String:
            StringBuilder sb = new StringBuilder(2 * btHash.length);
            for (byte b : btHash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            zOut = sb.toString();
            //.selesai:... .
        }catch(NoSuchAlgorithmException | UnsupportedEncodingException ex0){
            //.EXXX.
//            System.err.println(ex0);
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return zOut;
    }
    
    public static String reverseString(String original) {
        int length = original.length();
        String reverse = "";
        try{
            for (int i = (length - 1); i >= 0; i--) {
                reverse = reverse + original.charAt(i);
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return reverse;
    }

    public static String convertToBase64(String zInputString, String zInputEncodingName){
        String zOut = "";
        try{
            if (!isNullOrEmpty(zInputString)){
                if (isNullOrEmpty(zInputEncodingName)){
                    zInputEncodingName = "UTF-8";
                }
                zOut = DatatypeConverter.printBase64Binary(zInputString.getBytes(zInputEncodingName));
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return zOut;
    }
    
    public static String findFirstNumbers(String zInputString, int iStartIndex){
        String zOut = "";
        try{
            if (!isNullOrEmpty(zInputString)){
                int iLen = zInputString.length();
                for (int iPos = 0; iPos < iLen; iPos++){
                    if (iPos >= iStartIndex){
                        char chrCur = zInputString.charAt(iPos);
                        if ((chrCur >= 48) && (chrCur <= 57)){
                            zOut += (char)chrCur;
                        }
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return zOut;
    }
    
    
    
    public static String getNominalTerbilangIDN(double dInputNominal, boolean bWithCurrency){
        String zOut = "";
        try{
            //.penamaan bilangan dan kelompoknya:
            String[] arrBilangan = new String[]{"Nol", "Satu", "Dua", "Tiga", "Empat", "Lima", "Enam", "Tujuh", "Delapan", "Sembilan"};
            String[] arrKelompok = new String[]{"Sebelas", "Belas", "Sepuluh", "Puluh", "Seratus", "Ratus", "Ribu", "Juta", "Milyar", "Triliun", "Kuadriliun", "Kuintiliun", "Sekstiliun", "Septiliun", "Oktiliun", "Noniliun", "Desiliun"};
            String zKoma = "Koma";
            String zMataUang = "Rupiah";
            //.pisahkan antara depan decimal dan belakang decimal:
            String zBilTerbaca = "";
            String zBilBulat = "0";
            String zBilPecah = "0";
            String[] arrBilParts = splitTwo(fromDouble(dInputNominal), ".");
            if (arrBilParts != null){
                if (arrBilParts.length == 1){
                    zBilBulat = arrBilParts[0];
                }else if (arrBilParts.length >= 2){
                    zBilBulat = arrBilParts[0];
                    zBilPecah = arrBilParts[1];
                }
            }
            //.untuk yg bulat:
            ArrayList<String> lstDigitGroupBulatParts = new ArrayList<>();
            String zPerGroupAppend = "";
            for (int ipc = (zBilBulat.length() - 1); ipc >= 0 ; ipc--){
                zPerGroupAppend = zBilBulat.charAt(ipc) + zPerGroupAppend;
                if (zPerGroupAppend.length() >= 3){
                    lstDigitGroupBulatParts.add(zPerGroupAppend);
                    zPerGroupAppend = "";
                }
            }
            if (zPerGroupAppend.length() > 0){ //.sisa yg bulat... .
                lstDigitGroupBulatParts.add(zPerGroupAppend);
                zPerGroupAppend = "";
            }
            //.untuk yg pecahan:
            //... .tetap... .
            //.lanjutan:
            //.baca yg bulat:
            for (int ibc = (lstDigitGroupBulatParts.size() - 1); ibc >= 0; ibc--){
                char[] chsBilPart = StringHelper.fromInt(StringHelper.toInt(lstDigitGroupBulatParts.get(ibc))).toCharArray(); //.pastikan bilangan yg utuh saja.
                if ((chsBilPart.length == 1) && (chsBilPart[0] == '0')){ //.digit group '000' tidak usah dibaca.
                    continue;
                }
                int uboundChs = (chsBilPart.length - 1);
                String zSatuan = "";
                String zPuluhan = "";
                String zRatusan = "";
                for (int ipa = 0; ipa <= uboundChs; ipa++){
                    char chCur = chsBilPart[uboundChs - ipa]; //.ambil dari kanan.
                    switch(ipa){
                        case 0: //.satuan:
                            if (uboundChs >= 1){
                                if (chsBilPart[uboundChs - ipa - 1] == '1'){ //.belasan.
                                    //.skip-next... .
                                }else if (chCur == '0'){
                                    //.skip-next... .
                                }else{ //.n:
                                    //.langsung:
                                    zSatuan = (" " + arrBilangan[chCur - 48]);
                                }
                            }else{ //.n:
                                //.langsung:
                                zSatuan = (" " + arrBilangan[chCur - 48]);
                            }
                            break;
                        case 1: //.puluhan:
                            if (chCur == '1'){ //.sepuluh atau belasan:
                                char chSatuan = chsBilPart[uboundChs - 0]; //.ambil satuan paling kanan.
                                if (chSatuan == '0'){
                                    zPuluhan = (" " + arrKelompok[2]);
                                }else if (chSatuan == '1'){
                                    zPuluhan = (" " + arrKelompok[0]);
                                }else{
                                    zPuluhan = (" " + (arrBilangan[chSatuan - 48] + " " + arrKelompok[1]));
                                }
                            }else if (chCur == '0'){
                                //.skip-forget... .
                            }else{ //.n puluh:
                                zPuluhan = (" " + (arrBilangan[chCur - 48] + " " + arrKelompok[3]));
                            }
                            break;
                        case 2: //.ratusan:
                            if (chCur == '1'){ //.seratus:
                                zRatusan = (" " + arrKelompok[4]);
                            }else{ //.n ratus:
                                zRatusan = (" " + (arrBilangan[chCur - 48] + " " + arrKelompok[5]));
                            }
                            break;
                    }
                }
                
                zBilTerbaca += (zRatusan + zPuluhan + zSatuan);
                if (ibc > 0){
                    zBilTerbaca += (" " + arrKelompok[ibc + 5]);
                }
            }
            //.baca yg pecah:
            if (StringHelper.toLong(zBilPecah) > 0){
                zBilTerbaca += (" " + zKoma);
                for (char chp : zBilPecah.toCharArray()){
                    zBilTerbaca += (" " + arrBilangan[chp - 48]);
                }
            }
            //.baca mata uang:
            if (bWithCurrency){
                zBilTerbaca += (" " + zMataUang);
            }
            //.kembalikan:
            zOut = zBilTerbaca.trim();
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return zOut;
    }
    
    //.20250106
    public static HashMap toHashMap(String data) {
        HashMap<String, Object> mapOut = new HashMap<>();
        try{
            String keyValuePairs = data.replaceAll("[{}\\s]", "");
            String[] pairs = keyValuePairs.split(",");

            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    mapOut.put(keyValue[0], keyValue[1]);
                }
            }
        }catch(Exception ex0){
            //.EXXX.
//            System.err.println(ex0);
        }
        return mapOut;
        
    }
    
}
