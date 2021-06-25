package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuriCooties extends CootiesState implements CootiesInterface {
    BuriCooties(String type, int time, String playerName) {
        super(type, time, playerName);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.FALLING_WATER, p.getEyeLocation(),1, 1.0, 1.0, 1.0);
        if (p.getName().equals(Config.buriCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }

        setTime(getTime()+1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.buriCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
        String cName = Config.buriCootiesPlayerName;
        String pName = cName.equals("") ? getPlayerName() : cName;
        if (!p.getName().equals(cName))
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}
