package de.liga.dart.gruppen.service;

import de.liga.dart.gruppen.CheckResult;
import de.liga.dart.gruppen.check.GroupCalculator;
import de.liga.dart.gruppen.check.GroupOptimizer;
import de.liga.dart.gruppen.check.ModelConverter;
import de.liga.dart.gruppen.check.model.OGroup;
import de.liga.dart.gruppen.check.model.OSetting;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Ligagruppe;

/**
 * Description:  Hier kommt der Aufruf zum Gruppenchecken an!<br/>
 * User: roman
 * Date: 03.11.2007, 16:14:06
 */
final class GruppenChecker {
    final GruppenService service;

    public GruppenChecker(GruppenService service) {
        this.service = service;
    }

    /**
     * Pr�fen und - falls autoSort==true - auch korrigieren.
     *
     * @param liga - nur Gruppen dieser Liga werden geprueft
     * @return das Ergebnis des Ganzen
     */
    public CheckResult check(Liga liga) {
        CheckResult result = new CheckResult();
        result.setOutcome(CheckResult.Outcome.KEINE_KONFLIKTE);
        result.start();
        OSetting setting = new ModelConverter(service).convert(liga);
        GroupOptimizer optimizer = new GroupOptimizer(setting);
        result.initializeFrom(optimizer);
        result.stop();
        return result;
    }

    public OGroup getGroup(Ligagruppe gruppe) {
        OSetting setting = new ModelConverter(service).convert(gruppe.getLiga());
        GroupCalculator optimizer = new GroupCalculator(setting);
        return optimizer.getCurrent().getGroup(gruppe.getGruppenId());
    }

    public OSetting getAufstellungsStatus(Liga liga) {
        OSetting setting = new ModelConverter(service).convert(liga);
        GroupCalculator optimizer = new GroupCalculator(setting);
        return optimizer.getCurrent();
    }
}
