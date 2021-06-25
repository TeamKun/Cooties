package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class BangCooties extends CootiesState implements CootiesInterface {
    int bangTime = 1;

    BangCooties(String type, int time, String name) {
        super(type, time, name);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.COMPOSTER, p.getEyeLocation(), 1, 1.0, 1.0, 1.0);
        if (p.getName().equals(Config.bangCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }

        if (!getShouldRun() && bangTime % 22 == 0) {
            setShouldRun(true);
            bangTime = 1;
        }

        if (!getShouldRun())
            bangTime += 1;
        setTime(getTime() + 1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.bangCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
        String cName = Config.bangCootiesPlayerName;
        String pName = cName.equals("") ? getPlayerName() : cName;
        if (!p.getName().equals(cName))
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}