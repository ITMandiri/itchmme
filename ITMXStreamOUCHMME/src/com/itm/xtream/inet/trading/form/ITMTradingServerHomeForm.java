/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.form;

import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController.FIX5IDXBridgeStatus;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.AppClientCode;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketController;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketController.OUCHBridgeStatus;
import com.itm.ts.ouch.callback.ITMSoupBinTCPOUCHPacketMgr;
import com.itm.xtream.inet.trading.consts.ITMTradingServerConsts;
import com.itm.xtream.inet.trading.consts.ITMTradingServerConsts.DispTextSetup;
import com.itm.xtream.inet.trading.sync.connection.ITMTradingServerSyncConnectionMgr;
import com.itm.xtream.inet.trading.viewer.ITMComponentLayoutHelper;
import com.itm.xtream.inet.trading.viewer.ITMDialogToolFIX5ChangePassword;
import com.itm.xtream.inet.trading.viewer.ITMSoupBinTCPViewFrame;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Console;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Ari Pambudi
 */
public class ITMTradingServerHomeForm extends javax.swing.JFrame {
    
    public final static String Z_REF_TABLE_COLUMN_SELECT                        = "Select";
    public final static String Z_REF_TABLE_COLUMN_NAME                          = "Name";
    public final static String Z_REF_TABLE_COLUMN_TYPE                          = "Type";
    public final static String Z_REF_TABLE_COLUMN_ADDRESS                       = "Address";
    public final static String Z_REF_TABLE_COLUMN_PORT                          = "Port";
    public final static String Z_REF_TABLE_COLUMN_SEQUENCE                      = "Sequence";
    public final static String Z_REF_TABLE_COLUMN_MESSAGE                       = "Message";
    
    public final static String Z_REF_CONNECTION_TYPE_ITCH                       = "ITCH";
    public final static String Z_REF_CONNECTION_TYPE_OUCH                       = "OUCH";
    public final static String Z_REF_CONNECTION_TYPE_FIX5                       = "FIX5";
    
    private Timer timerAutoRefreshConnList;
    private int   iTimerCounterCurrent                                          = 0;
    private int   iTimerCounterMax                                              = 1;
    
    
    private ExecutorService execConnectionService                               = Executors.newFixedThreadPool(10);
    
