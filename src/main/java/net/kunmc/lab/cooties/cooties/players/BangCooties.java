package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class BangCooties extends CootiesState implements CootiesInterface {
    int bangTime = 1;
    BangCooties(String name, int time){
        super(name, time);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.CRIT, p.getEyeLocation(), 1);
        if (p.getName().equals(Config.bangCootiesPlayerName))
            return;
        if (!getShouldRun() && bangTime % 22 == 0){
            getLogger().info(Integer.toString(bangTime));
            setShouldRun(true);
            bangTime = 1;
        }
        if (!getShouldRun())
            bangTime += 1;
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