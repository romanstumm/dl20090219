package org.en.tealEye.guiServices;

import org.en.tealEye.guiServices.FontSrv.AggregateFonts;
import org.en.tealEye.guiServices.IconSrv.AggregateIcons;
import org.en.tealEye.guiServices.PropSrv.AggregateProps;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class GlobalGuiServiceImpl implements GuiService {

    private Map<String, Font> fontMap = new HashMap<String, Font>();
    private Map<String, ImageIcon> iconMap = new HashMap<String, ImageIcon>();

    private final AggregateProps properties;

    public GlobalGuiServiceImpl() {
        this.properties = new AggregateProps();
        getMaps();
    }

    private void getMaps() {
        Properties props = properties.getProperties();
        AggregateFonts fonts = new AggregateFonts(props);
        AggregateIcons icons = new AggregateIcons();

        fontMap = fonts.getFontMap();
        iconMap = icons.getIconMap();


    }


    public Map<String, Font> getFontMap() {
        return fontMap;
    }

    public Map<String, ImageIcon> getIconMap() {
        return iconMap;
    }

    public void updateProps(Properties props) {
        properties.setProperties(props);
        properties.updateFile();
    }

}
