package Lab3.crackers;

public class MT19937 {
    private final static int n = 624;
    private final static int w = 32;
    private final static int r = 31;
    private final static int m = 397;
    private final static int a = 0x9908B0DF;
    private final static int u = 11;
    private final static int s = 7;
    private final static int t = 15;
    private final static int l = 18;
    private final static int b = 0x9D2C5680;
    private final static int c = 0xEFC60000;
    private final static long f = 1812433253L;
    private final static int d = 0xFFFFFFFF;
    private final static int lower_mask = 0x7FFFFFFF;
    private final static int upper_mask = 0x80000000;

    private int[] MT;
    private int index = n+1;

    public MT19937(int[] numbers) {
        MT = numbers;
        index = n;
    }
    public MT19937(int seed) {
        MT = new int[n];
        long longSeed = seed;
        MT[0] = (int) longSeed;
        for(index = 1; index<n; ++index) {
            longSeed = (f * (longSeed^(longSeed>>(w-2))) + index) & 0xFFFFFFFFL;
            MT[index] = (int) longSeed;
        }
    }

    public int getNext() {
        if (index >= n) {
            if (index > n) {
                System.out.println("MT error!");
            }
            twist();
            index = 0;
        }
        int result = temper(MT[index++]);
        return result;
    }

    private void twist() {
        for(int i=0; i<n-1; i++) {
            int x = (MT[i]&upper_mask) | (MT[i+1]&lower_mask);
            int xA = x>>>1;
            if (x % 2 != 0)  {
                xA = xA^a;
            }
            if(i < n-m)
                MT[i] = MT[i+m]^xA;
            else
                MT[i] = MT[i+(m-n)]^xA;
        }
        int x = MT[n-1]&upper_mask | MT[0]&lower_mask;
        int xA = x>>>1;
        if (x % 2 != 0)  {
            xA = xA^a;
        }
        MT[n-1] = MT[m-1]^xA;
    }

    private static int temper(int input) {
        int y = input;
        y = y^(y >>> u);
        y = y^((y << s)&b);
        y = y^((y << t)&c);
        y = y^(y >>> l);
        return y;
    }

    public static int[] untemperNumbers(int[] tempered) {
        int[] untempered = new int[n];
        for(int i=0; i<n; i++) {
            untempered[i] = untemper(tempered[i]);
        }
        return untempered;
    }
    private static int untemper(int input) {
        int y = input;
        y = unShiftRight(y, l);
        y = unShiftLeft(y, t, c);
        y = unShiftLeft(y, s, b);
        y = unShiftRight(y, u);
        return y;
    }
    private static int unShiftLeft(int input, int shift, int mask) {
        int result = input;
        for (int i=0; i<32; i++) {
            result = input^((result << shift) & mask);
        }
        return result;
    }
    private static int unShiftRight(int input, int shift) {
        int result = input;
        for (int i=0; i<32; i++) {
            result = input^(result >>> shift);
        }
        return result;
    }
}