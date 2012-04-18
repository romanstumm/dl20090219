package org.en.tealEye.guiExt.ExtPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.controller.PanelController;
import org.en.tealEye.controller.gui.MainController;
import org.en.tealEye.guiMain.DartComponentRegistry;
import org.en.tealEye.guiMain.Hypervisor;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 03.11.2007
 * Time: 08:37:23
 */
public class ExtendedJPanelImpl extends JPanel implements ExtendedJPanel {

    protected static final Log log =
            LogFactory.getLog(ExtendedJPanelImpl.class);

    private String title;
    private Map<String, ImageIcon> iconMap;// = new AggregateIcons().getIconMap();
    private Map<String, Font> fontMap;
    private PanelController controller;
    private JTable JTable;
    private Hypervisor h;

    public ExtendedJPanelImpl() {
        super();
    }

    public void setPanelController(PanelController controller) {
        this.controller = controller;
    }

    public PanelController getPanelController() {
        return controller;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setIconMap(Map<String, ImageIcon> map) {
        this.iconMap = map;
    }

    public Map<String, ImageIcon> getIconMap() {
        return this.iconMap;
    }

    public void setFontMap(Map<String, Font> map) {
        this.fontMap = map;
    }

    public Map<String, Font> getFontMap() {
        return this.fontMap;
    }

    public void setHypervisor(Hypervisor h){
        this.h = h;
    }

    protected void createCollectionModel(Component c) {
        if (c instanceof JComboBox) {
            DartComponentRegistry.getInstance()
                    .setComboBoxModel((JComboBox) c, this);
        } else if (c instanceof JList) {
            DartComponentRegistry.getInstance().setListModel((JList) c, this);
        } else if (c instanceof JTable) {
            DartComponentRegistry.getInstance().setTableModel((JTable) c, this);
        }
    }

    public void updatePanelLayout(Map<String, Font> fontMap) {
        Component[] comp = this.findComponents();
        for (Component c : comp) {
           
                if (c instanceof JLabel) {
                    c.setFont(fontMap.get("LabelFont"));
                } else if (c instanceof JTextField || c instanceof JComboBox ||
                        c instanceof JList || c instanceof JCheckBox ||
                        c instanceof JTree) {
                    c.setFont(fontMap.get("FormFont"));
                } else if (c instanceof JTable || c instanceof JTableHeader) {
                    c.setFont(fontMap.get("TableFont"));
                }


                if (c instanceof JList || c instanceof JTable)
                    c.addMouseListener(controller);
                else if (c instanceof JComboBox)
                    ((JComboBox) c).addActionListener(getPanelController());
                else if (c instanceof JButton){
                    ((JButton) c).addActionListener(getPanelController());
                }
        }
    }

    private Component[] components; // cache them

    protected Component[] findComponents() {
        if (components != null) return components;
        List<Component> found = new ArrayList();
        Class cls = this.getClass();
        while (cls != null) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value instanceof Component) {

                        Component comp = (Component)value;
                        comp.addKeyListener(new MainController(h));
                        found.add(comp);
                    }
                } catch (IllegalAccessException e) {
                    log.error("cannot find component", e);
                }
            }
            cls = cls.getSuperclass();
        }
        components = found.toArray(new Component[found.size()]);
        return components;
    }

    public void updatePanel() {
        doUpdatePanel();
    }

    protected void doUpdatePanel() {
        Component[] comp = this.findComponents();
        for (Component c : comp) {
            if (c.isVisible()) {
                createCollectionModel(c);
            }
        }
    }

    public void clearTextAreas() {

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value instanceof Component) {
                    Component comp = (Component) value;
                    if (comp instanceof JTextField) {
                        JTextField tf = (JTextField) comp;
                        tf.setText("");
                        if (tf instanceof JExtTextField) {
                            ((JExtTextField) tf).removeObject();
                        }
                    } else if (comp instanceof JCheckBox) {
                        ((JCheckBox) comp).setSelected(false);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public JTable getJTable() {
        Component[] c = findComponents();
        for(Component comp : c){
            if(comp instanceof JTable)
            this.JTable = (JTable) comp;
        }
        return JTable;
    }
}
