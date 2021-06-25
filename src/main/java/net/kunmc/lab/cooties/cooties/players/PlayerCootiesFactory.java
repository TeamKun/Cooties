package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesConst;
import net.kunmc.lab.cooties.cooties.CootiesContext;

import java.util.HashMap;
import java.util.Map;

public class PlayerCootiesFactory {
    public static Map<String, CootiesContext> createCooties(String name) {
        Map<String, CootiesContext> cooties = new HashMap<>();
        if (name.equals(Config.bangCootiesPlayerName)) {
            cooties.put(CootiesConst.BANGCOOTIES, new CootiesContext(new BangCooties(CootiesConst.BANGCOOTIES, 0, Config.bangCootiesPlayerName)));
        }
        if (name.equals(Config.barrierCootiesPlayerName)) {
            cooties.put(CootiesConst.BARRIERCOOTIES, new CootiesContext(new BarrierCooties(CootiesConst.BARRIERCOOTIES, 0, Config.barrierCootiesPlayerName)));
        }
        if (name.equals(Config.buriCootiesPlayerName)) {
            cooties.put(CootiesConst.BURICOOTIES, new CootiesContext(new BuriCooties(CootiesConst.BURICOOTIES, 0, Config.buriCootiesPlayerName)));
        }
        if (name.equals(Config.confusionCootiesPlayerName)) {
            cooties.put(CootiesConst.CONFUSIONCOOTIES, new CootiesContext(new ConfusionCooties(CootiesConst.CONFUSIONCOOTIES, 0, Config.confusionCootiesPlayerName)));
        }
        if (name.equals(Config.gazeCootiesPlayerName)) {
            cooties.put(CootiesConst.GAZECOOTIES, new CootiesContext(new GazeCooties(CootiesConst.GAZECOOTIES, 0, Config.gazeCootiesPlayerName)));
        }
        if (name.equals(Config.kickCootiesPlayerName)) {
            cooties.put(CootiesConst.KICKCOOTIES, new CootiesContext(new KickCooties(CootiesConst.KICKCOOTIES, 0, Config.kickCootiesPlayerName)));
        }
        if (name.equals(Config.nyaCootiesPlayerName)) {
            cooties.put(CootiesConst.NYACOOTIES, new CootiesContext(new NyaCooties(CootiesConst.NYACOOTIES, 0, Config.nyaCootiesPlayerName)));
        }
        return cooties;
    }
}
