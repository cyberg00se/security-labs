import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SalsaCracker {
    private static List<byte[]> ciphertext;

    public SalsaCracker(List<byte[]> input) {
        ciphertext = input;
    }

    public void crackByPart(byte[] part) {
        List<String> plain = new ArrayList<>();
        for (int i=0; i<ciphertext.size(); i++) {
            float maxDecrypted = 0;
            String plaintext = "";
            for (int j=0; j<ciphertext.size(); j++) {
                if(i != j) {
                    byte[] res = XORpart(XORpart(ciphertext.get(i), ciphertext.get(j)), part);
                    String resStr = new String(res, StandardCharsets.US_ASCII);
                    //System.out.println(resStr);
                    if(isDecrypted(resStr) > maxDecrypted) {
                        maxDecrypted = isDecrypted(resStr);
                        plaintext = resStr;
                    }
                }
            }
            plain.add(plaintext);
        }
        for (String row: plain) {
            System.out.println(row);
        }
    }

    private byte[] XORpart(byte[] text, byte[] part) {
        byte[] res = new byte[text.length];
        for (int i=0; i<text.length; i++) {
            res[i] = (byte) (text[i]^part[i%part.length]);
        }
        return res;
    }

    private static float isDecrypted(String text) {
        int count = 0;
        Pattern possibleChars = Pattern.compile("[a-z0-9 ,]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = possibleChars.matcher(text);
        while(matcher.find())
            count++;
        //System.out.println((float)count/text.length());
        return (float)count/text.length();
    }
}
