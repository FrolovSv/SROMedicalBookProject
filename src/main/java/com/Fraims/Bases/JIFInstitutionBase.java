package com.Fraims.Bases;

import com.Class.*;
import static com.Class.UserPrivilege.PrivilegeDefault.CHANGE_DELET;
import static com.Class.UserPrivilege.PrivilegeDefault.VIEW_ALL;
import static com.Class.UserPrivilege.PrivilegeDefault.VIEW_DELET;
import static com.Class.UserPrivilege.PrivilegeDefault.VIEW_EMPLOYEEUNDER;
import static com.Class.UserPrivilege.PrivilegeDefault.VIEW_LINESTAFF;
import static com.Class.UserPrivilege.PrivilegeDefault.VIEW_OWNDEPARTMENT;
import static com.Class.UserPrivilege.PrivilegeDefault.VIEW_OWNGROUP;
import static com.Class.UserPrivilege.PrivilegeInstitution.*;
import com.Progect.MedicalBookProgectMaven;
import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import com.notUse.MainInternalFrame;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SusPecT
 */
public class JIFInstitutionBase extends MainInternalFrame {
    
    private TreeMap<Integer,Institution> InstitutionTM;  
    private TreeMap<Integer,Employee> EmployeeCuratorTM ; 
    private TreeMap<Integer,TypeUnit> TypeUnitTM;  
    private MedicalBookProgectMaven MBProgect; 
    private UserPrivilege userPrivilege;
    private static DefaultTableModel TableInstitution;    
    private Integer SelectedInstitutionId = -1;            
    private Integer SelectedEmployeeId = -1;   
    private String SelectedInstitution = "";
    private Boolean ComboBoxStateChange = false;  
    private Boolean isChoiseInstitution = false;
    private Boolean isChoiseMainInstitution = false;
    private InternalFrameUI JIFcallingFrame;
    private static final String SearchMessageDefault = "Поиск по таблице (Название, номер ОУ, адрес)";
    
    public void setSelectedCuratorId(Integer SelectedCuratorEmployeeId) {
        new SQLQuery<>(new Institution()).ChangeCuratorInInstitution(SelectedInstitution, SelectedCuratorEmployeeId);
        verificationData();
    }
    
    // ================== сеттеры ==================
    public JIFInstitutionBase choise(InternalFrameUI JIFcallingFrame){
        this.JIFcallingFrame = JIFcallingFrame;
        isChoiseInstitution = true;
        isChoiseMainInstitution = false;
        jBAddInstitution.setText("ВЫБРАТЬ учереждение");
        jBAddInstitution.setEnabled(false);
        FillFieldsMain();
        verificationData();
        this.setVisible(true);
        
        return this;
    }
    public JIFInstitutionBase view(){        
        isChoiseInstitution = false;
        isChoiseMainInstitution = false;
        jBAddInstitution.setText("ДОБАВИТЬ новое учереждение");
        jBAddInstitution.setEnabled(true);
        FillFieldsMain();
        verificationData();
        this.setVisible(true);
        return this;
    }    
    public JIFInstitutionBase choiseMain(InternalFrameUI JIFcallingFrame){        
        this.JIFcallingFrame = JIFcallingFrame;
        isChoiseInstitution = false;
        isChoiseMainInstitution = true;
        jBAddInstitution.setText("ВЫБРАТЬ учереждение");
        jBAddInstitution.setEnabled(false);
        FillFieldsMain();
        verificationData();
        this.setVisible(true);
        return this;
    }
    
    // ================== обяъвление класса ==================
    public JIFInstitutionBase(MedicalBookProgectMaven MedicalBookProgect) {
        super(MedicalBookProgect);
        this.MBProgect = MedicalBookProgect;
        this.userPrivilege = MedicalBookProgect.getUserPrivilegeLogin();

        initComponents(); 
        InstitutionTM = new TreeMap<>();           
        jTInstitutions = new TableResize().setTableColumnsWidth(jTInstitutions,500);
        TableInstitution = (DefaultTableModel) jTInstitutions.getModel();          
        this.setVisible(false);
    }   
        
