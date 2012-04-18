package org.en.tealEye.guiExt.ExtPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 05.11.2007
 * Time: 16:32:53
 */
public interface ExtendedJPanel {

    void setTitle(String string);

    String getTitle();

    void setIconMap(Map<String, ImageIcon> map);

    Map<String, ImageIcon> getIconMap();

    void setFontMap(Map<String, Font> map);

    Map<String, Font> getFontMap();

    void updatePanelLayout(Map<String, Font> fontMap);

}
