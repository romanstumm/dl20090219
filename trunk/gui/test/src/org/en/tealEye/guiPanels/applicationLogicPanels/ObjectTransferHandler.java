package org.en.tealEye.guiPanels.applicationLogicPanels;

import de.liga.dart.model.Ligateam;

import javax.swing.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 18.03.2008
 * Time: 02:28:25
 */
public abstract class ObjectTransferHandler extends TransferHandler {
    private static final Log log = LogFactory.getLog(ObjectTransferHandler.class);

    protected abstract Object exportString(JComponent c);
    protected abstract void importString(JComponent c, Ligateam str);
    protected abstract void cleanup(JComponent c, boolean remove);
    protected abstract void cleanTableRow(JTable t,int index);

    protected int[] sourceIndex;
    protected int targetIndex;
    protected JComponent source;
    protected JComponent target;
    protected boolean listDrag = false;
    protected Vector<String> v = new Vector();




    private SourceHolder sh = new SourceHolder();

    static final DataFlavor OBJECT_FLAVOR = new DataFlavor(Object.class,"Ligateam");
    Object o = null;

    protected ExtendendTransferable createTransferable(JComponent c) {
        Object o = null;
        if(c instanceof JTable){
        v.add("Table");
        Clipboard clip = new Clipboard("Table");
            //clip.setContents(new ObjectTransferable(o,source));
        o = ((JTable)c).getValueAt(((JTable)c).getSelectedRow(),1);
        source = c;
        } else if(c instanceof JList){
        v.add("List");
        o = ((JList)c).getSelectedValue();
        source = c;
        listDrag = true;
        }
        return new ObjectTransferable(o, source);
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    public boolean importData(JComponent c, ExtendendTransferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
               isTargetRowEmpty(c,t);
               t.getClass();

            try {
                Object str = t.getTransferData(OBJECT_FLAVOR);
                importString(c, (Ligateam) str);
                return true;
            } catch (Exception ufe) {
               log.error("", ufe);
            }
        }
        return false;
    }



    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }


    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        int i = 0;
        while (i < flavors.length) {
            if (OBJECT_FLAVOR.equals(flavors[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    private void isTargetRowEmpty(JComponent c, ExtendendTransferable t){
        /*if(c instanceof JTable){
        }
        if(c instanceof JListExt){
        } */
    }
}

