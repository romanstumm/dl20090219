package de.liga.dart.fileimport.vfs.rangliste;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.05.2009, 16:44:17
 */
@XStreamAlias("vfs")
@XStreamInclude(VfsConfigLiga.class)
public class VfsConfig {

    /**
     * default: false. Auswertung nur bis zum Stichtag (heute einschl.)
     * true: alle Daten (kein Stichtag-Filter für Rangliste, ggf. schnellere Abfrage)
     */
    @XStreamAsAttribute
    private boolean complete;
    
    @XStreamImplicit
    private List<VfsConfigLiga> configLigen;

    public List<VfsConfigLiga> getConfigLigen() {
        return configLigen;
    }

    public void setConfigLigen(List<VfsConfigLiga> configLigen) {
        this.configLigen = configLigen;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
