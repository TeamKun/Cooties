package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuriCooties extends CootiesState implements CootiesInterface {
    BuriCooties(String name, int time) {
        super(name, time);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.WATER_DROP, p.getEyeLocation(), 1);
        if (p.getName().equals(Config.buriCootiesPlayerName))
            return;

        setTime(getTime()+1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.buriCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}
