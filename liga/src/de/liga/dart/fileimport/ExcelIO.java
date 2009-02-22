package de.liga.dart.fileimport;

import de.liga.dart.gruppen.check.ProgressIndicator;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.exception.DartException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Description:   <br/>
 * User: roman
 * Date: 22.02.2009, 11:58:43
 */
public abstract class ExcelIO implements DataExchanger {
    private static final Log log = LogFactory.getLog(ExcelIO.class);
    protected ProgressIndicator progressIndicator;
    private boolean success;
    /**
     * tables for excel export/import in sequence
     */
    protected static final String[] TABLES = {
            "db_version",
            "liga",
            "ligaklasse",
            "ligagruppe",
            "automatenaufsteller",
            "spielort",
            "ligateam",
            "ligateamspiel",
            "ligateamwunsch"};

    public boolean start() {
        ServiceFactory.runAsTransaction(new Runnable() {
            public void run() {
                try {
                    exchangeData();
                    success = true;
                } catch(DartException ex) {
                    throw ex;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    success = false;
                }
            }
        });
        return success;
    }

    public boolean start(String liganame) {
        return false;  // not supported
    }

    public void setProgressIndicator(ProgressIndicator indicator) {
        progressIndicator = indicator;
    }

    protected abstract void exchangeData() throws Exception;
}
