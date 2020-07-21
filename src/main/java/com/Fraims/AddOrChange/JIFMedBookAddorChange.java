
package com.Fraims.AddOrChange;

import com.Class.Employee;
import com.Class.Institution;
//import Class.JXDatePicker;
import com.Class.MedicalBook;
import com.Class.UserPrivilege;
import com.Progect.MedicalBookProgectMaven;
import static com.Class.UserPrivilege.PrivilegeDefault.*;
import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import com.notUse.MainInternalFrame;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.InternalFrameUI;


public class JIFMedBookAddorChange extends MainInternalFrame {
    private int AuthorizedUserId;
    private int SelectedNumMedicalBook;
    private int SelectedEmployeeId;
    private int SelectedRegionId;
    
    private boolean IsNewMedicalBook;
    private boolean IsNewEmployee;
    
    private UserPrivilege userPrivilege;    
    
    private boolean IsChoiseMedicalBook;
        
    private Employee employee;
    private Employee Newemployee;
    private MedicalBook medicalBook;
    private Institution institution;


    private MedicalBookProgectMaven MBProgect;
    

    public JIFMedBookAddorChange setEmployee(int SelectedEmployeeId) {        
        this.SelectedEmployeeId = SelectedEmployeeId;
        Newemployee = new SQLQuery<>(new Employee()).Read(SelectedEmployeeId);
        Newemployee.setId(SelectedEmployeeId);
        IsNewEmployee = true;
        jButton2.setEnabled(true);
        medicalBook.setEmployeeId(SelectedEmployeeId);
        institution = new SQLQuery<>(new Institution()).Read(Newemployee.getInstitutionId());
        FillFieldsEmployee(Newemployee);
        return this;
    }

    public JIFMedBookAddorChange setNumMedicalBook(int SelectedNumMedicalBook) {        
        this.SelectedNumMedicalBook = SelectedNumMedicalBook;   
        FillFieldsMedBook();
        return this;
    }
    

    public JIFMedBookAddorChange(MedicalBookProgectMaven MedicalBookProgect) {
        super(MedicalBookProgect);
        userPrivilege = MedicalBookProgect.getUserPrivilegeLogin();
        MBProgect = MedicalBookProgect;
        AuthorizedUserId = MedicalBookProgect.getAuthorizedUser().getId();
        SelectedEmployeeId = MBProgect.getSelectedEmployeeId();
        SelectedNumMedicalBook = MBProgect.getSelectedNumMedBook();
        SelectedRegionId = MBProgect.getRegionId();
        initComponents(); 
        
        SQLSelectDataRequest(); 
    }
 
    
    private boolean SQLSelectDataRequest(){
        Thread MBThread = new Thread(new Runnable(){
            @Override
            public void run() {
               try{                   
                    IsNewEmployee = false;                    
                    medicalBook = new SQLQuery<>(new MedicalBook()).Read(SelectedNumMedicalBook);
                    employee = new SQLQuery<>(new Employee()).Read(medicalBook.getEmployeeId());
                    institution = new SQLQuery<>(new Institution()).Read(employee.getInstitutionId());
                    SelectedEmployeeId = medicalBook.getEmployeeId();
                    if (SelectedNumMedicalBook<=0){
                        medicalBook.setDateAdd(new Date());
                        medicalBook.setEmployeeAddId(AuthorizedUserId);
                        jButton2.setText("ДОБАВИТЬ ЛМК");
                        //jTFEmploymentDate.setEnabled(false);
                        jButton2.setEnabled(false);
                        IsNewMedicalBook = true;                        
                    }
                    else {
                        medicalBook.setDateChange(new Date());
                        medicalBook.setEmployeeChangeId(AuthorizedUserId);  
                        jButton2.setText("ИЗМЕНИТЬ ЛМК");
                        jButton2.setEnabled(true);
                        IsNewMedicalBook = false;
                        //jTFEmploymentDate.setEnabled(true);
                        }
                    medicalBook.setRegionId(MBProgect.getRegionId());

                    FillFieldsEmployee(employee);
                    FillFieldsMedBook();
                              
                    medicalBook.setRegionId(SelectedRegionId);
                    employee.setRegionId(SelectedRegionId);
                    institution.setRegionId(SelectedRegionId);
                }catch(Exception ex){
                    System.out.println("Fraims.JIFAddorChangeMedicalBook.SQLSelectDataRequest() -"+ex.getMessage());
                }               
            }
        });
        MBThread.run();
        return true; 
    }

    // ====================== установка значений в поля ========================    
    private void FillFieldsEmployee(Employee Empl){
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        jBChoiseEmployee.setEnabled(MBProgect.getStringPrivEmployee().contains("VIEW") || MBProgect.getStringPrivEmployee().contains("CHANGE"));
        jBEmployeeCuratorDelete.setEnabled(jBChoiseEmployee.isEnabled());
        jChBMedicalBookIsDeleted.setEnabled(userPrivilege.getPrivMedicalBook().contains(DELET_ALL));
        // ==================== ГЛАВНАЯ ====================    
        if (Empl.getId()>0){
            //employee = employee.getOneEmployee(SelectedEmployeeId, new SQLConnection());
            jLEmplId.setText(Empl.getId().toString());
            jLEmpPhone.setText(Empl.getPhone1()+ " "+Empl.getPhone2());
            //jChBEmpisWork.setSelected(employee.isIsDeleted());
            jLEmpFIO.setText(Empl.getFullName());
            jLEmpBerthday.setText(df.format(Empl.getBerthday()).equals("01.01.1970") ? "" : df.format(Empl.getBerthday()));
            jLEmpGroup.setText(Empl.getUsGroupName());    
            jLEmpDepartment.setText(Empl.getUsDepartmentName());  
            jLEmpPosition.setText(Empl.getUsPositionName()); 
            jLabel8.setText(df.format(Empl.getEmploymentDate()));  
                        
            //jTAEmployeeNote.setText(employee.getNote());
        } else {
            jLEmplId.setText("сотрудник не выбран");
            //jChBEmpisWork.setSelected(employee.isIsDeleted());

            jLEmpFIO.setText("сотрудник не выбран");
            jLEmpPhone.setText("сотрудник не выбран");
            jLEmpBerthday.setText("сотрудник не выбран");
            jLEmpGroup.setText("сотрудник не выбран");    
            jLEmpDepartment.setText("сотрудник не выбран");  
            jLEmpPosition.setText("сотрудник не выбран");  
            jLabel8.setText("сотрудник не выбран");  
            
            //jTAEmployeeNote.setText("сотрудник не выбран");
        }          
        if (institution.getId()>0){
            jLMainInstitution.setText(institution.getMainInstitutionName().equals("") ? "Не выбран" : institution.getMainInstitutionName()); 
            jLInstitution.setText(institution.getName());
            jLInstitutionAdress.setText(institution.getAddress());
            jLInstitutionStatus.setText(institution.getIsClosed()? "Закрыто" : "Работает");
        }else{
            jLMainInstitution.setText("Учереждение не выбрано"); 
            jLInstitution.setText("Учереждение не выбрано");
            jLInstitutionAdress.setText("Учереждение не выбрано");
            jLInstitutionStatus.setText("Учереждение не выбрано");
        }

        // ==================== фото ====================
//        jLabel3.setText("");
//        jLabel5.setText("нет фото");
    }
    
