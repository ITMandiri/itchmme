/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.fileaccess.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author aripam
 */
public class ITMFileAccessForFastRead {
    
    //.untuk baca file:
    private File fileInput;
    private BufferedReader bufReader;
    private boolean bIsFileOpened = false;
    
    public ITMFileAccessForFastRead() {
        //.nothing todo here :)
    }
    
    public String getOpenedFilePath(){
        try{
            if (fileInput != null){
                if (bIsFileOpened){
                    return fileInput.getName();
                }
            }
        }catch(Exception ex0){
            return ITMFileAccessVarsConsts.STRING_EMPTY;
        }
        return ITMFileAccessVarsConsts.STRING_EMPTY;
    }
    
    public boolean openFileForReadLinesFromFirst(String zFileInputPath){
        //.cek apakah masih ada file yg dibuka:
        if (bIsFileOpened){
            return false;
        }
        //.cek apakah alamat file kosong:
        if (zFileInputPath.length() <= 0){
            return false;
        }
        //.test file:
        fileInput = new File(zFileInputPath);
        //.cek apakah file sudah ada:
        if (!fileInput.exists()){
            //.file tidak ada:
            return false;
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
        //.---[skip, hanya baca]
        //.coba buka file:
        try {
            InputStreamReader flFirstReader = new InputStreamReader(new FileInputStream(fileInput), ITMFileAccessVarsConsts.DEFAULT_CODEPAGE); //.new{per:20140916}.
            //.FileReader flFirstReader = new FileReader(fileInput); //.old{changed:20140916}.
            bufReader = new BufferedReader(flFirstReader);
            //.berhasil buka file:
            bIsFileOpened = true;
            return true;
        }catch(FileNotFoundException ex0) {
            //.file tidak dapat dibaca:
            return false;
        }catch(Exception ex0){
            //.file tidak dapat dibaca:
            return false;
        }
    }
    
    public boolean closeFile(){
        if (!bIsFileOpened){
            if (fileInput != null){
                fileInput = null;
            }
            if (bufReader != null){
                bufReader = null;
            }
            return true;
        }else{
            if (bufReader == null){
                bIsFileOpened = false;
                return true;
            }
            if (fileInput == null){
                bIsFileOpened = false;
                return true;
            }
            try {
                bufReader.close();
                bufReader = null;
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
        if (bufReader != null){
            if (bIsFileOpened){
                try {
                    return bufReader.readLine();
                } catch (IOException ioex0) {
                    return null;
                } catch (Exception ex0) {
                    return null;
                }
            }
        }
        return null;
    }
    
    public long deleteFileContents(){
        long ptrFileLen = (-1);
        if (fileInput != null){
            if (bIsFileOpened){
                try {
                    PrintWriter prWriter = new PrintWriter(fileInput);
                    prWriter.print(ITMFileAccessVarsConsts.STRING_EMPTY);
                    prWriter.close();
                    ptrFileLen = 0; //.sebagai tanda saja berhasil melewati baris ini.
                } catch (IOException ioex0) {
                    return ptrFileLen;
                } catch (Exception ex0) {
                    return ptrFileLen;
                }
            }
        }
        return ptrFileLen;
    }
    
}
