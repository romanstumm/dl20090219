package org.en.tealEye.framework;

import de.liga.util.Value;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * BeanTableModel is an implementation of AbtractTableModel.
 * There are two things needed for a TableModel :
 * A description of the table structure (TableDef) and a list of
 * objects which should be displayed
 *
 * @author Roman Stumm, 2004
 * @see TableDef
 * @see ColumnDef
 */
public class BeanTableModel<E>
        extends AbstractTableModel {
    protected static final Value REFLECT_HELPER = Value.getDefault();

    /**
     * the structure of the tablemodel
     */
    private final TableDef tableDef;
    /**
     * a list of value objects
     */
    private List<E> objects;

    public BeanTableModel(List<E> aList, TableDef aDef) {
        if (aList != null && aList.size() > 0) {
            objects = aList;
        } else {
            objects = new ArrayList<E>();
        }

        tableDef = aDef;
    }

    public BeanTableModel(TableDef aDef) {
        this(new ArrayList<E>(), aDef);
    }

    public int getColumnCount() {
        return tableDef.getColumnCount();
    }

    public int getRowCount() {
        return objects == null ? 0 : objects.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return getValue(getObject(rowIndex), columnIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (!setValue(getObject(rowIndex), columnIndex, aValue)) {
            objects.set(rowIndex, (E) aValue);
        }
    }

    public void touch() {
        for (Object each : objects) {
            for (int i = 0; i < tableDef.getColumnCount(); i++) {
                getValue(each, i);
            }
        }
    }

    public String getColumnName(int column) {
        return tableDef.getColumnName(column);
    }

    public TableDef getTableDescriptor() {
        return tableDef;
    }

    public E getObject(int rowIdx) {
        // Avoid request on non present model data
        if (objects == null)
            return null;
        if (rowIdx >= 0 && objects.size() > rowIdx) {
            return objects.get(rowIdx);
        }
        return null;
    }

    ColumnDef getDescription(int aColIdx) {
        return tableDef.getColumn(aColIdx);
    }

    protected Object getValue(Object bean, int colIdx) {
        final ColumnDef desc = getDescription(colIdx);
        Object value;
        if (desc.property.length() == 0) {
            value = bean;
        } else {
            value = REFLECT_HELPER.eval(bean, desc.property);
        }

        return value;
    }

    boolean setValue(Object bean, int colIdx, Object value) {
        final ColumnDef desc = getDescription(colIdx);
        if (desc.property.length() == 0) {
            return false;
        }
        REFLECT_HELPER.setPath(bean, desc.property, value, value != null);
        return true;
    }

    public List<E> getObjects() {
        return objects;
    }

    public List<E> getSelectedObjects(JTable mytable) {
        int[] selection = mytable.getSelectedRows();
        List<E> selectedObjects = new ArrayList(selection.length);
        for (int each : selection) {
            E eachObj = getObject(each);
            if(eachObj != null) selectedObjects.add(eachObj);
        }
        return selectedObjects;
    }

    public void setObjects(List<E> myList) {
        this.objects = myList;
        this.fireTableDataChanged();
    }

    public void addRow(E obj) {
        objects.add(obj);
        fireTableRowsInserted(getRowCount(), getRowCount());
    }

    public void deleteRow(int rowIndex) {
        objects.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
