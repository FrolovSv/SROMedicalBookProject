
package com.Class;

import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import lombok.Data;

@Data
public class UsPosition extends MainClassProgect<UsPosition>{
    private Integer Id = -1;  
    private String Name = "";    
    private String Note = ""; 
    private String UsDepartmentName = "";  
    private String UsGroupName = ""; 
    private Integer UsDepartmentId = -1; 
    private Integer UsGroupId = -1; 
    private Integer RegionId = -1; 

    public UsPosition(){}
       
    @Override // не определен
    public Object[] getDataForTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // ========================== установки sql ============================
    // ==================== геттеры  SELECT ====================
    @Override
    public String getQuerySelect(Integer Limit, Integer InstitutionId){
        String str = "SELECT "
                + "UP.`Id`, UP.`Name`, UP.`Note`, UP.`UsDepartmentId`, UP.`UsGroupId`, "
                + "UsDepartment.name, UsGroup.name, UP.`RegionId` "
                + "FROM `UsPosition` UP "
                + (InstitutionId <= 0 ? "" : "Where UD.Id = "+InstitutionId)
                + "LEFT join `UsDepartment` on (UP.UsDepartmentId = UsDepartment.Id) "
                + "LEFT join `UsGroup` on (UP.UsGroupId = UsGroup.Id) "
                + "GROUP BY UP.`id`"
                + (Limit <= 0 ? "" : "Limit "+Limit.toString());   
        return str;                
    }    
    @Override // не определен
    public String getQuerySelectTable(Integer Limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override // не определен
    public String getQueryLastKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override // не определен
    public String getQueryIsExistKey(Integer Key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getQueryIsExistKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ==================== геттеры  UPSERT ====================
    @Override // не определен
    public String getQueryUpdate(Integer UnitId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override // не определен
    public String getQueryInsert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // ==================== PreparedStatment для UPSERT запросов ====================
    @Override // не определен
    public void setPreparedStatment(PreparedStatement ps) throws SQLException {
        ps.setString(1, this.getName());              
        ps.setString(2, this.getNote());              
        ps.setInt(3, this.getUsDepartmentId());                      
        ps.setInt(4, this.getUsGroupId());                                    
        ps.setInt(5, this.getRegionId());
    }
    
    // ==================== ResultSet для SELECT запросов ====================
    @Override
    public UsPosition getDataFromResultSet(ResultSet rs) throws SQLException{
        UsPosition UsPosition = new UsPosition();                
        UsPosition.setId(rs.getInt(1));                
        UsPosition.setName(rs.getString(2));                
        UsPosition.setNote(rs.getString(3));                
        UsPosition.setUsDepartmentId(rs.getInt(4));                
        UsPosition.setUsGroupId(rs.getInt(5));                
        UsPosition.setUsDepartmentName(rs.getString(6));                
        UsPosition.setUsGroupName(rs.getString(7));                
        UsPosition.setRegionId(rs.getInt(8));                        
        return UsPosition;
    }
    @Override // не определен
    public UsPosition getDataFromResultSetTable(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
           
    // Прочее
    public Integer getIdByNameUsDep(String Name, Integer DepartmentId, SQLConnection SQLConnection){

        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        Integer IdGroup = -1;
        try{            
            String query = "SELECT "
                + "`Id` "
                + "FROM `UsPosition` "
                + "Where `name` = '" + Name +"' "
                + "and UsDepartmentId = " + DepartmentId + " "
                + "GROUP BY `id`";    
            
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(query));
            ResultSet rs = SQLConnection.getRs();
            while (rs.next()) {
                IdGroup = rs.getInt(1);                
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
    
    
    public TreeMap<Integer,String> getNameAndIdByUsDepartmentId(Integer DepartmentId, SQLConnection SQLConnection){
        TreeMap<Integer,String> strint = new TreeMap<>();
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        Integer IdGroup = -1;
        try{            
            String query = "SELECT "
                + "`Id`, `Name` "
                + "FROM `UsPosition` "
                + "Where UsDepartmentId = " + DepartmentId + " "
                + "GROUP BY `id`";                
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(query));
            ResultSet rs = SQLConnection.getRs();
            while (rs.next()) {
                strint.put(rs.getInt(1), rs.getString(2));
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
    
    
    public DefaultComboBoxModel getNameByUSDeparymenName(String NameUsDepartment, SQLConnection SQLConnection){
        DefaultComboBoxModel MyDCB = new DefaultComboBoxModel();
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        //Integer IdGroup = -1;
        try{            
            String query = "SELECT "
                + "P.`Id`, P.`name` "
                + "FROM `UsPosition` P "
                + "Left join UsDepartment on (UsDepartment.`id` = P.`UsDepartmentId`) "
                + "Where UsDepartment.`name` = '" + NameUsDepartment +"' "
                + "GROUP BY P.`id`";    
            
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(query));
            ResultSet rs = SQLConnection.getRs();
            while (rs.next()) {
                MyDCB.addElement(rs.getString(2));                
            }
            rs.close();
        } catch (SQLException sqlEx) {

            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            if (IsNewCon) SQLConnection.closeConnection();
        } 
    return MyDCB;
    } // ok
//    public DefaultComboBoxModel getIdByName(String NameUsDepartment, String Name, SQLConnection SQLConnection){
//        DefaultComboBoxModel MyDCB = new DefaultComboBoxModel();
//        TreeMap<Integer,Employee> Tree = new TreeMap<>();
//        Boolean IsNewCon = false;
//        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
//            SQLConnection.getConnection();
//            IsNewCon = true;
//        }
//        //Integer IdGroup = -1;
//        try{            
//            String query = "SELECT "
//                + "P.`Id`, P.`name` "
//                + "FROM `UsPosition` P "
//                + "Left join UsDepartment on (UsDepartment.`id` = P.`UsDepartmentId`) "
//                + "Where UsDepartment.`name` = '" + NameUsDepartment +"' and UsPosition.Name LIKE '%"+Name+"%' "
//                + "GROUP BY P.`id`";    
//            
//            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(query));
//            ResultSet rs = SQLConnection.getRs();
//            while (rs.next()) {
//                Employee empl = new Employee();
//                empl.setId(rs.getString(1));
//                empl.setName(Id);
//                Tree.put(rs.getString(1),empl);                
//            }
//            rs.close();
//        } catch (SQLException sqlEx) {
//
//            sqlEx.getMessage();
//            SQLConnection.closeConnection();
//        } finally {
//            if (IsNewCon) SQLConnection.closeConnection();
//        } 
//    return MyDCB;
//    } // o
 
}
