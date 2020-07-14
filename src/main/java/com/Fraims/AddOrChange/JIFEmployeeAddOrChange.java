
package com.Fraims.AddOrChange;

import com.Class.Employee;
import com.Class.Employee.EnumSex;
import com.Class.Institution;
import static com.Class.Institution.InstitutionType.*;
import com.Class.MainClassProgect;
import static com.notUse.InternalFrameViewable.*;
import com.Class.MedicalBook;
import com.Class.ReferralVaccinations;
import com.Class.TableResize;
import com.Class.TypeUnit;
import com.Class.UsGroup;
import com.Class.UserPrivilege;
import com.Class.UserPrivilege.PrivilegeDefault;
import static com.Class.UserPrivilege.PrivilegeDefault.*;
import com.Progect.MedicalBookProgectMaven;
import com.Progect.MedicalBookProgectMaven.statusFrame;
import com.SQL.SQLQuery;
import com.notUse.MainInternalFrame;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.plaf.InternalFrameUI;


public class JIFEmployeeAddOrChange extends MainInternalFrame {
    //private int AuthorizedUserId;
    private int SelectedNumMedicalBook;
    private int SelectedEmployeeId;
    private int SelectedMainEmployeeId;
    private int SelectedNumMedicalBookUnder;
    private int SelectedInstitutionId;
    private int SelectedDepartmentId = 0;
    private int SelectedGroupId = 0;
    private int SelectedPositionId = 0;
    private int SelectTypeUnitId = 0;
    private int RegionId = 0;
    
    private boolean ComboBoxStateChange = false;
    private boolean IsOnlyLineStaff = false;
    private boolean IsNewEmployee = false;
    private boolean IsNewMedicalBook = false;
    private boolean IsNewInstitution = false;
    
    private UserPrivilege userPrivilege;
    
    private TreeMap<Integer,Employee> EmployeeTM = new TreeMap<>();
    private TreeMap<Integer,Employee> EmployeeTMUnder = new TreeMap<>();
    private Employee employee;
    private Employee mainEmployee;
    private Employee addEmployee;
    private Employee changeEmployee;
    private MedicalBook medicalBook;
    private MedicalBook newMedicalBook;
    private Institution institution;
    private Institution newInstitution;

    private MedicalBookProgectMaven MBProgect;

    // ====================== УСТАНОВКИ ЗНАЧЕНИЯ В ПОЛЯ ПО ЗАПРОСУ ОТ ДРУГИХ ОКОН ========================  
    public void setSelectedNumMedicalBook(int SelectedNumMedicalBook) {
        this.SelectedNumMedicalBook = SelectedNumMedicalBook;
        IsNewMedicalBook = true;
        newMedicalBook = new SQLQuery<>(new MedicalBook()).Read(SelectedNumMedicalBook);
        jButton2.setEnabled(true);
        employee.setNumMedicalBook(SelectedNumMedicalBook);
        FillFieldsMedBook(newMedicalBook);
    }    
    public void setSelectedEmployeeId(int SelectedEmployeeId){
        this.SelectedEmployeeId = SelectedEmployeeId;        
        verificationData();
    }
    public void setSelectedMainEmployeeId(int SelectedMainEmployeeId){
        this.SelectedMainEmployeeId = SelectedMainEmployeeId;
        mainEmployee = new SQLQuery<>(new Employee()).Read(SelectedMainEmployeeId);
        employee.setMainEmployeeId(SelectedMainEmployeeId);
        FillFieldsEmployee();
        //FillTableBaseEmployeesUnder(Integer.valueOf(jComboBox7.getSelectedItem().toString()),"");
    }
    public void setSelectedInstitutionId(int SelectedInstitutionId) {
        this.SelectedInstitutionId = SelectedInstitutionId;
        IsNewInstitution = true;
        newInstitution = new SQLQuery<>(new Institution()).Read(SelectedInstitutionId);
        jButton2.setEnabled(true);
        
        employee.setInstitutionId(SelectedInstitutionId);
        FillFieldsInstitution(newInstitution);
    }

    public JIFEmployeeAddOrChange(MedicalBookProgectMaven MedicalBookProgect) {
        super(MedicalBookProgect);
        MBProgect = MedicalBookProgect;        
        SelectedEmployeeId = MedicalBookProgect.getSelectedEmployeeId();
        RegionId = MedicalBookProgect.getRegionId();
        //TypeUnitTM = MedicalBookProgect.getTypeUnitTM();
        SelectedNumMedicalBook = MedicalBookProgect.getSelectedNumMedBook();
        SelectTypeUnitId = MedicalBookProgect.getAuthorizedUser().getTypeUnitId();
        newMedicalBook = new MedicalBook();
        userPrivilege = MedicalBookProgect.getUserPrivilegeLogin();
        initComponents();
        jTEmployeesUnder = new TableResize().setTableColumnsWidth(jTEmployeesUnder,500);
//        TableEmployeesUnder = (DefaultTableModel) jTEmployeesUnder.getModel();        
        verificationData();        
    }
    
