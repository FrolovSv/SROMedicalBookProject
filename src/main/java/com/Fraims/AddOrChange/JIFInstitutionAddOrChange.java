
package com.Fraims.AddOrChange;

import com.Class.Employee;
import com.Class.Institution;
import com.Class.Institution.InstitutionType;
import static com.Class.Institution.InstitutionType.*;
import com.Class.TableResize;
import com.Class.TypeUnit;
import com.Class.UserPrivilege;
import static com.Class.UserPrivilege.PrivilegeInstitution.*;
import com.Progect.MedicalBookProgectMaven;
import com.Progect.MedicalBookProgectMaven.statusFrame;
import com.SQL.SQLQuery;
import com.notUse.MainInternalFrame;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.table.DefaultTableModel;


public class JIFInstitutionAddOrChange extends MainInternalFrame {
    private int AuthorizedUserId;
    private int SelectedNumMedicalBook;
    private int SelectedEmployeeId;
    private int SelectedNumMedicalBookUnder;
    private int SelectedInstitutionId;
    private int SelectedMainInstitutionId;
    private int RegionId;
    
    private boolean GroupStateChange = false;
    private boolean DepartmentStateChange = false;
    private boolean PositionStateChange = false;
    private boolean IsNewEmployee = false;
    private boolean IsNewMainInstitution = false;
    private boolean IsNewInstitution = false;
    
    private UserPrivilege userPrivilege;    
        
    private TreeMap<Integer,Employee> EmployeeTMUnder;
    private TreeMap<Integer,Institution> InstitutionTMUnder;
    private Employee employee;
    private Employee newEmployee;
    private Institution institution;
    private Institution mainInstitution;
    
    private static DefaultTableModel TableEmployeesUnder; 
    private static DefaultTableModel TableInstitutionUnder; 

    private MedicalBookProgectMaven MBProgect;
    
    public void setSelectedEmployeeId(int SelectedEmployeeId){
        this.SelectedEmployeeId = SelectedEmployeeId;
        institution.setEmployeeCuratorId(SelectedEmployeeId);
        newEmployee = new SQLQuery<>(new Employee()).Read(SelectedEmployeeId);
        jButton2.setEnabled(true);
        IsNewEmployee = true;
        FillFieldsEmployeeCurator(newEmployee);
    }

    public void setSelectedMainInstitutionId(int SelectedMainInstitutionId) {
        this.SelectedMainInstitutionId = SelectedMainInstitutionId;
        //MainInstitution = new Institution().Read(SelectedMainInstitutionId, new SQLConnection());
        mainInstitution = new SQLQuery<>(new Institution()).Read(SelectedMainInstitutionId);
        jButton2.setEnabled(true);        
        institution.setMainInstitutionId(SelectedMainInstitutionId);
        IsNewMainInstitution = true;
        FillFieldsMainInstitution(mainInstitution);
        FillTableBaseInstitutionUnder();
    }

    public JIFInstitutionAddOrChange(MedicalBookProgectMaven MedicalBookProgect) {
        super(MedicalBookProgect);
        MBProgect = MedicalBookProgect;
        AuthorizedUserId = MedicalBookProgect.getAuthorizedUser().getId();
        SelectedEmployeeId = MedicalBookProgect.getSelectedEmployeeId();
        SelectedInstitutionId = MedicalBookProgect.getSelectedInstitutionId();
        RegionId = MedicalBookProgect.getRegionId();
        this.userPrivilege = MedicalBookProgect.getUserPrivilegeLogin();
        initComponents();
        
        jTEmployeesUnder = new TableResize().setTableColumnsWidth(jTEmployeesUnder,500);
        jTInstitutionUnder = new TableResize().setTableColumnsWidth(jTInstitutionUnder,500);
        
        TableEmployeesUnder = (DefaultTableModel) jTEmployeesUnder.getModel();        
        TableInstitutionUnder = (DefaultTableModel) jTInstitutionUnder.getModel();        
        verificationData();        
    }
    
