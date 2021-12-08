import Lab3.models.Account;
import Lab3.models.GameResult;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

public class CasinoConnection {
    private static String baseURL = "http://95.217.177.249/casino/";

    public static Account createAcc() {
        Random rnd = new Random();
        String id = String.valueOf(rnd.nextInt(1000));
        String query = String.format("id=%s",
                URLEncoder.encode(id, StandardCharsets.UTF_8));
        String requestURL = baseURL + "createacc?";

        Account account = new Account();

        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                            URI.create(requestURL + query))
                    .GET()
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            while(response.body().contains("error")) {
                id = String.valueOf(rnd.nextInt(1000));
                query = String.format("id=%s",
                        URLEncoder.encode(id, StandardCharsets.UTF_8));
                request = HttpRequest.newBuilder(
                                URI.create(requestURL + query))
                        .GET()
                        .build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }
            account = Account.fromJSON(response.body());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public static GameResult play(Account acc, int bet, long number, String mode) {
        String query = String.format("id=%s&bet=%s&number=%s",
                URLEncoder.encode(acc.getId(), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(bet), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(number), StandardCharsets.UTF_8));
        String requestURL = baseURL + "play" + mode + "?" + query;

       GameResult gameResult = new GameResult();

        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                            URI.create(requestURL))
                    .GET()
                    .build();
            //System.out.println(request.uri());
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            gameResult = GameResult.fromJSON(response.body());
            acc.setMoney(gameResult.getAccount().getMoney());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return gameResult;
    }
}