    private void verificationData(){
        Thread MBThread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{                    
                    IsNewMedicalBook = false;    
                    employee = new SQLQuery<>(new Employee()).Read(SelectedEmployeeId);                    
                    medicalBook = new SQLQuery<>(new MedicalBook()).Read(employee.getNumMedicalBook());                    
                    institution = new SQLQuery<>(new Institution()).Read(employee.getInstitutionId());
                    newMedicalBook = new MedicalBook();
                    if (SelectedEmployeeId <= 0){
                        employee.setDateAdd(new Date());
                        employee.setEmployeeAddId(MBProgect.getAuthorizedUser().getId());
                        employee.setMainEmployeeId(MBProgect.getAuthorizedUser().getId());  
                        jButton2.setText("ДОБАВИТЬ СОТРУДНИКА");
                        jButton2.setEnabled(false);
                        IsNewEmployee = true;
                    } else {
                        employee.setDateChange(new Date());
                        employee.setEmployeeChangeId(MBProgect.getAuthorizedUser().getId()); 
                        jButton2.setText("Сохранить изменения");
                        jButton2.setEnabled(true);
                        IsNewEmployee = false;
                    }                              
                    mainEmployee = new SQLQuery<>(new Employee()).Read(employee.getMainEmployeeId());
                    addEmployee = new SQLQuery<>(new Employee()).Read(employee.getEmployeeAddId()); 
                    changeEmployee = new SQLQuery<>(new Employee()).Read(employee.getEmployeeChangeId());
                    
                    FillFieldsEmployee();
                    FillFieldsMedBook(medicalBook);
                    FillFieldsInstitution(institution);
                    //FillTableBaseEmployeesUnder(Integer.valueOf(jComboBox7.getSelectedItem().toString()),"");
                    employee.setRegionId(RegionId);
                    medicalBook.setRegionId(RegionId);                   
                    institution.setRegionId(RegionId);
                }catch(Exception ex){
                    System.out.println("JIFEmployeeAddOrChange.verificationData.run() ПОТОК - Ошибка загрузки данных "+ex.getMessage());
                }                 
            }
        });
        MBThread.run();
    }
    // ====================== установка значений в поля ========================    
    private void FillFieldsEmployee(){
                
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        jButton2.setEnabled(true);
            // ==================== ГЛАВНАЯ ====================    
        try{    
            jBChoiseInstitution.setEnabled(false);
            jBInstitutionDelete.setEnabled(false);   
            
            if (employee.getId()>0){
                jTFEmpId.setText(employee.getId().toString());
                jChBEmpisWork.setSelected(employee.isIsDeleted());
                jTFLastName.setText(employee.getLastName());
                jTFName.setText(employee.getName());
                jTFPatranymic.setText(employee.getPatronymic());
                jTFBerthDay.setText(employee.getBerthday().equals(new Date(0)) ? "" : new SimpleDateFormat("dd.MM.yyyy").format(employee.getBerthday())); 
                if (employee.getSex()==EnumSex.Male)
                    jCBSex.setSelectedIndex(1);
                else 
                    jCBSex.setSelectedIndex(0);
                jTFPhone.setText(employee.getPhone1());
                jTFPhone2.setText(employee.getPhone2());
                jTFEmail.setText(employee.getEmail());
            } else {
                jTFEmpId.setText("Номер сотрудника создается автоматически");
                jChBEmpisWork.setSelected(false);
                jTFLastName.setText("");
                jTFName.setText("");
                jTFPatranymic.setText("");
                jTFBerthDay.setText(""); 
                jCBSex.setSelectedIndex(0);
                jTFPhone.setText("");
                jTFPhone2.setText("");
                jTFEmail.setText("");
            }
        
        
            // ==================== сотрудик с правом доступа ====================
            if (mainEmployee.getId()>0){
                jLabel96.setText(mainEmployee.getId().toString());
                jLFIOEmployeer.setText(mainEmployee.getSurnameInitials());
                jLPhoneEmployeer.setText(mainEmployee.getPhone1());
                jLBerthdayEmployeer.setText(df.format(mainEmployee.getBerthday()));
                jLGroupEmployeer.setText(mainEmployee.getUsGroupName());
                jLDepartmentEmployeer.setText(mainEmployee.getUsDepartmentName());
                jLPositionEmployeer.setText(mainEmployee.getUsPositionName());
                jTANoteMainEmployeer.setText(mainEmployee.getNote());
                jLEmailEmployeer.setText(mainEmployee.getEmail());
            }else {
                jLabel96.setText("Сотрудник не выбран");
                jLFIOEmployeer.setText("Сотрудник не выбран");
                jLPhoneEmployeer.setText("Сотрудник не выбран");
                jLBerthdayEmployeer.setText("Сотрудник не выбран");
                jLGroupEmployeer.setText("Сотрудник не выбран");
                jLDepartmentEmployeer.setText("Сотрудник не выбран");
                jLPositionEmployeer.setText("Сотрудник не выбран");
                jTANoteMainEmployeer.setText("Сотрудник не выбран");
                jLEmailEmployeer.setText("Сотрудник не выбран");
            }

            // ==================== рабочая информация ====================
            jCBUsGroup.setEnabled(MBProgect.getStringPrivEmployee().contains("ALL"));
            jCBUsDepartment.setEnabled(MBProgect.getStringPrivEmployee().contains("OWNGROUP") ||
                    MBProgect.getStringPrivEmployee().contains("OWNDEPARTMENT") ||
                    MBProgect.getStringPrivEmployee().contains("EMPLOYEEUNDER") ||
                    MBProgect.getStringPrivEmployee().contains("LINESTAFF"));
            jCBPosition.setEnabled(MBProgect.getStringPrivEmployee().contains("OWNDEPARTMENT") ||
                    MBProgect.getStringPrivEmployee().contains("EMPLOYEEUNDER") ||
                    MBProgect.getStringPrivEmployee().contains("LINESTAFF"));
            jChBEmpisWork.setEnabled(MBProgect.getStringPrivEmployee().contains("DELET"));
            jBGetMainEmployee.setEnabled(MBProgect.getStringPrivEmployee().contains("ALL"));
            jBDeletMainEmployee.setEnabled(jBGetMainEmployee.isSelected());
            
            ComboBoxStateChange = false;  
            jCBUsGroup.setModel(getValuesGroup(false));
            jCBUsGroup.setSelectedItem(0);
            groupStateChange(jCBUsGroup, jCBUsDepartment, jCBPosition);
                
            StringBuilder strb = new StringBuilder();             
            if (userPrivilege.getPrivEmployee().contains(VIEW_ALL)) {
                strb.append("Без ограничений;");
            }else{
                jCBUsGroup.setSelectedItem(!IsNewEmployee? employee.getUsGroupName() : MBProgect.getAuthorizedUser().getUsGroupName()); // установим в      
                groupStateChange(jCBUsGroup, jCBUsDepartment, jCBPosition);
                if (!IsNewEmployee) jCBPosition.setSelectedItem(employee.getUsPositionName());
               
                SelectedGroupId = getKeyUsGroupByName(jCBUsGroup.getSelectedItem().toString()); 
                jBChoiseInstitution.setEnabled(MBProgect.getUsGroupTM().get(SelectedGroupId).getIsStaffGroup());
                jBInstitutionDelete.setEnabled(jBChoiseInstitution.isSelected());
                
                if (userPrivilege.getPrivEmployee().contains(VIEW_DELET)) {
                    if (userPrivilege.getPrivEmployee().contains(VIEW_DELET)) strb.append("Просмотр удаленных; ");
                    if (userPrivilege.getPrivEmployee().contains(CHANGE_DELET))  strb.append("Выбор удаленных; ");                    
                }
                if (userPrivilege.getPrivEmployee().contains(VIEW_OWNGROUP)) {                    
                    if (IsNewEmployee) strb.append("Редактирование сотрудника своей группы, подразделения и подчененного сотрудника; ");
                    else strb.append("Добавление нового сотрудника своей группы, подразделения и подчененного сотрудника; ");                    
                } else if (userPrivilege.getPrivEmployee().contains(VIEW_OWNDEPARTMENT)) {                    
                    if (IsNewEmployee) strb.append("Редактирование сотрудника своего подразделения и подчененного сотрудника; ");
                    else strb.append("Добавление нового сотрудника своего подразделения и подчененного сотрудника; ");
                } else if (userPrivilege.getPrivEmployee().contains(VIEW_EMPLOYEEUNDER)) {
//                    jCBUsDepartment.setSelectedItem(MBProgect.getAuthorizedUser().getUsDepartmentName());
//                    departmentStateChange(jCBUsDepartment, jCBPosition);                     
                    SelectedMainEmployeeId = MBProgect.getAuthorizedUser().getId();
                    if (IsNewEmployee) strb.append("Редактирование подчененного сотрудника; ");
                    else strb.append("Добавление подчененного сотрудника; ");
                } else if (userPrivilege.getPrivEmployee().contains(VIEW_LINESTAFF)){
                    IsOnlyLineStaff = true;
                    if (!IsNewEmployee)
                        jCBUsGroup.setSelectedItem(MBProgect.getUsGroupTM().get(employee.getUsGroupId()).getName());
                    else {
                        for (Map.Entry<Integer, UsGroup> entry : MBProgect.getUsGroupTM().entrySet()) {
                            UsGroup value = entry.getValue();
                            if (value.getTypeUnitId().equals(MBProgect.getAuthorizedUser().getTypeUnitId()) && value.getIsStaffGroup()){
                                jCBUsGroup.setSelectedItem(value.getName());
                                break;
                            }
                        }                        
                    }
                    groupStateChange(jCBUsGroup, jCBUsDepartment, jCBPosition);
                    //jCBPosition.setEnabled(true);
                    
                    //jCBUsDepartment.setSelectedItem(MBProgect.getAuthorizedUser().getUsDepartmentName());
                    SelectedMainEmployeeId = !IsOnlyLineStaff ? MBProgect.getAuthorizedUser().getId() : -1;   
                    
                    SelectedGroupId = getKeyUsGroupByName(jCBUsGroup.getSelectedItem().toString()); 
                    jBChoiseInstitution.setEnabled(true);
                    jBInstitutionDelete.setEnabled(true);
                    if (IsNewEmployee) strb.append("Редактирование линейного персонала; ");
                    else strb.append("Добавление линейного персонала; "); 
                }
            }
            if (IsNewEmployee) setTitle("Изменение сотрудника.");
            else setTitle("Добавление сотрудника.");
            
            setTitle(getTitle() + "Добавление изменение сотрудника. Право доступа (" + strb.toString().substring(0, strb.length() - 2) + ")");
            ComboBoxStateChange = true;            
            
            if (SelectedEmployeeId > 0) {           
                jLTypeUnit.setText(MBProgect.getUsGroupTM().get(employee.getUsGroupId()).getTypeUnitName());
                jLAddDate.setText(new SimpleDateFormat("dd.MM.yyyy hh:mm").format(addEmployee.getDateAdd()));
                jLAddEmpFIO.setText(addEmployee.getSurnameInitials());
                jLAddEmpPosition.setText(addEmployee.getUsPositionName());
                jLChangeDate.setText(new SimpleDateFormat("dd.MM.yyyy hh:mm").format(changeEmployee.getDateChange()));
                jLChangeEmpFIO.setText(changeEmployee.getSurnameInitials());
                jLChangeEmpPosotion.setText(changeEmployee.getUsPositionName());

            } else {      
                if (userPrivilege.getPrivMedicalBook().contains(CHANGE_ALL))
                    jLTypeUnit.setText(MBProgect.getTypeUnitTM().get(1).getName());
                else 
                    jLTypeUnit.setText(MBProgect.getTypeUnitTM().get(SelectTypeUnitId).getName());
                jLAddDate.setText(new SimpleDateFormat("dd.MM.yyyy hh:mm").format(addEmployee.getDateAdd()));
                jLAddEmpFIO.setText(addEmployee.getSurnameInitials());
                jLAddEmpPosition.setText(addEmployee.getUsPositionName());
                jLChangeDate.setText("");
                jLChangeEmpFIO.setText("");
                jLChangeEmpPosotion.setText("");
            }        
            
            ComboBoxStateChange = true;
            SelectedGroupId = getKeyByTreeMap(MBProgect.getUsGroupTM(), jCBUsGroup.getSelectedItem().toString()); 
            // выбор учереждения


            //

//            // ==================== сотрудники в подчинении ====================
//            jTextField13.setText("Поиск по таблице");
//            jComboBox8.setSelectedIndex(0);
//            jCheckBox2.setSelected(false);
//            jComboBox7.setSelectedItem(25);
//            //jTable2


            // ==================== фото ====================
            jLabel3.setText("");
            jLabel5.setText("нет фото");
        }catch (Exception ex){
            System.out.println("com.Fraims.AddOrChange.JIFEmployeeAddOrChange.FillFieldsEmployee() - " + ex.getLocalizedMessage());
        }
    }
    
    private void FillFieldsInstitution(Institution InstitutionLocal){
        //jBChoiseInstitution.setEnabled(false);
        if (InstitutionLocal.getId()>0){
            jLInstitutionId.setText(InstitutionLocal.getId().toString());
            jLMainInstitution.setText(InstitutionLocal.getMainInstitutionName().equals("") ? "Не выбран" : InstitutionLocal.getMainInstitutionName()); 
            jLInstitution.setText(InstitutionLocal.getName());
            jLInstitutionAdress.setText(InstitutionLocal.getAddress());
            jLInstitutionStatus.setText(InstitutionLocal.getIsClosed()? "Закрыто" : "Работает");
            //jBChoiseInstitution.setEnabled(true);
        }else{
            jLInstitutionId.setText("Учереждение не выбрано");
            jLMainInstitution.setText("Учереждение не выбрано"); 
            jLInstitution.setText("Учереждение не выбрано");
            jLInstitutionAdress.setText("Учереждение не выбрано");
            jLInstitutionStatus.setText("Учереждение не выбрано");
        }
    }
    // ==================== Медкнижка ====================
    private void FillFieldsMedBook(MedicalBook MedBook){
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (MedBook.getNumMedicalBook()>0){
            // ==================== главная ====================
            jLNumMedBook.setText(MedBook.getNumMedicalBook().toString());
            // ==================== прививки ====================
            jLBorneDiseases.setText(enumToString(MedBook.getBorneDiseases()));

            jLRubella.setText(df.format(MedBook.getRubella().equals(new Date(0)) ? "" : MedBook.getRubella()));
            jLDiphtheria.setText(df.format(MedBook.getDiphtheria().equals(new Date(0)) ? "" : MedBook.getDiphtheria()));

            jLMeasles.setText(df.format(MedBook.getMeasles().equals(new Date(0)) ? "" : MedBook.getMeasles()));
            jLMeasles_2.setText(df.format(MedBook.getMeasles_2().equals(new Date(0)) ? "" : MedBook.getMeasles_2()));

            jLHepatitis_B.setText(df.format(MedBook.getHepatitis_B().equals(new Date(0)) ? "" : MedBook.getHepatitis_B()));
            jLHepatitis_B2.setText(df.format(MedBook.getHepatitis_B2().equals(new Date(0)) ? "" : MedBook.getHepatitis_B2()));
            jLHepatitis_B3.setText(df.format(MedBook.getHepatitis_B3().equals(new Date(0)) ? "" : MedBook.getHepatitis_B3()));

            jLHepatitis_A.setText(df.format(MedBook.getHepatitis_A().equals(new Date(0)) ? "" : MedBook.getHepatitis_A()));
            jLHepatitis_A2.setText(df.format(MedBook.getHepatitis_A2().equals(new Date(0)) ? "" : MedBook.getHepatitis_A2()));
            jLhepatitisAvaccine.setText(enumToString(MedBook.getHepatitisAvaccine()));

            // ==================== периодиеские ==================== 
            jLShigellvak.setText(df.format(MedBook.getShigellvak().equals(new Date(0)) ? "" : MedBook.getShigellvak()));

            jLTherapist.setText(df.format(MedBook.getTherapist().equals(new Date(0)) ? "" : MedBook.getTherapist()));
            jLOtolaryngologist.setText(df.format(MedBook.getOtolaryngologist().equals(new Date(0)) ? "" : MedBook.getOtolaryngologist()));
            jLDentist.setText(df.format(MedBook.getDentist().equals(new Date(0)) ? "" : MedBook.getDentist()));
            jLPsychiatrist.setText(df.format(MedBook.getPsychiatrist().equals(new Date(0)) ? "" : MedBook.getPsychiatrist()));
            jLExpertInNarcology.setText(df.format(MedBook.getExpertInNarcology().equals(new Date(0)) ? "" : MedBook.getExpertInNarcology()));
            jLDermatovenerologist.setText(df.format(MedBook.getDermatovenerologist().equals(new Date(0)) ? "" : MedBook.getDermatovenerologist()));
            jLFluorography.setText(df.format(MedBook.getFluorography().equals(new Date(0)) ? "" : MedBook.getFluorography()));
            jLValidation1.setText(df.format(MedBook.getValidation().equals(new Date(0)) ? "" : MedBook.getValidation()));
            jLHelminths.setText(df.format(MedBook.getHelminths().equals(new Date(0)) ? "" : MedBook.getHelminths()));
            //jLValidationForYear.setSelected(medicalBook.isValidationForYear()); 

            // ==================== при поступлении ====================
            jLIntestinalInfection1.setText(df.format(MedBook.getIntestinalInfection().equals(new Date(0)) ? "" : MedBook.getIntestinalInfection()));
            jLTyphoidFever1.setText(df.format(MedBook.getTyphoidFever().equals(new Date(0)) ? "" : MedBook.getTyphoidFever()));
            jLStaphylococcus1.setText(df.format(MedBook.getStaphylococcus().equals(new Date(0)) ? "" : MedBook.getStaphylococcus()));
            jTAMedicalBookNote.setText(MedBook.getNote());

            // ==================== фото лмк ==================== 
            jLabel89.setText("");
            jLabel4.setText("в разработке");
        }else {
            jLNumMedBook.setText("ЛМК не выбрана");
            // ==================== прививки ====================
            jLBorneDiseases.setText("ЛМК не выбрана");

            jLRubella.setText("ЛМК не выбрана");
            jLDiphtheria.setText("ЛМК не выбрана");

            jLMeasles.setText("ЛМК не выбрана");      
            jLMeasles_2.setText("ЛМК не выбрана");

            jLHepatitis_B.setText("ЛМК не выбрана");
            jLHepatitis_B2.setText("ЛМК не выбрана");
            jLHepatitis_B3.setText("ЛМК не выбрана1");

            jLHepatitis_A.setText("ЛМК не выбрана");
            jLHepatitis_A2.setText("ЛМК не выбрана");
            jLhepatitisAvaccine.setText("ЛМК не выбрана1");

            // ==================== периодиеские ====================
            jLShigellvak.setText("ЛМК не выбрана");

            jLTherapist.setText("ЛМК не выбрана");
            jLOtolaryngologist.setText("ЛМК не выбрана");
            jLDentist.setText("ЛМК не выбрана");
            jLPsychiatrist.setText("ЛМК не выбрана");
            jLExpertInNarcology.setText("ЛМК не выбрана");
            jLDermatovenerologist.setText("ЛМК не выбрана");
            jLFluorography.setText("ЛМК не выбрана");
            jLValidation1.setText("ЛМК не выбрана");
            jLHelminths.setText("ЛМК не выбрана");
            //jCBValidationForYear.setSelected(false); 

            // ==================== при поступлении ====================
            jLIntestinalInfection1.setText("ЛМК не выбрана");
            jLTyphoidFever1.setText("ЛМК не выбрана");
            jLStaphylococcus1.setText("ЛМК не выбрана");
            jTAMedicalBookNote.setText("ЛМК не выбрана");

            //==================== фото лмк ====================
            jLabel89.setText("");
            jLabel4.setText("нет фото");
        }
    }
  
    //====================== получение значений из полей ======================

    private Employee getFildsEmployee(){
        Employee newemployee = new Employee();
        newemployee = employee;
        SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy");
        try{
            newemployee.setIsDeleted(jChBEmpisWork.isSelected());
            newemployee.setLastName(jTFLastName.getText());
            newemployee.setName(jTFName.getText());
            newemployee.setPatronymic(jTFPatranymic.getText());
            newemployee.setBerthday(ft.parse(jTFBerthDay.getText().equals("")? "01.01.1970" : jTFBerthDay.getText())); 
            newemployee.setSex(jCBSex.getSelectedIndex() == 0 ? EnumSex.Female : EnumSex.Male);
            newemployee.setPhone1(jTFPhone.getText());
            newemployee.setPhone2(jTFPhone2.getText());
            newemployee.setEmail(jTFEmail.getText());
            newemployee.setNote(jTextArea2.getText());
            newemployee.setUsPositionId(getKeyByUsDepartmentName(getKeyByTreeMap(MBProgect.getUsDepartmentTM(), jCBUsDepartment.getSelectedItem().toString()), jCBPosition.getSelectedItem().toString()));
        }catch(Exception ex){
            System.out.println("Fraims.JIFBaseUsers.getFildsEmployee()" + ex.getMessage());
        }
        return newemployee;
    }
    
    // ====================== установка значений в таблицы ========================     