    private void verificationData(){
        Thread MBThread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{                    
                    institution = new SQLQuery<>(new Institution()).Read(SelectedInstitutionId);
                    employee = new SQLQuery<>(new Employee()).Read(institution.getEmployeeCuratorId());
                    mainInstitution = new SQLQuery<>(new Institution()).Read(institution.getMainInstitutionId());
                   
                    institution.setRegionId(RegionId);
                    employee.setRegionId(RegionId);
                    mainInstitution.setRegionId(RegionId);
                    
                    if (SelectedInstitutionId <= 0){  
                        jButton2.setText("ДОБАВИТЬ УЧЕРЕЖДЕНИЕ");
                        jButton2.setEnabled(false);
                        IsNewInstitution = true;
                    } else {
                        jButton2.setText("Сохранить изменения");
                        jButton2.setEnabled(true);
                        IsNewInstitution = false;
                    }                              
                    
                    FillFieldsInstitution(institution);
                    FillFieldsEmployeeCurator(employee);
                    FillFieldsMainInstitution(institution);
                    //FillTableBaseEmployeesUnder();
                    //FillTableBaseInstitutionUnder();
                }catch(Exception ex){
                    System.out.println(".run() ПОТОК - Ошибка загрузки данных "+ex.getMessage());
                }                 
            }
        });
        MBThread.run();
    }
    // ====================== установка значений в поля ========================    
    private void FillFieldsInstitution(Institution institutionlocal){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Integer IdInst = new SQLQuery<>(new Institution()).getLastKey();                
        // ==================== ГЛАВНАЯ ====================    
        
        jCBTypeUnit.setEnabled(MBProgect.getStringPrivInstitution().contains("OWNTYPE"));
        jChBIsDeleted.setEnabled(userPrivilege.getPrivInstitution().contains(DELET));
        
        jBChoiseMainUnstitution.setEnabled(userPrivilege.getPrivInstitution().contains(CHANGE_CURATOR));
        jBMainInstitutionDelet.setEnabled(jBChoiseMainUnstitution.isSelected());

        jBChoiseCurator.setEnabled(userPrivilege.getPrivInstitution().contains(CHANGE_MAININSTITUTION));
        jBEmployeeCuratorDelete.setEnabled(jBChoiseCurator.isSelected());
        
        jCBTypeUnit.setModel(getValuesTupeUnit(false));
        if (institutionlocal.getId()>0){
            //jButton2.setEnabled(true);
            jTFInstitutionId.setText(institutionlocal.getId().toString());
            jChBIsDeleted.setSelected(institutionlocal.getIsClosed());
            jTFТName.setText(institutionlocal.getName());
            jTFТNameLong.setText(institutionlocal.getNameLong());
            jTFAdress.setText(institutionlocal.getAddress());
            jTANoteInst.setText(institutionlocal.getNote());
            jCBTypeUnit.setSelectedItem(institutionlocal.getTypeUnitId()!=null? institutionlocal.getTypeUnitId() : 0 );            
        } else {
            jTFInstitutionId.setText(IdInst.toString());
            jChBIsDeleted.setSelected(false);
            jTFТName.setText("");
            jTFТNameLong.setText("");
            jTFAdress.setText("");
            jCBTypeUnit.setSelectedItem(MBProgect.getTypeUnitTM().get(MBProgect.getAuthorizedUser().getTypeUnitId()).getName());
            jTANoteInst.setText("");
        }
        
//        if (institution.getId()>0){           
//            jLAddDate.setText( new SimpleDateFormat("yyy-MM-dd hh:mm").format(addEmployee.getDateAdd()));
//            jLAddEmpFIO.setText(addEmployee.getSurnameInitialsEmpl());
//            jLAddEmpPosition.setText(addEmployee.getUsPositionName());
//            jLChangeDate.setText( new SimpleDateFormat("yyy-MM-dd hh:mm").format(changeEmployee.getDateChange()));
//            jLChangeEmpFIO.setText(changeEmployee.getSurnameInitialsEmpl());
//            jLChangeEmpPosotion.setText(changeEmployee.getUsPositionName());
//        }
//        else {            
//            jLAddDate.setText(new SimpleDateFormat("yyy-MM-dd hh:mm").format(addEmployee.getDateAdd()));
//            jLAddEmpFIO.setText(addEmployee.getSurnameInitialsEmpl());
//            jLAddEmpPosition.setText(addEmployee.getUsPositionName());
//            jLChangeDate.setText("");
//            jLChangeEmpFIO.setText("");
//            jLChangeEmpPosotion.setText("");
//        }     
    }
    
    // ==================== Территориальный управляющий ====================
    private void FillFieldsEmployeeCurator(Employee EmplLocal){        
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (EmplLocal.getId()>0){
            jLEmployeeCuratorId.setText(EmplLocal.getId().toString());
            jLFIOEmployeer.setText(EmplLocal.getSurnameInitials());
            jLPhoneEmployeer.setText(EmplLocal.getPhone1());
            //jLBerthdayEmployeer.setText(df.format(EmplLocal.getBerthday()));
            jLGroupEmployeer.setText(EmplLocal.getUsGroupName());
            jLDepartmentEmployeer.setText(EmplLocal.getUsDepartmentName());
            jLPositionEmployeer.setText(EmplLocal.getUsPositionName());
            //jTANoteMainEmployeer.setText(EmplLocal.getNote());
            jLEmailEmployeer.setText(EmplLocal.getEmail());
        }else {
            jLEmployeeCuratorId.setText("Сотрудник не выбран");
            jLFIOEmployeer.setText("Сотрудник не выбран");
            jLPhoneEmployeer.setText("Сотрудник не выбран");
            //jLBerthdayEmployeer.setText("Сотрудник не выбран");
            jLGroupEmployeer.setText("Сотрудник не выбран");
            jLDepartmentEmployeer.setText("Сотрудник не выбран");
            jLPositionEmployeer.setText("Сотрудник не выбран");
            //jTANoteMainEmployeer.setText("Сотрудник не выбран");
            jLEmailEmployeer.setText("Сотрудник не выбран");
        }
        
                
        
        
        // ==================== Работающие сотрудники в ОУ ====================
//        jTFSearchEmpl.setText("Поиск по таблице (Номер, Адрес)");
//        jComboBox8.setSelectedIndex(0);
//        jCheckBox2.setSelected(false);
//        jComboBox7.setSelectedItem(25);
//        jCBIsDeleted.setSelected(false);
    }
    
    private void FillFieldsMainInstitution(Institution institutionlocal){
        if (mainInstitution.getId()>0){
            jLMainInstId.setText(institutionlocal.getId().toString());
            jLMainInstName.setText(institutionlocal.getName()); 
            jLMainInstNameLong.setText(institutionlocal.getNameLong());
            jLЬфштInstAdress.setText(institutionlocal.getAddress());
            jLMainInstStatus.setText(institutionlocal.getIsClosed()? "Закрыто" : "Работает");
        }else{
            jLMainInstId.setText("Учереждение не выбрано");
            jLMainInstName.setText("Учереждение не выбрано"); 
            jLMainInstNameLong.setText("Учереждение не выбрано");
            jLЬфштInstAdress.setText("Учереждение не выбрано");
            jLMainInstStatus.setText("Учереждение не выбрано");
        }
    }
    
    //====================== получение значений из полей ======================

    private Institution getFildsInstitution(){
        Institution newInstitution;
        newInstitution = institution;
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        try{
            //newemployee.setNumMedicalBook(Integer.valueOf(jTFNumMedBook.getText()));
            newInstitution.setIsClosed(jChBIsDeleted.isSelected());
            newInstitution.setName(jTFТName.getText());
            newInstitution.setNameLong(jTFТNameLong.getText());
            newInstitution.setAddress(jTFAdress.getText());
            //newInstitution.setTypeInstitution(jCBTypeInstitution.getSelectedItem().toString());
            for (Map.Entry<Integer, TypeUnit> entry : MBProgect.getTypeUnitTM().entrySet()) {
                TypeUnit value = entry.getValue();
                if (jCBTypeUnit.getSelectedItem().toString().equals(value.getName())){
                    newInstitution.setTypeUnitId(value.getId());
                    break;
                }                
            }
//            for (Iterator iterator =  MBProgect.getTypeUnitTM().entrySet().iterator(); iterator.hasNext();) {
//                Object next = (TypeUnit) iterator.next();                
//            }
                      
            newInstitution.setNote(jTANoteInst.getText());
            
        }catch(Exception ex){
            System.out.println("Fraims.JIFBaseUsers.getFildsEmployee()" + ex.getMessage());
        }
        return newInstitution;
    }
    
    // ====================== установка значений в таблицы ========================     
    private void FillTableBaseEmployeesUnder(){
        try{
            Employee SearhEmpl = new Employee();
            String Text = jTFSearchEmpl.getText();  
            if (Text.equals("Поиск по таблице (Номер, Адрес)")) Text = "";
            Integer Limit = Integer.valueOf(jComboBox7.getSelectedItem().toString());
            if (jCheckBox2.isSelected()) Limit = -1;        
            SearhEmpl.setTextSearch(Text);
            SearhEmpl.setIsDeleted(jCBIsDeleted.isSelected());   
            SearhEmpl.setInstitutionId(SelectedInstitutionId);

            EmployeeTMUnder = new SQLQuery(SearhEmpl).ReadForTable(Limit);
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
            jTEmployeesUnder = new TableResize().setTableColumnsWidth(jTEmployeesUnder,500);
        }catch(Exception ex){
            System.out.println("com.Fraims.AddOrChange.JIFInstitutionAddOrChange.FillTableBaseEmployeesUnder()");
        }
    }
    
    private void FillTableBaseInstitutionUnder(){
        Institution SerhInst = new Institution();
        String Text = jTFSearchInstUnder.getText(); 
        if (Text.equals("Поиск по таблице (Номер, Адрес)")) Text = "";
        SerhInst.setTextSearch(Text);              
        SerhInst.setMainInstitutionId(institution.getMainInstitutionId() > 0 ? institution.getMainInstitutionId() : 999999); 
        SerhInst.setTypeInst(null);
        
        InstitutionTMUnder = new SQLQuery(SerhInst).ReadForTable(-1);
        Object[] Data;
        while(TableInstitutionUnder.getRowCount()>0)
            TableInstitutionUnder.removeRow(0);
        for (Institution v : InstitutionTMUnder.values()) {
            if (v.getId()>0){
                Data = v.getDataForTableUnder();
                Data[0] = TableInstitutionUnder.getRowCount()+1;
                TableInstitutionUnder.insertRow(TableInstitutionUnder.getRowCount(), Data);
            }
        }
        jTInstitutionUnder = new TableResize().setTableColumnsWidth(jTInstitutionUnder,500);
    }
    
    //====================== запросы в SQL ======================  
    private void SaveOrAdd(){
        Institution newInstitution = new Institution();
        
        try{            
            newInstitution = getFildsInstitution(); 
            new SQLQuery(newInstitution).SaveOrWrite(newInstitution.getId());
            Integer key = newInstitution.getId();
            if (IsNewInstitution) key = new SQLQuery(newInstitution).getLastKey();
            
            mainInstitution.setMainInstitutionId(newInstitution.getMainInstitutionId());
            new SQLQuery(mainInstitution).Save(mainInstitution.getId());
            
            if (newEmployee!=null)
                if (!newEmployee.getId().equals(employee.getId())){
                    if (newEmployee.getId()>0){
                        //записываем номер ОУ новому сотруднику
                        newEmployee.setInstitutionId(key);
                        new SQLQuery<>(newEmployee).Save(newEmployee.getId());
                    }                    
                    // старому сотуднику обнуляем данные ОУ
                    if (employee.getId()>0){
                        employee.setInstitutionId(-1);
                        new SQLQuery<>(employee).Save(employee.getId());
                    }
                }         
             
            if (MBProgect.getJIFMedBookBase()!=null) MBProgect.getJIFMedBookBase().FillDataToTable();
            if (MBProgect.getJIFEmployeesBase()!=null) MBProgect.getJIFEmployeesBase().verificationData();
            if (MBProgect.getJIFInstitutionBase()!=null) MBProgect.getJIFInstitutionBase().verificationData();
            closeThis();
            MBProgect.setSelectedEmployeeId(-1);
            MBProgect.setSelectedNumMedBook(-1);
            MBProgect.setSelectedInstitutionId(-1);
            MBProgect.openBaseInstitutions(statusFrame.VIEW,null);
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
                FillTableBaseEmployeesUnder();
                jCheckBox2.setSelected(true);
            }         
        }else{
            FillTableBaseEmployeesUnder();
            jCheckBox2.setSelected(false);
        }      
    }
    
    private void closeThis(){
        this.setVisible(false);        
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedEmployeeId(-1);
        MBProgect.setSelectedNumMedBook(-1);
        MBProgect.setJIFInstitutionAddOrChange(null);
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
   
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMBaseEmployeeUnder = new javax.swing.JPopupMenu();
        jMIChangeMedicalBook = new javax.swing.JMenuItem();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTEmployeesUnder = new javax.swing.JTable();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTFSearchEmpl = new javax.swing.JTextField();
        jComboBox7 = new javax.swing.JComboBox<>();
        jComboBox8 = new javax.swing.JComboBox<>();
        jLabel92 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jCBIsDeleted = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTInstitutionUnder = new javax.swing.JTable();
        jTFSearchInstUnder = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jTFТName = new javax.swing.JTextField();
        jTFТNameLong = new javax.swing.JTextField();
        jTFAdress = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jCBTypeUnit = new javax.swing.JComboBox<>();
        jTANoteInst = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();
        jLAddDate = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLAddEmpFIO = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLAddEmpPosition = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLChangeDate = new javax.swing.JLabel();
        jLChangeEmpFIO = new javax.swing.JLabel();
        jLChangeEmpPosotion = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLMainInstId = new javax.swing.JLabel();
        jBChoiseMainUnstitution = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jLMainInstName = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLMainInstNameLong = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLЬфштInstAdress = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLMainInstStatus = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel95 = new javax.swing.JLabel();
        jLEmployeeCuratorId = new javax.swing.JLabel();
        jBChoiseCurator = new javax.swing.JButton();
        jBEmployeeCuratorDelete = new javax.swing.JButton();
        jLabel97 = new javax.swing.JLabel();
        jLFIOEmployeer = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jLPhoneEmployeer = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLEmailEmployeer = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLGroupEmployeer = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLDepartmentEmployeer = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLPositionEmployeer = new javax.swing.JLabel();
        jBMainInstitutionDelet = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTFInstitutionId = new javax.swing.JTextField();
        jChBIsDeleted = new javax.swing.JCheckBox();
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
            jTEmployeesUnder.getColumnModel().getColumn(4).setResizable(false);
            jTEmployeesUnder.getColumnModel().getColumn(5).setResizable(false);
            jTEmployeesUnder.getColumnModel().getColumn(5).setHeaderValue("Статус ЛМК");
        }

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox2.setText("Показать все");

        jTFSearchEmpl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFSearchEmpl.setText("Поиск по таблице (Номер ЛМК, ФИО, Должность)");
        jTFSearchEmpl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFSearchEmplFocusLost(evt);
            }
        });
        jTFSearchEmpl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTFSearchEmplMousePressed(evt);
            }
        });
        jTFSearchEmpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFSearchEmplActionPerformed(evt);
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

        jCBIsDeleted.setBackground(new java.awt.Color(255, 255, 255));
        jCBIsDeleted.setText("Уволенные");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jTFSearchEmpl)
                        .addGap(44, 44, 44)
                        .addComponent(jCBIsDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFSearchEmpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBIsDeleted))
                .addGap(6, 6, 6)
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTInstitutionUnder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTInstitutionUnder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "№ пп", "#", "Тип ОУ", "Норме ОУ", "Адрес", "Статус"
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
        jTInstitutionUnder.setFillsViewportHeight(true);
        jTInstitutionUnder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTInstitutionUnderMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTInstitutionUnderMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(jTInstitutionUnder);
        if (jTInstitutionUnder.getColumnModel().getColumnCount() > 0) {
            jTInstitutionUnder.getColumnModel().getColumn(0).setResizable(false);
            jTInstitutionUnder.getColumnModel().getColumn(1).setResizable(false);
            jTInstitutionUnder.getColumnModel().getColumn(2).setResizable(false);
            jTInstitutionUnder.getColumnModel().getColumn(3).setResizable(false);
            jTInstitutionUnder.getColumnModel().getColumn(4).setResizable(false);
            jTInstitutionUnder.getColumnModel().getColumn(5).setResizable(false);
        }

        jTFSearchInstUnder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFSearchInstUnder.setText("Поиск по таблице (Номер, Адрес)");
        jTFSearchInstUnder.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFSearchInstUnderFocusLost(evt);
            }
        });
        jTFSearchInstUnder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTFSearchInstUnderMousePressed(evt);
            }
        });
        jTFSearchInstUnder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFSearchInstUnderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 578, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(46, 46, 46)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTFSearchInstUnder)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE))
                    .addGap(46, 46, 46)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1073741823, 1073741823)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(56, 56, 56)
                    .addComponent(jTFSearchInstUnder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addGap(2147483090, 2147483090, 2147483090)))
        );

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Добавить изменить данные учереждения");
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
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTFТName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFТName.setText("учереждение не выбрано");

        jTFТNameLong.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFТNameLong.setText("учереждение не выбрано");

        jTFAdress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFAdress.setText("учереждение не выбрано");
        jTFAdress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFAdressActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Наименование");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Полное наименование");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Адрес");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Тип");

        jCBTypeUnit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBTypeUnit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Не выбрано", "Школьные подразделения", "Дошкольные подразделения", "Колледжи", "Казеные учереждения", "Социальные учереждения", "Больницы" }));

        jTANoteInst.setColumns(20);
        jTANoteInst.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTANoteInst.setRows(5);
        jTANoteInst.setBorder(javax.swing.BorderFactory.createTitledBorder("Примечание"));

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

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Изменение:");

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(153, 153, 153));
        jLabel28.setText("Дата");

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(153, 153, 153));
        jLabel29.setText("Сотудник");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(153, 153, 153));
        jLabel30.setText("Должность сотрудника");

        jLChangeDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLChangeDate.setForeground(new java.awt.Color(153, 153, 153));
        jLChangeDate.setText("Дата");

        jLChangeEmpFIO.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLChangeEmpFIO.setForeground(new java.awt.Color(153, 153, 153));
        jLChangeEmpFIO.setText("Сотудник");

        jLChangeEmpPosotion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLChangeEmpPosotion.setForeground(new java.awt.Color(153, 153, 153));
        jLChangeEmpPosotion.setText("Должность сотрудника");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Создание:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTANoteInst, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFAdress)
                            .addComponent(jTFТName)
                            .addComponent(jTFТNameLong)
                            .addComponent(jCBTypeUnit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel30))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLChangeEmpFIO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLChangeEmpPosotion, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLChangeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel24))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLAddEmpFIO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLAddEmpPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLAddDate, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 200, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFТName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFТNameLong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBTypeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTANoteInst, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLAddDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLAddEmpFIO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLAddEmpPosition)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLChangeDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLChangeEmpFIO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLChangeEmpPosotion))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("ОСНОВАНИЯ ИНФОРМАЦИЯ", jPanel3);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("# учерждения");

        jLMainInstId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLMainInstId.setText("учереждение не выбрано");
        jLMainInstId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLMainInstIdMouseClicked(evt);
            }
        });

        jBChoiseMainUnstitution.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBChoiseMainUnstitution.setText("Выбрать учереждение");
        jBChoiseMainUnstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBChoiseMainUnstitutionActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setText("Наименование");

        jLMainInstName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLMainInstName.setText("учереждение не выбрано");

        jLabel83.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel83.setText("Полное наименование");
        jLabel83.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jLabel83KeyReleased(evt);
            }
        });

        jLMainInstNameLong.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLMainInstNameLong.setText("учереждение не выбрано");

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel82.setText("Адрес");

        jLЬфштInstAdress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLЬфштInstAdress.setText("учереждение не выбрано");

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel87.setText("Статус учереждения");

        jLMainInstStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLMainInstStatus.setText("учереждение не выбрано");

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel95.setText("Территориальный управляющий");

        jLEmployeeCuratorId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLEmployeeCuratorId.setText("сотрудник не выбран");
        jLEmployeeCuratorId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLEmployeeCuratorIdMouseClicked(evt);
            }
        });

        jBChoiseCurator.setText("Изменить сотрудника");
        jBChoiseCurator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBChoiseCuratorActionPerformed(evt);
            }
        });

        jBEmployeeCuratorDelete.setText("х");
        jBEmployeeCuratorDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEmployeeCuratorDeleteActionPerformed(evt);
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

        jBMainInstitutionDelet.setText("х");
        jBMainInstitutionDelet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBMainInstitutionDeletActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLMainInstId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBChoiseMainUnstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBMainInstitutionDelet))
                            .addComponent(jLMainInstNameLong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLMainInstName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLMainInstStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLЬфштInstAdress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLGroupEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLDepartmentEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLPositionEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel95)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLEmployeeCuratorId, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBChoiseCurator, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBEmployeeCuratorDelete)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel98, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLFIOEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLEmailEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLPhoneEmployeer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jBChoiseMainUnstitution)
                    .addComponent(jLMainInstId)
                    .addComponent(jBMainInstitutionDelet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jLMainInstName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLMainInstNameLong)
                    .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(jLЬфштInstAdress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(jLMainInstStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel95)
                    .addComponent(jBChoiseCurator)
                    .addComponent(jLEmployeeCuratorId)
                    .addComponent(jBEmployeeCuratorDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(jLFIOEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel98)
                    .addComponent(jLPhoneEmployeer, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(jLEmailEmployeer))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(jLGroupEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel102)
                    .addComponent(jLDepartmentEmployeer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(jLPositionEmployeer))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Дополнительная информация", jPanel15);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("# Учереждения");

        jTFInstitutionId.setEditable(false);
        jTFInstitutionId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFInstitutionId.setText("Норме будет присвоен автоматически");
        jTFInstitutionId.setActionCommand("<Not Set>");
        jTFInstitutionId.setEnabled(false);
        jTFInstitutionId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFInstitutionIdFocusLost(evt);
            }
        });
        jTFInstitutionId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFInstitutionIdMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTFInstitutionIdMouseReleased(evt);
            }
        });

        jChBIsDeleted.setBackground(new java.awt.Color(255, 255, 255));
        jChBIsDeleted.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jChBIsDeleted.setText("Закрытое");
        jChBIsDeleted.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jChBIsDeleted.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jChBIsDeletedItemStateChanged(evt);
            }
        });
        jChBIsDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBIsDeletedActionPerformed(evt);
            }
        });

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
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFInstitutionId, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jChBIsDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jTFInstitutionId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jChBIsDeleted))
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

    private void jTFAdressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFAdressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFAdressActionPerformed

    private void jTFSearchEmplFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFSearchEmplFocusLost
        if(jTFSearchEmpl.getText().equals("")) jTFSearchEmpl.setText("Поиск по таблице");
    }//GEN-LAST:event_jTFSearchEmplFocusLost

    private void jTFSearchEmplMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFSearchEmplMousePressed
        if(jTFSearchEmpl.getText().equals("Поиск по таблице"))
        jTFSearchEmpl.setText("");
    }//GEN-LAST:event_jTFSearchEmplMousePressed

    private void jTFSearchEmplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFSearchEmplActionPerformed
        FillTableBaseEmployeesUnder();
    }//GEN-LAST:event_jTFSearchEmplActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        //System.out.println("Fraims.JIFBaseUsers.jComboBox10ItemStateChanged()");
        Integer Limit = Integer.valueOf(jComboBox7.getSelectedItem().toString());
        if (EmployeeTMUnder.size()>0&EmployeeTMUnder.size()!=Limit){
            FillTableBaseEmployeesUnder();
        }
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        closeThis();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jTFInstitutionIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFInstitutionIdFocusLost
//        if (jTFEmpId.getText().equals(""))
//            jTFEmpId.setText("Введите номер");
    }//GEN-LAST:event_jTFInstitutionIdFocusLost

    private void jTFInstitutionIdMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFInstitutionIdMouseReleased
       
    }//GEN-LAST:event_jTFInstitutionIdMouseReleased

    private void jTFInstitutionIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFInstitutionIdMouseClicked
