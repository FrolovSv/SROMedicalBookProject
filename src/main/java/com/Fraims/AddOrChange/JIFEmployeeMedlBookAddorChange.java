
package com.Fraims.AddOrChange;

import com.Class.Employee;
import com.Class.Employee.EnumSex;
import static com.Class.Institution.InstitutionType.*;
import com.Progect.MedicalBookProgectMaven.statusFrame;
import com.Class.Institution;
import com.Class.MainClassProgect;
import com.Class.MedicalBook;
import com.Class.TableResize;
import com.Class.UsDepartment;
import com.Class.UsGroup;
import com.Class.UsPosition;
import static com.Class.UserPrivilege.PrivilegeDefault.DELET_ALL;
import com.Progect.MedicalBookProgectMaven;
import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import com.notUse.MainInternalFrame;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.table.DefaultTableModel;


public class JIFEmployeeMedlBookAddorChange extends MainInternalFrame {
    private int AuthorizedUserId;
    private int SelectedNumMedicalBook;
    private int SelectedEmployeeId;
    //private int RegionId;
    private int SelectedGroupId = 0;
    private int SelectedMainEmployeeId;
    private int SelectedInstitutionId;
    private int SelectUsDepartmentId;
    private int SelectedNumMedicalBookUnder;
    
//    private boolean GroupStateChange = false;
//    private boolean DepartmentStateChange = false;
//    private boolean PositionStateChange = false;
    private boolean IsNewInstitution;
    private boolean ComboBoxStateChange;
    private boolean IsNewEmployee;
    
    private Institution institution;    
    private TreeMap<Integer,Employee> EmployeeTMUnder;
    private Employee employee;
    private Employee mainEmployee;
    private Employee addEmployee;
    private Employee changeEmployee;
    private MedicalBook medicalBook;
    private static DefaultTableModel TableEmployeesUnder; 
    private MedicalBookProgectMaven MBProgect;
    
    // ====================== УСТАНОВКИ ЗНАЧЕНИЯ В ПОЛЯ ПО ЗАПРОСУ ОТ ДРУГИХ ОКОН ========================  
    public void setSelectedMainEmployeeId(int SelectedMainEmployeeId){
        this.SelectedMainEmployeeId = SelectedMainEmployeeId;
        mainEmployee = new SQLQuery<>(new Employee()).Read(SelectedMainEmployeeId);
        employee.setMainEmployeeId(SelectedMainEmployeeId);
        jButton2.setEnabled(true);
        FillFieldsEmployee();
        FillTableBaseEmployeesUnder();
    }
    public void setSelectedInstitutionId(int SelectedInstitutionId) {
        this.SelectedInstitutionId = SelectedInstitutionId;
        IsNewInstitution = true;
        institution = new SQLQuery<>(new Institution()).Read(SelectedInstitutionId);
        IsNewEmployee = true;
        jButton2.setEnabled(true);        
        employee.setInstitutionId(SelectedInstitutionId);
        FillFieldsInstitution(institution);
    }

    // ====================== Объявление класса ======================== 
    public JIFEmployeeMedlBookAddorChange(MedicalBookProgectMaven MedicalBookProgect) {
        super(MedicalBookProgect);
        MBProgect = MedicalBookProgect;
        AuthorizedUserId = MedicalBookProgect.getAuthorizedUser().getId();        
        //RegionId = MBProgect.getRegionId();
        SelectedNumMedicalBook = MedicalBookProgect.getSelectedNumMedBook();
        //SelectedEmployeeId = -1;
        
        initComponents();
        jTEmployeesUnder = new TableResize().setTableColumnsWidth(jTEmployeesUnder,500);
        TableEmployeesUnder = (DefaultTableModel) jTEmployeesUnder.getModel();        
        verificationData(); 
    }
    
