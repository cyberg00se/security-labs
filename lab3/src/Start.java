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

        //playMtCheating();
        playMt();

        playBetterMt();
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

    private static MT19937 crackMt(Account crackerAcc) {
        System.out.println("\nStarted cracking MT mode");
        GameResult tempRes = CasinoConnection.play(crackerAcc, 1, 0, "Mt");
        long currentNumber = Integer.toUnsignedLong((int)Long.parseLong(tempRes.getRealNumber()));
        System.out.println("Current number: " + currentNumber);

        long seed = System.currentTimeMillis()/1000 - 5;
        int i=0;
        MT19937 twister = new MT19937((int)seed);
        while (Integer.toUnsignedLong(twister.getNext()) != currentNumber && i < 10) {
            twister = new MT19937((int)++seed);
            i++;
        }
        System.out.println("Current seed: " + seed);
        return twister;
    }
    private static void playMt() {
        Account playerAcc = CasinoConnection.createAcc();
        MT19937 twister = crackMt(playerAcc);

        System.out.println("\nStarted playing in MT mode");
        do {
            long number = Integer.toUnsignedLong(twister.getNext());
            GameResult tempRes = CasinoConnection.play(playerAcc, playerAcc.getMoney(), number, "Mt");
        } while (playerAcc.getMoney() < 1000000);
        System.out.println("Yaaay");
    }
    private static MT19937 crackBetterMt(Account crackerAcc) {
        System.out.println("\nStarted cracking BetterMT mode");
        int[] numbersMT = new int[624];
        for (int i=0; i< numbersMT.length; i++) {
            GameResult tempRes = CasinoConnection.play(crackerAcc, 1, 0, "BetterMt");
            numbersMT[i] = (int)(Long.parseLong(tempRes.getRealNumber()) & 0xFFFFFFFFL);
        }

        int[] untemperedNumbersMT = MT19937.untemperNumbers(numbersMT);
        MT19937 twister = new MT19937(untemperedNumbersMT);
        return twister;
    }
    private static void playBetterMt() {
        Account playerAcc = CasinoConnection.createAcc();
        MT19937 twister = crackBetterMt(playerAcc);

        System.out.println("\nStarted playing in BetterMT mode");
        do {
            long number = Integer.toUnsignedLong(twister.getNext());
            GameResult tempRes = CasinoConnection.play(playerAcc, playerAcc.getMoney(), number, "BetterMt");
        } while (playerAcc.getMoney() < 1000000);
        System.out.println("Yaaay");
    }
}
