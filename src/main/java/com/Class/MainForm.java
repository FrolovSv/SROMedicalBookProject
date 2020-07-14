/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Class;

import com.Progect.MedicalBookProgectMaven;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author SusPecT
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class MainForm extends javax.swing.JInternalFrame{
    public MedicalBookProgectMaven MBProgect;
    
    public MainForm(MedicalBookProgectMaven MedicalBookProgectMaven){
        this.MBProgect = MedicalBookProgectMaven;        
    }

    public MainForm(){        
    }
}
