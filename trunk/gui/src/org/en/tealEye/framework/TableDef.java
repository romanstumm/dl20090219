package org.en.tealEye.framework;

/**
 * describes the structure of the TableModel.
 * It contains an array of ColumnDef which describes the set of columns.
 * It provides some helpful convenience methods and a resource
 *
 * @author Roman Stumm, 2004
 * @see BeanTableModel
 * @see ColumnDef
 */

public class TableDef {
    private final ColumnDef[] columns;

    public TableDef(ColumnDef[] myAttributeses) {
        this.columns = myAttributeses;
    }

    /**
     * @return name of the corresponding column
     */
    public String getColumnName(int aColumnIndex) {
        String string = "";
        if (getColumn(aColumnIndex).header != null) {
            string = getColumn(aColumnIndex).header;
        }
        return string;
    }

    public int getColumnCount() {
        return columns.length;
    }

    public ColumnDef getColumn(int colIdx) {
        return columns[colIdx];
    }

    public ColumnDef[] getColumns() {
        return columns;
    }
}