//    private void FillTableBaseEmployeesUnder(Integer Limit, String Text){
//        Employee SerhEmpl = new Employee();
//        if (jCheckBox2.isSelected()) Limit = -1;
//        SerhEmpl.setNumMedicalBook(1);
//        SerhEmpl.setTextSearch(Text);
//        SerhEmpl.setIsDeleted(false);
//        SerhEmpl.setMainEmployeeId(mainEmployee.getId());
//        EmployeeTMUnder = new SQLQuery<>(SerhEmpl).ReadForTable(Limit);
//        Object[] Data;
//        while(TableEmployeesUnder.getRowCount()>0)
//            TableEmployeesUnder.removeRow(0);
//        for (Employee v : EmployeeTMUnder.values()) {
//            if (v.getId()>0){
//                Data = v.getDataForTableEmpUnder();
//                Data[0] = TableEmployeesUnder.getRowCount();
//                TableEmployeesUnder.insertRow(TableEmployeesUnder.getRowCount(), Data);
//            }
//        }
//        new TableResize().setTableColumnsWidth(jTEmployeesUnder,500);
//    }
    
    //====================== запросы в SQL ======================  
    private void SaveOrAdd(){
        Employee newEmployee = new Employee();
        try{            
            newEmployee = getFildsEmployee(); 
            if (new SQLQuery<>(newEmployee).SaveOrWrite(newEmployee.getId())){            
                Integer key = IsNewEmployee ? new SQLQuery<>(newEmployee).getLastKey() : newEmployee.getId();
                if (IsNewMedicalBook){ //если выбрана новая книжка
                    if (newMedicalBook.getNumMedicalBook() > 0) { //Если номер новой книжки больше 0 (добавили новую)
                        newMedicalBook.setEmployeeId(key);
                        new SQLQuery<>(newMedicalBook).Save(newMedicalBook.getNumMedicalBook());
                        medicalBook.setEmployeeId(-1);
                        new SQLQuery<>(medicalBook).Save(medicalBook.getNumMedicalBook());
                    } else { // если новая медкнижка удалено (-1)
                        medicalBook.setEmployeeId(-1);
                        new SQLQuery<>(medicalBook).Save(medicalBook.getNumMedicalBook());
                    }
                }
                closeThis(); 
                MBProgect.setSelectedEmployeeId(-1);
                MBProgect.setSelectedNumMedBook(-1);
                MBProgect.setSelectedInstitutionId(-1);
                //MBProgect.openBaseEmployees(statusFrame.VIEW, null);
                if (MBProgect.getJIFMedBookBase()!=null) MBProgect.openBaseMedicalBook(statusFrame.VIEW, null);
            }
        }catch(Exception ex){
            System.out.println("Fraims.JIFAddorChangeEmployee.SaveOrAdd() " + ex.getMessage());
        }             
    }
    
    private void getChoiseEmployeeId(){
        if (jTEmployeesUnder.getSelectedRow()>=0){
            SelectedEmployeeId = Integer.valueOf((Integer)jTEmployeesUnder.getValueAt(jTEmployeesUnder.convertRowIndexToModel(jTEmployeesUnder.getSelectedRow()), 1));
            String NumMedBook = jTEmployeesUnder.getValueAt(jTEmployeesUnder.convertRowIndexToModel(jTEmployeesUnder.getSelectedRow()), 4).toString();
            SelectedNumMedicalBookUnder = !NumMedBook.equals("") ? Integer.valueOf(NumMedBook) : 0;
            
            MBProgect.setSelectedNumMedBook(SelectedNumMedicalBookUnder);
            MBProgect.setSelectedEmployeeId(SelectedEmployeeId);    
        }  else {
            SelectedEmployeeId = -1;
            SelectedNumMedicalBookUnder = -1;
        }  
    }    
       
    private void closeThis(){
        this.setVisible(false);        
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedEmployeeId(-1);
        MBProgect.setSelectedNumMedBook(-1);
        MBProgect.setJIFEmployeeAddOrChange(null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMBaseEmployeeUnder = new javax.swing.JPopupMenu();
        jMIChangeMedicalBook = new javax.swing.JMenuItem();
        jPanel8 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTEmployeesUnder = new javax.swing.JTable();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField13 = new javax.swing.JTextField();
        jComboBox7 = new javax.swing.JComboBox<>();
        jComboBox8 = new javax.swing.JComboBox<>();
        jLabel92 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jTFLastName = new javax.swing.JTextField();
        jTFName = new javax.swing.JTextField();
        jTFPatranymic = new javax.swing.JTextField();
        jTFBerthDay = new javax.swing.JTextField();
        jTFPhone = new javax.swing.JTextField();
        jTFPhone2 = new javax.swing.JTextField();
        jTFEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jCBSex = new javax.swing.JComboBox<>();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel15 = new javax.swing.JPanel();
        jCBUsGroup = new javax.swing.JComboBox<>();
        jCBUsDepartment = new javax.swing.JComboBox<>();
        jCBPosition = new javax.swing.JComboBox<>();
        jBChoiseInstitution = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLMainInstitution = new javax.swing.JLabel();
        jLInstitution = new javax.swing.JLabel();
        jLInstitutionAdress = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLInstitutionStatus = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLAddDate = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLAddEmpFIO = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLAddEmpPosition = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLChangeDate = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLChangeEmpFIO = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLChangeEmpPosotion = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLInstitutionId = new javax.swing.JLabel();
        jBInstitutionDelete = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLTypeUnit = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jBGetMainEmployee = new javax.swing.JButton();
        jTANoteMainEmployeer = new javax.swing.JTextArea();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLFIOEmployeer = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jLPhoneEmployeer = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLEmailEmployeer = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLBerthdayEmployeer = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLGroupEmployeer = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLDepartmentEmployeer = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLPositionEmployeer = new javax.swing.JLabel();
        jBDeletMainEmployee = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLRubella = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLDiphtheria = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLMeasles = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLMeasles_2 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLHepatitis_B = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLHepatitis_B2 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLHepatitis_B3 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLHepatitis_A = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLHepatitis_A2 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLhepatitisAvaccine = new javax.swing.JLabel();
        jLBorneDiseases = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLNumMedBook = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jLShigellvak = new javax.swing.JLabel();
        jLTherapist = new javax.swing.JLabel();
        jLOtolaryngologist = new javax.swing.JLabel();
        jLDentist = new javax.swing.JLabel();
        jLPsychiatrist = new javax.swing.JLabel();
        jLExpertInNarcology = new javax.swing.JLabel();
        jLDermatovenerologist = new javax.swing.JLabel();
        jLFluorography = new javax.swing.JLabel();
        jLValidation1 = new javax.swing.JLabel();
        jLHelminths = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLIntestinalInfection1 = new javax.swing.JLabel();
        jLTyphoidFever1 = new javax.swing.JLabel();
        jLStaphylococcus1 = new javax.swing.JLabel();
        jBChoiseMedicalBook = new javax.swing.JButton();
        jTAMedicalBookNote = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        jBDeletMedicalBook = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTFEmpId = new javax.swing.JTextField();
        jChBEmpisWork = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        jMIChangeMedicalBook.setText("Изменить ЛМК");
        jMIChangeMedicalBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIChangeMedicalBookActionPerformed(evt);
            }
        });
        jPMBaseEmployeeUnder.add(jMIChangeMedicalBook);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton4.setText("Добавить фото");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("Х");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("нет фото");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel89.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(153, 153, 153));
        jLabel89.setText("Пусть к файлу: ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 321, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel89)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jTEmployeesUnder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTEmployeesUnder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "№ пп", "#", "Должность", "ФИО", "Номер ЛМК", "Статус ЛМК"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTEmployeesUnder.setFillsViewportHeight(true);
        jTEmployeesUnder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTEmployeesUnderMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTEmployeesUnderMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTEmployeesUnder);
        if (jTEmployeesUnder.getColumnModel().getColumnCount() > 0) {
            jTEmployeesUnder.getColumnModel().getColumn(0).setResizable(false);
            jTEmployeesUnder.getColumnModel().getColumn(1).setResizable(false);
            jTEmployeesUnder.getColumnModel().getColumn(2).setResizable(false);
            jTEmployeesUnder.getColumnModel().getColumn(3).setResizable(false);
            jTEmployeesUnder.getColumnModel().getColumn(5).setResizable(false);
        }

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox2.setText("Показать все");

        jTextField13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField13.setText("Поиск по таблице (Номер ЛМК, ФИО, Должность)");
        jTextField13.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField13FocusLost(evt);
            }
        });
        jTextField13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField13MousePressed(evt);
            }
        });
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });

        jComboBox7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "25", "50", "100" }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jComboBox8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));

        jLabel92.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(102, 102, 102));
        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel92.setText("Количество строк:");

        jButton11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton11.setText(">");
        jButton11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton11.setPreferredSize(new java.awt.Dimension(34, 21));

        jButton12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton12.setText(">>");
        jButton12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField13)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addContainerGap())
        );

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Добавить изменить данные о сотруднике");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
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

        jTabbedPane1.setBackground(new java.awt.Color(204, 204, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTFLastName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFLastName.setText("выберете сотрудника");

        jTFName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFName.setText("выберете сотрудника");

        jTFPatranymic.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFPatranymic.setText("выберете сотрудника");
        jTFPatranymic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFPatranymicActionPerformed(evt);
            }
        });

        jTFBerthDay.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFBerthDay.setText("выберете сотрудника");

        jTFPhone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFPhone.setText("выберете сотрудника");

        jTFPhone2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFPhone2.setText("выберете сотрудника");

        jTFEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFEmail.setText("выберете сотрудника");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Фамилия");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Имя");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Отчетсво");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Дата рождения");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Телефон");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Доп. Телефон");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("E-mail");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Пол");

        jCBSex.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBSex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Женщина", "Мужчина" }));

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextArea2.setRows(5);
        jTextArea2.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextArea2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFEmail)
                            .addComponent(jTFPhone2)
                            .addComponent(jTFPhone)
                            .addComponent(jTFBerthDay)
                            .addComponent(jTFPatranymic)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jCBSex, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 217, Short.MAX_VALUE))
                            .addComponent(jTFLastName)
                            .addComponent(jTFName))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFPatranymic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFBerthDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFPhone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("ОСНОВАНИЯ ИНФОРМАЦИЯ", jPanel3);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jCBUsGroup.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBUsGroup.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBUsGroupItemStateChanged(evt);
            }
        });
        jCBUsGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCBUsGroupMousePressed(evt);
            }
        });
        jCBUsGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBUsGroupActionPerformed(evt);
            }
        });

        jCBUsDepartment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBUsDepartment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBUsDepartmentItemStateChanged(evt);
            }
        });
        jCBUsDepartment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCBUsDepartmentMousePressed(evt);
            }
        });
        jCBUsDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBUsDepartmentActionPerformed(evt);
            }
        });

        jCBPosition.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBPositionActionPerformed(evt);
            }
        });

        jBChoiseInstitution.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBChoiseInstitution.setText("Выбрать учереждение");
        jBChoiseInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBChoiseInstitutionActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Группа");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setText("Подразделение");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Должность");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Учереждение");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setText("Комплекс");

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel82.setText("Адрес");

        jLabel83.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel83.setText("Подразделение");

        jLMainInstitution.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLMainInstitution.setText("учереждение не выбрано");

        jLInstitution.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLInstitution.setText("учереждение не выбрано");

        jLInstitutionAdress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLInstitutionAdress.setText("учереждение не выбрано");

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel87.setText("Статус учереждения");

        jLInstitutionStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLInstitutionStatus.setText("учереждение не выбрано");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(153, 153, 153));
        jLabel22.setText("Дата");

        jLAddDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLAddDate.setForeground(new java.awt.Color(153, 153, 153));
        jLAddDate.setText("Дата");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(153, 153, 153));
        jLabel23.setText("Сотудник");

        jLAddEmpFIO.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLAddEmpFIO.setForeground(new java.awt.Color(153, 153, 153));
        jLAddEmpFIO.setText("Сотудник");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(153, 153, 153));
        jLabel24.setText("Должность сотрудника");

        jLAddEmpPosition.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLAddEmpPosition.setForeground(new java.awt.Color(153, 153, 153));
        jLAddEmpPosition.setText("Должность сотрудника");

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(153, 153, 153));
        jLabel28.setText("Дата");

        jLChangeDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLChangeDate.setForeground(new java.awt.Color(153, 153, 153));
        jLChangeDate.setText("Дата");

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(153, 153, 153));
        jLabel29.setText("Сотудник");

        jLChangeEmpFIO.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLChangeEmpFIO.setForeground(new java.awt.Color(153, 153, 153));
        jLChangeEmpFIO.setText("Сотудник");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(153, 153, 153));
        jLabel30.setText("Должность сотрудника");

        jLChangeEmpPosotion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLChangeEmpPosotion.setForeground(new java.awt.Color(153, 153, 153));
        jLChangeEmpPosotion.setText("Должность сотрудника");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Создание:");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Изменение:");

        jLInstitutionId.setText("ID");
        jLInstitutionId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLInstitutionIdMouseClicked(evt);
            }
        });

        jBInstitutionDelete.setText("x");
        jBInstitutionDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBInstitutionDeleteActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Доступ по типу учереждения");

        jLTypeUnit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLTypeUnit.setText("не выбрано");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBUsGroup, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBUsDepartment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBPosition, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLInstitutionId, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBChoiseInstitution, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBInstitutionDelete))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel30))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLChangeEmpFIO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLChangeEmpPosotion, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLInstitution, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                                    .addComponent(jLMainInstitution, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLInstitutionAdress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLInstitutionStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLChangeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel24))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLAddEmpFIO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLAddEmpPosition, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                                        .addComponent(jLAddDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLTypeUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBUsGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jCBUsDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(27, 27, 27)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLTypeUnit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBInstitutionDelete)
                    .addComponent(jBChoiseInstitution)
                    .addComponent(jLInstitutionId)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLMainInstitution))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(jLInstitution))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(jLInstitutionAdress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(jLInstitutionStatus))
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLAddDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(jLAddEmpFIO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLAddEmpPosition)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLChangeDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLChangeEmpFIO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLChangeEmpPosotion))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30)))
                .addContainerGap())
        );

        jPanel15Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLTypeUnit, jLabel25});

        jTabbedPane1.addTab("Рабочая информация", jPanel15);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton5.setText("Добавить фото");
        jButton5.setEnabled(false);

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton6.setText("Х");
        jButton6.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Пусть к файлу: ");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("нет фото");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 321, Short.MAX_VALUE)
                        .addComponent(jButton6))
                    .addComponent(jLabel3))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        jTabbedPane1.addTab("Фото сотрудника", jPanel10);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jBGetMainEmployee.setText("Изменить сотрудника");
        jBGetMainEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGetMainEmployeeActionPerformed(evt);
            }
        });

        jTANoteMainEmployeer.setEditable(false);
        jTANoteMainEmployeer.setColumns(20);
        jTANoteMainEmployeer.setRows(5);
        jTANoteMainEmployeer.setAutoscrolls(false);
        jTANoteMainEmployeer.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel95.setText("# сотрудника");

        jLabel96.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel96.setText("сотрудник не выбран");
        jLabel96.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel96MouseClicked(evt);
            }
        });

        jLabel97.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel97.setText("ФИО");

        jLFIOEmployeer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLFIOEmployeer.setForeground(new java.awt.Color(153, 153, 153));
        jLFIOEmployeer.setText("сотрудник не выбран");

        jLabel98.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel98.setText("Телефон");

        jLPhoneEmployeer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLPhoneEmployeer.setForeground(new java.awt.Color(153, 153, 153));
        jLPhoneEmployeer.setText("сотрудник не выбран");

        jLabel99.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel99.setText("Email");

        jLEmailEmployeer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmailEmployeer.setForeground(new java.awt.Color(153, 153, 153));
        jLEmailEmployeer.setText("сотрудник не выбран");

        jLabel100.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel100.setText("Дата рождения");

        jLBerthdayEmployeer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLBerthdayEmployeer.setForeground(new java.awt.Color(153, 153, 153));
        jLBerthdayEmployeer.setText("сотрудник не выбран");

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel101.setText("Группа");

        jLGroupEmployeer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLGroupEmployeer.setForeground(new java.awt.Color(153, 153, 153));
        jLGroupEmployeer.setText("сотрудник не выбран");

        jLabel102.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel102.setText("Подразделение");

        jLDepartmentEmployeer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLDepartmentEmployeer.setForeground(new java.awt.Color(153, 153, 153));
        jLDepartmentEmployeer.setText("сотрудник не выбран");

        jLabel103.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel103.setText("Должность");

        jLPositionEmployeer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLPositionEmployeer.setForeground(new java.awt.Color(153, 153, 153));
        jLPositionEmployeer.setText("сотрудник не выбран");

        jBDeletMainEmployee.setText("x");
        jBDeletMainEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDeletMainEmployeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTANoteMainEmployeer, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLGroupEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                            .addComponent(jLDepartmentEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLPositionEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel99, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLEmailEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLBerthdayEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel98, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                            .addComponent(jLabel95))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jBGetMainEmployee)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jBDeletMainEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLFIOEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLPhoneEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel95)
                    .addComponent(jBGetMainEmployee)
                    .addComponent(jLabel96)
                    .addComponent(jBDeletMainEmployee))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(jLFIOEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel98)
                    .addComponent(jLPhoneEmployeer, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(jLEmailEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel100)
                    .addComponent(jLBerthdayEmployeer))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(jLGroupEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel102)
                    .addComponent(jLDepartmentEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(jLPositionEmployeer))
                .addGap(18, 18, 18)
                .addComponent(jTANoteMainEmployeer, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Сотрудник с правом доступа", jPanel1);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setText("Краснуха");

        jLRubella.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLRubella.setText("ЛМК не выбрана");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setText("АДС");

        jLDiphtheria.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLDiphtheria.setText("ЛМК не выбрана");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setText("ЖКВ");

        jLMeasles.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLMeasles.setText("ЛМК не выбрана");

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel41.setText("R - ЖКВ");

        jLMeasles_2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLMeasles_2.setText("ЛМК не выбрана");

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel43.setText("Геппатит В");

        jLHepatitis_B.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLHepatitis_B.setText("ЛМК не выбрана");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel45.setText("Геппатит В 2");

        jLHepatitis_B2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLHepatitis_B2.setText("ЛМК не выбрана");

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel47.setText("Геппатит В 3");

        jLHepatitis_B3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLHepatitis_B3.setText("ЛМК не выбрана");

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel49.setText("Геппатит А");

        jLHepatitis_A.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLHepatitis_A.setText("ЛМК не выбрана");

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel51.setText("Геппатит А 2");

        jLHepatitis_A2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLHepatitis_A2.setText("ЛМК не выбрана");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel53.setText("Вакцина");

        jLabel72.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel72.setText("Перенесенные заболевания");

        jLhepatitisAvaccine.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLhepatitisAvaccine.setText("ЛМК не выбрана");

        jLBorneDiseases.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLBorneDiseases.setText("ЛМК не выбрана");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Номер ЛМК");

        jLNumMedBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLNumMedBook.setText("ЛМК не выбрана");

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel84.setText("Шигелвак");

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel85.setText("Терапевт");

        jLabel86.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel86.setText("ЛОР");

        jLabel88.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel88.setText("Стоматолог");

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel90.setText("Психиатор");

        jLabel91.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel91.setText("Нарколог");

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel93.setText("КВД");

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel94.setText("ФЛГ");

        jLabel104.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel104.setText("Аттестация");

        jLabel105.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel105.setText("Гельминты");

        jLShigellvak.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLShigellvak.setText("ЛМК не выбрана");

        jLTherapist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLTherapist.setText("ЛМК не выбрана");

        jLOtolaryngologist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLOtolaryngologist.setText("ЛМК не выбрана");

        jLDentist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLDentist.setText("ЛМК не выбрана");

        jLPsychiatrist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLPsychiatrist.setText("ЛМК не выбрана");

        jLExpertInNarcology.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLExpertInNarcology.setText("ЛМК не выбрана");

        jLDermatovenerologist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLDermatovenerologist.setText("ЛМК не выбрана");

        jLFluorography.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLFluorography.setText("ЛМК не выбрана");

        jLValidation1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLValidation1.setText("ЛМК не выбрана");

        jLHelminths.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLHelminths.setText("ЛМК не выбрана");

        jLabel106.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel106.setText("Бак анализ");
        jLabel106.setMaximumSize(new java.awt.Dimension(51, 14));
        jLabel106.setMinimumSize(new java.awt.Dimension(51, 14));
        jLabel106.setPreferredSize(new java.awt.Dimension(51, 14));

        jLabel107.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel107.setText("Брюшной тиф");

        jLabel108.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel108.setText("Стефилококк");

        jLIntestinalInfection1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLIntestinalInfection1.setText("ЛМК не выбрана");

        jLTyphoidFever1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLTyphoidFever1.setText("ЛМК не выбрана");

        jLStaphylococcus1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLStaphylococcus1.setText("ЛМК не выбрана");

        jBChoiseMedicalBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBChoiseMedicalBook.setText("выбрать ЛМК");
        jBChoiseMedicalBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBChoiseMedicalBookActionPerformed(evt);
            }
        });

        jTAMedicalBookNote.setEditable(false);
        jTAMedicalBookNote.setColumns(20);
        jTAMedicalBookNote.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTAMedicalBookNote.setRows(5);
        jTAMedicalBookNote.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));

        jBDeletMedicalBook.setText("x");
        jBDeletMedicalBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDeletMedicalBookActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jTAMedicalBookNote)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel107, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLTyphoidFever1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLStaphylococcus1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLIntestinalInfection1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLHepatitis_A, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                        .addComponent(jLHepatitis_B3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLHepatitis_B2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLHepatitis_B, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLMeasles_2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLMeasles, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLDiphtheria, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLRubella, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLHepatitis_A2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLhepatitisAvaccine, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLTherapist, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLOtolaryngologist, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLDentist, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLPsychiatrist, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLExpertInNarcology, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLDermatovenerologist, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLFluorography, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel105, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLHelminths, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLShigellvak, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel104, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLValidation1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(11, 11, 11))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLBorneDiseases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLNumMedBook, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBChoiseMedicalBook, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBDeletMedicalBook)
                                .addGap(1, 1, 1)))
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLNumMedBook)
                    .addComponent(jBChoiseMedicalBook)
                    .addComponent(jBDeletMedicalBook))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(jLBorneDiseases))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jLRubella)
                    .addComponent(jLabel84)
                    .addComponent(jLShigellvak))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLDiphtheria)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLMeasles)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLMeasles_2)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel85)
                            .addComponent(jLTherapist))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel86)
                            .addComponent(jLOtolaryngologist))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel88)
                            .addComponent(jLDentist))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(jLHepatitis_B))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(jLHepatitis_B2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(jLHepatitis_B3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(jLHepatitis_A))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel51)
                            .addComponent(jLHepatitis_A2)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel90)
                            .addComponent(jLPsychiatrist))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel91)
                            .addComponent(jLExpertInNarcology))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel93)
                            .addComponent(jLDermatovenerologist))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel94)
                            .addComponent(jLFluorography))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel104)
                            .addComponent(jLValidation1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jLhepatitisAvaccine)
                    .addComponent(jLabel105)
                    .addComponent(jLHelminths))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLIntestinalInfection1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel107)
                            .addComponent(jLTyphoidFever1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel108)
                            .addComponent(jLStaphylococcus1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jTAMedicalBookNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("ИНФОРМАЦИЯ ЛМК", jPanel5);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Табельный номер сотрудника");

        jTFEmpId.setEditable(false);
        jTFEmpId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFEmpId.setText("Норме будет определен автоматически");
        jTFEmpId.setActionCommand("<Not Set>");
        jTFEmpId.setEnabled(false);
        jTFEmpId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFEmpIdFocusLost(evt);
            }
        });
        jTFEmpId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFEmpIdMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTFEmpIdMouseReleased(evt);
            }
        });

        jChBEmpisWork.setBackground(new java.awt.Color(255, 255, 255));
        jChBEmpisWork.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jChBEmpisWork.setText("Уволенный");
        jChBEmpisWork.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("СОХРАНИТЬ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton10.setText("ВЫЙТИ");
        jButton10.setPreferredSize(new java.awt.Dimension(93, 23));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFEmpId)
                        .addGap(82, 82, 82)
                        .addComponent(jChBEmpisWork, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTFEmpId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jChBEmpisWork))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        SaveOrAdd();        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTFPatranymicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFPatranymicActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPatranymicActionPerformed

    private void jCBUsGroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBUsGroupItemStateChanged

    }//GEN-LAST:event_jCBUsGroupItemStateChanged

    private void jCBUsGroupMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCBUsGroupMousePressed
        //DepartmentStateChange=true;        // TODO add your handling code here:
    }//GEN-LAST:event_jCBUsGroupMousePressed

    private void jCBUsGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUsGroupActionPerformed
        if (ComboBoxStateChange) {
            ComboBoxStateChange = false;
            ComboBoxStateChange = groupStateChange(jCBUsGroup, jCBUsDepartment, jCBPosition);
            
            SelectedGroupId = getKeyByTreeMap(MBProgect.getUsGroupTM(), jCBUsGroup.getSelectedItem().toString());            
            jLTypeUnit.setText(MBProgect.getUsGroupTM().get(SelectedGroupId).getTypeUnitName());
//            SelectTypeUnitId = MBProgect.getUsGroupTM().get(SelectedGroupId).getTypeUnitId();
//            SelectedDepartmentId = getKeyByTreeMap(MBProgect.getUsDepartmentTM(),jCBUsDepartment.getSelectedItem().toString());

            jBChoiseInstitution.setEnabled(MBProgect.getUsGroupTM().get(SelectedGroupId).getIsStaffGroup());
            jBInstitutionDelete.setEnabled(jBChoiseInstitution.isSelected());
        }
    }//GEN-LAST:event_jCBUsGroupActionPerformed

    private void jCBUsDepartmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBUsDepartmentItemStateChanged
        
    }//GEN-LAST:event_jCBUsDepartmentItemStateChanged

    private void jCBUsDepartmentMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCBUsDepartmentMousePressed

    }//GEN-LAST:event_jCBUsDepartmentMousePressed

    private void jCBUsDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUsDepartmentActionPerformed
        if (ComboBoxStateChange){ 
            ComboBoxStateChange = false;
            ComboBoxStateChange = departmentStateChange(jCBUsDepartment, jCBPosition);
        }
    }//GEN-LAST:event_jCBUsDepartmentActionPerformed

    private void jTextField13FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField13FocusLost
        if(jTextField13.getText().equals("")) jTextField13.setText("Поиск по таблице");
    }//GEN-LAST:event_jTextField13FocusLost

    private void jTextField13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField13MousePressed
        if(jTextField13.getText().equals("Поиск по таблице"))
        jTextField13.setText("");
    }//GEN-LAST:event_jTextField13MousePressed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        //FillTableBaseEmployeesUnder(employee.getMainEmployeeId(),jTextField13.getText());
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        //System.out.println("Fraims.JIFBaseUsers.jComboBox10ItemStateChanged()");
//        Integer Limit = Integer.valueOf(jComboBox7.getSelectedItem().toString());
//        if (EmployeeTMUnder.size()>0&EmployeeTMUnder.size()!=Limit){
//            FillTableBaseEmployeesUnder(Limit,"");
//        }
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        closeThis();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jTFEmpIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFEmpIdFocusLost
//        if (jTFEmpId.getText().equals(""))
//            jTFEmpId.setText("Введите номер");
    }//GEN-LAST:event_jTFEmpIdFocusLost

    private void jTFEmpIdMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFEmpIdMouseReleased
       
    }//GEN-LAST:event_jTFEmpIdMouseReleased

    private void jTFEmpIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFEmpIdMouseClicked
