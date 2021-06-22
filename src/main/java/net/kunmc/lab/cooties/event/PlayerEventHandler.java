package net.kunmc.lab.cooties.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.command.CommandConst;
import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.player.PlayerProcess;
import net.kunmc.lab.cooties.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class PlayerEventHandler implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChattedAddBuri(AsyncChatEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        Player p = e.getPlayer();
        boolean haveCooties = false;
        for (CootiesContext cc : GameManager.playerStates.get(p.getUniqueId()).getCooties()) {
            if (cc.getName().equals("buriCooties")) {
                haveCooties = true;
                break;
            }
        }
        if (!haveCooties)
            return;
        e.message(Component.text(String.format("いや、%sだが", ((TextComponent)e.message()).content())));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChattedAddNya(AsyncChatEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        Player p = e.getPlayer();
        boolean haveCooties = false;
        for (CootiesContext cc : GameManager.playerStates.get(p.getUniqueId()).getCooties()) {
            if (cc.getName().equals("nyaCooties")) {
                haveCooties = true;
                break;
            }
        }
        if (!haveCooties)
            return;
        e.message(Component.text(String.format("%sにゃ〜", ((TextComponent)e.message()).content())));
    }


    @EventHandler
    public void onMoved(PlayerMoveEvent e) {
       if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

       double distanceMoved = e.getFrom().distance(e.getTo());
       if (distanceMoved == 0.0)
           return;

       boolean haveCooties = false;
       Player p = e.getPlayer();
       CootiesContext pc;
       for (CootiesContext cc : GameManager.playerStates.get(p.getUniqueId()).getCooties()) {
           if (cc.getName().equals("bangCooties")) {
               haveCooties = true;
               pc = cc;
               break;
           }
       }
       if (!haveCooties)
           return;

       // TODO: 常時音が鳴ると訳わからんので適当にクールタイムを設ける
       p.playSound(p.getLocation(), "minecraft:cooties.footstep",1, 1);
    }

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

