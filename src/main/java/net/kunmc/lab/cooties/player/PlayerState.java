package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerState {
    static Map<String, List<CootiesInterface>> cooties;

    public static void initPlayerState(){
        cooties = Bukkit.getOnlinePlayers().stream().collect(
                Collectors.toMap(e -> e.getName(),
                                 e -> PlayerCootiesFactory.createCooties(e.getName())));
    }

    public static void swapPlayerCooties(Player touchedPlayer, Player touchPlayer){
        /* - 菌交換の流れ
         *   - 触れ合ったPlayer同士の菌を交換
         *     - 交換したのが菌持ちの特定プレイヤーであれば、自身の菌を追加しておく
         */
        String touchedPlayerName = touchedPlayer.getName();
        String touchPlayerName = touchedPlayer.getName();

        List tmpTouchedPlayerCooties = cooties.get(touchedPlayerName);
        List tmpTouchPlayerCooties = cooties.get(touchPlayerName);

        cooties.get(touchedPlayerName).clear();
        cooties.get(touchedPlayerName).addAll(tmpTouchPlayerCooties);
        removeCooties(touchedPlayer);

        cooties.get(touchPlayer.getName()).clear();
        cooties.get(touchPlayer.getName()).addAll(tmpTouchedPlayerCooties);
        removeCooties(touchPlayer);

    }

    private static void removeCooties(Player p){
        //if (cooties.get(p.getName()).contains(hogehoge){
        //    cooties.get(p.getName()).remove(hogehoge);
        //}
    }
}