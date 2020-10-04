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
                float actual_withdraw = Float.parseFloat(args[0]) - constants.withdraw_fee.floatValue();
                BigDecimal withdraw_amount = new BigDecimal(String.valueOf(actual_withdraw));;

                if (!(client.validateAddress(args[1]).isValid())) {
                    sender.sendMessage(ChatColor.RED + "The address you specified is not valid");
                }
                else {
                    if (player_balance.floatValue() >= withdraw_amount.floatValue()) {
                        String withdraw_sender = functions.getUserId(player.getDisplayName());
                        String txid = client.sendFrom(withdraw_sender, args[1], withdraw_amount);
                        float txfeeget = -(client.getTransaction(txid).fee().floatValue());
                        BigDecimal txfee = new BigDecimal(String.valueOf(txfeeget));
                        client.move(withdraw_sender, "mcwallet", constants.withdraw_fee);
                        client.move("mcwallet", withdraw_sender, txfee);
                        TextComponent tc = new TextComponent();
                        tc.setText(ChatColor.GREEN + "Withdraw Successful!\n" +
                                ChatColor.GREEN + "Withdrawn amount: " + " " + ChatColor.WHITE + withdraw_amount + " " + constants.ticker + "\n" +
                                ChatColor.GREEN + "Withdrawal Fee: " + " " + ChatColor.WHITE + constants.withdraw_fee + " " + constants.ticker + "\n" +
                                ChatColor.UNDERLINE + "Click here to view transaction");
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
