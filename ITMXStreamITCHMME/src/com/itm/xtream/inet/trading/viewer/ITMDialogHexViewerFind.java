/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.viewer;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author Ari Pambudi
 */
public class ITMDialogHexViewerFind extends javax.swing.JFrame {
    
    private Thread mFindWorker;
    
    private boolean isFindRunning = false;
    
    private ITMSoupBinTCPHexViewer hexViewer;
    
    public void openDialog(JComponent mParent, ITMSoupBinTCPHexViewer hexViewer){
        try{
            this.hexViewer = hexViewer;
            //.tampilkan:
            this.setVisible(true);
            verifyControlsState();
            jTextFieldVwInputText.requestFocus();
            //.center parent:
            ITMComponentLayoutHelper.centerParent(ITMComponentLayoutHelper.getParentFrame(mParent), this);
        }catch(SecurityException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void verifyControlsState(){
        try{
            jSpinnerVwLimitBytesValue.setEnabled(jCheckBoxVwLimitBytes.isSelected());
            if (this.isFindRunning){
                jTextFieldVwInputText.setEnabled(false);
                jCheckBoxVwUseHexadecimal.setEnabled(false);
                jCheckBoxVwSearchFromBeginning.setEnabled(false);
                jCheckBoxVwLimitBytes.setEnabled(false);
                jSpinnerVwLimitBytesValue.setEnabled(false);
                jButtonVwFindNext.setEnabled(false);
                jButtonVwFindPrevious.setEnabled(false);
                jButtonVwFindCancel.setEnabled(true);
                jButtonVwFindCancel.requestFocus();
            }else{
                jTextFieldVwInputText.setEnabled(true);
                jCheckBoxVwUseHexadecimal.setEnabled(true);
                jCheckBoxVwSearchFromBeginning.setEnabled(true);
                jCheckBoxVwLimitBytes.setEnabled(true);
                jButtonVwFindNext.setEnabled(true);
                jButtonVwFindPrevious.setEnabled(true);
                jButtonVwFindCancel.setEnabled(false);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void endFind(){
        try{
            if (this.hexViewer != null){
                this.hexViewer.stopFind();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void beginFind(boolean isSearchPrevious){
        try{
            final Component fnlThis = this;
            final boolean fnlIsSearchPrevious = isSearchPrevious;
            try{
                if (this.mFindWorker != null){
                    this.mFindWorker.interrupt();
                    this.mFindWorker = null;
                }
            }catch(Exception ex0){
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            if (this.mFindWorker == null){
                this.mFindWorker = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try{
                                isFindRunning = true;
                                verifyControlsState();
                            }catch(Exception ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }
                            try{
                                if (hexViewer != null){
                                    String zMsg = hexViewer.startFind(
                                            jTextFieldVwInputText.getText()
                                            , jCheckBoxVwUseHexadecimal.isSelected()
                                            , jCheckBoxVwSearchFromBeginning.isSelected()
                                            , jCheckBoxVwLimitBytes.isSelected()
                                            , (long)jSpinnerVwLimitBytesValue.getValue()
                                            , fnlIsSearchPrevious
                                            , true
                                    );
                                    if ((zMsg != null) && (!zMsg.isEmpty())){
                                        JOptionPane.showMessageDialog(fnlThis, zMsg, "Info", JOptionPane.INFORMATION_MESSAGE);
                                    }else{
                                        jCheckBoxVwSearchFromBeginning.setSelected(false);
                                    }
                                }
                            }catch(HeadlessException ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }catch(Exception ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }
                            try{
                                isFindRunning = false;
                                verifyControlsState();
                            }catch(Exception ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }
                        }
                    }
                );
            }
            this.mFindWorker.start();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    /**
     * Creates new form ITMDialogHexViewerFind
     */
    public ITMDialogHexViewerFind() {
        //.inisialisasi internal form:
        initComponents();
        //.inisialisasi eksternal form:
        initComponentsEx();
    }

    private void initComponentsEx() {
        try{
            //.listener:
            
            //.pasang title di atas program:
            this.setTitle("Find");
            //.perbaikan:
            
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

        jLabelVwTitleInput = new javax.swing.JLabel();
        jTextFieldVwInputText = new javax.swing.JTextField();
        jCheckBoxVwUseHexadecimal = new javax.swing.JCheckBox();
        jCheckBoxVwSearchFromBeginning = new javax.swing.JCheckBox();
        jCheckBoxVwLimitBytes = new javax.swing.JCheckBox();
        jLabelVwLimitBytesSuffix = new javax.swing.JLabel();
        jSpinnerVwLimitBytesValue = new javax.swing.JSpinner();
        jButtonVwFindPrevious = new javax.swing.JButton();
        jButtonVwFindNext = new javax.swing.JButton();
        jButtonVwFindCancel = new javax.swing.JButton();

        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(350, 220));
        setPreferredSize(new java.awt.Dimension(380, 240));
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelVwTitleInput.setText("Find What : ");

        jTextFieldVwInputText.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTextFieldVwInputText.setNextFocusableComponent(jCheckBoxVwUseHexadecimal);
        jTextFieldVwInputText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldVwInputTextActionPerformed(evt);
            }
        });
        jTextFieldVwInputText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldVwInputTextKeyPressed(evt);
            }
        });

        jCheckBoxVwUseHexadecimal.setText("Hexa Decimal");
        jCheckBoxVwUseHexadecimal.setNextFocusableComponent(jCheckBoxVwSearchFromBeginning);
        jCheckBoxVwUseHexadecimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxVwUseHexadecimalActionPerformed(evt);
            }
        });

