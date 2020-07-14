/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.notUse;

import com.Class.MainClassProgect;
import com.Class.UsDepartment;
import com.Class.UsGroup;
import com.Class.UsPosition;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author SusPecT
 */
public interface InternalFrameViewable {
    
    DefaultComboBoxModel getValuesGroup(TreeMap<Integer, UsGroup> GroupTreeMap, String FirstElement);
    DefaultComboBoxModel getValuesDepartmentOnGroupName(TreeMap<Integer, UsDepartment> DepartmentTreeMap, String GroupName, String FirstElement);
    DefaultComboBoxModel getValuesPositionOnDepartmentName(TreeMap<Integer, UsPosition> TreeMap, String DepartmentName, String FirstElement);    
    
    Integer getKeyByTreeMap(TreeMap<Integer, ? extends MainClassProgect> TreeMap, String Name);    
    Integer getKeyByUsDepartmentName(TreeMap<Integer, UsPosition> TreeMap, Integer UsDepartmentId, String UsPositionName);
    
}
