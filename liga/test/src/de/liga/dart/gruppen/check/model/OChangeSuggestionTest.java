package de.liga.dart.gruppen.check.model;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 21.12.2007, 10:27:54
 */
public class OChangeSuggestionTest extends TestCase {
    public void testSort() {
        OChangeOption diff = new OChangeOption(1, 2);
        OChangeOption same = new OChangeOption(1, 1);
        List<OChangeSuggestion> sugs = new ArrayList<OChangeSuggestion>();
        OChangeSuggestion s1, s2, s3;
        OChangeSuggestion s = new OChangeSuggestion(diff, diff, null);
        s1 = s;
        sugs.add(s);
        s = new OChangeSuggestion(diff, same, null);
        s2 = s;
        sugs.add(s);
        s = new OChangeSuggestion(same, diff, null);
        s3 = s;
        sugs.add(s);

        Collections.sort(sugs);
        assertEquals(s3, sugs.get(0));
        assertEquals(s2, sugs.get(1));
        assertEquals(s1, sugs.get(2));
    }
}
