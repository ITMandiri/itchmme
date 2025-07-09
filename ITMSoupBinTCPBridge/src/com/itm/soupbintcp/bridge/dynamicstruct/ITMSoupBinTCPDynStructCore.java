/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.soupbintcp.bridge.dynamicstruct;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPDynStructCore {
    
    public String version;
    public String specification;
    public CCS_message[] messages;
    public CCS_source[] sources;
    
    public class CCS_message {
        
        public String type;
        public String name;
        public String description;
        public String direction;
        public boolean sequenced;
        public CCS_message_field[] fields;
        
        public CCS_message_field[] cloneFields(){
            CCS_message_field[] mOut = null;
            if (fields != null){
                mOut = new CCS_message_field[fields.length];
                if (fields.length > 0){
                    for(int p = 0; p < fields.length; p++){
                        CCS_message_field mSrcField = fields[p];
                        CCS_message_field mDstField = new CCS_message_field();
                        mDstField.name = mSrcField.name;
                        mDstField.offset = mSrcField.offset;
                        mDstField.len = mSrcField.len;
                        mDstField.value = mSrcField.value;
                        mDstField.type = mSrcField.type;
                        mDstField.notes = mSrcField.notes;
                        mDstField.source = mSrcField.source;
                        mDstField.msgstrval = mSrcField.msgstrval;
                        mDstField.msglongval = mSrcField.msglongval;
                        mOut[p] = mDstField;
                    }
                }
            }
            return mOut;
        }
        
        public class CCS_message_field {
            
            public String name;
            public int offset;
            public int len;
            public String value;
            public String type;
            public String notes;
            public String source;
            //.msgvalue:
            public String msgstrval;
            public long msglongval;
            //. data double
            public double msgdoubleval;
            //. data date
            public Date msgdateval;
        }
        
    }
    
    public class CCS_source {
        
        public String code;
        public String type;
        public String description;
        public CCS_source_item[] items;
        
        public class CCS_source_item {
            
            public String key;
            public String value;
            
        }
        
    }
}
