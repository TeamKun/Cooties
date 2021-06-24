package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class KickCooties extends CootiesState implements CootiesInterface {
    Map<UUID, Boolean> vectorFlag;
    KickCooties(String name, int time){
        super(name, time);
        vectorFlag = new HashMap<UUID, Boolean>();
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.DRIPPING_HONEY, p.getEyeLocation(), 1);
        if (p.getName().equals(Config.kickCootiesPlayerName))
            return;

        List<Player> otherPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(e -> !(e.getName().equals(p.getName()) || e.getName().equals(Config.kickCootiesPlayerName))).collect(Collectors.toList());
        double px = p.getLocation().getX();
        double py = p.getLocation().getY();
        double pz = p.getLocation().getZ();

        for (Player otherPlayer: otherPlayers) {
            double opx = otherPlayer.getLocation().getX();
            double opy = otherPlayer.getLocation().getY();
            double opz = otherPlayer.getLocation().getZ();
            // 高さが上下5マスいないであれば判定
            double diffX = px - opx;
            double diffZ = pz - opz;
            double absX = Math.abs(diffX);
            double absZ = Math.abs(diffZ);

            if (Math.abs(py - opy) < 1.0 && absX < 1.0 && absZ < 1.0) {
                otherPlayer.kick(Component.text("kicked"));
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


