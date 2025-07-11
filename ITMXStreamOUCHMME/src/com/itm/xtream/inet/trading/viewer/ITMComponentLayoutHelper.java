/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.xtream.inet.trading.viewer;

import static com.itm.soupbintcp.bridge.packetformat.ITMSoupBinTCPBridgePacketFormat.isNullOrEmpty;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author fredy
 */
public class ITMComponentLayoutHelper {
    
    public static void enableFirstGlobalProgramTheme(){
        try{
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) { //.Nimbus
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex0) {
            }
        }catch(Exception ex0){
            
        }
    }
    
    public static JFrame getParentFrame(JComponent targetComponent){
        JFrame mOut = null;
        try{
            mOut = (JFrame)SwingUtilities.getWindowAncestor(targetComponent);
        }catch(Exception ex0){
        }
        return mOut;
    }
    
    public static void centerParent(JFrame parentFrame, JFrame targetFrame){
        try{
            if (parentFrame == null){
                centerScreen(targetFrame);
            }else{
                Dimension dimParentFrame = parentFrame.getSize();
                Dimension dimTargetFrame = targetFrame.getSize();
                int mergedX = ((parentFrame.getX() >= 0) ? parentFrame.getX() : 0) + ((dimParentFrame.width / 2) - (dimTargetFrame.width / 2));
                int mergedY = ((parentFrame.getY() >= 0) ? parentFrame.getY() : 0) + ((dimParentFrame.height / 2) - (dimTargetFrame.height / 2));
                targetFrame.setBounds(
                          mergedX
                        , mergedY
                        , dimTargetFrame.width, dimTargetFrame.height);
                
            }
        }catch(HeadlessException ex0){
        }catch(Exception ex0){
        }
    }
    
    public static void centerScreen(JFrame targetFrame){
        try{
            Dimension dimScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dimWindow = targetFrame.getSize();
            if (dimScreen != null){
                if (dimWindow != null){
                    targetFrame.setBounds((dimScreen.width / 2) - (dimWindow.width / 2), (dimScreen.height / 2) - (dimWindow.height / 2), dimWindow.width, dimWindow.height);
                }
            }
        }catch(HeadlessException ex0){
        }catch(Exception ex0){
        }
    }
    
    public static void centerScreen(JDialog targetDialog){
        try{
            Dimension dimScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dimWindow = targetDialog.getSize();
            if (dimScreen != null){
                if (dimWindow != null){
                    targetDialog.setBounds((dimScreen.width / 2) - (dimWindow.width / 2), (dimScreen.height / 2) - (dimWindow.height / 2), dimWindow.width, dimWindow.height);
                }
            }
        }catch(HeadlessException ex0){
        }catch(Exception ex0){
        }
    }
    
    public static int getTableColumnIndex(JTable targetTable, String targetColumnName){
        int iOut = (-1);
        try{
            if (targetTable != null){
                for (int iCol = 0; iCol < targetTable.getColumnCount(); iCol++){
                    if (targetTable.getColumnName(iCol).equalsIgnoreCase(targetColumnName)){
                        iOut = iCol;
                        break;
                    }
                }
            }
        }catch(Exception ex0){
        }
        return iOut;
    }
    
    public static int getTableRowIndexByRowId(JTable targetTable, String targetColumnName, Object targetRowId){
        int iOut = (-1);
        try{
            if ((targetTable != null) && (!isNullOrEmpty(targetColumnName))){
                DefaultTableModel dtmCmpCheck = (DefaultTableModel) targetTable.getModel();
                int cRowCount = dtmCmpCheck.getRowCount();
                if (cRowCount > 0){
                    int iColKeyIndex = getTableColumnIndex(targetTable, targetColumnName);
                    if (iColKeyIndex >= 0){
                        for (int iRow = 0; iRow < cRowCount; iRow++) {
                            if (dtmCmpCheck.getValueAt(iRow, iColKeyIndex).equals(targetRowId)) {
                                iOut = iRow;
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
        }
        return iOut;
    }
    
    public static void clearTableRows(JTable targetTable){
        try{
            if (targetTable != null){
                DefaultTableModel dtmCmpClear = (DefaultTableModel) targetTable.getModel();
                int cRowCount = dtmCmpClear.getRowCount();
                if (cRowCount > 0){
                    dtmCmpClear.setRowCount(0);
                }
            }
        }catch(Exception ex0){
        }
    }
    
    public static int getTableRowsCount(JTable targetTable){
        int mOut = 0;
        try{
            if (targetTable != null){
                DefaultTableModel dtmCmpCount = (DefaultTableModel) targetTable.getModel();
                mOut = dtmCmpCount.getRowCount();
            }
        }catch(Exception ex0){
        }
        return mOut;
    }
    
    public static void addTableEmptyRows(JTable targetTable, int targetRowsAddCount){
        try{
            if ((targetTable != null) && (targetRowsAddCount > 0)){
                DefaultTableModel dtmCmpAdd = (DefaultTableModel) targetTable.getModel();
                for (int iAdd = 0; iAdd < targetRowsAddCount; iAdd++){
                    dtmCmpAdd.addRow(new Object[]{null});
                }
            }
        }catch(Exception ex0){
        }
    }
    
    public static void removeTableEmptyRows(JTable targetTable, int targetRowsToRemove){
        try{
            if ((targetTable != null) && (targetRowsToRemove >= 0)){
                DefaultTableModel dtmCmpRem = (DefaultTableModel) targetTable.getModel();
                for (int iRem = 0; iRem < targetRowsToRemove; iRem++){
                    dtmCmpRem.removeRow(dtmCmpRem.getRowCount() - 1);
                }
            }
        }catch(Exception ex0){
        }
    }
    
    public static HashMap<Object, Boolean> getCheckedTableRows(JTable targetTable, String columnNameAsCheck, String columnNameAsID, boolean checkedState){
        HashMap<Object, Boolean> mOut = new HashMap();
        try{
            if ((targetTable != null) && (!isNullOrEmpty(columnNameAsID))){
                DefaultTableModel dtmCmpCheck = (DefaultTableModel) targetTable.getModel();
                int cRowCount = dtmCmpCheck.getRowCount();
                if (cRowCount > 0){
                    int iColCheckIndex = getTableColumnIndex(targetTable, columnNameAsCheck);
                    int iColKeyIndex = getTableColumnIndex(targetTable, columnNameAsID);
                    if ((iColCheckIndex >= 0) && (iColKeyIndex >= 0)){
                        for (int iRow = 0; iRow < cRowCount; iRow++) {
                            if (((boolean)(dtmCmpCheck.getValueAt(iRow, iColCheckIndex))) == checkedState) {
                                mOut.put(dtmCmpCheck.getValueAt(iRow, iColKeyIndex), checkedState);
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
        }
        return mOut;
    }
    
    public static int getCheckedTableRowsCount(JTable targetTable, String columnNameAsCheck, boolean checkedState){
        int mOut = 0;
        try{
            if ((targetTable != null) && (!isNullOrEmpty(columnNameAsCheck))){
                DefaultTableModel dtmCmpCheck = (DefaultTableModel) targetTable.getModel();
                int cRowCount = dtmCmpCheck.getRowCount();
                if (cRowCount > 0){
                    int iColCheckIndex = getTableColumnIndex(targetTable, columnNameAsCheck);
                    if (iColCheckIndex >= 0){
                        for (int iRow = 0; iRow < cRowCount; iRow++) {
                            if (((boolean)(dtmCmpCheck.getValueAt(iRow, iColCheckIndex))) == checkedState) {
                                mOut++;
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
        }
        return mOut;
    }
    
    public static int setCheckedTableRowsAll(JTable targetTable, String columnNameAsCheck, boolean checkedState){
        int mOut = 0;
        try{
            if ((targetTable != null) && (!isNullOrEmpty(columnNameAsCheck))){
                DefaultTableModel dtmCmpCheck = (DefaultTableModel) targetTable.getModel();
                int cRowCount = dtmCmpCheck.getRowCount();
                if (cRowCount > 0){
                    int iColCheckIndex = getTableColumnIndex(targetTable, columnNameAsCheck);
                    if (iColCheckIndex >= 0){
                        for (int iRow = 0; iRow < cRowCount; iRow++) {
                            if (((boolean)(dtmCmpCheck.getValueAt(iRow, iColCheckIndex))) != checkedState) {
                                dtmCmpCheck.setValueAt(checkedState, iRow, iColCheckIndex);
                                mOut++;
                            }
                        }
                    }
                }
            }
        }catch(Exception ex0){
        }
        return mOut;
    }
    
    public static HashMap<Integer, Object> getTableKeyColumnIndexAndValues(JTable targetTable, String columnNameAsKey){
        HashMap<Integer, Object> mOut = new HashMap<>();
        try{
            if ((targetTable != null) && (!isNullOrEmpty(columnNameAsKey))){
                DefaultTableModel dtmCmpCheck = (DefaultTableModel) targetTable.getModel();
                int cRowCount = dtmCmpCheck.getRowCount();
                if (cRowCount > 0){
                    int iColKeyIndex = getTableColumnIndex(targetTable, columnNameAsKey);
                    if (iColKeyIndex >= 0){
                        for (int iRow = 0; iRow < cRowCount; iRow++) {
                            mOut.put(iRow, dtmCmpCheck.getValueAt(iRow, iColKeyIndex));
                        }
                    }
                }
            }
        }catch(Exception ex0){
        }
        return mOut;
    }
    
    public static void setTableCellsAlignment(JTable targetTable, int alignment)
    {
        try{
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(alignment);
            TableModel tableModel = targetTable.getModel();
            for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
            {
                targetTable.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
            }
        }catch(Exception ex0){
        }
    }
    
}