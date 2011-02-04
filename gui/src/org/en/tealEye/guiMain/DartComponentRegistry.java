package org.en.tealEye.guiMain;

import de.liga.dart.automatenaufsteller.service.AutomatenaufstellerService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.liga.service.LigaService;
import de.liga.dart.ligaklasse.model.LigaklasseFilter;
import de.liga.dart.ligaklasse.service.LigaklasseService;
import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.ligateam.model.WunschArt;
import de.liga.dart.ligateam.service.LigateamService;
import de.liga.dart.model.*;
import de.liga.dart.spielort.service.SpielortService;
import de.liga.util.Value;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.en.tealEye.framework.*;
import org.en.tealEye.guiMain.util.SelectionUtil;
import org.en.tealEye.guiMain.util.TimeHandler;
import org.en.tealEye.guiMain.util.WeekdayHandler;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateGroup;
import org.en.tealEye.guiPanels.applicationLogicPanels.CreateTeam;
import org.en.tealEye.guiPanels.applicationLogicPanels.ShowGroups;
import org.en.tealEye.guiPanels.applicationLogicPanels.ShowTeams;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DartComponentRegistry extends ComponentRegistry {
    protected static final Log log = LogFactory.getLog(DartComponentRegistry.class);
    static final DartComponentRegistry instance;

    public static ComponentRegistry getInstance() {
        return instance;
    }

    static {
        instance = new DartComponentRegistry();
        instance.setup();
        TimeHandler.register(Value.getDefault());
        WeekdayHandler.register(Value.getDefault());
    }

    private static final TableDef TABLE_LIGATEAM = new TableDef(new ColumnDef[]{
            new ColumnDef("teamName", "Team"),
            new ColumnDef("spielort.spielortName", "Stammspielort"),
            new ColumnDef("gruppeKlasse", "Klasse"), new ColumnDef("liga.ligaName", "Liga"),
            new ColumnDef("wochentag.$weekday", "Spieltag"),
            new ColumnDef("spielzeit.$time", "Spielzeit")});

    private static final TableDef TABLE_SPIELORT = new TableDef(new ColumnDef[]{
            new ColumnDef("spielortName", "Spielort"), new ColumnDef("liga.ligaName", "Liga"),
            new ColumnDef("automatenaufsteller.aufstellerName", "Aufsteller"),
            new ColumnDef("freierTag.$weekday", "Freier Tag"),
            new ColumnDef("kontaktName", "Kontakt"), new ColumnDef("ort", "Ort"),
            new ColumnDef("telefon", "Telefonnr"), new ColumnDef("mobil", "Mobilnr"),
            new ColumnDef("email", "Email"),});

    private static final TableDef TABLE_AUFSTELLER = new TableDef(new ColumnDef[]{
            new ColumnDef("aufstellerName", "Aufsteller"), new ColumnDef("liga.ligaName", "Liga"),
            new ColumnDef("kontaktName", "Kontakt"), new ColumnDef("ort", "Ort"),
            new ColumnDef("telefon", "Telefonnr"), new ColumnDef("mobil", "Mobilnr"),
            new ColumnDef("email", "Email"),});

    private static final TableDef TABLE_GRUPPEN = new TableDef(new ColumnDef[]{
            new ColumnDef("liga.ligaName", "Liga"), new ColumnDef("gruppenName", "Name")});

    protected void setup() {
        setupCombos();
        setupLists();
        setupTables();
    }

    /**
     * zur Erkl�rung:
     * a) Was muss man bei den ColumnDefs beachten?
     * Man gibt einfach deklarativ an, welche Attribute eines Objekts angezeigt werden sollen.
     * Dabei muss man die korrekte Schreibweise (gross/klein) beachten.
     * Manche Attribute stehen nicht direkt im Objekt, sondern in einem assozierten, z.B.
     * Aufsteller->Liga->ligaName
     * Dazu verwendet man die . Schreibweise: "liga.ligaName"
     * Das ganze ist null-safe, d.h. es funktioniert auch, wenn keine Liga am Aufsteller h�ngt.
     * <p/>
     * Manche Attribute haben einen Datentyp, der dem Anwender nicht direkt dargestellt
     * werden soll, z.B. Wochentage (int) und Date/Time. Um die Formattierung angeben zu
     * k�nnen, k�nnen Formattierungs-Handler angegeben werden. (Interface: ValueHandler)
     * Um diese anzusprechen, wird ein $ vor den registierten Handlernamen gestellt, z.B.
     * "freierTag.$weekday"
     * "spielzeit.$time"
     * Das k�nnte man z.B. kombinieren: "freierTag.$weekday.$trim.$uppercase",
     * wenn man sich f�r "trim" und "uppercase" auch noch Handler erstellt h�tte...
     * <p/>
     * b) Wann nutzt man TransactedModelFactory, wann ModelFactory?
     * Weil - wie bei a) erkl�rt, manche Attribute aus benachbarten Objekten kommen,
     * werden diese von Hibernate lazy nachgeladen. Das darf nur innerhalb einer Transaktion
     * erfolgen, die jedoch nach dem Serviceaufruf normalerweise schon zuende ist.
     * --> Wenn keine "geschachtelten" Attribute verwendet werden, kann man ModelFactory nutzen.
     * --> Dann kann (soll) auch der Aufruf von model.touch() entfallen!
     * <p/>
     * --> Wenn "geschachtelte" Attribute in der Tabelle vorkommen, verwendet man TransactedModelFactory
     * --> Der Aufruf von model.touch() ist dann (leider) notwendig, um jedes Attribut einmal "anzufassen",
     * wodurch Hibernate es innerhalb der Transaktion sofort l�dt.
     */
    private void setupTables() {
        tableModels.put("Table_Ligateam", new TransactedModelFactory() {
            public Object createTransacted(JPanel panel) {
                BeanTableModel model = new BeanTableModel(TABLE_LIGATEAM);
                Liga liga = SelectionUtil.getLiga(panel);
                ShowTeams showTeams = null;
                if (panel instanceof ShowTeams) {
                    showTeams = (ShowTeams) panel;
                }
                if (showTeams != null) {
                    if (!showTeams.getFullTextMode().isSelected()) {
                        boolean keineGruppe = showTeams.getKeineGruppe().isSelected();
                        Spielort ort = SelectionUtil.getSpielort(panel);
                        LigaklasseFilter klasse = SelectionUtil.getLigaklasseFilter(panel);

                        model.setObjects(ServiceFactory
                                .get(LigateamService.class).findTeamsByLigaKlasseOrt(liga, klasse,
                                ort, keineGruppe));
                    } else {
                        model.setObjects(ServiceFactory
                                .get(LigateamService.class).findTeamsLikeNameByLiga(
                                showTeams.getSuchenTextTF().getText(),
                                liga));
                    }
                    model.touch();
                }
                return model;
            }
        });

        tableModels.put("Table_Spielort", new TransactedModelFactory() {
            public Object createTransacted(JPanel panel) {
                BeanTableModel model = new BeanTableModel(TABLE_SPIELORT);
                Liga liga = SelectionUtil.getLiga(panel);
                Automatenaufsteller aufsteller = SelectionUtil.getAufsteller(panel);
                model.setObjects(ServiceFactory
                        .get(SpielortService.class).findAllSpielortByLigaAufsteller(liga,
                        aufsteller));
                model.touch();
                return model;
            }
        });

        tableModels.put("Table_SpielortProLiga", new TransactedModelFactory() {
            public Object createTransacted(JPanel panel) {
                BeanTableModel model = new BeanTableModel(TABLE_SPIELORT);
                Liga liga = SelectionUtil.getLiga(panel);
                Automatenaufsteller aufsteller = SelectionUtil.getAufsteller(panel);
//                System.out.println("[" + Thread.currentThread().getName() + "] " + "Table_SpielortProLiga " + liga + " Aufsteller: " + aufsteller);
                if (liga != null) {
                    model.setObjects(ServiceFactory
                            .get(SpielortService.class).findAllSpielortByLigaAufsteller(liga,
                            aufsteller));
                    model.touch();
                }
                return model;
            }
        }

        );

        tableModels
                .put("Table_Automatenaufsteller", new

                        TransactedModelFactory() {
                            public Object createTransacted(JPanel panel) {
                                BeanTableModel model = new BeanTableModel(TABLE_AUFSTELLER);
                                Liga liga = SelectionUtil.getLiga(panel);
                                if (liga == null) {
                                    model.setObjects(ServiceFactory
                                            .get(AutomatenaufstellerService.class).findAllAutomatenaufsteller());
                                } else {
                                    model.setObjects(ServiceFactory
                                            .get(AutomatenaufstellerService.class).findAllAutomatenaufstellerByLiga(
                                            liga));
                                }
                                model.touch();
                                return model;
                            }
                        }

                );

        tableModels.put("Table_Ligagruppe", new

                TransactedModelFactory() {
                    public Object createTransacted(JPanel panel) {
                        BeanTableModel model = new BeanTableModel(TABLE_GRUPPEN);
                        Liga liga = SelectionUtil.getLiga(panel);
                        Spielort ort = SelectionUtil.getSpielort(panel);
                        ShowGroups showGroups = null;
                        if (panel instanceof ShowGroups) {
                            showGroups = (ShowGroups) panel;
                        }

                        if (!showGroups.getFullTextMode().isSelected()) {
                            model.setObjects(ServiceFactory
                                    .get(GruppenService.class).findGruppenAndOrt(liga, ort));
                        } else {
                           model.setObjects(ServiceFactory
                                .get(GruppenService.class).findGruppenLikeName(
                                   liga, showGroups.getSuchenTextTF().getText()));
                        }
                        model.touch();
                        return model;
                    }
                }

        );
    }

    private void setupLists() {
        listModels.put("List_TeamWunschList", new ModelFactory() {

            public Object create(JPanel panel) {
                DefaultListModel model = new DefaultListModel();
                Ligateam team = (Ligateam) ((CreateTeam) panel).getModelEntity();
                Set<LigateamWunsch> wuensche = team.getWuensche();
                for (LigateamWunsch each : wuensche) {
                    TeamWunsch entry = new TeamWunsch(team, each);
                    // workaround to resolve otherteam and to make class ready to be displayed:                    
                    entry.toString();
                    model.addElement(entry);
                }
                return model;
            }
        });

        listModels.put("List_possibleTeamsForWunschList", new ModelFactory() {

            public Object create(JPanel panel) {

                DefaultListModel model = new DefaultListModel();
                CreateTeam myPanel = ((CreateTeam) panel);
                Ligateam team = (Ligateam) myPanel.getModelEntity();
                Liga filterLiga = (Liga) myPanel.getLiga().getSelectedItem();
                if (filterLiga != null) {
                    Object selection = myPanel.getWunschListLigaklasse().getSelectedItem();
                    Ligaklasse filterKlasse = null;
                    if (selection instanceof Ligaklasse) {
                        filterKlasse = (Ligaklasse) selection;
                    }
                    List<Ligateam> ligen = ServiceFactory.get(LigateamService.class)
                            .findWunschListCandidates(team, filterLiga, filterKlasse);
                    for (Ligateam each : ligen) {
                        model.addElement(each);
                    }
                }
                return model;
            }
        });

        listModels.put("List_LigateamsNochFrei", new ModelFactory() {

            public Object create(JPanel panel) {
                final Liga liga = SelectionUtil.getLiga(panel);
                final Ligaklasse klasse = SelectionUtil.getLigaklasse(panel);
                final DefaultListModel model = new DefaultListModel();
                if (liga != null && klasse != null) {
                    final GruppenService service = ServiceFactory.get(GruppenService.class);
                    final Set<Long> teamsInGroup = new HashSet();
                    final Long gruppeId;
                    if (panel instanceof CreateGroup) {
                        CreateGroup createGroup = (CreateGroup) panel;
                        gruppeId = ((Ligagruppe) createGroup.getModelEntity()).getGruppenId();
                        for (int platzNr = 0; platzNr < 8; platzNr++) {
                            Ligateam team = (Ligateam) createGroup.getTable()
                                    .getValueAt(platzNr, CreateGroup.C_TEAMNAME);
                            Object cbox = createGroup.getTable()
                                    .getValueAt(platzNr, CreateGroup.C_SPIELFREI);
                            boolean boo = (Boolean) cbox;
                            if (team != null && !boo) {
                                teamsInGroup.add(team.getLigateamId());
                            }
                        }
                    } else {
                        gruppeId = null;
                    }
                    ServiceFactory.runAsTransaction(new Runnable() {
                        public void run() {
                            List<Ligateam> teams =
                                    service.findUnassignedTeams(liga, klasse, teamsInGroup,
                                            gruppeId);
                            for (Ligateam each : teams) {
                                each.getSpielort().getSpielortName(); // resolve
                                model.addElement(each);
                            }
                        }
                    });
                }
                return model;
            }
        });
    }

    private void setupCombos() {
        ModelFactory modelFactory = new ModelFactory() {

            public Object create(JPanel panel) {
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                List<Liga> ligen = ServiceFactory.get(LigaService.class).findAllLiga();
                for (Liga each : ligen) {
                    model.addElement(each);
                }
                return model;
            }
        };
        comboModels.put("Combo_Liga", modelFactory);

        modelFactory = new ModelFactory() {

            public Object create(JPanel panel) {
//                log.debug("Combo_Liga >>>>>>>>>>> update in " + panel, new Exception());
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                List<Ligaklasse> klassen = ServiceFactory
                        .get(LigaklasseService.class).findAllLigaklasse();
                for (Ligaklasse each : klassen) {
                    model.addElement(each);
                }
                return model;
            }
        };
        comboModels.put("Combo_Ligaklasse", modelFactory);

        comboModels.put("Combo_LigaMitLeer", new ModelFactory() {

            public Object create(JPanel panel) {
//                log.debug("Combo_LigaMitLeer >>>>>>>>>>> update in " + panel);
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                List<Liga> ligen = ServiceFactory.get(LigaService.class).findAllLiga();
                model.addElement("-alle-");

                for (Liga each : ligen) {
                    model.addElement(each);
                }
                return model;
            }
        });
        comboModels.put("Combo_LigaMitKeine", new ModelFactory() {

            public Object create(JPanel panel) {
//                log.debug("Combo_LigaMitLeer >>>>>>>>>>> update in " + panel);
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                List<Liga> ligen = ServiceFactory.get(LigaService.class).findAllLiga();
                model.addElement("-keine-");
                for (Liga each : ligen) {
                    model.addElement(each);
                }
                return model;
            }
        });
        modelFactory = new ModelFactory() {

            public Object create(JPanel panel) {
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                List<Ligaklasse> klassen = ServiceFactory
                        .get(LigaklasseService.class).findAllLigaklasse();
                LigaklasseFilter filter = new LigaklasseFilter("-alle-");
                model.addElement(filter);
                Ligaklasse bzo = null, bez = null, a = null, b = null, c = null;
                for (Ligaklasse each : klassen) {
                    if (each.getKlassenName().equals("Bzo")) bzo = each;
                    else if (each.getKlassenName().equals("Bez")) bez = each;
                    else if (each.getKlassenName().equals("A")) a = each;
                    else if (each.getKlassenName().equals("B")) b = each;
                    else if (each.getKlassenName().equals("C")) c = each;
                    model.addElement(new LigaklasseFilter(each));
                }
                model.addElement(new LigaklasseFilter("A+B+C", a, b, c));
                model.addElement(new LigaklasseFilter("Bez+A", bez, a));
                model.addElement(new LigaklasseFilter("Bez+A+B", bez, a, b));
                model.addElement(new LigaklasseFilter("Bzo+Bez", bzo, bez));
                model.addElement(new LigaklasseFilter("Bzo+Bez+A", bzo, bez, a));
                model.addElement(new LigaklasseFilter("Bzo+Bez+A+B", bzo, bez, a, b));
                model.addElement(new LigaklasseFilter("B+C", b, c));
                return model;
            }
        };
        comboModels.put("Combo_LigaklasseFilterOptions", modelFactory);

        modelFactory = new ModelFactory() {

            public Object create(JPanel panel) {
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                List<Ligaklasse> klassen = ServiceFactory
                        .get(LigaklasseService.class).findAllLigaklasse();
                model.addElement("-alle-");
                for (Ligaklasse each : klassen) {
                    model.addElement(each);
                }
                return model;
            }
        };
        comboModels.put("Combo_WunschListLigaklasse", modelFactory);

        comboModels.put("Combo_AufstellerMitLeer", new ModelFactory() {

            public Object create(JPanel panel) {
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                List<Automatenaufsteller> list = ServiceFactory
                        .get(AutomatenaufstellerService.class)
                        .findAllAutomatenaufsteller();
                model.addElement("-alle-");
                for (Automatenaufsteller each : list) {
                    model.addElement(each);
                }
                return model;
            }
        });
        comboModels.put("Combo_AufstellerMitKeine", new ModelFactory() {

            public Object create(JPanel panel) {
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                List<Automatenaufsteller> list = ServiceFactory
                        .get(AutomatenaufstellerService.class)
                        .findAllAutomatenaufsteller();
                model.addElement("-keine-");
                for (Automatenaufsteller each : list) {
                    model.addElement(each);
                }
                return model;
            }
        });
        comboModels.put("Combo_SpielortMitLeer", new ModelFactory() {
            public Object create(JPanel panel) {
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                Liga liga = SelectionUtil.getLiga(panel);
                model.addElement("-alle-");
                if (liga != null) {
                    List<Spielort> list = ServiceFactory
                            .get(SpielortService.class)
                            .findAllSpielortByLiga(liga);
                    for (Spielort each : list) {
                        model.addElement(each);
                    }
                }
                return model;
            }
        });
        comboModels.put("Combo_WunschArten", new ModelFactory() {
            public Object create(JPanel panel) {
                return new DefaultComboBoxModel(new String[]{
                        TeamWunsch.name(WunschArt.WHITELIST_MUST),
                        TeamWunsch.name(WunschArt.WHITELIST_SHALL),
                        TeamWunsch.name(WunschArt.BLACKLIST_MUST),
                        TeamWunsch.name(WunschArt.BLACKLIST_SHALL)});
            }
        });
    }
}
