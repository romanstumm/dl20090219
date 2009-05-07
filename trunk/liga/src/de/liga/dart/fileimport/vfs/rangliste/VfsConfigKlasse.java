package de.liga.dart.fileimport.vfs.rangliste;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.05.2009, 16:44:49
 */
@XStreamAlias("ligaklasse")
public class VfsConfigKlasse {
    private String name;
    private String klasse;
    private String liganame;
    private String ligameister;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKlasse() {
        return klasse;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public String getLiganame() {
        return liganame;
    }

    public void setLiganame(String liganame) {
        this.liganame = liganame;
    }

    public String getLigameister() {
        return ligameister;
    }

    public void setLigameister(String ligameister) {
        this.ligameister = ligameister;
    }
}
