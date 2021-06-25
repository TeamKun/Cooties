package net.kunmc.lab.cooties.game;

import net.kunmc.lab.cooties.player.PlayerProcess;
import net.kunmc.lab.cooties.player.PlayerState;

import java.util.Map;
import java.util.UUID;

public class GameManager {
    // 動作中のモード保持
    public static GameMode runningMode = GameMode.MODE_NEUTRAL;
    public static Map<UUID, PlayerState> playerStates;

    public static void controller(GameMode runningMode) {
        // モードを設定
        GameManager.runningMode = runningMode;

        switch (runningMode) {
            case MODE_START:
                playerStates = PlayerProcess.initPlayerState();
                break;
            case MODE_NEUTRAL:
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
