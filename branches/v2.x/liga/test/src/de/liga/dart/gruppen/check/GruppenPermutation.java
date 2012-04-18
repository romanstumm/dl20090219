package de.liga.dart.gruppen.check;

import de.liga.dart.gruppen.check.model.OGroup;
import de.liga.dart.gruppen.check.model.OPosition;
import de.liga.util.PermutationGenerator;

/**
 * Description:   <br/>
 * User: roman
 * Date: 18.11.2007, 12:37:40
 */
public class GruppenPermutation {
    
    private PermutationGenerator permgen;
    private OPosition[] platzierungs;
    private final OGroup group;

    public GruppenPermutation(OGroup group) {
        this.group = group;
        init();
    }

    private void init() {
        platzierungs = group.sortedPositions();
        reset();
    }

    public boolean hasNext() {
        return permgen.hasMore();
    }

    public void reset() {
        permgen = new PermutationGenerator(8);
    }

    public void next() {
       int[] seq = permgen.getNext();
       int i=0;
       for(OPosition position : platzierungs) {
           position.moveTo(seq[i++]+1);
       }
    }

    public OGroup getGruppe() {
        return group;
    }
}
