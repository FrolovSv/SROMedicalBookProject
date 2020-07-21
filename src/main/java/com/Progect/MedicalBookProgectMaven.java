package com.Progect;

import com.Fraims.AddOrChange.JIFUserPrivilegeChange;
import com.Class.*;
//import com.Class.openUserPrivilege.PrivilegeDefault;
import com.Fraims.AddOrChange.*;
import com.Fraims.Bases.*;
import com.SQL.SQLQuery;
import com.notUse.MainInternalFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.InternalFrameUI;
import lombok.Getter;
import lombok.Setter;
//import javax.swing.table.DefaultTableModel;
//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.cfg.Configuration;

//@Data
//@EqualsAndHashCode(callSuper=false)
public class MedicalBookProgectMaven extends javax.swing.JFrame {

    //private Integer AuthorizedUserId = 45;
    private @Getter Employee AuthorizedUser = new Employee();
    private @Getter UserPrivilege UserPrivilegeLogin = new UserPrivilege();
    private @Getter @Setter UserLogin UserRoodLogin = new UserLogin();
    private @Getter @Setter TypeUnit TypeUnit = new TypeUnit();
    //private String RegionName = "ЮАО";
    private Integer RegionId = 6;    

    public Integer getRegionId() {        return RegionId;    }
    
    private @Getter Integer SelectedNumMedBook = -1;
    private @Getter Integer SelectedEmployeeId = -1;
    private @Getter Integer SelectedInstitutionId = -1;
    private @Getter @Setter Integer SelectedUsGroupId = -1;
    private @Getter @Setter Integer SelectedUsDepartmentId = -1;
    private @Getter @Setter Integer SelectedUsPositionId = -1;
    private @Getter @Setter Integer SelectedUserPrivilegeId = -1;
    private @Getter @Setter Integer SelectedUserRootId = -1;
    
    private @Getter @Setter JIFEmployeesBase JIFEmployeesBase;
    private @Getter @Setter JIFMedBookAddorChange JIFMedBookAddorChange;
    private @Getter @Setter JIFMedBookBase JIFMedBookBase;
    private @Getter @Setter JIFEmployeeMedlBookAddorChange JIFEmployeeMedlBookAddorChange;
    private @Getter @Setter JIFEmployeeAddOrChange JIFEmployeeAddOrChange;
    private @Getter @Setter JIFInstitutionBase JIFInstitutionBase;
    private @Getter @Setter JIFInstitutionAddOrChange JIFInstitutionAddOrChange;
    private @Getter @Setter JIFUsGroup JIFUsGroup;
    private @Getter @Setter JIFUserPrivilegeChange JIFUserPrivilegeChange;
    private @Getter @Setter JIFUserPrivilegeBase JIFUserPrivilegeBase;
    private @Getter @Setter JIFUsersRootBase JIFUsersRootBase;
    
    private @Getter String StringPrivMedicalBook = "";
    private @Getter String StringPrivEmployee = "";
    private @Getter String StringPrivInstitution = "";
    private @Getter String StringPrivSettings = "";
    private @Getter String StringPrivUser = ""; 
    
    private @Getter TreeMap<Integer,UsDepartment> UsDepartmentTM ;
    private @Getter TreeMap<Integer,UsGroup> UsGroupTM;
    private @Getter TreeMap<Integer,UsPosition> UsPositionTM;
    private @Getter TreeMap<Integer,TypeUnit> TypeUnitTM;
    
    public void setSelectedInstitutionId(Integer SelectedInstitutionId){
        this.SelectedInstitutionId = SelectedInstitutionId;
        jMIInstitutionChange.setEnabled(SelectedInstitutionId > 0 ? true : false);
    }
    
    public void setSelectedNumMedBook(Integer SelectedNumMedBook){
        this.SelectedNumMedBook = SelectedNumMedBook;
        jMIMedicalBookChange.setEnabled(SelectedNumMedBook > 0 ? true : false);
    }
    
    public void setSelectedEmployeeId(Integer SelectedEmployeeId){
        this.SelectedEmployeeId = SelectedEmployeeId;
        jMIEmployeeChange.setEnabled(SelectedEmployeeId > 0 ? true : false);
    }

    // =======================  Геттеры и сеттеры для глобальных для панели =======================
    public void setjDesktopPane1(JDesktopPane jDesktopPane1) {this.jDesktopPane1 = jDesktopPane1;}
    public JDesktopPane getjDesktopPane1() {return jDesktopPane1;}
    
    public void setAuthorizedUser(Integer AuthorizedUserId){
        this.AuthorizedUser = new SQLQuery<>(AuthorizedUser).Read(AuthorizedUserId);
    }
    public void closeThis(){
        System.exit(0);
    }
    
