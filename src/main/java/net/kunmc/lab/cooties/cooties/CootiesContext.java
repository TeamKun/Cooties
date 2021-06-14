package net.kunmc.lab.cooties.cooties;

import org.checkerframework.checker.units.qual.C;

public class CootiesContext {
    CootiesInterface ci;
    public CootiesContext(CootiesInterface ci) {
        this.ci = ci;
    }

    public void runCootiesProcess(){
        ci.runCootiesProcess();
    }
}
