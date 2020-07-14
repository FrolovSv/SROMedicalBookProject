package Render;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class CellREndMedBook extends DefaultTableCellRenderer {
        
    JTable table1 = new JTable();   

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DefaultTableCellRenderer tcr = null;
        //table1 = table;
        DefaultTableModel Tablename = (DefaultTableModel) table.getModel();
        
        table1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table1.setFillsViewportHeight(true);
        table1.setGridColor(new java.awt.Color(255, 255, 255));
        table1.setInheritsPopupMenu(true);
        table1.setShowHorizontalLines(false);
        table1.setShowVerticalLines(false);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.setName(""); // NOI18N
        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(false);
        
        int Status = 0;
        String text = (String)Tablename.getValueAt(table.convertRowIndexToModel(row), 8);
        if (text.equals("Оплачено"))
            Status = 0;
        else if (text.equals("Не оплачено"))
            Status = 1;
        else if (text.equals("Удалено"))
            Status = 2;
        
//        StringBuffer Str2 = new StringBuffer();
        Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        if (column==4){            
//            StringBuffer TollTip = (StringBuffer)Tablename.getValueAt(table.convertRowIndexToModel(row), 4);       
//            String[] text1 = TollTip.toString().split(";");
//            Str2.append("<HTML>");
//            for (int i = 0; i < text1.length; i++) {
//                Str2.append(text1[i]);
//                Str2.append("<br>");            
//            }
//            table.setToolTipText(Str2.toString());
//        } else {
//            table.setToolTipText(null);
//        }
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        JTableHeader th = table.getTableHeader();
//        int prefWidthHead = 
//            Math.round((float) th.getFontMetrics(th.getFont()).getStringBounds(th.getTable().getColumnName(column),th.getGraphics()).getWidth());
//        
//        int prefWidth = 
//            Math.round((float) tableCellRendererComponent.getFontMetrics(tableCellRendererComponent.getFont()).
//                getStringBounds(String.valueOf(value),tableCellRendererComponent.getGraphics()).getWidth());        
//        TableColumn column1 = table.getColumnModel().getColumn(column);
//        if (prefWidthHead<=prefWidth)
//            column1.setPreferredWidth(prefWidth + 10);
//        else    column1.setPreferredWidth(prefWidthHead + 10);    
                
        Color Foregroundcolor = new Color(0,0,0);
        Color Backgroundcolor = new Color(255,255,255);
        if (isSelected)
            Backgroundcolor = new Color(0,153,153);
        
        if (Status==0) { 
            Foregroundcolor = new Color(0,204,51);            
            if (isSelected){
                Foregroundcolor = new Color(255,255,255);
            }
        }
        else if (Status==1) {
            Foregroundcolor = new Color(0,0,0);            
        }
        else if (Status==2) {
            Foregroundcolor = new Color(153,153,153);
            if (isSelected){
                Foregroundcolor = new Color(255,255,255);
            }
        }
        
        tableCellRendererComponent.setBackground(Backgroundcolor);
        tableCellRendererComponent.setForeground(Foregroundcolor);  
        

        return tableCellRendererComponent;
    }      
}
