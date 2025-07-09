/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.fileaccess.setup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author aripam
 */
public class ITMFileAccessForAdmin {
    //.untuk manipulasi file:
    
    public ITMFileAccessForAdmin() {
        //.nothing todo here :)
    }
    
    public static ArrayList<File> getFilesListFromCurrentEx(String zTargetRelativeDirectory, boolean bWithFolder, boolean bWithFile){
        ArrayList<File> mOut = new ArrayList<>();
        try{
            if ((zTargetRelativeDirectory != null) && (zTargetRelativeDirectory.length() > 0)){
                //.fix relative path:
                if (!zTargetRelativeDirectory.endsWith(ITMFileAccessVarsConsts.PATH_UNIX_SLASH)){
                    zTargetRelativeDirectory += ITMFileAccessVarsConsts.PATH_UNIX_SLASH;
                }
                //.lanjut:
                File flDirEx = new File(zTargetRelativeDirectory);
                //.cek apakah directory exist:
                if (flDirEx.isDirectory()){
                    File[] arrFiles = flDirEx.listFiles();
                    if ((arrFiles != null) && (arrFiles.length > 0)){
                        for (File flPerFile : arrFiles){
                            if (flPerFile != null){
                                if (bWithFolder){
                                    if (flPerFile.isDirectory()){
                                        mOut.add(flPerFile);
                                    }
                                }else if (bWithFile){
                                    if (flPerFile.isFile()){
                                        mOut.add(flPerFile);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){}
        return mOut;
    }
    
    public static boolean checkDirectoryExistFromCurrentEx(String zTargetRelativeDirectory){
        boolean bOut = false;
        if ((zTargetRelativeDirectory != null) && (zTargetRelativeDirectory.length() > 0)){
            //.fix relative path:
            if (!zTargetRelativeDirectory.endsWith(ITMFileAccessVarsConsts.PATH_UNIX_SLASH)){
                zTargetRelativeDirectory += ITMFileAccessVarsConsts.PATH_UNIX_SLASH;
            }
            //.lanjut:
            File flDirEx = new File(zTargetRelativeDirectory);
            //.cek apakah directory exist:
            try{
                if (flDirEx.isDirectory()){
                    bOut = true;
                }
            }catch(Exception ex0){
            }
        }
        return bOut;
    }
    
    public static String createNewDirectoryFromCurrentEx(String zNewRelativeDirectory){
        //.Ex = fungsi terisolasi. berdiri sendiri. bisa buat multi-sub-sub-sub-dst-directories di dalamnya hanya 1 kali panggil;
        if ((zNewRelativeDirectory != null) && (zNewRelativeDirectory.length() > 0)){
            //.fix relative path:
            if (!zNewRelativeDirectory.endsWith(ITMFileAccessVarsConsts.PATH_UNIX_SLASH)){
                zNewRelativeDirectory += ITMFileAccessVarsConsts.PATH_UNIX_SLASH;
            }
            //.lanjut:
            File flDirEx = new File(zNewRelativeDirectory);
            //.cek apakah directory exist:
            try{
                if (flDirEx.isDirectory()){
                    return zNewRelativeDirectory;
                }
            }catch(Exception ex0){
            }
            //.coba bikin directory baru:
            try{
                flDirEx.mkdirs();
            }catch(Exception ex0){
            }
            //.cek lagi apakah directory exist:
            try{
                if (flDirEx.isDirectory()){
                    return zNewRelativeDirectory;
                }
            }catch(Exception ex0){
            }
        }
        return ITMFileAccessVarsConsts.STRING_EMPTY;
    }
    
    public static String getFileNameFromFilePath(String zTargetFilePath, boolean bReturnInputIfError){
        String zOut = (bReturnInputIfError ? zTargetFilePath : "");
        try{
            if ((zTargetFilePath != null) && (zTargetFilePath.length() > 0)){
                int pLmtWin = zTargetFilePath.lastIndexOf(ITMFileAccessVarsConsts.PATH_WIN_SLASH);
                int pLmtUnx = zTargetFilePath.lastIndexOf(ITMFileAccessVarsConsts.PATH_UNIX_SLASH);
                int pLmtAll = (-1);
                if (pLmtWin >= 0){
                    pLmtAll = pLmtWin;
                }
                if ((pLmtUnx >= 0) && (pLmtUnx > pLmtAll)){
                    pLmtAll = pLmtUnx;
                }
                if (pLmtAll >= 0){
                    //.delimiter sendiri tidak dimasukkan:
                    pLmtAll += 1;
                    if (pLmtAll < zTargetFilePath.length()){
                        zOut = zTargetFilePath.substring(pLmtAll);
                    }
                }
            }
        }catch(Exception ex0){
        }
        return zOut;
    }
    
    public static boolean deleteFile(String zTargetFilePath){
        boolean bOut = false;
        try{
            if ((zTargetFilePath != null) && (zTargetFilePath.length() > 0)){
                File flDel = new File(zTargetFilePath);
                if ((flDel.exists()) && (flDel.isFile())){
                    bOut = flDel.delete();
                }
            }
        }catch(Exception ex0){
        }
        return bOut;
    }
    
    public static boolean renameFile(String zTargetFilePath, String zToFilePath){
        boolean bOut = false;
        try{
            if (
                (zTargetFilePath != null) && (zTargetFilePath.length() > 0)
                && (zToFilePath != null) && (zToFilePath.length() > 0)
                && (!zTargetFilePath.equalsIgnoreCase(zToFilePath))
            ){
                File flFrom = new File(zTargetFilePath);
                File flTo = new File(zToFilePath);
                bOut = flFrom.renameTo(flTo);
            }
        }catch(Exception ex0){
        }
        return bOut;
    }
    
    public static String getResourceDataAsString(Class loaderClass, String zResourceDataPath){
        String zOut = "";
        try{
            InputStream inResStrm = loaderClass.getResourceAsStream(zResourceDataPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] btBuf = new byte[1024];
            int iLength = 0;
            while ((iLength = inResStrm.read(btBuf)) != -1) {
                baos.write(btBuf, 0, iLength);
            }
            zOut = baos.toString(ITMFileAccessVarsConsts.DEFAULT_CODEPAGE);
        }catch(Exception ex0){
        }
        return zOut;
    }
    
}
