package me.msicraft.ctspecialability.Command;

import me.msicraft.ctspecialability.CTSpecialAbility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SubCommand implements CommandExecutor {

    private final CTSpecialAbility plugin;

    public SubCommand(CTSpecialAbility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equals("ctspecialability")) {
            if (sender.isOp()) {
                String var = args[0];
                switch (var) {
                    case "reload" -> {
                        plugin.reloadVariables();
                        sender.sendMessage(CTSpecialAbility.PREFIX + " 구성 리로드 됨");
                    }
                }
                return true;
            }
        }
        return false;
    }

}