    // ==================== Медкнижка ====================
    private void FillFieldsMedBook(){        
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (SelectedNumMedicalBook>0){
            
            Calendar CalMinus55Year = new GregorianCalendar();
            CalMinus55Year.add(Calendar.YEAR, -55);
            
            jTFRubella.setEnabled(medicalBook.getEmployeeId()>0 ? employee.getBerthday().after(CalMinus55Year.getTime()) : true);
            jTFHepatitis_B.setEnabled(jTFRubella.isEnabled());
            jTFHepatitis_B2.setEnabled(jTFRubella.isEnabled());
            jTFHepatitis_B3.setEnabled(jTFRubella.isEnabled());
            jTFMeasles.setEnabled(jTFRubella.isEnabled());
            jTFMeasles_2.setEnabled(jTFRubella.isEnabled());
            //jTFEmploymentDate.setEnabled(jTFRubella.isEnabled());
            
            // ==================== главная ====================
            jTFNumMedBook.setText(medicalBook.getNumMedicalBook().toString());
            jChBMedicalBookIsDeleted.setSelected(medicalBook.isIsDeleted());
            jLEmploymentDate.setText(df.format(medicalBook.getEmploymentDate()));
            
            // ==================== прививки ====================
            String str = enumToString(medicalBook.getBorneDiseases());
            StringBuilder borneDiseases = new StringBuilder();
            if (!str.isEmpty()){
                if (str.contains("Rubella")){
                    borneDiseases.append("Краснуха,");
                    jTFRubella.setEnabled(false);
                }
                if (str.contains("Measles")){
                    borneDiseases.append("Корь,");                
                    jTFMeasles.setEnabled(false);
                    jTFMeasles_2.setEnabled(false);
                }
                if (str.contains("Diphtheria")){
                    borneDiseases.append("Дифтерия,");
                    jTFDiphtheria.setEnabled(false);
                }
                if (str.contains("Hepatitis_A")){
                    borneDiseases.append("Геппатит А,");
                    jTFHepatitis_A.setEnabled(false);
                    jTFHepatitis_A2.setEnabled(false);
                }
                if (str.contains("Hepatitis_B")){
                    borneDiseases.append("Геппатит Б,");
                    jTFHepatitis_B.setEnabled(false);
                    jTFHepatitis_B2.setEnabled(false);
                    jTFHepatitis_B3.setEnabled(false);
                }
                
                jTFBorneDiseases.setText(borneDiseases.toString().substring(0, borneDiseases.length() - 1));
            }

            jTFRubella.setText(df.format(medicalBook.getRubella()).equals("01.01.1970") ? "" : df.format(medicalBook.getRubella()));
            jTFDiphtheria.setText(df.format(medicalBook.getDiphtheria()).equals("01.01.1970") ? "" : df.format(medicalBook.getDiphtheria()));

            jTFMeasles.setText(df.format(medicalBook.getMeasles()).equals("01.01.1970") ? "" : df.format(medicalBook.getMeasles()));
            jTFMeasles_2.setText(df.format(medicalBook.getMeasles_2()).equals("01.01.1970") ? "" : df.format(medicalBook.getMeasles_2()));

            jTFHepatitis_B.setText(df.format(medicalBook.getHepatitis_B()).equals("01.01.1970") ? "" : df.format(medicalBook.getHepatitis_B()));
            jTFHepatitis_B2.setText(df.format(medicalBook.getHepatitis_B2()).equals("01.01.1970") ? "" : df.format(medicalBook.getHepatitis_B2()));
            jTFHepatitis_B3.setText(df.format(medicalBook.getHepatitis_B3()).equals("01.01.1970") ? "" : df.format(medicalBook.getHepatitis_B3()));

            jTFHepatitis_A.setText(df.format(medicalBook.getHepatitis_A()).equals("01.01.1970") ? "" : df.format(medicalBook.getHepatitis_A()));
            jTFHepatitis_A2.setText(df.format(medicalBook.getHepatitis_A2()).equals("01.01.1970") ? "" : df.format(medicalBook.getHepatitis_A2()));
            
            if (enumToString(medicalBook.getHepatitisAvaccine()).equals(""))
                jCBhepatitisAvaccine.setSelectedItem(0);
            else jCBhepatitisAvaccine.setSelectedItem(enumToString(medicalBook.getHepatitisAvaccine()));

            // ==================== периодиеские ==================== 
            jTFShigellvak.setText(df.format(medicalBook.getShigellvak()).equals("01.01.1970") ? "" : df.format(medicalBook.getShigellvak()));

            jTFTherapist.setText(df.format(medicalBook.getTherapist()).equals("01.01.1970") ? "" : df.format(medicalBook.getTherapist()));
            jTFOtolaryngologist.setText(df.format(medicalBook.getOtolaryngologist()).equals("01.01.1970") ? "" : df.format(medicalBook.getOtolaryngologist()));
            jTFDentist.setText(df.format(medicalBook.getDentist()).equals("01.01.1970") ? "" : df.format(medicalBook.getDentist()));
            jTFPsychiatrist.setText(df.format(medicalBook.getPsychiatrist()).equals("01.01.1970") ? "" : df.format(medicalBook.getPsychiatrist()));
            jTFExpertInNarcology.setText(df.format(medicalBook.getExpertInNarcology()).equals("01.01.1970") ? "" : df.format(medicalBook.getExpertInNarcology()));
            jTFDermatovenerologist.setText(df.format(medicalBook.getDermatovenerologist()).equals("01.01.1970") ? "" : df.format(medicalBook.getDermatovenerologist()));
            jTFFluorography.setText(df.format(medicalBook.getFluorography()).equals("01.01.1970") ? "" : df.format(medicalBook.getFluorography()));
            jTFValidation.setText(df.format(medicalBook.getValidation()).equals("01.01.1970") ? "" : df.format(medicalBook.getValidation()));
            jTFHelminths.setText(df.format(medicalBook.getHelminths()).equals("01.01.1970") ? "" : df.format(medicalBook.getHelminths()));
            jCBValidationForYear.setSelected(medicalBook.isValidationForYear());

            // ==================== при поступлении ====================
            jTFIntestinalInfection.setText(df.format(medicalBook.getIntestinalInfection()).equals("01.01.1970") ? "" : df.format(medicalBook.getIntestinalInfection()));
            jTFTyphoidFever.setText(df.format(medicalBook.getTyphoidFever()).equals("01.01.1970") ? "" : df.format(medicalBook.getTyphoidFever()));
            jTFStaphylococcus.setText(df.format(medicalBook.getStaphylococcus()).equals("01.01.1970") ? "" : df.format(medicalBook.getStaphylococcus()));
            jTAMedBookNote.setText(medicalBook.getNote());

            // ==================== фото лмк ==================== 
            jLabel89.setText("");
            jLabel4.setText("в разработке");
        }else {
            jTFNumMedBook.setText("Введите номер");
            jChBMedicalBookIsDeleted.setSelected(false);
            jLEmploymentDate.setText("");
            // ==================== прививки ====================
            jTFBorneDiseases.setText("");

            jTFRubella.setText("");
            jTFDiphtheria.setText("");

            jTFMeasles.setText("");      
            jTFMeasles_2.setText("");

            jTFHepatitis_B.setText("");
            jTFHepatitis_B2.setText("");
            jTFHepatitis_B3.setText("");

            jTFHepatitis_A.setText("");
            jTFHepatitis_A2.setText("");
            jCBhepatitisAvaccine.setSelectedIndex(0);

            // ==================== периодиеские ====================
            jTFShigellvak.setText("");

            jTFTherapist.setText("");
            jTFOtolaryngologist.setText("");
            jTFDentist.setText("");
            jTFPsychiatrist.setText("");
            jTFExpertInNarcology.setText("");
            jTFDermatovenerologist.setText("");
            jTFFluorography.setText("");
            jTFValidation.setText("");
            jTFHelminths.setText("");
            jCBValidationForYear.setSelected(false); 

            // ==================== при поступлении ====================
            jTFIntestinalInfection.setText("");
            jTFTyphoidFever.setText("");
            jTFStaphylococcus.setText("");
            jTAMedBookNote.setText("");

            //==================== фото лмк ====================
            jLabel89.setText("");
            jLabel4.setText("нет фото");
        }
    }
  
