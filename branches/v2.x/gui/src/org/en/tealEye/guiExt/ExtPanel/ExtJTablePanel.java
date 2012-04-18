package org.en.tealEye.guiExt.ExtPanel;

import org.en.tealEye.framework.BeanTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.util.Vector;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 07.11.2007
 * Time: 23:32:12
 */
public abstract class ExtJTablePanel extends ExtendedJPanelImpl {
    public ExtJTablePanel() {
    }

    public abstract Class getModelClass();

    public JTable getPanelTable() {
        Component[] components = this.findComponents();
        for (Component comp : components) {
            if (comp instanceof JTable) return (JTable) comp;
        }
        return null;
    }

    public void printPanelTable() {
        try {
            getPanelTable().print();
        } catch (PrinterException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void printSelectionTable() {
        JTable jtable = new JTable();
        int[] selectedRows = getPanelTable().getSelectedRows();
        int cCount = getPanelTable().getColumnCount();
        TableColumnModel cModel = getPanelTable().getColumnModel();
        BeanTableModel sourceModel = (BeanTableModel) getPanelTable().getModel();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(cCount);
        model.setRowCount(selectedRows.length);
        jtable.setModel(model);
        int j = 0;
        for (int row : selectedRows) {
            Vector v = new Vector();
            for (int i = 0; i < cCount; i++) {
                Object obj = sourceModel.getValueAt(row, i);
                v.addElement(obj);
            }
            j++;
            model.insertRow(j, v);
        }

        JTable table = new JTable(model, cModel);
        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        frame.getContentPane().add(new JScrollPane(table), gbc);
        table.setOpaque(true);
        table.setTableHeader(getPanelTable().getTableHeader());
        table.setFillsViewportHeight(true);
         

        frame.setVisible(true);
        frame.validate();
        jtable.setPreferredScrollableViewportSize(getSize());
        //jtable.setTableHeader(getPanelTable().getTableHeader());
        try {
            table.print();
        } catch (PrinterException e) {
            log.error(e.getMessage(), e);
        }
    }


}
