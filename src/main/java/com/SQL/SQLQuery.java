package com.SQL;

import com.Class.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;


public class SQLQuery<AbstactClass extends MainClassProgect> extends SQLConnection implements Runnable{

    private AbstactClass AbstactUnit;
    
    @Override
    public void run() {       
    }

    public SQLQuery(AbstactClass Testobject){        
        AbstactUnit = Testobject;    
    }   
    
    // ======================== get query Update or Insert ========================
    private String getQueryUpdateOrInsert(Integer UnitId) throws SQLException{        
        String str = "";
        String strUpdate = "";
        String strInsert = "";
        str = AbstactUnit.getQueryIsExistKey();
        strUpdate = AbstactUnit.getQueryUpdate(UnitId);
        strInsert = AbstactUnit.getQueryInsert();

        ResultSet rs = getStmt().executeQuery(str);   
        int KeyQuery = 0;
        while (rs.next()) { KeyQuery = rs.getInt(1);} // = 0 - номера нет в базе; = 1 - номер есть
        rs.close();
        if (KeyQuery > 0){ // если ключ есть обновлем запись
            str = strUpdate;
        }else { // если нет добавляем запись
            str = strInsert;    
        }  
        return str;
    }    
    // ======================== ======================== ========================
    // ======================== mySQL SELECT ========================
    public AbstactClass Read(Integer UnitId){
        if (UnitId!=null && UnitId>0){
            TreeMap <Integer,AbstactClass> TreeMap = Read(-1, UnitId);
            AbstactUnit = TreeMap.get(TreeMap.firstKey());
        }
    return AbstactUnit;
    }
    public TreeMap <Integer,AbstactClass> Read(Integer Limit, Integer UnitId){
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
        TreeMap <Integer,AbstactClass> TreeMap = new TreeMap();
        try{                                               
            setRs(getStmt().executeQuery(AbstactUnit.getQuerySelect(Limit, UnitId)));
            ResultSet rs = getRs();
            while (rs.next()) { 
                if (rs.getInt(1)>0)
                    TreeMap.put(rs.getInt(1), (AbstactClass) AbstactUnit.getDataFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            System.out.println("com.SQL.SQLQuery.SQLSelect() - " + sqlEx.getMessage());
            closeConnection();
        } finally {
            if (IsNewCon) closeConnection();
        } 
    return TreeMap;
    }    
    public TreeMap <Integer,AbstactClass> ReadForTable(Integer Limit){
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
        TreeMap <Integer,AbstactClass> TreeMap = new TreeMap();
        try{                                               
            setRs(getStmt().executeQuery(AbstactUnit.getQuerySelectTable(Limit)));
            ResultSet rs = getRs();
            while (rs.next()) {                           
                TreeMap.put(rs.getInt(1), (AbstactClass) AbstactUnit.getDataFromResultSetTable(rs));
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            System.out.println("com.SQL.SQLQuery.SQLSelectTable() -" + sqlEx.getMessage());
            closeConnection();
        }catch (NullPointerException ex){
            ex.getMessage();
            System.out.println("com.SQL.SQLQuery.SQLSelectTable() -" + ex.getMessage());
            closeConnection();
        } finally {
            if (IsNewCon) closeConnection();
        } 
    return TreeMap;
    }
    
    public Integer getLastKey(){
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
        Integer Id = 0;
        try{
            
            setRs(getStmt().executeQuery(AbstactUnit.getQueryLastKey()));
            ResultSet rs = getRs();
            while (rs.next()) {                           
                Id = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            System.out.println("com.SQL.SQLQuery.SQLSelectLastKey() -" + sqlEx.getMessage());
            closeConnection();
        } finally {
            if (IsNewCon) closeConnection();
        } 
        return Id;
    }   
    
    public Integer isKeyExist(Integer Key){
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
        Integer Id = 0;
        try{
            
            setRs(getStmt().executeQuery(AbstactUnit.getQueryIsExistKey(Key)));
            ResultSet rs = getRs();
            while (rs.next()) {                           
                Id = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            System.out.println("com.SQL.SQLQuery.SQLSelectIsKeyExist()- " +sqlEx.getMessage());
            closeConnection();
        } finally {
            if (IsNewCon) closeConnection();
        } 
        return Id;
    }  
    
    // ======================== ======================== ========================
    // ======================== mySQL UPSERT ========================
    public boolean SaveOrWrite(Integer UnitId){
        Boolean status = true;
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
            try{                           
                PreparedStatement ps = getCon().prepareStatement(getQueryUpdateOrInsert(UnitId));
                AbstactUnit.setPreparedStatment(ps);       
                ps.executeUpdate();
                ps.close(); 
            } catch (SQLException sqlEx) {         
                sqlEx.getMessage();
                System.out.println("com.SQL.SQLQuery.SQLUpdateOrInsert() - " + sqlEx.getMessage());
                status = false;
                closeConnection();            
            } finally {
                if (IsNewCon) closeConnection();
            } 
    return status;
    }    
    public boolean Save(Integer UnitId){
        Boolean status = true;
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
            try{                           
                PreparedStatement ps = getCon().prepareStatement(AbstactUnit.getQueryUpdate(UnitId));
                AbstactUnit.setPreparedStatment(ps);       
                ps.executeUpdate();
                ps.close(); 
            } catch (SQLException sqlEx) {         
                sqlEx.getMessage();
                System.out.println("com.SQL.SQLQuery.SQLUpdate()- " + sqlEx.getMessage());
                status = false;
                closeConnection();            
            } finally {
                if (IsNewCon) closeConnection();
            } 
    return status;
    }
    public boolean Write(){
        Boolean status = true;
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
            try{                           
                PreparedStatement ps = getCon().prepareStatement(AbstactUnit.getQueryInsert());
                AbstactUnit.setPreparedStatment(ps);       
                ps.executeUpdate();
                ps.close(); 
            } catch (SQLException sqlEx) {         
                sqlEx.getMessage();
                System.out.println("com.SQL.SQLQuery.SQLInsert() - " + sqlEx.getMessage());
                status = false;
                closeConnection();            
            } finally {
                if (IsNewCon) closeConnection();
            } 
    return status;
    }
    
    
    public boolean ChangeCuratorInInstitution(String Institutions, Integer CuratorId){
        Boolean isUpdate = false;
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
        try{            
            String query = "UPDATE `Institution` SET `EmployeeCuratorId` = "+CuratorId+" WHERE `Id` in ("+Institutions+")";    
            PreparedStatement ps = getCon().prepareStatement(query);     
            ps.executeUpdate();
            ps.close(); 
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            System.out.println("com.SQL.SQLQuery.ChangeCuratorInInstitution() - " + sqlEx.getMessage());
            closeConnection();
            isUpdate = false;
        } finally {
            if (IsNewCon) closeConnection();
        } 
        return isUpdate;
    }
    
    public DefaultComboBoxModel getNames(){
        //System.out.println("com.Class.UsGroup.getNamesUsGroup() - start ");
        DefaultComboBoxModel MyDCB = new DefaultComboBoxModel();
        Boolean IsNewCon = false;
        if (!IsConnected()){
            getConnection();
            IsNewCon = true;
        }
        try{            
            String query = "SELECT "
                + "`name` "
                + "FROM `"+AbstactUnit.getTableName()+"` "
                + "Where id > 0 "
                + "GROUP BY `id`";
            setRs(getStmt().executeQuery(query));
            ResultSet rs = getRs();
            while (rs.next()) {                 
                MyDCB.addElement(rs.getString(1));
            }
            rs.close();
            //System.out.println("com.SQL.SQLQuery.getNames() - and ");
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            System.out.println("com.SQL.SQLQuery.getNames() - " + sqlEx.getMessage());
            closeConnection();
            return MyDCB;
        } finally {
            if (IsNewCon) closeConnection();
        } 
        return MyDCB;
    }
    
}


