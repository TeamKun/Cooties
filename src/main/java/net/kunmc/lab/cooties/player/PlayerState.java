package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerState implements Cloneable{
    Player player;
    List<CootiesContext> cooties;

    public PlayerState(Player player, List<CootiesContext> cooties){
        this.player = player;
        this.cooties = cooties;
    }

    public List<CootiesContext> getCooties(){
        return cooties;
    }

    public Player getPlayer(){
        return player;
    }

    @Override
    public PlayerState clone(){
        PlayerState ps = null;
        try {
            ps = (PlayerState)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ps;
    }
}