package nugetzrul3.mc.tipbot;

import nugetzrul3.mc.tipbot.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new HelpCommand(this);
        new InfoCommand(this);
        new DepositCommand(this);
        new BalanceCommand(this);
        new TipCommand(this);
        new WithdrawCommand(this);
        Bukkit.getPluginManager().registerEvents(new SignListeners(), this);

    }
}