    //====================== получение значений из полей ======================

    private MedicalBook getMedicalBookFromFields(){        
        MedicalBook medBook = new MedicalBook();
        medBook = medicalBook;
        try{
            SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy"); 
            medBook.setNumMedicalBook(Integer.valueOf(jTFNumMedBook.getText()));
            medBook.setIsDeleted(jChBMedicalBookIsDeleted.isSelected());
//            Rubella,
//            Diphtheria,
//            Measles,
//            Hepatitis_A,
//            Hepatitis_B
            StringBuilder borneDiseases = new StringBuilder();
            if (!jTFBorneDiseases.getText().isEmpty()){
                if (jTFBorneDiseases.getText().contains("краснуха") || jTFBorneDiseases.getText().contains("Краснуха"))
                    borneDiseases.append("Rubella,");
                if (jTFBorneDiseases.getText().contains("Корь") || jTFBorneDiseases.getText().contains("корь"))
                    borneDiseases.append("Measles,");
                if (jTFBorneDiseases.getText().contains("Дифтерия") || jTFBorneDiseases.getText().contains("дифтерия"))
                    borneDiseases.append("Diphtheria,");
                if (jTFBorneDiseases.getText().contains("Геппатит А") || jTFBorneDiseases.getText().contains("геппатит А"))
                    borneDiseases.append("Hepatitis_A,");
                if (jTFBorneDiseases.getText().contains("Геппатит Б") || jTFBorneDiseases.getText().contains("геппатит Б"))
                    borneDiseases.append("Hepatitis_B,");
                
                medBook.setBorneDiseases(stringToEnumBorneDeseases(borneDiseases.toString().substring(0, borneDiseases.length() - 1)));
            }           

            //medBook.setEmploymentDate(ft.parse(jTFEmploymentDate.getText().equals("") ? "01.01.1970" : jTFEmploymentDate.getText()));
            
            medBook.setRubella(ft.parse(jTFRubella.getText().equals("") ? "01.01.1970" : jTFRubella.getText()));
            medBook.setDiphtheria(ft.parse(jTFDiphtheria.getText().equals("") ? "01.01.1970" : jTFDiphtheria.getText()));

            medBook.setMeasles(ft.parse(jTFMeasles.getText().equals("") ? "01.01.1970" : jTFMeasles.getText()));
            medBook.setMeasles_2(ft.parse(jTFMeasles_2.getText().equals("") ? "01.01.1970" : jTFMeasles_2.getText()));

            medBook.setHepatitis_B(ft.parse(jTFHepatitis_B.getText().equals("") ? "01.01.1970" : jTFHepatitis_B.getText()));
            medBook.setHepatitis_B2(ft.parse(jTFHepatitis_B2.getText().equals("") ? "01.01.1970" : jTFHepatitis_B2.getText()));
            medBook.setHepatitis_B3(ft.parse(jTFHepatitis_B3.getText().equals("") ? "01.01.1970" : jTFHepatitis_B3.getText()));

            medBook.setHepatitis_A(ft.parse(jTFHepatitis_A.getText().equals("") ? "01.01.1970" : jTFHepatitis_A.getText()));
            medBook.setHepatitis_A2(ft.parse(jTFHepatitis_A2.getText().equals("") ? "01.01.1970" : jTFHepatitis_A2.getText()));
            
            if (jCBhepatitisAvaccine.getSelectedIndex()==0)
                medBook.setHepatitisAvaccine(StringToEnumHepatitisAvaccine(""));
            else  medBook.setHepatitisAvaccine(StringToEnumHepatitisAvaccine(jCBhepatitisAvaccine.getSelectedItem().toString()));

            medBook.setShigellvak(ft.parse(jTFShigellvak.getText().equals("") ? "01.01.1970" : jTFShigellvak.getText()));

            medBook.setTherapist(ft.parse(jTFTherapist.getText().equals("") ? "01.01.1970" : jTFTherapist.getText()));
            medBook.setOtolaryngologist(ft.parse(jTFOtolaryngologist.getText().equals("") ? "01.01.1970" : jTFOtolaryngologist.getText()));
            medBook.setDentist(ft.parse(jTFDentist.getText().equals("") ? "01.01.1970" : jTFDentist.getText()));
            medBook.setPsychiatrist(ft.parse(jTFPsychiatrist.getText().equals("") ? "01.01.1970" : jTFPsychiatrist.getText()));
            medBook.setExpertInNarcology(ft.parse(jTFExpertInNarcology.getText().equals("") ? "01.01.1970" : jTFExpertInNarcology.getText()));
            medBook.setDermatovenerologist(ft.parse(jTFDermatovenerologist.getText().equals("") ? "01.01.1970" : jTFDermatovenerologist.getText()));
            medBook.setFluorography(ft.parse(jTFFluorography.getText().equals("") ? "01.01.1970" : jTFFluorography.getText()));
            medBook.setValidation(ft.parse(jTFValidation.getText().equals("") ? "01.01.1970" : jTFValidation.getText()));
            medBook.setHelminths(ft.parse(jTFHelminths.getText().equals("") ? "01.01.1970" : jTFHelminths.getText()));
            medBook.setIntestinalInfection(ft.parse(jTFIntestinalInfection.getText().equals("") ? "01.01.1970" : jTFIntestinalInfection.getText()));
            medBook.setTyphoidFever(ft.parse(jTFTyphoidFever.getText().equals("") ? "01.01.1970" : jTFTyphoidFever.getText()));
            medBook.setStaphylococcus(ft.parse(jTFStaphylococcus.getText().equals("") ? "01.01.1970" : jTFStaphylococcus.getText()));

            medBook.setEmployeeId(SelectedEmployeeId);

            medBook.setEmployeeChangeId(AuthorizedUserId);
            medBook.setDateChange(new Date()); 
            
//            if (SelectedEmployeeId<=0){
//                medBook.setEmployeeAddId(AuthorizedUserId);  
//                medBook.setDateAdd(new Date());      
//            }else {
//                medBook.setEmployeeChangeID(AuthorizedUserId);
//                medBook.setDateChange(new Date()); 
//            }
            
            medBook.setNote(jTAMedBookNote.getText());
            medBook.setRegionId(employee.getRegionId());
            medBook.setValidationForYear(jCBValidationForYear.isSelected());
            
            // ================= фото ================
            //
            // ================= ================ ================
            
        }catch(Exception ex){
            ex.getMessage();
            System.out.println("Fraims.JIFAddMedicalBook.getFildsMedicalBook()" + ex.getMessage());
        }          
        return medBook;
    }
    
