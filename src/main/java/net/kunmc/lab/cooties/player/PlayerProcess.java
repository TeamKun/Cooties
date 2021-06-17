package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerProcess {
    /**
     * PlayerStateの初期化
     */
    public static List<PlayerState> initPlayerState(){
        List<PlayerState> playerStateList = new ArrayList<>();
        for (Player p: Bukkit.getOnlinePlayers().stream().collect(Collectors.toList())){
            playerStateList.add(new PlayerState(p, PlayerCootiesFactory.createCooties(p.getName())));
        }
        return playerStateList;
    }

    public static void swapPlayerCooties(Player touchedPlayer, Player touchPlayer){
        String touchedPlayerName = touchedPlayer.getName();
        String touchPlayerName = touchPlayer.getName();

        PlayerState newtouchedPlayerState = null;
        PlayerState newtouchPlayerState = null;

        // 入れ替え候補探し
        for (PlayerState ps: GameManager.playerStateList) {
            String checkPlayerName = ps.getPlayer().getName();
            if (checkPlayerName.equals(touchedPlayerName)){
                newtouchPlayerState = ps.clone();
            } else if (checkPlayerName.equals(touchPlayerName)){
                newtouchedPlayerState = ps.clone();
            }
        }

        // 入れ替え
        if (newtouchedPlayerState != null && newtouchPlayerState != null) {
            for (PlayerState ps: GameManager.playerStateList) {
                String checkPlayerName = ps.getPlayer().getName();
                if (checkPlayerName.equals(touchedPlayerName)) {
                    GameManager.playerStateList.remove(ps);
                    // 特定のPlayerの場合、特定の菌を追加する
                    List<CootiesContext> cc = PlayerCootiesFactory.createCooties(touchedPlayerName);
                    if (cc != null){
                        newtouchedPlayerState.getCooties().addAll(cc);
                    }
                    GameManager.playerStateList.add(newtouchedPlayerState);
                } else if (checkPlayerName.equals(touchPlayerName)) {
                    GameManager.playerStateList.remove(ps);
                    // 特定のPlayerの場合、特定の菌を追加する
                    List<CootiesContext> cc = PlayerCootiesFactory.createCooties(touchPlayerName);
                    if (cc != null){
                        newtouchPlayerState.getCooties().addAll(cc);
                    }
                    GameManager.playerStateList.add(newtouchPlayerState);
                }
            }
        }
    }
}
