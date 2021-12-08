import Lab3.crackers.LcgCracker;
import Lab3.crackers.MT19937;
import Lab3.models.Account;
import Lab3.models.GameResult;

import java.util.Date;

public class Start {
    public static void main(String[] args) {
        int[] LcgCracked = crackLCG();
        System.out.println(String.format("LCG cracker results:\na = %d\nc = %d", LcgCracked[0], LcgCracked[1]));

        //LCG cracker results:
        //a = 1664525
        //c = 1013904223
        playLCG(LcgCracked);

        playMtCheating();
        //long mtSeed = crackMt();
        //System.out.println("Seed: " + mtSeed);
        //playMt(mtSeed);
    }

    private static int[] crackLCG() {
        System.out.println("\nStarted cracking LCG mode");
        Account crackerAcc = CasinoConnection.createAcc();
        GameResult[] crackLCGResults = new GameResult[3];
        for(int i = 0 ; i < 3; i++) {
            GameResult tempRes = CasinoConnection.play(crackerAcc, 1, i, "Lcg");
            crackLCGResults[i] = tempRes;
        }
        return LcgCracker.Start(crackLCGResults);
    }
    private static void playLCG(int[] params) {
        System.out.println("\nStarted playing in LCG mode");
        Account playerAcc = CasinoConnection.createAcc();
        GameResult testRes = CasinoConnection.play(playerAcc, 1, 0, "Lcg");
        int seed = Integer.parseInt(testRes.getRealNumber());

        do {
            int number = LcgCracker.getNext(seed, params);
            GameResult tempRes = CasinoConnection.play(playerAcc, playerAcc.getMoney(), number, "Lcg");
            seed = number;
        } while (playerAcc.getMoney() <= 1000000);

        System.out.println("Yaaay");
    }
    //1 itr = 1M money
    private static void playMtCheating() {
        System.out.println("\nStarted playing in MT mode with cheating");
        Account crackerAcc = CasinoConnection.createAcc();
        Account playerAcc = CasinoConnection.createAcc();

        while(playerAcc.getMoney() <= 1000000) {
            GameResult crackerRes = CasinoConnection.play(crackerAcc, 1, 0, "Mt");
            long mtSeed = Integer.toUnsignedLong((int)Long.parseLong(crackerRes.getRealNumber()));
            System.out.println("Seed: " + mtSeed);
            GameResult playerRes = CasinoConnection.play(playerAcc, 1000, mtSeed, "Mt");
        }

        System.out.println("Yaaay");
    }

    /*private static long crackMt() {
        Account crackerAcc = CasinoConnection.createAcc();
        GameResult tempRes = CasinoConnection.play(crackerAcc, 1, 0, "Mt");
        return Integer.toUnsignedLong((int)Long.parseLong(tempRes.getRealNumber()));
    }
    private static void playMt(long startSeed) {
        System.out.println("\nStarted playing in MT mode");
        Account playerAcc = CasinoConnection.createAcc();
        GameResult firstRes = CasinoConnection.play(playerAcc, 1, startSeed, "Mt");

        MT19937 twister = new MT19937((int)startSeed);
        do {
            long number = twister.getNext();
            GameResult tempRes = CasinoConnection.play(playerAcc, 1, number, "Mt");
            System.out.println(GameResult.toJSON(tempRes));
        } while (playerAcc.getMoney() < 1000000);
        System.out.println("Yaaay");
    }*/
}
