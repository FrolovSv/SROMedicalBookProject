
package com.Class;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuthorizationHistory extends MainClassProgect<AuthorizationHistory>{
    private Integer Id = -1;
    private Integer LoginEmployeeId = -1;
    private Date DateIn = new Date(0);
    private Date DateOut = new Date(0);
    private String Note = "";
    private Integer RegionId = -1;    
    
//    private static final String mySQLTableName = "AuthorizationHistory"; 
//    private static final String[] mySQLColumsName = {"Id","UserLogin", "IdDateIn", "DateOut", "Note", "RegionId"}; 
//    private static final Object[] mySQLColumsClass = {Integer.class,Integer.class, Date.class, Date.class, String.class, Integer.class}; 
    
    public Integer getRegionId() {        return RegionId;    }
    public void setRegionId(Integer RegionId) {        this.RegionId = RegionId;    }
    public Integer getId() {        return Id;    }
    public void setId(Integer Id) {        this.Id = Id;    }
    public Integer getLoginEmployeeId() {        return LoginEmployeeId;    }
    public void setLoginEmployeeId(Integer LoginEmployeeId) {        this.LoginEmployeeId = LoginEmployeeId;    }
    public Date getDateIn() {        return DateIn;    }
    public void setDateIn(Date DateIn) {        this.DateIn = DateIn;    }
    public Date getDateOut() {        return DateOut;    }
    public void setDateOut(Date DateOut) {        this.DateOut = DateOut;    }
    public String getNote() {        return Note;    }
    public void setNote(String Note) {        this.Note = Note;    }
    
    public AuthorizationHistory(){
    }
    
    
    @Override
    public Object[] getDataForTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // ========================== установки sql ============================
    // ==================== геттеры  SELECT ====================
    @Override
    public String getQuerySelect(Integer Limit, Integer AuthorizationHistoryId){
        String str = "SELECT "        
                + getFieldNamesStr("AH",AuthorizationHistory.class.getDeclaredFields())
                + "FROM `"+AuthorizationHistory.class.getSimpleName()+"` AH "
                + (AuthorizationHistoryId <= 0 ? "" : "Where AH.Id = "+ AuthorizationHistoryId)
                + "GROUP BY AH.`id` "
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
    // ==================== геттеры  UPSERT ====================
    @Override
    public String getQueryIsExistKey(Integer Key){
        String str = "SELECT count(AH.`Id`) "
                + "FROM `AuthorizationHistory` AH "
                + "Where AH.Id = " + Key;
        return str;                
    }    
    @Override
    public String getQueryIsExistKey(){
        String str = "SELECT count(AH.`Id`) "
                + "FROM `AuthorizationHistory` AH "
                + "Where AH.Id = " + this.getId();
        return str;                
    }    
    @Override
    public String getQueryUpdate(Integer UnitId){
        String str = "UPDATE `AuthorizationHistory` SET "
                + "`LoginEmployeeId`=?,`DateIn`=?,`DateOut`=?, `Note`=?, `RegionId`=? "
                + "WHERE `AuthorizationHistory`.`Id` = " + UnitId;
        return str;                
    }
    @Override
    public String getQueryInsert(){
        String str = "INSERT INTO `AuthorizationHistory` "
                + "(`LoginEmployeeId`, `DateIn`, `DateOut`, `Note`, `RegionId`) "
                + "VALUES ("
                + "?,?,?,?,?)";
        return str;                
    }
    
    // ==================== ResultSet  ====================
    @Override
    public AuthorizationHistory getDataFromResultSet(ResultSet rs) throws SQLException{
        AuthorizationHistory AuthorizationHistory = new AuthorizationHistory();
        AuthorizationHistory.setId(rs.getInt(1));                
        AuthorizationHistory.setLoginEmployeeId(rs.getInt(2));                
        AuthorizationHistory.setDateIn(rs.getDate(3));                
        AuthorizationHistory.setDateOut(rs.getDate(4));                              
        AuthorizationHistory.setNote(rs.getString(5));                              
        AuthorizationHistory.setRegionId(rs.getInt(6));
        return AuthorizationHistory;
    }
    @Override // не определен
    public AuthorizationHistory getDataFromResultSetTable(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // ==================== PreparedStatment  ====================
    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException{
        ps.setInt(1, this.getLoginEmployeeId());              
        ps.setDate(2, this.getDateIn());              
        ps.setDate(3, this.getDateOut());                                                         
        ps.setString(4, this.getNote());                                                         
        ps.setInt(5, this.getRegionId());                     
    }
     
}
