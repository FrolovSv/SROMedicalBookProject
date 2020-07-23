package com.Fraims.Bases;

import com.Class.*;
import static com.Class.UserPrivilege.PrivilegeDefault.*;
import com.Progect.MedicalBookProgectMaven;
import com.SQL.SQLQuery;
import com.notUse.MainInternalFrame;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.table.DefaultTableModel;

public class JIFMedBookBase extends MainInternalFrame {

    private Boolean ComboBoxStateChange = false; 
    private Boolean isChoiseMedicalBook = false;
    private Boolean IsOnlyLineStaff = false;
    
      

    private static DefaultTableModel TableMedicalBook;
    private MedicalBookProgectMaven MBProgect;
    private UserPrivilege userPrivilege;
    private ArrayList<UserPrivilege.PrivilegeDefault> PrivEmployee;
    private ArrayList<UserPrivilege.PrivilegeInstitution> PrivInstitution;
    private ArrayList<UserPrivilege.PrivilegeSettings> PrivSettings;
    private ArrayList<UserPrivilege.PrivilegeDefault> PrivUser;
    private ArrayList<UserPrivilege.PrivilegeDefault> PrivMedicalBook ;


    private Integer SelectedNumMedicalBook = -1;
    private Integer SelectedEmployeeId = -1;
    private Integer SelectedInstitutionId = -1;
    private Integer SelectedMainEmployeeId = -1;
    
    private static final String SerchMessageDefault = "Поиск по таблице (Номер ЛМК, ФИО)";

    private TreeMap<Integer, MedicalBook> MedicalBookTM = new TreeMap<>();

    private InternalFrameUI JIFcallingFrame;

    @Override
    public JIFMedBookBase choise(InternalFrameUI JIFcallingFrame) {
        this.JIFcallingFrame = JIFcallingFrame;
        isChoiseMedicalBook = true;
        jBAddOrChoiseMedBook.setText("ВЫБРАТЬ ЛМК");
        jBAddOrChoiseMedBook.setEnabled(false);
        jCBMedicalBookWithoutEmployees.setSelected(true);
        jCBMedicalBookWithoutEmployees.setEnabled(false);
        fillFieldsMain();
        FillDataToTable();
        this.setVisible(true);
        return this;
    }

    @Override
    public JIFMedBookBase view() {
        isChoiseMedicalBook = false;
        jBAddOrChoiseMedBook.setText("ДОБАВИТЬ новую ЛМК");
        jBAddOrChoiseMedBook.setEnabled(true);
        fillFieldsMain();
        FillDataToTable();
        this.setVisible(true);
        return this;
    }

    // ===================== объявление класса =====================
    //@Override
    public JIFMedBookBase(MedicalBookProgectMaven MedicalBookProgect) {
        super(MedicalBookProgect);
        this.MBProgect = MedicalBookProgect;
        this.userPrivilege = MedicalBookProgect.getUserPrivilegeLogin();
        //this.SelectedTypeUnitId = MedicalBookProgect.getAuthorizedUser().getTypeUnitId();
        
        this.PrivEmployee = MedicalBookProgect.getUserPrivilegeLogin().getPrivEmployee();
        this.PrivInstitution = MedicalBookProgect.getUserPrivilegeLogin().getPrivInstitution();
        this.PrivSettings = MedicalBookProgect.getUserPrivilegeLogin().getPrivSettings();
        this.PrivUser = MedicalBookProgect.getUserPrivilegeLogin().getPrivUser();
        this.PrivMedicalBook = MedicalBookProgect.getUserPrivilegeLogin().getPrivMedicalBook();
                
        initComponents();

        jTMedaicalBook = new TableResize().setTableColumnsWidth(jTMedaicalBook, 500);
        TableMedicalBook = (DefaultTableModel) jTMedaicalBook.getModel();
        this.setVisible(false);
    }

