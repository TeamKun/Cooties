package net.kunmc.lab.cooties.cooties;

public class CootiesState {
    // 菌種別
    String type;
    // 菌の保持時間
    int time;
    // particleの管理時間
    int particleTime;
    // 菌元プレイヤー（特定条件下で使用）
    String playerName;
    // 菌の初期処理をするかどうかのフラグ
    boolean isInit;
    // 特定の処理を実行するかのフラグ
    // 継承するクラスに入れるのは微妙だけどコード管理が楽になるので入れる
    boolean shouldRun;
    // アクションバー用のメッセージ
    String effectMessage;

    public CootiesState(String type, int time, String playerName) {
        this.type = type;
        this.time = time;
        this.playerName = playerName;
        isInit = true;
        shouldRun = true;
        particleTime = 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setIsInit(boolean isInit) {
        this.isInit = isInit;
    }

    public boolean getIsInit() {
        return isInit;
    }

    public void setShouldRun(boolean flag) {
        this.shouldRun = flag;
    }

    public boolean getShouldRun() {
        return shouldRun;
    }

    public void setEffectMessage(String message) {
        effectMessage = message;
    }

    public String getEffectMessage() {
        return effectMessage;
    }

    public void setParticleTime(int time) {
        particleTime = time;
    }

    public int getParticleTime() {
        return particleTime;
    }
}
