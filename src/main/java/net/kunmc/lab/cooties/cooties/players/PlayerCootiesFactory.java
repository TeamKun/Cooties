package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.cooties.CootiesContext;

import java.util.ArrayList;
import java.util.List;

public class PlayerCootiesFactory {
    public static List<CootiesContext> createCooties(String name) {
        List<CootiesContext> cooties = new ArrayList<>();
        switch (name) {
            case "test1" :
                cooties.add(new CootiesContext(new ConfusionCooties(name, 0)));
                break;
            default:
        }
        return cooties;
    }
}
