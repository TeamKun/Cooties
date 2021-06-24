package net.kunmc.lab.cooties.cooties;

import org.bukkit.entity.Player;

public class CootiesContext {
    CootiesInterface ci;
    public CootiesContext(CootiesInterface ci) {
        this.ci = ci;
    }

    public void runCootiesProcess(Player p) {
        ci.runCootiesProcess(p);
    }

    public boolean shouldRemoveCooties(Player p) {
        return ci.shouldRemoveCooties(p);
    }

    public void initTimeProcess(Player p) {
        ci.initTimeProcess(p);
    }

    public void stopCootiesProcess(Player p) {
        ci.stopCootiesProcess(p);
    }

    public String getName() {
        return ci.getName();
    }

    public int getTime() {
        return ci.getTime();
    }

    public boolean getIsInit() {
        return ci.getIsInit();
    }

    public void setIsInit(boolean isInit) {
        ci.setIsInit(isInit);
    }

    public void setShouldRun(boolean flag) {
        ci.setShouldRun(flag);
    }

    public boolean getShouldRun() {
        return ci.getShouldRun();
    }
}