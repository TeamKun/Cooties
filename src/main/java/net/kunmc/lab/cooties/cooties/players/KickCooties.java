package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class KickCooties extends CootiesState implements CootiesInterface {
    Map<UUID, Boolean> vectorFlag;


    KickCooties(String type, int time, String playerName) {
        super(type, time, playerName);
        vectorFlag = new HashMap<UUID, Boolean>();
        setEffectMessage("近づくとkickする");
    }

    @Override
    public void runCootiesProcess(Player p) {
        if (getParticleTime() % 4 == 0)
            p.getLocation().getWorld().spawnParticle(Particle.CRIT, p.getEyeLocation(), 1, 0.8, 0.8, 0.8);
        setParticleTime(getParticleTime() + 1);
        if (p.getName().equals(Config.kickCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }

        List<Player> otherPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(e -> !(e.getName().equals(p.getName()) || e.getName().equals(Config.kickCootiesPlayerName))).collect(Collectors.toList());
        double px = p.getLocation().getX();
        double py = p.getLocation().getY();
        double pz = p.getLocation().getZ();

        for (Player otherPlayer : otherPlayers) {
            double opx = otherPlayer.getLocation().getX();
            double opy = otherPlayer.getLocation().getY();
            double opz = otherPlayer.getLocation().getZ();
            double diffX = px - opx;
            double diffZ = pz - opz;
            double absX = Math.abs(diffX);
            double absZ = Math.abs(diffZ);

            if (Math.abs(py - opy) < 1.0 && absX < 1.0 && absZ < 1.0) {
                String cName = Config.kickCootiesPlayerName;
                String pName = cName.equals("") ? getPlayerName() : cName;
                otherPlayer.kick(Component.text(pName + "菌でkickされた"));
            }
        }
        setTime(getTime() + 1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.kickCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
        String cName = Config.kickCootiesPlayerName;
        String pName = cName.equals("") ? getPlayerName() : cName;
        if (!p.getName().equals(cName)) {
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
        }

    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}


