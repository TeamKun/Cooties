package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesContext;

import java.util.ArrayList;
import java.util.List;

public class PlayerCootiesFactory {
    public static List<CootiesContext> createCooties(String name) {
        List<CootiesContext> cooties = new ArrayList<>();
        //if (name.equals(Config.bangCootiesPlayerName)){
        //    cooties.add(new CootiesContext(new BangCooties("bangCooties", 0)));
        //}
        if (name.equals(Config.barrierCootiesPlayerName)){
            cooties.add(new CootiesContext(new BarrierCooties("barrierCooties", 0)));
        }
        //if (name.equals(Config.buriCootiesPlayerName)){
        //    cooties.add(new CootiesContext(new BuriCooties("buriCooties", 0)));
        //}
        //if (name.equals(Config.gazeCootiesPlayerName)){
        //    cooties.add(new CootiesContext(new GazeCooties("gazeCooties", 0)));
        //}
        //if (name.equals(Config.confusionCootiesPlayerName)){
        //    cooties.add(new CootiesContext(new ConfusionCooties("confusionCooties", 0)));
        //}
        //if (name.equals(Config.nyaCootiesPlayerName)){
        //    cooties.add(new CootiesContext(new NyaCooties("nyaCooties", 0)));
        //}
        return cooties;
    }
}
