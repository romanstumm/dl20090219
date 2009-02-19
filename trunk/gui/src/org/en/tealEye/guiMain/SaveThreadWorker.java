package org.en.tealEye.guiMain;

import de.liga.dart.automatenaufsteller.service.AutomatenaufstellerService;
import de.liga.dart.common.service.DartService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.ligateam.service.LigateamService;
import de.liga.dart.model.*;
import de.liga.dart.spielort.service.SpielortService;
import org.en.tealEye.framework.PanelMapper;
import org.en.tealEye.framework.TransactionWorker;
import org.en.tealEye.guiExt.ExtPanel.ExtJEditPanel;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateGroup;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateTeam;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 12.11.2007
 * Time: 07:17:23
 */
public class SaveThreadWorker extends TransactionWorker {

    private ExtJEditPanel jPanel = null;
    private final MainAppFrame mainApp;

    public SaveThreadWorker(ExtJEditPanel jPanel, MainAppFrame mainApp) {
        this.jPanel = jPanel;
        this.mainApp = mainApp;
    }

    public Object executeTransaction() throws Exception {
        try {
            mainApp.showProgress(25, "");
            LigaPersistence current = (LigaPersistence) jPanel.getModelEntity();
            if (current.getKeyValue() != 0L) {
                current = ServiceFactory.get(DartService.class).
                        rejoin((LigaPersistence) jPanel.getModelEntity());
                if (current != null) {
                    jPanel.setModelEntity(current);
                }
            }
            PanelMapper.getInstance()
                    .setObjectValues(jPanel.getModelEntity(), jPanel);

            if (jPanel.getModelEntity() instanceof Ligagruppe) {
                GruppenService service =
                        ServiceFactory.get(GruppenService.class);
                Ligagruppe gruppe = (Ligagruppe) jPanel.getModelEntity();
                service.saveLigagruppe(gruppe);
                CreateGroup createGroup = (CreateGroup) jPanel;
                List<Ligateamspiel> spiele =
                        service.findSpieleInGruppe(gruppe);
                for (Ligateamspiel spiel : spiele) {
                    service.deleteSpiel(spiel);
                }
                DartService ds = ServiceFactory.get(DartService.class);
                for (int platzNr = 0; platzNr < 8; platzNr++) {
                    //JExtTextField field = createGroup.getGroupTeamName(platzNr);
                    Ligateam team = (Ligateam) createGroup.getTable().getValueAt(platzNr,1);
                    Boolean spielfrei = (Boolean)createGroup.getTable().getValueAt(platzNr,CreateGroup.C_SPIELFREI);
                    Boolean fixiert = (Boolean) createGroup.getTable().getValueAt(platzNr, CreateGroup.C_FIXIERT);
                    if (team != null && spielfrei.equals(false)) {
                        team = (Ligateam) ds.rejoin(team);
                        service.setTeamIntoGruppe(gruppe, team, platzNr + 1, fixiert);
                    } else if (spielfrei) {
                        service.setSpielfreiIntoGruppe(gruppe, platzNr + 1, fixiert);
                    }
                }
                jPanel.updatePanel(gruppe);   // noewendig um z.B. den TeamStatus neu anzuzeigen!
            } else if (jPanel.getModelEntity() instanceof Ligateam) {
                CreateTeam createTeam = (CreateTeam) jPanel;
                Ligateam team = (Ligateam) createTeam.getModelEntity();
                ListModel wunschFee =
                        createTeam.getWunschListTeams().getModel();
                List<TeamWunsch> wuensche = new ArrayList(wunschFee.getSize());
                for (int i = 0; i < wunschFee.getSize(); i++) {
                    TeamWunsch wunsch = (TeamWunsch) wunschFee.getElementAt(i);
                    wuensche.add(wunsch);
                }
                ServiceFactory.get(LigateamService.class).
                        saveLigateam(team, wuensche);

            } else if (jPanel.getModelEntity() instanceof Automatenaufsteller) {
                ServiceFactory.get(AutomatenaufstellerService.class).
                        saveAutomatenaufsteller(
                                (Automatenaufsteller) jPanel.getModelEntity());
            } else if (jPanel.getModelEntity() instanceof Spielort) {
                ServiceFactory.get(SpielortService.class).
                        saveSpielort((Spielort) jPanel.getModelEntity());
            }
            mainApp.setMessage(current.toInfoString() + " gespeichert");
        } catch (DartValidationException ex) {
            mainApp.setMessage(ex.getMessages().toString());
        } catch (DartException ex) {
            mainApp.setMessage(ex.getMessage());
        }
        mainApp.showProgress(100, "");
        return null;
    }

    protected void displayError(String msg) {
        mainApp.setMessage(msg);
    }

    protected void done() {
        super.done();    // call super!
        mainApp.showProgress(0, "");
    }
}
