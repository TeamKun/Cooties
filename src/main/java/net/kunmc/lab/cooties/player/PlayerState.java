package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.cooties.CootiesBase;
import java.util.ArrayList;
import java.util.List;

public class PlayerState {
    List<CootiesBase> cooties = new ArrayList<CootiesBase>();

    public void addCooties(CootiesBase c){
        cooties.add(c);
    }
}