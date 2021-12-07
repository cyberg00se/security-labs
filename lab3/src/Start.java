public class Start {
    public static void main(String[] args) {
        int[] LcgCracked = LcgCracker.Start();
        System.out.println(String.format("LCG cracker results:\na = %d\nc = %d", LcgCracked[0], LcgCracked[1]));
    }
}
