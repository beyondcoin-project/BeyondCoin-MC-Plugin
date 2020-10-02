package nugetzrul3.mc.tipbot.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nugetzrul3.mc.tipbot.config.Constants;
import nugetzrul3.mc.tipbot.config.Functions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.json.JSONException;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SignListeners implements Listener {

    Constants constants = new Constants();
    Functions functions = new Functions();


    String[] commands_arr = new String[] {
            "/help",
            "/pay",
            "/withdraw",
            "/deposit",
    };

    String user = constants.user;
    String password = constants.pass;
    String host = constants.host;
    String port = constants.port;

    List<String> commands_list = Arrays.asList(commands_arr);

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String command = event.getLine(0);
        assert command != null;
        if (command.contains("/")) {
            if (commands_list.contains(command)) {
                event.setLine(0, "§a" + command);
            }
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block;
        try {
            block = Objects.requireNonNull(event.getClickedBlock()).getState().getBlock();
        } catch (NullPointerException e) {
            return;
        }
        BitcoinJSONRPCClient client = null;
        try {
            URL url = new URL("http://" + user + ":" + password + "@" + host + ":" + port + "/");
            client = new BitcoinJSONRPCClient(url);
        } catch (MalformedURLException | BitcoinRPCException e) {
            e.printStackTrace();
            player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "There was an error connecting to the " + constants.coinName + " daemon. Please notify the admins");
        }
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (Objects.requireNonNull(event.getClickedBlock()).getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();
            if (!(sign.getLine(0).contains("/"))) return;
            String sign_command = sign.getLine(0);
            if (sign_command.equalsIgnoreCase("§a/help")) {
                player.sendMessage(ChatColor.GREEN + "BeyondCoin Sign usage help!!");
            }
            else if (sign_command.equalsIgnoreCase("§a/pay")) {
                String amount = sign.getLine(1);
                String sign_target = sign.getLine(2);
                Player target = Bukkit.getPlayerExact(sign_target);

                if (amount.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "You didn't specify any amount to send");
                    sign.getBlock().breakNaturally();
                }
                else if (!(functions.isFloat(amount))) {
                    player.sendMessage(ChatColor.RED + "The amount you are trying to send is not a valid number");
                    sign.getBlock().breakNaturally();
                }
                else if (Float.parseFloat(amount) < 0.00000001) {
                    player.sendMessage(ChatColor.RED + "Amount must be greater than 0.00000001" + constants.ticker);
                    sign.getBlock().breakNaturally();
                }
                else {
                    if (sign_target.isEmpty()) {
                        player.sendMessage(ChatColor.RED + "You have not specified a player username to send BYND to");
                        sign.getBlock().breakNaturally();
                    }
                    else if (target == null) {
                        player.sendMessage(ChatColor.RED + "The player you are trying to send to doesn't exist in this server");
                        sign.getBlock().breakNaturally();
                    }
                    /*else if (target.getDisplayName() == player.getDisplayName()) {
                        player.sendMessage(ChatColor.RED + "You can't tip yourself m8")
                        sign.getBlock().breakNaturally();
                    }*/
                    else {
                        player.sendMessage(ChatColor.GREEN + "Payment successful. You payed " + amount + " " + constants.ticker + " to " + target.getDisplayName());
                    }
                }


            }
            else if (sign_command.equalsIgnoreCase("§a/withdraw")) {
                player.sendMessage(ChatColor.GREEN + "Withdraw command");
            }
            else if (sign_command.equalsIgnoreCase("§a/deposit")) {
                assert client != null;
                String dep_address;
                try {
                    dep_address = client.getAccountAddress(functions.getUserId(player.getDisplayName()));
                    TextComponent tc = new TextComponent();
                    tc.setText(net.md_5.bungee.api.ChatColor.GREEN + "Your deposit address: " + net.md_5.bungee.api.ChatColor.LIGHT_PURPLE +  dep_address + ". " + net.md_5.bungee.api.ChatColor.UNDERLINE + "Click to copy");
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(dep_address)));

                    player.spigot().sendMessage(tc);
                } catch (JSONException | IOException | BitcoinRPCException e) {
                    player.sendMessage(ChatColor.RED + "There was an error connecting to the " + constants.coinName + " daemon. Please notify the admins");
                }
            }
        }
    }

}
