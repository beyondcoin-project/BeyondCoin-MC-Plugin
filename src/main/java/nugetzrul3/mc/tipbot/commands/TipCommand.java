package nugetzrul3.mc.tipbot.commands;

import nugetzrul3.mc.tipbot.Main;
import nugetzrul3.mc.tipbot.config.Constants;
import nugetzrul3.mc.tipbot.config.Functions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

public class TipCommand implements CommandExecutor {
    private Main plugin;

    public TipCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("tip").setExecutor(this);
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
        int conf = constants.conf;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You did not specify a user to send to! Usage: /tip <user> <amount>");
        }
        else if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "You did not specify an amount to send to the user!: /tip <user> <amount>");
        }
        else {

            if (!args[0].equals(player.getDisplayName())) {

                try {
                    URL url = new URL("http://" + user + ":" + password + "@" + host + ":" + port + "/");
                    BitcoinJSONRPCClient client = new BitcoinJSONRPCClient(url);
                    float sender_balance_get = client.getBalance(functions.getUserId(player.getDisplayName()), conf).floatValue();
                    BigDecimal sender_balance = new BigDecimal(String.valueOf(sender_balance_get));
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target != null) {
                        if (functions.isFloat(args[1])) {
                            float tip_get = Float.parseFloat(args[1]);
                            BigDecimal tip_amount = new BigDecimal(String.valueOf(tip_get));
                            if (sender_balance.floatValue() < tip_get) {
                                player.sendMessage(ChatColor.RED + "You do not have enough BYND to tip that amount");
                            }
                            else if (tip_get < 0.00000001) {
                                player.sendMessage(ChatColor.RED + "Amount must be greater than 0.00000001");
                            }
                            else {
                                client.move(functions.getUserId(player.getDisplayName()), args[0], tip_amount);
                                player.sendMessage(ChatColor.GREEN + "Tip sent successfully!! " + player.getDisplayName() + " sent " + tip_amount + constants.ticker + " to " + args[0]);
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "The amount you sent was not a valid number");
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "That player does not exist!!");
                    }

                } catch (MalformedURLException | BitcoinRPCException e) {
                    player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "There was an error connecting to the " + constants.coinName + " daemon. Please notify the admins");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "You can't tip yourself m8");
            }
        }

        return false;
    }
}
