import Lab3.crackers.LcgCracker;
import Lab3.models.Account;
import Lab3.models.GameResult;

public class Start {
    public static void main(String[] args) {
        int[] LcgCracked = crackLCG();
        System.out.println(String.format("LCG cracker results:\na = %d\nc = %d", LcgCracked[0], LcgCracked[1]));

        //LCG cracker results:
        //a = 1664525
        //c = 1013904223
        playLCG(LcgCracked);
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
        System.out.println(GameResult.toJSON(testRes));
        int seed = Integer.parseInt(testRes.getRealNumber());

        do {
            int number = LcgCracker.getNext(seed, params);
            GameResult tempRes = CasinoConnection.play(playerAcc, playerAcc.getMoney(), number, "Lcg");
            System.out.println(GameResult.toJSON(tempRes));
            seed = number;
        } while (playerAcc.getMoney() < 1000000);

        System.out.println("Yaaay");
    }
}
