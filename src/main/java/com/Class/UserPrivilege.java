package com.Class;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
//@Entity
//@Table(name="UserPrivilege")
public class UserPrivilege extends MainClassProgect<UserPrivilege> {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name = "Id")
    private Integer Id = -1;
    //@Column(name = "Name", unique = true, nullable = false, length = 100)
    private String Name = "";
    private EnumStatus StatusPrivilege = EnumStatus.ACTIVE;
    private String Note = "";
    private ArrayList<PrivilegeDefault> PrivMedicalBook = new ArrayList<>();
    private ArrayList<PrivilegeDefault> PrivEmployee = new ArrayList<>();
    private ArrayList<PrivilegeInstitution> PrivInstitution = new ArrayList<>();
    private ArrayList<PrivilegeSettings> PrivSettings = new ArrayList<>();
    private ArrayList<PrivilegeDefault> PrivUser = new ArrayList<>();

    private String UserName = "";
    
    public UserPrivilege(){        
    }

    @Override
    public String getQuerySelect(Integer Limit, Integer UnitId) {
        StringBuilder str = new StringBuilder();
        str.append("");
        if (Limit !=null & UnitId !=null){
            str.append("SELECT ");
            str.append("UR.`Id`, UR.`Name`, UR.EmployeeChangeId, UR.EmployeeAddId, UR.DateAdd, ");
            str.append("UR.DateChange, UR.`Status`, UR.`Note`, UR.`MedicalBook`, UR.`Employee`, ");
            str.append("UR.`Institution`, UR.`Settings`, UR.`User` ");
            str.append("FROM `UsersPrivilege` UR ");
            str.append(UnitId > 0 ? "WHERE UR.Id = " + UnitId + " " : "");
            str.append(Limit > 0 ? "Limit " + Limit : "");
        }
        System.err.println(str);
        return str.toString();
    }

    @Override
    public String getQuerySelectTable(Integer Limit) {
        StringBuilder str = new StringBuilder();
        str.append("");
        if (Limit != null) {
            str.append("SELECT ");
            str.append("UR.`Id`, UR.`Name`, UR.`Status`, UR.`Note`, ");
            str.append("UR.`MedicalBook`, UR.`Employee`, UR.`Institution`, UR.`Settings`, UR.`User` ");
            str.append("FROM `UsersPrivilege` UR ");
            str.append("WHERE UR.Status  = '" + getStatusPrivilege() + "' ");
            str.append(getId() > 0 ? " and UR.Id" + getId() + " " : "");
            if (!this.getTextSearch().equals(""))
                if (!isDigit(this.getTextSearch()))
                    str.append(" and UR.`Name` LIKE '%" + this.getTextSearch() + "%' ");
                else if (isDigit(this.getTextSearch()))
                    str.append(" and UR.`Id` = " + this.getTextSearch() + " ");
            str.append("Limit ").append(Limit);
        }
        System.err.println(str);
        return str.toString();        
    }

    @Override
    public String getQueryLastKey() {
        String str = "select `id` from UsersPrivilege ORDER BY `Id` DESC Limit 1";
        return str;
    }

    @Override
    public String getQueryIsExistKey(Integer Key) {
        String str = "SELECT count(UP.`Id`) "
                + "FROM `UsersPrivilege` UP "
                + "Where UP.Id = " + Key;
        return str;
    }

    @Override
    public String getQueryIsExistKey() {
        String str = "SELECT count(UP.`Id`) "
                + "FROM `UsersPrivilege` UP "
                + "Where UP.Id = " + this.getId();
        return str;
    }

    @Override
    public String getQueryUpdate(Integer UnitId) {
        String str = "UPDATE `UsersPrivilege` SET"
                + " `Name` = ?, `EmployeeChangeId` = ?, `EmployeeAddId` = ?, `DateAdd` = ?,"
                + " `DateChange` = ?, `Status` = ?, `Note` = ?, `MedicalBook` = ?, `Employee` = ?, "
                + " `Institution` = ?, `Settings` = ?, `User` = ? "
                + "Where UsersPrivilege.Id = " + UnitId;
        System.err.println(str);
        return str;
    }

    @Override
    public String getQueryInsert() {
        String str = "INSERT INTO `UsersPrivilege`( "
                + " `Name`, `EmployeeChangeId`, `EmployeeAddId`, `DateAdd`, `DateChange`, "
                + " `Status`, `Note`, `MedicalBook`, `Employee`, `Institution`, "
                + " `Settings`, User) "
                + "VALUES "
                + " (?, ?, ?, ?,"
                + " ?, ?, ?, ?, ?,"
                + " ?, ?, ? ";
        System.err.println(str);
        return str;
    }

    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException {
//                + " `Name` = ?, `EmployeeChangeId` = ?, `EmployeeAddId` = ?, `DateAdd` = ?,"
//                + " `DateChange` = ?, `Status` = ?, `Note` = ?, `MedicalBook` = ?, `Employee` = ?, "
//                + " `Institution` = ?, `Settings` = ?, `User` = ? "
        ps.setString(1, getName());
        ps.setInt(2, getEmployeeChangeId());
        ps.setInt(3, getEmployeeAddId());
        ps.setDate(4, new java.sql.Date(getDateAdd().getTime()));
        ps.setTimestamp(5, new java.sql.Timestamp(getDateChange().getTime()));
        ps.setString(6, getStatusPrivilege().toString());
        ps.setString(7, getNote());      
        ps.setString(8, enumToString(getPrivMedicalBook()));
        //ps.setArray(8, new java.sql.Array(getPrivMedicalBooktest()));
        ps.setString(9, enumToString(getPrivEmployee()));
        ps.setString(10, enumToString(getPrivInstitution()));
        ps.setString(11, enumToString(getPrivSettings()));
        ps.setString(12, enumToString(getPrivUser()));        
    }

    @Override
    public UserPrivilege getDataFromResultSet(ResultSet rs) throws SQLException {
        UserPrivilege userPrivilege = new UserPrivilege();
        userPrivilege.setId(rs.getInt(1));
        userPrivilege.setName(rs.getString(2));
        userPrivilege.setEmployeeAddId(rs.getInt(3));
        userPrivilege.setEmployeeChangeId(rs.getInt(4));
        userPrivilege.setDateAdd(rs.getDate(5));
        userPrivilege.setDateChange(rs.getDate(6));
        userPrivilege.setStatusPrivilege(EnumStatus.valueOf(rs.getString(7)));
        userPrivilege.setNote(rs.getString(8));
        userPrivilege.setPrivMedicalBook(StringToPrivilegeMedicalBook(rs.getString(9)));
        //userPrivilege.setPrivMedicalBooktest(PrivMedicalBooktest.copyOf(rs.getArray(9).getArray()));
        userPrivilege.setPrivEmployee(StringToPrivilegeEmployee(rs.getString(10)));
        userPrivilege.setPrivInstitution(StringToPrivilegeInstitution(rs.getString(11)));
        userPrivilege.setPrivSettings(StringToPrivilegeSettings(rs.getString(12)));
        userPrivilege.setPrivUser(StringToPrivilegeUser(rs.getString(13)));
        return userPrivilege;
    }

    @Override
    public UserPrivilege getDataFromResultSetTable(ResultSet rs) throws SQLException {
        UserPrivilege userPrivilege = new UserPrivilege();
        userPrivilege.setId(rs.getInt(1));
        userPrivilege.setName(rs.getString(2));
        userPrivilege.setStatusPrivilege(EnumStatus.valueOf(rs.getString(3)));
        userPrivilege.setNote(rs.getString(4));
        userPrivilege.setPrivMedicalBook(StringToPrivilegeMedicalBook(rs.getString(5)));
        //userPrivilege.setPrivMedicalBooktest(PrivMedicalBooktest.addAll(Arrays.asList(rs.getArray(5).getArray())));
        userPrivilege.setPrivEmployee(StringToPrivilegeEmployee(rs.getString(6)));
        userPrivilege.setPrivInstitution(StringToPrivilegeInstitution(rs.getString(7)));
        userPrivilege.setPrivSettings(StringToPrivilegeSettings(rs.getString(8)));
        userPrivilege.setPrivUser(StringToPrivilegeUser(rs.getString(9)));
        return userPrivilege;
    }

    @Override
    public Object[] getDataForTable() {
        String Text = "";
        ArrayList<String> str = new ArrayList<>();
        
        if (!getPrivMedicalBook().isEmpty()) 
            str.add("Медицинские книжки");
        if (!getPrivEmployee().isEmpty()) 
            str.add("Сотрудники");
        if (!getPrivInstitution().isEmpty()) 
            str.add("Учереждения");
        if (!getPrivUser().isEmpty()) 
            str.add("Пользователи");
        if (!getPrivSettings().isEmpty()) 
            str.add("Медицинские");
        
        
        Object[] DataForTable = new Object[5];
        DataForTable[0] = (int) 1;
        DataForTable[1] = getId();
        DataForTable[2] = getName();
        DataForTable[3] = enumToString(str);
        DataForTable[4] = getStatusPrivilege();
        return DataForTable;       
    }
    
    
    // ======================= ======================= =======================
    public ArrayList<PrivilegeDefault> StringToPrivilegeMedicalBook(String String) {
        ArrayList<PrivilegeDefault> arrayList = new ArrayList<>();
        try{        
            String[] subString = new String[10];
            if (String != null) {
                if (String.length() > 0) {
                    subString = String.split(",");
                    for (int i = 0; i < subString.length; i++) {
                        arrayList.add(PrivilegeDefault.valueOf(subString[i]));
                    }
                }
            } 
        }catch (Exception ex){
            System.out.println("com.Class.UserPrivilege.StringToPrivilegeMedicalBook() -" + ex.getMessage());
        }
        return arrayList;
    }
    
    public ArrayList<PrivilegeDefault> StringToPrivilegeEmployee(String String) {
        ArrayList<PrivilegeDefault> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split(",");
                for (int i = 0; i < subString.length; i++) {
                    arrayList.add(PrivilegeDefault.valueOf(subString[i]));
                }
            }
        } 
        return arrayList;
    }
    
    public ArrayList<PrivilegeInstitution> StringToPrivilegeInstitution(String String) {
        ArrayList<PrivilegeInstitution> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split(",");
                for (int i = 0; i < subString.length; i++) {
                    arrayList.add(PrivilegeInstitution.valueOf(subString[i]));
                }
            }
        } 
        return arrayList;
    }
    
    public ArrayList<PrivilegeSettings> StringToPrivilegeSettings(String String) {
        ArrayList<PrivilegeSettings> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split(",");
                for (int i = 0; i < subString.length; i++) {
                    arrayList.add(PrivilegeSettings.valueOf(subString[i]));
                }
            }
        } 
        return arrayList;
    }
    
    public ArrayList<PrivilegeDefault> StringToPrivilegeUser(String String) {
        ArrayList<PrivilegeDefault> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split(",");
                for (int i = 0; i < subString.length; i++) {
                    arrayList.add(PrivilegeDefault.valueOf(subString[i]));
                }
            }
        }        
        return arrayList;
    }    
    