    // ======================= Объявление метода =======================
    public MedicalBookProgectMaven() {            
        initComponents();        
        jMIInstitutionChange.setEnabled(false);
        jMIEmployeeChange.setEnabled(false);
        jMIMedicalBookChange.setEnabled(false);        
        jMenuItem15.setEnabled(false);
        jMenuItem18.setEnabled(false);      
        jMIUserRootChange.setEnabled(false);
        jMIUserPrivilegeChange.setEnabled(false);
                
        UserRoodLogin = new SQLQuery<>(UserRoodLogin).Read(5);
        AuthorizedUser = new SQLQuery<>(AuthorizedUser).Read(UserRoodLogin.getEmployeeId());        
        UserPrivilegeLogin = new SQLQuery<>(UserPrivilegeLogin).Read(UserRoodLogin.getUserPrivilegeId());   

        this.setTitle("СРО. Программа по работе с базой ЛМК. Округ: " + "ЮАО" 
                + ". Пользователь: " + AuthorizedUser.getSurnameInitials(AuthorizedUser.getLastName(), AuthorizedUser.getName(), AuthorizedUser.getPatronymic()) 
                + ". Должность: " + AuthorizedUser.getUsPositionName()
                + ". Права доступа: "+ UserPrivilegeLogin.getName());
        
        UsDepartmentTM = new SQLQuery<>(new UsDepartment()).Read(-1, -1);
        UsGroupTM = new SQLQuery<>(new UsGroup()).Read(-1, -1);        
        UsPositionTM = new SQLQuery<>(new UsPosition()).Read(-1, -1);
        TypeUnitTM = new SQLQuery<>(new TypeUnit()).Read(-1, -1);
        
        jLabel1.setText("Предварительная загрузка данных прошла успешно.");
        
        ApplicationOfAccessRights();
        
        
        
        
        //openForm(JIFMedBookBase);
//        setVisible(false);
//        NewJDialog preload = new NewJDialog(this,true,this);
//        //preload.setUndecorated(true);
//        preload.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
//        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
//        preload.setSize(new Dimension(434, 416));
//        preload.setLocation(sSize.width/2-preload.getWidth()/2, sSize.height/2-preload.getHeight()/2);
//        preload.setVisible(true);
//        String str = "";
//        Field[] allFields = AuthorizationHistory.class.getDeclaredFields();
//        String[] Names = new String[allFields.length];
//        int i = 0;
//        for (Field field : allFields)
//        {
//            if (i==0) str = str.concat(field.getValueByTreeMap() + " ");
//            else 
//                str = str.concat(", " + field.getValueByTreeMap() + " ");
//            i++;
//        }
//        System.out.println(str);
//        SQLConnection Con = new SQLConnection();
//        Con.getConnection();
//        //UsGroupTM = new UsGroup().SQLSelectDataRequest(Con);
//        //UsDepartmentTM = new UsDepartment().SQLSelectDataRequest(Con);
//        //UsPositionTM = new UsPosition().SQLSelectDataRequest(Con);
//        Con.closeConnection();
//        System.out.println(toString());
//        EmployeeTM = new SQLQuery<>(new Employee()).Read(10,-1);
//        System.out.println(EmployeeTM.toString()); 
//        Employee empl = new SQLQuery<>(new Employee()).Read(10,4).get(4);
//        System.out.println(empl.toString());
        // =============== тестовая загрузка окон ===============
        //openAddChangeMedicalBook();
        //openAddOrChangeEmployee();
        //openBaseMedicalBook();
        //openJIFBase(JIFEmployeesBase, statusFrame.VIEW, null);
        //this.setEmployee(45);
        //openAddChangeEmployee(); 
        //openAddChangeEmployeeAndMedicalBook();
        SelectedNumMedBook = 332211;
        openAddChangeMedicalBook();
    }
    
    public void ApplicationOfAccessRights(){
        UserPrivilegeLogin = new SQLQuery<>(UserPrivilegeLogin).Read(UserRoodLogin.getUserPrivilegeId());    
        UserRoodLogin = new SQLQuery<>(UserRoodLogin).Read(5); 
        
        if (!UserPrivilegeLogin.getPrivMedicalBook().isEmpty())
            StringPrivMedicalBook = enumToString(UserPrivilegeLogin.getPrivMedicalBook(), ",");
        if (!UserPrivilegeLogin.getPrivEmployee().isEmpty())
            StringPrivEmployee = enumToString(UserPrivilegeLogin.getPrivEmployee(), ",");
        if (!UserPrivilegeLogin.getPrivInstitution().isEmpty())
            StringPrivInstitution = enumToString(UserPrivilegeLogin.getPrivInstitution(), ",");
        if (!UserPrivilegeLogin.getPrivSettings().isEmpty())
            StringPrivSettings = enumToString(UserPrivilegeLogin.getPrivSettings(), ",");
        if (!UserPrivilegeLogin.getPrivUser().isEmpty())
            StringPrivUser = enumToString(UserPrivilegeLogin.getPrivUser(), ",");
        
        jMMedicalBook.setEnabled(!UserPrivilegeLogin.getPrivMedicalBook().isEmpty());
        jMIMedicalBookBase.setEnabled(StringPrivMedicalBook.contains("VIEW")||StringPrivMedicalBook.contains("CHANGE"));        
        jMIMedicalBookAdd.setEnabled(StringPrivMedicalBook.contains("CHANGE"));
        //jMIMedicalBookChange.setEnabled(StringPrivMedicalBook.contains("CHANGE"));   
        
        jMEmployee.setEnabled(!UserPrivilegeLogin.getPrivEmployee().isEmpty());
        jMIEmployeeBase.setEnabled(StringPrivEmployee.contains("VIEW")||StringPrivEmployee.contains("CHANGE"));
        jMIEmployeeAndMedicalBookAdd.setEnabled(StringPrivMedicalBook.contains("CHANGE") && StringPrivEmployee.contains("CHANGE"));
        jMIEmployeeAdd.setEnabled(StringPrivEmployee.contains("CHANGE"));
        //jMIEmployeeChange.setEnabled(StringPrivEmployee.contains("CHANGE"));
        
        jMInstitution.setEnabled(!UserPrivilegeLogin.getPrivInstitution().isEmpty());
        jMIInstitutionBase.setEnabled(StringPrivInstitution.contains("VIEW")||StringPrivMedicalBook.contains("CHANGE"));        
        jMIInstitutionAdd.setEnabled(StringPrivInstitution.contains("CHANGE"));
        //jMIInstitutionChange.setEnabled(StringPrivInstitution.contains("CHANGE"));   
        
        jMMailing.setEnabled(StringPrivSettings.contains("MAILING"));
        
        jMSettings.setEnabled(!UserPrivilegeLogin.getPrivSettings().isEmpty());
        jMSettingsGroup.setEnabled(StringPrivSettings.contains("GROUP"));
        jMSettingsDepartment.setEnabled(StringPrivSettings.contains("DEPARTMENT"));
        jMSettingsPosition.setEnabled(StringPrivSettings.contains("POSITION"));
        jMSettingsRegion.setEnabled(StringPrivSettings.contains("REGION"));
        
        jMSettingsMedicalBook.setEnabled(false);
        
        jMUserRoot.setEnabled(!UserPrivilegeLogin.getPrivUser().isEmpty());
        jMIUserRootBase.setEnabled(StringPrivUser.contains("VIEW"));        
        jMIUserRootAdd.setEnabled(StringPrivUser.contains("CHANGE"));
        jMIUserRootChange.setEnabled(StringPrivUser.contains("CHANGE")); 
        
        jMIUserPrivilegeBase.setEnabled(!StringPrivUser.contains("NULL"));
        jMIUserPrivilegeAdd.setEnabled(!StringPrivUser.contains("NULL"));
        //jMIUserPrivilegeChange.setEnabled(!StringPrivUser.contains("NULL"));
        jLabel1.setText("Устанвка прав доступа прошла успешно");
   }   


