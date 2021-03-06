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

        e.setMessage(String.format("ãã%sã ã", e.getMessage()));
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

        e.setMessage(String.format("%sã«ã", e.getMessage()));
    }

    @EventHandler
    public void onMovedBang(PlayerMoveEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        double distanceMoved = e.getFrom().distance(e.getTo());
        // ä»æ§ãç¥ããªãããç§»åç´å¾ã®ã¿ã«ãããã«åããã¨ãããã®ã§å¯¾å¿
        // åèï¼ ã¹ãã¼ã¯æã® distanceMoved ã0.06ç¨åº¦
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
        // ä»æ§ãç¥ããªãããç§»åç´å¾ã®ã¿ã«ãããã«åããã¨ãããã®ã§å¯¾å¿
        // åèï¼ ã¹ãã¼ã¯æã® distanceMoved ã0.06ç¨åº¦
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

        // ã­ã°ã¤ã³æã«ãã§ã«ç»é²ããã¦ãããªãã²ã¼ã å¾©å¸°ããããã«ãã
        UUID targetId = e.getPlayer().getUniqueId();
        if (GameManager.playerStates.containsKey(targetId)) {
            for (UUID id : GameManager.playerStates.keySet()) {
                // åä¸IDãªãä¸åº¦æå ±ãæ¶ãã¦åå¥å
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
         * Respawnæã®èª¿æ´å¦ç
         *  ConfusionCooties ã®ããã«PotionEffectãªã©ãªã¹ãã¼ã³æã«æ¶ããå¦çã«ã¤ãã¦å¯¾å¿
         *  ä¿æãã¦ããèã®ååãkillæã«åé¤ãã¦ããã®ã§ããã§åæå¥ãã
         */

        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL) {
            return;
        }

        Player p = e.getPlayer();
        UUID targetId = p.getUniqueId();
        if (GameManager.playerStates.containsKey(targetId)) {
            for (CootiesContext cc : GameManager.playerStates.get(targetId).getCooties().values()) {
                if (cc.getType().equals(CootiesConst.CONFUSIONCOOTIES)) {
                    // åã«addPotionEffectããã ãã§ã¯å¯¾å¿ã§ããªãã®ã§ãrunTaskLaterãã
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
         * Logoutæã®æå
         *   èåã®è¡¨ç¤ºãæ®ãã®ã§åé¤ãã¦ãã
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
         * Logoutæã®æå
         *   èåã®è¡¨ç¤ºãæ®ãã®ã§åé¤ãã¦ãã
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
         * Deathæã®æå
         *   èåã®è¡¨ç¤ºãæ®ãã®ã§åé¤ãã¦ãã
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