//    public EnumSet arrayToEnumSet(enum enumunit, Arrays arr){
//        EnumSet<enumunit> testenum = null;
//        testenum.addAll(Arrays.asList(arr));
//        return testenum;
//    }

    public enum PrivilegeSettings {
        GROUP_CHANGE,
        DEPARTMENT_CHANGE,
        POSITION_CHANGE,
        MAILING_VIEW, MAILING_CHANGE, MAILING_DELET, MAILING_SELECT, 
        PRIVILEGE_VIEW, PRIVILEGE_CHANGE, PRIVILEGE_DELET
    }
    
    public enum PrivilegeDefault {
        VIEW_ALL,VIEW_DELET,VIEW_OWNGROUP,VIEW_OWNDEPARTMENT,VIEW_EMPLOYEEUNDER,VIEW_LINESTAFF,
        CHANGE_ALL,CHANGE_DELET,CHANGE_OWNGROUP,CHANGE_OWNDEPARTMENT,CHANGE_EMPLOYEEUNDER,CHANGE_LINESTAFF,
        DELET_ALL,
        //DELET_DELET,DELET_OWNGROUP,DELET_OWNDEPARTMENT,DELET_EMPLOYEEUNDER,DELET_LINESTAFF,
        SELECT_ALL,SELECT_DELET,SELECT_OWNGROUP,SELECT_OWNDEPARTMENT,SELECT_EMPLOYEEUNDER,SELECT_LINESTAFF
    }
//    VIEW_ALL - 
//	руковдитель округа, 
//	сан врач округа
//    VIEW_DELET - 
//	по необходимости
//    VIEW_OWNGROUP - 
//	сан врачи направлений
//	руководители направлений,
//    VIEW_OWNDEPARTMENT -
//	руковдодители подразделений
//      Территориальные управляющие
//    VIEW_LINESTAFF -
//	для любой категории
    
    
    public enum PrivilegeInstitution {
        VIEW,VIEW_CLOSED,VIEW_OWNTYPE,VIEW_INSTITUTIONUNDER,
        CHANGE,CHANGE_CLOSED,CHANGE_OWNTYPE,CHANGE_INSTITUTIONUNDER,CHANGE_CURATOR,CHANGE_MAININSTITUTION,
        //SELECT,SELECT_CLOSED,SELECT_OWNTYPE,SELECT_INSTITUTIONUNDER,
        DELET,
        DELET_CLOSED,DELET_OWNTYPE,DELET_INSTITUTIONUNDER
    }

}