//        if (jTFEmpId.getText().equals("Введите номер"))
//            jTFEmpId.setText("");
    }//GEN-LAST:event_jTFInstitutionIdMouseClicked

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

    private void jBChoiseMainUnstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBChoiseMainUnstitutionActionPerformed
        MBProgect.openBaseInstitutions(statusFrame.CHOISE_MAIN,this.getUI());
//        if (MBProgect.getJIFInstitutionBase()!=null)
//            MBProgect.getJIFInstitutionBase().ChoiseMain(this.getUI());
//        Integer SelectTypeUnitId = MBProgect.getAuthorizedUser().getTypeUnitId();
//        MBProgect.getJIFInstitutionBase().choise(this.getUI().toString(), SelectTypeUnitId);
    }//GEN-LAST:event_jBChoiseMainUnstitutionActionPerformed

    private void jBChoiseCuratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBChoiseCuratorActionPerformed
        MBProgect.openBaseEmployees(statusFrame.CHOISE,this.getUI());
    }//GEN-LAST:event_jBChoiseCuratorActionPerformed

    private void jLabel83KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel83KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel83KeyReleased

    private void jLMainInstIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLMainInstIdMouseClicked
        if (evt.getClickCount()>1){
            setSelectedMainInstitutionId(-1);
        }
    }//GEN-LAST:event_jLMainInstIdMouseClicked

    private void jTInstitutionUnderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTInstitutionUnderMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTInstitutionUnderMouseClicked

    private void jTInstitutionUnderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTInstitutionUnderMouseReleased
        if(jTFSearchInstUnder.getText().equals("Поиск по таблице (Номер, Адрес)")) 
            jTFSearchInstUnder.setText(""); 
    }//GEN-LAST:event_jTInstitutionUnderMouseReleased

    private void jTFSearchInstUnderFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFSearchInstUnderFocusLost
        if(jTFSearchInstUnder.getText().equals("")) 
            jTFSearchInstUnder.setText("Поиск по таблице (Номер, Адрес)");
    }//GEN-LAST:event_jTFSearchInstUnderFocusLost

    private void jTFSearchInstUnderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFSearchInstUnderMousePressed
        if(jTFSearchInstUnder.getText().equals("Поиск по таблице (Номер, Адрес)")) 
            jTFSearchInstUnder.setText("");     
    }//GEN-LAST:event_jTFSearchInstUnderMousePressed

    private void jTFSearchInstUnderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFSearchInstUnderActionPerformed
        FillTableBaseInstitutionUnder();
    }//GEN-LAST:event_jTFSearchInstUnderActionPerformed

    private void jLEmployeeCuratorIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLEmployeeCuratorIdMouseClicked
        if (evt.getClickCount()>1){
            setSelectedEmployeeId(-1);
        }
    }//GEN-LAST:event_jLEmployeeCuratorIdMouseClicked

    private void jBEmployeeCuratorDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEmployeeCuratorDeleteActionPerformed
        setSelectedEmployeeId(-1);
    }//GEN-LAST:event_jBEmployeeCuratorDeleteActionPerformed

    private void jBMainInstitutionDeletActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBMainInstitutionDeletActionPerformed
        setSelectedMainInstitutionId(-1);
    }//GEN-LAST:event_jBMainInstitutionDeletActionPerformed

    private void jChBIsDeletedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jChBIsDeletedItemStateChanged
        if (jChBIsDeleted.isSelected())
            jChBIsDeleted.setBackground(new Color(255,102,102));
        else
            jChBIsDeleted.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_jChBIsDeletedItemStateChanged

    private void jChBIsDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBIsDeletedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jChBIsDeletedActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBChoiseCurator;
    private javax.swing.JButton jBChoiseMainUnstitution;
    private javax.swing.JButton jBEmployeeCuratorDelete;
    private javax.swing.JButton jBMainInstitutionDelet;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCBIsDeleted;
    private javax.swing.JComboBox<String> jCBTypeUnit;
    private javax.swing.JCheckBox jChBIsDeleted;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JLabel jLAddDate;
    private javax.swing.JLabel jLAddEmpFIO;
    private javax.swing.JLabel jLAddEmpPosition;
    private javax.swing.JLabel jLChangeDate;
    private javax.swing.JLabel jLChangeEmpFIO;
    private javax.swing.JLabel jLChangeEmpPosotion;
    private javax.swing.JLabel jLDepartmentEmployeer;
    private javax.swing.JLabel jLEmailEmployeer;
    private javax.swing.JLabel jLEmployeeCuratorId;
    private javax.swing.JLabel jLFIOEmployeer;
    private javax.swing.JLabel jLGroupEmployeer;
    private javax.swing.JLabel jLMainInstId;
    private javax.swing.JLabel jLMainInstName;
    private javax.swing.JLabel jLMainInstNameLong;
    private javax.swing.JLabel jLMainInstStatus;
    private javax.swing.JLabel jLPhoneEmployeer;
    private javax.swing.JLabel jLPositionEmployeer;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JLabel jLЬфштInstAdress;
    private javax.swing.JMenuItem jMIChangeMedicalBook;
    private javax.swing.JPopupMenu jPMBaseEmployeeUnder;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTextArea jTANoteInst;
    private javax.swing.JTable jTEmployeesUnder;
    private javax.swing.JTextField jTFAdress;
    private javax.swing.JTextField jTFInstitutionId;
    private javax.swing.JTextField jTFSearchEmpl;
    private javax.swing.JTextField jTFSearchInstUnder;
    private javax.swing.JTextField jTFТName;
    private javax.swing.JTextField jTFТNameLong;
    private javax.swing.JTable jTInstitutionUnder;
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