//        if (jTFEmpId.getText().equals("Введите номер"))
//            jTFEmpId.setText("");
    }//GEN-LAST:event_jTFEmpIdMouseClicked

    private void jBChoiseMedicalBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBChoiseMedicalBookActionPerformed
        MBProgect.openBaseMedicalBook(statusFrame.CHOISE, this.getUI());
        //MBProgect.getJIFMedBookBase().setChoiseMedicalBook(this.getUI());
    }//GEN-LAST:event_jBChoiseMedicalBookActionPerformed

    private void jTEmployeesUnderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTEmployeesUnderMouseClicked
        
    }//GEN-LAST:event_jTEmployeesUnderMouseClicked

    private void jTEmployeesUnderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTEmployeesUnderMouseReleased
        getChoiseEmployeeId();
        if (evt.getClickCount()>=2){ 
//            if (isChoiseEployee){
//                MBProgect.openAddChangeMedicalBook();
//                MBProgect.getJIFAddorChangeMedicalBook().setEmployee(SelectedEmployeeId);
//            }
//            else
//                MBProgect.openAddOrChangeEmployee();
//            
//            closeThis();
        }
        
        if (evt.isPopupTrigger())
        {
            JTable source = (JTable)evt.getSource();
            int row = source.rowAtPoint( evt.getPoint() );
            int column = source.columnAtPoint( evt.getPoint() );
            if ( row>=0){
                source.changeSelection(row, column, false, false);                
                MBProgect.setSelectedEmployeeId(Integer.valueOf((Integer)source.getModel().getValueAt(source.convertRowIndexToModel(source.getSelectedRow()), 1)));
                jPMBaseEmployeeUnder.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTEmployeesUnderMouseReleased

    private void jMIChangeMedicalBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIChangeMedicalBookActionPerformed
        getChoiseEmployeeId();
        MBProgect.openAddChangeMedicalBook();
        MBProgect.getJIFMedBookAddorChange().setNumMedicalBook(SelectedNumMedicalBookUnder);
        closeThis();
    }//GEN-LAST:event_jMIChangeMedicalBookActionPerformed

    private void jBChoiseInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBChoiseInstitutionActionPerformed
        MBProgect.openBaseInstitutions(statusFrame.CHOISE,this.getUI());
//        MBProgect.getJIFInstitutionBase().choise(this.getUI().toString(),SelectTypeUnitId);
        //MBProgect.getJIFInstitutionBase().setInstitutionType(SelectTypeUnitId);
    }//GEN-LAST:event_jBChoiseInstitutionActionPerformed

    private void jBGetMainEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGetMainEmployeeActionPerformed
        MBProgect.openBaseEmployees(statusFrame.CHOISE,this.getUI());        
    }//GEN-LAST:event_jBGetMainEmployeeActionPerformed

    private void jLabel96MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel96MouseClicked
        this.setSelectedMainEmployeeId(-1);
