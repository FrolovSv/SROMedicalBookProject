/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.notUse;

import com.Class.Employee;
import com.Class.MainClassProgect;
import com.Class.MedicalBook;
import com.Class.MedicalBook.EnumBorneDiseases;
import com.Class.MedicalBook.EnumHepatitisAvaccine;
import com.Class.TypeUnit;
import com.Class.UsDepartment;
import com.Class.UsGroup;
import com.Class.UsPosition;
import com.Progect.MedicalBookProgectMaven;
import com.SQL.SQLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.control.CheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.plaf.InternalFrameUI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author SusPecT
 */
//@Data
//@EqualsAndHashCode(callSuper=false)
public abstract class MainInternalFrame extends javax.swing.JInternalFrame {

    public final static String FirstElementDepartment = "Все подразделения";
    public final static String FirstElementGroup = "Все группы";
    public final static String FirstElementPosition = "Все должности";
    public final static String FirstElementTypeUnit = "Все типы";
    public final static String FirstElementCurator = "Все управляющие";
    //private final static InternalFrameUI JIFcallingFrame = 0;
    
    private MedicalBookProgectMaven MBProgect;    
    
    public MainInternalFrame(MedicalBookProgectMaven medicalBookProgectMaven) {
        this.MBProgect = medicalBookProgectMaven;
    }
    
    public abstract MainInternalFrame choise(InternalFrameUI JIFcallingFrame);
    
    public abstract MainInternalFrame choiseMain(InternalFrameUI JIFcallingFrame);
    
    public abstract MainInternalFrame view();
    
    public DefaultComboBoxModel getValuesGroup(TreeMap<Integer, UsGroup> GroupTreeMap, boolean isAddFirstElement) {
        DefaultComboBoxModel ComboBoxModel = new DefaultComboBoxModel();
        if (isAddFirstElement) {
            ComboBoxModel.addElement(FirstElementGroup);
        }
        if (GroupTreeMap.size() > 0) {
            GroupTreeMap.entrySet().forEach((entry) -> {
                ComboBoxModel.addElement(entry.getValue().getName());
            });
        }
        return ComboBoxModel;
    }   
    public DefaultComboBoxModel getValuesGroup(boolean isAddFirstElement) {
        return getValuesGroup(MBProgect.getUsGroupTM(), isAddFirstElement);
    }
    
    public DefaultComboBoxModel getValuesTupeUnit(TreeMap<Integer, TypeUnit> TypeUnitTreeMap, boolean isAddFirstElement) {
        DefaultComboBoxModel ComboBoxModel = new DefaultComboBoxModel();
        if (isAddFirstElement) {
            ComboBoxModel.addElement(FirstElementTypeUnit);
        }
        if (TypeUnitTreeMap.size() > 0) {
            TypeUnitTreeMap.entrySet().forEach((entry) -> {
                ComboBoxModel.addElement(entry.getValue().getName());
            });
        }
        return ComboBoxModel;
    } 
    public DefaultComboBoxModel getValuesTupeUnit(boolean isAddFirstElement) {
        return getValuesTupeUnit(MBProgect.getTypeUnitTM(),isAddFirstElement);
    }
    
    public DefaultComboBoxModel getValuesDepartmentOnGroupName(TreeMap<Integer, UsDepartment> DepartmentTreeMap, String GroupName, boolean isAddFirstElement) {
        DefaultComboBoxModel ComboBoxModel = new DefaultComboBoxModel();
        if (isAddFirstElement) {
            ComboBoxModel.addElement(FirstElementDepartment);
        }
        if (DepartmentTreeMap.size() > 0 && GroupName != null) {
            DepartmentTreeMap.entrySet().forEach((entry) -> {
                if (entry.getKey() > 0 && entry.getValue().getUsGroupName().equals(GroupName)) {
                    ComboBoxModel.addElement(entry.getValue().getName());
                }
            });
        }
        return ComboBoxModel;
    }
    public DefaultComboBoxModel getValuesDepartmentOnGroupName(TreeMap<Integer, UsDepartment> DepartmentTreeMap, JComboBox ComboBoxUsGroup, boolean isAddFirstElement) {
        return getValuesDepartmentOnGroupName(DepartmentTreeMap, ComboBoxUsGroup.getSelectedItem().toString(), isAddFirstElement);
    }    
    public DefaultComboBoxModel getValuesDepartmentOnGroupName(JComboBox ComboBoxUsGroup, boolean isAddFirstElement) {
        return getValuesDepartmentOnGroupName(MBProgect.getUsDepartmentTM(), ComboBoxUsGroup.getSelectedItem().toString(), isAddFirstElement);
    }
    public DefaultComboBoxModel getValuesDepartmentOnGroupName(JComboBox ComboBoxUsGroup) {
        return getValuesDepartmentOnGroupName(MBProgect.getUsDepartmentTM(), ComboBoxUsGroup.getSelectedItem().toString(), false);
    }
     
