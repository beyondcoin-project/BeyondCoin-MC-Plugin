package nugetzrul3.mc.tipbot.config;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Functions {

    public String getUserId(String username) throws Exception { ;
        String path = "usercache.json";
        String userid = null;

        String json = readFileAsString(path);

        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(json);


        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonobj = (JSONObject) parser.parse(array.get(i).toString());
                String cache_username = jsonobj.get("name").toString();

                if (cache_username.equalsIgnoreCase(username)) {
                    userid = jsonobj.get("uuid").toString();
                }
            }
        }

        return userid;
    }

    public boolean isFloat(String amount) {
        try {
            Float.parseFloat(amount);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    private static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
