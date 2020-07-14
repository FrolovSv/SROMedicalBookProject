
package com.Class;

import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Employee extends MainClassProgect<Employee>{
    
    private Integer Id = -1;
    //private String Name = "";
    //private String LastName = "";
    //private String Patronymic = "";
    private Integer UsDepartmentId = -1;
    private Integer UsGroupId  = -1;
    private Integer UsPositionId = -1;
    private String UsDepartmentName = "";
    private String UsGroupName  = "";
    private String UsPositionName = "";
    private EnumSex Sex = EnumSex.Female;
    private String Email = "";
    private String Phone1 = "";       
    private String Phone2 = "";       
    private byte[]Photo = null;        
    private Date Berthday = new Date(0);       
    private String Note = "";         
    private Integer NumMedicalBook = -1;       
    private boolean IsDeleted = true;        
    private Integer RegionId = -1;
    private String RegionName = "";
    private Integer MainEmployeeId = -1;
    private Integer RootId = -1;
    private boolean IsAccessLogin = false;
    private String Login = "";
    private String Password = "";
    private Integer InstitutionId = null;
    private String InstitutionName = "";
    private String InstitutionAddress = "";
    private Institution.InstitutionType InstitutionType = null;
    private Integer TypeUnitId = -1;
    private String TypeName = "";
    private UsGroup.UsGroupType GroupType = null;
    public Integer getInstitutionId() {         return InstitutionId;    }
    public Integer TypeUnitId() {                 return TypeUnitId > 0 ? TypeUnitId : 0;    }
    public Integer getMainEmployeeId() {        return MainEmployeeId > 0 ? MainEmployeeId : 0;    }
    @Override
    public void setId(Integer Id) {        this.Id = Id ==null ? 0: Id ;    }
    public void setPhoto(byte[] Photo) {
        try{
        if (Photo.length>0)
            this.Photo = Photo;    
        else 
            this.Photo = null;
        }catch(Exception ex){
            this.Photo = null;
        }
    }
    public Integer getNumMedicalBook() { return NumMedicalBook == null ? 0 : NumMedicalBook;     }
            
    private String TextSearch = "";
    private static final String TableName = "Employee";
    private Boolean SerchWithoutMedicalBook = false;
    private Boolean SerchLineStaff = false;
    private Date EmploymentDate = new Date(0);
     
    public Employee(){
        
    }    
    
    @Override
    public Object[] getDataForTable() { 
//        String Name = getUsPositionName();
//        if (Name.length()>20){
//            Name = Name.substring(0, 20).concat("...");
//        }
        MedicalBook MB = new MedicalBook();
        if (getNumMedicalBook()!=null && getNumMedicalBook()>0){
            MB = new SQLQuery<>(new MedicalBook()).Read(getNumMedicalBook());
        }
            
            
        String Department = "";
        if (getInstitutionId() == null)
            Department = getUsDepartmentName();
        else if (getInstitutionId() > 0)
            Department = getInstitutionName();
        else if (getInstitutionId() == 0)
            Department = getUsDepartmentName();
        
        Object[] DataForTable = new Object[9];
        DataForTable[0] = (int) 1;
        DataForTable[1] = getId();
        DataForTable[2] = getUsGroupName();
        DataForTable[3] = Department;
        DataForTable[4] = getUsPositionName();
        DataForTable[5] = getFullName();
        DataForTable[6] = getPhone1() + "; "+ getPhone2();
        DataForTable[7] = getNumMedicalBook() <=0 ? "" : getNumMedicalBook();
        DataForTable[8] = getNumMedicalBook()!=null && getNumMedicalBook()>0 ? (MB.checkValidMedicalBook() ?  "Норма" : "Просрочено" ): "";   
        return DataForTable;        
    }
    public Object[] getDataForTableEmpUnder() {
        Object[] DataForTable = new Object[6];
        String Name = getUsPositionName();
        if (Name.length()>20){
            Name = Name.substring(0, 20);
            Name = Name.concat("...");
        }
        //#пп АЙДИ группа подразделение должность фио телефон номер ЛМК
        
        DataForTable[0] = (int) 1;
        DataForTable[1] = getId();
        DataForTable[2] = Name;
        DataForTable[3] = getSurnameInitials(getLastName(),getName(),getPatronymic());
        DataForTable[4] = getNumMedicalBook() <=0 ? "" : getNumMedicalBook();        
        DataForTable[5] = "Норма";        
        return DataForTable;        
    }   

    
    // ========================== установки sql ============================
    // ==================== геттеры  SELECT ====================
    @Override
    public String getQuerySelect(Integer Limit, Integer EmployeeId){
        String str = "SELECT "        
                + "EM.`Id`, EM.`LastName`, EM.`Name`, EM.`Patronymic`, UsDepartment.`Id`, "
                + "UsGroup.`Id`, EM.`UsPositionId`, EM.`Sex`, EM.`Phone1`, EM.`Phone2`, "
                + "EM.`Email`, EM.`Photo`, EM.`Berthday`, EM.`Note`, EM.`DateAdd`, "
                + "EM.`DateChange`, EM.`EmployeeAddId`, EM.`EmployeeChangeId`, EM.`NumMedicalBook`, EM.`isDeleted`, "
                + "EM.`RegionId`, EM.`MainEmployeeId` ,UsDepartment.name, UsGroup.name, UsPosition.name, "
                + "Region.name, EM.`RootId`, EM.`IsAccessLogin`, EM.`Login`, EM.`Password`, EM.`InstitutionId`, EM.InstitutionType, TypeUnit.Id, "
                + "EM.EmploymentDate "
                + "FROM `Employee` EM "            
                + "LEFT join `UsPosition` on (EM.UsPositionId = UsPosition.Id) "
                + "LEFT join `UsDepartment` on (UsDepartment.Id = UsPosition.UsDepartmentId)"
                + "LEFT join `UsGroup` on (UsGroup.Id = UsDepartment.UsGroupId)"
                + "LEFT join `Region` on (EM.RegionId = Region.Id) "
                + "LEFT join `Institution` on (EM.InstitutionId = Institution.Id) "
                + "LEFT join `TypeUnit` on (UsGroup.TypeUnitId = TypeUnit.Id) "
                + (EmployeeId <= 0 ? "" : "Where EM.Id = "+ EmployeeId+" ")
                + "GROUP BY EM.`id` "
                + (Limit <= 0 ? "" : "Limit "+Limit);
        return str;                
    }    
    @Override
    public String getQuerySelectTable(Integer Limit){
        
        String str = "SELECT "        
                + "EM.`Id`, EM.`LastName`, EM.`Name`, EM.`Patronymic`, EM.`Phone1`, "
                + "EM.`Phone2`, EM.`NumMedicalBook`, EM.`isDeleted`, "
                + "UsDepartment.name, UsGroup.name, UsPosition.name, "
                + "EM.InstitutionId, Institution.Name, Institution.Address, "
                + "EM.InstitutionType, TypeUnit.Id, EM.`MainEmployeeId`, EM.EmploymentDate  "
                + "FROM `Employee` EM "
                + "LEFT join `UsPosition` on (EM.UsPositionId = UsPosition.Id) "
                + "LEFT join `UsDepartment` on (UsDepartment.Id = UsPosition.UsDepartmentId) "
                + "LEFT join `UsGroup` on (UsGroup.Id = UsDepartment.UsGroupId) "
                + "LEFT join `Region` on (EM.RegionId = Region.Id) "
                + "LEFT join `Institution` on (EM.InstitutionId = Institution.Id) "
                + "LEFT join `TypeUnit` on (UsGroup.TypeUnitId = TypeUnit.Id) "
                + "LEFT join `Employee` on (EM.`MainEmployeeId`= Employee.`Id`) "
                + "Where EM.isDeleted = " + (this.isIsDeleted() ? 1 : 0) + " "
                + " and EM.NumMedicalBook" + (this.getSerchWithoutMedicalBook()? " is null " : " is not null ")
                + "%getSerchLineStaff"
                + "%getTypeUnitId"
                + "%getMainEmployeeId"                                       
                + "%getGroup"
                + "%getDepartment"
                + "%getPosition"
                + "%6where"
                + "%7where"                            
                + "GROUP BY EM.`id` "
                + (Limit==-1? "" : "Limit "+Limit.toString());
        
        if (this.getSerchWithoutMedicalBook()){
            this.setMainEmployeeId(0);
            str = str.replace("%getSerchLineStaff", "");
            str = str.replace("%getTypeUnitId", "");
        }else{
            str = str.replace("%getSerchLineStaff", (this.getSerchLineStaff() ? " and EM.InstitutionId is not null " : "and EM.InstitutionId is null"));
            str = str.replace("%getTypeUnitId", (this.getTypeUnitId() > 0 ? " and TypeUnit.Id = " + this.getTypeUnitId() + " " : " "));
        }
        
        if(this.getMainEmployeeId() > 0) {
            str = str.replace("%getMainEmployeeId", " and EM.`MainEmployeeId` = " + this.getMainEmployeeId() + " ");
            str = str.replace("%getGroup", "");
            str = str.replace("%getDepartment", "");               
            str = str.replace("%getPosition", "");               
        }else {
            str = str.replace("%getMainEmployeeId", "");            
            str = str.replace("%getGroup", (!this.getUsGroupName().equals("") ? "and UsGroup.name LIKE '%"+this.getUsGroupName()+"%' " : ""));
            str = str.replace("%getDepartment", (!this.getUsDepartmentName().equals("") ? "and UsDepartment.name LIKE '%"+this.getUsDepartmentName()+"%' " : ""));
            str = str.replace("%getPosition", (!this.getUsPositionName().equals("") ? "and UsPosition.name LIKE '%"+this.getUsPositionName()+"%' " : ""));
            str = str.replace("%getTypeUnitId", (this.getTypeUnitId() > 0 ? " and TypeUnit.Id = " + this.getTypeUnitId() + " " : " "));
        }

        if (this.getTextSearch().equals("")){
            str = str.replace("%6where", " ");
            str = str.replace("%7where", " ");
        }else if(!isDigit(this.getTextSearch())){
            str = str.replace("%6where", "and ("
                    + "EM.`LastName` LIKE '%"+this.getTextSearch()+"%' "
                    + "or EM.`Name` LIKE '%"+this.getTextSearch()+"%' "
                    + "or EM.`Patronymic` LIKE '%"+this.getTextSearch()+"%' "
                    + "or Institution.Name LIKE '%"+this.getTextSearch()+"%' ) ");
            str = str.replace("%7where", " ");
        }else if(isDigit(this.getTextSearch())) {
            str = str.replace("%6where", "and ( "
                    + "EM.`NumMedicalBook` LIKE '%"+this.getTextSearch()+"%' "
                    + "or EM.`Phone1` LIKE '%"+this.getTextSearch()+"%' "
                    + "or EM.`Phone2` LIKE '%"+this.getTextSearch()+"%'"
                    + "or Institution.Name LIKE '%"+this.getTextSearch()+"%' ) ");
            str = str.replace("%7where", " ");                    
        }
        
        
        return str;                
    }
    // ==================== геттеры  UPSERT ====================
    @Override
    public String getQueryIsExistKey(Integer Key){
        String str = "SELECT count(EM.`Id`) "
                + "FROM `Employee` EM "
                + "Where EM.Id = " + Key;
        return str;                
    }  
    @Override
    public String getQueryIsExistKey(){
        String str = "SELECT count(EM.`Id`) "
                + "FROM `Employee` EM "
                + "Where EM.Id = " + this.getId();
        return str;                
    }  
    @Override
    public String getQueryLastKey(){
        String str = "select `id` from Employee ORDER BY `Id` DESC Limit 1";
        return str;                
    }   
    @Override
    public String getQueryUpdate(Integer UnitId){
        String str = "UPDATE `Employee` SET "
                + "`LastName`=?, `Name`=?, `Patronymic`=?, "
                + "`UsPositionId`=?, `Sex`=?, `Phone1`=?, `Phone2`=?, "
                + "`Email`=?, `Photo`=?, `Berthday`=?, `Note`=?, `DateAdd`=?, "
                + "`DateChange`=?, `EmployeeAddId`=?, `EmployeeChangeId`=?, `NumMedicalBook`=?, `isDeleted`=?, "
                + "`RegionId`=?, `MainEmployeeId`=?, `RootId`=?, `IsAccessLogin`=?, `Login`=?, "
                + "`Password`=?, `InstitutionId`=?, InstitutionType = ?, EM.EmploymentDate = ? "
                + "WHERE `Employee`.`Id` = " + UnitId;
        return str;                
    }
    @Override
    public String getQueryInsert(){
        String str = "INSERT INTO `Employee` "
                + "(`LastName`, `Name`, `Patronymic`, "
                + "`UsPositionId`, `Sex`, `Phone1`, `Phone2`, "
                + "`Email`, `Photo`, `Berthday`, `Note`, `DateAdd`, "
                + "`DateChange`, `EmployeeAddId`, `EmployeeChangeId`, `NumMedicalBook`, `isDeleted`, "
                + "`RegionId`, `MainEmployeeId`, `RootId`, `IsAccessLogin`, `Login`, "
                + "`Password`, `InstitutionId`, `InstitutionType`, EM.EmploymentDate ) "
                + "VALUES ("
                + "?,?,?,"
                + "?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?)";
        return str;                
    }
    // ==================== ResultSet SELECT ====================
    @Override
    public Employee getDataFromResultSet(ResultSet rs) throws SQLException{
        Employee Employee = new Employee();                
            Employee.setId(rs.getInt(1));                
            Employee.setLastName(rs.getString(2));                
            Employee.setName(rs.getString(3));                
            Employee.setPatronymic(rs.getString(4));                
            Employee.setUsDepartmentId(rs.getInt(5));                
            Employee.setUsGroupId(rs.getInt(6));                
            Employee.setUsPositionId(rs.getInt(7));                
            Employee.setSex(EnumSex.valueOf(rs.getString(8)));  
            Employee.setPhone1(rs.getString(9));
            Employee.setPhone2(rs.getString(10));                
            Employee.setEmail(rs.getString(11));                
            Employee.setPhoto(rs.getBytes(12));                
            Employee.setBerthday(rs.getDate(13));                              
            Employee.setNote(rs.getString(14));                
            Employee.setDateAdd(rs.getDate(15));                
            Employee.setDateChange(rs.getDate(16));                
            Employee.setEmployeeAddId(rs.getInt(17));
            Employee.setEmployeeChangeId(rs.getInt(18));
            Employee.setNumMedicalBook(rs.getInt(19));
            Employee.setIsDeleted(rs.getBoolean(20));
            Employee.setRegionId(rs.getInt(21));  
            Employee.setMainEmployeeId(rs.getInt(22)); 

            Employee.setUsDepartmentName(rs.getString(23));  
            Employee.setUsGroupName(rs.getString(24));  
            Employee.setUsPositionName(rs.getString(25));  
            Employee.setRegionName(rs.getString(26));  

            Employee.setRootId(rs.getInt(27));  
            Employee.setIsAccessLogin(rs.getBoolean(28));  
            Employee.setLogin(rs.getString(29));  
            Employee.setPassword(rs.getString(30)); 
            Employee.setInstitutionId(rs.getInt(31)); 
            if (rs.getString(32)==null)
                Employee.setInstitutionType(null);
            else 
                Employee.setInstitutionType(Institution.InstitutionType.valueOf(rs.getString(32))); 
            Employee.setTypeUnitId(rs.getInt(33)); 
            //if (rs.getBytes(22)!=null)
            //    User.setPhotoPath(WriteByteArrayToFile(rs.getBytes(22), "User"+rs.getString(1)));
            Employee.setEmploymentDate(rs.getDate(34)); 
        return Employee;
    }
    
    @Override
    public Employee getDataFromResultSetTable(ResultSet rs) throws SQLException{
        Employee Employee = new Employee();                               
        Employee.setId(rs.getInt(1));                
        Employee.setLastName(rs.getString(2));                
        Employee.setName(rs.getString(3));                
        Employee.setPatronymic(rs.getString(4));             
        Employee.setPhone1(rs.getString(5));
        Employee.setPhone2(rs.getString(6));                
        Employee.setNumMedicalBook(rs.getInt(7));                
        Employee.setIsDeleted(rs.getBoolean(8));                           
        Employee.setUsDepartmentName(rs.getString(9));  
        Employee.setUsGroupName(rs.getString(10));
        Employee.setUsPositionName(rs.getString(11));
        Employee.setInstitutionId(rs.getInt(12));
        Employee.setInstitutionName(rs.getString(13));
        Employee.setInstitutionAddress(rs.getString(14));
        if (rs.getString(15)==null)
                Employee.setInstitutionType(null);
            else 
                Employee.setInstitutionType(Institution.InstitutionType.valueOf(rs.getString(15))); 
        Employee.setTypeUnitId(rs.getInt(16));
        Employee.setMainEmployeeId(rs.getInt(17));
        Employee.setEmploymentDate(rs.getDate(18));
        return Employee;
    }    
        
    // ==================== PreparedStatment UPSERT ====================
    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException{
//                + "`LastName`=?,`Name`=?,`Patronymic`=?,`UsDepartmentId`=?,"
//                + "`UsGroupId`=?,`UsPositionId`=?,`Sex`=?,`Phone1`=?,`Phone2`=?,"
//                + "`Email`=?,`Photo`=?,`Berthday`=?,`Note`=?,`DateAdd`=?,"
//                + "`DateChange`=?,`EmployeeAddId`=?,`EmployeeChangeId`=?,`NumMedicalBook`=?,`isDeleted`=?,"
//                + "`RegionId`=?, `MainEmployeeId`=?, `RootId`=?, `IsAccessLogin`=?, `Login`=?, "
//                + "`Password`=?, `InstitutionId`=?, InstitutionType = ?"
        ps.setString(1, this.getLastName());                
        ps.setString(2, this.getName());              
        ps.setString(3, this.getPatronymic());          
        //ps.setInt(4, this.getUsDepartmentId());
        
        //ps.setInt(5, this.getUsGroupId());             
        ps.setInt(4, this.getUsPositionId());          
        ps.setString(5, this.getSex().toString()); 
        ps.setString(6, this.getPhone1()); 
        ps.setString(7, this.getPhone2()); 
        
        ps.setString(8, this.getEmail()); 
        if (this.getPhoto()==null)  ps.setNull(9, 0); 
        else ps.setBytes(9, this.getPhoto());
        ps.setDate(10, new java.sql.Date(this.getBerthday().getTime()));                      
        ps.setString(11, this.getNote());   
        ps.setDate(12, new java.sql.Date(this.getDateAdd().getTime()));
        
        ps.setTimestamp(13, new java.sql.Timestamp(getDateChange().getTime()));       
        ps.setInt(14, this.getEmployeeAddId()); 
        ps.setInt(15, this.getEmployeeChangeId());
        if (this.getNumMedicalBook()<=0) ps.setNull(16, 0); 
        else  ps.setInt(16, this.getNumMedicalBook()); 
        ps.setBoolean(17, this.isIsDeleted()); 
        
        ps.setInt(18, this.getRegionId()); 
        ps.setInt(19, this.getMainEmployeeId());         
        ps.setInt(20, this.getRootId()); 
        ps.setBoolean(21, this.isIsAccessLogin()); 
        ps.setString(22, this.getLogin()); 
        
        ps.setString(23, this.getPassword()); 
        if (this.getInstitutionId() == null) ps.setNull(24, 0);
        else {
            if (this.getInstitutionId() > 0 ) ps.setInt(24, this.getInstitutionId());
            else ps.setNull(24, 0);
        }
        if (this.getInstitutionType()==null)  ps.setNull(25, 0);
        else ps.setString(25, this.getInstitutionType().toString()); 
        ps.setDate(26, new java.sql.Date(this.getEmploymentDate().getTime()));
    }
        
    
     
    // ========================= ПРОЧЕЕ =========================   
    private Employee cloneAll(){
        Employee newEmp = new Employee();
        newEmp.setInstitutionId(InstitutionId);
        newEmp.setRootId(RootId);
        newEmp.setIsAccessLogin(IsAccessLogin);
        newEmp.setLogin(Login);
        newEmp.setPassword(Password);
        newEmp.setUsDepartmentName(UsDepartmentName);
        newEmp.setUsGroupName(UsGroupName);
        newEmp.setUsPositionName(UsPositionName);
        newEmp.setRegionName(RegionName);
        newEmp.setMainEmployeeId(MainEmployeeId); 
        newEmp.setId(Id);
        newEmp.setName(getName());
        newEmp.setLastName(getLastName());
        newEmp.setPatronymic(getPatronymic());
        newEmp.setUsDepartmentId(UsDepartmentId);
        newEmp.setUsGroupId(UsGroupId);
        newEmp.setUsPositionId(UsPositionId);
        newEmp.setSex(Sex);
        newEmp.setPhone1(Phone1);
        newEmp.setPhone2(Phone2);
        newEmp.setPhoto(Photo);
        newEmp.setBerthday(Berthday);
        newEmp.setNote(Note);
        newEmp.setDateAdd(getDateAdd());
        newEmp.setDateChange(getDateChange());
        newEmp.setEmployeeAddId(getEmployeeAddId());
        newEmp.setEmployeeChangeId(getEmployeeChangeId());
        newEmp.setNumMedicalBook(NumMedicalBook);
        newEmp.setIsDeleted(IsDeleted);
        newEmp.setRegionId(RegionId);
        newEmp.setEmail(Email);
        newEmp.setInstitutionName(InstitutionName);
        newEmp.setInstitutionAddress(InstitutionAddress);
        newEmp.setInstitutionType(InstitutionType);
       
        return newEmp;
    }
        
    // ПЕРЕДЕЛАТЬ!!!!
    public Integer CheckNumMedicalBookFromEmployee(SQLConnection SQLConnection, Integer NumMedicalBook){
        Integer IdEmployee = 0;
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        try{            
            String query = "SELECT `id` FROM Employee WHERE `NumMedicalBook`= '"+NumMedicalBook+"' LIMIT 1";    
            ResultSet rs = SQLConnection.getStmt().executeQuery(query);   
            //int Key = 0;
            while (rs.next()) {
                    IdEmployee = rs.getInt(1);
                }
            rs.close(); 
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            if (IsNewCon) SQLConnection.closeConnection();
        } 
        return IdEmployee;
    }
    public boolean CheckPhone(SQLConnection SQLConnection, Integer Phone){
        Boolean IsPhone = false;
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        try{            
            String query = "SELECT `Phone` FROM Employee WHERE `Phone`= Phone LIMIT 1";    
            ResultSet rs = SQLConnection.getStmt().executeQuery(query);   
            //int Key = 0;
            while (rs.next()) {
                    IsPhone = (rs.getInt(1) > 0 ? true : false);
                }
            rs.close(); 
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            SQLConnection.closeConnection();
            IsPhone = false;
        } finally {
            if (IsNewCon) SQLConnection.closeConnection();
        } 
        return IsPhone;
    }
    public TreeMap<Integer,Employee> getNameEmployeeByTypeUnit(Integer TypeUnit, SQLConnection SQLConnection ){
        DefaultComboBoxModel MyDCB = new DefaultComboBoxModel();
        TreeMap<Integer,Employee> EmplTM = new TreeMap<>();
        Boolean IsNewCon = false;
        if (SQLConnection.getCon()==null || SQLConnection.getStmt()==null){
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        //String[] Names;
        try{            
            String str = "SELECT "
                    + "EM.`id`, EM.`LastName`, EM.`Name`, EM.`Patronymic` "
                    + "FROM `Employee` EM "
                    + "left join UsPosition on (EM.UsPositionId = UsPosition.id) "
                    + "Left join UsDepartment on (UsPosition.UsDepartmentId = UsDepartment.Id) "
                    + "Left join UsGroup on (UsDepartment.UsGroupId = UsGroup.Id) "
                    + "%TypeUnit"
                    + " and EM.UsPositionId in (41,74,86,101,109) "
                    + "GROUP BY EM.`id`";
            
            if (TypeUnit <= 0)
                str = str.replace("%TypeUnit", "");
            else 
                str = str.replace("%TypeUnit", " Where UsGroup.TypeUnitId = " + TypeUnit + " ");
            
            if (getUsPositionId()> 0)
                str = str.replace("%InstitutionType", " and UsDepartment.UsPositionId in (41,74,86,101,109)");
            else 
                str = str.replace("%InstitutionType", "");
//            if (TypeUnit == null)
//                str = str.replace("%InstitutionType", "");
//            else 
//                str = str.replace("%InstitutionType", " and EM.InstitutionType = '" + Type + "' ");
            
            
//            if (TypeInstitution.equals("Школьное направление"))
//                str = str.replace("%InstitutionType", " Where EM.UsPositionId in (41,74) ");
//            else if(TypeInstitution.equals("Дошкольное направление"))
//                str = str.replace("%InstitutionType", " Where EM.UsPositionId in (86,101) ");
//            else 
//                str = str.replace("%InstitutionType", " Where EM.UsPositionId in (-1) "); 
            
            SQLConnection.setRs(SQLConnection.getStmt().executeQuery(str));
            ResultSet rs = SQLConnection.getRs();
            while (rs.next()) {   
                Employee empl = new Employee();
                empl.setId(rs.getInt(1));
                empl.setLastName(rs.getString(2));
                empl.setName(rs.getString(3));
                empl.setPatronymic(rs.getString(4));
                //MyDCB.addElement(rs.getInt(1) +" - "+ getFullName(getLastName(),getName(),getPatronymic())); 
                EmplTM.put(rs.getInt(1), empl);
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            System.out.println("com.Class.Employee.getNameEmployeeByTypeUnit() -" + sqlEx.getMessage());
            SQLConnection.closeConnection();
        } finally {
            if (IsNewCon) SQLConnection.closeConnection();
        } 
        return EmplTM;
    } 
    
    public enum EnumSex{
        Female,
        Male
    }
}