    // ===================== установка дефолтов полей =====================
    public boolean FillDataToTable() {
        try {
            Thread MBThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TreeMap<Integer,MedicalBook> Staff = new TreeMap<>();
                        TreeMap<Integer,MedicalBook> LineStaff = new TreeMap<>();
                        Integer Limit = (jChBAll.isSelected() ? -1 : Integer.valueOf(jCBLimitCount.getSelectedItem().toString()));
                        String TextSearch = (jTFFindText.getText().equals(SerchMessageDefault) ? "" : jTFFindText.getText());
                        String NameGroup = (jCBUsGroup.getSelectedIndex()==0 ? "" : jCBUsGroup.getSelectedItem().toString());
                        String NameDepartment = (jCBUsDepartment.getSelectedIndex()==0 ? "" : jCBUsDepartment.getSelectedItem().toString());
                        String NamePosition = (jCBPosition.getSelectedIndex()==0 ? "" : jCBPosition.getSelectedItem().toString());
                        Integer TypeUnit = -1;
                        if (jCBUsGroup.getSelectedIndex()!=0)
                            TypeUnit = MBProgect.getUsGroupTM().get(getKeyByTreeMap(MBProgect.getUsGroupTM(),jCBUsGroup.getSelectedItem().toString())).getTypeUnitId();

                        MedicalBook searchMedicalBook = new MedicalBook();
                        searchMedicalBook.setMainEmployeeId(SelectedMainEmployeeId);
                        searchMedicalBook.setTypeUnitId(TypeUnit);                        
                        searchMedicalBook.setIsDeleted(jChBDeleted.isSelected());                        
                        searchMedicalBook.setTextSearch(TextSearch);                        
                        searchMedicalBook.setSerchWithoutEmployees(jCBMedicalBookWithoutEmployees.isSelected());                        
                        searchMedicalBook.setEmployeeUsGroupName(NameGroup);
                        searchMedicalBook.setEmployeeUsDepartmentName(NameDepartment);
                        searchMedicalBook.setEmployeeUsPositionName(NamePosition);
                        
                        if (!IsOnlyLineStaff)
                            Staff = new SQLQuery<>(searchMedicalBook).ReadForTable(Limit > 0 ? Limit : -1);
                                                
                        if (jChBLineStaff.isSelected()){
                            searchMedicalBook.setSerchLineStaff(jChBLineStaff.isSelected());
                            searchMedicalBook.setEmployeeUsGroupName("");
                            searchMedicalBook.setEmployeeUsDepartmentName("");
                            LineStaff = new SQLQuery<>(searchMedicalBook).ReadForTable(Limit > 0 ? Limit : -1);    
                        }
                        
                        MedicalBookTM.clear();
                        if (!IsOnlyLineStaff){           
                            MedicalBookTM.putAll(Staff);
                            MedicalBookTM.putAll(LineStaff);
                        }else{
                            MedicalBookTM.putAll(LineStaff);
                        }
                        
                        TableMedicalBook = (DefaultTableModel) jTMedaicalBook.getModel();                        
                        while (TableMedicalBook.getRowCount() > 0) {
                            TableMedicalBook.removeRow(0);
                        }
                        Object[] Data;
                        for (MedicalBook v : MedicalBookTM.values()) {
                            if (v.getNumMedicalBook() != null) {
                                if (v.getNumMedicalBook() > 0) {
                                    Data = v.getDataForTable();
                                    Data[0] = TableMedicalBook.getRowCount() + 1;
                                    TableMedicalBook.insertRow(TableMedicalBook.getRowCount(), Data);
                                }
                            }
                        }
                        new TableResize().setTableColumnsWidth(jTMedaicalBook, 500);

                    } catch (Exception ex) {
                        System.out.println("JIFMedBookBase.FillDataToTable.run() - Ошибка загрузки данных " + ex.getMessage());
                    }
                }
            });
            MBThread.run();
            return true;
        } catch (Exception ex) {
            System.out.println("Fraims.JFBaseMedBook.SQLSelectDataRequest() -" + ex.getMessage());
            return false;
        }
    }

    private void fillFieldsMain() {
        try {
            jMIEditEmployee.setEnabled(MBProgect.getStringPrivEmployee().contains("CHANGE"));
            jMIViewEmployee.setEnabled(MBProgect.getStringPrivEmployee().contains("VIEW"));
            jMIEditMedicalBook.setEnabled(MBProgect.getStringPrivMedicalBook().contains("CHANGE"));
            jMIViewMedicalBook.setEnabled(MBProgect.getStringPrivMedicalBook().contains("VIEW"));            
            jMIEditInstitution.setEnabled(MBProgect.getStringPrivInstitution().contains("CHANGE"));
            jMIViewInstitution.setEnabled(MBProgect.getStringPrivInstitution().contains("VIEW"));
                        
            jCBUsGroup.setEnabled(MBProgect.getStringPrivMedicalBook().contains("ALL"));
            jCBUsDepartment.setEnabled(MBProgect.getStringPrivMedicalBook().contains("OWNGROUP")||
                    MBProgect.getStringPrivMedicalBook().contains("OWNDEPARTMENT")||
                    MBProgect.getStringPrivMedicalBook().contains("EMPLOYEEUNDER"));
            jCBPosition.setEnabled(MBProgect.getStringPrivMedicalBook().contains("OWNDEPARTMENT")||
                    MBProgect.getStringPrivMedicalBook().contains("EMPLOYEEUNDER"));
            
            jChBDeleted.setEnabled(MBProgect.getStringPrivMedicalBook().contains("DELET"));
            jChBLineStaff.setEnabled(MBProgect.getStringPrivMedicalBook().contains("LINESTAFF"));
            
            jChBLineStaff.setSelected(false);
            jChBAll.setSelected(true);
            
            jChBAll.setSelected(true);
            ComboBoxStateChange = false;
            IsOnlyLineStaff = false;
            
            // устанавливаем в поле данные по названием всех групп
            jCBUsGroup.setModel(getValuesGroup(true));
            groupStateChange(jCBUsGroup, jCBUsDepartment, jCBPosition, true);
            //========================= реализация логики прав доступа =========================
            StringBuilder strb = new StringBuilder();

            if (userPrivilege.getPrivMedicalBook().contains(VIEW_ALL)) {
                strb.append("Без ограничений;");
                jChBLineStaff.setSelected(true);
            }else{
                jCBUsGroup.setSelectedItem(MBProgect.getUsGroupTM().get(MBProgect.getAuthorizedUser().getUsGroupId()).getName());
                jCBUsDepartment.setModel(getValuesDepartmentOnGroupName(jCBUsGroup, true));
                jCBUsDepartment.setSelectedIndex(0);                
                jChBLineStaff.setEnabled(false);  
                if (userPrivilege.getPrivMedicalBook().contains(VIEW_DELET)) {
                    if (userPrivilege.getPrivMedicalBook().contains(VIEW_DELET)) strb.append("Просмотр удаленных; ");
                    if (userPrivilege.getPrivMedicalBook().contains(CHANGE_DELET))  strb.append("Выбор удаленных; ");                    
                }
                if (userPrivilege.getPrivMedicalBook().contains(VIEW_OWNGROUP)) {                    
                    if (!isChoiseMedicalBook) strb.append("Просмотр своей группы, подразделения и подчененных сотрудников; ");
                    else strb.append("Выбор своей группы, подразделения и подчененных сотрудников; ");                    
                } else if (userPrivilege.getPrivMedicalBook().contains(VIEW_OWNDEPARTMENT)) {                    
                    if (!isChoiseMedicalBook) strb.append("Просмотр своего подразделения и подчененных сотрудников; ");
                    else strb.append("Выбор своего подразделения и подчененных сотрудников; ");
                } else if (userPrivilege.getPrivMedicalBook().contains(VIEW_EMPLOYEEUNDER)) {
                    jCBUsDepartment.setSelectedItem(MBProgect.getAuthorizedUser().getUsDepartmentName());
                    SelectedMainEmployeeId = MBProgect.getAuthorizedUser().getId();
                    if (!isChoiseMedicalBook) strb.append("Просмотр подчененных сотрудников; ");
                    else strb.append("Выбор подчененных сотрудников; ");
                } else 
                    IsOnlyLineStaff = true;
                
                if (userPrivilege.getPrivMedicalBook().contains(VIEW_LINESTAFF)){
                    jCBUsDepartment.setSelectedItem(MBProgect.getAuthorizedUser().getUsDepartmentName());
                    SelectedMainEmployeeId = !IsOnlyLineStaff ? MBProgect.getAuthorizedUser().getId() : -1;                      
                    jChBLineStaff.setSelected(true);
                    jChBLineStaff.setEnabled(false);                    
                    if (!isChoiseMedicalBook) strb.append("Просмотр линейного персонала; ");
                    else strb.append("Выбор линейного персонала; "); 
                }
            }

            setTitle("База ЛМК. Право доступа (" + strb.toString().substring(0, strb.length() - 2) + ")");
            // ========================= ========================= =========================

            ComboBoxStateChange = true;
            
            jTFFindText.setText(SerchMessageDefault);
            jCBLimitCount.setSelectedIndex(0);
            jChBDeleted.setSelected(false);
            jLRegion.setText("Округ - ");
        } catch (Exception ex) {
            System.out.println("com.Fraims.Bases.JIFMedBookBase.fillFieldsMain() - " + ex.getMessage());
        }
    }

    // ===================== прочие методы =====================        
    private void closeThis() {
        this.setVisible(false);
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1();
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedNumMedBook(-1);
        MBProgect.setJIFMedBookBase(null);
    }

    private void getSelectedUnitsFromTable() {
        if (jTMedaicalBook.getSelectedRow() >= 0) {
            SelectedNumMedicalBook = Integer.valueOf((Integer) jTMedaicalBook.getValueAt(jTMedaicalBook.convertRowIndexToModel(jTMedaicalBook.getSelectedRow()), 1));
            SelectedEmployeeId = MedicalBookTM.get(SelectedNumMedicalBook).getEmployeeId();
            SelectedInstitutionId = MedicalBookTM.get(SelectedNumMedicalBook).getInstitutionId()>0 ? MedicalBookTM.get(SelectedNumMedicalBook).getInstitutionId() : -1;
            jBAddOrChoiseMedBook.setEnabled(true);
            MBProgect.setSelectedEmployeeId(SelectedEmployeeId);
            MBProgect.setSelectedNumMedBook(SelectedNumMedicalBook);
            MBProgect.setSelectedInstitutionId(SelectedInstitutionId);
        } else {
            SelectedNumMedicalBook = -1;
            SelectedEmployeeId = -1;
            SelectedInstitutionId = -1;
            MBProgect.setSelectedEmployeeId(-1);
            MBProgect.setSelectedNumMedBook(-1);
            MBProgect.setSelectedInstitutionId(-1);
            if (isChoiseMedicalBook) {
                jBAddOrChoiseMedBook.setEnabled(false);
            } else {
                jBAddOrChoiseMedBook.setEnabled(true);
            }
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
    // ===================== метод вызова новых окон =====================  
    private void editEmployee() {
        MedicalBook medbook = new MedicalBook();
        getSelectedUnitsFromTable();

        medbook = new SQLQuery<>(medbook).Read(SelectedNumMedicalBook);
        MBProgect.setSelectedEmployeeId(medbook.getEmployeeId());
        MBProgect.openAddOrChangeEmployee();
    }

    private void editMedicalBook() {
        getSelectedUnitsFromTable();
        MBProgect.setSelectedNumMedBook(SelectedNumMedicalBook);
        MBProgect.openAddChangeMedicalBook();
    }

    private void choiseJIFToOpen() {
        if (MBProgect.getJIFMedBookAddorChange() != null) {
            if (JIFcallingFrame.equals(MBProgect.getJIFMedBookAddorChange().getUI())) {
                MBProgect.openAddChangeMedicalBook();
                MBProgect.getJIFMedBookAddorChange().setNumMedicalBook(SelectedNumMedicalBook);
            }
        }        
        if (MBProgect.getJIFEmployeeAddOrChange() != null) {
            if (JIFcallingFrame.equals(MBProgect.getJIFEmployeeAddOrChange().getUI())) {
                MBProgect.openAddOrChangeEmployee();
                MBProgect.getJIFEmployeeAddOrChange().setSelectedNumMedicalBook(SelectedNumMedicalBook);
            }
        }
        closeThis();
    }


    // =========================================================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMTable = new javax.swing.JPopupMenu();
        jMMedicalBook = new javax.swing.JMenu();
        jMIViewMedicalBook = new javax.swing.JMenuItem();
        jMIEditMedicalBook = new javax.swing.JMenuItem();
        jMEmployee = new javax.swing.JMenu();
        jMIViewEmployee = new javax.swing.JMenuItem();
        jMIEditEmployee = new javax.swing.JMenuItem();
        jMinstitution = new javax.swing.JMenu();
        jMIViewInstitution = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMIEditInstitution = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTMedaicalBook = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jCBUsGroup = new javax.swing.JComboBox<>();
        jCBUsDepartment = new javax.swing.JComboBox<>();
        jCBMedicalBookWithoutEmployees = new javax.swing.JCheckBox();
        jChBDeleted = new javax.swing.JCheckBox();
        jChBLineStaff = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jCBPosition = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLRegion = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTFFindText = new javax.swing.JTextField();
        jCBLimitCount = new javax.swing.JComboBox<>();
        jBAddOrChoiseMedBook = new javax.swing.JButton();
        jBShow = new javax.swing.JButton();
        jChBAll = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();

        jMMedicalBook.setText("ЛМК");

        jMIViewMedicalBook.setText("Просмотр ЛМК");
        jMMedicalBook.add(jMIViewMedicalBook);

        jMIEditMedicalBook.setText("Редактировать ЛМК");
        jMIEditMedicalBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEditMedicalBookActionPerformed(evt);
            }
        });
        jMMedicalBook.add(jMIEditMedicalBook);

        jPMTable.add(jMMedicalBook);

        jMEmployee.setText("Сотрудник");

        jMIViewEmployee.setText("Просмотр сотрудника");
        jMEmployee.add(jMIViewEmployee);

        jMIEditEmployee.setText("Редактировать сотрудника");
        jMIEditEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEditEmployeeActionPerformed(evt);
            }
        });
        jMEmployee.add(jMIEditEmployee);

        jPMTable.add(jMEmployee);

        jMinstitution.setText("Учереждения");
        jMinstitution.setToolTipText("");
        jMinstitution.setActionCommand("");

        jMIViewInstitution.setText("Показать данные учереждения");
        jMIViewInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIViewInstitutionActionPerformed(evt);
            }
        });
        jMinstitution.add(jMIViewInstitution);
        jMinstitution.add(jSeparator4);

        jMIEditInstitution.setText("Изменить Учереждение");
        jMIEditInstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIEditInstitutionActionPerformed(evt);
            }
        });
        jMinstitution.add(jMIEditInstitution);

        jPMTable.add(jMinstitution);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("БАЗА ЛМК");
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

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTMedaicalBook.setAutoCreateRowSorter(true);
        jTMedaicalBook.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "№ п/п", "Номер ЛМК", "ФИО", "Год рождения", "Группа", "Подразделение", "Должность", "ФИО руководителя", "Ближайшее", "Статус", "Удаленная"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTMedaicalBook.setFillsViewportHeight(true);
        jTMedaicalBook.setInheritsPopupMenu(true);
        jTMedaicalBook.setShowHorizontalLines(false);
        jTMedaicalBook.setShowVerticalLines(false);
        jTMedaicalBook.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTMedaicalBookMouseMoved(evt);
            }
        });
        jTMedaicalBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTMedaicalBookMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTMedaicalBookMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTMedaicalBookMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTMedaicalBook);
        if (jTMedaicalBook.getColumnModel().getColumnCount() > 0) {
            jTMedaicalBook.getColumnModel().getColumn(0).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(1).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(2).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(3).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(4).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(5).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(6).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(7).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(9).setResizable(false);
            jTMedaicalBook.getColumnModel().getColumn(10).setResizable(false);
        }

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Тут могла быть ваша реклама");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Фильтр");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Группа");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Подразделение");

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

        jCBMedicalBookWithoutEmployees.setBackground(new java.awt.Color(255, 255, 255));
        jCBMedicalBookWithoutEmployees.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBMedicalBookWithoutEmployees.setText("ЛМК без сотрудников");
        jCBMedicalBookWithoutEmployees.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBMedicalBookWithoutEmployeesItemStateChanged(evt);
            }
        });
        jCBMedicalBookWithoutEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMedicalBookWithoutEmployeesActionPerformed(evt);
            }
        });

        jChBDeleted.setBackground(new java.awt.Color(255, 255, 255));
        jChBDeleted.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jChBDeleted.setText("Удаленные");
        jChBDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBDeletedActionPerformed(evt);
            }
        });

        jChBLineStaff.setBackground(new java.awt.Color(255, 255, 255));
        jChBLineStaff.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jChBLineStaff.setText("ЛМК линейного персонала");
        jChBLineStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChBLineStaffActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Должность");

        jCBPosition.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCBPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBPositionActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Показать также:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(jCBMedicalBookWithoutEmployees, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jChBDeleted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator6)
                    .addComponent(jChBLineStaff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBUsDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBUsGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
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
                .addComponent(jLabel6)
                .addGap(4, 4, 4)
                .addComponent(jCBUsGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBUsDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCBMedicalBookWithoutEmployees)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jChBDeleted, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jChBLineStaff)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Количество строк");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("БАЗА ЛМК");

        jLRegion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLRegion.setText("ОКРУГ - ");

        jTFFindText.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTFFindText.setText("Поиск по таблице (Номер ЛМК, ФИО)");
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

        jCBLimitCount.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "25", "50", "100" }));

        jBAddOrChoiseMedBook.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBAddOrChoiseMedBook.setText("Добваить новую ЛМК");
        jBAddOrChoiseMedBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAddOrChoiseMedBookActionPerformed(evt);
            }
        });

        jBShow.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBShow.setText("Показать");
        jBShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBShowActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTFFindText, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                        .addComponent(jChBAll, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBLimitCount, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBShow, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLRegion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBAddOrChoiseMedBook, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBAddOrChoiseMedBook, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLRegion)))
                .addGap(11, 11, 11)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFFindText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBLimitCount, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBShow)
                    .addComponent(jChBAll))
                .addGap(12, 12, 12))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCBUsGroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBUsGroupItemStateChanged

    }//GEN-LAST:event_jCBUsGroupItemStateChanged

    private void jCBUsDepartmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBUsDepartmentItemStateChanged

    }//GEN-LAST:event_jCBUsDepartmentItemStateChanged

    private void jCBUsGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUsGroupActionPerformed
        if (ComboBoxStateChange) {
            ComboBoxStateChange = false;
            ComboBoxStateChange = groupStateChange(jCBUsGroup, jCBUsDepartment, jCBPosition, true);
        }
    }//GEN-LAST:event_jCBUsGroupActionPerformed

    private void jCBUsGroupMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCBUsGroupMousePressed

    }//GEN-LAST:event_jCBUsGroupMousePressed

    private void jBAddOrChoiseMedBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAddOrChoiseMedBookActionPerformed
        if (isChoiseMedicalBook) {
            choiseJIFToOpen();
        } else {
//            MBProgect.openAddChangeMedicalBook();
//            MBProgect.setSelectedNumMedBook(-1);
//            closeThis();
        }
    }//GEN-LAST:event_jBAddOrChoiseMedBookActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jTMedaicalBookMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTMedaicalBookMouseReleased
        try{
            JTable source = (JTable) evt.getSource();
            int row = source.rowAtPoint(evt.getPoint());
            int column = source.columnAtPoint(evt.getPoint());
            jTMedaicalBook.setRowSelectionInterval(row, row);
            
            getSelectedUnitsFromTable();
            if (evt.getClickCount() >= 2) {
                if (isChoiseMedicalBook) {
                    choiseJIFToOpen();
                } else {
                    //открыть окно краткого просмотра информации
                    //MBProgect.openAddChangeMedicalBook();                      
                }
            }

            if (evt.isPopupTrigger()) {
                
                if (row >= 0) {
                    source.changeSelection(row, column, false, false);
                    MBProgect.setSelectedNumMedBook(Integer.valueOf((Integer) source.getModel().getValueAt(source.convertRowIndexToModel(source.getSelectedRow()), 1)));
                    jPMTable.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        }catch(Exception ex){
            System.out.println("com.Fraims.Bases.JIFMedBookBase.jTMedaicalBookMouseReleased()");
        }
    }//GEN-LAST:event_jTMedaicalBookMouseReleased

    private void jMIEditEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEditEmployeeActionPerformed
        editEmployee();
    }//GEN-LAST:event_jMIEditEmployeeActionPerformed

    private void jMIEditMedicalBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEditMedicalBookActionPerformed
        editMedicalBook();
    }//GEN-LAST:event_jMIEditMedicalBookActionPerformed

    private void jTFFindTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFFindTextFocusLost
        if (jTFFindText.getText().equals(""))
            jTFFindText.setText(SerchMessageDefault);
    }//GEN-LAST:event_jTFFindTextFocusLost

    private void jTFFindTextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFFindTextMousePressed
        if (jTFFindText.getText().equals(SerchMessageDefault))
            jTFFindText.setText("");
    }//GEN-LAST:event_jTFFindTextMousePressed

    private void jTFFindTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFindTextActionPerformed
        FillDataToTable();
    }//GEN-LAST:event_jTFFindTextActionPerformed

    private void jChBDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBDeletedActionPerformed
        FillDataToTable();
    }//GEN-LAST:event_jChBDeletedActionPerformed

    private void jCBUsDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUsDepartmentActionPerformed
        if (ComboBoxStateChange){ 
            ComboBoxStateChange = false;
            ComboBoxStateChange = departmentStateChange(jCBUsDepartment, jCBPosition, true);
        }
    }//GEN-LAST:event_jCBUsDepartmentActionPerformed

    private void jBShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBShowActionPerformed
        FillDataToTable();
    }//GEN-LAST:event_jBShowActionPerformed

    private void jChBAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBAllActionPerformed

    }//GEN-LAST:event_jChBAllActionPerformed

    private void jChBAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jChBAllItemStateChanged

    }//GEN-LAST:event_jChBAllItemStateChanged

    private void jCBMedicalBookWithoutEmployeesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBMedicalBookWithoutEmployeesItemStateChanged

    }//GEN-LAST:event_jCBMedicalBookWithoutEmployeesItemStateChanged

    private void jCBMedicalBookWithoutEmployeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMedicalBookWithoutEmployeesActionPerformed
        FillDataToTable();
    }//GEN-LAST:event_jCBMedicalBookWithoutEmployeesActionPerformed

    private void jChBLineStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChBLineStaffActionPerformed
        FillDataToTable();
    }//GEN-LAST:event_jChBLineStaffActionPerformed

    private void jCBPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBPositionActionPerformed
        if (ComboBoxStateChange) FillDataToTable();
    }//GEN-LAST:event_jCBPositionActionPerformed

    private void jMIEditInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIEditInstitutionActionPerformed
        getSelectedUnitsFromTable();
        MBProgect.setSelectedInstitutionId(SelectedInstitutionId);
        MBProgect.openAddOrChangeInstitution();
    }//GEN-LAST:event_jMIEditInstitutionActionPerformed

    private void jMIViewInstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIViewInstitutionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMIViewInstitutionActionPerformed

    private void jTMedaicalBookMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTMedaicalBookMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTMedaicalBookMousePressed

    private void jTMedaicalBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTMedaicalBookMouseClicked
        //getChoiseEmployeeId();
        //System.out.println(evt.getButton());
    }//GEN-LAST:event_jTMedaicalBookMouseClicked

    private void jTMedaicalBookMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTMedaicalBookMouseMoved
        try {
            JTable source = (JTable) evt.getSource();
            int row = source.rowAtPoint(evt.getPoint());
            int column = source.columnAtPoint(evt.getPoint());
            if (row >= 0 && column == 8) {
                StringBuilder TollTip = new StringBuilder(source.getModel().getValueAt(source.convertRowIndexToModel(row), column).toString());
                String[] text1 = TollTip.toString().split(", ");
                StringBuilder Str2 = new StringBuilder();
                Str2.append("<HTML>");
                for (int i = 0; i < text1.length; i++) {
                    Str2.append(text1[i]);
                    Str2.append("<br>");
                }
                source.setToolTipText(Str2.toString());
            } else {
                source.setToolTipText(null);
            }
        } catch (Exception ex) {
            System.out.println("com.Fraims.Bases.JIFMedBookBase.jTMedaicalBookMouseMoved()");
        }
    }//GEN-LAST:event_jTMedaicalBookMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAddOrChoiseMedBook;
    private javax.swing.JButton jBShow;
    private javax.swing.JComboBox<String> jCBLimitCount;
    private javax.swing.JCheckBox jCBMedicalBookWithoutEmployees;
    private javax.swing.JComboBox<String> jCBPosition;
    private javax.swing.JComboBox<String> jCBUsDepartment;
    private javax.swing.JComboBox<String> jCBUsGroup;
    private javax.swing.JCheckBox jChBAll;
    private javax.swing.JCheckBox jChBDeleted;
    private javax.swing.JCheckBox jChBLineStaff;
    private javax.swing.JLabel jLRegion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMEmployee;
    private javax.swing.JMenuItem jMIEditEmployee;
    private javax.swing.JMenuItem jMIEditInstitution;
    private javax.swing.JMenuItem jMIEditMedicalBook;
    private javax.swing.JMenuItem jMIViewEmployee;
    private javax.swing.JMenuItem jMIViewInstitution;
    private javax.swing.JMenuItem jMIViewMedicalBook;
    private javax.swing.JMenu jMMedicalBook;
    private javax.swing.JMenu jMinstitution;
    private javax.swing.JPopupMenu jPMTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTextField jTFFindText;
    private javax.swing.JTable jTMedaicalBook;
    // End of variables declaration//GEN-END:variables

    @Override
    public MainInternalFrame choiseMain(InternalFrameUI JIFcallingFrame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