//        mainEmployee = new Employee();
//        employee.setMainEmployeeId(-1);
    }//GEN-LAST:event_jLabel96MouseClicked

    private void jLInstitutionIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLInstitutionIdMouseClicked
        if (evt.getClickCount()>=2){
            setSelectedInstitutionId(-1);
        }
    }//GEN-LAST:event_jLInstitutionIdMouseClicked

    private void jBInstitutionDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstitutionDeleteActionPerformed
        setSelectedInstitutionId(-1);
    }//GEN-LAST:event_jBInstitutionDeleteActionPerformed

    private void jCBPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBPositionActionPerformed

    private void jBDeletMedicalBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDeletMedicalBookActionPerformed
        this.setSelectedNumMedicalBook(-1);
    }//GEN-LAST:event_jBDeletMedicalBookActionPerformed

    private void jBDeletMainEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDeletMainEmployeeActionPerformed
        this.setSelectedMainEmployeeId(-1);
    }//GEN-LAST:event_jBDeletMainEmployeeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBChoiseInstitution;
    private javax.swing.JButton jBChoiseMedicalBook;
    private javax.swing.JButton jBDeletMainEmployee;
    private javax.swing.JButton jBDeletMedicalBook;
    private javax.swing.JButton jBGetMainEmployee;
    private javax.swing.JButton jBInstitutionDelete;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jCBPosition;
    private javax.swing.JComboBox<String> jCBSex;
    private javax.swing.JComboBox<String> jCBUsDepartment;
    private javax.swing.JComboBox<String> jCBUsGroup;
    private javax.swing.JCheckBox jChBEmpisWork;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JLabel jLAddDate;
    private javax.swing.JLabel jLAddEmpFIO;
    private javax.swing.JLabel jLAddEmpPosition;
    private javax.swing.JLabel jLBerthdayEmployeer;
    private javax.swing.JLabel jLBorneDiseases;
    private javax.swing.JLabel jLChangeDate;
    private javax.swing.JLabel jLChangeEmpFIO;
    private javax.swing.JLabel jLChangeEmpPosotion;
    private javax.swing.JLabel jLDentist;
    private javax.swing.JLabel jLDepartmentEmployeer;
    private javax.swing.JLabel jLDermatovenerologist;
    private javax.swing.JLabel jLDiphtheria;
    private javax.swing.JLabel jLEmailEmployeer;
    private javax.swing.JLabel jLExpertInNarcology;
    private javax.swing.JLabel jLFIOEmployeer;
    private javax.swing.JLabel jLFluorography;
    private javax.swing.JLabel jLGroupEmployeer;
    private javax.swing.JLabel jLHelminths;
    private javax.swing.JLabel jLHepatitis_A;
    private javax.swing.JLabel jLHepatitis_A2;
    private javax.swing.JLabel jLHepatitis_B;
    private javax.swing.JLabel jLHepatitis_B2;
    private javax.swing.JLabel jLHepatitis_B3;
    private javax.swing.JLabel jLInstitution;
    private javax.swing.JLabel jLInstitutionAdress;
    private javax.swing.JLabel jLInstitutionId;
    private javax.swing.JLabel jLInstitutionStatus;
    private javax.swing.JLabel jLIntestinalInfection1;
    private javax.swing.JLabel jLMainInstitution;
    private javax.swing.JLabel jLMeasles;
    private javax.swing.JLabel jLMeasles_2;
    private javax.swing.JLabel jLNumMedBook;
    private javax.swing.JLabel jLOtolaryngologist;
    private javax.swing.JLabel jLPhoneEmployeer;
    private javax.swing.JLabel jLPositionEmployeer;
    private javax.swing.JLabel jLPsychiatrist;
    private javax.swing.JLabel jLRubella;
    private javax.swing.JLabel jLShigellvak;
    private javax.swing.JLabel jLStaphylococcus1;
    private javax.swing.JLabel jLTherapist;
    private javax.swing.JLabel jLTypeUnit;
    private javax.swing.JLabel jLTyphoidFever1;
    private javax.swing.JLabel jLValidation1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JLabel jLhepatitisAvaccine;
    private javax.swing.JMenuItem jMIChangeMedicalBook;
    private javax.swing.JPopupMenu jPMBaseEmployeeUnder;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTextArea jTAMedicalBookNote;
    private javax.swing.JTextArea jTANoteMainEmployeer;
    private javax.swing.JTable jTEmployeesUnder;
    private javax.swing.JTextField jTFBerthDay;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFEmpId;
    private javax.swing.JTextField jTFLastName;
    private javax.swing.JTextField jTFName;
    private javax.swing.JTextField jTFPatranymic;
    private javax.swing.JTextField jTFPhone;
    private javax.swing.JTextField jTFPhone2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField13;
    // End of variables declaration//GEN-END:variables

    @Override
    public MainInternalFrame choise(InternalFrameUI JIFcallingFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MainInternalFrame choiseMain(InternalFrameUI JIFcallingFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MainInternalFrame view() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
