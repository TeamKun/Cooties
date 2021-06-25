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

public class BarrierCooties extends CootiesState implements CootiesInterface {
    Map<UUID, Boolean> vectorFlag;

    BarrierCooties(String type, int time, String playerName) {
        super(type, time, playerName);
        vectorFlag = new HashMap<UUID, Boolean>();
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.NOTE, p.getEyeLocation(), 1, 1.0, 1.0, 1.0);
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

        for (Player otherPlayer : otherPlayers) {
            double opx = otherPlayer.getLocation().getX();
            double opy = otherPlayer.getLocation().getY();
            double opz = otherPlayer.getLocation().getZ();
            // 高さが上下5マスいないであれば判定
            double diffX = px - opx;
            double diffZ = pz - opz;
            double absX = Math.abs(diffX);
            double absZ = Math.abs(diffZ);

            if (Math.abs(py - opy) < 5.0 && (absX < 5.0 && absZ < 5.0)) {
                int vectorX = 0;
                int vectorZ = 0;
                if (absX < 5.0 && diffX > 0) {
                    vectorX = -1;
                } else if (absX < 5.0 && diffX <= 0) {
                    vectorX = 1;
                }
                if (absZ < 5.0 && diffZ > 0) {
                    vectorZ = -1;
                } else if (absZ < 5.0 && diffZ <= 0) {
                    vectorZ = 1;
                }

                vectorFlag.put(otherPlayer.getUniqueId(), true);
                otherPlayer.setVelocity(new Vector(vectorX, 0.5, vectorZ));
            } else if (vectorFlag.get(otherPlayer.getUniqueId()) != null && vectorFlag.get(otherPlayer.getUniqueId())) {
                otherPlayer.setVelocity(new Vector(0, 0, 0));
                vectorFlag.put(otherPlayer.getUniqueId(), false);
            }
        }
        setTime(getTime() + 1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.barrierCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
        String cName = Config.barrierCootiesPlayerName;
        String pName = cName.equals("") ? getPlayerName() : cName;
        if (!p.getName().equals(cName))
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}
