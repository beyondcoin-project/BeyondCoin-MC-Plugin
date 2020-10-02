package nugetzrul3.mc.tipbot.config;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;

public class Functions {

    public String getUserId(String username) throws IOException, JSONException {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + username;
        URL request = new URL(url);

        JSONTokener token = new JSONTokener(request.openStream());
        JSONObject obj = new JSONObject(token);

        return obj.get("id").toString();
    }

    public boolean isFloat(String amount) {
        try {
            Float.parseFloat(amount);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
}
