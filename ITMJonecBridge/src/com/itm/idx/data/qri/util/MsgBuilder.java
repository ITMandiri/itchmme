/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.util;

/**
 *
 * @author Hirin
 */
public class MsgBuilder {
    private String innerTag;
    private String interTag;
    private String message;
    public MsgBuilder(String innerTag, String interTag){
        //. init
        this.message = "";
        this.innerTag = innerTag;
        this.interTag = interTag;
    }

    public String getInnerTag() {
        return innerTag;
    }

    public void setInnerTag(String innerTag) {
        this.innerTag = innerTag;
    }

    public String getInterTag() {
        return interTag;
    }

    public void setInterTag(String interTag) {
        this.interTag = interTag;
    }

    public String getMessage() {
        return message;
    }
    
    public void addData(String fieldName, String value){
        if (value == null)return;
        
        message +=  fieldName + innerTag + value + interTag;
    }
    
    private void trimLastSeparator(){
        int len = message.length();
        if (len == 0) return;
        if (message.charAt(len-1) == interTag.charAt(0)){
            message.substring(0, len-1);
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
