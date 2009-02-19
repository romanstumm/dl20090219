package de.liga.dart.gruppen;

/**
 * Description:   <br/>
 * User: roman
 * Date: 19.12.2007, 20:09:55
 */
public enum TeamStatus {
    WECHSEL_MUSS(" ! ", "Konflikt, da nicht im Wechsel gesetzt!"), // ! Konflikt
    WECHSEL_SOLL("(!)", "Suboptimal, da Besetzung trotz unterschiedlicher Tage im Wechsel erwünscht"),
    SPIELFREI_WECHSEL_SOLL("(!)", "Spielfreie der Gruppe auf letzten Tag setzen"),
    WUNSCH_MUSS(" ~ ", "Wunsch nicht erfüllt"), // ~ MUSS-Wunsch nicht erfuellt
    WUNSCH_SOLL("(~)", "Wunsch nicht erfüllt"), // ~ Optionaler Wunsch nicht erfuellt
    FIXIERT_OVERRULED(" * ", "Fixiert, sonst wäre es ein Konflikt"), // es wäre eigentlich ein Konflikt oder Wunschkonflikt, doch das Team ist fixiert
    WUNSCH_OVERRULED(" W ", "Ein Wunsch wurde vorrangig berücksichtigt, daher keine Besetzung im Wechsel"),
    GESETZT(" X ", "Im Wechsel gesetzt (gleicher Tag, andere Gruppe)"), // X, Wechselbesetzung
    PAARUNG(" O ", "In Gruppe gesetzt (gleicher Tag, gleiche Gruppe)"), // o  (in einer Gruppe)
    GESETZT_OPTIONAL("(x)", "Im Wechsel gesetzt (anderer Tag)"), // (x) (anderer Tag)
    PAARUNG_OPTIONAL("(o)", "In Gruppe gesetzt (anderer Tag)"), // (o) (in einer Gruppe, anderer Tag)
    FREI("   ", "Frei");

    TeamStatus(String info, String toolTip) {
        this.info = info;
        this.toolTip = toolTip;
    }

    private final String info;
    private final String toolTip;

    public String getInfo() {
        return info;
    }

    public String getToolTip() {
        return toolTip;
    }

    /**
     * hole die Instanz anhand des (eindeutigen) Info-Strings, Beispiel:
     * <pre>
     * TeamStatus gesetzt = TeamStatus.fromInfo(" X ");
     * </pre><br><br>
     *
     * ACHTUNG: Um die Enum anhand des name() zu holen, geht
     * <pre>
     * TeamStatus gesetzt = TeamStatus.valueOf("GESETZT");
     * </pre>
      * @param info
     * @return
     */
    public static TeamStatus fromInfo(String info)
    {
        for(TeamStatus each : values()) {
            if(info.equals(each.getInfo())) return each;
        }
        return null;
    }
}
