package de.liga.dart.gruppen.service;

import de.liga.dart.common.service.Service;
import de.liga.dart.exception.DartException;
import de.liga.dart.gruppen.CheckResult;
import de.liga.dart.gruppen.PositionStatusInfo;
import de.liga.dart.gruppen.check.model.OSetting;
import de.liga.dart.model.*;

import java.util.List;
import java.util.Set;

public interface GruppenService extends Service {
    Ligagruppe findLigagruppeById(long id);

    /**
     * Speichere eine Gruppe.
     * Die GruppenNummer wird - falls sie noch auf 0 steht - automatisch vergeben.
     * ACHTUNG: Liga + Ligaklasse m�ssen gesetzt sein!
     *
     * @param gruppe
     */
    void saveLigagruppe(Ligagruppe gruppe);

    void autonumber(Ligagruppe gruppe);

    /**
     * Lade all gruppen der liga/klasse
     *
     * @param liga   - filter: liga (nie null!)
     * @param klasse - filter: ligaklasse (null == kein Filterung nach Klassen!)
     * @return order by liga, gruppennr
     */
    List<Ligagruppe> findGruppen(Liga liga, Ligaklasse klasse);

    /**
     *
     * @param liga - null oder eine liga
     * @param ort - null oder ein ort
     * @return
     */
    List<Ligagruppe> findGruppenAndOrt(Liga liga, Spielort ort);

    /**
     * Nur die teams, die in der angeg. liga sind
     * UND noch in keiner ligagruppe sind
     * UND der angegebenen ligaklasse angeh�ren
     * zurueckgeben.
     * @param teamsInGroup - teams, die sich in der gerade offenen Gruppe befinden. ggf. ungespeichert,
     * daher sind diese Teams aus der Ergebnis ZU ENTFERNEN
     * @param gruppeId - die gruppe, die gerade offen ist. Teams, die hieraus entfernt wurden, aber
     * noch in der Datenbank als in der Gruppe gespeichert sind, sind zum Ergebnis ZUZUFUEGEN
     *
     * Note: teamsInGroup und gruppeId duerfen auch null sein!
     *
     * @return
     */
    List<Ligateam> findUnassignedTeams(Liga liga, Ligaklasse klasse, Set<Long> teamsInGroup,
                                       Long gruppeId);

    /**
     * alle spiele (teams/spielfrei) einer group holen.
     *
     * @param gruppe
     * @return
     */
    List<Ligateamspiel> findSpieleInGruppe(Ligagruppe gruppe);

    /**
     * Setze das Team in der Gruppe an den angeg. Platz.
     * Wenn der Platz zuvor durch ein anderes Team belegt wurde (oder
     * spielfrei gesetzt war), �ndert sich der Platz entsprechend und
     * das andere Team verliert seinen Platz in der Gruppe.
     * <p/>
     * Wenn das hier angegebene Team zuvor auf einem anderen Gruppenplatz
     * war, wird es auf den neuen Platz gesetzt.
     *
     * @param gruppe
     * @param team
     * @param platzNr
     * @return
     */
    Ligateamspiel setTeamIntoGruppe(Ligagruppe gruppe, Ligateam team,
                                    int platzNr, boolean fixiert);

    /**
     * Setze in der Gruppe die angegebene Position auf "spielfrei".
     * War hier zuvor ein Team gesetzt, verliert dies seinen Platz
     * in der Gruppe.
     *
     * @param gruppe
     * @param platzNr
     * @return
     */
    Ligateamspiel setSpielfreiIntoGruppe(Ligagruppe gruppe, int platzNr, boolean fixiert);

    void deleteSpiel(Ligateamspiel spiel);

    /**
     * Pruefe alle Gruppen dieser Liga auf Konfliktfreiheit.
     * Wenn Konflikte entstehen, korrigiere sie, falls m�glich.
     *
     * @param liga - die Liga der zu pr�fenden Gruppen
     * autoSort - false = es wird nicht korrigiert, nur geprueft
     * @return gibt auskunft dar�ber, was passiert ist.
     */
    CheckResult checkGruppen(Liga liga);
    
    GruppenSortierer createSortierer(Liga liga);

    /**
     * Loesche Gruppe und alle darin gespeicherten Spiele.
     * @param each
     * @throws DartException
     */
    void deleteLigagruppe(Ligagruppe each) throws DartException;

    /**
     * ggf. zur Anzeige bei "Gruppe bearbeiten":
     *
     * Zu jeder Spielposition in der gruppe gibt den Status zurück (berechnet).
     * !! ACHTUNG: Es wird der GESPEICHERTE Zustand verwendet!!
     * @param gruppe
     * @return
     */
    PositionStatusInfo[] getStatus(Ligagruppe gruppe);

    /**
     * ggf. zum Drucken der Gruppen: 
     *
     * Den Status für alle Gruppen einer Liga berechnen.
     * !! ACHTUNG: Es wird der GESPEICHERTE Zustand verwendet!!
     *
     * @param liga
     * @return
     */
    OSetting getAufstellungsStatus(Liga liga);

    /**
     * Zur vorigen Gruppe "blättern"
     * @return null (wenn es keine vorige gibt) oder eine vorige Gruppe in der gleichen Liga,
     * die Ligaklase ist ggf. die vorige
     */
    Ligagruppe findPreviousGroup(Ligagruppe gruppe);

    /**
     * Zur nächsten Gruppe "blättern"
     * @return null (wenn es keine nächste gibt) oder eine vorige Gruppe in der gleichen Liga,
     * die Ligaklasse ist ggf. die nächste
     */
    Ligagruppe findNextGroup(Ligagruppe gruppe);
}