    // ====================== установка значений в таблицы ========================
   
    //====================== запросы в SQL ======================  
    
    
    private void SaveOrAdd(){        
        // при создании новой ЛМК
        // medicalbook = -1
        // employee = -1

        // А также при создании новой ЛМК
        // Newmedicalbook = -1
        // Newemployee = -1
        
        //после выбора валидного номера 
        //Newmedicalbook = номер ЛМК
        
        //после выбора нового пользователя
        //medicalbook = номер Пользователя
        Integer Check = verificationNumMedicalBook();
        
        try{
            if (Check>0){
            //Собираем данные с полей и сохраняем в новую переменную 
            // основанной на переменной medicalbook
            MedicalBook newMedicalBook = getMedicalBookFromFields();     
            if (!IsNewEmployee){ //если новый сотрудик не выбран
                // создаем или крректируем выбранную ЛМК
                newMedicalBook.setEmployeeId(newMedicalBook.getEmployeeId());
                if (Check==2){
                    employee.setNumMedicalBook(-1);
                    new SQLQuery<>(employee).Save(employee.getId());
                    new SQLQuery<>(newMedicalBook).Save(medicalBook.getNumMedicalBook());
                    employee.setNumMedicalBook(newMedicalBook.getNumMedicalBook());
                    new SQLQuery<>(employee).Save(employee.getId());
                }
                else 
                    new SQLQuery<>(newMedicalBook).SaveOrWrite(newMedicalBook.getNumMedicalBook());
            }else { // если сотрудик выбран
                if (employee.getId()!=Newemployee.getId()){ // если новый сотрудник отличается от сотрдника по умолчанию
                    // если выбран новый сотруднк, вносим изменения в данные старого сотрудника
                    if (employee.getId()>0){
                        employee.setNumMedicalBook(-1);
                        new SQLQuery<>(employee).Save(employee.getId());
                    }
                    // также вносим изменения у нового сотрудника
                    if (Newemployee.getId()>0){
                        Newemployee.setNumMedicalBook(newMedicalBook.getNumMedicalBook());
                        new SQLQuery<>(Newemployee).Save(Newemployee.getId());
                    }
                    // и сохраняем данные о новом сотруднике в выбранной ЛМК
                    newMedicalBook.setEmployeeId(Newemployee.getId());
                    
                    if (Check==2){
                        //newMedicalBook.setNumMedicalBook(medicalBook.getNumMedicalBook());
                        new SQLQuery<>(newMedicalBook).Save(medicalBook.getNumMedicalBook());
                    }else 
                        new SQLQuery<>(newMedicalBook).SaveOrWrite(newMedicalBook.getNumMedicalBook()); 
                }else { // если новый и старый сотруднк одинаковые (не было измененно)
                    //вносим изменения в ЛМК
                    if (Check==2){
                        //newMedicalBook.setNumMedicalBook(medicalBook.getNumMedicalBook());
                        new SQLQuery<>(newMedicalBook).Save(medicalBook.getNumMedicalBook());
                    }else 
                        new SQLQuery<>(newMedicalBook).SaveOrWrite(newMedicalBook.getNumMedicalBook()); 
                }                             
            }

            if (MBProgect.getJIFMedBookBase()!=null) MBProgect.getJIFMedBookBase().FillDataToTable();
            if (MBProgect.getJIFEmployeesBase()!=null) MBProgect.getJIFEmployeesBase().verificationData();
            closeThis(); 
            }
        }catch(Exception ex){
            System.out.println("Fraims.JIFAddorChangeEmployee.SaveOrAdd() " + ex.getMessage());
        }
    }
    
