package net.kunmc.lab.cooties.event;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.Cooties;
import net.kunmc.lab.cooties.cooties.CootiesConst;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class PlayerEventHandler implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChattedAddBuri(AsyncPlayerChatEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        Player p = e.getPlayer();
        if (p.getName().equals(Config.buriCootiesPlayerName))
            return;

        if (!GameManager.playerStates.get(p.getUniqueId()).getCooties().containsKey(CootiesConst.BURICOOTIES))
            return;

        e.setMessage(String.format("いや%sだが", e.getMessage()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChattedAddNya(AsyncPlayerChatEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        Player p = e.getPlayer();
        if (p.getName().equals(Config.nyaCootiesPlayerName))
            return;

        if (!GameManager.playerStates.get(p.getUniqueId()).getCooties().containsKey(CootiesConst.NYACOOTIES))
            return;

        e.setMessage(String.format("%sにゃ", e.getMessage()));
    }

    @EventHandler
    public void onMovedBang(PlayerMoveEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        double distanceMoved = e.getFrom().distance(e.getTo());
        // 仕様か知らないが、移動直後のみにわずかに動くことがあるので対応
        // 参考： スニーク時の distanceMoved が0.06程度
        if (distanceMoved < 0.04)
            return;

        Player p = e.getPlayer();

        Map<String, CootiesContext> pc = GameManager.playerStates.get(p.getUniqueId()).getCooties();

        if (p.getName().equals(Config.bangCootiesPlayerName))
            return;

        if (!pc.containsKey(CootiesConst.BANGCOOTIES))
            return;

        if (!pc.get(CootiesConst.BANGCOOTIES).getShouldRun())
            return;

        pc.get(CootiesConst.BANGCOOTIES).setShouldRun(false);

        p.getWorld().playSound(p.getLocation(), "minecraft:cooties.footstep", 1, 1);
    }

    @EventHandler
    public void onMovedConfusion(PlayerMoveEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        double distanceMoved = e.getFrom().distance(e.getTo());
        // 仕様か知らないが、移動直後のみにわずかに動くことがあるので対応
        // 参考： スニーク時の distanceMoved が0.06程度
        if (distanceMoved < 0.04)
            return;

        Player p = e.getPlayer();

        Map<String, CootiesContext> pc = GameManager.playerStates.get(p.getUniqueId()).getCooties();

        if (p.getName().equals(Config.confusionCootiesPlayerName))
            return;

        if (!pc.containsKey(CootiesConst.CONFUSIONCOOTIES))
            return;

        Vector pv = p.getVelocity();
        double x = pv.getX();
        double y = pv.getY();
        double z = pv.getZ();
        Random random = new Random();
        if (random.nextInt(100) > 90 && Math.abs(e.getFrom().getY() - e.getTo().getY()) < 0.01) {
            double min = -1.5;
            double max = 1.5;
            x += min + (max - min) * random.nextDouble();
            z += min + (max - min) * random.nextDouble();
            p.setVelocity(new Vector(x, y, z));
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player touchedPlayer = (Player) e.getEntity();
            Player touchPlayer = (Player) e.getDamager();
            PlayerProcess.transmitCooties(touchedPlayer, touchPlayer);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
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
                    GameManager.playerStates.get(id).removeAllPassengerRecursive(GameManager.playerStates.get(id).getFirstAec());
                    GameManager.playerStates.get(id).addAllPassenger();
                    break;
                }
            }
            GameManager.playerStates.get(targetId).passerngerReset = true;
        } else {
            GameManager.playerStates.put(targetId, new PlayerState(e.getPlayer(), PlayerCootiesFactory.createCooties(e.getPlayer().getName())));
            GameManager.playerStates.get(targetId).passerngerReset = true;
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        /**
         * Respawn時の調整処理
         *  ConfusionCooties のようにPotionEffectなどリスポーン時に消える処理について対応
         *  保持している菌の名前をkill時に削除しているのでここで再投入する
         */

        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL) {
            return;
        }

        Player p = e.getPlayer();
        UUID targetId = p.getUniqueId();
        if (GameManager.playerStates.containsKey(targetId)) {
            for (CootiesContext cc : GameManager.playerStates.get(targetId).getCooties().values()) {
                if (cc.getType().equals(CootiesConst.CONFUSIONCOOTIES)) {
                    // 単にaddPotionEffectするだけでは対応できないので、runTaskLaterする
                    // https://www.spigotmc.org/threads/java-lang-unsupportedoperationexception-use-bukkitrunnable-runtasklater-plugin-long.396457/
                    new ConfusionCootiesLaterTask(p, cc).runTaskLater(Cooties.getPlugin(), 1);
                }
            }
            GameManager.playerStates.get(targetId).passerngerReset = true;
        }
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent e) {
        /**
         * Logout時の挙動
         *   菌名の表示が残るので削除しておく
         */
        Player p = e.getPlayer();
        UUID targetId = p.getUniqueId();
        if (GameManager.playerStates.containsKey(targetId)) {
            GameManager.playerStates.get(targetId).removeAllPassengerRecursive(GameManager.playerStates.get(targetId).getFirstAec());
        }
    }

    @EventHandler
    public void onKickout(PlayerKickEvent e) {
        /**
         * Logout時の挙動
         *   菌名の表示が残るので削除しておく
         */
        Player p = e.getPlayer();
        UUID targetId = p.getUniqueId();
        if (GameManager.playerStates.containsKey(targetId)) {
            GameManager.playerStates.get(targetId).removeAllPassengerRecursive(GameManager.playerStates.get(targetId).getFirstAec());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        /**
         * Death時の挙動
         *   菌名の表示が残るので削除しておく
         */
        Player p = e.getEntity().getPlayer();
        UUID targetId = p.getUniqueId();
        if (GameManager.playerStates.containsKey(targetId)) {
            GameManager.playerStates.get(targetId).removeAllPassenger();
        }
    }
}

class ConfusionCootiesLaterTask extends BukkitRunnable {
    Player p;
    CootiesContext cc;

    ConfusionCootiesLaterTask(Player p, CootiesContext cc) {
        this.p = p;
        this.cc = cc;
    }

    @Override
    public void run() {
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Config.cootiesTick - cc.getTime(), 0, false, false));
    }
}