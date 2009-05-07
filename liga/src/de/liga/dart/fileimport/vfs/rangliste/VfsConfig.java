package de.liga.dart.fileimport.vfs.rangliste;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.05.2009, 16:44:17
 */
@XStreamAlias("vfs")
@XStreamInclude(VfsConfigLiga.class)
public class VfsConfig {
    @XStreamImplicit
    private List<VfsConfigLiga> configLigen;

    public List<VfsConfigLiga> getConfigLigen() {
        return configLigen;
    }

    public void setConfigLigen(List<VfsConfigLiga> configLigen) {
        this.configLigen = configLigen;
    }
}
