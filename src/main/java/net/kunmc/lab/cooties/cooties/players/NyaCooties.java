package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class NyaCooties extends CootiesState implements CootiesInterface {

    NyaCooties(String type, int time, String playerName) {
        super(type, time, playerName);
        setEffectMessage("〇〇にゃ としか喋られない");
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.SPELL_INSTANT, p.getEyeLocation(), 1, 1.0, 1.0, 1.0);
        if (p.getName().equals(Config.nyaCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }

        setTime(getTime() + 1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.nyaCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
        String cName = Config.nyaCootiesPlayerName;
        String pName = cName.equals("") ? getPlayerName() : cName;
        if (!p.getName().equals(cName)) {
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
        }
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }
}