        jCheckBoxVwSearchFromBeginning.setText("Search From Beginning");
        jCheckBoxVwSearchFromBeginning.setNextFocusableComponent(jCheckBoxVwLimitBytes);
        jCheckBoxVwSearchFromBeginning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxVwSearchFromBeginningActionPerformed(evt);
            }
        });

        jCheckBoxVwLimitBytes.setText("Limit ");
        jCheckBoxVwLimitBytes.setNextFocusableComponent(jSpinnerVwLimitBytesValue);
        jCheckBoxVwLimitBytes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxVwLimitBytesActionPerformed(evt);
            }
        });

        jLabelVwLimitBytesSuffix.setText("Bytes");

        jSpinnerVwLimitBytesValue.setModel(new javax.swing.SpinnerNumberModel(1000L, 1L, null, 1L));
        jSpinnerVwLimitBytesValue.setEnabled(false);
        jSpinnerVwLimitBytesValue.setNextFocusableComponent(jButtonVwFindNext);

        jButtonVwFindPrevious.setText("Find Previous");
        jButtonVwFindPrevious.setNextFocusableComponent(jTextFieldVwInputText);
        jButtonVwFindPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVwFindPreviousActionPerformed(evt);
            }
        });

        jButtonVwFindNext.setText("Find Next");
        jButtonVwFindNext.setNextFocusableComponent(jButtonVwFindPrevious);
        jButtonVwFindNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVwFindNextActionPerformed(evt);
            }
        });

        jButtonVwFindCancel.setText("Cancel");
        jButtonVwFindCancel.setEnabled(false);
        jButtonVwFindCancel.setNextFocusableComponent(jTextFieldVwInputText);
        jButtonVwFindCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVwFindCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonVwFindCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVwFindPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVwFindNext, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelVwTitleInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldVwInputText, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jCheckBoxVwSearchFromBeginning, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(jCheckBoxVwUseHexadecimal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jCheckBoxVwLimitBytes)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jSpinnerVwLimitBytesValue)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelVwLimitBytesSuffix)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelVwTitleInput)
                    .addComponent(jTextFieldVwInputText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxVwUseHexadecimal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxVwSearchFromBeginning)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxVwLimitBytes)
                    .addComponent(jSpinnerVwLimitBytesValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelVwLimitBytesSuffix))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVwFindNext)
                    .addComponent(jButtonVwFindPrevious)
                    .addComponent(jButtonVwFindCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldVwInputTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldVwInputTextActionPerformed
        // TODO add your handling code here:
        verifyControlsState();
    }//GEN-LAST:event_jTextFieldVwInputTextActionPerformed

    private void jButtonVwFindPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVwFindPreviousActionPerformed
        // TODO add your handling code here:
        verifyControlsState();
        beginFind(true);
    }//GEN-LAST:event_jButtonVwFindPreviousActionPerformed

    private void jButtonVwFindNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVwFindNextActionPerformed
        // TODO add your handling code here:
        verifyControlsState();
        beginFind(false);
    }//GEN-LAST:event_jButtonVwFindNextActionPerformed

    private void jCheckBoxVwUseHexadecimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxVwUseHexadecimalActionPerformed
        // TODO add your handling code here:
        verifyControlsState();
    }//GEN-LAST:event_jCheckBoxVwUseHexadecimalActionPerformed

    private void jCheckBoxVwSearchFromBeginningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxVwSearchFromBeginningActionPerformed
        // TODO add your handling code here:
        verifyControlsState();
    }//GEN-LAST:event_jCheckBoxVwSearchFromBeginningActionPerformed

    private void jCheckBoxVwLimitBytesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxVwLimitBytesActionPerformed
        // TODO add your handling code here:
        verifyControlsState();
    }//GEN-LAST:event_jCheckBoxVwLimitBytesActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        endFind();
    }//GEN-LAST:event_formWindowClosing

    private void jButtonVwFindCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVwFindCancelActionPerformed
        // TODO add your handling code here:
        endFind();
    }//GEN-LAST:event_jButtonVwFindCancelActionPerformed

    private void jTextFieldVwInputTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldVwInputTextKeyPressed
        // TODO add your handling code here:
        try{
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) && (!evt.isActionKey()) && (!evt.isAltDown()) && (!evt.isControlDown())){
                beginFind(evt.isShiftDown());
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_jTextFieldVwInputTextKeyPressed
//////
//////    /**
//////     * @param args the command line arguments
//////     */
//////    public static void main(String args[]) {
//////        /* Set the Nimbus look and feel */
//////        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//////        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//////         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//////         */
//////        try {
//////            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//////                if ("Nimbus".equals(info.getName())) {
//////                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//////                    break;
//////                }
//////            }
//////        } catch (ClassNotFoundException ex) {
//////            java.util.logging.Logger.getLogger(ITMDialogHexViewerFind.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (InstantiationException ex) {
//////            java.util.logging.Logger.getLogger(ITMDialogHexViewerFind.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (IllegalAccessException ex) {
//////            java.util.logging.Logger.getLogger(ITMDialogHexViewerFind.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//////            java.util.logging.Logger.getLogger(ITMDialogHexViewerFind.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        }
//////        //</editor-fold>
//////
//////        /* Create and display the form */
//////        java.awt.EventQueue.invokeLater(new Runnable() {
//////            public void run() {
//////                new ITMDialogHexViewerFind().setVisible(true);
//////            }
//////        });
//////    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonVwFindCancel;
    private javax.swing.JButton jButtonVwFindNext;
    private javax.swing.JButton jButtonVwFindPrevious;
    private javax.swing.JCheckBox jCheckBoxVwLimitBytes;
    private javax.swing.JCheckBox jCheckBoxVwSearchFromBeginning;
    private javax.swing.JCheckBox jCheckBoxVwUseHexadecimal;
    private javax.swing.JLabel jLabelVwLimitBytesSuffix;
    private javax.swing.JLabel jLabelVwTitleInput;
    private javax.swing.JSpinner jSpinnerVwLimitBytesValue;
    private javax.swing.JTextField jTextFieldVwInputText;
    // End of variables declaration//GEN-END:variables
}
