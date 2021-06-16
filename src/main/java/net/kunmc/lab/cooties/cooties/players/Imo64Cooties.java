package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Imo64Cooties extends CootiesState implements CootiesInterface {
    Imo64Cooties(String name, int time){
        super(name, time);
    }

    @Override
    public void runCootiesProcess(Player p) {
        int time = getTime();
        if (getTime() == 0) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 0, false, false));
        }
        setTime(time++);
    }
}