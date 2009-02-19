package org.en.tealEye.guiPanels.applicationLogicPanels;

import javax.swing.*;
import java.awt.datatransfer.Transferable;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 20.03.2008
 * Time: 17:49:00
 */
public interface ExtendendTransferable extends Transferable {

    void setTransferableSource(JComponent source);

    JComponent getTransferableSource();

}
