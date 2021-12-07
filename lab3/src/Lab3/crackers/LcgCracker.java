package Lab3.crackers;

import Lab3.models.Account;
import Lab3.models.GameResult;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class LcgCracker {
    private static List<BigInteger> numbers = new LinkedList<>();

    public static int[] Start(GameResult[] gameResults) {
        for(int i = 0 ; i < 3; i++) {
            GameResult tempRes = gameResults[i];
            System.out.println(GameResult.toJSON(tempRes));
            BigInteger tempNumber = new BigInteger(tempRes.getRealNumber());
            if(tempNumber.signum() == -1) {
                tempNumber = tempNumber.add(BigInteger.ONE.shiftLeft(32));
            }
            System.out.println(tempNumber);
            numbers.add(tempNumber);
        }
        BigInteger m = BigInteger.TWO.pow(32);

        BigInteger diff21 = numbers.get(2).subtract(numbers.get(1));
        BigInteger diff10 = numbers.get(1).subtract(numbers.get(0)).modInverse(m);

        BigInteger a = diff21.multiply(diff10).mod(m);
        BigInteger c = numbers.get(1).subtract(a.multiply(numbers.get(0)));

        int[] results = {a.intValue(), c.intValue()};
        return results;
    }

    public static int getNext(int last, int[] params) {
        BigInteger a = new BigInteger(String.valueOf(params[0]));
        BigInteger c = new BigInteger(String.valueOf(params[1]));
        BigInteger m = BigInteger.TWO.pow(32);

        int next = (new BigInteger(String.valueOf(last)).multiply(a).add(c).mod(m)).intValue();
        return next;
    }
}
