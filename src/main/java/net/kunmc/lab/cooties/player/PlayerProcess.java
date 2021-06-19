package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.Cooties;
import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getLogger;

public class PlayerProcess {
    /**
     * PlayerStateの初期化
     */
    public static Map<UUID, PlayerState> initPlayerState(){
        Map<UUID, PlayerState> playerStateList = new HashMap<>();
        for (Player p: Bukkit.getOnlinePlayers().stream().collect(Collectors.toList())){
            playerStateList.put(p.getUniqueId(), new PlayerState(p, PlayerCootiesFactory.createCooties(p.getName())));
        }
        return playerStateList;
    }

    public static void transmitCooties(Player touchedPlayer, Player touchPlayer) {
        /**
         * ■ 菌の受け渡し時の仕様
         * - パターン1: 殴られた側が初期菌持ち
         *   - 殴られた側は菌を渡す
         *   - 殴った側は(持っていれば)菌を渡す
         * - パターン2: 殴った側が初期菌持ち
         *   - 殴った側は菌を渡す
         * - パターン3: 初菌持ちなし
         *   - 殴った側は(持っていれば)菌を渡す
         */

        UUID touchedPlayerId = touchedPlayer.getUniqueId();
        UUID touchPlayerId = touchPlayer.getUniqueId();
        List<String> originCootiesPlayer = new ArrayList<>(Arrays.asList(
                Config.bangCootiesPlayerName,
                Config.buriCootiesPlayerName,
                Config.confusionCootiesPlayerName,
                Config.gazeCootiesPlayerName,
                Config.kickCootiesPlayerName,
                Config.nyaCootiesPlayerName));

        //　渡す菌を保持する変数
        //// 殴られた側が渡す菌
        List<CootiesContext> willTransmitTouchedPlayerCooties = new ArrayList<>();
        //// 殴った側が渡す菌
        List<CootiesContext> willTransmitTouchPlayerCooties = new ArrayList<>();

        // パターン1: 殴られた側が初期菌持ちなら渡す
        if (originCootiesPlayer.contains(touchedPlayer.getName())){
            willTransmitTouchedPlayerCooties.addAll(PlayerCootiesFactory.createCooties(touchedPlayer.getName()));
        }

        // パターン1, 2, 3: 殴った側は菌を渡す
        PlayerState ps =  GameManager.playerStates.get(touchPlayerId).clone();
        willTransmitTouchPlayerCooties.addAll(ps.getCooties());

        // 菌を渡す側は綺麗にする
        GameManager.playerStates.get(touchPlayerId).clearCooties();

        // 菌渡し
        for (CootiesContext cc: willTransmitTouchedPlayerCooties){
            GameManager.playerStates.get(touchPlayerId).addCooties(cc);
        }
        for (CootiesContext cc: willTransmitTouchPlayerCooties){
            GameManager.playerStates.get(touchedPlayerId).addCooties(cc);
        }
    }


    ///**
    // * 菌を双方で交換するパータン、一旦使用しない
    // * @param touchedPlayer
    // * @param touchPlayer
    // */
    //public static void swapPlayerCooties(Player touchedPlayer, Player touchPlayer){
    //    getLogger().info("AAAA");
    //    for (UUID id: GameManager.playerStates.keySet()){
    //        for (CootiesContext cc: GameManager.playerStates.get(id).cooties){
    //            getLogger().info( GameManager.playerStates.get(id).getPlayer().getName() + ": " + cc.getName());
    //        }
    //    }
    //    UUID touchedPlayerId = touchedPlayer.getUniqueId();
    //    UUID touchPlayerId = touchPlayer.getUniqueId();

    //    // 入れ替え先のPlayerState取得
    //    PlayerState newTouchedPlayerState = GameManager.playerStates.get(touchPlayerId).clone();
    //    PlayerState newTouchPlayerState = GameManager.playerStates.get(touchedPlayerId).clone();

    //    // 入れ替え対象のPlayerが菌保持者なら、元々保持している菌を付与する
    //    List<CootiesContext> newTouchedPlayerCooties = PlayerCootiesFactory.createCooties(
    //            newTouchedPlayerState.getPlayer().getName());
    //    List<CootiesContext> newTouchPlayerCooties = PlayerCootiesFactory.createCooties(
    //            newTouchPlayerState.getPlayer().getName());

    //    for (CootiesContext cc: newTouchedPlayerCooties){
    //        newTouchedPlayerState.addCooties(cc);
    //    }
    //    for (CootiesContext cc: newTouchPlayerCooties){
    //        newTouchedPlayerState.addCooties(cc);
    //    }

    //    // 入れ替え
    //    GameManager.playerStates.remove(touchedPlayerId);
    //    GameManager.playerStates.put(touchedPlayerId, newTouchedPlayerState);

    //    GameManager.playerStates.remove(touchPlayerId);
    //    GameManager.playerStates.put(touchPlayerId, newTouchPlayerState);

    //    for (UUID id: GameManager.playerStates.keySet()){
    //        for (CootiesContext cc: GameManager.playerStates.get(id).cooties){
    //            getLogger().info( GameManager.playerStates.get(id).getPlayer().getName() + ": " + cc.getName());
    //        }
    //    }
    //}
}
