/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.viewer;

import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupMsgBase;
import com.itm.soupbintcp.bridge.backup.ITMSoupBinTCPBridgeBackupParser;
import com.itm.soupbintcp.bridge.dynamicstruct.ITMSoupBinTCPBridgeDynStructITCH;
import com.itm.soupbintcp.bridge.dynamicstruct.ITMSoupBinTCPBridgeDynStructOUCH;
import com.itm.soupbintcp.bridge.dynamicstruct.ITMSoupBinTCPDynStructBase;
import com.itm.soupbintcp.bridge.dynamicstruct.ITMSoupBinTCPDynStructMsgBase;
import com.itm.soupbintcp.bridge.dynamicstruct.ITMSoupBinTCPDynStructParser;
import static com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty;
import static com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat.splitTwo;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Ari Pambudi
 */
public class ITMSoupBinTCPMessageViewer extends javax.swing.JPanel {
    
    //.callback:
    public interface MessageViewerCallBack {
        public void onButtonNavigationGoToClick(boolean goToFirst, boolean goToPrevious, boolean goToNext, boolean goToLast);
    }
    
    //.register:
    private ITMSoupBinTCPMessageViewer.MessageViewerCallBack callBackObject;
    
    private final static String MODULE_CODE_ITCH = "ITCH";
    private final static String MODULE_CODE_OUCH = "OUCH";
    
    private final static String MODULE_JSON_FILE_NAME_ITCH = "itch_messages.json";
    private final static String MODULE_JSON_FILE_NAME_OUCH = "ouch_messages.json";
    
    private String moduleCode = "";
    private String moduleJSON = "";
    
    private ITMSoupBinTCPDynStructBase moduleDynStructObj;
    private ArrayList<String> moduleDynStructErrorMessages;
    
    //.flags:
    private boolean isViewerOpened = false;
    private boolean isModuleLoaded = false;
    
    public boolean isViewerOpened(){
        return this.isViewerOpened;
    }
    
