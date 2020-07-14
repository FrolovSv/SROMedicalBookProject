package com.Fraims.Bases;

import com.Class.*;
import com.Progect.MedicalBookProgectMaven;
import com.Progect.MedicalBookProgectMaven.statusFrame;
import static com.Class.UserPrivilege.PrivilegeDefault.*;
import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import com.notUse.MainInternalFrame;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SusPecT
 */
public class JIFEmployeesBase extends MainInternalFrame {
    
    private TreeMap<Integer,Employee> employeeTM;    
    private static DefaultTableModel TableEmployee;      
    private MedicalBookProgectMaven MBProgect;     
    private UserPrivilege userPrivilege;
    
    private Integer SelectedEmployeeId = -1;     
    private Integer SelectedNumMedicalBook = -1;  
    private Integer SelectedInstitutionId = -1;   
    private Integer SelectedMainEmployeeId = -1;  
    
    private static final String SearchMessageDefault = "Поиск по таблице (Телефон, ФИО, Номер ОУ, ЛМК)";
    
    private String StringPrivMedicalBook = "";
    private String StringPrivEmployee = "";
    
    private Boolean isChoiseEployee = false;    
    private Boolean ComboBoxStateChange = false;    
    private Boolean IsOnlyLineStaff = false;      
    private InternalFrameUI JIFcallingFrame;
    private ArrayList<UserPrivilege.PrivilegeDefault> PrivEmployee ;
    
    // ================== сеттеры ==================
    @Override
    public JIFEmployeesBase choise(InternalFrameUI JIFcallingFrame){
        //super.choise(JIFcallingFrame);
        this.JIFcallingFrame = JIFcallingFrame;
        isChoiseEployee = true;
        fillFieldsMain();
        verificationData();
        this.setVisible(true);
        jBAddEmp.setText("ВЫБРАТЬ сотрудника");
        jBAddEmp.setEnabled(false);
        jCBEmployeeWithoutMedicalBook.setSelected(true);
        jCBEmployeeWithoutMedicalBook.setEnabled(true);                           
        return this;
    }
    
    @Override
    public JIFEmployeesBase view(){
        isChoiseEployee = false;        
        fillFieldsMain();
        verificationData();        
        this.setVisible(true);
        jBAddEmp.setText("ДОБАВИТЬ нового сотрудника");
        jBAddEmp.setEnabled(true);
        jCBEmployeeWithoutMedicalBook.setSelected(false);
        return this;
    }
    
