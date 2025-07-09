/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.fileaccess.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author aripam
 */
public class ITMFileAccessForReadWrite {
    //.untuk baca tulis file:
    private File fileInput;
    private RandomAccessFile raFileAccess;
    private boolean bIsFileOpened = false;
    
    public ITMFileAccessForReadWrite() {
        //.nothing todo here :)
    }
    
    public String getOpenedFilePath(){
        if (fileInput != null){
            if (raFileAccess != null){
                if (bIsFileOpened){
                    return fileInput.getName();
                }
            }
        }
        return ITMFileAccessVarsConsts.STRING_EMPTY;
    }
    
    public boolean isFileAlreadyOpened(){
        return bIsFileOpened;
    }
    
    public boolean openCreateFileForReadWrite(String zInputFilePath){
        //.cek apakah masih ada file yg dibuka:
        if (bIsFileOpened){
            return false;
        }
        //.cek apakah alamat file kosong:
        if (zInputFilePath.length() <= 0){
            return false;
        }
        //.test file:
        fileInput = new File(zInputFilePath);
        //.cek apakah file sudah ada:
        if (!fileInput.exists()){
            try {
                //.file belum ada, coba buat baru:
                fileInput.createNewFile();
            } catch (IOException ioex0) {
                return false;
            } catch (Exception ex0) {
                return false;
            }
            //.apakah file berhasil dibuat:
            if (!fileInput.exists()){
                //.tidak berhasil dibuat:
                return false;
            }
        }
        //.cek apakah benar file:
        if (!fileInput.isFile()){
            //.file tidak dapat diakses / bukan file:
            return false;
        }
        //.cek apakah file bisa dibaca:
        if (!fileInput.canRead()){
            try{
                //.coba buka kunci file:
                if (!fileInput.setReadable(true)){
                    //.file tidak dapat dibaca:
                    return false;
                }
            }catch(Exception ex0){
                //.file tidak dapat dibaca:
                return false;
            }
        }
        //.cek apakah file bisa ditulis:
        if (!fileInput.canWrite()){
            try{
                //.coba buka kunci file:
                if (!fileInput.setWritable(true)){
                    //.file tidak dapat ditulis:
                    return false;
                }
            }catch(Exception ex0){
                //.file tidak dapat ditulis:
                return false;
            }
        }
        try {
            //.coba buka file:
            raFileAccess = new RandomAccessFile(fileInput, "rwd");
            //.berhasil buka file:
            bIsFileOpened = true;
            return true;
        } catch (FileNotFoundException flex0) {
            //.file tidak dapat dibuka:
            return false;
        } catch (Exception ex0) {
            //.file tidak dapat dibuka:
            return false;
        }
    }
    
    public boolean closeFile(){
        if (!bIsFileOpened){
            if (fileInput != null){
                fileInput = null;
            }
            if (raFileAccess != null){
                raFileAccess = null;
            }
            return true;
        }else{
            if (raFileAccess == null){
                bIsFileOpened = false;
                return true;
            }
            if (fileInput == null){
                bIsFileOpened = false;
                return true;
            }
            try {
                raFileAccess.close();
                raFileAccess = null;
                fileInput = null;
                //.file sudah berhasil ditutup:
                bIsFileOpened = false;
                return true;
            } catch (IOException ioex0) {
                //.error: anggap sudah ditutup:
                bIsFileOpened = false;
                return true;
            } catch (Exception ex0) {
                //.error: anggap sudah ditutup:
                bIsFileOpened = false;
                return true;
            }
        }
    }
    
    public String readLine(){
        if (raFileAccess != null){
            if (bIsFileOpened){
                try {
                    return raFileAccess.readLine();
                } catch (IOException ioex0) {
                    return null;
                } catch (Exception ex0) {
                    return null;
                }
            }
        }
        return null;
    }
    
    public boolean writeLine(String zLineToWrite){
        if (raFileAccess != null){
            if (bIsFileOpened){
                try {
                    raFileAccess.writeBytes(zLineToWrite + ITMFileAccessVarsConsts.STRING_CRLF);
                    return true;
                } catch (IOException ioex0) {
                    return false;
                } catch (Exception ex0) {
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean appendLine(String zLineToAppend){
        if (raFileAccess != null){
            if (bIsFileOpened){
                try {
                    long pFileLen = raFileAccess.length();
                    if (raFileAccess.getFilePointer() != pFileLen){
                        raFileAccess.seek(pFileLen);
                    }
                    raFileAccess.writeBytes(zLineToAppend + ITMFileAccessVarsConsts.STRING_CRLF);
                    return true;
                } catch (IOException ioex0) {
                    return false;
                } catch (Exception ex0) {
                    return false;
                }
            }
        }
        return false;
    }
    
    public long resetPointer(){
        long pPrevPtr = -1;
        if (raFileAccess != null){
            if (bIsFileOpened){
                try {
                    pPrevPtr = raFileAccess.getFilePointer();
                } catch (IOException ioex0) {
                    return pPrevPtr;
                } catch (Exception ex0) {
                    return pPrevPtr;
                }
                try {
                    raFileAccess.seek(0);
                } catch (IOException ioex0) {
                    return pPrevPtr;
                } catch (Exception ex0) {
                    return pPrevPtr;
                }
                return pPrevPtr;
            }
        }
        return pPrevPtr;
    }
    
    public long deleteFileContents(){
        long ptrFileLen = (-1);
        if (fileInput != null){
            if (raFileAccess != null){
                if (bIsFileOpened){
                    try {
                        raFileAccess.seek(0);
                        raFileAccess.setLength(0);
                        ptrFileLen = raFileAccess.length();
                        return ptrFileLen;
                    } catch (IOException ioex0) {
                        return ptrFileLen;
                    } catch (Exception ex0) {
                        return ptrFileLen;
                    }
                }
            }
        }
        return ptrFileLen;
    }
    
}
