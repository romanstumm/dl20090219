package org.en.tealEye.guiPanels.applicationLogicPanels;

import de.liga.dart.model.Ligateam;

import javax.swing.*;

public class ListTransferHandler extends ObjectTransferHandler {
    private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
    
    //Bundle up the selected items in the list
    //as a single string, for export.
    protected Object exportString(JComponent c) {
        JList list = (JList)c;
        indices = list.getSelectedIndices();
        //cleanup(list,true);
        return list.getSelectedValue();
    }

    //Take the incoming string and wherever there is a
    //newline, break it into a separate item in the list.
    protected void importString(JComponent c, Ligateam str) {
        JList target = (JList)c;
        DefaultListModel listModel = (DefaultListModel)target.getModel();
        int index = target.getSelectedIndex();
        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving items #4,#5,#6 and #7 and
        //attempts to insert the items after item #5, this would
        //be problematic when removing the original items.
        //So this is not allowed.
        if (indices != null && index >= indices[0] - 1 &&
              index <= indices[indices.length - 1]) {
            indices = null;
            return;
        }

        int max = listModel.getSize();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        addIndex = index;
        //String[] values = str.split("\n");
        //addCount = values.length;
            listModel.addElement(str);

    }

    //If the remove argument is true, the drop has been
    //successful and it's time to remove the selected items
    //from the list. If the remove argument is false, it
    //was a Copy operation and the original list is left
    //intact.
    protected void cleanup(JComponent c, boolean remove) {

            JList source = (JList)c;
            DefaultListModel model  = (DefaultListModel)source.getModel();
            Ligateam team = (Ligateam)source.getModel().getElementAt(source.getSelectedIndex()) ;
            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly, since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            model.remove(source.getSelectedIndex());

        
        indices = null;
        addCount = 0;
        addIndex = -1;
    }

    protected void cleanTableRow(JTable t, int index) {
        // do nothing
    }



}