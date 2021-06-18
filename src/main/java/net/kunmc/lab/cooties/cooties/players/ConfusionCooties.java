package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        if (time == 0) {
            // TODO: 10はConfigで設定するか検討
            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 0, false, false));
        }
        setTime(time++);
    }
}