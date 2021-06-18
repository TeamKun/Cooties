package net.kunmc.lab.cooties.task;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Task extends BukkitRunnable {
    private JavaPlugin plugin;

    public Task(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        // Update処理を実行
        for (PlayerState ps: GameManager.playerStateList){
            Player p = ps.getPlayer();
            for (CootiesContext cc: ps.getCooties()){
                cc.runCootiesProcess(p);
            }
        }
    }
}