    public DefaultComboBoxModel getValuesPositionOnDepartmentName(TreeMap<Integer, UsPosition> TreeMap, String DepartmentName, boolean isAddFirstElement) {
        DefaultComboBoxModel ComboBoxModel = new DefaultComboBoxModel();
        if (isAddFirstElement) {
            ComboBoxModel.addElement(FirstElementPosition);
        }
        if (TreeMap.size() > 0 && DepartmentName != null) {
            TreeMap.entrySet().forEach((entry) -> {
                if (entry.getKey() > 0 && entry.getValue().getUsDepartmentName().equals(DepartmentName)) {
                    ComboBoxModel.addElement(entry.getValue().getName());
                }
            });
        }
        return ComboBoxModel;
    }
    public DefaultComboBoxModel getValuesPositionOnDepartmentName(TreeMap<Integer, UsPosition> TreeMap, JComboBox ComboBoxUsDepartment, boolean isAddFirstElement) {
        return getValuesPositionOnDepartmentName(TreeMap, ComboBoxUsDepartment.getSelectedItem().toString(), isAddFirstElement);
    }
    public DefaultComboBoxModel getValuesPositionOnDepartmentName(JComboBox ComboBoxUsDepartment, boolean isAddFirstElement) {
        return getValuesPositionOnDepartmentName(MBProgect.getUsPositionTM(), ComboBoxUsDepartment.getSelectedItem().toString(), isAddFirstElement);
    }
    public DefaultComboBoxModel getValuesPositionOnDepartmentName(JComboBox ComboBoxUsDepartment) {
        return getValuesPositionOnDepartmentName(MBProgect.getUsPositionTM(), ComboBoxUsDepartment.getSelectedItem().toString(), false);
    }

    public Integer getKeyByTreeMap(TreeMap<Integer, ? extends MainClassProgect> TreeMap, String Name) {
        Integer id = 0;
        for (Map.Entry<Integer, ? extends MainClassProgect> entry : TreeMap.entrySet()) {
            if (entry.getKey() > 0) {
                if (entry.getValue().getName().equals(Name)) {
                    return id = entry.getKey();
                }
            }
        }
        return id;
    }
    public Integer getKeyUsGroupByName(String Name) {
        Integer id = 0;
        for (Map.Entry<Integer,UsGroup> entry : MBProgect.getUsGroupTM().entrySet()) {
            if (entry.getKey() > 0) {
                if (entry.getValue().getName().equals(Name)) {
                    return id = entry.getKey();
                }
            }
        }
        return id;
    }
    
    public Integer getKeyByUsDepartmentName(TreeMap<Integer, UsPosition> TreeMap, Integer UsDepartmentId, String UsPositionName) {
        Integer id = 0;
        for (Map.Entry<Integer, UsPosition> entry : TreeMap.entrySet()) {
            if (entry.getKey() > 0) {
                if (entry.getValue().getUsDepartmentId().equals(UsDepartmentId) && entry.getValue().getName().equals(UsPositionName)) {
                    id = entry.getKey();
                    break;
                }
            }
        }
        return id;
    }
    public Integer getKeyByUsDepartmentName(Integer UsDepartmentId, String UsPositionName) {        
        return getKeyByUsDepartmentName(MBProgect.getUsPositionTM(), UsDepartmentId, UsPositionName);
    }

