package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesConst;
import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.util.DecolationConst;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;

public class PlayerState implements Cloneable {
    Player player;
    // 保持している菌を管理
    Map<String, CootiesContext> cooties;
    // プレイヤーログアウト時にplayer.getPassengerが効かなくなるので、別途持っておく
    AreaEffectCloud firstAec;
    // passengerから特定の菌を削除する際にCustomNameベースでの削除しかできないので、削除を管理するリストを設ける
    Map<String, Entity> passengersList;

    public PlayerState(Player player, Map<String, CootiesContext> cooties) {
        this.player = player;
        this.cooties = cooties;
        // AreaEffectCloudを二つ挟まないとArmorStandに攻撃判定が吸われるので対応
        if (this.player.getPassengers().isEmpty()) {
            firstAec = createAec();
            getTopPassenger(this.player).addPassenger(firstAec);
            getTopPassenger(this.player).addPassenger(createAec());
        }
        passengersList = new HashMap<>();
    }

    public AreaEffectCloud getFirstAec(){
        return firstAec;
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

        //loggingPassenger(player);
        //getLogger().info(passengersList.toString());
        cooties.remove(cootiesType);
        //removeAllPassenger();
        //addAllPassenger();
        //getLogger().info("Remove");
        removePassenger(player, null, cootiesType);
        //loggingPassenger(player);
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
        Entity topEntity = getTopPassenger(player);
        ArmorStand as = createCootiesNameEntity(topEntity.getLocation(), cooties.getType());
        topEntity.addPassenger(as);
        passengersList.put(cooties.getType(), as);

        cooties.setIsInit(true);
        return true;
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

    private AreaEffectCloud createAec(){
        AreaEffectCloud aec = player.getLocation().getWorld().spawn(player.getLocation(), AreaEffectCloud.class);
        aec.setDuration(999999999);
        aec.setRadius(0);
        return aec;
    }

    private ArmorStand createCootiesNameEntity(Location loc, String cootiesType){
        Map<String, String> typePlayerList = new HashMap<>();
        typePlayerList.put(CootiesConst.BANGCOOTIES, Config.bangCootiesPlayerName);
        typePlayerList.put(CootiesConst.BARRIERCOOTIES, Config.barrierCootiesPlayerName);
        typePlayerList.put(CootiesConst.BURICOOTIES, Config.buriCootiesPlayerName);
        typePlayerList.put(CootiesConst.CONFUSIONCOOTIES, Config.confusionCootiesPlayerName);
        typePlayerList.put(CootiesConst.GAZECOOTIES, Config.gazeCootiesPlayerName);
        typePlayerList.put(CootiesConst.KICKCOOTIES, Config.kickCootiesPlayerName);
        typePlayerList.put(CootiesConst.NYACOOTIES, Config.nyaCootiesPlayerName);

        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
            ArmorStand armorStand = ((ArmorStand) entity);
            armorStand.setMarker(false);
            armorStand.setSmall(true);
            armorStand.setGlowing(false);
            armorStand.setVisible(false);
            armorStand.setSilent(true);
            armorStand.setGravity(false);
            armorStand.setCustomName(DecolationConst.GREEN + typePlayerList.get(cootiesType) + "菌");
            armorStand.setCustomNameVisible(true);
        });
        return as;
    }

    private Entity getTopPassenger(Entity entity) {
        if (entity.getPassengers().isEmpty()) {
            return entity;
        }
        return getTopPassenger(entity.getPassengers().get(0));
    }

    private void removePassenger(Entity entity, Entity preEntity, String cooiesType){
        /**
         * 削除処理
         *  - あるEntityを消した時の挙動
         *    - 削除対象Entityを乗せているEntityはpassengerを削除
         *    - 削除対象Entityを乗せているEntityはpassengerを削除対象E
         */
        if (entity.equals(passengersList.get(cooiesType))){
            entity.remove();
            preEntity.removePassenger(entity);

            // 削除したEntityがpassengerを持っている場合は一個前に付け替え
            if (!entity.getPassengers().isEmpty()){
                preEntity.addPassenger(entity.getPassengers().get(0));
            }
            passengersList.remove(cooiesType);
            return;
        }
        if (entity.getPassengers().isEmpty()) {
            return;
        }
        removePassenger(entity.getPassengers().get(0), entity, cooiesType);
    }
    public void removeAllPassenger(){
        removeAllPassengerRecursive(player);
        passengersList.clear();
    }

    public void removeAllPassengerRecursive(Entity entity){
        if (entity.getPassengers().isEmpty()) {
            passengersList.clear();
            return;
        }
        removeAllPassengerRecursive(entity.getPassengers().get(0));
        if ((entity.getPassengers().get(0) instanceof ArmorStand)) {
            entity.getPassengers().get(0).remove();
            entity.removePassenger(entity.getPassengers().get(0));
        }
    }

    public void addAllPassenger(){
        for(CootiesContext cc: cooties.values()){
            ArmorStand as = createCootiesNameEntity(getTopPassenger(player).getLocation(), cc.getType());
            getTopPassenger(player).addPassenger(as);
            passengersList.put(cc.getType(), as);
        }
    }

    private Entity loggingPassenger(Entity entity) {
        getLogger().info(entity.getName());
        if (entity.getPassengers().isEmpty()) {
            return entity;
        }
        return loggingPassenger(entity.getPassengers().get(0));
    }
}