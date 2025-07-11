/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.ts.ouch.bridge;

import com.itm.ts.ouch.books.SheetOfOUCHBase;
import com.itm.ts.ouch.structs.OUCHMsgBase;

/**
 *
 * @author fredy
 */
public interface ITMOUCHMsgMemoryListener {
    public abstract void onMessage (OUCHMsgBase itchMessage, SheetOfOUCHBase mSheetBase);
}
