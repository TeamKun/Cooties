package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesContext;

import java.util.ArrayList;
import java.util.List;

public class PlayerCootiesFactory {
    public static List<CootiesContext> createCooties(String name) {
        List<CootiesContext> cooties = new ArrayList<>();
        if (name.equals(Config.bangCootiesPlayerName)){
            //cooties.add(new CootiesContext(new BangCooties(name, 0)));
        }
        if (name.equals(Config.confusionCootiesPlayerName)){
            cooties.add(new CootiesContext(new ConfusionCooties("confusionCooties", 0)));
        }
        return cooties;
    }
}
