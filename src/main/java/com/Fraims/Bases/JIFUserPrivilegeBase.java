package com.Fraims.Bases;

import com.Class.*;
import com.Class.EnumStatus;
import com.Class.UserPrivilege;
import com.Progect.MedicalBookProgectMaven;
import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.TreeMap;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


public class JIFUserPrivilegeBase extends javax.swing.JInternalFrame {
    
    private TreeMap<Integer,UserPrivilege> UserPrivilegeTM;    
    private static DefaultTableModel TableUserPrivilage;      
    private MedicalBookProgectMaven MBProgect;     
    private Integer SelectedUserPrivilegeId;     
    //private Integer SelectedUserPrivilege;    
    
    private Boolean isChoiseUserRoot = false;
    
    private InternalFrameUI JIFCallingFrame;
    
    // ================== сеттеры ==================
    public JIFUserPrivilegeBase Choise(InternalFrameUI JIFCallingFrame){
        this.JIFCallingFrame = JIFCallingFrame;
        isChoiseUserRoot = true;
        FillFieldsMain();
        FillDataToTable();
        this.setVisible(true);
        return this;
    }
    
     public JIFUserPrivilegeBase View(){
        this.JIFCallingFrame = JIFCallingFrame;
        isChoiseUserRoot = false;
        FillFieldsMain();
        FillDataToTable();
        this.setVisible(true);
        return this;
    }
    
