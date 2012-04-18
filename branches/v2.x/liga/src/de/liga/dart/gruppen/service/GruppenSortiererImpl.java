package de.liga.dart.gruppen.service;

import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.exception.DartException;
import de.liga.dart.gruppen.CheckResult;
import de.liga.dart.gruppen.check.GroupOptimizer;
import de.liga.dart.gruppen.check.ModelConverter;
import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.gruppen.check.model.OGroup;
import de.liga.dart.gruppen.check.model.OPosition;
import de.liga.dart.gruppen.check.model.OSetting;
import de.liga.dart.gruppen.check.model.OTeam;
import de.liga.dart.ligateam.service.LigateamService;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Ligateamspiel;

import java.util.List;

/**
 * Description: <br/>
 * User: Stephan
 * Date: 25.11.2007
 * Time: 18:02:05
 */
public class GruppenSortiererImpl implements GruppenSortierer {
    private final GruppenService service;
    private final Liga liga;
    private ProgressIndicator progressIndicator;
    private CheckResult r = new CheckResult();
    private GroupOptimizer optimizer;

    public GruppenSortiererImpl(GruppenService service, Liga liga) {
        this.service = service;
        this.liga = liga;
    }

    /**
     * API - start sortierung
     */
    public void start() {
        r.setStoppedByUserInteraction(false);
        r.start();
        OSetting setting = new ModelConverter(service).convert(liga);
        optimizer = new GroupOptimizer(setting);
        optimizer.setProgressIndicator(progressIndicator);
        optimizer.optimize();
        r.initializeFrom(optimizer);
        r.stop();
    }

    /**
     * API - stop sortierung vorzeitig
     */
    public void stop() {
        r.setStoppedByUserInteraction(true);
        optimizer.setStopSignaled(true);
    }

    /**
     * API - setze fortschrittsanzeiger
     *
     * @param progressIndicator
     */
    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
        if (optimizer != null)
            optimizer.setProgressIndicator(progressIndicator);
    }

    /**
     * API - gib anzeigbares Ergebnis zurück
     *
     * @return
     */
    public CheckResult getCheckResult() {
        return r;
    }

    /**
     * API - speichere optimales Ergebnis in der Datenbank
     *
     * @throws DartException
     */
    public void saveAufstellung() throws DartException {
        LigateamService tservice = ServiceFactory.get(LigateamService.class);
        for (OGroup group : optimizer.getOptimal().getGroups()) {
            Ligagruppe gruppe =
                    service.findLigagruppeById(group.getGroupId());
            List<Ligateamspiel> spiele = service.findSpieleInGruppe(gruppe);
            for(Ligateamspiel spiel : spiele) {
                service.deleteSpiel(spiel);
            }
            for(OPosition pos : group.getPositions()) {
                if(pos.isTeam()) {
                    OTeam team = (OTeam) pos;
                    Ligateam ligateam =
                            tservice.findLigateamById(team.getTeamId());
                    service.setTeamIntoGruppe(gruppe, ligateam,
                            pos.getPosition(), pos.isFixiert());
                } else {
                   service.setSpielfreiIntoGruppe(gruppe, pos.getPosition(), pos.isFixiert());
                }
            }
        }
    }

}
