package org.en.tealEye.guiMain;

import javax.swing.*;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 28.02.2008
 * Time: 16:49:06
 */
public interface HypervisorAPI {

    void loadClass(String name);

    JPanel showPanel(String name);

    void removePanel(String name);
}