    /**
     * Creates new form ITMTradingServerHomeForm
     */
    public ITMTradingServerHomeForm() {
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
//            if (ITMFileLoggerVarsConsts.APP_CLIENT_CODE.equalsIgnoreCase(AppClientCode.CLIENT_RELI_LS)){
//                this.setTitle(ITMTradingServerConsts.SVR_APP_CUSTOM_CAPTION);
//            }else{
                this.setTitle("X-stream INET Trading Server" + " [" + ITMTradingServerConsts.SVR_APP_VERSION_BUILD + "] - " + ITMFileLoggerVarsConsts.APP_CLIENT_CODE);
            //}

            //.perbaikan:
            fixTableLayout();
            verifyControlsState();
            initAutoRefreshConnList();
            initToggleSelectConnectionList();
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
    
    public void setMainControlsEnableState(boolean bToEnable){
        try{
            jTableIdxConnectionList.setEnabled(bToEnable);
            jButtonDoConnect.setEnabled(bToEnable);
            jButtonDoDisconnect.setEnabled(bToEnable);
            jButtonDoRefreshConnList.setEnabled(bToEnable);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    public void setBottomInfoMessage(String zInfoMessage){
        try{
            jLabelBottomInfo.setText(zInfoMessage);
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void fixTableLayout(){
        try{
            
            //.port:
            DefaultTableCellRenderer portRenderer = new DefaultTableCellRenderer();
            portRenderer.setHorizontalAlignment(JLabel.CENTER);
            jTableIdxConnectionList.getColumnModel().getColumn(jTableIdxConnectionList.getColumn(Z_REF_TABLE_COLUMN_PORT).getModelIndex()).setCellRenderer(portRenderer);
            
            //.sequence:
            DefaultTableCellRenderer sequenceRenderer = new DefaultTableCellRenderer();
            sequenceRenderer.setHorizontalAlignment(JLabel.RIGHT);
            jTableIdxConnectionList.getColumnModel().getColumn(jTableIdxConnectionList.getColumn(Z_REF_TABLE_COLUMN_SEQUENCE).getModelIndex()).setCellRenderer(sequenceRenderer);
            
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void verifyControlsState(){
        try{
            
            
            
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void initAutoRefreshConnList(){
        try{
            if (this.timerAutoRefreshConnList == null){
                this.timerAutoRefreshConnList = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            iTimerCounterCurrent++;
                            
                            if (iTimerCounterCurrent < iTimerCounterMax){
                                timerAutoRefreshConnList.restart();
                                return;
                            }
                            iTimerCounterCurrent = 0;
                            refreshConnectionList();
                            timerAutoRefreshConnList.restart();
                        }catch(Exception ex0){
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                    }
                });
                this.timerAutoRefreshConnList.start();
            }else{
                this.timerAutoRefreshConnList.stop();
                this.timerAutoRefreshConnList.start();
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void refreshConnectionList(){
        try{
            
            ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmOUCHs = ITMSoupBinTCPOUCHPacketMgr.getInstance.getAllConnectionLines();
            ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5s = FIX5IDXBridgeManager.getInstance.getChmFIX5ConnLines();
            
            boolean bMayFirstTimeFill = (ITMComponentLayoutHelper.getTableRowsCount(this.jTableIdxConnectionList) <= 0);
            HashMap<Object, Boolean> mCmpCheck = ITMComponentLayoutHelper.getCheckedTableRows(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SELECT, Z_REF_TABLE_COLUMN_NAME, true);
            ITMComponentLayoutHelper.clearTableRows(this.jTableIdxConnectionList);
            
            int pCurRow = 0;
            String zCurMsg = "";
            
            if ((chmOUCHs != null) && (!chmOUCHs.isEmpty())){
                for(ITMSoupBinTCPOUCHPacketController mEachCtl : chmOUCHs.values()){
                    if ((mEachCtl != null) && (!mEachCtl.getConnectionName().isEmpty())){
                        boolean bSetConnSelect = false;
                        if (bMayFirstTimeFill){
                            bSetConnSelect = mEachCtl.isAutoSelect();
                        }else if (mCmpCheck.containsKey(mEachCtl.getConnectionName())){
                            bSetConnSelect = true;
                        } 
                        zCurMsg = "-";
                        if (mEachCtl.getChannel() != null){
                            zCurMsg = (mEachCtl.getChannel().isConnected() ? ((mEachCtl.isAuthLastAccepted()) ? "+LoggedOn" : "+Connected") : "-Disconnected") + ((mEachCtl.getStsBridgeStatus() == OUCHBridgeStatus.SCK_CONNECTING || mEachCtl.getStsBridgeStatus() == OUCHBridgeStatus.SCK_DISCONNECTING) ? " [" + mEachCtl.getStsBridgeStatus().cname + "]" : "");
                        }
                        ITMComponentLayoutHelper.addTableEmptyRows(this.jTableIdxConnectionList, 1);
                        try{ this.jTableIdxConnectionList.setValueAt(bSetConnSelect, pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SELECT));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(mEachCtl.getConnectionName(), pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_NAME));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(Z_REF_CONNECTION_TYPE_OUCH, pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_TYPE));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(mEachCtl.getIPAddress(), pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_ADDRESS));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(mEachCtl.getPort(), pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_PORT));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(mEachCtl.getCurrentSequencedNo(), pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SEQUENCE));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(zCurMsg, pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_MESSAGE));}catch(Exception ex0){}
                        pCurRow++;
                    }
                }
            }
            
            if ((chmFIX5s != null) && (!chmFIX5s.isEmpty())){
                for(FIX5IDXBridgeController mEachCtl : chmFIX5s.values()){
                    if ((mEachCtl != null) && (!mEachCtl.getConnectionName().isEmpty())){
                        boolean bSetConnSelect = false;
                        if (bMayFirstTimeFill){
                            bSetConnSelect = mEachCtl.isAutoSelect();
                        }else if (mCmpCheck.containsKey(mEachCtl.getConnectionName())){
                            bSetConnSelect = true;
                        } 
                        zCurMsg = "-";
                        if (mEachCtl.getChannel() != null){
                            zCurMsg = (mEachCtl.getChannel().isConnected() ? ((mEachCtl.getChannel().isChannelAlreadyWasted()) ? "-Disconnected" : ((mEachCtl.isAdminLoggedOn()) ? "+LoggedOn" : "+Connected")) : "-Disconnected") + ((mEachCtl.getStsBridgeStatus() == FIX5IDXBridgeStatus.SCK_CONNECTING || mEachCtl.getStsBridgeStatus() == FIX5IDXBridgeStatus.SCK_DISCONNECTING) ? " [" + mEachCtl.getStsBridgeStatus().cname + "]" : "") + ((!StringHelper.isNullOrEmpty(mEachCtl.getLastMessage())) ? ". Msg:" + mEachCtl.getLastMessage() : "") ;
                        }
                        ITMComponentLayoutHelper.addTableEmptyRows(this.jTableIdxConnectionList, 1);
                        try{ this.jTableIdxConnectionList.setValueAt(bSetConnSelect, pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SELECT));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(mEachCtl.getConnectionName(), pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_NAME));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(Z_REF_CONNECTION_TYPE_FIX5, pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_TYPE));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(mEachCtl.getRefServerAddress(), pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_ADDRESS));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(mEachCtl.getRefServerPort(), pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_PORT));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt("T:" + mEachCtl.getCurrentTXSequencedNo() + "/R:" + mEachCtl.getCurrentRXSequencedNo(), pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SEQUENCE));}catch(Exception ex0){}
                        try{ this.jTableIdxConnectionList.setValueAt(zCurMsg, pCurRow, ITMComponentLayoutHelper.getTableColumnIndex(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_MESSAGE));}catch(Exception ex0){}
                        pCurRow++;
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
        
        
    }
    
