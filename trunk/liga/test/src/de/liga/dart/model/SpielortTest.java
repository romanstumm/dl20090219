package de.liga.dart.model;

import junit.framework.TestCase;

/**
 * viaboxx GmbH, (c) 2010
 * User: roman.stumm
 * Date: 17.04.2011
 * Time: 19:24:39
 */
public class SpielortTest extends TestCase {
    public void testGetLigagruppenLabel() {
        Spielort ort = new Spielort();
        String text = ort.getLigagruppenLabel(null);
        assertEquals("", text);

        Ligagruppe gruppeA1 = new Ligagruppe();
        gruppeA1.setGruppenNr(1);
        Ligaklasse klasseA = new Ligaklasse();
        klasseA.setKlassenName("A");
        gruppeA1.setLigaklasse(klasseA);

        Ligagruppe gruppeB1 = new Ligagruppe();
        gruppeB1.setGruppenNr(1);
        Ligaklasse klasseB = new Ligaklasse();
        klasseB.setKlassenName("B");
        gruppeB1.setLigaklasse(klasseB);

        Ligagruppe gruppeB2 = new Ligagruppe();
        gruppeB2.setGruppenNr(2);
        gruppeB2.setLigaklasse(klasseB);

        Ligateam team = new Ligateam();
        ort.getLigateams().add(team);
        Ligateamspiel spiel = new Ligateamspiel();
        team.setLigateamspiel(spiel);
        spiel.setLigagruppe(gruppeA1);

        team = new Ligateam();
        ort.getLigateams().add(team);
        spiel = new Ligateamspiel();
        team.setLigateamspiel(spiel);
        spiel.setLigagruppe(gruppeA1);

        team = new Ligateam();
        ort.getLigateams().add(team);
        spiel = new Ligateamspiel();
        team.setLigateamspiel(spiel);
        spiel.setLigagruppe(gruppeA1);

        team = new Ligateam();
        ort.getLigateams().add(team);
        spiel = new Ligateamspiel();
        team.setLigateamspiel(spiel);
        spiel.setLigagruppe(gruppeB1);

        team = new Ligateam();
        ort.getLigateams().add(team);
        spiel = new Ligateamspiel();
        team.setLigateamspiel(spiel);
        spiel.setLigagruppe(gruppeB2);

        team = new Ligateam();
        ort.getLigateams().add(team);
        spiel = new Ligateamspiel();
        team.setLigateamspiel(spiel);
        spiel.setLigagruppe(gruppeB1);

        text = ort.getLigagruppenLabel(null);
        assertEquals("3A1 2B1 B2", text);


    }
}
