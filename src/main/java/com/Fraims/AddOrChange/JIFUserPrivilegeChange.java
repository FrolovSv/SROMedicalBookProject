package com.Fraims.AddOrChange;

import com.Class.UserPrivilege.PrivilegeSettings;
import com.Class.UserPrivilege.PrivilegeDefault;
import com.Class.UserPrivilege.PrivilegeInstitution;
import static com.Class.UserPrivilege.PrivilegeSettings.*;
import static com.Class.UserPrivilege.PrivilegeDefault.*;
import static com.Class.UserPrivilege.PrivilegeInstitution.*;
import com.Class.UserPrivilege;
import com.Progect.MedicalBookProgectMaven;
import com.SQL.SQLQuery;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JDesktopPane;


public class JIFUserPrivilegeChange extends javax.swing.JInternalFrame {
    private MedicalBookProgectMaven MBProgect;
    private Integer SelectedUserPrivilegeid = -1;
    private UserPrivilege userPrivilege = new UserPrivilege();
    private Boolean IsNewUserPrivilege = false;
    private Integer AuthorizedUserId = -1;
    
    public void setSelectedPrivilegeUser(Integer UserPrivilegeid){
        this.SelectedUserPrivilegeid = UserPrivilegeid;
        userPrivilege = new SQLQuery<>(userPrivilege).Read(UserPrivilegeid);
        FillFieldsPrivilegeUser();
    }
    
    
    public JIFUserPrivilegeChange(MedicalBookProgectMaven medBookProgect) {
        this.MBProgect = medBookProgect;
        this.AuthorizedUserId = MBProgect.getAuthorizedUser().getId();
        this.SelectedUserPrivilegeid = MBProgect.getSelectedUserPrivilegeId();
        initComponents();
        verificationData();   
    }
    