    private void connectSelectedLines(boolean bAsync){
        try{
            
            ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmOUCHs = ITMSoupBinTCPOUCHPacketMgr.getInstance.getAllConnectionLines();
            ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5s = FIX5IDXBridgeManager.getInstance.getChmFIX5ConnLines();
            
            HashMap<Object, Boolean> mCmpCheck = ITMComponentLayoutHelper.getCheckedTableRows(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SELECT, Z_REF_TABLE_COLUMN_NAME, true);
            if ((mCmpCheck != null) && (!mCmpCheck.isEmpty())){
                for(Object oConnName : mCmpCheck.keySet()){
                    final String zConnName = (String)oConnName;
                    if ((zConnName != null) && (!zConnName.isEmpty())){
                        if (chmOUCHs.containsKey(zConnName)){
                            if (bAsync){
                                //.pakai executor:
                                execConnectionService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            ITMSoupBinTCPOUCHPacketMgr.getInstance.doConnectLine(false, zConnName);
                                        }catch(Exception ex0){
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                                        }
                                    }
                                });
                            }else{
                                ITMSoupBinTCPOUCHPacketMgr.getInstance.doConnectLine(false, zConnName);
                            }
                        } else if (chmFIX5s.containsKey(zConnName)){
                            if (bAsync){
                                //.pakai executor:
                                execConnectionService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            FIX5IDXBridgeManager.getInstance.connectFIX5ConnLine(zConnName);
                                        }catch(Exception ex0){
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                                        }
                                    }
                                });
                            }else{
                                FIX5IDXBridgeManager.getInstance.connectFIX5ConnLine(zConnName);
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void disconnectSelectedLines(boolean bAsync){
        try{
            
            ConcurrentHashMap<String, ITMSoupBinTCPOUCHPacketController> chmOUCHs = ITMSoupBinTCPOUCHPacketMgr.getInstance.getAllConnectionLines();
            ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5s = FIX5IDXBridgeManager.getInstance.getChmFIX5ConnLines();
            
            HashMap<Object, Boolean> mCmpCheck = ITMComponentLayoutHelper.getCheckedTableRows(this.jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SELECT, Z_REF_TABLE_COLUMN_NAME, true);
            if ((mCmpCheck != null) && (!mCmpCheck.isEmpty())){
                for(Object oConnName : mCmpCheck.keySet()){
                    final String zConnName = (String)oConnName;
                    if ((zConnName != null) && (!zConnName.isEmpty())){
                        if (chmOUCHs.containsKey(zConnName)){
                            if (bAsync){
                                //.pakai executor:
                                execConnectionService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            ITMSoupBinTCPOUCHPacketMgr.getInstance.doDisconnectLine(false, zConnName);
                                        }catch(Exception ex0){
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                                        }
                                    }
                                });
                            }else{
                                ITMSoupBinTCPOUCHPacketMgr.getInstance.doDisconnectLine(false, zConnName);
                            }
                        } else if (chmFIX5s.containsKey(zConnName)){
                            if (bAsync){
                                //.pakai executor:
                                execConnectionService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            FIX5IDXBridgeManager.getInstance.disconnectFIX5ConnLine(zConnName, true);
                                        }catch(Exception ex0){
                                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                                        }
                                    }
                                });
                            }else{
                                FIX5IDXBridgeManager.getInstance.disconnectFIX5ConnLine(zConnName, true);
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void initToggleSelectConnectionList(){
        try{
            
            jTableIdxConnectionList.getTableHeader().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try{
                        int pCol = jTableIdxConnectionList.columnAtPoint(e.getPoint());
                        String zColName = jTableIdxConnectionList.getColumnName(pCol);
                        if (zColName.toUpperCase().equalsIgnoreCase(Z_REF_TABLE_COLUMN_SELECT)){
                            int cChecked = ITMComponentLayoutHelper.getCheckedTableRowsCount(jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SELECT, true);
                            int cAllCount = jTableIdxConnectionList.getRowCount();
                            
                            if (cAllCount > 0){
                                ITMComponentLayoutHelper.setCheckedTableRowsAll(jTableIdxConnectionList, Z_REF_TABLE_COLUMN_SELECT, (((cChecked < cAllCount) || (cChecked <= 0))));
                            }
                        }
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }
                }
            });
            
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

        jScrollPanelList = new javax.swing.JScrollPane();
        jTableIdxConnectionList = new javax.swing.JTable();
        jButtonDoRefreshConnList = new javax.swing.JButton();
        jButtonDoConnect = new javax.swing.JButton();
        jButtonDoDisconnect = new javax.swing.JButton();
        jLabelBottomInfo = new javax.swing.JLabel();
        jMenuBarTop = new javax.swing.JMenuBar();
        jMenuTools = new javax.swing.JMenu();
        jMenuItemOpenSoupBinTCPMessageViewer = new javax.swing.JMenuItem();
        jSeparatorMenu1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemTerminateAllClientConnections = new javax.swing.JMenuItem();
        jSeparatorMenu2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemFIX5ChangePassword = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemOpenHelp = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 200));
        setSize(new java.awt.Dimension(700, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTableIdxConnectionList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Name", "Type", "Address", "Port", "Sequence", "Message"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableIdxConnectionList.setRowHeight(22);
        jTableIdxConnectionList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableIdxConnectionListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableIdxConnectionListMousePressed(evt);
            }
        });
        jScrollPanelList.setViewportView(jTableIdxConnectionList);
        if (jTableIdxConnectionList.getColumnModel().getColumnCount() > 0) {
            jTableIdxConnectionList.getColumnModel().getColumn(0).setResizable(false);
            jTableIdxConnectionList.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTableIdxConnectionList.getColumnModel().getColumn(1).setPreferredWidth(130);
            jTableIdxConnectionList.getColumnModel().getColumn(2).setPreferredWidth(40);
            jTableIdxConnectionList.getColumnModel().getColumn(3).setPreferredWidth(90);
            jTableIdxConnectionList.getColumnModel().getColumn(4).setPreferredWidth(40);
            jTableIdxConnectionList.getColumnModel().getColumn(5).setPreferredWidth(60);
            jTableIdxConnectionList.getColumnModel().getColumn(6).setPreferredWidth(180);
        }

        jButtonDoRefreshConnList.setText("Refresh");
        jButtonDoRefreshConnList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDoRefreshConnListActionPerformed(evt);
            }
        });

        jButtonDoConnect.setText("Connect");
        jButtonDoConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDoConnectActionPerformed(evt);
            }
        });

        jButtonDoDisconnect.setText("Disconnect");
        jButtonDoDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDoDisconnectActionPerformed(evt);
            }
        });

        jLabelBottomInfo.setForeground(new java.awt.Color(102, 102, 102));
        jLabelBottomInfo.setText("bottom info");

        jMenuBarTop.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jMenuBarTop.setPreferredSize(new java.awt.Dimension(100, 30));

        jMenuTools.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jMenuTools.setLabel("Tools");

        jMenuItemOpenSoupBinTCPMessageViewer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jMenuItemOpenSoupBinTCPMessageViewer.setText("SoupBinTCP Message Viewer");
        jMenuItemOpenSoupBinTCPMessageViewer.setPreferredSize(new java.awt.Dimension(195, 25));
        jMenuItemOpenSoupBinTCPMessageViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenSoupBinTCPMessageViewerActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemOpenSoupBinTCPMessageViewer);
        jMenuTools.add(jSeparatorMenu1);

        jMenuItemTerminateAllClientConnections.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jMenuItemTerminateAllClientConnections.setText("Terminate All Client Connections");
        jMenuItemTerminateAllClientConnections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTerminateAllClientConnectionsActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemTerminateAllClientConnections);
        jMenuTools.add(jSeparatorMenu2);

        jMenuItemFIX5ChangePassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jMenuItemFIX5ChangePassword.setText("FIX5 Change Password");
        jMenuItemFIX5ChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFIX5ChangePasswordActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemFIX5ChangePassword);

        jMenuBarTop.add(jMenuTools);

        jMenuHelp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jMenuHelp.setLabel("Help");

        jMenuItemOpenHelp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jMenuItemOpenHelp.setText("Open Help");
        jMenuItemOpenHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenHelpActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemOpenHelp);

        jMenuBarTop.add(jMenuHelp);
        jMenuHelp.setVisible(false);

        setJMenuBar(jMenuBarTop);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPanelList, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonDoRefreshConnList, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelBottomInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDoConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDoDisconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPanelList, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDoDisconnect)
                    .addComponent(jButtonDoConnect)
                    .addComponent(jButtonDoRefreshConnList)
                    .addComponent(jLabelBottomInfo))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemOpenSoupBinTCPMessageViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenSoupBinTCPMessageViewerActionPerformed
        // TODO add your handling code here:
        try{
            ITMSoupBinTCPViewFrame mViewerFrame = new ITMSoupBinTCPViewFrame();
            mViewerFrame.showThis();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_jMenuItemOpenSoupBinTCPMessageViewerActionPerformed

    private void jButtonDoConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDoConnectActionPerformed
        // TODO add your handling code here:
        connectSelectedLines(true);
    }//GEN-LAST:event_jButtonDoConnectActionPerformed

    private void jButtonDoDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDoDisconnectActionPerformed
        // TODO add your handling code here:
        disconnectSelectedLines(true);
    }//GEN-LAST:event_jButtonDoDisconnectActionPerformed

    private void jButtonDoRefreshConnListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDoRefreshConnListActionPerformed
        // TODO add your handling code here:
        refreshConnectionList();
    }//GEN-LAST:event_jButtonDoRefreshConnListActionPerformed

    private void jTableIdxConnectionListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableIdxConnectionListMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTableIdxConnectionListMouseClicked

    private void jTableIdxConnectionListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableIdxConnectionListMousePressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTableIdxConnectionListMousePressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        //.saat jendela mau ditutup:
        //.konfirmasi dulu:
        try{
            //.konfirmasi dulu sebelum eksekusi (default:"NO"):
            Object[] oBtnOptions = {"Yes","No"};
            int iMsgRet = JOptionPane.showOptionDialog(this, "Are you sure to <Shutdown> Trading Server ?", DispTextSetup.Z_TITLE_MSGBOX_CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, oBtnOptions, oBtnOptions[1]);
            if (iMsgRet != JOptionPane.YES_OPTION){
                return; //.batalkan.
            }
            //.lanjut menghentikan ts:
            //.dari client / user dulu yg dihentikan, agar tidak ada request lagi:
            ITMSoupBinTCPOUCHPacketMgr.getInstance.doDisconnectLine(true, "");
            FIX5IDXBridgeManager.getInstance.stopAllConnections();
            //.tunggu penyelesaian:
            new Thread(new Runnable() {
                //.jalan di thread baru:
                @Override
                public void run() {
                    try{
                        try{
                            Thread.sleep(3456);
                        }catch(InterruptedException ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }catch(Exception ex0){
                            //.EXXX.
                            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        }
                        //.shutdown runtime:
                        System.exit(0);
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                    }
                }
            }).start();
            //.selesai... .
        }catch(HeadlessException ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }catch(Exception ex0){
            //.EXXX.
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_formWindowClosing
    
    private void jMenuItemOpenHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenHelpActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jMenuItemOpenHelpActionPerformed

    private void jMenuItemTerminateAllClientConnectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTerminateAllClientConnectionsActionPerformed
        // TODO add your handling code here:
        ITMTradingServerSyncConnectionMgr.getInstance.terminateAllClientConnections(true);
    }//GEN-LAST:event_jMenuItemTerminateAllClientConnectionsActionPerformed

    private void jMenuItemFIX5ChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFIX5ChangePasswordActionPerformed
        // TODO add your handling code here:
        try{
            ITMDialogToolFIX5ChangePassword mToolFrame = new ITMDialogToolFIX5ChangePassword();
            mToolFrame.showThis();
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }//GEN-LAST:event_jMenuItemFIX5ChangePasswordActionPerformed
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
//////            java.util.logging.Logger.getLogger(ITMTradingServerHomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (InstantiationException ex) {
//////            java.util.logging.Logger.getLogger(ITMTradingServerHomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (IllegalAccessException ex) {
//////            java.util.logging.Logger.getLogger(ITMTradingServerHomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//////            java.util.logging.Logger.getLogger(ITMTradingServerHomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//////        }
//////        //</editor-fold>
//////        //</editor-fold>
//////
//////        /* Create and display the form */
//////        java.awt.EventQueue.invokeLater(new Runnable() {
//////            public void run() {
//////                new ITMTradingServerHomeForm().setVisible(true);
//////            }
//////        });
//////    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDoConnect;
    private javax.swing.JButton jButtonDoDisconnect;
    private javax.swing.JButton jButtonDoRefreshConnList;
    private javax.swing.JLabel jLabelBottomInfo;
    private javax.swing.JMenuBar jMenuBarTop;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemFIX5ChangePassword;
    private javax.swing.JMenuItem jMenuItemOpenHelp;
    private javax.swing.JMenuItem jMenuItemOpenSoupBinTCPMessageViewer;
    private javax.swing.JMenuItem jMenuItemTerminateAllClientConnections;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JScrollPane jScrollPanelList;
    private javax.swing.JPopupMenu.Separator jSeparatorMenu1;
    private javax.swing.JPopupMenu.Separator jSeparatorMenu2;
    private javax.swing.JTable jTableIdxConnectionList;
    // End of variables declaration//GEN-END:variables
}
