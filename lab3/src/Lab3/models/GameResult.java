package Lab3.models;
import com.google.gson.*;

public class GameResult {
    private String message;
    private String realNumber;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getRealNumber() {
        return realNumber;
    }
    public void setRealNumber(String realNumber) {
        this.realNumber = realNumber;
    }

    public static GameResult fromJSON(String jsonRes) {
        Gson gson = new Gson();
        return gson.fromJson(jsonRes, GameResult.class);
    }
    public static String toJSON(GameResult gameRes) {
        Gson gson = new Gson();
        return gson.toJson(gameRes);
    }
}
