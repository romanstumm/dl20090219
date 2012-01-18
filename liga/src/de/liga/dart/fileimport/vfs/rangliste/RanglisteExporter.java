package de.liga.dart.fileimport.vfs.rangliste;

import com.thoughtworks.xstream.XStream;
import de.liga.dart.fileimport.DataExchanger;
import de.liga.dart.fileimport.vfs.DbfImportErgebnis;
import de.liga.dart.gruppen.check.ProgressIndicator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * Description:  Veranlaßt den Import der Daten aus vfs samt Rangberechnung,
 * sortiert die Teams nach Ligen und Rängen, und veranlaßt den
 * anschließenden Excel-Export für den Urkundendruck.<br/>
 * User: roman
 * Date: 03.05.2009, 16:52:47
 */
public class RanglisteExporter implements DataExchanger {
    private static final Log log = LogFactory.getLog(RanglisteExporter.class);
    private static final XStream xstream;
    private static VfsConfig vfsConfig;
    private final Configuration freemarker = new Configuration();
    private final File outFile;
    private ProgressIndicator indicator;

    static {
        xstream = new XStream();
        xstream.processAnnotations(VfsConfig.class);
    }

    public RanglisteExporter(File outFile) {
        this.outFile = outFile;
    }

    public boolean start() {
        return false;  // not supported
    }


    /**
     * @param liganame - Liga.liganame, nur für connect zum richtigen DBF-Verzeichnis von vfs
     * @throws java.io.IOException
     * @throws freemarker.template.TemplateException
     *
     */
    public boolean start(String liganame) {
        try {
            DbfImportErgebnis dbfImport = new DbfImportErgebnis(getVfsConfig().isComplete());
            dbfImport.setProgressIndicator(indicator);
            dbfImport.start(liganame);
            // RanglisteExporter: compute data zum seriendruck-export
            if (indicator != null) indicator.showProgress(90, "Sortiere " + liganame + " ...");
            List<VfsLiga> ligen = sortLigen(dbfImport);
            List<VfsTeam> rangListe = computeRaenge(dbfImport, ligen);
            ExcelExportErgebnis exporter = new ExcelExportErgebnis(outFile, rangListe);
            exporter.setProgressIndicator(indicator);
            exporter.start();
            return true;
        } catch (Exception e) {
            log.error("Fehler beim Export der Rangliste für " + liganame, e);
            if (indicator != null) indicator.showProgress(0, "");
            return false;
        }
    }

    public void setProgressIndicator(ProgressIndicator indicator) {
        this.indicator = indicator;
    }

    public ProgressIndicator getIndicator() {
        return indicator;
    }

    public static VfsConfig getVfsConfig() {
        if (vfsConfig == null) readVfsConfig();
        return vfsConfig;
    }

    private static void readVfsConfig() {
        vfsConfig = (VfsConfig) xstream
                .fromXML(RanglisteExporter.class.getClassLoader().getResourceAsStream(
                        "vfs-rang-config.xml"));
    }

    private List<VfsTeam> computeRaenge(DbfImportErgebnis dbfImport, List<VfsLiga> ligen) {
        List<VfsTeam> rangListe = new ArrayList(dbfImport.getTeams().size());
        for (VfsLiga liga : ligen) {
            rangListe.addAll(computeRaenge(dbfImport, liga));
        }
        return rangListe;
    }

    private List<VfsTeam> computeRaenge(DbfImportErgebnis dbfImport, VfsLiga liga) {
        List<VfsTeam> ligaTeams = filterLigaTeams(dbfImport.getTeams(), liga);
        Collections.sort(ligaTeams, new Comparator<VfsTeam>() {
            public int compare(VfsTeam t1, VfsTeam t2) {
                int diff = t1.getPunkte() - t2.getPunkte();
                if (diff != 0) {
                    return -diff; // negativ: absteigend sortieren
                } else { // gleiche punkte: spiele1 bewerten
                    diff = t1.getSpiele1() - t2.getSpiele1();
                    if (diff != 0) {
                        return -diff;
                    } else { // gleiche spiele1: saetze1 bewerten
                        diff = t1.getSaetze1() - t2.getSaetze1();
                        return -diff;
                    }
                }
            }
        });
        int rang = 1;
        for (VfsTeam team : ligaTeams) {
            team.setRang(rang);
            if (rang == 1) {
                team.setPlatz(null);
            } else {
                team.setPlatz(rang + ". Sieger");
                team.setLigameister(null);
            }
            rang++;
        }
        return ligaTeams;
    }

