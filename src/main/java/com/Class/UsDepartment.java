
package com.Class;

import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import lombok.Data;

@Data
public class UsDepartment extends MainClassProgect<UsDepartment>{
    TreeMap <Integer,UsDepartment> UsDepartmentTM; 
    private Integer Id = -1;
    private String Name = "";
    private String Note = "";
    private Integer UsGroupId = -1;
    private String UsGroupName = "";
    private Integer RegionId = -1;
    private String NameLong = "";
    private String LocalGroup = "";    
    
    // инициализация дефолтовых значений
    public UsDepartment(){   
    }
    
    @Override // не определено
    public Object[] getDataForTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
    // ========================== установки sql ============================
    // ==================== геттеры SELECT====================
    @Override
    public String getQuerySelect(Integer Limit, Integer UsDepartmentId){
        String str = "SELECT "
            + "UD.`Id`, UD.`Name`, UD.`Note`, UD.`UsGroupId`, `UsGroup`.`name`, "
            + "UD.`RegionId`, UD.`NameLong`, UD.`LocalGroup` "
            + "FROM `UsDepartment` UD "
            + (UsDepartmentId <= 0 ? "" : "Where UD.Id = "+UsDepartmentId)
            + "LEFT join `UsGroup` on (UD.UsGroupId = UsGroup.Id) "
            + "GROUP BY UD.`id` "
            + (Limit <= 0 ? "" : "Limit "+Limit.toString());
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
    
    @Override
    public String getQueryIsExistKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ==================== геттеры UPSERT ====================
    @Override // не определено
    public String getQueryUpdate(Integer UnitId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override // не определено
    public String getQueryInsert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    // ==================== ResultSet для SELECT запросов ====================
    @Override
    public UsDepartment getDataFromResultSet(ResultSet rs) throws SQLException{
        UsDepartment UsDepartment = new UsDepartment();                
        UsDepartment.setId(rs.getInt(1));                
        UsDepartment.setName(rs.getString(2));                
        UsDepartment.setNote(rs.getString(3));                
        UsDepartment.setUsGroupId(rs.getInt(4));                
        UsDepartment.setUsGroupName(rs.getString(5));                
        UsDepartment.setRegionId(rs.getInt(6));                
        UsDepartment.setNameLong(rs.getString(7));                
        UsDepartment.setLocalGroup(rs.getString(8));                
        return UsDepartment;
    }
    @Override // не определено
    public UsDepartment getDataFromResultSetTable(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ==================== ResultSet для UPSERT запросов ====================
    @Override // не определено
    public void setPreparedStatment(PreparedStatement ps) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }            
    
    // ====================== Select ======================   
    public Integer getIdByName(String UsDepartmentName, Integer UsGroupId, SQLConnection SQLConnection){

        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        Integer IdDepartment = -1;
        try{            
            String query = "SELECT "
                + "`Id` "
                + "FROM `UsDepartment` "
                + "Where `name` = '" + UsDepartmentName + "' "
                + " and UsGroupId = " + UsGroupId + " "
                + "GROUP BY `id`";
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(query));
            ResultSet rs = SQLConnection.getRs();
            while (rs.next()) {          
                IdDepartment = rs.getInt(1);                
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            if (IsNewCon) SQLConnection.closeConnection();
        } 
    return IdDepartment;
    }     
    public DefaultComboBoxModel getNamesByUsGroupName(String UsGroupName, SQLConnection SQLConnection){
        DefaultComboBoxModel MyDCB = new DefaultComboBoxModel();
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        String[] Names;
        try{            
            String query = "SELECT "
                    + "D.`id`, D.`Name` "
                    + "FROM `UsDepartment` D "
                    + "Left join UsGroup on (UsGroup.id = D.UsGroupId) "
                    + "WHERE UsGroup.name = '"+UsGroupName+"' "
                    + "GROUP BY D.`id`";
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
    } 
    public Integer getIdByNameUsPos(String GroupName, SQLConnection SQLConnection){
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        Integer IdGroup = -1;
        try{            
            String query = "SELECT " 
                    +"UsDep.`Id`," 
                    +"UsDep.`Name`" 
                    +"FROM `UsDepartment` UsDep " 
                    +"WHERE " 
                    +"UsDep.`Name` Like '%"+GroupName+"%'";
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(query));
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
    
    public TreeMap<String,Integer> getNameAndIdByUsGroupId(Integer GroupId, SQLConnection SQLConnection){
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
                + "FROM `UsDepartment` "
                + "Where UsGroupId = " + GroupId + " "
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

}
