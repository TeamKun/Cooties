package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.CootiesInterface;

import java.util.ArrayList;
import java.util.List;

public class PlayerCootiesFactory {
    public static List<CootiesInterface> createCooties(String name) {
        List<CootiesInterface> cooties = new ArrayList<>();
        switch (name) {
            case "test1" :
                cooties.add(new Imo64Cooties(name, 0));
                break;
            default:
        }
        return cooties;
    }
}
