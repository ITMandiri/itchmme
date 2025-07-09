/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itm.generic.engine.database.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author hirin
 */
public class ITMDBQueryBuilder {
    private QBDBType dbtype;
    private String tableName;
    private String schemaName;

    private HashMap operatorList = new HashMap();
    private HashMap whereList = new HashMap();
    private HashMap whereType = new HashMap();
    private HashMap columnList = new HashMap();
    private HashMap columnType = new HashMap();
    
    //.{APM:20141015}sekali pakai yg MM, harus pakai MM sampai dengan getXXQuery():
    private class MMKVItem {
        public Object oKey;
        public Object oValue;
        public QBDataType oType = QBDataType.Numeric;
        public QBOperatorType oOperator = QBOperatorType.AND;
    }
    
    private ArrayList<MMKVItem> arrMMWhereList = new ArrayList();
    
    
    private String COLUMN_TABLE_IDENTIFIER_GENERAL = "`"; //. for mysql,sybase,etc
    private String COLUMN_TABLE_IDENTIFIER_OPEN = "["; //. for mssql
    private String COLUMN_TABLE_IDENTIFIER_CLOSE = "]"; //. for mssql
    
    public enum QBDBType{
        SYBASE,
        MYSQL,
        MSSQL,
        POSTGRE
    }
    public enum QBOperatorType{
        AND,
        OR,
        ORLIKE,
        ANDLIKE
    }
    public enum QBDataType{
        String,
        Numeric,
        Date
    }

    public ITMDBQueryBuilder(){
        //. default mssql
        this.dbtype = QBDBType.MSSQL;
    }
    
    public ITMDBQueryBuilder(QBDBType dbtype){
        this.dbtype = dbtype;
    }
    
