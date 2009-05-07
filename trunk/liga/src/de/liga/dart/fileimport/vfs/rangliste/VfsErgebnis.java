package de.liga.dart.fileimport.vfs.rangliste;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.05.2009, 18:02:41
 */
public class VfsErgebnis {
    private VfsGastHeim heim = new VfsGastHeim();
    private VfsGastHeim gast = new VfsGastHeim();
    private int PAR_RUNDE;
    private int PAR_SPIEL;
    private String PAR_CODE;

    public VfsGastHeim heim() {
        return heim;
    }

    public VfsGastHeim gast() {
        return gast;
    }

    public int getPAR_RUNDE() {
        return PAR_RUNDE;
    }

    public void setPAR_RUNDE(int PAR_RUNDE) {
        this.PAR_RUNDE = PAR_RUNDE;
    }

    public int getPAR_SPIEL() {
        return PAR_SPIEL;
    }

    public void setPAR_SPIEL(int PAR_SPIEL) {
        this.PAR_SPIEL = PAR_SPIEL;
    }

    public String getPAR_CODE() {
        return PAR_CODE;
    }

    public void setPAR_CODE(String PAR_CODE) {
        this.PAR_CODE = PAR_CODE;
    }
}
