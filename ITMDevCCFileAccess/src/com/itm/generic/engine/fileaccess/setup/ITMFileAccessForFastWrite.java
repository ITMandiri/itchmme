/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.fileaccess.setup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author aripam
 */
public class ITMFileAccessForFastWrite {
    //.untuk tulis file:
    private File fileInput;
    private boolean bIsFileOpened = false;
    private BufferedWriter bufWriter;
    
    public ITMFileAccessForFastWrite() {
        //.nothing todo here :)
    }
    
    public String getOpenedFilePath(){
        if (fileInput != null){
            if (bufWriter != null){
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
    
    
    public boolean openCreateFileForWrite(String zInputFilePath, boolean bForAppend){
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
            } catch (IOException ex0) {
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
            OutputStreamWriter flFirstWriter = new OutputStreamWriter(new FileOutputStream(fileInput, bForAppend), ITMFileAccessVarsConsts.DEFAULT_CODEPAGE); //.new{per:20140917}.
            //bufWriter = new BufferedWriter(new FileWriter(fileInput, true)); //.old{changed:20140917}.
            bufWriter = new BufferedWriter(flFirstWriter); //.new{per:20140917}.
            //.berhasil buka file:
            bIsFileOpened = true;
            return true;
        } catch (FileNotFoundException ex0) {
            //.file tidak dapat dibuka:
            return false;
        } catch (Exception ex0) {
            //.file tidak dapat dibuka:
            return false;
        }
    }
    
    public boolean appendLine(String zLineToAppend){
        if (bufWriter != null){
            if (bIsFileOpened){
                try {
                    //bufWriter.write(zLineToAppend + ITMFileAccessVarsConsts.STRING_CRLF); //.old{changed:20140917}.
                    boolean bAlreadyNewLine = false;
                    if (zLineToAppend.endsWith(ITMFileAccessVarsConsts.STRING_CRLF)){
                        bAlreadyNewLine = true;
                    }
                    if (bAlreadyNewLine){
                        bufWriter.write(zLineToAppend); //.check{per:20141008}.
                    }else{
                        bufWriter.write(zLineToAppend + ITMFileAccessVarsConsts.STRING_CRLF); //.new{per:20140917}.
                    }
                    bufWriter.flush();
                    return true;
                } catch (IOException ex0) {
                    return false;
                } catch (Exception ex0) {
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean closeFile(){
        if (!bIsFileOpened){
            if (fileInput != null){
                fileInput = null;
            }
            if (bufWriter != null){
                bufWriter = null;
            }
            return true;
        }else{
            if (bufWriter == null){
                bIsFileOpened = false;
                return true;
            }
            if (fileInput == null){
                bIsFileOpened = false;
                return true;
            }
            try {
                bufWriter.close();
                bufWriter = null;
                fileInput = null;
                //.file sudah berhasil ditutup:
                bIsFileOpened = false;
                return true;
            } catch (IOException ex0) {
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
    
}
