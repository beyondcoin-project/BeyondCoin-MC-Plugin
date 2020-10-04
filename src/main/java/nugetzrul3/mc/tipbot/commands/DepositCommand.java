package nugetzrul3.mc.tipbot.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nugetzrul3.mc.tipbot.Main;
import nugetzrul3.mc.tipbot.config.Constants;
import nugetzrul3.mc.tipbot.config.Functions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import org.json.*;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;

import java.io.IOException;
import java.net.URL;

public class DepositCommand implements CommandExecutor {
    private Main plugin;

    public DepositCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("deposit").setExecutor(this);
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

        try {
            URL url = new URL("http://" + user + ":" + password + "@" + host + ":" + port + "/");
            BitcoinJSONRPCClient client = new BitcoinJSONRPCClient(url);
            String address = client.getAccountAddress(functions.getUserId(player.getDisplayName()));
            TextComponent tc = new TextComponent();
            tc.setText(ChatColor.GREEN + "Your deposit address: " + ChatColor.LIGHT_PURPLE +  address + ". " + ChatColor.UNDERLINE + "Click to copy");
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(address)));

            player.spigot().sendMessage(tc);

        } catch (IOException | JSONException | BitcoinRPCException e) {
            player.sendMessage(ChatColor.RED + "There was an error connecting to the " + constants.coinName + " daemon. Please notify the admins");
        }

        return false;
    }



}
