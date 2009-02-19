package org.en.tealEye.guiPanels.ConfigPanels;

import org.en.tealEye.guiMain.MainAppFrame;
import org.en.tealEye.guiServices.GlobalGuiService;
import org.en.tealEye.guiServices.GuiService;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;
import java.util.Properties;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 05.11.2007
 * Time: 18:53:29
 */
public class MainConfigFrame extends JFrame implements TreeSelectionListener, ActionListener, WindowListener {

    private final MainAppFrame supervisor;
    private final GuiService service = new GlobalGuiService();
    private final JPanel jPanel = new JPanel(new BorderLayout());
//    private Map<String, ExtendedJPanelImpl> panelMap = new HashMap<String, ExtendedJPanelImpl>();
    private FontConfigPanel fConf;

    public MainConfigFrame(MainAppFrame supervisor) {
        super("Einstellungen");
        this.supervisor = supervisor;
        initComponents();

    }

    private void initComponents() {
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new MainConfigTree(this), new JScrollPane(jPanel));
        this.getContentPane().add(new MainConfigMenu(this), BorderLayout.NORTH);
        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        this.setVisible(true);
        this.validate();
        this.addWindowListener(this);
    }

    private void updatePanel(JPanel jPanel) {
        this.jPanel.setVisible(false);
        this.jPanel.removeAll();
        this.jPanel.add(jPanel, BorderLayout.CENTER);
        this.jPanel.setVisible(true);
    }

    public void valueChanged(TreeSelectionEvent e) {
        JTree tree = (JTree) e.getSource();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node.getUserObject().equals("Schriftarten")) {
            fConf = new FontConfigPanel(this);
            fConf.setFontMap(supervisor.getFontMap());
            fConf.updateInternalMap();
            updatePanel(fConf);
//            panelMap.put("FontConfigPanel", fConf);
        } else if (node.getUserObject().equals("Datenbank")) {

        }
    }

    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if (ac.equals("Beenden")) {
            this.dispose();
        } else if (ac.equals("BT_Font_Accept")) {
            service.updateProps(buildNewFontProperties(fConf.getNewFonts()));
            supervisor.globalUpdateFonts();
        } else if (ac.equals("BT_Font_Decline")){
          fConf.setVisible(false);  
        }
    }

    public void windowOpened(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowClosing(WindowEvent e) {
        this.dispose();
    }

    public void windowClosed(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowIconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeiconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowActivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void windowDeactivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private Properties buildNewFontProperties(Map<String, Font> fontMap) {
        Properties props = new Properties();
        props.setProperty("LabelFontType", fontMap.get("LabelFont").getFontName());
        props.setProperty("LabelFontStyle", String.valueOf(fontMap.get("LabelFont").getStyle()));
        props.setProperty("LabelFontSize", String.valueOf(fontMap.get("LabelFont").getSize()));
        props.setProperty("TableFontType", fontMap.get("TableFont").getFontName());
        props.setProperty("TableFontStyle", String.valueOf(fontMap.get("TableFont").getStyle()));
        props.setProperty("TableFontSize", String.valueOf(fontMap.get("TableFont").getSize()));
        props.setProperty("FormFontType", fontMap.get("FormFont").getFontName());
        props.setProperty("FormFontStyle", String.valueOf(fontMap.get("FormFont").getStyle()));
        props.setProperty("FormFontSize", String.valueOf(fontMap.get("FormFont").getSize()));

        return props;
    }
}
