package com.Class;

import com.Class.MainClassProgect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserLogin extends MainClassProgect<UserLogin>{
    
    private Integer Id = -1;      
    private Integer EmployeeId = -1; 
    private String Login = "";
    private String Password = "";
    private EnumStatus Status = EnumStatus.ACTIVE;
    private Integer UserPrivilegeId = -1;        
    
    private String EmployeeLastName = "";
    private String EmployeeName = "";
    private String EmployeePatronymic = "";
    
    private String UsGroupName = "";
    private String UsDepartmentName = "";
    private String UsPositionName = "";
    
    private String UserPrivilegeName = "";
    
    public UserLogin(){
        
    } 
    
    @Override
    public String getQuerySelect(Integer Limit, Integer UnitId) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT ");
        str.append("`Id`, `EmployeeId`, `Login`, `Password`, `EmployeeAddId`, ");
        str.append("`EmployeeChangeId`, `DateAdd`, `DateChange`, `Status`, ");
        str.append("`UserPrivilegeId` ");
        str.append("FROM `User` ");
        str.append(UnitId > 0 ? "WHERE Id = " + UnitId + " " : "");
        str.append(Limit > 0 ? "Limit " + Limit : "");
        return str.toString();
    }

    @Override
    public String getQuerySelectTable(Integer Limit) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT ");
        str.append("Us.`Id`, Us.`Status`, ");
        str.append("Empl.LastName, Empl.Name, Empl.Patronymic, UsGroup.Name, UsDepartment.Name, UsPosition.Name,");
        str.append("UsPr.Name, Us.UserPrivilegeId ");
        str.append("FROM `User` Us ");
        str.append("Left Join Employee Empl on (Empl.Id = Us.EmployeeId) ");
        str.append("Left Join UsersPrivilege UsPr on (UsPr.Id = Us.UserPrivilegeId) ");
        str.append("LEFT join `UsDepartment` on (Empl.UsDepartmentId = UsDepartment.Id) ");
        str.append("LEFT join `UsGroup` on (Empl.UsGroupId = UsGroup.Id) ");
        str.append("LEFT join `UsPosition` on (Empl.UsPositionId = UsPosition.Id) ");
        str.append("Where Us.`Status` = '" + getStatus() + "' ");
        str.append(getId() > 0 ? " and Id = " + getId() + " " : "");
        if (getTextSearch().length()>0)
            if (!isDigit(getTextSearch())){
                str.append("and (Empl.LastName = Like '%" + getTextSearch() + "%' ");
                str.append(" or Empl.Name = '%" + getTextSearch() + "%' ");
                str.append(" or Empl.Patronymic = '%" + getTextSearch() + "%' ");
                str.append(" or UsDepartment.Name = '%" + getTextSearch() + "%' ");
                str.append(" or UsGroup.Name = '%" + getTextSearch() + "%' ");
                str.append(" or UsPosition.Name = '%" + getTextSearch() + "%' ");
                str.append(" or UsPr.Name = '%" + getTextSearch() + "%') ");
            }
        str.append(Limit > 0 ? "Limit " + Limit : "");
        return str.toString();
    }

    @Override
    public String getQueryLastKey() {
        StringBuilder str = new StringBuilder(); 
        str.append("select `id` from User ORDER BY `Id` DESC Limit 1");
        return str.toString(); 
    }

    @Override
    public String getQueryIsExistKey(Integer Key) {
        StringBuilder str = new StringBuilder(); 
        str.append("SELECT count(Us.`Id`) ");
        str.append("FROM `User` Us ");
        str.append("Where Us.Id = " + Key);
        return str.toString();   
    }

    @Override
    public String getQueryIsExistKey() {
        StringBuilder str = new StringBuilder(); 
        str.append("SELECT count(Us.`Id`) ");
        str.append("FROM `User` Us ");
        str.append("Where Us.Id = " + this.getId());
        return str.toString();   
    }

    @Override
    public String getQueryUpdate(Integer UnitId) {
        StringBuilder str = new StringBuilder(); 
        str.append("UPDATE `User` SET ");
        str.append("`EmployeeId`=?,`Login`=?,`Password`=?,`EmployeeAddId`=?,`EmployeeChangeId`=?,");
        str.append("`DateAdd`=?,`DateChange`=?,`Status`=?,`UserPrivilegeId`=?) ");
        str.append("WHERE Id = " + UnitId);
        return str.toString();  
    }

    @Override
    public String getQueryInsert() {
        StringBuilder str = new StringBuilder(); 
        str.append("INSERT INTO `User`(");
        str.append("`EmployeeId`, `Login`, `Password`, `EmployeeAddId`, `EmployeeChangeId`, ");
        str.append("`DateAdd`, `DateChange`, `Status`, `UserPrivilegeId`) ");
        str.append("VALUES ");
        str.append("(?,?,?,?,?");
        str.append("?,?,?,? )");
        return str.toString();          
    }

    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException {
        ps.setInt(1, this.getEmployeeId());                
        ps.setString(2, this.getLogin());              
        ps.setString(3, this.getPassword());          
        ps.setInt(4, this.getEmployeeAddId());          
        ps.setInt(5, this.getEmployeeChangeId());          
        ps.setDate(6, new java.sql.Date(this.getDateAdd().getTime()));          
        ps.setTimestamp(7, new java.sql.Timestamp(this.getDateChange().getTime()));        
        ps.setString(8, this.getStatus().toString());          
        ps.setInt(9, this.getUserPrivilegeId());          
    }

    @Override
    public UserLogin getDataFromResultSet(ResultSet rs) throws SQLException {
//        str.append("`Id`, `EmployeeId`, `Login`, `Password`, `EmployeeAddId`, ");
//        str.append("`EmployeeChangeId`, `DateAdd`, `DateChange`, `Status`, ");
//        str.append("`UserPrivilegeId` ");
        UserLogin userRoot = new UserLogin();                
        userRoot.setId(rs.getInt(1));                
        userRoot.setEmployeeId(rs.getInt(2));                
        userRoot.setLogin(rs.getString(3));       
        userRoot.setPassword(rs.getString(4));       
        userRoot.setEmployeeAddId(rs.getInt(5));       
        userRoot.setEmployeeChangeId(rs.getInt(6));       
        userRoot.setDateAdd(rs.getDate(7));       
        userRoot.setDateChange(rs.getDate(8));       
        userRoot.setStatus(EnumStatus.valueOf(rs.getString(9)));       
        userRoot.setUserPrivilegeId(rs.getInt(10));       
        return userRoot;
    }

    @Override
    public UserLogin getDataFromResultSetTable(ResultSet rs) throws SQLException {
//        str.append("Us.`Id`, Us.`Status`, ");
//        str.append("Empl.LastName, Empl.Name, Empl.Patronymic, UsGroup.Name, UsDepartment.Name, UsPosition.Name,");
//        str.append("UsPr.Name ");
        UserLogin userRoot = new UserLogin();                
        userRoot.setId(rs.getInt(1));                      
        userRoot.setStatus(EnumStatus.valueOf(rs.getString(2)));       
        userRoot.setEmployeeLastName(rs.getString(3));       
        userRoot.setEmployeeName(rs.getString(4));       
        userRoot.setEmployeePatronymic(rs.getString(5));  
        
        userRoot.setUsGroupName(rs.getString(6));  
        userRoot.setUsDepartmentName(rs.getString(7));  
        userRoot.setUsPositionName(rs.getString(8));  
        
        userRoot.setUserPrivilegeName(rs.getString(9));       
        userRoot.setUserPrivilegeId(rs.getInt(10));       
        return userRoot;
    }

    @Override
    public Object[] getDataForTable() {
//        №пп	String	false	false
//        #	String	false	false
//        ФИО	String	false	false
//        Подраздление	String	false	false
//        Должность	String	false	false
//        Право доступа	String	false	false
//        Статус	String	false	false
        Object[] DataForTable = new Object[8];

        String Group = getUsGroupName().length()>90 ? getUsGroupName().substring(0, 90).concat("...") : getUsGroupName();
        String Department = getUsDepartmentName().length()>90 ? getUsDepartmentName().substring(0, 90).concat("...") : getUsDepartmentName();
        String Position = getUsPositionName().length()>90 ? getUsPositionName().substring(0, 90).concat("...") : getUsPositionName();        
        DataForTable[0] = (int) 1;
        DataForTable[1] = getId();
        DataForTable[2] = getSurnameInitials(getEmployeeLastName(),getEmployeeName(),getEmployeePatronymic());
        DataForTable[3] = Group;
        DataForTable[4] = Department;
        DataForTable[5] = Position;
        DataForTable[6] = getUserPrivilegeName();
        DataForTable[7] = getStatus();
        return DataForTable;       
    }
    
}

