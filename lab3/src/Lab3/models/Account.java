package Lab3.models;
import com.google.gson.*;

public class Account {
    private String id;
    private int money;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public static Account fromJSON(String jsonAcc) {
        Gson gson = new Gson();
        return gson.fromJson(jsonAcc, Account.class);
    }
    public static String toJSON(Account acc) {
        Gson gson = new Gson();
        return gson.toJson(acc);
    }
}
