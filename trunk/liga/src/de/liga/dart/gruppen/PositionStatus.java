package de.liga.dart.gruppen;

/**
 * Description:   <br/>
 * User: roman
 * Date: 19.12.2007, 20:09:55
 */
public enum PositionStatus {
    C_WECHSEL_MUSS(true, " ! ", "Konflikt, da nicht im Wechsel gesetzt!"), // ! Konflikt
    C_WECHSEL_SOLL(true, "(!)",
            "Suboptimal, da Besetzung trotz unterschiedlicher Tage im Wechsel erwünscht"),
    C_SPIELFREI_WECHSEL_SOLL(true, "(!)", "Spielfreie der Gruppe auf letzten Tag setzen"),
    C_WUNSCH_MUSS(true, " ~ ", "Muss-Wunsch nicht erfüllt"), // ~ MUSS-Wunsch nicht erfuellt
    C_WUNSCH_SOLL(true, "(~)", "Soll-Wunsch nicht erfüllt"), // ~ Optionaler Wunsch nicht erfuellt
    C_FIXIERT_CONFLICT(true, " * ",
            "Position ist fixiert, aber Konflikt gefunden"), // ein Konflikt oder Wunschkonflikt, doch das Team ist fixiert
    WUNSCH_OVERRULED(false, " W ",
            "Ein Wunsch wurde vorrangig berücksichtigt, daher keine Besetzung im Wechsel"),
    GESETZT(false, " X ",
            "Im Wechsel gesetzt (gleicher Tag, andere Gruppe)"), // X, Wechselbesetzung
    PAARUNG(false, " O ",
            "In Gruppe gesetzt (gleicher Tag, gleiche Gruppe)"), // o  (in einer Gruppe)
    GESETZT_OPTIONAL(false, "(x)", "Im Wechsel gesetzt (anderer Tag)"), // (x) (anderer Tag)
    PAARUNG_OPTIONAL(false, "(o)",
            "In Gruppe gesetzt (anderer Tag)"), // (o) (in einer Gruppe, anderer Tag)
    FREI(false, "   ", "Frei");

    PositionStatus(boolean conflict, String info, String toolTip) {
        this.info = info;
        this.toolTip = toolTip;
        this.conflict = conflict;
    }

    private final String info;
    private final String toolTip;
    private final boolean conflict;

    public String getInfo() {
        return info;
    }

    public String getToolTip() {
        return toolTip;
    }

    public boolean isConflict() {
        return conflict;
    }

    /**
     * hole die Instanz anhand des (eindeutigen) Info-Strings, Beispiel:
     * <pre>
     * PositionStatus gesetzt = PositionStatus.fromInfo(" X ");
     * </pre><br><br>
     * <p/>
     * ACHTUNG: Um die Enum anhand des name() zu holen, geht
     * <pre>
     * PositionStatus gesetzt = PositionStatus.valueOf("GESETZT");
     * </pre>
     *
     * @param info
     * @return
     */
    public static PositionStatus fromInfo(String info) {
        for (PositionStatus each : values()) {
            if (info.equals(each.getInfo())) return each;
        }
        return null;
    }
}
