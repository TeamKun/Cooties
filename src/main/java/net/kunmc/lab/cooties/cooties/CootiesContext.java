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

    public String getType() {
        return ci.getType();
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

    public String getEffectMessage() {
        return ci.getEffectMessage();
    }

    public String getPlayerName() {
        return ci.getPlayerName();
    }
}