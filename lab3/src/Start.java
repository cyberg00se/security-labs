import Lab3.models.Account;
import Lab3.models.GameResult;

import java.math.BigInteger;

public class Start {
    public static void main(String[] args) {
        int[] LcgCracked = LcgCracker.Start();
        System.out.println(String.format("LCG cracker results:\na = %d\nc = %d", LcgCracked[0], LcgCracked[1]));

        //LCG cracker results:
        //a = 1664525
        //c = 1013904223
        playLCG(LcgCracked);
    }

    private static void playLCG(int[] params) {
        System.out.println("\nStarted playing in LCG mode");
        Account playerAcc = CasinoConnection.createAcc();
        GameResult testRes = CasinoConnection.playLCG(playerAcc, 1, 0);
        System.out.println(GameResult.toJSON(testRes));
        int seed = Integer.parseInt(testRes.getRealNumber());

        while (playerAcc.getMoney() < 100000) {
            int number = LcgCracker.getNext(seed, params);
            GameResult tempRes = CasinoConnection.playLCG(playerAcc, playerAcc.getMoney(), number);
            System.out.println(GameResult.toJSON(tempRes));
            seed = number;
        }

        System.out.println("Yaaay");
    }
}
