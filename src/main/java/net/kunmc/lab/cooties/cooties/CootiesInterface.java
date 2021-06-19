package net.kunmc.lab.cooties.cooties;

import org.bukkit.entity.Player;

public interface CootiesInterface {
    // 菌作成直後や菌を別Playerに移した直後に実行する処理
    void initTimeProcess(Player p);
    // メインで実行する処理
    void runCootiesProcess(Player p);
    // 期限がすぎた菌の削除判定処理
    boolean shouldRemoveCooties(Player p);
    // 菌の処理を停止させる処理
    void stopCootiesProcess(Player p);


    String getName();
    int getTime();
    boolean getIsInit();
    void setIsInit(boolean isInit);
}
