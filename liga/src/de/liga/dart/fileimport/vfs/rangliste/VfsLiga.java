package de.liga.dart.fileimport.vfs.rangliste;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.05.2009, 19:20:55
 */
public class VfsLiga {
    private long nr;
    private String name;
    private String klasse;

    public long getNr() {
        return nr;
    }

    public void setNr(long nr) {
        this.nr = nr;
    }

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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VfsLiga vfsLiga = (VfsLiga) o;

        if (nr != vfsLiga.nr) return false;
        if (!klasse.equals(vfsLiga.klasse)) return false;
        return name.equals(vfsLiga.name);
    }

    public int hashCode() {
        return (int) (nr ^ (nr >>> 32));
    }
}
