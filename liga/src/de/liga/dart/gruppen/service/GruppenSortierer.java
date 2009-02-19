package de.liga.dart.gruppen.service;

import de.liga.dart.exception.DartException;
import de.liga.dart.gruppen.CheckResult;
import de.liga.dart.gruppen.check.ProgressIndicator;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 27.11.2007 <br/>
 * Time: 17:41:54 <br/>
 * Copyright: Agimatec GmbH
 */
public interface GruppenSortierer {
    void start();

    void stop();

    void setProgressIndicator(ProgressIndicator progressIndicator);

    void saveAufstellung() throws DartException;

    CheckResult getCheckResult();
}
