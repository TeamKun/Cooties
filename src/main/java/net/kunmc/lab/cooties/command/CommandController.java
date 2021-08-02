package net.kunmc.lab.cooties.command;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesConst;
import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.player.PlayerProcess;
import net.kunmc.lab.cooties.player.PlayerState;
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
    private String[] cootiesPlayerConfig = {
            CommandConst.COMMAND_CONFIG_BANG_COOTIES_PLAYER,
            CommandConst.COMMAND_CONFIG_BURI_COOTIES_PLAYER,
            CommandConst.COMMAND_CONFIG_BARRIER_COOTIES_PLAYER,
            CommandConst.COMMAND_CONFIG_CONFUSION_COOTIES_PLAYER,
            CommandConst.COMMAND_CONFIG_GAZE_COOTIES_PLAYER,
            CommandConst.COMMAND_CONFIG_GAZE_TARGET_PLAYER,
            CommandConst.COMMAND_CONFIG_KICK_COOTIES_PLAYER,
            CommandConst.COMMAND_CONFIG_NYA_COOTIES_PLAYER};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
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
        } else if (args.length == 4 && args[2].equals(CommandConst.COMMAND_CONFIG_COOTIES_TICK)){
            completions.add("<数字>");
        } else if (args.length == 4 && cootiesPlayerConfigList.contains(args[2])) {
            List<String> name = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            name.add(CommandConst.COMMAND_CONFIG_OFF);
            completions.addAll(name.stream().filter(e -> e.startsWith(args[3])).collect(Collectors.toList()));
        }
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(DecolationConst.RED + "引数がありません");
            sendConfigUsage(sender);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはゲーム内からのみ実行できます");
            return true;
        }

        String commandName = args[0];
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (commandName) {
            case CommandConst.COMMAND_START:
                if (GameManager.runningMode == GameManager.GameMode.MODE_START){
                    sender.sendMessage(DecolationConst.RED + "すでに開始しています");
                    return true;
                }
                GameManager.controller(GameManager.GameMode.MODE_START);
                sender.sendMessage(DecolationConst.GREEN + "〇〇菌を開始します");
                break;
            case CommandConst.COMMAND_STOP:
                if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL){
                    sender.sendMessage(DecolationConst.RED + "開始されていません");
                    return true;
                }
                GameManager.controller(GameManager.GameMode.MODE_NEUTRAL);
                sender.sendMessage(DecolationConst.GREEN + "〇〇菌を終了します");
                break;
            case CommandConst.COMMAND_CONFIG:
                runConfig(sender, commandArgs);
                break;
            default:
                sender.sendMessage(DecolationConst.RED + "存在しないコマンドです");
                sendConfigUsage(sender);
                return true;
        }
        return true;
    }

    private void runConfig(CommandSender sender, String[] args) {
        // 設定値一覧
        if (args.length == 0) {
            sender.sendMessage(DecolationConst.GREEN + "設定値一覧:");
            sender.sendMessage(String.format("  %s: %d", CommandConst.COMMAND_CONFIG_COOTIES_TICK, Config.cootiesTick));
            sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_BANG_COOTIES_PLAYER, Config.bangCootiesPlayerName));
            sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_BARRIER_COOTIES_PLAYER, Config.barrierCootiesPlayerName));
            sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_BURI_COOTIES_PLAYER, Config.buriCootiesPlayerName));
            sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_CONFUSION_COOTIES_PLAYER, Config.confusionCootiesPlayerName));
            sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_GAZE_COOTIES_PLAYER, Config.gazeCootiesPlayerName));
            sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_GAZE_TARGET_PLAYER, Config.gazeTargetPlayerName));
            sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_KICK_COOTIES_PLAYER, Config.kickCootiesPlayerName));
            sender.sendMessage(String.format("  %s: %s", CommandConst.COMMAND_CONFIG_NYA_COOTIES_PLAYER, Config.nyaCootiesPlayerName));
        } else if (args.length == 1 && args[0].equals(CommandConst.COMMAND_CONFIG_RELOAD)) {
            PlayerProcess.removeCootiesProcess(Config.bangCootiesPlayerName, CootiesConst.BANGCOOTIES);
            PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BARRIERCOOTIES);
            PlayerProcess.removeCootiesProcess(Config.buriCootiesPlayerName, CootiesConst.BURICOOTIES);
            PlayerProcess.removeCootiesProcess(Config.confusionCootiesPlayerName, CootiesConst.CONFUSIONCOOTIES);
            PlayerProcess.removeCootiesProcess(Config.gazeCootiesPlayerName, CootiesConst.GAZECOOTIES);
            PlayerProcess.removeCootiesProcess(Config.kickCootiesPlayerName, CootiesConst.KICKCOOTIES);
            PlayerProcess.removeCootiesProcess(Config.nyaCootiesPlayerName, CootiesConst.NYACOOTIES);
            Config.loadConfig(true);
            PlayerProcess.appendCootiesProcess(Config.bangCootiesPlayerName);
            PlayerProcess.appendCootiesProcess(Config.barrierCootiesPlayerName);
            PlayerProcess.appendCootiesProcess(Config.buriCootiesPlayerName);
            PlayerProcess.appendCootiesProcess(Config.confusionCootiesPlayerName);
            PlayerProcess.appendCootiesProcess(Config.gazeCootiesPlayerName);
            PlayerProcess.appendCootiesProcess(Config.kickCootiesPlayerName);
            PlayerProcess.appendCootiesProcess(Config.nyaCootiesPlayerName);
            sender.sendMessage(DecolationConst.GREEN + "コンフィグファイルをリロードしました");
        } else if (args.length == 3 && args[0].equals(CommandConst.COMMAND_CONFIG_SET)) {
            List<String> cootiesPlayerConfigList = new ArrayList<>(Arrays.asList(cootiesPlayerConfig));
            if (cootiesPlayerConfigList.contains(args[1]) && !args[2].equals(CommandConst.COMMAND_CONFIG_OFF) && (Bukkit.selectEntities(sender, args[2]).isEmpty())) {
                sender.sendMessage(DecolationConst.RED + "指定されたプレイヤーが見つかりません");
                return;
            }
            switch (args[1]) {
                case CommandConst.COMMAND_CONFIG_COOTIES_TICK:
                    int num;
                    try{
                        num = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e){
                        sender.sendMessage(DecolationConst.RED + "整数以外が入力されています");
                        return;
                    }
                    if (num < 1) {
                        sender.sendMessage(DecolationConst.RED + "0より大きい整数を入力してください");
                        return;
                    }

                    Config.cootiesTick = num;
                    break;
                case CommandConst.COMMAND_CONFIG_BANG_COOTIES_PLAYER:
                    if (Config.bangCootiesPlayerName.equals(args[2])){
                        sender.sendMessage(DecolationConst.AQUA + "同名プレイヤーが既に設定されています");
                        return;
                    }
                    PlayerProcess.removeCootiesProcess(Config.bangCootiesPlayerName, CootiesConst.BANGCOOTIES);
                    Config.bangCootiesPlayerName = args[2].equals(CommandConst.COMMAND_CONFIG_OFF) ? "" : args[2];
                    if (! args[2].equals(CommandConst.COMMAND_CONFIG_OFF)) {
                        PlayerProcess.appendCootiesProcess(args[2]);
                        PlayerProcess.updatePlayerCootiesName(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_BURI_COOTIES_PLAYER:
                    if (Config.buriCootiesPlayerName.equals(args[2])){
                        sender.sendMessage(DecolationConst.AQUA + "同名プレイヤーが既に設定されています");
                        return;
                    }
                    PlayerProcess.removeCootiesProcess(Config.buriCootiesPlayerName, CootiesConst.BURICOOTIES);
                    Config.buriCootiesPlayerName = args[2].equals(CommandConst.COMMAND_CONFIG_OFF) ? "" : args[2];
                    if (! args[2].equals(CommandConst.COMMAND_CONFIG_OFF)) {
                        PlayerProcess.appendCootiesProcess(args[2]);
                        PlayerProcess.updatePlayerCootiesName(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_BARRIER_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BURICOOTIES);
                    Config.barrierCootiesPlayerName = args[2].equals(CommandConst.COMMAND_CONFIG_OFF) ? "" : args[2];
                    if (! args[2].equals(CommandConst.COMMAND_CONFIG_OFF)) {
                        PlayerProcess.appendCootiesProcess(args[2]);
                        PlayerProcess.updatePlayerCootiesName(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_CONFUSION_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BURICOOTIES);
                    Config.confusionCootiesPlayerName = args[2].equals(CommandConst.COMMAND_CONFIG_OFF) ? "" : args[2];
                    if (! args[2].equals(CommandConst.COMMAND_CONFIG_OFF)) {
                        PlayerProcess.appendCootiesProcess(args[2]);
                        PlayerProcess.updatePlayerCootiesName(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_GAZE_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BURICOOTIES);
                    Config.gazeCootiesPlayerName = args[2].equals(CommandConst.COMMAND_CONFIG_OFF) ? "" : args[2];
                    if (! args[2].equals(CommandConst.COMMAND_CONFIG_OFF)) {
                        PlayerProcess.appendCootiesProcess(args[2]);
                        PlayerProcess.updatePlayerCootiesName(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_KICK_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BURICOOTIES);
                    Config.kickCootiesPlayerName = args[2].equals(CommandConst.COMMAND_CONFIG_OFF) ? "" : args[2];
                    if (! args[2].equals(CommandConst.COMMAND_CONFIG_OFF)) {
                        PlayerProcess.appendCootiesProcess(args[2]);
                        PlayerProcess.updatePlayerCootiesName(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_NYA_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BURICOOTIES);
                    Config.nyaCootiesPlayerName = args[2].equals(CommandConst.COMMAND_CONFIG_OFF) ? "" : args[2];
                    if (! args[2].equals(CommandConst.COMMAND_CONFIG_OFF)) {
                        PlayerProcess.appendCootiesProcess(args[2]);
                        PlayerProcess.updatePlayerCootiesName(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_GAZE_TARGET_PLAYER:
                    Config.gazeTargetPlayerName = args[2].equals(CommandConst.COMMAND_CONFIG_OFF) ? "" : args[2];
                    break;
                default:
                    sender.sendMessage(DecolationConst.RED + "存在しないパラメータです");
                    return;
            }
            sender.sendMessage(String.format("%s%sの値を%sに更新しました", DecolationConst.GREEN, args[1], args[2]));
        } else {
            sender.sendMessage(DecolationConst.RED + "存在しないコマンド・引数、または引数が不足しています");
            sendConfigUsage(sender);
            return;
        }
    }

    private void sendConfigUsage(CommandSender sender) {
        String usagePrefix = String.format("  /%s ", CommandConst.MAIN_COMMAND);
        String descPrefix = "    ";
        sender.sendMessage(DecolationConst.GREEN + "Usage:");
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, CommandConst.COMMAND_START));
        sender.sendMessage(String.format("%sゲームを開始する", descPrefix));
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, CommandConst.COMMAND_STOP));
        sender.sendMessage(String.format("%sゲームを終了する", descPrefix));
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, CommandConst.COMMAND_CONFIG));
        sender.sendMessage(String.format("%sコンフィグ一覧を表示", descPrefix));
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, CommandConst.COMMAND_CONFIG_RELOAD));
        sender.sendMessage(String.format("%sデフォルトのコンフィグをリロード", descPrefix));
        sender.sendMessage(String.format("%s%s %s <コンフィグ名> <値>"
                , usagePrefix, CommandConst.COMMAND_CONFIG, CommandConst.COMMAND_CONFIG_SET));
        sender.sendMessage(String.format("%sコンフィグを設定(設定項目は /coo config で確認)", descPrefix));
    }
}