    // ================== загрузка данных и установка значений ==================
    public boolean verificationData(){
        try{
            Thread MBThread;
            MBThread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        Integer Limit = Integer.valueOf(jCBLimit.getSelectedItem().toString());                        
                        String SearchText = jTFSearchInstitution.getText();
                        String empl = jCBCuratorEmpl.getSelectedItem().toString();
                        Integer TypeUnit = getKeyByTreeMap(MBProgect.getTypeUnitTM(), jCBTypeUnit.getSelectedItem().toString());
                        if (SearchText.equals(SearchMessageDefault)) SearchText = "";
                        if (empl.equals(SearchMessageDefault))
                            empl = "-1";
                        else{ 
                            if (EmployeeCuratorTM!=null)
                                for (Map.Entry<Integer, Employee> entry : EmployeeCuratorTM.entrySet()) {
                                    if (entry.getValue().getFullName().equals(empl))
                                        empl = entry.getValue().getId().toString();
                                }
                            else {
                                empl = MBProgect.getAuthorizedUser().getId().toString();
                            }
                        } 
                        
                        Institution serhInstitution = new Institution();                        
                        serhInstitution.setTextSearch(SearchText);
                        serhInstitution.setTypeUnitId(TypeUnit < 0 ? -1 : TypeUnit);                        
                        serhInstitution.setEmployeeCuratorId(Integer.valueOf(empl));
                        serhInstitution.setIsClosed(jCBisClosed.isSelected());
                        
//                        InstitutionTM = serhInstitution.SQLSelectToTable(jCBViewAll.isSelected()? -1:Limit,new SQLConnection());
                        InstitutionTM = new SQLQuery<>(serhInstitution).ReadForTable(jChBAll.isSelected() ? -1 : Limit);
                        Object[] Data;
                        while (TableInstitution.getRowCount() > 0) {
                            TableInstitution.removeRow(0);
                        }
                        for (Institution v : InstitutionTM.values()) {
                            if (v.getId() > 0) {
                                Data = v.getDataForTable();
                                Data[0] = TableInstitution.getRowCount() + 1;
                                TableInstitution.insertRow(TableInstitution.getRowCount(), Data);
                            }
                        }
                        new TableResize().setTableColumnsWidth(jTInstitutions, 500);
                    } catch (Exception ex) {
                        System.out.println("Fraims.JIFInstitutionBase.setDataToTable() - " + ex.getMessage());
                    }
                }
            });
            MBThread.run();
            return true;
        } catch (Exception ex) {
            System.out.println("Fraims.JIFInstitutionBase.SQLSelectDataRequest() -" + ex.getMessage());
            return false;
        }
    }
    
    private void FillFieldsMain(){
        try{
            
            jMIChangeCurator.setEnabled(MBProgect.getStringPrivEmployee().contains("CHANGE"));
            
            jMIEditEmployee.setEnabled(MBProgect.getStringPrivEmployee().contains("CHANGE"));
            jMIViewEmployee.setEnabled(MBProgect.getStringPrivEmployee().contains("VIEW"));                       
            jMIEditInstitution.setEnabled(MBProgect.getStringPrivInstitution().contains("CHANGE"));
            jMIViewInstitution.setEnabled(MBProgect.getStringPrivInstitution().contains("VIEW"));
                    
            jCBTypeUnit.setEnabled(MBProgect.getStringPrivInstitution().contains("ALL"));
            jCBCuratorEmpl.setEnabled(MBProgect.getStringPrivInstitution().contains("OWNTYPE"));
            jCBisClosed.setEnabled(MBProgect.getStringPrivInstitution().contains("CLOSED"));      
            
            ComboBoxStateChange = false;

            // ========================= реализация логики прав доступа =========================
            StringBuilder strb = new StringBuilder();
            // если разрешен просмотр без ограничений
            if (userPrivilege.getPrivInstitution().contains(VIEW)){   
                jCBTypeUnit.setModel(getValuesTupeUnit(true));
                jCBTypeUnit.setSelectedItem(0);
                jCBCuratorEmpl.setModel(getFullNamesCurarorByTypeUnitId(getKeyByTreeMap(TypeUnitTM, jCBTypeUnit.getSelectedItem().toString()),true));
                jCBTypeUnit.setSelectedItem(0);
                strb.append("Без ограничений; ");
            }else {                  
                // если разрешен просмотр удаленных учереждений
                if (userPrivilege.getPrivInstitution().contains(VIEW_CLOSED)){  
                    strb.append(isChoiseInstitution ? "Просмотр удаленных; " : "Выбор удаленных;");                  
                }
                // если разрешен просмотр учереждений группы авторизованного пользователя
                if (userPrivilege.getPrivInstitution().contains(VIEW_OWNTYPE)){
                    jCBTypeUnit.setModel(getValuesTupeUnit(true));
                    jCBTypeUnit.setSelectedItem(0);
                    jCBCuratorEmpl.setModel(getFullNamesCurarorByTypeUnitId(getKeyByTreeMap(TypeUnitTM, jCBTypeUnit.getSelectedItem().toString()),true));
                    jCBTypeUnit.setSelectedItem(0);
                    //InstitutionGroupStateChange = true;                    
                    if (isChoiseInstitution) strb.append("Просмотр учереждений своей группы и просмотр своих учереждений; ");
                    else strb.append("Выбор учереждений своей группы и выбор своих учереждений; ");   
                // если разрешен просмотр учереждений ТОЛЬКО авторизованного пользователя (ДЛЯ ТУ)
                }else if (userPrivilege.getPrivInstitution().contains(VIEW_INSTITUTIONUNDER)){                    
                    jCBTypeUnit.setModel(getValuesTupeUnit(true));
                    jCBTypeUnit.setSelectedItem(MBProgect.getAuthorizedUser().getTypeUnitId());
                    jCBCuratorEmpl.addItem(MBProgect.getAuthorizedUser().getFullName());
                    strb.append(isChoiseInstitution ? "Просмотр своих учереждений; " : "Выбор своих учереждений; ");
                }
            }

            setTitle("База учереждений. Право доступа (" +strb.toString().substring(0, strb.length()-2) +")");
            // ========================= ========================= =========================
            
            if (isChoiseMainInstitution){
                jCBTypeUnit.setEnabled(true);
                jCBCuratorEmpl.setEnabled(true);            
            }
            
            ComboBoxStateChange = true;

            jLRegion.setText("Округ - ");
            jTFSearchInstitution.setText(SearchMessageDefault);
            jCBViewParts.setSelectedIndex(0);
            jChBAll.setSelected(true);
            jCBLimit.setSelectedIndex(0);  
        }catch(Exception ex){
            System.out.println("com.Fraims.Bases.JIFInstitutionBase.FillFieldsMain() - " + ex.getLocalizedMessage());
        }
    }
    
    ////====================== прочее //======================   
    private void getSelectedUnitsFromTable(){
        if (jTInstitutions.getSelectedRow()>=0){
            SelectedInstitutionId = Integer.valueOf((Integer)jTInstitutions.getValueAt(jTInstitutions.convertRowIndexToModel(jTInstitutions.getSelectedRow()), 1));
            SelectedEmployeeId = InstitutionTM.get(SelectedInstitutionId).getEmployeeAddId() == null ? -1 : InstitutionTM.get(SelectedInstitutionId).getEmployeeAddId();
            
            MBProgect.setSelectedInstitutionId(SelectedInstitutionId);
            MBProgect.setSelectedEmployeeId(SelectedEmployeeId);
            jBAddInstitution.setEnabled(true);
        }else {
            SelectedInstitutionId = -1;
            SelectedEmployeeId = -1;
            MBProgect.setSelectedInstitutionId(-1);
            MBProgect.setSelectedEmployeeId(-1);
            jBAddInstitution.setEnabled(isChoiseInstitution);
        }  
        
        jMIChangeCurator.setEnabled(SelectedInstitutionId > 0);            
        jMIEditEmployee.setEnabled(SelectedEmployeeId > 0);
        jMIViewEmployee.setEnabled(SelectedEmployeeId > 0);
        jMIEditInstitution.setEnabled(SelectedInstitutionId > 0);
        jMIViewInstitution.setEnabled(SelectedInstitutionId > 0);
        
    }    
    private void closeThis(){
        this.setVisible(false);      
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedInstitutionId(-1);
        MBProgect.setJIFInstitutionBase(null);
    }    
    private void choiseJIFToOpen(){
        if (MBProgect.getJIFEmployeeAddOrChange()!=null)
            if (JIFcallingFrame.toString().equals(MBProgect.getJIFEmployeeAddOrChange().getUI().toString())){
                MBProgect.openAddOrChangeEmployee();
                MBProgect.getJIFEmployeeAddOrChange().setSelectedInstitutionId(SelectedInstitutionId);
            }
        if (MBProgect.getJIFInstitutionAddOrChange()!=null)
            if(JIFcallingFrame.toString().equals(MBProgect.getJIFInstitutionAddOrChange().getUI().toString())){
                MBProgect.openAddOrChangeInstitution();
                MBProgect.getJIFInstitutionAddOrChange().setSelectedMainInstitutionId(SelectedInstitutionId);
            }
        if (MBProgect.getJIFEmployeeMedlBookAddorChange()!=null)
            if(JIFcallingFrame.toString().equals(MBProgect.getJIFEmployeeMedlBookAddorChange().getUI().toString())){
                MBProgect.openAddChangeEmployeeAndMedicalBook();
                MBProgect.getJIFEmployeeMedlBookAddorChange().setSelectedInstitutionId(SelectedInstitutionId);
            }
        //closeThis();
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
    
    DefaultComboBoxModel getFullNameByTreeMap(TreeMap<Integer,? extends MainClassProgect> TreeMap, String FirstElement){
        DefaultComboBoxModel ComboBoxModel = new DefaultComboBoxModel();
        ComboBoxModel.addElement(FirstElement);
        for (Map.Entry<Integer,? extends MainClassProgect> entry : TreeMap.entrySet()) {
            ComboBoxModel.addElement(entry.getValue().getFullName());
        }
        return ComboBoxModel;
    }
        
    //====================== служебные ======================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMBaseInstitution = new javax.swing.JPopupMenu();
        jMEmployee = new javax.swing.JMenu();
        jMIViewEmployee = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMIEditEmployee = new javax.swing.JMenuItem();
        jMinstitution = new javax.swing.JMenu();
        jMIViewInstitution = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMIEditInstitution = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMIChangeCurator = new javax.swing.JMenuItem();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jTFSearchInstitution = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel90 = new javax.swing.JLabel();
        jLRegion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTInstitutions = new javax.swing.JTable();
        jCBViewParts = new javax.swing.JComboBox<>();
        jBViewNext = new javax.swing.JButton();
        jBViewLast = new javax.swing.JButton();
        jChBAll = new javax.swing.JCheckBox();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel93 = new javax.swing.JLabel();
        jCBLimit = new javax.swing.JComboBox<>();
        jSeparator14 = new javax.swing.JSeparator();
        jBAddInstitution = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jCBCuratorEmpl = new javax.swing.JComboBox<>();
        jCBisClosed = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jCBTypeUnit = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();

        jMEmployee.setText("Сотрудник");

        jMIViewEmployee.setText("Показать данные сотрудника");
        jMEmployee.add(jMIViewEmployee);
        jMEmployee.add(jSeparator5);

        jMIEditEmployee.setText("Изменить данные сотрудника");
        jMIEditEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEditEmployeeActionPerformed(evt);
            }
        });
        jMEmployee.add(jMIEditEmployee);

        jPMBaseInstitution.add(jMEmployee);

        jMinstitution.setText("Учереждения");
        jMinstitution.setToolTipText("");
        jMinstitution.setActionCommand("");

        jMIViewInstitution.setText("Показать данные учереждения");
        jMinstitution.add(jMIViewInstitution);
        jMinstitution.add(jSeparator6);

        jMIEditInstitution.setText("Изменить Учереждение");
        jMIEditInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEditInstitutionActionPerformed(evt);
            }
        });
        jMinstitution.add(jMIEditInstitution);
        jMinstitution.add(jSeparator7);

        jMIChangeCurator.setText("Изменить Управляющего");
        jMIChangeCurator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIChangeCuratorActionPerformed(evt);
            }
        });
        jMinstitution.add(jMIChangeCurator);

        jPMBaseInstitution.add(jMinstitution);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("База учереждений");
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
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jTFSearchInstitution.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFSearchInstitution.setText("Поиск по таблице (Название, номер ОУ, адрес)");
        jTFSearchInstitution.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFSearchInstitutionFocusLost(evt);
            }
        });
        jTFSearchInstitution.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTFSearchInstitutionMousePressed(evt);
            }
        });
        jTFSearchInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFSearchInstitutionActionPerformed(evt);
            }
        });

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel90.setText("База учереждений");

        jLRegion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLRegion.setText("Округ");

        jTInstitutions.setAutoCreateRowSorter(true);
        jTInstitutions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "№пп", "#", "Номер ОУ", "Адрес", "Главное здание", "Подразделение", "Управляющий", "Статус"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTInstitutions.setFillsViewportHeight(true);
        jTInstitutions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTInstitutionsMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTInstitutions);
        if (jTInstitutions.getColumnModel().getColumnCount() > 0) {
            jTInstitutions.getColumnModel().getColumn(0).setResizable(false);
            jTInstitutions.getColumnModel().getColumn(1).setResizable(false);
            jTInstitutions.getColumnModel().getColumn(2).setResizable(false);
            jTInstitutions.getColumnModel().getColumn(3).setResizable(false);
            jTInstitutions.getColumnModel().getColumn(4).setResizable(false);
            jTInstitutions.getColumnModel().getColumn(5).setResizable(false);
            jTInstitutions.getColumnModel().getColumn(6).setResizable(false);
            jTInstitutions.getColumnModel().getColumn(7).setResizable(false);
        }

        jCBViewParts.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBViewParts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));

        jBViewNext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBViewNext.setText(">");
        jBViewNext.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jBViewNext.setPreferredSize(new java.awt.Dimension(32, 20));

        jBViewLast.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBViewLast.setText(">>");
        jBViewLast.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jBViewLast.setPreferredSize(new java.awt.Dimension(32, 20));

        jChBAll.setBackground(new java.awt.Color(255, 255, 255));
        jChBAll.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(102, 102, 102));
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel93.setText("Количество строк:");

        jCBLimit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBLimit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "25", "50", "100" }));
        jCBLimit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBLimitItemStateChanged(evt);
            }
        });
        jCBLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBLimitActionPerformed(evt);
            }
        });

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jBAddInstitution.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBAddInstitution.setText("ДОБАВИТЬ новое учереждение");
        jBAddInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAddInstitutionActionPerformed(evt);
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
                        .addComponent(jLRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBAddInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jTFSearchInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBViewParts, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBViewNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBViewLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jChBAll, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                        .addComponent(jBAddInstitution)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFSearchInstitution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jCBLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jBViewNext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBViewLast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCBViewParts, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jChBAll))
                    .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Фильтр");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Куратор");

        jCBCuratorEmpl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBCuratorEmpl.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBCuratorEmplItemStateChanged(evt);
            }
        });
        jCBCuratorEmpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBCuratorEmplActionPerformed(evt);
            }
        });

        jCBisClosed.setBackground(new java.awt.Color(255, 255, 255));
        jCBisClosed.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBisClosed.setText("Показать закрытые");
        jCBisClosed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBisClosedItemStateChanged(evt);
            }
        });
        jCBisClosed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBisClosedActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Тип учереждений");

        jCBTypeUnit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBTypeUnit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBTypeUnitItemStateChanged(evt);
            }
        });
        jCBTypeUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTypeUnitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jCBisClosed, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jCBCuratorEmpl, 0, 217, Short.MAX_VALUE)
                            .addComponent(jCBTypeUnit, 0, 217, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(4, 4, 4)
                .addComponent(jCBTypeUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBCuratorEmpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCBisClosed)
                .addContainerGap(301, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFSearchInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFSearchInstitutionActionPerformed
        verificationData();
    }//GEN-LAST:event_jTFSearchInstitutionActionPerformed

    private void jCBLimitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBLimitItemStateChanged
        
    }//GEN-LAST:event_jCBLimitItemStateChanged

    private void jChBAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jChBAllItemStateChanged
        //setDataToTable();
    }//GEN-LAST:event_jChBAllItemStateChanged

    private void jCBLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBLimitActionPerformed
        Integer Limit = Integer.valueOf(jCBLimit.getSelectedItem().toString());
        if (InstitutionTM.size()>0&InstitutionTM.size()!=Limit){            
            verificationData();  
        }
    }//GEN-LAST:event_jCBLimitActionPerformed

    private void jTInstitutionsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTInstitutionsMouseReleased
        getSelectedUnitsFromTable();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            getSelectedUnitsFromTable();
        JTable source = (JTable)evt.getSource();
            int row = source.rowAtPoint( evt.getPoint() );
            int column = source.columnAtPoint( evt.getPoint() );
        jTInstitutions.setRowSelectionInterval(row, row);
        if (evt.getClickCount()>=2){ 
            if (isChoiseInstitution){
                choiseJIFToOpen();                
            }else{
                //открыть окно краткого просмотра информации
                //MBProgect.openAddChangeMedicalBook();
            }
            
        }
        
        if (evt.isPopupTrigger())
        {
            
            if ( row>=0){
                if (source.getSelectedRowCount()<=1)
                    source.changeSelection(row, column, false, false);                
                MBProgect.setSelectedInstitutionId(Integer.valueOf((Integer)source.getModel().getValueAt(source.convertRowIndexToModel(source.getSelectedRow()), 1)));
                jPMBaseInstitution.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }        
    }//GEN-LAST:event_jTInstitutionsMouseReleased

    private void jChBAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBAllActionPerformed
        showMassegeALotOfData(jChBAll);  
        FillFieldsMain();   
    }//GEN-LAST:event_jChBAllActionPerformed

    private void jTFSearchInstitutionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFSearchInstitutionMousePressed
        if(jTFSearchInstitution.getText().equals(SearchMessageDefault)) 
            jTFSearchInstitution.setText("");        
    }//GEN-LAST:event_jTFSearchInstitutionMousePressed

    private void jTFSearchInstitutionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFSearchInstitutionFocusLost
        if(jTFSearchInstitution.getText().equals("")) 
            jTFSearchInstitution.setText(SearchMessageDefault);
    }//GEN-LAST:event_jTFSearchInstitutionFocusLost

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jCBisClosedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBisClosedItemStateChanged
        verificationData();
    }//GEN-LAST:event_jCBisClosedItemStateChanged

    private void jCBisClosedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBisClosedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBisClosedActionPerformed

    private void jBAddInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAddInstitutionActionPerformed
        if (isChoiseInstitution){
            choiseJIFToOpen();             
        }
        else{
//            MBProgect.setSelectedInstitutionId(-1);
//            MBProgect.openAddOrChangeInstitution();
//            closeThis();
        }             
    }//GEN-LAST:event_jBAddInstitutionActionPerformed

    private void jCBCuratorEmplItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBCuratorEmplItemStateChanged
        //if (EmployeeStateChange) verificationData(); 
    }//GEN-LAST:event_jCBCuratorEmplItemStateChanged

    private void jCBCuratorEmplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBCuratorEmplActionPerformed
        if (ComboBoxStateChange){ 
            verificationData();
        }
    }//GEN-LAST:event_jCBCuratorEmplActionPerformed

    private void jMIChangeCuratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIChangeCuratorActionPerformed
        int[] SelectedRows = jTInstitutions.getSelectedRows();
        Integer SelectedInst;
        StringBuilder Inst = new StringBuilder();
        for (int i = 0; i < SelectedRows.length; i++) {
            SelectedInst = Integer.valueOf((Integer)jTInstitutions.getValueAt(jTInstitutions.convertRowIndexToModel(SelectedRows[i]), 1));
            if (i==0)
                Inst.append(SelectedInst);
            else
                Inst.append("," + SelectedInst);
        }
        SelectedInstitution = Inst.toString();
        Integer n = showDialogYN("Вы хотите сменить управляющего у выбранных учереждений?");
        if (n==0){            
            //setSelectedCuratorId(SelectedEmployeeId, SelectedInstitution);
            MBProgect.openBaseEmployees(MedicalBookProgectMaven.statusFrame.CHOISE,this.getUI());          
        }
    }//GEN-LAST:event_jMIChangeCuratorActionPerformed

    private void jCBTypeUnitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBTypeUnitItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBTypeUnitItemStateChanged

    private void jCBTypeUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBTypeUnitActionPerformed
        if (ComboBoxStateChange){
            ComboBoxStateChange = false;
            jCBCuratorEmpl.setModel(getFullNamesCurarorByTypeUnitId(getKeyByTreeMap(TypeUnitTM, jCBTypeUnit.getSelectedItem().toString()),true));
            verificationData();
            ComboBoxStateChange = true;
        }
        
    }//GEN-LAST:event_jCBTypeUnitActionPerformed

    private void jMIEditEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEditEmployeeActionPerformed
        getSelectedUnitsFromTable();
        MBProgect.openAddOrChangeEmployee();
        //MBProgect.getJIFAddorChangeEmployee().setS
        //closeThis();
    }//GEN-LAST:event_jMIEditEmployeeActionPerformed

    private void jMIEditInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEditInstitutionActionPerformed
        getSelectedUnitsFromTable();
        MBProgect.setSelectedInstitutionId(SelectedInstitutionId);
        MBProgect.openAddOrChangeInstitution();
    }//GEN-LAST:event_jMIEditInstitutionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAddInstitution;
    private javax.swing.JButton jBViewLast;
    private javax.swing.JButton jBViewNext;
    private javax.swing.JComboBox<String> jCBCuratorEmpl;
    private javax.swing.JComboBox<String> jCBLimit;
    private javax.swing.JComboBox<String> jCBTypeUnit;
    private javax.swing.JComboBox<String> jCBViewParts;
    private javax.swing.JCheckBox jCBisClosed;
    private javax.swing.JCheckBox jChBAll;
    private javax.swing.JLabel jLRegion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JMenu jMEmployee;
    private javax.swing.JMenuItem jMIChangeCurator;
    private javax.swing.JMenuItem jMIEditEmployee;
    private javax.swing.JMenuItem jMIEditInstitution;
    private javax.swing.JMenuItem jMIViewEmployee;
    private javax.swing.JMenuItem jMIViewInstitution;
    private javax.swing.JMenu jMinstitution;
    private javax.swing.JPopupMenu jPMBaseInstitution;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JTextField jTFSearchInstitution;
    private javax.swing.JTable jTInstitutions;
    // End of variables declaration//GEN-END:variables
}
