package org.en.tealEye.guiServices.IconSrv;

import javax.swing.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan
 * Date: 05.11.2007
 * Time: 16:45:59
 * To change this template use File | Settings | File Templates.
 */
public interface Icons {

    final static String ICON_DIR = System.getProperty("user.dir") + "/media";

    void updateIconMap();

    Map<String, ImageIcon> getIconMap();

    ImageIcon getIcon(String str);
}
