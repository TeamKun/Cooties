package net.kunmc.lab.cooties.game;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.player.PlayerProcess;
import net.kunmc.lab.cooties.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.UUID;

public class GameManager {
    // 動作中のモード保持
    public static GameMode runningMode = GameMode.MODE_NEUTRAL;

    public static Map<UUID, PlayerState> playerStates;
    public static Team team = null;

    public static void controller(GameMode runningMode) {
        // モードを設定
        GameManager.runningMode = runningMode;

        switch (runningMode) {
            case MODE_START:
                String boardName = "Cooties";
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                Team objective = scoreboard.getTeam(boardName);
                if (objective == null) {
                    team = scoreboard.registerNewTeam(boardName);
                }
                playerStates = PlayerProcess.initPlayerState();
                break;
            case MODE_NEUTRAL:
                for (PlayerState ps : playerStates.values()) {
                    ps.removeAllPassenger();
                    for (CootiesContext cc : ps.getCooties().values()) {
                        cc.stopCootiesProcess(ps.getPlayer());
                    }
                }
                playerStates = null;
                break;
        }
    }

    public enum GameMode {
        // ゲーム外の状態
        MODE_NEUTRAL,
        MODE_START
    }
}
