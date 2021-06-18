package net.kunmc.lab.cooties.command;

import net.kunmc.lab.cooties.game.GameManager;
import net.kunmc.lab.cooties.util.DecolationConst;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandController implements CommandExecutor, TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<String>();
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(DecolationConst.RED + "引数がありません");
            return true;
        }
        String commandName = args[0];
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはゲーム内からのみ実行できます");
            return false;
        }

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