    private void verificationData(){
        Thread MBThread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{                    
                    IsNewUserPrivilege = false;    
                    userPrivilege = new SQLQuery<>(userPrivilege).Read(SelectedUserPrivilegeid);                    
                    if (SelectedUserPrivilegeid <= 0){
                        userPrivilege.setDateAdd(new Date());
                        userPrivilege.setEmployeeAddId(AuthorizedUserId);
                        jBChangeOrAddPrivilegeUser.setText("ДОБАВИТЬ правило");
                        jBChangeOrAddPrivilegeUser.setEnabled(true);
                        IsNewUserPrivilege = true;
                    } else {
                        userPrivilege.setDateChange(new Date());
                        userPrivilege.setEmployeeChangeId(AuthorizedUserId); 
                        jBChangeOrAddPrivilegeUser.setText("Сохранить правило");
                        jBChangeOrAddPrivilegeUser.setEnabled(true);
                        IsNewUserPrivilege = false;
                    }                              
                    
                    FillFieldsPrivilegeUser();
                }catch(Exception ex){
                    System.out.println(".run() ПОТОК - Ошибка загрузки данных "+ex.getMessage());
                }                 
            }
        });
        MBThread.run();
    }  
    
    private void FillFieldsPrivilegeUser(){
        jTFPrivilegeName.setText(userPrivilege.getName());
        // ========================= MedicalBook =============================
        jCBMedicalBookVIEW_ALL.setSelected(userPrivilege.getPrivMedicalBook().contains(VIEW_ALL));
        jCBMedicalBookVIEW_DELET.setSelected(userPrivilege.getPrivMedicalBook().contains(VIEW_DELET));
        jCBMedicalBookVIEW_OWNGROUP.setSelected(userPrivilege.getPrivMedicalBook().contains(VIEW_OWNGROUP));
        jCBMedicalBookVIEW_OWNDEPARTMENT.setSelected(userPrivilege.getPrivMedicalBook().contains(VIEW_OWNDEPARTMENT));
        jCBMedicalBookVIEW_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivMedicalBook().contains(VIEW_EMPLOYEEUNDER));
        jCBMedicalBookVIEW_LINESTAFF.setSelected(userPrivilege.getPrivMedicalBook().contains(VIEW_LINESTAFF));
        jCBMedicalBookCHANGE_DELET.setSelected(userPrivilege.getPrivMedicalBook().contains(CHANGE_DELET));
        jCBMedicalBookCHANGE_ALL.setSelected(userPrivilege.getPrivMedicalBook().contains(CHANGE_ALL));
        jCBMedicalBookCHANGE_OWNGROUP.setSelected(userPrivilege.getPrivMedicalBook().contains(CHANGE_OWNGROUP));
        jCBMedicalBookCHANGE_OWNDEPARTMENT.setSelected(userPrivilege.getPrivMedicalBook().contains(CHANGE_OWNDEPARTMENT));
        jCBMedicalBookCHANGE_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivMedicalBook().contains(CHANGE_EMPLOYEEUNDER));
        jCBMedicalBookCHANGE_LINESTAFF.setSelected(userPrivilege.getPrivMedicalBook().contains(CHANGE_LINESTAFF));
        jCBMedicalBookDELET_ALL.setSelected(userPrivilege.getPrivMedicalBook().contains(DELET_ALL));
        //jCBMedicalBookDELET_DELET.setSelected(userPrivilege.getPrivMedicalBook().contains(DELET_DELET));
        //jCBMedicalBookDELET_OWNGROUP.setSelected(userPrivilege.getPrivMedicalBook().contains(DELET_OWNGROUP));
        //jCBMedicalBookDELET_OWNDEPARTMENT.setSelected(userPrivilege.getPrivMedicalBook().contains(DELET_OWNDEPARTMENT));
        //jCBMedicalBookDELET_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivMedicalBook().contains(DELET_EMPLOYEEUNDER));
        //jCBMedicalBookDELET_LINESTAFF.setSelected(userPrivilege.getPrivMedicalBook().contains(DELET_LINESTAFF));
        jCBMedicalBookSELECT_ALL.setSelected(userPrivilege.getPrivMedicalBook().contains(SELECT_ALL));
        jCBMedicalBookSELECT_DELET.setSelected(userPrivilege.getPrivMedicalBook().contains(SELECT_DELET));
        jCBMedicalBookSELECT_OWNGROUP.setSelected(userPrivilege.getPrivMedicalBook().contains(SELECT_OWNGROUP));
        jCBMedicalBookSELECT_OWNDEPARTMENT.setSelected(userPrivilege.getPrivMedicalBook().contains(SELECT_OWNDEPARTMENT));
        jCBMedicalBookSELECT_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivMedicalBook().contains(SELECT_EMPLOYEEUNDER));
        jCBMedicalBookSELECT_LINESTAFF.setSelected(userPrivilege.getPrivMedicalBook().contains(SELECT_LINESTAFF));
        
        // ========================= Employee =============================
        jCBEmployeeVIEW_ALL.setSelected(userPrivilege.getPrivEmployee().contains(VIEW_ALL));
        jCBEmployeeVIEW_DELET.setSelected(userPrivilege.getPrivEmployee().contains(VIEW_DELET));
        jCBEmployeeVIEW_OWNGROUP.setSelected(userPrivilege.getPrivEmployee().contains(VIEW_OWNGROUP));
        jCBEmployeeVIEW_OWNDEPARTMENT.setSelected(userPrivilege.getPrivEmployee().contains(VIEW_OWNDEPARTMENT));
        jCBEmployeeVIEW_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivEmployee().contains(VIEW_EMPLOYEEUNDER));
        jCBEmployeeVIEW_LINESTAFF.setSelected(userPrivilege.getPrivEmployee().contains(VIEW_LINESTAFF));
        jCBEmployeeCHANGE_DELET.setSelected(userPrivilege.getPrivEmployee().contains(CHANGE_DELET));
        jCBEmployeeCHANGE_ALL.setSelected(userPrivilege.getPrivEmployee().contains(CHANGE_ALL));
        jCBEmployeeCHANGE_OWNGROUP.setSelected(userPrivilege.getPrivEmployee().contains(CHANGE_OWNGROUP));
        jCBEmployeeCHANGE_OWNDEPARTMENT.setSelected(userPrivilege.getPrivEmployee().contains(CHANGE_OWNDEPARTMENT));
        jCBEmployeeCHANGE_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivEmployee().contains(CHANGE_EMPLOYEEUNDER));
        jCBEmployeeCHANGE_LINESTAFF.setSelected(userPrivilege.getPrivEmployee().contains(CHANGE_LINESTAFF));
        jCBEmployeeDELET_ALL.setSelected(userPrivilege.getPrivEmployee().contains(DELET_ALL));
        //jCBEmployeeDELET_DELET.setSelected(userPrivilege.getPrivEmployee().contains(DELET_DELET));
        //jCBEmployeeDELET_OWNGROUP.setSelected(userPrivilege.getPrivEmployee().contains(DELET_OWNGROUP));
        //jCBEmployeeDELET_OWNDEPARTMENT.setSelected(userPrivilege.getPrivEmployee().contains(DELET_OWNDEPARTMENT));
        //jCBEmployeeDELET_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivEmployee().contains(DELET_EMPLOYEEUNDER));
        //jCBEmployeeDELET_LINESTAFF.setSelected(userPrivilege.getPrivEmployee().contains(DELET_LINESTAFF));
        jCBEmployeeSELECT_ALL.setSelected(userPrivilege.getPrivEmployee().contains(SELECT_ALL));
        jCBEmployeeSELECT_DELET.setSelected(userPrivilege.getPrivEmployee().contains(SELECT_DELET));
        jCBEmployeeSELECT_OWNGROUP.setSelected(userPrivilege.getPrivEmployee().contains(SELECT_OWNGROUP));
        jCBEmployeeSELECT_OWNDEPARTMENT.setSelected(userPrivilege.getPrivEmployee().contains(SELECT_OWNDEPARTMENT));
        jCBEmployeeSELECT_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivEmployee().contains(SELECT_EMPLOYEEUNDER));
        jCBEmployeeSELECT_LINESTAFF.setSelected(userPrivilege.getPrivEmployee().contains(SELECT_LINESTAFF));
        
        // ========================= Institution =============================
        jCBInstitutionVIEW_ALL.setSelected(userPrivilege.getPrivInstitution().contains(VIEW));
        jCBInstitutionVIEW_CLOSED.setSelected(userPrivilege.getPrivInstitution().contains(VIEW_CLOSED));
        jCBInstitutionVIEW_OWNGROUP.setSelected(userPrivilege.getPrivInstitution().contains(VIEW_OWNTYPE));
        jCBInstitutionVIEW_INSTITUTIONUNDER.setSelected(userPrivilege.getPrivInstitution().contains(VIEW_INSTITUTIONUNDER));
        jCheckBox167.setSelected(userPrivilege.getPrivInstitution().isEmpty());
        jCBInstitutionCHANGE_CLOSED.setSelected(userPrivilege.getPrivInstitution().contains(CHANGE_CLOSED));
        jCBInstitutionCHANGE_ALL.setSelected(userPrivilege.getPrivInstitution().contains(CHANGE));
        jCBInstitutionCHANGE_OWNGROUP.setSelected(userPrivilege.getPrivInstitution().contains(CHANGE_OWNTYPE));
        jCBInstitutionCHANGE_INSTITUTIONUNDER.setSelected(userPrivilege.getPrivInstitution().contains(CHANGE_INSTITUTIONUNDER));
        jCheckBox168.setSelected(userPrivilege.getPrivInstitution().isEmpty());
        jCBInstitutionDELET_ALL.setSelected(userPrivilege.getPrivInstitution().contains(DELET));
        //jCBInstitutionDELET_CLOSED.setSelected(userPrivilege.getPrivInstitution().contains(DELET_CLOSED));
        //jCBInstitutionDELET_OWNGROUP.setSelected(userPrivilege.getPrivInstitution().contains(DELET_OWNTYPE));
        //jCBInstitutionDELET_INSTITUTIONUNDER.setSelected(userPrivilege.getPrivInstitution().contains(DELET_INSTITUTIONUNDER));
        jCheckBox169.setSelected(userPrivilege.getPrivInstitution().isEmpty());
        //jCBInstitutionSELECT_ALL.setSelected(userPrivilege.getPrivInstitution().contains(SELECT));
        //jCBInstitutionSELECT_CLOSED.setSelected(userPrivilege.getPrivInstitution().contains(SELECT_CLOSED));
        //jCBInstitutionSELECT_OWNGROUP.setSelected(userPrivilege.getPrivInstitution().contains(SELECT_OWNTYPE));
        //jCBInstitutionSELECT_INSTITUTIONUNDER.setSelected(userPrivilege.getPrivInstitution().contains(SELECT_INSTITUTIONUNDER));
        jCheckBox170.setSelected(userPrivilege.getPrivInstitution().isEmpty());
        
        // ========================= User =============================
        jCBUserVIEW_ALL.setSelected(userPrivilege.getPrivUser().contains(VIEW_ALL));
        jCBUserVIEW_DELET.setSelected(userPrivilege.getPrivUser().contains(VIEW_DELET));
        jCBUserVIEW_OWNGROUP.setSelected(userPrivilege.getPrivUser().contains(VIEW_OWNGROUP));
        jCBUserVIEW_OWNDEPARTMENT.setSelected(userPrivilege.getPrivUser().contains(VIEW_OWNDEPARTMENT));
        jCBUserVIEW_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivUser().contains(VIEW_EMPLOYEEUNDER));
        jCBUserCHANGE_DELET.setSelected(userPrivilege.getPrivUser().contains(CHANGE_DELET));
        jCBUserCHANGE_ALL.setSelected(userPrivilege.getPrivUser().contains(CHANGE_ALL));
        jCBUserCHANGE_OWNGROUP.setSelected(userPrivilege.getPrivUser().contains(CHANGE_OWNGROUP));
        jCBUserCHANGE_OWNDEPARTMENT.setSelected(userPrivilege.getPrivUser().contains(CHANGE_OWNDEPARTMENT));
        jCBUserCHANGE_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivUser().contains(CHANGE_EMPLOYEEUNDER));
        jCBUserDELET_ALL.setSelected(userPrivilege.getPrivUser().contains(DELET_ALL));
        //jCBUserDELET_DELET.setSelected(userPrivilege.getPrivUser().contains(DELET_DELET));
        //jCBUserDELET_OWNGROUP.setSelected(userPrivilege.getPrivUser().contains(DELET_OWNGROUP));
        //jCBUserDELET_OWNDEPARTMENT.setSelected(userPrivilege.getPrivUser().contains(DELET_OWNDEPARTMENT));
        //jCBUserDELET_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivUser().contains(DELET_EMPLOYEEUNDER));
        jCBUserSELECT_ALL.setSelected(userPrivilege.getPrivUser().contains(SELECT_ALL));
        jCBUserSELECT_DELET.setSelected(userPrivilege.getPrivUser().contains(SELECT_DELET));
        jCBUserSELECT_OWNGROUP.setSelected(userPrivilege.getPrivUser().contains(SELECT_OWNGROUP));
        jCBUserSELECT_OWNDEPARTMENT.setSelected(userPrivilege.getPrivUser().contains(SELECT_OWNDEPARTMENT));
        jCBUserSELECT_EMPLOYEEUNDER.setSelected(userPrivilege.getPrivUser().contains(SELECT_EMPLOYEEUNDER));
        
        // ========================= Settings =============================
        jCBSettingsGROUP_CHANGE.setSelected(userPrivilege.getPrivSettings().contains(PrivilegeSettings.GROUP_CHANGE));
        jCBSettingsDEPARTMENT_CHANGE.setSelected(userPrivilege.getPrivSettings().contains(PrivilegeSettings.DEPARTMENT_CHANGE));
        jCBSettingsPOSITION_CHANGE.setSelected(userPrivilege.getPrivSettings().contains(PrivilegeSettings.POSITION_CHANGE));
        jCBSettingsPRIVILEGE_CHANGE.setSelected(userPrivilege.getPrivSettings().contains(PrivilegeSettings.PRIVILEGE_CHANGE));
        jCB.setSelected(userPrivilege.getPrivSettings().isEmpty());
        
        // ========================= Mailing =============================
        jCheckBox172.setSelected(userPrivilege.getPrivSettings().contains(PrivilegeSettings.MAILING_CHANGE));
        jCheckBox173.setSelected(userPrivilege.getPrivSettings().isEmpty());
        jCheckBox174.setSelected(userPrivilege.getPrivSettings().isEmpty());
        jCheckBox177.setSelected(userPrivilege.getPrivSettings().isEmpty());
        jCheckBox178.setSelected(userPrivilege.getPrivSettings().isEmpty());
    }
    
    private void setMedicalBookField(Boolean check){
        jCBMedicalBookVIEW_ALL.setSelected(check);
        jCBMedicalBookVIEW_DELET.setSelected(check);
        jCBMedicalBookVIEW_OWNGROUP.setSelected(check);
        jCBMedicalBookVIEW_OWNDEPARTMENT.setSelected(check);
        jCBMedicalBookVIEW_EMPLOYEEUNDER.setSelected(check);
        jCBMedicalBookVIEW_LINESTAFF.setSelected(check);
        jCBMedicalBookCHANGE_DELET.setSelected(check);
        jCBMedicalBookCHANGE_ALL.setSelected(check);
        jCBMedicalBookCHANGE_OWNGROUP.setSelected(check);
        jCBMedicalBookCHANGE_OWNDEPARTMENT.setSelected(check);
        jCBMedicalBookCHANGE_EMPLOYEEUNDER.setSelected(check);
        jCBMedicalBookCHANGE_LINESTAFF.setSelected(check);
        jCBMedicalBookDELET_ALL.setSelected(check);
        jCBMedicalBookDELET_DELET.setSelected(check);
        jCBMedicalBookDELET_OWNGROUP.setSelected(check);
        jCBMedicalBookDELET_OWNDEPARTMENT.setSelected(check);
        jCBMedicalBookDELET_EMPLOYEEUNDER.setSelected(check);
        jCBMedicalBookDELET_LINESTAFF.setSelected(check);
        jCBMedicalBookSELECT_ALL.setSelected(check);
        jCBMedicalBookSELECT_DELET.setSelected(check);
        jCBMedicalBookSELECT_OWNGROUP.setSelected(check);
        jCBMedicalBookSELECT_OWNDEPARTMENT.setSelected(check);
        jCBMedicalBookSELECT_EMPLOYEEUNDER.setSelected(check);
        jCBMedicalBookSELECT_LINESTAFF.setSelected(check);
    }
    private void setEmployeeField(Boolean check){
        jCBEmployeeVIEW_ALL.setSelected(check);
        jCBEmployeeVIEW_DELET.setSelected(check);
        jCBEmployeeVIEW_OWNGROUP.setSelected(check);
        jCBEmployeeVIEW_OWNDEPARTMENT.setSelected(check);
        jCBEmployeeVIEW_EMPLOYEEUNDER.setSelected(check);
        jCBEmployeeVIEW_LINESTAFF.setSelected(check);
        jCBEmployeeCHANGE_DELET.setSelected(check);
        jCBEmployeeCHANGE_ALL.setSelected(check);
        jCBEmployeeCHANGE_OWNGROUP.setSelected(check);
        jCBEmployeeCHANGE_OWNDEPARTMENT.setSelected(check);
        jCBEmployeeCHANGE_EMPLOYEEUNDER.setSelected(check);
        jCBEmployeeCHANGE_LINESTAFF.setSelected(check);
        jCBEmployeeDELET_ALL.setSelected(check);
        jCBEmployeeDELET_DELET.setSelected(check);
        jCBEmployeeDELET_OWNGROUP.setSelected(check);
        jCBEmployeeDELET_OWNDEPARTMENT.setSelected(check);
        jCBEmployeeDELET_EMPLOYEEUNDER.setSelected(check);
        jCBEmployeeDELET_LINESTAFF.setSelected(check);
        jCBEmployeeSELECT_ALL.setSelected(check);
        jCBEmployeeSELECT_DELET.setSelected(check);
        jCBEmployeeSELECT_OWNGROUP.setSelected(check);
        jCBEmployeeSELECT_OWNDEPARTMENT.setSelected(check);
        jCBEmployeeSELECT_EMPLOYEEUNDER.setSelected(check);
        jCBEmployeeSELECT_LINESTAFF.setSelected(check);

    }
    private void setInstitutionField(Boolean check){
        jCBInstitutionVIEW_ALL.setSelected(check);
        jCBInstitutionVIEW_CLOSED.setSelected(check);
        jCBInstitutionVIEW_OWNGROUP.setSelected(check);
        jCBInstitutionVIEW_INSTITUTIONUNDER.setSelected(check);
        jCheckBox167.setSelected(check);
        jCBInstitutionCHANGE_CLOSED.setSelected(check);
        jCBInstitutionCHANGE_ALL.setSelected(check);
        jCBInstitutionCHANGE_OWNGROUP.setSelected(check);
        jCBInstitutionCHANGE_INSTITUTIONUNDER.setSelected(check);
        jCheckBox168.setSelected(check);
        jCBInstitutionDELET_ALL.setSelected(check);
        jCBInstitutionDELET_CLOSED.setSelected(check);
        jCBInstitutionDELET_OWNGROUP.setSelected(check);
        jCBInstitutionDELET_INSTITUTIONUNDER.setSelected(check);
        jCheckBox169.setSelected(check);
        jCBInstitutionSELECT_ALL.setSelected(check);
        jCBInstitutionSELECT_CLOSED.setSelected(check);
        jCBInstitutionSELECT_OWNGROUP.setSelected(check);
        jCBInstitutionSELECT_INSTITUTIONUNDER.setSelected(check);
        jCheckBox170.setSelected(check);
    }
    private void setUserField(Boolean check){
        jCBUserVIEW_ALL.setSelected(check);
        jCBUserVIEW_DELET.setSelected(check);
        jCBUserVIEW_OWNGROUP.setSelected(check);
        jCBUserVIEW_OWNDEPARTMENT.setSelected(check);
        jCBUserVIEW_EMPLOYEEUNDER.setSelected(check);
        jCBUserCHANGE_DELET.setSelected(check);
        jCBUserCHANGE_ALL.setSelected(check);
        jCBUserCHANGE_OWNGROUP.setSelected(check);
        jCBUserCHANGE_OWNDEPARTMENT.setSelected(check);
        jCBUserCHANGE_EMPLOYEEUNDER.setSelected(check);
        jCBUserDELET_ALL.setSelected(check);
        jCBUserDELET_DELET.setSelected(check);
        jCBUserDELET_OWNGROUP.setSelected(check);
        jCBUserDELET_OWNDEPARTMENT.setSelected(check);
        jCBUserDELET_EMPLOYEEUNDER.setSelected(check);
        jCBUserSELECT_ALL.setSelected(check);
        jCBUserSELECT_DELET.setSelected(check);
        jCBUserSELECT_OWNGROUP.setSelected(check);
        jCBUserSELECT_OWNDEPARTMENT.setSelected(check);
        jCBUserSELECT_EMPLOYEEUNDER.setSelected(check);
    }
    private void setSettingsField(Boolean check){
        jCBSettingsGROUP_CHANGE.setSelected(check);
        jCBSettingsDEPARTMENT_CHANGE.setSelected(check);
        jCBSettingsPOSITION_CHANGE.setSelected(check);
        jCBSettingsPRIVILEGE_CHANGE.setSelected(check);
        jCB.setSelected(check);
    }
    private void setMailingField(Boolean check){
        jCheckBox172.setSelected(check);
        jCheckBox173.setSelected(check);
        jCheckBox174.setSelected(check);
        jCheckBox177.setSelected(check);
        jCheckBox178.setSelected(check);
    }
    
    private UserPrivilege getFildsPrivilege(){
        UserPrivilege userPrivilegeLocal = new UserPrivilege();
        userPrivilegeLocal = userPrivilege;
        ArrayList<PrivilegeDefault> PrivMedicalBooklocal = new ArrayList<>();
        ArrayList<PrivilegeDefault> PrivEmployeelocal= new ArrayList<>();
        ArrayList<PrivilegeInstitution> PrivInstitutionlocal = new ArrayList<>();
        ArrayList<PrivilegeSettings> PrivSettingslocal = new ArrayList<>();
        ArrayList<PrivilegeDefault> PrivUserlocal = new ArrayList<>();
        
        userPrivilegeLocal.setName(jTFPrivilegeName.getText());
        if (jCBMedicalBookVIEW_ALL.isSelected()) PrivMedicalBooklocal.add(VIEW_ALL);
        if (jCBMedicalBookVIEW_DELET.isSelected()) PrivMedicalBooklocal.add(VIEW_DELET);
        if (jCBMedicalBookVIEW_OWNGROUP.isSelected()) PrivMedicalBooklocal.add(VIEW_OWNGROUP);
        if (jCBMedicalBookVIEW_OWNDEPARTMENT.isSelected()) PrivMedicalBooklocal.add(VIEW_OWNDEPARTMENT);
        if (jCBMedicalBookVIEW_EMPLOYEEUNDER.isSelected()) PrivMedicalBooklocal.add(VIEW_EMPLOYEEUNDER);
        if (jCBMedicalBookVIEW_LINESTAFF.isSelected()) PrivMedicalBooklocal.add(VIEW_LINESTAFF);
        if (jCBMedicalBookCHANGE_DELET.isSelected()) PrivMedicalBooklocal.add(CHANGE_DELET);
        if (jCBMedicalBookCHANGE_ALL.isSelected()) PrivMedicalBooklocal.add(CHANGE_ALL);
        if (jCBMedicalBookCHANGE_OWNGROUP.isSelected()) PrivMedicalBooklocal.add(CHANGE_OWNGROUP);
        if (jCBMedicalBookCHANGE_OWNDEPARTMENT.isSelected()) PrivMedicalBooklocal.add(CHANGE_OWNDEPARTMENT);
        if (jCBMedicalBookCHANGE_EMPLOYEEUNDER.isSelected()) PrivMedicalBooklocal.add(CHANGE_EMPLOYEEUNDER);
        if (jCBMedicalBookCHANGE_LINESTAFF.isSelected()) PrivMedicalBooklocal.add(CHANGE_LINESTAFF);
        if (jCBMedicalBookDELET_ALL.isSelected()) PrivMedicalBooklocal.add(DELET_ALL);
        //if (jCBMedicalBookDELET_DELET.isSelected()) PrivMedicalBooklocal.add(DELET_DELET);
        //if (jCBMedicalBookDELET_OWNGROUP.isSelected()) PrivMedicalBooklocal.add(DELET_OWNGROUP);
        //if (jCBMedicalBookDELET_OWNDEPARTMENT.isSelected()) PrivMedicalBooklocal.add(DELET_OWNDEPARTMENT);
        //if (jCBMedicalBookDELET_EMPLOYEEUNDER.isSelected()) PrivMedicalBooklocal.add(DELET_EMPLOYEEUNDER);
        //if (jCBMedicalBookDELET_LINESTAFF.isSelected()) PrivMedicalBooklocal.add(DELET_LINESTAFF);
        if (jCBMedicalBookSELECT_ALL.isSelected()) PrivMedicalBooklocal.add(SELECT_ALL);
        if (jCBMedicalBookSELECT_DELET.isSelected()) PrivMedicalBooklocal.add(SELECT_DELET);
        if (jCBMedicalBookSELECT_OWNGROUP.isSelected()) PrivMedicalBooklocal.add(SELECT_OWNGROUP);
        if (jCBMedicalBookSELECT_OWNDEPARTMENT.isSelected()) PrivMedicalBooklocal.add(SELECT_OWNDEPARTMENT);
        if (jCBMedicalBookSELECT_EMPLOYEEUNDER.isSelected()) PrivMedicalBooklocal.add(SELECT_EMPLOYEEUNDER);
        if (jCBMedicalBookSELECT_LINESTAFF.isSelected()) PrivMedicalBooklocal.add(SELECT_LINESTAFF);

        //=========================Employee=============================
        if (jCBEmployeeVIEW_ALL.isSelected()) PrivEmployeelocal.add(VIEW_ALL);
        if (jCBEmployeeVIEW_DELET.isSelected()) PrivEmployeelocal.add(VIEW_DELET);
        if (jCBEmployeeVIEW_OWNGROUP.isSelected()) PrivEmployeelocal.add(VIEW_OWNGROUP);
        if (jCBEmployeeVIEW_OWNDEPARTMENT.isSelected()) PrivEmployeelocal.add(VIEW_OWNDEPARTMENT);
        if (jCBEmployeeVIEW_EMPLOYEEUNDER.isSelected()) PrivEmployeelocal.add(VIEW_EMPLOYEEUNDER);
        if (jCBEmployeeVIEW_LINESTAFF.isSelected()) PrivEmployeelocal.add(VIEW_LINESTAFF);
        if (jCBEmployeeCHANGE_DELET.isSelected()) PrivEmployeelocal.add(CHANGE_DELET);
        if (jCBEmployeeCHANGE_ALL.isSelected()) PrivEmployeelocal.add(CHANGE_ALL);
        if (jCBEmployeeCHANGE_OWNGROUP.isSelected()) PrivEmployeelocal.add(CHANGE_OWNGROUP);
        if (jCBEmployeeCHANGE_OWNDEPARTMENT.isSelected()) PrivEmployeelocal.add(CHANGE_OWNDEPARTMENT);
        if (jCBEmployeeCHANGE_EMPLOYEEUNDER.isSelected()) PrivEmployeelocal.add(CHANGE_EMPLOYEEUNDER);
        if (jCBEmployeeCHANGE_LINESTAFF.isSelected()) PrivEmployeelocal.add(CHANGE_LINESTAFF);
        if (jCBEmployeeDELET_ALL.isSelected()) PrivEmployeelocal.add(DELET_ALL);
        //if (jCBEmployeeDELET_DELET.isSelected()) PrivEmployeelocal.add(DELET_DELET);
        //if (jCBEmployeeDELET_OWNGROUP.isSelected()) PrivEmployeelocal.add(DELET_OWNGROUP);
        //if (jCBEmployeeDELET_OWNDEPARTMENT.isSelected()) PrivEmployeelocal.add(DELET_OWNDEPARTMENT);
        //if (jCBEmployeeDELET_EMPLOYEEUNDER.isSelected()) PrivEmployeelocal.add(DELET_EMPLOYEEUNDER);
        //if (jCBEmployeeDELET_LINESTAFF.isSelected()) PrivEmployeelocal.add(DELET_LINESTAFF);
        if (jCBEmployeeSELECT_ALL.isSelected()) PrivEmployeelocal.add(SELECT_ALL);
        if (jCBEmployeeSELECT_DELET.isSelected()) PrivEmployeelocal.add(SELECT_DELET);
        if (jCBEmployeeSELECT_OWNGROUP.isSelected()) PrivEmployeelocal.add(SELECT_OWNGROUP);
        if (jCBEmployeeSELECT_OWNDEPARTMENT.isSelected()) PrivEmployeelocal.add(SELECT_OWNDEPARTMENT);
        if (jCBEmployeeSELECT_EMPLOYEEUNDER.isSelected()) PrivEmployeelocal.add(SELECT_EMPLOYEEUNDER);
        if (jCBEmployeeSELECT_LINESTAFF.isSelected()) PrivEmployeelocal.add(SELECT_LINESTAFF);
        
        //=========================Institution=============================
        if (jCBInstitutionVIEW_ALL.isSelected()) PrivInstitutionlocal.add(VIEW);
        if (jCBInstitutionVIEW_CLOSED.isSelected()) PrivInstitutionlocal.add(VIEW_CLOSED);
        if (jCBInstitutionVIEW_OWNGROUP.isSelected()) PrivInstitutionlocal.add(VIEW_OWNTYPE);
        if (jCBInstitutionVIEW_INSTITUTIONUNDER.isSelected()) PrivInstitutionlocal.add(VIEW_INSTITUTIONUNDER);
        jCheckBox167.isSelected();
        if (jCBInstitutionCHANGE_CLOSED.isSelected()) PrivInstitutionlocal.add(CHANGE_CLOSED);
        if (jCBInstitutionCHANGE_ALL.isSelected()) PrivInstitutionlocal.add(CHANGE);
        if (jCBInstitutionCHANGE_OWNGROUP.isSelected()) PrivInstitutionlocal.add(CHANGE_OWNTYPE);
        if (jCBInstitutionCHANGE_INSTITUTIONUNDER.isSelected()) PrivInstitutionlocal.add(CHANGE_INSTITUTIONUNDER);
        jCheckBox168.isSelected();
        if (jCBInstitutionDELET_ALL.isSelected()) PrivInstitutionlocal.add(DELET);
        //if (jCBInstitutionDELET_CLOSED.isSelected()) PrivInstitutionlocal.add(DELET_CLOSED);
        //if (jCBInstitutionDELET_OWNGROUP.isSelected()) PrivInstitutionlocal.add(DELET_OWNTYPE);
        //if (jCBInstitutionDELET_INSTITUTIONUNDER.isSelected()) PrivInstitutionlocal.add(DELET_INSTITUTIONUNDER);
        jCheckBox169.isSelected();
        //if (jCBInstitutionSELECT_ALL.isSelected()) PrivInstitutionlocal.add(SELECT);
        //if (jCBInstitutionSELECT_CLOSED.isSelected()) PrivInstitutionlocal.add(SELECT_CLOSED);
        //if (jCBInstitutionSELECT_OWNGROUP.isSelected()) PrivInstitutionlocal.add(SELECT_OWNTYPE);
        //if (jCBInstitutionSELECT_INSTITUTIONUNDER.isSelected()) PrivInstitutionlocal.add(SELECT_INSTITUTIONUNDER);
        jCheckBox170.isSelected();
        
        //=========================User=============================
        if (jCBUserVIEW_ALL.isSelected()) PrivUserlocal.add(VIEW_ALL);
        if (jCBUserVIEW_DELET.isSelected()) PrivUserlocal.add(VIEW_DELET);
        if (jCBUserVIEW_OWNGROUP.isSelected()) PrivUserlocal.add(VIEW_OWNGROUP);
        if (jCBUserVIEW_OWNDEPARTMENT.isSelected()) PrivUserlocal.add(VIEW_OWNDEPARTMENT);
        if (jCBUserVIEW_EMPLOYEEUNDER.isSelected()) PrivUserlocal.add(VIEW_EMPLOYEEUNDER);
        if (jCBUserCHANGE_DELET.isSelected()) PrivUserlocal.add(CHANGE_DELET);
        if (jCBUserCHANGE_ALL.isSelected()) PrivUserlocal.add(CHANGE_ALL);
        if (jCBUserCHANGE_OWNGROUP.isSelected()) PrivUserlocal.add(CHANGE_OWNGROUP);
        if (jCBUserCHANGE_OWNDEPARTMENT.isSelected()) PrivUserlocal.add(CHANGE_OWNDEPARTMENT);
        if (jCBUserCHANGE_EMPLOYEEUNDER.isSelected()) PrivUserlocal.add(CHANGE_EMPLOYEEUNDER);
        if (jCBUserDELET_ALL.isSelected()) PrivUserlocal.add(DELET_ALL);
        //if (jCBUserDELET_DELET.isSelected()) PrivUserlocal.add(DELET_DELET);
        //if (jCBUserDELET_OWNGROUP.isSelected()) PrivUserlocal.add(DELET_OWNGROUP);
        //if (jCBUserDELET_OWNDEPARTMENT.isSelected()) PrivUserlocal.add(DELET_OWNDEPARTMENT);
        //if (jCBUserDELET_EMPLOYEEUNDER.isSelected()) PrivUserlocal.add(DELET_EMPLOYEEUNDER);
        if (jCBUserSELECT_ALL.isSelected()) PrivUserlocal.add(SELECT_ALL);
        if (jCBUserSELECT_DELET.isSelected()) PrivUserlocal.add(SELECT_DELET);
        if (jCBUserSELECT_OWNGROUP.isSelected()) PrivUserlocal.add(SELECT_OWNGROUP);
        if (jCBUserSELECT_OWNDEPARTMENT.isSelected()) PrivUserlocal.add(SELECT_OWNDEPARTMENT);
        if (jCBUserSELECT_EMPLOYEEUNDER.isSelected()) PrivUserlocal.add(SELECT_EMPLOYEEUNDER);
        
        //=========================Settings=============================
        if (jCBSettingsGROUP_CHANGE.isSelected()) PrivSettingslocal.add(GROUP_CHANGE);
        if (jCBSettingsDEPARTMENT_CHANGE.isSelected()) PrivSettingslocal.add(DEPARTMENT_CHANGE);
        if (jCBSettingsPOSITION_CHANGE.isSelected()) PrivSettingslocal.add(POSITION_CHANGE);
        if (jCBSettingsPRIVILEGE_CHANGE.isSelected()) PrivSettingslocal.add(PRIVILEGE_CHANGE);
        jCB.isSelected();
        
        //=========================Mailing=============================
        if (jCheckBox172.isSelected()) PrivSettingslocal.add(MAILING_CHANGE);
        jCheckBox173.isSelected();
        jCheckBox174.isSelected();
        jCheckBox177.isSelected();
        jCheckBox178.isSelected();

        userPrivilegeLocal.setPrivEmployee(PrivEmployeelocal);
        userPrivilegeLocal.setPrivInstitution(PrivInstitutionlocal);
        userPrivilegeLocal.setPrivMedicalBook(PrivMedicalBooklocal);
        userPrivilegeLocal.setPrivSettings(PrivSettingslocal);
        userPrivilegeLocal.setPrivUser(PrivUserlocal);
        return userPrivilegeLocal;
    }
    
    
    private void SaveOrAdd(){
        UserPrivilege newUserPrivilege = new UserPrivilege();
        
        try{            
            newUserPrivilege = getFildsPrivilege(); 
            new SQLQuery<>(newUserPrivilege).SaveOrWrite(newUserPrivilege.getId());                  
             
            if (MBProgect.getJIFUserPrivilegeBase()!=null) MBProgect.getJIFUserPrivilegeBase().FillDataToTable();
            closeThis();
            MBProgect.setSelectedUserPrivilegeId(-1);
            MBProgect.openUserPrivilege(MedicalBookProgectMaven.statusFrame.VIEW,null);
        }catch(Exception ex){
            System.out.println("Fraims.JIFAddorChangeEmployee.SaveOrAdd() " + ex.getMessage());
        }    
    }

    private void closeThis(){
        this.setVisible(false);        
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);      
        MBProgect.setSelectedUserPrivilegeId(-1); 
        MBProgect.setJIFUserPrivilegeChange(null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jCheckBox12 = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox13 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jCheckBox17 = new javax.swing.JCheckBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jCheckBox36 = new javax.swing.JCheckBox();
        jCheckBox37 = new javax.swing.JCheckBox();
        jCheckBox38 = new javax.swing.JCheckBox();
        jCheckBox39 = new javax.swing.JCheckBox();
        jCheckBox40 = new javax.swing.JCheckBox();
        jCheckBox41 = new javax.swing.JCheckBox();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jCheckBox42 = new javax.swing.JCheckBox();
        jLabel37 = new javax.swing.JLabel();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        jCheckBox26 = new javax.swing.JCheckBox();
        jCheckBox27 = new javax.swing.JCheckBox();
        jCheckBox28 = new javax.swing.JCheckBox();
        jCheckBox29 = new javax.swing.JCheckBox();
        jCheckBox30 = new javax.swing.JCheckBox();
        jCheckBox31 = new javax.swing.JCheckBox();
        jCheckBox32 = new javax.swing.JCheckBox();
        jCheckBox33 = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jCheckBox43 = new javax.swing.JCheckBox();
        jCheckBox44 = new javax.swing.JCheckBox();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jCheckBox34 = new javax.swing.JCheckBox();
        jCheckBox35 = new javax.swing.JCheckBox();
        jCheckBox95 = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jCheckBox45 = new javax.swing.JCheckBox();
        jLabel38 = new javax.swing.JLabel();
        jCheckBox46 = new javax.swing.JCheckBox();
        jLabel39 = new javax.swing.JLabel();
        jCheckBox47 = new javax.swing.JCheckBox();
        jCheckBox48 = new javax.swing.JCheckBox();
        jCheckBox49 = new javax.swing.JCheckBox();
        jCheckBox50 = new javax.swing.JCheckBox();
        jCheckBox51 = new javax.swing.JCheckBox();
        jCheckBox52 = new javax.swing.JCheckBox();
        jCheckBox53 = new javax.swing.JCheckBox();
        jCheckBox54 = new javax.swing.JCheckBox();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jCheckBox55 = new javax.swing.JCheckBox();
        jCheckBox56 = new javax.swing.JCheckBox();
        jCheckBox57 = new javax.swing.JCheckBox();
        jCheckBox58 = new javax.swing.JCheckBox();
        jCheckBox59 = new javax.swing.JCheckBox();
        jCheckBox60 = new javax.swing.JCheckBox();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jCheckBox61 = new javax.swing.JCheckBox();
        jLabel54 = new javax.swing.JLabel();
        jCheckBox63 = new javax.swing.JCheckBox();
        jCheckBox64 = new javax.swing.JCheckBox();
        jCheckBox65 = new javax.swing.JCheckBox();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jCheckBox66 = new javax.swing.JCheckBox();
        jCheckBox67 = new javax.swing.JCheckBox();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jCheckBox90 = new javax.swing.JCheckBox();
        jCheckBox91 = new javax.swing.JCheckBox();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jCheckBox92 = new javax.swing.JCheckBox();
        jCheckBox93 = new javax.swing.JCheckBox();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jCheckBox94 = new javax.swing.JCheckBox();
        jLabel88 = new javax.swing.JLabel();
        jCheckBox96 = new javax.swing.JCheckBox();
        jCheckBox97 = new javax.swing.JCheckBox();
        jCheckBox98 = new javax.swing.JCheckBox();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jCheckBox68 = new javax.swing.JCheckBox();
        jLabel60 = new javax.swing.JLabel();
        jCheckBox69 = new javax.swing.JCheckBox();
        jLabel61 = new javax.swing.JLabel();
        jCheckBox70 = new javax.swing.JCheckBox();
        jCheckBox71 = new javax.swing.JCheckBox();
        jCheckBox72 = new javax.swing.JCheckBox();
        jCheckBox73 = new javax.swing.JCheckBox();
        jCheckBox74 = new javax.swing.JCheckBox();
        jCheckBox75 = new javax.swing.JCheckBox();
        jCheckBox76 = new javax.swing.JCheckBox();
        jCheckBox77 = new javax.swing.JCheckBox();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jCheckBox78 = new javax.swing.JCheckBox();
        jCheckBox79 = new javax.swing.JCheckBox();
        jCheckBox80 = new javax.swing.JCheckBox();
        jCheckBox81 = new javax.swing.JCheckBox();
        jCheckBox82 = new javax.swing.JCheckBox();
        jCheckBox83 = new javax.swing.JCheckBox();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jCheckBox84 = new javax.swing.JCheckBox();
        jLabel76 = new javax.swing.JLabel();
        jCheckBox85 = new javax.swing.JCheckBox();
        jCheckBox86 = new javax.swing.JCheckBox();
        jCheckBox87 = new javax.swing.JCheckBox();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jCheckBox88 = new javax.swing.JCheckBox();
        jCheckBox89 = new javax.swing.JCheckBox();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jCheckBox22 = new javax.swing.JCheckBox();
        jCheckBox23 = new javax.swing.JCheckBox();
        jLabel100 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        jLabel95 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jCBMedicalBookVIEW_ALL = new javax.swing.JCheckBox();
        jCBMedicalBookVIEW_DELET = new javax.swing.JCheckBox();
        jCBMedicalBookVIEW_OWNGROUP = new javax.swing.JCheckBox();
        jCBMedicalBookVIEW_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBMedicalBookVIEW_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBMedicalBookVIEW_LINESTAFF = new javax.swing.JCheckBox();
        jCBMedicalBookCHANGE_DELET = new javax.swing.JCheckBox();
        jCBMedicalBookCHANGE_ALL = new javax.swing.JCheckBox();
        jCBMedicalBookCHANGE_OWNGROUP = new javax.swing.JCheckBox();
        jCBMedicalBookCHANGE_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBMedicalBookCHANGE_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBMedicalBookCHANGE_LINESTAFF = new javax.swing.JCheckBox();
        jCBMedicalBookDELET_ALL = new javax.swing.JCheckBox();
        jCBMedicalBookDELET_DELET = new javax.swing.JCheckBox();
        jCBMedicalBookDELET_OWNGROUP = new javax.swing.JCheckBox();
        jCBMedicalBookDELET_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBMedicalBookDELET_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBMedicalBookDELET_LINESTAFF = new javax.swing.JCheckBox();
        jCBMedicalBookSELECT_ALL = new javax.swing.JCheckBox();
        jCBMedicalBookSELECT_DELET = new javax.swing.JCheckBox();
        jCBMedicalBookSELECT_OWNGROUP = new javax.swing.JCheckBox();
        jCBMedicalBookSELECT_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBMedicalBookSELECT_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBMedicalBookSELECT_LINESTAFF = new javax.swing.JCheckBox();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jCheckBox123 = new javax.swing.JCheckBox();
        jCheckBox124 = new javax.swing.JCheckBox();
        jLabel134 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jCBEmployeeVIEW_ALL = new javax.swing.JCheckBox();
        jCBEmployeeVIEW_DELET = new javax.swing.JCheckBox();
        jCBEmployeeVIEW_OWNGROUP = new javax.swing.JCheckBox();
        jCBEmployeeVIEW_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBEmployeeVIEW_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBEmployeeVIEW_LINESTAFF = new javax.swing.JCheckBox();
        jCBEmployeeCHANGE_DELET = new javax.swing.JCheckBox();
        jCBEmployeeCHANGE_ALL = new javax.swing.JCheckBox();
        jCBEmployeeCHANGE_OWNGROUP = new javax.swing.JCheckBox();
        jCBEmployeeCHANGE_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBEmployeeCHANGE_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBEmployeeCHANGE_LINESTAFF = new javax.swing.JCheckBox();
        jCBEmployeeDELET_ALL = new javax.swing.JCheckBox();
        jCBEmployeeDELET_DELET = new javax.swing.JCheckBox();
        jCBEmployeeDELET_OWNGROUP = new javax.swing.JCheckBox();
        jCBEmployeeDELET_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBEmployeeDELET_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBEmployeeDELET_LINESTAFF = new javax.swing.JCheckBox();
        jCBEmployeeSELECT_ALL = new javax.swing.JCheckBox();
        jCBEmployeeSELECT_DELET = new javax.swing.JCheckBox();
        jCBEmployeeSELECT_OWNGROUP = new javax.swing.JCheckBox();
        jCBEmployeeSELECT_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBEmployeeSELECT_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBEmployeeSELECT_LINESTAFF = new javax.swing.JCheckBox();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jCheckBox149 = new javax.swing.JCheckBox();
        jCheckBox150 = new javax.swing.JCheckBox();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jSeparator29 = new javax.swing.JSeparator();
        jPanel14 = new javax.swing.JPanel();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel115 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jCBInstitutionVIEW_ALL = new javax.swing.JCheckBox();
        jCBInstitutionVIEW_CLOSED = new javax.swing.JCheckBox();
        jCBInstitutionVIEW_OWNGROUP = new javax.swing.JCheckBox();
        jCBInstitutionVIEW_INSTITUTIONUNDER = new javax.swing.JCheckBox();
        jCheckBox167 = new javax.swing.JCheckBox();
        jCBInstitutionCHANGE_CLOSED = new javax.swing.JCheckBox();
        jCBInstitutionCHANGE_ALL = new javax.swing.JCheckBox();
        jCBInstitutionCHANGE_OWNGROUP = new javax.swing.JCheckBox();
        jCBInstitutionCHANGE_INSTITUTIONUNDER = new javax.swing.JCheckBox();
        jCheckBox168 = new javax.swing.JCheckBox();
        jCBInstitutionDELET_ALL = new javax.swing.JCheckBox();
        jCBInstitutionDELET_CLOSED = new javax.swing.JCheckBox();
        jCBInstitutionDELET_OWNGROUP = new javax.swing.JCheckBox();
        jCBInstitutionDELET_INSTITUTIONUNDER = new javax.swing.JCheckBox();
        jCheckBox169 = new javax.swing.JCheckBox();
        jCBInstitutionSELECT_ALL = new javax.swing.JCheckBox();
        jCBInstitutionSELECT_CLOSED = new javax.swing.JCheckBox();
        jCBInstitutionSELECT_OWNGROUP = new javax.swing.JCheckBox();
        jCBInstitutionSELECT_INSTITUTIONUNDER = new javax.swing.JCheckBox();
        jCheckBox170 = new javax.swing.JCheckBox();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jCheckBox175 = new javax.swing.JCheckBox();
        jCheckBox176 = new javax.swing.JCheckBox();
        jLabel123 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel125 = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jSeparator23 = new javax.swing.JSeparator();
        jCBUserVIEW_ALL = new javax.swing.JCheckBox();
        jCBUserVIEW_DELET = new javax.swing.JCheckBox();
        jCBUserVIEW_OWNGROUP = new javax.swing.JCheckBox();
        jCBUserVIEW_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBUserVIEW_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBUserCHANGE_DELET = new javax.swing.JCheckBox();
        jCBUserCHANGE_ALL = new javax.swing.JCheckBox();
        jCBUserCHANGE_OWNGROUP = new javax.swing.JCheckBox();
        jCBUserCHANGE_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBUserCHANGE_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBUserDELET_ALL = new javax.swing.JCheckBox();
        jCBUserDELET_DELET = new javax.swing.JCheckBox();
        jCBUserDELET_OWNGROUP = new javax.swing.JCheckBox();
        jCBUserDELET_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBUserDELET_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jCBUserSELECT_ALL = new javax.swing.JCheckBox();
        jCBUserSELECT_DELET = new javax.swing.JCheckBox();
        jCBUserSELECT_OWNGROUP = new javax.swing.JCheckBox();
        jCBUserSELECT_OWNDEPARTMENT = new javax.swing.JCheckBox();
        jCBUserSELECT_EMPLOYEEUNDER = new javax.swing.JCheckBox();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        jSeparator26 = new javax.swing.JSeparator();
        jCheckBox179 = new javax.swing.JCheckBox();
        jCheckBox180 = new javax.swing.JCheckBox();
        jSeparator27 = new javax.swing.JSeparator();
        jLabel133 = new javax.swing.JLabel();
        jSeparator28 = new javax.swing.JSeparator();
        jPanel15 = new javax.swing.JPanel();
        jCheckBox62 = new javax.swing.JCheckBox();
        jCheckBox171 = new javax.swing.JCheckBox();
        jSeparator22 = new javax.swing.JSeparator();
        jCheckBox172 = new javax.swing.JCheckBox();
        jCheckBox173 = new javax.swing.JCheckBox();
        jCheckBox174 = new javax.swing.JCheckBox();
        jCheckBox177 = new javax.swing.JCheckBox();
        jCheckBox178 = new javax.swing.JCheckBox();
        jLabel30 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jCBSettingsGROUP_CHANGE = new javax.swing.JCheckBox();
        jCBSettingsDEPARTMENT_CHANGE = new javax.swing.JCheckBox();
        jCBSettingsPOSITION_CHANGE = new javax.swing.JCheckBox();
        jCBSettingsPRIVILEGE_CHANGE = new javax.swing.JCheckBox();
        jCB = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jBChangeOrAddPrivilegeUser = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jTFPrivilegeName = new javax.swing.JTextField();
        jCheckBox99 = new javax.swing.JCheckBox();
        jCheckBox181 = new javax.swing.JCheckBox();

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox12.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox12.setText("Просмотр ЛМК");

        jLabel7.setText("подробнее");

        jCheckBox13.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox13.setText("Просмотр ЛМК");
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });

        jLabel8.setText("подробнее");

        jCheckBox14.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox14.setText("Просмотр ЛМК подчененных сотрудников");
        jCheckBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox14ActionPerformed(evt);
            }
        });

        jCheckBox15.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox15.setText("Просмотр своей ЛМК");
        jCheckBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox15ActionPerformed(evt);
            }
        });

        jCheckBox16.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox16.setText("Редактирование ЛМК");

        jCheckBox17.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox17.setText("Редактирование ЛМК");
        jCheckBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox17ActionPerformed(evt);
            }
        });

        jCheckBox18.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox18.setText("Редактирование ЛМК своей группы");
        jCheckBox18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox18ActionPerformed(evt);
            }
        });

        jCheckBox19.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox19.setText("Редактирование ЛМК своего подразделения");
        jCheckBox19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox19ActionPerformed(evt);
            }
        });

        jCheckBox20.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox20.setText("Добавление ЛМК");

        jCheckBox21.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox21.setText("Добавление ЛМК");
        jCheckBox21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox21ActionPerformed(evt);
            }
        });

        jLabel9.setText("подробнее");

        jLabel10.setText("подробнее");

        jLabel11.setText("подробнее");

        jLabel12.setText("подробнее");

        jLabel15.setText("подробнее");

        jLabel16.setText("подробнее");

        jLabel17.setText("подробнее");

        jLabel18.setText("подробнее");

        jCheckBox36.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox36.setText("Выбор ЛМК");
        jCheckBox36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox36ActionPerformed(evt);
            }
        });

        jCheckBox37.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox37.setText("Выбор ЛМК");
        jCheckBox37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox37ActionPerformed(evt);
            }
        });

        jCheckBox38.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox38.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox38.setText("Выбор удаленных ЛМК");
        jCheckBox38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox38ActionPerformed(evt);
            }
        });

        jCheckBox39.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox39.setText("Выбор ЛМК своей группы");
        jCheckBox39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox39ActionPerformed(evt);
            }
        });

        jCheckBox40.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox40.setText("Просмотр удаленных ЛМК");
        jCheckBox40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox40ActionPerformed(evt);
            }
        });

        jCheckBox41.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox41.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox41.setText("Редактирование удаленных ЛМК");
        jCheckBox41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox41ActionPerformed(evt);
            }
        });

        jLabel31.setText("подробнее");

        jLabel32.setText("подробнее");

        jLabel33.setText("подробнее");

        jLabel34.setText("подробнее");

        jLabel35.setText("подробнее");

        jLabel36.setText("подробнее");

        jCheckBox42.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox42.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox42.setText("Выбор ЛМК своего подразделения");
        jCheckBox42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox42ActionPerformed(evt);
            }
        });

        jLabel37.setText("подробнее");

        jCheckBox24.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox24.setText("Просмотр ЛМК своей группы");
        jCheckBox24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox24ActionPerformed(evt);
            }
        });

        jCheckBox25.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox25.setText("Просмотр ЛМК своего подразделения");
        jCheckBox25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox25ActionPerformed(evt);
            }
        });

        jCheckBox26.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox26.setText("Просмотр ЛМК линейного персонала");
        jCheckBox26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox26ActionPerformed(evt);
            }
        });

        jCheckBox27.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox27.setText("Редактирование ЛМК подчененных сотрудников");
        jCheckBox27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox27ActionPerformed(evt);
            }
        });

        jCheckBox28.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox28.setText("Редактирование ЛМК линейного персонала");
        jCheckBox28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox28ActionPerformed(evt);
            }
        });

        jCheckBox29.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox29.setText("Редактирование своей ЛМК");
        jCheckBox29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox29ActionPerformed(evt);
            }
        });

        jCheckBox30.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox30.setText("Добавление ЛМК своей группы");
        jCheckBox30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox30ActionPerformed(evt);
            }
        });

        jCheckBox31.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox31.setText("Добавление ЛМК своего подразделения");
        jCheckBox31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox31ActionPerformed(evt);
            }
        });

        jCheckBox32.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox32.setText("Добавление ЛМК подчененых сотрудников");
        jCheckBox32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox32ActionPerformed(evt);
            }
        });

        jCheckBox33.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox33.setText("Добавление ЛМК линейного персонала");
        jCheckBox33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox33ActionPerformed(evt);
            }
        });

        jLabel13.setText("подробнее");

        jLabel14.setText("подробнее");

        jLabel19.setText("подробнее");

        jLabel20.setText("подробнее");

        jLabel21.setText("подробнее");

        jLabel22.setText("подробнее");

        jLabel23.setText("подробнее");

        jLabel24.setText("подробнее");

        jLabel25.setText("подробнее");

        jLabel26.setText("подробнее");

        jCheckBox43.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox43.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox43.setText("Выбор ЛМК подчененных сотрудников");
        jCheckBox43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox43ActionPerformed(evt);
            }
        });

        jCheckBox44.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox44.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox44.setText("Выбор ЛМК линейных сотрудников");
        jCheckBox44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox44ActionPerformed(evt);
            }
        });

        jLabel82.setText("подробнее");

        jLabel83.setText("подробнее");

        jCheckBox34.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox34.setText("Удаление и востановление ЛМК");
        jCheckBox34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox34ActionPerformed(evt);
            }
        });

        jCheckBox35.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox35.setText("Удаление ЛМК");
        jCheckBox35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox35ActionPerformed(evt);
            }
        });

        jCheckBox95.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox95.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox95.setText("Востановление ЛМК");
        jCheckBox95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox95ActionPerformed(evt);
            }
        });

        jLabel27.setText("подробнее");

        jLabel28.setText("подробнее");

        jLabel29.setText("подробнее");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox34, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox27, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                            .addComponent(jCheckBox28, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                            .addComponent(jCheckBox29, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                            .addComponent(jCheckBox30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox95, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(77, 77, 77)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox13)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox40, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox24)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox25)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox14)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox26)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox15)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox41)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox18)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox19, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox27, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox28, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox29, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox34, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox35, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox95, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox20, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox21)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox30)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox31)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox32)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox33)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox36)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox37)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox38)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox39)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox42)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox43)
                    .addComponent(jLabel82))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox44)
                    .addComponent(jLabel83))
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel2);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox4.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox4.setText("Предоставить все права");
        jCheckBox4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox2.setText("Снять все права");
        jCheckBox2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(380, Short.MAX_VALUE)
                .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox45.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox45.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox45.setText("Просмотр Сотрудников");

        jLabel38.setText("подробнее");

        jCheckBox46.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox46.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox46.setText("Просмотр Сотрудников");
        jCheckBox46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox46ActionPerformed(evt);
            }
        });

        jLabel39.setText("подробнее");

        jCheckBox47.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox47.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox47.setText("Просмотр Сотрудников своей групы");
        jCheckBox47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox47ActionPerformed(evt);
            }
        });

        jCheckBox48.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox48.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox48.setText("Просмотр Сотрудников своего подразделения");
        jCheckBox48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox48ActionPerformed(evt);
            }
        });

        jCheckBox49.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox49.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox49.setText("Редактирование Сотрудника");

        jCheckBox50.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox50.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox50.setText("Редактирование Сотрудников");
        jCheckBox50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox50ActionPerformed(evt);
            }
        });

        jCheckBox51.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox51.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox51.setText("Редактирование Сотрудников своей группы");
        jCheckBox51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox51ActionPerformed(evt);
            }
        });

        jCheckBox52.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox52.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox52.setText("Редактирование Сотрудников своего подразделения");
        jCheckBox52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox52ActionPerformed(evt);
            }
        });

        jCheckBox53.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox53.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox53.setText("Добавление Сотрудников");

        jCheckBox54.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox54.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox54.setText("Добавление Сотрудников");
        jCheckBox54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox54ActionPerformed(evt);
            }
        });

        jLabel40.setText("подробнее");

        jLabel41.setText("подробнее");

        jLabel42.setText("подробнее");

        jLabel43.setText("подробнее");

        jLabel44.setText("подробнее");

        jLabel45.setText("подробнее");

        jLabel46.setText("подробнее");

        jLabel47.setText("подробнее");

        jCheckBox55.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox55.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox55.setText("Выбор Сотрудников");
        jCheckBox55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox55ActionPerformed(evt);
            }
        });

        jCheckBox56.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox56.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox56.setText("Выбор Сотрудников");
        jCheckBox56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox56ActionPerformed(evt);
            }
        });

        jCheckBox57.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox57.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox57.setText("Выбор удаленных Сотрудников");
        jCheckBox57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox57ActionPerformed(evt);
            }
        });

        jCheckBox58.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox58.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox58.setText("Выбор Сотрудников своей группы");
        jCheckBox58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox58ActionPerformed(evt);
            }
        });

        jCheckBox59.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox59.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox59.setText("Просмотр удаленных Сотрудников");
        jCheckBox59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox59ActionPerformed(evt);
            }
        });

        jCheckBox60.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox60.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox60.setText("Редактирование удаленных Сотрудников");
        jCheckBox60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox60ActionPerformed(evt);
            }
        });

        jLabel48.setText("подробнее");

        jLabel49.setText("подробнее");

        jLabel50.setText("подробнее");

        jLabel51.setText("подробнее");

        jLabel52.setText("подробнее");

        jLabel53.setText("подробнее");

        jCheckBox61.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox61.setText("Выбор линейного персонала ");
        jCheckBox61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox61ActionPerformed(evt);
            }
        });

        jLabel54.setText("подробнее");

        jCheckBox63.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox63.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox63.setText("Добавление Сотрудников своей группы");
        jCheckBox63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox63ActionPerformed(evt);
            }
        });

        jCheckBox64.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox64.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox64.setText("Добавление Сотрудников своего подразделения");
        jCheckBox64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox64ActionPerformed(evt);
            }
        });

        jCheckBox65.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox65.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox65.setText("Добавление Сотрудников линейного персонала");
        jCheckBox65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox65ActionPerformed(evt);
            }
        });

        jLabel55.setText("подробнее");

        jLabel56.setText("подробнее");

        jLabel57.setText("подробнее");

        jCheckBox66.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox66.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox66.setText("Выбор Сотрудников своего подразделения");
        jCheckBox66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox66ActionPerformed(evt);
            }
        });

        jCheckBox67.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox67.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox67.setText("Выбор подчененных сотрудников");
        jCheckBox67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox67ActionPerformed(evt);
            }
        });

        jLabel58.setText("подробнее");

        jLabel59.setText("подробнее");

        jCheckBox90.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox90.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox90.setText("Просмотр подчененных Сотрудников");
        jCheckBox90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox90ActionPerformed(evt);
            }
        });

        jCheckBox91.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox91.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox91.setText("Просмотр Сотрудников линейного персонала");
        jCheckBox91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox91ActionPerformed(evt);
            }
        });

        jLabel84.setText("подробнее");

        jLabel85.setText("подробнее");

        jCheckBox92.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox92.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox92.setText("Редактирование подчененных Сотрудников");
        jCheckBox92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox92ActionPerformed(evt);
            }
        });

        jCheckBox93.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox93.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox93.setText("Редактирование Сотрудников линейного персонала");
        jCheckBox93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox93ActionPerformed(evt);
            }
        });

        jLabel86.setText("подробнее");

        jLabel87.setText("подробнее");

        jCheckBox94.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox94.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox94.setText("Редактирование личные данные");
        jCheckBox94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox94ActionPerformed(evt);
            }
        });

        jLabel88.setText("подробнее");

        jCheckBox96.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox96.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox96.setText("Удаление и востановления Сотрудника");

        jCheckBox97.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox97.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox97.setText("Удаление Сотрудников");
        jCheckBox97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox97ActionPerformed(evt);
            }
        });

        jCheckBox98.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox98.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox98.setText("Востановление Сотрудников");
        jCheckBox98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox98ActionPerformed(evt);
            }
        });

        jLabel89.setText("подробнее");

        jLabel90.setText("подробнее");

        jLabel91.setText("подробнее");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox52, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                            .addComponent(jCheckBox48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox58, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox59, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox61, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox66, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox67, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox91, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox92, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox93, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox94, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox97, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox98, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jCheckBox96, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(56, 56, 56)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel88, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox45, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox46)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox59)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox47)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox48)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox90)
                    .addComponent(jLabel84))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox91)
                    .addComponent(jLabel85))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox49, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox50)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox60)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox51)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox52, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox92, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox93, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel87))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox94, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel88))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox96, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel89))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox97)
                    .addComponent(jLabel90))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox98)
                    .addComponent(jLabel91))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox53, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox54)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox63)
                    .addComponent(jLabel55))
                .addGap(4, 4, 4)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox64)
                    .addComponent(jLabel56))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox65)
                    .addComponent(jLabel57))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox55)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox56)
                    .addComponent(jLabel51))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox57)
                    .addComponent(jLabel52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox58)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox66)
                    .addComponent(jLabel58))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox67)
                    .addComponent(jLabel59))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox61)
                    .addComponent(jLabel54))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel7);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox5.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox5.setText("Предоставить все права");
        jCheckBox5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jCheckBox6.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox6.setText("Снять все права");
        jCheckBox6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(380, Short.MAX_VALUE)
                .addComponent(jCheckBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox68.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox68.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox68.setText("Просмотр Учереждений");

        jLabel60.setText("подробнее");

        jCheckBox69.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox69.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox69.setText("Просмотр Учереждений подразделения \"ШКОЛЫ\"");
        jCheckBox69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox69ActionPerformed(evt);
            }
        });

        jLabel61.setText("подробнее");

        jCheckBox70.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox70.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox70.setText("Просмотр закрытых Учереждений");
        jCheckBox70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox70ActionPerformed(evt);
            }
        });

        jCheckBox71.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox71.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox71.setText("Просмотр закрепленных Учереждений");
        jCheckBox71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox71ActionPerformed(evt);
            }
        });

        jCheckBox72.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox72.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox72.setText("Редактирование Учереждения");

        jCheckBox73.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox73.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox73.setText("Редактирование Учереждения подразделения \"ШКОЛЫ\"");
        jCheckBox73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox73ActionPerformed(evt);
            }
        });

        jCheckBox74.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox74.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox74.setText("Редактирование закрытых Учереждений");
        jCheckBox74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox74ActionPerformed(evt);
            }
        });

        jCheckBox75.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox75.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox75.setText("Редактирование Закрепленных Учереждений");
        jCheckBox75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox75ActionPerformed(evt);
            }
        });

        jCheckBox76.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox76.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox76.setText("Добавление Сотрудников");

        jCheckBox77.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox77.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox77.setText("Добавление Сотрудников любой группы");
        jCheckBox77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox77ActionPerformed(evt);
            }
        });

        jLabel62.setText("подробнее");

        jLabel63.setText("подробнее");

        jLabel64.setText("подробнее");

        jLabel65.setText("подробнее");

        jLabel66.setText("подробнее");

        jLabel67.setText("подробнее");

        jLabel68.setText("подробнее");

        jLabel69.setText("подробнее");

        jCheckBox78.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox78.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox78.setText("Выбор Сотрудников");
        jCheckBox78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox78ActionPerformed(evt);
            }
        });

        jCheckBox79.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox79.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox79.setText("Выбор удаленных Сотрудников");
        jCheckBox79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox79ActionPerformed(evt);
            }
        });

        jCheckBox80.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox80.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox80.setText("Выбор Сотрудников любой группы");
        jCheckBox80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox80ActionPerformed(evt);
            }
        });

        jCheckBox81.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox81.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox81.setText("Выбор Сотрудников своей группы");
        jCheckBox81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox81ActionPerformed(evt);
            }
        });

        jCheckBox82.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox82.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox82.setText("Просмотр Учереждений подразделения \"САДЫ\"");
        jCheckBox82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox82ActionPerformed(evt);
            }
        });

        jCheckBox83.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox83.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox83.setText("Редактирование Учереждения подразделения \"САДЫ\"");
        jCheckBox83.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox83ActionPerformed(evt);
            }
        });

        jLabel70.setText("подробнее");

        jLabel71.setText("подробнее");

        jLabel72.setText("подробнее");

        jLabel73.setText("подробнее");

        jLabel74.setText("подробнее");

        jLabel75.setText("подробнее");

        jCheckBox84.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox84.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox84.setText("Выбор линейного персонала ");
        jCheckBox84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox84ActionPerformed(evt);
            }
        });

        jLabel76.setText("подробнее");

        jCheckBox85.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox85.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox85.setText("Добавление Сотрудников своей группы");
        jCheckBox85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox85ActionPerformed(evt);
            }
        });

        jCheckBox86.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox86.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox86.setText("Добавление Сотрудников своего подразделения");
        jCheckBox86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox86ActionPerformed(evt);
            }
        });

        jCheckBox87.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox87.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox87.setText("Добавление линейного персонала");
        jCheckBox87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox87ActionPerformed(evt);
            }
        });

        jLabel77.setText("подробнее");

        jLabel78.setText("подробнее");

        jLabel79.setText("подробнее");

        jCheckBox88.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox88.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox88.setText("Выбор Сотрудников своего подразделения");
        jCheckBox88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox88ActionPerformed(evt);
            }
        });

        jCheckBox89.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox89.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox89.setText("Выбор подчененных сотрудников");
        jCheckBox89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox89ActionPerformed(evt);
            }
        });

        jLabel80.setText("подробнее");

        jLabel81.setText("подробнее");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox76, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox75, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox74, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                            .addComponent(jCheckBox71, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox70, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox81, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox82, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox84, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox86, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox88, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox89, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(77, 77, 77)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox68, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox69)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox82)
                    .addComponent(jLabel70))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox70)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox71)
                    .addComponent(jLabel63))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox72, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox73)
                    .addComponent(jLabel69))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox83)
                    .addComponent(jLabel71))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox74)
                    .addComponent(jLabel68))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox75, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox76, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox77)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox85)
                    .addComponent(jLabel77))
                .addGap(4, 4, 4)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox86)
                    .addComponent(jLabel78))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox87)
                    .addComponent(jLabel79))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox78)
                    .addComponent(jLabel72))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox79)
                    .addComponent(jLabel73))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox80)
                    .addComponent(jLabel74))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox81)
                    .addComponent(jLabel75))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox88)
                    .addComponent(jLabel80))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox89)
                    .addComponent(jLabel81))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox84)
                    .addComponent(jLabel76))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel11);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox22.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox22.setText("Предоставить все права");
        jCheckBox22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jCheckBox23.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox23.setText("Снять все права");
        jCheckBox23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(380, Short.MAX_VALUE)
                .addComponent(jCheckBox23, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox22, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox23)
                    .addComponent(jCheckBox22))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
        );

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel100.setText("Просмотр");

        jLabel135.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel135.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel135.setText("<html><align=\"center\">Редактирование<p>Добавление");
        jLabel135.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel136.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel136.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel136.setText("<html><align=\"center\">Востановление<p>Удаление");
        jLabel136.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel137.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel137.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel137.setText("Выбор");
        jLabel137.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Изменение прав доступа");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("Просмотр");

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel99.setText("<html><align=\"center\">Редактирование<p>Добавление");
        jLabel99.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel101.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel101.setText("Выбор");
        jLabel101.setEnabled(false);
        jLabel101.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jCBMedicalBookVIEW_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookVIEW_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookVIEW_ALLActionPerformed(evt);
            }
        });

        jCBMedicalBookVIEW_DELET.setBackground(new java.awt.Color(255, 255, 255));

        jCBMedicalBookVIEW_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));

        jCBMedicalBookVIEW_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookVIEW_OWNDEPARTMENT.setEnabled(false);

        jCBMedicalBookVIEW_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));

        jCBMedicalBookVIEW_LINESTAFF.setBackground(new java.awt.Color(255, 255, 255));

        jCBMedicalBookCHANGE_DELET.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookCHANGE_DELET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookCHANGE_DELETActionPerformed(evt);
            }
        });

        jCBMedicalBookCHANGE_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookCHANGE_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookCHANGE_ALLActionPerformed(evt);
            }
        });

        jCBMedicalBookCHANGE_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookCHANGE_OWNGROUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookCHANGE_OWNGROUPActionPerformed(evt);
            }
        });

        jCBMedicalBookCHANGE_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookCHANGE_OWNDEPARTMENT.setEnabled(false);

        jCBMedicalBookCHANGE_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));

        jCBMedicalBookCHANGE_LINESTAFF.setBackground(new java.awt.Color(255, 255, 255));

        jCBMedicalBookDELET_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookDELET_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookDELET_ALLActionPerformed(evt);
            }
        });

        jCBMedicalBookDELET_DELET.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookDELET_DELET.setEnabled(false);
        jCBMedicalBookDELET_DELET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookDELET_DELETActionPerformed(evt);
            }
        });

        jCBMedicalBookDELET_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookDELET_OWNGROUP.setEnabled(false);
        jCBMedicalBookDELET_OWNGROUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookDELET_OWNGROUPActionPerformed(evt);
            }
        });

        jCBMedicalBookDELET_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookDELET_OWNDEPARTMENT.setEnabled(false);

        jCBMedicalBookDELET_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookDELET_EMPLOYEEUNDER.setEnabled(false);

        jCBMedicalBookDELET_LINESTAFF.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookDELET_LINESTAFF.setEnabled(false);

        jCBMedicalBookSELECT_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookSELECT_ALL.setEnabled(false);
        jCBMedicalBookSELECT_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookSELECT_ALLActionPerformed(evt);
            }
        });

        jCBMedicalBookSELECT_DELET.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookSELECT_DELET.setEnabled(false);

        jCBMedicalBookSELECT_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookSELECT_OWNGROUP.setEnabled(false);

        jCBMedicalBookSELECT_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookSELECT_OWNDEPARTMENT.setEnabled(false);

        jCBMedicalBookSELECT_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookSELECT_EMPLOYEEUNDER.setEnabled(false);

        jCBMedicalBookSELECT_LINESTAFF.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookSELECT_LINESTAFF.setEnabled(false);

        jLabel96.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel96.setText("Все ЛМК");

        jLabel97.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel97.setText("Удаленные ЛМК");

        jLabel98.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel98.setText("ЛМК Своей группы");

        jLabel102.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel102.setText("резерв");
        jLabel102.setEnabled(false);

        jLabel103.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel103.setText("ЛМК Подчененных сотрудников");

        jLabel104.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel104.setText("ЛМК Линейного персонала (для ТУ)");

        jCheckBox123.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox123.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox123.setText("Снять все права");
        jCheckBox123.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox123.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox123ActionPerformed(evt);
            }
        });

        jCheckBox124.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox124.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox124.setSelected(true);
        jCheckBox124.setText("Предоставить все права");
        jCheckBox124.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox124.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox124ActionPerformed(evt);
            }
        });

        jLabel134.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel134.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel134.setText("<html><align=\"center\">Востановление<p>Удаление");
        jLabel134.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(352, 352, 352)
                        .addComponent(jCheckBox123, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox124, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(jLabel95)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel97, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel98, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel102, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel103, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel104, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBMedicalBookVIEW_ALL)
                            .addComponent(jCBMedicalBookVIEW_DELET)
                            .addComponent(jCBMedicalBookVIEW_OWNGROUP)
                            .addComponent(jCBMedicalBookVIEW_OWNDEPARTMENT)
                            .addComponent(jCBMedicalBookVIEW_EMPLOYEEUNDER)
                            .addComponent(jCBMedicalBookVIEW_LINESTAFF))
                        .addGap(84, 84, 84)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBMedicalBookCHANGE_DELET)
                            .addComponent(jCBMedicalBookCHANGE_ALL)
                            .addComponent(jCBMedicalBookCHANGE_OWNGROUP)
                            .addComponent(jCBMedicalBookCHANGE_OWNDEPARTMENT)
                            .addComponent(jCBMedicalBookCHANGE_EMPLOYEEUNDER)
                            .addComponent(jCBMedicalBookCHANGE_LINESTAFF))
                        .addGap(125, 125, 125)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBMedicalBookDELET_ALL)
                            .addComponent(jCBMedicalBookDELET_DELET)
                            .addComponent(jCBMedicalBookDELET_OWNGROUP)
                            .addComponent(jCBMedicalBookDELET_OWNDEPARTMENT)
                            .addComponent(jCBMedicalBookDELET_EMPLOYEEUNDER)
                            .addComponent(jCBMedicalBookDELET_LINESTAFF))
                        .addGap(87, 87, 87)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBMedicalBookSELECT_ALL)
                            .addComponent(jCBMedicalBookSELECT_DELET)
                            .addComponent(jCBMedicalBookSELECT_OWNGROUP)
                            .addComponent(jCBMedicalBookSELECT_OWNDEPARTMENT)
                            .addComponent(jCBMedicalBookSELECT_EMPLOYEEUNDER)
                            .addComponent(jCBMedicalBookSELECT_LINESTAFF))))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jSeparator21, jSeparator3, jSeparator4, jSeparator5, jSeparator6, jSeparator7, jSeparator8});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel102, jLabel103, jLabel104, jLabel96, jLabel97, jLabel98});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox123)
                    .addComponent(jCheckBox124))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel101, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBMedicalBookVIEW_ALL)
                    .addComponent(jCBMedicalBookCHANGE_ALL)
                    .addComponent(jCBMedicalBookDELET_ALL)
                    .addComponent(jCBMedicalBookSELECT_ALL)
                    .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookVIEW_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookCHANGE_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookDELET_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookSELECT_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel98, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookVIEW_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookCHANGE_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookDELET_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookSELECT_OWNGROUP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookVIEW_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookCHANGE_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookDELET_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookSELECT_OWNDEPARTMENT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookVIEW_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookCHANGE_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookDELET_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookSELECT_EMPLOYEEUNDER))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookVIEW_LINESTAFF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookCHANGE_LINESTAFF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookDELET_LINESTAFF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBMedicalBookSELECT_LINESTAFF))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("ЛМК", jPanel1);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeVIEW_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeVIEW_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBEmployeeVIEW_ALLActionPerformed(evt);
            }
        });

        jCBEmployeeVIEW_DELET.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeVIEW_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeVIEW_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeVIEW_OWNDEPARTMENT.setEnabled(false);

        jCBEmployeeVIEW_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeVIEW_LINESTAFF.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeCHANGE_DELET.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeCHANGE_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeCHANGE_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBEmployeeCHANGE_ALLActionPerformed(evt);
            }
        });

        jCBEmployeeCHANGE_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeCHANGE_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeCHANGE_OWNDEPARTMENT.setEnabled(false);

        jCBEmployeeCHANGE_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeCHANGE_LINESTAFF.setBackground(new java.awt.Color(255, 255, 255));

        jCBEmployeeDELET_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeDELET_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBEmployeeDELET_ALLActionPerformed(evt);
            }
        });

        jCBEmployeeDELET_DELET.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeDELET_DELET.setEnabled(false);

        jCBEmployeeDELET_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeDELET_OWNGROUP.setEnabled(false);

        jCBEmployeeDELET_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeDELET_OWNDEPARTMENT.setEnabled(false);

        jCBEmployeeDELET_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeDELET_EMPLOYEEUNDER.setEnabled(false);

        jCBEmployeeDELET_LINESTAFF.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeDELET_LINESTAFF.setEnabled(false);

        jCBEmployeeSELECT_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeSELECT_ALL.setEnabled(false);
        jCBEmployeeSELECT_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBEmployeeSELECT_ALLActionPerformed(evt);
            }
        });

        jCBEmployeeSELECT_DELET.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeSELECT_DELET.setEnabled(false);

        jCBEmployeeSELECT_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeSELECT_OWNGROUP.setEnabled(false);

        jCBEmployeeSELECT_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeSELECT_OWNDEPARTMENT.setEnabled(false);

        jCBEmployeeSELECT_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeSELECT_EMPLOYEEUNDER.setEnabled(false);

        jCBEmployeeSELECT_LINESTAFF.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeSELECT_LINESTAFF.setEnabled(false);

        jLabel109.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel109.setText("Все Сотрудники");

        jLabel110.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel110.setText("Удаленные Сотрудники");

        jLabel111.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel111.setText("Сотрудники Своей группы");

        jLabel112.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel112.setText("резерв");
        jLabel112.setEnabled(false);

        jLabel113.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel113.setText("Подчененные сотрудники");

        jLabel114.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel114.setText("Сотрудники Линейного персонала");

        jCheckBox149.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox149.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox149.setText("Снять все права");
        jCheckBox149.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox149.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox149ActionPerformed(evt);
            }
        });

        jCheckBox150.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox150.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox150.setSelected(true);
        jCheckBox150.setText("Предоставить все права");
        jCheckBox150.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox150.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox150ActionPerformed(evt);
            }
        });

        jLabel138.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel138.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel138.setText("Просмотр");

        jLabel139.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel139.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel139.setText("<html><align=\"center\">Редактирование<p>Добавление");
        jLabel139.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel140.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel140.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel140.setText("<html><align=\"center\">Востановление<p>Удаление");
        jLabel140.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel141.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel141.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel141.setText("Выбор");
        jLabel141.setEnabled(false);
        jLabel141.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel110, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel112, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel109, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBEmployeeVIEW_ALL)
                            .addComponent(jCBEmployeeVIEW_DELET)
                            .addComponent(jCBEmployeeVIEW_OWNGROUP)
                            .addComponent(jCBEmployeeVIEW_OWNDEPARTMENT)
                            .addComponent(jCBEmployeeVIEW_EMPLOYEEUNDER)
                            .addComponent(jCBEmployeeVIEW_LINESTAFF))
                        .addGap(84, 84, 84)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBEmployeeCHANGE_DELET)
                            .addComponent(jCBEmployeeCHANGE_ALL)
                            .addComponent(jCBEmployeeCHANGE_OWNGROUP)
                            .addComponent(jCBEmployeeCHANGE_OWNDEPARTMENT)
                            .addComponent(jCBEmployeeCHANGE_EMPLOYEEUNDER)
                            .addComponent(jCBEmployeeCHANGE_LINESTAFF))
                        .addGap(124, 124, 124)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBEmployeeDELET_DELET)
                            .addComponent(jCBEmployeeDELET_OWNGROUP)
                            .addComponent(jCBEmployeeDELET_OWNDEPARTMENT)
                            .addComponent(jCBEmployeeDELET_EMPLOYEEUNDER)
                            .addComponent(jCBEmployeeDELET_LINESTAFF)
                            .addComponent(jCBEmployeeDELET_ALL))
                        .addGap(87, 87, 87)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBEmployeeSELECT_ALL)
                            .addComponent(jCBEmployeeSELECT_DELET)
                            .addComponent(jCBEmployeeSELECT_OWNGROUP)
                            .addComponent(jCBEmployeeSELECT_OWNDEPARTMENT)
                            .addComponent(jCBEmployeeSELECT_EMPLOYEEUNDER)
                            .addComponent(jCBEmployeeSELECT_LINESTAFF)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(jLabel138)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(352, 352, 352)
                        .addComponent(jCheckBox149, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox150, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator29, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jSeparator10, jSeparator11, jSeparator12, jSeparator13, jSeparator14, jSeparator9});

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel109, jLabel110, jLabel111, jLabel112, jLabel113, jLabel114});

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCBEmployeeDELET_ALL, jCBEmployeeDELET_DELET, jCBEmployeeDELET_EMPLOYEEUNDER, jCBEmployeeDELET_LINESTAFF, jCBEmployeeDELET_OWNDEPARTMENT, jCBEmployeeDELET_OWNGROUP});

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCBEmployeeCHANGE_ALL, jCBEmployeeCHANGE_DELET, jCBEmployeeCHANGE_EMPLOYEEUNDER, jCBEmployeeCHANGE_LINESTAFF, jCBEmployeeCHANGE_OWNDEPARTMENT, jCBEmployeeCHANGE_OWNGROUP});

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCBEmployeeVIEW_ALL, jCBEmployeeVIEW_DELET, jCBEmployeeVIEW_EMPLOYEEUNDER, jCBEmployeeVIEW_LINESTAFF, jCBEmployeeVIEW_OWNDEPARTMENT, jCBEmployeeVIEW_OWNGROUP});

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCBEmployeeSELECT_ALL, jCBEmployeeSELECT_DELET, jCBEmployeeSELECT_EMPLOYEEUNDER, jCBEmployeeSELECT_LINESTAFF, jCBEmployeeSELECT_OWNDEPARTMENT, jCBEmployeeSELECT_OWNGROUP});

        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox149)
                    .addComponent(jCheckBox150))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator29, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBEmployeeVIEW_ALL)
                    .addComponent(jCBEmployeeCHANGE_ALL)
                    .addComponent(jCBEmployeeDELET_ALL)
                    .addComponent(jCBEmployeeSELECT_ALL)
                    .addComponent(jLabel109, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel110, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeVIEW_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeCHANGE_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeDELET_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeSELECT_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeVIEW_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeCHANGE_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeDELET_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeSELECT_OWNGROUP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel112, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeVIEW_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeCHANGE_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeDELET_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeSELECT_OWNDEPARTMENT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel113, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeVIEW_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeCHANGE_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeDELET_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeSELECT_EMPLOYEEUNDER))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel114, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeVIEW_LINESTAFF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeCHANGE_LINESTAFF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeDELET_LINESTAFF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeSELECT_LINESTAFF))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Сотрудники", jPanel13);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel115.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel115.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel115.setText("Просмотр");

        jLabel116.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel116.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel116.setText("Редактирование");

        jLabel117.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel117.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel117.setText("Удаление и востановление");

        jLabel118.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel118.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel118.setText("Выбор");
        jLabel118.setEnabled(false);
        jLabel118.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jCBInstitutionVIEW_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionVIEW_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionVIEW_ALLActionPerformed(evt);
            }
        });

        jCBInstitutionVIEW_CLOSED.setBackground(new java.awt.Color(255, 255, 255));

        jCBInstitutionVIEW_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));

        jCBInstitutionVIEW_INSTITUTIONUNDER.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox167.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox167.setEnabled(false);

        jCBInstitutionCHANGE_CLOSED.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionCHANGE_CLOSED.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jCBInstitutionCHANGE_CLOSEDCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jCBInstitutionCHANGE_CLOSED.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionCHANGE_CLOSEDActionPerformed(evt);
            }
        });

        jCBInstitutionCHANGE_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionCHANGE_ALL.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBInstitutionCHANGE_ALLItemStateChanged(evt);
            }
        });
        jCBInstitutionCHANGE_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionCHANGE_ALLActionPerformed(evt);
            }
        });

        jCBInstitutionCHANGE_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionCHANGE_OWNGROUP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBInstitutionCHANGE_OWNGROUPItemStateChanged(evt);
            }
        });
        jCBInstitutionCHANGE_OWNGROUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionCHANGE_OWNGROUPActionPerformed(evt);
            }
        });

        jCBInstitutionCHANGE_INSTITUTIONUNDER.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox168.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox168.setEnabled(false);

        jCBInstitutionDELET_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionDELET_ALL.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBInstitutionDELET_ALLItemStateChanged(evt);
            }
        });
        jCBInstitutionDELET_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionDELET_ALLActionPerformed(evt);
            }
        });

        jCBInstitutionDELET_CLOSED.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionDELET_CLOSED.setEnabled(false);
        jCBInstitutionDELET_CLOSED.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBInstitutionDELET_CLOSEDItemStateChanged(evt);
            }
        });
        jCBInstitutionDELET_CLOSED.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionDELET_CLOSEDActionPerformed(evt);
            }
        });

        jCBInstitutionDELET_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionDELET_OWNGROUP.setEnabled(false);
        jCBInstitutionDELET_OWNGROUP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBInstitutionDELET_OWNGROUPItemStateChanged(evt);
            }
        });
        jCBInstitutionDELET_OWNGROUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionDELET_OWNGROUPActionPerformed(evt);
            }
        });

        jCBInstitutionDELET_INSTITUTIONUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionDELET_INSTITUTIONUNDER.setEnabled(false);
        jCBInstitutionDELET_INSTITUTIONUNDER.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBInstitutionDELET_INSTITUTIONUNDERItemStateChanged(evt);
            }
        });
        jCBInstitutionDELET_INSTITUTIONUNDER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionDELET_INSTITUTIONUNDERActionPerformed(evt);
            }
        });

        jCheckBox169.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox169.setEnabled(false);

        jCBInstitutionSELECT_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionSELECT_ALL.setEnabled(false);
        jCBInstitutionSELECT_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBInstitutionSELECT_ALLActionPerformed(evt);
            }
        });

        jCBInstitutionSELECT_CLOSED.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionSELECT_CLOSED.setEnabled(false);

        jCBInstitutionSELECT_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionSELECT_OWNGROUP.setEnabled(false);

        jCBInstitutionSELECT_INSTITUTIONUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBInstitutionSELECT_INSTITUTIONUNDER.setEnabled(false);

        jCheckBox170.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox170.setEnabled(false);

        jLabel119.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel119.setText("Все Учереждения");

        jLabel120.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel120.setText("Закрытые Учереждения");

        jLabel121.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel121.setText("Учереждения своей группы");

        jLabel122.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel122.setText("Учереждения в подчинении");

        jCheckBox175.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox175.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox175.setText("Снять все права");
        jCheckBox175.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox175.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox175ActionPerformed(evt);
            }
        });

        jCheckBox176.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox176.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox176.setSelected(true);
        jCheckBox176.setText("Предоставить все права");
        jCheckBox176.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox176.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox176ActionPerformed(evt);
            }
        });

        jLabel123.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel123.setText("резерв");
        jLabel123.setEnabled(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel123, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(477, 477, 477))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jCheckBox170, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel119, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBInstitutionVIEW_ALL)
                                .addGap(84, 84, 84)
                                .addComponent(jCBInstitutionCHANGE_ALL)
                                .addGap(125, 125, 125)
                                .addComponent(jCBInstitutionDELET_ALL)
                                .addGap(130, 130, 130)
                                .addComponent(jCBInstitutionSELECT_ALL))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel121, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBInstitutionVIEW_OWNGROUP)
                                .addGap(84, 84, 84)
                                .addComponent(jCBInstitutionCHANGE_OWNGROUP)
                                .addGap(125, 125, 125)
                                .addComponent(jCBInstitutionDELET_OWNGROUP)
                                .addGap(130, 130, 130)
                                .addComponent(jCBInstitutionSELECT_OWNGROUP))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel122, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBInstitutionVIEW_INSTITUTIONUNDER)
                                .addGap(84, 84, 84)
                                .addComponent(jCBInstitutionCHANGE_INSTITUTIONUNDER)
                                .addGap(125, 125, 125)
                                .addComponent(jCBInstitutionDELET_INSTITUTIONUNDER)
                                .addGap(130, 130, 130)
                                .addComponent(jCBInstitutionSELECT_INSTITUTIONUNDER))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel120, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBInstitutionVIEW_CLOSED)
                                .addGap(84, 84, 84)
                                .addComponent(jCBInstitutionCHANGE_CLOSED)
                                .addGap(125, 125, 125)
                                .addComponent(jCBInstitutionDELET_CLOSED)
                                .addGap(130, 130, 130)
                                .addComponent(jCBInstitutionSELECT_CLOSED)))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 79, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jCheckBox167)
                        .addGap(84, 84, 84)
                        .addComponent(jCheckBox168)
                        .addGap(125, 125, 125)
                        .addComponent(jCheckBox169)
                        .addGap(182, 182, 182))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jCheckBox175, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox176, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel115)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel116, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel117, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))))
        );

        jPanel14Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jSeparator15, jSeparator16, jSeparator17, jSeparator18, jSeparator19, jSeparator20});

        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox175)
                    .addComponent(jCheckBox176))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel115)
                    .addComponent(jLabel116)
                    .addComponent(jLabel117)
                    .addComponent(jLabel118))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBInstitutionVIEW_ALL)
                    .addComponent(jCBInstitutionCHANGE_ALL)
                    .addComponent(jCBInstitutionDELET_ALL)
                    .addComponent(jCBInstitutionSELECT_ALL)
                    .addComponent(jLabel119, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel120, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionVIEW_CLOSED, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionCHANGE_CLOSED, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionDELET_CLOSED, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionSELECT_CLOSED, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel121, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionVIEW_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionCHANGE_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionDELET_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionSELECT_OWNGROUP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel122, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionVIEW_INSTITUTIONUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionCHANGE_INSTITUTIONUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionDELET_INSTITUTIONUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBInstitutionSELECT_INSTITUTIONUNDER))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel123, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox167, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox168, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox169, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox170))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Учереждения", jPanel14);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel125.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel125.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel125.setText("Просмотр");

        jLabel126.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel126.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel126.setText("Редактирование");

        jLabel127.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel127.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel127.setText("Удаление и востановление");

        jLabel128.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel128.setText("Выбор");
        jLabel128.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jCBUserVIEW_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserVIEW_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBUserVIEW_ALLActionPerformed(evt);
            }
        });

        jCBUserVIEW_DELET.setBackground(new java.awt.Color(255, 255, 255));

        jCBUserVIEW_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));

        jCBUserVIEW_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserVIEW_OWNDEPARTMENT.setEnabled(false);

        jCBUserVIEW_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserVIEW_EMPLOYEEUNDER.setEnabled(false);

        jCBUserCHANGE_DELET.setBackground(new java.awt.Color(255, 255, 255));

        jCBUserCHANGE_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserCHANGE_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBUserCHANGE_ALLActionPerformed(evt);
            }
        });

        jCBUserCHANGE_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));

        jCBUserCHANGE_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserCHANGE_OWNDEPARTMENT.setEnabled(false);

        jCBUserCHANGE_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserCHANGE_EMPLOYEEUNDER.setEnabled(false);

        jCBUserDELET_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserDELET_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBUserDELET_ALLActionPerformed(evt);
            }
        });

        jCBUserDELET_DELET.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserDELET_DELET.setEnabled(false);

        jCBUserDELET_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserDELET_OWNGROUP.setEnabled(false);

        jCBUserDELET_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserDELET_OWNDEPARTMENT.setEnabled(false);

        jCBUserDELET_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserDELET_EMPLOYEEUNDER.setEnabled(false);

        jCBUserSELECT_ALL.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserSELECT_ALL.setEnabled(false);
        jCBUserSELECT_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBUserSELECT_ALLActionPerformed(evt);
            }
        });

        jCBUserSELECT_DELET.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserSELECT_DELET.setEnabled(false);

        jCBUserSELECT_OWNGROUP.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserSELECT_OWNGROUP.setEnabled(false);

        jCBUserSELECT_OWNDEPARTMENT.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserSELECT_OWNDEPARTMENT.setEnabled(false);

        jCBUserSELECT_EMPLOYEEUNDER.setBackground(new java.awt.Color(255, 255, 255));
        jCBUserSELECT_EMPLOYEEUNDER.setEnabled(false);

        jLabel129.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel129.setText("Все Пользователи");

        jLabel130.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel130.setText("Удаленные Пользователи");

        jLabel131.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel131.setText("Пользователи своей группы");

        jLabel132.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel132.setText("резерв");

        jCheckBox179.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox179.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox179.setText("Снять все права");
        jCheckBox179.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox179.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox179ActionPerformed(evt);
            }
        });

        jCheckBox180.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox180.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox180.setSelected(true);
        jCheckBox180.setText("Предоставить все права");
        jCheckBox180.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox180ActionPerformed(evt);
            }
        });

        jLabel133.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel133.setText("резерв");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel129, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBUserVIEW_ALL)
                                .addGap(84, 84, 84)
                                .addComponent(jCBUserCHANGE_ALL)
                                .addGap(125, 125, 125)
                                .addComponent(jCBUserDELET_ALL)
                                .addGap(130, 130, 130)
                                .addComponent(jCBUserSELECT_ALL))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel131, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBUserVIEW_OWNGROUP)
                                .addGap(84, 84, 84)
                                .addComponent(jCBUserCHANGE_OWNGROUP)
                                .addGap(125, 125, 125)
                                .addComponent(jCBUserDELET_OWNGROUP)
                                .addGap(130, 130, 130)
                                .addComponent(jCBUserSELECT_OWNGROUP))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel132, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBUserVIEW_OWNDEPARTMENT)
                                .addGap(84, 84, 84)
                                .addComponent(jCBUserCHANGE_OWNDEPARTMENT)
                                .addGap(125, 125, 125)
                                .addComponent(jCBUserDELET_OWNDEPARTMENT)
                                .addGap(130, 130, 130)
                                .addComponent(jCBUserSELECT_OWNDEPARTMENT))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel130, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBUserVIEW_DELET)
                                .addGap(84, 84, 84)
                                .addComponent(jCBUserCHANGE_DELET)
                                .addGap(125, 125, 125)
                                .addComponent(jCBUserDELET_DELET)
                                .addGap(130, 130, 130)
                                .addComponent(jCBUserSELECT_DELET)))
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator28, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator26, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator25, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator24, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator23))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel133, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jCBUserVIEW_EMPLOYEEUNDER)
                        .addGap(84, 84, 84)
                        .addComponent(jCBUserCHANGE_EMPLOYEEUNDER)
                        .addGap(125, 125, 125)
                        .addComponent(jCBUserDELET_EMPLOYEEUNDER)
                        .addGap(130, 130, 130)
                        .addComponent(jCBUserSELECT_EMPLOYEEUNDER)
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                        .addComponent(jLabel125)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel126, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel128, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                        .addComponent(jCheckBox179, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jCheckBox180, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jSeparator27))
                        .addContainerGap())))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox179)
                    .addComponent(jCheckBox180))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator27, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel125)
                    .addComponent(jLabel126)
                    .addComponent(jLabel127)
                    .addComponent(jLabel128))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBUserVIEW_ALL)
                    .addComponent(jCBUserCHANGE_ALL)
                    .addComponent(jCBUserDELET_ALL)
                    .addComponent(jCBUserSELECT_ALL)
                    .addComponent(jLabel129, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator23, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel130, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserVIEW_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserCHANGE_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserDELET_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserSELECT_DELET, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel131, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserVIEW_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserCHANGE_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserDELET_OWNGROUP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserSELECT_OWNGROUP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator25, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel132, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserVIEW_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserCHANGE_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserDELET_OWNDEPARTMENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserSELECT_OWNDEPARTMENT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator26, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel133, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserVIEW_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserCHANGE_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserDELET_EMPLOYEEUNDER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBUserSELECT_EMPLOYEEUNDER))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator28, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Пользователи", jPanel16);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox62.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox62.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox62.setText("Снять все права");
        jCheckBox62.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox62ActionPerformed(evt);
            }
        });

        jCheckBox171.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox171.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox171.setSelected(true);
        jCheckBox171.setText("Предоставить все права");
        jCheckBox171.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox171.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox171ActionPerformed(evt);
            }
        });

        jCheckBox172.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox172.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox172.setText("Отправка E-mail рассылок");
        jCheckBox172.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox172ActionPerformed(evt);
            }
        });

        jCheckBox173.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox173.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox173.setText("резерв");
        jCheckBox173.setEnabled(false);

        jCheckBox174.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox174.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox174.setText("резерв");
        jCheckBox174.setEnabled(false);

        jCheckBox177.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox177.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox177.setText("резерв");
        jCheckBox177.setEnabled(false);

        jCheckBox178.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox178.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox178.setText("резерв");
        jCheckBox178.setEnabled(false);

        jLabel30.setText("подробнее");

        jLabel92.setText("подробнее");

        jLabel93.setText("подробнее");

        jLabel94.setText("подробнее");

        jLabel124.setText("подробнее");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(388, 388, 388)
                .addComponent(jCheckBox62, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox171, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator22))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox173, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox174, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox177, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox178, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox172, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE))
                        .addGap(100, 100, 100)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox62)
                    .addComponent(jCheckBox171))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox172, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox173)
                    .addComponent(jLabel92))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox174)
                    .addComponent(jLabel93))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox177)
                    .addComponent(jLabel94))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox178)
                    .addComponent(jLabel124))
                .addGap(172, 172, 172))
        );

        jTabbedPane1.addTab("Рассылки и уведомления", jPanel15);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox1.setText("Снять все права");
        jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Предоставить все права");
        jCheckBox3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCBSettingsGROUP_CHANGE.setBackground(new java.awt.Color(255, 255, 255));
        jCBSettingsGROUP_CHANGE.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBSettingsGROUP_CHANGE.setText("Редактирование групп пользователей");
        jCBSettingsGROUP_CHANGE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBSettingsGROUP_CHANGEActionPerformed(evt);
            }
        });

        jCBSettingsDEPARTMENT_CHANGE.setBackground(new java.awt.Color(255, 255, 255));
        jCBSettingsDEPARTMENT_CHANGE.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBSettingsDEPARTMENT_CHANGE.setText("Редактирование подразделений пользователей");

        jCBSettingsPOSITION_CHANGE.setBackground(new java.awt.Color(255, 255, 255));
        jCBSettingsPOSITION_CHANGE.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBSettingsPOSITION_CHANGE.setText("Редактирование должностей пользователей");

        jCBSettingsPRIVILEGE_CHANGE.setBackground(new java.awt.Color(255, 255, 255));
        jCBSettingsPRIVILEGE_CHANGE.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBSettingsPRIVILEGE_CHANGE.setText("Редактирование прав пользователей");

        jCB.setBackground(new java.awt.Color(255, 255, 255));
        jCB.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCB.setText("резерв");
        jCB.setEnabled(false);

        jLabel2.setText("подробнее");

        jLabel3.setText("подробнее");

        jLabel4.setText("подробнее");

        jLabel5.setText("подробнее");

        jLabel6.setText("подробнее");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(406, 406, 406)
                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBSettingsGROUP_CHANGE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCBSettingsDEPARTMENT_CHANGE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCBSettingsPOSITION_CHANGE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCBSettingsPRIVILEGE_CHANGE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(115, 115, 115)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13))))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jCBSettingsGROUP_CHANGE, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCBSettingsDEPARTMENT_CHANGE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCBSettingsPOSITION_CHANGE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBSettingsPRIVILEGE_CHANGE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jCB, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(230, 230, 230))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jCB, jCBSettingsDEPARTMENT_CHANGE, jCBSettingsGROUP_CHANGE, jCBSettingsPOSITION_CHANGE, jCBSettingsPRIVILEGE_CHANGE, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        jTabbedPane1.addTab("Настройки", jPanel3);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jBChangeOrAddPrivilegeUser.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBChangeOrAddPrivilegeUser.setText("Сохранить изменения");
        jBChangeOrAddPrivilegeUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBChangeOrAddPrivilegeUserActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("выйти");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Название");

        jTFPrivilegeName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFPrivilegeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFPrivilegeNameFocusLost(evt);
            }
        });
        jTFPrivilegeName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTFPrivilegeNameMousePressed(evt);
            }
        });

        jCheckBox99.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox99.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox99.setText("Снять все права");
        jCheckBox99.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox99ActionPerformed(evt);
            }
        });

        jCheckBox181.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox181.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox181.setSelected(true);
        jCheckBox181.setText("Предоставить все права");
        jCheckBox181.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox181.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox181ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFPrivilegeName, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox99, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox181, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jBChangeOrAddPrivilegeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox99)
                        .addComponent(jCheckBox181))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTFPrivilegeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBChangeOrAddPrivilegeUser)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();    
    }//GEN-LAST:event_formInternalFrameClosing

    private void jCheckBox21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox21ActionPerformed

    private void jCheckBox19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox19ActionPerformed

    private void jCheckBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox18ActionPerformed

    private void jCheckBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox17ActionPerformed

    private void jCheckBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox15ActionPerformed

    private void jCheckBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox14ActionPerformed

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox13ActionPerformed

    private void jCheckBox36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox36ActionPerformed

    private void jCheckBox37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox37ActionPerformed

    private void jCheckBox38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox38ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox38ActionPerformed

    private void jCheckBox39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox39ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox39ActionPerformed

    private void jCheckBox40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox40ActionPerformed

    private void jCheckBox41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox41ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox41ActionPerformed

    private void jCheckBox42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox42ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox42ActionPerformed

    private void jCheckBox46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox46ActionPerformed

    private void jCheckBox47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox47ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox47ActionPerformed

    private void jCheckBox48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox48ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox48ActionPerformed

    private void jCheckBox50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox50ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox50ActionPerformed

    private void jCheckBox51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox51ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox51ActionPerformed

    private void jCheckBox52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox52ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox52ActionPerformed

    private void jCheckBox54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox54ActionPerformed

    private void jCheckBox55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox55ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox55ActionPerformed

    private void jCheckBox56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox56ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox56ActionPerformed

    private void jCheckBox57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox57ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox57ActionPerformed

    private void jCheckBox58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox58ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox58ActionPerformed

    private void jCheckBox59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox59ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox59ActionPerformed

    private void jCheckBox60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox60ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox60ActionPerformed

    private void jCheckBox61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox61ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox61ActionPerformed

    private void jCheckBox63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox63ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox63ActionPerformed

    private void jCheckBox64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox64ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox64ActionPerformed

    private void jCheckBox65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox65ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox65ActionPerformed

    private void jCheckBox66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox66ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox66ActionPerformed

    private void jCheckBox67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox67ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox67ActionPerformed

    private void jCheckBox69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox69ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox69ActionPerformed

    private void jCheckBox70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox70ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox70ActionPerformed

    private void jCheckBox71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox71ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox71ActionPerformed

    private void jCheckBox73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox73ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox73ActionPerformed

    private void jCheckBox74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox74ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox74ActionPerformed

    private void jCheckBox75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox75ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox75ActionPerformed

    private void jCheckBox77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox77ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox77ActionPerformed

    private void jCheckBox78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox78ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox78ActionPerformed

    private void jCheckBox79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox79ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox79ActionPerformed

    private void jCheckBox80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox80ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox80ActionPerformed

    private void jCheckBox81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox81ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox81ActionPerformed

    private void jCheckBox82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox82ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox82ActionPerformed

    private void jCheckBox83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox83ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox83ActionPerformed

    private void jCheckBox84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox84ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox84ActionPerformed

    private void jCheckBox85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox85ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox85ActionPerformed

    private void jCheckBox86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox86ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox86ActionPerformed

    private void jCheckBox87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox87ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox87ActionPerformed

    private void jCheckBox88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox88ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox88ActionPerformed

    private void jCheckBox89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox89ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox89ActionPerformed

    private void jCheckBox24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox24ActionPerformed

    private void jCheckBox25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox25ActionPerformed

    private void jCheckBox26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox26ActionPerformed

    private void jCheckBox27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox27ActionPerformed

    private void jCheckBox28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox28ActionPerformed

    private void jCheckBox29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox29ActionPerformed

    private void jCheckBox30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox30ActionPerformed

    private void jCheckBox31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox31ActionPerformed

    private void jCheckBox32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox32ActionPerformed

    private void jCheckBox33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox33ActionPerformed

    private void jCheckBox43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox43ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox43ActionPerformed

    private void jCheckBox44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox44ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox44ActionPerformed

    private void jCheckBox90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox90ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox90ActionPerformed

    private void jCheckBox91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox91ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox91ActionPerformed

    private void jCheckBox92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox92ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox92ActionPerformed

    private void jCheckBox93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox93ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox93ActionPerformed

    private void jCheckBox94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox94ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox94ActionPerformed

    private void jCheckBox34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox34ActionPerformed

    private void jCheckBox35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox35ActionPerformed

    private void jCheckBox95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox95ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox95ActionPerformed

    private void jCheckBox97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox97ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox97ActionPerformed

    private void jCheckBox98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox98ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox98ActionPerformed

    private void jCBSettingsGROUP_CHANGEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBSettingsGROUP_CHANGEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBSettingsGROUP_CHANGEActionPerformed

    private void jCheckBox172ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox172ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox172ActionPerformed

    private void jBChangeOrAddPrivilegeUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBChangeOrAddPrivilegeUserActionPerformed
        SaveOrAdd();
        MBProgect.ApplicationOfAccessRights();
    }//GEN-LAST:event_jBChangeOrAddPrivilegeUserActionPerformed

    private void jCheckBox123ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox123ActionPerformed
        jCheckBox123.setSelected(false);
        setMedicalBookField(false);
    }//GEN-LAST:event_jCheckBox123ActionPerformed

    private void jCheckBox124ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox124ActionPerformed
        jCheckBox124.setSelected(true);
        setMedicalBookField(true);
    }//GEN-LAST:event_jCheckBox124ActionPerformed

    private void jCheckBox149ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox149ActionPerformed
        jCheckBox149.setSelected(false);
        setEmployeeField(false);
    }//GEN-LAST:event_jCheckBox149ActionPerformed

    private void jCheckBox150ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox150ActionPerformed
        jCheckBox150.setSelected(true);
        setEmployeeField(true);
    }//GEN-LAST:event_jCheckBox150ActionPerformed

    private void jCheckBox175ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox175ActionPerformed
        jCheckBox175.setSelected(false);
        setInstitutionField(false);
    }//GEN-LAST:event_jCheckBox175ActionPerformed

    private void jCheckBox176ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox176ActionPerformed
        jCheckBox176.setSelected(true);
        setInstitutionField(true);
    }//GEN-LAST:event_jCheckBox176ActionPerformed

    private void jCheckBox179ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox179ActionPerformed
        jCheckBox179.setSelected(false);
        setUserField(false);
    }//GEN-LAST:event_jCheckBox179ActionPerformed

    private void jCheckBox180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox180ActionPerformed
        jCheckBox180.setSelected(true);
        setUserField(true);
    }//GEN-LAST:event_jCheckBox180ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        jCheckBox1.setSelected(false);
        setSettingsField(false);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        jCheckBox3.setSelected(true);
        setSettingsField(true);
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox62ActionPerformed
        jCheckBox62.setSelected(false);
        setMailingField(false);
    }//GEN-LAST:event_jCheckBox62ActionPerformed

    private void jCheckBox171ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox171ActionPerformed
        jCheckBox171.setSelected(true);
        setMailingField(true);
    }//GEN-LAST:event_jCheckBox171ActionPerformed

    private void jCheckBox99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox99ActionPerformed
        jCheckBox99.setSelected(false);
        setMedicalBookField(false);
        setEmployeeField(false);
        setInstitutionField(false);
        setUserField(false);
        setSettingsField(false);
        setMailingField(false);

    }//GEN-LAST:event_jCheckBox99ActionPerformed

    private void jCheckBox181ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox181ActionPerformed
        jCheckBox181.setSelected(true);
        setMedicalBookField(true);
        setEmployeeField(true);
        setInstitutionField(true);
        setUserField(true);
        setSettingsField(true);
        setMailingField(true);
    }//GEN-LAST:event_jCheckBox181ActionPerformed

    private void jCBMedicalBookVIEW_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookVIEW_ALLActionPerformed
        jCBMedicalBookVIEW_DELET.setSelected(jCBMedicalBookVIEW_ALL.isSelected());
        jCBMedicalBookVIEW_OWNGROUP.setSelected(jCBMedicalBookVIEW_ALL.isSelected());
        jCBMedicalBookVIEW_OWNDEPARTMENT.setSelected(jCBMedicalBookVIEW_ALL.isSelected());
        jCBMedicalBookVIEW_EMPLOYEEUNDER.setSelected(jCBMedicalBookVIEW_ALL.isSelected());
        jCBMedicalBookVIEW_LINESTAFF.setSelected(jCBMedicalBookVIEW_ALL.isSelected());
    }//GEN-LAST:event_jCBMedicalBookVIEW_ALLActionPerformed

    private void jCBMedicalBookCHANGE_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookCHANGE_ALLActionPerformed
        jCBMedicalBookVIEW_ALL.setSelected(jCBMedicalBookCHANGE_ALL.isSelected());
        //jCBMedicalBookDELET_ALL.setSelected(jCBMedicalBookCHANGE_ALL.isSelected());
        
        jCBMedicalBookCHANGE_DELET.setSelected(jCBMedicalBookCHANGE_ALL.isSelected());
        jCBMedicalBookCHANGE_OWNGROUP.setSelected(jCBMedicalBookCHANGE_ALL.isSelected());
        jCBMedicalBookCHANGE_OWNDEPARTMENT.setSelected(jCBMedicalBookCHANGE_ALL.isSelected());
        jCBMedicalBookCHANGE_EMPLOYEEUNDER.setSelected(jCBMedicalBookCHANGE_ALL.isSelected());
        jCBMedicalBookCHANGE_LINESTAFF.setSelected(jCBMedicalBookCHANGE_ALL.isSelected());
    }//GEN-LAST:event_jCBMedicalBookCHANGE_ALLActionPerformed

    private void jCBMedicalBookDELET_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookDELET_ALLActionPerformed
        jCBMedicalBookVIEW_ALL.setSelected(jCBMedicalBookDELET_ALL.isSelected());
        jCBMedicalBookCHANGE_ALL.setSelected(jCBMedicalBookDELET_ALL.isSelected());
        
        jCBMedicalBookDELET_ALL.setSelected(jCBMedicalBookDELET_ALL.isSelected());
        jCBMedicalBookDELET_DELET.setSelected(jCBMedicalBookDELET_ALL.isSelected());
        jCBMedicalBookDELET_OWNGROUP.setSelected(jCBMedicalBookDELET_ALL.isSelected());
        jCBMedicalBookDELET_OWNDEPARTMENT.setSelected(jCBMedicalBookDELET_ALL.isSelected());
        jCBMedicalBookDELET_EMPLOYEEUNDER.setSelected(jCBMedicalBookDELET_ALL.isSelected());
        jCBMedicalBookDELET_LINESTAFF.setSelected(jCBMedicalBookDELET_ALL.isSelected());
        
    }//GEN-LAST:event_jCBMedicalBookDELET_ALLActionPerformed

    private void jCBMedicalBookSELECT_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookSELECT_ALLActionPerformed
        jCBMedicalBookVIEW_ALL.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
        jCBMedicalBookCHANGE_ALL.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
        jCBMedicalBookDELET_ALL.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
        
        jCBMedicalBookSELECT_ALL.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
        jCBMedicalBookSELECT_DELET.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
        jCBMedicalBookSELECT_OWNGROUP.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
        jCBMedicalBookSELECT_OWNDEPARTMENT.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
        jCBMedicalBookSELECT_EMPLOYEEUNDER.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
        jCBMedicalBookSELECT_LINESTAFF.setSelected(jCBMedicalBookSELECT_ALL.isSelected());
    }//GEN-LAST:event_jCBMedicalBookSELECT_ALLActionPerformed

    private void jCBEmployeeVIEW_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEmployeeVIEW_ALLActionPerformed
        jCBEmployeeVIEW_ALL.setSelected(jCBEmployeeVIEW_ALL.isSelected());
        jCBEmployeeVIEW_DELET.setSelected(jCBEmployeeVIEW_ALL.isSelected());
        jCBEmployeeVIEW_OWNGROUP.setSelected(jCBEmployeeVIEW_ALL.isSelected());
        jCBEmployeeVIEW_OWNDEPARTMENT.setSelected(jCBEmployeeVIEW_ALL.isSelected());
        jCBEmployeeVIEW_EMPLOYEEUNDER.setSelected(jCBEmployeeVIEW_ALL.isSelected());
        jCBEmployeeVIEW_LINESTAFF.setSelected(jCBEmployeeVIEW_ALL.isSelected());
        
    }//GEN-LAST:event_jCBEmployeeVIEW_ALLActionPerformed

    private void jCBEmployeeCHANGE_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEmployeeCHANGE_ALLActionPerformed
        jCBEmployeeCHANGE_DELET.setSelected(jCBEmployeeCHANGE_ALL.isSelected());
        jCBEmployeeCHANGE_ALL.setSelected(jCBEmployeeCHANGE_ALL.isSelected());
        jCBEmployeeCHANGE_OWNGROUP.setSelected(jCBEmployeeCHANGE_ALL.isSelected());
        jCBEmployeeCHANGE_OWNDEPARTMENT.setSelected(jCBEmployeeCHANGE_ALL.isSelected());
        jCBEmployeeCHANGE_EMPLOYEEUNDER.setSelected(jCBEmployeeCHANGE_ALL.isSelected());
        jCBEmployeeCHANGE_LINESTAFF.setSelected(jCBEmployeeCHANGE_ALL.isSelected());
        
    }//GEN-LAST:event_jCBEmployeeCHANGE_ALLActionPerformed

    private void jCBEmployeeDELET_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEmployeeDELET_ALLActionPerformed
        jCBEmployeeDELET_ALL.setSelected(jCBEmployeeDELET_ALL.isSelected());
        jCBEmployeeDELET_DELET.setSelected(jCBEmployeeDELET_ALL.isSelected());
        jCBEmployeeDELET_OWNGROUP.setSelected(jCBEmployeeDELET_ALL.isSelected());
        jCBEmployeeDELET_OWNDEPARTMENT.setSelected(jCBEmployeeDELET_ALL.isSelected());
        jCBEmployeeDELET_EMPLOYEEUNDER.setSelected(jCBEmployeeDELET_ALL.isSelected());
        jCBEmployeeDELET_LINESTAFF.setSelected(jCBEmployeeDELET_ALL.isSelected());
        
    }//GEN-LAST:event_jCBEmployeeDELET_ALLActionPerformed

    private void jCBEmployeeSELECT_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEmployeeSELECT_ALLActionPerformed
        jCBEmployeeSELECT_ALL.setSelected(jCBEmployeeSELECT_ALL.isSelected());
        jCBEmployeeSELECT_DELET.setSelected(jCBEmployeeSELECT_ALL.isSelected());
        jCBEmployeeSELECT_OWNGROUP.setSelected(jCBEmployeeSELECT_ALL.isSelected());
        jCBEmployeeSELECT_OWNDEPARTMENT.setSelected(jCBEmployeeSELECT_ALL.isSelected());
        jCBEmployeeSELECT_EMPLOYEEUNDER.setSelected(jCBEmployeeSELECT_ALL.isSelected());
        jCBEmployeeSELECT_LINESTAFF.setSelected(jCBEmployeeSELECT_ALL.isSelected());
    }//GEN-LAST:event_jCBEmployeeSELECT_ALLActionPerformed

    private void jCBInstitutionVIEW_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionVIEW_ALLActionPerformed
        jCBInstitutionVIEW_ALL.setSelected(jCBInstitutionVIEW_ALL.isSelected());
        jCBInstitutionVIEW_CLOSED.setSelected(jCBInstitutionVIEW_ALL.isSelected());
        jCBInstitutionVIEW_OWNGROUP.setSelected(jCBInstitutionVIEW_ALL.isSelected());
        jCBInstitutionVIEW_INSTITUTIONUNDER.setSelected(jCBInstitutionVIEW_ALL.isSelected());
        jCheckBox167.setSelected(jCBInstitutionVIEW_ALL.isSelected());
        
    }//GEN-LAST:event_jCBInstitutionVIEW_ALLActionPerformed

    private void jCBInstitutionCHANGE_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionCHANGE_ALLActionPerformed