    public DefaultComboBoxModel getFullNamesCurarorByTypeUnitId(Integer TypeUnitId, boolean FirstElement){
        DefaultComboBoxModel ComboBoxModel = new DefaultComboBoxModel();
        if (FirstElement) ComboBoxModel.addElement(FirstElementCurator);
        
        TreeMap<Integer,Employee> EmployeeCuratorTM = new Employee().getNameEmployeeByTypeUnit(TypeUnitId, new SQLConnection());   
        if (EmployeeCuratorTM.size()>0)
            for (Map.Entry<Integer,Employee> entry : EmployeeCuratorTM.entrySet()) {
                if (entry.getValue().getTypeUnitId().equals(TypeUnitId))
                    ComboBoxModel.addElement(entry.getValue().getFullName());
            }
        return ComboBoxModel;
    }
    
    public boolean groupStateChange(JComboBox jComboBoxGroup, JComboBox jComboBoxDepartment, JComboBox jComboBoxPosition, boolean isAddFirstElement){
        try {
            jComboBoxDepartment.setModel(getValuesDepartmentOnGroupName(jComboBoxGroup, isAddFirstElement));
            jComboBoxDepartment.setSelectedIndex(0);                
            departmentStateChange(jComboBoxDepartment, jComboBoxPosition, isAddFirstElement);               
        } catch (Exception ex) {
            System.out.println("Fraims.JFBaseMedBook.jCBUsGroupItemStateChanged() - проблемы - " + ex.getMessage());
            return false;
        }
        return true;
    }
    public boolean groupStateChange(JComboBox jComboBoxGroup, JComboBox jComboBoxDepartment, JComboBox jComboBoxPosition){
        return groupStateChange(jComboBoxGroup, jComboBoxDepartment, jComboBoxPosition, false);
    }

    public boolean departmentStateChange(JComboBox jComboBoxDepartment , JComboBox jComboBoxPosition, boolean isAddFirstElement){
            try{              
                jComboBoxPosition.setModel(getValuesPositionOnDepartmentName(jComboBoxDepartment, isAddFirstElement));                
                jComboBoxPosition.setSelectedIndex(0);                 
            }catch(Exception ex){
                System.out.println("Fraims.JFBaseMedBook.jCBUsDepartmentActionPerformed() - проблемы -" + ex.getMessage());
            }
        return true;
    }
    public boolean departmentStateChange(JComboBox jComboBoxDepartment , JComboBox jComboBoxPosition){
        return departmentStateChange(jComboBoxDepartment , jComboBoxPosition, false);
    }
    
    public int showMassegeALotOfData(JCheckBox jCheckBox) {
        int res = 1;
        if (jCheckBox.isSelected()) {
            Object[] options = {"Да, показать", "Отмена",};
            int n = JOptionPane.showOptionDialog(null,
                    "Большое кол-во записей в базе может привести к замедлению работы программы. \n"
                    + "Показать все записи в базе?",
                    "Внимание!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, //do not use a custom Icon
                    options, //the titles of buttons
                    options[0]); //default button title
            if (n == 0) {
                jCheckBox.setSelected(true);
                res = 0;
            }
        } else {
            jCheckBox.setSelected(false);
            res = 1;
        }
        return res;
    }
    
    public String enumToString(ArrayList<?> EnumList, String Seporator) {
        String str = "";
        if (EnumList.size()>0){  
            StringBuilder bstr = new StringBuilder("");
            EnumList.forEach((object) -> {
                bstr.append(object).append(Seporator);
            });
            str = bstr.toString().substring(0, bstr.toString().length() - 1);
        }
        return str;
    }
    public String enumToString(ArrayList<?> EnumList) {
        return enumToString(EnumList, ",");
    }
    
    public ArrayList<EnumBorneDiseases> StringToEnumBorneDeseases(String String) {
        ArrayList<EnumBorneDiseases> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split(",");
                for (String subString1 : subString) {
                    arrayList.add(EnumBorneDiseases.valueOf(subString1));
                }
            }
        } 
        return arrayList;
    }
    public ArrayList<EnumHepatitisAvaccine> StringToEnumHepatitisAvaccine(String String) {
        ArrayList<EnumHepatitisAvaccine> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split(",");
                for (String subString1 : subString) {
                    arrayList.add(EnumHepatitisAvaccine.valueOf(subString1));
                }
            }
        } 
        return arrayList;
    }
    
}
