package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.game.GameManager;
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

    public void clearCooties(){
        /**
         * 菌のリセット、ただし常時菌を持つPlayerは削除後に追加される
         */
        for (CootiesContext cc: cooties){
            cc.stopCootiesProcess(player);
        }
        cooties.clear();
        cooties = PlayerCootiesFactory.createCooties(player.getName());
    }

    public void addCooties(CootiesContext cooties){
        /**
         * 菌の追加、別Playerからの受け渡し時に使用されることを想定
         *   受け渡しがされる場合は初期に実行される処理を行う
         */
        for (CootiesContext cc: this.cooties){
            if (cc.getName().equals(cooties.getName()))
                return;
        }
        this.cooties.add(cooties);
        cooties.setIsInit(true);
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