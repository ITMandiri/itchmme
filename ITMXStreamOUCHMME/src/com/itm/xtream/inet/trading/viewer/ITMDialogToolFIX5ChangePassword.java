/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.viewer;

import com.itm.fix5.data.jonec.admin.FIX5JonecDoReqAdministration;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeController;
import com.itm.fix5.data.message.bridge.FIX5IDXBridgeManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerManager;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logLevel;
import com.itm.generic.engine.filelogger.setup.ITMFileLoggerVarsConsts.logSource;
import com.itm.generic.engine.socket.uhelpers.StringHelper;
import com.itm.mis.itch.callback.ITMSoupBinTCPITCHPacketController;
import com.itm.xtream.inet.trading.consts.ITMTradingServerConsts.DispTextSetup;
import com.itm.xtream.inet.trading.settings.ITMTradingServerSettingsMgr;
import java.awt.Color;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ari Pambudi
 */
public class ITMDialogToolFIX5ChangePassword extends javax.swing.JFrame {

    /**
     * Creates new form ITMDialogToolFIX5ChangePassword
     */
    public ITMDialogToolFIX5ChangePassword() {
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
            this.setTitle("FIX5 Change Password");
            //.perbaikan:
            verifyControlsState(true);
            
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
    
    private void verifyControlsState(boolean bResetInputs){
        try{
            if (jComboBoxName.getItemCount() <= 0){
                ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5s = FIX5IDXBridgeManager.getInstance.getChmFIX5ConnLines();
                for(FIX5IDXBridgeController mEachCtl : chmFIX5s.values()){
                    if ((mEachCtl != null) && (!mEachCtl.getConnectionName().isEmpty())){
                        jComboBoxName.addItem(mEachCtl.getConnectionName());
                    }
                }
            }
            if (bResetInputs){
                jPasswordFieldOldPwd.setText("");
                jPasswordFieldNewPwd.setText("");
                jPasswordFieldNewPwdRepeat.setText("");
            }
            jButtonCmdExecute.setEnabled(false);
            String zOldPwd = String.valueOf(jPasswordFieldOldPwd.getPassword());
            String zNewPwd = String.valueOf(jPasswordFieldNewPwd.getPassword());
            String zNewPwdRepeat = String.valueOf(jPasswordFieldNewPwdRepeat.getPassword());
            if (
                    (!StringHelper.isNullOrEmpty(zOldPwd))
                &&  (!StringHelper.isNullOrEmpty(zNewPwd))
                &&  (!StringHelper.isNullOrEmpty(zNewPwdRepeat))
                &&  (!zNewPwd.equals(zOldPwd))
                &&  (zNewPwd.equals(zNewPwdRepeat))
            ){
                jButtonCmdExecute.setEnabled(true);
                jPasswordFieldOldPwd.setBackground(Color.white);
                jPasswordFieldNewPwd.setBackground(Color.white);
                jPasswordFieldNewPwdRepeat.setBackground(Color.white);
            }else{
                if (StringHelper.isNullOrEmpty(zOldPwd)){
                    jPasswordFieldOldPwd.setBackground(Color.yellow);
                }else{
                    jPasswordFieldOldPwd.setBackground(Color.white);
                }
                if (StringHelper.isNullOrEmpty(zNewPwd)){
                    jPasswordFieldNewPwd.setBackground(Color.yellow);
                }else{
                    jPasswordFieldNewPwd.setBackground(Color.white);
                    if (!zNewPwd.equals(zNewPwdRepeat)){
                        jPasswordFieldNewPwd.setBackground(Color.orange);
                    }
                    if (!StringHelper.isNullOrEmpty(zOldPwd) && zNewPwd.equals(zOldPwd)){
                        jPasswordFieldOldPwd.setBackground(Color.orange);
                        jPasswordFieldNewPwd.setBackground(Color.orange);
                    }
                }
                if (StringHelper.isNullOrEmpty(zNewPwdRepeat)){
                    jPasswordFieldNewPwdRepeat.setBackground(Color.yellow);
                }else{
                    jPasswordFieldNewPwdRepeat.setBackground(Color.white);
                    if (!zNewPwdRepeat.equals(zNewPwd)){
                        jPasswordFieldNewPwdRepeat.setBackground(Color.orange);
                    }
                    if (!StringHelper.isNullOrEmpty(zOldPwd) && zNewPwdRepeat.equals(zOldPwd)){
                        jPasswordFieldOldPwd.setBackground(Color.orange);
                        jPasswordFieldNewPwdRepeat.setBackground(Color.orange);
                    }
                }
                
            }
        }catch(Exception ex0){
            ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
        }
    }
    
    private void doExecuteChangePwd(){
        try{
            final ITMDialogToolFIX5ChangePassword _this = this;
            final String zName = (String)jComboBoxName.getSelectedItem();
            final String zOldPwd = String.valueOf(jPasswordFieldOldPwd.getPassword());
            final String zNewPwd = String.valueOf(jPasswordFieldNewPwd.getPassword());
            final String zNewPwdRepeat = String.valueOf(jPasswordFieldNewPwdRepeat.getPassword());
            
            SwingUtilities.invokeLater(new Runnable() {
                //.jalan di thread baru:
                @Override
                public void run() {
                    try{
                        if (
                            (!StringHelper.isNullOrEmpty(zName))
                            && (!StringHelper.isNullOrEmpty(zOldPwd))   
                            && (!StringHelper.isNullOrEmpty(zNewPwd))
                            && (!StringHelper.isNullOrEmpty(zNewPwdRepeat))
                        ){
                            FIX5IDXBridgeController mCtl = null;
                            ConcurrentHashMap<String, FIX5IDXBridgeController> chmFIX5s = FIX5IDXBridgeManager.getInstance.getChmFIX5ConnLines();
                            for(FIX5IDXBridgeController mEachCtl : chmFIX5s.values()){
                                if ((mEachCtl != null) && (!mEachCtl.getConnectionName().isEmpty()) && (zName.equals(mEachCtl.getConnectionName()))){
                                    mCtl = mEachCtl;
                                    break;
                                }
                            }
                            if (mCtl == null){
                                //.nama harus ada:
                                //.kesalahan:
                                JOptionPane.showMessageDialog(_this, "-failed: fix5 connection name: " + zName + " not found.", DispTextSetup.Z_TITLE_MSGBOX_CONFIRM, JOptionPane.WARNING_MESSAGE);
                            }else if (!zOldPwd.equals(mCtl.getPassword1())) {
                                //.password lama harus sama:
                                //.kesalahan:
                                JOptionPane.showMessageDialog(_this, "-failed: fix5 'typed' existing password not match with 'real' existing password for connection name: " + zName + ".", DispTextSetup.Z_TITLE_MSGBOX_CONFIRM, JOptionPane.WARNING_MESSAGE);
                            }else{
                                final FIX5IDXBridgeController mFinalCtl = mCtl;
                                //.konfirmasi dulu sebelum persiapan eksekusi (default:"NO"):
                                Object[] oBtnOptions = {"Yes","No"};
                                int iMsgRet = JOptionPane.showOptionDialog(_this, "Are you sure to prepare execute change FIX5 password for: " + zName + " ?" + "\r\n(This action will disconnect active FIX5 connection)", DispTextSetup.Z_TITLE_MSGBOX_CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, oBtnOptions, oBtnOptions[1]);
                                if (iMsgRet == JOptionPane.YES_OPTION){
                                    //.disconnect active connection:
                                    FIX5IDXBridgeManager.getInstance.disconnectFIX5ConnLine(zName, true);
                                    //.continue:
                                    Thread thRespondWaitCtrl = new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            try{

                                                int MAX_WAIT_SECONDS = 30; //.wait for n seconds;
                                                int cDecrWait = MAX_WAIT_SECONDS;

                                                //.disable inputs:
                                                _this.jComboBoxName.setEnabled(false);
                                                _this.jPasswordFieldOldPwd.setEnabled(false);
                                                _this.jPasswordFieldNewPwd.setEnabled(false);
                                                _this.jPasswordFieldNewPwdRepeat.setEnabled(false);
                                                _this.jButtonCmdExecute.setEnabled(false);
                                                _this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

                                                //.tunda waktu (tunggu):
                                                //.make sure already disconnected:
                                                while (cDecrWait > 0){
                                                    try{
                                                        cDecrWait--;
                                                        Thread.sleep(1000); //.tunggu tiap 1 detik.
                                                        if (!mFinalCtl.isConnected() || mFinalCtl.getChannel().isChannelAlreadyWasted()){
                                                            cDecrWait = 0;
                                                            break;
                                                        }
                                                    }catch(InterruptedException ex0){}
                                                    //.beri teks waktu tunggu (detik) di tombol:
                                                    _this.jButtonCmdExecute.setText("Checking Disconnection (" + StringHelper.fromInt(cDecrWait + 1) + ")");
                                                }
                                                
                                                //.try login with new password:
                                                String zRealExistingPassword1 = mFinalCtl.getPassword1();
                                                String zRealExistingPassword2 = mFinalCtl.getPassword2();
                                                
                                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "~try to change fix5 password: name=" + zName + ", typed_oldpwd=" + zOldPwd + ", typed_newpwd=" + zNewPwd + ", real_existingpwd1=" + zRealExistingPassword1 + ", real_existingpwd2=" + zRealExistingPassword2);
                                                
                                                String zTxtFlagChangingPwd = "@CHANGING_PASSWORD:";
                                                String zTxtRecvMsg = "";
                                                //.connect & logon connection:
                                                mFinalCtl.setPassword2(zNewPwd);
                                                mFinalCtl.setLastMessage(zTxtFlagChangingPwd + mFinalCtl.getLastMessage());
                                                FIX5IDXBridgeManager.getInstance.connectFIX5ConnLine(zName);
                                                
                                                //.tunda waktu (tunggu):
                                                //.make sure connected:
                                                cDecrWait = MAX_WAIT_SECONDS;
                                                while (cDecrWait > 0){
                                                    try{
                                                        cDecrWait--;
                                                        Thread.sleep(1000); //.tunggu tiap 1 detik.
                                                        zTxtRecvMsg = mFinalCtl.getLastMessage();
                                                        if ((!StringHelper.isNullOrEmpty(zTxtRecvMsg)) && (!zTxtRecvMsg.startsWith(zTxtFlagChangingPwd))){
                                                            cDecrWait = 0;
                                                            break;
                                                        }else if (mFinalCtl.isAdminLoggedOn()){
                                                            mFinalCtl.setLastMessage("System: FIX5 Change Password Success by Logged On Status");
                                                            zTxtRecvMsg = mFinalCtl.getLastMessage();
                                                            cDecrWait = 0;
                                                            break;
                                                        }
                                                    }catch(InterruptedException ex0){}
                                                    //.beri teks waktu tunggu (detik) di tombol:
                                                    _this.jButtonCmdExecute.setText("Checking Connection (" + StringHelper.fromInt(cDecrWait + 1) + ")");
                                                }
                                                
                                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.WARNING, "~respond to change fix5 password: name=" + zName + ", typed_oldpwd=" + zOldPwd + ", typed_newpwd=" + zNewPwd + ", real_existingpwd1=" + zRealExistingPassword1 + ", real_existingpwd2=" + zRealExistingPassword2 + ", recvmsg=" + zTxtRecvMsg);
                                                
                                                if (zTxtRecvMsg == null){ zTxtRecvMsg = ""; }
                                                
                                                if (
                                                    (zTxtRecvMsg.contains("(77)")) //.(77): Current password incorrectly entered
                                                    || (zTxtRecvMsg.contains("(124)")) //.(124): Invalid logon credentials
                                                    || (!zTxtRecvMsg.toLowerCase().contains("success"))
                                                ){
                                                    mFinalCtl.setPassword2(mFinalCtl.getPassword1());
                                                }else{
                                                    mFinalCtl.setPassword1(mFinalCtl.getPassword2());
                                                    //.simpan ke file settings:
                                                    try{
                                                        for (ITMTradingServerSettingsMgr.FIX_Connection fix5_connection : ITMTradingServerSettingsMgr.getInstance.getSettings().server_settings.fix5_connections) {
                                                            if (zName.equals(fix5_connection.name)) {
                                                                fix5_connection.password1 = zNewPwd;
                                                                fix5_connection.password2 = zNewPwd;
                                                                break;
                                                            }
                                                        }
                                                        ITMTradingServerSettingsMgr.getInstance.saveSettings();
                                                    }catch(Exception ex0){
                                                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                                                    }
                                                }
                                                
                                                //.disconnect active connection, agar user yg aware connect sendiri:
                                                FIX5IDXBridgeManager.getInstance.disconnectFIX5ConnLine(zName, true);
                                                
                                                //.enable inputs:
                                                _this.jComboBoxName.setEnabled(true);
                                                _this.jPasswordFieldOldPwd.setEnabled(true);
                                                _this.jPasswordFieldNewPwd.setEnabled(true);
                                                _this.jPasswordFieldNewPwdRepeat.setEnabled(true);
                                                _this.jButtonCmdExecute.setEnabled(true);
                                                _this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                                                
                                                _this.jButtonCmdExecute.setText("Execute");
                                                
                                                JOptionPane.showMessageDialog(_this, "Received Message: " + zTxtRecvMsg, DispTextSetup.Z_TITLE_MSGBOX_WARNING, JOptionPane.WARNING_MESSAGE);
                                                
                                            }catch(HeadlessException ex0){
                                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                                            }catch(Exception ex0){
                                                ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                                            }
                                        }
                                    });
                                    thRespondWaitCtrl.start();

                                }
                            }
                        }else{
                            //.tidak boleh ada field yg kosong:
                            //.kesalahan:
                            JOptionPane.showMessageDialog(_this, "-failed: please fill empty input field(s).", DispTextSetup.Z_TITLE_MSGBOX_CONFIRM, JOptionPane.WARNING_MESSAGE);
                        }
                    }catch(HeadlessException ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        //.konfirmasi error:
                        JOptionPane.showMessageDialog(_this, String.format("-error: exception for change FIX5 connection password request.\r\nexception='%s'.", ex0.getMessage()), DispTextSetup.Z_TITLE_MSGBOX_CONFIRM, JOptionPane.ERROR_MESSAGE);
                    }catch(Exception ex0){
                        ITMFileLoggerManager.getInstance.insertLog(this, logSource.XTTS, logLevel.ERROR, ex0);
                        //.konfirmasi error:
                        JOptionPane.showMessageDialog(_this, String.format("-error: exception for change FIX5 connection password request.\r\nexception='%s'.", ex0.getMessage()), DispTextSetup.Z_TITLE_MSGBOX_CONFIRM, JOptionPane.ERROR_MESSAGE);
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

        jLabelCaptionName = new javax.swing.JLabel();
        jComboBoxName = new javax.swing.JComboBox<>();
        jLabelCaptionOldPwd = new javax.swing.JLabel();
        jPasswordFieldOldPwd = new javax.swing.JPasswordField();
        jLabelNewPwd = new javax.swing.JLabel();
        jPasswordFieldNewPwd = new javax.swing.JPasswordField();
        jLabelNewPwdRepeat = new javax.swing.JLabel();
        jPasswordFieldNewPwdRepeat = new javax.swing.JPasswordField();
        jButtonCmdExecute = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(300, 220));

        jLabelCaptionName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelCaptionName.setLabelFor(jComboBoxName);
        jLabelCaptionName.setText("Name");

        jComboBoxName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNameActionPerformed(evt);
            }
        });

        jLabelCaptionOldPwd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelCaptionOldPwd.setLabelFor(jPasswordFieldOldPwd);
        jLabelCaptionOldPwd.setText("Existing Password");

        jPasswordFieldOldPwd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPasswordFieldOldPwd.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jPasswordFieldOldPwdCaretUpdate(evt);
            }
        });
        jPasswordFieldOldPwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldOldPwdActionPerformed(evt);
            }
        });

        jLabelNewPwd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNewPwd.setLabelFor(jPasswordFieldNewPwd);
        jLabelNewPwd.setText("New Password");

        jPasswordFieldNewPwd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPasswordFieldNewPwd.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jPasswordFieldNewPwdCaretUpdate(evt);
            }
        });
        jPasswordFieldNewPwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldNewPwdActionPerformed(evt);
            }
        });

        jLabelNewPwdRepeat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelNewPwdRepeat.setLabelFor(jPasswordFieldNewPwdRepeat);
        jLabelNewPwdRepeat.setText("New Password (Repeat)");

        jPasswordFieldNewPwdRepeat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPasswordFieldNewPwdRepeat.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jPasswordFieldNewPwdRepeatCaretUpdate(evt);
            }
        });
        jPasswordFieldNewPwdRepeat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldNewPwdRepeatActionPerformed(evt);
            }
        });

        jButtonCmdExecute.setText("Execute");
        jButtonCmdExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCmdExecuteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCaptionOldPwd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCaptionName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelNewPwdRepeat, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(jLabelNewPwd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCmdExecute, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(jPasswordFieldOldPwd, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPasswordFieldNewPwd, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPasswordFieldNewPwdRepeat)
                    .addComponent(jComboBoxName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxName, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabelCaptionName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCaptionOldPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldOldPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNewPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldNewPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNewPwdRepeat, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldNewPwdRepeat, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jButtonCmdExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNameActionPerformed
        // TODO add your handling code here:
        verifyControlsState(true);
    }//GEN-LAST:event_jComboBoxNameActionPerformed

    private void jPasswordFieldOldPwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldOldPwdActionPerformed
        // TODO add your handling code here:
        verifyControlsState(false);
    }//GEN-LAST:event_jPasswordFieldOldPwdActionPerformed

    private void jPasswordFieldNewPwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldNewPwdActionPerformed
        // TODO add your handling code here:
        verifyControlsState(false);
    }//GEN-LAST:event_jPasswordFieldNewPwdActionPerformed

    private void jPasswordFieldNewPwdRepeatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldNewPwdRepeatActionPerformed
        // TODO add your handling code here:
        verifyControlsState(false);
    }//GEN-LAST:event_jPasswordFieldNewPwdRepeatActionPerformed

    private void jPasswordFieldOldPwdCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jPasswordFieldOldPwdCaretUpdate
        // TODO add your handling code here:
        verifyControlsState(false);
    }//GEN-LAST:event_jPasswordFieldOldPwdCaretUpdate

    private void jPasswordFieldNewPwdCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jPasswordFieldNewPwdCaretUpdate
        // TODO add your handling code here:
        verifyControlsState(false);
    }//GEN-LAST:event_jPasswordFieldNewPwdCaretUpdate

    private void jPasswordFieldNewPwdRepeatCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jPasswordFieldNewPwdRepeatCaretUpdate
        // TODO add your handling code here:
        verifyControlsState(false);
    }//GEN-LAST:event_jPasswordFieldNewPwdRepeatCaretUpdate

    private void jButtonCmdExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCmdExecuteActionPerformed
        // TODO add your handling code here:
        doExecuteChangePwd();
    }//GEN-LAST:event_jButtonCmdExecuteActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCmdExecute;
    private javax.swing.JComboBox<String> jComboBoxName;
    private javax.swing.JLabel jLabelCaptionName;
    private javax.swing.JLabel jLabelCaptionOldPwd;
    private javax.swing.JLabel jLabelNewPwd;
    private javax.swing.JLabel jLabelNewPwdRepeat;
    private javax.swing.JPasswordField jPasswordFieldNewPwd;
    private javax.swing.JPasswordField jPasswordFieldNewPwdRepeat;
    private javax.swing.JPasswordField jPasswordFieldOldPwd;
    // End of variables declaration//GEN-END:variables
}
