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


    public String getHashFormat(float hashes) {
        if (hashes < 1e3) {
            return hashes + " H/s";
        }
        else if (hashes > 1e3 && hashes < 1e6) {
            return hashes / 1e3 + " KH/s";
        }
        else if (hashes > 1e6 && hashes < 1e9) {
            return hashes / 1e6 + " MH/s";
        }
        else if (hashes > 1e9 && hashes < 1e12) {
            return hashes / 1e9 + " GH/s";
        }
        else if (hashes > 1e12 && hashes < 1e15) {
            return hashes / 1e12 + " TH/s";
        }
        else if (hashes > 1e15 && hashes < 1e18) {
            return hashes / 1e15 + " PH/s";
        }
        else if (hashes > 1e18 && hashes < 1e21) {
            return hashes / 1e18 + " EH/s";
        }
        else {
            return null;
        }
    }
}
