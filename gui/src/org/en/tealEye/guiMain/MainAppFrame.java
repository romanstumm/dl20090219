package org.en.tealEye.guiMain;

import de.liga.dart.Application;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.license.Licensable;
import de.liga.dart.license.License;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.controller.*;
import org.en.tealEye.controller.gui.MenuController;
import org.en.tealEye.controller.gui.WindowController;
import org.en.tealEye.guiExt.ExtPanel.ExtJTablePanel;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;
import org.en.tealEye.guiExt.TitleBarPanel;
import org.en.tealEye.guiMain.FloatPanel.ActiveFrameInfo;
import org.en.tealEye.guiMain.FloatPanel.ActiveFrameMenu;
import org.en.tealEye.guiMain.FloatPanel.FloatingToolbarPanel;
import org.en.tealEye.guiMain.FloatPanel.FloatingTreeMenu;
import org.en.tealEye.guiMain.TaskbarPanel.TaskbarConstr;
import org.en.tealEye.guiPanels.applicationLogicPanels.*;
import org.en.tealEye.guiServices.GlobalGuiService;
import org.en.tealEye.guiServices.GuiService;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MainAppFrame extends Component implements Licensable, ProgressIndicator {
    private static final Log log = LogFactory.getLog(MainAppFrame.class);

    private JDesktopPane desktop;
    private FloatingToolbarPanel floatingToolbar;
    private final Map<String, JInternalFrame> frameMap =
            new HashMap<String, JInternalFrame>();
    private final Map<String, ExtendedJPanelImpl> panelMap =
            new HashMap<String, ExtendedJPanelImpl>();
    public ActiveFrameMenu activeFrameMenu;
    private final GuiService guiService = new GlobalGuiService();
    private final TaskbarConstr task;
    private JFrame jFrame;
    private final MenuController menuController;
    private final WindowController windowController;

    private final CreateGroupController createGroupController;
    private final ShowGroupsController showGroupsController;
    private final TeamController teamController;
    private final LocationController locationController;
    private final VendorController vendorController;
    private JInternalFrame activeFrame;
    private MainMenu mainMenu;
    private ActiveFrameInfo activeFrameInfo;

    private final Hypervisor h;
    private License license;

    public MainAppFrame() {
        this.h = new Hypervisor(this);
        this.menuController = new MenuController(this, h);
        this.windowController = new WindowController(this);
        this.createGroupController = new CreateGroupController(this, h);
        this.showGroupsController = new ShowGroupsController(this, h);
        this.teamController = new TeamController(this, h);
        this.locationController = new LocationController(this, h);
        this.vendorController = new VendorController(this, h);

        task = new TaskbarConstr();
        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Collection<ExtendedJPanelImpl> panels = getPanelMap().values();
                for (ExtendedJPanelImpl panel : panels) {
                    if (panel instanceof ExtJTablePanel || panel instanceof CreateGroup)
                        panel.getPanelController().refreshAndWait(panel);
                }
            }
        });
    }

    //GUI-Erstellung
    private JPanel constructDesktopEnvironment() {
        JPanel jPanel = new JPanel(new GridBagLayout());
        desktop = new JDesktopPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        jPanel.add(desktop, gbc);
        desktop.setBackground(Color.GRAY);

        return jPanel;
    }

    private JToolBar constructFloatingMenuEnvironment() {
        floatingToolbar = new FloatingToolbarPanel();
        TitleBarPanel treeMenu =
                new FloatingTreeMenu(menuController, guiService.getIconMap());
        activeFrameMenu = new ActiveFrameMenu(menuController);
        activeFrameMenu.setIconMap(guiService.getIconMap());
        floatingToolbar.addPanelComponent(treeMenu);
        floatingToolbar.addPanelComponent(activeFrameMenu);
        floatingToolbar
                .addPanelComponent(activeFrameInfo = new ActiveFrameInfo());
        return floatingToolbar;
    }

    private JPanel constructTaskbarEnvironment() {
        return task;
    }

    private JMenuBar constructMainMenu() {
        this.mainMenu = new MainMenu(menuController);
        return mainMenu;
    }

    protected JFrame buildApplicationFrame() {
        jFrame.setTitle(Application.APPLICATION_NAME + " " + Application
                .APPLICATION_VERSION);
        jFrame.setLayout(new BorderLayout());
        jFrame.setSize(1024, 768);
        jFrame.getContentPane()
                .add(constructDesktopEnvironment(), BorderLayout.CENTER);
        jFrame.getContentPane().add(constructMainMenu(), BorderLayout.NORTH);
        jFrame.getContentPane()
                .add(constructFloatingMenuEnvironment(), BorderLayout.WEST);
        jFrame.getContentPane()
                .add(constructTaskbarEnvironment(), BorderLayout.SOUTH);
        this.addKeyListener(teamController);
        desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
        jFrame.addWindowListener(windowController);
        jFrame.setVisible(true);
        jFrame.validate();
        return jFrame;
    }

    public void insertInternalFrame(ExtendedJPanelImpl name) {
        ExtendedJPanelImpl jPanelImpl;
        try {
            jPanelImpl = name;
            panelMap.put(name.getName(), name);
            JInternalFrame jInternalFrame = new JInternalFrame();
            jInternalFrame.setSize(400, 300);
            jInternalFrame.setName(jPanelImpl.getName());
            jInternalFrame.setTitle(jPanelImpl.getTitle());
            jInternalFrame.getContentPane()
                    .add(new JScrollPane(jPanelImpl));
            jInternalFrame.setIconifiable(true);
            jInternalFrame.setMaximizable(true);
            jInternalFrame.setResizable(true);
            jInternalFrame.setClosable(true);
            jInternalFrame.addInternalFrameListener(windowController);
            frameMap.put(jInternalFrame.getName(), jInternalFrame);
            desktop.add(jInternalFrame);
            desktop.getDesktopManager().activateFrame(jInternalFrame);
            desktop.getDesktopManager().maximizeFrame(jInternalFrame);
            jInternalFrame.moveToFront();
            jInternalFrame.setSelected(true);
            jInternalFrame.setVisible(true);
            jInternalFrame.validate();
            activeFrameMenu.addFrameButton(jPanelImpl.getName());
            menuController.setActiveFrameName(jInternalFrame.getName());
        } catch (Exception e) {
            log.error("cannot open panel " + name, e);
        }

    }

    public void removeInternalFrame(String name) {
        closeInternalFrame(name);
        h.removePanel(name);
    }

    public void activateInternalFrame(String panelName) {
        JInternalFrame[] frames = desktop.getAllFrames();
        for (JInternalFrame frame : frames) {
            if (frame.getName().equals(panelName)) {
                frame.moveToFront();
                //desktop.getDesktopManager().deactivateFrame(frame);
                desktop.getDesktopManager().activateFrame(frame);
                try {
                    frame.setSelected(true);
                    menuController.setActiveFrameName(frame.getName());
                } catch (PropertyVetoException e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                frame.moveToBack();
            }
        }
    }

    public void minimizeAllFrames() {
        Object[] obj = frameMap.values().toArray();
        for (Object oframe : obj) {
            JInternalFrame frame = (JInternalFrame) oframe;
            desktop.getDesktopManager().iconifyFrame(frame);
            frame.setResizable(true);
            frame.setMaximizable(true);
            try {
                frame.setIcon(true);
            } catch (PropertyVetoException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void maximizeAllFrames() {
        Object[] obj = frameMap.values().toArray();
        for (Object oframe : obj) {
            JInternalFrame frame = (JInternalFrame) oframe;
            desktop.getDesktopManager().deiconifyFrame(frame);
            desktop.getDesktopManager().maximizeFrame(frame);
            desktop.getDesktopManager().activateFrame(frame);
        }
    }

    public void normalizeAllFrames() {
        Collection<JInternalFrame> obj = frameMap.values();
        int i = 0;
        for (JInternalFrame oframe : obj) {
            if (oframe.isIcon()) try {
                desktop.getDesktopManager().deiconifyFrame(oframe);
                oframe.setIcon(false);

            } catch (PropertyVetoException e) {
                log.error(e.getMessage(), e);
            }
            desktop.getDesktopManager().resizeFrame(oframe, i, i, 450, 450);
            desktop.getDesktopManager().activateFrame(oframe);
            try {
                oframe.setSelected(false);
            } catch (PropertyVetoException e) {
                log.error(e.getMessage(), e);
            }
            oframe.moveToFront();
            i = i + 10;
        }
    }

    public void closeAllFrames() {
        Object[] obj = frameMap.values().toArray();
        for (Object oframe : obj) {
            JInternalFrame frame = (JInternalFrame) oframe;
            desktop.getDesktopManager().closeFrame(frame);
            frameMap.remove(frame.getName());
            h.removePanel(frame.getName());
            activeFrameMenu.removeAllButtons();
        }
    }

    void addController(ExtendedJPanelImpl panel) {
        if (panel instanceof CreateGroup) {
            panel.setPanelController(createGroupController);
            createGroupController.setCreatePanel((CreateGroup) panel);
        }
        if (panel instanceof CreateTeam) {
            panel.setPanelController(teamController);
            teamController.setCreatePanel((CreateTeam) panel);
        }
        if (panel instanceof CreateLocation) {
            panel.setPanelController(locationController);
            locationController.setCreatePanel((CreateLocation) panel);
        }
        if (panel instanceof CreateVendor) {
            panel.setPanelController(vendorController);
            vendorController.setCreatePanel((CreateVendor) panel);
        }
        if (panel instanceof ShowGroups) {
            panel.setPanelController(showGroupsController);
            showGroupsController.setShowPanel((ShowGroups) panel);
        }
        if (panel instanceof ShowTeams) {
            panel.setPanelController(teamController);
            teamController.setShowPanel((ShowTeams) panel);
        }
        if (panel instanceof ShowLocations) {
            panel.setPanelController(locationController);
            locationController.setShowPanel((ShowLocations) panel);
        }
        if (panel instanceof ShowVendors) {
            panel.setPanelController(vendorController);
            vendorController.setShowPanel((ShowVendors) panel);
        }
    }

    //Getter/Setter für GUI-Elemente ( Container, zb Panel, InternalFrames etc )
    public Map<String, JInternalFrame> getFrameMap() {
        return frameMap;
    }

    public void setTaskbarValue(int value) {
        task.setProgress(value);
    }

    public void setTaskbarTask(String task) {
        this.task.setTaskbarTask(task);
    }

    public void setTaskbarMessage(String message) {
        this.task.setTaskbarMessage(message);
    }

    public Map<String, Font> getFontMap() {
        return guiService.getFontMap();
    }

    public void globalUpdateFonts() {
        Collection<ExtendedJPanelImpl> frames = panelMap.values();
        for (ExtendedJPanelImpl panel : frames) {
            panel.updatePanelLayout(guiService.getFontMap());
        }
    }

    public Map<String, ExtendedJPanelImpl> getPanelMap() {
        return panelMap;
    }

    public void hideFloatingToolbar() {
        jFrame.getContentPane().remove(floatingToolbar);
        jFrame.validate();

    }

    public void showFloatingToolbar() {
        jFrame.getContentPane().add(floatingToolbar, BorderLayout.WEST);
        jFrame.validate();

    }

    public void setIcon(String framename, String iconName) {
        if (frameMap.get(framename) != null)
            frameMap.get(framename)
                    .setFrameIcon(guiService.getIconMap().get(iconName));
    }

    public void closeInternalFrame(String name) {
        desktop.getDesktopManager().closeFrame(frameMap.get(name));
        frameMap.remove(name);
        panelMap.remove(name);
        activeFrameMenu.removeFrameButton(name);
    }

    public void run() {
        buildApplicationFrame();
    }

    /**
     * @param originController - ursprung des events
     */
    public void commitRefresh(PanelController originController) {
        this.firePropertyChange("refresh", originController, null);
    }

    public void setActiveFrame(JInternalFrame jIFrame) {
        this.activeFrame = jIFrame;
        if (activeFrame != null &&
                panelMap.get(activeFrame.getName()) instanceof ExtJTablePanel)
            mainMenu.setPrintingEnabled();
        else mainMenu.setPrintingDisabled();
    }

    public JInternalFrame getActiveFrame() {
        return activeFrame;
    }

    public ExtJTablePanel getActiveFramePanel() {
        JPanel panel = panelMap.get(activeFrame.getName());
        if (panel instanceof ExtJTablePanel) return (ExtJTablePanel) panel;
        else return null;
    }

    public void setMessage(String message) {
        activeFrameInfo.setMessage(message);
    }

    public void useLicense(Object license) {
        // security by obscurity... ;-)
        if (license instanceof License) {
            jFrame = new JFrame();  // damit es Probleme gibt, wenn unlizensiert gestartet wird
            this.license = (License) license;
        }
    }

    public License getLicense() {
        return license;
    }

    public GuiService getGuiService() {
        return guiService;
    }

    public void showProgress(int percent, String message) {
        synchronized (task.getProgBar()) {
            if (percent >= 0) {
                task.getProgBar().setValue(percent);
            }
            if (message != null) {
                task.getProgBar().setString(message);
            }
        }
    }
}


