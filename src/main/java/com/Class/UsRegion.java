
package com.Class;

import com.SQL.SQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;


//@Entity
//@Table(name="Region1")
@Data
@EqualsAndHashCode(callSuper=false)
public class UsRegion extends MainClassProgect<UsRegion>{
    
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //@Column
    private Integer Id;    
    //@Column
    private String Name;   
    //@Column
    private String NameLong;     
    //@Column
    private String Note;    

    public UsRegion(){
        setDefaultVariables();
    }
    
    private void setDefaultVariables(){
        Id = -1;  
        Name = "";    
        NameLong = ""; 
        Note = ""; 
    }
    
    // ============================ Установки запросов тиап SELECT ============================
    // =========== отправка запроса в базу на UPDATE и INCERT всех полей класса ===========
    public UsRegion setDataFromResultSet(ResultSet rs) throws SQLException{
        UsRegion Region = new UsRegion(); 
        Region.setId(rs.getInt(1));                
        Region.setName(rs.getString(2));                
        Region.setNameLong(rs.getString(3));                
        Region.setNote(rs.getString(4)); 
       return Region;
    }
    // =========== формирование запроса в базу на SELECT всех полей класса ===========
    public String getSelectQuery(Integer Limit, UsRegion SerchUsRegion){
        String str = "";
        str = "SELECT "
                + "R.`Id`, R.`Name`, R.`NameLong`, R.`Note` "
                + "FROM `Region` R "
                + "GROUP BY `R`.`id`"
                + (Limit<=0? "" : "Limit "+Limit);
        return str; 
    }
    
    public String getSelectQueryOne(Integer UsRegionId){
        String str = "";
        str = "SELECT "
                + "R.`Id`, R.`Name`, R.`NameLong`, R.`Note` "
                + "FROM `Region` R "
                + (UsRegionId <=0 ? "" : "Where R.`Id` = " + UsRegionId);
        return str; 
    }
    
    
    // ============================ Установки запросов тиап UPDATE INSERT ============================
    // =========== отправка запроса в базу на UPDATE и INCERT всех полей класса ===========
    public void getPreparedStatmentFromQuery(UsRegion usRegion, Connection con, String query) throws SQLException{
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, usRegion.getName());
        ps.setString(2, usRegion.getNameLong());
        ps.setString(3, usRegion.getNote());
        ps.executeUpdate();
        ps.close();    
    };
        
    // =========== формирование запроса в базу на UPDATE всех полей класса ===========
    public String getUpdateQuery(Integer UsRegionId){        
        String query  = "UPDATE `Region` SET "
                + "`Name`=?,`NameLong`=?,`Note`=? "
                + "WHERE `Region`.`Id` = " + UsRegionId;
        return query;
    }
    
    public String getInsertQuery(Integer UsRegionId){        
        String query = "INSERT INTO `Region`"
                + "(`Name`, `NameLong`, `Note`) "
                + "VALUES (?,?,?)";  
        return query;
    }
    
    
    
    // ==================== Select не используется ====================
    private TreeMap <Integer,UsRegion> SQLSelectDataRequest(SQLConnection SQLConnection, Integer Limit, UsRegion SerchUsRegion){
        TreeMap <Integer,UsRegion> TreeMapRegion = new TreeMap();
        try{            
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(getSelectQuery(Limit, SerchUsRegion)));
            ResultSet rs = SQLConnection.getRs();
            while (rs.next()) {                             
                TreeMapRegion.put(rs.getInt(1), setDataFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException sqlEx) {
            //addLogsToBase(getClassName()+sqlEx.getLocalizedMessage()+sqlEx.getMessage()); 
            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            
        } 
    return TreeMapRegion;
    }
    
    // ==================== Update and Insert ====================
    public boolean UPSERTRegion(UsRegion Region, SQLConnection SQLConnection){
        Boolean status = true;
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        if (CheckName(SQLConnection)){
            try{ 
                String query;        
                query = "select count(*) from Region where id = "+ Region.getId(); // запрос на наличие ключа в базе
                ResultSet rs = SQLConnection.getStmt().executeQuery(query);   
                int Key = 0;
                while (rs.next()) {
                        Key = rs.getInt(1);
                    }
                rs.close(); 

                if (Key>=1){ // если ключ есть обновлем запись
                    query  = "UPDATE `Region` SET "
                            + "`Name`=?,`NameLong`=?,`Note`=? "
                            + "WHERE `Region`.`Id` = " + Region.getId();
                }else { // если нет добавляем запись
                    query = "INSERT INTO `Region`"
                            + "(`Name`, `NameLong`, `Note`) "
                            + "VALUES ("
                            + "?,?,?)";    
                }        
                    getPreparedStatmentFromQuery(this,SQLConnection.getCon(),query);
            } catch (SQLException sqlEx) {
                sqlEx.getMessage();
                status = false;
                SQLConnection.closeConnection();            
            } finally {
                if (IsNewCon) SQLConnection.closeConnection();
            } 
        } else {
            System.err.println("Таблица \"Округ\". Значение наименовение округа: "+this.getName()+" - не уникальное");
        }
    return status;
    }
    
    // ==================== проверка ====================
    private boolean CheckName(SQLConnection SQLConnection)
    {
        Boolean Result = false;
        try{            
            String query = "SELECT `id` FROM Region WHERE `Name`= '"+this.getName()+"' LIMIT 1";    
            ResultSet rs = SQLConnection.getStmt().executeQuery(query);   
            int Key = 0;
            while (rs.next()) {
                    Key = rs.getInt(1);
                }
            if (Key>0) {
                Result=true;
            }
            rs.close(); 
        } catch (SQLException sqlEx) {
            //addLogsToBase(getClassName()+sqlEx.getLocalizedMessage()+sqlEx.getMessage());
            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            
        } 
            return Result;
    }
    
    
      @Override
    public String toString() {
        return "Region{" + "Id=" + Id + ", Name=" + Name + ", NameLong=" + NameLong + ", Note=" + Note + '}';
    } 

    @Override
    public String getQuerySelect(Integer Limit, Integer UnitId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getQuerySelectTable(Integer Limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getQueryLastKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getQueryIsExistKey(Integer Key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getQueryIsExistKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getQueryUpdate(Integer UnitId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getQueryInsert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UsRegion getDataFromResultSet(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UsRegion getDataFromResultSetTable(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] getDataForTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
