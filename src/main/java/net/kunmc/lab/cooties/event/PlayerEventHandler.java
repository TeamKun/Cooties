package net.kunmc.lab.cooties.event;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.command.CommandConst;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.player.PlayerProcess;
import net.kunmc.lab.cooties.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class PlayerEventHandler implements Listener {
    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent e){
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player touchedPlayer = (Player) e.getEntity();
            Player touchPlayer = (Player) e.getDamager();
            PlayerProcess.transmitCooties(touchedPlayer, touchPlayer);
       }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL) {
            return;
        }

        // ログイン時にすでに登録されているならゲーム復帰するようにする
        UUID targetId = e.getPlayer().getUniqueId();
        if (GameManager.playerStates.containsKey(targetId)) {
            for (UUID id : GameManager.playerStates.keySet()) {
                // 同一IDなら一度情報を消して再入力
                if (id.equals(targetId)) {
                    PlayerState targetPlayerState = GameManager.playerStates.get(id);
                    PlayerState newPlayerState = new PlayerState(e.getPlayer(), targetPlayerState.getCooties());
                    GameManager.playerStates.remove(id);
                    GameManager.playerStates.put(id, newPlayerState);
                    break;
                }
            }
        } else {
            GameManager.playerStates.put(targetId, new PlayerState(e.getPlayer(), PlayerCootiesFactory.createCooties(e.getPlayer().getName())));
        }
    }
}

