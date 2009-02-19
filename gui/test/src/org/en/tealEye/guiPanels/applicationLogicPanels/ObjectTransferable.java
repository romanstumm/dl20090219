package org.en.tealEye.guiPanels.applicationLogicPanels;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 18.03.2008
 * Time: 03:42:12
 */
public class ObjectTransferable implements ExtendendTransferable {
    static final DataFlavor OBJECT_FLAVOR = new DataFlavor(Object.class,"Object");
    Object o;
    JComponent source;

    public ObjectTransferable(Object o, JComponent source) {
        this.o = o;
        this.source = source;
    }

  public DataFlavor[] getTransferDataFlavors()
  {
    return new DataFlavor[]{OBJECT_FLAVOR};
  }

  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    return flavor==OBJECT_FLAVOR;
  }

  public Object getTransferData(DataFlavor flavor) throws
         UnsupportedFlavorException, IOException
  {
      
    return o;
  }

    public void setTransferableSource(JComponent source) {
        this.source = source;
    }

    public JComponent getTransferableSource() {
        return source;
    }
}