//        jCBInstitutionCHANGE_CLOSED.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
//        jCBInstitutionCHANGE_ALL.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
//        jCBInstitutionCHANGE_OWNGROUP.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
//        jCBInstitutionCHANGE_INSTITUTIONUNDER.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
//        jCheckBox168.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
        
    }//GEN-LAST:event_jCBInstitutionCHANGE_ALLActionPerformed

    private void jCBInstitutionDELET_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionDELET_ALLActionPerformed
//        jCBInstitutionDELET_ALL.setSelected(jCBInstitutionDELET_ALL.isSelected());
//        jCBInstitutionDELET_CLOSED.setSelected(jCBInstitutionDELET_ALL.isSelected());
//        jCBInstitutionDELET_OWNGROUP.setSelected(jCBInstitutionDELET_ALL.isSelected());
//        jCBInstitutionDELET_INSTITUTIONUNDER.setSelected(jCBInstitutionDELET_ALL.isSelected());
//        jCheckBox169.setSelected(jCBInstitutionDELET_ALL.isSelected());
//        if (jCBInstitutionDELET_ALL.isSelected())
//            jCBInstitutionCHANGE_ALL.setSelected(true);  
//        else 
//            jCBInstitutionCHANGE_ALL.setSelected(false);        
        
    }//GEN-LAST:event_jCBInstitutionDELET_ALLActionPerformed

    private void jCBInstitutionSELECT_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionSELECT_ALLActionPerformed
        jCBInstitutionSELECT_ALL.setSelected(jCBInstitutionSELECT_ALL.isSelected());
        jCBInstitutionSELECT_CLOSED.setSelected(jCBInstitutionSELECT_ALL.isSelected());
        jCBInstitutionSELECT_OWNGROUP.setSelected(jCBInstitutionSELECT_ALL.isSelected());
        jCBInstitutionSELECT_INSTITUTIONUNDER.setSelected(jCBInstitutionSELECT_ALL.isSelected());
        jCheckBox170.setSelected(jCBInstitutionSELECT_ALL.isSelected());
    }//GEN-LAST:event_jCBInstitutionSELECT_ALLActionPerformed

    private void jCBUserVIEW_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUserVIEW_ALLActionPerformed
        jCBUserVIEW_ALL.setSelected(jCBUserVIEW_ALL.isSelected());
        jCBUserVIEW_DELET.setSelected(jCBUserVIEW_ALL.isSelected());
        jCBUserVIEW_OWNGROUP.setSelected(jCBUserVIEW_ALL.isSelected());
        jCBUserVIEW_OWNDEPARTMENT.setSelected(jCBUserVIEW_ALL.isSelected());
        jCBUserVIEW_EMPLOYEEUNDER.setSelected(jCBUserVIEW_ALL.isSelected());
        
    }//GEN-LAST:event_jCBUserVIEW_ALLActionPerformed

    private void jCBUserCHANGE_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUserCHANGE_ALLActionPerformed
        jCBUserCHANGE_DELET.setSelected(jCBUserCHANGE_ALL.isSelected());
        jCBUserCHANGE_ALL.setSelected(jCBUserCHANGE_ALL.isSelected());
        jCBUserCHANGE_OWNGROUP.setSelected(jCBUserCHANGE_ALL.isSelected());
        jCBUserCHANGE_OWNDEPARTMENT.setSelected(jCBUserCHANGE_ALL.isSelected());
        jCBUserCHANGE_EMPLOYEEUNDER.setSelected(jCBUserCHANGE_ALL.isSelected());
        
    }//GEN-LAST:event_jCBUserCHANGE_ALLActionPerformed

    private void jCBUserDELET_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUserDELET_ALLActionPerformed
        jCBUserDELET_ALL.setSelected(jCBUserDELET_ALL.isSelected());
        jCBUserDELET_DELET.setSelected(jCBUserDELET_ALL.isSelected());
        jCBUserDELET_OWNGROUP.setSelected(jCBUserDELET_ALL.isSelected());
        jCBUserDELET_OWNDEPARTMENT.setSelected(jCBUserDELET_ALL.isSelected());
        jCBUserDELET_EMPLOYEEUNDER.setSelected(jCBUserDELET_ALL.isSelected());
        
    }//GEN-LAST:event_jCBUserDELET_ALLActionPerformed

    private void jCBUserSELECT_ALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUserSELECT_ALLActionPerformed
        jCBUserSELECT_ALL.setSelected(jCBUserSELECT_ALL.isSelected());
        jCBUserSELECT_DELET.setSelected(jCBUserSELECT_ALL.isSelected());
        jCBUserSELECT_OWNGROUP.setSelected(jCBUserSELECT_ALL.isSelected());
        jCBUserSELECT_OWNDEPARTMENT.setSelected(jCBUserSELECT_ALL.isSelected());
        jCBUserSELECT_EMPLOYEEUNDER.setSelected(jCBUserSELECT_ALL.isSelected());
    }//GEN-LAST:event_jCBUserSELECT_ALLActionPerformed

    private void jTFPrivilegeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFPrivilegeNameFocusLost
        if(jTFPrivilegeName.getText().equals("")) 
            jTFPrivilegeName.setText("Введите имя");
    }//GEN-LAST:event_jTFPrivilegeNameFocusLost

    private void jTFPrivilegeNameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFPrivilegeNameMousePressed
        if(jTFPrivilegeName.getText().equals("Введите имя")) 
            jTFPrivilegeName.setText("");
    }//GEN-LAST:event_jTFPrivilegeNameMousePressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        closeThis();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCBMedicalBookDELET_DELETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookDELET_DELETActionPerformed
        jCBMedicalBookVIEW_DELET.setSelected(jCBMedicalBookDELET_DELET.isSelected());
        jCBMedicalBookCHANGE_DELET.setSelected(jCBMedicalBookDELET_DELET.isSelected());
    }//GEN-LAST:event_jCBMedicalBookDELET_DELETActionPerformed

    private void jCBMedicalBookCHANGE_DELETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookCHANGE_DELETActionPerformed
        jCBMedicalBookVIEW_DELET.setSelected(jCBMedicalBookCHANGE_DELET.isSelected());
    }//GEN-LAST:event_jCBMedicalBookCHANGE_DELETActionPerformed

    private void jCBMedicalBookDELET_OWNGROUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookDELET_OWNGROUPActionPerformed
        jCBMedicalBookVIEW_OWNGROUP.setSelected(jCBMedicalBookDELET_OWNGROUP.isSelected());
        jCBMedicalBookCHANGE_OWNGROUP.setSelected(jCBMedicalBookDELET_OWNGROUP.isSelected());     
    }//GEN-LAST:event_jCBMedicalBookDELET_OWNGROUPActionPerformed

    private void jCBMedicalBookCHANGE_OWNGROUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookCHANGE_OWNGROUPActionPerformed
        jCBMedicalBookVIEW_OWNGROUP.setSelected(jCBMedicalBookCHANGE_OWNGROUP.isSelected());     
    }//GEN-LAST:event_jCBMedicalBookCHANGE_OWNGROUPActionPerformed

    private void jCBInstitutionDELET_INSTITUTIONUNDERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionDELET_INSTITUTIONUNDERActionPerformed