    public void openViewer(MessageViewerCallBack callBackObject, String moduleCode){
        try{
            closeViewer();
            if (callBackObject != null){
                this.callBackObject = callBackObject;
            }else{
                this.callBackObject = new MessageViewerCallBack() {
                    @Override
                    public void onButtonNavigationGoToClick(boolean goToFirst, boolean goToPrevious, boolean goToNext, boolean goToLast) {}
                };
            }
            this.moduleCode = moduleCode;
            //.muat module json:
            loadModule(this.moduleCode);
            //.beri tanda:
            this.isViewerOpened = true;
            
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
            this.isModuleLoaded = false;
            this.moduleCode = "";
            this.moduleJSON = "";
            this.moduleDynStructObj = null;
            this.moduleDynStructErrorMessages = new ArrayList<>();
            
            //... .
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private boolean loadModule(String moduleCode){
        boolean mOut = false;
        try{
            if ((moduleCode != null) && (!moduleCode.isEmpty())
                && (
                    (MODULE_CODE_ITCH.equalsIgnoreCase(moduleCode)) 
                    || (MODULE_CODE_OUCH.equalsIgnoreCase(moduleCode))
                )
            ){
                String zLocalPath = "./";
                /**********
                if (zLocalPath.isEmpty()){
                    try{
                        File mFile = new File(ITMSoupBinTCPMessageViewer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                        if (mFile.isDirectory()){
                            zLocalPath = mFile.getPath();
                        }
                    }catch(URISyntaxException ex0){
                        zLocalPath = "";
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }catch(Exception ex0){
                        zLocalPath = "";
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }
                }
                if ((zLocalPath == null) || (zLocalPath.isEmpty())){
                    try{
                        File mFile = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
                        if (mFile.isDirectory()){
                            zLocalPath = mFile.getPath();
                        }
                    }catch(Exception ex0){
                        zLocalPath = "";
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }
                }
                **********/
                boolean bCheckModuleFileFound = false;
                String zCheckModuleFileDir = zLocalPath;
                String zCheckModuleFilePath = "";
                String zPathDelimiter = "\\";
                /**********
                if (zCheckModuleFileDir == null){
                    zCheckModuleFileDir = "";
                }
                **********/
                if (zCheckModuleFileDir.contains("/")){
                    zPathDelimiter = "/";
                }
                while(!bCheckModuleFileFound){
                    File mCurrentDir = new File(zCheckModuleFileDir);
                    zCheckModuleFilePath = (zCheckModuleFileDir + (zCheckModuleFileDir.endsWith(zPathDelimiter) ? "" : zPathDelimiter));
                    switch(moduleCode){
                        case MODULE_CODE_ITCH:
                            zCheckModuleFilePath += MODULE_JSON_FILE_NAME_ITCH;
                            break;
                        case MODULE_CODE_OUCH:
                            zCheckModuleFilePath += MODULE_JSON_FILE_NAME_OUCH;
                            break;
                    }
                    File mCurrentFile = new File(zCheckModuleFilePath);
                    if ((mCurrentFile.isFile()) && (mCurrentFile.exists())){
                        try (RandomAccessFile mReader = new RandomAccessFile(mCurrentFile, "r")) {
                            byte[] arrBt = new byte[(int)mReader.length()];
                            mReader.seek(0);
                            mReader.readFully(arrBt);
                            this.moduleJSON = new String(arrBt);
                            if ((this.moduleJSON != null) && (!this.moduleJSON.isEmpty())){
                                
                                switch(moduleCode){
                                    case MODULE_CODE_ITCH:
                                        this.moduleDynStructObj = ITMSoupBinTCPBridgeDynStructITCH.getInstance.loadToDynStruct(this.moduleJSON, this.moduleDynStructErrorMessages);
                                        mOut = (this.moduleDynStructErrorMessages.size() <= 0);
                                        break;
                                    case MODULE_CODE_OUCH:
                                        this.moduleDynStructObj = ITMSoupBinTCPBridgeDynStructOUCH.getInstance.loadToDynStruct(this.moduleJSON, this.moduleDynStructErrorMessages);
                                        mOut = (this.moduleDynStructErrorMessages.size() <= 0);
                                        break;
                                    default:
                                        this.moduleDynStructObj = null;
                                        break;
                                }
                                
                            }
                        }
                        break;
                    }
                    File mParentDir = mCurrentDir.getParentFile();
                    if (mParentDir != null){
                        zCheckModuleFileDir = mParentDir.getPath();
                    }else{
                        break;
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
    
    public void reloadFromBackupLines(List<String> listBackupLines){
        try{
            
            DefaultTreeModel mTreeModel = (DefaultTreeModel)jTree1.getModel();
            DefaultMutableTreeNode mRoot = (DefaultMutableTreeNode) mTreeModel.getRoot();
            mRoot.removeAllChildren();
            
            if ((listBackupLines != null) && (listBackupLines.size() > 0)){
                for (String listBackupLine : listBackupLines) {
                    ITMSoupBinTCPBridgeBackupMsgBase mEachBackupMsg = ITMSoupBinTCPBridgeBackupParser.getInstance.parsePacket(listBackupLine);
                    
                    if (mEachBackupMsg.isHeaderParsed){
                        if ((this.moduleJSON != null) && (!this.moduleJSON.isEmpty())){
                            
                            switch(moduleCode){
                                case MODULE_CODE_ITCH:
                                    /***
                                    ITCHMsgBase itchMessage = ITMSoupBinTCPITCHMsgParser.getInstance.parseMessage(mEachBackupMsg.arbMessage, mEachBackupMsg.sbMessage);
                                    if (itchMessage != null){
                                        //... .
                                    }
                                    ***/
                                    ///ITMITCHMsgMemory.getInstance.mapMessage(mEachBackupMsg.arbMessage, mEachBackupMsg.sbMessage);
                                    ITMSoupBinTCPDynStructMsgBase mDynITCHMsg = ITMSoupBinTCPDynStructParser.getInstance.parseITCHPacket(mEachBackupMsg.arbMessage, mEachBackupMsg.isOutput, (mEachBackupMsg.recordNo > 0), this.moduleDynStructObj);
                                    if (mDynITCHMsg != null){
                                        mEachBackupMsg.dynMessage = mDynITCHMsg;
                                    }
                                    break;
                                case MODULE_CODE_OUCH:
                                    /***
                                    OUCHMsgBase ouchMessage = ITMSoupBinTCPOUCHMsgParser.getInstance.parseMessage(mEachBackupMsg.arbMessage, mEachBackupMsg.sbMessage);
                                    if (ouchMessage != null){
                                        //... .
                                    }
                                    ***/
                                    ITMSoupBinTCPDynStructMsgBase mDynOUCHMsg = ITMSoupBinTCPDynStructParser.getInstance.parseOUCHPacket(mEachBackupMsg.arbMessage, mEachBackupMsg.isOutput, (mEachBackupMsg.recordNo > 0), this.moduleDynStructObj);
                                    if (mDynOUCHMsg != null){
                                        mEachBackupMsg.dynMessage = mDynOUCHMsg;
                                    }
                                    break;
                                default:
                                    //... .
                                    break;
                            }

                        }
                    }
                    
                    ITMSoupBinTCPBridgeBackupMsgBase mMsg = mEachBackupMsg;
                    TreeMsgNode csMsg = new TreeMsgNode(mMsg, false, 0);
                    DefaultMutableTreeNode mMsgNode = new DefaultMutableTreeNode(csMsg);
                    mRoot.add(mMsgNode);
                    if ((mMsg.dynMessage.msg.fields != null) && (mMsg.dynMessage.msg.fields.length > 0)){
                        for(int pf = 0; pf < mMsg.dynMessage.msg.fields.length; pf++){
                            TreeMsgNode csField = new TreeMsgNode(mMsg, true, pf);
                            DefaultMutableTreeNode mFieldNode = new DefaultMutableTreeNode(csField);
                            mMsgNode.add(mFieldNode);
                        }
                    }
                    
                }
            }
            
            mTreeModel.reload();
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void setButtonNavigationState(boolean enableFirst, boolean enablePrevious, boolean enableNext, boolean enableLast){
        try{
            this.jButtonGoToFirst.setEnabled(enableFirst);
            this.jButtonGoToPrevious.setEnabled(enablePrevious);
            this.jButtonGoToNext.setEnabled(enableNext);
            this.jButtonGoToLast.setEnabled(enableLast);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public class TreeMsgNode {
        public ITMSoupBinTCPBridgeBackupMsgBase mMsg;
        public boolean isForField = false;
        public int pFieldIndex = 0;
        
        public TreeMsgNode(ITMSoupBinTCPBridgeBackupMsgBase mMsg, boolean isForField, int pFieldIndex){
            this.mMsg = mMsg;
            this.isForField = isForField;
            this.pFieldIndex = pFieldIndex;
        }
        
        @Override
        public String toString(){
            String mOut = "";
            try{
                if (isForField){
                    if ((mMsg.dynMessage.msg.fields != null) && (mMsg.dynMessage.msg.fields.length > this.pFieldIndex)){
                        mOut = "" + mMsg.dynMessage.msg.fields[this.pFieldIndex].name + " [" + mMsg.dynMessage.msg.fields[this.pFieldIndex].type + "] = " + ( ( (mMsg.dynMessage.msg.fields[this.pFieldIndex].type.equalsIgnoreCase("Integer")) || (mMsg.dynMessage.msg.fields[this.pFieldIndex].type.equalsIgnoreCase("Token")) ) ? mMsg.dynMessage.msg.fields[this.pFieldIndex].msglongval : mMsg.dynMessage.msg.fields[this.pFieldIndex].msgstrval);
                    }else{
                        mOut = "" + "<Field Index Invalid [ " + this.pFieldIndex + " ]>";
                    }
                }else{
                    mOut = "" + mMsg.dynMessage.msg.direction + (mMsg.dynMessage.msg.sequenced ? (" - #" + mMsg.recordNo) : "") + " - (" + mMsg.dynMessage.msg.type + ") " + (mMsg.dynMessage.msg.name);
                }
            }catch(Exception ex0){ 
                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
            }
            return mOut;
        }
        
    }
    /**
     * Creates new form ITMSoupBinTCPMessageViewer
     */
    public ITMSoupBinTCPMessageViewer() {
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
    
    private void doExpandCollapseTree(boolean bExpand) {
        try{
            DefaultTreeModel mTreeModel = (DefaultTreeModel)jTree1.getModel();
            DefaultMutableTreeNode mRoot = (DefaultMutableTreeNode) mTreeModel.getRoot();
            int cChildCount = mRoot.getChildCount();
            if (cChildCount > 0){
                ArrayList<DefaultMutableTreeNode> childList = Collections.list(mRoot.children());
                for (DefaultMutableTreeNode treeNode : childList) {
                    if (!treeNode.isRoot()){
                        if (bExpand){
                            jTree1.expandPath(new TreePath(treeNode.getPath()));
                        }else{
                            jTree1.collapsePath(new TreePath(treeNode.getPath()));
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private boolean doSearchMessageOnTree(String zStringToSearch, boolean bBackWard){
        boolean mOut = false;
        try{
            if (!isNullOrEmpty(zStringToSearch)){
                String[] arrSearchParts = splitTwo(zStringToSearch, "|");
                String zSearchAtMsg = (arrSearchParts.length > 1 ? arrSearchParts[0] : arrSearchParts[0]);
                String zSearchAtField = (arrSearchParts.length > 1 ? arrSearchParts[1] : arrSearchParts[0]);
                if (!isNullOrEmpty(zSearchAtMsg)){ zSearchAtMsg = zSearchAtMsg.toLowerCase(); }
                if (!isNullOrEmpty(zSearchAtField)){ zSearchAtField = zSearchAtField.toLowerCase(); }
                DefaultTreeModel mTreeModel = (DefaultTreeModel)jTree1.getModel();
                DefaultMutableTreeNode mRoot = (DefaultMutableTreeNode) mTreeModel.getRoot();
                DefaultMutableTreeNode mFirstMsgSelected = null;
                if (jTree1.getSelectionPath() != null){
                    mFirstMsgSelected = (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
                    if (mFirstMsgSelected != null && mFirstMsgSelected.isLeaf()){
                        mFirstMsgSelected = (DefaultMutableTreeNode)mFirstMsgSelected.getParent();
                        if (mFirstMsgSelected.isRoot()){
                            mFirstMsgSelected = null;
                        }else{
                            mFirstMsgSelected = (bBackWard ? mFirstMsgSelected.getPreviousSibling() : mFirstMsgSelected.getNextSibling());
                        }
                    }else if (mFirstMsgSelected != null && !mFirstMsgSelected.isLeaf()){
                        if (mFirstMsgSelected.isRoot()){
                            mFirstMsgSelected = null;
                        }else{
                            mFirstMsgSelected = (bBackWard ? mFirstMsgSelected.getPreviousSibling() : mFirstMsgSelected.getNextSibling());
                        }
                    }
                }
                int cMsgCount = mRoot.getChildCount();
                if (cMsgCount > 0){
                    boolean bFlagCanSearch = false;
                    boolean bFlagNodeFound = false;
                    ArrayList<DefaultMutableTreeNode> msgList = Collections.list(mRoot.children());
                    if (bBackWard){
                        Collections.reverse(msgList);
                    }
                    for (DefaultMutableTreeNode msgNode : msgList) {
                        bFlagNodeFound = false;
                        if (!msgNode.isRoot()){
                            if (!bFlagCanSearch){
                                if (mFirstMsgSelected == null){
                                    bFlagCanSearch = true;
                                }else if (msgNode == mFirstMsgSelected){
                                    bFlagCanSearch = true;
                                }
                            }
                            if (bFlagCanSearch){
                                String zMsgNodeText = msgNode.toString();
                                if (!isNullOrEmpty(zMsgNodeText)){
                                    zMsgNodeText = zMsgNodeText.toLowerCase();
                                    ///System.out.println("@" + zMsgNodeText);
                                    if ((!isNullOrEmpty(zSearchAtMsg)) && (zMsgNodeText.contains(zSearchAtMsg))){
                                        bFlagNodeFound = true;
                                    } else if ((!isNullOrEmpty(zSearchAtField)) && (zMsgNodeText.contains(zSearchAtField))){
                                        bFlagNodeFound = true;
                                    }
                                }
                                ArrayList<DefaultMutableTreeNode> fieldList = Collections.list(msgNode.children());
                                if (fieldList.size() > 0){
                                    for (DefaultMutableTreeNode fieldNode : fieldList) {
                                        if (!fieldNode.isRoot()){
                                            String zfieldNodeText = fieldNode.toString();
                                            if (!isNullOrEmpty(zfieldNodeText)){
                                                zfieldNodeText = zfieldNodeText.toLowerCase();
                                                ///System.out.println(">" + zfieldNodeText);
                                                if ((!isNullOrEmpty(zSearchAtMsg)) && (zfieldNodeText.contains(zSearchAtMsg))){
                                                    bFlagNodeFound = true;
                                                } else if ((!isNullOrEmpty(zSearchAtField)) && (zfieldNodeText.contains(zSearchAtField))){
                                                    bFlagNodeFound = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (bFlagNodeFound){
                                    TreePath mMsgPath = new TreePath(msgNode.getPath());
                                    jTree1.expandPath(mMsgPath);
                                    jTree1.scrollPathToVisible(mMsgPath);
                                    jTree1.setSelectionPath(mMsgPath);
                                    mOut = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jButtonDoCollapse = new javax.swing.JButton();
        jButtonDoExpand = new javax.swing.JButton();
        jButtonGoToFirst = new javax.swing.JButton();
        jButtonGoToPrevious = new javax.swing.JButton();
        jButtonGoToNext = new javax.swing.JButton();
        jButtonGoToLast = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();

        jTree1.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
        jTree1.setLargeModel(true);
        jTree1.setRootVisible(false);
        jTree1.setScrollsOnExpand(false);
        jScrollPane1.setViewportView(jTree1);

        jButtonDoCollapse.setText("Collapse");
        jButtonDoCollapse.setToolTipText("collapse message(s)");
        jButtonDoCollapse.setMaximumSize(new java.awt.Dimension(85, 27));
        jButtonDoCollapse.setMinimumSize(new java.awt.Dimension(85, 27));
        jButtonDoCollapse.setPreferredSize(new java.awt.Dimension(85, 27));
        jButtonDoCollapse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDoCollapseActionPerformed(evt);
            }
        });

        jButtonDoExpand.setText("Expand");
        jButtonDoExpand.setToolTipText("expand message(s)");
        jButtonDoExpand.setMaximumSize(new java.awt.Dimension(85, 27));
        jButtonDoExpand.setMinimumSize(new java.awt.Dimension(85, 27));
        jButtonDoExpand.setPreferredSize(new java.awt.Dimension(85, 27));
        jButtonDoExpand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDoExpandActionPerformed(evt);
            }
        });

        jButtonGoToFirst.setText("First");
        jButtonGoToFirst.setToolTipText("");
        jButtonGoToFirst.setEnabled(false);
        jButtonGoToFirst.setMaximumSize(new java.awt.Dimension(85, 27));
        jButtonGoToFirst.setMinimumSize(new java.awt.Dimension(85, 27));
        jButtonGoToFirst.setPreferredSize(new java.awt.Dimension(85, 27));
        jButtonGoToFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToFirstActionPerformed(evt);
            }
        });

        jButtonGoToPrevious.setText("Previous");
        jButtonGoToPrevious.setToolTipText("");
        jButtonGoToPrevious.setEnabled(false);
        jButtonGoToPrevious.setMaximumSize(new java.awt.Dimension(85, 27));
        jButtonGoToPrevious.setMinimumSize(new java.awt.Dimension(85, 27));
        jButtonGoToPrevious.setPreferredSize(new java.awt.Dimension(85, 27));
        jButtonGoToPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToPreviousActionPerformed(evt);
            }
        });

        jButtonGoToNext.setText("Next");
        jButtonGoToNext.setToolTipText("");
        jButtonGoToNext.setEnabled(false);
        jButtonGoToNext.setMaximumSize(new java.awt.Dimension(85, 27));
        jButtonGoToNext.setMinimumSize(new java.awt.Dimension(85, 27));
        jButtonGoToNext.setPreferredSize(new java.awt.Dimension(85, 27));
        jButtonGoToNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToNextActionPerformed(evt);
            }
        });

        jButtonGoToLast.setText("Last");
        jButtonGoToLast.setToolTipText("");
        jButtonGoToLast.setEnabled(false);
        jButtonGoToLast.setMaximumSize(new java.awt.Dimension(85, 27));
        jButtonGoToLast.setMinimumSize(new java.awt.Dimension(85, 27));
        jButtonGoToLast.setPreferredSize(new java.awt.Dimension(85, 27));
        jButtonGoToLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoToLastActionPerformed(evt);
            }
        });

        jTextFieldSearch.setToolTipText("search message(s). separate message and field with pipeline \"|\"");
        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButtonDoCollapse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDoExpand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jButtonGoToFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGoToPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGoToNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGoToLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jTextFieldSearch)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonDoCollapse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGoToFirst, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGoToPrevious, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGoToNext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGoToLast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDoExpand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDoCollapseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDoCollapseActionPerformed
        // TODO add your handling code here:
        doExpandCollapseTree(false);
    }//GEN-LAST:event_jButtonDoCollapseActionPerformed

    private void jButtonDoExpandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDoExpandActionPerformed
        // TODO add your handling code here:
        doExpandCollapseTree(true);
    }//GEN-LAST:event_jButtonDoExpandActionPerformed

    private void jButtonGoToLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToLastActionPerformed
        // TODO add your handling code here:
        callBackObject.onButtonNavigationGoToClick(false, false, false, true);
    }//GEN-LAST:event_jButtonGoToLastActionPerformed

    private void jButtonGoToNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToNextActionPerformed
        // TODO add your handling code here:
        callBackObject.onButtonNavigationGoToClick(false, false, true, false);
    }//GEN-LAST:event_jButtonGoToNextActionPerformed

    private void jButtonGoToPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToPreviousActionPerformed
        // TODO add your handling code here:
        callBackObject.onButtonNavigationGoToClick(false, true, false, false);
    }//GEN-LAST:event_jButtonGoToPreviousActionPerformed

    private void jButtonGoToFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGoToFirstActionPerformed
        // TODO add your handling code here:
        callBackObject.onButtonNavigationGoToClick(true, false, false, false);
    }//GEN-LAST:event_jButtonGoToFirstActionPerformed

    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
        // TODO add your handling code here:
        if(!StringHelper.isNullOrEmpty(evt.getActionCommand())){
            if (!doSearchMessageOnTree(evt.getActionCommand(), false)){
                if (jButtonGoToLast.isEnabled()){
                    callBackObject.onButtonNavigationGoToClick(false, false, true, false);
                }
            }
        }
    }//GEN-LAST:event_jTextFieldSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDoCollapse;
    private javax.swing.JButton jButtonDoExpand;
    private javax.swing.JButton jButtonGoToFirst;
    private javax.swing.JButton jButtonGoToLast;
    private javax.swing.JButton jButtonGoToNext;
    private javax.swing.JButton jButtonGoToPrevious;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
