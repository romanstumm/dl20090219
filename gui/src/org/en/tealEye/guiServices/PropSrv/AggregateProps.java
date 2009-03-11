package org.en.tealEye.guiServices.PropSrv;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 05.11.2007
 * Time: 16:44:37
 */
public class AggregateProps implements Props {
    private static final Logger log = Logger.getLogger(AggregateProps.class);
    private Properties props = new Properties();

    public AggregateProps() {
        super();
        readProperties();
    }


    private void readProperties() {

        File f = new File(Props.PROPERTIES_FILE);

        if (f.getTotalSpace() > 0) {

            try {
                FileInputStream fis = new FileInputStream(f);
                props.loadFromXML(fis);
            } catch (FileNotFoundException e) {
                setDefaultProperties(f);
            } catch (InvalidPropertiesFormatException e) {
                setDefaultProperties(f);
            } catch (IOException e) {
                setDefaultProperties(f);
            }
        } else {
            setDefaultProperties(f);
        }
    }

    private void writeProperties() {
        File f = new File(Props.PROPERTIES_FILE);

        try {
            FileOutputStream fos = new FileOutputStream(f);
            props.storeToXML(fos, "properties");
        } catch (FileNotFoundException e) {
            setDefaultProperties(f);
        } catch (InvalidPropertiesFormatException e) {
            setDefaultProperties(f);
        } catch (IOException e) {
            setDefaultProperties(f);
        }

    }


    public Properties getProps() {
        return props;
    }

    private void setDefaultProperties(File f) {
        props = new Properties();
        props.setProperty("LabelFontType", Props.DEFAULT_LABEL_FONT_TYPE);
        props.setProperty("LabelFontStyle", String.valueOf(Props.DEFAULT_LABEL_FONT_STYLE));
        props.setProperty("LabelFontSize", String.valueOf(Props.DEFAULT_LABEL_FONT_SIZE));
        props.setProperty("TableFontType", Props.DEFAULT_TABLE_FONT_TYPE);
        props.setProperty("TableFontStyle", String.valueOf(Props.DEFAULT_TABLE_FONT_STYLE));
        props.setProperty("TableFontSize", String.valueOf(Props.DEFAULT_TABLE_FONT_SIZE));
        props.setProperty("FormFontType", Props.DEFAULT_FORM_FONT_TYPE);
        props.setProperty("FormFontStyle", String.valueOf(Props.DEFAULT_FORM_FONT_STYLE));
        props.setProperty("FormFontSize", String.valueOf(Props.DEFAULT_FORM_FONT_SIZE));
        props.setProperty("EtiFontType", Props.DEFAULT_ETI_FONT_TYPE);
        props.setProperty("EtiFontStyle", String.valueOf(Props.DEFAULT_ETI_FONT_STYLE));
        props.setProperty("EtiFontSize", String.valueOf(Props.DEFAULT_ETI_FONT_SIZE));

        try {
            FileOutputStream propOutFile =
                    new FileOutputStream(f);
            props.storeToXML(propOutFile, "properties");
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }


    public void setProperty(String key, String value) {
        props.setProperty(key, value);
    }

    public void updateFile() {
        writeProperties();
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public void setProperties(Properties props) {
        this.props = props;
    }

    public Properties getProperties() {
        return props;
    }

    public void updateProperties() {
        readProperties();
    }


}
