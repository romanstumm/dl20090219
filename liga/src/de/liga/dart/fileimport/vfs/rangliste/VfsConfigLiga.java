package de.liga.dart.fileimport.vfs.rangliste;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.05.2009, 16:44:41
 */
@XStreamAlias("liga")
@XStreamInclude(VfsConfigKlasse.class)
public class VfsConfigLiga {
    @XStreamAsAttribute
    private String name;
    @XStreamImplicit
    private List<VfsConfigKlasse> configKlassen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VfsConfigKlasse> getConfigKlassen() {
        return configKlassen;
    }

    public void setConfigKlassen(List<VfsConfigKlasse> configKlassen) {
        this.configKlassen = configKlassen;
    }
}
