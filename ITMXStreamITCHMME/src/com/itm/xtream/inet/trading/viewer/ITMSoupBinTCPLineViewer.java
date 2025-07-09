/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.viewer;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPLineViewer extends javax.swing.JPanel {
    
    //.timer resize event:
    private Timer timerResized;
    
    //timer check source:
    private Timer timerCheckSource;
    
    private static final int MAX_ELAPSED_BEFORE_RECHECK_SOURCE = 5;
    
    private int elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
    
    //.callback:
    public interface LineViewerCallBack {
        public void onRequestData(long offset, long length);
        public void onBackupLinesChanges(List<String> listBackupLines);
        public void onButtonNavigationStateChanges(boolean enableFirst, boolean enablePrevious, boolean enableNext, boolean enableLast);
    }
    
    //.register:
    private ITMSoupBinTCPLineViewer.LineViewerCallBack callBackObject;
    
    //.flags:
    private boolean isViewerOpened = false;
    
    //.posisi arrayrespond[0]:
    private long requestedOffset = 0; //.yg diminta;
    private long respondedOffset = 0; //.yg diberi;
    
    //.ukuran arrayrespond[n]:
    private long requestedLength = 0; //.yg diminta;
    private long respondedLength = 0; //.yg diberi;
    
    private long lastRedrawOffset = 0;
    private long lastRedrawLength = 0;
    
    private boolean needAdjustRespondedOffset = false;
    
    //.penampung data:
    private static final int LINE_MAXIMUM_CHARACTERS_LENGTH = (4 * 1000);
    private static final int ARRAY_RESPOND_PAGE_MAX_SIZE = (500 * 1000 * 1); //.500rb bytes;
    private final byte[] arrayRespond = new byte[ARRAY_RESPOND_PAGE_MAX_SIZE];
    
    //.ukuran sumber:
    private long sourceSize = ARRAY_RESPOND_PAGE_MAX_SIZE;
    
    public long getRespondedOffset() {
        return this.respondedOffset;
    }

    public long getRespondedLength() {
        return this.respondedLength;
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
    
    public void openViewer(LineViewerCallBack callBackObject){
        try{
            closeViewer();
            if (callBackObject != null){
                this.callBackObject = callBackObject;
            }else{
                this.callBackObject = new LineViewerCallBack() {
                    @Override
                    public void onRequestData(long offset, long length) {}
                    @Override
                    public void onBackupLinesChanges(List<String> listBackupLines) {}
                    @Override
                    public void onButtonNavigationStateChanges(boolean enableFirst, boolean enablePrevious, boolean enableNext, boolean enableLast) {}
                };
            }
            this.isViewerOpened = true;
            this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
            this.requestedOffset = 0;
            this.requestedLength = ARRAY_RESPOND_PAGE_MAX_SIZE;
            this.sourceSize = ARRAY_RESPOND_PAGE_MAX_SIZE;
            this.lastRedrawOffset = 0;
            this.lastRedrawLength = 0;
            this.needAdjustRespondedOffset = false;
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
            this.sourceSize = ARRAY_RESPOND_PAGE_MAX_SIZE;
            this.requestedOffset = 0;
            this.respondedOffset = 0;
            this.requestedLength = 0;
            this.respondedLength = 0;
            this.elapsedBeforeReCheckSource = MAX_ELAPSED_BEFORE_RECHECK_SOURCE;
            this.lastRedrawOffset = 0;
            this.lastRedrawLength = 0;
            this.needAdjustRespondedOffset = false;
            redrawViewer();
            //.close dialogs:
            
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
                int trimSrcStart = 0;
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
                    //.cek crlf terakhir, harus dipotong (berhenti) di situ:
                    long pFirstCrLf = -1;
                    long pLastCrLf = -1;
                    if (length >= 2){
                        for(int pBt = 0; pBt < (length - 1); pBt++){
                            if ((arrayRespond[pBt + 0] == 13) && (arrayRespond[pBt + 1] == 10)){
                                pLastCrLf = (pBt + 1);
                                if (pFirstCrLf == -1){
                                    pFirstCrLf = pLastCrLf;
                                }
                            }
                        }
                    }
                    if (pLastCrLf > 0){
                        length = (pLastCrLf + 1);
                    }
                    this.respondedLength = length;
                    if (this.needAdjustRespondedOffset){
                        this.needAdjustRespondedOffset = false;
                        if ((pFirstCrLf >= 0) && (pLastCrLf >= 0) && (pFirstCrLf < pLastCrLf)){
                            trimSrcStart = (int) (pFirstCrLf + 1);
                            offset += trimSrcStart;
                            length -= trimSrcStart;
                            this.respondedOffset = offset;
                            this.respondedLength = length;
                            this.requestedOffset = offset;
                            this.requestedLength = ARRAY_RESPOND_PAGE_MAX_SIZE;
                        }
                    }
                    System.arraycopy(arrayRespond, trimSrcStart, this.arrayRespond, 0, (int) length);
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
            boolean bScrollToBottom = false;
            if ((this.isViewerOpened) && ((this.lastRedrawOffset != this.respondedOffset) || (this.lastRedrawLength != this.respondedLength))){
                this.lastRedrawOffset = this.respondedOffset;
                this.lastRedrawLength = this.respondedLength;
                //.cek & set scrollbar apakah berada di paling bawah:
                long sbValue = jScrollPaneVw.getVerticalScrollBar().getValue();
                long sbLowestValue = sbValue + jScrollPaneVw.getVerticalScrollBar().getVisibleAmount();
                long sbMaxValue = jScrollPaneVw.getVerticalScrollBar().getMaximum();
                bScrollToBottom = ((this.respondedOffset > 0) && (sbMaxValue == sbLowestValue));
                int pLineBegin = 0;
                int pLineEnd = (pLineBegin - 1);
                DefaultListModel mdlList = (DefaultListModel) jListVw.getModel();
                mdlList.clear();
                String zEachLine = "";
                for(int pBt = 0; pBt < this.respondedLength; pBt++){
                    if ((pBt <= (this.respondedLength - 2)) && (this.arrayRespond[pBt + 0] == 13) && (this.arrayRespond[pBt + 1] == 10)){
                        if ((pLineEnd >= pLineBegin) && ((pLineEnd - pLineBegin) >= 0)){
                            if ((pLineEnd - pLineBegin) <= LINE_MAXIMUM_CHARACTERS_LENGTH){
                                zEachLine = new String(arrayRespond, pLineBegin, (pLineEnd - pLineBegin) + 1);
                            }else{
                                zEachLine = "#LINE_ERROR : MORE THAN " + LINE_MAXIMUM_CHARACTERS_LENGTH + " CHARACTERS (" + (pLineEnd - pLineBegin) + ")#";
                            }
                            mdlList.addElement(zEachLine);
                        }
                        pLineBegin = (pBt + 2);
                        pLineEnd = (pLineBegin - 1);
                        pBt = pLineBegin;
                    }else{
                        pLineEnd = pBt;
                    }
                }
                if ((pLineEnd >= pLineBegin) && ((pLineEnd - pLineBegin) >= 0)){
                    if ((pLineEnd - pLineBegin) <= LINE_MAXIMUM_CHARACTERS_LENGTH){
                        zEachLine = new String(arrayRespond, pLineBegin, (pLineEnd - pLineBegin) + 1);
                    }else{
                        zEachLine = "#LINE_ERROR : MORE THAN " + LINE_MAXIMUM_CHARACTERS_LENGTH + " CHARACTERS (" + (pLineEnd - pLineBegin) + ")#";
                    }
                    mdlList.addElement(zEachLine);
                }
                processSelectionChanges();
            }else if (!this.isViewerOpened) {
                DefaultListModel mdlList = (DefaultListModel) jListVw.getModel();
                mdlList.clear();
                processSelectionChanges();
            }
            //.button state:
            this.jButtonGoToFirst.setEnabled(this.isViewerOpened);
            this.jButtonGoToPrevious.setEnabled(this.isViewerOpened);
            this.jButtonGoToNext.setEnabled(this.isViewerOpened);
            this.jButtonGoToLast.setEnabled(this.isViewerOpened);
            if (this.isViewerOpened){
                if (this.respondedOffset <= 0){
                    this.jButtonGoToFirst.setEnabled(false);
                    this.jButtonGoToPrevious.setEnabled(false);
                }
                if ((this.respondedOffset + this.respondedLength) >= this.sourceSize){
                    this.jButtonGoToNext.setEnabled(false);
                    this.jButtonGoToLast.setEnabled(false);
                }
            }
            processButtonNavigationStateChanges();
            //.scroll:
            if (bScrollToBottom){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jScrollPaneVw.getVerticalScrollBar().setValue(jScrollPaneVw.getVerticalScrollBar().getMaximum());
                    }
                });
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void gotoFirstPage(){
        try{
            if (this.isViewerOpened){
                this.requestedOffset = 0;
                this.requestedLength = ARRAY_RESPOND_PAGE_MAX_SIZE;
                requestData();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void gotoPreviousPage(){
        try{
            if (this.isViewerOpened){
                this.requestedLength = ARRAY_RESPOND_PAGE_MAX_SIZE;
                if ((this.respondedOffset - this.requestedLength) < 0){
                    this.requestedOffset = 0;
                    this.requestedLength = (this.respondedOffset - 0);
                }else{
                    this.requestedOffset = (this.respondedOffset - this.requestedLength);
                }
                if (this.requestedOffset > 0){
                    this.needAdjustRespondedOffset = true;
                }
                requestData();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void gotoNextPage(){
        try{
            if (this.isViewerOpened){
                if ((this.respondedOffset + this.respondedLength) < this.sourceSize){
                    this.requestedOffset = (this.respondedOffset + this.respondedLength);
                    this.requestedLength = ARRAY_RESPOND_PAGE_MAX_SIZE;
                    requestData();
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void gotoLastPage(){
        try{
            if (this.isViewerOpened){
                this.requestedLength = ARRAY_RESPOND_PAGE_MAX_SIZE;
                if (this.requestedLength > this.sourceSize){
                    this.requestedLength = this.sourceSize;
                }
                this.requestedOffset = (this.sourceSize - this.requestedLength);
                this.needAdjustRespondedOffset = true;
                requestData();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void processSelectionChanges(){
        try{
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    List<String> listSelected = jListVw.getSelectedValuesList();
                    DefaultListModel mListModel = (DefaultListModel)jListVw.getModel();
                    if ((listSelected.isEmpty()) && (mListModel.getSize() > 0)){
                        List<String> listAvailable = new ArrayList<>();
                        for(int pp = 0; pp < mListModel.getSize(); pp++){
                            listAvailable.add((String) mListModel.get(pp));
                        }
                        callBackObject.onBackupLinesChanges(listAvailable);
                    }else{
                        callBackObject.onBackupLinesChanges(listSelected);
                    }
                }
            });
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void processButtonNavigationStateChanges(){
        try{
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    callBackObject.onButtonNavigationStateChanges(jButtonGoToFirst.isEnabled(), jButtonGoToPrevious.isEnabled(), jButtonGoToNext.isEnabled(), jButtonGoToLast.isEnabled());
                }
            });
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void doButtonNavigationGoToClick(boolean goToFirst, boolean goToPrevious, boolean goToNext, boolean goToLast){
        try{
            if (goToFirst){
                gotoFirstPage();
            }else if (goToPrevious){
                gotoPreviousPage();
            }else if (goToNext){
                gotoNextPage();
            }else if (goToLast){
                gotoLastPage();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    /**
     * Creates new form ITMSoupBinTCPLineViewer
     */
    public ITMSoupBinTCPLineViewer() {
        //.inisialisasi internal form:
        initComponents();
        //.inisialisasi eksternal form:
        initComponentsEx();
    }
    
    private void initComponentsEx() {
        try{
            //.rapikan:
            
            //... .
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
        jListVw = new javax.swing.JList<>();
        jButtonGoToFirst = new javax.swing.JButton();
        jButtonGoToPrevious = new javax.swing.JButton();
        jButtonGoToNext = new javax.swing.JButton();
        jButtonGoToLast = new javax.swing.JButton();

        jListVw.setBackground(new java.awt.Color(254, 254, 254));
        jListVw.setFont(new java.awt.Font("Lucida Console", 0, 11)); // NOI18N
        jListVw.setModel(new DefaultListModel());
        jListVw.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListVwValueChanged(evt);
            }
        });
        jScrollPaneVw.setViewportView(jListVw);

        jButtonGoToFirst.setText("First");
        jButtonGoToFirst.setEnabled(false);
        jButtonGoToFirst.setPreferredSize(new java.awt.Dimension(85, 23));
        jButtonGoToFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToFirstActionPerformed(evt);
            }
        });

        jButtonGoToPrevious.setText("Previous");
        jButtonGoToPrevious.setEnabled(false);
        jButtonGoToPrevious.setPreferredSize(new java.awt.Dimension(85, 23));
        jButtonGoToPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToPreviousActionPerformed(evt);
            }
        });

        jButtonGoToNext.setText("Next");
        jButtonGoToNext.setEnabled(false);
        jButtonGoToNext.setPreferredSize(new java.awt.Dimension(85, 23));
        jButtonGoToNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToNextActionPerformed(evt);
            }
        });

        jButtonGoToLast.setText("Last");
        jButtonGoToLast.setEnabled(false);
        jButtonGoToLast.setPreferredSize(new java.awt.Dimension(85, 23));
        jButtonGoToLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 50, Short.MAX_VALUE)
                .addComponent(jButtonGoToFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGoToPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGoToNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGoToLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPaneVw)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPaneVw, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonGoToPrevious, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGoToNext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGoToLast, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGoToFirst, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGoToFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToFirstActionPerformed
        // TODO add your handling code here:
        gotoFirstPage();
    }//GEN-LAST:event_jButtonGoToFirstActionPerformed

    private void jButtonGoToPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToPreviousActionPerformed
        // TODO add your handling code here:
        gotoPreviousPage();
    }//GEN-LAST:event_jButtonGoToPreviousActionPerformed

    private void jButtonGoToNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToNextActionPerformed
        // TODO add your handling code here:
        gotoNextPage();
    }//GEN-LAST:event_jButtonGoToNextActionPerformed

    private void jButtonGoToLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToLastActionPerformed
        // TODO add your handling code here:
        gotoLastPage();
    }//GEN-LAST:event_jButtonGoToLastActionPerformed

    private void jListVwValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListVwValueChanged
        // TODO add your handling code here:
        if (!evt.getValueIsAdjusting()){
            processSelectionChanges();
        }
    }//GEN-LAST:event_jListVwValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonGoToFirst;
    private javax.swing.JButton jButtonGoToLast;
    private javax.swing.JButton jButtonGoToNext;
    private javax.swing.JButton jButtonGoToPrevious;
    private javax.swing.JList<String> jListVw;
    private javax.swing.JScrollPane jScrollPaneVw;
    // End of variables declaration//GEN-END:variables
}
