/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itm.idx.data.qri.util;

import com.itm.idx.data.jonec.consts.JonecConst;

/**
 *
 * @author Hirin
 */
public class JonecUtil {

    public JonecUtil(){
    }
    
    public static String clearJonecHeader(String oriMsg){
        String[] arrTmp = oriMsg.split(Character.toString(JonecConst.JONEC_HEADER_MSG_SEPARATOR));
        if (arrTmp.length == 2){
            return arrTmp[1];
        }
        return oriMsg;
    }
}
