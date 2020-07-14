
package com.Class;

public class CheckingValue {
    
    public CheckingValue(){}
    
    public static boolean isPhone(String s) throws NumberFormatException {
        try {
            if (s.contains("."))
                Double.parseDouble(s);
            else{
                Long.parseLong(s);  
            }            
                return s.length() <= 11;
            } 
        catch (NumberFormatException e) {
                //addLogsToBase(getClassName()+e.getMessage());
                return false;                
            }
    } 
       
    public static boolean isEmail(String s) {
        return s.contains("@");
    } 
    
}
