package de.liga.util;

public final class TinyPermutationGenerator {

    private int[] a;
    private int numLeft;
    private int total;

    //-----------------------------------------------------------
    // Constructor. WARNING: Don't make n too large.
    // Recall that the number of permutations is n!
    //----------------------------------------------------------
    public TinyPermutationGenerator(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Min 1");
        }
        if (n > 12) {
            throw new IllegalArgumentException("Max 12");
        }
        a = new int[n];
        total = getFactorial(n);
        reset();
    }

    //------
    // Reset
    //------

    public void reset() {
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        numLeft = total;
    }

    //------------------------------------------------
    // Return number of permutations not yet generated
    //------------------------------------------------

    public int getNumLeft() {
        return numLeft;
    }

    //------------------------------------
    // Return total number of permutations
    //------------------------------------

    public int getTotal() {
        return total;
    }

    //-----------------------------
    // Are there more permutations?
    //-----------------------------

    public boolean hasMore() {
        return numLeft > 0;
    }

    //------------------
    // Compute factorial
    //------------------

    private static int getFactorial(int n) {
        int fact = 1;
        for (int i = n; i > 1; i--) {
            fact = fact * i;
        }
        return fact;
    }

    //--------------------------------------------------------
    // Generate next permutation (algorithm from Rosen p. 284)
    //--------------------------------------------------------

    public int[] getNext() {

        if (numLeft == total) {
            numLeft--;
            return a;
        }

        int temp;

        // Find largest index j with a[j] < a[j+1]

        int j = a.length - 2;
        while (a[j] > a[j + 1]) {
            j--;
        }

        // Find index k such that a[k] is smallest integer
        // greater than a[j] to the right of a[j]

        int k = a.length - 1;
        while (a[j] > a[k]) {
            k--;
        }

        // Interchange a[j] and a[k]

        temp = a[k];
        a[k] = a[j];
        a[j] = temp;

        // Put tail end of permutation after jth position in increasing order

        int r = a.length - 1;
        int s = j + 1;

        while (r > s) {
            temp = a[s];
            a[s] = a[r];
            a[r] = temp;
            r--;
            s++;
        }

        numLeft--;
        return a;

    }

}
