package net.kunmc.lab.cooties.command;

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
        switch (commandName) {
            case CommandConst.COMMAND_START:
                GameManager.controller(GameManager.GameMode.MODE_START);
                break;
            case CommandConst.COMMAND_STOP:
                GameManager.controller(GameManager.GameMode.MODE_NEUTRAL);
                break;
            default:
                sender.sendMessage(DecolationConst.RED + "存在しないコマンドです");
                return true;
        }
        return false;
    }
}