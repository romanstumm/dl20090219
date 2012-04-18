package de.liga.dart.fileimport;

import de.liga.dart.gruppen.check.ProgressIndicator;

/**
 * Description:   <br/>
 * User: roman
 * Date: 21.03.2008, 12:30:40
 */
public interface DataExchanger {
    /**
     * @return success
     */
    boolean start();

    /**
     * @param liganame
     * @return success
     */
    boolean start(String liganame);

    void setProgressIndicator(ProgressIndicator indicator);
}
