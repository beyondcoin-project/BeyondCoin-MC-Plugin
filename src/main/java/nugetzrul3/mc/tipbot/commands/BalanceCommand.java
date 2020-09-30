package nugetzrul3.mc.tipbot.commands;

import net.md_5.bungee.api.ChatColor;
import nugetzrul3.mc.tipbot.Main;
import nugetzrul3.mc.tipbot.config.Constants;
import nugetzrul3.mc.tipbot.config.Functions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONException;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BalanceCommand implements CommandExecutor {
    private Main plugin;

    public BalanceCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("balance").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You're not a player m8");
            return true;
        }

        Player player = (Player) sender;

        Constants constants = new Constants();
        Functions functions = new Functions();

        String user = constants.user;
        String password = constants.pass;
        String host = constants.host;
        String port = constants.port;
        int conf = constants.conf;

        try {
            URL url = new URL("http://" + user + ":" + password + "@" + host + ":" + port + "/");
            BitcoinJSONRPCClient client = new BitcoinJSONRPCClient(url);
            float balance = client.getBalance(functions.getUserId(player.getDisplayName())).floatValue();
            float unconfirmed = client.getBalance(functions.getUserId(player.getDisplayName()), 0).floatValue() -
                    client.getBalance(functions.getUserId(player.getDisplayName()), conf).floatValue();

            player.sendMessage(ChatColor.GREEN + "Your current balance: " + ChatColor.WHITE + balance + " " + constants.ticker +"\n" +
                    ChatColor.GREEN + "Your unconfirmed balance: " + ChatColor.WHITE + unconfirmed + " " + constants.ticker);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
