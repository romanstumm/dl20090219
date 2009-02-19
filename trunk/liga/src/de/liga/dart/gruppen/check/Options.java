package de.liga.dart.gruppen.check;

/**
 * Description:   <br/>
 * User: roman
 * Date: 12.02.2008, 23:20:55
 */
public class Options {
    public static boolean sortBlacklist = true;
    public static boolean sortWhitelist = true;
    public static boolean sortOptional = true;
    /**
     * # experimentell: true, wenn die Rekursionstiefe nur allmaehlich gesteigert werden soll,
     * # um eine Lösung mit möglichst WENIG Tauschaktionen zu finden.
     * # Effekt: Sortierung dauert u.U. laenger, geht aber erst die Horizontale ab,
     * # bevor tiefer eingestiegen wird.
     * # false: Es wird sofort nach jedem Tausch in die naechsttiefere Rekursionsebene verzweigt.
     */
    public static boolean increaseRecursion = false;
    /**
     * # experimentell: eine Zahl die die Rekursionstiefe angibt, um sehr lange Laufzeiten zu verhindern.
     * # Bei grossen Ligen muss die Zahl erhoeht werden. (Werte normalerweise zwischen 5 und 50?)
     * # Ist die Zahl zu gering, kann es sein, dass die optimale Loesung nicht mit einem Sortierlauf
     * # gefunden wird.
     * # Der Wert 0 bedeutet, dass es keine Obergrenze gibt.
     */
    public static int maxRecursionDepth = 0;
    /**
     * # neu in 2.2.5: true (default) Wenn eine Optimierung gefunden wurde, aus der
     * # recursion aussteigen und von vorn optimieren.
     * # false (altes Verhalten): in der Recursion fortfahren (kann länger dauern)
     */
    public static boolean optimizedRecusionExit = true;
}
