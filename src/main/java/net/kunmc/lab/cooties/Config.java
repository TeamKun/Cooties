package net.kunmc.lab.cooties;

import net.kunmc.lab.cooties.command.CommandConst;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    // 菌の処理スパン
    public static int cootiesTick;

    // 各種菌を保持するPlayer
    public static String bangCootiesPlayerName;
    public static String buriCootiesPlayerName;
    public static String confusionCootiesPlayerName;
    public static String gazeCootiesPlayerName;
    public static String kickCootiesPlayerName;
    public static String nyaCootiesPlayerName;

    public static void loadConfig(boolean isReload) {

        Cooties plugin = Cooties.getPlugin();

        plugin.saveDefaultConfig();

        if (isReload) {
            plugin.reloadConfig();
        }

        FileConfiguration config = plugin.getConfig();

        cootiesTick = config.getInt(CommandConst.COMMAND_CONFIG_COOTIES_TICK);
        bangCootiesPlayerName = config.getString(CommandConst.COMMAND_CONFIG_BANG_COOTIES_PLAYER);
        buriCootiesPlayerName = config.getString(CommandConst.COMMAND_CONFIG_BURI_COOTIES_PLAYER);
        confusionCootiesPlayerName = config.getString(CommandConst.COMMAND_CONFIG_CONFUSION_COOTIES_PLAYER);
        gazeCootiesPlayerName = config.getString(CommandConst.COMMAND_CONFIG_GAZE_COOTIES_PLAYER);
        kickCootiesPlayerName = config.getString(CommandConst.COMMAND_CONFIG_KICK_COOTIES_PLAYER);
        nyaCootiesPlayerName = config.getString(CommandConst.COMMAND_CONFIG_NYA_COOTIES_PLAYER);
    }
}
