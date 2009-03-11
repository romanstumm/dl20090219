package org.en.tealEye.guiServices.FontSrv;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan
 * Date: 05.11.2007
 * Time: 16:44:55
 * To change this template use File | Settings | File Templates.
 */
public class AggregateFonts implements Fonts {

    private Properties props = new Properties();
    private final Map<String, Font> fontMap = new HashMap<String, Font>();

    public AggregateFonts(Properties props) {
        this.props = props;
    }

    private void buildFontMap() {
        Font labelFont = new Font(props.getProperty("LabelFontType"),
                Integer.parseInt(props.getProperty("LabelFontStyle")), Integer.parseInt(props.getProperty("LabelFontSize")));
        Font tableFont = new Font(props.getProperty("TableFontType"),
                Integer.parseInt(props.getProperty("TableFontStyle")), Integer.parseInt(props.getProperty("TableFontSize")));
        Font formFont = new Font(props.getProperty("FormFontType"),
                Integer.parseInt(props.getProperty("FormFontStyle")), Integer.parseInt(props.getProperty("FormFontSize")));
        Font etiFont = new Font(props.getProperty("EtiFontType"),
                        Integer.parseInt(props.getProperty("EtiFontStyle")), Integer.parseInt(props.getProperty("EtiFontSize")));


        fontMap.put("LabelFont", labelFont);
        fontMap.put("TableFont", tableFont);
        fontMap.put("FormFont", formFont);
        fontMap.put("EtiFont", etiFont);
    }

    public Map<String, Font> getFontMap() {
        buildFontMap();
        return fontMap;
    }

}
