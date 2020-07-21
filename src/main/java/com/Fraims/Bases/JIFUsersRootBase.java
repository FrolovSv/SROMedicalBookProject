package com.Fraims.Bases;

import com.Class.*;
import com.Progect.MedicalBookProgectMaven;
import com.SQL.SQLQuery;
import java.util.TreeMap;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class JIFUsersRootBase extends javax.swing.JInternalFrame {
    
    private TreeMap<Integer,UserLogin> userRootTM;    
    private static DefaultTableModel TableUserRoot;      
    private MedicalBookProgectMaven MBProgect;     
    private Integer SelectedUserRootId;     
    private Integer SelectedUserPrivilegeId;    
    
    private Boolean isChoiseUserRoot = false;
    
    private String JIFCallingName;
    
    // ================== сеттеры ==================
    public void setChoiseUserRoot(String JIFCallingName){
        this.JIFCallingName = JIFCallingName;
        isChoiseUserRoot = true;
        FillFieldsMain();
        FillDataToTable();
    }
    
    // ================== обяъвление класса ==================
    public JIFUsersRootBase(MedicalBookProgectMaven MedicalBookProgect) {
        this.MBProgect = MedicalBookProgect;
        initComponents();
        //this.AuthorizedUserId = MBprogect.getAuthorizedUserId();
        userRootTM = new TreeMap<>();   
        
        jTUserRoot = new TableResize().setTableColumnsWidth(jTUserRoot,500);
        TableUserRoot = (DefaultTableModel) jTUserRoot.getModel();
        FillFieldsMain();
        FillDataToTable();       
    }   
    
    
    // ================== загрузка данных и установка значений ==================
    public boolean FillDataToTable(){
        try{
            Thread MBThread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try{                         
                        Integer Limit = Integer.valueOf(jComboBox10.getSelectedItem().toString());
                        String SearchText = jTextField12.getText();
                        if (jCheckBox3.isSelected()) Limit = -1;
                        if (SearchText.equals("Поиск по таблице (ФИО, Должность, Подразделение, Право доступа)")) SearchText = "";
                        
                        UserLogin serhUserRoot = new UserLogin();
                        serhUserRoot.setStatus(jCheckBox4.isSelected()? EnumStatus.BLOCKED : EnumStatus.ACTIVE);
                        serhUserRoot.setTextSearch(SearchText);
                          
                        userRootTM = new SQLQuery<>(serhUserRoot).ReadForTable(Limit);
                        Object[] Data;
                        while(TableUserRoot.getRowCount()>0)
                            TableUserRoot.removeRow(0);
                        for (UserLogin v : userRootTM.values()) {
                            if (v.getId()>0){
                                Data = v.getDataForTable();
                                Data[0] = TableUserRoot.getRowCount()+1;
                                TableUserRoot.insertRow(TableUserRoot.getRowCount(), Data);
                            }
                        }
                        new TableResize().setTableColumnsWidth(jTUserRoot,500);
                    }catch(Exception ex){
                        System.out.println("Fraims.JIFUsersBase.setDataToTable()" + ex.getMessage());
                    }                 
                }
            });
            MBThread.run();           
            return true;            
        }catch(Exception ex){
            System.out.println("Fraims.JIFBaseEmployees.SQLSelectDataRequest() -"+ex.getMessage());
            return false;
        }
    }
    
    private void FillFieldsMain(){
        if (isChoiseUserRoot) {
            jBAddUserRoot.setText("ВЫБРАТЬ пользователя");
            jBAddUserRoot.setEnabled(false);
        }
        else{
            jBAddUserRoot.setText("ДОБАВИТЬ нового пользователя");
            jBAddUserRoot.setEnabled(true);
        }
            
        jLRegion.setText("Округ - ");
        jTextField12.setText("Поиск по таблице (ФИО, Должность, Подразделение, Право доступа)");
        jComboBox9.setSelectedIndex(0);
        jCheckBox3.setSelected(false);
        jComboBox10.setSelectedIndex(0);  
    }    
    

    ////====================== прочее //======================
    private void SelectLimitBaseEmloyee(){
        if(jCheckBox3.isSelected()){
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
                jCheckBox3.setSelected(true);
            }         
        }else{
            FillDataToTable();
            jCheckBox3.setSelected(false);
        }      
    }
    
    private void getChoiseUserRootId(){
        if (jTUserRoot.getSelectedRow()>=0){
            SelectedUserRootId = Integer.valueOf((Integer)jTUserRoot.getValueAt(jTUserRoot.convertRowIndexToModel(jTUserRoot.getSelectedRow()), 1));
            String UserPrivilegeId = jTUserRoot.getValueAt(jTUserRoot.convertRowIndexToModel(jTUserRoot.getSelectedRow()), 6).toString();
            //SelectedUserPrivilegeId = !UserPrivilegeId.equals("") ? Integer.valueOf(UserPrivilegeId) : 0;
            SelectedUserPrivilegeId = userRootTM.get(SelectedUserRootId).getUserPrivilegeId();
            
            MBProgect.setSelectedUserPrivilegeId(SelectedUserPrivilegeId);
            MBProgect.setSelectedUserRootId(SelectedUserRootId);    
            jBAddUserRoot.setEnabled(true);
        }  else {
            SelectedUserRootId = -1;
            if (isChoiseUserRoot)
                jBAddUserRoot.setEnabled(false);
            else 
                jBAddUserRoot.setEnabled(true);
        }   
        
    }
    
    private void closeThis(){
        this.setVisible(false);      
        JDesktopPane jDesktopPane1 = MBProgect.getjDesktopPane1(); 
        jDesktopPane1.remove(this);
        MBProgect.setjDesktopPane1(jDesktopPane1);
        MBProgect.setSelectedUserPrivilegeId(-1);
        MBProgect.setSelectedUserRootId(-1);
        MBProgect.setJIFUsersRootBase(null);
    }
    
    private void choiseJIFToOpen(){
//        if (MBProgect.getJIFMedBookAddorChange()!=null)
//            if (JIFCallingName.equals(MBProgect.getJIFMedBookAddorChange().getUI().toString())){
//                MBProgect.openAddChangeMedicalBook();
//                MBProgect.getJIFMedBookAddorChange().setSelectedEmployeeId(SelectedUserRootId);
//        }     
        closeThis();
    }
    
    
    //====================== служебные ======================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPMBaseUserRoot = new javax.swing.JPopupMenu();
        jMChangeUserRoot = new javax.swing.JMenuItem();
        jMChangeUserPrivilege = new javax.swing.JMenuItem();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jTextField12 = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel90 = new javax.swing.JLabel();
        jLRegion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTUserRoot = new javax.swing.JTable();
        jComboBox9 = new javax.swing.JComboBox<>();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jCheckBox3 = new javax.swing.JCheckBox();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel93 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox<>();
        jSeparator14 = new javax.swing.JSeparator();
        jCheckBox4 = new javax.swing.JCheckBox();
        jBAddUserRoot = new javax.swing.JButton();

        jMChangeUserRoot.setText("Изменить данные пользователя");
        jMChangeUserRoot.setToolTipText("");
        jMChangeUserRoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMChangeUserRootActionPerformed(evt);
            }
        });
        jPMBaseUserRoot.add(jMChangeUserRoot);

        jMChangeUserPrivilege.setText("Изменить право пользователя");
        jMChangeUserPrivilege.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMChangeUserPrivilegeActionPerformed(evt);
            }
        });
        jPMBaseUserRoot.add(jMChangeUserPrivilege);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("База пользователей");
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

        jTextField12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField12.setText("Поиск по таблице (ФИО, Должность, Подразделение, Право доступа)");
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

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel90.setText("База пользователей");

        jLRegion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLRegion.setText("Округ");

        jTUserRoot.setAutoCreateRowSorter(true);
        jTUserRoot.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "№пп", "#", "ФИО", "Группа", "Подраздление", "Должность", "Право доступа", "Статус"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jTUserRoot.setFillsViewportHeight(true);
        jTUserRoot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTUserRootMouseReleased(evt);
            }
        });
        jTUserRoot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTUserRootKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTUserRoot);
        if (jTUserRoot.getColumnModel().getColumnCount() > 0) {
            jTUserRoot.getColumnModel().getColumn(0).setResizable(false);
            jTUserRoot.getColumnModel().getColumn(1).setResizable(false);
            jTUserRoot.getColumnModel().getColumn(2).setResizable(false);
            jTUserRoot.getColumnModel().getColumn(3).setResizable(false);
            jTUserRoot.getColumnModel().getColumn(4).setResizable(false);
            jTUserRoot.getColumnModel().getColumn(5).setResizable(false);
            jTUserRoot.getColumnModel().getColumn(6).setResizable(false);
            jTUserRoot.getColumnModel().getColumn(7).setResizable(false);
        }

        jComboBox9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));

        jButton13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton13.setText(">");
        jButton13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton13.setPreferredSize(new java.awt.Dimension(32, 20));

        jButton14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton14.setText(">>");
        jButton14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton14.setPreferredSize(new java.awt.Dimension(32, 20));

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox3.setText("Показать все");
        jCheckBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox3ItemStateChanged(evt);
            }
        });
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(102, 102, 102));
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel93.setText("Количество строк:");

        jComboBox10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "25", "50", "100" }));
        jComboBox10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox10ItemStateChanged(evt);
            }
        });
        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jCheckBox4.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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

        jBAddUserRoot.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBAddUserRoot.setText("ДОБАВИТЬ пользователя");
        jBAddUserRoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAddUserRootActionPerformed(evt);
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
                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBAddUserRoot, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
                        .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(jBAddUserRoot)
                        .addComponent(jCheckBox4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCheckBox3))
                    .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
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

    private void jComboBox10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox10ItemStateChanged
        
    }//GEN-LAST:event_jComboBox10ItemStateChanged

    private void jCheckBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox3ItemStateChanged
        //setDataToTable();
    }//GEN-LAST:event_jCheckBox3ItemStateChanged

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox10ActionPerformed
        Integer Limit = Integer.valueOf(jComboBox10.getSelectedItem().toString());
        if (userRootTM.size()>0&userRootTM.size()!=Limit){            
            FillDataToTable();  
        }
    }//GEN-LAST:event_jComboBox10ActionPerformed

    private void jTUserRootMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTUserRootMouseReleased
        getChoiseUserRootId();
        JTable source = (JTable)evt.getSource();
        int row = source.rowAtPoint( evt.getPoint() );
        int column = source.columnAtPoint( evt.getPoint() );
        
        jTUserRoot.setRowSelectionInterval(row, row);
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
                jPMBaseUserRoot.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTUserRootMouseReleased

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        SelectLimitBaseEmloyee();    
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jTextField12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField12MousePressed
        if(jTextField12.getText().equals("Поиск по таблице (ФИО, Должность, Подразделение, Право доступа)")) 
            jTextField12.setText("");        
    }//GEN-LAST:event_jTextField12MousePressed

    private void jTextField12FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField12FocusLost
        if(jTextField12.getText().equals("")) 
            jTextField12.setText("Поиск по таблице (ФИО, Должность, Подразделение, Право доступа)");
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

    private void jBAddUserRootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAddUserRootActionPerformed
        if (isChoiseUserRoot){
            choiseJIFToOpen();
        }
        else{
            //MBProgect.setSelectedEmployeeId(-1);
            //MBProgect.openAddOrChangeEmployee();
            //closeThis();
        }  
                
    }//GEN-LAST:event_jBAddUserRootActionPerformed

    private void jMChangeUserRootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMChangeUserRootActionPerformed
//        getChoiseUserRootId();        
//        MBProgect.openAddOrChangeEmployee();
        //MBProgect.getJIFAddorChangeEmployee().setS
        //closeThis();
    }//GEN-LAST:event_jMChangeUserRootActionPerformed

    private void jMChangeUserPrivilegeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMChangeUserPrivilegeActionPerformed
        getChoiseUserRootId();
        MBProgect.openAddOrChangeUserPrivilege();
        //MBProgect.getJIFUserPrivilegeChange().setSelectedPrivilegeUser(SelectedUserPrivilegeId);
        //closeThis();
    }//GEN-LAST:event_jMChangeUserPrivilegeActionPerformed

    private void jTUserRootKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTUserRootKeyTyped
        System.out.println("Table - "+evt.getKeyChar());
    }//GEN-LAST:event_jTUserRootKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAddUserRoot;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLRegion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JMenuItem jMChangeUserPrivilege;
    private javax.swing.JMenuItem jMChangeUserRoot;
    private javax.swing.JPopupMenu jPMBaseUserRoot;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTUserRoot;
    private javax.swing.JTextField jTextField12;
    // End of variables declaration//GEN-END:variables
}
