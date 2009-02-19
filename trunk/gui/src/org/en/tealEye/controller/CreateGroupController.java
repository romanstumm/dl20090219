package org.en.tealEye.controller;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligaklasse;
import de.liga.dart.model.Ligateam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.SwingUtils;
import org.en.tealEye.guiExt.ExtPanel.ExtJTablePanel;
import org.en.tealEye.guiExt.ExtPanel.ExtendedJPanelImpl;
import org.en.tealEye.guiExt.ExtPanel.JListExt;
import org.en.tealEye.guiMain.*;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateGroup;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 14.11.2007
 * Time: 00:23:07
 */
public class CreateGroupController extends PanelController {
    private static final Log log = LogFactory.getLog(CreateGroupController.class);


    private CreateGroup createGroup;

    private final Hypervisor h;
    boolean ALTDown = false;

    public CreateGroupController(MainAppFrame mainApp, Hypervisor h) {
        super(mainApp);
//        this.mainApp.addPropertyChangeListener(this); // RSt: entfernt 
        this.h = h;
    }

    public void actionPerformed(ActionEvent e) {
        if (isUpdateInProgress())
            return; // sonst wird beim Blaettern ggf. eine neue Gruppe angelegt!

        JComponent obj = (JComponent) e.getSource();
        ExtendedJPanelImpl parent = SwingUtils.getExtendedJPanel(obj);
        if (obj instanceof JComboBox) {
            if (obj.getName().equals("Combo_Liga")) {
                prepareNewGroup(false);
            } else if (obj.getName().equals("Combo_Ligaklasse")) {
                prepareNewGroup(false);
            }
        } else if (obj instanceof JButton) {
            String ac = obj.getName();


            if ("BACKWARD".equals(ac)) { // vorherige Gruppe
                Ligagruppe previous = ServiceFactory.get(GruppenService.class)
                        .findPreviousGroup((Ligagruppe) createGroup.getModelEntity());
                if (previous != null) {
                    EditThreadWorker worker = new EditThreadWorker(this, createGroup, previous, mainApp);
                    worker.addPropertyChangeListener(this);
                    worker.execute();
                }
            } else if ("FORWARD".equals(ac)) { // naechste Gruppe
                Ligagruppe next = ServiceFactory.get(GruppenService.class)
                        .findNextGroup((Ligagruppe) createGroup.getModelEntity());
                if (next != null) {
                    EditThreadWorker worker = new EditThreadWorker(this, createGroup, next, mainApp);
                    worker.addPropertyChangeListener(this);
                    worker.execute();
                }
            } else if (ac.equals("neu")) {
                mainApp.setMessage("Neue Gruppe");
                prepareNewGroup(true);
            } else if (ac.equals("verifizieren")) {
                SwingWorker worker = new SaveThreadWorker(createGroup, mainApp);
                worker.execute();
                try {
                    worker.get();
                    worker = new CheckThreadWorker(createGroup, mainApp);
                    worker.execute();
                } catch (Exception ex) {
                    log.error("error while saving", ex);
                }
            } else if (ac.equals("speichern")) {
                new SaveThreadWorker(createGroup, mainApp).execute();
            } else if (ac.equals("abbrechen")) {
                mainApp.removeInternalFrame(parent.getName());
                h.removePanel(parent.getName());
            }
        }
    }

    private void prepareNewGroup(boolean forceNew) {
        Ligaklasse klasse = (Ligaklasse) createGroup.getLigaklasse()
                .getSelectedItem();
        Liga liga = (Liga) createGroup.getLiga().getSelectedItem();

        if (!forceNew) {
            Ligagruppe gruppe = (Ligagruppe) createGroup.getModelEntity();
            if (klasse != null) {
                if (gruppe.getLigaklasse() == null ||
                        gruppe.getLigaklasse().getKlassenId() != klasse.getKlassenId()) {
                    forceNew = true;
                }
            }
            if (liga != null) {
                if (gruppe.getLiga() == null || gruppe.getLiga().getLigaId() != liga.getLigaId()) {
                    forceNew = true;
                }
            }
        }
        if (!forceNew) return;
        createGroup.newModelEntity();
        removeItem();

        Ligagruppe gruppe = (Ligagruppe) createGroup.getModelEntity();
        gruppe.setLiga(liga);
        gruppe.setLigaklasse(klasse);
        ServiceFactory.get(GruppenService.class).autonumber(gruppe);
        createGroup.getGruppenName().setText(gruppe.getGruppenName());
        DartComponentRegistry.getInstance()
                .setListModel(createGroup.getLigateamsNochFrei(), createGroup); // zum Schluss!
    }

