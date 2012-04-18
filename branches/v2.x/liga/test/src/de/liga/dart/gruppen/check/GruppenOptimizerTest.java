package de.liga.dart.gruppen.check;

import de.liga.util.CombinationGenerator;
import de.liga.util.CountGenerator;
import junit.framework.TestCase;

/**
 * Description:   <br/>
 * User: roman
 * Date: 24.11.2007, 14:53:09
 */
public class GruppenOptimizerTest extends TestCase {
    GroupOptimizer opt;

    static {
        Options.sortOptional = false; // sonst kommt dieser Test nicht zurecht!!
    }

    public GruppenOptimizerTest(String string) {
        super(string);
    }
/*
    public void testComputeParties() {
        OSetting setting = GruppenPermutationTest.createAufstellung();
        opt = new GroupOptimizer(setting);
        List<OParties> pots = opt.computeParties(opt.getCurrent());
        assertTrue(!pots.isEmpty());
    }*/

    /*
    public void testComputeCombinationCount() {
        opt = new GroupOptimizer(
                GruppenPermutationTest.createAufstellung(20, 15));
        List<OParties> parties = opt.computeParties(opt.getCurrent());
        List<OTeam> teams =
                OParties.getConflictTeams(parties, new OHotSpot(0, 0));
        assertEquals(3, teams.size());
        GroupOptimizer.computeCombinationCount(teams);

        teams = OParties.getConflictTeams(parties, new OHotSpot(5, 4));
        assertEquals(4, teams.size());
        GroupOptimizer.computeCombinationCount(teams);
    }      */

    public void testCombinations() {
        String[] elements = {"1", "2", "3", "4", "5", "6", "7", "8"};
        int[] indices;
        CombinationGenerator x = new CombinationGenerator(elements.length, 2);
        StringBuffer combination;
        while (x.hasMore()) {
            combination = new StringBuffer();
            indices = x.getNext();
            for (int indice : indices) {
                combination.append(elements[indice]).append(", ");
            }
            System.out.println(combination.toString());
        }
    }

    public void testCountGenerator_2() {
        int[] radixes = {3, 4, 1};
        CountGenerator gen = new CountGenerator(radixes);
        while(gen.hasMore()) {
            print(gen.getNext());
        }
    }


    public void testCountGenerator_1() {
        CountGenerator gen = new CountGenerator(2,4);
        while(gen.hasMore()) {
            print(gen.getNext());
        }
    }

    private void print(int[] next) {
        for(int i : next) {
            System.out.print(i);
            System.out.print(", ");
        }
        System.out.println();
    }

    /**
     * Loesbar nach 12 Schritten
     */
    public void testOptimize_2() {
        opt = new GroupOptimizer(GruppenPermutationTest.createAufstellung());
        opt.optimize();
    }

    /**
     * gelöst nach 25 schritten
     */
    public void testOptimize_10_10_7() {
        opt = new GroupOptimizer(GruppenPermutationTest.createAufstellung(10, 10, 7));
        opt.optimize();
    }

    /**
     * gelöst nach 9 schritten
     */
    public void testOptimize_3_2_2() {
        opt = new GroupOptimizer(GruppenPermutationTest.createAufstellung(3, 2, 2));
        opt.optimize();
    }

    /**
     * gelöst nach 8 schritten
     */
    public void testOptimize_3_3_3() {
        opt = new GroupOptimizer(GruppenPermutationTest.createAufstellung(3, 3, 3));
        opt.optimize();
    }

    /**
     * gelöst nach 17 schritten
     */
    public void testOptimize_2_3_7() {
        opt = new GroupOptimizer(GruppenPermutationTest.createAufstellung(2, 3, 7));
        opt.optimize();
    }

    /**
     * unlösbar? rest konflikte: 
     * B 1/0 [!: P1=[412 in K2 at T1] + P7=[417 in K2 at T1]
     *          +P7=[417 in K2 at T1] + P1=[412 in K2 at T1]]
     */
    public void testOptimize_10_10_1() {
        opt = new GroupOptimizer(GruppenPermutationTest.createAufstellung(10, 10, 1));
        opt.optimize();
    }

}
