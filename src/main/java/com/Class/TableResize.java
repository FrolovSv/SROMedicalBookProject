
package com.Class;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


public class TableResize {
    
    public void TableResize(){}
    
    public JTable setTableColumnsWidth(JTable table, int Maxlength) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader th = table.getTableHeader();      
        
        for (int i = 0; i < table.getColumnCount(); i++) {            
            int prefWidth = 0;
            
            for (int j = 0; j < table.getRowCount(); j++) {
                Object value1 = table.getModel().getValueAt(j, i);
                Component tableComponent = (Component) table.getCellRenderer(j, i);
                int prefWidth1 = 
                Math.round((float) tableComponent.getFontMetrics(tableComponent.getFont()).
                    getStringBounds(String.valueOf(value1),tableComponent.getGraphics()).getWidth());
                if (prefWidth1 > prefWidth)
                    prefWidth = prefWidth1;
            }
                      
            int prefWidthHead = 
            Math.round((float) th.getFontMetrics(th.getFont()).getStringBounds(th.getTable().getColumnName(i),th.getGraphics()).getWidth());
            
            TableColumn column1 = table.getColumnModel().getColumn(i);
            if (prefWidthHead <= prefWidth)
                if (prefWidth > Maxlength)
                    column1.setPreferredWidth(Maxlength);
                else    column1.setPreferredWidth(prefWidth + 16);
            else    column1.setPreferredWidth(prefWidthHead + 16); 
        }
        return table;
    }
    
}
