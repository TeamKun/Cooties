package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerState implements Cloneable {
    Player player;
    Map<String, CootiesContext> cooties;

    public PlayerState(Player player, Map<String, CootiesContext> cooties) {
        this.player = player;
        this.cooties = cooties;
    }

    public Map<String, CootiesContext> getCooties() {
        return cooties;
    }

    public void clearCooties() {
        /**
         * 菌のリセット、ただし常時菌を持つPlayerは削除後に追加される
         */
        for (CootiesContext cc : cooties.values()) {
            cc.stopCootiesProcess(player);
        }
        cooties.clear();
        cooties = PlayerCootiesFactory.createCooties(player.getName());
    }

    public void removeCooties(String cootiesType) {
        /**
         * 菌の削除
         */
        if (!cooties.containsKey(cootiesType))
            return;

        cooties.remove(cootiesType);
    }

    public void addCooties(CootiesContext cooties) {
        /**
         * 菌の追加、別Playerからの受け渡し時に使用されることを想定
         *   受け渡しがされる場合は初期に実行される処理を行う
         */
        if (this.cooties.containsKey(cooties.getType()))
            return;
        this.cooties.put(cooties.getType(), cooties);
        cooties.setIsInit(true);
    }


    public Player getPlayer() {
        return player;
    }

    @Override
    public PlayerState clone() {
        PlayerState ps = null;
        try {
            ps = (PlayerState) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ps;
    }
}