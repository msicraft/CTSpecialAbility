package me.msicraft.ctspecialability.Command;

import me.msicraft.ctspecialability.CTSpecialAbility;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MainCommand implements CommandExecutor {

    private final CTSpecialAbility plugin;

    public MainCommand(CTSpecialAbility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equals("특수능력")) {
            if (sender.isOp()) {
                String var = args[0];
                switch (var) {
                    case "전투" -> {
                        if (sender instanceof Player player) {
                            ItemStack itemStack = new ItemStack(Material.COMPASS, 1);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.displayName(Component.text(ChatColor.AQUA + "전투용 특수능력 부여"));
                            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
                            dataContainer.set(new NamespacedKey(plugin, "CTSpecialAbility_Apply_Item"), PersistentDataType.STRING, "combat");
                            dataContainer.set(new NamespacedKey(plugin, "CTSpecialAbility_Apply_UnStack"), PersistentDataType.STRING, UUID.randomUUID().toString());
                            itemStack.setItemMeta(itemMeta);

                            player.getInventory().addItem(itemStack);
                        }
                    }
                    case "생활" -> {
                        if (sender instanceof Player player) {
                            ItemStack itemStack = new ItemStack(Material.COMPASS, 1);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.displayName(Component.text(ChatColor.AQUA + "생활용 특수능력 부여"));
                            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
                            dataContainer.set(new NamespacedKey(plugin, "CTSpecialAbility_Apply_Item"), PersistentDataType.STRING, "life");
                            dataContainer.set(new NamespacedKey(plugin, "CTSpecialAbility_Apply_UnStack"), PersistentDataType.STRING, UUID.randomUUID().toString());
                            itemStack.setItemMeta(itemMeta);

                            player.getInventory().addItem(itemStack);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

}
