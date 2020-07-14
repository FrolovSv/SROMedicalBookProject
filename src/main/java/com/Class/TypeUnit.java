
package com.Class;

import com.SQL.SQLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TypeUnit extends MainClassProgect<TypeUnit>{
    
    private Integer id = -1;
    private String Name = "";
    private String NameLong = "";
    private String Note = "";
    private String TableName = "TypeUnit";
    
    @Override
    public String getQuerySelect(Integer Limit, Integer UnitId) {
        String str = "";
        str = "SELECT "
                + "TU.`Id`, TU.`Name`, TU.`NameLong`, TU.`Note` "
                + "FROM `TypeUnit` TU "
                + "GROUP BY `TU`.`id`"
                + (Limit <= 0 ? "" : "Limit " + Limit);
        return str;
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
        ps.setInt(1, this.getId());              
        ps.setString(2, this.getName());              
        ps.setString(3, this.getNameLong());                                                         
        ps.setString(4, this.getNote());                                                         
    }

    @Override
    public TypeUnit getDataFromResultSet(ResultSet rs) throws SQLException {
        TypeUnit TypeUnit = new TypeUnit();
        TypeUnit.setId(rs.getInt(1));                
        TypeUnit.setName(rs.getString(2));                
        TypeUnit.setNameLong(rs.getString(3));                
        TypeUnit.setNote(rs.getString(4));                
        return TypeUnit;
    }

    @Override
    public TypeUnit getDataFromResultSetTable(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] getDataForTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    
}