    public String getDeleteQuery(){
        String tQuery = "";
        String whereCondition = "";
        String compSymb = "";
        int i = 0;
        try{
            QBDataType type;
            QBOperatorType opType;
            String strKey;
            Set set = whereList.keySet();
            Iterator iter = set.iterator();

            while (iter.hasNext()) {
                i++;
                strKey = (String) iter.next();
                if (whereType.get(strKey) == null){
                    type = QBDataType.String;
                    opType = QBOperatorType.OR;
                }else{
                    type = (QBDataType)whereType.get(strKey);
                    opType = (QBOperatorType)operatorList.get(strKey);
                }

                String strCond = "";
                if (type == QBDataType.String){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                        compSymb = "='" + whereList.get(strKey).toString() + "' " + strCond;
                    }else if(opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                        compSymb = "='" + whereList.get(strKey).toString() + "' " + strCond;
                    }else if(opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                        compSymb = " LIKE '%" + whereList.get(strKey).toString() + "%' " + strCond;
                    }else if(opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                        compSymb = " LIKE '%" + whereList.get(strKey).toString() + "%' " + strCond;
                    }

                }else if (type == QBDataType.Numeric){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }else if(opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if(opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if(opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }
                    compSymb = "=" + whereList.get(strKey).toString() + " " + strCond;
                }else if(type == QBDataType.Date){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }else if(opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if(opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if(opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }
                    compSymb = "=" + whereList.get(strKey).toString() + " " + strCond;
                }

                if (i == 1)
                {
                    whereCondition = " WHERE " + strKey + compSymb;
                }
                else
                {
                    whereCondition += " " + strKey + compSymb;
                }

            }
            
            tQuery = "DELETE FROM " + getLongTableName() + whereCondition;
        }catch(Exception ex0){
            //.exxx.
        }
        return tQuery;
    }
    
    public String getUpdateQuery() {
        String tQuery = "";
        String tField = "";
        String tmpVal = "";
        String compSymb = "";
        String whereCondition = "";
        int i = 0;
        try{
            QBDataType type;
            QBOperatorType opType;

            String strKey;
            Set set = columnList.keySet();
            Iterator iter = set.iterator();

            while (iter.hasNext()) {
                i++;
                strKey = (String) iter.next();
                if (columnType.get(strKey) == null){
                    type = QBDataType.String;
                }else{
                    type = (QBDataType)columnType.get(strKey);
                }
                
                if (type == QBDataType.String){
                    tmpVal = columnList.get(strKey).toString();//.replace("'", "''"); //. handle for char '
                    tmpVal = "'" + tmpVal + "'";
                }else if (type == QBDataType.Numeric){
                    tmpVal = columnList.get(strKey).toString();
                }else if (type == QBDataType.Date){
                    tmpVal = "'" + columnList.get(strKey).toString() + "'";
                }


                if (i > 1) {
                    //. tField += "," + strKey + "=" + tmpVal;
                    tField += "," + addIdentifier (strKey) + "=" + tmpVal;
                }else{
                    //. tField += strKey + "=" + tmpVal;
                    tField += addIdentifier (strKey) + "=" + tmpVal;
                }

            }

            set = whereList.keySet();
            iter = set.iterator();
            i = 0;
            while (iter.hasNext()){
                i++;
                strKey = (String) iter.next();
                if (whereType.get(strKey) == null) {
                    type = QBDataType.String;
                    opType = QBOperatorType.OR;
                }else{
                    type = (QBDataType)whereType.get(strKey);
                    opType = (QBOperatorType)operatorList.get(strKey);
                }

                String strCond = "";

                if (type == QBDataType.String){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                        compSymb = "='" + whereList.get(strKey).toString() + "' " + strCond;
                    }else if (opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                        compSymb = "='" + whereList.get(strKey).toString() + "' " + strCond;
                    }else if (opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                        compSymb = " LIKE '%" + whereList.get(strKey).toString() + "%' " + strCond;
                    }else if (opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                        compSymb = " LIKE '%" + whereList.get(strKey).toString() + "%' " + strCond;
                    }
                    
                }else if(type == QBDataType.Numeric){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }else if (opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if (opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if (opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }
                    compSymb = "=" + whereList.get(strKey).toString() + " " + strCond;
                }else if (type == QBDataType.Date){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }else if (opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if (opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if (opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }
                    compSymb = "=" + whereList.get(strKey).toString() + " " + strCond;
                }

                if (i == 1){
                    whereCondition = " WHERE " + strKey + compSymb;
                }else {
                    whereCondition += " " + strKey + compSymb;
                }
            }
            tQuery = "UPDATE " + getLongTableName() + " SET " + tField + whereCondition;
        }catch(Exception ex0){
            //.exxx.
        }
        return tQuery;
    }

    public String getInsertQuery(){
        String tQuery = "";
        String tField = "";
        String tValue = "";
        String tmpVal = "";
        int i = 0;
        try{
            QBDataType type;

            String strKey;
            Set set = columnList.keySet();
            Iterator iter = set.iterator();

            while (iter.hasNext()) {
                i++;
                strKey = (String) iter.next();
                if (columnType.get(strKey) == null){
                    type = QBDataType.String;
                }else{
                    type = (QBDataType)columnType.get(strKey);
                }

                if (type == QBDataType.String){
                    tmpVal = columnList.get(strKey).toString(); //.replace("'", "''"); //. handle for char '
                    tmpVal = "'" + tmpVal + "'";
                }else if (type == QBDataType.Numeric){
                    tmpVal = columnList.get(strKey).toString();
                }else if (type == QBDataType.Date){
                    tmpVal = "'" + columnList.get(strKey).toString() + "'";
                }

                if (i > 1){
                    //tField += "," + strKey;
                    tField += "," + addIdentifier (strKey);                
                    tValue += "," + tmpVal;
                }else{
                    //tField += strKey;
                    tField += addIdentifier (strKey);
                    tValue += tmpVal;
                }
            }

            tQuery = "INSERT INTO " + getLongTableName() + "(" + tField + ") VALUES (" + tValue +")";
        } catch(Exception ex0){
            //.exxx.
        }
        return tQuery;

    }
    
    public String getSelectQuery(){
        String tQuery = "";
        String tField = "";
        String whereCondition = "";
        String compSymb = "";
        int i = 0;
        try{
            QBDataType type;
            QBOperatorType opType;

            String strKey;
            Set set = whereList.keySet();
            Iterator iter = set.iterator();

            while (iter.hasNext()) {
                i++;
                strKey = (String) iter.next();
                if (whereType.get(strKey) == null)
                {
                    type = QBDataType.String;
                    opType = QBOperatorType.OR;
                }else{
                    type = (QBDataType)whereType.get(strKey);
                    opType = (QBOperatorType)operatorList.get(strKey);
                }

                String strCond = "";

                if (type == QBDataType.String){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()){ strCond = "";}else{strCond = "AND";}
                        compSymb = "='" + whereList.get(strKey).toString() + "' " + strCond;
                    }else if(opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                        compSymb = "='" + whereList.get(strKey).toString() + "' " + strCond;
                    }else if(opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                        compSymb = " LIKE '%" + whereList.get(strKey).toString() + "%' " + strCond;
                    }else if(opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                        compSymb = " LIKE '%" + whereList.get(strKey).toString() + "%' " + strCond;
                    }
                }else if (type == QBDataType.Numeric){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()){ strCond = "";}else{strCond = "AND";}
                    }else if(opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if(opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if(opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }
                    compSymb = "=" + whereList.get(strKey).toString() + " " + strCond;
                }else if (type == QBDataType.Date){
                    if (opType == QBOperatorType.AND){
                        if (i >= whereList.size()){ strCond = "";}else{strCond = "AND";}
                    }else if(opType == QBOperatorType.OR){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if(opType == QBOperatorType.ORLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if(opType == QBOperatorType.ANDLIKE){
                        if (i >= whereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }
                    compSymb = "=" + whereList.get(strKey).toString() + " " + strCond;
                }

                if (i == 1){
                    whereCondition = " WHERE " + strKey + compSymb;
                }else{
                    whereCondition += " " + strKey + compSymb;
                }
            }


            i=0;
            set = columnList.keySet();
            iter = set.iterator();

            while (iter.hasNext()) {
                i++;
                strKey = (String) iter.next();
                if (i > 1){
                    tField += "," + strKey;
                }else{
                    tField += strKey;
                }

            }

            tQuery = "SELECT " + tField + " FROM " + getLongTableName() + whereCondition;
        } catch(Exception ex0){
            //.exxx.
        }
        return tQuery;
    }




    public void addWhere(String fieldName, Object conditionValue, QBDataType type, QBOperatorType opType){
        if (fieldName == null) return;
        Object cond;
        
        if (conditionValue == null){
            type = QBDataType.Numeric;
            cond = "NULL";
        }else{
            if (conditionValue instanceof String){
                conditionValue = bufferFieldValue((String)conditionValue);
            }
            cond = conditionValue;
        }
        
        whereType.put(fieldName, type);
        whereList.put(fieldName, cond);
        operatorList.put(fieldName, opType);
    }
    
    public void addWhere(String fieldName, Object conditionValue){
        if (fieldName == null) return;
        Object cond;
        if (conditionValue == null){
            whereType.put(fieldName, QBDataType.Numeric);
            cond = "NULL";
        }else{
            if (conditionValue instanceof String){
                conditionValue = bufferFieldValue((String)conditionValue);
            }
            cond = conditionValue;
            whereType.put(fieldName, QBDataType.String);
        }
        
        
        whereList.put(fieldName, cond);
        operatorList.put(fieldName, QBOperatorType.AND);
    }

    public void removeWhere(String fieldName){
        if (fieldName == null) return;

        if (whereType.containsKey(fieldName)){
            whereList.remove(fieldName);
            whereType.remove(fieldName);
        }
    }

    public void addColumn(String colName, Object value, QBDataType type){
        if (colName == null || value == null) return;
        if (value instanceof String){
            value = bufferFieldValue((String)value);
        }
        columnType.put(colName, type);
        columnList.put(colName,value);
    }
    
    public void addColumn(String colName, Object value){
        if (colName == null || value == null) return;
        if (value instanceof String){
            value = bufferFieldValue((String)value);
        }
        columnType.put(colName, QBDataType.String);
        columnList.put(colName,value);
    }

    public void removeColumn(String colName){
        if (colName == null) return;

        if (columnList.containsKey(colName)) {
            columnType.remove(colName);
            columnList.remove(colName);
        }
    }

    public void removeAllWhere(){
        whereList.clear();
        whereType.clear();
    }

    public void removeAllColumn(){
        columnList.clear();
        columnType.clear();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public java.lang.String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(java.lang.String schemaName) {
        this.schemaName = schemaName;
    }
    
    public String getLongTableName(){
        if (this.schemaName != null){
            return addIdentifier(this.schemaName) + "." + addIdentifier(tableName);
        }
        
        return addIdentifier(this.tableName);
    }

    public QBDBType getDbtype() {
        return dbtype;
    }

    public void setDbtype(QBDBType dbtype) {
        this.dbtype = dbtype;
    }
    
    public String addIdentifier(String dbObject){
        String result = "";
        switch(this.dbtype){
            case MYSQL:
                result = COLUMN_TABLE_IDENTIFIER_GENERAL + dbObject + COLUMN_TABLE_IDENTIFIER_GENERAL;
                break;
            case SYBASE:
                result = COLUMN_TABLE_IDENTIFIER_GENERAL + dbObject + COLUMN_TABLE_IDENTIFIER_GENERAL;
                break;
            case MSSQL:
                result = COLUMN_TABLE_IDENTIFIER_OPEN + dbObject + COLUMN_TABLE_IDENTIFIER_CLOSE;
                break;
            case POSTGRE:
                result = COLUMN_TABLE_IDENTIFIER_GENERAL + dbObject + COLUMN_TABLE_IDENTIFIER_GENERAL;
                break;
            default:
                result = COLUMN_TABLE_IDENTIFIER_GENERAL + dbObject + COLUMN_TABLE_IDENTIFIER_GENERAL;
                break;
        }
        
        return result;
    }
    
    private String bufferFieldValue(String originalVal){
        return originalVal.replaceAll("'", "''");
    }
    
    
    
    //.{APM:20141015}--------untuk versi MM:-----------------------------------:
    
    public void MMRemoveAllWhere(){
        arrMMWhereList.clear();
    }
    
    public void MMAddWhere(String fieldName, Object conditionValue){
        if (fieldName == null) return;
        //.preset item:
        MMKVItem mWhereItem = new MMKVItem();
        mWhereItem.oKey = fieldName;
        mWhereItem.oValue = conditionValue;
        arrMMWhereList.add(mWhereItem);
        //.set item:
        Object cond;
        if (conditionValue == null){
            mWhereItem.oType = QBDataType.Numeric;
            cond = "NULL";
        }else{
            if (conditionValue instanceof String){
                conditionValue = bufferFieldValue((String)conditionValue);
            }
            cond = conditionValue;
            mWhereItem.oType = QBDataType.String;
        }
        
        mWhereItem.oValue = cond;
        mWhereItem.oOperator = QBOperatorType.AND;
    }

    
    public void MMAddWhere(String fieldName, Object conditionValue, QBDataType type, QBOperatorType opType){
        if (fieldName == null) return;
        //.preset item:
        MMKVItem mWhereItem = new MMKVItem();
        mWhereItem.oKey = fieldName;
        mWhereItem.oValue = conditionValue;
        arrMMWhereList.add(mWhereItem);
        //.set item:
        Object cond;
        
        if (conditionValue == null){
            type = QBDataType.Numeric;
            cond = "NULL";
        }else{
            if (conditionValue instanceof String){
                conditionValue = bufferFieldValue((String)conditionValue);
            }
            cond = conditionValue;
        }
        
        mWhereItem.oType = type;
        mWhereItem.oValue = cond;
        mWhereItem.oOperator = opType;
    }
    
    
    public String MMGetUpdateQuery() {
        String tQuery = "";
        String tField = "";
        String tmpVal = "";
        String compSymb = "";
        String whereCondition = "";
        int i = 0;
        try{
            QBDataType type;
            QBOperatorType opType;

            String strKey;
            Set set = columnList.keySet();
            Iterator iter = set.iterator();

            while (iter.hasNext()) {
                i++;
                strKey = (String) iter.next();
                if (columnType.get(strKey) == null){
                    type = QBDataType.String;
                }else{
                    type = (QBDataType)columnType.get(strKey);
                }
                
                if (type == QBDataType.String){
                    tmpVal = columnList.get(strKey).toString();//.replace("'", "''"); //. handle for char '
                    tmpVal = "'" + tmpVal + "'";
                }else if (type == QBDataType.Numeric){
                    tmpVal = columnList.get(strKey).toString();
                }else if (type == QBDataType.Date){
                    tmpVal = "'" + columnList.get(strKey).toString() + "'";
                }


                if (i > 1) {
                    //. tField += "," + strKey + "=" + tmpVal;
                    tField += "," + addIdentifier (strKey) + "=" + tmpVal;
                }else{
                    //. tField += strKey + "=" + tmpVal;
                    tField += addIdentifier (strKey) + "=" + tmpVal;
                }
                
            }
            
            //.SOU{APM:20141015}-----------------------------------------------;
            iter = arrMMWhereList.iterator();
            i = 0;
            while (iter.hasNext()){
                i++;
                MMKVItem mmItem = (MMKVItem) iter.next();
                if (mmItem.oType == null) {
                    type = QBDataType.String;
                    opType = QBOperatorType.OR;
                }else{
                    type = (QBDataType)mmItem.oType;
                    opType = (QBOperatorType)mmItem.oOperator;
                }

                String strCond = "";

                if (type == QBDataType.String){
                    if (opType == QBOperatorType.AND){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "AND"; }
                        compSymb = "='" + mmItem.oValue.toString() + "' " + strCond;
                    }else if (opType == QBOperatorType.OR){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "OR"; }
                        compSymb = "='" + mmItem.oValue.toString() + "' " + strCond;
                    }else if (opType == QBOperatorType.ORLIKE){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "OR"; }
                        compSymb = " LIKE '%" + mmItem.oValue.toString() + "%' " + strCond;
                    }else if (opType == QBOperatorType.ANDLIKE){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "AND"; }
                        compSymb = " LIKE '%" + mmItem.oValue.toString() + "%' " + strCond;
                    }
                    
                }else if(type == QBDataType.Numeric){
                    if (opType == QBOperatorType.AND){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }else if (opType == QBOperatorType.OR){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if (opType == QBOperatorType.ORLIKE){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if (opType == QBOperatorType.ANDLIKE){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }
                    compSymb = "=" + mmItem.oValue.toString() + " " + strCond;
                }else if (type == QBDataType.Date){
                    if (opType == QBOperatorType.AND){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }else if (opType == QBOperatorType.OR){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if (opType == QBOperatorType.ORLIKE){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "OR"; }
                    }else if (opType == QBOperatorType.ANDLIKE){
                        if (i >= arrMMWhereList.size()) { strCond = ""; } else { strCond = "AND"; }
                    }
                    compSymb = "=" + mmItem.oValue.toString() + " " + strCond;
                }
                
                if (i == 1){
                    whereCondition = " WHERE " + mmItem.oKey.toString() + compSymb;
                }else {
                    whereCondition += " " + mmItem.oKey.toString() + compSymb;
                }
                //.EOU{APM:20141015}-------------------------------------------;
            }
            tQuery = "UPDATE " + getLongTableName() + " SET " + tField + whereCondition;
        }catch(Exception ex0){
            //.exxx.
        }
        return tQuery;
    }

}
