package net.kunmc.lab.cooties.command;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.util.DecolationConst;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandController implements CommandExecutor, TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        String[] cootiesPlayerConfig = {
                CommandConst.COMMAND_CONFIG_BANG_COOTIES_PLAYER,
                CommandConst.COMMAND_CONFIG_BURI_COOTIES_PLAYER,
                CommandConst.COMMAND_CONFIG_BARRIER_COOTIES_PLAYER,
                CommandConst.COMMAND_CONFIG_CONFUSION_COOTIES_PLAYER,
                CommandConst.COMMAND_CONFIG_GAZE_COOTIES_PLAYER,
                CommandConst.COMMAND_CONFIG_KICK_COOTIES_PLAYER,
                CommandConst.COMMAND_CONFIG_NYA_COOTIES_PLAYER};

        List<String> cootiesPlayerConfigList = new ArrayList<>(Arrays.asList(cootiesPlayerConfig));

        if (args.length == 1) {
            completions.addAll(Stream.of(
                    CommandConst.COMMAND_START,
                    CommandConst.COMMAND_STOP,
                    CommandConst.COMMAND_CONFIG)
                    .filter(e -> e.startsWith(args[0])).collect(Collectors.toList()));
        } else if (args.length == 2 && args[0].equals(CommandConst.COMMAND_CONFIG)) {
            completions.addAll(Stream.of(
                    CommandConst.COMMAND_CONFIG_SET,
                    CommandConst.COMMAND_CONFIG_RELOAD)
                    .filter(e -> e.startsWith(args[1])).collect(Collectors.toList()));
        } else if (args.length == 3 && args[1].equals(CommandConst.COMMAND_CONFIG_SET)) {
            cootiesPlayerConfigList.add(CommandConst.COMMAND_CONFIG_COOTIES_TICK);
            completions.addAll(cootiesPlayerConfigList.stream()
                    .filter(e -> e.startsWith(args[2])).collect(Collectors.toList()));
        } else if (args.length == 4 && cootiesPlayerConfigList.contains(args[2])) {
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(e -> e.startsWith(args[3])).collect(Collectors.toList()));
        }
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(DecolationConst.RED + "引数がありません");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはゲーム内からのみ実行できます");
            return false;
        }

        String commandName = args[0];
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (commandName) {
            case CommandConst.COMMAND_START:
                GameManager.controller(GameManager.GameMode.MODE_START);
                break;
            case CommandConst.COMMAND_STOP:
                GameManager.controller(GameManager.GameMode.MODE_NEUTRAL);
                break;
            case CommandConst.COMMAND_CONFIG:
                runConfig(sender, commandArgs);
                break;
            default:
                sender.sendMessage(DecolationConst.RED + "存在しないコマンドです");
                return true;
        }
        return false;
    }

    private static void runConfig(CommandSender sender, String[] args) {
        // 設定値一覧
        if (args.length == 0){
            sender.sendMessage(DecolationConst.GREEN + "設定値一覧:");
            //sender.sendMessage(String.format("  %s: %d", CommandConst.COMMAND_CONFIG_KILL_TICK_INTERVAL, Config.killProcessTickInterval));
            //sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_F0V, Config.fov));
            //sender.sendMessage(String.format("  %s: %.1f", CommandConst.COMMAND_CONFIG_ASPECT_RATIO_WIDE, Config.aspectRatioWide));
            //sender.sendMessage(String.format("  %s: %.1f", CommandConst.COMMAND_CONFIG_ASPECT_RATIO_HEIGHT, Config.aspectRatioHeight));
            //sender.sendMessage(String.format("  %s: %.1f", CommandConst.COMMAND_CONFIG_FAR_CLIP_DISTANCE, Config.farClipDistance));
            //sender.sendMessage(String.format("  %s: %d", CommandConst.COMMAND_CONFIG_GRIM_REAPER_NUM, Config.grimReaperNum));
            //sender.sendMessage(String.format("  %s: %d", CommandConst.COMMAND_CONFIG_GRIM_REAPER_RANDOM_UODATE_TICK_INTERVAL, Config.grimReaperUpdateTickInterval));
        } else if (args.length == 1 && args[0].equals(CommandConst.COMMAND_CONFIG_RELOAD)) {
            Config.loadConfig(true);
            sender.sendMessage(DecolationConst.GREEN + "コンフィグファイルをリロードしました");
        } else if (args.length == 3 && args[0].equals(CommandConst.COMMAND_CONFIG_SET)) {
            switch (args[1]){
                case CommandConst.COMMAND_CONFIG_COOTIES_TICK:
                    Config.cootiesTick = Integer.parseInt(args[2]);
                    break;
                case CommandConst.COMMAND_CONFIG_BANG_COOTIES_PLAYER:
                    Config.bangCootiesPlayerName = args[2];
                    break;
                case CommandConst.COMMAND_CONFIG_BURI_COOTIES_PLAYER:
                    Config.buriCootiesPlayerName = args[2];
                    break;
                case CommandConst.COMMAND_CONFIG_BARRIER_COOTIES_PLAYER:
                    Config.barrierCootiesPlayerName = args[2];
                    break;
                case CommandConst.COMMAND_CONFIG_CONFUSION_COOTIES_PLAYER:
                    Config.confusionCootiesPlayerName = args[2];
                    break;
                case CommandConst.COMMAND_CONFIG_GAZE_COOTIES_PLAYER:
                    Config.gazeCootiesPlayerName = args[2];
                    break;
                case CommandConst.COMMAND_CONFIG_KICK_COOTIES_PLAYER:
                    Config.kickCootiesPlayerName = args[2];
                    break;
                case CommandConst.COMMAND_CONFIG_NYA_COOTIES_PLAYER:
                    Config.nyaCootiesPlayerName = args[2];
                    break;
                default:
                    sender.sendMessage(DecolationConst.RED + "存在しないパラメータです");
                    return;
            }
            sender.sendMessage(String.format("%s%sの値を%sに更新しました", DecolationConst.GREEN, args[1], args[2]));
        }
    }
}