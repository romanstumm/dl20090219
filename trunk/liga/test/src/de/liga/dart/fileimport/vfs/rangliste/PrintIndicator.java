package de.liga.dart.fileimport.vfs.rangliste;

import de.liga.dart.gruppen.check.ProgressIndicator;

/**
 * Description:   <br/>
 * User: roman
 * Date: 09.05.2009, 12:21:08
 */
public class PrintIndicator implements ProgressIndicator {
    public void showProgress(int percent, String message) {
        System.out.println(percent + "% - " + message);
    }
}