    // ====================== Прочие функции =====================
   
    private void closeThis(){
        this.setVisible(false);        
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedEmployeeId(-1);        
        MBProgect.setSelectedInstitutionId(-1); 
        MBProgect.setJIFMedBookAddorChange(null);
    }
    
    
    
    private JInternalFrame setLocationAndSizeWindow(JInternalFrame JInternalFrame, Dimension FrameAddPriceLocation) {
        try {
            JInternalFrame.setSelected(true);
            Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
            JInternalFrame.setSize(FrameAddPriceLocation);
            JInternalFrame.setLocation(sSize.width/2-JInternalFrame.getWidth()/2, sSize.height/2-JInternalFrame.getHeight()/2);
            JInternalFrame.setVisible(true);
            return JInternalFrame;
        } catch (PropertyVetoException ex) {           
            return JInternalFrame;
        }
    }
    private boolean checkNumMedicalBook(){
        Boolean check = false;
        if (isDigit(jTFNumMedBook.getText())){
            Integer nemMedBook = Integer.valueOf(jTFNumMedBook.getText());
            MedicalBook medbook = new MedicalBook();
            Employee empl = new Employee();
            Boolean IsNumMedBookExist = medbook.isNumMedicalBookExist(new SQLConnection(),nemMedBook); 
            Integer emlpId = employee.CheckNumMedicalBookFromEmployee(new SQLConnection(), nemMedBook);
            if (IsNumMedBookExist){
                check = false;
            }else if(emlpId>0){                                
                check = false;
            } else check = true;
        } else check = false;
        return check;
    }
    
    
    private static boolean isDigit(String s) throws NumberFormatException {
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
    private void showDialogOr(String Message){
        Object[] options = {"закрыть",};
            int n = JOptionPane.showOptionDialog(null,
                Message,
                "Внимание",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
            if(n==0){                 
                
            }
    }
    private Integer showDialogYN(String Message){
        Object[] options = {"ДА", "Нет, закрыть"};
            Integer n = JOptionPane.showOptionDialog(null,
                Message,
                "Внимание!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        return n;
    }
    private Integer verificationNumMedicalBook(){
        Integer check = 0;
        if (isDigit(jTFNumMedBook.getText())){
            Integer IsNumMedBookExistInMedBookBase = new SQLQuery<>(new MedicalBook()).isKeyExist(Integer.valueOf(jTFNumMedBook.getText()));
            //Integer emlpId = employee.CheckNumMedicalBookFromEmployee(new SQLConnection(), Integer.valueOf(jTFNumMedBook.getText());
            
            jButton2.setEnabled(false);
            if (IsNewMedicalBook){ // если вносим новую ЛМК             
                if (IsNumMedBookExistInMedBookBase>0){//если книжка есть в базе           
                    showDialogOr("Данный номер ЛМК уже зарегистрирован в базе ЛМК");
                    jTFNumMedBook.setText("Введите номер");
                }else {
                    jButton2.setEnabled(true);
                    check = 1;
                    }
            } else { // если изменяем старую ЛМК
                if (!medicalBook.getNumMedicalBook().equals(Integer.valueOf(jTFNumMedBook.getText()))){ // если новый номер отличается от номера старой ЛМК
                    Integer n = showDialogYN("Хотите изменить номер ЛМК?");
                    if (n==0) // выбрали, что хотим изменить
                        if (IsNumMedBookExistInMedBookBase>0){//если книжка есть в базе 
                            showDialogOr("Данный номер ЛМК уже зарегистрирован в базе ЛМК");
                            jTFNumMedBook.setText("Введите номер");
                        }else{ 
                            jButton2.setEnabled(true);
                            check = 2;
                        }
                    else{
                        jTFNumMedBook.setText(medicalBook.getNumMedicalBook().toString());
                        jButton2.setEnabled(true);
                        check = 1;
                    }                                        
                }else{ 
                    check = 1;
                    jButton2.setEnabled(true);
                } 
            }
        }else showDialogOr("Номер ЛМК должен быть числом");  
        //if (jButton2.isEnabled()) showDialogOr("Данный номер ЛМК не числитс в базе ЛМК", "Корректно");
        return check;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel89 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jTFRubella = new javax.swing.JTextField();
        jTFDiphtheria = new javax.swing.JTextField();
        jTFMeasles = new javax.swing.JTextField();
        jTFMeasles_2 = new javax.swing.JTextField();
        jTFHepatitis_B = new javax.swing.JTextField();
        jTFHepatitis_B2 = new javax.swing.JTextField();
        jTFHepatitis_B3 = new javax.swing.JTextField();
        jTFHepatitis_A = new javax.swing.JTextField();
        jTFHepatitis_A2 = new javax.swing.JTextField();
        jTFBorneDiseases = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jCBhepatitisAvaccine = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jTFShigellvak = new javax.swing.JTextField();
        jTFTherapist = new javax.swing.JTextField();
        jTFOtolaryngologist = new javax.swing.JTextField();
        jTFDentist = new javax.swing.JTextField();
        jTFPsychiatrist = new javax.swing.JTextField();
        jTFExpertInNarcology = new javax.swing.JTextField();
        jTFDermatovenerologist = new javax.swing.JTextField();
        jTFFluorography = new javax.swing.JTextField();
        jTFValidation = new javax.swing.JTextField();
        jTFHelminths = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jCBValidationForYear = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jTFIntestinalInfection = new javax.swing.JTextField();
        jTFTyphoidFever = new javax.swing.JTextField();
        jTFStaphylococcus = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jTAMedBookNote = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLEmpFIO = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLEmplId = new javax.swing.JLabel();
        jBChoiseEmployee = new javax.swing.JButton();
        jLEmpBerthday = new javax.swing.JLabel();
        jLEmpPhone = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLEmpGroup = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLEmpDepartment = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLEmpPosition = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLMainInstitution = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLInstitution = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLInstitutionAdress = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLInstitutionStatus = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jBEmployeeCuratorDelete = new javax.swing.JButton();
        jLabel84 = new javax.swing.JLabel();
        jLEmploymentDate = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel81 = new javax.swing.JLabel();
        jTFNumMedBook = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jChBMedicalBookIsDeleted = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel94.setText("Выданные направления");

        jButton1.setText("Выдать направление");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel94)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 220, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(17, 17, 17))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(jButton1))
                .addContainerGap(437, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton4.setText("Добавить фото");
        jButton4.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("нет фото");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("Х");
        jButton3.setEnabled(false);

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
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 341, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addComponent(jLabel89, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13))
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
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jButton5.setText("Добавить фото");

        jButton6.setText("Х");

        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Пусть к файлу: ");

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
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                        .addComponent(jButton6)))
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
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Добавить изменить ЛМК");
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

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jTFRubella.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFRubella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFRubellaActionPerformed(evt);
            }
        });

        jTFDiphtheria.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFMeasles.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFMeasles_2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFHepatitis_B.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFHepatitis_B2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFHepatitis_B3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFHepatitis_A.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFHepatitis_A2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFBorneDiseases.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setText("Краснуха");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel34.setText("Формат даты: дд.мм.гггг");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setText("АДС");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel36.setText("Формат даты: дд.мм.гггг");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setText("ЖКВ");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel38.setText("Формат даты: дд.мм.гггг");

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel41.setText("R - ЖКВ");

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel42.setText("Формат даты: дд.мм.гггг");

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel43.setText("Геппатит В");

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel44.setText("Формат даты: дд.мм.гггг");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel45.setText("Геппатит В 2");

        jLabel46.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel46.setText("Формат даты: дд.мм.гггг");

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel47.setText("Геппатит В 3");

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel48.setText("Формат даты: дд.мм.гггг");

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel49.setText("Геппатит А");

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel50.setText("Формат даты: дд.мм.гггг");

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel51.setText("Геппатит А 2");

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel52.setText("Формат даты: дд.мм.гггг");

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel53.setText("Вакцина");

        jLabel72.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel72.setText("Перенесенные заболевания");

        jLabel11.setText("Пример: Краснуха, Корь, ...");

        jCBhepatitisAvaccine.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "не выбрано", "Algawak", "Avaxim", "Wakta" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFBorneDiseases, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFRubella, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel34))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFDiphtheria, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFMeasles, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel38))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFMeasles_2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel42))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFHepatitis_B, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel44))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFHepatitis_B2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel46))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFHepatitis_B3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel48))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFHepatitis_A, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel50))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFHepatitis_A2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel52))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBhepatitisAvaccine, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(jTFBorneDiseases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jTFRubella, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jTFDiphtheria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jTFMeasles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jTFMeasles_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jTFHepatitis_B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jTFHepatitis_B2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(jTFHepatitis_B3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jTFHepatitis_A, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jTFHepatitis_A2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jCBhepatitisAvaccine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(176, 176, 176))
        );

        jTabbedPane1.addTab("ЛМК - Прививки", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jTFShigellvak.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFTherapist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFTherapist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFTherapistActionPerformed(evt);
            }
        });

        jTFOtolaryngologist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFOtolaryngologist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFOtolaryngologistActionPerformed(evt);
            }
        });

        jTFDentist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFPsychiatrist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFExpertInNarcology.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFDermatovenerologist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFFluorography.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFFluorography.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFFluorographyActionPerformed(evt);
            }
        });

        jTFValidation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFHelminths.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFHelminths.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFHelminthsActionPerformed(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel54.setText("Шигелвак");

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel55.setText("Формат даты: дд.мм.гггг");

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel56.setText("Терапевт");

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel57.setText("Формат даты: дд.мм.гггг");

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel58.setText("ЛОР");

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel59.setText("Формат даты: дд.мм.гггг");

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel60.setText("Стоматолог");

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel61.setText("Формат даты: дд.мм.гггг");

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel62.setText("Психиатор");

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel63.setText("Формат даты: дд.мм.гггг");

        jLabel64.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel64.setText("Нарколог");

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel65.setText("Формат даты: дд.мм.гггг");

        jLabel66.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel66.setText("КВД");

        jLabel67.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel67.setText("Формат даты: дд.мм.гггг");

        jLabel68.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel68.setText("ФЛГ");

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel69.setText("Формат даты: дд.мм.гггг");

        jLabel70.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel70.setText("Аттестация");

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel71.setText("Формат даты: дд.мм.гггг");

        jLabel73.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel73.setText("Гельминты");

        jLabel74.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel74.setText("Формат даты: дд.мм.гггг");

        jCBValidationForYear.setBackground(new java.awt.Color(255, 255, 255));
        jCBValidationForYear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBValidationForYear.setText("Годовалая аттестация");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCBValidationForYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFHelminths, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel74))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel71))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFShigellvak, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel55))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFTherapist, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel57))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFOtolaryngologist, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel59))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFDentist, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel61))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFPsychiatrist, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel63))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFExpertInNarcology, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel65))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFDermatovenerologist, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel67))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFFluorography, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel69)))
                .addGap(91, 91, 91))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jTFShigellvak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jTFTherapist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(jTFOtolaryngologist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60)
                    .addComponent(jTFDentist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(jTFPsychiatrist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(jTFExpertInNarcology, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(jTFDermatovenerologist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(jTFFluorography, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(jTFValidation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel71))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBValidationForYear)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(jTFHelminths, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74))
                .addContainerGap())
        );

        jTabbedPane1.addTab("ЛМК - Периодические", jPanel6);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel75.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel75.setText("Бак анализ");
        jLabel75.setMaximumSize(new java.awt.Dimension(51, 14));
        jLabel75.setMinimumSize(new java.awt.Dimension(51, 14));
        jLabel75.setPreferredSize(new java.awt.Dimension(51, 14));

        jLabel77.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel77.setText("Брюшной тиф");

        jLabel79.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel79.setText("Стефилококк");

        jTFIntestinalInfection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFTyphoidFever.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFStaphylococcus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel76.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel76.setText("Формат даты: дд.мм.гггг");

        jLabel78.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel78.setText("Формат даты: дд.мм.гггг");

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel80.setText("Формат даты: дд.мм.гггг");

        jTAMedBookNote.setColumns(20);
        jTAMedBookNote.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTAMedBookNote.setRows(5);
        jTAMedBookNote.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));

        jLabel7.setText("Дата траудоустройства сотрудника: ");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTAMedBookNote)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                            .addComponent(jLabel75, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFStaphylococcus, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFTyphoidFever, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFIntestinalInfection, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel80)
                            .addComponent(jLabel78)
                            .addComponent(jLabel76))))
                .addGap(12, 12, 12))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFIntestinalInfection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel76))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(jTFTyphoidFever, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel78))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(jTFStaphylococcus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80))
                .addGap(18, 18, 18)
                .addComponent(jTAMedBookNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("ЛМК - При поступлении", jPanel7);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("ФИО");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Дата рождения");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Телефон");

        jLEmpFIO.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmpFIO.setText("сотрудник не выбран");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Сотрудник");

        jLEmplId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmplId.setText("сотрудник не выбран");
        jLEmplId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLEmplIdMouseClicked(evt);
            }
        });

        jBChoiseEmployee.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBChoiseEmployee.setText("Выбрать сотрудника");
        jBChoiseEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBChoiseEmployeeActionPerformed(evt);
            }
        });

        jLEmpBerthday.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmpBerthday.setText("сотрудник не выбран");

        jLEmpPhone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmpPhone.setText("сотрудник не выбран");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Группа");

        jLEmpGroup.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmpGroup.setText("сотрудник не выбран");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setText("Подразделение");

        jLEmpDepartment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmpDepartment.setText("сотрудник не выбран");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Должность");

        jLEmpPosition.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmpPosition.setText("сотрудник не выбран");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Учереждение");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setText("Комплекс");

        jLMainInstitution.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLMainInstitution.setText("учереждение не выбрано");

        jLabel83.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel83.setText("Подразделение");

        jLInstitution.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLInstitution.setText("учереждение не выбрано");

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel82.setText("Адрес");

        jLInstitutionAdress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLInstitutionAdress.setText("учереждение не выбрано");

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel87.setText("Статус учереждения");

        jLInstitutionStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLInstitutionStatus.setText("учереждение не выбрано");

        jBEmployeeCuratorDelete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBEmployeeCuratorDelete.setText("х");
        jBEmployeeCuratorDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEmployeeCuratorDeleteActionPerformed(evt);
            }
        });

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel84.setText("Дата трудоустройства");

        jLEmploymentDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmploymentDate.setText("сотрудник не выбран");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLEmpDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLEmpPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLEmpGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLInstitution, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLMainInstitution, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLInstitutionAdress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLInstitutionStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 25, Short.MAX_VALUE)))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLEmplId, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBChoiseEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBEmployeeCuratorDelete)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLEmpFIO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLEmpBerthday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLEmpPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLEmploymentDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLEmplId)
                    .addComponent(jBChoiseEmployee)
                    .addComponent(jBEmployeeCuratorDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(jLEmploymentDate))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLEmpFIO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLEmpBerthday))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLEmpPhone))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLEmpGroup))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(jLEmpDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLEmpPosition))
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLMainInstitution))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(jLInstitution))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(jLInstitutionAdress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(jLInstitutionStatus))
                .addGap(186, 186, 186))
        );

        jTabbedPane1.addTab("Информация владельца ЛМК", jPanel3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("СОХРАНИТЬ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel81.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel81.setText("Норме ЛМК");

        jTFNumMedBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFNumMedBook.setText("Введите номер");
        jTFNumMedBook.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNumMedBookFocusLost(evt);
            }
        });
        jTFNumMedBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFNumMedBookMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTFNumMedBookMousePressed(evt);
            }
        });
        jTFNumMedBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNumMedBookActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton10.setText("ВЫЙТИ");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jChBMedicalBookIsDeleted.setBackground(new java.awt.Color(255, 255, 255));
        jChBMedicalBookIsDeleted.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jChBMedicalBookIsDeleted.setText("ЛМК удалена");
        jChBMedicalBookIsDeleted.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jChBMedicalBookIsDeleted.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jChBMedicalBookIsDeletedItemStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Проверить ЛМК");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFNumMedBook, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(88, 88, 88)
                        .addComponent(jChBMedicalBookIsDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(jTFNumMedBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jChBMedicalBookIsDeleted)
                    .addComponent(jLabel1))
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
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        SaveOrAdd();       
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTFRubellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFRubellaActionPerformed
        
    }//GEN-LAST:event_jTFRubellaActionPerformed

    private void jTFHelminthsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFHelminthsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFHelminthsActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        closeThis();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jTFTherapistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFTherapistActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFTherapistActionPerformed

    private void jTFFluorographyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFluorographyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFFluorographyActionPerformed

    private void jTFNumMedBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFNumMedBookMouseClicked
        if (jTFNumMedBook.getText().equals("Введите номер"))
            jTFNumMedBook.setText("");
    }//GEN-LAST:event_jTFNumMedBookMouseClicked

    private void jTFNumMedBookMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFNumMedBookMousePressed
        
    }//GEN-LAST:event_jTFNumMedBookMousePressed

    private void jTFNumMedBookFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNumMedBookFocusLost
        if (jTFNumMedBook.getText().equals(""))
            jTFNumMedBook.setText("Введите номер");
        //if (this.isVisible() & this.isEnabled()) verificationNumMedicalBook();
    }//GEN-LAST:event_jTFNumMedBookFocusLost

    private void jBChoiseEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBChoiseEmployeeActionPerformed
        //MBProgect.openChoiseEmployee();
        MBProgect.openBaseEmployees(MedicalBookProgectMaven.statusFrame.CHOISE,this.getUI());
    }//GEN-LAST:event_jBChoiseEmployeeActionPerformed

    private void jTFNumMedBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNumMedBookActionPerformed
        verificationNumMedicalBook();                
    }//GEN-LAST:event_jTFNumMedBookActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        verificationNumMedicalBook();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLEmplIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLEmplIdMouseClicked
        this.setEmployee(-1);
