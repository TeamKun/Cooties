package net.kunmc.lab.cooties.cooties.players;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesInterface;
import net.kunmc.lab.cooties.cooties.CootiesState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class GazeCooties extends CootiesState implements CootiesInterface {

    GazeCooties(String type, int time, String playerName) {
        super(type, time, playerName);
    }

    @Override
    public void runCootiesProcess(Player p) {
        p.getLocation().getWorld().spawnParticle(Particle.HEART, p.getEyeLocation(), 1, 1.0, 1.0, 1.0);
        String playerName = p.getName();
        if (playerName.equals(Config.gazeCootiesPlayerName))
            return;

        if (getIsInit()) {
            initTimeProcess(p);
            setIsInit(false);
        }

        Player targetPlayer = null;
        List<String> existPlayerName = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());

        // 見つめる対象がいない場合は終了
        if (!existPlayerName.contains(Config.gazeTargetPlayerName))
            return;
        if (playerName.equals(Config.gazeTargetPlayerName)) {
            if (existPlayerName.contains(Config.gazeCootiesPlayerName))
                targetPlayer = Bukkit.getPlayer(Config.gazeCootiesPlayerName);
        } else {
            targetPlayer = Bukkit.getPlayer(Config.gazeTargetPlayerName);
        }

        if (getTime() % 5 == 0 && targetPlayer != null)
            lookAtPlayer(p, targetPlayer);

        setTime(getTime() + 1);
    }

    @Override
    public boolean shouldRemoveCooties(Player p) {
        return getTime() > Config.cootiesTick && !p.getName().equals(Config.gazeCootiesPlayerName) ? true : false;
    }

    @Override
    public void initTimeProcess(Player p) {
        String cName = Config.gazeCootiesPlayerName;
        String pName = cName.equals("") ? getPlayerName() : cName;
        if (!p.getName().equals(cName))
            p.sendMessage(String.format("%sは%s菌を移された", p.getName(), pName));
    }

    @Override
    public void stopCootiesProcess(Player p) {
    }

    private void lookAtPlayer(Player p, Player target) {
        // See: https://stackoverflow.com/questions/21363968/making-minecraft-player-look-at-point
        // 2点間の差分から単位ベクトルを算出
        double directX = p.getEyeLocation().getX() - target.getEyeLocation().getX();
        double directY = p.getEyeLocation().getY() - target.getEyeLocation().getY();
        double directZ = p.getEyeLocation().getZ() - target.getEyeLocation().getZ();
        double len = Math.sqrt(directX * directX + directY * directY + directZ * directZ);
        directX /= len;
        directY /= len;
        directZ /= len;

        /**
         * See: https://seesaawiki.jp/mcpemodders/d/%B6%F5%B4%D6%BA%C2%C9%B8%A4%CB%A4%C4%A4%A4%A4%C6%A1%A7%B4%F0%C1%C3%CA%D4
         * pitch
         *   顎の角度
         *     真上：-90°
         *     正面：0°
         *     真下：90°
         * yaw
         *   エンティティの向いてる方向、0~360の範囲ではなく無限に値が加減されるらしい（初期値は-360 ~ 360のどれか）
         */

        /**
         * asin: sinx = 引数 となるような xを求める
         *   Yの単位ベクトルを与えることで特定地点への上下（顎）の角度が出せる
         */
        double pitch = Math.asin(directY);
        /**
         * See: https://talavax.com/math-atan2.html
         * atan2: 直交座標から極座標へ変換したときの角を求める
         *   Z, Xの単位ベクトルを与えることで、左右の角度が出せる
         */
        double yaw = Math.atan2(directZ, directX);

        /**
         * ラジアンから度数に変換
         *   yawの+90は -90 ~ 90の範囲に対応して調整してるだけ
         */

        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI + 90;

        // See: https://bukkit.org/threads/change-pitch-yaw-of-a-player-without-a-teleport.58582/
        // 向き変更後にパケットをサーバ側に送る必要がある（TPが一番簡単っぽい）
        Location loc = p.getLocation();
        loc.setYaw((float) yaw);
        loc.setPitch((float) pitch);
        p.teleport(loc);
    }
}