    // ====================== загрузка всех данных при открытии EMPLOYEE======================== 
    private void verificationData(){
        Thread MBThread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{                      
                    //jButton2.setEnabled(false); 
                    //jButton2.setText("ДОБАВИТЬ"); 
                    
                    employee = new Employee(); 
                    medicalBook = new MedicalBook();
                    institution = new Institution();
                    
                    //employee = new SQLQuery<>(new Employee()).Read(SelectedEmployeeId);                     
                    
                    employee.setMainEmployeeId(MBProgect.getAuthorizedUser().getId());
                    employee.setEmployeeAddId(MBProgect.getAuthorizedUser().getId());
                    
                    //medicalBook = new SQLQuery<>(new MedicalBook()).Read(employee.getNumMedicalBook());                    
                    //institution = new SQLQuery<>(new Institution()).Read(employee.getInstitutionId());
                    mainEmployee = new SQLQuery<>(new Employee()).Read(employee.getMainEmployeeId());
                    addEmployee = new SQLQuery<>(new Employee()).Read(employee.getEmployeeAddId()); 
                    //changeEmployee = new SQLQuery<>(new Employee()).Read(employee.getEmployeeChangeId());
                    
                    // установка значений                   
                    medicalBook.setEmployeeAddId(MBProgect.getAuthorizedUser().getId());                  

                    
                    FillFieldsEmployee();
                    FillFieldsMedBook();
                    FillFieldsInstitution(institution);
                    FillTableBaseEmployeesUnder();                                        
                }catch(Exception ex){
                    System.out.println("Fraims.AddOrChange.JIFEmployeeMedlBookAddorChange.verificationData().run() ПОТОК - Ошибка загрузки данных "+ex.getMessage());
                }                 
            }
        });
        MBThread.run();
    }
    
    // ====================== ==============================================    
    // ====================== установка EMPLOYEE ========================    
    // *******************************************************************       
    // ====================== установка значений в поля ========================    
    private void FillFieldsEmployee(){        
        Integer EmpId = (new SQLQuery<>(employee).getLastKey()) + 1;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        jTFEmpId.setText(EmpId.toString());
        jChBEmpisWork.setSelected(false);
        jTFLastName.setText("");
        jTFName.setText("");
        jTFPatranymic.setText("");
        jTFBerthDay.setText(""); 
        jCBSex.setSelectedIndex(0);
        jTFPhone.setText("");
        jTFPhone2.setText("");
        jTFEmail.setText("");        
        jLTypeUnit.setText("");
        
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
        
        ComboBoxStateChange = false;        
        
        
        if (jCBUsGroup.getItemCount()>0)jCBUsGroup.removeAllItems();
        jCBUsGroup.setModel(new UsGroup().getNamesUsGroup(new SQLConnection()));                 
        
        if (jCBUsDepartment.getItemCount()>0)jCBUsDepartment.removeAllItems();
        jCBUsDepartment.setModel(new UsDepartment().getNamesByUsGroupName(employee.getUsGroupName(),new SQLConnection()));            

        if (jCBPosition.getItemCount()>0)jCBPosition.removeAllItems();
        jCBPosition.setModel(new UsPosition().getNameByUSDeparymenName(employee.getUsDepartmentName(),new SQLConnection()));            
        
        
        
        ComboBoxStateChange = true; 
        
        jCBUsGroup.setSelectedIndex(0);     
        jCBUsDepartment.setSelectedIndex(0);
        jCBPosition.setSelectedIndex(0);

        jLAddDate.setText(new SimpleDateFormat("yyy-MM-dd hh:mm").format(addEmployee.getDateAdd()));
        jLAddEmpFIO.setText(addEmployee.getSurnameInitials());
        jLAddEmpPosition.setText(addEmployee.getUsPositionName());
//        jLChangeDate.setText("");
//        jLChangeEmpFIO.setText("");
//        jLChangeEmpPosotion.setText("");

        // ==================== сотрудники в подчинении ====================
//        jTextField13.setText("Поиск по таблице (Номер ЛМК, ФИО, Должность)");
//        jComboBox8.setSelectedIndex(0);
//        jCheckBox2.setSelected(false);
//        jComboBox7.setSelectedItem(25);        
        
        // ==================== фото ====================
//        jLabel3.setText("");
//        jLabel5.setText("нет фото");
    }    
    private void FillFieldsInstitution(Institution InstitutionLocal){
        jBChoiseInstitution.setEnabled(false);
        if (InstitutionLocal.getId()>0){
            jLInstitutionId.setText(InstitutionLocal.getId().toString());
            jLMainInstitution.setText(InstitutionLocal.getMainInstitutionName().equals("") ? "Не выбран" : InstitutionLocal.getMainInstitutionName()); 
            jLInstitution.setText(InstitutionLocal.getName());
            jLInstitutionAdress.setText(InstitutionLocal.getAddress());
            jLInstitutionStatus.setText(InstitutionLocal.getIsClosed()? "Закрыто" : "Работает");
            jBChoiseInstitution.setEnabled(true);
        }else{
            jLInstitutionId.setText("Учереждение не выбрано");
            jLMainInstitution.setText("Учереждение не выбрано"); 
            jLInstitution.setText("Учереждение не выбрано");
            jLInstitutionAdress.setText("Учереждение не выбрано");
            jLInstitutionStatus.setText("Учереждение не выбрано");
        }
    }   
    //====================== получение значений из полей ======================
    private void getFildsEmployee(){
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        try{
            employee.setIsDeleted(jChBEmpisWork.isSelected());
            employee.setLastName(jTFLastName.getText());
            employee.setName(jTFName.getText());
            employee.setPatronymic(jTFPatranymic.getText());
            employee.setBerthday(ft.parse(jTFBerthDay.getText())); 
            employee.setSex(jCBSex.getSelectedIndex()==0? EnumSex.Female : EnumSex.Male);
            employee.setPhone1(jTFPhone.getText());
            employee.setPhone2(jTFPhone2.getText());
            employee.setEmail(jTFEmail.getText());

            //рабочая информация
            employee.setRootId(0); // тут надо будет сделать поиск из имени в АЙДИ            
//            employee.setUsGroupId(new UsGroup().getIdByName(jCBUsGroup.getSelectedItem().toString(), new SQLConnection()));
//            employee.setUsDepartmentId(new UsDepartment().getIdByNameUsPos(jCBUsDepartment.getSelectedItem().toString(), new SQLConnection()));
            
            employee.setUsGroupId(getKeyByTreeMap(MBProgect.getUsGroupTM(),jCBUsGroup.getSelectedItem().toString()));
            employee.setUsDepartmentId(SelectUsDepartmentId);
            employee.setUsPositionId(new UsPosition().getIdByNameUsDep(jCBPosition.getSelectedItem().toString(), SelectUsDepartmentId, new SQLConnection()));
            employee.setNote(jTANoteEmployee.getText());
            
        }catch(Exception ex){
            System.out.println("Fraims.JIFBaseUsers.getFildsEmployee()" + ex.getMessage());
        }
    }
    // ====================== установка значений в таблицы ========================     
    private void FillTableBaseEmployeesUnder(){
        try{
            Employee SerhEmpl = new Employee();
            Integer Limit = Integer.valueOf(jComboBox7.getSelectedItem().toString());
            if (jCheckBox2.isSelected()) Limit = -1;
            String Text = jTextField13.getText();
            if (Text.equals("Поиск по таблице (Номер ЛМК, ФИО, Должность)")) Text = "";

            SerhEmpl.setNumMedicalBook(1);
            SerhEmpl.setTextSearch(Text);
            SerhEmpl.setIsDeleted(false);
            SerhEmpl.setMainEmployeeId(employee.getMainEmployeeId());
            EmployeeTMUnder = new SQLQuery<>(SerhEmpl).ReadForTable(Limit);
            Object[] Data;
            while(TableEmployeesUnder.getRowCount()>0)
                TableEmployeesUnder.removeRow(0);
            for (Employee v : EmployeeTMUnder.values()) {
                if (v.getId()>0){
                    Data = v.getDataForTableEmpUnder();
                    Data[0] = TableEmployeesUnder.getRowCount()+1;                
                    TableEmployeesUnder.insertRow(TableEmployeesUnder.getRowCount(), Data);
                }
            }
            new TableResize().setTableColumnsWidth(jTEmployeesUnder,500);
        }catch (Exception c){
            System.out.println("com.Fraims.AddOrChange.JIFEmployeeMedlBookAddorChange.FillTableBaseEmployeesUnder()");
        }
    }    
    // *******************************************************************
    
    
    // ====================== ==============================================  
    // ====================== установка MEDICALBOOK ========================    
    // *******************************************************************    
    // ==================== Заполнение данных в поля ====================
    private void FillFieldsMedBook(){        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        jTFNumMedBook.setText("Введите номер");
        jChBMedicalBookIsDeleted.setSelected(false);
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
        //jTFhepatitisAvaccine.setText("");
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
    // ==================== Получение данных с полей формы ====================
    private void getMedicalBookFromFields(){        
        try{
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 
            medicalBook.setNumMedicalBook(Integer.valueOf(jTFNumMedBook.getText()));
            medicalBook.setIsDeleted(jChBMedicalBookIsDeleted.isSelected());
            medicalBook.setBorneDiseases(stringToEnumBorneDeseases(jTFBorneDiseases.getText()));

            medicalBook.setRubella(ft.parse(jTFRubella.getText()));
            medicalBook.setDiphtheria(ft.parse(jTFDiphtheria.getText()));

            medicalBook.setMeasles(ft.parse(jTFMeasles.getText()));      
            medicalBook.setMeasles_2(ft.parse(jTFMeasles_2.getText()));

            medicalBook.setHepatitis_B(ft.parse(jTFHepatitis_B.getText()));
            medicalBook.setHepatitis_B2(ft.parse(jTFHepatitis_B2.getText()));
            medicalBook.setHepatitis_B3(ft.parse(jTFHepatitis_B3.getText()));

            medicalBook.setHepatitis_A(ft.parse(jTFHepatitis_A.getText()));
            medicalBook.setHepatitis_A2(ft.parse(jTFHepatitis_A2.getText()));
            //medicalBook.setHepatitisAvaccine(StringToEnumHepatitisAvaccine(jTFhepatitisAvaccine.getText()));
            medicalBook.setHepatitisAvaccine(StringToEnumHepatitisAvaccine(jCBhepatitisAvaccine.getSelectedItem().toString()));

            medicalBook.setShigellvak(ft.parse(jTFShigellvak.getText()));

            medicalBook.setTherapist(ft.parse(jTFTherapist.getText()));
            medicalBook.setOtolaryngologist(ft.parse(jTFOtolaryngologist.getText()));
            medicalBook.setDentist(ft.parse(jTFDentist.getText()));
            medicalBook.setPsychiatrist(ft.parse(jTFPsychiatrist.getText()));
            medicalBook.setExpertInNarcology(ft.parse(jTFExpertInNarcology.getText()));
            medicalBook.setDermatovenerologist(ft.parse(jTFDermatovenerologist.getText()));
            medicalBook.setFluorography(ft.parse(jTFFluorography.getText()));
            medicalBook.setValidation(ft.parse(jTFValidation.getText()));
            medicalBook.setHelminths(ft.parse(jTFHelminths.getText()));
            medicalBook.setIntestinalInfection(ft.parse(jTFIntestinalInfection.getText()));
            medicalBook.setTyphoidFever(ft.parse(jTFTyphoidFever.getText()));
            medicalBook.setStaphylococcus(ft.parse(jTFStaphylococcus.getText()));

            medicalBook.setEmployeeId(SelectedEmployeeId);
            
            medicalBook.setEmployeeChangeId(AuthorizedUserId);
            medicalBook.setDateChange(new Date()); 
            
            
            
//            if (SelectedEmployeeId<=0){
//                medBook.setEmployeeAddId(AuthorizedUserId);  
//                medBook.setDateAdd(new Date());      
//            }else {
//                medBook.setEmployeeChangeID(AuthorizedUserId);
//                medBook.setDateChange(new Date()); 
//            }
            
            medicalBook.setNote(jTAMedBookNote.getText());
            medicalBook.setRegionId(employee.getRegionId());
            medicalBook.setValidationForYear(jCBValidationForYear.isSelected());
            
            // ================= фото ================
            //
            // ================= ================ ================
            
        }catch(Exception ex){
            ex.getMessage();
            System.out.println("Fraims.JIFAddMedicalBook.getFildsMedicalBook()" + ex.getMessage());
        }          
    }
    private Integer verificationNumMedicalBook(){
        Integer check = 0;
        if (isDigit(jTFNumMedBook.getText())){
            Integer IsNumMedBookExist = new SQLQuery<>(new MedicalBook()).isKeyExist(Integer.valueOf(jTFNumMedBook.getText()));
            jButton2.setEnabled(false);            
            if (IsNumMedBookExist>0){//если книжка есть в базе           
                showDialogOr("Данный номер ЛМК уже зарегистрирован в базе ЛМК");
                jTFNumMedBook.setText("Введите номер");
            }else {
                jButton2.setEnabled(true);
                check = 1;
                }
        }else showDialogOr("Номер ЛМК должен быть числом");  
        if (isDigit(jTFNumMedBook.getText())){
            Boolean IsPhoneExist = employee.CheckPhone(new SQLConnection(),Integer.valueOf(jTFNumMedBook.getText()));
            jButton2.setEnabled(false);            
            if (IsPhoneExist){//если телефон есть в базе           
                showDialogOr("Данный номер телефона уже зарегистрирован в базе ЛМК");
                jTFNumMedBook.setText("Введите номер");
            }else {
                jButton2.setEnabled(true);
                check = 1;
                }
        }else showDialogOr("Номер телефона должен быть числом");  
        return check;
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
        
    // СОХРАНИТЬ И ЗАКРЫТЬ
    private void SaveOrAdd(){        
        Integer Check = verificationNumMedicalBook();        
        try{
            if (Check>0){
                getMedicalBookFromFields();                 
                getFildsEmployee();
                employee.setNumMedicalBook(-1); 
                new SQLQuery<>(employee).Write();
                Integer EmpId = new SQLQuery<>(employee).getLastKey();
                if (EmpId > 0){
                    medicalBook.setEmployeeId(EmpId);
                    new SQLQuery<>(medicalBook).Write();
                    employee.setNumMedicalBook(medicalBook.getNumMedicalBook());
                    new SQLQuery<>(employee).Save(EmpId);
                }

                if (MBProgect.getJIFMedBookBase()!=null) MBProgect.getJIFMedBookBase().FillDataToTable();
                if (MBProgect.getJIFEmployeesBase()!=null) MBProgect.getJIFEmployeesBase().verificationData();
                
                closeThis(); 
            }
        }catch(Exception ex){
            System.out.println("Fraims.JIFAddorChangeEmployee.SaveOrAdd() " + ex.getMessage());
        }
    }  
    

    
    // ====================== Прочие функции ======================
    private void SelectLimitBaseEmloyeeUnder(){
        if(jCheckBox2.isSelected()){
            Object[] options = {"Показать","Отмена",};
            int n = JOptionPane.showOptionDialog(null,
                "Большое кол-во записей в базе может привести к замедлению работы программы",
                "Внимание!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
            if(n==0){
                // Внести изменения в получение
                FillTableBaseEmployeesUnder();
                jCheckBox2.setSelected(true);
            }         
        }else{
            // Внести изменения в получение
            FillTableBaseEmployeesUnder();
            jCheckBox2.setSelected(false);
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
        }   
        
    }
    
    private void closeThis(){
        this.setVisible(false);        
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedEmployeeId(-1);
        MBProgect.setSelectedNumMedBook(-1);
        MBProgect.setJIFEmployeeMedlBookAddorChange(null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMBaseEmployeeUnder = new javax.swing.JPopupMenu();
        jMIChangeMedicalBook = new javax.swing.JMenuItem();
        jPanel9 = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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
        jTFhepatitisAvaccine = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTFLastName = new javax.swing.JTextField();
        jTFName = new javax.swing.JTextField();
        jTFPatranymic = new javax.swing.JTextField();
        jTFBerthDay = new javax.swing.JTextField();
        jTFPhone = new javax.swing.JTextField();
        jTFPhone2 = new javax.swing.JTextField();
        jTFEmail = new javax.swing.JTextField();
        jCBSex = new javax.swing.JComboBox<>();
        jTANoteEmployee = new javax.swing.JTextArea();
        jPMainEmpl = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
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
        jBEmployeeCuratorDelete1 = new javax.swing.JButton();
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
        jLabel26 = new javax.swing.JLabel();
        jLInstitutionId = new javax.swing.JLabel();
        jBInstitutionDelete = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLTypeUnit = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
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
        jCBhepatitisAvaccine = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
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
        jCBValidationForYear = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jTFIntestinalInfection = new javax.swing.JTextField();
        jTFTyphoidFever = new javax.swing.JTextField();
        jTFStaphylococcus = new javax.swing.JTextField();
        jTAMedBookNote = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel89 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTFEmpId = new javax.swing.JTextField();
        jChBEmpisWork = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jLabel81 = new javax.swing.JLabel();
        jTFNumMedBook = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jChBMedicalBookIsDeleted = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();

        jMIChangeMedicalBook.setText("Изменить ЛМК");
        jMIChangeMedicalBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIChangeMedicalBookActionPerformed(evt);
            }
        });
        jPMBaseEmployeeUnder.add(jMIChangeMedicalBook);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(35, 35, 35))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(jButton1))
                .addContainerGap(439, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTFhepatitisAvaccine.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFhepatitisAvaccine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFhepatitisAvaccineActionPerformed(evt);
            }
        });

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
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

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

        jTFLastName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFPatranymic.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFPatranymic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFPatranymicActionPerformed(evt);
            }
        });

        jTFBerthDay.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFBerthDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFBerthDayActionPerformed(evt);
            }
        });

        jTFPhone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFPhone2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jCBSex.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBSex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Женщина", "Мужчина" }));

        jTANoteEmployee.setColumns(20);
        jTANoteEmployee.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTANoteEmployee.setRows(5);
        jTANoteEmployee.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTANoteEmployee)
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
                                .addGap(0, 204, Short.MAX_VALUE))
                            .addComponent(jTFLastName)
                            .addComponent(jTFName))))
                .addGap(34, 34, 34))
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
                .addComponent(jTANoteEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("ОСНОВАНИЯ ИНФОРМАЦИЯ", jPanel3);

        jPMainEmpl.setBackground(new java.awt.Color(255, 255, 255));

        jButton9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton9.setText("Изменить сотрудника");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTANoteMainEmployeer.setEditable(false);
        jTANoteMainEmployeer.setColumns(20);
        jTANoteMainEmployeer.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTANoteMainEmployeer.setRows(5);
        jTANoteMainEmployeer.setAutoscrolls(false);
        jTANoteMainEmployeer.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel95.setText("Сотрудник");

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

        jBEmployeeCuratorDelete1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBEmployeeCuratorDelete1.setText("х");
        jBEmployeeCuratorDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEmployeeCuratorDelete1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPMainEmplLayout = new javax.swing.GroupLayout(jPMainEmpl);
        jPMainEmpl.setLayout(jPMainEmplLayout);
        jPMainEmplLayout.setHorizontalGroup(
            jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPMainEmplLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTANoteMainEmployeer, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPMainEmplLayout.createSequentialGroup()
                        .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLGroupEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                            .addComponent(jLDepartmentEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLPositionEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPMainEmplLayout.createSequentialGroup()
                        .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel99, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLEmailEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLBerthdayEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPMainEmplLayout.createSequentialGroup()
                        .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel98, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                            .addComponent(jLabel95))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPMainEmplLayout.createSequentialGroup()
                                .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBEmployeeCuratorDelete1))
                            .addComponent(jLFIOEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLPhoneEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPMainEmplLayout.setVerticalGroup(
            jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPMainEmplLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel95)
                    .addComponent(jButton9)
                    .addComponent(jLabel96)
                    .addComponent(jBEmployeeCuratorDelete1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(jLFIOEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel98)
                    .addComponent(jLPhoneEmployeer, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(jLEmailEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel100)
                    .addComponent(jLBerthdayEmployeer))
                .addGap(18, 18, 18)
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(jLGroupEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel102)
                    .addComponent(jLDepartmentEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPMainEmplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(jLPositionEmployeer))
                .addGap(18, 18, 18)
                .addComponent(jTANoteMainEmployeer, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Сотрудник с правом доступа", jPMainEmpl);

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

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Создание:");

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
                        .addComponent(jBChoiseInstitution, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBInstitutionDelete))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLAddEmpFIO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLAddEmpPosition, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                                    .addComponent(jLAddDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addGap(124, 124, 124))
        );

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 314, Short.MAX_VALUE)
                        .addComponent(jButton6))
                    .addComponent(jLabel3))
                .addGap(28, 28, 28))
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
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        jTabbedPane1.addTab("Фото сотрудника", jPanel10);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

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
        jLabel53.setText("Вакцина против Геппатита А");

        jLabel72.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel72.setText("Перенесенные заболевания");

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

        jCBhepatitisAvaccine.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBhepatitisAvaccine.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "не выбрано", "Algawak", "Avaxim", "Wakta" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jCBhepatitisAvaccine, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jTFBorneDiseases, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)))
                .addGap(12, 12, 12))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(jTFBorneDiseases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBhepatitisAvaccine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addGap(135, 135, 135))
        );

        jTabbedPane1.addTab("ЛМК - Прививки", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

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

        jCBValidationForYear.setBackground(new java.awt.Color(255, 255, 255));
        jCBValidationForYear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBValidationForYear.setText("Аттестация на год");

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

        jLabel76.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel76.setText("Формат даты: дд.мм.гггг");

        jLabel78.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel78.setText("Формат даты: дд.мм.гггг");

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel80.setText("Формат даты: дд.мм.гггг");

        jTFIntestinalInfection.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFTyphoidFever.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTFStaphylococcus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTAMedBookNote.setColumns(20);
        jTAMedBookNote.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTAMedBookNote.setRows(5);
        jTAMedBookNote.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));

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
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
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
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(37, 37, 37))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(13, 13, 13))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(54, 54, 54))))
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
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("ЛМК - фото", jPanel8);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

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

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("ДОБАВИТЬ");
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTFEmpId, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .addComponent(jTFNumMedBook))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jChBMedicalBookIsDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jChBEmpisWork, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))))
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
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(jTFNumMedBook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jChBMedicalBookIsDeleted))
                .addGap(9, 9, 9)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        closeThis();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jTFNumMedBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFNumMedBookMouseClicked
        if (jTFNumMedBook.getText().equals("Введите номер"))
            jTFNumMedBook.setText("");
    }//GEN-LAST:event_jTFNumMedBookMouseClicked

    private void jTFNumMedBookMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFNumMedBookMousePressed
        
    }//GEN-LAST:event_jTFNumMedBookMousePressed

    private void jTFNumMedBookFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNumMedBookFocusLost
        if (jTFNumMedBook.getText().equals(""))
            jTFNumMedBook.setText("Введите номер");
    }//GEN-LAST:event_jTFNumMedBookFocusLost

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

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        MBProgect.openBaseEmployees(statusFrame.CHOISE,this.getUI());
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jLabel96MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel96MouseClicked
        this.setSelectedMainEmployeeId(-1);
        //        mainEmployee = new Employee();
        //        employee.setMainEmployeeId(-1);
    }//GEN-LAST:event_jLabel96MouseClicked

    private void jTEmployeesUnderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTEmployeesUnderMouseClicked

    }//GEN-LAST:event_jTEmployeesUnderMouseClicked

    private void jTEmployeesUnderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTEmployeesUnderMouseReleased
        getChoiseEmployeeId();
        if (evt.getClickCount()>=2){
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

    private void jTextField13FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField13FocusLost
        if(jTextField13.getText().equals("")) jTextField13.setText("Поиск по таблице");
    }//GEN-LAST:event_jTextField13FocusLost

    private void jTextField13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField13MousePressed
        if(jTextField13.getText().equals("Поиск по таблице"))
        jTextField13.setText("");
    }//GEN-LAST:event_jTextField13MousePressed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        FillTableBaseEmployeesUnder();
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        Integer Limit = Integer.valueOf(jComboBox7.getSelectedItem().toString());
        if (EmployeeTMUnder.size()>0&EmployeeTMUnder.size()!=Limit){
            FillTableBaseEmployeesUnder();
        }
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jTFRubellaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFRubellaActionPerformed

    }//GEN-LAST:event_jTFRubellaActionPerformed

    private void jTFhepatitisAvaccineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFhepatitisAvaccineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFhepatitisAvaccineActionPerformed

    private void jTFTherapistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFTherapistActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFTherapistActionPerformed

    private void jTFFluorographyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFluorographyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFFluorographyActionPerformed

    private void jTFHelminthsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFHelminthsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFHelminthsActionPerformed

    private void jMIChangeMedicalBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIChangeMedicalBookActionPerformed
        getChoiseEmployeeId();
        MBProgect.openAddChangeMedicalBook();
        MBProgect.getJIFMedBookAddorChange().setNumMedicalBook(SelectedNumMedicalBookUnder);
        closeThis();
    }//GEN-LAST:event_jMIChangeMedicalBookActionPerformed

    private void jBEmployeeCuratorDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEmployeeCuratorDelete1ActionPerformed
        setSelectedMainEmployeeId(-1);
    }//GEN-LAST:event_jBEmployeeCuratorDelete1ActionPerformed

    private void jTFOtolaryngologistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFOtolaryngologistActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFOtolaryngologistActionPerformed

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

    private void jCBPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBPositionActionPerformed

    private void jBChoiseInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBChoiseInstitutionActionPerformed
        MBProgect.openBaseInstitutions(statusFrame.CHOISE,this.getUI());
        //        MBProgect.getJIFInstitutionBase().choise(this.getUI().toString(),SelectTypeUnitId);
        //MBProgect.getJIFInstitutionBase().setInstitutionType(SelectTypeUnitId);
    }//GEN-LAST:event_jBChoiseInstitutionActionPerformed

    private void jLInstitutionIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLInstitutionIdMouseClicked
        if (evt.getClickCount()>=2){
            setSelectedInstitutionId(-1);
        }
    }//GEN-LAST:event_jLInstitutionIdMouseClicked

    private void jBInstitutionDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstitutionDeleteActionPerformed
        setSelectedInstitutionId(-1);
    }//GEN-LAST:event_jBInstitutionDeleteActionPerformed

    private void jTFBerthDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFBerthDayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFBerthDayActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBChoiseInstitution;
    private javax.swing.JButton jBEmployeeCuratorDelete1;
    private javax.swing.JButton jBInstitutionDelete;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jCBPosition;
    private javax.swing.JComboBox<String> jCBSex;
    private javax.swing.JComboBox<String> jCBUsDepartment;
    private javax.swing.JComboBox<String> jCBUsGroup;
    private javax.swing.JCheckBox jCBValidationForYear;
    private javax.swing.JComboBox<String> jCBhepatitisAvaccine;
    private javax.swing.JCheckBox jChBEmpisWork;
    private javax.swing.JCheckBox jChBMedicalBookIsDeleted;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JLabel jLAddDate;
    private javax.swing.JLabel jLAddEmpFIO;
    private javax.swing.JLabel jLAddEmpPosition;
    private javax.swing.JLabel jLBerthdayEmployeer;
    private javax.swing.JLabel jLDepartmentEmployeer;
    private javax.swing.JLabel jLEmailEmployeer;
    private javax.swing.JLabel jLFIOEmployeer;
    private javax.swing.JLabel jLGroupEmployeer;
    private javax.swing.JLabel jLInstitution;
    private javax.swing.JLabel jLInstitutionAdress;
    private javax.swing.JLabel jLInstitutionId;
    private javax.swing.JLabel jLInstitutionStatus;
    private javax.swing.JLabel jLMainInstitution;
    private javax.swing.JLabel jLPhoneEmployeer;
    private javax.swing.JLabel jLPositionEmployeer;
    private javax.swing.JLabel jLTypeUnit;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
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
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JMenuItem jMIChangeMedicalBook;
    private javax.swing.JPopupMenu jPMBaseEmployeeUnder;
    private javax.swing.JPanel jPMainEmpl;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTextArea jTAMedBookNote;
    private javax.swing.JTextArea jTANoteEmployee;
    private javax.swing.JTextArea jTANoteMainEmployeer;
    private javax.swing.JTable jTEmployeesUnder;
    private javax.swing.JTextField jTFBerthDay;
    private javax.swing.JTextField jTFBorneDiseases;
    private javax.swing.JTextField jTFDentist;
    private javax.swing.JTextField jTFDermatovenerologist;
    private javax.swing.JTextField jTFDiphtheria;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFEmpId;
    private javax.swing.JTextField jTFExpertInNarcology;
    private javax.swing.JTextField jTFFluorography;
    private javax.swing.JTextField jTFHelminths;
    private javax.swing.JTextField jTFHepatitis_A;
    private javax.swing.JTextField jTFHepatitis_A2;
    private javax.swing.JTextField jTFHepatitis_B;
    private javax.swing.JTextField jTFHepatitis_B2;
    private javax.swing.JTextField jTFHepatitis_B3;
    private javax.swing.JTextField jTFIntestinalInfection;
    private javax.swing.JTextField jTFLastName;
    private javax.swing.JTextField jTFMeasles;
    private javax.swing.JTextField jTFMeasles_2;
    private javax.swing.JTextField jTFName;
    private javax.swing.JTextField jTFNumMedBook;
    private javax.swing.JTextField jTFOtolaryngologist;
    private javax.swing.JTextField jTFPatranymic;
    private javax.swing.JTextField jTFPhone;
    private javax.swing.JTextField jTFPhone2;
    private javax.swing.JTextField jTFPsychiatrist;
    private javax.swing.JTextField jTFRubella;
    private javax.swing.JTextField jTFShigellvak;
    private javax.swing.JTextField jTFStaphylococcus;
    private javax.swing.JTextField jTFTherapist;
    private javax.swing.JTextField jTFTyphoidFever;
    private javax.swing.JTextField jTFValidation;
    private javax.swing.JTextField jTFhepatitisAvaccine;
    private javax.swing.JTabbedPane jTabbedPane1;
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
