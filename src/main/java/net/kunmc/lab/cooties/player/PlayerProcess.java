package net.kunmc.lab.cooties.player;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerProcess {
    /**
     * PlayerStateの初期化
     */
    public static Map<UUID, PlayerState> initPlayerState() {
        Map<UUID, PlayerState> playerStateList = new HashMap<>();
        for (Player p : Bukkit.getOnlinePlayers().stream().collect(Collectors.toList())) {
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
                Config.barrierCootiesPlayerName,
                Config.buriCootiesPlayerName,
                Config.confusionCootiesPlayerName,
                Config.gazeCootiesPlayerName,
                Config.kickCootiesPlayerName,
                Config.nyaCootiesPlayerName));

        //　渡す菌を保持する変数
        //// 殴られた側が渡す菌
        Map<String, CootiesContext> willTransmitTouchedPlayerCooties = new HashMap<>();
        //// 殴った側が渡す菌
        Map<String, CootiesContext> willTransmitTouchPlayerCooties = new HashMap<>();

        // パターン1: 殴られた側が初期菌持ちなら渡す
        if (originCootiesPlayer.contains(touchedPlayer.getName())) {
            willTransmitTouchedPlayerCooties.putAll(PlayerCootiesFactory.createCooties(touchedPlayer.getName()));
        }

        // パターン1, 2, 3: 殴った側は菌を渡す
        PlayerState ps = GameManager.playerStates.get(touchPlayerId).clone();
        willTransmitTouchPlayerCooties.putAll(ps.getCooties());

        // 菌を渡す側は綺麗にする
        GameManager.playerStates.get(touchPlayerId).clearCooties();

        // 菌渡し
        for (CootiesContext cc : willTransmitTouchedPlayerCooties.values()) {
            GameManager.playerStates.get(touchPlayerId).addCooties(cc);
        }
        for (CootiesContext cc : willTransmitTouchPlayerCooties.values()) {
            GameManager.playerStates.get(touchedPlayerId).addCooties(cc);
        }
    }

    public static void appendCootiesProcess(String playerName) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;
        Player p = Bukkit.getPlayer(playerName);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(playerName))
                for (CootiesContext cc : PlayerCootiesFactory.createCooties(playerName).values())
                    GameManager.playerStates.get(player.getUniqueId()).addCooties(cc);
        }
    }

    public static void removeCootiesProcess(String playerName, String cootiesType) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(playerName)) {
                GameManager.playerStates.get(Bukkit.getPlayer(playerName).getUniqueId())
                        .removeCooties(cootiesType);
            }
        }
    }
}
