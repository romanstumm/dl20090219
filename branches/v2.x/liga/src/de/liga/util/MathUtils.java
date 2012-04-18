package de.liga.util;

import java.math.BigInteger;

/**
 * Description:   <br/>
 * User: roman
 * Date: 24.11.2007, 20:41:36
 */
public class MathUtils {

    //------------------
    // Compute factorial
    //------------------

    public static BigInteger getFactorial(int n) {
        BigInteger fact = BigInteger.ONE;
        for (int i = n; i > 1; i--) {
            fact = fact.multiply(new BigInteger(Integer.toString(i)));
        }
        return fact;
    }

    public static BigInteger getBinomialkoeffizient(int r, int n) {
        BigInteger nFact = getFactorial(n);
        BigInteger rFact = getFactorial(r);
        BigInteger nminusrFact = getFactorial(n - r);
        return nFact.divide(rFact.multiply(nminusrFact));
    }
}
