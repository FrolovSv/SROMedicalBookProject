
package com.Class;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import lombok.Data;

@Data
public abstract class MainClassProgect<Ob>{
    private Integer Id = -1;
    private String Name = "";
    private String TableName = "";

    private String LastName = "";
    private String Patronymic = "";
    
    private Integer EmployeeAddId = -1; 
    private Integer EmployeeChangeId = -1; 
    private Date DateAdd = new Date(0);
    private Date DateChange = new Date();
    private String TextSearch = "";
    
    public Integer getEmployeeAddId() {        return EmployeeAddId > 0 ? EmployeeAddId : 0;    }
    public Integer getEmployeeChangeId() {        return EmployeeChangeId > 0 ? EmployeeChangeId : 0;    }

// =================== обяъвление класса =================== 
    public MainClassProgect(){}
    
    public String enumToString(ArrayList<?> MedBookView, String Seporator) {
        String str = "";
        if (MedBookView.size()>0){  
            StringBuilder bstr = new StringBuilder("");
            MedBookView.forEach((object) -> {
                bstr.append(object + Seporator);
            });
            str = bstr.toString().substring(0, bstr.toString().length() - 1);
        }
        return str;
    }
    public String enumToString(ArrayList<?> MedBookView) {
        return enumToString(MedBookView, ",");
    }
    
    public String getFullName(String str1, String str2, String str3) {
        String full = "";
        if (!str1.equals("")) {
            full = full + str1 + " ";
        }
        if (!str2.equals("")) {
            full = full + str2;
        }
        if (!str3.equals("")) {
            full = full + " " + str3;
        }
        return full;
    }

    public String getFullName() {
        String full = "";
        if (!getLastName().equals("")) {
            full = full + getLastName() + " ";
        }
        if (!getName().equals("")) {
            full = full + getName();
        }
        if (!getPatronymic().equals("")) {
            full = full + " " + getPatronymic();
        }
        return full;
    }

    public String getSurnameInitials(String str1, String str2, String str3) {
        String full = "";
        if (!str1.equals("")) {
            full = full + str1 + " ";
        }
        if (!str2.equals("")) {
            full = full + str2.charAt(0) + ".";
        }
        if (!str3.equals("")) {
            full = full + str3.charAt(0) + ".";
        }
        return full;
    }

    public String getSurnameInitials() {
        String full = "";
        if (!getLastName().equals("")) {
            full = full + getLastName() + " ";
        }
        if (!getName().equals("")) {
            full = full + getName().charAt(0) + ".";
        }
        if (!getPatronymic().equals("")) {
            full = full + getPatronymic().charAt(0) + ".";
        }
        return full;
    }
    
    // ========================== установки sql ============================
    // ==================== геттеры  SELECT ====================
    //@Override
    public abstract String getQuerySelect(Integer Limit, Integer UnitId); 
    //@Override
    public abstract String getQuerySelectTable(Integer Limit); 
    //@Override
    public abstract String getQueryLastKey();
    //@Override
    public abstract String getQueryIsExistKey(Integer Key);
    //@Overrid
    public abstract String getQueryIsExistKey();
    // ==================== геттеры  UPSERT ====================
    //@Override
    public abstract String getQueryUpdate(Integer UnitId);
    //@Override
    public abstract String getQueryInsert();

    // ==================== PreparedStatment для UPSERT запросов ====================
    //@Override
    public abstract void setPreparedStatment(PreparedStatement ps) throws SQLException;
    
    // ==================== ResultSet для SELECT запросов ====================
    //@Override
    public abstract Ob getDataFromResultSet(ResultSet rs) throws SQLException;
    //@Override
    public abstract Ob getDataFromResultSetTable(ResultSet rs) throws SQLException;
    //@Override
    public abstract Object[] getDataForTable();
    
    
    public static String getFieldNamesStr(String NameTableShort, Field[] allFieldsClass){
        String str = "";
        int i = 0;
        for (Field field : allFieldsClass)
        {
            if (i==0) str = str.concat(NameTableShort+"."+field.getName() + " ");
            else 
                str = str.concat(", " + NameTableShort+"."+field.getName() + " ");
            i++;
        }
        return str;
    }
    public static Boolean isDigit(String s) throws NumberFormatException {
        try {
            if (s.contains("."))
                Double.parseDouble(s);
            else{
                Long.parseLong(s);  
            }            
            return true;
            } 
        catch (NumberFormatException e) {
                //addLogsToBase(getClassName()+e.getMessage());
                return false;                
            }
    }
    public static String SubSting(String str,int Lenght){
        if(str.length()>Lenght & !str.equals("")){
            str = str.substring(0, Lenght);
            str = str.concat("...");
        }
        return str;
    }
    
    
    DefaultComboBoxModel getNamesFromTreeMap(TreeMap<Integer,? extends MainClassProgect> TreeMap, String FirstElement){
        DefaultComboBoxModel ComboBoxModel = new DefaultComboBoxModel();
        TreeMap.entrySet().forEach((entry) -> {
            if (entry.getKey()==0) 
                ComboBoxModel.addElement(FirstElement);
            else
                ComboBoxModel.addElement(entry.getValue().getName());
        });
        return ComboBoxModel;
    }
}
