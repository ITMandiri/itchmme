/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.viewer;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupConsts;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.List;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPViewFrame extends javax.swing.JFrame {
    
    //timer check recent files:
    private Timer timerCheckRecentFiles;
    
    private String zLastSourceFilePath = "";
    
    RandomAccessFile mFileInput;
    
    private long vFileLastReadSize = 0;
    private long vFileLastReadOffset = 0;
    private int vFileLastReadLength = 0;
    
    /**
     * Creates new form ITMSoupBinTCPViewFrame
     */
    public ITMSoupBinTCPViewFrame() {
        //.inisialisasi internal form:
        initComponents();
        //.inisialisasi eksternal form:
        initComponentsEx();
        //.letakkan form di tengah layar:
        centerThisControlPanel();
    }

    private void initComponentsEx() {
        try{
            //.listener:
            
            //.pasang title di atas program:
            this.setTitle("SoupBinTCP Message Viewer");
            //.perbaikan:
            verifyControlsState();
            refreshRecentFilesList(false);
            //... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void centerThisControlPanel() {
        try{
            ITMComponentLayoutHelper.centerScreen(this);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void showThis(){
        try{
            setVisible(true);
            if (getState() == Frame.ICONIFIED){
                setState(Frame.NORMAL);
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void hideThis(){
        try{
            setVisible(false);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void verifyControlsState(){
        try{
            jButtonSourceFileBrowse.setEnabled(!iTMSoupBinTCPLineViewer1.isViewerOpened());
            jButtonSourceFileOpen.setEnabled((!iTMSoupBinTCPLineViewer1.isViewerOpened()) && (zLastSourceFilePath != null) && (!zLastSourceFilePath.isEmpty()));
            jButtonSourceFileClose.setEnabled(iTMSoupBinTCPLineViewer1.isViewerOpened());
            jComboBoxModuleChooser.setEnabled(!iTMSoupBinTCPLineViewer1.isViewerOpened());
            jListRecentFiles.setEnabled(!iTMSoupBinTCPLineViewer1.isViewerOpened());
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void refreshRecentFilesList(boolean bForcedNow){
        try{
            if (bForcedNow){
                if (!iTMSoupBinTCPLineViewer1.isViewerOpened()){
                    long msdtNow = (new Date()).getTime();
                    DefaultListModel mdlList = (DefaultListModel) jListRecentFiles.getModel();
                    mdlList.clear();
                    File fDir = new File(ITMSoupBinTCPBridgeBackupConsts.ALTER_SOUPBINTCP_BACKUP_FILE_DIRECTORY);
                    if (fDir.isDirectory()){
                        String[] arrFiles = fDir.list();
                        if (arrFiles.length > 0){
                            for(String zEachFileName : arrFiles){
                                if (zEachFileName.toLowerCase().endsWith(ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_FILE_EXTENSION)
                                    || zEachFileName.toLowerCase().endsWith(ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_OLD_FILE_EXTENSION)
                                ){
                                    File fFile = new File(fDir, zEachFileName);
                                    if ((fFile.isFile()) && ((fFile.lastModified() >= msdtNow) || ((msdtNow - fFile.lastModified()) <= (3 * 24 * 60 * 60 * 1000)))
                                    ){
                                        mdlList.addElement(fFile.getCanonicalPath());
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                refreshRecentFilesList(true);
                if (this.timerCheckRecentFiles == null){
                    this.timerCheckRecentFiles = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try{
                                refreshRecentFilesList(true);
                            }catch(Exception ex0){
                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                            }
                        }
                    });
                }
                this.timerCheckRecentFiles.stop();
                this.timerCheckRecentFiles.start();
            }
        }catch(IOException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void openFile(String zFilePath){
        try{
            closeFile();
            this.mFileInput = new RandomAccessFile(zFilePath, "r");
            this.mFileInput.seek(0);
        }catch(IOException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void closeFile(){
        try{
            if (this.mFileInput != null){
                this.mFileInput.close();
                this.mFileInput = null;
            }
        }catch(IOException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private byte[] readFile(long offset, int length){
        byte[] mOut = new byte[]{};
        try{
            this.vFileLastReadSize = 0;
            this.vFileLastReadOffset = offset;
            this.vFileLastReadLength = length;
            if (this.vFileLastReadLength > 0){
                if (this.mFileInput != null){
                    this.vFileLastReadSize = this.mFileInput.length();
                    if (this.vFileLastReadSize > 0){
                        if (this.vFileLastReadOffset >= this.vFileLastReadSize){
                            this.vFileLastReadOffset = (this.vFileLastReadSize - 1);
                        }
                        if ((this.vFileLastReadOffset + this.vFileLastReadLength) > this.vFileLastReadSize){
                            this.vFileLastReadLength = (int) (this.vFileLastReadSize - this.vFileLastReadOffset);
                        }
                        mOut = new byte[this.vFileLastReadLength];
                        this.mFileInput.seek(this.vFileLastReadOffset);
                        this.mFileInput.readFully(mOut, 0, this.vFileLastReadLength);
                    }
                }
            }
        }catch(IOException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        return mOut;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneBase1 = new javax.swing.JTabbedPane();
        jScrollPaneA = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabelTitleModule = new javax.swing.JLabel();
        jComboBoxModuleChooser = new javax.swing.JComboBox<>();
        jLabelTitleFilePath = new javax.swing.JLabel();
        jTextFieldSourceFilePath = new javax.swing.JTextField();
        jButtonSourceFileBrowse = new javax.swing.JButton();
        jButtonSourceFileOpen = new javax.swing.JButton();
        jButtonSourceFileClose = new javax.swing.JButton();
        jScrollPaneRecentFiles = new javax.swing.JScrollPane();
        jListRecentFiles = new javax.swing.JList<>();
        iTMSoupBinTCPLineViewer1 = new com.itm.xtream.inet.trading.viewer.ITMSoupBinTCPLineViewer();
        iTMSoupBinTCPMessageViewer1 = new com.itm.xtream.inet.trading.viewer.ITMSoupBinTCPMessageViewer();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(850, 350));
        setPreferredSize(new java.awt.Dimension(900, 400));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 300));

        jLabelTitleModule.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTitleModule.setText("Module : ");

        jComboBoxModuleChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NONE", "ITCH", "MDF" }));

        jLabelTitleFilePath.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTitleFilePath.setText("File Path : ");

        jTextFieldSourceFilePath.setEditable(false);

        jButtonSourceFileBrowse.setText("Browse...");
        jButtonSourceFileBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSourceFileBrowseActionPerformed(evt);
            }
        });

        jButtonSourceFileOpen.setText("Open");
        jButtonSourceFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSourceFileOpenActionPerformed(evt);
            }
        });

        jButtonSourceFileClose.setText("Close");
        jButtonSourceFileClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSourceFileCloseActionPerformed(evt);
            }
        });

        jListRecentFiles.setModel(new DefaultListModel());
        jListRecentFiles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListRecentFiles.setToolTipText("double click file to open");
        jListRecentFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListRecentFilesMouseClicked(evt);
            }
        });
        jScrollPaneRecentFiles.setViewportView(jListRecentFiles);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneRecentFiles)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelTitleModule)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxModuleChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTitleFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSourceFilePath, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSourceFileBrowse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSourceFileOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSourceFileClose, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSourceFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTitleFilePath)
                    .addComponent(jButtonSourceFileBrowse)
                    .addComponent(jButtonSourceFileOpen)
                    .addComponent(jButtonSourceFileClose)
                    .addComponent(jLabelTitleModule)
                    .addComponent(jComboBoxModuleChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneRecentFiles, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPaneA.setViewportView(jPanel1);

        jTabbedPaneBase1.addTab("Source", jScrollPaneA);
        jTabbedPaneBase1.addTab("Lines", iTMSoupBinTCPLineViewer1);
        jTabbedPaneBase1.addTab("Message", iTMSoupBinTCPMessageViewer1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneBase1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneBase1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        System.out.println("formWindowClosing=" + evt.paramString());
        
        
        
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        System.out.println("formWindowClosed=" + evt.paramString());
        
        
        
    }//GEN-LAST:event_formWindowClosed

    private void jButtonSourceFileBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSourceFileBrowseActionPerformed
        // TODO add your handling code here:
        try{
            if (!iTMSoupBinTCPLineViewer1.isViewerOpened()){
                String zLastDir = ITMSoupBinTCPBridgeBackupConsts.ALTER_SOUPBINTCP_BACKUP_FILE_DIRECTORY;
                if ((zLastSourceFilePath != null) && (!zLastSourceFilePath.isEmpty())){
                    try{
                        File fCurFile = new File(zLastSourceFilePath);
                        if ((fCurFile.getParent() != null) && (!fCurFile.getParent().isEmpty())){
                            zLastDir = fCurFile.getParent();
                        }
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }
                }
                JFileChooser mfc = new JFileChooser();
                mfc.setCurrentDirectory(new File(zLastDir));
                try{
                    Action mAct = mfc.getActionMap().get("viewTypeDetails");
                    mAct.actionPerformed(null);
                }catch(Exception ex0){
                    ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                }
                int fcRet = mfc.showOpenDialog(this);
                if (fcRet == JFileChooser.APPROVE_OPTION){
                    File mFile = mfc.getSelectedFile();
                    if (mFile.exists()){
                        zLastSourceFilePath = mFile.getCanonicalPath();
                        jTextFieldSourceFilePath.setText(zLastSourceFilePath);
                        //.tentukan selected module type di module chooser:
                        jComboBoxModuleChooser.setSelectedItem("NONE");
                        for(int pItem = 0; pItem < jComboBoxModuleChooser.getItemCount(); pItem++){
                            if ((zLastSourceFilePath.toLowerCase().endsWith(ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_FILE_EXTENSION) || zLastSourceFilePath.toLowerCase().endsWith(ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_OLD_FILE_EXTENSION)) 
                                && (zLastSourceFilePath.toUpperCase().indexOf(jComboBoxModuleChooser.getItemAt(pItem)) > 0)){
                                jComboBoxModuleChooser.setSelectedItem(jComboBoxModuleChooser.getItemAt(pItem));
                                break;
                            }
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(this, "Please Close Previous File", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            verifyControlsState();
        }catch(HeadlessException | IOException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_jButtonSourceFileBrowseActionPerformed

    private void jButtonSourceFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSourceFileOpenActionPerformed
        // TODO add your handling code here:
        try{
            if (!iTMSoupBinTCPLineViewer1.isViewerOpened()){
                iTMSoupBinTCPLineViewer1.openViewer(new ITMSoupBinTCPLineViewer.LineViewerCallBack() {
                    @Override
                    public void onRequestData(long offset, long length) {
                        openFile(zLastSourceFilePath);
                        byte[] arrData = readFile(offset, (int)length);
                        closeFile();
                        iTMSoupBinTCPLineViewer1.respondData(vFileLastReadSize, vFileLastReadOffset, vFileLastReadLength, arrData);
                    }

                    @Override
                    public void onBackupLinesChanges(List<String> listBackupLines) {
                        iTMSoupBinTCPMessageViewer1.reloadFromBackupLines(listBackupLines);
                    }
                    
                    @Override
                    public void onButtonNavigationStateChanges(boolean enableFirst, boolean enablePrevious, boolean enableNext, boolean enableLast) {
                        iTMSoupBinTCPMessageViewer1.setButtonNavigationState(enableFirst,enablePrevious,enableNext,enableLast);
                    }
                    
                });
                iTMSoupBinTCPMessageViewer1.openViewer(new ITMSoupBinTCPMessageViewer.MessageViewerCallBack() {
                    @Override
                    public void onButtonNavigationGoToClick(boolean goToFirst, boolean goToPrevious, boolean goToNext, boolean goToLast) {
                        iTMSoupBinTCPLineViewer1.doButtonNavigationGoToClick(goToFirst, goToPrevious, goToNext, goToLast);
                    }
                    
                }, (String) jComboBoxModuleChooser.getSelectedItem());
                jTabbedPaneBase1.setSelectedIndex(1); //.langsung arahkan ke tab lineviewer;
            }
            verifyControlsState();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_jButtonSourceFileOpenActionPerformed
    
    private void jButtonSourceFileCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSourceFileCloseActionPerformed
        // TODO add your handling code here:
        try{
            if (iTMSoupBinTCPLineViewer1.isViewerOpened()){
                iTMSoupBinTCPLineViewer1.closeViewer();
            }
            if (iTMSoupBinTCPMessageViewer1.isViewerOpened()){
                iTMSoupBinTCPMessageViewer1.closeViewer();
            }
            verifyControlsState();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_jButtonSourceFileCloseActionPerformed

    private void jListRecentFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListRecentFilesMouseClicked
        // TODO add your handling code here:
        try{
            if (!iTMSoupBinTCPLineViewer1.isViewerOpened()){
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1){
                    int idx = list.locationToIndex(evt.getPoint());
                    if (idx >= 0){
                        String zFilePath = jListRecentFiles.getSelectedValue();
                        File mFile = new File(zFilePath);
                        if (mFile.exists()){
                            zLastSourceFilePath = mFile.getCanonicalPath();
                            jTextFieldSourceFilePath.setText(zLastSourceFilePath);
                            //.tentukan selected module type di module chooser:
                            jComboBoxModuleChooser.setSelectedItem("NONE");
                            for(int pItem = 0; pItem < jComboBoxModuleChooser.getItemCount(); pItem++){
                                if ((zLastSourceFilePath.toLowerCase().endsWith(ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_FILE_EXTENSION) || zLastSourceFilePath.toLowerCase().endsWith(ITMSoupBinTCPBridgeBackupConsts.DEFAULT_SOUPBINTCP_BACKUP_OLD_FILE_EXTENSION)) 
                                    && (zLastSourceFilePath.toUpperCase().indexOf(jComboBoxModuleChooser.getItemAt(pItem)) > 0)){
                                    jComboBoxModuleChooser.setSelectedItem(jComboBoxModuleChooser.getItemAt(pItem));
                                    break;
                                }
                            }
                            verifyControlsState();
                            //.click:
                            jButtonSourceFileOpen.doClick();
                        }
                    }
                }
            }
        }catch(IOException ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_jListRecentFilesMouseClicked
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
//////            java.util.logging.Logger.getLogger(ITMSoupBinTCPViewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (InstantiationException ex) {
//////            java.util.logging.Logger.getLogger(ITMSoupBinTCPViewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (IllegalAccessException ex) {
//////            java.util.logging.Logger.getLogger(ITMSoupBinTCPViewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//////            java.util.logging.Logger.getLogger(ITMSoupBinTCPViewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        }
//////        //</editor-fold>
//////
//////        /* Create and display the form */
//////        java.awt.EventQueue.invokeLater(new Runnable() {
//////            public void run() {
//////                new ITMSoupBinTCPViewFrame().setVisible(true);
//////            }
//////        });
//////    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.itm.xtream.inet.trading.viewer.ITMSoupBinTCPLineViewer iTMSoupBinTCPLineViewer1;
    private com.itm.xtream.inet.trading.viewer.ITMSoupBinTCPMessageViewer iTMSoupBinTCPMessageViewer1;
    private javax.swing.JButton jButtonSourceFileBrowse;
    private javax.swing.JButton jButtonSourceFileClose;
    private javax.swing.JButton jButtonSourceFileOpen;
    private javax.swing.JComboBox<String> jComboBoxModuleChooser;
    private javax.swing.JLabel jLabelTitleFilePath;
    private javax.swing.JLabel jLabelTitleModule;
    private javax.swing.JList<String> jListRecentFiles;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPaneA;
    private javax.swing.JScrollPane jScrollPaneRecentFiles;
    private javax.swing.JTabbedPane jTabbedPaneBase1;
    private javax.swing.JTextField jTextFieldSourceFilePath;
    // End of variables declaration//GEN-END:variables
}
