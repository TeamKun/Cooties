package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class NyaCooties extends CootiesState implements CootiesInterface {
    NyaCooties(String name, int time) {
        super(name, time);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.HEART, p.getEyeLocation(), 1);
        if (p.getName().equals(Config.nyaCootiesPlayerName))
            return;

        setTime(getTime()+1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.nyaCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}
