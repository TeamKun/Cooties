package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getLogger;

public class BarrierCooties extends CootiesState implements CootiesInterface {
    Map<UUID, Boolean> vectorFlag;
    BarrierCooties(String name, int time){
        super(name, time);
        vectorFlag = new HashMap<UUID, Boolean>();
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.DRIP_LAVA, p.getEyeLocation(), 1);
        if (p.getName().equals(Config.barrierCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }

        List<Player> otherPlayers = Bukkit.getOnlinePlayers().stream().filter(e -> !e.getName().equals(p.getName())).collect(Collectors.toList());
        double px = p.getLocation().getX();
        double py = p.getLocation().getY();
        double pz = p.getLocation().getZ();

        for (Player otherPlayer: otherPlayers) {
            getLogger().info(otherPlayer.getName());
            double opx = otherPlayer.getLocation().getX();
            double opy = otherPlayer.getLocation().getY();
            double opz = otherPlayer.getLocation().getZ();
            // 高さが上下5マスいないであれば判定
            double diffX = px - opx;
            double diffZ = pz - opz;
            getLogger().info(Double.toString(px));
            getLogger().info(Double.toString(opx));

            if (Math.abs(py - opy) < 5.0 && (Math.abs(diffX) < 5.0 || Math.abs(diffZ) < 5.0)) {
                vectorFlag.put(otherPlayer.getUniqueId(), true);
                otherPlayer.setVelocity(new Vector(1, 0.5, 1));
            } else if (vectorFlag.get(otherPlayer.getUniqueId())) {
                otherPlayer.setVelocity(new Vector(0, 0, 0));
                vectorFlag.put(otherPlayer.getUniqueId(), false);
            }
        }
        setTime(getTime()+1);
    }

    @Override
    public boolean shouldRemoveCooties (Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.bangCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess (Player p) {
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}
