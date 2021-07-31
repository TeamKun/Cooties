package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.util.DecolationConst;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerState implements Cloneable {
    Player player;
    // 保持している菌を管理
    Map<String, CootiesContext> cooties;
    // 保持している菌の名前表示を管理
    Map<String, ArmorStand> cootiesViews = new HashMap<String, ArmorStand>();

    public PlayerState(Player player, Map<String, CootiesContext> cooties) {
        this.player = player;
        this.cooties = cooties;
    }

    public Map<String, CootiesContext> getCooties() {
        return cooties;
    }

    public void clearCootiesWhenTouch(Player touchedPlayer, List originCootiesPlayer) {
        /**
         * 相手を触った時に自身の菌を綺麗にする。
         *   - 触った相手が常時菌を持つPlayerの場合は対応する菌を削除しない(菌をもっている時に菌保持者から菌をうつされることはない)
         *   - 常時菌を持つPlayerは削除後に追加される
         */
        List<CootiesContext> willRemove = new ArrayList<>();
        for (CootiesContext cc : cooties.values()) {
            if (!originCootiesPlayer.contains(touchedPlayer.getName())) {
                cc.stopCootiesProcess(player);
                willRemove.add(cc);
            }
        }
        for (CootiesContext cc : willRemove) {
            removeCooties(cc.getType());
        }

        // 殴った側が菌持ちだった場合は追加
        if (originCootiesPlayer.contains(player.getName())) {
            Map<String, CootiesContext> appendCooties = PlayerCootiesFactory.createCooties(player.getName());
            for (CootiesContext cc : appendCooties.values()) {
                addCooties(cc);
            }
        }
    }

    public void removeCooties(String cootiesType) {
        /**
         * 菌の削除
         */
        if (!cooties.containsKey(cootiesType))
            return;

        cooties.remove(cootiesType);
    }

    /**
     * 追加した場合はtrueを返す
     *
     * @param cooties
     * @return
     */
    public boolean addCooties(CootiesContext cooties) {
        /**
         * 菌の追加、別Playerからの受け渡し時に使用されることを想定
         *   受け渡しがされる場合は初期に実行される処理を行う
         */
        if (this.cooties.containsKey(cooties.getType()))
            return false;
        this.cooties.put(cooties.getType(), cooties);
        cooties.setIsInit(true);
        return true;
    }


    public Player getPlayer() {
        return player;
    }

    public Map getCootiesViews() {
        return cootiesViews;
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

    public void removeAllCootiesViews() {
        for (String cootiesType : cootiesViews.keySet()) {
            cootiesViews.get(cootiesType).remove();
        }
        cootiesViews.clear();
    }

    public void renderCootiesSentence() {
        Location location = player.getLocation();
        location.setY(location.getY() + 1.5);
        for (String cootiesType : cooties.keySet()) {
            if (cootiesViews.containsKey(cootiesType)) {
                // 菌の名前を既に表示に持っている場合

                // 位置を更新
                cootiesViews.get(cootiesType).teleport(location);
            } else {
                // 菌の名前を表示に持っていない場合
                ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
                    ArmorStand armorStand = ((ArmorStand) entity);
                    armorStand.setMarker(false);
                    armorStand.setSmall(true);
                    armorStand.setGlowing(false);
                    armorStand.setVisible(false);
                    armorStand.setSilent(true);
                    armorStand.setGravity(false);
                    armorStand.setCustomName(DecolationConst.GREEN + cooties.get(cootiesType).getPlayerName() + "菌");
                    armorStand.setCustomNameVisible(true);
                });
                cootiesViews.put(cootiesType, as);
            }
            // 表示位置を順にずらす
            location.setY(location.getY() + 0.5);
        }

        // 表示にいるが、菌を持っていない場合は削除
        ArrayList<String> removes = new ArrayList<>();
        for (String cootiesType : cootiesViews.keySet()) {
            if (!cooties.containsKey(cootiesType)) {
                removes.add(cootiesType);
            }
        }
        for (String cootiesType : removes) {
            cootiesViews.get(cootiesType).remove();
            cootiesViews.remove(cootiesType);
        }
    }
}