package org.en.tealEye.guiPanels.applicationLogicPanels;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.dart.spielort.service.SpielortService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TableTransferHandler extends ObjectTransferHandler{
    private int[] rows = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.

    private int internalMoveExport;
    private int internalMoveImport;
    private boolean export = false;
    private boolean tImport = false;

    protected Object exportString(JComponent c) {
        JTable table = (JTable)c;
        rows = table.getSelectedRows();
        int colCount = table.getColumnCount();
        Object team = table.getValueAt(table.getSelectedRow(), 1);
        internalMoveExport = table.getSelectedRow();
        export = true;
        return team;
    }

    protected void importString(JComponent c, Ligateam str) {
        JTable target = (JTable)c;
        int index = target.getSelectedRow();

        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving rows #4,#5,#6 and #7 and
        //attempts to insert the rows after row #5, this would
        //be problematic when removing the original rows.
        //So this is not allowed.


        internalMoveImport = index;
        addIndex = index;
        //String[] values = str.split("\n");
        //addCount = values.length;
        int colCount = target.getColumnCount();
        Spielort service = ServiceFactory.get(SpielortService.class)
                .findSpielortById(str.getSpielort().getSpielortId());
        target.setValueAt(str,index, 1);
        target.setValueAt(service.getSpielortName(),index,2);
        target.setValueAt(str.getWochentagName(), index, 3);
        target.setValueAt(str.getSpielzeit().toString(),index,4 );
        target.setValueAt(str,index, 5);
        target.setValueAt(false, index, 6);
        tImport = true;
    }
    protected void cleanup(JComponent c, boolean remove) {
        JTable source = (JTable)c;

            DefaultTableModel model =
                 (DefaultTableModel)source.getModel();

            //If we are moving items around in the same table, we
            //need to adjust the rows accordingly, since those
            //after the insertion point have moved.
       /* for(int i = 1; i<8;i++){
            if(internalMoveExport == internalMoveImport)
            break;
            if(i == 6)
            model.setValueAt(true,internalMoveExport,i);
            else
            model.setValueAt(null,internalMoveExport,i);
        }*/
        //model.setValueAt(true,internalMoveExport,6);

        internalMoveExport = source.getSelectedRow();
        rows = null;
        addCount = 0;
        addIndex = -1;

    }

    protected void cleanTableRow(JTable target, int index){

    }
}