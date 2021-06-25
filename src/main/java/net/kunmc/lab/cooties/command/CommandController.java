package net.kunmc.lab.cooties.command;

import net.kunmc.lab.cooties.Config;
import net.kunmc.lab.cooties.cooties.CootiesConst;
import net.kunmc.lab.cooties.cooties.CootiesContext;
import net.kunmc.lab.cooties.cooties.players.PlayerCootiesFactory;
import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.player.PlayerProcess;
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

import static org.bukkit.Bukkit.getLogger;

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
                sendConfigUsage(sender);
                return true;
        }
        return true;
    }

    private void runConfig(CommandSender sender, String[] args) {
        // 設定値一覧
        if (args.length == 0){
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
            if (GameManager.runningMode == GameManager.GameMode.MODE_START){
                sender.sendMessage(DecolationConst.RED + "リロードはゲームを停止(/coo stop)してから実行してください");
                return;
            }
            Config.loadConfig(true);
            sender.sendMessage(DecolationConst.GREEN + "コンフィグファイルをリロードしました");
        } else if (args.length == 3 && args[0].equals(CommandConst.COMMAND_CONFIG_SET)) {
            List<String> cootiesPlayerConfigList = new ArrayList<>(Arrays.asList(cootiesPlayerConfig));
            if (cootiesPlayerConfigList.contains(args[1]) && !args[2].equals(CommandConst.COMMAND_CONFIG_OFF) && (Bukkit.selectEntities(sender, args[2]).isEmpty())){
                sender.sendMessage(DecolationConst.RED + "指定されたプレイヤーが見つかりません");
                return;
            }
            switch (args[1]){
                case CommandConst.COMMAND_CONFIG_COOTIES_TICK:
                    Config.cootiesTick = Integer.parseInt(args[2]);
                    break;
                case CommandConst.COMMAND_CONFIG_BANG_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.bangCootiesPlayerName, CootiesConst.BANGCOOTIES);
                    if (args[2].equals(CommandConst.COMMAND_CONFIG_OFF)){
                        Config.bangCootiesPlayerName = "";
                    } else {
                        PlayerProcess.removeCootiesProcess(Config.bangCootiesPlayerName, CootiesConst.BANGCOOTIES);
                        Config.bangCootiesPlayerName = args[2];
                        PlayerProcess.appendCootiesProcess(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_BURI_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BURICOOTIES);
                    if (args[2].equals(CommandConst.COMMAND_CONFIG_OFF)){
                        Config.buriCootiesPlayerName = "";
                    } else {
                        Config.buriCootiesPlayerName = args[2];
                        PlayerProcess.appendCootiesProcess(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_BARRIER_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BARRIERCOOTIES);
                    if (args[2].equals(CommandConst.COMMAND_CONFIG_OFF)){
                        Config.barrierCootiesPlayerName = "";
                    } else {
                        PlayerProcess.removeCootiesProcess(Config.barrierCootiesPlayerName, CootiesConst.BARRIERCOOTIES);
                        Config.barrierCootiesPlayerName = args[2];
                        PlayerProcess.appendCootiesProcess(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_CONFUSION_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.confusionCootiesPlayerName, CootiesConst.CONFUSIONCOOTIES);
                    if (args[2].equals(CommandConst.COMMAND_CONFIG_OFF)){
                        Config.confusionCootiesPlayerName = "";
                    } else {
                        Config.confusionCootiesPlayerName = args[2];
                        PlayerProcess.appendCootiesProcess(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_GAZE_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.gazeCootiesPlayerName, CootiesConst.GAZECOOTIES);
                    if (args[2].equals(CommandConst.COMMAND_CONFIG_OFF)){
                        Config.gazeCootiesPlayerName = "";
                    } else {
                        Config.gazeCootiesPlayerName = args[2];
                        PlayerProcess.appendCootiesProcess(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_KICK_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.kickCootiesPlayerName, CootiesConst.KICKCOOTIES);
                    if (args[2].equals(CommandConst.COMMAND_CONFIG_OFF)){
                        Config.kickCootiesPlayerName = "";
                    } else {
                        Config.kickCootiesPlayerName = args[2];
                        PlayerProcess.appendCootiesProcess(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_NYA_COOTIES_PLAYER:
                    PlayerProcess.removeCootiesProcess(Config.nyaCootiesPlayerName, CootiesConst.NYACOOTIES);
                    if (args[2].equals(CommandConst.COMMAND_CONFIG_OFF)){
                        Config.nyaCootiesPlayerName = "";
                    } else {
                        Config.nyaCootiesPlayerName = args[2];
                        PlayerProcess.appendCootiesProcess(args[2]);
                    }
                    break;
                case CommandConst.COMMAND_CONFIG_GAZE_TARGET_PLAYER:
                    if (args[2].equals(CommandConst.COMMAND_CONFIG_OFF)){
                        Config.gazeTargetPlayerName = "";
                    } else {
                        Config.gazeTargetPlayerName = args[2];
                    }
                    break;
                default:
                    sender.sendMessage(DecolationConst.RED + "存在しないパラメータです");
                    return;
            }
            sender.sendMessage(String.format("%s%sの値を%sに更新しました", DecolationConst.GREEN, args[1], args[2]));
        } else {
            sender.sendMessage(DecolationConst.RED + "存在しないコマンド、または引数です");
            return;
        }
    }

    private void sendConfigUsage(CommandSender sender){
        String usagePrefix = String.format("  /%s ", CommandConst.MAIN_COMMAND);
        String descPrefix = "    ";
        sender.sendMessage(DecolationConst.GREEN + "Usage:");
        sender.sendMessage(String.format("%s%s"
                ,usagePrefix, CommandConst.COMMAND_START));
        sender.sendMessage(String.format("%sゲームを開始する", descPrefix));
        sender.sendMessage(String.format("%s%s"
                ,usagePrefix, CommandConst.COMMAND_STOP));
        sender.sendMessage(String.format("%sゲームを終了する", descPrefix));
        sender.sendMessage(String.format("%s%s"
                ,usagePrefix, CommandConst.COMMAND_CONFIG));
        sender.sendMessage(String.format("%sコンフィグ一覧を表示", descPrefix));
        sender.sendMessage(String.format("%s%s"
                ,usagePrefix, CommandConst.COMMAND_CONFIG_RELOAD));
        sender.sendMessage(String.format("%sデフォルトのコンフィグをリロード", descPrefix));
        sender.sendMessage(String.format("%s%s <コンフィグ名> <値>"
                ,usagePrefix, CommandConst.COMMAND_CONFIG_SET));
        sender.sendMessage(String.format("%sコンフィグを設定(設定項目は /coo config で確認)", descPrefix));
    }
}