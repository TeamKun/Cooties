package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class BangCooties extends CootiesState implements CootiesInterface {
    private int bangTime = 1;

    BangCooties(String type, int time, String name) {
        super(type, time, name);
        setEffectMessage("移動で台パン音が鳴る");
    }

    @Override
    public void runCootiesProcess(Player p) {
        if (getParticleTime() % 4 == 0)
            p.getLocation().getWorld().spawnParticle(Particle.COMPOSTER, p.getEyeLocation(), 1, 0.8, 0.8, 0.8);
        setParticleTime(getParticleTime()+1);
        if (p.getName().equals(Config.bangCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }

        if (!getShouldRun() && bangTime % 10 == 0) {
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
        if (!p.getName().equals(cName)) {
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
        }
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}