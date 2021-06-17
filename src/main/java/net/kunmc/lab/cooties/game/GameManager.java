package net.kunmc.lab.cooties.game;

import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.player.PlayerProcess;
import net.kunmc.lab.cooties.player.PlayerState;

import java.util.List;

public class GameManager {
    // 動作中のモード保持
    public static GameMode runningMode = GameMode.MODE_NEUTRAL;
    public static List<PlayerState> playerStateList;
    public static void controller(GameMode runningMode) {
        // モードを設定
        GameManager.runningMode = runningMode;

        switch (runningMode) {
            case MODE_START:
                playerStateList = PlayerProcess.initPlayerState();
                break;
            case MODE_NEUTRAL:
                break;
        }
    }

    public enum GameMode {
        // ゲーム外の状態
        MODE_NEUTRAL,
        MODE_START
    }
}