    // ======================= открытие рабочих окон =======================
    public void openBaseMedicalBook(statusFrame Status, InternalFrameUI JIFUI){     
        jLabel1.setText("Открытие базы медицинских книжек");
        if (StringPrivMedicalBook.contains("VIEW") || StringPrivMedicalBook.contains("CHANGE")){
            if (JIFMedBookBase==null){
                if (JIFUI==null)
                    JIFMedBookBase = new JIFMedBookBase(this).view();
                else 
                    switch (Status) {
                        case VIEW: JIFMedBookBase = new JIFMedBookBase(this).view(); break;
                        case CHOISE: JIFMedBookBase = new JIFMedBookBase(this).choise(JIFUI); break;
                        default: JIFMedBookBase = new JIFMedBookBase(this).view(); break;
                    }                    
                Dimension JIFMedBookBaseSize = new Dimension(1100, 500);
                jDesktopPane1.add(JIFMedBookBase,jDesktopPane1.getAllFrames().length+1);
                JIFMedBookBase = (JIFMedBookBase) setLocationAndSizeWindow(JIFMedBookBase,JIFMedBookBaseSize); 
            } else{
                try {
                    JIFMedBookBase.setSelected(true);
                    jDesktopPane1.setSelectedFrame(JIFMedBookBase);
                    JIFMedBookBase.setIcon(false);
                    if (JIFUI==null)
                        JIFMedBookBase = new JIFMedBookBase(this).view();
                    else
                        switch (Status) {
                            case VIEW: JIFMedBookBase.view(); break;
                            case CHOISE: JIFMedBookBase.choise(JIFUI);break;
                            default: JIFMedBookBase.view(); break;
                        }
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.BaseMedicalBook() - " + ex.getMessage());
                }             
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
    
    public void openBaseUserRoot() {
        jLabel1.setText("Открытие базы прав пользователей");
        if (StringPrivUser.contains("VIEW")){
            if (JIFUsersRootBase== null) {
                JIFUsersRootBase = new JIFUsersRootBase(this);
                Dimension JIFUsersBaseSize = new Dimension(1100, 500);
                jDesktopPane1.add(JIFUsersRootBase, jDesktopPane1.getAllFrames().length + 1);
                JIFUsersRootBase = (JIFUsersRootBase) setLocationAndSizeWindow(JIFUsersRootBase, JIFUsersBaseSize);
            } else {
                try {
                    JIFMedBookBase.setSelected(true);                    
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openBaseMedicalBook()");
                }
                JIFMedBookBase.setVisible(true);
                jDesktopPane1.setSelectedFrame(JIFUsersRootBase);
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
    
    public void openBaseEmployees(statusFrame Status, InternalFrameUI JIFUI){
        jLabel1.setText("Открытие базы сотруднико");
        if (StringPrivEmployee.contains("VIEW") || StringPrivEmployee.contains("CHANGE")){
            if (JIFEmployeesBase==null){
                if (JIFUI==null)
                    JIFEmployeesBase = new JIFEmployeesBase(this).view();
                else 
                    switch (Status) {
                        case VIEW: JIFEmployeesBase = new JIFEmployeesBase(this).view(); break;
                        case CHOISE: JIFEmployeesBase = new JIFEmployeesBase(this).choise(JIFUI); break;
                        default: JIFEmployeesBase = new JIFEmployeesBase(this).view(); break;
                    }                    
                Dimension JIFEmployeesBaseSize = new Dimension(1100, 500);
                jDesktopPane1.add(JIFEmployeesBase,jDesktopPane1.getAllFrames().length+1);
                jDesktopPane1.setSelectedFrame(JIFEmployeesBase);
                JIFEmployeesBase = (JIFEmployeesBase) setLocationAndSizeWindow(JIFEmployeesBase,JIFEmployeesBaseSize); 
            } else{
                try {
                    JIFEmployeesBase.setSelected(true);
                    jDesktopPane1.setSelectedFrame(JIFEmployeesBase);
                    JIFEmployeesBase.setIcon(false);
                    if (JIFUI==null)
                        JIFEmployeesBase = new JIFEmployeesBase(this).view();
                    else
                        switch (Status) {
                            case VIEW: JIFEmployeesBase.view(); break;
                            case CHOISE: JIFEmployeesBase.choise(JIFUI);break;
                            default: JIFEmployeesBase.view(); break;
                        }
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openBaseEmployees() - " + ex.getMessage());
                }             
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
    
    public void openUserPrivilege(statusFrame Status, InternalFrameUI JIFUI){
        jLabel1.setText("Открытие базы прав пользователей");
        if (StringPrivSettings.contains("PRIVILEGE") || StringPrivEmployee.contains("PRIVILEGE")){
            if (JIFUserPrivilegeBase==null){
                if (JIFUI==null)
                    JIFUserPrivilegeBase = new JIFUserPrivilegeBase(this).View();
                else 
                    switch (Status) {
                        case VIEW: JIFUserPrivilegeBase = new JIFUserPrivilegeBase(this).View(); break;
                        case CHOISE: JIFUserPrivilegeBase = new JIFUserPrivilegeBase(this).Choise(JIFUI); break;
                        default: JIFUserPrivilegeBase = new JIFUserPrivilegeBase(this).View(); break;
                    }                    
                Dimension JIFUserPrivilegeBaseSize = new Dimension(1100, 500);
                jDesktopPane1.add(JIFUserPrivilegeBase,jDesktopPane1.getAllFrames().length+1);
                JIFUserPrivilegeBase = (JIFUserPrivilegeBase) setLocationAndSizeWindow(JIFUserPrivilegeBase,JIFUserPrivilegeBaseSize); 
            } else{
                try {
                    JIFUserPrivilegeBase.setSelected(true);
                    jDesktopPane1.setSelectedFrame(JIFUserPrivilegeBase);
                    JIFUserPrivilegeBase.setIcon(false);
                    if (JIFUI==null)
                        JIFUserPrivilegeBase = new JIFUserPrivilegeBase(this).View();
                    else
                        switch (Status) {
                            case VIEW: JIFUserPrivilegeBase.View(); break;
                            case CHOISE: JIFUserPrivilegeBase.Choise(JIFUI);break;
                            default: JIFUserPrivilegeBase.View(); break;
                        }
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openBaseEmployees() - " + ex.getMessage());
                }             
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
    
    public void openBaseInstitutions(statusFrame Status, InternalFrameUI JIFUI){
        jLabel1.setText("Открытие базы учереждений");
        if (StringPrivInstitution.contains("VIEW") || StringPrivInstitution.contains("CHANGE")){
            if (JIFInstitutionBase==null){
                if (JIFUI==null)
                    JIFInstitutionBase = new JIFInstitutionBase(this).view();
                else 
                    switch (Status) {
                        case VIEW: JIFInstitutionBase = new JIFInstitutionBase(this).view(); break;
                        case CHOISE: JIFInstitutionBase = new JIFInstitutionBase(this).choise(JIFUI); break;
                        case CHOISE_MAIN: JIFInstitutionBase = new JIFInstitutionBase(this).choiseMain(JIFUI); break;
                        default: JIFInstitutionBase = new JIFInstitutionBase(this).view(); break;
                    }
                    
                Dimension JIFInstitutionBaseSize = new Dimension(1100, 500);
                jDesktopPane1.add(JIFInstitutionBase,jDesktopPane1.getAllFrames().length+1);
                JIFInstitutionBase = (JIFInstitutionBase) setLocationAndSizeWindow(JIFInstitutionBase,JIFInstitutionBaseSize); 
            } else{
                try {
                    JIFInstitutionBase.setSelected(true);
                    jDesktopPane1.setSelectedFrame(JIFUserPrivilegeBase);
                    if (JIFUI==null)
                        JIFInstitutionBase = new JIFInstitutionBase(this).view();
                    else
                        switch (Status) {
                            case VIEW: JIFInstitutionBase.view(); break;
                            case CHOISE: JIFInstitutionBase.choise(JIFUI);break;
                            case CHOISE_MAIN: JIFInstitutionBase.choiseMain(JIFUI);break;
                            default: JIFInstitutionBase.view(); break;
                        }
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openBaseEmployees()");
                }
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }   
    
    // ======================= открытие рабочих окон =======================
    public void openAddChangeMedicalBook(){
        jLabel1.setText("Открытие формы измнеения данных медицинской книжки");
        if (StringPrivMedicalBook.contains("CHANGE") || StringPrivMedicalBook.contains("DELET")){
            if (JIFMedBookAddorChange==null){
                JIFMedBookAddorChange = new JIFMedBookAddorChange(this);
                Dimension JIFAddorChangeMedicalBookSize = new Dimension(710, 640);
                jDesktopPane1.add(JIFMedBookAddorChange,jDesktopPane1.getAllFrames().length+1);
                JIFMedBookAddorChange = (JIFMedBookAddorChange) setLocationAndSizeWindow(JIFMedBookAddorChange,JIFAddorChangeMedicalBookSize);  
            }else{ 
                try {
                    JIFMedBookAddorChange.setSelected(true);
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openAddChangeMedicalBook()");
                }
                JIFMedBookAddorChange.setVisible(true);
                jDesktopPane1.setSelectedFrame(JIFUserPrivilegeBase);
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
       
    public void openAddChangeEmployeeAndMedicalBook(){
        jLabel1.setText("Открытие формы добавления сотрудника и медицинской книжки");
        if ((StringPrivEmployee.contains("CHANGE") || StringPrivMedicalBook.contains("DELET")) &
                (StringPrivMedicalBook.contains("CHANGE") || StringPrivMedicalBook.contains("DELET"))){
            if (JIFEmployeeMedlBookAddorChange==null){
                JIFEmployeeMedlBookAddorChange = new JIFEmployeeMedlBookAddorChange(this);
                Dimension JIFAddorChangeEmployeeMedicalBookSize = new Dimension(730, 640);
                jDesktopPane1.add(JIFEmployeeMedlBookAddorChange,jDesktopPane1.getAllFrames().length+1);
                JIFEmployeeMedlBookAddorChange = (JIFEmployeeMedlBookAddorChange) setLocationAndSizeWindow(JIFEmployeeMedlBookAddorChange,JIFAddorChangeEmployeeMedicalBookSize);
            } else{ 
                try {
                    JIFEmployeeMedlBookAddorChange.setSelected(true);
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openAddChangeEmployeeAndMedicalBook()");
                }
                JIFEmployeeMedlBookAddorChange.setVisible(true);
                jDesktopPane1.setSelectedFrame(JIFUserPrivilegeBase);
            }
        }else{
            showDialogRestrictionOfАccessRights();
        }
    }
    
    public void openAddOrChangeEmployee() {
        jLabel1.setText("Открытие формы изменения данных сотрудника");
        if (StringPrivEmployee.contains("CHANGE") || StringPrivMedicalBook.contains("DELET")){
            if (JIFEmployeeAddOrChange == null) {
                JIFEmployeeAddOrChange = new JIFEmployeeAddOrChange(this);
                Dimension JIFAddorChangeEmployeeSize = new Dimension(730, 640);
                jDesktopPane1.add(JIFEmployeeAddOrChange, jDesktopPane1.getAllFrames().length + 1);
                JIFEmployeeAddOrChange = (JIFEmployeeAddOrChange) setLocationAndSizeWindow(JIFEmployeeAddOrChange, JIFAddorChangeEmployeeSize);
            } else {
                try {
                    JIFEmployeeAddOrChange.setSelected(true);
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openAddOrChangeEmployee()");
                }
                JIFEmployeeAddOrChange.setVisible(true);
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
    
    public void openAddOrChangeInstitution() {
        jLabel1.setText("Открытие формы изменения данных учереждения");
        if (StringPrivInstitution.contains("CHANGE") || StringPrivInstitution.contains("DELET")){
            if (JIFInstitutionAddOrChange == null) {
                JIFInstitutionAddOrChange = new JIFInstitutionAddOrChange(this);
                Dimension JIFInstitutionAddOrChangeSize = new Dimension(730, 640);
                jDesktopPane1.add(JIFInstitutionAddOrChange, jDesktopPane1.getAllFrames().length + 1);
                JIFInstitutionAddOrChange = (JIFInstitutionAddOrChange) setLocationAndSizeWindow(JIFInstitutionAddOrChange, JIFInstitutionAddOrChangeSize);
            } else {
                try {
                    JIFInstitutionAddOrChange.setSelected(true);
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openAddOrChangeEmployee()");
                }
                JIFInstitutionAddOrChange.setVisible(true);
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
    
    public void openUsGroup() {
        jLabel1.setText("Открытие базы групп пользователей");
        if (StringPrivUser.contains("VIEW")){
            if (JIFUsGroup == null) {
                JIFUsGroup = new JIFUsGroup(this);
                Dimension JIFUsGroupSize = new Dimension(1110, 500);
                jDesktopPane1.add(JIFUsGroup, jDesktopPane1.getAllFrames().length + 1);
                JIFUsGroup = (JIFUsGroup) setLocationAndSizeWindow(JIFUsGroup, JIFUsGroupSize);
            } else {
                try {
                    JIFUsGroup.setSelected(true);
                } catch (PropertyVetoException ex) {
                    System.out.println("Main.MedicalBookProgect.openAddOrChangeEmployee()");
                }
                JIFUsGroup.setVisible(true);
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
    
    public void openAddOrChangeUserPrivilege() {
        jLabel1.setText("Открытие формы изменения прав доступа сотрудников");
        if (StringPrivUser.contains("CHANGE") || StringPrivUser.contains("DELET")){
            if (JIFUserPrivilegeChange == null) {
                JIFUserPrivilegeChange = new JIFUserPrivilegeChange(this);
                Dimension JIFPrivilegeUserChangeSize = new Dimension(890, 540);
                jDesktopPane1.add(JIFUserPrivilegeChange, jDesktopPane1.getAllFrames().length + 1);
                JIFUserPrivilegeChange = (JIFUserPrivilegeChange) setLocationAndSizeWindow(JIFUserPrivilegeChange, JIFPrivilegeUserChangeSize);
            } else {
                try {
                    JIFUserPrivilegeChange.setSelected(true);
                } catch (Exception ex) {
                    System.out.println("Main.MedicalBookProgect.openAddOrChangeUserPrivilege()");
                }
                JIFUserPrivilegeChange.setVisible(true);
            }
        }else {
            showDialogRestrictionOfАccessRights();
        }
    }
    
    // ======================= установка появление окон по середине окна =======================        
    private JInternalFrame setLocationAndSizeWindow(JInternalFrame JInternalFrame, Dimension FrameAddPriceLocation) {
        try {
            JInternalFrame.setSelected(true);
            Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
            JInternalFrame.setSize(FrameAddPriceLocation);
            JInternalFrame.setLocation(sSize.width / 2 - JInternalFrame.getWidth() / 2 - 50, sSize.height / 2 - JInternalFrame.getHeight() / 2 - 50);
            JInternalFrame.setVisible(true);
            return JInternalFrame;
        } catch (PropertyVetoException ex) {
            return JInternalFrame;
        }
    }
    
    private void showDialogRestrictionOfАccessRights(){
        Object[] options = {"закрыть",};
            int n = JOptionPane.showOptionDialog(null,
                "Недостаточно прав доступа. Обратитесь к системному администратору.",
                "Внимание",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
            if(n==0){                 
                
            }
    }
    
    public String enumToString(ArrayList<?> MedBookView, String Seporator) {
        String str = "";
        if (MedBookView.size()>0){  
            StringBuilder bstr = new StringBuilder("");
            MedBookView.forEach((object) -> {
                bstr.append(object).append(Seporator);
            });
            str = bstr.toString().substring(0, bstr.toString().length() - 1);
        }
        return str;
    }
    
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMMedicalBook = new javax.swing.JMenu();
        jMIMedicalBookBase = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMIMedicalBookAdd = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMIMedicalBookChange = new javax.swing.JMenuItem();
        jMEmployee = new javax.swing.JMenu();
        jMIEmployeeBase = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMIEmployeeAndMedicalBookAdd = new javax.swing.JMenuItem();
        jMIEmployeeAdd = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMIEmployeeChange = new javax.swing.JMenuItem();
        jMInstitution = new javax.swing.JMenu();
        jMIInstitutionBase = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMIInstitutionAdd = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMIInstitutionChange = new javax.swing.JMenuItem();
        jMMailing = new javax.swing.JMenu();
        jMenu16 = new javax.swing.JMenu();
        jMenu15 = new javax.swing.JMenu();
        jMSettings = new javax.swing.JMenu();
        jMSettingsGroup = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMSettingsDepartment = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem17 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMSettingsPosition = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItem20 = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMSettingsRegion = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMSettingsMedicalBook = new javax.swing.JMenu();
        jMenuItem25 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMUserRoot = new javax.swing.JMenu();
        jMIUserRootBase = new javax.swing.JMenuItem();
        jMIUserRootAdd = new javax.swing.JMenuItem();
        jMIUserRootChange = new javax.swing.JMenuItem();
        jMUserPrivilege = new javax.swing.JMenu();
        jMIUserPrivilegeBase = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMIUserPrivilegeAdd = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMIUserPrivilegeChange = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("База медицинских книжек");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1123, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jMMenu.setText("ФАЙЛ");

        jMenuItem1.setText("Настройки");
        jMenuItem1.setEnabled(false);
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMMenu.add(jMenuItem1);
        jMMenu.add(jSeparator1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Выход");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMMenu.add(jMenuItem2);

        jMenuBar1.add(jMMenu);
        jMMenu.getAccessibleContext().setAccessibleName("");
        jMMenu.getAccessibleContext().setAccessibleDescription("");

        jMMedicalBook.setText("БАЗА ЛМК");

        jMIMedicalBookBase.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMIMedicalBookBase.setText("БАЗА ЛМК");
        jMIMedicalBookBase.setDoubleBuffered(true);
        jMIMedicalBookBase.setName(""); // NOI18N
        jMIMedicalBookBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIMedicalBookBaseActionPerformed(evt);
            }
        });
        jMMedicalBook.add(jMIMedicalBookBase);
        jMMedicalBook.add(jSeparator5);

        jMIMedicalBookAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIMedicalBookAdd.setText("Добавить ЛМК");
        jMIMedicalBookAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIMedicalBookAddActionPerformed(evt);
            }
        });
        jMMedicalBook.add(jMIMedicalBookAdd);
        jMMedicalBook.add(jSeparator6);

        jMIMedicalBookChange.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIMedicalBookChange.setText("Редактировать ЛМК");
        jMIMedicalBookChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIMedicalBookChangeActionPerformed(evt);
            }
        });
        jMMedicalBook.add(jMIMedicalBookChange);

        jMenuBar1.add(jMMedicalBook);

        jMEmployee.setText("СОТРУДНИКИ");

        jMIEmployeeBase.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMIEmployeeBase.setText("БАЗА СОТРУДНИКОВ");
        jMIEmployeeBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEmployeeBaseActionPerformed(evt);
            }
        });
        jMEmployee.add(jMIEmployeeBase);
        jMEmployee.add(jSeparator2);

        jMIEmployeeAndMedicalBookAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIEmployeeAndMedicalBookAdd.setText("Добавить сотрудника и ЛМК");
        jMIEmployeeAndMedicalBookAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEmployeeAndMedicalBookAddActionPerformed(evt);
            }
        });
        jMEmployee.add(jMIEmployeeAndMedicalBookAdd);

        jMIEmployeeAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIEmployeeAdd.setText("Добавить сотрудника");
        jMIEmployeeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEmployeeAddActionPerformed(evt);
            }
        });
        jMEmployee.add(jMIEmployeeAdd);
        jMEmployee.add(jSeparator4);

        jMIEmployeeChange.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIEmployeeChange.setText("Редактировать сотрудника");
        jMIEmployeeChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEmployeeChangeActionPerformed(evt);
            }
        });
        jMEmployee.add(jMIEmployeeChange);

        jMenuBar1.add(jMEmployee);

        jMInstitution.setText("УЧЕРЕЖДЕНИЯ");

        jMIInstitutionBase.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMIInstitutionBase.setText("БАЗА УЧЕРЕЖДЕНИЙ");
        jMIInstitutionBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIInstitutionBaseActionPerformed(evt);
            }
        });
        jMInstitution.add(jMIInstitutionBase);
        jMInstitution.add(jSeparator8);

        jMIInstitutionAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIInstitutionAdd.setText("Добавить новое учереждение");
        jMIInstitutionAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIInstitutionAddActionPerformed(evt);
            }
        });
        jMInstitution.add(jMIInstitutionAdd);
        jMInstitution.add(jSeparator7);

        jMIInstitutionChange.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIInstitutionChange.setText("Редактировать учереждение");
        jMIInstitutionChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIInstitutionChangeActionPerformed(evt);
            }
        });
        jMInstitution.add(jMIInstitutionChange);

        jMenuBar1.add(jMInstitution);

        jMMailing.setText("Рассылки");

        jMenu16.setText("Рассылка Email");
        jMMailing.add(jMenu16);

        jMenu15.setText("Найтроука уведомлений по Email");
        jMMailing.add(jMenu15);

        jMenuBar1.add(jMMailing);

        jMSettings.setText("Сервис");

        jMSettingsGroup.setText("Найтрока групп");

        jMenuItem13.setText("Группы сотрудников");
        jMSettingsGroup.add(jMenuItem13);
        jMSettingsGroup.add(jSeparator14);

        jMenuItem14.setText("Добавить группу");
        jMSettingsGroup.add(jMenuItem14);
        jMSettingsGroup.add(jSeparator16);

        jMenuItem15.setText("Изменить группу");
        jMSettingsGroup.add(jMenuItem15);

        jMSettings.add(jMSettingsGroup);

        jMSettingsDepartment.setText("Найтройка подразделений");

        jMenuItem16.setText("Подразделения");
        jMSettingsDepartment.add(jMenuItem16);
        jMSettingsDepartment.add(jSeparator9);

        jMenuItem17.setText("Добавить подразделение");
        jMSettingsDepartment.add(jMenuItem17);
        jMSettingsDepartment.add(jSeparator10);

        jMenuItem18.setText("Изменить подразделение");
        jMSettingsDepartment.add(jMenuItem18);

        jMSettings.add(jMSettingsDepartment);

        jMSettingsPosition.setText("Настройка должностей");

        jMenuItem19.setText("Должности");
        jMSettingsPosition.add(jMenuItem19);
        jMSettingsPosition.add(jSeparator11);

        jMenuItem20.setText("Добавить должность");
        jMSettingsPosition.add(jMenuItem20);
        jMSettingsPosition.add(jSeparator12);

        jMenuItem21.setText("Изменить должность");
        jMSettingsPosition.add(jMenuItem21);

        jMSettings.add(jMSettingsPosition);

        jMSettingsRegion.setText("Настройка районов");

        jMenuItem22.setText("Районы");
        jMSettingsRegion.add(jMenuItem22);

        jMSettings.add(jMSettingsRegion);

        jMSettingsMedicalBook.setText("Настройки ЛМК");

        jMenuItem25.setText("Периодичность прохождения");
        jMSettingsMedicalBook.add(jMenuItem25);

        jMSettings.add(jMSettingsMedicalBook);
        jMSettings.add(jSeparator3);

        jMUserRoot.setText("Настройка пользователей");

        jMIUserRootBase.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        jMIUserRootBase.setText("База пользователей");
        jMIUserRootBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIUserRootBaseActionPerformed(evt);
            }
        });
        jMUserRoot.add(jMIUserRootBase);

        jMIUserRootAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIUserRootAdd.setText("Добавить пользователя");
        jMIUserRootAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIUserRootAddActionPerformed(evt);
            }
        });
        jMUserRoot.add(jMIUserRootAdd);

        jMIUserRootChange.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIUserRootChange.setText("Изменить пользователя");
        jMUserRoot.add(jMIUserRootChange);

        jMSettings.add(jMUserRoot);

        jMUserPrivilege.setText("Найтройка прав доступа");

        jMIUserPrivilegeBase.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMIUserPrivilegeBase.setText("База прав доступа");
        jMIUserPrivilegeBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIUserPrivilegeBaseActionPerformed(evt);
            }
        });
        jMUserPrivilege.add(jMIUserPrivilegeBase);
        jMUserPrivilege.add(jSeparator15);

        jMIUserPrivilegeAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIUserPrivilegeAdd.setText("Добавить право доступа");
        jMIUserPrivilegeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIUserPrivilegeAddActionPerformed(evt);
            }
        });
        jMUserPrivilege.add(jMIUserPrivilegeAdd);
        jMUserPrivilege.add(jSeparator13);

        jMIUserPrivilegeChange.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMIUserPrivilegeChange.setText("Редактировать право доступа");
        jMIUserPrivilegeChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIUserPrivilegeChangeActionPerformed(evt);
            }
        });
        jMUserPrivilege.add(jMIUserPrivilegeChange);

        jMSettings.add(jMUserPrivilege);

        jMenuBar1.add(jMSettings);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jDesktopPane1)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMIEmployeeBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEmployeeBaseActionPerformed
        openBaseEmployees(statusFrame.VIEW,null);
    }//GEN-LAST:event_jMIEmployeeBaseActionPerformed

    private void jMIEmployeeAndMedicalBookAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEmployeeAndMedicalBookAddActionPerformed
        openAddChangeEmployeeAndMedicalBook();
    }//GEN-LAST:event_jMIEmployeeAndMedicalBookAddActionPerformed

    private void jMIMedicalBookBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIMedicalBookBaseActionPerformed
        openBaseMedicalBook(statusFrame.VIEW, null);
    }//GEN-LAST:event_jMIMedicalBookBaseActionPerformed

    private void jMIMedicalBookAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIMedicalBookAddActionPerformed
        this.SelectedNumMedBook = -1;
        openAddChangeMedicalBook();
    }//GEN-LAST:event_jMIMedicalBookAddActionPerformed

    private void jMIEmployeeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEmployeeAddActionPerformed
        this.setSelectedEmployeeId(-1);
        openAddOrChangeEmployee();
    }//GEN-LAST:event_jMIEmployeeAddActionPerformed

    private void jMIInstitutionBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIInstitutionBaseActionPerformed
        openBaseInstitutions(statusFrame.VIEW, null);
    }//GEN-LAST:event_jMIInstitutionBaseActionPerformed

    private void jMIMedicalBookChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIMedicalBookChangeActionPerformed
        openAddChangeMedicalBook();
    }//GEN-LAST:event_jMIMedicalBookChangeActionPerformed

    private void jMIEmployeeChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEmployeeChangeActionPerformed
        openAddOrChangeEmployee();
    }//GEN-LAST:event_jMIEmployeeChangeActionPerformed

    private void jMIInstitutionAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIInstitutionAddActionPerformed
        this.setSelectedInstitutionId(-1);
        openAddOrChangeInstitution();
    }//GEN-LAST:event_jMIInstitutionAddActionPerformed

    private void jMIInstitutionChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIInstitutionChangeActionPerformed
        openAddOrChangeInstitution();
    }//GEN-LAST:event_jMIInstitutionChangeActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Object[] options = {"Да, закрыть",
                    "Отмена",};
        int n = JOptionPane.showOptionDialog(this,
            "Вы желаете закрыть программу?",
            "Закрыть программу?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            options,  //the titles of buttons
            options[0]); //default button title;
        if(n==1){
            
        }else System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Object[] options = {"Да, закрыть",
                    "Отмена",};
        int n = JOptionPane.showOptionDialog(this,
            "Вы желаете закрыть программу?",
            "Закрыть программу?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            options,  //the titles of buttons
            options[0]); //default button title;
        if(n==1){
            
        }else System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMIUserPrivilegeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIUserPrivilegeAddActionPerformed
        this.setSelectedUserPrivilegeId(-1);
        openAddOrChangeUserPrivilege();
    }//GEN-LAST:event_jMIUserPrivilegeAddActionPerformed

    private void jMIUserPrivilegeChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIUserPrivilegeChangeActionPerformed
        openAddOrChangeUserPrivilege();
    }//GEN-LAST:event_jMIUserPrivilegeChangeActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMIUserPrivilegeBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIUserPrivilegeBaseActionPerformed
        openUserPrivilege(statusFrame.VIEW,null);
    }//GEN-LAST:event_jMIUserPrivilegeBaseActionPerformed

    private void jMIUserRootBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIUserRootBaseActionPerformed
        openBaseUserRoot();
    }//GEN-LAST:event_jMIUserRootBaseActionPerformed

    private void jMIUserRootAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIUserRootAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMIUserRootAddActionPerformed

    
    // ======================= главный метод =======================
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MedicalBookProgectMaven.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MedicalBookProgectMaven.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MedicalBookProgectMaven.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MedicalBookProgectMaven.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MedicalBookProgectMaven().setVisible(true);
                //new MedicalBookProgectMaven();               
            }
        });
    }
    
    public enum statusFrame {
        VIEW,
        CHOISE,
        CHOISE_MAIN
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMEmployee;
    private javax.swing.JMenuItem jMIEmployeeAdd;
    private javax.swing.JMenuItem jMIEmployeeAndMedicalBookAdd;
    private javax.swing.JMenuItem jMIEmployeeBase;
    private javax.swing.JMenuItem jMIEmployeeChange;
    private javax.swing.JMenuItem jMIInstitutionAdd;
    private javax.swing.JMenuItem jMIInstitutionBase;
    private javax.swing.JMenuItem jMIInstitutionChange;
    private javax.swing.JMenuItem jMIMedicalBookAdd;
    private javax.swing.JMenuItem jMIMedicalBookBase;
    private javax.swing.JMenuItem jMIMedicalBookChange;
    private javax.swing.JMenuItem jMIUserPrivilegeAdd;
    private javax.swing.JMenuItem jMIUserPrivilegeBase;
    private javax.swing.JMenuItem jMIUserPrivilegeChange;
    private javax.swing.JMenuItem jMIUserRootAdd;
    private javax.swing.JMenuItem jMIUserRootBase;
    private javax.swing.JMenuItem jMIUserRootChange;
    private javax.swing.JMenu jMInstitution;
    private javax.swing.JMenu jMMailing;
    private javax.swing.JMenu jMMedicalBook;
    private javax.swing.JMenu jMMenu;
    private javax.swing.JMenu jMSettings;
    private javax.swing.JMenu jMSettingsDepartment;
    private javax.swing.JMenu jMSettingsGroup;
    private javax.swing.JMenu jMSettingsMedicalBook;
    private javax.swing.JMenu jMSettingsPosition;
    private javax.swing.JMenu jMSettingsRegion;
    private javax.swing.JMenu jMUserPrivilege;
    private javax.swing.JMenu jMUserRoot;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    // End of variables declaration//GEN-END:variables

}
