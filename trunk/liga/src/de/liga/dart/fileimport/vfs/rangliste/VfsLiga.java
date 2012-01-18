package de.liga.dart.fileimport.vfs.rangliste;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.05.2009, 19:20:55
 */
public class VfsLiga {
    private int nr;
    private String name;
    private String klasse;

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getName() {
        return name;
    }

    /**
     * Der letzte Teil des Liganamens nach dem Leerzeichen ist
     * der PlainName der Liga
     */
    public String getPlainName() {
        String n = getName().trim();
        int idx = n.lastIndexOf(' ');
        if (idx >= 0 && idx < n.length()) {
            return n.substring(idx + 1);
        } else {
            return null;
        }
    }

    /**
     * Fische die Zahl aus dem Liganamen
     */
    public String getPlainGruppenNr() {
        int start = 0;
        String txt = getName();
        while (start < txt.length() && !Character.isDigit(txt.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < txt.length() && Character.isDigit(txt.charAt(end))) {
            end++;
        }
        return txt.substring(start, end);
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

    public String toString() {
        return getClass().getName() + "{ nr=" + nr + "; name=" + name + ";klasse=" + klasse + "}";
    }
}
