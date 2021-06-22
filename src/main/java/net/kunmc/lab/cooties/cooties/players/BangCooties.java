package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BangCooties extends CootiesState implements CootiesInterface {

    BangCooties(String name, int time){
        super(name, time);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.CRIT, p.getEyeLocation(), 1);
        if (p.getName().equals(Config.bangCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
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