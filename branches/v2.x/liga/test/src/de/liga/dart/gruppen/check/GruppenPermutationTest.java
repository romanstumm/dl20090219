package de.liga.dart.gruppen.check;

import de.liga.dart.gruppen.check.model.*;
import de.liga.util.PermutationGenerator;
import junit.framework.TestCase;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 12:58:10
 */
public class GruppenPermutationTest extends TestCase {

 /*   public void testOptimize() {
        OSetting initial = createAufstellung();
        GroupOptimizer opt = new GroupOptimizer(initial);
        System.out.println("Anfang: " + opt.getInitial().getBewertung().getWertung());
        opt.optimizePermutation(null);
        System.out.println("Ergebnis: " + opt.getOptimal().getBewertung().getWertung());
    }*/

    public static OSetting createAufstellung() {
        return createAufstellung(30, 10, 7);
    }

    public static OSetting createAufstellung(int numberOfPubs, int numberOfGroups, int numberDays) {
        OSetting initial = new OSetting();
        for (int i = 0; i < numberOfPubs; i++) {
            OPub pub = new OPub(i);
            initial.addPub(pub);
        }
        for (int i = 0; i < numberOfGroups; i++) {
            OGroup group = createGruppe((i + 1) * 100, numberDays, i);
            for (OPosition position : group.getPositions()) {
                OPub pub = initial.getPub(((i + 1) *
                        position.getPosition()) % initial.getPubs().size());
                OTeam team = (OTeam) position;
                team.setPub(pub);
                pub.getTeams().add(team);
            }
            initial.getGroups().add(group);
        }
        return initial;
    }

    public void testPermutation()  {

        PermutationGenerator gen = new PermutationGenerator(3);
        int z = 0;
        while(gen.hasMore()) {
            int[] each = gen.getNext();
            System.out.print(++z + ": ");
            print(each);
        }
    }

    private void print(int[] each) {
        for(int i : each ) {
        System.out.print(i + ",");
        }
        System.out.println();
    }


    public static OGroup createGruppe(int n, int numberDays, int groupIdx) {
        OGroup group = new OGroup(n, groupIdx);
        for (int i = 0; i < 8; i++) {
            OTeam platz = new OTeam(group, null, n + i + 10, (i % numberDays)+1, false);
            platz.setPosition(i + 1);
            group.getPositions().add(platz);
        }
        return group;
    }
}
