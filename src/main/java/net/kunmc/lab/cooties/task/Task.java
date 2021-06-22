package net.kunmc.lab.cooties.task;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getLogger;

public class Task extends BukkitRunnable {
    private JavaPlugin plugin;

    public Task(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        // Playerの更新処理を実行
        for (Player p: Bukkit.getOnlinePlayers().stream().collect(Collectors.toList())){
            PlayerState ps = GameManager.playerStates.get(p.getUniqueId());
            if (ps == null)
                continue;
            List<CootiesContext> shouldRemoveCooties = new ArrayList<>();
            for (CootiesContext cc: ps.getCooties()){
                cc.runCootiesProcess(p);

                if (cc.shouldRemoveCooties(p)){
                    //　CootiesContextのループ中にremoveするとExceptionが発生するため、後で削除
                    shouldRemoveCooties.add(cc);
                }
            }
            for (CootiesContext cc: shouldRemoveCooties){
                ps.getCooties().remove(cc);
            }
        }

        //for (PlayerState ps: GameManager.playerStateList) {
        //    getLogger().info(ps.getPlayer().getName());
        //}
    }
}
