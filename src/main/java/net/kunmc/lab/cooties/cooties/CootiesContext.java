package net.kunmc.lab.cooties.cooties;

import org.bukkit.entity.Player;

public class CootiesContext {
    CootiesInterface ci;
    public CootiesContext(CootiesInterface ci) {
        this.ci = ci;
    }

    public void runCootiesProcess(Player p){
        ci.runCootiesProcess(p);
    }
}