package org.en.tealEye.guiServices.IconSrv;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan
 * Date: 05.11.2007
 * Time: 16:22:24
 * To change this template use File | Settings | File Templates.
 */
public class AggregateIcons implements Icons {
    private static final Logger log = Logger.getLogger(AggregateIcons.class);

    private final Map<String, ImageIcon> iconMap = new HashMap<String, ImageIcon>();

    public AggregateIcons() {
        loadIcons();
    }

    private void loadIcons() {
        File f = new File(Icons.ICON_DIR);
        File[] fa = new File[f.listFiles().length];
        if (f.isDirectory()) {
            fa = f.listFiles();
        }

        for (File file : fa) {
            try {
                if (file.getName().contains("png")) {
                    BufferedImage img = ImageIO.read(file);
                    ImageIcon icon = new ImageIcon(img);
                    iconMap.put(file.getName(), icon);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public ImageIcon getIcon(String str) {
        return iconMap.get(str);
    }

    public void updateIconMap() {
        loadIcons();
    }

    public Map<String, ImageIcon> getIconMap() {
        return iconMap;
    }
}
