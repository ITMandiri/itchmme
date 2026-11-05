/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.extramsg.reflection;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author Ari Pambudi
 */
public class ITMExtraMsgReflection {
    //.single instance ya:
    public final static ITMExtraMsgReflection getInstance = new ITMExtraMsgReflection();
    
    //.constructor:
    public ITMExtraMsgReflection(){
        //.nothing todo here :)
        ITMFileLoggerManager.getInstance.insertLog(this, ITMFileLoggerVarsConsts.logSource.SERVER, ITMFileLoggerVarsConsts.logLevel.INIT, "");
    }
    
    public Object getObjectFieldAsObj(Object mObject, String zFieldName, Object defValue){
        Object mOut = defValue;
        try{
            Field mField = mObject.getClass().getField(zFieldName);
            if (mField != null){
                if (!mField.isAccessible()){
                    mField.setAccessible(true);
                }
                mOut = mField.get(mObject);
            }
        }catch(Exception ex0){
            //System.err.println(ex0);
        }
        return mOut;
    }
    
    public byte getObjectField(Object mObject, String zFieldName, byte defValue){
        return (byte)getObjectFieldAsObj(mObject, zFieldName, (byte)defValue);
    }
    
    public short getObjectField(Object mObject, String zFieldName, short defValue){
        return (short)getObjectFieldAsObj(mObject, zFieldName, (short)defValue);
    }
    
    public int getObjectField(Object mObject, String zFieldName, int defValue){
        return (int)getObjectFieldAsObj(mObject, zFieldName, (int)defValue);
    }
    
    public long getObjectField(Object mObject, String zFieldName, long defValue){
        return (long)getObjectFieldAsObj(mObject, zFieldName, (long)defValue);
    }
    
    public double getObjectField(Object mObject, String zFieldName, double defValue){
        return (double)getObjectFieldAsObj(mObject, zFieldName, (double)defValue);
    }
    
    public float getObjectField(Object mObject, String zFieldName, float defValue){
        return (float)getObjectFieldAsObj(mObject, zFieldName, (float)defValue);
    }
    
    public String getObjectField(Object mObject, String zFieldName, String defValue){
        return (String)getObjectFieldAsObj(mObject, zFieldName, (String)defValue);
    }
    
    //=============================:;
    
    public Object getObjectMethod0ParamAsObj(Object mObject, String zMethodName, Object defValue){
        Object mOut = defValue;
        try{
            Method mMethod = mObject.getClass().getMethod(zMethodName);
            if (mMethod != null){
                if (!mMethod.isAccessible()){
                    mMethod.setAccessible(true);
                }
                mOut = mMethod.invoke(mObject);
            }
        }catch(Exception ex0){
            //System.err.println(ex0);
        }
        return mOut;
    }
    
    public byte getObjectMethod0Param(Object mObject, String zMethodName, byte defValue){
        return (byte)getObjectMethod0ParamAsObj(mObject, zMethodName, (byte)defValue);
    }
    
    public short getObjectMethod0Param(Object mObject, String zMethodName, short defValue){
        return (short)getObjectMethod0ParamAsObj(mObject, zMethodName, (short)defValue);
    }
    
    public int getObjectMethod0Param(Object mObject, String zMethodName, int defValue){
        return (int)getObjectMethod0ParamAsObj(mObject, zMethodName, (int)defValue);
    }
    
    public long getObjectMethod0Param(Object mObject, String zMethodName, long defValue){
        return (long)getObjectMethod0ParamAsObj(mObject, zMethodName, (long)defValue);
    }
    
    public double getObjectMethod0Param(Object mObject, String zMethodName, double defValue){
        return (double)getObjectMethod0ParamAsObj(mObject, zMethodName, (double)defValue);
    }
    
    public float getObjectMethod0Param(Object mObject, String zMethodName, float defValue){
        return (float)getObjectMethod0ParamAsObj(mObject, zMethodName, (float)defValue);
    }
    
    public String getObjectMethod0Param(Object mObject, String zMethodName, String defValue){
        return (String)getObjectMethod0ParamAsObj(mObject, zMethodName, (String)defValue);
    }
    
    
}
