package org.en.tealEye.printing.service;

/**
 * Created by roman.stumm@viaboxx.de
 * User: roman
 * Date: 19.05.2011
 * Time: 18:16:57
 * (c) Viaboxx GmbH, Koenigswinter, 2011
 */
public class LabelEntry {
    private String spielortName;
    private String spielortNameLine1;
    private String spielortNameLine2;
    private String gruppenLabel;
    private String ligaName;
    private String strasse;
    private String plzUndOrt;
    private int anzahl;
    private long spielortId;

    public String getSpielortName() {
        return spielortName;
    }

    public long getSpielortId() {
        return spielortId;
    }

    String[] toStringArray() {
        String[] array = new String[7];
        array[0] = spielortNameLine1;
        array[1] = spielortNameLine2;
        array[2] = strasse;
        array[3] = plzUndOrt;
        if (anzahl > 1) {
            array[4] = String.valueOf(anzahl);
        } else {
            array[4] = "";
        }
        array[5] = gruppenLabel;
        array[6] = ligaName;
        return array;
    }

    public void setSpielortNameLine1(String line) {
        spielortNameLine1 = line;
    }

    public void setGruppenLabel(String ligagruppenLabel) {
        gruppenLabel = ligagruppenLabel;
    }

    public void setLigaName(String ligaName) {
        this.ligaName = ligaName;
    }

    public void setSpielortNameLine2(String s) {
        spielortNameLine2 = s;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public void setPlzUndOrt(String plzUndOrt) {
        this.plzUndOrt = plzUndOrt;
    }

    public void setAnzahl(int anz) {
        anzahl = anz;
    }

    public void setSpielortId(long spielortId) {
        this.spielortId = spielortId;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setSpielortName(String spielortName) {
        this.spielortName = spielortName;
    }
}
