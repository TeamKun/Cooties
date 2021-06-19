package net.kunmc.lab.cooties;

import net.kunmc.lab.cooties.command.CommandConst;
import net.kunmc.lab.cooties.command.CommandController;
import net.kunmc.lab.cooties.event.PlayerEventHandler;
import net.kunmc.lab.cooties.task.Task;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Cooties extends JavaPlugin {
    private BukkitTask task;
    private static Cooties plugin;

    public static Cooties getPlugin() {
        return plugin;
    }


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), plugin);
        task = new Task(plugin).runTaskTimer(this, 0, 1);
        Config.loadConfig(false);

        getCommand(CommandConst.MAIN_COMMAND).setExecutor(new CommandController());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