//        Newemployee = new Employee();
//        medicalBook.setEmployeeId(-1);
    }//GEN-LAST:event_jLEmplIdMouseClicked

    private void jBEmployeeCuratorDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEmployeeCuratorDeleteActionPerformed
        setEmployee(-1);
        //jTFEmploymentDate.setEnabled(false);
    }//GEN-LAST:event_jBEmployeeCuratorDeleteActionPerformed

    private void jChBMedicalBookIsDeletedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jChBMedicalBookIsDeletedItemStateChanged
        jChBMedicalBookIsDeleted.setBackground(jChBMedicalBookIsDeleted.isSelected() ? new Color(255, 102, 102) : new Color(255, 255, 255));
    }//GEN-LAST:event_jChBMedicalBookIsDeletedItemStateChanged

    private void jTFOtolaryngologistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFOtolaryngologistActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFOtolaryngologistActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBChoiseEmployee;
    private javax.swing.JButton jBEmployeeCuratorDelete;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCBValidationForYear;
    private javax.swing.JComboBox<String> jCBhepatitisAvaccine;
    private javax.swing.JCheckBox jChBMedicalBookIsDeleted;
    private javax.swing.JLabel jLEmpBerthday;
    private javax.swing.JLabel jLEmpDepartment;
    private javax.swing.JLabel jLEmpFIO;
    private javax.swing.JLabel jLEmpGroup;
    private javax.swing.JLabel jLEmpPhone;
    private javax.swing.JLabel jLEmpPosition;
    private javax.swing.JLabel jLEmplId;
    private javax.swing.JLabel jLEmploymentDate;
    private javax.swing.JLabel jLInstitution;
    private javax.swing.JLabel jLInstitutionAdress;
    private javax.swing.JLabel jLInstitutionStatus;
    private javax.swing.JLabel jLMainInstitution;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
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
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTAMedBookNote;
    private javax.swing.JTextField jTFBorneDiseases;
    private javax.swing.JTextField jTFDentist;
    private javax.swing.JTextField jTFDermatovenerologist;
    private javax.swing.JTextField jTFDiphtheria;
    private javax.swing.JTextField jTFExpertInNarcology;
    private javax.swing.JTextField jTFFluorography;
    private javax.swing.JTextField jTFHelminths;
    private javax.swing.JTextField jTFHepatitis_A;
    private javax.swing.JTextField jTFHepatitis_A2;
    private javax.swing.JTextField jTFHepatitis_B;
    private javax.swing.JTextField jTFHepatitis_B2;
    private javax.swing.JTextField jTFHepatitis_B3;
    private javax.swing.JTextField jTFIntestinalInfection;
    private javax.swing.JTextField jTFMeasles;
    private javax.swing.JTextField jTFMeasles_2;
    private javax.swing.JTextField jTFNumMedBook;
    private javax.swing.JTextField jTFOtolaryngologist;
    private javax.swing.JTextField jTFPsychiatrist;
    private javax.swing.JTextField jTFRubella;
    private javax.swing.JTextField jTFShigellvak;
    private javax.swing.JTextField jTFStaphylococcus;
    private javax.swing.JTextField jTFTherapist;
    private javax.swing.JTextField jTFTyphoidFever;
    private javax.swing.JTextField jTFValidation;
    private javax.swing.JTabbedPane jTabbedPane1;
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
