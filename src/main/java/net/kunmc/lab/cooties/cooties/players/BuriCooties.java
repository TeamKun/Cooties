package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class BuriCooties extends CootiesState implements CootiesInterface {
    BuriCooties(String type, int time, String playerName) {
        super(type, time, playerName);
        setEffectMessage("チャットが「いや、〇〇だが」になる");
    }

    @Override
    public void runCootiesProcess(Player p) {
        if (getParticleTime() % 4 == 0)
            p.getLocation().getWorld().spawnParticle(Particle.FALLING_WATER, p.getEyeLocation(), 1, 0.8, 0.8, 0.8);
        setParticleTime(getParticleTime() + 1);
        if (p.getName().equals(Config.buriCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }

        setTime(getTime() + 1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.buriCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
        String cName = Config.buriCootiesPlayerName;
        String pName = cName.equals("") ? getPlayerName() : cName;
        if (!p.getName().equals(cName)) {
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
        }
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}
