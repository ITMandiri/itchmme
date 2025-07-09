/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.viewer;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPHexViewer extends javax.swing.JPanel {
    
    //.timer resize event:
    private Timer timerResized;
    
    //timer check source:
    private Timer timerCheckSource;
    
    private static final int CHARACTERS_COLUMN_LENGTH = 16;
    
    private static final int MAX_ELAPSED_BEFORE_RECHECK_SOURCE = 5;
    
    private int elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
    
    //.callback:
    public interface HexViewerCallBack {
        public void onRequestData(long offset, long length);
    }
    
    //.register:
    private HexViewerCallBack callBackObject;
    
    private ITMDialogHexViewerFind dialogFind;
    
    //.flags:
    private boolean isViewerOpened = false;
    
    private boolean isFindNeedCancel = false;
    
    //.posisi arrayrespond[0]:
    private long requestedOffset = 0; //.yg diminta;
    private long respondedOffset = 0; //.yg diberi;
    
    //.ukuran arrayrespond[n]:
    private long requestedLength = 0; //.yg diminta;
    private long respondedLength = 0; //.yg diberi;
    
    //.posisi yang ditampilkan:
    private long viewedStart = 0; //.berdasar index arrayRespond;
    private long viewedLength = 0;
    
    //.penampung data:
    private static final int ARRAY_RESPOND_MAX_SIZE = (CHARACTERS_COLUMN_LENGTH * 1000 * 1000 * 1); //.n columns & 1jt rows;
    private final byte[] arrayRespond = new byte[ARRAY_RESPOND_MAX_SIZE];
    
    //.ukuran sumber:
    private long sourceSize = ARRAY_RESPOND_MAX_SIZE;
    
    public long getRespondedOffset() {
        return this.respondedOffset;
    }

    public long getRespondedLength() {
        return this.respondedLength;
    }

    public long getViewedStart() {
        return this.viewedStart;
    }

    public long getViewedLength() {
        return this.viewedLength;
    }

    public byte[] getArrayRespond() {
        return this.arrayRespond;
    }

    public long getSourceSize() {
        return this.sourceSize;
    }
    
    public boolean isViewerOpened(){
        return this.isViewerOpened;
    }
    
    public void openViewer(HexViewerCallBack callBackObject){
        try{
            closeViewer();
            if (callBackObject != null){
                this.callBackObject = callBackObject;
            }else{
                this.callBackObject = new HexViewerCallBack() {
                    @Override
                    public void onRequestData(long offset, long length) {}
                };
            }
            this.isViewerOpened = true;
            this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
            this.requestedOffset = 0;
            this.requestedLength = ARRAY_RESPOND_MAX_SIZE;
            this.sourceSize = ARRAY_RESPOND_MAX_SIZE;
            requestData();
            if (this.timerCheckSource == null){
                this.timerCheckSource = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            elapsedBeforeReCheckSource--;
                            if (elapsedBeforeReCheckSource <= 0){
                                elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
                                requestData();
                            }
                            if (isViewerOpened){
                                timerCheckSource.restart();
                            }
                        }catch(Exception ex0){
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }
                });
            }
            this.timerCheckSource.stop();
            this.timerCheckSource.start();
            //... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void closeViewer(){
        try{
            if (this.isViewerOpened){
                this.isViewerOpened = false;
            }
            if (this.timerCheckSource != null){
                this.timerCheckSource.stop();
            }
            this.sourceSize = ARRAY_RESPOND_MAX_SIZE;
            this.requestedOffset = 0;
            this.respondedOffset = 0;
            this.requestedLength = 0;
            this.respondedLength = 0;
            this.viewedStart = 0;
            this.viewedLength = 0;
            this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
            redrawViewer();
            //.close dialogs:
            if (this.dialogFind != null) { 
                this.dialogFind.endFind();
                this.dialogFind.dispose(); 
                this.dialogFind = null; 
            }
            //... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void requestData(){
        try{
            if (this.isViewerOpened){
                this.callBackObject.onRequestData(this.requestedOffset, this.requestedLength);
            }
            //... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void respondData(long sourceSize, long offset, long length, byte[] arrayRespond){
        try{
            if (this.isViewerOpened){
                if (sourceSize <= 0){
                    sourceSize = 0;
                }
                if (offset < 0){
                    offset = 0;
                }
                if (offset < sourceSize){
                    this.sourceSize = sourceSize;
                    this.respondedOffset = offset;
                    if (length > this.arrayRespond.length){
                        length = this.arrayRespond.length;
                    }
                    if (length > arrayRespond.length){
                        length = arrayRespond.length;
                    }
                    if ((offset + length) > sourceSize){
                        length = (int) (sourceSize - offset);
                    }
                    this.respondedLength = length;
                    System.arraycopy(arrayRespond, 0, this.arrayRespond, 0, (int) length);
                    redrawViewer();
                }
            }
            //... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void redrawViewer(){
        try{
            long vColsCount = jTableVw.getColumnCount();
            long vRowsCount = jTableVw.getRowCount();
            if (this.isViewerOpened){
                //.view:
                long vCurScroll = jScrollBarVw.getValue();
                long vCurOffset = (vCurScroll * CHARACTERS_COLUMN_LENGTH);
                long vViewOffset = (int) (this.respondedOffset % CHARACTERS_COLUMN_LENGTH);
                long vMaxViewedLength = (vRowsCount * CHARACTERS_COLUMN_LENGTH);
                this.viewedStart = 0;
                if ((vCurOffset >= this.respondedOffset) && (vCurOffset < (this.respondedOffset + ARRAY_RESPOND_MAX_SIZE))){
                    this.viewedStart = (int) (vCurOffset - this.respondedOffset);
                }else{
                    this.viewedStart = vViewOffset;
                }
                this.viewedLength = vMaxViewedLength;
                if (this.viewedLength > 0){
                    if ((this.viewedStart + this.viewedLength) > ARRAY_RESPOND_MAX_SIZE){
                        this.viewedLength = (ARRAY_RESPOND_MAX_SIZE - this.viewedStart);
                    }
                    if ((this.respondedOffset + this.viewedStart + this.viewedLength) > this.sourceSize){
                        this.viewedLength = (int) (this.sourceSize - this.respondedOffset - this.viewedStart);
                    }
                    if ((this.respondedOffset + this.viewedStart + this.viewedLength) > (this.respondedOffset + this.respondedLength)){
                        this.viewedLength = (int) ((this.respondedOffset + this.respondedLength) - this.respondedOffset - this.viewedStart);
                    }
                }
                String[] arrCells = new String[(int)vColsCount];
                if (vRowsCount > 0){
                    for(int pRow = 0; pRow < vRowsCount; pRow++){
                        for(int pCol = 0; pCol < vColsCount; pCol++){
                            jTableVw.setValueAt("", pRow, pCol);
                        }
                    }
                }
                long cViewedCount = 0;
                if (this.viewedLength > 0){
                    for(int pRow = 0; pRow < vRowsCount; pRow++){
                        for(int pCol = 0; pCol < vColsCount; pCol++){
                            if ((pCol >= 1) && (pCol <= 16)){
                                cViewedCount++;
                            }
                            if (cViewedCount <= this.viewedLength){
                                if (pCol == 0){
                                    arrCells[pCol] = String.format("%012X", ((this.respondedOffset + this.viewedStart) + (pRow * CHARACTERS_COLUMN_LENGTH)));
                                    jTableVw.setValueAt(arrCells[pCol], pRow, pCol);
                                }else if ((pCol >= 1) && (pCol <= 16)){
                                    arrCells[pCol] = String.format("%02X", this.arrayRespond[(int)this.viewedStart + (pRow * CHARACTERS_COLUMN_LENGTH) + (pCol - 1)]);
                                    arrCells[pCol + 16] = ("" + (char)this.arrayRespond[(int)this.viewedStart + (pRow * CHARACTERS_COLUMN_LENGTH) + (pCol - 1)]);
                                    jTableVw.setValueAt(arrCells[pCol], pRow, pCol);
                                    jTableVw.setValueAt(arrCells[pCol + 16], pRow, (pCol + 16));
                                }else if (pCol >= 33) {
                                    arrCells[pCol] = "";
                                    jTableVw.setValueAt(arrCells[pCol], pRow, pCol);
                                }
                            }
                        }
                    }
                }
                //.status bar:
                jLabelVwSourceSizeValue.setText(String.format("%,d", this.sourceSize) + " bytes");
                try{
                    if (this.sourceSize < 1024){
                        jLabelVwSourceSizeValue.setToolTipText(String.format("%,d", this.sourceSize) + " B");
                    }else{
                        jLabelVwSourceSizeValue.setToolTipText(String.format("%,d", (long)Math.round(((double)this.sourceSize) / 1024)) + " KB");
                    }
                }catch(Exception ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }
                jLabelVwTopOffsetValue.setText(String.format("%,d", (this.respondedOffset + this.viewedStart)));
                //.fix untuk selanjutnya:
                long vMaxScroll = (long) ((this.sourceSize > 0) ? Math.floor(((double)(this.sourceSize - 1)) / ((double)CHARACTERS_COLUMN_LENGTH)) : 0);
                if (vMaxScroll != jScrollBarVw.getMaximum()){
                    jScrollBarVw.setMaximum((int) vMaxScroll);
                }
                if (jScrollBarVw.getValue() > vMaxScroll){
                    jScrollBarVw.setValue((int) vMaxScroll);
                }
            }else{
                //.clear:
                if (vRowsCount > 0){
                    for(int pRow = 0; pRow < vRowsCount; pRow++){
                        for(int pCol = 0; pCol < vColsCount; pCol++){
                            jTableVw.setValueAt("", pRow, pCol);
                        }
                    }
                }
                //.status bar:
                jLabelVwSourceSizeValue.setText("--");
                jLabelVwSourceSizeValue.setToolTipText("--");
                jLabelVwTopOffsetValue.setText("--");
                //.fix untuk selanjutnya:
                jScrollBarVw.setValue(0);
                jScrollBarVw.setMinimum(0);
                jScrollBarVw.setMaximum(0);
            }
            //... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    /**
     * Creates new form ITMSoupBinTCPHexEditor
     */
    public ITMSoupBinTCPHexViewer() {
        //.inisialisasi internal form:
        initComponents();
        //.inisialisasi eksternal form:
        initComponentsEx();
    }

    private void initComponentsEx() {
        try{
            //.rapikan:
            DefaultTableCellRenderer tblHeaderRenderer = ((DefaultTableCellRenderer)jTableVw.getTableHeader().getDefaultRenderer());
            tblHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
            ITMComponentLayoutHelper.setTableCellsAlignment(jTableVw, SwingUtilities.CENTER);
            //.shortcuts:
            jTableVw.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK), "CTRL+F");
            jTableVw.getActionMap().put("CTRL+F", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onShowDialogFind();
                }
            });
            //.visibility:
            jLabelVwFindCancel.setVisible(false);
            //... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void onShowDialogFind(){
        try{
            if (this.isViewerOpened){
                if (this.dialogFind == null){
                    this.dialogFind = new ITMDialogHexViewerFind();
                }
                this.dialogFind.openDialog(this, this);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void stopFind(){
        try{
            this.isFindNeedCancel = true;
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public String startFind(String zText, boolean isHexaDecimal, boolean isSearchFromBeginning, boolean isLimited, long vLimitBytes, boolean isSearchPrevious, boolean isInsideMultiThreading){
        String mOut = "No Response";
        boolean isFound = false;
        long vNextFindSize = 0;
        long vNextViewedOffset = 0;
        long vNextRespondOffset = 0;
        long vNextRespondLength = 0;
        try{
            this.isFindNeedCancel = false;
            jLabelVwFindCancel.setVisible(true);
            jScrollPaneVw.setEnabled(false);
            jTableVw.setEnabled(false);
            jScrollBarVw.setEnabled(false);
            vNextViewedOffset = this.viewedStart;
            vNextRespondOffset = this.respondedOffset;
            vNextRespondLength = this.respondedLength;
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        try{
            String zErrorMsg = "";
            boolean bCanContinue = true;
            boolean bBreak = false;
            boolean bFound = false;
            byte[] arrToFind = new byte[0];
            int cToFindSize = 0;
            if ((this.isViewerOpened) && (zText != null) && (!zText.isEmpty())){
                if (isHexaDecimal){
                    if (!zText.matches("^[0-9a-fA-F]+$")){
                        zErrorMsg = "Invalid Hexa Decimal Character(s).";
                        bCanContinue = false;
                    }else{
                        if ((zText.length() % 2) != 0){
                            zText = ("0" + zText);
                        }
                        cToFindSize = (zText.length() / 2);
                        arrToFind = new byte[cToFindSize];
                        int ff = 0;
                        for(int pp = 0; pp < cToFindSize; pp++){
                            ff = (pp * 2);
                            arrToFind[pp] = Integer.decode("0x" + zText.substring(ff, (ff + 2))).byteValue();
                        }
                    }
                }else{
                    arrToFind = zText.getBytes();
                    cToFindSize = arrToFind.length;
                }
                vNextFindSize = cToFindSize;
                if (bCanContinue){
                    long cMaximumTravelLength = ((isLimited) ? vLimitBytes : this.sourceSize);
                    long cCurrentTravelLength = 0;
                    long cMaximumRepeatedLoop = 2;
                    long cCurrentRepeatedLoop = 0;
                    long pBeginOffset = 0;
                    long pLoopTargetOffset = 0;
                    long pLoopRequestOffset = 0;
                    long pLoopRequestLength = 0;
                    long pLoopBufferOffset = 0;
                    long pp = 0;
                    long pf = 0;
                    long mc = 0;
                    if (isSearchFromBeginning){
                        pBeginOffset = 0;
                        pLoopTargetOffset = pBeginOffset;
                    }else{
                        pBeginOffset = (this.respondedOffset + this.viewedStart);
                        long pSelCol = jTableVw.getSelectedColumn();
                        long pSelRow = jTableVw.getSelectedRow();
                        long pSelOffset = -1;
                        if ((pSelCol != -1) && (pSelRow != -1)){
                            if (pSelCol == 0){
                                pSelOffset = (pSelRow * CHARACTERS_COLUMN_LENGTH);
                            }else if ((pSelCol >= 1) && (pSelCol <= 16)){
                                pSelOffset = ((pSelRow * CHARACTERS_COLUMN_LENGTH) + (pSelCol - 1));
                            }else if ((pSelCol >= 17) && (pSelCol <= 32)){
                                pSelOffset = ((pSelRow * CHARACTERS_COLUMN_LENGTH) + (pSelCol - 17));
                            }else{
                                pSelOffset = ((pSelRow * CHARACTERS_COLUMN_LENGTH) + (CHARACTERS_COLUMN_LENGTH - 1));
                            }
                            if (pSelOffset >= 0){
                                pBeginOffset += pSelOffset;
                            }
                        }
                        pLoopTargetOffset = pBeginOffset;
                    }
                    
                    while ((!bFound) && (!bBreak)){
                        if (cCurrentTravelLength < cMaximumTravelLength){
                            if (isSearchPrevious){
                                //.mundur:
                                if (pLoopTargetOffset > 0){
                                    pLoopTargetOffset--;
                                }
                                if (pLoopTargetOffset <= (cToFindSize - 1)){
                                    //.sudah berada paling atas, ambil dari bawah saja:
                                    pLoopRequestLength = ARRAY_RESPOND_MAX_SIZE;
                                    pLoopRequestOffset = (this.sourceSize - pLoopRequestLength);
                                    if (pLoopRequestOffset < 0){
                                        pLoopRequestOffset = 0;
                                        pLoopRequestLength = (this.sourceSize - pLoopRequestOffset);
                                    }
                                }else{
                                    pLoopRequestLength = ARRAY_RESPOND_MAX_SIZE;
                                    pLoopRequestOffset = (pLoopTargetOffset - pLoopRequestLength + cToFindSize);
                                    if (pLoopRequestOffset < 0){
                                        pLoopRequestOffset = 0;
                                    }
                                    if ((pLoopRequestOffset + pLoopRequestLength) > this.sourceSize){
                                        pLoopRequestLength = (this.sourceSize - pLoopRequestOffset);
                                    }
                                }
                                if (pLoopRequestOffset == this.requestedOffset){
                                    cCurrentRepeatedLoop++;
                                }
                                if (cCurrentRepeatedLoop > cMaximumRepeatedLoop){
                                    bBreak = true;
                                }
                                this.requestedOffset = pLoopRequestOffset;
                                this.requestedLength = pLoopRequestLength;
                                this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
                                requestData();
                                pLoopRequestOffset = this.respondedOffset;
                                pLoopRequestLength = this.respondedLength;
                                //.search:
                                pp = 0;
                                for (pp = ((this.respondedOffset + this.respondedLength) - 1); pp >= this.respondedOffset; pp--){
                                    if ((pLoopRequestOffset == this.respondedOffset) && (pLoopRequestLength == this.respondedLength)){
                                        pLoopTargetOffset = pp;
                                        pLoopBufferOffset = (pLoopTargetOffset - this.respondedOffset);
                                        cCurrentTravelLength++;
                                        if (cCurrentTravelLength >= cMaximumTravelLength){
                                            bBreak = true;
                                            break;
                                        }
                                        //.pastikan punya awalan yg sama:
                                        if ((this.arrayRespond[(int)pLoopBufferOffset] == arrToFind[0]) && ((pLoopBufferOffset + cToFindSize) <= ARRAY_RESPOND_MAX_SIZE)){
                                            pf = 0;
                                            mc = 0;
                                            for(pf = 0; pf < cToFindSize; pf++){
                                                if (this.arrayRespond[(int)(pLoopBufferOffset + pf)] != arrToFind[(int)pf]){
                                                    break;
                                                }else{
                                                    mc++;
                                                }
                                            }
                                            if (mc >= cToFindSize){
                                                //.ketemu:
                                                vNextViewedOffset = pLoopBufferOffset;
                                                vNextRespondOffset = this.respondedOffset;
                                                vNextRespondLength = this.respondedLength;
                                                bFound = true;
                                                break;
                                            }
                                        }
                                    }else{
                                        break;
                                    }
                                }
                            }else{
                                //.maju:
                                if ((pLoopTargetOffset + cToFindSize) > this.sourceSize){
                                    pLoopRequestLength = ARRAY_RESPOND_MAX_SIZE;
                                    pLoopRequestOffset = 0;
                                    if ((pLoopRequestOffset + pLoopRequestLength) > this.sourceSize){
                                        pLoopRequestLength = (this.sourceSize - pLoopRequestOffset);
                                    }
                                }else{
                                    pLoopRequestLength = ARRAY_RESPOND_MAX_SIZE;
                                    pLoopRequestOffset = pLoopTargetOffset;
                                    if ((pLoopRequestOffset + pLoopRequestLength) > this.sourceSize){
                                        pLoopRequestLength = (this.sourceSize - pLoopRequestOffset);
                                    }
                                }
                                if (pLoopRequestOffset == this.requestedOffset){
                                    cCurrentRepeatedLoop++;
                                }
                                if (cCurrentRepeatedLoop > cMaximumRepeatedLoop){
                                    bBreak = true;
                                }
                                this.requestedOffset = pLoopRequestOffset;
                                this.requestedLength = pLoopRequestLength;
                                this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
                                requestData();
                                pLoopRequestOffset = this.respondedOffset;
                                pLoopRequestLength = this.respondedLength;
                                //.search:
                                pp = 0;
                                for (pp = this.respondedOffset; pp < (this.respondedOffset + this.respondedLength - cToFindSize); pp++){
                                    if ((pLoopRequestOffset == this.respondedOffset) && (pLoopRequestLength == this.respondedLength)){
                                        pLoopTargetOffset = pp;
                                        pLoopBufferOffset = (pLoopTargetOffset - this.respondedOffset);
                                        cCurrentTravelLength++;
                                        if (cCurrentTravelLength >= cMaximumTravelLength){
                                            bBreak = true;
                                            break;
                                        }
                                        //.pastikan punya awalan yg sama:
                                        if ((this.arrayRespond[(int)pLoopBufferOffset] == arrToFind[0]) && ((pLoopBufferOffset + cToFindSize) <= ARRAY_RESPOND_MAX_SIZE)){
                                            pf = 0;
                                            mc = 0;
                                            for(pf = 0; pf < cToFindSize; pf++){
                                                if (this.arrayRespond[(int)(pLoopBufferOffset + pf)] != arrToFind[(int)pf]){
                                                    break;
                                                }else{
                                                    mc++;
                                                }
                                            }
                                            if (mc >= cToFindSize){
                                                //.ketemu:
                                                vNextViewedOffset = pLoopBufferOffset;
                                                vNextRespondOffset = this.respondedOffset;
                                                vNextRespondLength = this.respondedLength;
                                                bFound = true;
                                                break;
                                            }
                                        }
                                        
                                    }else{
                                        break;
                                    }
                                }
                            }
                        }
                        if (cCurrentTravelLength >= cMaximumTravelLength){
                            bBreak = true;
                        }
                        if (this.isFindNeedCancel){
                            bBreak = true;
                        }
                        if (bBreak || bFound){
                            break;
                        }
                        if (isInsideMultiThreading){
                            try{
                                Thread.sleep(1);
                            }catch(InterruptedException ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }catch(Exception ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }
                        }
                    }
                }
            }else if (!this.isViewerOpened) {
                zErrorMsg = "Viewer Closed.";
            }else if ((zText == null) || (zText.isEmpty())) {
                zErrorMsg = "Empty Input.";
            }
            if (bFound){
                mOut = "";
            }else{
                mOut = "Not Found ! \r\n\r\n" + zErrorMsg;
            }
            isFound = bFound;
        }catch(NumberFormatException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        try{
            this.isFindNeedCancel = false;
            jLabelVwFindCancel.setVisible(false);
            jScrollPaneVw.setEnabled(true);
            jTableVw.setEnabled(true);
            jScrollBarVw.setEnabled(true);
            if (isFound){
                long pActualOffset = ((vNextRespondOffset + vNextViewedOffset));
                long pBasicOffset = pActualOffset;
                if ((pActualOffset % CHARACTERS_COLUMN_LENGTH) != 0){
                    pBasicOffset = (pActualOffset - (pActualOffset % CHARACTERS_COLUMN_LENGTH));
                    vNextViewedOffset = (vNextViewedOffset + (pActualOffset - pBasicOffset));
                    vNextRespondOffset = pBasicOffset;
                    vNextRespondLength = (vNextRespondLength + (pActualOffset - pBasicOffset));
                    if ((vNextRespondOffset + vNextRespondLength) > ARRAY_RESPOND_MAX_SIZE){
                        vNextRespondLength = ARRAY_RESPOND_MAX_SIZE;
                    }
                    if ((vNextRespondOffset + vNextRespondLength) > this.sourceSize){
                        vNextRespondLength = (this.sourceSize - vNextRespondOffset);
                    }
                }
                this.requestedOffset = vNextRespondOffset;
                this.requestedLength = vNextRespondLength;
                requestData();
                long pScrollValue = (long) Math.floor((this.respondedOffset + vNextViewedOffset) / CHARACTERS_COLUMN_LENGTH);
                jScrollBarVw.setValue((int) pScrollValue);
                redrawViewer();
                long pSelColIndex = 0;
                long pSelRowIndex = 0;
                long pSelBufferOffset = 0;
                if (isSearchPrevious){
                    pSelBufferOffset = vNextViewedOffset;
                }else{
                    pSelBufferOffset = (vNextViewedOffset + vNextFindSize - 1);
                }
                pSelBufferOffset = (pSelBufferOffset - this.viewedStart);
                if (pSelBufferOffset < 0){
                    pSelBufferOffset = 0;
                }
                pSelRowIndex = (pSelBufferOffset / CHARACTERS_COLUMN_LENGTH);
                pSelColIndex = (pSelBufferOffset % CHARACTERS_COLUMN_LENGTH);
                if (isHexaDecimal){
                    pSelColIndex = (1 + pSelColIndex);
                }else{
                    pSelColIndex = (17 + pSelColIndex);
                }
                jTableVw.clearSelection();
                jTableVw.changeSelection((int)pSelRowIndex, (int)pSelColIndex, true, false);
            }else{
                this.requestedOffset = vNextRespondOffset;
                this.requestedLength = vNextRespondLength;
                requestData();
                long pScrollValue = (long) Math.floor((this.respondedOffset + vNextViewedOffset) / CHARACTERS_COLUMN_LENGTH);
                jScrollBarVw.setValue((int) pScrollValue);
                redrawViewer();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    public String startFind_20200129(String zText, boolean isHexaDecimal, boolean isSearchFromBeginning, boolean isLimited, long vLimitBytes, boolean isSearchPrevious, boolean isInsideMultiThreading){
        String mOut = "No Response";
        boolean isFound = false;
        long vNextFindSize = 0;
        long vNextViewedOffset = 0;
        long vNextRespondOffset = 0;
        long vNextRespondLength = 0;
        try{
            this.isFindNeedCancel = false;
            jLabelVwFindCancel.setVisible(true);
            jScrollPaneVw.setEnabled(false);
            jTableVw.setEnabled(false);
            jScrollBarVw.setEnabled(false);
            vNextViewedOffset = this.viewedStart;
            vNextRespondOffset = this.respondedOffset;
            vNextRespondLength = this.respondedLength;
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        try{
            String zErrorMsg = "";
            boolean bCanContinue = true;
            boolean bBreak = false;
            boolean bFound = false;
            byte[] arrToFind = new byte[0];
            int cToFindSize = 0;
            if ((this.isViewerOpened) && (zText != null) && (!zText.isEmpty())){
                if (isHexaDecimal){
                    if (!zText.matches("^[0-9a-fA-F]+$")){
                        zErrorMsg = "Invalid Hexa Decimal Character(s).";
                        bCanContinue = false;
                    }else{
                        if ((zText.length() % 2) != 0){
                            zText = ("0" + zText);
                        }
                        cToFindSize = (zText.length() / 2);
                        arrToFind = new byte[cToFindSize];
                        int ff = 0;
                        for(int pp = 0; pp < cToFindSize; pp++){
                            ff = (pp * 2);
                            arrToFind[pp] = Integer.decode("0x" + zText.substring(ff, (ff + 2))).byteValue();
                        }
                    }
                }else{
                    arrToFind = zText.getBytes();
                    cToFindSize = arrToFind.length;
                }
                vNextFindSize = cToFindSize;
                if (bCanContinue){
                    long cMaximumTravelLength = ((isLimited) ? vLimitBytes : this.sourceSize);
                    long cCurrentTravelLength = 0;
                    long cMaximumRepeatedLoop = 2;
                    long cCurrentRepeatedLoop = 0;
                    long pBeginOffset = 0;
                    long pLoopTargetOffset = 0;
                    long pLoopRequestOffset = 0;
                    long pLoopRequestLength = 0;
                    long pLoopBufferOffset = 0;
                    long pp = 0;
                    long pf = 0;
                    long mc = 0;
                    if (isSearchFromBeginning){
                        pBeginOffset = 0;
                        pLoopTargetOffset = pBeginOffset;
                    }else{
                        pBeginOffset = (this.respondedOffset + this.viewedStart);
                        long pSelCol = jTableVw.getSelectedColumn();
                        long pSelRow = jTableVw.getSelectedRow();
                        long pSelOffset = -1;
                        if ((pSelCol != -1) && (pSelRow != -1)){
                            if (pSelCol == 0){
                                pSelOffset = (pSelRow * CHARACTERS_COLUMN_LENGTH);
                            }else if ((pSelCol >= 1) && (pSelCol <= 16)){
                                pSelOffset = ((pSelRow * CHARACTERS_COLUMN_LENGTH) + (pSelCol - 1));
                            }else if ((pSelCol >= 17) && (pSelCol <= 32)){
                                pSelOffset = ((pSelRow * CHARACTERS_COLUMN_LENGTH) + (pSelCol - 17));
                            }else{
                                pSelOffset = ((pSelRow * CHARACTERS_COLUMN_LENGTH) + (CHARACTERS_COLUMN_LENGTH - 1));
                            }
                            if (pSelOffset >= 0){
                                pBeginOffset += pSelOffset;
                            }
                        }
                        pLoopTargetOffset = pBeginOffset;
                    }
                    
                    while ((!bFound) && (!bBreak)){
                        if (cCurrentTravelLength < cMaximumTravelLength){
                            if (isSearchPrevious){
                                //.mundur:
                                if (pLoopTargetOffset > 0){
                                    pLoopTargetOffset--;
                                }
                                if (pLoopTargetOffset <= (cToFindSize - 1)){
                                    //.sudah berada paling atas, ambil dari bawah saja:
                                    pLoopRequestLength = ARRAY_RESPOND_MAX_SIZE;
                                    pLoopRequestOffset = (this.sourceSize - pLoopRequestLength);
                                    if (pLoopRequestOffset < 0){
                                        pLoopRequestOffset = 0;
                                        pLoopRequestLength = (this.sourceSize - pLoopRequestOffset);
                                    }
                                }else{
                                    pLoopRequestLength = ARRAY_RESPOND_MAX_SIZE;
                                    pLoopRequestOffset = (pLoopTargetOffset - pLoopRequestLength + cToFindSize);
                                    if (pLoopRequestOffset < 0){
                                        pLoopRequestOffset = 0;
                                    }
                                    if ((pLoopRequestOffset + pLoopRequestLength) > this.sourceSize){
                                        pLoopRequestLength = (this.sourceSize - pLoopRequestOffset);
                                    }
                                }
                                if (pLoopRequestOffset == this.requestedOffset){
                                    cCurrentRepeatedLoop++;
                                }
                                if (cCurrentRepeatedLoop > cMaximumRepeatedLoop){
                                    bBreak = true;
                                }
                                this.requestedOffset = pLoopRequestOffset;
                                this.requestedLength = pLoopRequestLength;
                                this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
                                requestData();
                                pLoopRequestOffset = this.respondedOffset;
                                pLoopRequestLength = this.respondedLength;
                                //.search:
                                pp = 0;
                                for (pp = ((this.respondedOffset + this.respondedLength) - 1); pp >= this.respondedOffset; pp--){
                                    if ((pLoopRequestOffset == this.respondedOffset) && (pLoopRequestLength == this.respondedLength)){
                                        pLoopTargetOffset = pp;
                                        pLoopBufferOffset = (pLoopTargetOffset - this.respondedOffset);
                                        cCurrentTravelLength++;
                                        if (cCurrentTravelLength >= cMaximumTravelLength){
                                            bBreak = true;
                                            break;
                                        }
                                        //.pastikan punya awalan yg sama:
                                        if ((this.arrayRespond[(int)pLoopBufferOffset] == arrToFind[0]) && ((pLoopBufferOffset + cToFindSize) <= ARRAY_RESPOND_MAX_SIZE)){
                                            pf = 0;
                                            mc = 0;
                                            for(pf = 0; pf < cToFindSize; pf++){
                                                if (this.arrayRespond[(int)(pLoopBufferOffset + pf)] != arrToFind[(int)pf]){
                                                    break;
                                                }else{
                                                    mc++;
                                                }
                                            }
                                            if (mc >= cToFindSize){
                                                //.ketemu:
                                                vNextViewedOffset = pLoopBufferOffset;
                                                vNextRespondOffset = this.respondedOffset;
                                                vNextRespondLength = this.respondedLength;
                                                bFound = true;
                                                break;
                                            }
                                        }
                                    }else{
                                        break;
                                    }
                                }
                            }else{
                                //.maju:
                                if ((pLoopTargetOffset + cToFindSize) > this.sourceSize){
                                    pLoopRequestLength = ARRAY_RESPOND_MAX_SIZE;
                                    pLoopRequestOffset = 0;
                                    if ((pLoopRequestOffset + pLoopRequestLength) > this.sourceSize){
                                        pLoopRequestLength = (this.sourceSize - pLoopRequestOffset);
                                    }
                                }else{
                                    pLoopRequestLength = ARRAY_RESPOND_MAX_SIZE;
                                    pLoopRequestOffset = pLoopTargetOffset;
                                    if ((pLoopRequestOffset + pLoopRequestLength) > this.sourceSize){
                                        pLoopRequestLength = (this.sourceSize - pLoopRequestOffset);
                                    }
                                }
                                if (pLoopRequestOffset == this.requestedOffset){
                                    cCurrentRepeatedLoop++;
                                }
                                if (cCurrentRepeatedLoop > cMaximumRepeatedLoop){
                                    bBreak = true;
                                }
                                this.requestedOffset = pLoopRequestOffset;
                                this.requestedLength = pLoopRequestLength;
                                this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
                                requestData();
                                pLoopRequestOffset = this.respondedOffset;
                                pLoopRequestLength = this.respondedLength;
                                //.search:
                                pp = 0;
                                for (pp = this.respondedOffset; pp < (this.respondedOffset + this.respondedLength - cToFindSize); pp++){
                                    if ((pLoopRequestOffset == this.respondedOffset) && (pLoopRequestLength == this.respondedLength)){
                                        pLoopTargetOffset = pp;
                                        pLoopBufferOffset = (pLoopTargetOffset - this.respondedOffset);
                                        cCurrentTravelLength++;
                                        if (cCurrentTravelLength >= cMaximumTravelLength){
                                            bBreak = true;
                                            break;
                                        }
                                        //.pastikan punya awalan yg sama:
                                        if ((this.arrayRespond[(int)pLoopBufferOffset] == arrToFind[0]) && ((pLoopBufferOffset + cToFindSize) <= ARRAY_RESPOND_MAX_SIZE)){
                                            pf = 0;
                                            mc = 0;
                                            for(pf = 0; pf < cToFindSize; pf++){
                                                if (this.arrayRespond[(int)(pLoopBufferOffset + pf)] != arrToFind[(int)pf]){
                                                    break;
                                                }else{
                                                    mc++;
                                                }
                                            }
                                            if (mc >= cToFindSize){
                                                //.ketemu:
                                                vNextViewedOffset = pLoopBufferOffset;
                                                vNextRespondOffset = this.respondedOffset;
                                                vNextRespondLength = this.respondedLength;
                                                bFound = true;
                                                break;
                                            }
                                        }
                                        
                                    }else{
                                        break;
                                    }
                                }
                            }
                        }
                        if (cCurrentTravelLength >= cMaximumTravelLength){
                            bBreak = true;
                        }
                        if (this.isFindNeedCancel){
                            bBreak = true;
                        }
                        if (bBreak || bFound){
                            break;
                        }
                        if (isInsideMultiThreading){
                            try{
                                Thread.sleep(1);
                            }catch(InterruptedException ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }catch(Exception ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }
                        }
                    }
                }
            }else if (!this.isViewerOpened) {
                zErrorMsg = "Viewer Closed.";
            }else if ((zText == null) || (zText.isEmpty())) {
                zErrorMsg = "Empty Input.";
            }
            if (bFound){
                mOut = "";
            }else{
                mOut = "Not Found ! \r\n\r\n" + zErrorMsg;
            }
            isFound = bFound;
        }catch(NumberFormatException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        try{
            this.isFindNeedCancel = false;
            jLabelVwFindCancel.setVisible(false);
            jScrollPaneVw.setEnabled(true);
            jTableVw.setEnabled(true);
            jScrollBarVw.setEnabled(true);
            if (isFound){
                long pActualOffset = ((vNextRespondOffset + vNextViewedOffset));
                long pBasicOffset = pActualOffset;
                if ((pActualOffset % CHARACTERS_COLUMN_LENGTH) != 0){
                    pBasicOffset = (pActualOffset - (pActualOffset % CHARACTERS_COLUMN_LENGTH));
                    vNextViewedOffset = (vNextViewedOffset + (pActualOffset - pBasicOffset));
                    vNextRespondOffset = pBasicOffset;
                    vNextRespondLength = (vNextRespondLength + (pActualOffset - pBasicOffset));
                    if ((vNextRespondOffset + vNextRespondLength) > ARRAY_RESPOND_MAX_SIZE){
                        vNextRespondLength = ARRAY_RESPOND_MAX_SIZE;
                    }
                    if ((vNextRespondOffset + vNextRespondLength) > this.sourceSize){
                        vNextRespondLength = (this.sourceSize - vNextRespondOffset);
                    }
                }
                this.requestedOffset = vNextRespondOffset;
                this.requestedLength = vNextRespondLength;
                requestData();
                long pScrollValue = (long) Math.floor((this.respondedOffset + vNextViewedOffset) / CHARACTERS_COLUMN_LENGTH);
                jScrollBarVw.setValue((int) pScrollValue);
                redrawViewer();
                long pSelColIndex = 0;
                long pSelRowIndex = 0;
                long pSelBufferOffset = 0;
                if (isSearchPrevious){
                    pSelBufferOffset = vNextViewedOffset;
                }else{
                    pSelBufferOffset = (vNextViewedOffset + vNextFindSize - 1);
                }
                pSelBufferOffset = (pSelBufferOffset - this.viewedStart);
                if (pSelBufferOffset < 0){
                    pSelBufferOffset = 0;
                }
                pSelRowIndex = (pSelBufferOffset / CHARACTERS_COLUMN_LENGTH);
                pSelColIndex = (pSelBufferOffset % CHARACTERS_COLUMN_LENGTH);
                if (isHexaDecimal){
                    pSelColIndex = (1 + pSelColIndex);
                }else{
                    pSelColIndex = (17 + pSelColIndex);
                }
                //////System.out.println("@pSelBufferOffset=" + pSelBufferOffset + ", pSelRowIndex=" + pSelRowIndex + ", pSelColIndex=" + pSelColIndex + ", vNextViewedOffset=" + vNextViewedOffset + ", vNextFindSize=" + vNextFindSize);
                jTableVw.clearSelection();
                jTableVw.changeSelection((int)pSelRowIndex, (int)pSelColIndex, true, false);
            }else{
                this.requestedOffset = vNextRespondOffset;
                this.requestedLength = vNextRespondLength;
                requestData();
                long pScrollValue = (long) Math.floor((this.respondedOffset + vNextViewedOffset) / CHARACTERS_COLUMN_LENGTH);
                jScrollBarVw.setValue((int) pScrollValue);
                redrawViewer();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    private void fixOnResized(){
        try{
            if (timerResized != null){
                timerResized.restart();
            }else{
                timerResized = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            timerResized.stop();
                            int vBaseHeight = (jScrollPaneVw.getHeight() - 0);
                            int vHeaderHeight = jTableVw.getTableHeader().getHeight();
                            int vContentHeight = (vBaseHeight - vHeaderHeight - (vHeaderHeight / 2));
                            int vRowHeight = jTableVw.getRowHeight();
                            int vRowMargin = jTableVw.getRowMargin();
                            int cPrevRowCount = jTableVw.getRowCount();
                            int cNextRowCount = (vContentHeight / (vRowHeight + vRowMargin));
                            int cDiffRowCount = 0;
                            if (cNextRowCount != cPrevRowCount){
                                if (cNextRowCount > cPrevRowCount){
                                    cDiffRowCount = (cNextRowCount - cPrevRowCount);
                                    ITMComponentLayoutHelper.addTableEmptyRows(jTableVw, cDiffRowCount);
                                }else if (cNextRowCount < cPrevRowCount){
                                    cDiffRowCount = (cPrevRowCount - cNextRowCount);
                                    ITMComponentLayoutHelper.removeTableEmptyRows(jTableVw, cDiffRowCount);
                                }
                            }
                            redrawViewer();
                        }catch(Exception ex0){
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }
                });
                timerResized.start();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void fixOnMouseScroll(java.awt.event.MouseWheelEvent evt){
        try{
            long vMaxScroll = jScrollBarVw.getMaximum();
            long vCurScroll = jScrollBarVw.getValue();
            long vNextScroll = vCurScroll;
            vNextScroll += evt.getWheelRotation();
            if (vNextScroll <= 0){
                vNextScroll = 0;
            } else if (vNextScroll > vMaxScroll){
                vNextScroll = vMaxScroll;
            }
            if (vNextScroll != vCurScroll){
                jScrollBarVw.setValue((int) vNextScroll);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void fixOnScroll(){
        try{
            //.recalculate:
            long vCurScroll = jScrollBarVw.getValue();
            long vCurOffset = (vCurScroll * CHARACTERS_COLUMN_LENGTH);
            if (vCurOffset < this.respondedOffset){
                this.requestedOffset = (vCurOffset - (ARRAY_RESPOND_MAX_SIZE / 2));
                if (this.requestedOffset <= 0){
                    this.requestedOffset = 0;
                }
                this.requestedLength = ARRAY_RESPOND_MAX_SIZE;
                if (this.requestedLength > this.sourceSize){
                    this.requestedLength = (int) this.sourceSize;
                }
                this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
                requestData();
            } else if (vCurOffset >= (this.respondedOffset + this.respondedLength)){
                this.requestedOffset = (vCurOffset - (ARRAY_RESPOND_MAX_SIZE / 4));
                if (this.requestedOffset <= 0){
                    this.requestedOffset = 0;
                }
                this.requestedLength = ARRAY_RESPOND_MAX_SIZE;
                if (this.requestedLength > this.sourceSize){
                    this.requestedLength = (int) this.sourceSize;
                }
                this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
                requestData();
            }else{
                redrawViewer();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneVw = new javax.swing.JScrollPane();
        jTableVw = new javax.swing.JTable();
        jScrollBarVw = new javax.swing.JScrollBar();
        jPanelVw = new javax.swing.JPanel();
        jLabelVwSourceSizeTitle = new javax.swing.JLabel();
        jLabelVwSourceSizeValue = new javax.swing.JLabel();
        jLabelVwTopOffsetTitle = new javax.swing.JLabel();
        jLabelVwTopOffsetValue = new javax.swing.JLabel();
        jLabelVwShowFind = new javax.swing.JLabel();
        jLabelVwFindCancel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(500, 150));
        setPreferredSize(new java.awt.Dimension(650, 200));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                formAncestorResized(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jTableVw.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTableVw.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Offset", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableVw.setCellSelectionEnabled(true);
        jTableVw.setRowHeight(20);
        jTableVw.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableVw.getTableHeader().setReorderingAllowed(false);
        jTableVw.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTableVwMouseMoved(evt);
            }
        });
        jTableVw.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jTableVwMouseWheelMoved(evt);
            }
        });
        jScrollPaneVw.setViewportView(jTableVw);
        jTableVw.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (jTableVw.getColumnModel().getColumnCount() > 0) {
            jTableVw.getColumnModel().getColumn(0).setPreferredWidth(170);
            jTableVw.getColumnModel().getColumn(1).setResizable(false);
            jTableVw.getColumnModel().getColumn(1).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(2).setResizable(false);
            jTableVw.getColumnModel().getColumn(2).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(3).setResizable(false);
            jTableVw.getColumnModel().getColumn(3).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(4).setResizable(false);
            jTableVw.getColumnModel().getColumn(4).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(5).setResizable(false);
            jTableVw.getColumnModel().getColumn(5).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(6).setResizable(false);
            jTableVw.getColumnModel().getColumn(6).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(7).setResizable(false);
            jTableVw.getColumnModel().getColumn(7).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(8).setResizable(false);
            jTableVw.getColumnModel().getColumn(8).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(9).setResizable(false);
            jTableVw.getColumnModel().getColumn(9).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(10).setResizable(false);
            jTableVw.getColumnModel().getColumn(10).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(11).setResizable(false);
            jTableVw.getColumnModel().getColumn(11).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(12).setResizable(false);
            jTableVw.getColumnModel().getColumn(12).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(13).setResizable(false);
            jTableVw.getColumnModel().getColumn(13).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(14).setResizable(false);
            jTableVw.getColumnModel().getColumn(14).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(15).setResizable(false);
            jTableVw.getColumnModel().getColumn(15).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(16).setResizable(false);
            jTableVw.getColumnModel().getColumn(16).setPreferredWidth(34);
            jTableVw.getColumnModel().getColumn(17).setResizable(false);
            jTableVw.getColumnModel().getColumn(17).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(18).setResizable(false);
            jTableVw.getColumnModel().getColumn(18).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(19).setResizable(false);
            jTableVw.getColumnModel().getColumn(19).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(20).setResizable(false);
            jTableVw.getColumnModel().getColumn(20).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(21).setResizable(false);
            jTableVw.getColumnModel().getColumn(21).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(22).setResizable(false);
            jTableVw.getColumnModel().getColumn(22).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(23).setResizable(false);
            jTableVw.getColumnModel().getColumn(23).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(24).setResizable(false);
            jTableVw.getColumnModel().getColumn(24).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(25).setResizable(false);
            jTableVw.getColumnModel().getColumn(25).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(26).setResizable(false);
            jTableVw.getColumnModel().getColumn(26).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(27).setResizable(false);
            jTableVw.getColumnModel().getColumn(27).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(28).setResizable(false);
            jTableVw.getColumnModel().getColumn(28).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(29).setResizable(false);
            jTableVw.getColumnModel().getColumn(29).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(30).setResizable(false);
            jTableVw.getColumnModel().getColumn(30).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(31).setResizable(false);
            jTableVw.getColumnModel().getColumn(31).setPreferredWidth(20);
            jTableVw.getColumnModel().getColumn(32).setResizable(false);
            jTableVw.getColumnModel().getColumn(32).setPreferredWidth(20);
        }

        jScrollBarVw.setMaximum(0);
        jScrollBarVw.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBarVwAdjustmentValueChanged(evt);
            }
        });

        jLabelVwSourceSizeTitle.setText("Source Size : ");

        jLabelVwSourceSizeValue.setText("--");

        jLabelVwTopOffsetTitle.setText("Top Offset : ");

        jLabelVwTopOffsetValue.setText("--");

        jLabelVwShowFind.setText("Find Text");
        jLabelVwShowFind.setToolTipText("Find Text ( CTRL+F )");
        jLabelVwShowFind.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelVwShowFind.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelVwShowFindMouseClicked(evt);
            }
        });

        jLabelVwFindCancel.setBackground(new java.awt.Color(255, 0, 0));
        jLabelVwFindCancel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelVwFindCancel.setForeground(new java.awt.Color(255, 255, 255));
        jLabelVwFindCancel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelVwFindCancel.setText("X");
        jLabelVwFindCancel.setToolTipText("Stop Find Process");
        jLabelVwFindCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelVwFindCancel.setOpaque(true);
        jLabelVwFindCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelVwFindCancelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelVwLayout = new javax.swing.GroupLayout(jPanelVw);
        jPanelVw.setLayout(jPanelVwLayout);
        jPanelVwLayout.setHorizontalGroup(
            jPanelVwLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVwLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelVwSourceSizeTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelVwSourceSizeValue, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelVwTopOffsetTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelVwTopOffsetValue, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelVwShowFind)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelVwFindCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelVwLayout.setVerticalGroup(
            jPanelVwLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVwLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabelVwSourceSizeValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelVwTopOffsetTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabelVwTopOffsetValue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabelVwShowFind)
                .addComponent(jLabelVwFindCancel))
            .addComponent(jLabelVwSourceSizeTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPaneVw, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollBarVw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanelVw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneVw, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollBarVw, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelVw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        fixOnResized();
    }//GEN-LAST:event_formComponentResized

    private void formAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formAncestorResized
        // TODO add your handling code here:
        fixOnResized();
    }//GEN-LAST:event_formAncestorResized

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here:
        fixOnResized();
    }//GEN-LAST:event_formFocusGained

    private void jScrollBarVwAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBarVwAdjustmentValueChanged
        // TODO add your handling code here:
        fixOnScroll();
    }//GEN-LAST:event_jScrollBarVwAdjustmentValueChanged

    private void jTableVwMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jTableVwMouseWheelMoved
        // TODO add your handling code here:
        fixOnMouseScroll(evt);
    }//GEN-LAST:event_jTableVwMouseWheelMoved

    private void jLabelVwShowFindMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelVwShowFindMouseClicked
        // TODO add your handling code here:
        onShowDialogFind();
    }//GEN-LAST:event_jLabelVwShowFindMouseClicked
    
    private void jLabelVwFindCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelVwFindCancelMouseClicked
        // TODO add your handling code here:
        stopFind();
    }//GEN-LAST:event_jLabelVwFindCancelMouseClicked

    private void jTableVwMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVwMouseMoved
        // TODO add your handling code here:
        try{
            //System.out.println("MouseMove: evt=" + evt.paramString());
            //jTableVw.setToolTipText(evt.paramString());
            Point pt = evt.getPoint();
            int pColIndex = jTableVw.columnAtPoint(pt);
            int pRowIndex = jTableVw.rowAtPoint(pt);
            int pBufferIndex = -1;
            String zToolTip = "";
            if (this.isViewerOpened){
                if (pColIndex == 0){
                    pBufferIndex = (int) (this.viewedStart + (pRowIndex * CHARACTERS_COLUMN_LENGTH));
                    if (pBufferIndex < (this.respondedLength)){
                        zToolTip = String.format("%012X", (this.respondedOffset + pBufferIndex)) + " = " + String.format("%,d", (this.respondedOffset + pBufferIndex));
                    }
                }else if ((pColIndex >= 1) && (pColIndex <= 16)){
                    pBufferIndex = (int) (this.viewedStart + (pRowIndex * CHARACTERS_COLUMN_LENGTH) + (pColIndex - 1));
                    if (pBufferIndex < (this.respondedLength)){
                        zToolTip = String.format("%02X", this.arrayRespond[pBufferIndex + 0]) + " = " + String.format("%,d", this.arrayRespond[pBufferIndex + 0]);
                    }
                }else if ((pColIndex >= 17) && (pColIndex <= 32)){
                    pBufferIndex = (int) (this.viewedStart + (pRowIndex * CHARACTERS_COLUMN_LENGTH) + (pColIndex - 17));
                    if (pBufferIndex < (this.respondedLength)){
                        zToolTip = String.format("%2X", this.arrayRespond[pBufferIndex + 0]) + " = " + String.format("%,d", this.arrayRespond[pBufferIndex + 0]);
                    }
                }
            }
            jTableVw.setToolTipText(zToolTip);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_jTableVwMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelVwFindCancel;
    private javax.swing.JLabel jLabelVwShowFind;
    private javax.swing.JLabel jLabelVwSourceSizeTitle;
    private javax.swing.JLabel jLabelVwSourceSizeValue;
    private javax.swing.JLabel jLabelVwTopOffsetTitle;
    private javax.swing.JLabel jLabelVwTopOffsetValue;
    private javax.swing.JPanel jPanelVw;
    private javax.swing.JScrollBar jScrollBarVw;
    private javax.swing.JScrollPane jScrollPaneVw;
    private javax.swing.JTable jTableVw;
    // End of variables declaration//GEN-END:variables
}