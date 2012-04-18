package org.en.tealEye.guiServices.PropSrv;

import java.awt.*;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan
 * Date: 05.11.2007
 * Time: 16:46:08
 * To change this template use File | Settings | File Templates.
 */
public interface Props {
    public static final String PROPERTIES_FILE = System.getProperty("user.dir") + "/config/properties.xml";

//Font Properties

    public static final String DEFAULT_LABEL_FONT_TYPE = "Arial";
    public static final String DEFAULT_TABLE_FONT_TYPE = "Arial";
    public static final String DEFAULT_FORM_FONT_TYPE = "Arial";
    public static final String DEFAULT_ETI_FONT_TYPE = "Arial";
    public static final String DEFAULT_SENDER_FONT_TYPE = "Arial";
    public static final String DEFAULT_ADDRESS_FONT_TYPE = "Arial";

    public static final int DEFAULT_LABEL_FONT_STYLE = 0;
    public static final int DEFAULT_TABLE_FONT_STYLE = Font.BOLD;
    public static final int DEFAULT_FORM_FONT_STYLE = 0;
    public static final int DEFAULT_ETI_FONT_STYLE = 0;
    public static final int DEFAULT_SENDER_FONT_STYLE = 0;
    public static final int DEFAULT_ADDRESS_FONT_STYLE = 0;


    public static final int DEFAULT_LABEL_FONT_SIZE = 12;
    public static final int DEFAULT_TABLE_FONT_SIZE = 12;
    public static final int DEFAULT_FORM_FONT_SIZE = 12;
    public static final int DEFAULT_ETI_FONT_SIZE = 12;
    public static final int DEFAULT_SENDER_FONT_SIZE = 12;
    public static final int DEFAULT_ADDRESS_FONT_SIZE = 12;

    void setProperty(String key, String value);

    String getProperty(String key);

    void setProperties(Properties props);

    Properties getProperties();

    void updateProperties();

    void updateFile();
}