    // ================== обяъвление класса ==================
    public JIFUserPrivilegeBase(MedicalBookProgectMaven MedicalBookProgect) {
        this.MBProgect = MedicalBookProgect;
        initComponents();
        UserPrivilegeTM = new TreeMap<>();   
        
        //jTUserPrivilege = new TableResize().setTableColumnsWidth(jTUserPrivilege,900);
        TableUserPrivilage = (DefaultTableModel) jTUserPrivilege.getModel();
//        FillFieldsMain();
//        FillDataToTable();
        this.setVisible(false);
    }   
    
    
    // ================== загрузка данных и установка значений ==================
    public boolean FillDataToTable(){
        try{
            Thread MBThread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try{                          
                        
                        Integer Limit = Integer.valueOf(jCBLimits.getSelectedItem().toString());
                        String SearchText = jTextField12.getText();
                        if (jCBAllRecords.isSelected()) Limit = -1;
                        if (SearchText.equals("Поиск по таблице (Названию, номеру)")) SearchText = "";
                        
                        UserPrivilege serhUserPrivilege = new UserPrivilege();
                        serhUserPrivilege.setStatusPrivilege(jCheckBox4.isSelected()? EnumStatus.BLOCKED : EnumStatus.ACTIVE);
                        serhUserPrivilege.setTextSearch(SearchText);
                        
                        //EmployeeTM = serhEmployee.SQLSelectToTable(jCheckBox3.isSelected()? -1:Limit,new SQLConnection());   
                        UserPrivilegeTM = new SQLQuery<>(serhUserPrivilege).ReadForTable(Limit);
                        Object[] Data;
                        while(TableUserPrivilage.getRowCount()>0)
                            TableUserPrivilage.removeRow(0);
                        for (UserPrivilege v : UserPrivilegeTM.values()) {
                            if (v.getId()>0){
                                Data = v.getDataForTable();
                                Data[0] = TableUserPrivilage.getRowCount()+1;
                                TableUserPrivilage.insertRow(TableUserPrivilage.getRowCount(), Data);
                            }
                        }
                        jTUserPrivilege = new TableResize().setTableColumnsWidth(jTUserPrivilege,900);
                    }catch(Exception ex){
                        System.out.println("Fraims.JIFUserPrivilege.setDataToTable() " + ex.getMessage());
                    }                 
                }
            });
            MBThread.run();           
            return true;            
        }catch(Exception ex){
            System.out.println("Fraims.JIFUserPrivilege.SQLSelectDataRequest() -"+ex.getMessage());
            return false;
        }
    }
    
    private void FillFieldsMain(){
        if (isChoiseUserRoot) {
            jBAddEmp.setText("ВЫБРАТЬ право доступа");
            jBAddEmp.setEnabled(false);
        }
        else{
            jBAddEmp.setText("ДОБАВИТЬ право доступа");
            jBAddEmp.setEnabled(true);
        }
            
        jLRegion.setText("Округ - ");
        jTextField12.setText("Поиск по таблице (Названию, номеру)");
        jComboBox9.setSelectedIndex(0);
        jCBAllRecords.setSelected(false);
        jCBLimits.setSelectedIndex(0);  
    }    
    

    ////====================== прочее //======================
    private void SelectLimitBaseEmloyee(){
        if(jCBAllRecords.isSelected()){
            Object[] options = {"Да, показать","Отмена",};
            int n = JOptionPane.showOptionDialog(null,
                "Большое кол-во записей в базе может привести к замедлению работы программы. \n"
                + "Показать все записи в базе?",
                "Внимание!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
            if(n==0){
                FillDataToTable();
                jCBAllRecords.setSelected(true);
            }         
        }else{
            FillDataToTable();
            jCBAllRecords.setSelected(false);
        }      
    }
    
    private void getChoiseUserRoot(){
        if (jTUserPrivilege.getSelectedRow()>=0){
            SelectedUserPrivilegeId = Integer.valueOf((Integer)jTUserPrivilege.getValueAt(jTUserPrivilege.convertRowIndexToModel(jTUserPrivilege.getSelectedRow()), 1));
            //String NumMedBook = jTUserPrivilege.getValueAt(jTUserPrivilege.convertRowIndexToModel(jTUserPrivilege.getSelectedRow()), 7).toString();
            //SelectedUserPrivilege = !NumMedBook.equals("") ? Integer.valueOf(NumMedBook) : 0;
            
            MBProgect.setSelectedUserPrivilegeId(SelectedUserPrivilegeId); 
            jBAddEmp.setEnabled(true);
        }  else {
            SelectedUserPrivilegeId = -1;
            if (isChoiseUserRoot)
                jBAddEmp.setEnabled(false);
            else 
                jBAddEmp.setEnabled(true);
        }   
        
    }
    
    private void closeThis(){
        this.setVisible(false);      
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedUserPrivilegeId(-1);       
        MBProgect.setJIFUserPrivilegeBase(null);
    }
    
    private void choiseJIFToOpen(){
        if (MBProgect.getJIFUserPrivilegeChange()!=null)
            if (JIFCallingFrame.equals(MBProgect.getJIFUserPrivilegeChange().getUI().toString())){
                MBProgect.openAddOrChangeUserPrivilege();
                MBProgect.getJIFUserPrivilegeChange().setSelectedPrivilegeUser(SelectedUserPrivilegeId);
        }
//        if (MBProgect.getJIFInstitutionAddOrChange()!=null)
//               if (JIFCallingName.equals(MBProgect.getJIFInstitutionAddOrChange().getUI().toString())){
//                    MBProgect.openAddOrChangeInstitution();
//                    MBProgect.getJIFInstitutionAddOrChange().setSelectedEmployeeId(SelectedEmployeeId);
//        }
//        if (MBProgect.getJIFEmployeeAddOrChange()!=null)
//               if (JIFCallingName.equals(MBProgect.getJIFEmployeeAddOrChange().getUI().toString())){
//                    MBProgect.openAddOrChangeEmployee();
//                    MBProgect.getJIFEmployeeAddOrChange().setSelectedMainEmployeeId(SelectedEmployeeId);
//               }
//        if (MBProgect.getJIFEmployeeMedlBookAddorChange()!=null)
//               if (JIFCallingName.equals(MBProgect.getJIFEmployeeMedlBookAddorChange().getUI().toString())){
//                    MBProgect.openAddChangeEmployeeAndMedicalBook();
//                    MBProgect.getJIFEmployeeMedlBookAddorChange().setSelectedMainEmployeeId(SelectedEmployeeId);
//               }
//        if (MBProgect.getJIFInstitutionBase()!=null)
//               if (JIFCallingName.equals(MBProgect.getJIFInstitutionBase().getUI().toString())){
//                    MBProgect.openBaseInstitutions();
//                    MBProgect.getJIFInstitutionBase().setSelectedCuratorEmployeeId(SelectedEmployeeId);
//               }
        closeThis();
    }
    
    
    //====================== служебные ======================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMBaseEmployee = new javax.swing.JPopupMenu();
        jMChangePrivilegeUser = new javax.swing.JMenuItem();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jTextField12 = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel90 = new javax.swing.JLabel();
        jLRegion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTUserPrivilege = new javax.swing.JTable();
        jComboBox9 = new javax.swing.JComboBox<>();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jCBAllRecords = new javax.swing.JCheckBox();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel93 = new javax.swing.JLabel();
        jCBLimits = new javax.swing.JComboBox<>();
        jSeparator14 = new javax.swing.JSeparator();
        jCheckBox4 = new javax.swing.JCheckBox();
        jBAddEmp = new javax.swing.JButton();

        jMChangePrivilegeUser.setText("Изменить данные прав пользователей");
        jMChangePrivilegeUser.setToolTipText("");
        jMChangePrivilegeUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMChangePrivilegeUserActionPerformed(evt);
            }
        });
        jPMBaseEmployee.add(jMChangePrivilegeUser);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("База привелегий");
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

        jTextField12.setText("Поиск по таблице (Названию, номеру)");
        jTextField12.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField12FocusLost(evt);
            }
        });
        jTextField12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField12MousePressed(evt);
            }
        });
        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });

        jLabel90.setText("База привелегий");

        jLRegion.setText("Округ");

        jTUserPrivilege.setAutoCreateRowSorter(true);
        jTUserPrivilege.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "№пп", "#", "Название", "Привелегии", "Статус"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTUserPrivilege.setFillsViewportHeight(true);
        jTUserPrivilege.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTUserPrivilegeMouseReleased(evt);
            }
        });
        jTUserPrivilege.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTUserPrivilegeKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTUserPrivilege);
        if (jTUserPrivilege.getColumnModel().getColumnCount() > 0) {
            jTUserPrivilege.getColumnModel().getColumn(0).setResizable(false);
            jTUserPrivilege.getColumnModel().getColumn(1).setResizable(false);
            jTUserPrivilege.getColumnModel().getColumn(2).setResizable(false);
            jTUserPrivilege.getColumnModel().getColumn(3).setResizable(false);
            jTUserPrivilege.getColumnModel().getColumn(4).setResizable(false);
        }

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));

        jButton13.setText(">");
        jButton13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton13.setPreferredSize(new java.awt.Dimension(32, 20));

        jButton14.setText(">>");
        jButton14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton14.setPreferredSize(new java.awt.Dimension(32, 20));

        jCBAllRecords.setBackground(new java.awt.Color(255, 255, 255));
        jCBAllRecords.setText("Показать все");
        jCBAllRecords.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBAllRecordsItemStateChanged(evt);
            }
        });
        jCBAllRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAllRecordsActionPerformed(evt);
            }
        });

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel93.setForeground(new java.awt.Color(102, 102, 102));
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel93.setText("Количество строк:");

        jCBLimits.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "25", "50", "100" }));
        jCBLimits.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBLimitsItemStateChanged(evt);
            }
        });
        jCBLimits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBLimitsActionPerformed(evt);
            }
        });

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jCheckBox4.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox4.setText("Показать удаленных");
        jCheckBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox4ItemStateChanged(evt);
            }
        });
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jBAddEmp.setText("ДОБАВИТЬ сотрудника");
        jBAddEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAddEmpActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBAddEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCBAllRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBLimits, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 4, Short.MAX_VALUE)))
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
                        .addComponent(jBAddEmp)
                        .addComponent(jCheckBox4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jCBLimits, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBAllRecords))
                    .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        FillDataToTable();
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jCBLimitsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBLimitsItemStateChanged
        
    }//GEN-LAST:event_jCBLimitsItemStateChanged

    private void jCBAllRecordsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBAllRecordsItemStateChanged
        //setDataToTable();
    }//GEN-LAST:event_jCBAllRecordsItemStateChanged

    private void jCBLimitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBLimitsActionPerformed
        Integer Limit = Integer.valueOf(jCBLimits.getSelectedItem().toString());
        if (UserPrivilegeTM.size()>0&UserPrivilegeTM.size()!=Limit){            
            FillDataToTable();  
        }
    }//GEN-LAST:event_jCBLimitsActionPerformed

    private void jTUserPrivilegeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTUserPrivilegeMouseReleased
        getChoiseUserRoot();
        JTable source = (JTable)evt.getSource();
        int row = source.rowAtPoint( evt.getPoint() );
        int column = source.columnAtPoint( evt.getPoint() );
        jTUserPrivilege.setRowSelectionInterval(row, row);
        
        if (evt.getClickCount()>=2){ 
            if (isChoiseUserRoot){
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
                source.changeSelection(row, column, false, false);                
                MBProgect.setSelectedEmployeeId(Integer.valueOf((Integer)source.getModel().getValueAt(source.convertRowIndexToModel(source.getSelectedRow()), 1)));
                jPMBaseEmployee.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTUserPrivilegeMouseReleased

    private void jCBAllRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAllRecordsActionPerformed
        SelectLimitBaseEmloyee();    
    }//GEN-LAST:event_jCBAllRecordsActionPerformed

    private void jTextField12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField12MousePressed
        if(jTextField12.getText().equals("Поиск по таблице (Названию, номеру)")) 
            jTextField12.setText("");        
    }//GEN-LAST:event_jTextField12MousePressed

    private void jTextField12FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField12FocusLost
        if(jTextField12.getText().equals("")) 
            jTextField12.setText("Поиск по таблице (Названию, номеру)");
    }//GEN-LAST:event_jTextField12FocusLost

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        closeThis();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jCheckBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox4ItemStateChanged
        FillDataToTable();
    }//GEN-LAST:event_jCheckBox4ItemStateChanged

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jBAddEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAddEmpActionPerformed
        if (isChoiseUserRoot){
            choiseJIFToOpen();
        }
        else{
            MBProgect.setSelectedUserPrivilegeId(-1);
            MBProgect.openAddOrChangeUserPrivilege();
            closeThis();
        }  
                
    }//GEN-LAST:event_jBAddEmpActionPerformed

    private void jMChangePrivilegeUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMChangePrivilegeUserActionPerformed
        getChoiseUserRoot();    
        MBProgect.openAddOrChangeUserPrivilege();
        //MBProgect.getJIFUserPrivilegeChange().setSelectedPrivilegeUser(SelectedUserPrivilegeId);
        //closeThis();
    }//GEN-LAST:event_jMChangePrivilegeUserActionPerformed

    private void jTUserPrivilegeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTUserPrivilegeKeyTyped
        System.out.println("Table - "+evt.getKeyChar());
    }//GEN-LAST:event_jTUserPrivilegeKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAddEmp;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JCheckBox jCBAllRecords;
    private javax.swing.JComboBox<String> jCBLimits;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLRegion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JMenuItem jMChangePrivilegeUser;
    private javax.swing.JPopupMenu jPMBaseEmployee;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTUserPrivilege;
    private javax.swing.JTextField jTextField12;
    // End of variables declaration//GEN-END:variables
}
