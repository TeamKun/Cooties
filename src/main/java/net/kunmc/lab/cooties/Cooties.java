package net.kunmc.lab.cooties;

import net.kunmc.lab.cooties.command.CommandConst;
import net.kunmc.lab.cooties.command.CommandController;
import net.kunmc.lab.cooties.event.PlayerEventHandler;
import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.player.PlayerState;
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
        plugin = this;
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), plugin);
        task = new Task(plugin).runTaskTimer(this, 0, 1);
        Config.loadConfig(false);

        getCommand(CommandConst.MAIN_COMMAND).setExecutor(new CommandController());
        getLogger().info("Cooties Plugin is enabled");
    }

    @Override
    public void onDisable() {
        if (GameManager.playerStates != null) {
            for (PlayerState ps : GameManager.playerStates.values()) {
                ps.removeAllPassenger();
            }
        }
        getLogger().info("Cooties Plugin is disabled");
    }
}
