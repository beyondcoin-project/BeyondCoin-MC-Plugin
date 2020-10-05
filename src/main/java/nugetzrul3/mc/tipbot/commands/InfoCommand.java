package nugetzrul3.mc.tipbot.commands;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import nugetzrul3.mc.tipbot.Main;
import nugetzrul3.mc.tipbot.config.Constants;
import nugetzrul3.mc.tipbot.config.Functions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InfoCommand implements CommandExecutor {
    private Main plugin;

    Constants constants = new Constants();
    Functions functions = new Functions();

    public InfoCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("info").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You're not a player m8");
            return true;
        }

        Player player = (Player) sender;



        String user = constants.user;
        String password = constants.pass;
        String host = constants.host;
        String port = constants.port;

        try {
            URL url_daemon = new URL("http://" + user + ":" + password + "@" + host + ":" + port + "/");
            BitcoinJSONRPCClient client = new BitcoinJSONRPCClient(url_daemon);
            List<String> prices = getPrice();
            String usd = prices.get(0);
            String btc = prices.get(1);
            player.sendMessage(ChatColor.BLUE + "-- " + constants.coinName + "Network Information --");
            player.sendMessage(ChatColor.GREEN + "Current Block Height: " + ChatColor.WHITE + client.getBlockCount() + "\n" +
                    ChatColor.GREEN + "Current Network hashrate: " + ChatColor.WHITE + functions.getHashFormat(client.getNetworkHashPs().floatValue()) + "\n" +
                    ChatColor.GREEN + "Current Network Difficulty: " + ChatColor.WHITE + client.getDifficulty() + "\n" +
                    ChatColor.GREEN + constants.ticker + "/BTC: " + ChatColor.WHITE + btc + " BTC" + "\n" +
                    ChatColor.GREEN + constants.ticker + "/USD: " + ChatColor.WHITE + "$" + usd + "\n" +
                    ChatColor.GREEN + "Client Version: " + ChatColor.WHITE + client.getNetworkInfo().subversion()
            );
        } catch (MalformedURLException | BitcoinRPCException e) {
            player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "There was an error connecting to the " + constants.coinName + " daemon. Please notify the admins");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private List<String> getPrice() throws IOException, ParseException {

        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();
        HttpURLConnection connection;

        try {
            URL url = new URL(constants.price_url);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        JSONObject obj1 = (JSONObject) parser.parse(response.toString());
        JSONObject obj2 = (JSONObject) parser.parse(obj1.get("beyondcoin").toString());

        BigDecimal usdPrice = new BigDecimal(obj2.get("usd").toString());
        BigDecimal btcPrice = new BigDecimal(obj2.get("btc").toString());

        usdPrice = usdPrice.setScale(8, RoundingMode.HALF_EVEN);
        btcPrice = btcPrice.setScale(8, RoundingMode.HALF_EVEN);

        ArrayList<String> list = new ArrayList<>();

        list.add(usdPrice.toPlainString());
        list.add(btcPrice.toPlainString());


        return list;
    }
}
