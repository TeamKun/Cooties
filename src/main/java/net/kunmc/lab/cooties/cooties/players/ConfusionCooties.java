package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.Bukkit.getLogger;

public class ConfusionCooties extends CootiesState implements CootiesInterface {

    ConfusionCooties(String name, int time){
        super(name, time);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation(), 1);
        if (p.getName().equals(Config.confusionCootiesPlayerName))
            return;

        int time = getTime();
        if (getIsInit()){
            getLogger().info("AAA");
            //initTimeProcess(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 20, 0, false, false));
            setIsInit(false);
        }
        setTime(time+1);
    }

    @Override
    public boolean shouldRemoveCooties (Player p) {
        return getTime() > 20 * 20 && !p.getName().equals(Config.confusionCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess (Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 20, 0, false, false));
    }

    @Override
    public void stopCootiesProcess(Player p) {
        p.removePotionEffect(PotionEffectType.CONFUSION);
    }
}