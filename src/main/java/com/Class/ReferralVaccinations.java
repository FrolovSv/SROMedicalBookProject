
package com.Class;

import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;
import lombok.Data;

@Data
public class ReferralVaccinations extends MainClassProgect<ReferralVaccinations>{
    
    private Integer Id = -1;
    private Integer MedicalBookId = -1;
    private Integer EmloyeeId = -1;
    private boolean Therapist = false;
    private boolean Otolaryngologist = false;
    private boolean Dentist = false;
    private boolean ExpertInNarcology = false;
    private boolean Psychiatrist = false;
    private boolean Dermatovenerologist = false;
    private boolean Fluorography = false;
    private boolean Helminths = false;
    private boolean IntestinalInfection = false;
    private boolean TyphoidFever  = false;
    private boolean Staphylococcus = false;
    private boolean Validation = false;
    private boolean Shigellvak = false;
    private boolean Hepatitis_A = false;
    private boolean Hepatitis_A2 = false;
    private boolean Diphtheria = false;
    private boolean Measles = false;
    private boolean Measles_2 = false;
    private boolean Hepatitis_B = false;
    private boolean Hepatitis_B2 = false;
    private boolean Hepatitis_B3 = false;
    private boolean ValidationForYear = false;
    private boolean Rubella = false;
    private String Note = "";
    private Integer RegionId = -1;

    
    public ReferralVaccinations(){}
    
    @Override // не определено
    public Object[] getDataForTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // ========================== установки sql ============================
    // ==================== геттеры SELECT ====================
    @Override
    public String getQuerySelect(Integer Limit, Integer ReferralVaccinationsId){
        String str = "SELECT "
                + "RV.`id`, RV.`MedicalBookId`, RV.`EmloyeeId`, RV.`UserAddId`, RV.`DateAdd`, "
                + "RV.`DateChange`, RV.`Therapist`, RV.`Otolaryngologist`, RV.`Dentist`, RV.`ExpertInNarcology`, "
                + "RV.`Psychiatrist`, RV.`Dermatovenerologist`, RV.`Fluorography`, RV.`Helminths`, RV.`IntestinalInfection`, "
                + "RV.`TyphoidFever`, RV.`Staphylococcus`, RV.`Validation`, RV.`Shigellvak`, RV.`Hepatitis_A`, "
                + "RV.`Hepatitis_A2`, RV.`Diphtheria`, RV.`Measles`, RV.`Hepatitis_B`, RV.`Hepatitis_B2`, "
                + "RV.`Hepatitis_B3`, RV.`ValidationForYear`, RV.`Note`, RV.`RegionId`, RV.`Rubella`, RV.`Measles_2` "
                + "FROM `ReferralVaccinations` RV "
                + (ReferralVaccinationsId > 0 ? "Where MB.`NumMedicalBook` = "+ReferralVaccinationsId+" " : "")
                + "GROUP BY `RV`.`id` "
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
    // ==================== геттеры UPSERT====================
    @Override
    public String getQueryIsExistKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // ==================== геттеры UPSERT====================
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
    public ReferralVaccinations getDataFromResultSet(ResultSet rs) throws SQLException{
        ReferralVaccinations ReferralVaccinations = new ReferralVaccinations();                
            ReferralVaccinations.setId(rs.getInt(1));                
            ReferralVaccinations.setMedicalBookId(rs.getInt(2));                
            ReferralVaccinations.setEmloyeeId(rs.getInt(3));                
            ReferralVaccinations.setEmployeeAddId(rs.getInt(4));                
            ReferralVaccinations.setDateAdd(rs.getDate(5));                
            ReferralVaccinations.setDateChange(rs.getDate(6));                             
            ReferralVaccinations.setTherapist(rs.getBoolean(7));  
            ReferralVaccinations.setOtolaryngologist(rs.getBoolean(8));
            ReferralVaccinations.setDentist(rs.getBoolean(9));                
            ReferralVaccinations.setExpertInNarcology(rs.getBoolean(10));                
            ReferralVaccinations.setPsychiatrist(rs.getBoolean(11));                
            ReferralVaccinations.setDermatovenerologist(rs.getBoolean(12));                              
            ReferralVaccinations.setFluorography(rs.getBoolean(13));                
            ReferralVaccinations.setHelminths(rs.getBoolean(14));                
            ReferralVaccinations.setIntestinalInfection(rs.getBoolean(15));                
            ReferralVaccinations.setTyphoidFever(rs.getBoolean(16));
            ReferralVaccinations.setStaphylococcus(rs.getBoolean(17));
            ReferralVaccinations.setValidation(rs.getBoolean(18));
            ReferralVaccinations.setShigellvak(rs.getBoolean(19));
            ReferralVaccinations.setHepatitis_A(rs.getBoolean(20));  
            ReferralVaccinations.setHepatitis_A2(rs.getBoolean(21));  
            ReferralVaccinations.setDiphtheria(rs.getBoolean(22));  
            ReferralVaccinations.setMeasles(rs.getBoolean(23));  
            ReferralVaccinations.setHepatitis_B(rs.getBoolean(24));  
            ReferralVaccinations.setHepatitis_B2(rs.getBoolean(25));  
            ReferralVaccinations.setHepatitis_B3(rs.getBoolean(26));   
            ReferralVaccinations.setNote(rs.getString(27));  
            ReferralVaccinations.setRegionId(rs.getInt(28));  
            ReferralVaccinations.setRubella(rs.getBoolean(29));  
            ReferralVaccinations.setMeasles_2(rs.getBoolean(30));
        return ReferralVaccinations;
    }
    @Override // не определено
    public ReferralVaccinations getDataFromResultSetTable(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ==================== PreparedStatment для UPSERT запросов ====================
    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException{
        ps.setInt(1, this.getMedicalBookId());
        ps.setInt(2, this.getEmloyeeId());
        ps.setInt(3, this.getEmployeeAddId());
        ps.setDate(4,(java.sql.Date) this.getDateAdd());
        ps.setDate(5,(java.sql.Date) this.getDateChange());
        ps.setBoolean(6, this.isTherapist());
        ps.setBoolean(7, this.isOtolaryngologist());
        ps.setBoolean(8, this.isDentist());
        ps.setBoolean(9, this.isExpertInNarcology());
        ps.setBoolean(10, this.isPsychiatrist());
        ps.setBoolean(11, this.isDermatovenerologist());
        ps.setBoolean(12, this.isFluorography());
        ps.setBoolean(13, this.isHelminths());
        ps.setBoolean(14, this.isIntestinalInfection());
        ps.setBoolean(15, this.isTyphoidFever());
        ps.setBoolean(16, this.isStaphylococcus());
        ps.setBoolean(17, this.isValidation());
        ps.setBoolean(18, this.isShigellvak());
        ps.setBoolean(19, this.isHepatitis_A());
        ps.setBoolean(20, this.isHepatitis_A2());
        ps.setBoolean(21, this.isDiphtheria());
        ps.setBoolean(22, this.isMeasles());
        ps.setBoolean(23, this.isHepatitis_B());
        ps.setBoolean(24, this.isHepatitis_B2());
        ps.setBoolean(25, this.isHepatitis_B3());
        ps.setBoolean(26, this.isValidationForYear());
        ps.setString(27, this.getNote());
        ps.setInt(28, this.getRegionId());
        ps.setBoolean(29, this.isRubella());
        ps.setBoolean(30, this.isMeasles_2());
    }
        
    
}
