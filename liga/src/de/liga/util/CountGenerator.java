package de.liga.util;

/**
 * Description:   <br/>
 * User: roman
 * Date: 24.11.2007, 21:37:10
 */
public class CountGenerator {
    private long total;
    private long num;
    private final int[] max;

    private int[] next;

    /** @param radix <= 10 */
    public CountGenerator(int length, int radix) {
        this.max = new int[length];
        total = 1;
        for (int i = 0; i < length; i++) {
            total *= radix;
            max[i] = radix-1;
        }
        next = new int[length];
    }

    public CountGenerator(int[] radixes) {
        this.max = radixes;
        total = 1;
        for (int i = 0; i < radixes.length; i++) {
            total *= radixes[i];
            max[i] = radixes[i]-1;
        }
        next = new int[radixes.length];
    }

    public boolean hasMore() {
        return num < total;
    }

    public int[] getNext() {
        if (num++ > 0) {
            for (int i = next.length - 1; i >= 0; i--) {
                if (next[i] < max[i]) {
                    next[i]++;
                    break;
                } else {
                    next[i] = 0;
                }
            }
        }
        return next;
    }

    public long getTotal() {
        return total;
    }

    public void reset() {
        num = 0;
        next = new int[next.length];
    }
}