    // ================== обяъвление класса ==================
    public JIFEmployeesBase(MedicalBookProgectMaven MedicalBookProgect) {
        super(MedicalBookProgect);
        this.MBProgect = MedicalBookProgect;
        this.userPrivilege = MedicalBookProgect.getUserPrivilegeLogin();
        this.StringPrivMedicalBook = MedicalBookProgect.getStringPrivMedicalBook();
        this.StringPrivEmployee = MedicalBookProgect.getStringPrivMedicalBook();
        
        initComponents();
        employeeTM = new TreeMap<>();   
        
        jTEmployees = new TableResize().setTableColumnsWidth(jTEmployees,500);
        TableEmployee = (DefaultTableModel) jTEmployees.getModel();
        this.setVisible(false);
    }   
    
    
    // ================== загрузка данных и установка значений ==================
    public boolean verificationData(){
        try{
            Thread MBThread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try{   
                        //TreeMap<Integer,Employee> employeeTM = new TreeMap<>(); 
                        
                        Integer Limit = (jChBAll.isSelected() ? -1 : Integer.valueOf(jCBLimitCount.getSelectedItem().toString()));
                        String TextSearch = (jTFFindText.getText().equals(SearchMessageDefault) ? "" : jTFFindText.getText());
                        String NameGroup = (jCBUsGroup.getSelectedIndex()==0 ? "" : jCBUsGroup.getSelectedItem().toString());
                        String NameDepartment = (jCBUsDepartment.getSelectedIndex()==0 ? "" : jCBUsDepartment.getSelectedItem().toString());
                        String NamePosition = (jCBPosition.getSelectedIndex()==0 ? "" : jCBPosition.getSelectedItem().toString());
                        Integer TypeUnit = -1;
                        if (jCBUsGroup.getSelectedIndex()!=0)
                            TypeUnit = MBProgect.getUsGroupTM().get(getKeyByTreeMap(MBProgect.getUsGroupTM(),jCBUsGroup.getSelectedItem().toString())).getTypeUnitId();
                                                
                        TreeMap<Integer,Employee> Staff = new TreeMap<>();
                        TreeMap<Integer,Employee> LineStaff = new TreeMap<>();

                        Employee searchEmployee = new Employee();
                        searchEmployee.setMainEmployeeId(SelectedMainEmployeeId);
                        searchEmployee.setTypeUnitId(TypeUnit);                        
                        searchEmployee.setIsDeleted(jChBDeleted.isSelected());                        
                        searchEmployee.setTextSearch(TextSearch);
                        
                        searchEmployee.setSerchWithoutMedicalBook(jCBEmployeeWithoutMedicalBook.isSelected());
                        
                        searchEmployee.setUsGroupName(NameGroup);
                        searchEmployee.setUsDepartmentName(NameDepartment);
                        searchEmployee.setUsPositionName(NamePosition);
                        
                        if (!IsOnlyLineStaff)
                            Staff = new SQLQuery<>(searchEmployee).ReadForTable(Limit > 0 ? Limit : -1);
                        
                        if (jChBLineStaff.isSelected()){
                            searchEmployee.setSerchLineStaff(jChBLineStaff.isSelected());
                            searchEmployee.setUsGroupName("");
                            searchEmployee.setUsDepartmentName("");
                            LineStaff = new SQLQuery<>(searchEmployee).ReadForTable(Limit > 0 ? Limit : -1);    
                        }
                        
                        employeeTM.clear();
                        if (!IsOnlyLineStaff){         
                            employeeTM.putAll(Staff);
                            employeeTM.putAll(LineStaff);
                        }else{
                            employeeTM.putAll(LineStaff);
                        }
                        
                        //employeeTM = new SQLQuery<>(searchEmployee).ReadForTable(Limit);
                        Object[] Data;
                        while(TableEmployee.getRowCount()>0)
                            TableEmployee.removeRow(0);
                        for (Employee v : employeeTM.values()) {
                            if (v.getId()>0){
                                Data = v.getDataForTable();
                                Data[0] = TableEmployee.getRowCount()+1;
                                TableEmployee.insertRow(TableEmployee.getRowCount(), Data);
                            }
                        }
                        new TableResize().setTableColumnsWidth(jTEmployees,250);
                    }catch(Exception ex){
                        System.out.println("Fraims.JIFBaseEmployees.setDataToTable()" + ex.getMessage());
                    }                 
                }
            });
            MBThread.start();
            return true;            
        }catch(Exception ex){
            System.out.println("Fraims.JIFBaseEmployees.SQLSelectDataRequest() -"+ex.getMessage());
            return false;
        }
    }
    
    private void fillFieldsMain(){
        try{            
            jMIEditEmployee.setEnabled(MBProgect.getStringPrivEmployee().contains("CHANGE"));
            jMIViewEmployee.setEnabled(MBProgect.getStringPrivEmployee().contains("VIEW"));
            jMIEditMedicalBook.setEnabled(MBProgect.getStringPrivMedicalBook().contains("CHANGE"));
            jMIViewMedicalBook.setEnabled(MBProgect.getStringPrivMedicalBook().contains("VIEW"));            
            jMIEditInstitution.setEnabled(MBProgect.getStringPrivInstitution().contains("CHANGE"));
            jMIViewInstitution.setEnabled(MBProgect.getStringPrivInstitution().contains("VIEW"));
                    
            jCBUsGroup.setEnabled(MBProgect.getStringPrivEmployee().contains("ALL"));
            jCBUsDepartment.setEnabled(MBProgect.getStringPrivEmployee().contains("OWNGROUP") ||
                    MBProgect.getStringPrivEmployee().contains("OWNDEPARTMENT")||
                    MBProgect.getStringPrivEmployee().contains("EMPLOYEEUNDER"));
            jCBPosition.setEnabled(MBProgect.getStringPrivEmployee().contains("OWNDEPARTMENT") ||
                    MBProgect.getStringPrivEmployee().contains("EMPLOYEEUNDER"));
            
            jChBDeleted.setEnabled(MBProgect.getStringPrivEmployee().contains("DELET"));
            jChBLineStaff.setEnabled(MBProgect.getStringPrivEmployee().contains("LINESTAFF"));            
            jChBLineStaff.setSelected(false);
            
            jChBAll.setSelected(true);
            ComboBoxStateChange = false;
            IsOnlyLineStaff = false;
            
            // устанавливаем в поле данные по названием всех групп
            jCBUsGroup.setModel(getValuesGroup(true));
            jCBUsGroup.setSelectedItem(0);
            groupStateChange(jComboBox9, jCBUsDepartment, jCBPosition, true);
            //========================= реализация логики прав доступа =========================
            StringBuilder strb = new StringBuilder();

            if (userPrivilege.getPrivEmployee().contains(VIEW_ALL)) {
                strb.append("Без ограничений;");
                jChBLineStaff.setSelected(true);
            }else{
                jCBUsGroup.setSelectedItem(MBProgect.getUsGroupTM().get(MBProgect.getAuthorizedUser().getUsGroupId()).getName());
                jCBUsDepartment.setModel(getValuesDepartmentOnGroupName(jCBUsGroup, true));
                jCBUsDepartment.setSelectedIndex(0);                
                jChBLineStaff.setEnabled(false);  
                if (userPrivilege.getPrivEmployee().contains(VIEW_DELET)) {
                    if (userPrivilege.getPrivEmployee().contains(VIEW_DELET)) strb.append("Просмотр удаленных; ");
                    if (userPrivilege.getPrivEmployee().contains(CHANGE_DELET))  strb.append("Выбор удаленных; ");                    
                }
                if (userPrivilege.getPrivEmployee().contains(VIEW_OWNGROUP)) {                    
                    if (!isChoiseEployee) strb.append("Просмотр своей группы, подразделения и подчененных сотрудников; ");
                    else strb.append("Выбор своей группы, подразделения и подчененных сотрудников; ");                    
                } else if (userPrivilege.getPrivEmployee().contains(VIEW_OWNDEPARTMENT)) {                    
                    if (!isChoiseEployee) strb.append("Просмотр своего подразделения и подчененных сотрудников; ");
                    else strb.append("Выбор своего подразделения и подчененных сотрудников; ");
                } else if (userPrivilege.getPrivEmployee().contains(VIEW_EMPLOYEEUNDER)) {
                    jCBUsDepartment.setSelectedItem(MBProgect.getAuthorizedUser().getUsDepartmentName());
                    SelectedMainEmployeeId = MBProgect.getAuthorizedUser().getId();
                    if (!isChoiseEployee) strb.append("Просмотр подчененных сотрудников; ");
                    else strb.append("Выбор подчененных сотрудников; ");
                } else 
                    IsOnlyLineStaff = true;
                
                if (userPrivilege.getPrivEmployee().contains(VIEW_LINESTAFF)){
                    jCBUsDepartment.setSelectedItem(MBProgect.getAuthorizedUser().getUsDepartmentName());
                    SelectedMainEmployeeId = !IsOnlyLineStaff ? MBProgect.getAuthorizedUser().getId() : -1;                      
                    jChBLineStaff.setSelected(true);
                    jChBLineStaff.setEnabled(false);                    
                    if (!isChoiseEployee) strb.append("Просмотр линейного персонала; ");
                    else strb.append("Выбор линейного персонала; "); 
                }
            }

            setTitle("База сотрудников. Право доступа (" + strb.toString().substring(0, strb.length() - 2) + ")");
            // ========================= ========================= =========================

            ComboBoxStateChange = true;
            
            jTFFindText.setText(SearchMessageDefault);
            jCBLimitCount.setSelectedIndex(0);
            jChBDeleted.setSelected(false);
            jLRegion.setText("Округ - ");
        } catch (Exception ex) {
            System.out.println("com.Fraims.Bases.JIFEmployeeBase.fillFieldsMain() - " + ex.getMessage());
        }
    }        

    ////====================== прочее //======================    
    
    private void getSelectedUnitsFromTable(){
        if (jTEmployees.getSelectedRow()>=0){
            SelectedEmployeeId = Integer.valueOf((Integer)jTEmployees.getValueAt(jTEmployees.convertRowIndexToModel(jTEmployees.getSelectedRow()), 1));
            String NumMedBook = jTEmployees.getValueAt(jTEmployees.convertRowIndexToModel(jTEmployees.getSelectedRow()), 7).toString();
            SelectedNumMedicalBook = !NumMedBook.equals("") ? Integer.valueOf(NumMedBook) : 0;
            //String NameInstitution = jTEmployees.getValueAt(jTEmployees.convertRowIndexToModel(jTEmployees.getSelectedRow()), 3).toString();
            //if (new SQLQuery<>(new Institution()).)
            SelectedInstitutionId = employeeTM.get(SelectedEmployeeId).getInstitutionId();
            if (SelectedInstitutionId > 0)
                jMIEditInstitution.setEnabled(true);
            else 
                jMIEditInstitution.setEnabled(false);
            
            MBProgect.setSelectedInstitutionId(SelectedInstitutionId);
            MBProgect.setSelectedNumMedBook(SelectedNumMedicalBook);
            MBProgect.setSelectedEmployeeId(SelectedEmployeeId);    
            jBAddEmp.setEnabled(true);
        }  else {
            SelectedEmployeeId = -1;
            if (isChoiseEployee)
                jBAddEmp.setEnabled(false);
            else 
                jBAddEmp.setEnabled(true);
        } 
        
        jMMedicalBook.setEnabled((SelectedNumMedicalBook > 0));
        jMIEditMedicalBook.setEnabled((SelectedNumMedicalBook > 0));
        jMIViewMedicalBook.setEnabled((SelectedNumMedicalBook > 0));

        jMEmployee.setEnabled((SelectedEmployeeId > 0));
        jMIEditEmployee.setEnabled((SelectedEmployeeId > 0));
        jMIViewEmployee.setEnabled((SelectedEmployeeId > 0));

        jMinstitution.setEnabled((SelectedInstitutionId > 0));
        jMIEditInstitution.setEnabled((SelectedInstitutionId > 0));
        jMIViewInstitution.setEnabled((SelectedInstitutionId > 0));
    }
    
    private void closeThis(){
        this.setVisible(false);      
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedEmployeeId(-1);
        MBProgect.setSelectedInstitutionId(-1);
        MBProgect.setJIFEmployeesBase(null);
    }
    
    private void choiseJIFToOpen(){
        if (MBProgect.getJIFMedBookAddorChange()!=null)
            if (JIFcallingFrame.equals(MBProgect.getJIFMedBookAddorChange().getUI())){
                MBProgect.openAddChangeMedicalBook();
                MBProgect.getJIFMedBookAddorChange().setEmployee(SelectedEmployeeId);
        }
        if (MBProgect.getJIFInstitutionAddOrChange()!=null)
            if (JIFcallingFrame.equals(MBProgect.getJIFInstitutionAddOrChange().getUI())){
                MBProgect.openAddOrChangeInstitution();
                MBProgect.getJIFInstitutionAddOrChange().setSelectedEmployeeId(SelectedEmployeeId);
        }
        if (MBProgect.getJIFEmployeeAddOrChange()!=null)
            if (JIFcallingFrame.equals(MBProgect.getJIFEmployeeAddOrChange().getUI())){
                MBProgect.openAddOrChangeEmployee();
                MBProgect.getJIFEmployeeAddOrChange().setSelectedMainEmployeeId(SelectedEmployeeId);
            }
        if (MBProgect.getJIFEmployeeMedlBookAddorChange()!=null)
            if (JIFcallingFrame.equals(MBProgect.getJIFEmployeeMedlBookAddorChange().getUI())){
                MBProgect.openAddChangeEmployeeAndMedicalBook();
                MBProgect.getJIFEmployeeMedlBookAddorChange().setSelectedMainEmployeeId(SelectedEmployeeId);
            }
        closeThis();
    }
   
    //====================== служебные ======================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMBaseEmployee = new javax.swing.JPopupMenu();
        jMMedicalBook = new javax.swing.JMenu();
        jMIViewMedicalBook = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMIEditMedicalBook = new javax.swing.JMenuItem();
        jMEmployee = new javax.swing.JMenu();
        jMIViewEmployee = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMIEditEmployee = new javax.swing.JMenuItem();
        jMinstitution = new javax.swing.JMenu();
        jMIViewInstitution = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMIEditInstitution = new javax.swing.JMenuItem();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        jLRegion = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jTFFindText = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTEmployees = new javax.swing.JTable();
        jComboBox9 = new javax.swing.JComboBox<>();
        jCBLimitCount = new javax.swing.JComboBox<>();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jBAddEmp = new javax.swing.JButton();
        jChBAll = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jCBUsGroup = new javax.swing.JComboBox<>();
        jCBUsDepartment = new javax.swing.JComboBox<>();
        jCBPosition = new javax.swing.JComboBox<>();
        jChBLineStaff = new javax.swing.JCheckBox();
        jCBEmployeeWithoutMedicalBook = new javax.swing.JCheckBox();
        jChBDeleted = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();

        jMMedicalBook.setText("ЛМК");

        jMIViewMedicalBook.setText("Показать данные ЛМК");
        jMMedicalBook.add(jMIViewMedicalBook);
        jMMedicalBook.add(jSeparator2);

        jMIEditMedicalBook.setText("Изменить ЛМК");
        jMIEditMedicalBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEditMedicalBookActionPerformed(evt);
            }
        });
        jMMedicalBook.add(jMIEditMedicalBook);

        jPMBaseEmployee.add(jMMedicalBook);

        jMEmployee.setText("Сотрудник");

        jMIViewEmployee.setText("Показать данные сотрудника");
        jMEmployee.add(jMIViewEmployee);
        jMEmployee.add(jSeparator1);

        jMIEditEmployee.setText("Изменить данные сотрудника");
        jMIEditEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEditEmployeeActionPerformed(evt);
            }
        });
        jMEmployee.add(jMIEditEmployee);

        jPMBaseEmployee.add(jMEmployee);

        jMinstitution.setText("Учереждения");
        jMinstitution.setToolTipText("");
        jMinstitution.setActionCommand("");

        jMIViewInstitution.setText("Показать данные учереждения");
        jMinstitution.add(jMIViewInstitution);
        jMinstitution.add(jSeparator4);

        jMIEditInstitution.setText("Изменить Учереждение");
        jMIEditInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEditInstitutionActionPerformed(evt);
            }
        });
        jMinstitution.add(jMIEditInstitution);

        jPMBaseEmployee.add(jMinstitution);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("База сотрудников");
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

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Место для вашей рекламы");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1085, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel90.setText("База сотрудников");

        jLRegion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLRegion.setText("Округ");

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(102, 102, 102));
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel93.setText("Количество строк:");

        jTFFindText.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFFindText.setText("Поиск по таблице (Телефон, ФИО, ЛМК)");
        jTFFindText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFFindTextFocusLost(evt);
            }
        });
        jTFFindText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTFFindTextMousePressed(evt);
            }
        });
        jTFFindText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFFindTextActionPerformed(evt);
            }
        });

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTEmployees.setAutoCreateRowSorter(true);
        jTEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "№пп", "#", "Группа", "Подраздление", "Должность", "ФИО", "Телефон", "№ ЛМК", "Статус ЛМК"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTEmployees.setFillsViewportHeight(true);
        jTEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTEmployeesMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTEmployeesMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTEmployeesMouseReleased(evt);
            }
        });
        jTEmployees.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTEmployeesKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTEmployees);
        if (jTEmployees.getColumnModel().getColumnCount() > 0) {
            jTEmployees.getColumnModel().getColumn(0).setResizable(false);
            jTEmployees.getColumnModel().getColumn(1).setResizable(false);
            jTEmployees.getColumnModel().getColumn(2).setResizable(false);
            jTEmployees.getColumnModel().getColumn(3).setResizable(false);
            jTEmployees.getColumnModel().getColumn(4).setResizable(false);
            jTEmployees.getColumnModel().getColumn(5).setResizable(false);
            jTEmployees.getColumnModel().getColumn(6).setResizable(false);
            jTEmployees.getColumnModel().getColumn(7).setResizable(false);
            jTEmployees.getColumnModel().getColumn(8).setResizable(false);
        }

        jComboBox9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));

        jCBLimitCount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBLimitCount.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "25", "50", "100" }));
        jCBLimitCount.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBLimitCountItemStateChanged(evt);
            }
        });
        jCBLimitCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBLimitCountActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton13.setText(">");
        jButton13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton13.setPreferredSize(new java.awt.Dimension(32, 20));

        jButton14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton14.setText(">>");
        jButton14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton14.setPreferredSize(new java.awt.Dimension(32, 20));

        jBAddEmp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBAddEmp.setText("ДОБАВИТЬ сотрудника");
        jBAddEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAddEmpActionPerformed(evt);
            }
        });

        jChBAll.setBackground(new java.awt.Color(255, 255, 255));
        jChBAll.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jChBAll.setSelected(true);
        jChBAll.setText("Показать все");
        jChBAll.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jChBAllItemStateChanged(evt);
            }
        });
        jChBAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel90)
                        .addGap(93, 93, 93)
                        .addComponent(jLRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 362, Short.MAX_VALUE)
                        .addComponent(jBAddEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jTFFindText, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jChBAll, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBLimitCount, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
            .addComponent(jScrollPane1)
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLRegion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBAddEmp)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFFindText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jCBLimitCount, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jChBAll))
                    .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(235, 497));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Фильтр");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Группа");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Подразделение");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Должность");

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

        jChBLineStaff.setBackground(new java.awt.Color(255, 255, 255));
        jChBLineStaff.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jChBLineStaff.setSelected(true);
        jChBLineStaff.setText("Линейный персонала (ОУ)");
        jChBLineStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBLineStaffActionPerformed(evt);
            }
        });

        jCBEmployeeWithoutMedicalBook.setBackground(new java.awt.Color(255, 255, 255));
        jCBEmployeeWithoutMedicalBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBEmployeeWithoutMedicalBook.setText("Сотрудники без ЛМК");
        jCBEmployeeWithoutMedicalBook.setToolTipText("");
        jCBEmployeeWithoutMedicalBook.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBEmployeeWithoutMedicalBookItemStateChanged(evt);
            }
        });
        jCBEmployeeWithoutMedicalBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBEmployeeWithoutMedicalBookActionPerformed(evt);
            }
        });

        jChBDeleted.setBackground(new java.awt.Color(255, 255, 255));
        jChBDeleted.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jChBDeleted.setText("Показать удаленных");
        jChBDeleted.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jChBDeletedItemStateChanged(evt);
            }
        });
        jChBDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBDeletedActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Показать также:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator5)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBEmployeeWithoutMedicalBook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jChBDeleted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jChBLineStaff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator6)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBUsDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBUsGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(4, 4, 4)
                .addComponent(jCBUsGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBUsDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCBEmployeeWithoutMedicalBook)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jChBDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jChBLineStaff)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jCBPosition, jCBUsDepartment, jCBUsGroup});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jCBEmployeeWithoutMedicalBook, jChBDeleted, jChBLineStaff});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFFindTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFindTextActionPerformed
        verificationData();
    }//GEN-LAST:event_jTFFindTextActionPerformed

    private void jCBLimitCountItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBLimitCountItemStateChanged
        
    }//GEN-LAST:event_jCBLimitCountItemStateChanged

    private void jChBAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jChBAllItemStateChanged
        //setDataToTable();
    }//GEN-LAST:event_jChBAllItemStateChanged

    private void jCBLimitCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBLimitCountActionPerformed
        Integer Limit = Integer.valueOf(jCBLimitCount.getSelectedItem().toString());
        if (employeeTM.size()>0&employeeTM.size()!=Limit){            
            verificationData();  
        }
    }//GEN-LAST:event_jCBLimitCountActionPerformed

    private void jTEmployeesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTEmployeesMouseReleased
        JTable source = (JTable)evt.getSource();
        int row = source.rowAtPoint( evt.getPoint() );
        int column = source.columnAtPoint( evt.getPoint() );

        getSelectedUnitsFromTable();
        if (evt.getClickCount()>=2){ 
            if (isChoiseEployee){
                choiseJIFToOpen();
            }
            else{
//                открыть окно краткого просмотра информации
//                MBProgect.openAddOrChangeEmployee(); 
            }
        }
        
        if (evt.isPopupTrigger())
        {         
            if ( row>=0){
                //source.changeSelection(row, column, false, false);`
                MBProgect.setSelectedEmployeeId(Integer.valueOf((Integer)source.getModel().getValueAt(source.convertRowIndexToModel(source.getSelectedRow()), 1)));
                jPMBaseEmployee.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTEmployeesMouseReleased

    private void jChBAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBAllActionPerformed
        showMassegeALotOfData(jChBAll);  
        fillFieldsMain();
    }//GEN-LAST:event_jChBAllActionPerformed

    private void jTFFindTextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFFindTextMousePressed
        if(jTFFindText.getText().equals(SearchMessageDefault)) 
            jTFFindText.setText("");        
    }//GEN-LAST:event_jTFFindTextMousePressed

    private void jTFFindTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFFindTextFocusLost
        if(jTFFindText.getText().equals("")) 
            jTFFindText.setText(SearchMessageDefault);
    }//GEN-LAST:event_jTFFindTextFocusLost

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jChBDeletedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jChBDeletedItemStateChanged
        //FillDataToTable();
    }//GEN-LAST:event_jChBDeletedItemStateChanged

    private void jChBDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBDeletedActionPerformed
        verificationData();
    }//GEN-LAST:event_jChBDeletedActionPerformed

    private void jBAddEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAddEmpActionPerformed
        getSelectedUnitsFromTable();
        if (isChoiseEployee){
            choiseJIFToOpen();
        }
        else{
//            MBProgect.setSelectedEmployeeId(-1);
//            MBProgect.openAddOrChangeEmployee();
            //closeThis();
        }  
                
    }//GEN-LAST:event_jBAddEmpActionPerformed

    private void jMIEditEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEditEmployeeActionPerformed
        getSelectedUnitsFromTable();        
        MBProgect.openAddOrChangeEmployee();
        //MBProgect.getJIFAddorChangeEmployee().setS
        //closeThis();
    }//GEN-LAST:event_jMIEditEmployeeActionPerformed

    private void jMIEditMedicalBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEditMedicalBookActionPerformed
        getSelectedUnitsFromTable();
        MBProgect.openAddChangeMedicalBook();
        MBProgect.getJIFMedBookAddorChange().setNumMedicalBook(SelectedNumMedicalBook);
        //closeThis();
    }//GEN-LAST:event_jMIEditMedicalBookActionPerformed

    private void jCBEmployeeWithoutMedicalBookItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBEmployeeWithoutMedicalBookItemStateChanged
        //FillDataToTable();
    }//GEN-LAST:event_jCBEmployeeWithoutMedicalBookItemStateChanged

    private void jTEmployeesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTEmployeesKeyTyped
        //System.out.println("Table - "+evt.getKeyChar());
    }//GEN-LAST:event_jTEmployeesKeyTyped

    private void jMIEditInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEditInstitutionActionPerformed
        getSelectedUnitsFromTable();
        MBProgect.setSelectedInstitutionId(SelectedInstitutionId);
        MBProgect.openAddOrChangeInstitution();
    }//GEN-LAST:event_jMIEditInstitutionActionPerformed

    private void jTEmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTEmployeesMouseClicked
        //getSelectedUnitsFromTable();
    }//GEN-LAST:event_jTEmployeesMouseClicked

    private void jCBUsGroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBUsGroupItemStateChanged

    }//GEN-LAST:event_jCBUsGroupItemStateChanged

    private void jCBUsGroupMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCBUsGroupMousePressed

    }//GEN-LAST:event_jCBUsGroupMousePressed

    private void jCBUsGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUsGroupActionPerformed
        if (ComboBoxStateChange) {
            ComboBoxStateChange = false;
            ComboBoxStateChange = groupStateChange(jCBUsGroup, jCBUsDepartment, jCBPosition, true);
        }
    }//GEN-LAST:event_jCBUsGroupActionPerformed

    private void jCBUsDepartmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBUsDepartmentItemStateChanged

    }//GEN-LAST:event_jCBUsDepartmentItemStateChanged

    private void jCBUsDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUsDepartmentActionPerformed
        if (ComboBoxStateChange){ 
            ComboBoxStateChange = false;
            ComboBoxStateChange = departmentStateChange(jCBUsDepartment, jCBPosition, true);
        }
    }//GEN-LAST:event_jCBUsDepartmentActionPerformed

    private void jCBPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBPositionActionPerformed
        if (ComboBoxStateChange) verificationData();
    }//GEN-LAST:event_jCBPositionActionPerformed

    private void jCBEmployeeWithoutMedicalBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEmployeeWithoutMedicalBookActionPerformed
        verificationData();
    }//GEN-LAST:event_jCBEmployeeWithoutMedicalBookActionPerformed

    private void jChBLineStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBLineStaffActionPerformed
        verificationData();
    }//GEN-LAST:event_jChBLineStaffActionPerformed

    private void jTEmployeesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTEmployeesMousePressed
        //getSelectedUnitsFromTable();
    }//GEN-LAST:event_jTEmployeesMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAddEmp;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JCheckBox jCBEmployeeWithoutMedicalBook;
    private javax.swing.JComboBox<String> jCBLimitCount;
    private javax.swing.JComboBox<String> jCBPosition;
    private javax.swing.JComboBox<String> jCBUsDepartment;
    private javax.swing.JComboBox<String> jCBUsGroup;
    private javax.swing.JCheckBox jChBAll;
    private javax.swing.JCheckBox jChBDeleted;
    private javax.swing.JCheckBox jChBLineStaff;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLRegion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JMenu jMEmployee;
    private javax.swing.JMenuItem jMIEditEmployee;
    private javax.swing.JMenuItem jMIEditInstitution;
    private javax.swing.JMenuItem jMIEditMedicalBook;
    private javax.swing.JMenuItem jMIViewEmployee;
    private javax.swing.JMenuItem jMIViewInstitution;
    private javax.swing.JMenuItem jMIViewMedicalBook;
    private javax.swing.JMenu jMMedicalBook;
    private javax.swing.JMenu jMinstitution;
    private javax.swing.JPopupMenu jPMBaseEmployee;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable jTEmployees;
    private javax.swing.JTextField jTFFindText;
    // End of variables declaration//GEN-END:variables

    @Override
    public MainInternalFrame choiseMain(InternalFrameUI JIFcallingFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
