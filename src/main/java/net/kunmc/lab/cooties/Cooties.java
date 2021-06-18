package net.kunmc.lab.cooties;

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
        task = new Task(this).runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