    private List<VfsTeam> filterLigaTeams(List<VfsTeam> rangListe, VfsLiga liga) {
        List<VfsTeam> result = new ArrayList(rangListe.size());
        for (VfsTeam each : rangListe) {
            if (each.getVfsLiga().equals(liga)) {
                result.add(each);
            }
        }
        return result;
    }

    private List<VfsLiga> sortLigen(DbfImportErgebnis dbfImport)
            throws IOException, TemplateException {
        Set<VfsLiga> ligen = new HashSet();
        for (VfsTeam team : dbfImport.getTeams()) {
            if (compile(team)) {
                ligen.add(team.getVfsLiga());
            }
        }
        List<VfsLiga> sortedLigen = new ArrayList(ligen);
        Collections.sort(sortedLigen, new Comparator<VfsLiga>() {
            public int compare(VfsLiga l1, VfsLiga l2) {
                if (l1.getKlasse().equals(l2.getKlasse())) {
                    // gleiche Klasse: sortiere nach Name
                    return l1.getName().compareTo(l2.getName());
                }
                // TODO nach Ligaklasse.rang sortieren! hard-coded Werte entfernen!
                // BZO zuerst
                else if (l1.getKlasse().equals("BZO")) return -1;
                else if (l2.getKlasse().equals("BZO")) return 1;
                    // BZ danach
                else if (l1.getKlasse().equals("BZ")) return -1;
                else if (l2.getKlasse().equals("BZ")) return 1;
                    // nach Name alphabetisch
                else {
                    return l1.getName().compareTo(l2.getName());
                }
            }
        });
        return sortedLigen;
    }

    /**
     * die variablen in der config durch freemarker ersetzen lassen.
     *
     * @param team - klasse, ligameister + liganame werden ersetzt
     * @return false to exclude the team
     * @throws IOException
     * @throws TemplateException
     */
    protected boolean compile(VfsTeam team) throws IOException, TemplateException {
        VfsConfigLiga configLiga = findConfigLiga(team.getPlainLigaName());
        if (configLiga == null)
            throw new IllegalArgumentException(
                    "Liga " + team.getPlainLigaName() + " nicht konfiguriert bei " + team);
        VfsConfigKlasse configKlasse = findConfigKlasse(configLiga, team.getVfsLiga().getKlasse());
        if (configKlasse == null) {
            log.warn("Lasse Team  " + team.getTeamname() + " aus, weil Klasse " +
                    team.getVfsLiga().getKlasse() + " nicht konfiguriert bei " + team);
            return false;
        } else {
            Map<String, Object> templateVars = new HashMap();
            setTemplateVars(templateVars, team);
            team.setKlasse(
                    processTemplate(freemarker, templateVars, configKlasse.getKlasse()));
            team.setLigameister(
                    processTemplate(freemarker, templateVars, configKlasse.getLigameister()));
            team.setLiganame(
                    processTemplate(freemarker, templateVars, configKlasse.getLiganame()));
            return true;
        }
    }

    private void setTemplateVars(Map<String, Object> templateVars, VfsTeam team) {
        templateVars.put("team", team);
    }

    private String processTemplate(Configuration freemarker, Map<String, Object> templateVars,
                                   String templateSource)
            throws IOException, TemplateException {
        Template template = new Template("temp", new StringReader(templateSource), freemarker);
        StringWriter out = new StringWriter();
        template.process(templateVars, out);
        return out.toString();
    }

    private VfsConfigKlasse findConfigKlasse(VfsConfigLiga configLiga, String vfsLigaKlasse) {
        if (vfsLigaKlasse == null) return null;  // not found
        for (VfsConfigKlasse each : configLiga.getConfigKlassen()) {
            if (vfsLigaKlasse.equalsIgnoreCase(each.getName())) return each;
        }
        return null;  // not found
    }

    private VfsConfigLiga findConfigLiga(String vfsLigaName) {
        for (VfsConfigLiga each : getVfsConfig().getConfigLigen()) {
            if (vfsLigaName.equalsIgnoreCase(each.getName())) return each;
            if (each.getAliases() != null) { // search by alias
                for (String alias : each.getAliases()) {
                    if (vfsLigaName.equalsIgnoreCase(alias)) return each;
                }
            }
        }
        return null;  // not found
    }

}
