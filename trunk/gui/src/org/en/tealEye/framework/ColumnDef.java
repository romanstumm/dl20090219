package org.en.tealEye.framework;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * an ColumnDef describes only one column of the table.
 * The minimum row insists of the properties name of the belonging value object,
 * the column name and it's cellsize.
 *
 * @author Roman Stumm, 2004-2007
 * @see BeanTableModel
 * @see TableDef
 */
public final class ColumnDef {
    public final String property;
    public final String header;

/*    public final int cellSize;
    public TableCellRenderer tableCellRenderer;
    public TableCellEditor tableCellEditor;*/

    /**
     * @param aProperty  the attribute name of associated value object
     * @param aLabelText name of the column header
     * @param aCellSize  the width of cell
     * @param aRenderer  the renderer of the Celle
     * @param aEditor    the Editor of the Cell
     */
    public ColumnDef(String aProperty, String aLabelText, int aCellSize,
                     TableCellRenderer aRenderer, TableCellEditor aEditor) {
        this(aProperty, aLabelText, aCellSize);
//        tableCellEditor = aEditor;
//        tableCellRenderer = aRenderer;
    }

    /**
     * @param aProperty  the attribute name of associated value object
     * @param aLabelText name of the column header
     * @param aCellSize  the width of cell
     */
    public ColumnDef(String aProperty, String aLabelText, int aCellSize) {
        property = aProperty;
        header = aLabelText;
//        cellSize = aCellSize;
    }

    public ColumnDef(String aProperty, String aLabelText) {
        this(aProperty, aLabelText, 0);
    }

}
