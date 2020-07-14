
package com.Class;

import com.SQL.SQLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Institution extends MainClassProgect<Institution>{
    private Integer Id = 0;
    private Integer MainInstitutionId = 0;
    private String Name = "";
    private String NameLong = "";
    private String Address = "";
    private Boolean IsClosed = false;
    private String Note = "";
    private Integer EmployeeCuratorId = 0;
    private Integer RegionId = 0;
    
    private Integer TypeUnitId = 0;
    private String TypeUnitName = "";
    

    private String MainInstitutionName = "";
    private String MainInstitutionAddress = "";
    
    //private String TypeInstitution = "";
    private InstitutionType TypeInst = null;
    
    private String EmployeeCuratorName = "";
    private String EmployeeCuratorLastName = "";
    private String EmployeeCuratorPatronymic= "";
    
    private String TextSearch = "";

    // =================== геттеры и сеттеры ===================
   
    public String getEmployeeCuratorName() {      return EmployeeCuratorName == null ? "" : EmployeeCuratorName;    }
    public String getEmployeeCuratorLastName() {  return EmployeeCuratorLastName == null ? "" : EmployeeCuratorLastName;    }
    public String getEmployeeCuratorPatronymic() {return EmployeeCuratorPatronymic == null ? "" : EmployeeCuratorPatronymic;    }
    public String getMainInstitutionName() {      return MainInstitutionName == null ? "" : MainInstitutionName;     }
    public String getMainInstitutionAddress() {   return MainInstitutionAddress == null ? "" : MainInstitutionAddress;     }
    public Integer getEmployeeCuratorId() {return EmployeeCuratorId == null ? 0 : EmployeeCuratorId;    }
    public Integer getMainInstitutionId() {return MainInstitutionId == null ? 0 : MainInstitutionId;}
    @Override
    public String getName() {        return Name == null ? "" : Name;   }
    
    // =================== обяъвление класса =================== 
    public Institution(){}
    
    @Override
    public Object[] getDataForTable() {
        Object[] DataForTable = new Object[8];  
        
        DataForTable[0] = (int) 1;
        DataForTable[1] = getId();
        DataForTable[2] = getName();
        DataForTable[3] = getAddress();
        DataForTable[4] = !getMainInstitutionId().equals(getId()) ? getMainInstitutionName() : "Главное здание";
        DataForTable[5] = getTypeUnitName();
        DataForTable[6] = getSurnameInitials(getEmployeeCuratorLastName(),getEmployeeCuratorName(),getEmployeeCuratorPatronymic());
        DataForTable[7] = getIsClosed() ? "Закрыто" : "Работает";
        return DataForTable;
    }
    public Object[] getDataForTableUnder() {
        Object[] DataForTable = new Object[6]; 
        
        DataForTable[0] = (int) 1;
        DataForTable[1] = getId();
        DataForTable[2] = getTypeInst();
        DataForTable[3] = getName();
        DataForTable[4] = getAddress();
        DataForTable[5] = getIsClosed() ? "Закрыто" : "Работает";
        return DataForTable;
    }
    
    // ========================== установки sql ============================
    // ==================== геттеры  SELECT ====================
    @Override
    public String getQuerySelect(Integer Limit, Integer InstitutionId){
        String str = "SELECT "
                + "Ins.`Id`, Ins.`MainInstitutionId`, Ins.`Name`, Ins.`NameLong`, Ins.`Address`, "
                + "Ins.`IsClosed`, Ins.`Note`, Ins.`TypeInstitution`, Ins.`EmployeeCuratorId`, "
                + "MainIns.`Name`, MainIns.`Address`, "
                + "Emp.`LastName`, Emp.`Name`, Emp.`Patronymic`, Ins.RegionId, Ins.TypeUnitId, TypeUnit.Name "
                + "FROM `Institution` Ins "
                + "LEFT join `Institution` MainIns on (Ins.MainInstitutionId = MainIns.Id) "
                + "LEFT join `Employee` Emp on (Ins.EmployeeCuratorId = Emp.Id) "
                + "LEFT join `TypeUnit` on (Ins.TypeUnitId = TypeUnit.Id) "
                + (InstitutionId > 0 ? "Where Ins.`Id` = " + InstitutionId + " " : " ")
                + "GROUP BY Ins.`id` "
                + (Limit <= 0 ? "" : "Limit " + Limit.toString());
        return str;
    }    
    @Override
    public String getQuerySelectTable(Integer Limit) {
        String str = "SELECT "        
                + "Ins.`Id`, Ins.`Name`, Ins.`Address`, Ins.`IsClosed`, "
                + "MainIst.`Name`, MainIst.`Address`, Ins.TypeInstitution, "
                + "Emp.`LastName`, Emp.`Name`, Emp.`Patronymic`, Ins.RegionId, TypeUnit.Name "
                + "FROM `Institution` Ins "
                + "LEFT join `Institution` MainIst on (Ins.MainInstitutionId = MainIst.Id) "
                + "LEFT join `Employee` Emp on (Ins.EmployeeCuratorId = Emp.Id) "
                + "LEFT join `TypeUnit` on (Ins.TypeUnitId = TypeUnit.Id) "
                + "Where Ins.IsClosed = " + (this.getIsClosed() ? 1 : 0) + " "
                + (this.getEmployeeCuratorId() <= 0 ? "" : " and Ins.EmployeeCuratorId = " + this.getEmployeeCuratorId() + " ")
                //+ (this.getTypeInst() == null ? "" : " and Ins.TypeInstitution = '" + this.getTypeInst() + "' ")
                + (this.getTypeUnitId() <= 0 ? "" : " and Ins.TypeUnitId = " + this.getTypeUnitId() + " ")
                + (this.getMainInstitutionId() <= 0 ? "" : "and Ins.MainInstitutionId = " + this.getMainInstitutionId() + " ")
                + "%2where %3where "
                + "GROUP BY Ins.`id` "
                + (Limit <= 0 ? "" : "Limit " + Limit.toString());

        if (this.getTextSearch().equals("")) {
            str = str.replace("%2where", "");
            str = str.replace("%3where", "");
        } else if (!isDigit(this.getTextSearch())) {
            str = str.replace("%2where", "and ("
                    + "   Ins.`Name` LIKE '%" + this.getTextSearch() + "%' "
                    + "or Ins.`Address` LIKE '%" + this.getTextSearch() + "%' "
                    + "or MainIst.`Name` LIKE '%" + this.getTextSearch() + "%' "
                    + "or TypeUnit.Name LIKE '%" + this.getTextSearch() + "%' "
                    + "or MainIst.`Address` LIKE '%" + this.getTextSearch() + "%') ");
            str = str.replace("%3where", "");
        } else if (isDigit(this.getTextSearch())) {
            str = str.replace("%3where", "and ( "
                    + "   Ins.`Id` LIKE '%" + this.getTextSearch() + "%' "
                    + "or Ins.`Address` LIKE '%" + this.getTextSearch() + "%' "
                    + "or Ins.`Name` LIKE '%" + this.getTextSearch() + "%' "
                    + "or MainIst.`Name` LIKE '%" + this.getTextSearch() + "%') ");
            str = str.replace("%2where", "");
        }

         return str; 
    }
    @Override
    public String getQueryIsExistKey(Integer Key) {
        String str = "SELECT count(Ins.`Id`) "
                + "FROM `Institution` Ins "
                + "Where Ins.Id = " + Key;
        return str; 
    }    
    @Override
    public String getQueryIsExistKey() {
        String str = "SELECT count(Ins.`Id`) "
                + "FROM `Institution` Ins "
                + "Where Ins.Id = " + this.getId();
        return str; 
    }    
    @Override
    public String getQueryLastKey() {
        String str = "select `Id` From Institution ORDER BY `Id` DESC Limit 1";
        return str; 
    }

    // ==================== геттеры  UPSERT ====================
    @Override
    public String getQueryUpdate(Integer UnitId) {
        String str = "UPDATE `Institution` SET "
                + "`MainInstitutionId`=?,`Name`=?,`NameLong`=?, `Address`=?, `IsClosed`=?,  "
                + "Note =?,  TypeInstitution=?, EmployeeCuratorId=?, RegionId=?, `TypeUnitId` = ?"
                + "WHERE `Institution`.`Id` = " + UnitId;
        return str; 
    }
    @Override
    public String getQueryInsert() {
        String str = "INSERT INTO `Institution` "
                + "(`MainInstitutionId`, `Name`, `NameLong`, `Address`, `IsClosed`, "
                + "Note , TypeInstitution , EmployeeCuratorId, RegionId, `TypeUnitId` ) "
                + "VALUES ("
                + "?,?,?,?,?,"
                + "?,?,?,?)";
        return str;   
    }

    // ==================== PreparedStatment для UPSERT запросов ====================
    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException{
//        + "`MainInstitutionId`=?,`Name`=?,`NameLong`=?, `Address`=?, `IsClosed`=?, "
//        + "Note =?,  TypeInstitution=?, EmployeeCuratorId=?, RegionId=? "
        //ps.setInt(1, Inst.getId());                
        if (this.getMainInstitutionId()<=0)
            ps.setNull(1, 0);
        else 
            ps.setInt(1, this.getMainInstitutionId());                
        ps.setString(2, this.getName());                
        ps.setString(3, this.getNameLong());                
        ps.setString(4, this.getAddress());                
        ps.setBoolean(5, this.getIsClosed());                
        ps.setString(6, this.getNote());                
        ps.setString(7, this.getTypeInst().toString());
        if (this.getEmployeeCuratorId()<=0)
            ps.setNull(8, 0);
        else 
            ps.setInt(8, this.getEmployeeCuratorId());                    
        ps.setInt(9, this.getRegionId());      
        ps.setInt(10, this.getTypeUnitId());      
    }
    // ==================== ResultSet для SELECT запросов ====================
    @Override
    public Institution getDataFromResultSet(ResultSet rs) throws SQLException {
//                + "Ins.`Id`, Ins.`MainInstitutionId`, Ins.`Name`, Ins.`NameLong`, Ins.`Address`, "
//                + "Ins.`IsClosed`, Ins.`Note`, Ins.`Department`, Ins.`EmployeeCuratorId`, "
//                + "MainIns.`Name`, MainIns.`Address`, "
//                + "Emp.`LastName`, Emp.`Name`, Emp.`Patronymic`, Ins.RegionId"
        Institution Institution = new Institution();
        Institution.setId(rs.getInt(1));                
        Institution.setMainInstitutionId(rs.getInt(2));                
        Institution.setName(rs.getString(3));                
        Institution.setNameLong(rs.getString(4));                              
        Institution.setAddress(rs.getString(5));                              
        Institution.setIsClosed(rs.getBoolean(6));
        Institution.setNote(rs.getString(7));
        Institution.setTypeInst(InstitutionType.valueOf(rs.getString(8)));
        Institution.setEmployeeCuratorId(rs.getInt(9));
        Institution.setMainInstitutionName(rs.getString(10));
        Institution.setMainInstitutionAddress(rs.getString(11));
        Institution.setEmployeeCuratorLastName(rs.getString(12));
        Institution.setEmployeeCuratorName(rs.getString(13));
        Institution.setEmployeeCuratorPatronymic(rs.getString(14));
        Institution.setRegionId(rs.getInt(15));        
        Institution.setTypeUnitId(rs.getInt(16));        
        Institution.setTypeUnitName(rs.getString(17));        
        return Institution;
    }
    @Override
    public Institution getDataFromResultSetTable(ResultSet rs) throws SQLException {
        Institution Institution = new Institution();
        Institution.setId(rs.getInt(1));               
        Institution.setName(rs.getString(2));                
        Institution.setAddress(rs.getString(3));  
        Institution.setIsClosed(rs.getBoolean(4));        
        Institution.setMainInstitutionName(rs.getString(5));                
        Institution.setMainInstitutionAddress(rs.getString(6));                
        Institution.setTypeInst(InstitutionType.valueOf(rs.getString(7)));
        Institution.setEmployeeCuratorLastName(rs.getString(8));
        Institution.setEmployeeCuratorName(rs.getString(9));
        Institution.setEmployeeCuratorPatronymic(rs.getString(10));
        Institution.setRegionId(rs.getInt(11));        
        Institution.setTypeUnitName(rs.getString(12)); 
        return Institution;
    }
    
    public enum InstitutionType{
        SCHOOL,
        KINDERGARDEN,
        GOVERNMENT,
        SOCIAL,
        HOSPITALS
    }
          
//    public Institution SQLSelect(Integer UnitId){
//        Institution Institution = new Institution();
//        Boolean IsNewCon = false;
//        SQLConnection sql = new SQLConnection();
//        if (!sql.IsConnected()){
//            sql.getConnection();
//            IsNewCon = true;
//        }
//        //AbstactClass TreeMap = new TreeMap();
//        try{                                               
//            sql.setRs(sql.getStmt().executeQuery(getQuerySelect(1,UnitId)));
//            ResultSet rs = sql.getRs();
//            while (rs.next()) {                           
//                Institution = Institution.getDataFromResultSet(rs);
//            }
//            rs.close();
//        } catch (SQLException sqlEx) {
//            sqlEx.getMessage();
//            System.out.println("com.SQL.SQLQuery.SQLSelect() - " + sqlEx.getMessage());
//            sql.closeConnection();
//        } finally {
//            if (IsNewCon) sql.closeConnection();
//        } 
//    return Institution;
//    } 
    
    
}
