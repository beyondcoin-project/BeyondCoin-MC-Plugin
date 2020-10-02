package nugetzrul3.mc.tipbot.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import nugetzrul3.mc.tipbot.Main;
import nugetzrul3.mc.tipbot.config.Constants;
import nugetzrul3.mc.tipbot.config.Functions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONException;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

public class WithdrawCommand implements CommandExecutor {
    private Main plugin;

    public WithdrawCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("withdraw").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You're not a player m8");
        }

        Player player = (Player) sender;

        Constants constants = new Constants();
        Functions functions = new Functions();

        String user = constants.user;
        String password = constants.pass;
        String host = constants.host;
        String port = constants.port;
        String explorer = constants.explorer;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You didn't specify how much you want to withdraw");
        }
        else if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "You didn't specify an address to withdraw to");
        }
        else {
            try {
                URL url = new URL("http://" + user + ":" + password + "@" + host + ":" + port + "/");
                BitcoinJSONRPCClient client = new BitcoinJSONRPCClient(url);
                BigDecimal player_balance = client.getBalance(functions.getUserId(player.getDisplayName()));
                BigDecimal withdraw_amount = BigDecimal.valueOf(Float.parseFloat(args[0]));

                if (!(client.validateAddress(args[1]).isValid())) {
                    sender.sendMessage(ChatColor.RED + "The address you specified is not valid");
                }
                else {
                    if (player_balance.floatValue() >= withdraw_amount.floatValue()) {
                        String txid = client.sendFrom(player.getDisplayName(), args[1], withdraw_amount);
                        TextComponent tc = new TextComponent();
                        tc.setText(ChatColor.GREEN + "Withdraw Successful!" + ChatColor.UNDERLINE + "Click here to view transaction");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, explorer + txid));
                        player.spigot().sendMessage(tc);
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "You have insufficient funds!");
                    }
                }
            } catch (JSONException | IOException | BitcoinRPCException e) {
                e.printStackTrace();
                player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "There was an error connecting to the " + constants.coinName + " daemon. Please notify the admins");
            }
        }
        return false;
    }
}
