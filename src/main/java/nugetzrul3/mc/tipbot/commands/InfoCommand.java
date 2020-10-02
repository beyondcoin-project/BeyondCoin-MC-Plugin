package nugetzrul3.mc.tipbot.commands;

import nugetzrul3.mc.tipbot.Main;
import nugetzrul3.mc.tipbot.config.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;

import java.net.MalformedURLException;
import java.net.URL;

public class InfoCommand implements CommandExecutor {
    private Main plugin;

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

        Constants constants = new Constants();

        String user = constants.user;
        String password = constants.pass;
        String host = constants.host;
        String port = constants.port;

        try {
            URL url = new URL("http://" + user + ":" + password + "@" + host + ":" + port + "/");
            BitcoinJSONRPCClient client = new BitcoinJSONRPCClient(url);
            player.sendMessage(ChatColor.BLUE + "-- " + constants.coinName + "Network Information --");
            player.sendMessage(ChatColor.GREEN + "Current Block Height: " + ChatColor.WHITE + client.getBlockCount() + "\n" +
                    ChatColor.GREEN + "Current Network hashrate: " + ChatColor.WHITE + getHashFormat(client.getNetworkHashPs().floatValue()) + "\n" +
                    ChatColor.GREEN + "Current Network Difficulty: " + ChatColor.WHITE + client.getDifficulty() + "\n" +
                    ChatColor.GREEN + "Client Version: " + ChatColor.WHITE + client.getNetworkInfo().subversion()
            );
        } catch (MalformedURLException | BitcoinRPCException e) {
            e.printStackTrace();
            player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "There was an error connecting to the " + constants.coinName + " daemon. Please notify the admins");
        }

        return false;
    }

    private String getHashFormat(float hashes) {
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