    public void mouseClicked(final MouseEvent e) {
        final Object obj = e.getSource();
        if (obj instanceof JTable) {
            JTable jtable = (JTable) obj;
            if (jtable.equals(createGroup.getTable())) {
                handleClickInTable(jtable);
            }
        } else if (obj instanceof JListExt) {
            if (ALTDown || e.getClickCount() == 2) {
                Object item =
                        createGroup.getLigateamsNochFrei().getSelectedValue();
                Ligateam team = (Ligateam) item;

                boolean boo = dragItemIntoTable(team);

                if (boo) {
                    DefaultListModel listModel =
                            (DefaultListModel) createGroup
                                    .getLigateamsNochFrei().getModel();
                    listModel.remove(createGroup
                            .getLigateamsNochFrei().getSelectedIndex());
                }
                Ligaklasse klasse = (Ligaklasse) createGroup.getLigaklasse()
                        .getSelectedItem();
                Liga liga = (Liga) createGroup.getLiga().getSelectedItem();
                Ligagruppe gruppe = (Ligagruppe) createGroup.getModelEntity();
                gruppe.setLiga(liga);
                gruppe.setLigaklasse(klasse);
                ServiceFactory.get(GruppenService.class).autonumber(gruppe);
                createGroup.getGruppenName().setText(gruppe.getGruppenName());
            }
        }
    }

    private void handleClickInTable(JTable jtable) {
        if (isUpdateInProgress()) return;
        int rowIdx = jtable.getSelectedRow();
        if (rowIdx < 0) return;

        boolean isSpielfrei =
                Boolean.TRUE.equals(jtable.getValueAt(rowIdx, CreateGroup.C_SPIELFREI));
        if (ALTDown || isSpielfrei) {
            Ligateam team = (Ligateam) jtable.getValueAt(rowIdx, CreateGroup.C_TEAMNAME);
            if (team != null) {
                if (isSpielfrei) {
                    createGroup.removeTeamInTable(rowIdx);
                } else {
                    dragItemFromTable(rowIdx);
                }
                DefaultListModel listModel =
                        (DefaultListModel) createGroup.getLigateamsNochFrei()
                                .getModel();
                listModel.addElement(team);
            }
        }
    }

    private boolean dragItemIntoTable(Ligateam team) {
        boolean boo = false;
        if (team != null) {
            for (int i = 0; i < 8; i++) {
                if (testForFreeRow(i)) {
                    setUpdateInProgress(true);
                    try {
                        createGroup.updateSpielInTable(team, null, i, null);
                    } finally {
                        setUpdateInProgress(false);
                    }
                    boo = true;
                    break;
                } else {
                    boo = false;
                }
            }
        }
        return boo;
    }

    private boolean testForFreeRow(int index) {
        boolean rowFree = false;

        Ligateam team = (Ligateam) createGroup.getTable().getValueAt(index, CreateGroup.C_TEAMNAME);
        if (team == null) {
            Boolean spielfrei =
                    (Boolean) createGroup.getTable().getValueAt(index, CreateGroup.C_SPIELFREI);
            return (spielfrei == null || !spielfrei);
        }
        return rowFree;
    }

    private void dragItemFromTable(int index) {
        createGroup.updateSpielInTable(null, null, index, null);
    }

    private void removeItem() {
        for (int i = 0; i < 8; i++) {
            createGroup.updateSpielInTable(null, null, i, null);
        }
    }

    public void setCreatePanel(CreateGroup panel) {
        this.createGroup = panel;
        this.createGroup.getTable().addKeyListener(this);
        createGroup.getLigateamsNochFrei().addKeyListener(this);
        createGroup.getTable().getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == CreateGroup.C_SPIELFREI) {
                    handleClickInTable(createGroup.getTable());
                }
            }
        });
    }

    public void initiateRefreshEvent() {
        Collection<ExtendedJPanelImpl> panels = mainApp.getPanelMap().values();
        for (ExtendedJPanelImpl panel : panels) {
            if (panel instanceof ExtJTablePanel || panel instanceof CreateGroup)
                panel.getPanelController().refreshAndWait(panel);
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.isAltDown() || e.isControlDown()) {
            ALTDown = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (ALTDown) {
            ALTDown = false;
        }
    }

}
