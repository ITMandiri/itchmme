/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.packetformat;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPBridgePacketFormat {
    
    public static final char CHARACTER_SPACE                                    = ' ';
    public static final char CHARACTER_NULL                                     = '\0';
    
    public static final String STRING_DECODE_FORMAT                             = "ASCII";
    
    public static final char[] CONFIG_HEX_ARRAY                                 = "0123456789ABCDEF".toCharArray();
    
    public final static String DEF_FORMAT_IDX_TRX_DATETIME_NORMAL   = "yyyyMMdd-HH:mm:ss";
    public final static String DEF_FORMAT_SVR_TRX_DATETIME_NORMAL   = "yyyy-MM-dd HH:mm:ss";
    public final static String DEF_FORMAT_IDX_TRX_DATE_NORMAL       = "yyyyMMdd";
    public final static String DEF_FORMAT_SVR_TRX_DATE_NORMAL       = "yyyy-MM-dd";
    public final static String DEF_FORMAT_SVR_TRX_TIME_NORMAL       = "HH:mm:ss";
    
    public final static String DEF_FORMAT_IDX_TRX_TIME_DATAFEED     = "HHmmss";
    public final static String DEF_FORMAT_IDX_TRX_DATE_DATAFEED     = "yyyyMMdd";
    
    public final static String DEF_FORMAT_IDN_TRX_DATE_NORMAL       = "dd-MMM-yyyy";
    
    public final static String DEF_FORMAT_DATE_FILESAFE             = "yyyyMMdd";
    public final static String DEF_FORMAT_DATETIME_FILESAFE         = "yyyyMMdd_HHmmss";
    
    
    public final static int I_SERVERDAY_UNKNOWN                     = 0;
    public final static int I_SERVERDAY_MONDAY                      = 1;
    public final static int I_SERVERDAY_TUESDAY                     = 2;
    public final static int I_SERVERDAY_WEDNESDAY                   = 3;
    public final static int I_SERVERDAY_THURSDAY                    = 4;
    public final static int I_SERVERDAY_FRIDAY                      = 5;
    public final static int I_SERVERDAY_SATURDAY                    = 6;
    public final static int I_SERVERDAY_SUNDAY                      = 7;
    
    public final static String Z_SERVERDAY_UNKNOWN                  = "???";
    public final static String Z_SERVERDAY_SENIN                    = "Senin";
    public final static String Z_SERVERDAY_SELASA                   = "Selasa";
    public final static String Z_SERVERDAY_RABU                     = "Rabu";
    public final static String Z_SERVERDAY_KAMIS                    = "Kamis";
    public final static String Z_SERVERDAY_JUMAT                    = "Jum'at";
    public final static String Z_SERVERDAY_SABTU                    = "Sabtu";
    public final static String Z_SERVERDAY_MINGGU                   = "Minggu";
    
    public static byte[] bufferAlloc(int vLength, char vFill){
        return bufferAlloc(vLength,(byte)vFill);
    }
    
    public static byte[] bufferAlloc(int vLength, byte vFill){
        byte[] mOut = {};
        if (vLength > 0){
            mOut = new byte[vLength];
            for(int p = 0; p < mOut.length; p++){
                mOut[p] = vFill;
            }
        }
        return mOut;
    }
    
    public static byte[] bufferAppend(byte[] vBuffer1, byte[] vBuffer2){
        byte[] mOut = new byte[((vBuffer1 != null) ? vBuffer1.length : 0) + ((vBuffer2 != null) ? vBuffer2.length : 0)];
        if ((vBuffer1 != null) && (vBuffer1.length > 0)){
            System.arraycopy(vBuffer1, 0, mOut, 0, vBuffer1.length);
        }
        if ((vBuffer2 != null) && (vBuffer2.length > 0)){
            System.arraycopy(vBuffer2, 0, mOut, ((vBuffer1 != null) ? vBuffer1.length : 0), vBuffer2.length);
        }
        return mOut;
    }
    
    public static byte[] bufferSubGet(byte[] vBuffer, int vOffset, int vLength){
        byte[] mOut = {};
        if ((vBuffer != null) && (vBuffer.length > 0) && (vOffset < vBuffer.length) && (vLength > 0)){
            if ((vOffset + vLength) > vBuffer.length){
                vLength = (vBuffer.length - vOffset);
            }
            mOut = new byte[vLength];
            System.arraycopy(vBuffer, vOffset, mOut, 0, vLength);
        }
        return mOut;
    }
    
    public static byte[] bufferSubSet(byte[] vBufferDest, byte[] vBufferSrc, int vOffsetDest, int vLengthSrc){
        byte[] mOut = vBufferDest;
        if ((vBufferDest != null) && (vBufferDest.length > 0) && (vBufferSrc != null) && (vBufferSrc.length > 0) && (vOffsetDest >= 0) && (vOffsetDest < vBufferDest.length) && (vLengthSrc > 0)){
            if (vLengthSrc > vBufferSrc.length){
                vLengthSrc = vBufferSrc.length;
            }
            if ((vOffsetDest + vLengthSrc) > vBufferDest.length){
                vLengthSrc = (vBufferDest.length - vOffsetDest);
            }
            System.arraycopy(vBufferSrc, 0, mOut, vOffsetDest, vLengthSrc);
        }
        return mOut;
    }
    
    public static byte[] encodeShort(int vShort, int vLimitBytesLength){
        byte[] mOut = {};
        if (vLimitBytesLength > 0){
            byte[] arb = ByteBuffer.allocate(Short.SIZE / 8).putInt(vShort).array();
            if (vLimitBytesLength > arb.length){
                vLimitBytesLength = arb.length;
            }
            mOut = new byte[vLimitBytesLength];
            for(int p = (arb.length - 1); p >= 0; p--){
                vLimitBytesLength--;
                if (vLimitBytesLength >= 0){
                    mOut[vLimitBytesLength] = arb[p];
                }else{
                    break;
                }
            }
        }
        return mOut;
    }
    
    public static byte[] encodeInteger(int vInteger, int vLimitBytesLength){
        byte[] mOut = {};
        if (vLimitBytesLength > 0){
            byte[] arb = ByteBuffer.allocate(Integer.SIZE / 8).putInt(vInteger).array();
            if (vLimitBytesLength > arb.length){
                vLimitBytesLength = arb.length;
            }
            mOut = new byte[vLimitBytesLength];
            for(int p = (arb.length - 1); p >= 0; p--){
                vLimitBytesLength--;
                if (vLimitBytesLength >= 0){
                    mOut[vLimitBytesLength] = arb[p];
                }else{
                    break;
                }
            }
        }
        return mOut;
    }
    
    public static byte[] encodeLong(long vLong, int vLimitBytesLength){
        byte[] mOut = {};
        if (vLimitBytesLength > 0){
            byte[] arb = ByteBuffer.allocate(Long.SIZE / 8).putLong(vLong).array();
            if (vLimitBytesLength > arb.length){
                vLimitBytesLength = arb.length;
            }
            mOut = new byte[vLimitBytesLength];
            for(int p = (arb.length - 1); p >= 0; p--){
                vLimitBytesLength--;
                if (vLimitBytesLength >= 0){
                    mOut[vLimitBytesLength] = arb[p];
                }else{
                    break;
                }
            }
        }
        return mOut;
    }
    
    public static byte[] encodeString(String vString, int vLimitBytesLength){
        byte[] mOut = {};
        if ((vString != null) && (vString.length() > 0) && (vLimitBytesLength > 0)){
            byte[] arb = vString.getBytes();
            vLimitBytesLength = ((arb.length < vLimitBytesLength) ? arb.length : vLimitBytesLength );
            mOut = new byte[vLimitBytesLength];
            System.arraycopy(arb, 0, mOut, 0, vLimitBytesLength);
        }
        return mOut;
    }
    
    public static byte[] encodeStringLeftPadded(String vString, int vLimitBytesLength, char vFill){
        byte[] mOut = bufferAlloc(vLimitBytesLength, vFill); //.contoh:"      12345";
        if ((vString != null) && (vString.length() > 0)){
            byte[] arb = encodeString(vString,vLimitBytesLength);
            mOut = bufferSubSet(mOut, arb, 0, arb.length);
        }
        return mOut;
    }
    
    public static byte[] encodeStringRightPadded(String vString, int vLimitBytesLength, char vFill){
        byte[] mOut = bufferAlloc(vLimitBytesLength, vFill); //.contoh:"12345     ";
        if ((vString != null) && (vString.length() > 0)){
            byte[] arb = encodeString(vString,vLimitBytesLength);
            mOut = bufferSubSet(mOut, arb, (mOut.length - arb.length), arb.length);
        }
        return mOut;
    }
    
    public static byte decodeByte(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        return buffer.get();
    }
    
    public static short decodeShort(byte[] vBuffer, int vOffset, int vLength){
        short mOut = 0;
        if (vLength > (Short.SIZE / 8)){
            vLength = (Short.SIZE / 8);
        }
        byte[] arb = bufferAlloc((Short.SIZE / 8), (byte)0);
        arb = bufferSubSet(arb, bufferSubGet(vBuffer, vOffset, vLength), ((Short.SIZE / 8) - vLength), vLength);
        if ((arb != null) && (arb.length > 0)){
            vLength = arb.length;
            for(int p = 0; p < vLength; p++){
                //mOut |= ((arb[p] & 0xFF) << (Short.SIZE - (8 * (p + 1)))); //.salah;
                mOut |= (arb[p] & 0xFF);
                if (p < (vLength - 1)){
                    mOut <<= 8;
                }
            }
        }
        return mOut;
    }
    
    public static int decodeInteger(byte[] vBuffer, int vOffset, int vLength){
        int mOut = 0;
        if (vLength > (Integer.SIZE / 8)){
            vLength = (Integer.SIZE / 8);
        }
        byte[] arb = bufferAlloc((Integer.SIZE / 8), (byte)0);
        arb = bufferSubSet(arb, bufferSubGet(vBuffer, vOffset, vLength), ((Integer.SIZE / 8) - vLength), vLength);
        if ((arb != null) && (arb.length > 0)){
            vLength = arb.length;
            for(int p = 0; p < vLength; p++){
                //mOut |= ((arb[p] & 0xFF) << (Integer.SIZE - (8 * (p + 1)))); //.salah;
                mOut |= (arb[p] & 0xFF);
                if (p < (vLength - 1)){
                    mOut <<= 8;
                }
            }
        }
        return mOut;
    }
    
    public static long decodeUnsignedInteger(byte[] vBuffer, int vOffset, int vLength){
        long mOut = 0;
        if (vLength > (Integer.SIZE / 8)){
            vLength = (Integer.SIZE / 8);
        }
        byte[] arb = bufferAlloc((Integer.SIZE / 8), (byte)0);
        arb = bufferSubSet(arb, bufferSubGet(vBuffer, vOffset, vLength), ((Integer.SIZE / 8) - vLength), vLength);
        if ((arb != null) && (arb.length > 0)){
            vLength = arb.length;
            for(int p = 0; p < vLength; p++){
                //mOut |= ((arb[p] & 0xFF) << (Integer.SIZE - (8 * (p + 1)))); //.salah;
                mOut |= (arb[p] & 0xFF);
                if (p < (vLength - 1)){
                    mOut <<= 8;
                }
            }
        }
        return mOut;
    }
    
    public static long decodeLong(byte[] vBuffer, int vOffset, int vLength){
        long mOut = 0;
        if (vLength > (Long.SIZE / 8)){
            vLength = (Long.SIZE / 8);
        }
        byte[] arb = bufferAlloc((Long.SIZE / 8), (byte)0);
        arb = bufferSubSet(arb, bufferSubGet(vBuffer, vOffset, vLength), ((Long.SIZE / 8) - vLength), vLength);
        if ((arb != null) && (arb.length > 0)){
            vLength = arb.length;
            for(int p = 0; p < vLength; p++){
                //mOut |= ((arb[p] & 0xFF) << (Long.SIZE - (8 * (p + 1)))); //.salah;
                mOut |= (arb[p] & 0xFF);
                if (p < (vLength - 1)){
                    mOut <<= 8;
                }
            }
        }
        return mOut;
    }
    
    public static double decodeDouble(byte[] vBuffer, int vOffset, int vLength){
        long mOut = 0;
        if (vLength > (Long.SIZE / 8)){
            vLength = (Long.SIZE / 8);
        }
        byte[] arb = bufferAlloc((Long.SIZE / 8), (byte)0);
        arb = bufferSubSet(arb, bufferSubGet(vBuffer, vOffset, vLength), ((Long.SIZE / 8) - vLength), vLength);
        if ((arb != null) && (arb.length > 0)){
            vLength = arb.length;
            for(int p = 0; p < vLength; p++){
                mOut |= (arb[p] & 0xFF);
                if (p < (vLength - 1)){
                    mOut <<= 8;
                }
            }
        }
        return Double.longBitsToDouble(mOut);
    }

    public static String decodeString(byte[] vBuffer, int vOffset, int vLength){
        String mOut = "";
        if ((vBuffer != null) && (vBuffer.length > 0) && (vOffset > 0) && (vLength > 0)){
            if ((vOffset + vLength) > vBuffer.length){
                vLength = (vBuffer.length - vOffset);
            }
            try {
                mOut = new String(bufferSubGet(vBuffer,vOffset,vLength), STRING_DECODE_FORMAT);
            } catch (UnsupportedEncodingException ex0) {
                //.EXXX.
            } catch (Exception ex0) {
                //.EXXX.
            }
        }
        return mOut;
    }
    
    public static String decodeStringTrim(byte[] vBuffer, int vOffset, int vLength){
        return decodeString(vBuffer, vOffset, vLength).trim();
    }
    
    public static String decodeStringNullTerminatedTrim(byte[] vBuffer, int vOffset, int vLength){
        String mOut = decodeString(vBuffer, vOffset, vLength);
        if (mOut != null){
            mOut = mOut.replace("\0", "").trim();
        }
        return mOut;
    }
    
    public static String convertBytesToHex(byte[] vBuffer){
        char[] arHexChars = new char[vBuffer.length * 2];
        for (int j = 0; j < vBuffer.length; j++) {
            int v = vBuffer[j] & 0xFF;
            arHexChars[j * 2] = CONFIG_HEX_ARRAY[v >>> 4];
            arHexChars[j * 2 + 1] = CONFIG_HEX_ARRAY[v & 0x0F];
        }
        return new String(arHexChars);
    }
    
    public static byte[] convertHexStringToBytes(String vHexString) {
        int len = ((vHexString != null) ? vHexString.length() : 0);
        byte[] mOut = new byte[len / 2];
        if (vHexString != null) {
            for (int i = 0; i < len; i += 2) {
                mOut[i / 2] = (byte) ((Character.digit(vHexString.charAt(i), 16) << 4)
                                     + Character.digit(vHexString.charAt(i+1), 16));
            }
        }
        return mOut;
    }
    
    public static boolean compareBytesIsEqual(byte[] vBuffer1, byte[] vBuffer2){
        boolean mOut = false;
        if ((vBuffer1 != null) && (vBuffer1.length > 0) && (vBuffer2 != null) && (vBuffer2.length > 0) && (vBuffer1.length == vBuffer2.length)){
            mOut = true;
            for(int p = 0; p < vBuffer1.length; p++){
                if (vBuffer1[p] != vBuffer2[p]){
                    mOut = false;
                    break;
                }
            }
        }
        return mOut;
    }
    
    public static boolean compareMessageIsEqual(byte[] vBuffer1, byte[] vBuffer2, boolean exactTimeStamp){
        boolean mOut = false;
        if ((vBuffer1 != null) && (vBuffer1.length > 0) && (vBuffer2 != null) && (vBuffer2.length > 0) && (vBuffer1.length == vBuffer2.length)){
            if (exactTimeStamp){
                mOut = compareBytesIsEqual(vBuffer1, vBuffer2);
            }else{
                mOut = true;
                for(int p = 0; p < vBuffer1.length; p++){
                    if (((p <= 3) || (p >= 8)) && (vBuffer1[p] != vBuffer2[p])){
                        mOut = false;
                        break;
                    }
                }
            }
        }
        return mOut;
    }
    
//////    public static String convertSlowBytesToHex(byte[] vBuffer){
//////        String mOut = "";
//////        if ((vBuffer != null) && (vBuffer.length > 0)){
//////            for (byte bt : vBuffer) {
//////                mOut += String.format("%02X", bt);
//////            }
//////        }
//////        return mOut;
//////    }
    
    public static int toInt(String zInputString){
        int iOut = 0;
        try{
            iOut = Integer.parseInt(zInputString);
        }catch(NumberFormatException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
        }
        return iOut;
    }
    
    public static long toLong(String zInputString){
        long lOut = 0;
        try{
            lOut = Long.parseLong(zInputString);
        }catch(NumberFormatException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
        }
        return lOut;
    }
    
    public static double toDouble(String zInputString){
        double dOut = 0;
        try{
            dOut = Double.parseDouble(zInputString);
        }catch(NumberFormatException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
        }
        return dOut;
    }
    
    public static String fromInt(int iInputInteger){
        String zOut = "";
        try{
            zOut = Integer.toString(iInputInteger);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String fromLong(long lInputLong){
        String zOut = "";
        try{
            zOut = Long.toString(lInputLong);
        }catch(Exception ex0){
            //.EXXX.
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
        }catch(NoSuchAlgorithmException ex0){
            //.EXXX.
        }catch(UnsupportedEncodingException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
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
        }catch(UnsupportedEncodingException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
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
        }
        return zOut;
    }
    
    public static double doubleFromParts(long number, long decimalPoint){
        double mOut = 0;
        if (number != 0){
            if (decimalPoint > 0){
                boolean signed = (number < 0);
                String numText = fromLong(Math.abs(number));
                mOut = toDouble(((numText.length() - decimalPoint) > 0) ? (numText.substring(0, (int) (numText.length() - decimalPoint))) : fromLong(0));
                double decValue = toDouble("0." + (((numText.length() - decimalPoint) >= 0) ? numText.substring((int) (numText.length() - decimalPoint)) : addZeroFromInt(toInt(numText), (int) decimalPoint)));
                mOut += decValue;
                if (signed){
                    mOut = (mOut * -1);
                }
            }else{
                mOut = (double)number;
            }
        }
        return mOut;
    }
    
    public static boolean isValidDateFormatString(String zInputDateString, String zDateFormat){
        boolean mOut = false;
        try{
            if (
                (!isNullOrEmpty(zInputDateString)) 
                && (!isNullOrEmpty(zDateFormat))
                && (zInputDateString.length() == zDateFormat.length())
            ){
                SimpleDateFormat sdf = new SimpleDateFormat(zDateFormat);
                if (zInputDateString.equals(sdf.format(sdf.parse(zInputDateString)))){
                    mOut = true;
                }
            }
        }catch(ParseException ex){
        }catch(Exception ex){
        }
        return mOut;
    }
    
    public static String getDateTimeIDXTRXFormat(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATETIME_NORMAL);
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateTimeSVRTRXFormat(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATETIME_NORMAL);
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String buildDateTimeSVRTRXFormat(Date dtInput){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATETIME_NORMAL);
            zOut = cTimeFormat.format(dtInput);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String buildDateSVRTRXFormat(Date dtInput){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
            zOut = cTimeFormat.format(dtInput);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String buildTimeSVRTRXFormat(Date dtInput){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
            zOut = cTimeFormat.format(dtInput);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateSVRTRXFormat(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getTimeSVRTRXFormat(){
        String zOut = "";
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
            Date cDate = new Date();
            zOut = cTimeFormat.format(cDate);
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static Date getDateSVR(){
        Date mOut = null;
        try{
            //.otomatis hari ini:
            mOut = new Date();
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static String getDateIDXTRXFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATE_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateSVRTRXFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getTimeSVRTRXFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getTimeDataFeedFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_TIME_DATAFEED);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static String getDateDataFeedFormatFromDate(Date dtInputDate){
        String zOut = "";
        try{
            if (dtInputDate != null){
                DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATE_DATAFEED);
                zOut = cTimeFormat.format(dtInputDate);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return zOut;
    }
    
    public static Date parseToDateTimeIDXFormat(String zInputDateString){
        Date mOut = null;
        try{
            if (!isNullOrEmpty(zInputDateString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATETIME_NORMAL);
                mOut = cDateFormat.parse(zInputDateString);
            }
        }catch(ParseException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static Date parseToDateTimeSVRFormat(String zInputDateString){
        Date mOut = null;
        try{
            if (!isNullOrEmpty(zInputDateString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATETIME_NORMAL);
                mOut = cDateFormat.parse(zInputDateString);
            }
        }catch(ParseException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static Date parseToDateIDXFormat(String zInputDateString){
        Date mOut = null;
        try{
            if (!isNullOrEmpty(zInputDateString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_IDX_TRX_DATE_NORMAL);
                mOut = cDateFormat.parse(zInputDateString);
            }
        }catch(ParseException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static Date parseToDateSVRFormat(String zInputDateString){
        Date mOut = null;
        try{
            if (!isNullOrEmpty(zInputDateString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
                mOut = cDateFormat.parse(zInputDateString);
            }
        }catch(ParseException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static Date parseToTimeSVRFormat(String zInputTimeString){
        Date mOut = null;
        try{
            if (!isNullOrEmpty(zInputTimeString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
                mOut = cDateFormat.parse(zInputTimeString);
            }
        }catch(ParseException ex0){
            //.EXXX.
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static boolean hasCommaDelimitedTimeSVRTRXFormatError(String zInputTimeString){
        boolean bOut = false; //.09:00:00,13:30:00,... .
        try{
            if (!isNullOrEmpty(zInputTimeString)){
                String[] arrInputTimes = splitAll(zInputTimeString, ",");
                if (arrInputTimes.length > 0){
                    for (String zTime : arrInputTimes){
                        if (hasTimeSVRTRXFormatError(zTime)){
                            bOut = true;
                            break;
                        }
                    }
                }else{
                    bOut = true;
                }
            }else{
                bOut = true;
            }
        }catch(Exception ex0){
            bOut = true;
            //.EXXX.
        }
        return bOut;
    }
    
    public static boolean hasTimeSVRTRXFormatError(String zInputTimeString){
        boolean bOut = false;
        try{
            if (!isNullOrEmpty(zInputTimeString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_TIME_NORMAL);
                Date mOut = cDateFormat.parse(zInputTimeString);
                if (mOut == null){
                    bOut = true;
                }
            }else{
                bOut = true;
            }
        }catch(ParseException ex0){
            bOut = true;
            //.EXXX.
        }catch(Exception ex0){
            bOut = true;
            //.EXXX.
        }
        return bOut;
    }
    
    public static boolean hasDateSVRTRXFormatError(String zInputTimeString){
        boolean bOut = false;
        try{
            if (!isNullOrEmpty(zInputTimeString)){
                DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_SVR_TRX_DATE_NORMAL);
                Date mOut = cDateFormat.parse(zInputTimeString);
                if (mOut == null){
                    bOut = true;
                }
            }else{
                bOut = true;
            }
        }catch(ParseException ex0){
            bOut = true;
            //.EXXX.
        }catch(Exception ex0){
            bOut = true;
            //.EXXX.
        }
        return bOut;
    }
    
    public static Date addDays(Date dateStart, int iDaysAddCount){
        Date dOut = null; //iDaysAddCount relatif terhadap hari ini. (negatif = dikurangi, positif = ditambah).
        try{
            if (dateStart != null){
                dOut = dateStart;
                if (iDaysAddCount != 0){
                    Calendar gCalendar = new GregorianCalendar();
                    gCalendar.setTime(dateStart);
                    gCalendar.add(GregorianCalendar.DAY_OF_MONTH, iDaysAddCount);
                    dOut = gCalendar.getTime();
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return dOut;
    }
    
    public static Date addMinutes(Date dateStart, int iMinutesAddCount){
        Date dOut = null; //iDaysAddCount relatif terhadap hari ini. (negatif = dikurangi, positif = ditambah).
        try{
            if (dateStart != null){
                dOut = dateStart;
                if (iMinutesAddCount != 0){
                    Calendar gCalendar = new GregorianCalendar();
                    gCalendar.setTime(dateStart);
                    gCalendar.add(GregorianCalendar.MINUTE, iMinutesAddCount);
                    dOut = gCalendar.getTime();
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return dOut;
    }
    
    public static int compareTwoDate(Date dateA, Date dateB){
        int mOut = (0); //.dateA terhadap dateB. (-1=dateA < dateB; 0=dateA==dateB; 1=dateA > dateB).
        try{
            if ((dateA == null) && (dateB == null)){
                mOut = 0;
            }else if ((dateA == null) && (dateB != null)){
                mOut = (-1);
            }else if ((dateA != null) && (dateB == null)){
                mOut = (1);
            }else{
                mOut = dateA.compareTo(dateB);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static int compareTwoDate(Date dateA, Date dateB, boolean bWithDate, boolean bWithTime){
        int mOut = (0); //.dateA terhadap dateB. (-1=dateA < dateB; 0=dateA==dateB; 1=dateA > dateB).
        try{
            if ((dateA == null) && (dateB == null)){
                mOut = 0;
            }else if ((dateA == null) && (dateB != null)){
                mOut = (-1);
            }else if ((dateA != null) && (dateB == null)){
                mOut = (1);
            }else if ((!bWithDate) && (!bWithTime)){
                //.tidak ada yg dicocokkan:
                mOut = 0;
            }else if ((bWithDate) && (!bWithTime)){
                //.cocokkan date saja (hapus time):
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateA);
                Date dtCmpA = new GregorianCalendar(gCalendar.get(GregorianCalendar.YEAR), gCalendar.get(GregorianCalendar.MONTH), gCalendar.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
                gCalendar.setTime(dateB);
                Date dtCmpB = new GregorianCalendar(gCalendar.get(GregorianCalendar.YEAR), gCalendar.get(GregorianCalendar.MONTH), gCalendar.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
                mOut = dtCmpA.compareTo(dtCmpB);
            }else if ((!bWithDate) && (bWithTime)){
                //.cocokkan time saja (hapus date):
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateA);
                long lTimeCmpA = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1); //.ambil time saja.
                gCalendar.setTime(dateB);
                long lTimeCmpB = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1); //.ambil time saja.
                if (lTimeCmpA < lTimeCmpB){
                    mOut = (-1);
                }else if (lTimeCmpA > lTimeCmpB){
                    mOut = (1);
                }else if (lTimeCmpA == lTimeCmpB){
                    mOut = (0);
                }
            }else{
                //.semua(date & time):
                mOut = dateA.compareTo(dateB);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static long diffDateAbsolute(Date dateA, Date dateB){
        long mOut = (-1); //.dateA terhadap dateB. (-1=dateA < dateB; 0=dateA==dateB; 1=dateA > dateB).
        try{
            if ((dateA == null) && (dateB == null)){
                mOut = (-1);
            }else if ((dateA == null) && (dateB != null)){
                mOut = (-1);
            }else if ((dateA != null) && (dateB == null)){
                mOut = (-1);
            }else{
                //.cocokkan date saja (hapus time):
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateA);
                Date dtCmpA = new GregorianCalendar(gCalendar.get(GregorianCalendar.YEAR), gCalendar.get(GregorianCalendar.MONTH), gCalendar.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
                gCalendar.setTime(dateB);
                Date dtCmpB = new GregorianCalendar(gCalendar.get(GregorianCalendar.YEAR), gCalendar.get(GregorianCalendar.MONTH), gCalendar.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
                long lCmpDiff = Math.abs(dtCmpA.getTime() - dtCmpB.getTime());
                mOut = lCmpDiff / (1000 * 60 * 60 * 24);
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return mOut;
    }
    
    public static boolean timeOnlyBetween(Date dateFind, Date dateFrom, Date dateTo, boolean bInclusiveFrom, boolean bInclusiveTo){
        boolean bOut = false; //.mengabaikan date(). hanya time yg dilihat. kalau ada parameter yg null dianggap tidak ditemukan. @inclusive: dianggap date tsb ikut dihitung (bukan batas luar).
        try{
            if ((dateFind != null) && (dateFrom != null) && (dateTo != null)){
                //.netralkan date:
                Calendar gCalendar = new GregorianCalendar();
                gCalendar.setTime(dateFind);
                long lTimeFind = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                gCalendar.setTime(dateFrom);
                long lTimeFrom = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                gCalendar.setTime(dateTo);
                long lTimeTo = (gCalendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600) + (gCalendar.get(GregorianCalendar.MINUTE) * 60) + (gCalendar.get(GregorianCalendar.SECOND) * 1);
                if ((lTimeFind > lTimeFrom) && (lTimeFind < lTimeTo)){
                    bOut = true;
                }else if (bInclusiveFrom){
                    if (lTimeFind == lTimeFrom){
                        bOut = true;
                    }
                }else if (bInclusiveTo){
                    if (lTimeFind == lTimeTo){
                        bOut = true;
                    }
                }
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return bOut;
    }
    
    public static int determineTradingDayNumberByDate(Date dtTarget){
        int iOut = I_SERVERDAY_UNKNOWN;
        try{
            if (dtTarget != null){
                GregorianCalendar ggCal = new GregorianCalendar();
                ggCal.setTime(dtTarget);
                int dyGetDay = ggCal.get(Calendar.DAY_OF_WEEK);
                //.7=Sabtu=6; 1=Minggu=7; 2=Senin=1; dst.
                switch (dyGetDay){
                    case Calendar.MONDAY: //.senin.
                        dyGetDay = I_SERVERDAY_MONDAY;
                        break;
                    case Calendar.TUESDAY: //.selasa.
                        dyGetDay = I_SERVERDAY_TUESDAY;
                        break;
                    case Calendar.WEDNESDAY: //.rabu.
                        dyGetDay = I_SERVERDAY_WEDNESDAY;
                        break;
                    case Calendar.THURSDAY: //.kamis.
                        dyGetDay = I_SERVERDAY_THURSDAY;
                        break;
                    case Calendar.FRIDAY: //.jumat.
                        dyGetDay = I_SERVERDAY_FRIDAY;
                        break;
                    case Calendar.SATURDAY: //.sabtu.
                        dyGetDay = I_SERVERDAY_SATURDAY;
                        break;
                    case Calendar.SUNDAY: //.minggu.
                        dyGetDay = I_SERVERDAY_SUNDAY;
                        break;
                    default:
                        dyGetDay = I_SERVERDAY_UNKNOWN;
                        break;
                }
                iOut = dyGetDay;
            }
        }catch(Exception ex0){
            //.EXXX.
        }
        return iOut;
    }
    
    public static int currentTradingDayNumber()
    {
        try{
            int dyCurDay = determineTradingDayNumberByDate(getDateSVR());
            return dyCurDay;
        }catch(Exception ex){
        }
        return I_SERVERDAY_UNKNOWN; //.nilai 0 akan dicocokkan dengan nomer di setting bahwa nilai ini tidak ada.
    }
    
    public static String getTradingDayNameIDN(Date dtTarget){
        String zOut = Z_SERVERDAY_UNKNOWN;
        try{
            switch(determineTradingDayNumberByDate(dtTarget)){
                case I_SERVERDAY_MONDAY:
                    zOut = Z_SERVERDAY_SENIN;
                    break;
                case I_SERVERDAY_TUESDAY:
                    zOut = Z_SERVERDAY_SELASA;
                    break;
                case I_SERVERDAY_WEDNESDAY:
                    zOut = Z_SERVERDAY_RABU;
                    break;
                case I_SERVERDAY_THURSDAY:
                    zOut = Z_SERVERDAY_KAMIS;
                    break;
                case I_SERVERDAY_FRIDAY:
                    zOut = Z_SERVERDAY_JUMAT;
                    break;
                case I_SERVERDAY_SATURDAY:
                    zOut = Z_SERVERDAY_SABTU;
                    break;
                case I_SERVERDAY_SUNDAY:
                    zOut = Z_SERVERDAY_MINGGU;
                    break;
                default:
                    break;
            }
        }catch(Exception ex){
        }
        return zOut;
    }
    
    public static String getTradingDateNameIDN(Date dtTarget){
        String zOut = Z_SERVERDAY_UNKNOWN;
        try{
            DateFormat cTimeFormat = new SimpleDateFormat(DEF_FORMAT_IDN_TRX_DATE_NORMAL);
            zOut = cTimeFormat.format(dtTarget);
        }catch(Exception ex){
        }
        return zOut;
    }
    
    public static Date applyPartsYearMonthDayToDate(Date dtSourceDate, Date dtDestDate){
        Date mOut = dtDestDate;
        if (mOut != null){
            Calendar gSourceCalendar = new GregorianCalendar();
            gSourceCalendar.setTime(dtSourceDate);
            Calendar gDestCalendar = new GregorianCalendar();
            gDestCalendar.setTime(dtDestDate);
            gDestCalendar.set(GregorianCalendar.YEAR, gSourceCalendar.get(GregorianCalendar.YEAR));
            gDestCalendar.set(GregorianCalendar.MONTH, gSourceCalendar.get(GregorianCalendar.MONTH));
            gDestCalendar.set(GregorianCalendar.DAY_OF_MONTH, gSourceCalendar.get(GregorianCalendar.DAY_OF_MONTH));
            mOut = gDestCalendar.getTime();
        }
        return mOut;
    }
    
    public static String strCurrentFormattedDateFileSafe(){
        DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_DATE_FILESAFE);
        Date cDate = new Date();
        return cDateFormat.format(cDate);
    }
    
    public static String strCurrentFormattedDateTimeFileSafe(){
        DateFormat cDateFormat = new SimpleDateFormat(DEF_FORMAT_DATETIME_FILESAFE);
        Date cDate = new Date();
        return cDateFormat.format(cDate);
    }
    
    public static Date retrieveMessageDate(long tBaseSeconds, long tOffsetNanoSeconds){
        Date mOut = null;
        if (tBaseSeconds >= 0){
            Calendar gDestCalendar = new GregorianCalendar();
            long tRemainingSeconds = (long) (tBaseSeconds + (((double)tOffsetNanoSeconds) / 1000000000D));
            long vHours = 0;
            long vMinutes = 0;
            long vSeconds = 0;
            if (tRemainingSeconds > 0){
                vHours = (tRemainingSeconds / 3600L);
                tRemainingSeconds = (tRemainingSeconds - (vHours * 3600L));
                vMinutes = (tRemainingSeconds / 60L);
                tRemainingSeconds = (tRemainingSeconds - (vMinutes * 60L));
                vSeconds = tRemainingSeconds;
            }
            gDestCalendar.set(gDestCalendar.get(Calendar.YEAR), gDestCalendar.get(Calendar.MONTH), gDestCalendar.get(Calendar.DATE), (int)vHours, (int)vMinutes, (int)vSeconds);
            mOut = gDestCalendar.getTime();
        }
        return mOut;
    }
    
}
