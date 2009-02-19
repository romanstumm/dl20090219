package org.en.tealEye.guiMain;

import de.liga.dart.exception.DartException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 28.02.2008
 * Time: 16:15:54
 */
public class Hypervisor implements HypervisorAPI {
    private static final Log log = LogFactory.getLog(Hypervisor.class);

    public static final int CREATE_GROUP_CONTROLLER = 0;
    public static final int SHOW_GROUPS_CONTROLLER = 4;
    public static final int LOCATION_CONTROLLER = 1;
    public static final int TEAM_CONTROLLER = 2;
    public static final int VENDOR_CONTROLLER = 3;

    private final Map<String, ExtendedJPanelImpl> panelMapping =
            new HashMap<String, ExtendedJPanelImpl>();

    private final MainAppFrame maf;
    private final ThreadSupervisor ts;

    public Hypervisor(MainAppFrame maf) {
        this.maf = maf;
        this.ts = new ThreadSupervisor(this, maf);

    }

    public void loadClass(String name) {

        Class<? extends ExtendedJPanelImpl> newPanelClass;
        try {
            newPanelClass = (Class<? extends ExtendedJPanelImpl>) Class
                    .forName(
                            "org.en.tealEye.guiPanels.applicationLogicPanels." +
                                    name);
        } catch (ClassNotFoundException e) {
            throw new DartException(e.getMessage(), e);
        }

        try {
            ExtendedJPanelImpl panel = newPanelClass.newInstance();
            panelMapping.put(panel.getName(), panel);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public JPanel showPanel(String name) {
        if (panelMapping.containsKey(name)) {
            maf.activateInternalFrame(name);
        } else {
            loadClass(name);
            ts.startConstrWorkerThread(panelMapping.get(name));
        }
        return panelMapping.get(name);
    }

    public void removePanel(String name) {
        panelMapping.remove(name);
    }

    public void postPropertyChange(ExtendedJPanelImpl p) {
        switch (getController(p.getName())) {

        }

    }

    public void registerBareWorkerThread(TransactionWorker ta) {
        ts.registerBareWorkerThread(ta);
    }

    public void startBareWorkerThread(String name) {
        ts.startBareWorkerThread(name);
    }

    public int getController(String name) {
        if (name.equals("CreateGroup")) {
            return CREATE_GROUP_CONTROLLER;
        } else if (name.equals("ShowGroups")) {
            return SHOW_GROUPS_CONTROLLER;
        } else if (name.equals("CreateLocation") || name.equals("ShowLocations")) {
            return LOCATION_CONTROLLER;
        } else if (name.equals("CreateTeam") || name.equals("ShowTeams")) {
            return TEAM_CONTROLLER;
        } else if (name.equals("CreateVendor") || name.equals("ShowVendors")) {
            return VENDOR_CONTROLLER;
        }
        return 0;
    }

    public void registerNewPanel(ExtendedJPanelImpl threadedClass) {
        panelMapping.put(threadedClass.getName(),threadedClass);
        maf.insertInternalFrame(threadedClass);
    }
}