//        if (jCBInstitutionDELET_INSTITUTIONUNDER.isSelected())
//            jCBInstitutionCHANGE_INSTITUTIONUNDER.setSelected(true);  
//        else 
//            jCBInstitutionCHANGE_INSTITUTIONUNDER.setSelected(false);
    }//GEN-LAST:event_jCBInstitutionDELET_INSTITUTIONUNDERActionPerformed

    private void jCBInstitutionDELET_OWNGROUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionDELET_OWNGROUPActionPerformed
//        if (jCBInstitutionDELET_OWNGROUP.isSelected())
//            jCBInstitutionCHANGE_OWNGROUP.setSelected(true);  
//        else 
//            jCBInstitutionCHANGE_OWNGROUP.setSelected(false);
    }//GEN-LAST:event_jCBInstitutionDELET_OWNGROUPActionPerformed

    private void jCBInstitutionDELET_CLOSEDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionDELET_CLOSEDActionPerformed
//        if (jCBInstitutionDELET_CLOSED.isSelected())
//            jCBInstitutionCHANGE_CLOSED.setSelected(true);  
//        else 
//            jCBInstitutionCHANGE_CLOSED.setSelected(false);
    }//GEN-LAST:event_jCBInstitutionDELET_CLOSEDActionPerformed

    private void jCBInstitutionCHANGE_ALLItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBInstitutionCHANGE_ALLItemStateChanged
        jCBInstitutionCHANGE_CLOSED.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
        jCBInstitutionCHANGE_ALL.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
        jCBInstitutionCHANGE_OWNGROUP.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
        jCBInstitutionCHANGE_INSTITUTIONUNDER.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
        jCheckBox168.setSelected(jCBInstitutionCHANGE_ALL.isSelected());
    }//GEN-LAST:event_jCBInstitutionCHANGE_ALLItemStateChanged

    private void jCBInstitutionDELET_ALLItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBInstitutionDELET_ALLItemStateChanged
        jCBInstitutionDELET_ALL.setSelected(jCBInstitutionDELET_ALL.isSelected());
        jCBInstitutionDELET_CLOSED.setSelected(jCBInstitutionDELET_ALL.isSelected());
        jCBInstitutionDELET_OWNGROUP.setSelected(jCBInstitutionDELET_ALL.isSelected());
        jCBInstitutionDELET_INSTITUTIONUNDER.setSelected(jCBInstitutionDELET_ALL.isSelected());
        jCheckBox169.setSelected(jCBInstitutionDELET_ALL.isSelected());

        jCBInstitutionCHANGE_ALL.setSelected(jCBInstitutionDELET_ALL.isSelected());  
    }//GEN-LAST:event_jCBInstitutionDELET_ALLItemStateChanged

    private void jCBInstitutionDELET_CLOSEDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBInstitutionDELET_CLOSEDItemStateChanged
        jCBInstitutionCHANGE_CLOSED.setSelected(jCBInstitutionDELET_CLOSED.isSelected());  
    }//GEN-LAST:event_jCBInstitutionDELET_CLOSEDItemStateChanged

    private void jCBInstitutionDELET_OWNGROUPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBInstitutionDELET_OWNGROUPItemStateChanged
        jCBInstitutionCHANGE_OWNGROUP.setSelected(jCBInstitutionDELET_OWNGROUP.isSelected());  
    }//GEN-LAST:event_jCBInstitutionDELET_OWNGROUPItemStateChanged

    private void jCBInstitutionDELET_INSTITUTIONUNDERItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBInstitutionDELET_INSTITUTIONUNDERItemStateChanged
        jCBInstitutionCHANGE_INSTITUTIONUNDER.setSelected(jCBInstitutionDELET_INSTITUTIONUNDER.isSelected());  
    }//GEN-LAST:event_jCBInstitutionDELET_INSTITUTIONUNDERItemStateChanged

    private void jCBInstitutionCHANGE_CLOSEDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionCHANGE_CLOSEDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBInstitutionCHANGE_CLOSEDActionPerformed

    private void jCBInstitutionCHANGE_CLOSEDCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jCBInstitutionCHANGE_CLOSEDCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBInstitutionCHANGE_CLOSEDCaretPositionChanged

    private void jCBInstitutionCHANGE_OWNGROUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBInstitutionCHANGE_OWNGROUPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBInstitutionCHANGE_OWNGROUPActionPerformed

    private void jCBInstitutionCHANGE_OWNGROUPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBInstitutionCHANGE_OWNGROUPItemStateChanged
        jCBInstitutionCHANGE_INSTITUTIONUNDER.setSelected(jCBInstitutionCHANGE_OWNGROUP.isSelected());  
    }//GEN-LAST:event_jCBInstitutionCHANGE_OWNGROUPItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBChangeOrAddPrivilegeUser;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCB;
    private javax.swing.JCheckBox jCBEmployeeCHANGE_ALL;
    private javax.swing.JCheckBox jCBEmployeeCHANGE_DELET;
    private javax.swing.JCheckBox jCBEmployeeCHANGE_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBEmployeeCHANGE_LINESTAFF;
    private javax.swing.JCheckBox jCBEmployeeCHANGE_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBEmployeeCHANGE_OWNGROUP;
    private javax.swing.JCheckBox jCBEmployeeDELET_ALL;
    private javax.swing.JCheckBox jCBEmployeeDELET_DELET;
    private javax.swing.JCheckBox jCBEmployeeDELET_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBEmployeeDELET_LINESTAFF;
    private javax.swing.JCheckBox jCBEmployeeDELET_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBEmployeeDELET_OWNGROUP;
    private javax.swing.JCheckBox jCBEmployeeSELECT_ALL;
    private javax.swing.JCheckBox jCBEmployeeSELECT_DELET;
    private javax.swing.JCheckBox jCBEmployeeSELECT_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBEmployeeSELECT_LINESTAFF;
    private javax.swing.JCheckBox jCBEmployeeSELECT_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBEmployeeSELECT_OWNGROUP;
    private javax.swing.JCheckBox jCBEmployeeVIEW_ALL;
    private javax.swing.JCheckBox jCBEmployeeVIEW_DELET;
    private javax.swing.JCheckBox jCBEmployeeVIEW_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBEmployeeVIEW_LINESTAFF;
    private javax.swing.JCheckBox jCBEmployeeVIEW_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBEmployeeVIEW_OWNGROUP;
    private javax.swing.JCheckBox jCBInstitutionCHANGE_ALL;
    private javax.swing.JCheckBox jCBInstitutionCHANGE_CLOSED;
    private javax.swing.JCheckBox jCBInstitutionCHANGE_INSTITUTIONUNDER;
    private javax.swing.JCheckBox jCBInstitutionCHANGE_OWNGROUP;
    private javax.swing.JCheckBox jCBInstitutionDELET_ALL;
    private javax.swing.JCheckBox jCBInstitutionDELET_CLOSED;
    private javax.swing.JCheckBox jCBInstitutionDELET_INSTITUTIONUNDER;
    private javax.swing.JCheckBox jCBInstitutionDELET_OWNGROUP;
    private javax.swing.JCheckBox jCBInstitutionSELECT_ALL;
    private javax.swing.JCheckBox jCBInstitutionSELECT_CLOSED;
    private javax.swing.JCheckBox jCBInstitutionSELECT_INSTITUTIONUNDER;
    private javax.swing.JCheckBox jCBInstitutionSELECT_OWNGROUP;
    private javax.swing.JCheckBox jCBInstitutionVIEW_ALL;
    private javax.swing.JCheckBox jCBInstitutionVIEW_CLOSED;
    private javax.swing.JCheckBox jCBInstitutionVIEW_INSTITUTIONUNDER;
    private javax.swing.JCheckBox jCBInstitutionVIEW_OWNGROUP;
    private javax.swing.JCheckBox jCBMedicalBookCHANGE_ALL;
    private javax.swing.JCheckBox jCBMedicalBookCHANGE_DELET;
    private javax.swing.JCheckBox jCBMedicalBookCHANGE_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBMedicalBookCHANGE_LINESTAFF;
    private javax.swing.JCheckBox jCBMedicalBookCHANGE_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBMedicalBookCHANGE_OWNGROUP;
    private javax.swing.JCheckBox jCBMedicalBookDELET_ALL;
    private javax.swing.JCheckBox jCBMedicalBookDELET_DELET;
    private javax.swing.JCheckBox jCBMedicalBookDELET_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBMedicalBookDELET_LINESTAFF;
    private javax.swing.JCheckBox jCBMedicalBookDELET_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBMedicalBookDELET_OWNGROUP;
    private javax.swing.JCheckBox jCBMedicalBookSELECT_ALL;
    private javax.swing.JCheckBox jCBMedicalBookSELECT_DELET;
    private javax.swing.JCheckBox jCBMedicalBookSELECT_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBMedicalBookSELECT_LINESTAFF;
    private javax.swing.JCheckBox jCBMedicalBookSELECT_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBMedicalBookSELECT_OWNGROUP;
    private javax.swing.JCheckBox jCBMedicalBookVIEW_ALL;
    private javax.swing.JCheckBox jCBMedicalBookVIEW_DELET;
    private javax.swing.JCheckBox jCBMedicalBookVIEW_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBMedicalBookVIEW_LINESTAFF;
    private javax.swing.JCheckBox jCBMedicalBookVIEW_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBMedicalBookVIEW_OWNGROUP;
    private javax.swing.JCheckBox jCBSettingsDEPARTMENT_CHANGE;
    private javax.swing.JCheckBox jCBSettingsGROUP_CHANGE;
    private javax.swing.JCheckBox jCBSettingsPOSITION_CHANGE;
    private javax.swing.JCheckBox jCBSettingsPRIVILEGE_CHANGE;
    private javax.swing.JCheckBox jCBUserCHANGE_ALL;
    private javax.swing.JCheckBox jCBUserCHANGE_DELET;
    private javax.swing.JCheckBox jCBUserCHANGE_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBUserCHANGE_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBUserCHANGE_OWNGROUP;
    private javax.swing.JCheckBox jCBUserDELET_ALL;
    private javax.swing.JCheckBox jCBUserDELET_DELET;
    private javax.swing.JCheckBox jCBUserDELET_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBUserDELET_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBUserDELET_OWNGROUP;
    private javax.swing.JCheckBox jCBUserSELECT_ALL;
    private javax.swing.JCheckBox jCBUserSELECT_DELET;
    private javax.swing.JCheckBox jCBUserSELECT_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBUserSELECT_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBUserSELECT_OWNGROUP;
    private javax.swing.JCheckBox jCBUserVIEW_ALL;
    private javax.swing.JCheckBox jCBUserVIEW_DELET;
    private javax.swing.JCheckBox jCBUserVIEW_EMPLOYEEUNDER;
    private javax.swing.JCheckBox jCBUserVIEW_OWNDEPARTMENT;
    private javax.swing.JCheckBox jCBUserVIEW_OWNGROUP;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox123;
    private javax.swing.JCheckBox jCheckBox124;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox149;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox150;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox167;
    private javax.swing.JCheckBox jCheckBox168;
    private javax.swing.JCheckBox jCheckBox169;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox170;
    private javax.swing.JCheckBox jCheckBox171;
    private javax.swing.JCheckBox jCheckBox172;
    private javax.swing.JCheckBox jCheckBox173;
    private javax.swing.JCheckBox jCheckBox174;
    private javax.swing.JCheckBox jCheckBox175;
    private javax.swing.JCheckBox jCheckBox176;
    private javax.swing.JCheckBox jCheckBox177;
    private javax.swing.JCheckBox jCheckBox178;
    private javax.swing.JCheckBox jCheckBox179;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox180;
    private javax.swing.JCheckBox jCheckBox181;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox27;
    private javax.swing.JCheckBox jCheckBox28;
    private javax.swing.JCheckBox jCheckBox29;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox30;
    private javax.swing.JCheckBox jCheckBox31;
    private javax.swing.JCheckBox jCheckBox32;
    private javax.swing.JCheckBox jCheckBox33;
    private javax.swing.JCheckBox jCheckBox34;
    private javax.swing.JCheckBox jCheckBox35;
    private javax.swing.JCheckBox jCheckBox36;
    private javax.swing.JCheckBox jCheckBox37;
    private javax.swing.JCheckBox jCheckBox38;
    private javax.swing.JCheckBox jCheckBox39;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox40;
    private javax.swing.JCheckBox jCheckBox41;
    private javax.swing.JCheckBox jCheckBox42;
    private javax.swing.JCheckBox jCheckBox43;
    private javax.swing.JCheckBox jCheckBox44;
    private javax.swing.JCheckBox jCheckBox45;
    private javax.swing.JCheckBox jCheckBox46;
    private javax.swing.JCheckBox jCheckBox47;
    private javax.swing.JCheckBox jCheckBox48;
    private javax.swing.JCheckBox jCheckBox49;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox50;
    private javax.swing.JCheckBox jCheckBox51;
    private javax.swing.JCheckBox jCheckBox52;
    private javax.swing.JCheckBox jCheckBox53;
    private javax.swing.JCheckBox jCheckBox54;
    private javax.swing.JCheckBox jCheckBox55;
    private javax.swing.JCheckBox jCheckBox56;
    private javax.swing.JCheckBox jCheckBox57;
    private javax.swing.JCheckBox jCheckBox58;
    private javax.swing.JCheckBox jCheckBox59;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox60;
    private javax.swing.JCheckBox jCheckBox61;
    private javax.swing.JCheckBox jCheckBox62;
    private javax.swing.JCheckBox jCheckBox63;
    private javax.swing.JCheckBox jCheckBox64;
    private javax.swing.JCheckBox jCheckBox65;
    private javax.swing.JCheckBox jCheckBox66;
    private javax.swing.JCheckBox jCheckBox67;
    private javax.swing.JCheckBox jCheckBox68;
    private javax.swing.JCheckBox jCheckBox69;
    private javax.swing.JCheckBox jCheckBox70;
    private javax.swing.JCheckBox jCheckBox71;
    private javax.swing.JCheckBox jCheckBox72;
    private javax.swing.JCheckBox jCheckBox73;
    private javax.swing.JCheckBox jCheckBox74;
    private javax.swing.JCheckBox jCheckBox75;
    private javax.swing.JCheckBox jCheckBox76;
    private javax.swing.JCheckBox jCheckBox77;
    private javax.swing.JCheckBox jCheckBox78;
    private javax.swing.JCheckBox jCheckBox79;
    private javax.swing.JCheckBox jCheckBox80;
    private javax.swing.JCheckBox jCheckBox81;
    private javax.swing.JCheckBox jCheckBox82;
    private javax.swing.JCheckBox jCheckBox83;
    private javax.swing.JCheckBox jCheckBox84;
    private javax.swing.JCheckBox jCheckBox85;
    private javax.swing.JCheckBox jCheckBox86;
    private javax.swing.JCheckBox jCheckBox87;
    private javax.swing.JCheckBox jCheckBox88;
    private javax.swing.JCheckBox jCheckBox89;
    private javax.swing.JCheckBox jCheckBox90;
    private javax.swing.JCheckBox jCheckBox91;
    private javax.swing.JCheckBox jCheckBox92;
    private javax.swing.JCheckBox jCheckBox93;
    private javax.swing.JCheckBox jCheckBox94;
    private javax.swing.JCheckBox jCheckBox95;
    private javax.swing.JCheckBox jCheckBox96;
    private javax.swing.JCheckBox jCheckBox97;
    private javax.swing.JCheckBox jCheckBox98;
    private javax.swing.JCheckBox jCheckBox99;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTextField jTFPrivilegeName;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
