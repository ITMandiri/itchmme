/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.dbscheme;

/**
 *
 * @author fredy
 */
public class DbSchemeVarsConsts {
    
    public DbSchemeVarsConsts() {
        //.nothing todo here :)
    }
    
    public static enum DbColType {
        STRING (1,true),
        NUMERIC (2,false);
        
        private final int order;
        private final boolean quote;
        
        private DbColType(int order, boolean quote) {
            this.order = order;
            this.quote = quote;
        }
        
        public int getOrder() {
            return order;
        }
        
        public boolean getQuote() {
            return quote;
        }
        
    }
    
}
