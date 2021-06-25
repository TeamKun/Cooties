package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ConfusionCooties extends CootiesState implements CootiesInterface {

    ConfusionCooties(String type, int time, String playerName) {
        super(type, time, playerName);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.TOTEM, p.getEyeLocation(), 1, 0.5, 1.0, 0.5);
        if (p.getName().equals(Config.confusionCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }
        setTime(getTime() + 1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.confusionCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
        String cName = Config.confusionCootiesPlayerName;
        String pName = cName.equals("") ? getPlayerName() : cName;
        if (!p.getName().equals(cName))
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Config.cootiesTick, 0, false, false));
    }

    @Override
    public void stopCootiesProcess(Player p) {
        p.removePotionEffect(PotionEffectType.CONFUSION);
    }

}