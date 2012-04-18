package de.liga.dart.gruppen.check;

import de.liga.dart.gruppen.check.model.*;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 11:37:43
 */
public class ModelConverter {

    private final GruppenService service;

    public ModelConverter(GruppenService service) {
        this.service = service;
    }

    public OSetting convert(Liga liga) {
        OSetting setting = new OSetting();
        List<Ligagruppe> ligagruppen = service.findGruppen(liga, null);

        List postProcess = new ArrayList();
        int groupIndex = 0;
        for (Ligagruppe lgrup : ligagruppen) {
            OGroup grup = new OGroup(lgrup.getGruppenId(), groupIndex++);
            setting.getGroups().add(grup);
            List<Ligateamspiel> spiele = service.findSpieleInGruppe(lgrup);
            for (Ligateamspiel spiel : spiele) {
                if (spiel.isSpielfrei()) {
                    OFree free = new OFree(grup, spiel.isFixiert());
                    free.setPosition(spiel.getPlatzNr());
                    grup.getPositions().add(free);
                } else {
                    Ligateam lteam = spiel.getLigateam();
                    OPub pub =
                            findOrCreatePub(setting,
                                    lteam.getSpielort());
                    OTeam team = new OTeam(grup, pub, lteam.getLigateamId(),
                            lteam.getWochentag(), spiel.isFixiert());
                    team.setPosition(spiel.getPlatzNr());
                    team.setTime(lteam.getSpielzeit());
                    grup.getPositions().add(team);
                    pub.getTeams().add(team);
                    Set<LigateamWunsch> lwuensche = lteam.getWuensche();
                    if (!lwuensche.isEmpty()) {
                        postProcess.add(team);
                        postProcess.add(lteam);
                    }
                }
            }
            if (grup.getPositions().size() <
                    8) { // leere Plaetze mit Spielfrei fuellen
                for (int i = 1; i <= 8; i++) {
                    if (grup.getPosition(i) == null) {
                        OFree free = new OFree(grup, false);
                        free.setPosition(i);
                        grup.getPositions().add(free);
                    }
                }
            }
        }

        for (int i = 0; i < postProcess.size(); i++) {
            OTeam team = (OTeam) postProcess.get(i++);
            Ligateam lteam = (Ligateam) postProcess.get(i);
            Set<LigateamWunsch> lwuensche = lteam.getWuensche();
            team.setWuensche(new ArrayList(lwuensche.size()));
            for (LigateamWunsch lwunsch : lwuensche) {
                TeamWunsch twunsch = new TeamWunsch(lteam, lwunsch);
                OTeam team2 =
                        setting.getTeam(twunsch.getOtherTeam().getLigateamId());
                if (team2 != null) {
                    // es werden nur gesetzte Teams in der gleichen Liga beruecksichtigt
                    OWunsch owunsch =
                            new OWunsch(lwunsch.getWunschId(), team2, lwunsch.getWunschArt());
                    team.getWuensche().add(owunsch);
                }
            }
        }
        return setting;
    }

    private OPub findOrCreatePub(OSetting setting,
                                 Spielort spielort) {
        OPub found = setting.getPub(spielort.getSpielortId());
        if (found == null) {
            found = new OPub(spielort.getSpielortId());
            setting.addPub(found);
        }
        return found;
    }

}
