package net.kunmc.lab.cooties.event;

import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.player.PlayerProcess;
import net.kunmc.lab.cooties.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerEventHandler implements Listener {
    @EventHandler
    public void onTouch(EntityDamageByEntityEvent e){
        if (GameManager.runningMode == GameManager.GameMode.MODE_START)
            return;
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player touchedPlayer = (Player) e.getEntity();
            Player touchPlayer = (Player) e.getDamager();
            PlayerProcess.swapPlayerCooties(touchedPlayer, touchPlayer);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (GameManager.runningMode == GameManager.GameMode.MODE_START) {
            return;
        }

        // ログイン時にすでに登録されているならゲーム復帰するようにする
        UUID id = e.getPlayer().getUniqueId();
        for (PlayerState ps: GameManager.playerStateList) {
            // 同一IDなら一度情報を消して再入力
            // TODO: 情報削除の際にエラーが出る可能性がある(psをremoveしているため)ので確認する
            if (ps.getPlayer().getUniqueId().equals(id)){
                PlayerState newPlayerState = new PlayerState(ps.getPlayer(), ps.getCooties());
                GameManager.playerStateList.remove(ps);
                GameManager.playerStateList.add(newPlayerState);
            }
        }
    }
}

