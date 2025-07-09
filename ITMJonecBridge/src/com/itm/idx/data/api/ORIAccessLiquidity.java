/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.idx.data.api;

/**
 *
 * @author aripam
 */
public class ORIAccessLiquidity {
    private String fUserID                                      = "";
    private String fPassword                                    = "";
    private String fConnectionName                              = "";
    
    public ORIAccessLiquidity(String zUserID, String zPassword, String zConnectionName) {
        this.fUserID = zUserID;
        this.fPassword = zPassword;
        this.fConnectionName = zConnectionName;
    }
    
}
