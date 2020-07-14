
package com.Class;

import com.SQL.SQLConnection;
import com.Class.Institution.InstitutionType;
import com.SQL.SQLQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsGroup extends MainClassProgect<UsGroup>{
    private Integer Id = 0;  
    private String Name = "";  
    private String NameLong = "";  
    private String Note = ""; 
    private Integer RegionId = 0;    
    private Integer TypeUnitId = 0;    
    private String TypeUnitName = "";    
    private UsGroupType Type = UsGroupType.GENERAL;    
    private Boolean isStaffGroup = false;    

    private String querySelectIdFromName;
    private String querySelectNameFromId;
    private String querySelect;
    
    private String TableName = "UsGroup";
    
    public UsGroup(){
    }
    
    @Override // не определено
    public Object[] getDataForTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    // ========================== установки sql ============================
    // ==================== геттеры SELECT запосов====================
    @Override
    public String getQuerySelect(Integer Limit, Integer InstitutionId) {
         String str = "SELECT "
                + "UG.`Id`, UG.`Name`, UG.`NameLong`, UG.`Note`, UG.`RegionId`, UG.TypeUnitId, UG.Type, TypeUnit.name, UG.isStaffGroup "
                + "FROM `UsGroup` UG "
                + "Left join TypeUnit on (UG.TypeUnitId = TypeUnit.id) "
                + "%1where " 
                + "GROUP BY UG.`id`"
                + (Limit <= 0 ? "" : "Limit "+Limit.toString());
        str = str.replace("%1where", (InstitutionId <= 0 ? "" : "Where UG.Id = "+ InstitutionId.toString()));
        return str; 
    }
    @Override // не определено
    public String getQuerySelectTable(Integer Limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override // не определено
    public String getQueryLastKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override // не определено
    public String getQueryIsExistKey(Integer Key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // ==================== геттеры UPSERT запосов====================
    @Override
    public String getQueryIsExistKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // ==================== геттеры UPSERT запосов====================
    @Override // не определено
    public String getQueryUpdate(Integer UnitId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override // не определено
    public String getQueryInsert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ==================== ResultSet для SELECT ====================
    @Override
    public UsGroup getDataFromResultSet(ResultSet rs) throws SQLException{
        UsGroup UsGroup = new UsGroup();
                
        UsGroup.setId(rs.getInt(1));                
        UsGroup.setName(rs.getString(2));                
        UsGroup.setNameLong(rs.getString(3));                
        UsGroup.setNote(rs.getString(4));                              
        UsGroup.setRegionId(rs.getInt(5));                              
        UsGroup.setTypeUnitId(rs.getInt(6));
        UsGroup.setType(UsGroupType.valueOf(rs.getString(7)));
        UsGroup.setTypeUnitName(rs.getString(8));
        UsGroup.setIsStaffGroup(rs.getBoolean(9));
             
        return UsGroup;
    }
    @Override // не определено
    public UsGroup getDataFromResultSetTable(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // ==================== PreparedStatement для UPSERT  ====================
    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException {
        ps.setString(1, this.getName());              
        ps.setString(2, this.getNameLong());              
        ps.setString(3, this.getNote());  
        ps.setInt(4, this.getRegionId());  
        ps.setInt(5, this.getTypeUnitId());  
        ps.setString(6, this.getType().toString());  
        ps.setBoolean(7, this.getIsStaffGroup());  
    }
        
    // прочее    
    public Integer getIdByName(String GroupName, SQLConnection SQLConnection){
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        Integer IdGroup = -1;
        try{            
            querySelectIdFromName = querySelectIdFromName.replace("%1where", "Where G.`Name`='"+GroupName+"' ");
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(querySelectIdFromName));
            ResultSet rs = SQLConnection.getRs();
            while (rs.next()){        
                IdGroup = (rs.getInt(1));                                             
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            if (IsNewCon) SQLConnection.closeConnection();
        } 
    return IdGroup;
    } // ok
    public DefaultComboBoxModel getNamesUsGroup(SQLConnection Connection){
        System.out.println("com.Class.UsGroup.getNamesUsGroup() - start ");
        DefaultComboBoxModel MyDCB = new DefaultComboBoxModel();
        Boolean IsNewCon = false;
        if (Connection.getCon()==null || Connection.getStmt()==null){
            Connection.getConnection();
            IsNewCon = true;
        }
        try{            
            String query = "SELECT "
                + "`name` "
                + "FROM `UsGroup` "
                + "Where id > 0 "
                + "GROUP BY `id`";
            Connection.setRs(Connection.getStmt().executeQuery(query));
            ResultSet rs = Connection.getRs();
            while (rs.next()) {                 
                MyDCB.addElement(rs.getString(1));
            }
            rs.close();
            System.out.println("com.Class.UsGroup.getNamesUsGroup() - and ");
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            Connection.closeConnection();
            return MyDCB;
        } finally {
            if (IsNewCon) Connection.closeConnection();
        } 
        return MyDCB;
    }
    
    public TreeMap<String,Integer> getNameAndId(SQLConnection SQLConnection){
        TreeMap<String,Integer> strint = new TreeMap<>();
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        Integer IdGroup = -1;
        try{            
            String query = "SELECT "
                + "`Id`, `Name` "
                + "FROM `UsGroup` "
                + "GROUP BY `id`";                
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(query));
            ResultSet rs = SQLConnection.getRs();
            while (rs.next()) {
                strint.put(rs.getString(2), rs.getInt(1));
            }
            rs.close();
        } catch (SQLException sqlEx) {
            System.out.println("com.Class.UsPosition.getNameAndIdByUsDepartmentId() - " + sqlEx.getMessage());
            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            if (IsNewCon) SQLConnection.closeConnection();
        } 
    return strint;
    } // ok 
    
    
    public enum UsGroupType{
        GENERAL,
        SCHOOL,
        KINDERGARDEN,
        SOCIAL,
        HOSPITALS
    }
    
    
//    public DefaultComboBoxModel getNamesUsGroupByInstitutionType(SQLConnection Connection, InstitutionType InstitutionType){
//        System.out.println("com.Class.UsGroup.getNamesUsGroup() - start ");
//        DefaultComboBoxModel MyDCB = new DefaultComboBoxModel();
//        Boolean IsNewCon = false;
//        if (Connection.getCon()==null || Connection.getStmt()==null){
//            Connection.getConnection();
//            IsNewCon = true;
//        }
//        try{            
//            String query = "SELECT "
//                + "`name` "
//                + "FROM `UsGroup` "
//                + "Where id > 0 "
//                + "GROUP BY `id`";
//            Connection.setRs(Connection.getStmt().executeQuery(query));
//            ResultSet rs = Connection.getRs();
//            while (rs.next()) {                 
//                MyDCB.addElement(rs.getString(1));
//            }
//            rs.close();
//            System.out.println("com.Class.UsGroup.getNamesUsGroup() - and ");
//        } catch (SQLException sqlEx) {
//            sqlEx.getMessage();
//            Connection.closeConnection();
//            return MyDCB;
//        } finally {
//            if (IsNewCon) Connection.closeConnection();
//        } 
//        return MyDCB;
//    } 

    
}
