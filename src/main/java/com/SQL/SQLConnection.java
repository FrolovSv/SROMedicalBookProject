
package com.SQL;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


// организация подключения к базе SQL
public class SQLConnection { 
    
    private Boolean isConnected = false;
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    
    private static String url = "jdbc:mysql://188.127.230.7/medicalbook"; // база
    private static String user = "medbook_rootuser";  //пользователь
    private static String password = "K0s5I0y4"; //пароль
    
    
    public SQLConnection(){        
    }
    
    public boolean checkInternetConnection() {
        Boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL("http://www.ya.ru").openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    
    public void getConnection(){    
        if (checkInternetConnection()){
            if (!isConnected){
                try {
                    con = DriverManager.getConnection(url, user, password);
                    stmt = con.createStatement();
                    isConnected = true;
                } catch (SQLException ex) {
                    con = null;
                    stmt = null;
                    isConnected = false;
                }
            }else {
                System.err.println("Подключение уже установлено");
            }
        }else {
            System.err.println("Нет подключения к интернет");
            JOptionPane.showMessageDialog(null,
                "Отсутствует соединение с интернетом",
                "Ошибка!",
                JOptionPane.ERROR_MESSAGE);
            isConnected = false;
        }
    }
    
    public void setUser(String User){user = User;}
    public void setPassword(String Pass){password = Pass;}
       
    public  Statement getStmt(){return stmt;}     
    public Connection getCon(){return con;}
    public ResultSet getRs(){return rs;}

    public Boolean IsConnected() {return isConnected;}    
    
    public void setRs(ResultSet rs) {this.rs = rs;}
    
    public void closeConnection(){
        try{
            try { 
                this.con.close();  
                this.con = null;
            } catch(SQLException se) {
            }
            try { 
                this.stmt.close(); 
                this.stmt = null;
            } catch(SQLException se) {
            }
            try { 
                this.rs.close(); 
                this.rs = null;
            } catch(SQLException se) {
            }
        }catch (Exception ex) {
            this.con = null;
            this.stmt = null;
            this.rs = null;            
        }        
    }
    
//    public void setSQLConnection(SQLConnection SQLConn){
//        this = SQLConn;
//    }
}
